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

import antafes.vampireEditor.entity.character.Nature;
import antafes.vampireEditor.entity.storage.NatureStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter: {@code Nature} ↔ nature key or name {@code String}.
 * Manual natures are stored by their display name; regular natures by their key.
 */
public class NatureKeyAdapter extends XmlAdapter<String, Nature> {

    @Override
    public Nature unmarshal(String key) {
        if (key == null || key.isEmpty()) return null;
        NatureStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.NATURE);
        try {
            return storage.getEntity(key);
        } catch (Exception e) {
            throw new RuntimeException("Could not find nature with key: " + key, e);
        }
    }

    @Override
    public String marshal(Nature nature) {
        if (nature == null) return null;
        return nature.isManual() ? nature.getName() : nature.getKey();
    }
}


