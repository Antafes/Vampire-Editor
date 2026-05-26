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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Renderer for grouped combo box entries.
 *
 * @param <T> Item type
 */
public class GroupedComboBoxRenderer<T> implements ListCellRenderer<Object>
{
    private final DefaultListCellRenderer delegate = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    )
    {
        if (value instanceof GroupedComboBox.HeaderEntry<?> headerEntry) {
            return this.createHeaderComponent(list, headerEntry, index);
        }

        if (value instanceof GroupedComboBox.ItemEntry<?> itemEntry) {
            Component component = this.delegate.getListCellRendererComponent(list, itemEntry.getText(), index, isSelected, cellHasFocus);
            this.applyItemPadding(component, index);
            return component;
        }

        if (value instanceof GroupedComboBox.EmptyEntry<?> emptyEntry) {
            String text = emptyEntry.getText().isEmpty() ? " " : emptyEntry.getText();
            Component component = this.delegate.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            this.applyItemPadding(component, index);
            return component;
        }

        String text = value != null ? value.toString() : "";
        Component component = this.delegate.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        this.applyItemPadding(component, index);
        return component;
    }

    private Component createHeaderComponent(
        JList<?> list,
        GroupedComboBox.HeaderEntry<?> headerEntry,
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

        // Keep the line vertically centered in taller combo rows.
        JPanel separatorContainer = new JPanel(new GridBagLayout());
        separatorContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        separatorContainer.add(separator, gbc);

        panel.add(textLabel, BorderLayout.WEST);
        panel.add(separatorContainer, BorderLayout.CENTER);

        return panel;
    }

    private void applyItemPadding(Component component, int index)
    {
        if (index < 0 || !(component instanceof JComponent jComponent)) {
            return;
        }

        Border currentBorder = jComponent.getBorder();
        jComponent.setBorder(BorderFactory.createCompoundBorder(currentBorder, new EmptyBorder(0, 10, 0, 0)));
    }
}
