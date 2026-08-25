package au.gov.nehta.vendorlibrary.mhr.test.unittests;

import static org.junit.Assert.assertTrue;

import jakarta.xml.ws.Service;
import org.junit.Test;

/**
 * Smoke test: generated MHR SOAP types are on the compile classpath via
 * {@code mhr-wsdl}.
 */
public class MhrWsdlArtifactSmokeTest {

    @Test
    public void mhrProfileServiceTypeIsOnClasspath() throws Exception {
        Class<?> serviceType = Class.forName(
                "au.net.electronichealth.ns.mhr.b2b.svc.mhrprofile._1.MHRProfileService");
        assertTrue(Service.class.isAssignableFrom(serviceType));
    }
}
