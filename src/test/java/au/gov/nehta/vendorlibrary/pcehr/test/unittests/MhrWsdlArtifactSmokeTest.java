package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import static org.junit.Assert.assertTrue;

import jakarta.xml.ws.Service;
import org.junit.Test;

/**
 * Smoke test: generated MHR SOAP types are on the compile classpath (from in-repo wsimport or {@code -Pmhr-wsdl-artifact}).
 */
public class MhrWsdlArtifactSmokeTest {

    @Test
    public void pcehrProfileServiceTypeIsOnClasspath() throws Exception {
        Class<?> serviceType = Class.forName(
            "au.net.electronichealth.ns.pcehr.b2b.svc.pcehrprofile._1.PCEHRProfileService");
        assertTrue(Service.class.isAssignableFrom(serviceType));
    }
}
