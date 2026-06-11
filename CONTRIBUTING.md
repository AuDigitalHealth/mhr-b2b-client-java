# Contributing

**Audience:** people submitting patches or running this repository's CI locally.

- **Build:** **JDK 8** and **Apache Maven 3.6.3+**. From the repo root: **`mvn -B -Dgpg.skip=true clean verify`**. Optional faster local **`verify`** without Javadoc: **`mvn -B -Pdev-javadoc-off -Dgpg.skip=true clean verify`**. Integration tests: **`mvn -B -Pintegration -Dgpg.skip=true clean test`**. Sample compile: **`mvn -B -Psample -Dgpg.skip=true -DskipTests=true clean compile`**. Shaded JAR: **`mvn -B -Pfat-jar -Dgpg.skip=true clean verify`**. Optional **`./build.sh`**, **`build.ps1`**.
- **Dependencies:** **`au.gov.nehta:pcehr-compiled-wsdl`** at **`pcehr.wsdl.version`** = **`${project.version}`** (**`1.6.3-SNAPSHOT`** during development). **Local SNAPSHOT:** **`mvn install`** in **[pcehr-compiled-wsdl-java](https://github.com/AuDigitalHealth/pcehr-compiled-wsdl-java)** at the **matching version** before **`verify`** here.
- **CI:** **`.github/workflows/ci.yml`** installs **`pcehr-compiled-wsdl-java`** then runs **`verify`** on **Temurin 8**.
- **Do not commit:** **`local.properties`**, real keystores, passwords, API tokens, or production URLs. See **`SECURITY.md`**.
- **Line endings:** configure Git **`core.autocrlf false`** on Windows before committing.
- **Optional WSDL codegen:** **`cd wsdls`**, **`./sync-lib.ps1`**, then **`ant -f build.xml generate-src`** (Apache Ant on PATH; see **`MAINTAINERS.md`**). **`WsdlsCodegenToolingTest`** (default Surefire) verifies **`wsdls/lib/provided/`** contains EE4J tooling and no Metro bundles.
- **Internals:** **`MAINTAINERS.md`**.
