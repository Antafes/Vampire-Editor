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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import vampireEditor.Configuration;

/**
 *
 * @author Marian Pollzien
 */
abstract class BaseEditableListPanel extends BaseListPanel {
    public final int UNLIMITEDMAXFIELDS = -1;

    private boolean useWeightings = true;
    private HashMap<String, ArrayList<JComboBox>> comboBoxes;
    private int spinnerMaximumValue = 10;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param parent
     * @param configuration
     */
    public BaseEditableListPanel(NewCharacterDialog parent, Configuration configuration) {
        super(parent, configuration);
    }

    /**
     * This method is called to initialize the form.
     */
    @Override
    protected void initComponents() {
        super.initComponents();

        this.comboBoxes = new HashMap<>();
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        super.init();
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param elementList
     * @param spinnerMinimum
     */
    @Override
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum) {
        this.addFields(headline, null, elementList, spinnerMinimum, this.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param elementList
     * @param spinnerMinimum
     * @param maxFields
     */
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum, int maxFields) {
        this.addFields(headline, null, elementList, spinnerMinimum, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param type
     * @param elementList
     */
    protected void addFields(String headline, String type, ArrayList<String> elementList) {
        this.addFields(headline, type, elementList, 0, this.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param type
     * @param elementList
     * @param maxFields
     */
    protected void addFields(String headline, String type, ArrayList<String> elementList, int maxFields) {
        this.addFields(headline, type, elementList, 0, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param type
     */
    protected void addFields(String headline, String type) {
        this.addFields(headline, type, new ArrayList<>(), 0, this.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param type
     * @param maxFields
     */
    protected void addFields(String headline, String type, int maxFields) {
        this.addFields(headline, type, new ArrayList<>(), 0, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param type
     * @param elementList
     * @param spinnerMinimum
     * @param maxFields
     */
    protected void addFields(String headline, String type, ArrayList<String> elementList, int spinnerMinimum, int maxFields) {
        if (!this.getFields().containsKey(headline)) {
            this.getFields().put(headline, new ArrayList<>());
        }

        if (!this.getPointFields().containsKey(headline)) {
            this.getPointFields().put(headline, new HashMap<>());
        }

        if (!this.comboBoxes.containsKey(headline)) {
            this.comboBoxes.put(type, new ArrayList<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.getLanguage().translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup().addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel);

        if (this.isUsingWeightings()) {
            JComboBox weightingElement = this.addWeighting(headline);
            listHorizontalGroup.addGroup(layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
                .addComponent(weightingElement, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
            );

            listVerticalGroup.addGap(6, 6, 6)
                .addComponent(weightingElement, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
        }

        listVerticalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup listOuterVerticalGroup = layout.createSequentialGroup();
        listVerticalGroup.addGroup(listOuterVerticalGroup);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        outerLabelHorizontalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup outerElementHorizontalGroup = layout.createSequentialGroup();
        outerElementHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        GroupLayout.ParallelGroup labelHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup elementHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        HashMap<String, GroupLayout.Group> groups = new HashMap<>();
        groups.put("labelHorizontalGroup", labelHorizontalGroup);
        groups.put("elementHorizontalGroup", elementHorizontalGroup);
        groups.put("listOuterVerticalGroup", listVerticalGroup);
        groups.put("listVerticalGroup", listOuterVerticalGroup);
        groups.put("listHorizontalGroup", listHorizontalGroup);

        elementList.forEach((element) -> {
            HashMap<String, Component> newElements = this.addRow(
                element, this.getEntity(type, element), type, spinnerMinimum, this.getFields(headline), groups, layout
            );
            this.comboBoxes.get(type).add((JComboBox) newElements.get("comboBox"));
        });

        if (maxFields == this.UNLIMITEDMAXFIELDS || this.getFields(headline).size() < maxFields) {
            HashMap<String, Component> newElements = this.addRow(
                null, type, spinnerMinimum, this.getFields(headline), groups, layout
            );
            ((JComboBox) newElements.get("comboBox")).addItemListener(
                this.getComboBoxItemListener(type, spinnerMinimum, this.getFields(headline), groups, layout, maxFields)
            );
            this.comboBoxes.get(type).add((JComboBox) newElements.get("comboBox"));
        }

        this.addPointFields(headline, groups, layout);
        outerLabelHorizontalGroup.addGroup(labelHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
        outerElementHorizontalGroup.addGroup(elementHorizontalGroup);
        listHorizontalGroup.addGroup(outerElementHorizontalGroup);
        outerLabelHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    @Override
    protected HashMap<String, Component> addRow(String element, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, null, null, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param selected
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    protected HashMap<String, Component> addRow(String element, Object selected, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, selected, null, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param selected
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    protected HashMap<String, Component> addRow(String element, String type, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, null, type, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param selected
     * @param type
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    protected HashMap<String, Component> addRow(
        String element,
        Object selected,
        String type,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        JComboBox elementComboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        if (element == null) {
            model.addElement("");
        }

        this.getValues(type).forEach((value) -> {
            model.addElement(value);
        });
        elementComboBox.setModel(model);

        if (selected != null) {
            elementComboBox.setSelectedIndex(this.getValues(type).indexOf(selected));
        }

        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, this.spinnerMaximumValue, 1));
        Dimension spinnerDimension = new Dimension(36, 20);
        spinner.setPreferredSize(spinnerDimension);
        spinner.setMinimumSize(spinnerDimension);
        spinner.setMaximumSize(spinnerDimension);
        spinner.setName(element != null ? element : "new");
        this.addChangeListener(spinner);
        this.getOrder().add(spinner);
        fields.add(spinner);
        groups.get("labelHorizontalGroup").addComponent(elementComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
        groups.get("elementHorizontalGroup").addComponent(spinner);
        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        verticalGroup.addComponent(elementComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(spinner);
        groups.get("listVerticalGroup").addGroup(verticalGroup)
            .addGap(6, 6, 6);

        HashMap<String, Component> elements = new HashMap<>();
        elements.put("comboBox", elementComboBox);
        elements.put("spinner", spinner);

        return elements;
    }

    /**
     * Create an item listener for the combobox.
     *
     * @param type
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     * @param maxFields
     *
     * @return
     */
    protected ItemListener getComboBoxItemListener(
        String type,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout,
        int maxFields
    ) {
        return (ItemEvent e) -> {
            JComboBox element = (JComboBox) e.getSource();

            if (element.getSelectedItem() == null || element.getSelectedItem().equals("")) {
                return;
            }

            HashMap<String, Component> newElements = this.addRow(
                null, type, spinnerMinimum, fields, groups, layout
            );
            this.comboBoxes.get(type).add((JComboBox) newElements.get("comboBox"));

            if (maxFields == this.UNLIMITEDMAXFIELDS || fields.size() < maxFields) {
                ((JComboBox) newElements.get("comboBox")).addItemListener(
                    this.getComboBoxItemListener(type, spinnerMinimum, fields, groups, layout, maxFields)
                );
            }

            element.removeItemListener(element.getItemListeners()[0]);

            // The below method calls are needed to show the newly added components
            this.revalidate();
            this.repaint();
        };
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     *
     * @param headline
     *
     * @return
     */
    @Override
    protected int getMaxPointsForField(String headline) {
        switch (headline) {
            case "background":
                return 5;
            case "disciplins":
                return 4;
            case "virtues":
                return 7;
            default:
                return 0;
        }
    }

    /**
     * Check whether this uses weightings or not.
     *
     * @return
     */
    public boolean isUsingWeightings() {
        return useWeightings;
    }

    /**
     * Set whether this should use weightings or not.
     *
     * @param useWeightings
     */
    public void setUseWeightings(boolean useWeightings) {
        this.useWeightings = useWeightings;
    }

    /**
     * Get the hashmap with all combo boxes that were added to the form.
     *
     * @return
     */
    public HashMap<String, ArrayList<JComboBox>> getComboBoxes() {
        return this.comboBoxes;
    }

    /**
     * Get all combo boxes for the given type.
     *
     * @param type
     *
     * @return
     */
    public ArrayList<JComboBox> getComboBoxes(String type) {
        return this.comboBoxes.get(type);
    }

    /**
     * Get the spinner maximum value.
     *
     * @return
     */
    public int getSpinnerMaximumValue() {
        return this.spinnerMaximumValue;
    }

    /**
     * Set the spinner maximum value.
     *
     * @param spinnerMaximumValue
     */
    public void setSpinnerMaximumValue(int spinnerMaximumValue) {
        this.spinnerMaximumValue = spinnerMaximumValue;
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type
     *
     * @return
     */
    abstract protected ArrayList getValues(String type);

    /**
     * Get an entity of the given type for the given key.
     *
     * @param type
     * @param key
     *
     * @return Returns an object if found, otherwise null.
     */
    abstract protected Object getEntity(String type, String key);
}
