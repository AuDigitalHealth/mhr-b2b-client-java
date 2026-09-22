package au.gov.nehta.vendorlibrary.mhr.test.unittests;


import au.gov.nehta.common.utils.IOUtils;
import au.gov.nehta.vendorlibrary.mhr.clients.common.type.DocumentMetadata;
import au.gov.nehta.vendorlibrary.mhr.clients.common.util.MetadataUtils;
import au.gov.nehta.vendorlibrary.mhr.sample.common.util.MessageComponents;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class MetadataUtilsOrgIDNameTest {

    private static MHRHeader exampleHeader;

    static {
        exampleHeader = MessageComponents.createRequest
                (
                        MessageComponents.createUser(MHRHeader.User.IDType.HPII, "8003619166674595", null, "Ross John", false),
                        "8003602348687628",
                        MessageComponents.createProductType("NeHTA", "Test Harness", "1.0", "Windows 7 - Java"),
                        MHRHeader.ClientSystemType.CIS,
                        MessageComponents.createAccessingOrganisation("8003624166667177", "Medicare305", null)
                );
    }

    @Test
    public void test_Organisation_CUSTODIAN_NameId() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String doc = IOUtils.read(new File("src/test/resources/TestFiles/metadataTest/CUSTODIAN_ORG_NAME_TEST.xml"));
        DocumentMetadata documentMetadata = MetadataUtils.toDocumentMetadata(exampleHeader, doc.getBytes());

        Assert.assertEquals("General Practice Clinic", documentMetadata.getAuthorInstitution().getOrganisationName());
        Assert.assertEquals("1.2.36.1.2001.1003.0.8003620833333789", documentMetadata.getAuthorInstitution().getOrganisationIdentifier());


    }

    @Test
    public void test_Organisation_HCF_NameId() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String doc = IOUtils.read(new File("src/test/resources/TestFiles/metadataTest/HCF_ORG_NAME_TEST.xml"));
        DocumentMetadata documentMetadata = MetadataUtils.toDocumentMetadata(exampleHeader, doc.getBytes());

        Assert.assertEquals("West End Healthiness", documentMetadata.getAuthorInstitution().getOrganisationName());
        Assert.assertEquals("1.2.36.1.2001.1003.0.8003620833333782", documentMetadata.getAuthorInstitution().getOrganisationIdentifier());
    }

    @Test
    public void test_Organisation_AUTHOR_NameId() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String doc = IOUtils.read(new File("src/test/resources/TestFiles/metadataTest/Author_ORG_NAME_TEST.xml"));
        DocumentMetadata documentMetadata = MetadataUtils.toDocumentMetadata(exampleHeader, doc.getBytes());

        Assert.assertEquals("Author Good Hospital", documentMetadata.getAuthorInstitution().getOrganisationName());
        Assert.assertEquals("1.2.36.1.2001.1003.0.8013620833333787", documentMetadata.getAuthorInstitution().getOrganisationIdentifier());
    }
}
