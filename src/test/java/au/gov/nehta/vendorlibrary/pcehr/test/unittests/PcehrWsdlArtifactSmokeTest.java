package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import static org.junit.Assert.assertTrue;

import jakarta.xml.ws.Service;
import org.junit.Test;

/**
 * Smoke test: generated PCEHR SOAP types are on the compile classpath via pcehr-compiled-wsdl.
 */
public class PcehrWsdlArtifactSmokeTest {

    @Test
    public void pcehrProfileServiceTypeIsOnClasspath() throws Exception {
        Class<?> serviceType = Class.forName(
                "au.net.electronichealth.ns.pcehr.b2b.svc.pcehrprofile._1.PCEHRProfileService");
        assertTrue(Service.class.isAssignableFrom(serviceType));
    }
}
