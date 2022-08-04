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
public class MeritTest {
    private Merit merit;

    @BeforeMethod
    public void setUp() throws EntityException {
        this.merit = Merit.builder()
            .setKey("testMerit")
            .addName(Configuration.Language.ENGLISH, "Test merit")
            .setType(SpecialFeatureInterface.SpecialFeatureType.PHYSICAL)
            .setCost(2)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.merit = null;
    }

    public void testGetKey() {
        final String expected = "testMerit";
        final String actual = this.merit.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.merit.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test merit";
        final String actual = this.merit.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetType() {
        final SpecialFeatureInterface.SpecialFeatureType expected = SpecialFeatureInterface.SpecialFeatureType.PHYSICAL;
        final SpecialFeatureInterface.SpecialFeatureType actual = this.merit.getType();

        Assert.assertEquals(actual, expected);
    }

    public void testGetCost() {
        final int expected = 2;
        final int actual = this.merit.getCost();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test merit (2)";
        final String actual = this.merit.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.merit, this.merit);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.merit, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.merit, "");
    }

    public void testDifferentMerit() throws EntityException {
        final Merit object = Merit.builder()
            .setKey("testMerit2")
            .addName(Configuration.Language.ENGLISH, "Test merit 2")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .setCost(22)
            .build();

        Assert.assertNotEquals(this.merit, object);
    }

    public void testDifferentCost() throws EntityException {
        final Merit object = this.merit.toBuilder()
            .setCost(22)
            .build();

        Assert.assertNotEquals(this.merit, object);
    }

    public void testDifferentType() throws EntityException {
        final Merit object = this.merit.toBuilder()
            .setType(SpecialFeatureInterface.SpecialFeatureType.SOCIAL)
            .build();

        Assert.assertNotEquals(this.merit, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.merit.hashCode();
        final int actual = this.merit.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.merit.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.merit.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.merit.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.merit.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing type for entity.*")
    public void testBuilderEmptyType() throws EntityException {
        this.merit.toBuilder()
            .setType(null)
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.merit.toBuilder()
            .build();
    }
}
