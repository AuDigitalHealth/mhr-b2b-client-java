# Change Log/Revision History

= 1.1.7 =
=========
- Converted to Maven

= 1.1.6 =
=========
- updated WSDL jar to 2.4.4, changes to HRO response object.
- updated FormatCodes.java and DocumentTypeCodes.java (version staying the same)

= 1.1.5 =
=========
- updated WSDL jar to 2.4.3, changes to view Common Types => streeNameType > streetNameType
- removed AvancedCarePlaning and AchievmentDiary view

= 1.1.4 =
=========
- Updated "Advanced Care Directive Custodian Record" display name

= 1.1.3 =
=========
- Updated PCEHR wsdl jar

= 1.1.2 =
=========
- Aligned document type code and class code display name with NeHTA C# client for XDSMetadata - upload document

= 1.1.1 =
=========
- Updated PCEHR wsdl jar to nehta-vendorlibrary-java-pcehr-compiled-wsdl-2.4.1.jar
- added new DocumentTypeCodes for Pathology and DI
- modified XDS Metadata service start stop time for pathology/di

= 1.1.0 =
=========
- updates to the nehta-smi-xsp and nehta-vendorlibrary-common jars
- Updated all clients to replace CertificateVerifier with CertificateValidator
  this is a backwards compatible change in line with the smi-xsp jar
- added metro.xml in META-INF to support metro 2.3 and override WSIT tube
- added GetPathologyView
- added GetDiagnosticView
- added getHROView

= 1.0.1 =
=========
- changed slot name for class code queries to $XDSDocumentEntryClassCode.

= 1.0.0 =
=========
- Removed document title from XDS metadata for Document Upload

= 0.7.12 (DRAFT) =
=========
- Changed wsdl reference to updated 2.3.0 libraries with XMLDSIG overrides
- Changed NeHTA common jar to 1.0.4

= 0.7.11 (DRAFT) =
=========
- Changed getDocumentList client, client encodes CodedValues with the carrot (^) delimiter now.
- Changed DocumentTypeCodes coding system name to align with C# lang Impl
- Changed DocumentQueryParams.java
  - TemplateId is now a CodedValue
  - Added DocumentEventClass Query
  NB:this release is backward-compatible with Java 6.

= 0.7.10 (DRAFT) =
=========
- Changed MTOM handling to allow for optional Register Document
  NB:this release is backward-compatible with Java 6.



= 0.7.9 (DRAFT) =
=========
- Added utility function in UploadDocumentClient to include respository ID, hash and size information
  in the XDS metadata (for NPDR only, as the  repository is deficient in adding these additional fields when
  registering the document on the PCEHR).
- changed instances of "Summarisation" to "Summarization" as in "Discharge Summarisation Note" and
  "Summarization of episode note"

  NB:this release is backward-compatible with Java 6.

= 0.7.8 (DRAFT) =
=========
- Modifications To RegisterPCEHRClient and Client.javato fix a bug with MTOM.
  NB:this release is backward-compatible with Java 6.

= 0.7.7 (DRAFT) =
=========
- generic Object type for getView client request
  NB:this release is backward-compatible with Java 6.

= 0.7.6 (DRAFT) =
=========
- added default language code
- no empty document hashes
- NB:this release is backward-compatible with Java 6.

= 0.7.5 (DRAFT) =
=========
- bug fix for NPE on documentHash
- NB:this release is backward-compatible with Java 6.

= 0.7.4 (DRAFT) =
=========
- added ObservationView capability to the getView client
- modified XDS metadata location for prescription organisation author
  NB:this release is backward-compatible with Java 6.

= 0.7.3 (DRAFT) =
=========
- updated compiled wsdl jar to vesrsion 2.2.4 which fixes a bug with xml binding to an xsi:anyType on Health Check Schedule VSiew.
  NB:this release is backward-compatible with Java 6.

= 0.7.2 (DRAFT) =
=========
- Modified PCEHR client for new view types using xsi:anyType
  NB:this release is backward-compatible with Java 6.

= 0.7.1.6 (DRAFT) =
=========
- This is a special rebuild of 0.7.1 for vendors using Java 6 beyond its End Of Life date.
  see:  http://www.oracle.com/technetwork/java/eol-135779.html

