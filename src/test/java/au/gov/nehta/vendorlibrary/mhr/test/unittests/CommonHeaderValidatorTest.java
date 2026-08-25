package au.gov.nehta.vendorlibrary.mhr.test.unittests;

import au.gov.nehta.vendorlibrary.mhr.clients.common.util.CommonHeaderValidator;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import org.junit.Test;

public class CommonHeaderValidatorTest {

    @Test
    public void validate_acceptsValidHeaderWhenIhiRequired() {
        CommonHeaderValidator.validate(validHeader(), true);
    }

    @Test
    public void validate_acceptsMissingIhiWhenNotRequired() {
        MHRHeader header = validHeader();
        header.setIhiNumber(null);
        CommonHeaderValidator.validate(header, false);
    }

    @Test(expected = NullPointerException.class)
    public void validate_rejectsMissingIhiWhenRequired() {
        MHRHeader header = validHeader();
        header.setIhiNumber(null);
        CommonHeaderValidator.validate(header, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_rejectsInvalidIhiPrefix() {
        MHRHeader header = validHeader();
        header.setIhiNumber("1234501234567890");
        CommonHeaderValidator.validate(header, true);
    }

    @Test(expected = NullPointerException.class)
    public void validate_rejectsRoleMissingWhenAuditRoleEnabled() {
        MHRHeader header = validHeader();
        header.getUser().setUseRoleForAudit(true);
        header.getUser().setRole(null);
        CommonHeaderValidator.validate(header, false);
    }

    @Test(expected = NullPointerException.class)
    public void validate_rejectsOrganisationIdWhenOrganisationPresent() {
        MHRHeader header = validHeader();
        header.getAccessingOrganisation().setOrganisationID(null);
        CommonHeaderValidator.validate(header, false);
    }

    private static MHRHeader validHeader() {
        MHRHeader.User user = new MHRHeader.User();
        user.setIDType(MHRHeader.User.IDType.HPII);
        user.setID("8003610000001145");
        user.setUserName("test-user");
        user.setUseRoleForAudit(false);

        MHRHeader.ProductType productType = new MHRHeader.ProductType();
        productType.setVendor("ADHA");
        productType.setProductName("myhr-test");
        productType.setProductVersion("1.0.0");
        productType.setPlatform("JVM");

        MHRHeader.AccessingOrganisation org = new MHRHeader.AccessingOrganisation();
        org.setOrganisationID("8003620000001234");
        org.setOrganisationName("Test Organisation");

        MHRHeader header = new MHRHeader();
        header.setIhiNumber("8003601234567890");
        header.setClientSystemType(MHRHeader.ClientSystemType.OTHER);
        header.setUser(user);
        header.setProductType(productType);
        header.setAccessingOrganisation(org);

        return header;
    }
}
