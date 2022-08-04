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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.entity.BaseTypedTranslatedEntity;
import antafes.vampireEditor.entity.character.EntityTypeInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class BaseTypedStorage<C extends BaseTypedTranslatedEntity, T extends EntityTypeInterface> extends BaseStorage<C>
{
    /**
     * Fetch a subset of abilities from the storage.
     *
     * @param type The type of abilities to fetch
     * @return Map of abilities
     */
    public HashMap<String, C> getEntityMapByType(T type)
    {
        HashMap<String, C> list = new HashMap<>();

        this.getList().forEach((String key, C entity) -> {
            if (Objects.equals(type, entity.getType())) {
                list.put(key, entity);
            }
        });

        return list;
    }

    /**
     * Fetch a subset of abilities from the storage.
     *
     * @param type The type of abilities to fetch
     * @return List of abilities
     */
    public ArrayList<C> getEntityListByType(T type)
    {
        return new ArrayList<>(this.getEntityMapByType(type).values());
    }
}
