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
 * @copyright (c) 2018, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.exception.CharacterInvalidXmlException;
import antafes.vampireEditor.entity.exception.CharacterMissingGenerationException;
import antafes.vampireEditor.entity.exception.CharacterMissingIdException;
import antafes.vampireEditor.entity.exception.CharacterValidationUnavailableException;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.exception.MissingClanException;
import antafes.vampireEditor.entity.exception.MissingRoadException;
import antafes.vampireEditor.xml.jaxb.JaxbBindingSupport;
import antafes.vampireEditor.xml.validation.XmlValidationException;
import antafes.vampireEditor.xml.validation.XsdValidator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Storage for characters
 *
 * @author Marian Pollzien
 */
@Repository
public class CharacterStorage extends BaseStorage<Character> {
    private final Configuration configuration;
    private final JAXBContext jaxbContext;


    /**
     * Create a new character storage.
     *
     * @param configuration The configuration object
     */
    @Autowired
    public CharacterStorage(Configuration configuration) {
        super();
        this.configuration = configuration;
        this.jaxbContext = JaxbBindingSupport.createContext(Character.class);
    }

    @Override
    public void init() {
    }

    /**
     * Save the given character.
     *
     * @param character The character to save
     * @param filename The filename to use for saving
     */
    public void save(Character character, String filename) {
        File savedFile = this.configuration.getSaveDirPath(filename);

        try {
            Marshaller marshaller = JaxbBindingSupport.createMarshaller(jaxbContext);
            marshaller.marshal(character, savedFile);
        } catch (JAXBException e) {
            throw new RuntimeException("Could not save character '" + filename + "'", e);
        }

        try (InputStream schemaStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(savedFile, schemaStream);
        } catch (XmlValidationException e) {
            savedFile.delete();
            throw new RuntimeException("Saved character '" + filename + "' failed XSD validation and was removed", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not read schema for validation", e);
        }

        this.getList().put(character.getId().toString(), character);
    }

    /**
     * Load a character from the given file.
     *
     * @param filename The file to load
     *
     * @return The loaded character
     * @throws EntityStorageException Thrown if character couldn't be loaded
     */
    public Character load(String filename) throws EntityStorageException {
        File characterFile = new File(this.configuration.getOpenDirPath(), filename);

        if (!characterFile.isFile() || !characterFile.canRead()) {
            EntityStorageException ex = new EntityStorageException("Could not load character '" + filename + "'!");
            ex.addSuppressed(new FileNotFoundException(characterFile.getAbsolutePath()));
            throw ex;
        }

        try (InputStream schemaStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(characterFile, schemaStream);
        } catch (XmlValidationException e) {
            throw new CharacterInvalidXmlException("Character file '" + filename + "' failed XSD validation!", e);
        } catch (IOException e) {
            throw new CharacterValidationUnavailableException("Could not read schema for validation", e);
        }

        try (FileInputStream fis = new FileInputStream(characterFile)) {
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(jaxbContext);
            XMLStreamReader xsr = JaxbBindingSupport.createSecureStreamReader(fis);
            Throwable primaryFailure = null;
            try {
                Character character = (Character) unmarshaller.unmarshal(xsr);

                this.validateLoadedCharacter(character);
                character = this.rebuildLoadedCharacter(character);
                this.getList().put(character.getId().toString(), character);
                return character;
            } catch (Throwable t) {
                primaryFailure = t;
                throw t;
            } finally {
                try {
                    xsr.close();
                } catch (XMLStreamException closeException) {
                    if (primaryFailure != null) {
                        primaryFailure.addSuppressed(closeException);
                    } else {
                        throw closeException;
                    }
                }
            }
        } catch (JAXBException | IllegalArgumentException | IOException | XMLStreamException e) {
            EntityStorageException ex = new EntityStorageException("Could not load character '" + filename + "'!");
            ex.addSuppressed(e);
            throw ex;
        }
    }

    private void validateLoadedCharacter(Character character) throws EntityStorageException
    {
        if (character == null || character.getId() == null) {
            throw new CharacterMissingIdException("Character document has no id");
        }

        if (!character.isNpc() && character.getClan() == null) {
            throw new MissingClanException("Missing clan for non-NPC character!");
        }

        if (character.getGeneration() == null) {
            throw new CharacterMissingGenerationException("Missing generation for character!");
        }

        if (!character.isNpc() && character.getRoad() == null) {
            throw new MissingRoadException("Missing road for non-NPC character!");
        }
    }

    private Character rebuildLoadedCharacter(Character character) throws EntityStorageException
    {
        try {
            Character.CharacterBuilder<?, ?> builder = character.toBuilder();

            if (character.getAttributes() == null) {
                builder.setAttributes(new HashMap<>());
            }

            if (character.getAbilities() == null) {
                builder.setAbilities(new HashMap<>());
            }

            if (character.getAdvantages() == null) {
                builder.setAdvantages(new HashMap<>());
            }

            if (character.getMerits() == null) {
                builder.setMerits(new HashMap<>());
            }

            if (character.getFlaws() == null) {
                builder.setFlaws(new HashMap<>());
            }

            return builder.build();
        } catch (Exception e) {
            EntityStorageException ex = new EntityStorageException("Could not rebuild loaded character '" + character.getId() + "'!");
            ex.addSuppressed(e);
            throw ex;
        }
    }
}
