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
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.Road;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Storage for roads.
 */
public class RoadStorage extends BaseStorage<Road> {
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
        InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "roads.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            AdvantageStorage advantageStorage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);

            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> names.put(
                    Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                    name.getFirstChild().getNodeValue()
                ));

                List<Advantage> merits = new ArrayList<>();
                Element advantagesElement = XMLParser.getTagElement("advantages", element);
                if (advantagesElement != null) {
                    XMLParser.getAllChildren(advantagesElement).forEach((advantageNode) -> {
                        String key = advantageNode.getFirstChild().getNodeValue();
                        try {
                            merits.add(advantageStorage.getEntity(key));
                        } catch (EntityStorageException e) {
                            Logger.getLogger(RoadStorage.class.getName()).log(Level.WARNING,
                                "Advantage key ''{0}'' not found for road ''{1}''",
                                new Object[]{key, element.getAttribute("key")});
                        }
                    });
                }

                    this.getList().put(
                        element.getAttribute("key"),
                        Road.builder()
                            .setNames(names)
                            .setKey(element.getAttribute("key"))
                            .setMerits(merits)
                            .build()
                    );
            });
        }
    }
}
