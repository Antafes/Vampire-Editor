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

import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.RoadStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter: {@code Road} ↔ {@code <road key="...">value</road>} node.
 */
public class RoadXmlAdapter extends XmlAdapter<RoadXmlAdapter.RoadXml, Road> {

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoadXml {
        @XmlAttribute(name = "key")
        public String key;

        @XmlValue
        public Integer value;
    }

    @Override
    public Road unmarshal(RoadXml v) {
        if (v == null || v.key == null) return null;
        RoadStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
        try {
            return storage.getEntity(v.key)
                    .toBuilder()
                    .setValue(v.value != null ? v.value : 0)
                    .build();
        } catch (EntityStorageException e) {
            throw new RuntimeException("Could not find road with key: " + v.key, e);
        }
    }

    @Override
    public RoadXml marshal(Road road) {
        if (road == null) return null;
        RoadXml xml = new RoadXml();
        xml.key = road.getKey();
        xml.value = road.getValue();
        return xml;
    }
}


