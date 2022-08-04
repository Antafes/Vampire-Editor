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
public class WeaknessTest {
    private Weakness weakness;

    @BeforeMethod
    public void setUp() throws EntityException {
        this.weakness = Weakness.builder()
            .setKey("testWeakness")
            .addName(Configuration.Language.ENGLISH, "Test weakness")
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.weakness = null;
    }

    public void testGetKey() {
        final String expected = "testWeakness";
        final String actual = this.weakness.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.weakness.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test weakness";
        final String actual = this.weakness.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test weakness";
        final String actual = this.weakness.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.weakness, this.weakness);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.weakness, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.weakness, "");
    }

    public void testDifferentAbility() throws EntityException {
        final Weakness object = Weakness.builder()
            .setKey("testWeakness2")
            .addName(Configuration.Language.ENGLISH, "Test weakness 2")
            .build();

        Assert.assertNotEquals(this.weakness, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.weakness.hashCode();
        final int actual = this.weakness.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.weakness.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.weakness.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.weakness.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.weakness.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.weakness.toBuilder()
            .build();
    }
}
