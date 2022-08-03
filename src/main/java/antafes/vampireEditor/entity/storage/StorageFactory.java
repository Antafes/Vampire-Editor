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

import antafes.vampireEditor.Configuration;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for fetching certain storages.
 */
public class StorageFactory {
    private static final HashMap<StorageType, BaseStorage> storages = new HashMap<>();

    public enum StorageType {
        ABILITY ("antafes.vampireEditor.entity.storage.AbilityStorage"),
        ADVANTAGE ("antafes.vampireEditor.entity.storage.AdvantageStorage"),
        ATTRIBUTE ("antafes.vampireEditor.entity.storage.AttributeStorage"),
        WEAKNESS ("antafes.vampireEditor.entity.storage.WeaknessStorage"),
        CLAN ("antafes.vampireEditor.entity.storage.ClanStorage"),
        MERIT ("antafes.vampireEditor.entity.storage.MeritStorage"),
        FLAW ("antafes.vampireEditor.entity.storage.FlawStorage"),
        GENERATION ("antafes.vampireEditor.entity.storage.GenerationStorage"),
        ROAD ("antafes.vampireEditor.entity.storage.RoadStorage"),
        CHARACTER ("antafes.vampireEditor.entity.storage.CharacterStorage"),
        EMPTY ("antafes.vampireEditore.entity.storage.EmptyEntityStorage");

        private final String storageClass;

        /**
         * Constructor
         *
         * @param storageClass The storage class name
         */
        StorageType(String storageClass) {
            this.storageClass = storageClass;
        }

        /**
         * Get the storage class name.
         *
         * @return
         */
        public String getStorageClass() {
            return storageClass;
        }
    }

    /**
     * Warm up the storages to contain every available data.
     */
    public static void storageWarmUp() {
        for (StorageType type : StorageType.values()) {
            try {
                BaseStorage storage = (BaseStorage) Class.forName(type.getStorageClass()).newInstance();
                storage.init();
                StorageFactory.storages.put(type, storage);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, e);
            }
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
