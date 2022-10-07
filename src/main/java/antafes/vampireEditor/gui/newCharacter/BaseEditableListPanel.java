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

import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.storage.EmptyEntityStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.utility.Weighting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Marian Pollzien
 */
abstract public class BaseEditableListPanel extends BaseListPanel {
    public static final int UNLIMITEDMAXFIELDS = -1;

    private boolean useWeightings = true;
    private HashMap<String, ArrayList<JComboBox<BaseTranslatedEntity>>> comboBoxes;
    private int spinnerMaximumValue = 10;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param parent Parent element
     */
    public BaseEditableListPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * This method is called to initialize the form.
     */
    @Override
    protected void initComponents() {
        super.initComponents();

        this.spinnerMaximumValue = 10;
        this.comboBoxes = new HashMap<>();
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param spinnerMinimum Maximum value for the spinners
     */
    @Override
    protected void addFields(String headline, HashMap<String, String> elementList, int spinnerMinimum) {
        this.addFields(headline, null, elementList, spinnerMinimum, BaseEditableListPanel.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param spinnerMinimum Maximum value for the spinners
     * @param maxFields Maximum number of fields to create
     */
    protected void addFields(String headline, HashMap<String, String> elementList, int spinnerMinimum, int maxFields) {
        this.addFields(headline, null, elementList, spinnerMinimum, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     */
    protected void addFields(String headline, String type, HashMap<String, String> elementList) {
        this.addFields(headline, type, elementList, 0, BaseEditableListPanel.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param maxFields Maximum number of fields to create
     */
    protected void addFields(String headline, String type, HashMap<String, String> elementList, int maxFields) {
        this.addFields(headline, type, elementList, 0, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     */
    protected void addFields(String headline, String type) {
        this.addFields(headline, type, new HashMap<>(), 0, BaseEditableListPanel.UNLIMITEDMAXFIELDS);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     * @param maxFields Maximum number of fields to create
     */
    protected void addFields(String headline, String type, int maxFields) {
        this.addFields(headline, type, new HashMap<>(), 0, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param spinnerMinimum Maximum value for the spinners
     * @param maxFields Maximum number of fields to create
     */
    protected void addFields(
        String headline,
        String type,
        HashMap<String, String> elementList,
        int spinnerMinimum,
        int maxFields
    ) {
        this.addFields(headline, type, elementList, false, spinnerMinimum, maxFields);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param type Identifier for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param nonEditable Whether the fields should be editable or not
     * @param spinnerMinimum Maximum value for the spinners
     * @param maxFields Maximum number of fields to create
     */
    protected void addFields(
        String headline,
        String type,
        HashMap<String, String> elementList,
        boolean nonEditable,
        int spinnerMinimum,
        int maxFields
    ) {
        if (!this.getFields().containsKey(type)) {
            this.getFields().put(type, new ArrayList<>());
        }

        if (!this.getPointFields().containsKey(type)) {
            this.getPointFields().put(type, new HashMap<>());
        }

        if (!this.getComboBoxes().containsKey(headline)) {
            this.getComboBoxes().put(type, new ArrayList<>());
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
            JComboBox<Weighting> weightingElement = this.addWeighting(headline);
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

        elementList.forEach((key, element) -> {
            if (nonEditable) {
                super.addRow(element, spinnerMinimum, this.getFields(type), groups, layout);
            } else {
                HashMap<String, Component> newElements = this.addRow(
                    element, this.getEntity(type, element), type, spinnerMinimum, this.getFields(type), groups, layout
                );
                this.getComboBoxes().get(type).add((JComboBox<BaseTranslatedEntity>) newElements.get("comboBox"));
            }
        });

        if (maxFields == BaseEditableListPanel.UNLIMITEDMAXFIELDS || this.getFields(type).size() < maxFields) {
            HashMap<String, Component> newElements = this.addRow(
                null, type, spinnerMinimum, this.getFields(type), groups, layout
            );
            this.addComboBoxItemListener(
                newElements,
                type,
                spinnerMinimum,
                this.getFields(type),
                groups,
                layout,
                maxFields
            );
            this.getComboBoxes().get(type).add((JComboBox<BaseTranslatedEntity>) newElements.get("comboBox"));
        }

        this.addPointFields(type, groups, layout);
        outerLabelHorizontalGroup.addGroup(labelHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
        outerElementHorizontalGroup.addGroup(elementHorizontalGroup);
        listHorizontalGroup.addGroup(outerElementHorizontalGroup);
        outerLabelHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The name of the element to add
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     *
     * @return Map with the label and the element
     */
    @Override
    protected HashMap<String, Component> addRow(String element, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, null, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The name of the element to add
     * @param selected The selected index
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     *
     * @return Map with the label and the element
     */
    protected HashMap<String, Component> addRow(String element, Object selected, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, selected, null, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The name of the element to add
     * @param type Identifier for the fields
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     *
     * @return Map with the label and the element
     */
    protected HashMap<String, Component> addRow(String element, String type, int spinnerMinimum, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout) {
        return this.addRow(element, null, type, spinnerMinimum, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element The name of the element to add
     * @param selected The selected index
     * @param type Identifier for the field
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     *
     * @return Map with the label and the element
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
        JComboBox<BaseTranslatedEntity> elementComboBox = new JComboBox<>();
        DefaultComboBoxModel<BaseTranslatedEntity> model = new DefaultComboBoxModel<>();

        if (element == null) {
            EmptyEntity empty = ((EmptyEntityStorage) StorageFactory.getStorage(StorageFactory.StorageType.EMPTY)).getEntity();
            model.addElement(empty);
        }

        this.getValues(type).forEach(anObject -> model.addElement((BaseTranslatedEntity) anObject));
        elementComboBox.setModel(model);

        if (selected != null) {
            elementComboBox.setSelectedIndex(this.getValues(type).indexOf(selected));
        }

        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, this.getSpinnerMaximumValue(), 1));
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
     * Add an item listener for the combobox.
     *
     * @param elements Map with the combobox and the spinner
     * @param type Identifier for the field
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     * @param maxFields Maximum amount of fields
     */
    protected void addComboBoxItemListener(
        HashMap<String, Component> elements,
        String type,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout,
        int maxFields
    ) {
        ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox<BaseTranslatedEntity> element = (JComboBox<BaseTranslatedEntity>) e.getSource();
                BaseEditableListPanel panel = (BaseEditableListPanel) element.getParent();

                if (e.getStateChange() != ItemEvent.SELECTED
                    || element.getSelectedItem() == null
                    || element.getSelectedItem().equals("")
                ) {
                    return;
                }

                HashMap<String, Component> newElements = panel.addRow(
                    null, type, spinnerMinimum, fields, groups, layout
                );
                panel.getComboBoxes().get(type).add((JComboBox<BaseTranslatedEntity>) newElements.get("comboBox"));

                if (maxFields == BaseEditableListPanel.UNLIMITEDMAXFIELDS || fields.size() < maxFields) {
                    panel.addComboBoxItemListener(newElements, type, spinnerMinimum, fields, groups, layout, maxFields);
                }

                element.removeItemListener(this);

                // The below method calls are needed to show the newly added components
                panel.revalidate();
                panel.repaint();
            }
        };
        ((JComboBox<BaseTranslatedEntity>) elements.get("comboBox")).addItemListener(listener);
    }

    /**
     * Check whether this uses weightings or not.
     */
    public boolean isUsingWeightings() {
        return useWeightings;
    }

    /**
     * Set whether this should use weightings or not.
     */
    public void setUseWeightings(boolean useWeightings) {
        this.useWeightings = useWeightings;
    }

    /**
     * Get the hashmap with all combo boxes that were added to the form.
     */
    public HashMap<String, ArrayList<JComboBox<BaseTranslatedEntity>>> getComboBoxes() {
        return this.comboBoxes;
    }

    /**
     * Get all combo boxes for the given type.
     *
     * @param type Identifier for the group
     *
     * @return List of comboboxes
     */
    public ArrayList<JComboBox<BaseTranslatedEntity>> getComboBoxes(String type) {
        return this.comboBoxes.get(type);
    }

    /**
     * Get the spinner maximum value.
     */
    public int getSpinnerMaximumValue() {
        return this.spinnerMaximumValue;
    }

    /**
     * Set the spinner maximum value.
     */
    public void setSpinnerMaximumValue(int spinnerMaximumValue) {
        this.spinnerMaximumValue = spinnerMaximumValue;
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type Identifier for the group of comboboxes
     *
     * @return List of values
     */
    abstract protected ArrayList<?> getValues(String type);

    /**
     * Get an entity of the given type for the given key.
     *
     * @param type Identifier for the group of comboboxes
     * @param key Key for the object to get
     *
     * @return Returns an object if found, otherwise null.
     */
    abstract protected Object getEntity(String type, String key);
}
