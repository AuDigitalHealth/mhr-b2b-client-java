package au.gov.nehta.vendorlibrary.mhr.test.unittests;

import static org.junit.Assert.assertTrue;

import javax.xml.ws.Service;
import org.junit.Test;

/**
 * Smoke test: generated PCEHR SOAP types are on the compile classpath via mhr-wsdl.
 */
public class MhrWsdlArtifactSmokeTest {

    @Test
    public void mhrProfileServiceTypeIsOnClasspath() throws Exception {
        Class<?> serviceType = Class.forName(
                "au.net.electronichealth.ns.mhr.b2b.svc.mhrprofile._1.MHRProfileService");
        assertTrue(Service.class.isAssignableFrom(serviceType));
    }
}
