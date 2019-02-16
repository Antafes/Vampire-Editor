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
public class RoadTest {
    private Road road;

    @BeforeMethod
    public void setUp() throws EntityException {
        this.road = new Road.Builder()
            .setKey("testRoad")
            .addName(Configuration.Language.ENGLISH, "Test road")
            .setValue(11)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.road = null;
    }

    public void testGetKey() {
        final String expected = "testRoad";
        final String actual = this.road.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap actual = this.road.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test road";
        final String actual = this.road.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetValue() {
        final int expected = 11;
        final int actual = this.road.getValue();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test road";
        final String actual = this.road.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.road, this.road);
    }

    public void testObjectNull() {
        Assert.assertFalse(this.road.equals(null));
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.road, "");
    }

    public void testDifferentAbility() throws EntityException {
        final Road object = new Road.Builder()
            .setKey("testRoad2")
            .addName(Configuration.Language.ENGLISH, "Test road 2")
            .setValue(22)
            .build();

        Assert.assertNotEquals(this.road, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.road.hashCode();
        final int actual = new Road.Builder()
            .fillDataFromObject(this.road)
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        new Road.Builder()
            .fillDataFromObject(this.road)
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        new Road.Builder()
            .fillDataFromObject(this.road)
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        new Road.Builder()
            .fillDataFromObject(this.road)
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        new Road.Builder()
            .fillDataFromObject(this.road)
            .setNames(new HashMap<>())
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        new Road.Builder()
            .fillDataFromObject(this.road)
            .build();
    }
}