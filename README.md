# MHR B2B Client

Maven library for Australia's **My Health Record (PCEHR) B2B** SOAP APIs over **JAX-WS**.

**Audience:** applications that depend on **`au.gov.nehta:mhr-b2b-client`**, supply mutual-TLS credentials and ADHA-registered product metadata, and call PCEHR record access, document exchange, views, and templates. To **build or change this repository**, see **`CONTRIBUTING.md`**, **`MAINTAINERS.md`**, and **`SECURITY.md`**.

Traffic uses **HTTPS with mutual TLS** and **signed** SOAP. You need ADHA registration, test or production **certificates**, and **endpoint URLs** before live calls succeed.

---

## Dependency

```xml
<dependency>
  <groupId>au.gov.nehta</groupId>
  <artifactId>mhr-b2b-client</artifactId>
  <version>1.6.3-SNAPSHOT</version>
</dependency>
```

**Runtime:** JDK **8+** (see **`maven.compiler.release`** in **`pom.xml`**). Maven pulls EE4J **`jaxws-rt`** **2.3.x** and SOAP types from **`au.gov.nehta:pcehr-compiled-wsdl`** at **`pcehr.wsdl.version`** = **`${project.version}`**.

Application code uses **`javax.xml.ws`**, **`javax.xml.bind`**, and related **`javax`** APIs on this branch.

### Local development (SNAPSHOT)

When both repositories are unpublished, install the types JAR **first** at the **same Maven version** (**`1.6.3-SNAPSHOT`**):

```text
# 1) pcehr-compiled-wsdl-java
mvn -B "-Dgpg.skip=true" clean install

# 2) mhr-b2b-client-java
mvn -B "-Dgpg.skip=true" clean verify
```

If Maven warns that a **GA** POM is missing, remove stale **`au.gov.nehta:pcehr-compiled-wsdl`** entries from your local Maven repository and reinstall the SNAPSHOT. **`mvn clean`** does not clear the local Maven cache.

---

## WSDL/XSD

PCEHR B2B WSDL and XSD are committed under **`wsdls/src/main/resources/`**. They are **not** separate ADHA-licensed artefacts (contrast with **hi-b2b-client-java** HI contracts). The published facade JAR uses generated types from **`pcehr-compiled-wsdl`**; the in-repo tree is the canonical contract reference. See **`wsdls/readme.txt`**.

### Optional: regenerate types with Ant (maintainers)

To run **`wsimport`** locally from the in-repo WSDL tree (not required for normal **`mvn verify`**):

```text
cd wsdls
./sync-lib.ps1                         # refresh lib/provided if ee4j.jaxws.version changes
ant -f build.xml generate-src          # requires Apache Ant on PATH
```

Tooling under **`wsdls/lib/provided/`** is **Eclipse EE4J** (**`jaxws-tools`**, **`jaxws-rt`**) — not legacy Metro **`webservices-*`**. Offline test **`WsdlsCodegenToolingTest`** guards this layout in CI.

---

## What you configure in your application

Construct a facade client (for example **`DoesPCEHRExistClient`**) with:

| Item | Purpose |
| ---- | ------- |
| **`SSLSocketFactory`** | Mutual TLS to the PCEHR endpoint. |
| Private key + certificate | TLS and SOAP signing. |
| Endpoint URL | SOAP service URL from your registration. |
| Product / vendor / user qualified IDs | Values issued for your software product. |

Load keystores, truststores, and identifiers from your platform. Do not commit credentials to source control (**`SECURITY.md`**).

---

## Client classes

Package base: **`au.gov.nehta.vendorlibrary.pcehr.clients`**.

| Area | Examples |
| ---- | -------- |
| Record access | `DoesPCEHRExistClient`, `GainPCEHRAccessClient` |
| Document exchange | `UploadDocumentClient`, `GetDocumentClient`, `RemoveDocumentClient` |
| Views | `GetViewClient`, `GetDocumentListClient`, `GetAuditViewClient` |
| Templates | `GetTemplateClient`, `SearchTemplateClient` |

---

## Build and test

From the repository root:

```text
mvn -B "-Dgpg.skip=true" clean verify
```

- Default Surefire: **offline** unit tests only.
- Full mutual-TLS integration suite: **`mvn -B -Pintegration -Dgpg.skip=true clean test`** with local keystores and endpoints configured.
- Sample sources: **`mvn -B -Psample -Dgpg.skip=true -DskipTests=true clean compile`**
- Shaded JAR (classifier **`all`**): **`mvn -B -Pfat-jar -Dgpg.skip=true clean verify`**

Optional: **`./build.sh`**, **`build.ps1`**.

---

## Publishing and public hosting

This repository is intended for public Git hosting. Do not commit secrets, real keystores, or production endpoint credentials. **`local.properties`** is gitignored.

---

## Related repositories

| Repository | Role |
| ---------- | ---- |
| [pcehr-compiled-wsdl-java](https://github.com/AuDigitalHealth/pcehr-compiled-wsdl-java) | Generated PCEHR SOAP types (**1.6.3**, Java 8 / `javax`) |
| [hi-b2b-client-java](https://github.com/AuDigitalHealth/hi-b2b-client-java) | Healthcare Identifiers client (separate domain) |
