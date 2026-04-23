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
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui;

import antafes.vampireEditor.entity.exception.MissingRoadException;
import antafes.vampireEditor.language.English;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class BaseWindowTest
{
    public void testGetCouldNotLoadCharacterMessageWithoutMissingRoad()
    {
        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(new English(), new Exception("Other failure")),
            "Could not load the character."
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithMissingRoad()
    {
        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(
                new English(),
                new MissingRoadException("Missing road for non-NPC character!")
            ),
            "Could not load the character.\nMissing road for non-NPC character!"
        );
    }
}
