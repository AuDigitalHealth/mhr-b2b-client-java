# Third-party B2B scope

This library (**`au.gov.nehta:mhr-b2b-client`**, Maven line **17.0.0**, Java **17** / **Jakarta**) implements the **vendor-facing My Health Record B2B** interfaces listed by **mhr-b2b-client-dotnet** (logical names below). Wire SOAP still uses published **`pcehr`** namespaces and operation names; Java packages and type names use **`mhr`**.

## In scope (full)

| Logical name (.NET README) | Java facade                      | SOAP / IHE backing                      |
| -------------------------- | -------------------------------- | --------------------------------------- |
| doesPCEHRExist             | `DoesMHRExistClient`             | MHR profile                             |
| gainPCEHRAccess            | `GainMHRAccessClient`            | MHR profile                             |
| uploadDocument             | `UploadDocumentClient`           | Document repository ProvideAndRegister  |
| retrieveDocument           | `GetDocumentClient`              | Document repository RetrieveDocumentSet |
| uploadDocumentMetadata     | `UploadDocumentMetadataClient`   | Document registry RegisterDocumentSet-b |
| findDocuments              | `GetDocumentListClient`          | Document registry RegistryStoredQuery   |
| removeDocument             | `RemoveDocumentClient`           | removeDocument                          |
| getAuditView               | `GetAuditViewClient`             | getAuditView                            |
| getChangeHistoryView       | `GetChangeHistoryViewClient`     | getChangeHistoryView                    |
| getView                    | `GetViewClient`                  | getView + 7 clinical views              |
| getTemplate                | `GetTemplateClient`              | getTemplate                             |
| searchTemplate             | `SearchTemplateClient`           | searchTemplate                          |
| getIndividualDetailsView   | `GetIndividualDetailsViewClient` | getIndividualDetailsView                |
| getRepresentativeList      | `GetRepresentativeListClient`    | getRepresentativeList                   |
| registerPCEHR              | `RegisterMHRClient`              | registerPCEHR                           |

### getView clinical views (7)

- healthCheckScheduleView
- medicareOverview
- observationView
- prescriptionAndDispenseView
- healthRecordOverview
- diagnosticImagingReportView
- pathologyReportView

Generated request types and facade overloads live in **`mhr-wsdl`** / **`GetViewClient`**. Offline tests **`MhrFacadeCoverageTest`** and **`MhrWsdlArtifactSmokeTest`** lock this set (**15** facades, **7** getView clinical views).

Types and classpath WSDL for the 12 SOAP services are published as **`au.gov.nehta:mhr-wsdl`** at the same Maven version.

## Out of scope

- **Advance Care Planning** view (NPP / non-vendor packs; not in the .NET third-party README list).
- **Achievement Diary** view (removed from the vendor B2B pack; see CHANGELOG **1.1.5**).

Do not treat commented historical snippets in older source as a gap. Those views are intentionally absent.
