package au.gov.nehta.vendorlibrary.pcehr.test._20120718_regression.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RegisterSuite.class,
        RecordAccessSuite.class,
        DocumentExchangeSuite.class,
        ViewSuite.class,
        TemplateSuite.class
})

public class TestSuite {
}
