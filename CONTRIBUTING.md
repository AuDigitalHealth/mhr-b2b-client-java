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

On PowerShell, quote **`-D`** arguments as shown.

Skip tests (default in **`pom.xml`** today): **`skipTests`** is **`true`** unless you enable the integration profile.

| Goal | Command |
| ---- | ------- |
| Compile + package (no tests) | `mvn -B "-Dgpg.skip=true" clean verify` |
| Generate + compile from WSDL | `mvn -B "-Dgpg.skip=true" clean compile` |
| Integration / network tests | `mvn -B "-Dgpg.skip=true" -Pintegration clean test` |
| Sample sources | `mvn -B -Psample "-DskipTests=true" clean compile` |
| Install to local repo | `mvn clean install` |

`mvn clean` removes generated sources, so follow with a full compile/package run to regenerate SOAP/JAXB types from the WSDL tree.

---

## Integration tests

Historical tests under **`src/test/java`** call live or cert-environment endpoints and embed URLs in **`Endpoints.java`**. They are **not** run by default (**`skipTests=true`**).

**`-Pintegration`** sets **`skipTests=false`** and runs **`**/*Test.java`**. You need valid mutual-TLS material and registration metadata before expecting green results.

A future refresh may adopt **`local.properties`** (see **`local.properties.example`**) similar to **`hi-b2b-client-java`**. Until then, configure endpoints and keystores in test utilities or environment-specific copies kept **out of Git**.

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