= 0.7.1 (DRAFT) =
=========
- Added class codes for PCEHR Prescription Record and PCEHR Dispense Record.
- Updated GetViewClient to use MTOM binding by default.
- Updated GetView wsdl class to include a "view" wrapper on the view objects (elements).
- Updated GetViewClient to support the following views:
    1. PrescriptionAndDispenseView
    2. MedicareOverview
    3. HealthCheckScheduleView
- Updated XdsMetadata class to use "DateTime Prescription Written" as Service Start Time and Service End Time for PCEHR Prescription Records.
- Updated XdsMetadata class to use "DateTime of Dispense Event" as Service Start Time and Service End Time for PCEHR Dispense Records.
- Updated eSignature libraries to v1.1
- Updated clinicalPackage libraries to v1.2.2

= 0.7.0 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
! Allowable client properties are dependent on the JAX-WS implementation in use. The vendor libraries provided are compiled against the JAX-WS
reference implementation provided with Java 6. Should you require a different version or be using the endorsed standards override mechanism, the
PCEHR client libraries and properties will need to be updated and re-compiled accordingly.

Change History:
- Added new view type GetObservationalView
- Removed getConstolidatedView

= 0.6.9 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
! Allowable client properties are dependent on the JAX-WS implementation in use. The vendor libraries provided are compiled against the JAX-WS
reference implementation provided with Java 6. Should you require a different version or be using the endorsed standards override mechanism, the
PCEHR client libraries and properties will need to be updated and re-compiled accordingly.

Change History:
- Added new format codes for PCEHR Prescription Record and PCEHR Dispense Record.
- Added "authorSpecialty" into XDS metadata for both UploadDocumentClient and UploadDocumentMetadataClient.
- Updated class code for ePrescription and Dispense Record.

= 0.6.8 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
! Allowable client properties are dependent on the JAX-WS implementation in use. The vendor libraries provided are compiled against the JAX-WS
reference implementation provided with Java 6. Should you require a different version or be using the endorsed standards override mechanism, the
PCEHR client libraries and properties will need to be updated and re-compiled accordingly.

Change History:
- Updated to reflect changes in PCEHR WSDLs. New sample clients for getRepresentativeList,  getIndividualDetailsView, and getView web services.

= 0.6.7 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
! Allowable client properties are dependent on the JAX-WS implementation in use. The vendor libraries provided are compiled against the JAX-WS
reference implementation provided with Java 6. Should you require a different version or be using the endorsed standards override mechanism, the
PCEHR client libraries and properties will need to be updated and re-compiled accordingly.

Change History:
- Updated MetadataUtils.toDocumentMetadata() to allow for PAI-D and LocalSystemIdentifier XCN Author IDs

= 0.6.6 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
! Allowable client properties are dependent on the JAX-WS implementation in use. The vendor libraries provided are compiled against the JAX-WS
reference implementation provided with Java 6. Should you require a different version or be using the endorsed standards override mechanism, the
PCEHR client libraries and properties will need to be updated and re-compiled accordingly.

Change History:
- Enhancement to allow client properties to be set using 'setProperty' method. For instance:
  DoesPCEHRExistClient client = ... // constructor call
  client.setProperty(JAXWSProperties.CONNECT_TIMEOUT, 180000); // Timeout of three minutes.
- Abstraction of client code to improve manageability.
- Accessor method for 'Client' port.

= 0.6.5 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
- 'GetDocument' SOAP response from SVT does not comply with DEXS-T 109 of the PCEHR Document Exchange Service Technical Service Specification. This is
  in the process of being corrected.
- 'UploadDocument' SOAP request is being incorrectly rejected by the SVT as having an invalid signature. This is in the process of being corrected.

Change History:
- Added new 'MTOMHandler.java' SOAPHandler to address JAX-WS limitations. This applies to 'UploadDocument'. Without this additional functionality,
  the request is limited to a single SOAP envelope that contains the Base64-encoded document, inline. This is due to SOAPHandlers forcing the content
  back into the original SOAP envelope. The new handler manually splits out the content into a new MTOM MIME part and injects an XOP reference to that
  new part in the SOAP envelope, in place of the old Base64 text.
