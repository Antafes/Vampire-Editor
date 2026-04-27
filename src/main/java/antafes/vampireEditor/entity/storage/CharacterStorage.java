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
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.exception.MissingClanException;
import antafes.vampireEditor.entity.exception.MissingRoadException;
import antafes.vampireEditor.xml.jaxb.JaxbBindingSupport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.HashMap;

/**
 * Storage for characters
 *
 * @author Marian Pollzien
 */
public class CharacterStorage extends BaseStorage<Character> {
    private final Configuration configuration;
    private final JAXBContext jaxbContext;

    /**
     * Create a new character storage.
     */
    CharacterStorage() {
        super();
        this.configuration = Configuration.getInstance();
        this.jaxbContext = JaxbBindingSupport.createContext(Character.class);
    }

    /**
     * Initializes the storage and pre-loads available data.
     * TODO This might be used in the future to preload previously opened characters.
     */
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
        try {
            Marshaller marshaller = JaxbBindingSupport.createMarshaller(jaxbContext);
            marshaller.marshal(character, this.configuration.getSaveDirPath(filename));
            this.getList().put(character.getId().toString(), character);
        } catch (JAXBException e) {
            throw new RuntimeException("Could not save character '" + filename + "'", e);
        }
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
        try {
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(jaxbContext);
            Character character = (Character) unmarshaller.unmarshal(
                new File(this.configuration.getOpenDirPath(), filename)
            );

            this.normalizeLoadedCollections(character);
            this.validateLoadedCharacter(character);
            character = this.rebuildLoadedCharacter(character);
            this.getList().put(character.getId().toString(), character);
            return character;
        } catch (JAXBException | IllegalArgumentException e) {
            EntityStorageException ex = new EntityStorageException("Could not load character '" + filename + "'!");
            ex.addSuppressed(e);
            throw ex;
        }
    }

    private void normalizeLoadedCollections(Character character)
    {
        if (character.getAttributes() == null) {
            character.setAttributes(new HashMap<>());
        }

        if (character.getAbilities() == null) {
            character.setAbilities(new HashMap<>());
        }

        if (character.getAdvantages() == null) {
            character.setAdvantages(new HashMap<>());
        }

        if (character.getMerits() == null) {
            character.setMerits(new HashMap<>());
        }

        if (character.getFlaws() == null) {
            character.setFlaws(new HashMap<>());
        }
    }

    private void validateLoadedCharacter(Character character) throws EntityStorageException
    {
        if (character == null || character.getId() == null) {
            throw new EntityStorageException("Character document has no id");
        }

        if (!character.isNpc() && character.getClan() == null) {
            throw new MissingClanException("Missing clan for non-NPC character!");
        }

        if (character.getGeneration() == null) {
            throw new EntityStorageException("Missing generation for character!");
        }

        if (!character.isNpc() && character.getRoad() == null) {
            throw new MissingRoadException("Missing road for non-NPC character!");
        }
    }

    private Character rebuildLoadedCharacter(Character character) throws EntityStorageException
    {
        try {
            return character.toBuilder().build();
        } catch (Exception e) {
            EntityStorageException ex = new EntityStorageException("Could not rebuild loaded character '" + character.getId() + "'!");
            ex.addSuppressed(e);
            throw ex;
        }
    }
}
