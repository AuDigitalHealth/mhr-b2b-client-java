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
package au.gov.nehta.vendorlibrary.pcehr.clients.common.util;

import au.gov.nehta.xsp.CertificateValidationException;
import au.gov.nehta.xsp.CertificateValidator;
import au.gov.nehta.xsp.XspException;

import java.security.cert.X509Certificate;

/**
 * Certificate verifier implementation that performs some basic verification of the certificate.
 * The sample is provided for illustrative purposes only and should be implemented in production.
 */
public class MinimalCertificateVerifier implements CertificateValidator {

  @Override
  public void validate(X509Certificate certificate) throws CertificateValidationException, XspException {
    // no-op
  }
}