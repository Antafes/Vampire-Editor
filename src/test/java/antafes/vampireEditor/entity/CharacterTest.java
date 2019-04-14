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

package antafes.vampireEditor.entity;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.*;
import antafes.vampireEditor.entity.storage.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

@Test
public class CharacterTest {
    private Character character;

    @BeforeMethod
    public void setUp() {
        new VampireEditor();
        Configuration configuration = Configuration.getInstance();
        configuration.loadProperties();
        configuration.setLanguage(Configuration.Language.ENGLISH);
        this.character = TestCharacterUtility.createTestCharacter();
    }

    @AfterMethod
    public void tearDown() {
        this.character = null;
    }

    public void testSexEnum() {
        final String expected = "male";
        final String actual = Character.Sex.MALE.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testGetId() {
        Assert.assertEquals(this.character.getId(), UUID.fromString("8ddb1360-316b-11e9-b210-d663bd873d93"));
    }

    public void testGetNewId() throws EntityException {
        Character character = new Character.Builder()
            .fillDataFromObject(this.character)
            .setId(null)
            .build();
        final UUID actual = character.getId();

        Assert.assertNotNull(actual);
        Assert.assertNotEquals(actual, UUID.fromString("8ddb1360-316b-11e9-b210-d663bd873d93"));
    }

    public void testGetName() {
        Assert.assertEquals(this.character.getName(), "Test Character");
    }

    public void testGetClan() throws EntityStorageException {
        ClanStorage clanStorage = (ClanStorage) StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
        Assert.assertEquals(this.character.getClan(), clanStorage.getEntity("brujah"));
    }

    public void testGetGeneration() throws EntityStorageException {
        GenerationStorage generationStorage = (GenerationStorage) StorageFactory.getStorage(StorageFactory.StorageType.GENERATION);
        Assert.assertEquals(this.character.getGeneration(), generationStorage.getEntity(4));
    }

    public void testGetChronicle() {
        Assert.assertEquals(this.character.getChronicle(), "Test chronicle");
    }

    public void testGetNullChronicle() throws EntityException {
        Character character = new Character.Builder()
            .fillDataFromObject(this.character)
            .setChronicle(null)
            .build();

        Assert.assertNull(character.getChronicle());
    }

    public void testGetEmptyChronicle() throws EntityException {
        Character character = new Character.Builder()
            .fillDataFromObject(this.character)
            .setChronicle("")
            .build();

        Assert.assertTrue(character.getChronicle().isEmpty());
    }

    public void testGetExperience() {
        Assert.assertEquals(this.character.getExperience(), 23);
    }

    public void testGetNature() {
        Assert.assertEquals(this.character.getNature(), "wise");
    }

    public void testGetHideout() {
        Assert.assertEquals(this.character.getHideout(), "Test hideout");
    }

    public void testGetPlayer() {
        Assert.assertEquals(this.character.getPlayer(), "Test player");
    }

    public void testGetDemeanor() {
        Assert.assertEquals(this.character.getDemeanor(), "strict");
    }

    public void testGetConcept() {
        Assert.assertEquals(this.character.getConcept(), "Really no concept!");
    }

    public void testGetSire() {
        Assert.assertEquals(this.character.getSire(), "Sir Testing");
    }

    public void testGetSect() {
        Assert.assertEquals(this.character.getSect(), "Test sect");
    }

    public void testGetAttributes() {
        final ArrayList actual = this.character.getAttributes();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.size(), 9);
    }

