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
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Nature;
import antafes.vampireEditor.utility.StringUtility;

import java.io.InputStream;
import java.util.HashMap;

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
        InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "natures.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(element).forEach((name) -> names.put(
                    Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                    name.getFirstChild().getNodeValue()
                ));

                this.getList().put(
                    element.getAttribute("key"),
                    Nature.builder()
                        .setKey(element.getAttribute("key"))
                        .setNames(names)
                        .build()
                );
            });
        }
    }
}
