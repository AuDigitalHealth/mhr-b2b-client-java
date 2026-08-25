package au.gov.nehta.vendorlibrary.mhr.test._20120724_noc.tests.recordaccess;

import org.junit.Test;

public class AllRecordAccessTest_NOC {
  @Test
  public void test_027() throws Exception {

    DoesPCHERExistClientTest_NOC doesPCEHRExist = new DoesPCHERExistClientTest_NOC();
    DoesPCHERExistClientTest_NOC.initialSetup();
    doesPCEHRExist.setUp();
    doesPCEHRExist.test_015();
    doesPCEHRExist.tearDown();

    GainMHRAccessClientTest_NOC gainPCEHRAccess = new GainMHRAccessClientTest_NOC();
    GainMHRAccessClientTest_NOC.initialSetup();
    gainPCEHRAccess.setUp();
    gainPCEHRAccess.test_018();
    gainPCEHRAccess.tearDown();
    gainPCEHRAccess.setUp();
    gainPCEHRAccess.test_020();
    gainPCEHRAccess.tearDown();
    gainPCEHRAccess.setUp();
    gainPCEHRAccess.test_021();
    gainPCEHRAccess.tearDown();
  }
}
