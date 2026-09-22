package au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.suites;

import au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.tests.recordaccess.AllRecordAccessTest_NOC;
import au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.tests.recordaccess.DoesPCHERExistClientTest_NOC;
import au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.tests.recordaccess.GainMHRAccessClientTest_NOC;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  DoesPCHERExistClientTest_NOC.class,
  GainMHRAccessClientTest_NOC.class,
  AllRecordAccessTest_NOC.class
})

public class RecordAccessSuite_NOC {

}
