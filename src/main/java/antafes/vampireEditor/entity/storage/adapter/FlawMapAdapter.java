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

/*
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package antafes.vampireEditor.entity.storage.adapter;

import antafes.vampireEditor.entity.character.Flaw;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.FlawStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JAXB adapter: {@code HashMap<String, Flaw>} ↔ list of flaw key strings.
 */
public class FlawMapAdapter extends XmlAdapter<FlawMapAdapter.FlawKeyList, HashMap<String, Flaw>> {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "flawKeyListXml")
    public static class FlawKeyList {
        @XmlElement(name = "flaw")
        public List<String> keys = new ArrayList<>();
    }

    @Override
    public HashMap<String, Flaw> unmarshal(FlawKeyList v) {
        HashMap<String, Flaw> map = new HashMap<>();
        if (v == null || v.keys == null) return map;
        FlawStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.FLAW);
        for (String key : v.keys) {
            try {
                Flaw flaw = storage.getEntity(key);
                map.put(key, flaw);
            } catch (EntityStorageException e) {
                Logger.getLogger(FlawMapAdapter.class.getName())
                        .log(Level.SEVERE, "Flaw key ''{0}'' not found", key);
            }
        }
        return map;
    }

    @Override
    public FlawKeyList marshal(HashMap<String, Flaw> v) {
        FlawKeyList list = new FlawKeyList();
        if (v != null) {
            list.keys = new ArrayList<>(v.keySet());
        }
        return list;
    }
}



