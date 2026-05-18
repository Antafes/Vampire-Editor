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

package antafes.vampireEditor.xml.validation;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * XSD validation utility with guaranteed XSD 1.1 support.
 * Strict mode: no fallback to JDK/XSD 1.0.
 */
public final class XsdValidator {
    private static final String STRICT_SCHEMA_FACTORY_CLASS = "org.apache.xerces.jaxp.validation.XMLSchema11Factory";

    private static Boolean strictValidatorAvailable = null;

    private XsdValidator() {
    }

    /**
     * Validates an XML file against an XSD schema.
     * Uses Xerces XMLSchema11Factory for XSD 1.1 validation.
     * The caller owns {@code schemaInputStream} and is responsible for closing it.
     *
     * @param xmlFile the XML file to validate
     * @param schemaInputStream the XSD schema as input stream
     * @throws XmlValidationException if validation fails or schema is invalid
     */
    public static void validate(File xmlFile, InputStream schemaInputStream) throws XmlValidationException {
        validate(new StreamSource(xmlFile), schemaInputStream);
    }

    /**
     * Validates an XML source against an XSD schema.
     * Uses Xerces XMLSchema11Factory for XSD 1.1 validation.
     *
     * @param xmlSource the XML source to validate
     * @param schemaInputStream the XSD schema as input stream
     * @throws XmlValidationException if validation fails or schema is invalid
     */
    public static void validate(StreamSource xmlSource, InputStream schemaInputStream) throws XmlValidationException {
        try {
            Schema schema = buildSchema(schemaInputStream);
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
        } catch (SAXParseException e) {
            throw new XmlValidationException("XML validation error: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw new XmlValidationException("XML validation failed: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XmlValidationException("Unable to read XML source for validation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new XmlValidationException("Unexpected error during validation: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a Schema from an XSD input stream.
     * Uses Xerces XMLSchema11Factory for XSD 1.1 validation.
     *
     * @param schemaInputStream the XSD schema as input stream
     * @return a Schema object
     * @throws Exception if schema cannot be created
     */
    private static Schema buildSchema(InputStream schemaInputStream) throws Exception {
        SchemaFactory schemaFactory = createSchemaFactory();
        return schemaFactory.newSchema(new StreamSource(schemaInputStream));
    }

    /**
     * Creates the appropriate SchemaFactory.
     * Requires Xerces XMLSchema11Factory, no fallback to JDK (XSD 1.0).
     *
     * @return a SchemaFactory instance
     * @throws Exception if no suitable factory can be created
     */
    private static SchemaFactory createSchemaFactory() throws Exception {
        if (!isStrictValidatorAvailable()) {
            throw new IllegalStateException("Strict XSD validation requires Xerces XMLSchema11Factory on the classpath.");
        }
        return createStrictSchemaFactory();
    }

    /**
     * Creates a Xerces XMLSchema11Factory for XSD 1.1 validation.
     *
     * @return a Xerces SchemaFactory
     * @throws Exception if Xerces factory cannot be created
     */
    private static SchemaFactory createStrictSchemaFactory() throws Exception {
        Class<?> factoryClass = Class.forName(STRICT_SCHEMA_FACTORY_CLASS);
        SchemaFactory factory = (SchemaFactory) factoryClass.getDeclaredConstructor().newInstance();
        try {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        } catch (Exception ignored) {
            // Xerces XMLSchema11Factory does not support this property - safe to skip
        }
        try {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        } catch (Exception ignored) {
            // Xerces XMLSchema11Factory does not support this property - safe to skip
        }
        return factory;
    }

    /**
     * Checks if the strict validator (Xerces XMLSchema11Factory) is available on the classpath.
     *
     * @return true if the strict validator can be loaded, false otherwise
     */
    private static boolean isStrictValidatorAvailable() {
        if (strictValidatorAvailable == null) {
            try {
                Class.forName(STRICT_SCHEMA_FACTORY_CLASS);
                strictValidatorAvailable = true;
                System.out.println("Strict XSD 1.1 validator available - xsd:assert enabled");
            } catch (ClassNotFoundException e) {
                strictValidatorAvailable = false;
                System.out.println("Strict XSD 1.1 validator not available");
            }
        }
        return strictValidatorAvailable;
    }

    /**
     * Returns the currently active validator implementation name.
     *
     * @return "Xerces XMLSchema11Factory (XSD 1.1)" or "Unavailable"
     */
    public static String getValidatorImplementation() {
        return isStrictValidatorAvailable() ? "Xerces XMLSchema11Factory (XSD 1.1)" : "Unavailable";
    }
}


