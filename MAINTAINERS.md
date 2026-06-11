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

Published artifact: compiled facades and helpers. PCEHR WSDL/XSD are tracked in Git for
builds (not HI-style licensed material) but excluded from the published JAR (maven-jar-plugin excludes *.wsdl).

---

## 2. Repository layout

| Path | Role |
| ---- | ---- |
| pom.xml | Build, profiles, dependency versions (`pcehr-compiled-wsdl` at `${pcehr.wsdl.version}`) |
| src/main/java/au/gov/nehta/vendorlibrary/pcehr/clients/ | Public PCEHR client facades |
| src/main/java/au/gov/nehta/vendorlibrary/ws/ | Shared JAX-WS helpers |
| src/main/java/au/gov/nehta/common/utils/ | Vendored ArgumentUtils (duplicate excluded in fat-jar) |
| src/main/java/META-INF/metro.xml | JAX-WS RI tubeline config (not a separate Metro Maven bundle) |
| src/sample/java/ | Sample programs (`-Psample`; restored from `java-8-javax` with Jakarta imports) |
| src/test/java/ | Offline unit tests and historical integration tests |
| src/test/resources/TestFiles/ | CDA/XML fixtures for metadata unit tests |
| wsdls/src/main/resources/wsdl/External/ | B2B_*.wsdl (tracked) |
| wsdls/src/main/resources/schema/ | XSD includes |
| wsdls/src/main/resources/binding/ | GlobalBindings.jxb, PCEHR_CommonTypes.xsd.jxb, per-WSDL *.jxb |
| wsdls/src/main/java/ | DateAdapter; **pcehr_override/org/w3/** hand-written xmldsig types (used by **pcehr-compiled-wsdl-java**) |
| local.properties.example | Template reserved for future integration-test configuration |
| settings.xml.example | Optional Maven user settings fragment (copy to gitignored **settings.xml**) |
| build.ps1, build.sh, build.bat | Thin wrappers around **mvn clean verify**; **wsimport** / **shaded** args |
| .github/workflows/ci.yml | **verify** (default) and **wsimport** jobs; installs **pcehr-compiled-wsdl-java** first |

Branch model (maintainer-only): **`java-11-jakarta-full-wsdl`** — JDK **11**, Jakarta XML Web Services, **`jaxws-rt` 4.0.4**, **26** facades. Compile against **`au.gov.nehta:pcehr-compiled-wsdl`** at **`${pcehr.wsdl.version}`** = **`${project.version}`** (**`1.7.0-SNAPSHOT`** during dev); install matching **`pcehr-compiled-wsdl-java`** first.

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
- **WebServiceClientUtil** loads generated Service classes from **`pcehr-compiled-wsdl`**
  (wsdlLocation metadata from that artifact). Integrators pass the HTTPS endpoint URL into
  the facade constructor.
- **CommonHeaderValidator** validates PCEHRHeader before requests (use requireNonEmpty
  for strings; commons-lang3 3.14+ throws NullPointerException from Validate.notEmpty on
  null CharSequence).
- **MetadataUtils**, **XDSFactory**, **XDSMapper** implement XDS metadata and document
  packaging helpers using **pcehr-compiled-wsdl** RIM/XDS types (e.g. `getSlots()`,
  `getClassifications()`, `Slot`, `RetrieveDocumentSetRequest`).

### 3.2 Facade inventory (15 classes + Client base)

| Package | Class | Primary WSDL |
| ------- | ----- | ------------------------ |
| recordaccess/ | DoesPCEHRExistClient, GainPCEHRAccessClient | B2B_PCEHRProfile |
| registration/ | RegisterPCEHRClient | B2B_RegisterPCEHR |
| documentexchange/ | UploadDocumentClient, GetDocumentClient, UploadDocumentMetadataClient, RemoveDocumentClient | B2B_DocumentRepository, B2B_DocumentRegistry, B2B_RemoveDocument |
| view/ | GetDocumentListClient, GetViewClient, GetAuditViewClient, GetChangeHistoryViewClient, GetIndividualDetailsViewClient, GetRepresentativeListClient | `B2B_GetView` (7 third-party types), `B2B_GetAuditView`, `B2B_GetChangeHistoryView`, `B2B_GetIndividualDetailsView`, `B2B_GetRepresentativeList`; `GetDocumentListClient` uses `B2B_DocumentRegistry`. See **`ADHA-THIRD-PARTY-SCOPE.md`**. Offline: **`ViewClientsSmokeTest`**; integration: `tests/view/*`, **`TestGetView`**. |
| template/ | GetTemplateClient, SearchTemplateClient | B2B_GetTemplate, B2B_SearchTemplate |

GainPCEHRAccess authorisation accessType is a String enum value ("AccessCode",
"EmergencyAccess"), not a nested AccessType enum.

PCEHRHeader may carry an IHI resolved via the HI client in integrator applications; this
repo does not embed HI WSDL.

**GetView:** seven view types (see **`ADHA-THIRD-PARTY-SCOPE.md`**). Use HRO `versionNumber` `"1.1"` in samples and tests.

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

1. Compile facades against **`au.gov.nehta:pcehr-compiled-wsdl`** at **`${pcehr.wsdl.version}`** (main dependency; all profiles).
2. Optional **`-Pwsimport`**: validate in-repo wsimport without putting generated sources on the compile classpath.
3. Exclude *.wsdl from the published mhr-b2b-client JAR.
4. Default test run: offline unit tests only; full historical suite via **`-Pintegration`**.

### 4.2 Lifecycle (Maven phases)

```
validate / initialize
  enforcer: require-pcehr-wsdl-tree  -> wsdls/src/main/resources/wsdl/External/B2B_PCEHRProfile.wsdl

[-Pwsimport only]
  initialize: purge prior wsimport output
  generate-sources: jaxws-maven-plugin (12 B2B wsimport executions)
  process-sources: GMavenPlus merge -> target/generated-sources/wsimport (not compiled)

compile / test / package  (types from pcehr-compiled-wsdl in both profiles)
  compiler (default testIncludes: offline unit tests + sample/common helpers)
  surefire (default includes: same offline set)
  jar (WSDL excluded), optional gpg/javadoc/source attachments
```

SOAP/JAXB types come from **`au.gov.nehta:pcehr-compiled-wsdl`** (main **`pom.xml`** dependency). Install **`pcehr-compiled-wsdl-java`** at **`${project.version}`** before building. Regenerate that artifact from the WSDL tree under **`wsdls/src/main/resources/`** when schemas or bindings change.

**Optional (`-Pwsimport`):** runs the same **12** `jaxws-maven-plugin` executions as **`pcehr-compiled-wsdl-java`**, merges output under **`target/generated-sources/wsimport`** (validation / diff only — **not** on the compile classpath).

```bash
mvn -B -Dgpg.skip=true clean verify              # default
mvn -B -Dgpg.skip=true -Pwsimport clean verify   # + wsimport validation (~50s)
```

Requires **`pcehr-compiled-wsdl`** installed locally at **`${project.version}`**.

### 4.3 Key Maven properties (pom.xml)

| Property | Default / role |
| -------- | -------------- |
| maven.compiler.release | 11 |
| jaxws.rt.version | jaxws-rt and jaxws-tools (4.0.4) |
| mhr.wsdl.dir | Reference WSDL sentinel path for enforcer |
| mhr.wsdl.codegen.skip | **true** (default); **false** when **`-Pwsimport`** |
| mhr.wsdl.resources.root | WSDL/XSD root; override via **`-Dmhr.wsdl.resources.root`** or **`MHR_WSDL_RESOURCES_ROOT`** |
| jaxws.maven.plugin.version / jaxb.xjc.version | wsimport profile only (4.0.2 / 4.0.7) |
| pcehr.wsdl.version | ${project.version}; **`au.gov.nehta:pcehr-compiled-wsdl`** coordinate |
| maven.shade.plugin.version | fat-jar profile |
| skipTests | false; default surefire includes offline unit tests only |

### 4.4 Profiles

| Profile | Effect |
| ------- | ------ |
| wsimport | In-repo **12**-execution wsimport validation (see §4.2); compile still uses main **`pcehr-compiled-wsdl`** dependency |
| integration | Clears compiler **testIncludes**; Surefire runs **/*Test.java (needs endpoints, certs; see section 5) |
| sample | Adds src/sample/java at generate-sources |
| fat-jar | maven-shade-plugin produces mhr-b2b-client-*-all.jar; excludes duplicate ArgumentUtils from smi-xsp |

