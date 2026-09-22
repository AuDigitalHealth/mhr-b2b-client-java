# Contributing

**Audience:** developers building or changing **this repository**. Integrators should use **README.md** and Maven Central coordinates. See **SECURITY.md** before committing.

## Prerequisites

- **JDK 8** with **`JAVA_HOME`** set (see **`maven.compiler.release`** in **`pom.xml`**).
- **Maven 3.6.3+** on **`PATH`**.

Dependencies resolve from **[Maven Central](https://central.sonatype.com/)** unless you are installing a **local SNAPSHOT** (below).

## Versioning

The **first number** of **`au.gov.nehta:mhr-b2b-client`** is the **Java SE** version that this client targets. **8.0.0** is **`javax`** with **15** facades; **11.0.0.1** and later are **Jakarta** with **15** facades. See **`README.md`**.

**`mhr-b2b-client`** pins **`mhr-wsdl`** to **`${project.version}`**. Use the **same** version for both artifacts.

## Build

From the project root, after **`mhr-wsdl`** is resolvable:

```text
mvn -B "-Dgpg.skip=true" clean verify
```

| Goal | Command |
| ---- | ------- |
| Compile + attach sources/Javadoc | `mvn -B "-Dgpg.skip=true" clean verify` |
| Skip tests | `mvn -B "-Dgpg.skip=true" clean verify "-DskipTests=true"` |
| Skip Javadoc | `mvn -B -Pdev-javadoc-off "-Dgpg.skip=true" clean verify` |
| Integration tests | `mvn -B -Pintegration "-Dgpg.skip=true" clean test` |
| Sample compile | `mvn -B -Psample "-Dgpg.skip=true" -DskipTests=true clean compile` |
| Shaded JAR | `mvn -B -Pfat-jar "-Dgpg.skip=true" clean verify` |

GPG signing is skipped by default (**`-Dgpg.skip=true`**). Release builds: **`-Dgpg.skip=false`**. Optional **`./build.sh`**, **`build.ps1`**.

## Dependencies

- Types: **`au.gov.nehta:mhr-wsdl`** at **`${project.version}`**.
- Shared libs: **`au.gov.nehta:smi-xsp`**, **`au.gov.nehta:smi-common-utils`**, and **`au.gov.nehta:common-library`** at **`${project.version}`** (**8.0.0**).
- Runtime SOAP stack: **`com.sun.xml.ws:jaxws-rt`** **2.3.7**.
- **`maven-enforcer-plugin`** rejects Metro **`webservices-*`** and **`jakarta.xml.bind-api`**, **`jakarta.xml.ws-api`**, **`jakarta.xml.soap-api`**, **`jakarta.jws-api`**.

## Local builds (unpublished artifacts)

Install matching siblings first (same Java line: **`8.0.0-SNAPSHOT`**):

```text
# in mhr-wsdl-java
mvn -B "-Dgpg.skip=true" clean install

# in smi-xsp-java
mvn -B "-Dgpg.skip=true" clean install

# in smi-common-utils-java
mvn -B "-Dgpg.skip=true" clean install

# in common-library-java
mvn -B "-Dgpg.skip=true" clean install

# in mhr-b2b-client-java
mvn -B "-Dgpg.skip=true" clean verify
```

Integrators using GA versions from Maven Central do not need a source checkout.

Maintainer notes: **MAINTAINERS.md**.

## Optional WSDL codegen

The default lifecycle does **not** run **`wsimport`**. To regenerate from **`wsdls/`**:

```text
cd wsdls
./sync-lib.ps1
ant -f build.xml generate-src
```

Apache Ant must be on **`PATH`**. **`WsdlsCodegenToolingTest`** (default Surefire) verifies **`wsdls/lib/provided/`** contains EE4J tooling and no Metro bundles.

## Repository hygiene

- **Do not commit** **`local.properties`**, real keystores, passwords, API tokens, or production URLs. See **SECURITY.md**.
- **Line endings:** the repository uses **LF** (see **`.gitattributes`**). On **Windows**, run **`git config core.autocrlf false`** in your clone before committing.