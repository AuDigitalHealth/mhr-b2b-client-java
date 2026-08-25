# Maintainer notes

**Audience:** people changing the **`mhr-b2b-client`** build, dependency coordinates, tests, or WSDL layout - not library integrators. Integrators should use **README.md**, published Javadoc, and **`pom.xml`** coordinates.

Paths are relative to the repository root (directory containing **`pom.xml`**). This Git repository is **`mhr-b2b-client-java`**; the Maven artifact id remains **`mhr-b2b-client`**.

## Versioning

The **first number** of **`<version>`** is the **Java SE** target of **this** client JAR.

| Maven version | Java SE |
| ------------- | ------- |
| **11.0.0**    | **11**  |
| **17.0.0**    | **17**  |
| **21.0.0**    | **21**  |
| **24.0.0**    | **24**  |

**Documentation convention:** README, CONTRIBUTING, CHANGELOG, and integrator-facing text use **version numbers only** - never Git branch names.

| Version    | Java | XML APIs                  | Facades          |
| ---------- | ---- | ------------------------- | ---------------- |
| **11.0.0** | 11   | **Jakarta** XML WS / Bind | **15** (MHR B2B) |
| **17.0.0** | 17   | **Jakarta** XML WS / Bind | **15** (MHR B2B) |
| **21.0.0** | 21   | **Jakarta** XML WS / Bind | **15** (MHR B2B) |
| **24.0.0** | 24   | **Jakarta** XML WS / Bind | **15** (MHR B2B) |

**Git branch mapping (maintainers / checkout only - do not use in integrator docs):**

| Version    | Official Git branch |
| ---------- | ------------------- |
| **11.0.0** | `java-11`           |
| **17.0.0** | `java-17`           |
| **21.0.0** | `java-21`           |
| **24.0.0** | `java-24`           |

Artifact id stays **`mhr-b2b-client`**; the version distinguishes the Java SE line. Pair **`mhr-wsdl`**, **`common-library`**, **`smi-xsp`**, and **`smi-common-utils`** at the **same** version and the **same** branch names on the matching sibling repositories.

On a given branch, **do not change the first number** of **`<version>`**. A new Java SE target is a **new branch**, not a bump on this one.

**This checkout (`21.0.0-SNAPSHOT`):** Java **21**, **Jakarta** facade clients, sibling **`au.gov.nehta`** libs at **`${project.version}`**, **15** facades (no **`wsimport`** in the default lifecycle). Stack and **`.github/workflows/ci.yml`** (branch **`java-21`**, JDK **21**, checkout **`mhr-wsdl-java`** **`java-21`**) below apply to **this line only**.

Java packages and type names use **`mhr`**. SOAP/XML namespaces, element names, and operation names stay the published B2B contract (**`PCEHRHeader`**, **`registerPCEHR`**, **`/pcehr/`** namespace URIs).

## Artifact

