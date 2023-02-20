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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.character;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.EntityException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class NatureTest extends BaseTest
{
    private Nature nature;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        this.nature = Nature.builder()
            .setKey("testNature")
            .addName(Configuration.Language.ENGLISH, "Test nature")
            .build();
    }

    @AfterMethod
    public void tearDown()
    {
        this.nature = null;
    }

    @Test
    public void testIsManual()
    {
    }

    @Test
    public void testSetManual()
    {
    }

    public void testGetKey() {
        final String expected = "testNature";
        final String actual = this.nature.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.nature.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test nature";
        final String actual = this.nature.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test nature";
        final String actual = this.nature.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.nature, this.nature);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.nature, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.nature, "");
    }

    public void testDifferentAbility() throws EntityException {
        final Nature object = Nature.builder()
            .setKey("testNature2")
            .addName(Configuration.Language.ENGLISH, "Test nature 2")
            .build();

        Assert.assertNotEquals(this.nature, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.nature.hashCode();
        final int actual = this.nature.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.nature.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.nature.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.nature.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.nature.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.nature.toBuilder()
            .build();
    }
}
