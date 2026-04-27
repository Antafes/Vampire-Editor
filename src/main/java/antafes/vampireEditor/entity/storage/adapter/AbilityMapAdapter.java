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

import antafes.vampireEditor.entity.character.Ability;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.AbilityStorage;
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
 * JAXB adapter: {@code HashMap<String, Ability>} ↔ list of {@code <ability key="...">value</ability>}.
 */
public class AbilityMapAdapter
        extends XmlAdapter<AbilityMapAdapter.AbilityList, HashMap<String, Ability>> {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "abilityListXml")
    public static class AbilityList {
        @XmlElement(name = "ability")
        public List<KeyedIntEntry> entries = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "abilityKeyedIntEntryXml")
    public static class KeyedIntEntry {
        @XmlAttribute(name = "key")
        public String key;

        @XmlValue
        public Integer value;
    }

    @Override
    public HashMap<String, Ability> unmarshal(AbilityList v) {
        HashMap<String, Ability> map = new HashMap<>();
        if (v == null || v.entries == null) return map;
        AbilityStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);
        for (KeyedIntEntry entry : v.entries) {
            if (entry.key == null) continue;
            try {
                Ability ability = storage.getEntity(entry.key);
                map.put(entry.key, ability.toBuilder().setValue(entry.value != null ? entry.value : 0).build());
            } catch (EntityStorageException e) {
                Logger.getLogger(AbilityMapAdapter.class.getName())
                        .log(Level.SEVERE, "Ability key ''{0}'' not found", entry.key);
            }
        }
        return map;
    }

    @Override
    public AbilityList marshal(HashMap<String, Ability> v) {
        AbilityList list = new AbilityList();
        if (v == null) return list;
        v.forEach((key, ability) -> {
            KeyedIntEntry e = new KeyedIntEntry();
            e.key = key;
            e.value = ability.getValue();
            list.entries.add(e);
        });
        return list;
    }
}


