/*
 * Copyright 2011 NEHTA
 * Copyright 2021-2026 ADHA (Australian Digital Health Agency)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package au.gov.nehta.vendorlibrary.ws;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;
import javax.xml.namespace.QName;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.soap.AddressingFeature;

public final class WebServiceClientUtil {

    private static final String REQUEST_CONTEXT_SSL_SOCKET_FACTORY_PROPERTY_NAME =
            "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String INTERNAL_REQUEST_CONTEXT_SSL_SOCKET_FACTORY_PROPERTY_NAME =
            "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";

    private WebServiceClientUtil() {
    }

    public static <T> T getPort(Class<T> serviceInterface,
                                Class<? extends Service> serviceClass,
                                SocketFactory sslSocketFactory,
                                String endpoint,
                                List<Handler> handlerChain) {
        T port = getPort(serviceInterface, serviceClass, sslSocketFactory, endpoint);
        addHandlerChain(port, handlerChain);
        return port;
    }

    public static <T> T getPort(Class<T> serviceInterface,
                                Class<? extends Service> serviceClass,
                                SocketFactory sslSocketFactory,
                                String endpoint) {
        WebServiceClient annotation = serviceClass.getAnnotation(WebServiceClient.class);
        String wsdlFileLoc = annotation.wsdlLocation();
        Service service = createGeneratedService(serviceClass, annotation, wsdlFileLoc);
        T port = service.getPort(serviceInterface);
        configurePortWithEndpoint(port, endpoint);
        configurePortWithSslSocketFactory(port, sslSocketFactory);
        return port;
    }

    public static <T> T getPort(Class<T> serviceInterface,
                                Class<? extends Service> serviceClass,
                                SocketFactory sslSocketFactory,
                                String endpoint,
                                List<Handler> handlerChain,
                                boolean addressingFeature) {
        WebServiceClient annotation = serviceClass.getAnnotation(WebServiceClient.class);
        String wsdlFileLoc = annotation.wsdlLocation();
        Service service = createGeneratedService(serviceClass, annotation, wsdlFileLoc);
        T port = service.getPort(serviceInterface, new AddressingFeature(addressingFeature));
        configurePortWithEndpoint(port, endpoint);
        configurePortWithSslSocketFactory(port, sslSocketFactory);
        addHandlerChain(port, handlerChain);
        return port;
    }

    public static <T> T getPort(Class<T> serviceInterface,
                                Class<? extends Service> serviceClass,
                                SocketFactory sslSocketFactory,
                                List<Handler> handlerChain) {
        T port = getPort(serviceInterface, serviceClass, sslSocketFactory);
        addHandlerChain(port, handlerChain);
        return port;
    }

    public static <T> T getPort(Class<T> serviceInterface,
                                Class<? extends Service> serviceClass,
                                SocketFactory sslSocketFactory) {
        WebServiceClient annotation = serviceClass.getAnnotation(WebServiceClient.class);
        String wsdlFileLoc = annotation.wsdlLocation();
        Service service = createGeneratedService(serviceClass, annotation, wsdlFileLoc);
        T port = service.getPort(serviceInterface);
        configurePortWithSslSocketFactory(port, sslSocketFactory);
        return port;
    }

    private static Service createGeneratedService(
            Class<? extends Service> serviceClass,
            WebServiceClient annotation,
            String wsdlFileLoc) {
        final URL wsdlURL = retrieveWsdlUrl(wsdlFileLoc);
        if (wsdlURL == null) {
            throw new WebServiceException(
                    "WSDL not found: " + wsdlFileLoc + " (for " + serviceClass.getName()
                            + "). Place the WSDL on the classpath under wsdl/External/.");
        }
        final QName serviceQName = new QName(annotation.targetNamespace(), annotation.name());
        try {
            Constructor<? extends Service> ctor = serviceClass.getConstructor(URL.class, QName.class);
            return ctor.newInstance(wsdlURL, serviceQName);
        } catch (ReflectiveOperationException e) {
            throw new WebServiceException("Cannot instantiate generated service " + serviceClass.getName(), e);
        }
    }

    private static URL retrieveWsdlUrl(String filePath) {
        if (filePath == null) {
            return null;
        }
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        URL result = tccl != null ? tccl.getResource(filePath) : null;
        if (result == null) {
            ClassLoader cl = WebServiceClientUtil.class.getClassLoader();
            result = cl != null ? cl.getResource(filePath) : null;
        }
        return result;
    }

    private static void configurePortWithSslSocketFactory(final Object servicePort,
                                                          final SocketFactory sslSocketFactory) {
        final BindingProvider bindingProvider = (BindingProvider) servicePort;
        final Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(REQUEST_CONTEXT_SSL_SOCKET_FACTORY_PROPERTY_NAME, sslSocketFactory);
        requestContext.put(INTERNAL_REQUEST_CONTEXT_SSL_SOCKET_FACTORY_PROPERTY_NAME, sslSocketFactory);
    }

    private static void configurePortWithEndpoint(final Object servicePort, final String endpoint) {
        final BindingProvider bindingProvider = (BindingProvider) servicePort;
        final Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint.trim());
    }

    private static void addHandlerChain(final Object servicePort, final List<Handler> handlerChain) {
        final BindingProvider bindingProvider = (BindingProvider) servicePort;
        bindingProvider.getBinding().setHandlerChain(handlerChain);
    }
}
