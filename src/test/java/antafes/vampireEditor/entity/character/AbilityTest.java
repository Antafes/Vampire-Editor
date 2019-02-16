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
public class AbilityTest {
    private Ability ability;

    @BeforeMethod
    public void setUp() throws EntityException {
        Configuration.getInstance().loadProperties();
        this.ability = new Ability.Builder()
            .setKey("testAbility")
            .addName(Configuration.Language.ENGLISH, "Test ability")
            .setType(AbilityInterface.AbilityType.TALENT)
            .setValue(11)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.ability = null;
    }

    public void testGetKey() {
        final String expected = "testAbility";
        final String actual = this.ability.getKey();

        Assert.assertEquals(actual, expected);
    }

    public void testGetNames() {
        final HashMap actual = this.ability.getNames();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
    }

    public void testGetName() {
        final String expected = "Test ability";
        final String actual = this.ability.getName();

        Assert.assertEquals(actual, expected);
    }

    public void testGetType() {
        final AbilityInterface.AbilityType expected = Ability.AbilityType.TALENT;
        final AbilityInterface.AbilityType actual = this.ability.getType();

        Assert.assertEquals(actual, expected);
    }

    public void testGetValue() {
        final int expected = 11;
        final int actual = this.ability.getValue();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "Test ability";
        final String actual = this.ability.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.ability, this.ability);
    }

    public void testObjectNull() {
        Assert.assertFalse(this.ability.equals(null));
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.ability, "");
    }

    public void testDifferentAbility() throws EntityException {
        final Ability object = new Ability.Builder()
            .setKey("testAbility2")
            .addName(Configuration.Language.ENGLISH, "Test ability 2")
            .setType(AbilityInterface.AbilityType.KNOWLEDGE)
            .setValue(22)
            .build();

        Assert.assertNotEquals(this.ability, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.ability.hashCode();
        final int actual = new Ability.Builder()
            .fillDataFromObject(this.ability)
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderNullKey() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .setKey(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing key for entity.*")
    public void testBuilderEmptyKey() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .setKey("")
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderNullNames() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .setNames(null)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing names for entity.*")
    public void testBuilderEmptyNames() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .setNames(new HashMap<>())
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing type")
    public void testBuilderEmptyType() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .setType(null)
            .build();
    }

    public void testBuilderBuild() throws EntityException {
        new Ability.Builder()
            .fillDataFromObject(this.ability)
            .build();
    }
}