- Added 'MTOMHandler' to client handler chain.
- Split out common handler functionality into new 'HandlerUtils.java'.
- Added a new namespace for XOP in 'XMLNamespaces.java'.
- Updated 'SecurityHandler.java' to make use of the common methods in 'HandlerUtils.java'.
- Removed skipping of the 'GetDocument' operation response verification check in 'SecurityHandler.java' (see version 0.6.4 release notes for further
  detail).

= 0.6.4 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
- 'GetDocument' SOAP response from SVT does not comply with DEXS-T 109 of the PCEHR Document Exchange Service Technical Service Specification.

Change History:
- Added new enum value to 'DocumentStatus' => 'urn:ordreq:names:statusType:Deleted', as documented in the PCEHR Implementation Guide.

= 0.6.3 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Known Limitations:
- 'GetDocument' SOAP response from SVT does not comply with DEXS-T 109 of the PCEHR Document Exchange Service Technical Service Specification.

Change History:
- New 'SecurityHandler' code to partially skip validation of the GetDocument SOAP response. This code is as follows:

  /** Start DEXS-T 109 Ignore **/
  if (isGetDocumentResponse(context)) {
  return;
  }
  /** End DEXS-T 109 Ignore **/

  If this functionality is undesired, remove/comment out the above lines. Signature verification will then fail for that response, with the handling
  of the exception and response left to the vendor.

= 0.6.2 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.

Pre-Requisites:
- SVT 5.11 (or greater) environment required for support of:
    1) CDA document <id> root value of type UUID in document uploads.
    2) Signature verification
    3) 'GetRepresentativeList' and 'RegisterPCEHR' clients.

Change History:
- Fix for 'UploadDocument' submission set metadata association for document replacement.
- Fix metadata value mismatch for 'XDSDocumentEntry.typeCodeDisplayName'.
- Updated 'FormatCodes' values to reflect current environment values. 'CodedValue' types can be passed
  to the 'UploadDocument*' clients in the event that the 'FormatCodes' are insufficient or incorrect.
- Fix to 'UploadDocument' time-based metadata population (refer to 'XPathExpressions' for the corresponding XPaths for the following rules):
    1) DocumentMetadata.ServiceStartTime = [XPathExpressions.SERVICE_START_TIME|XPathExpressions.SERVICE_FIXED_TIME|XPathExpressions.CREATION_TIME]
    2) DocumentMetadata.ServiceStopTime = [XPathExpressions.SERVICE_STOP_TIME|XPathExpressions.SERVICE_FIXED_TIME|XPathExpressions.CREATION_TIME]
       The value of each time is populated from a single XPath result, from right to left, in order of precision.

= 0.6.1 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.
This release works against the current interfaces.

Pre-Requisites:
- SVT 5.11 (or greater) environment required for support of:
    1) CDA document <id> root value of type UUID in document uploads.
    2) Signature verification
    3) 'GetRepresentativeList' and 'RegisterPCEHR' clients.

Change History:
- Modified 'RegisterPCEHR' sample code to provide an correct example for:
    1) registering a PCEHR with an IHI (child-specific sample code, with parent assertion details)
    2) registering a PCEHR with demographics (individual-specific code)
- Inclusion of generated code JAR files supporting the deprecation of the 'GetIndividualDetails'
  operation:
    1) nehta-vendorlibrary-java-pcehr-compiled-wsdl-2.1.0-docs.jar
    2) nehta-vendorlibrary-java-pcehr-compiled-wsdl-2.1.0-sources.jar
    3) nehta-vendorlibrary-java-pcehr-compiled-wsdl-2.1.0.jar

= 0.6.0 (DRAFT) =
=========
This is a draft release of sample code for the PCEHR interfaces.
This release works against the current interfaces.

Pre-Requisites:
- SVT 5.11 (or greater) environment required for support of:
    1) CDA document <id> root value of type UUID in document uploads.
    2) Signature verification
    3) 'GetRepresentativeList' and 'RegisterPCEHR' clients.

