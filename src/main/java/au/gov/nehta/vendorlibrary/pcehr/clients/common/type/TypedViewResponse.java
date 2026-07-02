package au.gov.nehta.vendorlibrary.pcehr.clients.common.type;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import au.gov.nehta.vendorlibrary.pcehr.clients.common.exception.ViewUnmarshallException;
import au.net.electronichealth.ns.pcehr.xsd.interfaces.getview._1.GetViewResponse;

/**
 * Immutable holder for an unmarshalled view payload and the original {@link GetViewResponse}.
 *
 * @param <T> type held in the {@code data} element of the view
 */
public class TypedViewResponse<T> {
	private final T responseObject;
	private final GetViewResponse getViewResponse;
	
	public TypedViewResponse(GetViewResponse getViewResponse, T responseObject){
		this.getViewResponse=getViewResponse;
		this.responseObject=responseObject;
	}
	
	/**
	 * Create a typed view response
	 * 
	 * @param clazz           type to unmarshal from the view {@code data} element
	 * @param getViewResponse full getView response
	 * @param <T>             unmarshalled view payload type
	 * @return typed view holder
	 */
	@SuppressWarnings("unchecked")
	public static <T> TypedViewResponse<T> unmarshall(Class<T> clazz, GetViewResponse getViewResponse ){
		try {
			
			T innerObject = null;
			
			if(getViewResponse.getView() != null && getViewResponse.getView().getData() != null){
				JAXBContext context = JAXBContext.newInstance(clazz);
				Unmarshaller u = context.createUnmarshaller();
				String xml = new String(getViewResponse.getView().getData());
				innerObject = (T) u.unmarshal(new StringReader(xml));
			}
			
			return new TypedViewResponse<T>(getViewResponse,  innerObject); 
		} catch (JAXBException e) {
			throw new ViewUnmarshallException(e);
		}   
	}

	/**
	 * Get the typed response object held in the {@code data} element.
	 *
	 * @return unmarshalled view payload
	 */
	public T getResponseObject() {
		return responseObject;
	}

	/**
	 * Get the original {@link GetViewResponse}.
	 *
	 * @return full getView response
	 */
	public GetViewResponse getGetViewResponse() {
		return getViewResponse;
	}

	/**
	 * Convenience accessor for {@code getViewResponse.getResponseStatus().getCode()}.
	 *
	 * @return response status code
	 */
	public String getCode() {
		return getViewResponse.getResponseStatus().getCode();
	}
	
}
