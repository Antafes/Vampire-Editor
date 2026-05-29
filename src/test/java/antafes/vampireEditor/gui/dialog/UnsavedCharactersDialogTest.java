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

package antafes.vampireEditor.gui.dialog;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.gui.UnsavedCharactersDialog;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Test
public class UnsavedCharactersDialogTest extends BaseTest
{
    public void testSingleCharacterUsesSingularMessage()
    {
        this.skipIfHeadless();
        UnsavedCharactersDialog dialog = new UnsavedCharactersDialog(null, List.of("Johannes"), this.configuration);

        String message = this.getInfoMessage(dialog);

        Assert.assertTrue(message.startsWith("Character \"Johannes\" has unsaved changes."));
        Assert.assertFalse(message.contains("Multiple characters have unsaved changes."));

        dialog.dispose();
    }

    public void testMultipleCharactersUsesMultipleMessageAndList()
    {
        this.skipIfHeadless();
        UnsavedCharactersDialog dialog = new UnsavedCharactersDialog(null, List.of("Johannes", "Carolina"), this.configuration);

        String message = this.getInfoMessage(dialog);

        Assert.assertTrue(message.startsWith("Multiple characters have unsaved changes."));
        Assert.assertTrue(message.contains("The following characters have unsaved changes:"));
        Assert.assertTrue(message.contains("\n- Johannes"));
        Assert.assertTrue(message.contains("\n- Carolina"));

        dialog.dispose();
    }

    public void testEmptyCharacterListUsesGenericUnsavedCharactersMessage()
    {
        this.skipIfHeadless();
        UnsavedCharactersDialog dialog = new UnsavedCharactersDialog(null, List.of(), this.configuration);

        String message = this.getInfoMessage(dialog);

        Assert.assertEquals(message, "There are unsaved characters open. Do you want to save them now?");

        dialog.dispose();
    }

    private String getInfoMessage(UnsavedCharactersDialog dialog)
    {
        Component textAreaComponent = dialog.getContentPane().getComponent(0);
        Assert.assertTrue(textAreaComponent instanceof JTextArea);

        return ((JTextArea) textAreaComponent).getText();
    }

    private void skipIfHeadless()
    {
        if (GraphicsEnvironment.isHeadless()) {
            throw new SkipException("Skipping GUI test in headless environment");
        }
    }
}
