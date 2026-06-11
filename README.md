# MHR B2B Client

Maven library for Australia's **My Health Record (PCEHR) B2B** SOAP APIs over **Jakarta XML Web Services** (version **`1.7.0`** on Maven Central; **`1.7.0-SNAPSHOT`** during local development).

**Audience:** applications that depend on **`au.gov.nehta:mhr-b2b-client`**, supply mutual-TLS credentials and ADHA-registered product metadata, and call PCEHR record access, document exchange, views, and templates. To **build or change this repository**, see **`CONTRIBUTING.md`**, **`MAINTAINERS.md`**, and **`SECURITY.md`**.

It provides typed client facades for:
- record access
- registration
- document exchange (including MTOM)
- views
- templates

All service traffic uses HTTPS mutual TLS and SOAP message signing.

## Dependency

```xml
<dependency>
  <groupId>au.gov.nehta</groupId>
  <artifactId>mhr-b2b-client</artifactId>
  <version>VERSION</version>
</dependency>
```

Use the published version from [Maven Central](https://central.sonatype.com/).

## Runtime requirements

- **JDK 11+** (see **`maven.compiler.release`** in **`pom.xml`**)
- Maven pulls **Eclipse EE4J `jaxws-rt` 4.x**; application code uses **`jakarta.xml.ws`**, **`jakarta.xml.bind`**, and related **`jakarta.*`** APIs — not legacy **`javax.xml.ws`** / JAXB EE APIs
- Valid endpoint URL(s), client certificate and private key for mutual TLS, truststore, and populated **`PCEHRHeader`**

### Local development (SNAPSHOT)

Install **`pcehr-compiled-wsdl-java`** at the same Maven version (**`1.7.0-SNAPSHOT`**) before building this client:

```text
cd ../pcehr-compiled-wsdl-java
mvn -B "-Dgpg.skip=true" clean install

cd ../mhr-b2b-client-java
mvn -B "-Dgpg.skip=true" clean verify
```

## WSDL/XSD and build profiles

SOAP/JAXB types for compilation always come from **`au.gov.nehta:pcehr-compiled-wsdl`** (built from sibling **`pcehr-compiled-wsdl-java`**). WSDL/XSD sources are tracked under **`wsdls/src/main/resources/`**; see **`wsdls/readme.txt`**.

| Build | Command | What runs |
| ----- | ------- | --------- |
| **Default** (WSDL JAR) | `mvn clean verify` | Compiles against **`pcehr-compiled-wsdl`** only |
| **wsimport validation** | `mvn -Pwsimport clean verify` | Same compile classpath **plus** in-repo **12**-service wsimport (output under **`target/generated-sources/wsimport`**, not compiled) |

Both require **`pcehr-compiled-wsdl-java`** installed at **`${project.version}`** before building (see **Local development** above). Details: **`MAINTAINERS.md`** §4.

## Available clients

Package base: `au.gov.nehta.vendorlibrary.pcehr.clients`

- `recordaccess`: `DoesPCEHRExistClient`, `GainPCEHRAccessClient`
- `registration`: `RegisterPCEHRClient`
- `documentexchange`: `UploadDocumentClient`, `GetDocumentClient`, `UploadDocumentMetadataClient`, `RemoveDocumentClient`
- `view` (six facades; see **`ADHA-THIRD-PARTY-SCOPE.md`**):
  - `GetViewClient` — seven third-party `getView` types (`B2B_GetView`)
  - `GetIndividualDetailsViewClient`, `GetRepresentativeListClient`, `GetAuditViewClient`, `GetChangeHistoryViewClient`
  - `GetDocumentListClient` — document metadata via `B2B_DocumentRegistry`
- `template`: `GetTemplateClient`, `SearchTemplateClient`

## Build from source

```text
mvn -B "-Dgpg.skip=true" clean verify
```

Or use root **`build.ps1`** / **`build.sh`** / **`build.bat`**. Options:

| Wrapper argument | Maven profile |
| ---------------- | ------------- |
| *(none)* | default |
| **`wsimport`** / **`-Wsimport`** | **`-Pwsimport`** |
| **`shaded`** / **`-Shaded`** | **`-Pfat-jar`** (uber JAR) |

Compile dependency: **`pcehr-compiled-wsdl`** at **`${project.version}`** (**`1.7.0-SNAPSHOT`** during development).

## Tests

Default Surefire run (offline unit tests only, including **`ViewClientsSmokeTest`** for view SOAP types):

```text
mvn -B "-Dgpg.skip=true" test
```

Full mutual-TLS integration suite (view client tests under `src/test/java/.../tests/view/` and **`TestGetView`**):

```text
mvn -B "-Dgpg.skip=true" -Pintegration test
```

## Samples

Sample usage classes are under `src/sample/java`.

To compile samples from a source checkout:

```text
mvn -B -Psample "-DskipTests=true" clean compile
```

## Security and public hosting

Do not commit secrets, real keystores, or production endpoint credentials. **`local.properties`** is gitignored. See **`SECURITY.md`**.

## ADHA third-party scope

Supported third-party `getView` types: **`ADHA-THIRD-PARTY-SCOPE.md`**.

## License

Apache License 2.0. See `LICENSE.txt`.
