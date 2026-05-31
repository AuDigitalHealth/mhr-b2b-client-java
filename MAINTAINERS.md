# Maintainer guide (mhr-b2b-client-java)

Audience: maintainers changing the build, WSDL/bindings layout, dependency coordinates,
generated types, client facades, or tests. Integrators use README.md and published Javadoc.
Contributors start with CONTRIBUTING.md.

This document uses relative paths from the project root (the directory that contains
pom.xml).

---

## 1. What this artifact is

Maven coordinates: au.gov.nehta:mhr-b2b-client

Java library for Australia's My Health Record (PCEHR) B2B SOAP services: record access,
registration, document exchange (including MTOM), views, and templates. It is separate
from the Healthcare Identifiers (HI) client in the sibling hi-b2b-client-java repository.
Applications that need both depend on two JARs.

Published artifact: compiled facades and helpers. Licensed PCEHR WSDL is tracked in Git for
builds but excluded from the published JAR (maven-jar-plugin excludes *.wsdl).

---

## 2. Repository layout

| Path | Role |
| ---- | ---- |
| pom.xml | Build, 12 wsimport executions, profiles, dependency versions |
| src/main/java/au/gov/nehta/vendorlibrary/pcehr/clients/ | Public PCEHR client facades |
| src/main/java/au/gov/nehta/vendorlibrary/ws/ | Shared JAX-WS helpers |
| src/main/java/au/gov/nehta/common/utils/ | Vendored ArgumentUtils (duplicate excluded in fat-jar) |
| src/main/java/META-INF/metro.xml | JAX-WS RI tubeline config (not a separate Metro Maven bundle) |
| src/sample/java/ | Sample programs (sample profile; Jakarta migration may be incomplete) |
| src/test/java/ | Offline unit tests and historical integration tests |
| src/test/resources/TestFiles/ | CDA/XML fixtures for metadata unit tests |
| wsdls/src/main/resources/wsdl/External/ | B2B_*.wsdl (tracked) |
| wsdls/src/main/resources/schema/ | XSD includes |
| wsdls/src/main/resources/binding/ | GlobalBindings.jxb, PCEHR_CommonTypes.xsd.jxb, per-WSDL *.jxb |
| wsdls/src/main/java/ | DateAdapter (hand-written; added to compile sources) |
| scripts/generate-pom.py | Optional helper if it owns the wsimport block in pom.xml |
| target/generated-sources/wsimport/ | Merged wsimport output (build only) |
| local.properties.example | Template reserved for future integration-test configuration |
| build.ps1, build.sh, build.bat | Thin wrappers around mvn clean verify |

Branch model: single main line (master). JDK 11, Jakarta XML Web Services (jaxws-rt 4.x).

---

## 3. Client library design

### 3.1 Layering

```
  Application
       |
  au.gov.nehta.vendorlibrary.pcehr.clients.*Client  (facade per B2B service)
       |
  Client<PortType>  (abstract base)
       |
  Generated JAX-WS Service / Port types  (au.net.electronichealth.*, oasis names, etc.)
       |
  WebServiceClientUtil  (port creation, endpoint, TLS, optional WS-Addressing)
       |
  JAX-WS RI (jaxws-rt) over HTTPS mutual TLS
```

- **Client** constructor validates inputs, builds SecurityHandler (SOAP signing via
  smi-xsp), LoggingHandler, and optional IMTOMHandler for MTOM uploads.
- **WebServiceClientUtil** loads generated Service classes from classpath WSDL locations
  embedded at wsimport time (wsdlLocation in each execution). Unlike HI, there is no
  HiWsdlArtifactRoot; integrators rely on generated metadata plus the endpoint URL they
  pass into the facade constructor.
- **CommonHeaderValidator** validates PCEHRHeader before requests (use requireNonEmpty
  for strings; commons-lang3 3.14+ throws NullPointerException from Validate.notEmpty on
  null CharSequence).
- **MetadataUtils**, **XDSFactory**, **XDSMapper** implement XDS metadata and document
  packaging helpers using generated RIM/XDS list accessors getSlot(), getClassification(),
  getExternalIdentifier() (not plural getSlots() etc.; GlobalBindings.jxb uses xjc:simple).

### 3.2 Facade inventory (15 classes + Client base)

| Package | Class | Primary wsimport / WSDL |
| ------- | ----- | ------------------------ |
| recordaccess/ | DoesPCEHRExistClient, GainPCEHRAccessClient | B2B_PCEHRProfile |
| registration/ | RegisterPCEHRClient | B2B_RegisterPCEHR |
| documentexchange/ | UploadDocumentClient, GetDocumentClient, UploadDocumentMetadataClient, RemoveDocumentClient | B2B_DocumentRepository, B2B_DocumentRegistry, B2B_RemoveDocument |
| view/ | GetDocumentListClient, GetViewClient, GetAuditViewClient, GetChangeHistoryViewClient, GetIndividualDetailsViewClient, GetRepresentativeListClient | B2B_GetView, B2B_GetAuditView, etc.; GetDocumentListClient uses Document Registry types |
| template/ | GetTemplateClient, SearchTemplateClient | B2B_GetTemplate, B2B_SearchTemplate |

