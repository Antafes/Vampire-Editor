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
 * @copyright (c) 2018, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.print.General;
import antafes.vampireEditor.print.PrintBase;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class PrintPreviewPanel extends JPanel implements TranslatableComponent {
    @Getter
    @Setter
    private antafes.vampireEditor.entity.Character character;
    private PrintBase page;

    /**
     * Start construction of the print preview
     */
    public void start() {
        // Initially set the general page to be shown.
        General general = new General(this.character);
        general.create();
        this.page = general;

        this.addContent();
    }

    /**
     * Add content elements
     */
    protected void addContent() {
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            // Have to use the Nimbus LaF to use font files inside the text pane.
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBackground(Color.WHITE);
            scrollPane.setViewportView(this.page);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);

            UIManager.setLookAndFeel(previousLF);

            BasicArrowButton rightButton = new BasicArrowButton(BasicArrowButton.EAST);
            rightButton.addActionListener((ActionEvent e) -> {
                this.nextPage();
            });

            if (this.page.getFollowingPageObject() == null) {
                rightButton.setEnabled(false);
            }

            BasicArrowButton leftButton = new BasicArrowButton(BasicArrowButton.WEST);
            leftButton.addActionListener((ActionEvent e) -> {
                this.previousPage();
            });

            if (this.page.getPreviousPageObject() == null) {
                leftButton.setEnabled(false);
            }

            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(
                        layout.createSequentialGroup()
                            .addContainerGap(400, Integer.MAX_VALUE)
                            .addComponent(leftButton, 20, 20, 20)
                            .addComponent(rightButton, 20, 20, 20)
                            .addContainerGap()
                    )
                    .addGroup(
                        layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 912, Short.MAX_VALUE)
                            .addContainerGap()
                    )
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGap(5)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(leftButton, 20, 20, 20)
                                .addComponent(rightButton, 20, 20, 20)
                        )
                        .addGap(5)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                        .addContainerGap()
                )
            );
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException ex) {
            Logger.getLogger(PrintPreviewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update the texts of every component in the component.
     */
    @Override
    public void updateTexts() {
        this.page.updateTexts();
    }

    /**
     * Switch to the next page.
     */
    private void nextPage() {
        PrintBase nextPage = this.page.getFollowingPageObject();
        nextPage.create();
        this.page = nextPage;
        this.removeAll();
        this.addContent();
    }

    /**
     * Switch to the previous page.
     */
    private void previousPage() {
        PrintBase previousPage = this.page.getPreviousPageObject();
        previousPage.create();
        this.page = previousPage;
        this.removeAll();
        this.addContent();
    }
}
