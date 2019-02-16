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

package antafes.vampireEditor.gui.utility;

import antafes.vampireEditor.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class WeightingTest {
    @BeforeMethod
    public void setUp() {
        Configuration configuration = Configuration.getInstance();
        configuration.loadProperties();
        configuration.setLanguage(Configuration.Language.ENGLISH);
    }

    public void testToString() {
        final String expected = "Primary";
        final String actual = Weighting.PRIMARY.toString();

        Assert.assertEquals(actual, expected);
    }

    public void testGetAttributeMax() {
        final int expected = 7;
        final int actual = Weighting.PRIMARY.getAttributeMax();

        Assert.assertEquals(actual, expected);
    }

    public void testGetAbilitiesMax() {
        final int expected = 13;
        final int actual = Weighting.PRIMARY.getAbilitiesMax();

        Assert.assertEquals(actual, expected);
    }

    public void testGetRemaining() {
        final Weighting expected = Weighting.TERTIARY;
        final Weighting actual = Weighting.getRemaining(Weighting.PRIMARY, Weighting.SECONDARY);

        Assert.assertEquals(actual, expected);
    }
}