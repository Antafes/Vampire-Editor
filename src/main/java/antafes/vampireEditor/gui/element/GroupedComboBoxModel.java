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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper model that stores grouped items while preserving insertion order.
 *
 * @param <T> Item type
 */
public class GroupedComboBoxModel<T>
{
    private final LinkedHashMap<String, List<T>> groups = new LinkedHashMap<>();

    /**
     * Add a group with items in the given order.
     *
     * @param name Group label
     * @param items Group items
     */
    public void addGroup(String name, List<T> items)
    {
        this.groups.put(name, new ArrayList<>(items));
    }

    /**
     * Get all groups as unmodifiable structure.
     *
     * @return Groups mapped by group label
     */
    public Map<String, List<T>> getGroups()
    {
        LinkedHashMap<String, List<T>> copy = new LinkedHashMap<>();

        this.groups.forEach((groupName, groupItems) -> copy.put(groupName, Collections.unmodifiableList(groupItems)));

        return Collections.unmodifiableMap(copy);
    }
}

