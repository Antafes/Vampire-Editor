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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.Nature;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.utility.StringUtility;
import antafes.vampireEditor.xml.jaxb.JaxbBindingSupport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Storage for weaknesses.
 */
public class NatureStorage extends BaseStorage<Nature> {
    /**
     * Initializes the storage and pre-loads available data.
     */
    @Override
    public void init() {
        this.loadData();
    }

    @Override
    public Nature getEntity(String key) throws EntityStorageException
    {
        if (key.isEmpty()) {
            throw new EntityStorageException("The given key is empty, could not fetch or create a nature entity!");
        }

        try {
            return super.getEntity(key);
        } catch (EntityStorageException e) {
            HashMap<Configuration.Language, String> names = new HashMap<>();
            for (Configuration.Language language : Configuration.Language.values()) {
                names.put(language, key);
            }

            return Nature.builder()
                .setKey(StringUtility.toCamelCase(key))
                .setNames(names)
                .setManual(true)
                .build();
        }
    }

    /**
     * Load available data.
     */
    private void loadData() {
        try (InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "natures.xml")) {
            JAXBContext context = JaxbBindingSupport.createContext(NaturesDocument.class);
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(context);
            NaturesDocument doc = (NaturesDocument) unmarshaller.unmarshal(is);

            doc.natures.forEach((nature) -> this.getList().put(nature.getKey(), nature));
        } catch (Exception e) {
            Logger.getLogger(NatureStorage.class.getName()).log(Level.SEVERE, "Could not load natures", e);
        }
    }

    @XmlRootElement(name = "natures")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class NaturesDocument {
        @XmlElement(name = "nature")
        public List<Nature> natures = new ArrayList<>();
    }
}
