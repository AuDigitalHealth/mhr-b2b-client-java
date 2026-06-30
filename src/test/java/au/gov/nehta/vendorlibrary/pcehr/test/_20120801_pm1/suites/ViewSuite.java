package au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.view.GetAuditViewClientTest;
import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.view.GetChangeHistoryViewClientTest;
import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.view.GetDocumentListClientTest;
import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.view.GetIndividualDetailsViewClientTest;
import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.view.GetRepresentativeListClientTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  GetDocumentListClientTest.class,
  GetAuditViewClientTest.class,
  GetChangeHistoryViewClientTest.class,
  GetIndividualDetailsViewClientTest.class,
  GetRepresentativeListClientTest.class
})

public class ViewSuite {
}
