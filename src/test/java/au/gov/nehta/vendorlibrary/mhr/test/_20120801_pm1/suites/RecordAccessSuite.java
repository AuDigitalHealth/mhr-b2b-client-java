package au.gov.nehta.vendorlibrary.mhr.test._20120801_pm1.suites;

import au.gov.nehta.vendorlibrary.mhr.test._20120801_pm1.tests.recordaccess.GainMHRAccessClientTest;
import au.gov.nehta.vendorlibrary.mhr.test.unittests.ReubenArchitectTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  GainMHRAccessClientTest.class,
  ReubenArchitectTest.class
})

public class RecordAccessSuite {
}
