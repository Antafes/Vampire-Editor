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

import vampireEditor.Configuration;
import vampireEditor.language.LanguageInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
     */
    public BasePanel() {
        super();
        this.configuration = Configuration.getInstance();
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
     * @param type The type to get the fields for
     *
     * @return A list of components
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
     * @param text The text for the label
     *
     * @return The label object
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
     * @param text The text for the group label
     *
     * @return The label object
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
     * @param headline The headline of the element group
     * @param elementList List of elements
     */
    protected void addFields(String headline, ArrayList<String> elementList) {
        this.addFields(headline, true, elementList);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of elements
     */
    abstract protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList);

    /**
     * Add the given fields and with the given headline.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of components
     */
    protected void addFields(String headline, boolean addHeadline, HashMap<String, JComponent> elementList) {
        this.addFields(headline, addHeadline, elementList, true);
    }

    /**
     * Add the given fields and with the given headline.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of elements
     * @param addFieldLabels Whether to add field labels
     */
    protected void addFields(
        String headline, boolean addHeadline, HashMap<String, JComponent> elementList, boolean addFieldLabels
    ) {
        if (!this.getFields().containsKey(headline)) {
            this.getFields().put(headline, new ArrayList<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup();

        if (addHeadline) {
            JLabel groupLabel = this.createGroupLabel(headline);
            groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            listHorizontalGroup
                .addGap(11, 11, 11)
                .addComponent(groupLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
            listVerticalGroup
                .addGap(11, 11, 11)
                .addComponent(groupLabel);
        }

        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup().addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

        listVerticalGroup
            .addGap(11, 11, 11);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerElementHorizontalGroup = layout.createSequentialGroup();

        if (addFieldLabels) {
            outerElementHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        }

        GroupLayout.ParallelGroup labelHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup elementHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        elementList.forEach((key, element) -> {
            HashMap<String, GroupLayout.Group> groups = new HashMap<>();
            groups.put("labelHorizontalGroup", labelHorizontalGroup);
            groups.put("elementHorizontalGroup", elementHorizontalGroup);
            groups.put("listVerticalGroup", listVerticalGroup);
            this.addRow(
                element,
                key,
                this.getFields(headline),
                addFieldLabels,
                groups,
                layout,
                element.getSize().width,
                element.getSize().height
            );
        });

        if (addFieldLabels) {
            GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
            outerLabelHorizontalGroup.addGap(11, 11, 11);
            outerLabelHorizontalGroup.addGroup(labelHorizontalGroup);
            outerLabelHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE);
            listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
        }

        outerElementHorizontalGroup.addGroup(elementHorizontalGroup);
        listHorizontalGroup.addGroup(outerElementHorizontalGroup);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The element to add
     * @param label Label of the element
     * @param fields List of every field added
     * @param groups List of groups in the layout
     * @param layout GroupLayout object
     *
     * @return List of elements that are added
     */
    protected HashMap<String, Component> addRow(
        JComponent element,
        String label,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        return this.addRow(
            element, label, fields, true, groups, layout, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE
        );
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The element to add
     * @param label Label of the element
     * @param fields List of every field added
     * @param addFieldLabels Whether to add field labels
     * @param groups List of groups in the layout
     * @param layout GroupLayout object
     *
     * @return List of elements that are added
     */
    protected HashMap<String, Component> addRow(
        JComponent element,
        String label,
        ArrayList<Component> fields,
        boolean addFieldLabels,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        return this.addRow(
            element, label, fields, addFieldLabels, groups, layout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE
        );
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The element to add
     * @param label Label of the element
     * @param fields List of every field added
     * @param addFieldLabels Whether to add field labels
     * @param groups List of groups in the layout
     * @param layout GroupLayout object
     * @param fieldWidth Width of the field
     * @param fieldHeight Height of the field
     *
     * @return List of elements that are added
     */
    protected HashMap<String, Component> addRow(
        JComponent element,
        String label,
        ArrayList<Component> fields,
        boolean addFieldLabels,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout,
        int fieldWidth,
        int fieldHeight
    ) {
        this.order.add(element);
        fields.add(element);
        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        HashMap<String, Component> elements = new HashMap<>();

        if (addFieldLabels) {
            JLabel elementLabel = this.createLabel(label);
            elementLabel.setLabelFor(element);
            groups.get("labelHorizontalGroup").addComponent(elementLabel);
            verticalGroup.addComponent(elementLabel);
            elements.put("label", elementLabel);
        }

        groups.get("elementHorizontalGroup").addComponent(element, GroupLayout.PREFERRED_SIZE, fieldWidth, GroupLayout.PREFERRED_SIZE);
        verticalGroup.addComponent(element, GroupLayout.PREFERRED_SIZE, fieldHeight, GroupLayout.PREFERRED_SIZE);
        groups.get("listVerticalGroup").addGroup(verticalGroup)
            .addGap(6, 6, 6);

        elements.put("element", element);

        return elements;
    }
}
