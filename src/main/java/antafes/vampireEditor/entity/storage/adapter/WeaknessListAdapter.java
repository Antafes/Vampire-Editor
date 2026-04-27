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

import antafes.vampireEditor.entity.character.Weakness;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.entity.storage.WeaknessStorage;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * JAXB adapter: {@code List<Weakness>} ↔ list of key strings inside a {@code <weaknesses>} wrapper.
 */
public class WeaknessListAdapter extends XmlAdapter<WeaknessListAdapter.KeyList, List<Weakness>> {

    /** JAXB value type – list of weakness key strings. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "weaknessKeyListXml")
    public static class KeyList {
        @XmlElement(name = "weakness")
        public List<String> keys = new ArrayList<>();
    }

    @Override
    public List<Weakness> unmarshal(KeyList v) {
        if (v == null || v.keys == null) return new ArrayList<>();
        WeaknessStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.WEAKNESS);
        List<Weakness> result = new ArrayList<>();
        for (String key : v.keys) {
            try {
                result.add(storage.getEntity(key));
            } catch (EntityStorageException e) {
                Logger.getLogger(WeaknessListAdapter.class.getName())
                        .log(Level.WARNING, "Weakness key ''{0}'' not found", key);
            }
        }
        return result;
    }

    @Override
    public KeyList marshal(List<Weakness> v) {
        KeyList list = new KeyList();
        if (v != null) {
            list.keys = v.stream().map(Weakness::getKey).collect(Collectors.toList());
        }
        return list;
    }
}



