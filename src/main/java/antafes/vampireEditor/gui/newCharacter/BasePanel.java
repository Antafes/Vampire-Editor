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
package antafes.vampireEditor.gui.newCharacter;

import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.gui.NewCharacterDialog;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * BasePanel object.
 *
 * @author Marian Pollzien
 */
@Getter
abstract public class BasePanel extends antafes.vampireEditor.gui.BasePanel {
    @Getter(AccessLevel.NONE)
    private final NewCharacterDialog parent;
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;

    /**
     * Creates new form BasePanel
     *
     * @param parent Parent element
     */
    public BasePanel(NewCharacterDialog parent) {
        super();
        this.parent = parent;

        this.start();
    }

    /**
     * This method is called to initialize the form.
     */
    @Override
    protected void initComponents() {

        this.backButton = new javax.swing.JButton();
        this.nextButton = new javax.swing.JButton();

        this.getBackButton().setText(this.getLanguage().translate("back"));
        this.getBackButton().addActionListener(this::backButtonActionPerformed);

        this.getNextButton().setText(this.getLanguage().translate("next"));
        this.getNextButton().setEnabled(false);
        this.getNextButton().addActionListener(this::nextButtonActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        this.setOuterParallelHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING));
        this.setOuterSequentialHorizontalGroup(layout.createSequentialGroup());
        layout.setHorizontalGroup(
            this.getOuterParallelHorizontalGroup()
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, this.getOuterSequentialHorizontalGroup())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
                .addComponent(this.getBackButton(), javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.getNextButton(), javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(this.getNextButton())
                        .addComponent(this.getBackButton())
                    )
                    .addContainerGap()
                )
            )
        );
    }

    /**
     * ActionPerformed listener for the back button.
     *
     * @param evt Action event for the button
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.parent.getCharacterTabPane().setSelectedIndex(this.parent.getCharacterTabPane().getSelectedIndex() - 1);
    }

    /**
     * ActionPerformed listener for the next button.
     *
     * @param evt Action event for the button
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.parent.getCharacterTabPane().setSelectedIndex(this.parent.getCharacterTabPane().getSelectedIndex() + 1);
    }

    /**
     * Create the focus traversal policy for the attributes tab.
     */
    @Override
    protected void createFocusTraversalPolicy() {
        this.getOrder().add(this.getNextButton());
        this.getOrder().add(this.getBackButton());

        super.createFocusTraversalPolicy();
    }

    /**
     * Enable the next button.
     */
    protected void enableNextButton() {
        this.getNextButton().setEnabled(true);
    }

    /**
     * Disable the next button.
     */
    protected void disableNextButton() {
        this.getNextButton().setEnabled(false);
    }

    /**
     * Get the parent component.
     */
    public NewCharacterDialog getParentComponent() {
        return this.parent;
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
     * @param builder Character builder object
     */
    abstract public void fillCharacter(Character.CharacterBuilder<?, ?> builder);
}
