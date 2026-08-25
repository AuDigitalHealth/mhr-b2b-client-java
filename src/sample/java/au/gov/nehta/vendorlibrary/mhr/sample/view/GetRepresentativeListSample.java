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
package au.gov.nehta.vendorlibrary.mhr.sample.view;

import au.gov.nehta.vendorlibrary.mhr.clients.view.GetRepresentativeListClient;
import au.gov.nehta.vendorlibrary.mhr.sample.common.constants.SampleEndpoints;
import au.gov.nehta.vendorlibrary.mhr.sample.common.constants.SampleValues;
import au.gov.nehta.vendorlibrary.mhr.sample.common.util.MessageComponents;
import au.gov.nehta.vendorlibrary.mhr.sample.common.util.SecurityUtil;
import au.net.electronichealth.ns.mhr.svc.getrepresentativelist._1.StandardErrorMsg;
import au.net.electronichealth.ns.mhr.xsd.common.commoncoreelements._1.MHRHeader;
import au.net.electronichealth.ns.mhr.xsd.interfaces.getrepresentativelist._1.GetRepresentativeListResponse;
import com.sun.xml.ws.developer.JAXWSProperties;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * An example class showing usage of the Get Representative List web service
 *  */
public class GetRepresentativeListSample {


           }
        

        
    public static void main(String[] args) throws IOException, GeneralSecurityException, StandardErrorMsg {


                
        GetRepresentativeListClient client;


                
        // Sets the newly created sslsocketfactory as the default for all instances of the HttpsURLConnection class.
        SSLSocketFactory sslSocketFactory = SecurityUtil.getSsl
                // ocketFactory();
                
        // For testing purpos

                
                
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

        // Ins

                lient, providing:
                
                 
        //
        
                ke factory;
                     * endpoint URI string; and
        //  
                ging on/off.
        client = new GetR
                                Client(
             
                                ry,
                Sec
                                ificate(),
       
                                Util.getPrivateKey(),
              
                                .GET_R

                VE _LIST,
                true);
                


                

        //allow SSL connections to be set on a per client basis
   
                nt.setProperty(JAXWSProperties.SSL_SOCKET_FACTORY, 
                                
                                
                                

        // Create a 
                                g helper method.
                                er.User user = MessageComponents.cre

                            SampleValues.USER_ID_TYPE,
                         SampleValues.USER_ID,
                SampleVal
                                
                Sam
                                ME,
      
                                alues.USER_USE_ROLE_FOR_AUDIT
                                
        );


                
        // Create a sample product type,
                lper method.
        MHRHeader.ProductType productType = MessageComponents.
                                   SampleValues.PRODUCT_TYPE_VENDOR,
                                      SampleValues.PRODUCT_NAME,
                                          SampleValues.PRODUCT_VERSION,
                SampleValues.PRODUCT_PLATFORM
        );

        // Create a sample accessing organisation, using helper method.
        MHRHeader.AccessingOrganisation accessingOrganisation = MessageComponents.createAccessingOrganisation(
                SampleValues.ACCESSING_ORGANISATION_ID,
                SampleValues.ACCESSING_ORGANISATION_NAME,
                SampleValues.ACCESSING_ORGANISATION_ALTERNATE_NAME
        );

        // Create a sample PCEHR request header, using helper method.
        MHRHeader commonHeader = MessageComponents.createRequest(
                user,
                SampleValues.IHI_NUMBER,
                productType,
                SampleValues.CLIENT_SYSTEM_TYPE_CIS,
                accessingOrganisation
        );

        // Call operation - get all PCEHR documents associated with given IHI.
        GetRepresentativeListResponse response = client.getRepresentativeList(commonHeader);

        // Process response.
        System.out.println("Response Status: " + response.getResponseStatus().getCode());

        // Get list of documents.
        GetRepresentativeListResponse.MHRRecord.RepresentativeList representativeList = response.getMHRRecord().getRepresentativeList();

        // Get document status.
        System.out.println("Representative List Size: " + representativeList.getRepresentatives().size());
    }
}
