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

import lombok.Getter;

import javax.swing.*;

/**
 * Generic combo box that supports non-selectable group headers.
 *
 * @param <T> Item type
 */
public class GroupedComboBox<T> extends JComboBox<GroupedComboBox.ComboBoxEntry<T>>
{
    @Getter
    private ItemEntry<T> lastSelectableEntry;

    /**
     * Create a grouped combo box.
     */
    public GroupedComboBox()
    {
        super();
        super.setRenderer(new GroupedComboBoxRenderer<>());
        this.addActionListener(event -> this.handleSelection());
    }

    /**
     * Populate this combo box from a grouped model.
     *
     * @param groupedModel Grouped model
     */
    public void setModel(GroupedComboBoxModel<T> groupedModel)
    {
        DefaultComboBoxModel<ComboBoxEntry<T>> model = new DefaultComboBoxModel<>();

        groupedModel.getGroups().forEach((groupName, groupItems) -> {
            if (groupItems.isEmpty()) {
                return;
            }

            model.addElement(new HeaderEntry<>(new GroupHeader(groupName)));
            groupItems.forEach(item -> model.addElement(new ItemEntry<>(item)));
        });

        super.setModel(model);
        this.lastSelectableEntry = null;
        this.selectFirstSelectableItem();
    }

    /**
     * Return the selected item value. Headers return null.
     *
     * @return Selected item or null
     */
    @Override
    public T getSelectedItem()
    {
        Object selected = super.getSelectedItem();

        if (selected instanceof ItemEntry<?> itemEntry) {
            @SuppressWarnings("unchecked") T item = (T) itemEntry.getItem();
            return item;
        }

        return null;
    }

    /**
     * Select by item value, combo box entry, or null.
     *
     * @param object Item value, internal entry object, or null
     */
    @Override
    public void setSelectedItem(Object object)
    {
        if (object == null || object instanceof ComboBoxEntry<?>) {
            super.setSelectedItem(object);
            return;
        }

        int itemIndex = this.findItemIndex(object);
        if (itemIndex >= 0) {
            this.setSelectedIndex(itemIndex);
        }
    }

    /**
     * Keep headers non-selectable even for keyboard navigation.
     *
     * @param index Desired index
     */
    @Override
    public void setSelectedIndex(int index)
    {
        ComboBoxModel<ComboBoxEntry<T>> model = this.getModel();

        if (index < 0 || index >= model.getSize()) {
            super.setSelectedIndex(index);
            if (index < 0) {
                this.lastSelectableEntry = null;
            }
            return;
        }

        ComboBoxEntry<T> candidate = model.getElementAt(index);
        if (candidate instanceof ItemEntry<?> itemEntry) {
            super.setSelectedIndex(index);
            @SuppressWarnings("unchecked") ItemEntry<T> selectedEntry = (ItemEntry<T>) itemEntry;
            this.lastSelectableEntry = selectedEntry;
            return;
        }

        int currentIndex = super.getSelectedIndex();
        int direction = index >= currentIndex ? 1 : -1;
        int fallbackDirection = -direction;
        int nextSelectable = this.findSelectableIndex(index, direction);

        if (nextSelectable < 0) {
            nextSelectable = this.findSelectableIndex(index, fallbackDirection);
        }

        if (nextSelectable >= 0) {
            this.setSelectedIndex(nextSelectable);
            return;
        }

        if (this.lastSelectableEntry != null) {
            super.setSelectedItem(this.lastSelectableEntry);
        } else {
            super.setSelectedIndex(-1);
        }
    }

    /**
     * Internal grouped entry marker.
     *
     * @param <T> Item type
     */
    public sealed interface ComboBoxEntry<T> permits HeaderEntry, ItemEntry
    {
        String getText();
    }

    /**
     * Header entry wrapper.
     *
     * @param <T> Item type
     */
    public static final class HeaderEntry<T> implements ComboBoxEntry<T>
    {
        @Getter
        private final GroupHeader header;

        public HeaderEntry(GroupHeader header)
        {
            this.header = header;
        }

        @Override
        public String getText()
        {
            return this.header.text();
        }

        @Override
        public String toString()
        {
            return this.header.toString();
        }
    }

    /**
     * Selectable item entry wrapper.
     *
     * @param <T> Item type
     */
    public static final class ItemEntry<T> implements ComboBoxEntry<T>
    {
        @Getter
        private final T item;

        public ItemEntry(T item)
        {
            this.item = item;
        }

        @Override
        public String getText()
        {
            return this.item != null ? this.item.toString() : "";
        }

        @Override
        public String toString()
        {
            return this.getText();
        }
    }

    private void handleSelection()
    {
        Object selected = super.getSelectedItem();

        if (selected instanceof HeaderEntry<?>) {
            if (this.lastSelectableEntry != null) {
                super.setSelectedItem(this.lastSelectableEntry);
            } else {
                super.setSelectedIndex(-1);
            }
            return;
        }

        if (selected instanceof ItemEntry<?> itemEntry) {
            @SuppressWarnings("unchecked") ItemEntry<T> typedEntry = (ItemEntry<T>) itemEntry;
            this.lastSelectableEntry = typedEntry;
        }
    }

    private int findItemIndex(Object item)
    {
        ComboBoxModel<ComboBoxEntry<T>> model = this.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            ComboBoxEntry<T> entry = model.getElementAt(i);
            if (entry instanceof ItemEntry<?> itemEntry && java.util.Objects.equals(itemEntry.getItem(), item)) {
                return i;
            }
        }

        return -1;
    }

    private int findSelectableIndex(int startIndex, int direction)
    {
        ComboBoxModel<ComboBoxEntry<T>> model = this.getModel();

        for (int i = startIndex + direction; i >= 0 && i < model.getSize(); i += direction) {
            if (model.getElementAt(i) instanceof ItemEntry<?>) {
                return i;
            }
        }

        return -1;
    }

    private void selectFirstSelectableItem()
    {
        int index = this.findSelectableIndex(-1, 1);

        if (index >= 0) {
            this.setSelectedIndex(index);
        } else {
            super.setSelectedIndex(-1);
        }
    }
}

