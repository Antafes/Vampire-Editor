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

import java.util.HashMap;

/**
 * Factory for fetching certain storages.
 */
public class StorageFactory {
    private static final HashMap<StorageType, BaseStorage> storages = new HashMap<>();

    public enum StorageType {
        ABILITY,
        ADVANTAGE,
        CHARACTER;
    }

    /**
     * Warm up the storages to contain every available data.
     */
    public static void storageWarmUp() {
        for (StorageType type : StorageType.values()) {
            BaseStorage storage;

            switch (type) {
                case ABILITY:
                    storage = new AbilityStorage();
                    break;
                case ADVANTAGE:
                    storage = new AdvantageStorage();
                    break;
                case CHARACTER:
                default:
                    storage = new CharacterStorage();
                    break;
            }

            storage.init();
            StorageFactory.storages.put(type, storage);
        }
    }

    /**
     * Get a certain storage.
     *
     * @param type The type of storage to fetch.
     *
     * @return The storage
     */
    public static BaseStorage getStorage(StorageType type) {
        if (StorageFactory.storages.isEmpty()) {
            StorageFactory.storageWarmUp();
        }

        return StorageFactory.storages.get(type);
    }
}
