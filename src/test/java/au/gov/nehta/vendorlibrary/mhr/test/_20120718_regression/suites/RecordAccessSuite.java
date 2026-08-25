package au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.suites;

import au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.tests.recordaccess.DoesPCHERExistClientTest;
import au.gov.nehta.vendorlibrary.mhr.test._20120718_regression.tests.recordaccess.GainMHRAccessClientTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GainMHRAccessClientTest.class,
        DoesPCHERExistClientTest.class
})

public class RecordAccessSuite {
}
