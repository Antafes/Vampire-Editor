/**
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
package vampireEditor.gui.newCharacter;

import vampireEditor.gui.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import vampireEditor.Configuration;

/**
 * BasePanel object.
 *
 * @author Marian Pollzien
 */
abstract public class BasePanel extends vampireEditor.gui.BasePanel {

    private final NewCharacterDialog parent;
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;

    /**
     * Creates new form BasePanel
     *
     * @param parent
     * @param configuration
     */
    public BasePanel(NewCharacterDialog parent, Configuration configuration) {
        super(configuration);
        this.parent = parent;

        this.start();
    }

    /**
     * This method is called to initialize the form.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void initComponents() {

        this.backButton = new javax.swing.JButton();
        this.nextButton = new javax.swing.JButton();

        this.backButton.setText(this.getLanguage().translate("back"));
        this.backButton.addActionListener((ActionEvent evt) -> {
            backButtonActionPerformed(evt);
        });

        this.nextButton.setText(this.getLanguage().translate("next"));
        this.nextButton.setEnabled(false);
        this.nextButton.addActionListener((ActionEvent evt) -> {
            nextButtonActionPerformed(evt);
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        this.setOuterParallelHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING));
        this.setOuterSequentialHorizontalGroup(layout.createSequentialGroup());
        layout.setHorizontalGroup(
            this.getOuterParallelHorizontalGroup()
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, this.getOuterSequentialHorizontalGroup())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
                .addComponent(this.backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
            )
        );
        this.setOuterParallelVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING));
        this.setOuterSequentialVerticalGroup(layout.createSequentialGroup());
        layout.setVerticalGroup(
            this.getOuterParallelVerticalGroup()
            .addGroup(this.getOuterSequentialVerticalGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(this.nextButton)
                        .addComponent(this.backButton)
                    )
                    .addContainerGap()
                )
            )
        );
    }

    /**
     * ActionPerformed listener for the back button.
     *
     * @param evt
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.parent.getCharacterTabPane().setSelectedIndex(this.parent.getCharacterTabPane().getSelectedIndex() - 1);
    }

    /**
     * ActionPerformed listener for the next button.
     *
     * @param evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.parent.getCharacterTabPane().setSelectedIndex(this.parent.getCharacterTabPane().getSelectedIndex() + 1);
    }

    /**
     * Create the focus traversal policy for the attributes tab.
     */
    @Override
    protected void createFocusTraversalPolicy() {
        this.getOrder().add(this.nextButton);
        this.getOrder().add(this.backButton);

        super.createFocusTraversalPolicy();
    }

    /**
     * Enable the next button.
     */
    protected void enableNextButton() {
        this.nextButton.setEnabled(true);
    }

    /**
     * Disable the next button.
     */
    protected void disableNextButton() {
        this.nextButton.setEnabled(false);
    }

    /**
     * Get the parent component.
     *
     * @return
     */
    public NewCharacterDialog getParentComponent() {
        return this.parent;
    }

    /**
     * Get the back button component.
     *
     * @return
     */
    public JButton getBackButton() {
        return this.backButton;
    }

    /**
     * Get the next button component.
     *
     * @return
     */
    public JButton getNextButton() {
        return this.nextButton;
    }

    /**
     * Check if every attribute has been set.
     */
    abstract protected void checkFieldsFilled();

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    abstract public boolean checkAllFields();

    /**
     * Get a list with all field values.
     *
     * @param character
     */
    abstract public void fillCharacter(vampireEditor.Character character);
}
