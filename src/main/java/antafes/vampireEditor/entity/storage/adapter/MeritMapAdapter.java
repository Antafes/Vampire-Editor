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

package antafes.vampireEditor.entity.storage.adapter;

import antafes.vampireEditor.entity.character.Merit;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.MeritStorage;
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
 * JAXB adapter: {@code HashMap<String, Merit>} ↔ list of merit key strings.
 */
public class MeritMapAdapter extends XmlAdapter<MeritMapAdapter.MeritKeyList, HashMap<String, Merit>> {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "meritKeyListXml")
    public static class MeritKeyList {
        @XmlElement(name = "merit")
        public List<String> keys = new ArrayList<>();
    }

    @Override
    public HashMap<String, Merit> unmarshal(MeritKeyList v) {
        HashMap<String, Merit> map = new HashMap<>();
        if (v == null || v.keys == null) return map;
        MeritStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.MERIT);
        for (String key : v.keys) {
            try {
                Merit merit = storage.getEntity(key);
                map.put(key, merit);
            } catch (EntityStorageException e) {
                Logger.getLogger(MeritMapAdapter.class.getName())
                        .log(Level.SEVERE, "Merit key ''{0}'' not found", key);
            }
        }
        return map;
    }

    @Override
    public MeritKeyList marshal(HashMap<String, Merit> v) {
        MeritKeyList list = new MeritKeyList();
        if (v != null) {
            list.keys = new ArrayList<>(v.keySet());
        }
        return list;
    }
}



