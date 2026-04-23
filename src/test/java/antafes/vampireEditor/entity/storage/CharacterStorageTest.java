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

import antafes.myXML.XMLValidator;
import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.exception.MissingClanException;
import antafes.vampireEditor.entity.exception.MissingRoadException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        this.characterStorage = new CharacterStorage();
    }

    @AfterMethod
    public void tearDown() {
        this.characterStorage = null;
    }

    public void testSave() {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        File file = new File(this.saveDir + "/" + this.filename);
        XMLValidator validator = new XMLValidator(VampireEditor.getFileInJar("character.xsd"));

        Assert.assertTrue(file.exists());

        // Verbose error, if file couldn't be validated.
        if (!validator.validate(file)) {
            for (int i = 0; i < validator.getExceptionList().size(); i++) {
                System.out.println(validator.getExceptionList().get(i).getMessage());
            }
        }

        Assert.assertTrue(validator.validate(file));
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

    public void testLoadWithoutSex() throws Exception {
        final Character expected = TestCharacterUtility.createTestCharacter().toBuilder()
            .setSex(null)
            .build();
        this.characterStorage.save(expected, this.filename);
        File file = new File(this.saveDir + "/" + this.filename);

        Assert.assertTrue(file.exists());
        final Character actual = this.characterStorage.load(this.filename);

        Assert.assertEquals(actual, expected);
        Assert.assertNull(actual.getSex());
    }

    @Test(expectedExceptions = Exception.class, expectedExceptionsMessageRegExp = "Could not load character.*")
    public void testLoadFailed() throws Exception {
        this.characterStorage.load("path/to/not/existing/file.xml");
    }

    @Test(expectedExceptions = MissingRoadException.class, expectedExceptionsMessageRegExp = "Missing road for non-NPC character!")
    public void testLoadFailedMissingRoad() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        String xmlWithoutRoad = xml.replaceFirst("(?s)<road[^>]*>.*?</road>\\s*", "");

        Assert.assertNotEquals(xmlWithoutRoad, xml, "Test fixture corruption failed: road tag was not removed.");
        Files.write(filePath, xmlWithoutRoad.getBytes(StandardCharsets.UTF_8));

        this.characterStorage.load(this.filename);
    }

    @Test(expectedExceptions = MissingClanException.class, expectedExceptionsMessageRegExp = "Missing clan for non-NPC character!")
    public void testLoadFailedMissingClan() throws Exception {
        this.characterStorage.save(TestCharacterUtility.createTestCharacter(), this.filename);
        Path filePath = Paths.get(this.saveDir, this.filename);

        String xml = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        String xmlWithoutClan = xml.replaceFirst("(?s)<clan>.*?</clan>\\s*", "");

        Assert.assertNotEquals(xmlWithoutClan, xml, "Test fixture corruption failed: clan tag was not removed.");
        Files.write(filePath, xmlWithoutClan.getBytes(StandardCharsets.UTF_8));

        this.characterStorage.load(this.filename);
    }

    public void testLoadNpcWithoutClanAndRoad() throws Exception {
        Character npc = TestCharacterUtility.createTestCharacter().toBuilder()
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
