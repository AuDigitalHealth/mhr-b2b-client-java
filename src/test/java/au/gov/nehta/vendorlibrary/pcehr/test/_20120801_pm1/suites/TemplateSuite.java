/*
 * Copyright 2012 NEHTA
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

package au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.suites;

import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.template.GetTemplateClientTest;
import au.gov.nehta.vendorlibrary.pcehr.test._20120801_pm1.tests.template.SearchTemplateClientTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  GetTemplateClientTest.class,
  SearchTemplateClientTest.class
})

public class TemplateSuite {
}

