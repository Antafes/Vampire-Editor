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
package vampireEditor.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import vampireEditor.Configuration;
import vampireEditor.language.LanguageInterface;

/**
 * BasePanel object.
 *
 * @author Marian Pollzien
 */
abstract class BasePanel extends JPanel {

    private final Configuration configuration;
    private final LanguageInterface language;
    private final NewCharacterDialog parent;
    private GroupLayout.ParallelGroup outerParallelHorizontalGroup;
    private GroupLayout.ParallelGroup outerParallelVerticalGroup;
    private GroupLayout.SequentialGroup outerSequentialHorizontalGroup;
    private GroupLayout.SequentialGroup outerSequentialVerticalGroup;
    private final Vector<Component> order;
    private final HashMap<String, ArrayList<Component>> fields;
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param parent
     * @param configuration
     */
    public BasePanel(NewCharacterDialog parent, Configuration configuration) {
        super();
        this.parent = parent;
        this.configuration = configuration;
        this.language = this.configuration.getLanguageObject();
        this.order = new Vector<>();
        this.fields = new HashMap<>();

        this.start();
    }

    /**
     * Start up the creation process.
     */
    private void start() {
        this.initComponents();
        this.init();
    }

    /**
     * This method is called to initialize the form.
     */
    @SuppressWarnings("unchecked")
    protected void initComponents() {

        this.backButton = new javax.swing.JButton();
        this.nextButton = new javax.swing.JButton();

        this.backButton.setText(this.language.translate("back"));
        this.backButton.addActionListener((ActionEvent evt) -> {
            backButtonActionPerformed(evt);
        });

        this.nextButton.setText(this.language.translate("next"));
        this.nextButton.setEnabled(false);
        this.nextButton.addActionListener((ActionEvent evt) -> {
            nextButtonActionPerformed(evt);
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        this.outerParallelHorizontalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        this.outerSequentialHorizontalGroup = layout.createSequentialGroup();
        layout.setHorizontalGroup(
            this.outerParallelHorizontalGroup
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, this.outerSequentialHorizontalGroup)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
                .addComponent(this.backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
            )
        );
        this.outerParallelVerticalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        this.outerSequentialVerticalGroup = layout.createSequentialGroup();
        layout.setVerticalGroup(
            this.outerParallelVerticalGroup
            .addGroup(this.outerSequentialVerticalGroup
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
     * Initialize everything.
     */
    protected void init() {
        this.createFocusTraversalPolicy();
    }

    /**
     * Create the focus traversal policy for the attributes tab.
     */
    private void createFocusTraversalPolicy() {
        this.order.add(this.nextButton);
        this.order.add(this.backButton);

        this.setFocusTraversalPolicy(new NewCharacterFocusTraversalPolicy(this.order));
        this.setFocusTraversalPolicyProvider(true);
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
     * Get the fields for the given type.
     *
     * @param type
     *
     * @return
     */
    public ArrayList<Component> getFields(String type) {
        return this.fields.get(type);
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
     * Get the configuration object.
     *
     * @return
     */
    protected Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Get the language object.
     *
     * @return
     */
    protected LanguageInterface getLanguage() {
        return this.language;
    }

    /**
     * Get the order vector for the traversal policy.
     *
     * @return
     */
    protected Vector<Component> getOrder() {
        return this.order;
    }

    /**
     * Get the fields hashmap.
     *
     * @return
     */
    protected HashMap<String, ArrayList<Component>> getFields() {
        return fields;
    }

    /**
     * Get the outer parallel vertical group.
     *
     * @return
     */
    protected GroupLayout.ParallelGroup getOuterParallelVerticalGroup() {
        return outerParallelVerticalGroup;
    }

    /**
     * Get the outer sequential horizontal group.
     *
     * @return
     */
    protected GroupLayout.SequentialGroup getOuterSequentialHorizontalGroup() {
        return outerSequentialHorizontalGroup;
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
     * Add fields by the given list and under the given headline.
     *
     * @param headline
     * @param elementList
     */
    abstract protected void addFields(String headline, ArrayList<String> elementList);

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param fields
     * @param groups
     * @param layout
     */
    abstract protected HashMap<String, Component> addRow(
        String element, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout
    );

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
