package antafes.vampireEditor.xml.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * Central JAXB bootstrap for marshaller/unmarshaller configuration.
 */
public final class JaxbBindingSupport {
    private JaxbBindingSupport() {
    }

    public static JAXBContext createContext(Class<?>... boundClasses) {
        try {
            return JAXBContext.newInstance(boundClasses);
        } catch (JAXBException ex) {
            throw new JaxbBindingException("Could not initialize JAXB context", ex);
        }
    }

    public static Unmarshaller createUnmarshaller(JAXBContext context) {
        return createUnmarshaller(context, null);
    }

    public static Unmarshaller createUnmarshaller(JAXBContext context, InputStream schemaStream) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            if (schemaStream != null) {
                unmarshaller.setSchema(buildSchema(schemaStream));
            }
            return unmarshaller;
        } catch (Exception ex) {
            throw new JaxbBindingException("Could not create JAXB unmarshaller", ex);
        }
    }

    public static Marshaller createMarshaller(JAXBContext context) {
        return createMarshaller(context, null);
    }

    public static Marshaller createMarshaller(JAXBContext context, InputStream schemaStream) {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (schemaStream != null) {
                marshaller.setSchema(buildSchema(schemaStream));
            }
            return marshaller;
        } catch (Exception ex) {
            throw new JaxbBindingException("Could not create JAXB marshaller", ex);
        }
    }

    private static Schema buildSchema(InputStream schemaStream) {
        try (InputStream is = schemaStream) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(new javax.xml.transform.stream.StreamSource(is));
        } catch (Exception ex) {
            throw new JaxbBindingException("Could not create XML schema", ex);
        }
    }
}
