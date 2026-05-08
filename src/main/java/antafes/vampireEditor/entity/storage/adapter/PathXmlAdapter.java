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
 * JAXB adapter: {@code Road} (path) ↔ {@code <path key="...">value</path>} node.
 * Similar to RoadXmlAdapter but for path selection (a child road of the main road).
 */
public class PathXmlAdapter extends XmlAdapter<PathXmlAdapter.PathXml, Road> {

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PathXml {
        @XmlAttribute(name = "key")
        public String key;

        @XmlValue
        public Integer value;
    }

    @Override
    public Road unmarshal(PathXml v) {
        if (v == null || v.key == null) return null;
        RoadStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
        try {
            Road road = storage.getEntity(v.key);
            if (road.getParent() == null) {
                throw new RuntimeException(
                    "Key '" + v.key + "' refers to a top-level road, not a path. "
                        + "Only child roads (those with a parent) are valid in <path key=\"...\">."
                );
            }
            return road.toBuilder()
                    .setValue(v.value != null ? v.value : 0)
                    .build();
        } catch (EntityStorageException e) {
            throw new RuntimeException(
                "Unknown path key '" + v.key + "' in character XML. "
                    + "Please select an existing path key in <path key=\"...\"> or update roads data.",
                e
            );
        }
    }

    @Override
    public PathXml marshal(Road path) {
        if (path == null) return null;
        PathXml xml = new PathXml();
        xml.key = path.getKey();
        xml.value = path.getValue();
        return xml;
    }
}
