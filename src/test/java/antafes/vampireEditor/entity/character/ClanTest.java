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

package antafes.vampireEditor.entity.character;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.TestClanUtility;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.entity.storage.WeaknessStorage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

@Test
public class ClanTest {
    private Clan clan;

    @BeforeMethod
    public void setUp() throws EntityException, EntityStorageException {
        Configuration.getInstance().loadProperties();
        this.clan = TestClanUtility.createTestClan();
    }

    @AfterMethod
    public void tearDown() {
        this.clan = null;
    }

    public void testGetKey() {
        final String expected = "testClan";
        final String actual = this.clan.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.clan.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test clan";
        final String actual = this.clan.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNicknames() {
        final HashMap<Configuration.Language, String> actual = this.clan.getNicknames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetNickname() {
        final String expected = "Test nickname";
        final String actual = this.clan.getNickname();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNicknameOtherLanguageNotExisting() {
        Configuration.getInstance().setLanguage(Configuration.Language.GERMAN);
        final String expected = "Test nickname";
        final String actual = this.clan.getNickname();

        Assert.assertEquals(actual, expected);
    }

    public void testGetAdvantages() {
        final ArrayList<Advantage> actual = this.clan.getAdvantages();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.size(), 1);
    }

    public void testGetWeaknesses() {
        final ArrayList<Weakness> actual = this.clan.getWeaknesses();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.size(), 1);
    }

    public void testToString() {
        final String expected = "Test clan";
        final String actual = this.clan.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.clan, this.clan);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.clan, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.clan, "");
    }

    public void testDifferentClan() throws EntityException, EntityStorageException {
        AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        WeaknessStorage weaknessStorage = (WeaknessStorage) StorageFactory.getStorage(StorageFactory.StorageType.WEAKNESS);
        final Clan object = Clan.builder()
            .setKey("testClan2")
            .addName(Configuration.Language.ENGLISH, "Test clan 2")
            .addNickname(Configuration.Language.ENGLISH, "Test nickname 2")
            .addAdvantage(storage.getEntity("contacts"))
            .addWeakness(weaknessStorage.getEntity("darkening"))
            .build();

        Assert.assertNotEquals(this.clan, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.clan.hashCode();
        final int actual = this.clan.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.clan.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.clan.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.clan.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.clan.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nicknames for entity.*")
    public void testBuilderNullNicknames() throws EntityException {
        this.clan.toBuilder()
            .setNicknames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nicknames for entity.*")
    public void testBuilderEmptyNicknames() throws EntityException {
        this.clan.toBuilder()
            .setNicknames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing advantages for entity.*")
    public void testBuilderNullAdvantages() throws EntityException {
        this.clan.toBuilder()
            .setAdvantages(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing advantages for entity.*")
    public void testBuilderEmptyAdvantages() throws EntityException {
        this.clan.toBuilder()
            .setAdvantages(new ArrayList<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing weaknesses for entity.*")
    public void testBuilderNullWeaknesses() throws EntityException {
        this.clan.toBuilder()
            .setWeaknesses(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing weaknesses for entity.*")
    public void testBuilderEmptyWeaknesses() throws EntityException {
        this.clan.toBuilder()
            .setWeaknesses(new ArrayList<>())
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.clan.toBuilder()
            .build();
    }
}
