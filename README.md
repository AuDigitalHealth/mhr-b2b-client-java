# MHR B2B Client

Java Maven library for Australia's My Health Record PCEHR B2B SOAP services.

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

- JDK 11+
- valid endpoint URL(s)
- client certificate and private key for mutual TLS
- truststore for endpoint certificates
- populated `PCEHRHeader` with your organisation and user context

## Available clients

Package base: `au.gov.nehta.vendorlibrary.pcehr.clients`

- `recordaccess`: `DoesPCEHRExistClient`, `GainPCEHRAccessClient`
- `registration`: `RegisterPCEHRClient`
- `documentexchange`: `UploadDocumentClient`, `GetDocumentClient`, `UploadDocumentMetadataClient`, `RemoveDocumentClient`
- `view`: `GetDocumentListClient`, `GetViewClient`, `GetAuditViewClient`, `GetChangeHistoryViewClient`, `GetIndividualDetailsViewClient`, `GetRepresentativeListClient`
- `template`: `GetTemplateClient`, `SearchTemplateClient`

## Build from source

```text
mvn -B "-Dgpg.skip=true" clean verify
```

Or use root **`build.ps1`** / **`build.sh`** / **`build.bat`**. Pass **`shaded`** (or **`-Shaded`** on PowerShell) to activate the **`fat-jar`** profile and produce **`mhr-b2b-client-*-all.jar`**.

This build generates SOAP/JAXB sources from the WSDL set in `wsdls/src/main/resources/`.

### Optional: pre-generated types from mhr-wsdl-java

Instead of running the **12** in-repo **`wsimport`** executions, you can compile against **`mhr-wsdl-java`** (**`au.gov.nehta:mhr-wsdl`**, same version line as this client):

```text
cd ../mhr-wsdl-java
mvn -B "-Dgpg.skip=true" clean install
cd ../mhr-b2b-client-java
mvn -B "-Pmhr-wsdl-artifact" "-Dgpg.skip=true" clean verify
```

Do **not** combine **`mhr-wsdl`** on the classpath with a fresh **`wsimport`** output from this repo (duplicate generated types). Use either the default build or **`-Pmhr-wsdl-artifact`**, not both.

## Tests

Default Surefire run (offline unit tests only):

```text
mvn -B "-Dgpg.skip=true" test
```

Full mutual-TLS integration suite:

```text
mvn -B "-Dgpg.skip=true" -Pintegration test
```

## Samples

Sample usage classes are under `src/sample/java`.

To compile samples from a source checkout:

```text
mvn -B -Psample "-DskipTests=true" clean compile
```

## Security

Do not commit certificates, private keys, keystores, passwords, or endpoint secrets.
See `SECURITY.md` for reporting and repository security policy.

## ADHA third-party scope

Supported `getView` types: **`ADHA-THIRD-PARTY-SCOPE.md`**. WSDL-to-facade mapping: **`WSDL-CLIENT-PURPOSES.md`** (also in `myhr-b2b-client-java` for the three-client view).

## License

Apache License 2.0. See `LICENSE.txt`.
