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

import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
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
 * JAXB adapter: {@code List<Advantage>} ↔ list of key strings inside an {@code <advantages>} wrapper.
 */
public class AdvantageListAdapter extends XmlAdapter<AdvantageListAdapter.KeyList, List<Advantage>> {

    /** JAXB value type – list of advantage key strings. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "advantageKeyListXml")
    public static class KeyList {
        @XmlElement(name = "advantage")
        public List<String> keys = new ArrayList<>();
    }

    @Override
    public List<Advantage> unmarshal(KeyList v) {
        if (v == null || v.keys == null) return new ArrayList<>();
        AdvantageStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        List<Advantage> result = new ArrayList<>();
        for (String key : v.keys) {
            try {
                result.add(storage.getEntity(key));
            } catch (EntityStorageException e) {
                Logger.getLogger(AdvantageListAdapter.class.getName())
                        .log(Level.WARNING, "Advantage key ''{0}'' not found", key);
            }
        }
        return result;
    }

    @Override
    public KeyList marshal(List<Advantage> v) {
        KeyList list = new KeyList();
        if (v != null) {
            list.keys = v.stream().map(Advantage::getKey).collect(Collectors.toList());
        }
        return list;
    }
}



