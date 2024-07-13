package net.letsdank.platform.utils.string;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class XMLString {
    public static String convert(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return (Boolean) value ? "true" : "false";
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Date) {
            // XML representation of dateTime type of XML schema
            // See http://www.w3.org/TR/xmlschema-2/#dateTime
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return dateFormat.format((Date) value);
        } else if (value instanceof Number) {
            // XML representation of decimal type of the XML schema
            // See http://www.w3.org/TR/xmlschema-2/#decimal
            return value.toString();
        } else if (value instanceof Enum) {
            // as a name of enum value
            return ((Enum) value).name();
        } else if (value instanceof UUID) {
            // as a string representation of the unique identifier
            return value.toString();
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + value.getClass());
        }
    }
}