Build wrappers: mvn -B -Dgpg.skip=true clean verify; pass shaded for fat-jar.

---

## 5. External inputs and local configuration

### 5.1 WSDL/XSD in Git

Unlike HI, the PCEHR WSDL tree under wsdls/src/main/resources is tracked. Maintainers
update it when ADHA releases new contract versions. The enforcer sentinel is
wsdls/src/main/resources/wsdl/External/B2B_PCEHRProfile.wsdl (via ${mhr.wsdl.dir}).

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

No settings.xml.example in this repo. build.ps1 honours MVN_SETTINGS if set.

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

Default includes (45 tests): ArgumentUtilsTest, CommonHeaderValidatorTest, DateUtilsTest,
MetadataStartStopTimeTest, MetadataUtilsDefaultLangTest, MetadataUtilsOrgIDNameTest,
MetadataUtilsTimeTest, OIDUtilTest, PcehrWsdlArtifactSmokeTest, ViewClientsSmokeTest.

`unittests/views/GetRepresentativeListClientTest` is a mutual-TLS integration test (not in default includes); run with **`-Pintegration`**.

Default **test-compile** uses the same offline set (plus `src/test/java/.../sample/common`
helpers). **`-Pintegration`** compiles all tests under `src/test/java`.

---

## 7. JAXB / hand-written XDS notes

