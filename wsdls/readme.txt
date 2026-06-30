====================================
WSDL/XSD in this repository
====================================

PCEHR B2B WSDL and XSD live under:

    wsdls/src/main/resources/
        wsdl/External/     (12 B2B_* service WSDLs)
        schema/            (XSD includes)
        binding/           (GlobalBindings.jxb, per-WSDL *.jxb)
        src/main/java/     (DateAdapter, pcehr_override xmldsig types)

These files are tracked in Git (unlike HI's licensed MCA bundle). They are the
source of truth for regenerating generated SOAP/JAXB types.

====================================
Generated types (pcehr-compiled-wsdl-java)
====================================

Compile types (both profiles) come from:

    au.gov.nehta:pcehr-compiled-wsdl:${project.version}

Install from pcehr-compiled-wsdl-java (branch java-11-jakarta) before building
mhr-b2b-client-java.

Build (install pcehr-compiled-wsdl-java at same version first):
  mvn clean verify                 default — types from pcehr-compiled-wsdl JAR
  mvn -Pwsimport clean verify      + in-repo wsimport validation (12 B2B services)

Both builds compile facades against pcehr-compiled-wsdl. -Pwsimport also runs
wsimport and writes merged output to target/generated-sources/wsimport (validation
only; not on compile classpath). CI runs both paths. See README.md and MAINTAINERS.md.

====================================
Legacy Ant build (wsdls/build.xml)
====================================

wsdls/build.xml is a historical Ant wsimport helper. Prefer pcehr-compiled-wsdl-java
for codegen; use Maven for the client library.
