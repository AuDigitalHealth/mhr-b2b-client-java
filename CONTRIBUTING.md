# Contributing

**Audience:** developers building, testing, or changing **this repository**. Library integrators should use **`README.md`** and published Javadoc.

---

## Prerequisites

- **JDK 11+** with **`JAVA_HOME`** set (`maven.compiler.release` in **`pom.xml`**).
- **Maven 3.6+** on **`PATH`**.
- WSDL/XSD under **`wsdls/src/main/resources/`** (tracked in Git). The build requires **`wsdls/src/main/resources/wsdl/External/B2B_PCEHRProfile.wsdl`** (enforcer sentinel). See **`wsdls/readme.txt`**.

---

## Build

From the project root (directory containing **`pom.xml`**):

```text
mvn -B "-Dgpg.skip=true" clean verify
```

Skip tests: **`mvn -B "-Dgpg.skip=true" clean package "-DskipTests=true"`**.

| Goal | Command |
| ---- | ------- |
| Default unit tests (offline) | `mvn -B "-Dgpg.skip=true" test` |
| Full build with default tests | `mvn -B "-Dgpg.skip=true" clean verify` |
| Full mutual-TLS / historical suite | `mvn -B "-Dgpg.skip=true" -Pintegration clean test` |
| Sample sources | `mvn -B -Psample "-DskipTests=true" clean compile` |
| Install to local repo | `mvn clean install` |

Default Surefire **`<includes>`** (see **`pom.xml`**): **`ArgumentUtilsTest`**, **`CommonHeaderValidatorTest`**, **`DateUtilsTest`**, metadata unit tests, **`OIDUtilTest`**. Details: **`MAINTAINERS.md`**.

`mvn clean` removes generated sources; follow with a full compile or verify to regenerate SOAP/JAXB types from the WSDL tree.

## Integration tests

Historical tests under **`src/test/java`** call cert-environment endpoints configured in test helper classes (for example **`Endpoints.java`**). They are **not** in the default Surefire **`<includes>`** list.

**`-Pintegration`** runs **`**/*Test.java`**. You need valid mutual-TLS material and registration metadata before expecting green results.

**`local.properties.example`** documents planned **`MHR_*`** keys for a future **`TestConfiguration`** layer (aligned with **`hi-b2b-client-java`**). Until that lands, keep endpoints and keystores in gitignored copies or environment-specific test utilities. See **`MAINTAINERS.md`** section 5.

---

## Repository hygiene

- **Do not commit:** passwords, API tokens, private keys, real mutual-TLS keystores, or production PCEHR URLs with embedded credentials. See **`SECURITY.md`**.
- **Line endings:** LF per **`.gitattributes`**. On Windows: **`git config core.autocrlf false`** in this clone before committing.
- **Focused commits;** match existing style.
- **POM, `wsimport`, and dependency layout:** **`MAINTAINERS.md`**.

---

## `target/` locks

If **`mvn clean`** or **`wsimport`** cannot delete **`target/`**, close IDE Java language servers, antivirus scanners, or other processes holding files. **`maven-clean-plugin`** uses **`force`** and **`retryOnError`**.

**Windows:** from project root:

```text
attrib -R /S /D *.*
rmdir /s /q target
```

**macOS / Linux:** **`chmod -R u+w target`** then **`rm -rf target`**.

---

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Apache License 2.0 — see **LICENSE.txt**.
