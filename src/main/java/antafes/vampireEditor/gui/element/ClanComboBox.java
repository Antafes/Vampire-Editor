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

import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.character.ClanInterface;
import antafes.vampireEditor.entity.character.Weakness;
import antafes.vampireEditor.utility.ClanComparator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Combo box for clan selection with group headlines for clans and bloodlines.
 */
public class ClanComboBox extends JComboBox<ClanInterface>
{
    private static final String CLANS_HEADER = "Clans";
    private static final String BLOODLINES_HEADER = "Bloodlines";

    private ClanInterface lastSelectableItem;

    public ClanComboBox() {
        super();
        this.setRenderer(this.createRenderer());
        this.addActionListener(event -> this.handleSelection());
    }

    /**
     * Populate the combo box with grouped clan entries.
     */
    public void setClans(Collection<Clan> clans) {
        DefaultComboBoxModel<ClanInterface> model = new DefaultComboBoxModel<>();
        model.addElement(null);

        ArrayList<Clan> mainClans = new ArrayList<>();
        ArrayList<Clan> bloodlines = new ArrayList<>();
        clans.forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            } else {
                mainClans.add(clan);
            }
        });

        Comparator<Clan> comparator = new ClanComparator();
        mainClans.sort(comparator);
        bloodlines.sort(comparator);

        if (!mainClans.isEmpty()) {
            model.addElement(new ClanGroupHeader(CLANS_HEADER));
            mainClans.forEach(model::addElement);
        }

        if (!bloodlines.isEmpty()) {
            model.addElement(new ClanGroupHeader(BLOODLINES_HEADER));
            bloodlines.forEach(model::addElement);
        }

        this.setModel(model);
        this.lastSelectableItem = null;
        this.setSelectedItem(null);
    }

    /**
     * Get the selected clan or null.
     */
    public Clan getSelectedClan() {
        ClanInterface selectedItem = (ClanInterface) this.getSelectedItem();
        return selectedItem instanceof Clan ? (Clan) selectedItem : null;
    }

    private void handleSelection() {
        ClanInterface selectedItem = (ClanInterface) this.getSelectedItem();
        if (selectedItem instanceof ClanGroupHeader) {
            this.setSelectedItem(this.lastSelectableItem);
            return;
        }

        this.lastSelectableItem = selectedItem;
    }

    private DefaultListCellRenderer createRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClanGroupHeader) {
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                    label.setForeground(UIManager.getColor("Label.disabledForeground"));
                }

                return label;
            }
        };
    }

    private static class ClanGroupHeader implements ClanInterface
    {
        private final String label;

        private ClanGroupHeader(String label) {
            this.label = label;
        }

        @Override
        public String getKey() {
            return this.label;
        }

        @Override
        public String getName() {
            return this.label;
        }

        @Override
        public String getNickname() {
            return this.label;
        }

        @Override
        public ArrayList<Advantage> getAdvantages() {
            return new ArrayList<>();
        }

        @Override
        public ArrayList<Weakness> getWeaknesses() {
            return new ArrayList<>();
        }

        @Override
        public boolean isBloodline() {
            return false;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}

