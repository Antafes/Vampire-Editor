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
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Generation;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Storage for generations.
 */
public class GenerationStorage extends BaseStorage<Generation> {
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
        Element root;
        InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "generations.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            root = xp.getRootElement();
            ArrayList<Element> elements = XMLParser.getAllChildren(root);
            elements.forEach((element) -> {
                Generation generation = Generation.builder()
                    .setGeneration(Integer.parseInt(element.getAttribute("value")))
                    .setMaximumAttributes(XMLParser.getTagValueInt("maximumAttributes", element))
                    .setMaximumBloodPool(XMLParser.getTagValueInt("maximumBloodPool", element))
                    .setBloodPerRound(XMLParser.getTagValueInt("bloodPerRound", element))
                    .build();

                this.getList().put(Integer.toString(generation.getGeneration()), generation);
            });
        }
    }

    /**
     * Fetch a single generation for a given integer key.
     *
     * @param key The key as integer
     *
     * @return The entity
     */
    public Generation getEntity(int key) throws EntityStorageException {
        return this.getEntity(Integer.toString(key));
    }
}
