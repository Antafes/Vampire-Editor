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
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Storage for advantages
 */
public class AdvantageStorage extends BaseTypedStorage<Advantage, AdvantageInterface.AdvantageType> {
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

                this.getList().put(
                    element.getAttribute("key"),
                    Advantage.builder()
                        .setKey(element.getAttribute("key"))
                        .setNames(names)
                        .setType(AdvantageInterface.AdvantageType.valueOf(
                            XMLParser.getTagValue("type", element)
                        ))
                        .build()
                );
            });
        }
    }
}
