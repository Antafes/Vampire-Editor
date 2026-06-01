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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Test
public class GroupedComboBoxModelTest extends BaseTest
{
    public void testAddGroupPreservesGroupAndItemOrder()
    {
        GroupedComboBoxModel<String> model = new GroupedComboBoxModel<>();
        model.addGroup("Main", Arrays.asList("Brujah", "Ventrue"));
        model.addGroup("Bloodlines", Arrays.asList("Ahrimanes"));

        Map<String, List<String>> groups = model.getGroups();

        Assert.assertEquals(new ArrayList<>(groups.keySet()), Arrays.asList("Main", "Bloodlines"));
        Assert.assertEquals(groups.get("Main"), Arrays.asList("Brujah", "Ventrue"));
        Assert.assertEquals(groups.get("Bloodlines"), Arrays.asList("Ahrimanes"));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testReturnedMapIsUnmodifiable()
    {
        GroupedComboBoxModel<String> model = new GroupedComboBoxModel<>();
        model.addGroup("Main", Arrays.asList("Brujah"));

        Map<String, List<String>> groups = model.getGroups();
        groups.put("Another", Arrays.asList("Item"));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testReturnedListIsUnmodifiable()
    {
        GroupedComboBoxModel<String> model = new GroupedComboBoxModel<>();
        model.addGroup("Main", Arrays.asList("Brujah"));

        Map<String, List<String>> groups = model.getGroups();
        groups.get("Main").add("Ventrue");
    }

    public void testUngroupedEmptyEntryCanBeEnabled()
    {
        GroupedComboBoxModel<String> model = new GroupedComboBoxModel<>();
        model.setUngroupedEmptyEntry("");

        Assert.assertTrue(model.hasUngroupedEmptyEntry());
        Assert.assertEquals(model.getUngroupedEmptyEntryText(), "");
    }

    public void testUngroupedEmptyEntryDefaultsNullTextToEmptyString()
    {
        GroupedComboBoxModel<String> model = new GroupedComboBoxModel<>();
        model.setUngroupedEmptyEntry(null);

        Assert.assertTrue(model.hasUngroupedEmptyEntry());
        Assert.assertEquals(model.getUngroupedEmptyEntryText(), "");
    }
}
