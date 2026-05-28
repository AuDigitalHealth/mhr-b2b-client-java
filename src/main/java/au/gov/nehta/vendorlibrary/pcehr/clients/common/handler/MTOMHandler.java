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
package au.gov.nehta.vendorlibrary.pcehr.clients.common.handler;

import au.gov.nehta.vendorlibrary.pcehr.clients.common.constant.XMLNamespaces;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.exception.MTOMException;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.util.HandlerUtils;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * This SOAP handler functions to workaround limitations of SOAPHandlers that breaks MTOM,
 * resulting in binary SOAP message attachments being inlined into a single SOAP MIME part as
 * a Base64 encoded string.If a SOAP handler is specified in the handler chain of a JAX-WS client
 * or service, JAX-WS will call the SOAP handler before a SOAP message is sent and after a SOAP message
 * has been received.
 * 
 * @deprecated prefer {@link ConfigurableMTOMHandler} over this class with an explicitly set  MTOM element.
 */
@Deprecated
public class MTOMHandler implements SOAPHandler<SOAPMessageContext> {

  /**
   * Document XML element local name.
   */
  public static final String DOCUMENT_ELEM = "Document";

  /**
   * Provide and Register Document Set Request XML element local name.
   */
  public static final String UPLOAD_REQUEST_ELEM = "ProvideAndRegisterDocumentSetRequest";

  @Override
  public final Set<QName> getHeaders() {
    return null;
  }

  @Override
  public final boolean handleMessage(SOAPMessageContext context) {

    if (HandlerUtils.isOutgoing(context)) {
      try {
        // Convert message body to SOAP envelope and body objects.
        SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();

        // New reference ID for use in include and attachment part.
        UUID referenceId = UUID.randomUUID();

        // Retrieve the Base64 document content string.
        String documentContent = HandlerUtils.extractElementContent(body, XMLNamespaces.IHE.getNamespace(), DOCUMENT_ELEM);

        // Convert to an Input Stream, as required to add an attachment.
        InputStream is = new ByteArrayInputStream(documentContent.getBytes(StandardCharsets.UTF_8));

        // Create a new attachment for the document content.
        createDocumentAttachmentPart(context, is, referenceId);

        // Replace document content in SOAP envelope with XOP reference.
        replaceDocumentContent(body, referenceId);

      } catch (SOAPException e) {
        throw new MTOMException("Unable to extract SOAP components", e);
      }
    }

    return true;
  }

  @Override
  public final boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public final void close(MessageContext context) {
    // no-op
  }

  protected void createDocumentAttachmentPart(
      SOAPMessageContext context,
      InputStream inputStream,
      UUID referenceId
  ) {
    AttachmentPart attachmentPart = context.getMessage().createAttachmentPart(inputStream, "application/octet-stream");
    attachmentPart.setContentId(referenceId + "@ihe.net");
    context.getMessage().addAttachmentPart(attachmentPart);
  }

  protected SOAPBodyElement getDocumentBodyElement(SOAPBody body) {
    NodeList nodeList = body.getElementsByTagNameNS(XMLNamespaces.IHE.getNamespace(), DOCUMENT_ELEM);
    return (SOAPBodyElement) nodeList.item(0);
  }

  protected void replaceDocumentContent(
      SOAPBody body,
      UUID referenceId
  ) throws SOAPException {
    SOAPBodyElement bodyElement = getDocumentBodyElement(body);
    bodyElement.removeContents();
    bodyElement.addChildElement("Include", "xop", XMLNamespaces.XOP.getNamespace()).addAttribute(
        QName.valueOf("href"),
        "cid:" + referenceId + "@ihe.net"
    );
  }
}