GainPCEHRAccess authorisation accessType is a String enum value ("AccessCode",
"EmergencyAccess"), not a nested AccessType enum.

PCEHRHeader may carry an IHI resolved via the HI client in integrator applications; this
repo does not embed HI WSDL.

### 3.3 Runtime requirements (integrator view)

Every call expects:

- HTTPS endpoint URL
- SSLSocketFactory for mutual TLS
- X509 certificate and private key for SOAP message signing
- CertificateValidator (smi-xsp)
- Populated PCEHRHeader (user, product, clientSystemType; optional accessing organisation)

MTOM clients use a separate constructor path on Client with IMTOMHandler.

### 3.4 Compile/runtime dependencies (published JAR)

| Dependency | Purpose |
| ---------- | ------- |
| au.gov.nehta:smi-xsp | XML-DSig, certificate validation |
| com.sun.xml.ws:jaxws-rt | Jakarta XML Web Services RI |
| org.apache.commons:commons-lang3 | Validate and String utilities in facades |
| commons-io | IO helpers in document/metadata utilities |

Test-only: junit, clinical-document-packaging-library, esignature, contiperf, jaxb-api.
Do not add compile dependencies on legacy Metro webservices-rt or javax.xml webservices-api.

Vendored code (no separate Maven deps): au.gov.nehta.common.utils, parts of
au.gov.nehta.vendorlibrary.ws duplicated from HI-style layout.

---

## 4. Build process design

### 4.1 Goals

1. wsimport each primary B2B_*.wsdl with shared and per-WSDL JAXB bindings.
2. Merge outputs into one source tree; compile facades, DateAdapter, and generated types.
3. Exclude *.wsdl from the published JAR.
4. Default test run: offline unit tests; full historical suite via -Pintegration.

### 4.2 Lifecycle (Maven phases)

```
initialize
  gmavenplus: configure-mhr-wsdl-resources-root  -> sets mhr.wsdl.resources.root, mhr.wsdl.dir, mhr.binding.dir
  enforcer: require-pcehr-wsdl-tree              -> B2B_PCEHRProfile.wsdl unless skip

generate-sources
  gmavenplus: clean-wsimport-codegen             -> optional when mhr.wsdl.codegen.clean=true
  jaxws-maven-plugin: 12 x wsimport              -> target/generated-sources/wsimport-parts/<id>/

process-sources
  gmavenplus: merge-and-strip-wsimport           -> target/generated-sources/wsimport/
  build-helper: add-wsimport-source              -> also wsdls/src/main/java

compile / test / package
  compiler, surefire, jar (WSDL excluded), optional gpg/javadoc/source attachments
```

**WSDL directory:** ${mhr.wsdl.dir} defaults to
wsdls/src/main/resources/wsdl/External.

**Bindings on every execution:** GlobalBindings.jxb, PCEHR_CommonTypes.xsd.jxb, plus
B2B_<Service>.wsdl.jxb where present.

**Merge rule:** Same first-wins merge as HI; execution ids listed in pom.xml merge script.

### 4.3 Key Maven properties (pom.xml)

| Property | Default / role |
| -------- | -------------- |
| maven.compiler.release | 11 |
| jaxws.rt.version | jaxws-rt and jaxws-tools (4.0.4) |
| jaxws.maven.plugin.version | jaxws-maven-plugin (4.0.2 on JDK 11) |
| groovy.version | gmavenplus-plugin only |
| mhr.wsdl.codegen.skip | false; true skips wsimport and enforcer |
| mhr.wsdl.codegen.clean | false; true wipes prior wsimport output |
| mhr.wsdl.resources.root | Resolved in initialize; default wsdls/src/main/resources |
| mhr.wsdl.dir | ${mhr.wsdl.resources.root}/wsdl/External |
| mhr.binding.dir | ${mhr.wsdl.resources.root}/binding |
| mhr.wsdl.version | ${project.version}; used by **mhr-wsdl-artifact** profile |
| maven.shade.plugin.version | fat-jar profile |
| skipTests | false; default surefire includes offline unit tests only |

**Build-time WSDL root resolution order:**

1. -Dmhr.wsdl.resources.root=...
2. Environment MHR_WSDL_RESOURCES_ROOT
3. mhr.wsdl.resources.root from active Maven settings profile
4. Default: wsdls/src/main/resources

### 4.4 wsimport configuration

- xadditionalHeaders true (PCEHR SOAP headers).
- Same jaxws-rt stack as HI (not Metro webservices-rt).
- wsdlLocation values like wsdl/B2B_*.wsdl (classpath-relative after generation).

When adding an execution, update scripts/generate-pom.py if that script owns the block,
and extend the merge names list in pom.xml.

### 4.5 Profiles

