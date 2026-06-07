# Contributing

**Audience:** developers building, testing, or changing **this repository**. Library integrators should use **`README.md`** and published Javadoc.

---

## Prerequisites

- **JDK 11+** with **`JAVA_HOME`** set (`maven.compiler.release` in **`pom.xml`**).
- **Maven 3.6+** on **`PATH`**.
- **`pcehr-compiled-wsdl-java`** (**`java-11-jakarta`**, version **`${project.version}`**) installed locally (`mvn install`). See **`README.md`**.

---

## Build

From the project root (directory containing **`pom.xml`**):

```text
mvn -B "-Dgpg.skip=true" clean verify
```

Skip tests: **`mvn -B "-Dgpg.skip=true" clean package "-DskipTests=true"`**.

| Goal | Command |
| ---- | ------- |
| Default unit tests (offline, 45 tests) | `mvn -B "-Dgpg.skip=true" test` |
| Full build (WSDL JAR) | `mvn -B "-Dgpg.skip=true" clean verify` |
| Full build + wsimport validation | `mvn -B "-Dgpg.skip=true" -Pwsimport clean verify` |
| Full mutual-TLS / historical suite | `mvn -B "-Dgpg.skip=true" -Pintegration clean test` |
| Sample sources | `mvn -B -Psample "-DskipTests=true" clean compile` |
| Install to local repo | `mvn clean install` |

Default Surefire **`<includes>`** (see **`pom.xml`**): **`ArgumentUtilsTest`**, **`CommonHeaderValidatorTest`**, **`DateUtilsTest`**, metadata unit tests, **`OIDUtilTest`**, **`PcehrWsdlArtifactSmokeTest`**, **`ViewClientsSmokeTest`**. Details: **`MAINTAINERS.md`**.

**WSDL JAR vs wsimport:** compile types always come from **`au.gov.nehta:pcehr-compiled-wsdl`** (main **`pom.xml`** dependency). The default build does not run wsimport. **`-Pwsimport`** additionally validates in-repo codegen (12 B2B services); merged output is under **`target/generated-sources/wsimport`** and is not on the compile classpath. View facades and tests: **`ADHA-THIRD-PARTY-SCOPE.md`**. To change generated types, update **`pcehr-compiled-wsdl-java`**, reinstall at **`${project.version}`**, then run **`mvn clean verify`** (and **`mvn -Pwsimport clean verify`** when bindings change).

Optional Maven settings: copy **`settings.xml.example`** to **`settings.xml`** (gitignored) or set **`MVN_SETTINGS`** when using **`build.ps1`** / **`build.sh`**.

## Integration tests

Historical tests under **`src/test/java`** call cert-environment endpoints configured in test helper classes (for example **`Endpoints.java`**). They are **not** in the default Surefire **`<includes>`** list.

**`-Pintegration`** runs **`**/*Test.java`**. You need valid mutual-TLS material and registration metadata before expecting green results.

**`local.properties.example`** documents planned **`MHR_*`** keys for a future **`TestConfiguration`** layer (aligned with **`hi-b2b-client-java`**). Until that lands, keep endpoints and keystores in gitignored copies or environment-specific test utilities. See **`MAINTAINERS.md`** section 5.

---

## Repository hygiene

- **Do not commit:** passwords, API tokens, private keys, real mutual-TLS keystores, or production PCEHR URLs with embedded credentials. See **`SECURITY.md`**.
- **Line endings:** LF per **`.gitattributes`**. On Windows: **`git config core.autocrlf false`** in this clone before committing.
- **Focused commits;** match existing style.
- **POM, `pcehr-compiled-wsdl`, and dependency layout:** **`MAINTAINERS.md`**.

---

## `target/` locks

If **`mvn clean`** cannot delete **`target/`**, close IDE Java language servers, antivirus scanners, or other processes holding files. **`maven-clean-plugin`** uses **`force`** and **`retryOnError`**.

**Windows:** from project root:

```text
attrib -R /S /D *.*
rmdir /s /q target
```

**macOS / Linux:** **`chmod -R u+w target`** then **`rm -rf target`**.

---

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Apache License 2.0 — see **LICENSE.txt**.
