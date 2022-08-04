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
import antafes.vampireEditor.entity.EntityException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class AdvantageTest {
    private Advantage advantage;

    @BeforeMethod
    public void setUp() throws EntityException {
        Configuration.getInstance().loadProperties();
        this.advantage = Advantage.builder()
            .setKey("testAdvantage")
            .addName(Configuration.Language.ENGLISH, "Test advantage")
            .setType(AdvantageInterface.AdvantageType.BACKGROUND)
            .setValue(11)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.advantage = null;
    }

    public void testGetKey() {
        final String expected = "testAdvantage";
        final String actual = this.advantage.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.advantage.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test advantage";
        final String actual = this.advantage.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetType() {
        final AdvantageInterface.AdvantageType expected = AdvantageInterface.AdvantageType.BACKGROUND;
        final AdvantageInterface.AdvantageType actual = this.advantage.getType();

        Assert.assertEquals(actual, expected);
    }

    public void testGetValue() {
        final int expected = 11;
        final int actual = this.advantage.getValue();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test advantage";
        final String actual = this.advantage.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.advantage, this.advantage);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.advantage, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.advantage, "");
    }

    public void testDifferentAdvantage() throws EntityException {
        final Advantage object = Advantage.builder()
            .setKey("testAdvantage2")
            .addName(Configuration.Language.ENGLISH, "Test advantage 2")
            .setType(AdvantageInterface.AdvantageType.DISCIPLINE)
            .setValue(22)
            .build();

        Assert.assertNotEquals(this.advantage, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.advantage.hashCode();
        final int actual = this.advantage.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.advantage.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.advantage.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.advantage.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.advantage.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing type for entity.*")
    public void testBuilderEmptyType() throws EntityException {
        this.advantage.toBuilder()
            .setType(null)
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.advantage.toBuilder()
            .build();
    }
}
