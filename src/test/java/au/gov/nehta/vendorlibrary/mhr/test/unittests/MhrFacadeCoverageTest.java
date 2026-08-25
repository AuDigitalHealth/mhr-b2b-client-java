package au.gov.nehta.vendorlibrary.mhr.test.unittests;

import au.gov.nehta.vendorlibrary.mhr.clients.common.Client;
import au.gov.nehta.vendorlibrary.mhr.clients.view.GetViewClient;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import jakarta.xml.ws.Service;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Locks facade and service coverage to the third-party B2B set in
 * mhr-b2b-client-dotnet README (15 logical interfaces, getView + 7 views).
 */
public class MhrFacadeCoverageTest {

    /**
     * Logical name from mhr-b2b-client-dotnet README -> Java facade.
     * findDocuments maps to GetDocumentListClient (RegistryStoredQuery).
     */
    private static final Map<String, String> DOT_NET_LOGICAL_TO_FACADE = Map.ofEntries(
            Map.entry("doesPCEHRExist",
                    "au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.DoesMHRExistClient"),
            Map.entry("gainPCEHRAccess",
                    "au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.GainMHRAccessClient"),
            Map.entry("uploadDocument",
                    "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.UploadDocumentClient"),
            Map.entry("retrieveDocument",
                    "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.GetDocumentClient"),
            Map.entry("uploadDocumentMetadata",
                    "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.UploadDocumentMetadataClient"),
            Map.entry("findDocuments",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetDocumentListClient"),
            Map.entry("removeDocument",
                    "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.RemoveDocumentClient"),
            Map.entry("getAuditView",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetAuditViewClient"),
            Map.entry("getChangeHistoryView",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetChangeHistoryViewClient"),
            Map.entry("getView",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetViewClient"),
            Map.entry("getTemplate",
                    "au.gov.nehta.vendorlibrary.mhr.clients.template.GetTemplateClient"),
            Map.entry("searchTemplate",
                    "au.gov.nehta.vendorlibrary.mhr.clients.template.SearchTemplateClient"),
            Map.entry("getIndividualDetailsView",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetIndividualDetailsViewClient"),
            Map.entry("getRepresentativeList",
                    "au.gov.nehta.vendorlibrary.mhr.clients.view.GetRepresentativeListClient"),
            Map.entry("registerPCEHR",
                    "au.gov.nehta.vendorlibrary.mhr.clients.registration.RegisterMHRClient"));

    private static final List<String> FACADE_NAMES = Arrays.asList(
            "au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.DoesMHRExistClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.GainMHRAccessClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.registration.RegisterMHRClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.UploadDocumentMetadataClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetDocumentListClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.UploadDocumentClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.GetDocumentClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.documentexchange.RemoveDocumentClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetViewClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetIndividualDetailsViewClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetRepresentativeListClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetAuditViewClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.view.GetChangeHistoryViewClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.template.GetTemplateClient",
            "au.gov.nehta.vendorlibrary.mhr.clients.template.SearchTemplateClient");

    private static final List<String> SERVICE_NAMES = Arrays.asList(
            "au.net.electronichealth.ns.mhr.b2b.svc.mhrprofile._1.MHRProfileService",
            "au.net.electronichealth.ns.mhr.svc.registermhr._2.RegisterMHRService",
            "ihe.iti.xds_b._2007.DocumentRegistryService",
            "ihe.iti.xds_b._2007.DocumentRepositoryService",
            "au.net.electronichealth.ns.mhr.svc.removedocument._1.RemoveDocumentService",
            "au.net.electronichealth.ns.mhr.svc.getauditview._1.GetAuditViewService",
            "au.net.electronichealth.ns.mhr.svc.getchangehistoryview._1.GetChangeHistoryViewService",
            "au.net.electronichealth.ns.mhr.svc.getindividualdetailsview._2.GetIndividualDetailsViewService",
            "au.net.electronichealth.ns.mhr.svc.getrepresentativelist._1.GetRepresentativeListService",
            "au.net.electronichealth.ns.mhr.svc.getview._1.GetViewService",
            "au.net.electronichealth.ns.tplt.svc.gettemplate._1.GetTemplateService",
            "au.net.electronichealth.ns.tplt.svc.searchtemplate._1.SearchTemplateService");

    private static final List<String> GET_VIEW_REQUEST_TYPES = Arrays.asList(
            "au.net.electronichealth.ns.mhr.xsd.interfaces.healthcheckscheduleview._1.HealthCheckScheduleView",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.medicareoverview._1.MedicareOverview",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.observationview._1.ObservationView",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.prescriptionanddispenseview._1.PrescriptionAndDispenseView",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.healthrecordoverview._1.HealthRecordOverView",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.diagnosticimagingreportview._1.DiagnosticImagingReportView",
            "au.net.electronichealth.ns.mhr.xsd.interfaces.pathologyreportview._1.PathologyReportView");

    @Test
    public void allMhrFacadesAreOnClasspath() throws Exception {
        Assert.assertEquals(15, FACADE_NAMES.size());

        for (String facadeName : FACADE_NAMES) {
            Class<?> facadeType = Class.forName(facadeName);
            Assert.assertTrue(facadeName + " must extend Client",
                    Client.class.isAssignableFrom(facadeType));
        }
    }

    @Test
    public void facadesCoverDotNetLogicalB2bSet() throws Exception {
        Assert.assertEquals(15, DOT_NET_LOGICAL_TO_FACADE.size());
        for (Map.Entry<String, String> entry : DOT_NET_LOGICAL_TO_FACADE.entrySet()) {
            Class<?> facadeType = Class.forName(entry.getValue());
            Assert.assertTrue(entry.getKey() + " -> " + entry.getValue(),
                    Client.class.isAssignableFrom(facadeType));
            Assert.assertTrue("orphan facade mapping " + entry.getValue(),
                    FACADE_NAMES.contains(entry.getValue()));
        }
    }

    @Test
    public void allMhrGeneratedServicesAreOnClasspath() throws Exception {
        Assert.assertEquals(12, SERVICE_NAMES.size());

        for (String serviceName : SERVICE_NAMES) {
            Class<?> serviceType = Class.forName(serviceName);
            Assert.assertTrue(serviceName + " must extend jakarta.xml.ws.Service",
                    Service.class.isAssignableFrom(serviceType));
        }
    }

    @Test
    public void getViewClientExposesSevenClinicalViews() throws Exception {
        Assert.assertEquals(7, GET_VIEW_REQUEST_TYPES.size());
        Map<Class<?>, Method> bySecondArg = new LinkedHashMap<>();
        for (Method method : GetViewClient.class.getDeclaredMethods()) {
            if (!"getView".equals(method.getName()) || method.getParameterCount() != 2) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (!MHRHeader.class.equals(params[0]) || Object.class.equals(params[1])) {
                continue;
            }
            bySecondArg.put(params[1], method);
        }
        for (String typeName : GET_VIEW_REQUEST_TYPES) {
            Class<?> viewType = Class.forName(typeName);
            Assert.assertTrue("GetViewClient missing getView(" + typeName + ")",
                    bySecondArg.containsKey(viewType));
        }
    }
}
