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
import antafes.vampireEditor.entity.character.Ability;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.Attribute;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.storage.AbilityStorage;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.AttributeStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.BaseWindow;
import test.methodselectors.NoTest;

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
    @NoTest
    public static antafes.vampireEditor.entity.Character createTestCharacter() {
        try {
            GregorianCalendar calendarBirth = new GregorianCalendar(1200, 8, 23);
            GregorianCalendar calendarDeath = new GregorianCalendar(1400, 3, 23);
            antafes.vampireEditor.entity.Character.Builder builder = new antafes.vampireEditor.entity.Character.Builder()
                .setId(UUID.fromString("8ddb1360-316b-11e9-b210-d663bd873d93"))
                .setName("Test Character")
                .setGeneration(VampireEditor.getGeneration(4))
                .setChronicle("Test chronicle")
                .setNature("wise")
                .setDemeanor("strict")
                .setConcept("Really no concept!")
                .setHideout("Test hideout")
                .setSire("Sir Testing")
                .setSect("Test sect")
                .setPlayer("Test player")
                .setClan(VampireEditor.getClan("brujah"))
                .setSex(antafes.vampireEditor.entity.Character.Sex.MALE)
                .setRoad(VampireEditor.getRoads().get("roadOfHumanity"))
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

            Advantage.Builder advantageBuilder = new Advantage.Builder();
            AdvantageStorage advantageStorage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("allies"))
                .setValue(3);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("influence"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("auspex"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("celerity"))
                .setValue(1);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("dominate"))
                .setValue(1);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("conscience"))
                .setValue(3);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("courage"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(advantageStorage.getEntity("self-control"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());

            builder.addFlaw(VampireEditor.getFlaws().get("monstrous"));
            builder.addFlaw(VampireEditor.getFlaws().get("deepSleeper"));
            builder.addMerit(VampireEditor.getMerits().get("commonSense"));
            builder.addMerit(VampireEditor.getMerits().get("eideticMemory"));
            Road.Builder roadBuilder = new Road.Builder()
                .fillDataFromObject(VampireEditor.getRoad("roadOfHumanity"))
                .setValue(5);
            builder.setRoad(roadBuilder.build());

            return builder.build();
        } catch (EntityException | EntityStorageException ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void addAttributes(Character.Builder builder) throws EntityException, EntityStorageException {
        Attribute.Builder attributeBuilder = new Attribute.Builder();
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("strength")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("dexterity")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("stamina")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("charisma")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("appearance")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("manipulation")).setValue(2).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("intelligence")).setValue(2).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("perception")).setValue(3).build());
        builder.addAttribute(attributeBuilder.fillDataFromObject(storage.getEntity("wits")).setValue(1).build());
    }

    private static void addAbilities(Character.Builder builder) {
        Ability.Builder abilityBuilder = new Ability.Builder();
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);

        storage.getList().forEach(((s, ability) -> {
            try {
                builder.addAbility(abilityBuilder.fillDataFromObject(ability).setValue(3).build());
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }));
    }
}
