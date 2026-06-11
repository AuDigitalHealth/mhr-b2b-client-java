package au.gov.nehta.vendorlibrary.pcehr.test.unittests.views;

import static org.junit.Assert.assertTrue;

import jakarta.xml.ws.Service;
import org.junit.Test;

/**
 * Offline smoke test: view-related generated SOAP types are on the compile classpath via
 * {@code au.gov.nehta:pcehr-compiled-wsdl}.
 */
public class ViewClientsSmokeTest {

    @Test
    public void getViewServiceTypeIsOnClasspath() throws Exception {
        assertServiceType("au.net.electronichealth.ns.pcehr.svc.getview._1.GetViewService");
    }

    @Test
    public void getAuditViewServiceTypeIsOnClasspath() throws Exception {
        assertServiceType("au.net.electronichealth.ns.pcehr.svc.getauditview._1.GetAuditViewService");
    }

    @Test
    public void getChangeHistoryViewServiceTypeIsOnClasspath() throws Exception {
        assertServiceType("au.net.electronichealth.ns.pcehr.svc.getchangehistoryview._1.GetChangeHistoryViewService");
    }

    @Test
    public void getIndividualDetailsViewServiceTypeIsOnClasspath() throws Exception {
        assertServiceType("au.net.electronichealth.ns.pcehr.svc.getindividualdetailsview._2.GetIndividualDetailsViewService");
    }

    @Test
    public void getRepresentativeListServiceTypeIsOnClasspath() throws Exception {
        assertServiceType("au.net.electronichealth.ns.pcehr.svc.getrepresentativelist._1.GetRepresentativeListService");
    }

    @Test
    public void getViewRequestTypesAreOnClasspath() throws Exception {
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.medicareoverview._1.MedicareOverview");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.observationview._1.ObservationView");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.prescriptionanddispenseview._1.PrescriptionAndDispenseView");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.healthcheckscheduleview._1.HealthCheckScheduleView");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.pathologyreportview._1.PathologyReportView");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.diagnosticimagingreportview._1.DiagnosticImagingReportView");
        Class.forName("au.net.electronichealth.ns.pcehr.xsd.interfaces.healthrecordoverview._1.HealthRecordOverView");
    }

    private static void assertServiceType(String className) throws Exception {
        Class<?> serviceType = Class.forName(className);
        assertTrue(Service.class.isAssignableFrom(serviceType));
    }
}
