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

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * Factory for fetching certain storages.
 */
@Component
@RequiredArgsConstructor
public class StorageFactory {
    private static final HashMap<StorageType, BaseStorage<?>> storages = new HashMap<>();
    private static StorageFactory instance;

    private final AbilityStorage abilityStorage;
    private final AdvantageStorage advantageStorage;
    private final AttributeStorage attributeStorage;
    private final WeaknessStorage weaknessStorage;
    private final ClanStorage clanStorage;
    private final MeritStorage meritStorage;
    private final FlawStorage flawStorage;
    private final GenerationStorage generationStorage;
    private final RoadStorage roadStorage;
    private final NatureStorage natureStorage;
    private final CharacterStorage characterStorage;
    private final EmptyEntityStorage emptyEntityStorage;

    @Getter
    public enum StorageType {
        ABILITY (AbilityStorage.class),
        ADVANTAGE (AdvantageStorage.class),
        ATTRIBUTE (AttributeStorage.class),
        WEAKNESS (WeaknessStorage.class),
        CLAN (ClanStorage.class),
        MERIT (MeritStorage.class),
        FLAW (FlawStorage.class),
        GENERATION (GenerationStorage.class),
        ROAD (RoadStorage.class),
        NATURE (NatureStorage.class),
        CHARACTER (CharacterStorage.class),
        EMPTY (EmptyEntityStorage.class);

        private final Class<?> storageClass;

        StorageType(Class<?> storageClass) {
            this.storageClass = storageClass;
        }

    }

    @PostConstruct
    public void initializeAutowiredStorages()
    {
        StorageFactory.instance = this;
        if (!StorageFactory.storages.isEmpty()) {
            return;
        }

        EnumMap<StorageType, BaseStorage<?>> autowiredStorages = new EnumMap<>(StorageType.class);
        autowiredStorages.put(StorageType.ABILITY, this.abilityStorage);
        autowiredStorages.put(StorageType.ADVANTAGE, this.advantageStorage);
        autowiredStorages.put(StorageType.ATTRIBUTE, this.attributeStorage);
        autowiredStorages.put(StorageType.WEAKNESS, this.weaknessStorage);
        autowiredStorages.put(StorageType.CLAN, this.clanStorage);
        autowiredStorages.put(StorageType.MERIT, this.meritStorage);
        autowiredStorages.put(StorageType.FLAW, this.flawStorage);
        autowiredStorages.put(StorageType.GENERATION, this.generationStorage);
        autowiredStorages.put(StorageType.ROAD, this.roadStorage);
        autowiredStorages.put(StorageType.NATURE, this.natureStorage);
        autowiredStorages.put(StorageType.CHARACTER, this.characterStorage);
        autowiredStorages.put(StorageType.EMPTY, this.emptyEntityStorage);

        autowiredStorages.forEach(StorageFactory::putAndInitializeStorage);
    }

    /**
     * Warm up the storages to contain every available data.
     *
     * @throws IllegalStateException if the Spring-managed instance has not been initialized yet
     */
    public static void storageWarmUp() {
        if (!StorageFactory.storages.isEmpty()) {
            return;
        }

        if (StorageFactory.instance == null) {
            throw new IllegalStateException(
                "StorageFactory has not been initialized by Spring. "
                    + "Ensure the application context is started before calling storageWarmUp()."
            );
        }

        StorageFactory.instance.initializeAutowiredStorages();
    }

    private static void putAndInitializeStorage(StorageType type, BaseStorage<?> storage)
    {
        storage.init();
        StorageFactory.storages.put(type, storage);
    }

    /**
     * Get a certain storage.
     *
     * @param type The type of storage to fetch.
     *
     * @return The storage
     */
    public static <S extends BaseStorage<?>> S getStorage(StorageType type) {
        if (StorageFactory.storages.isEmpty()) {
            StorageFactory.storageWarmUp();
        }

        return (S) type.storageClass.cast(StorageFactory.storages.get(type));
    }
}
