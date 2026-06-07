# ADHA third-party B2B scope (mhr-b2b-client-java)

This library implements My Health Record B2B SOAP services for **third-party software developers** under ADHA published specifications. It tracks the **12 `B2B_*` service WSDLs** vendored in this repository.

Authoritative ADHA documents (obtain current copies from the [Developer Centre](https://developer.digitalhealth.gov.au/)):

| Document | Reference | Relevance |
| --- | --- | --- |
| View Service Technical Service Specification | DH-3423 (v2.0, 2021) | `getView` view types |

---

## View clients (package `clients.view`)

Six facade classes cover record and clinical views. WSDL/XSD sources are under `wsdls/src/main/resources/`; generated types ship in **`au.gov.nehta:pcehr-compiled-wsdl`**.

| Facade | WSDL | Capability |
| --- | --- | --- |
| `GetViewClient` | `B2B_GetView` | Third-party clinical view types (see table below) |
| `GetIndividualDetailsViewClient` | `B2B_GetIndividualDetailsView` | Individual demographics and details held in myHR |
| `GetRepresentativeListClient` | `B2B_GetRepresentativeList` | Authorised representatives for an individual |
| `GetAuditViewClient` | `B2B_GetAuditView` | Audit trail for record access |
| `GetChangeHistoryViewClient` | `B2B_GetChangeHistoryView` | Change history for the record |
| `GetDocumentListClient` | `B2B_DocumentRegistry` | Document metadata list (XDS query; not a `getView` type) |

Samples: `src/sample/java/.../sample/view/`. Offline smoke tests: `ViewClientsSmokeTest`. Integration tests: `src/test/java/.../tests/view/` and `test/system/TestGetView.java` (run with **`-Pintegration`**).

---

## Supported `getView` types (`GetViewClient`)

These view payloads work with **`B2B_GetView`** / `GetViewClient` (View Service TSS v2.0 §4.2):

| View | Java request type | Notes |
| --- | --- | --- |
| Prescription and dispense | `PrescriptionAndDispenseView` | |
| Observation | `ObservationView` | |
| Health check schedule | `HealthCheckScheduleView` | |
| Medicare overview | `MedicareOverview` | |
| Pathology report | `PathologyReportView` | Typed response |
| Diagnostic imaging report | `DiagnosticImagingReportView` | Typed response |
| Health record overview | `HealthRecordOverView` | Use `versionNumber` `"1.1"` |

XSDs are under `wsdls/src/main/resources/schema/External/View/`. Each type has a typed overload on `GetViewClient`. Custom view types may be passed to `getView(PCEHRHeader, Object)` if you add bindings locally.

---

## Related repositories

| Repository | Role |
| --- | --- |
| `pcehr-compiled-wsdl-java` | `au.gov.nehta:pcehr-compiled-wsdl` — generated SOAP/JAXB types for this client |
| `hi-b2b-client-java` | HI only |
