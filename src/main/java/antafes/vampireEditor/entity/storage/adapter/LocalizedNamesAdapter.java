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
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 */
package antafes.vampireEditor.entity.storage.adapter;

import antafes.vampireEditor.Configuration;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.HashMap;

/**
 * JAXB adapter for the localized names map to/from {@code <English>/<German>} children
 * wrapped inside a {@code <name>} element.
 */
public class LocalizedNamesAdapter
        extends XmlAdapter<LocalizedNamesAdapter.LocalizedNameXml, HashMap<Configuration.Language, String>> {

    /** JAXB value type representing a localized name element. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "localizedNamesXml")
    public static class LocalizedNameXml {
        @XmlElement(name = "English")
        public String english;

        @XmlElement(name = "German")
        public String german;
    }

    @Override
    public HashMap<Configuration.Language, String> unmarshal(LocalizedNameXml v) {
        if (v == null) return null;
        HashMap<Configuration.Language, String> map = new HashMap<>();
        if (v.english != null) map.put(Configuration.Language.ENGLISH, v.english);
        if (v.german != null) map.put(Configuration.Language.GERMAN, v.german);
        return map;
    }

    @Override
    public LocalizedNameXml marshal(HashMap<Configuration.Language, String> v) {
        if (v == null) return null;
        LocalizedNameXml xml = new LocalizedNameXml();
        xml.english = v.get(Configuration.Language.ENGLISH);
        xml.german = v.get(Configuration.Language.GERMAN);
        return xml;
    }
}



