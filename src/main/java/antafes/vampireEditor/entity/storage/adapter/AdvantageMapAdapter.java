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

import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JAXB adapter: {@code HashMap<String, Advantage>} ↔ list of {@code <advantage key="...">value</advantage>}.
 */
public class AdvantageMapAdapter
        extends XmlAdapter<AdvantageMapAdapter.AdvantageList, HashMap<String, Advantage>> {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "advantageListXml")
    public static class AdvantageList {
        @XmlElement(name = "advantage")
        public List<KeyedIntEntry> entries = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "advantageKeyedIntEntryXml")
    public static class KeyedIntEntry {
        @XmlAttribute(name = "key")
        public String key;

        @XmlValue
        public Integer value;
    }

    @Override
    public HashMap<String, Advantage> unmarshal(AdvantageList v) {
        HashMap<String, Advantage> map = new HashMap<>();
        if (v == null || v.entries == null) return map;
        AdvantageStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        for (KeyedIntEntry entry : v.entries) {
            if (entry.key == null) continue;
            try {
                Advantage adv = storage.getEntity(entry.key);
                map.put(entry.key, adv.toBuilder().setValue(entry.value != null ? entry.value : 0).build());
            } catch (EntityStorageException e) {
                Logger.getLogger(AdvantageMapAdapter.class.getName())
                        .log(Level.SEVERE, "Advantage key ''{0}'' not found", entry.key);
            }
        }
        return map;
    }

    @Override
    public AdvantageList marshal(HashMap<String, Advantage> v) {
        AdvantageList list = new AdvantageList();
        if (v == null) return list;
        v.forEach((key, adv) -> {
            KeyedIntEntry e = new KeyedIntEntry();
            e.key = key;
            e.value = adv.getValue();
            list.entries.add(e);
        });
        return list;
    }
}



