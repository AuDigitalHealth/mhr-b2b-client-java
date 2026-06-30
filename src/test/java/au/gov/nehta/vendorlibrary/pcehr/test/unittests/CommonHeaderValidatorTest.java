package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import au.gov.nehta.vendorlibrary.pcehr.clients.common.util.CommonHeaderValidator;
import au.net.electronichealth.ns.pcehr.xsd.common.commoncoreelements._1.PCEHRHeader;
import org.junit.Test;

public class CommonHeaderValidatorTest {

    @Test
    public void validate_acceptsValidHeaderWhenIhiRequired() {
        CommonHeaderValidator.validate(validHeader(), true);
    }

    @Test
    public void validate_acceptsMissingIhiWhenNotRequired() {
        PCEHRHeader header = validHeader();
        header.setIhiNumber(null);
        CommonHeaderValidator.validate(header, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_rejectsMissingIhiWhenRequired() {
        PCEHRHeader header = validHeader();
        header.setIhiNumber(null);
        CommonHeaderValidator.validate(header, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_rejectsInvalidIhiPrefix() {
        PCEHRHeader header = validHeader();
        header.setIhiNumber("1234501234567890");
        CommonHeaderValidator.validate(header, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_rejectsRoleMissingWhenAuditRoleEnabled() {
        PCEHRHeader header = validHeader();
        header.getUser().setUseRoleForAudit(true);
        header.getUser().setRole(null);
        CommonHeaderValidator.validate(header, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_rejectsOrganisationIdWhenOrganisationPresent() {
        PCEHRHeader header = validHeader();
        header.getAccessingOrganisation().setOrganisationID(null);
        CommonHeaderValidator.validate(header, false);
    }

    private static PCEHRHeader validHeader() {
        PCEHRHeader.User user = new PCEHRHeader.User();
        user.setIDType(PCEHRHeader.User.IDType.HPII);
        user.setID("8003610000001145");
        user.setUserName("test-user");
        user.setUseRoleForAudit(false);

        PCEHRHeader.ProductType productType = new PCEHRHeader.ProductType();
        productType.setVendor("ADHA");
        productType.setProductName("myhr-test");
        productType.setProductVersion("1.0.0");
        productType.setPlatform("JVM");

        PCEHRHeader.AccessingOrganisation org = new PCEHRHeader.AccessingOrganisation();
        org.setOrganisationID("8003620000001234");
        org.setOrganisationName("Test Organisation");

        PCEHRHeader header = new PCEHRHeader();
        header.setIhiNumber("8003601234567890");
        header.setClientSystemType(PCEHRHeader.ClientSystemType.OTHER);
        header.setUser(user);
        header.setProductType(productType);
        header.setAccessingOrganisation(org);

        return header;
    }
}
