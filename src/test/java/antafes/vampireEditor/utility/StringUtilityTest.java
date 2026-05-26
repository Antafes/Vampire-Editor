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

package antafes.vampireEditor.utility;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringUtilityTest
{
    @Test(dataProvider = "dataProvider")
    public void testToCamelCase(String string, String expected)
    {
        Assert.assertEquals(StringUtility.toCamelCase(string), expected);
    }

    @DataProvider(name = "dataProvider")
    public String[][] dataProvider()
    {
        return new String[][] {
            {"This is a nice text", "thisIsANiceText"},
            {"Another wonderful Text", "anotherWonderfulText"},
            {"This téxt contàins accents", "thisTextContainsAccents"},
            {"This ä contains ü umlauts ö, all ß of them", "thisAeContainsUeUmlautsOeAllSsOfThem"},
            {"There are big umlauts like Öffnung and small ones like Häuser", "thereAreBigUmlautsLikeOeffnungAndSmallOnesLikeHaeuser"}
        };
    }
}
