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

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for grouped combo box entries.
 *
 * @param <T> Item type
 */
public class GroupedComboBoxRenderer<T> implements ListCellRenderer<GroupedComboBox.ComboBoxEntry<T>>
{
    private final DefaultListCellRenderer delegate = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(
        JList<? extends GroupedComboBox.ComboBoxEntry<T>> list,
        GroupedComboBox.ComboBoxEntry<T> value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    )
    {
        if (value instanceof GroupedComboBox.HeaderEntry<T> headerEntry) {
            return this.createHeaderComponent(list, headerEntry, index);
        }

        String text = value != null ? value.getText() : "";
        return this.delegate.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
    }

    private Component createHeaderComponent(
        JList<? extends GroupedComboBox.ComboBoxEntry<T>> list,
        GroupedComboBox.HeaderEntry<T> headerEntry,
        int index
    )
    {
        JLabel base = (JLabel) this.delegate.getListCellRendererComponent(list, "", index, false, false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(6, 0));
        panel.setOpaque(true);
        panel.setBackground(base.getBackground());
        panel.setBorder(base.getBorder());

        JLabel textLabel = new JLabel(headerEntry.getText());
        textLabel.setOpaque(false);
        textLabel.setFont(base.getFont().deriveFont(Font.BOLD));
        textLabel.setForeground(base.getForeground());

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(base.getForeground());

        panel.add(textLabel, BorderLayout.WEST);
        panel.add(separator, BorderLayout.CENTER);

        return panel;
    }
}

