package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Smoke test: Eclipse EE4J jaxws-rt is on the runtime classpath (not legacy Metro webservices-rt).
 */
public class JaxwsRuntimeSmokeTest {

    @Test
    public void jaxwsRtDeveloperPropertiesClassIsPresent() throws Exception {
        assertNotNull(Class.forName("com.sun.xml.ws.developer.JAXWSProperties"));
    }
}
