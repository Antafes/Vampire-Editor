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

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.EntityException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class AttributeTest extends BaseTest
{
    private Attribute attribute;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        this.attribute = Attribute.builder()
            .setKey("testAttribute")
            .addName(Configuration.Language.ENGLISH, "Test attribute")
            .setType(AttributeInterface.AttributeType.PHYSICAL)
            .setValue(11)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.attribute = null;
    }

    public void testGetKey() {
        final String expected = "testAttribute";
        final String actual = this.attribute.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap<Configuration.Language, String> actual = this.attribute.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test attribute";
        final String actual = this.attribute.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetType() {
        final AttributeInterface.AttributeType expected = AttributeInterface.AttributeType.PHYSICAL;
        final AttributeInterface.AttributeType actual = this.attribute.getType();

        Assert.assertEquals(actual, expected);
    }

    public void testGetValue() {
        final int expected = 11;
        final int actual = this.attribute.getValue();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test attribute";
        final String actual = this.attribute.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.attribute, this.attribute);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.attribute, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.attribute, "");
    }

    public void testDifferentAttribute() throws EntityException {
        final Attribute object = Attribute.builder()
            .setKey("testAttribute2")
            .addName(Configuration.Language.ENGLISH, "Test attribute 2")
            .setType(AttributeInterface.AttributeType.MENTAL)
            .setValue(22)
            .build();

        Assert.assertNotEquals(this.attribute, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.attribute.hashCode();
        final int actual = this.attribute.toBuilder()
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        this.attribute.toBuilder()
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        this.attribute.toBuilder()
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        this.attribute.toBuilder()
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        this.attribute.toBuilder()
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing type for entity.*")
    public void testBuilderEmptyType() throws EntityException {
        this.attribute.toBuilder()
            .setType(null)
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        this.attribute.toBuilder()
            .build();
    }
}
