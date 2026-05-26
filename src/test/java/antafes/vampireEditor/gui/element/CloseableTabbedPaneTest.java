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

package antafes.vampireEditor.gui.element;

import antafes.vampireEditor.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;

@Test
public class CloseableTabbedPaneTest extends BaseTest
{
    public void testSetTitleAtUpdatesTabbedPaneModelAndCustomLabel()
    {
        CloseableTabbedPane tabbedPane = new CloseableTabbedPane();
        tabbedPane.insertTab("Original", null, new JPanel(), null, 0);

        tabbedPane.setTitleAt(0, "Renamed*");

        Assert.assertEquals(tabbedPane.getTitleAt(0), "Renamed*");

        Component tabComponent = tabbedPane.getTabComponentAt(0);
        Assert.assertTrue(tabComponent instanceof JPanel);

        JLabel titleLabel = null;
        for (Component child : ((JPanel) tabComponent).getComponents()) {
            if (child instanceof JLabel) {
                titleLabel = (JLabel) child;
                break;
            }
        }

        Assert.assertNotNull(titleLabel, "Custom tab title label should exist");
        Assert.assertEquals(titleLabel.getText(), "Renamed*");
    }
}

