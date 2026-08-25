package au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.tests.registration;

import au.gov.nehta.vendorlibrary.mhr.clients.common.type.IVCCorrespondeceChannelCode;
import au.gov.nehta.vendorlibrary.mhr.clients.common.type.IndigenousStatusCode;
import au.gov.nehta.vendorlibrary.mhr.clients.registration.RegisterMHRClient;
import au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.AllTests;
import au.gov.nehta.vendorlibrary.mhr.test.utils.Endpoints;
import au.gov.nehta.vendorlibrary.mhr.test.utils.Logging;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.ContactDetailsType;
import au.net.electronichealth.ns.mhr.xsd.interfaces.registermhr._2.RegisterMHR;
import au.net.electronichealth.ns.mhr.xsd.interfaces.registermhr._2.RegisterMHR.Assertions.Identity;
import au.net.electronichealth.ns.mhr.xsd.interfaces.registermhr._2.RegisterMHR.Assertions.Identity.EvidenceOfIdentity;
import au.net.electronichealth.ns.mhr.xsd.interfaces.registermhr._2.RegisterMHR.Assertions.IvcCorrespondence;
import au.net.electronichealth.ns.mhr.xsd.interfaces.registermhr._2.RegisterMHRResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegisterMHRClientTest {

    private RegisterMHRClient client;

    @Before
    public final void setUp() throws Exception {
        AllTests.setUp();
        client = new RegisterMHRClient(
                AllTests.getSslSocketFactory(),
                AllTests.getCertificate(),
                AllTests.getPrivateKey(),
                Endpoints.REGRESSION_REGISTER_PCEHR,
                //     Endpoints.SVT_UPLOAD_DOCUMENT+Endpoints.REGISTER_PCEHR,
                Logging.REGISTER_PCEHR
        );
    }

    @After
    public final void tearDown() throws Exception {
        AllTests.tearDown();
        client = null;
    }

    @Test
    public void test_010() throws Exception {

        // This test registers a PCEHR with an IHI.
        RegisterMHR registrationDetails = new RegisterMHR();
        RegisterMHR.Assertions assertions = new RegisterMHR.Assertions();
        IvcCorrespondence ivc = new IvcCorrespondence();
        ivc.setChannel(IVCCorrespondeceChannelCode.email.getCode());
        ContactDetailsType details = new ContactDetailsType();
        details.setEmailAddress("test@test.com");
        ivc.setContactDetails(details);
        //    assertions.setIdentityVerifiedByProvider(true);
//    assertions.setMedicareConsent(true);
//    assertions.setIVCCommunicationMethod("email");
//    assertions.setVersionOfTermsAndConditionsAgreed(BigInteger.valueOf(1));
        assertions.setIvcCorrespondence(ivc);
        assertions.setAcceptedTermsAndConditions(true);
        //assertions.setRepresentativeDeclaration(true);
        Identity id = new Identity();
        EvidenceOfIdentity ev = new EvidenceOfIdentity();
        ev.setType("passport");
        id.setEvidenceOfIdentity(ev);
        id.setIndigenousStatus(IndigenousStatusCode.indigenousStatus1.getCode());
        assertions.setIdentity(id);

        //TODO:this
        registrationDetails.setAssertions(assertions);

        RegisterMHRResponse response = client.registerPCEHR(AllTests.getDefaultRequest(), registrationDetails);
        Assert.assertEquals("PCEHR_SUCCESS", response.getResponseStatus().getCode());
    }
}
