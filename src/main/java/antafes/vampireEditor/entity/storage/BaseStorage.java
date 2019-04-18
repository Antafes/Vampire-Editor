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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.entity.BaseEntity;
import antafes.vampireEditor.entity.EntityStorageException;

import java.util.HashMap;

/**
 * Base storage object.
 */
public abstract class BaseStorage {
    private final HashMap<String, BaseEntity> list;

    /**
     * Constructor
     */
    BaseStorage() {
        this.list = new HashMap<>();
    }

    /**
     * Initializes the storage and pre-loads available data.
     */
    public abstract void init();

    /**
     * Fetch a single entity for a given key.
     *
     * @param key The key under which to find the entity
     *
     * @return The entity
     */
    public BaseEntity getEntity(String key) throws EntityStorageException {
        if (this.list.containsKey(key)) {
            return this.list.get(key);
        }

        throw new EntityStorageException("Entity for key '" + key + "' not found.");
    }

    /**
     * Get the list of stored entities.
     *
     * @return
     */
    public HashMap<String, BaseEntity> getList() {
        return this.list;
    }
}
