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

import antafes.vampireEditor.entity.character.Attribute;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.AttributeStorage;
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
 * JAXB adapter: {@code HashMap<String, Attribute>} ↔ list of {@code <attribute key="...">value</attribute>}.
 */
public class AttributeMapAdapter
        extends XmlAdapter<AttributeMapAdapter.AttributeList, HashMap<String, Attribute>> {

    /** JAXB value type – list of keyed integer entries. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "attributeListXml")
    public static class AttributeList {
        @XmlElement(name = "attribute")
        public List<KeyedIntEntry> entries = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "attributeKeyedIntEntryXml")
    public static class KeyedIntEntry {
        @XmlAttribute(name = "key")
        public String key;

        @XmlValue
        public Integer value;
    }

    @Override
    public HashMap<String, Attribute> unmarshal(AttributeList v) {
        HashMap<String, Attribute> map = new HashMap<>();
        if (v == null || v.entries == null) return map;
        AttributeStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        for (KeyedIntEntry entry : v.entries) {
            if (entry.key == null) continue;
            try {
                Attribute attr = storage.getEntity(entry.key);
                map.put(entry.key, attr.toBuilder().setValue(entry.value != null ? entry.value : 0).build());
            } catch (EntityStorageException e) {
                Logger.getLogger(AttributeMapAdapter.class.getName())
                        .log(Level.SEVERE, "Attribute key ''{0}'' not found", entry.key);
            }
        }
        return map;
    }

    @Override
    public AttributeList marshal(HashMap<String, Attribute> v) {
        AttributeList list = new AttributeList();
        if (v == null) return list;
        v.forEach((key, attr) -> {
            KeyedIntEntry e = new KeyedIntEntry();
            e.key = key;
            e.value = attr.getValue();
            list.entries.add(e);
        });
        return list;
    }
}