    public void testGetAttributesByTypePhysical() {
        final ArrayList<Attribute> actual = this.character.getAttributesByType(AttributeInterface.AttributeType.PHYSICAL);

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Attribute attribute : actual) {
            assertEquals(attribute.getClass(), Attribute.class);
            Assert.assertEquals(attribute.getType(), AttributeInterface.AttributeType.PHYSICAL);
        }
    }

    public void testGetAttributesByTypeMental() {
        final ArrayList<Attribute> actual = this.character.getAttributesByType(AttributeInterface.AttributeType.MENTAL);

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Attribute attribute : actual) {
            assertEquals(attribute.getClass(), Attribute.class);
            Assert.assertEquals(attribute.getType(), AttributeInterface.AttributeType.MENTAL);
        }
    }

    public void testGetAttributesByTypeSocial() {
        final ArrayList<Attribute> actual = this.character.getAttributesByType(AttributeInterface.AttributeType.SOCIAL);

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Attribute attribute : actual) {
            assertEquals(attribute.getClass(), Attribute.class);
            Assert.assertEquals(attribute.getType(), AttributeInterface.AttributeType.SOCIAL);
        }
    }

    public void testGetAbilities() {
        final ArrayList actual = this.character.getAbilities();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetAbilitiesByType() {
        final ArrayList<Ability> actual = this.character.getAbilitiesByType(AbilityInterface.AbilityType.TALENT);

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Ability ability : actual) {
            assertEquals(ability.getClass(), Ability.class);
            Assert.assertEquals(ability.getType(), AbilityInterface.AbilityType.TALENT);
        }
    }

    public void testGetAdvantages() {
        final ArrayList actual = this.character.getAdvantages();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetAdvantagesByType() {
        final ArrayList<Advantage> actual = this.character.getAdvantagesByType(AdvantageInterface.AdvantageType.BACKGROUND);

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Advantage advantage : actual) {
            assertEquals(advantage.getClass(), Advantage.class);
            Assert.assertEquals(advantage.getType(), AdvantageInterface.AdvantageType.BACKGROUND);
        }
    }

    public void testGetMerits() {
        final ArrayList<Merit> actual = this.character.getMerits();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Merit merit : actual) {
            assertEquals(merit.getClass(), Merit.class);
        }
    }

    public void testGetFlaws() {
        final ArrayList<Flaw> actual = this.character.getFlaws();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());

        for (Flaw flaw : actual) {
            Assert.assertEquals(flaw.getClass(), Flaw.class);
        }
    }

    public void testGetRoad() throws EntityException, EntityStorageException {
        RoadStorage roadStorage = (RoadStorage) StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
        final Road expected = new Road.Builder().fillDataFromObject(roadStorage.getEntity("roadOfHumanity"))
            .setValue(5)
            .build();
        final Road actual = this.character.getRoad();

        Assert.assertEquals(actual, expected);
    }

    public void testGetWillpower() {
        Assert.assertEquals(this.character.getWillpower(), 5);
    }

    public void testGetUsedWillpower() {
        Assert.assertEquals(this.character.getUsedWillpower(), 1);
    }

    public void testGetBloodPool() {
        Assert.assertEquals(this.character.getBloodPool(), 3);
    }

    public void testGetAge() {
        Assert.assertEquals(this.character.getAge(), 34);
    }

    public void testGetApparentAge() {
        Assert.assertEquals(this.character.getApparentAge(), 22);
    }

    public void testGetDayOfBirth() {
        final Date expected = new GregorianCalendar(1200, 8, 23).getTime();
        final Date actual = this.character.getDayOfBirth();

        Assert.assertEquals(actual, expected);
    }

    public void testGetDayOfDeath() {
        final Date expected = new GregorianCalendar(1400, 3, 23).getTime();
        final Date actual = this.character.getDayOfDeath();

        Assert.assertEquals(actual, expected);
    }

    public void testGetHairColor() {
        Assert.assertEquals(this.character.getHairColor(), "black");
    }

    public void testGetEyeColor() {
        Assert.assertEquals(this.character.getEyeColor(), "green");
    }

    public void testGetSkinColor() {
        Assert.assertEquals(this.character.getSkinColor(), "white");
    }

    public void testGetNationality() {
        Assert.assertEquals(this.character.getNationality(), "German");
    }

    public void testGetHeight() {
        Assert.assertEquals(this.character.getHeight(), 177);
    }

    public void testGetWeight() {
        Assert.assertEquals(this.character.getWeight(), 90);
    }

    public void testGetSex() {
        final Character.Sex expected = Character.Sex.MALE;
        final Character.Sex actual = this.character.getSex();

        Assert.assertEquals(actual, expected);
    }

    public void testGetStory() {
        Assert.assertEquals(this.character.getStory(), "Test story");
    }

    public void testGetDescription() {
        Assert.assertEquals(this.character.getDescription(), "Test description");
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing name")
    public void testBuilderEmptyName() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setName("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing name")
    public void testBuilderNullName() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setName(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing clan")
    public void testBuilderEmptyClan() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setClan(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing generation")
    public void testBuilderEmptyGeneration() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setGeneration(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nature")
    public void testBuilderEmptyNature() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setNature("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nature")
    public void testBuilderNullNature() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setNature(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing demeanor")
    public void testBuilderEmptyDemeanor() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setDemeanor("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing demeanor")
    public void testBuilderNullDemeanor() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setDemeanor(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing concept")
    public void testBuilderEmptyConcept() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setConcept("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing concept")
    public void testBuilderNullConcept() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setConcept(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Attributes are empty")
    public void testBuilderEmptyAttributes() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAttributes(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Abilities are empty")
    public void testBuilderEmptyAbilities() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAbilities(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Advantages are empty")
    public void testBuilderEmptyAdvantages() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAdvantages(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing attributes")
    public void testBuilderMissingAttributes() throws EntityException {
        ArrayList list = this.character.getAttributes();
        list.remove(0);
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAttributes(list)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing abilities")
    public void testBuilderMissingAbilities() throws EntityException {
        ArrayList list = this.character.getAbilities();
        list.remove(0);
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAbilities(list)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing advantages")
    public void testBuilderMissingAdvantages() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAdvantages(new ArrayList<>(this.character.getAdvantages().subList(0, 4)))
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Too many attributes")
    public void testBuilderTooManyAttributes() throws EntityException, EntityStorageException {
        ArrayList list = this.character.getAttributes();
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        list.add(storage.getEntity("strength"));
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAttributes(list)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Too many abilities")
    public void testBuilderTooManyAbilities() throws EntityException, EntityStorageException {
        ArrayList list = this.character.getAbilities();
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);
        list.add(storage.getEntity("alertness"));
        new Character.Builder()
            .fillDataFromObject(this.character)
            .setAbilities(list)
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        new Character.Builder()
            .fillDataFromObject(this.character)
            .build();
    }
}