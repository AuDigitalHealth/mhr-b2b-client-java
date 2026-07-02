# Maintainer notes

**Audience:** people changing the build, dependency coordinates, tests, or WSDL layout.

## Branch scope (Java 8 / `javax`)

- **Goal:** Java **8** bytecode (`maven.compiler.release` **8**) with **`javax.xml.ws`** / JAXB in application code; SOAP types **`au.net.electronichealth.*`** come from **`au.gov.nehta:pcehr-compiled-wsdl`** at **`pcehr.wsdl.version`**. This artifact does **not** run **`wsimport`** in the default lifecycle.
- **`wsdls/`** holds the canonical PCEHR B2B WSDL/XSD tree (committed in Git — not HI-style licensed material). See **`wsdls/readme.txt`**.

## Java / JAX stack

- **`ee4j.jaxws.version`** — **`com.sun.xml.ws:jaxws-rt`** **2.3.7** (last **2.3.x** for Java 8). Exclude legacy **`webservices-rt`** from **`common-library`** and **`clinical-document-packaging-library`** only.
- **`pcehr.wsdl.version`** — **`${project.version}`**; coordinate GA releases with **`pcehr-compiled-wsdl-java`**.
- **Enforcer** bans **`com.sun.xml.ws:webservices-*`** (Metro bundle). Application code uses **`javax.*`** only; EE4J **`jaxws-rt` 2.3.x** may transitively pull javax-bridge **`jakarta.*` API** JARs — that is expected on this line.

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

## Release

Publishing uses **`central-publishing-maven-plugin`** (Sonatype Central Portal). Copy **`settings.xml.example`** → **`settings.xml`**, server id **`central`**.

**Parallel release lines (maintainers only):** each Git branch publishes a **different Maven version** — integrators choose by coordinate, not branch name.

| Branch | Java | MHR client / WSDL version | Facades |
| ------ | ---- | ------------------------- | ------- |
| **`java-8-javax-full-wsdl`** | 8 / javax | **1.6.3** | 15 |
| **`java-11-jakarta-full-wsdl`** | 11 / Jakarta | **1.7.0** | 26 |

Release **`pcehr-compiled-wsdl`** and **`mhr-b2b-client`** at the **same GA version** on the matching branch pair before integrators upgrade.

### SNAPSHOT or manual GA

1. Update **CHANGELOG.md** (and **`pom.xml`** / SCM **`<tag>`** for manual GA).
2. **`mvn -B "-Prelease" clean verify`**
3. **`mvn -B "-Prelease" deploy`**

Git/SCM settings for **`maven-release-plugin`** live in **`pom.xml`** properties (**`scm.repo.url`**, **`release.*`**). Tags default to **`{artifactId}-{version}`** (e.g. **`mhr-b2b-client-1.7.0`**).

### Automated GA (`maven-release-plugin`)

Run on the **target branch** with a **clean** working tree. The plugin commits version bumps, creates the release tag, deploys from the tag checkout, bumps to the next **`-SNAPSHOT`**, and **pushes branch + tag** (**`pushChanges`** / **`remoteTagging`** in **`pom.xml`**). Git remote credentials (SSH or HTTPS) must work non-interactively.

```text
mvn -B "-Prelease" release:prepare release:perform -DreleaseVersion=1.7.0 -DdevelopmentVersion=1.7.1-SNAPSHOT -Dtag=mhr-b2b-client-1.7.0
```

Replace versions and **`-Dtag`** for the branch you are on (**`pcehr-compiled-wsdl-1.6.3`**, **`mhr-b2b-client-1.6.3`**, etc.). Omit **`-D…`** only if you accept interactive prompts.

**After success:** confirm the artifact on Central; repeat on the paired types/client repo. No extra Git steps unless push failed (then **`git push origin <branch>`** and **`git push origin <tag>`**).

**`-Dgpg.skip=false`** is equivalent to **`-Prelease`** for signing.

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Licensed under Apache 2.0 — see **LICENSE.txt**.
