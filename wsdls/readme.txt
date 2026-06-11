====================================
PCEHR B2B WSDL module (wsdls/)
====================================

Canonical PCEHR B2B WSDL and XSD for this product live under:

  src/main/resources/wsdl/
  src/main/resources/schema/

They are part of the open-source repository (not separate ADHA-licensed
artefacts like Healthcare Identifiers contracts in hi-b2b-client-java).

====================================
Default build (mhr-b2b-client-java)
====================================

The main Maven build does not run wsimport. SOAP types come from Maven:

  au.gov.nehta:pcehr-compiled-wsdl  (pcehr.wsdl.version = project version)

See repository README.md and CONTRIBUTING.md.

====================================
Optional: Ant wsimport (maintainers)
====================================

To regenerate Java types locally from the in-repo WSDL tree:

  Prerequisites: JDK 8+, Apache Maven 3.6+, Apache Ant on PATH.

  1. Refresh tooling (after ee4j.jaxws.version bump):
       ./sync-lib.ps1
     or:
       mvn -B -f ee4j-jaxws-lib-pom.xml package

  2. Run wsimport:
       ant -f build.xml generate-src

Tooling JARs: lib/provided/ (Eclipse EE4J jaxws-tools + jaxws-rt +
ant-contrib — not legacy Metro webservices-*).

Keep ee4j.jaxws.version in ee4j-jaxws-lib-pom.xml aligned with
ee4j.jaxws.version in the repository root pom.xml.

Offline verification (no Ant run): WsdlsCodegenToolingTest in the main
client test suite.

Ant build output (gitignored): build/, tmp/, dist/, extern/.

====================================
Licensing
====================================

Copyright 2012 NEHTA. Copyright 2021-2026 ADHA.

Licensed under the Apache License, Version 2.0. See LICENSE.txt at the
repository root (and license.txt in this directory for legacy Ant packages).
