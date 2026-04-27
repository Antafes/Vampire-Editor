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

import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.ClanStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter: {@code Clan} ↔ clan key {@code String}.
 */
public class ClanKeyAdapter extends XmlAdapter<String, Clan> {

    @Override
    public Clan unmarshal(String key) {
        if (key == null || key.isEmpty()) return null;
        ClanStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
        try {
            return storage.getEntity(key);
        } catch (EntityStorageException e) {
            throw new RuntimeException("Could not find clan with key: " + key, e);
        }
    }

    @Override
    public String marshal(Clan clan) {
        return clan != null ? clan.getKey() : null;
    }
}


