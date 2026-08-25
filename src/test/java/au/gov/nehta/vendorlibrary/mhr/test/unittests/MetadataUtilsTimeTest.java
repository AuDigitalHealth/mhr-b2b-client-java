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
import java.nio.charset.StandardCharsets;

public class MetadataUtilsTimeTest {

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
    public void testPrescriptionTime() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String doc = IOUtils.read(new File("src/test/resources/TestFiles/metadataTest/PCEHRPrescriptionRecord_3A_Min.xml"));
        DocumentMetadata documentMetadata = MetadataUtils.toDocumentMetadata(exampleHeader, doc.getBytes(StandardCharsets.UTF_8));
        // actual time 20130226155637+1000
        Assert.assertEquals("20130226055637", documentMetadata.getServiceStartTime());
        Assert.assertEquals("20130226055637", documentMetadata.getServiceStopTime());
    }

    @Test
    public void testDispenseTime() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String doc = IOUtils.read(new File("src/test/resources/TestFiles/metadataTest/PCEHRDispenseRecord_3A_Min.xml"));
        DocumentMetadata documentMetadata = MetadataUtils.toDocumentMetadata(exampleHeader, doc.getBytes(StandardCharsets.UTF_8));

        /// actual time 20130226115638+1000
        Assert.assertEquals("20130226015638", documentMetadata.getServiceStartTime());
        Assert.assertEquals("20130226015638", documentMetadata.getServiceStopTime());
    }

}
