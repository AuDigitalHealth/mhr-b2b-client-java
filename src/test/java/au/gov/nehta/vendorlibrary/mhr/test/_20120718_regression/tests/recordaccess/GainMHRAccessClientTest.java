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
package au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.tests.recordaccess;

import au.gov.nehta.vendorlibrary.mhr.clients.recordaccess.GainMHRAccessClient;
import au.gov.nehta.vendorlibrary.mhr.sample.common.util.MessageComponents;
import au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.AllTests;
import au.net.electronichealth.ns.mhr.xsd.interfaces.mhrprofile._1.GainMHRAccess;
import au.net.electronichealth.ns.mhr.xsd.interfaces.mhrprofile._1.GainMHRAccessResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GainMHRAccessClientTest {

    private GainMHRAccessClient client;

    @Before
    public final void setUp() throws Exception {
        AllTests.setUp();
        client = new GainMHRAccessClient(
                AllTests.getSslSocketFactory(),
                AllTests.getCertificate(),
                AllTests.getPrivateKey(),
                "https://b2b.ehealthvendortest.health.gov.au/gainPCEHRAccess",
                true
        );
    }

    @After
    public final void tearDown() throws Exception {
        AllTests.tearDown();
        client = null;
    }

    @Test
    public void test_020() throws Exception {
        GainMHRAccess.MHRRecord record = MessageComponents.createGainMHRRecord(null);
        GainMHRAccessResponse response = client.gainPCEHRAccess(record, AllTests.getDefaultRequest());
        Assert.assertEquals("PCEHR_SUCCESS", response.getResponseStatus().getCode());
        Assert.assertEquals("SUCCESS", response.getResponseStatus().getDescription());
        Assert.assertEquals("8003601008083417", response.getIndividual().getIhiNumber());
    }
}
