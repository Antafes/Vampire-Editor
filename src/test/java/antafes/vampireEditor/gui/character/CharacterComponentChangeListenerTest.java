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
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Test
public class CharacterComponentChangeListenerTest extends BaseTest
{
    public void testResolveCharacterUsesBaseCharacterPanelAncestor()
    {
        Character expectedCharacter = TestCharacterUtility.createTestCharacter();
        Character fallbackCharacter = TestCharacterUtility.createTestCharacter();
        GeneralPanel panel = new GeneralPanel(this.configuration);
        panel.setCharacter(expectedCharacter);

        JSpinner spinner = new JSpinner();
        spinner.setName("willpower");
        panel.add(spinner);

        CharacterComponentChangeListener listener = new CharacterComponentChangeListener();
        listener.setComponent(spinner);
        listener.setCharacter(fallbackCharacter);

        Assert.assertEquals(
            this.resolveCharacter(listener),
            expectedCharacter,
            "Listener must resolve character from BaseCharacterPanel ancestor."
        );
    }

    public void testResolveCharacterUsesBaseCharacterListPanelAncestor()
    {
        Character expectedCharacter = TestCharacterUtility.createTestCharacter();
        Character fallbackCharacter = TestCharacterUtility.createTestCharacter();
        AbilitiesPanel panel = new AbilitiesPanel(this.configuration);
        panel.setCharacter(expectedCharacter);

        JSpinner spinner = new JSpinner();
        spinner.setName("alertness");
        panel.add(spinner);

        CharacterComponentChangeListener listener = new CharacterComponentChangeListener();
        listener.setComponent(spinner);
        listener.setCharacter(fallbackCharacter);

        Assert.assertEquals(
            this.resolveCharacter(listener),
            expectedCharacter,
            "Listener must resolve character from BaseCharacterListPanel ancestor after save updates."
        );
    }

    private Character resolveCharacter(CharacterComponentChangeListener listener)
    {
        try {
            Method method = CharacterComponentChangeListener.class.getDeclaredMethod("resolveCharacter");
            method.setAccessible(true);

            return (Character) method.invoke(listener);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}

