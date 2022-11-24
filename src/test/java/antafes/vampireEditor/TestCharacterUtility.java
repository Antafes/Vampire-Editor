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

package antafes.vampireEditor;

import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.storage.*;
import antafes.vampireEditor.gui.BaseWindow;

import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test character utility class.
 */
public class TestCharacterUtility {
    /**
     * This method will create a test character, that can be used for tests and to check the gui.
     *
     * @return A test character
     */
    public static antafes.vampireEditor.entity.Character createTestCharacter() {
        try {
            ClanStorage clanStorage = (ClanStorage) StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
            GenerationStorage generationStorage = (GenerationStorage) StorageFactory
                .getStorage(StorageFactory.StorageType.GENERATION);
            NatureStorage natureStorage = (NatureStorage) StorageFactory.getStorage(StorageFactory.StorageType.NATURE);
            RoadStorage roadStorage = (RoadStorage) StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
            GregorianCalendar calendarBirth = new GregorianCalendar(1200, 8, 23);
            GregorianCalendar calendarDeath = new GregorianCalendar(1400, 3, 23);
            Character.CharacterBuilder<?, ?> builder = Character.builder()
                .setId(UUID.fromString("8ddb1360-316b-11e9-b210-d663bd873d93"))
                .setName("Test Character")
                .setGeneration(generationStorage.getEntity(4))
                .setChronicle("Test chronicle")
                .setNature(natureStorage.getEntity("wise"))
                .setDemeanor("strict")
                .setConcept("Really no concept!")
                .setHideout("Test hideout")
                .setSire("Sir Testing")
                .setSect("Test sect")
                .setPlayer("Test player")
                .setClan(clanStorage.getEntity("brujah"))
                .setSex(antafes.vampireEditor.entity.Character.Sex.MALE)
                .setRoad(roadStorage.getEntity("roadOfHumanity"))
                .setWillpower(5)
                .setUsedWillpower(1)
                .setBloodPool(3)
                .setExperience(23)
                .setAge(34)
                .setApparentAge(22)
                .setDayOfBirth(calendarBirth.getTime())
                .setDayOfDeath(calendarDeath.getTime())
                .setHairColor("black")
                .setEyeColor("green")
                .setSkinColor("white")
                .setNationality("German")
                .setHeight(177)
                .setWeight(90)
                .setStory("Test story")
                .setDescription("Test description");

            TestCharacterUtility.addAttributes(builder);
            TestCharacterUtility.addAbilities(builder);

            AdvantageStorage advantageStorage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
            builder.addAdvantage(advantageStorage.getEntity("allies").toBuilder().setValue(3).build());
            builder.addAdvantage(advantageStorage.getEntity("influence").toBuilder().setValue(2).build());
            builder.addAdvantage(advantageStorage.getEntity("auspex").toBuilder().setValue(2).build());
            builder.addAdvantage(advantageStorage.getEntity("celerity").toBuilder().setValue(1).build());
            builder.addAdvantage(advantageStorage.getEntity("dominate").toBuilder().setValue(1).build());
            builder.addAdvantage(advantageStorage.getEntity("conscience").toBuilder().setValue(3).build());
            builder.addAdvantage(advantageStorage.getEntity("courage").toBuilder().setValue(2).build());
            builder.addAdvantage(advantageStorage.getEntity("self-control").toBuilder().setValue(2).build());

            MeritStorage meritStorage = (MeritStorage) StorageFactory.getStorage(StorageFactory.StorageType.MERIT);
            FlawStorage flawStorage = (FlawStorage) StorageFactory.getStorage(StorageFactory.StorageType.FLAW);
            builder.addFlaw(flawStorage.getEntity("monstrous"));
            builder.addFlaw(flawStorage.getEntity("deepSleeper"));
            builder.addMerit(meritStorage.getEntity("commonSense"));
            builder.addMerit(meritStorage.getEntity("eideticMemory"));
            builder.setRoad(roadStorage.getEntity("roadOfHumanity").toBuilder().setValue(5).build());

            return builder.build();
        } catch (EntityException | EntityStorageException ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void addAttributes(Character.CharacterBuilder<?, ?> builder) throws EntityException, EntityStorageException {
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        builder.addAttribute(storage.getEntity("strength").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("dexterity").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("stamina").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("charisma").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("appearance").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("manipulation").toBuilder().setValue(2).build());
        builder.addAttribute(storage.getEntity("intelligence").toBuilder().setValue(2).build());
        builder.addAttribute(storage.getEntity("perception").toBuilder().setValue(3).build());
        builder.addAttribute(storage.getEntity("wits").toBuilder().setValue(1).build());
    }

    private static void addAbilities(Character.CharacterBuilder<?, ?> builder) {
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);

        storage.getList().forEach(((s, ability) -> {
                builder.addAbility((ability).toBuilder().setValue(3).build());
        }));
    }
}
