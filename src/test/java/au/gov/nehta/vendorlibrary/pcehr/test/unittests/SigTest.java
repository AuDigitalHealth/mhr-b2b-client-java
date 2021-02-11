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

package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import au.gov.nehta.vendorlibrary.clinicalpackage.util.PackageExtractionException;
import au.gov.nehta.vendorlibrary.common.security.SignedContainerProfileUtil;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.constant.XMLNamespaces;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.util.FileUtils;
import au.gov.nehta.xsp.*;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SigTest {

    public static void main(String[] args) throws XspException, JAXBException, IOException, PackageExtractionException, SignatureValidationException, CertificateValidationException {

        verifySignature(new File("./src/test/resources/TestFiles/Richard/phil.xml"));
        verifySignature(new File("./src/test/resources/TestFiles/Richard/kai.xml"));
        verifySignature(new File("./src/test/resources/TestFiles/Richard/richard.xml"));

        //  HashMap<String, ArrayList<Object>> items = new HashMap<String, ArrayList<Object>>();
    }

    private static void verifySignature(File file) throws IOException, PackageExtractionException, XspException, SignatureValidationException, CertificateValidationException {
        byte[] content = FileUtils.loadFile(file);
        Document signature = extractSignature(content);

        NodeList sigNodeList = signature.getElementsByTagNameNS(XMLNamespaces.DS.getNamespace(), "Signature");
        Element sigElement = getFirstElementFromNodeList(sigNodeList);

        XmlSignatureProfileService xmlSignatureProfileService = XspFactory.getInstance().getXmlSignatureProfileService(XspVersion.V_2010);
        xmlSignatureProfileService.check(sigElement, SignedContainerProfileUtil.NULL_CERTIFICATE_VALIDATOR);
    }

    private static Document extractSignature(final byte[] fileContent) throws PackageExtractionException {
        // Parse XML and create XML document object
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder domBuilder;
        try {
            domBuilder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PackageExtractionException("Unable to extract signature", e);
        }

        // Parse signature
        Document doc;
        try {
            doc = domBuilder.parse(new ByteArrayInputStream(fileContent));
        } catch (SAXException e) {
            throw new PackageExtractionException("Unable to parse signature XML", e);
        } catch (IOException e) {
            throw new PackageExtractionException("Unable to extract signature", e);
        }
        return doc;
    }

    private static Element getFirstElementFromNodeList(NodeList nodeList) {
        Validate.notNull(nodeList, "'nodeList' cannot be null.");
        Element element = null;
        if (nodeList.getLength() > 0) {
            Validate.notNull(nodeList.item(0), "'nodeList.item' cannot be null.");
            element = (Element) nodeList.item(0);
        }
        return element;
    }

}
