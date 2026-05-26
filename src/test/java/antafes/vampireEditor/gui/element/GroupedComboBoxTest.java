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
import java.util.Arrays;
import java.util.List;

@Test
public class GroupedComboBoxTest extends BaseTest
{
    public void testSetModelPopulatesEntries()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        Assert.assertEquals(comboBox.getModel().getSize(), 5);
        Assert.assertTrue(comboBox.getModel().getElementAt(0) instanceof GroupedComboBox.HeaderEntry<?>);
        Assert.assertTrue(comboBox.getModel().getElementAt(1) instanceof GroupedComboBox.ItemEntry<?>);
        Assert.assertTrue(comboBox.getModel().getElementAt(3) instanceof GroupedComboBox.HeaderEntry<?>);
    }

    public void testInitialSelectionSkipsHeader()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        Assert.assertEquals(comboBox.getSelectedItem(), "Brujah");
    }

    public void testSelectingHeaderKeepsPreviousSelection()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();
        GroupedComboBox.ComboBoxEntry<String> headerEntry = comboBox.getModel().getElementAt(3);

        comboBox.setSelectedItem("Ventrue");
        comboBox.setSelectedItem(headerEntry);

        Assert.assertEquals(comboBox.getSelectedItem(), "Ventrue");
    }

    public void testGetSelectedItemNeverReturnsHeader()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedIndex(0);

        Assert.assertNotNull(comboBox.getSelectedItem());
        Assert.assertNotEquals(comboBox.getSelectedItem(), "Main Clans");
    }

    public void testKeyboardUpDownSkipsHeaders()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Ventrue");
        comboBox.setSelectedIndex(3);
        Assert.assertEquals(comboBox.getSelectedItem(), "Ahrimanes");

        comboBox.setSelectedIndex(3);
        Assert.assertEquals(comboBox.getSelectedItem(), "Ventrue");
    }

    public void testKeyboardHomeSkipsHeader()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Ahrimanes");
        comboBox.setSelectedIndex(0);

        Assert.assertEquals(comboBox.getSelectedItem(), "Brujah");
    }

    public void testKeyboardEndSkipsToLastSelectable()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Brujah");
        comboBox.setSelectedIndex(4);

        Assert.assertEquals(comboBox.getSelectedItem(), "Ahrimanes");
    }

    public void testKeyboardPageUpSkipsHeader()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Ahrimanes");
        comboBox.setSelectedIndex(0);

        Assert.assertEquals(comboBox.getSelectedItem(), "Brujah");
    }

    public void testKeyboardPageDownSkipsHeader()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Brujah");
        comboBox.setSelectedIndex(3);

        Assert.assertEquals(comboBox.getSelectedItem(), "Ahrimanes");
    }

    public void testHeaderRenderingNeverUsesSelectionHighlight()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();
        ListCellRenderer<? super GroupedComboBox.ComboBoxEntry<String>> renderer = comboBox.getRenderer();
        JList<GroupedComboBox.ComboBoxEntry<String>> list = new JList<>();

        Component component = renderer.getListCellRendererComponent(
            list,
            comboBox.getModel().getElementAt(0),
            0,
            true,
            true
        );

        Assert.assertTrue(component instanceof JPanel);
        Assert.assertEquals(component.getBackground(), list.getBackground());
    }

    public void testSetSelectedItemByValue()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();

        comboBox.setSelectedItem("Ahrimanes");

        Assert.assertEquals(comboBox.getSelectedItem(), "Ahrimanes");
    }

    public void testRendererSupportsRawSelectedValue()
    {
        GroupedComboBox<String> comboBox = this.createComboBox();
        ListCellRenderer<? super GroupedComboBox.ComboBoxEntry<String>> renderer = comboBox.getRenderer();
        JList<GroupedComboBox.ComboBoxEntry<String>> list = new JList<>();

        Component component = ((ListCellRenderer<Object>) renderer).getListCellRendererComponent(
            list,
            "Brujah",
            -1,
            true,
            false
        );

        Assert.assertNotNull(component);
    }

    public void testUngroupedEmptyEntryIsInsertedBeforeGroups()
    {
        GroupedComboBoxModel<String> groupedModel = new GroupedComboBoxModel<>();
        groupedModel.setUngroupedEmptyEntry("");
        groupedModel.addGroup("Main Clans", Arrays.asList("Brujah", "Ventrue"));

        GroupedComboBox<String> comboBox = new GroupedComboBox<>();
        comboBox.setModel(groupedModel);

        Assert.assertTrue(comboBox.getModel().getElementAt(0) instanceof GroupedComboBox.EmptyEntry<?>);
        Assert.assertEquals(comboBox.getSelectedItem(), null);
    }

    public void testSetSelectedItemNullSelectsUngroupedEmptyEntry()
    {
        GroupedComboBoxModel<String> groupedModel = new GroupedComboBoxModel<>();
        groupedModel.setUngroupedEmptyEntry("");
        groupedModel.addGroup("Main Clans", Arrays.asList("Brujah", "Ventrue"));

        GroupedComboBox<String> comboBox = new GroupedComboBox<>();
        comboBox.setModel(groupedModel);
        comboBox.setSelectedItem("Ventrue");

        comboBox.setSelectedItem(null);

        Assert.assertTrue(comboBox.getModel().getElementAt(comboBox.getSelectedIndex()) instanceof GroupedComboBox.EmptyEntry<?>);
        Assert.assertNull(comboBox.getSelectedItem());
    }

    public void testEmptyEntryRendererUsesVisibleBlankText()
    {
        GroupedComboBox<String> comboBox = new GroupedComboBox<>();
        ListCellRenderer<? super GroupedComboBox.ComboBoxEntry<String>> renderer = comboBox.getRenderer();
        JList<GroupedComboBox.ComboBoxEntry<String>> list = new JList<>();

        Component component = ((ListCellRenderer<Object>) renderer).getListCellRendererComponent(
            list,
            new GroupedComboBox.EmptyEntry<String>(""),
            0,
            false,
            false
        );

        Assert.assertTrue(component instanceof JLabel);
        Assert.assertEquals(((JLabel) component).getText(), " ");
    }

    private GroupedComboBox<String> createComboBox()
    {
        GroupedComboBoxModel<String> groupedModel = new GroupedComboBoxModel<>();
        groupedModel.addGroup("Main Clans", Arrays.asList("Brujah", "Ventrue"));
        groupedModel.addGroup("Bloodlines", List.of("Ahrimanes"));

        GroupedComboBox<String> comboBox = new GroupedComboBox<>();
        comboBox.setModel(groupedModel);

        return comboBox;
    }
}