- **Compile classpath:** types from **pcehr-compiled-wsdl** (JAXB 2.x-style plural list accessors).
- **GlobalBindings.jxb:** DateAdapter for `xsd:date` → `java.util.Calendar`; optional **rim.xsd.jxb** / **XDS_DocumentRepository.xsd.jxb** for in-repo wsimport experiments.
- In-repo wsimport (Jakarta XJC 4.x) may emit different accessor names; **`-Pwsimport`** validates codegen but does not replace **pcehr-compiled-wsdl** on the compile classpath.
- DateUtils.toUtcDate: positive timezone offsets use operator "+" (required for
  findTimeZonePatternByLength validation).

---

## 8. Common maintainer tasks

**Add a B2B WSDL and facade**

1. Add WSDL (and XSD deltas) under wsdls/src/main/resources.
2. Add binding file under wsdls/src/main/resources/binding/ if needed.
3. Regenerate and install **pcehr-compiled-wsdl-java** at the matching version.
4. Implement facade extending Client under the appropriate package.
5. Add unit and/or integration tests.

**Bump commons-lang3**

Validate.notEmpty on null strings throws NullPointerException in 3.14+. Prefer explicit
null/empty checks throwing IllegalArgumentException where tests expect IAE (see
CommonHeaderValidator.requireNonEmpty).

**Bump jaxws-rt**

Update property, reinstall **pcehr-compiled-wsdl-java**, then `mvn clean verify` here.

**target/ locked**

Same as CONTRIBUTING.md (maven-clean-plugin force/retry; manual delete if needed).

---

## 9. Public release checklist

Before tagging or publishing to a public Git host:

1. **No secrets** in the tree (keystores, passwords, production URLs with credentials). See **SECURITY.md**.
2. Install **`pcehr-compiled-wsdl-java`** at **`${project.version}`**; run **`mvn -B "-Dgpg.skip=true" clean verify`** (default WSDL JAR build).
3. Run **`mvn -B "-Dgpg.skip=true" -Pwsimport clean verify`** (in-repo wsimport validation).
4. Confirm **`.github/workflows/ci.yml`** passes (both **verify** and **wsimport** jobs).
5. **LICENSE.txt**, **README.md**, **CONTRIBUTING.md**, **SECURITY.md**, **ADHA-THIRD-PARTY-SCOPE.md** current for release **`1.7.0`**.
6. Published JAR excludes **`*.wsdl`** (maven-jar-plugin); WSDL/XSD remain in Git for reference and **pcehr-compiled-wsdl-java** regeneration.

Integrators consume **`au.gov.nehta:mhr-b2b-client`** from Maven Central; they do **not** need **`-Pwsimport`**.

---

## 10. Sibling project alignment

Keep plugin and shared dependency versions aligned with hi-b2b-client-java (jaxws-rt,
surefire, compiler, shade, smi-xsp). HI requires a separately installed licensed tree; MHR
WSDL lives in wsdls/src/main/resources and is codegen’d in **pcehr-compiled-wsdl-java**.

---

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA (Australian Digital Health Agency).
Licensed under the Apache License, Version 2.0. See LICENSE.txt.
