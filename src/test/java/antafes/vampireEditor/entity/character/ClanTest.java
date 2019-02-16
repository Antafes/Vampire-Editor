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
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.EntityException;
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
    public void setUp() throws EntityException {
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
        final HashMap actual = this.clan.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test clan";
        final String actual = this.clan.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNicknames() {
        final HashMap actual = this.clan.getNicknames();

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
        final ArrayList actual = this.clan.getAdvantages();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.size(), 1);
    }

    public void testGetWeaknesses() {
        final ArrayList actual = this.clan.getWeaknesses();

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
        Assert.assertFalse(this.clan.equals(null));
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.clan, "");
    }

    public void testDifferentClan() throws EntityException {
        final Clan object = new Clan.Builder()
            .setKey("testClan2")
            .addName(Configuration.Language.ENGLISH, "Test clan 2")
            .addNickname(Configuration.Language.ENGLISH, "Test nickname 2")
            .addAdvantage(VampireEditor.getAdvantage("contacts"))
            .addWeakness(VampireEditor.getWeakness("diableryTraces"))
            .build();

        Assert.assertNotEquals(this.clan, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.clan.hashCode();
        final int actual = new Clan.Builder()
            .fillDataFromObject(this.clan)
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nicknames for entity.*")
    public void testBuilderNullNicknames() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setNicknames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing nicknames for entity.*")
    public void testBuilderEmptyNicknames() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setNicknames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing advantages for entity.*")
    public void testBuilderNullAdvantages() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setAdvantages(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing advantages for entity.*")
    public void testBuilderEmptyAdvantages() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setAdvantages(new ArrayList<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing weaknesses for entity.*")
    public void testBuilderNullWeaknesses() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setWeaknesses(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing weaknesses for entity.*")
    public void testBuilderEmptyWeaknesses() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .setWeaknesses(new ArrayList<>())
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        new Clan.Builder()
            .fillDataFromObject(this.clan)
            .build();
    }
}