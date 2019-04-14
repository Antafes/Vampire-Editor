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
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.character.Weakness;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Storage for abilities.
 */
public class ClanStorage extends BaseStorage {
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
        InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "clans.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                HashMap<Configuration.Language, String> nicknames = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> names.put(
                    Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                    name.getFirstChild().getNodeValue()
                ));

                XMLParser.getAllChildren(XMLParser.getTagElement("nickname", element)).forEach((name) -> nicknames.put(
                    Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                    name.getFirstChild().getNodeValue()
                ));

                try {
                    this.getList().put(
                        element.getAttribute("key"),
                        new Clan.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setNicknames(nicknames)
                            .setAdvantages(this.getClanDisciplins(XMLParser.getTagElement("advantages", element)))
                            .setWeaknesses(this.getWeaknesses(XMLParser.getTagElement("weaknesses", element)))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Fetch a single clan for a given key.
     *
     * @param key The key under which to find the entity
     *
     * @return The entity
     */
    @Override
    public Clan getEntity(String key) throws EntityStorageException {
        return (Clan) super.getEntity(key);
    }

    /**
     * Get the disciplines of the given clan element.
     *
     * @param element XML element
     *
     * @return List of advantage objects
     */
    private ArrayList<Advantage> getClanDisciplins(Element element) {
        ArrayList<Advantage> advantagesList = new ArrayList<>();
        ArrayList<Element> advantages = XMLParser.getAllChildren(element);
        AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);

        advantages.forEach(
            (listElement) -> {
                try {
                    advantagesList.add(
                        storage.getEntity(listElement.getChildNodes().item(0).getNodeValue())
                    );
                } catch (EntityStorageException e) {
                    e.printStackTrace();
                }
            }
        );

        return advantagesList;
    }

    /**
     * Get the weaknesses of the given clan element.
     *
     * @param element XML element
     *
     * @return List of weakness objects
     */
    private ArrayList<Weakness> getWeaknesses(Element element) {
        ArrayList<Weakness> weaknessesList = new ArrayList<>();
        ArrayList<Element> weaknesses = XMLParser.getAllChildren(element);
        WeaknessStorage storage = (WeaknessStorage) StorageFactory.getStorage(StorageFactory.StorageType.WEAKNESS);

        weaknesses.forEach(
            (listElement) -> {
                try {
                    weaknessesList.add(
                        storage.getEntity(listElement.getChildNodes().item(0).getNodeValue())
                    );
                } catch (EntityStorageException e) {
                    e.printStackTrace();
                }
            }
        );

        return weaknessesList;
    }
}
