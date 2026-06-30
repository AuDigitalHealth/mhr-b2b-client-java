package au.gov.nehta.vendorlibrary.pcehr.test._20120718_regression.tests.view;

import au.gov.nehta.vendorlibrary.pcehr.clients.view.GetIndividualDetailsViewClient;
import au.gov.nehta.vendorlibrary.pcehr.test._20120718_regression.AllTests;
import au.gov.nehta.vendorlibrary.pcehr.test.utils.Endpoints;
import au.gov.nehta.vendorlibrary.pcehr.test.utils.Logging;
import au.net.electronichealth.ns.pcehr.xsd.interfaces.getindividualdetailsview._2.GetIndividualDetailsViewResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetIndividualDetailsViewClientTest {

    private GetIndividualDetailsViewClient client;

    @Before
    public final void setUp() throws Exception {
        AllTests.setUp();
        client = new GetIndividualDetailsViewClient(
                AllTests.getSslSocketFactory(),
                AllTests.getCertificate(),
                AllTests.getPrivateKey(),
                Endpoints.REGRESSION_GET_INDIVIDUAL_DETAILS_VIEW,
                Logging.GET_INDIVIDUAL_DETAILS_VIEW
        );
    }

    @After
    public final void tearDown() throws Exception {
        AllTests.tearDown();
        client = null;
    }

    @Test
    public void test_091() throws Exception {
        GetIndividualDetailsViewResponse response = client.getIndividualDetailsView(AllTests.getDefaultRequest());
        Assert.assertEquals("PCEHR_SUCCESS", response.getResponseStatus().getCode());
    }
}
