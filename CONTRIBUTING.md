# Contributing

**Audience:** developers building or changing **this repository**. Integrators should use **README.md** and Maven Central coordinates. See **SECURITY.md** before committing.

## Prerequisites

- **JDK 17** with **`JAVA_HOME`** set (see **`maven.compiler.release`** in **`pom.xml`**).
- **Maven 3.6.3+** on **`PATH`**.

Dependencies resolve from **[Maven Central](https://central.sonatype.com/)** unless you are installing a **local SNAPSHOT** (below).

## Versioning

The **first number** of **`au.gov.nehta:mhr-b2b-client`** is the **Java SE** version that this client targets. **17.0.0** is the Java 17 Jakarta line with **15** facades. See **`README.md`**.

**`mhr-b2b-client`** pins sibling **`au.gov.nehta`** libs (**`mhr-wsdl`**, **`common-library`**, **`smi-xsp`**, **`smi-common-utils`**) to **`${project.version}`**. Use the **same** version for those artifacts.

## Build

From the project root, after sibling libs are resolvable:

```text
mvn -B "-Dgpg.skip=true" clean verify
```

| Goal                             | Command                                                            |
| -------------------------------- | ------------------------------------------------------------------ |
| Compile + attach sources/Javadoc | `mvn -B "-Dgpg.skip=true" clean verify`                            |
| Skip tests                       | `mvn -B "-Dgpg.skip=true" clean verify "-DskipTests=true"`         |
| Skip Javadoc                     | `mvn -B -Pdev-javadoc-off "-Dgpg.skip=true" clean verify`          |
| Integration tests                | `mvn -B -Pintegration "-Dgpg.skip=true" clean test`                |
| Sample compile                   | `mvn -B -Psample "-Dgpg.skip=true" -DskipTests=true clean compile` |
| Shaded JAR                       | `mvn -B -Pfat-jar "-Dgpg.skip=true" clean verify`                  |

GPG signing is skipped by default (**`-Dgpg.skip=true`**). Release builds: **`-Dgpg.skip=false`**. Optional **`./build.sh`**, **`build.ps1`**.

## Dependencies

- Types: **`au.gov.nehta:mhr-wsdl`** at **`${project.version}`**.
- Shared libs: **`common-library`**, **`smi-xsp`**, **`smi-common-utils`** at **`${project.version}`**.
- Runtime SOAP stack: **`com.sun.xml.ws:jaxws-rt`** **4.0.5**.
- **`maven-enforcer-plugin`** rejects Metro **`webservices-*`** and legacy **`javax.xml.ws`**, **`javax.xml.bind`**, and **`javax.xml.soap`** APIs.

## Local builds (unpublished artifacts)

Install matching sibling **`au.gov.nehta`** libs first (same Java line: **`17.0.0-SNAPSHOT`**):

```text
# in common-library-java / smi-xsp-java / smi-common-utils-java / mhr-wsdl-java
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
