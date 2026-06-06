# Maintainer notes

**Audience:** people changing the build, dependency coordinates, tests, or WSDL layout.

## Branch scope (Java 8 / `javax`)

- **Goal:** Java **8** bytecode (`maven.compiler.release` **8**) with **`javax.xml.ws`** / JAXB in application code; SOAP types **`au.net.electronichealth.*`** come from **`au.gov.nehta:pcehr-compiled-wsdl`** at **`pcehr.wsdl.version`**. This artifact does **not** run **`wsimport`** in the default lifecycle.
- **`wsdls/`** holds the canonical PCEHR B2B WSDL/XSD tree (committed in Git — not HI-style licensed material). See **`wsdls/readme.txt`**.

## Java / JAX stack

- **`ee4j.jaxws.version`** — **`com.sun.xml.ws:jaxws-rt`** **2.3.7** (last **2.3.x** for Java 8). Exclude legacy **`webservices-rt`** from **`common-library`** and **`clinical-document-packaging-library`** only.
- **`pcehr.wsdl.version`** — **`${project.version}`**; coordinate GA releases with **`pcehr-compiled-wsdl-java`**.
- **Enforcer** bans **`com.sun.xml.ws:webservices-*`** (Metro bundle), not **`javax.*`** API coordinates required on this branch.

## Default tests

Surefire default **`includes`** run offline-safe tests only. **`integration`** profile widens to **`**/*Test.java`** for mutual-TLS suites with local keystores.

## Optional `wsdls/` Ant wsimport

The default **`mhr-b2b-client`** build uses **`pcehr-compiled-wsdl`** from Maven. To regenerate SOAP types locally:

```text
cd wsdls
./sync-lib.ps1                    # or: mvn -B -f ee4j-jaxws-lib-pom.xml package
ant -f build.xml generate-src
```

Tooling lives in **`wsdls/lib/provided/`** (**`jaxws-tools`**, **`jaxws-rt`**, **`ant-contrib`** from Maven Central — **not** legacy Metro **`webservices-*`**). Commit updated JARs after **`sync-lib`**. **`ee4j.jaxws.version`** in **`ee4j-jaxws-lib-pom.xml`** must match the root **`pom.xml`**.

**`WsdlsCodegenToolingTest`** (default Surefire) checks tooling JARs, WSDL tree presence, version alignment, and absence of Metro **`webservices-*`** filenames — no Ant run in CI.

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Licensed under Apache 2.0 — see **LICENSE.txt**.
