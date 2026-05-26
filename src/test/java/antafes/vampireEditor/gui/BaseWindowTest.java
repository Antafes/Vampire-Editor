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

import antafes.vampireEditor.entity.exception.CharacterInvalidXmlException;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.exception.MissingClanException;
import antafes.vampireEditor.entity.exception.MissingRoadException;
import antafes.vampireEditor.language.English;
import antafes.vampireEditor.language.German;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

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
            "Could not load the character.\nMissing road for non-NPC character."
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithMissingClan()
    {
        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(
                new English(),
                new MissingClanException("Missing clan for non-NPC character!")
            ),
            "Could not load the character.\nMissing clan for non-NPC character."
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithFileNotFound()
    {
        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(
                new English(),
                new FileNotFoundException("C:/tmp/missing-character.xml")
            ),
            "Could not load the character.\nCharacter file not found: C:/tmp/missing-character.xml"
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithEntityStorageDetails()
    {
        EntityStorageException ex = new EntityStorageException("Could not load character 'foo.xml'!");
        ex.addSuppressed(new RuntimeException("Unknown road key 'invalidRoad' in character XML."));

        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(new English(), ex),
            "Could not load the character."
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithEntityStorageXsdValidationDetails()
    {
        EntityStorageException ex = new EntityStorageException("Could not load character 'foo.xml'!");
        ex.addSuppressed(new CharacterInvalidXmlException(
            "Character file 'foo.xml' failed XSD validation!",
            new Exception("XML validation error: cvc-complex-type.2.4.a")
        ));

        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(new English(), ex),
            "Could not load the character.\nCharacter file is invalid."
        );
    }

    public void testGetCouldNotLoadCharacterMessageWithMissingRoadInGerman()
    {
        Assert.assertEquals(
            BaseWindow.getCouldNotLoadCharacterMessage(
                new German(),
                new MissingRoadException("Missing road for non-NPC character!")
            ),
            "Konnte den Charakter nicht laden.\nDem Spielercharakter fehlt ein Pfad."
        );
    }
}
