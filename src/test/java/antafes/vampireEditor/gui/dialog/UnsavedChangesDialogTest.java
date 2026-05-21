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
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.awt.GraphicsEnvironment;

@Test
public class UnsavedChangesDialogTest extends BaseTest
{
    public void testResultEnumSaveHasValueZero()
    {
        Assert.assertEquals(UnsavedChangesDialog.Result.SAVE.getValue(), 0);
    }

    public void testResultEnumDiscardHasValueOne()
    {
        Assert.assertEquals(UnsavedChangesDialog.Result.DISCARD.getValue(), 1);
    }

    public void testResultEnumCancelHasValueTwo()
    {
        Assert.assertEquals(UnsavedChangesDialog.Result.CANCEL.getValue(), 2);
    }

    public void testDefaultUserChoiceIsCancelWithoutInteraction()
    {
        this.skipIfHeadless();
        UnsavedChangesDialog dialog = new UnsavedChangesDialog(null, "Test Character");

        Assert.assertEquals(dialog.getUserChoice(), UnsavedChangesDialog.Result.CANCEL,
            "Default user choice should be CANCEL before any button is clicked");

        dialog.dispose();
    }

    public void testDialogCreatedWithCharacterNameDoesNotThrow()
    {
        this.skipIfHeadless();
        UnsavedChangesDialog dialog = new UnsavedChangesDialog(null, "Dracula");
        Assert.assertNotNull(dialog);
        dialog.dispose();
    }

    public void testDialogCreatedWithEmptyCharacterNameDoesNotThrow()
    {
        this.skipIfHeadless();
        UnsavedChangesDialog dialog = new UnsavedChangesDialog(null, "");
        Assert.assertNotNull(dialog);
        dialog.dispose();
    }

    private void skipIfHeadless()
    {
        if (GraphicsEnvironment.isHeadless()) {
            throw new SkipException("Skipping GUI test in headless environment");
        }
    }
}
