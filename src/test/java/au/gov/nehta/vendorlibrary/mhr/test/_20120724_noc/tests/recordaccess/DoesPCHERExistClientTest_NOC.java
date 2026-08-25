/*
 * Copyright 2011 NEHTA
 *
 * Licensed under the NEHTA Open Source (Apache) License; you may not use this
 * file except in compliance with the License. A copy of the License is in the
 * 'LICENSE.txt' file, which should be provided with this work.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.tests.recordaccess;

import au.gov.nehta.vendorlibrary.mhr.clients.common.util.MinimalCertificateVerifier;
import au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.DoesMHRExistClient;
import au.gov.nehta.vendorlibrary.mhr.sample.common.util.MessageComponents;
import au.gov.nehta.vendorlibrary.mhr.test.utils.Endpoints;
import au.gov.nehta.vendorlibrary.mhr.test.utils.Logging;
import au.gov.nehta.vendorlibrary.mhr.test.utils.SecurityUtil;
import au.net.electronichealth.ns.mhr.b2b.svc.mhrprofile._1.StandardErrorMsg;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import au.net.electronichealth.ns.mhr.xsd.interfaces.mhrprofile._1.DoesMHRExistResponse;
import org.junit.*;

import javax.net.ssl.SSLSocketFactory;

public class DoesPCHERExistClientTest_NOC {

    private DoesMHRExistClient client;
    private static SSLSocketFactory sslSocketFactory;

    @BeforeClass
    public static void initialSetup() throws Exception {
//
//    HttpsURLConnection.setDefaultHostnameVerifier(
//      new HostnameVerifier() {
//        public boolean verify(String s, SSLSession sslSession) {
//          return true;
//        }
//      }
//    );

        sslSocketFactory = SecurityUtil.getSslSocketFactory();

        // Sets the newly created sslsocketfactory as the default for all instances OF the HttpsURLConnection class.
        //HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);

        // For testing purposes.
//    System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    }

    @Before
    public final void setUp() throws Exception {
        client = new DoesMHRExistClient(
                sslSocketFactory,
                SecurityUtil.getCertificate(),
                new MinimalCertificateVerifier(),
                SecurityUtil.getPrivateKey(),
                Endpoints.ACCENTURE_DOES_PCEHR_EXIST,
                Logging.DOES_PCEHR_EXIST
        );

        client.setProperty("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory" ,// JAXWSProperties.SSL_SOCKET_FACTORY,
                sslSocketFactory);

    }

    @After
    public final void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void test_015() throws Exception {

        DoesMHRExistResponse response = client.doesPCEHRExist(
                MessageComponents.createRequest
                        (
                                MessageComponents.createUser(MHRHeader.User.IDType.HPII, "8003619166674595", null, "Ross John", false),
                                "8003602348687602",
                                MessageComponents.createProductType("NeHTA", "Test Harness", "1.0", "Windows 7 - Java"),
                                MHRHeader.ClientSystemType.CIS,
                                MessageComponents.createAccessingOrganisation(SecurityUtil.getHPIOMatchingCertificate(), "Medicare305", null)
                        )
        );

        Assert.assertTrue(response.isPCEHRExists());
        Assert.assertEquals(DoesMHRExistResponse.AccessCodeRequired.ACCESS_GRANTED, response.getAccessCodeRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_016() throws Exception {
        DoesMHRExistResponse response = client.doesPCEHRExist(
                MessageComponents.createRequest
                        (
                                MessageComponents.createUser(MHRHeader.User.IDType.HPII, "8003619166674595", null, "Ross John", false),
                                "800360234868760",
                                MessageComponents.createProductType("NeHTA", "Harness", "1.0", "Windows 7 - Java"),
                                MHRHeader.ClientSystemType.CIS,
                                MessageComponents.createAccessingOrganisation("8003628233352432", "Medicare305", null)
                        )
        );
    }

    @Test(expected = StandardErrorMsg.class)
    public void test_017() throws Exception {
        System.setProperty("com.sun.xml.ws.util.pipe.StandaloneTubeAssembler.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.local.LocalTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");

        try {


            DoesMHRExistResponse response = client.doesPCEHRExist(
                    MessageComponents.createRequest
                            (
                                    MessageComponents.createUser(MHRHeader.User.IDType.HPII, "8003619166674595", null, "Ross John", false),
                                    "8003602348687602",
                                    MessageComponents.createProductType("NeHTA", "Harness", "1.0", "Windows 7 - Java"),
                                    MHRHeader.ClientSystemType.CIS,
                                    MessageComponents.createAccessingOrganisation("8000627500003640", "Medicare305", null)
                            )
            );
        } catch (StandardErrorMsg e) {
            Assert.assertEquals("badParam", e.getFaultInfo().getErrorCode().value());
            Assert.assertEquals("PCEHR_ERROR_0505 - Invalid HPI-O", e.getFaultInfo().getMessage());
            throw e;
        }
    }
}
