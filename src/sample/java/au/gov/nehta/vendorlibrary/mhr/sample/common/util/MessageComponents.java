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
package au.gov.nehta.vendorlibrary.mhr.sample.common.util;

import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import au.net.electronichealth.ns.mhr.xsd.interfaces.mhrprofile._1.GainMHRAccess;

/**
 * Helper class to create common components of a SOAP message.
 */
public final class MessageComponents {

  private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

  /**
   * Private constructor to prevent instantiation.
   */
  private MessageComponents() {
  }

  /**
   * Create PCEHR record with no individual records.
   *
   * @param authorisationDetails authorisation details.
   * @return populated {@link GainMHRAccess.MHRRecord} object.
   */
  public static GainMHRAccess.MHRRecord createGainMHRRecord
  (
    GainMHRAccess.MHRRecord.AuthorisationDetails authorisationDetails
  ) {
    GainMHRAccess.MHRRecord record = new GainMHRAccess.MHRRecord();
    record.setIndividual(null);
    record.setAuthorisationDetails(authorisationDetails);
    return record;
  }

  /**
   * Create authorisation details.
   *
   * @param accessType Authorisation access type.
   * @param accessCode Authorisation access code.
   * @return populated {@link GainMHRAccess.MHRRecord.AuthorisationDetails} object.
   */
  public static GainMHRAccess.MHRRecord.AuthorisationDetails createAuthorisationDetails(
    GainMHRAccess.MHRRecord.AuthorisationDetails.AccessType accessType,
    String accessCode
  ) {
    GainMHRAccess.MHRRecord.AuthorisationDetails authorisationDetails = new GainMHRAccess.MHRRecord.AuthorisationDetails();
    authorisationDetails.setAccessType(accessType);
    authorisationDetails.setAccessCode(accessCode);
    return authorisationDetails;
  }

  /**
   * Create a MHRHeader request with custom pre-populated objects.
   *
   * @param user                  Populated user.
   * @param ihiNumber             IHI number.
   * @param productType           Populated product type.
   * @param clientSystemType      Client system type.
   * @param accessingOrganisation Populated accessing organisation.
   * @return Populated {@link au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader}
   */
  public static MHRHeader createRequest(
    MHRHeader.User user,
    String ihiNumber,
    MHRHeader.ProductType productType,
    MHRHeader.ClientSystemType clientSystemType,
    MHRHeader.AccessingOrganisation accessingOrganisation
  ) {
    // Instantiate new request header.
    MHRHeader request = new MHRHeader();

    // Populate request.
    request.setUser(user);
    request.setIhiNumber(ihiNumber);
    request.setProductType(productType);
    request.setClientSystemType(clientSystemType);
    request.setAccessingOrganisation(accessingOrganisation);

    // Return populated request header object.
    return request;
  }

  /**
   * Create a header user.
   *
   * @param idType          User id type.
   * @param id              User id.
   * @param role            User role.
   * @param userName        User name.
   * @param useRoleForAudit Whether or not to use user role for audit purposes.
   * @return Populated {@link MHRHeader.User} object.
   */
  public static MHRHeader.User createUser(
    MHRHeader.User.IDType idType,
    String id,
    String role,
    String userName,
    boolean useRoleForAudit
  ) {
    // Populate user.
    MHRHeader.User user = new MHRHeader.User();
    user.setIDType(idType);
    user.setID(id);
    user.setRole(role);
    user.setUserName(userName);
    user.setUseRoleForAudit(useRoleForAudit);
    return user;
  }

  /**
   * Create a header product type.
   *
   * @param vendor         Product vendor.
   * @param productName    Product name.
   * @param productVersion Product version.
   * @param platform       Product platform.
   * @return Populated {@link MHRHeader.ProductType} object.
   */
  public static MHRHeader.ProductType createProductType(
    String vendor,
    String productName,
    String productVersion,
    String platform
  ) {
    MHRHeader.ProductType productType = new MHRHeader.ProductType();
    productType.setVendor(vendor);
    productType.setProductName(productName);
    productType.setProductVersion(productVersion);
    productType.setPlatform(platform);
    return productType;
  }

  /**
   * Create a header accessing organisation.
   *
   * @param organisationId            Accessing organisation id.
   * @param organisationName          Accessing organisation name.
   * @param alternateOrganisationName Access organisation alternate name.
   * @return Populated {@link MHRHeader.AccessingOrganisation} object.
   */
  public static MHRHeader.AccessingOrganisation createAccessingOrganisation(
    String organisationId,
    String organisationName,
    String alternateOrganisationName
  ) {
    // Populate accessing organisation.
    MHRHeader.AccessingOrganisation accessingOrganisation = new MHRHeader.AccessingOrganisation();
    accessingOrganisation.setOrganisationID(organisationId);
    accessingOrganisation.setOrganisationName(organisationName);
    accessingOrganisation.setAlternateOrganisationName(alternateOrganisationName);
    return accessingOrganisation;
  }
}