- **`au.gov.nehta:mhr-b2b-client`** - facade clients, TLS, signing.
- Types and classpath WSDL come from **[mhr-wsdl-java](https://github.com/AuDigitalHealth/mhr-wsdl-java)**.

## Layout

| Path                                            | Role                                                                            |
| ----------------------------------------------- | ------------------------------------------------------------------------------- |
| `src/main/java/au/gov/nehta/vendorlibrary/mhr/` | Facade clients                                                                  |
| `wsdls/`                                        | Canonical PCEHR B2B WSDL/XSD tree (kept in Git; not HI-style licensed material) |
| `src/sample/java/`                              | Sample sources (**`-Psample`**)                                                 |
| `src/test/java/`                                | Offline unit tests; **`-Pintegration`** widens Surefire                         |

See **`wsdls/readme.txt`**.

## Java / JAX stack (`21.0.0`)

- **`maven.compiler.release` 21**
- **`ee4j.jaxws.version`** - **`com.sun.xml.ws:jaxws-rt`** **4.0.5**. Exclude legacy **`webservices-rt`** from **`common-library`** and **`clinical-document-packaging-library`**.
- Sibling **`au.gov.nehta`** deps (**`mhr-wsdl`**, **`common-library`**, **`smi-xsp`**, **`smi-common-utils`**) at **`${project.version}`**; coordinate GA releases across those repositories.
- **`maven-enforcer-plugin`:** bans Metro **`webservices-*`** and legacy **`javax.xml.ws`**, **`javax.xml.bind`**, and **`javax.xml.soap`** APIs. Application code uses **Jakarta** XML APIs.
- **`maven-gpg-plugin`:** skipped unless **`-Dgpg.skip=false`**
- **`maven-javadoc-plugin`:** **`doclint=none`**, **`failOnWarnings=false`** (aligned with other Java **21** **`au.gov.nehta`** lines)
- Default package is a thin JAR; optional **`-Pfat-jar`** attaches classifier **`all`**.
- **`.github/workflows/ci.yml`:** GitHub Actions on **`java-21`**, JDK **21**; installs **`mhr-wsdl`** **`21.0.0-SNAPSHOT`** then **`verify`**

## Default tests

Surefire default **`includes`** run offline-safe tests only (**`JaxwsRuntimeSmokeTest`**, **`MhrWsdlArtifactSmokeTest`**, **`MhrFacadeCoverageTest`**, and other unit tests). **`MhrFacadeCoverageTest`** locks **15** facades and **getView** **7** clinical views against **mhr-b2b-client-dotnet**. **`MhrWsdlArtifactSmokeTest`** loads **`au.net.electronichealth.ns.mhr.b2b.svc.mhrprofile._1.MHRProfileService`**. **`integration`** profile widens to **`**/*Test.java`** for mutual-TLS suites with local keystores.

## Optional `wsdls/` Ant wsimport

The default **`mhr-b2b-client`** build uses **`mhr-wsdl`** from Maven. To regenerate SOAP types locally:

```text
cd wsdls
./sync-lib.ps1                    # or: mvn -B -f ee4j-jaxws-lib-pom.xml package
ant -f build.xml generate-src
```

Tooling lives in **`wsdls/lib/provided/`** (**`jaxws-tools`**, **`jaxws-rt`**, **`ant-contrib`** from Maven Central - **not** legacy Metro **`webservices-*`**). Keep updated JARs in Git after **`sync-lib`**. **`ee4j.jaxws.version`** in **`ee4j-jaxws-lib-pom.xml`** must match the root **`pom.xml`**.

**`WsdlsCodegenToolingTest`** (default Surefire) checks tooling JARs, WSDL tree presence, version alignment, and absence of Metro **`webservices-*`** filenames - no Ant run in CI.

## Contributors vs release publisher (`pom.xml`)

**Contributors (PRs, ordinary changes):** Do not change **`<version>`** (stay on **`-SNAPSHOT`** unless the maintainer requests a bump), **`<scm><tag>`**, or **`distributionManagement`**. If a maintainer requests a SNAPSHOT bump on this branch, change only the trailing numbers (**`21.0.1-SNAPSHOT`**), never the Java SE digit. Leave **`maven-gpg-plugin`** **`skip`** **`true`** so default **`mvn verify`** does not require a signing key. Record user-visible work under **`CHANGELOG.md`** in the **`= <pom-version> =`** block that matches **`pom.xml`** **`<version>`**.

**Release publisher:** In the release change set: set **`<version>`** to the GA coordinate (no **`-SNAPSHOT`**); set **`<scm><tag>`** to the Git tag you will publish (match existing tag naming). Move **`CHANGELOG.md`** bullets from the snapshot section into a new **`= <GA-version> =`** section; add a fresh **`-SNAPSHOT`** block for the next development cycle. Deploy via Sonatype Central Portal (**`central-publishing-maven-plugin`**; copy **`settings.xml.example`** -> **`settings.xml`**, server id **`central`**). See **Release** below.

## Release

Publishing uses **`central-publishing-maven-plugin`** (Sonatype Central Portal). Copy **`settings.xml.example`** -> **`settings.xml`**, server id **`central`**.

**Parallel release lines (maintainers only):** each Git branch of **this** repository publishes a **different Maven version** of **`mhr-b2b-client`**. Integrators choose by coordinate, not branch name. The first number of that version is the targeted Java SE version. Run **`release:prepare` / `release:perform`** (or manual deploy) **on that branch** (not detached HEAD).

| Branch        | Java         | `mhr-b2b-client` / siblings | Facades |
| ------------- | ------------ | --------------------------- | ------- |
| **`java-11`** | 11 / Jakarta | **11.0.0**                  | 15      |
| **`java-17`** | 17 / Jakarta | **17.0.0**                  | 15      |
| **`java-21`** | 21 / Jakarta | **21.0.0**                  | 15      |
| **`java-24`** | 24 / Jakarta | **24.0.0**                  | 15      |

**Order:** publish **`mhr-wsdl`** and other sibling **`au.gov.nehta`** libs first. This client cannot complete **`verify`** / **`release:perform`** until those coordinates are on Central (or installed locally).

**`-DdevelopmentVersion`:** keep the same first number as **`-DreleaseVersion`** (example on this line: **`21.0.0`** then **`21.0.1-SNAPSHOT`**).

### SNAPSHOT or manual GA

1. Update **CHANGELOG.md** (and **`pom.xml`** / SCM **`<tag>`** for manual GA).
2. **`mvn -B "-Prelease" clean verify`**
3. **`mvn -B "-Prelease" deploy`**

Git/SCM settings for **`maven-release-plugin`** live in **`pom.xml`** properties (**`scm.repo.url`**, **`release.*`**). Tags default to **`{artifactId}-{version}`** (e.g. **`mhr-b2b-client-21.0.0`**).

### Automated GA (`maven-release-plugin`)

Run on the **target branch** with a **clean** working tree. The plugin bumps versions, creates the release tag, deploys from the tag checkout, bumps to the next **`-SNAPSHOT`**, and updates the remote branch and tag (**`pushChanges`** / **`remoteTagging`** in **`pom.xml`**). Git remote credentials (SSH or HTTPS) must work non-interactively.

```text
mvn -B "-Prelease" release:prepare release:perform -DreleaseVersion=21.0.0 -DdevelopmentVersion=21.0.1-SNAPSHOT -Dtag=mhr-b2b-client-21.0.0
```

Replace **`-DreleaseVersion`**, **`-DdevelopmentVersion`**, and **`-Dtag`** for the branch you are on. Omit **`-D...`** only if you accept interactive prompts.

**After success:** confirm sibling libs then **`mhr-b2b-client`** GA on Central. If the plugin did not update the remote, send the release branch and the release tag from a local checkout.

**`-Dgpg.skip=false`** is equivalent to **`-Prelease`** for signing.

## Changelog and releases

**`CHANGELOG.md`** uses **`= version =`** section headers. Match the snapshot header to **`pom.xml`** **`<version>`** until the publisher cuts GA.

## New Java SE line

When adding a line (e.g. Java **25**): create **`java-25`** in **this** repository from the nearest existing client line; set **`<version>`** first number to **25** (e.g. **`25.0.0-SNAPSHOT`**); set **`maven.compiler.release`**, JAX-WS / JAXB coordinates, CI **`java-version`** / branch filter, and docs to that line. Do not retarget an existing branch.

The matching sibling **`au.gov.nehta`** lines must exist first.

## Public remote checklist

Before sending work to a **public** remote:

1. **`git status`** - no keystores, **`settings.xml`**, or **`local.properties`** staged.
2. No **`target/`** in the change set.
3. **`mvn -B "-Dgpg.skip=true" clean verify`** passes (with sibling libs installed or on Central).
4. **CHANGELOG.md** and **`pom.xml`** version reflect the release line.

## Copyright

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA. Apache License 2.0 - see **LICENSE.txt**.
