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

import antafes.vampireEditor.entity.EntityException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class GenerationTest {
    private Generation generation;

    @BeforeMethod
    public void setUp() throws EntityException {
        this.generation = new Generation.Builder()
            .setGeneration(5)
            .setMaximumAttributes(10)
            .setMaximumBloodPool(15)
            .setBloodPerRound(2)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        this.generation = null;
    }

    public void testGetGeneration() {
        final int expected = 5;
        final int actual = this.generation.getGeneration();

        Assert.assertEquals(actual, expected);
    }

    public void testGetMaximumAttributes() {
        final int expected = 10;
        final int actual = this.generation.getMaximumAttributes();

        Assert.assertEquals(actual, expected);
    }

    public void testGetMaximumBloodStock() {
        final int expected = 15;
        final int actual = this.generation.getMaximumBloodPool();

        Assert.assertEquals(actual, expected);
    }

    public void testGetMaximumBloodPoolIndefinite() throws EntityException {
        final int expected = Integer.MAX_VALUE;
        final int actual = new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setMaximumBloodPool(-1)
            .build()
            .getMaximumBloodPool();

        Assert.assertEquals(actual, expected);
    }

    public void testGetBloodPerRound() {
        final int expected = 2;
        final int actual = this.generation.getBloodPerRound();

        Assert.assertEquals(actual, expected);
    }

    public void testGetBloodPerRoundIndefinite() throws EntityException {
        final int expected = Integer.MAX_VALUE;
        final int actual = new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setBloodPerRound(-1)
            .build()
            .getBloodPerRound();

        Assert.assertEquals(actual, expected);
    }

    public void testToString() {
        final String expected = "5";
        final String actual = this.generation.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testEqualObjects() {
        Assert.assertEquals(this.generation, this.generation);
    }

    public void testObjectNull() {
        Assert.assertNotEquals(this.generation, null);
    }

    public void testDifferentObject() {
        Assert.assertNotEquals(this.generation, "");
    }

    public void testDifferentGeneration() throws EntityException {
        final Generation object = new Generation.Builder()
            .setGeneration(8)
            .setMaximumAttributes(8)
            .setMaximumBloodPool(12)
            .setBloodPerRound(1)
            .build();

        Assert.assertNotEquals(this.generation, object);
    }

    public void testHashCode() throws EntityException {
        final int expected = this.generation.hashCode();
        final int actual = new Generation.Builder()
            .fillDataFromObject(this.generation)
            .build()
            .hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing generation")
    public void testBuilderMissingGeneration() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setGeneration(0)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing generation")
    public void testBuilderMissingGenerationBelowZero() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setGeneration(-10)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing maximum attributes")
    public void testBuilderMissingMaximumAttributes() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setMaximumAttributes(0)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing maximum attributes")
    public void testBuilderMissingMaximumAttributesBelowZero() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setMaximumAttributes(-10)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing maximum blood pool")
    public void testBuilderMissingMaximumBloodStockool() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setMaximumBloodPool(0)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing maximum blood pool")
    public void testBuilderMissingMaximumBloodStockoolBelowZero() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setMaximumBloodPool(-10)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing blood per round")
    public void testBuilderMissingBloodPerRound() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setBloodPerRound(0)
            .build();
    }

    @Test(expectedExceptions = EntityException.class, expectedExceptionsMessageRegExp = "Missing blood per round")
    public void testBuilderMissingBloodPerRoundBelowZero() throws EntityException {
        new Generation.Builder()
            .fillDataFromObject(this.generation)
            .setBloodPerRound(-10)
            .build();
    }
}