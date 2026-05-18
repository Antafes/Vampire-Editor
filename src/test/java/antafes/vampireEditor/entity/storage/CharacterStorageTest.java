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

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.xml.validation.XsdValidator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Test
public class CharacterStorageTest extends BaseTest
{
    private CharacterStorage characterStorage;
    private final String saveDir = System.getProperty("user.home") + "/.vampire/testSave";
    private final String filename = "TestCharacter.xml";

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        new VampireEditor();
        Configuration configuration = Configuration.getInstance();
        configuration.loadProperties();
        configuration.setSaveDirPath(this.saveDir);
        configuration.setOpenDirPath(this.saveDir);

        try {
            Files.createDirectories(Paths.get(this.saveDir));
        } catch (Exception e) {
            Assert.fail("Could not create test save directory", e);
        }

        this.characterStorage = new CharacterStorage();
    }

    @AfterMethod
    public void tearDown() {
        this.characterStorage = null;
    }

    public void testSave() {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        File file = new File(this.saveDir + "/" + this.filename);

        Assert.assertTrue(file.exists());
    }

    public void testLoad() throws Exception {
        final Character expected = TestCharacterUtility.createTestCharacter();
        this.characterStorage.save(expected, this.filename);
        File file = new File(this.saveDir + "/" + this.filename);

        if (file.exists()) {
            final Character actual = this.characterStorage.load(this.filename);

            Assert.assertEquals(actual, expected);
        }
    }

    public void testSaveAndLoadWithPath() throws Exception {
        RoadStorage roadStorage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
        Character expected = Objects.requireNonNull(TestCharacterUtility.createTestCharacter()).toBuilder()
            .setRoad(roadStorage.getEntity("roadOfBeast").toBuilder().setValue(4).build())
            .setPath(roadStorage.getEntity("pathOfHunter").toBuilder().setValue(4).build())
            .build();

        this.characterStorage.save(expected, this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);
        String xml = Files.readString(filePath, StandardCharsets.UTF_8);
        Character actual = this.characterStorage.load(this.filename);
        int expectedScore = expected.getRoad().getValue();

        Assert.assertTrue(xml.contains("<road key=\"roadOfBeast\">" + expectedScore + "</road>"));
        Assert.assertTrue(xml.contains("<path key=\"pathOfHunter\">" + expectedScore + "</path>"));
        Assert.assertNotNull(actual.getPath());
        Assert.assertEquals(actual.getRoad().getValue(), actual.getPath().getValue());
        Assert.assertEquals(actual, expected);
    }

    public void testSaveWithoutPathOmitsPathElementAndLoads() throws Exception {
        Character expected = Objects.requireNonNull(TestCharacterUtility.createTestCharacter()).toBuilder()
            .setPath(null)
            .build();

        this.characterStorage.save(expected, this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);
        String xml = Files.readString(filePath, StandardCharsets.UTF_8);
        Character actual = this.characterStorage.load(this.filename);

        Assert.assertFalse(xml.contains("<path"));
        Assert.assertNull(actual.getPath());
        Assert.assertEquals(actual, expected);
    }

    public void testLoadWithoutSex() throws Exception {
        final Character expected = Objects.requireNonNull(TestCharacterUtility.createTestCharacter()).toBuilder()
            .setSex(null)
            .build();
        this.characterStorage.save(expected, this.filename);
        File file = new File(this.saveDir + "/" + this.filename);

        Assert.assertTrue(file.exists());
        final Character actual = this.characterStorage.load(this.filename);

        Assert.assertEquals(actual, expected);
        Assert.assertNull(actual.getSex());
    }

    public void testSaveNpcWithoutNatureIsXsdValid() throws Exception {
        Character npcWithoutNature = Objects.requireNonNull(TestCharacterUtility.createTestCharacter()).toBuilder()
            .setNpc(true)
            .setNature(null)
            .setClan(null)
            .setRoad(null)
            .build();

        this.characterStorage.save(npcWithoutNature, this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(filePath.toFile(), schemaInputStream);
        }
    }

    public void testSaveUsesDirectoryPreparedByTest() {
        Path missingSaveDir = Paths.get(this.saveDir, "missing-" + System.nanoTime());
        Configuration configuration = Configuration.getInstance();
        configuration.setSaveDirPath(missingSaveDir.toString());
        configuration.setOpenDirPath(missingSaveDir.toString());

        Assert.assertFalse(Files.exists(missingSaveDir));
        try {
            Files.createDirectories(missingSaveDir);
        } catch (Exception e) {
            Assert.fail("Could not create dedicated test save directory", e);
        }

        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);

        Assert.assertTrue(Files.isDirectory(missingSaveDir));
        Assert.assertTrue(Files.exists(missingSaveDir.resolve(this.filename)));
    }

    @Test(expectedExceptions = EntityStorageException.class, expectedExceptionsMessageRegExp = "Could not load character '.*'!")
    public void testLoadFailed() throws Exception {
        this.characterStorage.load("path/to/not/existing/file.xml");
    }

    @Test(expectedExceptions = EntityStorageException.class, expectedExceptionsMessageRegExp = "Character file '.*' failed XSD validation!")
    public void testLoadFailedMissingRoad() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = Files.readString(filePath);
        String xmlWithoutRoad = xml.replaceFirst("(?s)<road[^>]*>.*?</road>\\s*", "");

        Assert.assertNotEquals(xmlWithoutRoad, xml, "Test fixture corruption failed: road tag was not removed.");
        Files.writeString(filePath, xmlWithoutRoad);

        this.characterStorage.load(this.filename);
    }

    @Test(expectedExceptions = EntityStorageException.class, expectedExceptionsMessageRegExp = "Character file '.*' failed XSD validation!")
    public void testLoadFailedMissingClan() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = Files.readString(filePath);
        String xmlWithoutClan = xml.replaceFirst("(?s)<clan>.*?</clan>\\s*", "");

        Assert.assertNotEquals(xmlWithoutClan, xml, "Test fixture corruption failed: clan tag was not removed.");
        Files.writeString(filePath, xmlWithoutClan);

        this.characterStorage.load(this.filename);
    }

    @Test(expectedExceptions = EntityStorageException.class, expectedExceptionsMessageRegExp = "Character file '.*' failed XSD validation!")
    public void testLoadFailsForXmlWithWrongElementType() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = Files.readString(filePath);
        String invalidXml = xml.replaceFirst("<generation>\\d+</generation>", "<generation>invalid</generation>");

        Assert.assertNotEquals(invalidXml, xml, "Test fixture corruption failed: generation tag was not replaced.");
        Files.writeString(filePath, invalidXml);

        this.characterStorage.load(this.filename);
    }

    @Test(expectedExceptions = EntityStorageException.class, expectedExceptionsMessageRegExp = "Character file '.*' failed XSD validation!")
    public void testLoadFailsForXmlMissingRequiredElement() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = Files.readString(filePath);
        String invalidXml = xml.replaceFirst("(?s)<generation>.*?</generation>\\s*", "");

        Assert.assertNotEquals(invalidXml, xml, "Test fixture corruption failed: generation tag was not removed.");
        Files.writeString(filePath, invalidXml);

        this.characterStorage.load(this.filename);
    }

    public void testSaveProducesXsdValidFile() throws Exception {
        Character expected = TestCharacterUtility.createTestCharacter();
        this.characterStorage.save(expected, this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        Assert.assertTrue(Files.exists(filePath));
        try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(filePath.toFile(), schemaInputStream);
        }
    }

    public void testLoadNpcWithoutClanAndRoad() throws Exception {
        Character npc = Objects.requireNonNull(TestCharacterUtility.createTestCharacter()).toBuilder()
            .setNpc(true)
            .setClan(null)
            .setRoad(null)
            .build();
        this.characterStorage.save(npc, this.filename);

        Character actual = this.characterStorage.load(this.filename);

        Assert.assertTrue(actual.isNpc());
        Assert.assertNull(actual.getClan());
        Assert.assertNull(actual.getRoad());
    }
}