| Profile | Effect |
| ------- | ------ |
| mhr-wsdl-artifact | Skip wsimport; compile against **`au.gov.nehta:mhr-wsdl`** (install **mhr-wsdl-java** locally first; version **`${mhr.wsdl.version}`** = **`${project.version}`**) |
| integration | Surefire runs **/*Test.java (needs endpoints, certs; see section 5) |
| sample | Adds src/sample/java at generate-sources |
| fat-jar | maven-shade-plugin produces mhr-b2b-client-*-all.jar; excludes duplicate ArgumentUtils from smi-xsp |

Build wrappers: mvn -B -Dgpg.skip=true clean verify; pass shaded for fat-jar.

---

## 5. External inputs and local configuration

### 5.1 WSDL/XSD in Git

Unlike HI, the PCEHR WSDL tree under wsdls/src/main/resources is tracked. Maintainers
update it when ADHA releases new contract versions. The enforcer sentinel is
wsdls/src/main/resources/wsdl/External/B2B_PCEHRProfile.wsdl (via ${mhr.wsdl.dir}).

Override mhr.wsdl.resources.root only when pointing at an alternate resource tree (fork,
vendor drop, or CI mirror). Use forward slashes in property values for cross-platform builds.

**Skip codegen:** -Dmhr.wsdl.codegen.skip=true (compile needs existing
target/generated-sources/wsimport after clean).

### 5.2 local.properties

Copy local.properties.example to local.properties beside the JVM working directory
(gitignored).

Today: placeholder MHR_* keys for a future TestConfiguration-style layer (keystore,
truststore, endpoint base). Integration tests still read endpoint and certificate
constants from src/test/java utilities (for example Endpoints.java, SecurityConstants.java,
SampleEndpoints.java in sample code).

**Do not commit:** populated local.properties, certs/, real keystores, passwords, or
production endpoint URLs.

Planned alignment with hi-b2b-client-java: environment variable overrides
local.properties overrides test defaults.

### 5.3 settings.xml

No settings.xml.example in this repo. Use -Dmhr.wsdl.resources.root or
MHR_WSDL_RESOURCES_ROOT for one-off overrides. build.ps1 honours MVN_SETTINGS if set.

### 5.4 Integration test material

Historical tests under src/test/java call cert-environment endpoints configured in test
helper classes. They require mutual-TLS keystores (often under src/test/resources/security/
or paths in test code), truststores, and valid registration metadata.

Keep secrets in gitignored files or CI secret stores, not in source.

### 5.5 Test fixtures

Metadata unit tests load XML via IOUtils.readBytes from src/test/resources/TestFiles/.
Use readBytes (UTF-8 file bytes), not String.getBytes() without a charset, so Windows
default charset does not corrupt CDA input.

---

## 6. Tests (maintainer view)

| Command | Scope |
| ------- | ----- |
| mvn -B "-Dgpg.skip=true" test | Default offline unit tests (pom.xml surefire includes) |
| mvn -B "-Dgpg.skip=true" -Pintegration test | All *Test.java; needs live/cert endpoints and TLS material |

Default includes: ArgumentUtilsTest, CommonHeaderValidatorTest, DateUtilsTest,
MetadataStartStopTimeTest, MetadataUtilsDefaultLangTest, MetadataUtilsOrgIDNameTest,
MetadataUtilsTimeTest, OIDUtilTest.

After mvn clean, run full verify so wsimport regenerates before using
-Dmhr.wsdl.codegen.skip=true.

---

## 7. JAXB / hand-written XDS notes

- GlobalBindings.jxb: xjc:simple, DateAdapter for xsd:date to java.util.Calendar.
- Generated collection accessors are singular (getSlot(), not getSlots()).
- DateUtils.toUtcDate: positive timezone offsets use operator "+" (required for
  findTimeZonePatternByLength validation).

---

## 8. Common maintainer tasks

**Add a B2B WSDL and facade**

1. Add WSDL (and XSD deltas) under wsdls/src/main/resources.
2. Add binding file under wsdls/src/main/resources/binding/ if needed.
3. Add jaxws-maven-plugin execution; add id to merge list.
4. Implement facade extending Client under the appropriate package.
5. Add unit and/or integration tests.

**Bump commons-lang3**

Validate.notEmpty on null strings throws NullPointerException in 3.14+. Prefer explicit
null/empty checks throwing IllegalArgumentException where tests expect IAE (see
CommonHeaderValidator.requireNonEmpty).

**Bump jaxws-rt**

Same process as HI: update property, mvn clean verify, fix wsimport/binding regressions.

**target/ locked**

Same as CONTRIBUTING.md (maven-clean-plugin force/retry; manual delete if needed).

---

## 9. Sibling project alignment

Keep plugin and shared dependency versions aligned with hi-b2b-client-java (jaxws-rt,
jaxws-maven-plugin, gmavenplus, surefire, compiler, shade, smi-xsp). HI requires a
separately installed licensed tree; MHR WSDL lives in wsdls/src/main/resources.

---

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA (Australian Digital Health Agency).
Licensed under the Apache License, Version 2.0. See LICENSE.txt.
