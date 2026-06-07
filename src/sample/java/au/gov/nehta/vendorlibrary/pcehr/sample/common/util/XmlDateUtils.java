package au.gov.nehta.vendorlibrary.pcehr.sample.common.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class XmlDateUtils {

    private XmlDateUtils() {
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        try {
            GregorianCalendar gregorian = new GregorianCalendar();
            gregorian.setTimeInMillis(calendar.getTimeInMillis());
            gregorian.setTimeZone(calendar.getTimeZone());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorian);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}
