/*
 * Copyright 2013 NEHTA
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

import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.NodeList;

import au.gov.nehta.vendorlibrary.pcehr.clients.common.constant.XMLNamespaces;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.exception.MTOMException;
import au.gov.nehta.vendorlibrary.pcehr.clients.common.util.HandlerUtils;

/**
 * This SOAP handler functions to workaround limitations of SOAPHandlers that breaks MTOM,
 * resulting in binary SOAP message attachments being inlined into a single SOAP MIME part as
 * a Base64 encoded string.If a SOAP handler is specified in the handler chain of a JAX-WS client
 * or service, JAX-WS will call the SOAP handler before a SOAP message is sent and after a SOAP message
 * has been received.
 */
public class ConfigurableMTOMHandler implements IMTOMHandler {


  /**
   * the XML element request local name to search for MTOM replacement.
   *  to prevent MTOM being run on the wrong request
   */
  private final String requestType;
  
  /**
   * the XML of the request Type
   */
  private final String requestNameSpace;
  
  
  /**
   * the XML of the local elements to search for
   */
  private final String nameSpace;
 
  /**
   * the XML elements containing base64 to be MTOM'd
   */
  private final String mtomElement;
 
  /**
   * Include XML element local name.
   */
  public static final String INCLUDE_ELEM = "Include";

  /**
   * XOP include attribute string format (reference ID is denoted by '%s'.
   */
  public static final String XOP_INCLUDE = "cid:%s";

  /**
   * XOP include HREF content ID attribute name.
   */
  private static final String CONTENT_ID_ATTR = "href";

  /**
   * 'application/octet-stream' content type.
   */
  private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
  
  /**
  * ConfigurableMTOMHandler
  * A Jax WS request Handler to ensure certain Base64 elements added as binary attachments.
  *
  * @param requestType the XML element for the Enclosing request. eg: "ProvideAndRegisterDocumentSetRequest" or  "registerPCEHR"
  * @param xmlRequestNamespace the XML name space for the requestType eg: "urn:ihe:iti:xds-b:2007"
  * @param mtomElement the element containing bas64 content to be XOP included as a binary attachment eg: "Document"
  * @param the XML name space of the mtomElement eg: "urn:ihe:iti:xds-b:2007"
  **/ 
  public ConfigurableMTOMHandler(String requestType, String xmlRequestNamespace, String mtomElement, String xmlElementNamespace){
    this.requestType=requestType;
    this.requestNameSpace=xmlRequestNamespace;
    this.mtomElement=mtomElement;
    this.nameSpace=xmlElementNamespace;
  }
  
  /**
   * A short hand version of  ConfigurableMTOMHandler(requestType, requestNamespace, element, elementNamespace)
   *  where both elements share the same XML name space
   *
   * @param requestType the XML element for the Enclosing request. eg: "ProvideAndRegisterDocumentSetRequest" or  "registerPCEHR"
   * @param xmlRequestNamespace the XML name space for both supplied element names eg: "urn:ihe:iti:xds-b:2007"
   * @param mtomElement the element containing bas64 content to be XOP included as a binary attachment eg: "Document"
   */
  public ConfigurableMTOMHandler(String requestType, String requestNamespace, String mtomElement ){
    this(requestType,requestNamespace,mtomElement,requestNamespace);
  }

  /**
   * Updates the request to ensure byte content is correctly passed as a MTOM part.
   *
   * @param context the incoming / outgoing soap message context
   * @return true Always returns true.
   * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public final boolean handleMessage(SOAPMessageContext context) {

    if (HandlerUtils.isOutgoing(context)) {

      if (HandlerUtils.isResponseType(context, requestNameSpace , requestType)) {
        try {

          SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
          SOAPBody body = envelope.getBody();

          // Retrieve the Base64 document content string.
          String documentContent = HandlerUtils.extractElementContent(body, nameSpace, mtomElement);
          
          if(null == documentContent){
              //if no document is included, no need to proceed MTOMing
              return true;
          }
          
          // New reference ID for use in include and attachment part.
          UUID referenceId = UUID.randomUUID();

          // Convert to an Input Stream, as required to add an attachment.
          InputStream is = IOUtils.toInputStream(documentContent);

          // Create a new attachment for the document content.
          createDocumentAttachmentPart(context, is, referenceId);

          // Replace document content in SOAP envelope with XOP reference.
          replaceDocumentContent(body, referenceId);

        } catch (SOAPException e) {
          throw new MTOMException("Unable to extract SOAP components", e);
        }
      }else{
        throw new MTOMException("Expected request element "+requestNameSpace+":"+requestType+" was expected but not found ");
      }
    }

    return true;
  }

  /**
   * Replaces inline Base64 content of a 'Document' element and adds an XOP include reference to the SOAP binary attachment.
   *
   * @param body        the SOAP body to be altered.
   * @param referenceId the reference ID corresponding to the content-id in the SOAP attachment to contain the binary document content.
   */
  private void replaceDocumentContent(SOAPBody body, UUID referenceId) {

    NodeList nodeList = body.getElementsByTagNameNS(nameSpace, mtomElement);
    SOAPBodyElement documentElement = (SOAPBodyElement) HandlerUtils.getFirstElementFromNodeList(nodeList);

    // Need to do this otherwise the Base64 content still appears inline.
    documentElement.removeContents();

    // Add the new element.
    try {
      documentElement
        .addChildElement(INCLUDE_ELEM, XMLNamespaces.XOP.getPrefix(), XMLNamespaces.XOP.getNamespace())
        .setAttribute(CONTENT_ID_ATTR, String.format(XOP_INCLUDE, referenceId.toString()));
    } catch (SOAPException e) {
      throw new MTOMException("Failed to add the new XOP include element to the SOAP body.", e);
    }
  }

  /**
   * Creates a new attachment part that contains document element's binary content.
   *
   * @param context     context the incoming / outgoing soap message context
   * @param is          the {@link InputStream} encapsulating the Base64 encoded binary content.
   * @param referenceId the reference ID corresponding to the content-id in the SOAP attachment to contain the binary document content.
   */
  private void createDocumentAttachmentPart(SOAPMessageContext context, InputStream is, UUID referenceId) {

    AttachmentPart attachment = context.getMessage().createAttachmentPart();

    // The attachment is able to accept a Base64 encoded string and transform that to binary in the SOAP attachment part.
    try {
      attachment.setBase64Content(is, OCTET_STREAM_CONTENT_TYPE);
    } catch (SOAPException e) {
      throw new MTOMException("Failed to create a new attachment part", e);
    }

    // Set the reference ID to match that being used in the XOP include.
    attachment.setContentId(referenceId.toString());

    // Add the new attachment part to the SOAP message.
    context.getMessage().addAttachmentPart(attachment);
  }

  /**
   * Ignore Fault and continues with processing logical handling of message.
   *
   * @param context the incoming / outgoing soap message context
   * @return true if the handle signature check is successful.
   * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public final boolean handleFault(SOAPMessageContext context) {
    // Verifies the inbound fault signature.
    // Do nothing. Implement in production code.
    return true;
  }

  /**
   * Does nothing <br>
   * Not utilised for dumping SOAP message.
   *
   * @param context @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public void close(MessageContext context) {
    // Do nothing.
  }

  /**
   * Does nothing returns null.<br>
   * Ignore processing of SOAP header as the primary intention is just to
   * 'Dump' the SOAP message
   *
   * @return @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
   */
  public final Set<QName> getHeaders() {
    return null;
  }
}
