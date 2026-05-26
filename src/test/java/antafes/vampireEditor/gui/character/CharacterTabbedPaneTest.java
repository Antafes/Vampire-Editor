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

package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CharacterTabbedPaneTest extends BaseTest
{
    public void testIgnoresEventsFromOtherCharacters()
    {
        CharacterTabbedPane pane = new CharacterTabbedPane();
        Character tabCharacter = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(tabCharacter);
        pane.setCharacter(tabCharacter);

        Character otherCharacter = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(otherCharacter);

        CharacterChangedEvent event = new CharacterChangedEvent();
        event.setCharacter(otherCharacter);
        event.setComponentIdentifier("name");
        event.setChanged(true);

        pane.handleCharacterChangedEvent(event);

        Assert.assertFalse(pane.isCharacterChanged(), "Events from different characters must be ignored.");
        Assert.assertFalse(pane.isModified(), "Events from different characters must not mark this tab as modified.");
    }

    public void testTracksDirtyStatePerComponent()
    {
        CharacterTabbedPane pane = new CharacterTabbedPane();
        Character character = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(character);
        pane.setCharacter(character);

        CharacterChangedEvent firstFieldChanged = new CharacterChangedEvent();
        firstFieldChanged.setCharacter(character);
        firstFieldChanged.setComponentIdentifier("name");
        firstFieldChanged.setChanged(true);
        pane.handleCharacterChangedEvent(firstFieldChanged);
        Assert.assertTrue(pane.isModified(), "A changed component must mark the tab as modified.");

        CharacterChangedEvent secondFieldChanged = new CharacterChangedEvent();
        secondFieldChanged.setCharacter(character);
        secondFieldChanged.setComponentIdentifier("chronicle");
        secondFieldChanged.setChanged(true);
        pane.handleCharacterChangedEvent(secondFieldChanged);

        CharacterChangedEvent firstFieldReverted = new CharacterChangedEvent();
        firstFieldReverted.setCharacter(character);
        firstFieldReverted.setComponentIdentifier("name");
        firstFieldReverted.setChanged(false);
        pane.handleCharacterChangedEvent(firstFieldReverted);

        Assert.assertTrue(pane.isCharacterChanged(), "Character must stay dirty while any component remains changed.");

        CharacterChangedEvent secondFieldReverted = new CharacterChangedEvent();
        secondFieldReverted.setCharacter(character);
        secondFieldReverted.setComponentIdentifier("chronicle");
        secondFieldReverted.setChanged(false);
        pane.handleCharacterChangedEvent(secondFieldReverted);

        Assert.assertFalse(pane.isCharacterChanged(), "Character must be clean once all changed components are reverted.");
        Assert.assertFalse(pane.isModified(), "Tracker state must be reset once all changed components are reverted.");
    }

    public void testResetCharacterChangedClearsTrackedComponents()
    {
        CharacterTabbedPane pane = new CharacterTabbedPane();
        Character character = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(character);
        pane.setCharacter(character);

        CharacterChangedEvent changed = new CharacterChangedEvent();
        changed.setCharacter(character);
        changed.setComponentIdentifier("name");
        changed.setChanged(true);
        pane.handleCharacterChangedEvent(changed);

        pane.setCharacterChanged(false);

        CharacterChangedEvent falseEvent = new CharacterChangedEvent();
        falseEvent.setCharacter(character);
        falseEvent.setComponentIdentifier("chronicle");
        falseEvent.setChanged(false);
        pane.handleCharacterChangedEvent(falseEvent);

        Assert.assertFalse(pane.isCharacterChanged(), "After reset, stale dirty components must not keep the character dirty.");
        Assert.assertFalse(pane.isModified(), "After reset, tracker state must remain clean.");
    }
}

