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
 * @copyright (c) $year, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.element;

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.gui.BaseWindow;
import sun.awt.SunToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class CloseableTabbedPane extends JTabbedPane {
    private HashMap<Integer, JLabel> titleList;

    public CloseableTabbedPane() {
        this(TOP);
    }

    public CloseableTabbedPane(int tabPlacement) {
        this(tabPlacement, WRAP_TAB_LAYOUT);
    }

    public CloseableTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        this.titleList = new HashMap<>();
    }

    /**
     * Insert a new tab.
     *
     * @param title The title to be displayed on the tab
     * @param icon The icon to be displayed on the tab
     * @param component The component to be displayed when this tab is clicked.
     * @param tip The tooltip to be displayed for this tab
     * @param index The position to insert this new tab ({@code > 0 and <= getTabCount()})
     * @see #insertTab
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);

        this.addTabCloseButtons(title, index);
    }

    @Override
    public void setTitleAt(int index, String title)
    {
        this.titleList.get(index).setText(title);
    }

    /**
     * Add a close button for a tab.
     *
     * @param title Title of the tab
     * @param index Position of the tab
     */
    private void addTabCloseButtons(String title, int index) {
        JPanel panelTab = new JPanel(new GridBagLayout());
        panelTab.setOpaque(false);
        JLabel labelTitle = new JLabel(title);
        JButton closeButton = new JButton();
        Dimension buttonSize = new Dimension();
        buttonSize.setSize(12, 12);
        closeButton.setPreferredSize(buttonSize);
        ImageIcon icon = new ImageIcon(VampireEditor.getResourceInJar("images/x.png"));
        closeButton.setIcon(icon);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;

        panelTab.add(labelTitle, constraints);
        this.titleList.put(index, labelTitle);

        constraints.gridx++;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 3, 0, 0);

        panelTab.add(closeButton, constraints);

        this.setTabComponentAt(index, panelTab);

        TabCloseActionHandler handler = new TabCloseActionHandler(
            this.getComponentAt(index),
            this
        );
        closeButton.addActionListener(handler);
    }

    /**
     * Close action handler.
     */
    private class TabCloseActionHandler implements ActionListener {
        private final Component tab;
        private final CloseableTabbedPane pane;

        /**
         * Constructor
         *
         * @param tab The tab that should be closed
         * @param pane The pane the tab resides in
         */
        public TabCloseActionHandler(Component tab, CloseableTabbedPane pane) {
            this.tab = tab;
            this.pane = pane;
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e Event object
         * @see #actionPerformed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.pane.remove(this.tab);
            BaseWindow window = (BaseWindow) SunToolkit.getContainingWindow(this.pane);

            if (window.isNoCharacterLoaded()) {
                window.disablePrintMenuItem();
                window.disableSaveMenuItem();
            }
        }
    }
}