Change History:
- Abstraction of some common client behaviour to improve manageability.
- Updated 'license.txt' with new license text for vendor library usage.
- Added 'RegisterPCEHR' client and sample code.
- Added 'GetRepresentativeList' client and sample code.
- Added response signature verification to 'SecurityHandler', utilising XSP libraries. For further detail
  on the verification process, please see the '/lib/provided/nehta-smi-xsp-1.0.3*.jar' libraries.
- Added a constructor to clients that accepts an additional 'CertificateVerifier' object
  parameter. If certificate verification is not desired, the original constructor should be used. This
  constructor now passes an empty 'CertificateVerifier' implementation parameter.
- Added 'MinimalCertificateVerifier' implementation. This is an optional verifier that can be passed
  to client constructors. If this is not supplied, no certificate verification will occur.
- Added 'CertificateVerifierUsageSample' to show how a 'CertificateVerifier' instance would be supplied
  to a client.

= 0.5 (DRAFT) =
=========
This is a draft release of sample code the PCEHR interfaces.
This release works against the current interfaces.

Known Issues:
- CDA document <id> root value must be an OID, for document uploads. Other
  operations may accept both in situations where the SVT environment contains
  valid documents with UUIDs.

Change History:
- UTC times supported for 'SubmitObjectsRequest' times. These values are extracted from the CDA root
  document and support the following formats defined in the 'DateParsePatterns' enum. Note that all
  UTC times will conform to the following format:
  YYYY[MM[DD[hh[mm[ss]]]]]
  This means that all timezones, if present will be applied in the conversion and any fractional second
  precision will be dropped from the date/time string.
- Added 'UploadDocumentMetadataClient' support and associated 'UploadDocumentMetadataSample' code.
- 'service[Start/Stop]Time' for 'SubmitObjectRequests' are now derived from the CDA root document,
  if present at the following locations:
  serviceStartTime = '/ClinicalDocument/componentOf/encompassingEncounter/effectiveTime/low/@value'
  serviceStopTime = '/ClinicalDocument/componentOf/encompassingEncounter/effectiveTime/high/@value'

= 0.4 (DRAFT) =
=========
This is a draft release of sample code the PCEHR interfaces.
This release works against the current interfaces.

Known Issues:
- CDA document <id> root value must be an OID, for document uploads. Other
  operations may accept both in situations where the SVT environment contains
  valid documents with UUIDs.
- UploadDocumentMetadata operations are not supported in this release.

Change History:
- Updated template FormatCodes to reflect the current values supported in
  the SVT environment.
- Added a helper class 'DocumentUtils' which provides a method to update the ID
  in a CDA document, in memory. This is useful when testing multiple document
  uploads as it bypasses the need to manually edit the XML file by hand.
- Added an additional method for the 'UploadDocumentClient' to allow a document
  to be replaced.

= 0.3 (DRAFT) =
=========
This is a draft release of sample code the PCEHR interfaces.
This release works against the current interfaces.

Known Issues:
- CDA documents <id> root value must be an OID.
- UploadDocumentMetadata operations are not supported in this release.

Change History:
- Updated Template FormatCodes ID values to match current test IDs.
- Included additional enum entries for Template FormatCodes. This set
  provides the final template ID values to be used once the test environment
  is transitioned to these.
- Fixed bug in date conversion for GetAuditView client.
- GetDocumentClient's use of MTOM permantently set. Constructor param
  to allow configuration of this setting has been removed.
- UploadDocumentClient's use of MTOM permantently set. Constructor param
  to allow configuration of this setting has been removed.
- Fixed build issue where xmlsec-<version>.jar was not being included amongst
  provided libraries, causing sample code runtime failures.

= 0.2 (DRAFT) =
=========
This is a draft release of sample code the PCEHR interfaces.
This release works against the current interfaces.

Known issues include:
1) CDA documents <id> root value must be an OID.
2) UploadDocumentMetadata operations are not supported in this release.

= 0.1 (DRAFT) =
=========
This is a draft release of sample code the PCEHR interfaces.
This release works against the current interfaces.

Known issues include:
1)	MTOM does not work with uploadDocument, only retrieveDocument
2)	CDA documents must follow older IGs and the <id> root value must be an OID
3)	getAuditView has a deserialization issue
4)  UploadDocument and UploadDocumentMetadata operations are not supported in
    this release.
