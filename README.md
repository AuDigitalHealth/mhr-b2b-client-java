# MHR B2B Client

Maven artifact **`au.gov.nehta:mhr-b2b-client`** - Java facade clients for Australia's **My Health Record (MHR) B2B** SOAP APIs over **JAX-WS**.

For generated types and classpath WSDL, use **[mhr-wsdl-java](https://github.com/AuDigitalHealth/mhr-wsdl-java)**. This repository supplies **facade clients**, **TLS**, and **SOAP signing**. The pairing is the same as **`hi-wsdl`** / **`hi-b2b-client`**: same Maven version, types JAR first.

**Audience:** applications that depend on **`au.gov.nehta:mhr-b2b-client`**, supply mutual-TLS credentials and ADHA-registered product metadata, and call record access, document exchange, views, and templates. To **build or change this repository**, see **`CONTRIBUTING.md`**, **`MAINTAINERS.md`**, and **`SECURITY.md`**.

Traffic uses **HTTPS with mutual TLS** and **signed** SOAP. You need ADHA registration, test or production **certificates**, and **endpoint URLs** before live calls succeed.

## Dependency

Published releases are consumed from **[Maven Central](https://central.sonatype.com/)**. Use a **`<version>`** that matches your JDK (see **Versioning**).

```xml
<dependency>
  <groupId>au.gov.nehta</groupId>
  <artifactId>mhr-b2b-client</artifactId>
  <version>24.0.0</version>
</dependency>
```

**This line (`24.0.0`):** Java **24**, **Jakarta XML Web Services / JAXB**, **15** facade clients. Runtime SOAP stack is Eclipse EE4J **`com.sun.xml.ws:jaxws-rt`** **4.0.5**. Pair with **`au.gov.nehta:mhr-wsdl`** **`24.0.0`**. Sibling libs **`common-library`**, **`smi-xsp`**, and **`smi-common-utils`** use the same Maven version. Do **not** use legacy Metro **`webservices-*`** bundles.

When **`mhr-wsdl`** is also on the classpath, use the **same** Maven version for both artifacts.

---

## Versioning

The **first number** of the Maven version is the **Java SE** version that this client targets. **`mhr-b2b-client`** and **`mhr-wsdl`** always use the **same** version on a given line (same SNAPSHOT or GA).

| Maven version | Java SE | XML stack                               | Facades          |
| ------------- | ------- | --------------------------------------- | ---------------- |
| **11.0.0**    | **11**  | **Jakarta** / EE4J **`jaxws-rt` 4.0.x** | **15** (MHR B2B) |
| **17.0.0**    | **17**  | **Jakarta** / EE4J **`jaxws-rt` 4.0.x** | **15** (MHR B2B) |
| **21.0.0**    | **21**  | **Jakarta** / EE4J **`jaxws-rt` 4.0.x** | **15** (MHR B2B) |
| **24.0.0**    | **24**  | **Jakarta** / EE4J **`jaxws-rt` 4.0.x** | **15** (MHR B2B) |

Pick the coordinate that matches your JDK. Do not mix **`mhr-b2b-client`** versions with **`mhr-wsdl`** from a different line. All published versions are on **[Maven Central](https://central.sonatype.com/)**.

Java packages and type names use **`mhr`** (`au.gov.nehta.vendorlibrary.mhr`, **`DoesMHRExistClient`**, **`MHRHeader`**, **`RegisterMHRClient`**). SOAP/XML namespaces, element names, and operation names stay the published B2B contract (`http://ns.electronichealth.net.au/pcehr/...`, **`PCEHRHeader`**, **`registerPCEHR`**).

---

## Note

The **24.0.0** JAR ships **15** MHR B2B facade clients on **Jakarta**. That set matches the logical B2B interfaces in **mhr-b2b-client-dotnet** (including **getView** and its **7** clinical views). See **ADHA-THIRD-PARTY-SCOPE.md**.

---

## Local development (SNAPSHOT)

This repository builds **`24.0.0-SNAPSHOT`**. Install unpublished sibling **`au.gov.nehta`** libs at the **same Maven version** first (**`mhr-wsdl`**, **`common-library`**, **`smi-xsp`**, **`smi-common-utils`**), then **`verify`** here:

```text
# 1) sibling au.gov.nehta libs (same version)
mvn -B "-Dgpg.skip=true" clean install

# 2) mhr-b2b-client-java
mvn -B "-Dgpg.skip=true" clean verify
```

If Maven warns that a **GA** POM is missing (for example **`24.0.0`** before Central publish), clear stale **`au/gov/nehta/`** entries in your **local Maven repository** (folders with only **`.lastUpdated`** files) and reinstall the SNAPSHOTs. **`mvn clean`** in this project does not clear the local repository cache.

---

## WSDL/XSD

PCEHR B2B WSDL and XSD are committed under **`wsdls/`**. They are **not** separate ADHA-licensed artefacts (contrast with **hi-b2b-client-java** HI contracts). The published facade JAR uses generated types from **`mhr-wsdl`**; the in-repo tree is the canonical contract reference. See **`wsdls/readme.txt`**.

### Optional: regenerate types with Ant (maintainers)

To run **`wsimport`** locally from the in-repo WSDL tree (not required for normal **`mvn verify`**):

```text
cd wsdls
./sync-lib.ps1                         # refresh lib/provided if ee4j.jaxws.version changes
ant -f build.xml generate-src          # requires Apache Ant on PATH
```

Tooling under **`wsdls/lib/provided/`** is **Eclipse EE4J** (**`jaxws-tools`**, **`jaxws-rt`**) - not legacy Metro **`webservices-*`**. Offline test **`WsdlsCodegenToolingTest`** guards this layout in CI.

---

## What you configure in your application

Construct a facade client (for example **`DoesMHRExistClient`**) with:

| Item                                  | Purpose                                  |
| ------------------------------------- | ---------------------------------------- |
| **`SSLSocketFactory`**                | Mutual TLS to the PCEHR endpoint.        |
| Private key + certificate             | TLS and SOAP signing.                    |
| Endpoint URL                          | SOAP service URL from your registration. |
| Product / vendor / user qualified IDs | Values issued for your software product. |

Load keystores, truststores, and identifiers from your platform. Do not commit credentials to source control (**`SECURITY.md`**).

---

## Client classes

Package base: **`au.gov.nehta.vendorlibrary.mhr.clients`**.

| Area              | Examples                                                                                                                                                      |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Record access     | `DoesMHRExistClient`, `GainMHRAccessClient`                                                                                                                   |
| Document exchange | `UploadDocumentClient`, `GetDocumentClient`, `RemoveDocumentClient`, `UploadDocumentMetadataClient`                                                           |
| Registration      | `RegisterMHRClient`                                                                                                                                           |
| Views             | `GetViewClient`, `GetDocumentListClient`, `GetAuditViewClient`, `GetChangeHistoryViewClient`, `GetIndividualDetailsViewClient`, `GetRepresentativeListClient` |
| Templates         | `GetTemplateClient`, `SearchTemplateClient`                                                                                                                   |

---

## Build and test

From the repository root (after sibling **`au.gov.nehta`** libs are resolvable):

```text
mvn -B "-Dgpg.skip=true" clean verify
```

- Default Surefire: **offline** unit tests only.
- Full mutual-TLS integration suite: **`mvn -B -Pintegration "-Dgpg.skip=true" clean test`** with local keystores and endpoints configured.
- Sample sources: **`mvn -B -Psample "-Dgpg.skip=true" -DskipTests=true clean compile`**
- Shaded JAR (classifier **`all`**): **`mvn -B -Pfat-jar "-Dgpg.skip=true" clean verify`**

Optional: **`./build.sh`**, **`build.ps1`**.

---

## Related repositories

| Repository                                                                  | Role                                                     |
| --------------------------------------------------------------------------- | -------------------------------------------------------- |
| [mhr-wsdl-java](https://github.com/AuDigitalHealth/mhr-wsdl-java)           | Generated MHR SOAP types (**24.0.0**, Java 24 / Jakarta) |
| [hi-b2b-client-java](https://github.com/AuDigitalHealth/hi-b2b-client-java) | Healthcare Identifiers client (separate domain)          |

## Documentation

| Document                  | Audience                        |
| ------------------------- | ------------------------------- |
| **README.md** (this file) | Integrators                     |
| **CONTRIBUTING.md**       | Contributors                    |
| **MAINTAINERS.md**        | Releases and tooling            |
| **SECURITY.md**           | Secrets and reporting           |
| **CHANGELOG.md**          | Release history                 |
| **LICENSE.txt**           | Apache License 2.0 + ADHA terms |

## License

Apache License 2.0. See **LICENSE.txt**.
