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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB adapter: {@code List<String>} <-> list of clan key strings inside a {@code <clanRestrictions>} wrapper.
 */
public class StringListAdapter extends XmlAdapter<StringListAdapter.KeyList, List<String>> {

    /** JAXB value type - list of key strings. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "stringKeyListXml")
    public static class KeyList {
        @XmlElement(name = "clan")
        public List<String> keys = new ArrayList<>();
    }

    @Override
    public List<String> unmarshal(KeyList v) {
        if (v == null || v.keys == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(v.keys);
    }

    @Override
    public KeyList marshal(List<String> v) {
        KeyList list = new KeyList();
        if (v != null) {
            list.keys = new ArrayList<>(v);
        }

        return list;
    }
}

