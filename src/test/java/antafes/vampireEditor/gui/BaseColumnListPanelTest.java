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

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.gui.event.UpdateFreeAdditionalPointsEvent;
import antafes.vampireEditor.gui.event.listener.UpdateFreeAdditionalPointsListener;
import antafes.vampireEditor.gui.exception.TypeNotSupportedException;
import antafes.vampireEditor.gui.utility.FreeAdditionalPointsFields;
import antafes.vampireEditor.gui.utility.Weighting;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Test
public class BaseColumnListPanelTest extends BaseTest
{
    static {
        System.setProperty("java.awt.headless", "true");
    }

    private static final String GROUP = "test_group";
    private static final String ITEM_A = "itemA";
    private static final String ITEM_B = "itemB";
    private static final int MAX = 5;

    private TestPanel panel;
    private JSpinner spinnerA;
    private JSpinner spinnerB;
    private List<UpdateFreeAdditionalPointsEvent> capturedEvents;

    @BeforeClass
    public void classSetUp() throws TypeNotSupportedException
    {
        Configuration.getInstance().setLanguage(Configuration.Language.ENGLISH);

        panel = new TestPanel();
        panel.start();
        panel.build();

        spinnerA = findSpinnerByName(panel, ITEM_A);
        spinnerB = findSpinnerByName(panel, ITEM_B);

        Assert.assertNotNull(spinnerA, "Spinner '" + ITEM_A + "' not found in panel");
        Assert.assertNotNull(spinnerB, "Spinner '" + ITEM_B + "' not found in panel");

        capturedEvents = new ArrayList<>();
        VampireEditor.getDispatcher().addListener(
            UpdateFreeAdditionalPointsEvent.class,
            new UpdateFreeAdditionalPointsListener(capturedEvents::add)
        );
    }

    @BeforeMethod
    public void setUp()
    {
        spinnerA.setValue(0);
        spinnerB.setValue(0);
        capturedEvents.clear();
    }

    /**
     * When spinners sum to a value within the group maximum, the free-points field should display the exact sum
     * and the dispatched event should carry zero overflow.
     */
    public void testFreePointsFieldWithinMax()
    {
        spinnerA.setValue(3);

        FreeAdditionalPointsFields fields = panel.getFreeAdditionalPointsElementsForGroup(GROUP);
        Assert.assertEquals(fields.getFreeAdditionalPointsField().getText(), "3");

        Assert.assertFalse(capturedEvents.isEmpty());
        UpdateFreeAdditionalPointsEvent event = capturedEvents.get(capturedEvents.size() - 1);
        Assert.assertEquals(event.getGroupLabel(), GROUP);
        Assert.assertEquals(event.getPointsOverMax(), 0);
    }

    /**
     * When a single spinner exceeds the maximum, the free-points field should be capped at the max and
     * the dispatched event should carry the correct overflow amount.
     */
    public void testFreePointsFieldCappedAtMax()
    {
        spinnerA.setValue(MAX + 1);  // 6, overflow of 1

        FreeAdditionalPointsFields fields = panel.getFreeAdditionalPointsElementsForGroup(GROUP);
        Assert.assertEquals(fields.getFreeAdditionalPointsField().getText(), Integer.toString(MAX));

        Assert.assertFalse(capturedEvents.isEmpty());
        UpdateFreeAdditionalPointsEvent event = capturedEvents.get(capturedEvents.size() - 1);
        Assert.assertEquals(event.getGroupLabel(), GROUP);
        Assert.assertEquals(event.getPointsOverMax(), 1);
    }

    /**
     * When multiple spinners together exceed the maximum, the free-points field is capped and the event
     * carries the combined overflow.
     */
    public void testFreePointsFieldMultipleSpinnersExceedMax()
    {
        spinnerA.setValue(3);
        spinnerB.setValue(4);  // sum = 7, overflow = 2

        FreeAdditionalPointsFields fields = panel.getFreeAdditionalPointsElementsForGroup(GROUP);
        Assert.assertEquals(fields.getFreeAdditionalPointsField().getText(), Integer.toString(MAX));

        UpdateFreeAdditionalPointsEvent event = capturedEvents.get(capturedEvents.size() - 1);
        Assert.assertEquals(event.getGroupLabel(), GROUP);
        Assert.assertEquals(event.getPointsOverMax(), 2);
    }

    /**
     * When spinner values drop back below the maximum after previously exceeding it, the free-points field
     * reflects the lower sum and the event carries zero overflow.
     */
    public void testFreePointsFieldDropsBelowMax()
    {
        spinnerA.setValue(MAX + 3);  // push above max
        capturedEvents.clear();

        spinnerA.setValue(MAX - 2);  // drop below max

        FreeAdditionalPointsFields fields = panel.getFreeAdditionalPointsElementsForGroup(GROUP);
        Assert.assertEquals(
            fields.getFreeAdditionalPointsField().getText(),
            Integer.toString(MAX - 2)
        );

        Assert.assertFalse(capturedEvents.isEmpty());
        UpdateFreeAdditionalPointsEvent event = capturedEvents.get(capturedEvents.size() - 1);
        Assert.assertEquals(event.getGroupLabel(), GROUP);
        Assert.assertEquals(event.getPointsOverMax(), 0);
    }

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    private static JSpinner findSpinnerByName(Container container, String name)
    {
        for (Component c : container.getComponents()) {
            if (c instanceof JSpinner && name.equals(c.getName())) {
                return (JSpinner) c;
            }
            if (c instanceof Container) {
                JSpinner found = findSpinnerByName((Container) c, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------------
    // Minimal concrete subclass — non-editable spinner rows, no storage access
    // ---------------------------------------------------------------------------

    private static class TestPanel extends BaseColumnListPanel
    {
        @Override
        protected void init()
        {
            this.translateGroupLabels(false);
            this.translateFieldLabels(false);
            this.addGroup(1, GROUP, false, true);
            try {
                this.addRow(1, GROUP, ITEM_A, ElementType.SPINNER, MAX * 2);
                this.addRow(1, GROUP, ITEM_B, ElementType.SPINNER, MAX * 2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected int fetchMaxFreeAdditionalPoints(String groupLabelText, JComboBox<Weighting> weightingField)
        {
            return MAX;
        }

        @Override
        public void updateTexts()
        {
        }
    }
}
