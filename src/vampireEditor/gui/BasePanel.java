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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import vampireEditor.Configuration;
import vampireEditor.language.LanguageInterface;

/**
 * BasePanel object.
 *
 * @author Marian Pollzien
 */
abstract public class BasePanel extends JPanel {

    private final Configuration configuration;
    private LanguageInterface language;
    private GroupLayout.ParallelGroup outerParallelHorizontalGroup;
    private GroupLayout.ParallelGroup outerParallelVerticalGroup;
    private GroupLayout.SequentialGroup outerSequentialHorizontalGroup;
    private GroupLayout.SequentialGroup outerSequentialVerticalGroup;
    private final Vector<Component> order;
    private final HashMap<String, ArrayList<Component>> fields;
    private boolean translateFieldLabels = true;
    private boolean translateGroupLabels = true;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param configuration
     */
    public BasePanel(Configuration configuration) {
        super();
        this.configuration = configuration;
        this.language = this.configuration.getLanguageObject();
        this.order = new Vector<>();
        this.fields = new HashMap<>();
    }

    /**
     * Start up the creation process.
     */
    public void start() {
        this.initComponents();
        this.init();
    }

    /**
     * This method is called to initialize the form.
     */
    @SuppressWarnings("unchecked")
    protected void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        this.outerParallelHorizontalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        this.outerSequentialHorizontalGroup = layout.createSequentialGroup();
        layout.setHorizontalGroup(
            this.outerParallelHorizontalGroup
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, this.outerSequentialHorizontalGroup)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
            )
        );
        this.outerParallelVerticalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        this.outerSequentialVerticalGroup = layout.createSequentialGroup();
        layout.setVerticalGroup(
            this.outerParallelVerticalGroup
            .addGroup(this.outerSequentialVerticalGroup
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                )
            )
        );
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
    protected void createFocusTraversalPolicy() {
        this.setFocusTraversalPolicy(new NewCharacterFocusTraversalPolicy(this.order));
        this.setFocusTraversalPolicyProvider(true);
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
     * Set the language object. This will be used if the language has been changed to update everything.
     *
     * @param language
     */
    protected void setLanguage(LanguageInterface language) {
        this.language = language;
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
     * Get the outer sequential vertical group.
     *
     * @return
     */
    public GroupLayout.SequentialGroup getOuterSequentialVerticalGroup() {
        return this.outerSequentialVerticalGroup;
    }

    /**
     * Get the outer parallel vertical group.
     *
     * @return
     */
    protected GroupLayout.ParallelGroup getOuterParallelVerticalGroup() {
        return this.outerParallelVerticalGroup;
    }

    /**
     * Get the outer sequential horizontal group.
     *
     * @return
     */
    protected GroupLayout.SequentialGroup getOuterSequentialHorizontalGroup() {
        return this.outerSequentialHorizontalGroup;
    }

    /**
     * Get the outer parallel horizontal group.
     *
     * @return
     */
    public GroupLayout.ParallelGroup getOuterParallelHorizontalGroup() {
        return this.outerParallelHorizontalGroup;
    }

    /**
     * Set the outer parallel horizontal group.
     *
     * @param outerParallelHorizontalGroup
     */
    public void setOuterParallelHorizontalGroup(GroupLayout.ParallelGroup outerParallelHorizontalGroup) {
        this.outerParallelHorizontalGroup = outerParallelHorizontalGroup;
    }

    /**
     * Set the outer parallel vertical group.
     *
     * @param outerParallelVerticalGroup
     */
    public void setOuterParallelVerticalGroup(GroupLayout.ParallelGroup outerParallelVerticalGroup) {
        this.outerParallelVerticalGroup = outerParallelVerticalGroup;
    }

    /**
     * Set the outer sequential horizontal group.
     *
     * @param outerSequentialHorizontalGroup
     */
    public void setOuterSequentialHorizontalGroup(GroupLayout.SequentialGroup outerSequentialHorizontalGroup) {
        this.outerSequentialHorizontalGroup = outerSequentialHorizontalGroup;
    }

    /**
     * Set the outer sequential vertical group.
     *
     * @param outerSequentialVerticalGroup
     */
    public void setOuterSequentialVerticalGroup(GroupLayout.SequentialGroup outerSequentialVerticalGroup) {
        this.outerSequentialVerticalGroup = outerSequentialVerticalGroup;
    }

    /**
     * Set whether the field labels should be translated or not.
     *
     * @param translateFieldLabels
     */
    protected void setTranslateFieldLabels(boolean translateFieldLabels) {
        this.translateFieldLabels = translateFieldLabels;
    }

    /**
     * Set whether the group labels should be translated or not.
     *
     * @param translateGroupLabels
     */
    public void setTranslateGroupLabels(boolean translateGroupLabels) {
        this.translateGroupLabels = translateGroupLabels;
    }

    /**
     * Create a label element for a field.
     * Depending on whether translateFieldLabels is set to true or not, the label text will be translated.
     *
     * @param text
     *
     * @return
     */
    protected JLabel createLabel(String text) {
        JLabel label = new JLabel();

        if (this.translateFieldLabels) {
            label.setText(this.language.translate(text));
        } else {
            label.setText(text);
        }

        return label;
    }

    /**
     * Create a label element for a group of fields.
     * Depending on whether translateFieldLabels is set to true or not, the label text will be translated.
     *
     * @param text
     *
     * @return
     */
    protected JLabel createGroupLabel(String text) {
        JLabel label = new JLabel();

        if (this.translateGroupLabels) {
            label.setText(this.language.translate(text));
        } else {
            label.setText(text);
        }

        return label;
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
     *
     * @return
     */
    abstract protected HashMap<String, Component> addRow(
        String element, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout
    );
}
