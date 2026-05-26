/*
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package Vampire Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.xml.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * Central JAXB bootstrap for marshaller/unmarshaller configuration.
 */
public final class JaxbBindingSupport {
    /**
     * Per-thread, pre-configured {@link XMLInputFactory}.
     * <p>
     * StAX factories are not specified as thread-safe, so each thread gets its own hardened
     * factory instance with DOCTYPE declarations and external entity resolution disabled to
     * prevent XXE attacks.
     */
    private static final ThreadLocal<XMLInputFactory> XML_INPUT_FACTORY = ThreadLocal.withInitial(() -> {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return xif;
    });

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

    /**
     * Create a new {@link Unmarshaller} for the given context, optionally configured with a schema for validation.
     * <p>
     * The caller retains ownership of {@code schemaStream} and is responsible for closing it.
     *
     * @param context      The JAXB context to use
     * @param schemaStream Optional schema stream for validation; the caller must close it
     * @return A configured {@link Unmarshaller}
     */
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

    /**
     * Create a new {@link Marshaller} for the given context, optionally configured with a schema for validation.
     * <p>
     * The caller retains ownership of {@code schemaStream} and is responsible for closing it.
     *
     * @param context      The JAXB context to use
     * @param schemaStream Optional schema stream for validation; the caller must close it
     * @return A configured {@link Marshaller}
     */
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

    /**
     * Create a securely configured {@link XMLStreamReader} for the given input stream.
     * <p>
     * The reader has DOCTYPE declarations and external entity resolution disabled to prevent
     * XXE (XML External Entity) attacks. Use this when unmarshalling user-provided XML files.
     *
     * @param inputStream The input stream to read from
     * @return A hardened {@link XMLStreamReader}
     */
    public static XMLStreamReader createSecureStreamReader(InputStream inputStream) {
        try {
            return XML_INPUT_FACTORY.get().createXMLStreamReader(inputStream);
        } catch (XMLStreamException ex) {
            throw new JaxbBindingException("Could not create secure XML stream reader", ex);
        }
    }

    /**
     * Build a {@link Schema} from the given input stream.
     * <p>
     * The caller retains ownership of {@code schemaStream} and is responsible for closing it;
     * this method does not close the stream.
     *
     * @param schemaStream The stream to read the schema from
     * @return The compiled {@link Schema}
     */
    private static Schema buildSchema(InputStream schemaStream) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return schemaFactory.newSchema(new javax.xml.transform.stream.StreamSource(schemaStream));
        } catch (Exception ex) {
            throw new JaxbBindingException("Could not create XML schema", ex);
        }
    }
}
