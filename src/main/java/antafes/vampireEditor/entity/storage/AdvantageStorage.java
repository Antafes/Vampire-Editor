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

import antafes.myXML.XMLParser;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseEntity;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvantageStorage extends BaseStorage {
    /**
     * Initializes the storage and pre-loads available data.
     */
    @Override
    public void init() {
        this.loadData();
    }

    /**
     * Load available data.
     */
    private void loadData() {
        InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "advantages.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                Element name = XMLParser.getTagElement("name", element);
                XMLParser.getAllChildren(name).forEach((translatedName) -> names.put(
                    Configuration.Language.valueOf(translatedName.getNodeName().toUpperCase()),
                    translatedName.getFirstChild().getNodeValue()
                ));

                try {
                    this.getList().put(
                        element.getAttribute("key"),
                        new Advantage.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setType(AdvantageInterface.AdvantageType.valueOf(
                                XMLParser.getTagValue("type", element)
                            ))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Fetch a single ability for a given key.
     *
     * @param key The key under which to find the entity
     *
     * @return The entity
     */
    @Override
    public Advantage getEntity(String key) throws EntityStorageException {
        return (Advantage) super.getEntity(key);
    }

    /**
     * Fetch a subset of abilities from the storage.
     *
     * @param type The type of abilities to fetch
     *
     * @return Map of abilities
     */
    public HashMap<String, Advantage> getEntityMapByType(AdvantageInterface.AdvantageType type) {
        HashMap<String, Advantage> list = new HashMap<>();

        this.getList().forEach((String key, BaseEntity entity) -> {
            Advantage advantage = (Advantage) entity;

            if (Objects.equals(type, advantage.getType())) {
                list.put(key, advantage);
            }
        });

        return list;
    }

    /**
     * Fetch a subset of abilities from the storage.
     *
     * @param type The type of abilities to fetch
     *
     * @return List of abilities
     */
    public ArrayList<Advantage> getEntityListByType(AdvantageInterface.AdvantageType type) {
        return new ArrayList<>(this.getEntityMapByType(type).values());
    }
}
