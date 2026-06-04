# ADHA third-party B2B scope (mhr-b2b-client-java)

This library implements My Health Record B2B SOAP services for **third-party software developers** under ADHA published specifications. It tracks the **12 `B2B_*` service WSDLs** vendored in this repository.

Authoritative ADHA documents (obtain current copies from the [Developer Centre](https://developer.digitalhealth.gov.au/)):

| Document | Reference | Relevance |
| --- | --- | --- |
| View Service Technical Service Specification | DH-3423 (v2.0, 2021) | `getView` view types |

---

## Supported `getView` types

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

XSDs are under `wsdls/src/main/resources/schema/External/`. Each type has a typed overload on `GetViewClient`. Custom view types may be passed to `getView(PCEHRHeader, Object)` if you add bindings locally.

---

## Related repositories

| Repository | Role |
| --- | --- |
| `mhr-wsdl-java` | Optional `mhr-wsdl` artifact; same schema set as this repo |
| `hi-b2b-client-java` | HI only |
