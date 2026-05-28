# Maintainer notes

**Audience:** people changing the build, dependency coordinates, tests, or WSDL layout. Integrators use **README.md** and published Javadoc. Contributors start with **CONTRIBUTING.md**.

## Java / Jakarta stack (`pom.xml` properties)

- **`maven.compiler.release` 11** — language level and bytecode.
- **`jaxws.rt.version`** — Eclipse EE4J **`com.sun.xml.ws:jaxws-rt`** and the **`jaxws-tools`** version on **`jaxws-maven-plugin`**. **`jaxws.maven.plugin.version`** stays at **4.0.2** on JDK 11 (newer plugin releases may require JDK 17 to run **`wsimport`**).
- **`groovy.version`** — **`gmavenplus-plugin`** merge script only (not published with the JAR).
- **`mhr.wsdl.codegen.skip`** — controls **`wsimport`**, enforcer, and merge execution (default is **`false`**).
- **`mhr.wsdl.dir`** — **`wsdls/src/main/resources/wsdl/External`**
- **`mhr.binding.dir`** — **`wsdls/src/main/resources/binding`**

Runtime stack is **`jaxws-rt`**, not legacy Metro **`webservices-rt`**.

## Generated SOAP types (`wsimport`)

- **12** **`jaxws-maven-plugin`** executions (one per primary **`B2B_*.wsdl`** in **`pom.xml`**). **`GetDocumentListClient`** uses types from **Document Registry** wsimport.
- Each execution writes to **`target/generated-sources/wsimport-parts/<id>/`**.
- **`gmavenplus-plugin`** (**`process-sources`**) merges parts into **`target/generated-sources/wsimport`** (first file wins) and removes **`wsimport-parts`** from compile source roots.
- **`build-helper-maven-plugin`** adds **`target/generated-sources/wsimport`** and **`wsdls/src/main/java`** ( **`DateAdapter`** ).
- **`maven-enforcer-plugin`** (**`initialize`**) requires **`${mhr.wsdl.dir}/B2B_PCEHRProfile.wsdl`** unless codegen is skipped.

When adding a WSDL execution, update **`scripts/generate-pom.py`** if that script owns the wsimport block, and extend the merge **`names`** list in **`pom.xml`**.

## JAXB bindings

- **`GlobalBindings.jxb`**: **`xjc:simple`**, **`DateAdapter`** for **`xsd:date`** → **`java.util.Calendar`**.
- **`PCEHR_CommonTypes.xsd.jxb`**: typesafe enums for common PCEHR types.
- Per-WSDL **`B2B_*.wsdl.jxb`**: e.g. xmldsig package override on Document Repository.

Hand-written XDS helpers (**`XDSMapper`**, **`XDSFactory`**) must use generated list accessors **`getSlot()`**, **`getClassification()`**, **`getExternalIdentifier()`** (not plural `getSlots()` etc.).

## Profiles

| Profile | Purpose |
| ------- | ------- |
| **`integration`** | **`skipTests=false`**, all `**/*Test.java` |
| **`sample`** | Adds **`src/sample/java`** at **`generate-sources`** |

## `META-INF/metro.xml`

JAX-WS RI tubeline configuration only; not a separate Metro Maven dependency.

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Apache License 2.0 — see **LICENSE.txt**.
