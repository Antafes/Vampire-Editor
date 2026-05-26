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

package antafes.vampireEditor.gui.modification;

import antafes.vampireEditor.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class CharacterModificationTrackerTest extends BaseTest
{
    private CharacterModificationTracker tracker;

    @BeforeMethod
    public void setUp()
    {
        this.tracker = new CharacterModificationTracker();
    }

    public void testInitialStateIsNotModified()
    {
        Assert.assertFalse(this.tracker.isModified(), "Tracker should not be modified initially");
    }

    public void testMarkModifiedSetsModifiedTrue()
    {
        this.tracker.markModified();

        Assert.assertTrue(this.tracker.isModified(), "Tracker should be modified after markModified()");
    }

    public void testResetModifiedClearsFlag()
    {
        this.tracker.markModified();
        this.tracker.resetModified();

        Assert.assertFalse(this.tracker.isModified(), "Tracker should not be modified after resetModified()");
    }

    public void testMultipleMarkModifiedCallsStillModified()
    {
        this.tracker.markModified();
        this.tracker.markModified();
        this.tracker.markModified();

        Assert.assertTrue(this.tracker.isModified(), "Tracker should still be modified after multiple markModified() calls");
    }

    public void testResetAfterMultipleMarksClearsFlag()
    {
        this.tracker.markModified();
        this.tracker.markModified();
        this.tracker.resetModified();

        Assert.assertFalse(this.tracker.isModified(), "Tracker should not be modified after resetModified() even following multiple marks");
    }

    public void testMarkAfterResetSetsModifiedAgain()
    {
        this.tracker.markModified();
        this.tracker.resetModified();
        this.tracker.markModified();

        Assert.assertTrue(this.tracker.isModified(), "Tracker should be modified again after markModified() following a reset");
    }

    public void testResetOnFreshTrackerKeepsUnmodified()
    {
        this.tracker.resetModified();

        Assert.assertFalse(this.tracker.isModified(), "Resetting an unmodified tracker should keep it unmodified");
    }
}

