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

import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.NewCharacterDialog;
import vampireEditor.gui.Weighting;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Marian Pollzien
 */
abstract public class BaseListPanel extends BasePanel {

    private HashMap<String, HashMap<String, JTextField>> pointFields;
    private HashMap<String, JComboBox> weightings;
    private int weightingCounter = 0;

    public BaseListPanel(NewCharacterDialog parent) {
        super(parent);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        this.pointFields = new HashMap<>();
        this.weightings = new HashMap<>();
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
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline Headline for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     */
    @Override
    protected void addFields(String headline, ArrayList<String> elementList) {
        this.addFields(headline, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param addHeadline Whether to add the group headline
     * @param elementList List of element names that should be added as JSpinner
     */
    @Override
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList) {
        this.addFields(headline, addHeadline, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param elementList List of element names that should be added as JSpinner
     * @param spinnerMinimum Maximum value for the spinners
     */
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum) {
        this.addFields(headline, true, elementList, spinnerMinimum);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline Headline for the group of fields
     * @param addHeadline Whether to add the group headline
     * @param elementList List of element names that should be added as JSpinner
     * @param spinnerMinimum Maximum value for the spinners
     */
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList, int spinnerMinimum) {
        if (!this.getFields().containsKey(headline)) {
            this.getFields().put(headline, new ArrayList<>());
        }

        if (!this.pointFields.containsKey(headline)) {
            this.pointFields.put(headline, new HashMap<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.getLanguage().translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JComboBox weightingElement = this.addWeighting(headline);

        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup()
            .addGap(11, 11, 11);
        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11);

        if (addHeadline) {
            listHorizontalGroup
                .addComponent(
                    groupLabel,
                    GroupLayout.Alignment.LEADING,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE
                );
            listVerticalGroup
                .addComponent(groupLabel)
                .addGap(6, 6, 6);
        }
        listHorizontalGroup
            .addGroup(layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
                .addComponent(weightingElement, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
            );
        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup().addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

        listVerticalGroup
            .addComponent(weightingElement, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(11, 11, 11);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        outerLabelHorizontalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup outerElementHorizontalGroup = layout.createSequentialGroup();
        outerElementHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        GroupLayout.ParallelGroup labelHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup elementHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        elementList.forEach((element) -> {
            HashMap<String, GroupLayout.Group> groups = new HashMap<>();
            groups.put("labelHorizontalGroup", labelHorizontalGroup);
            groups.put("elementHorizontalGroup", elementHorizontalGroup);
            groups.put("listVerticalGroup", listVerticalGroup);
            this.addRow(element, spinnerMinimum, this.getFields(headline), groups, layout);
        });

        HashMap<String, GroupLayout.Group> groups = new HashMap<>();
        groups.put("listVerticalGroup", listVerticalGroup);
        groups.put("listHorizontalGroup", listHorizontalGroup);
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
     * @param element The name of the element to add
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     *
     * @return Map with the label and the element
     */
    protected HashMap<String, Component> addRow(
        String element,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        return this.addRow(element, 0, fields, groups, layout);
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
    protected HashMap<String, Component> addRow(
        String element,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        JLabel elementLabel = new JLabel(this.getElementLabelText(element));
        JSpinner spinner = new JSpinner();
        elementLabel.setLabelFor(spinner);
        spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, 10, 1));
        Dimension spinnerDimension = new Dimension(36, 20);
        spinner.setPreferredSize(spinnerDimension);
        spinner.setMinimumSize(spinnerDimension);
        spinner.setMaximumSize(spinnerDimension);
        spinner.setName(element);
        this.addChangeListener(spinner);
        this.getOrder().add(spinner);
        fields.add(spinner);
        groups.get("labelHorizontalGroup").addComponent(elementLabel);
        groups.get("elementHorizontalGroup").addComponent(spinner);
        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        verticalGroup.addComponent(elementLabel)
            .addComponent(spinner);
        groups.get("listVerticalGroup").addGroup(verticalGroup)
            .addGap(6, 6, 6);

        HashMap<String, Component> elements = new HashMap<>();
        elements.put("label", elementLabel);
        elements.put("spinner", spinner);

        return elements;
    }

    /**
     * This will translate the element name.
     *
     * @param element The element name to translate
     *
     * @return The translated name
     */
    protected String getElementLabelText(String element) {
        return this.getLanguage().translate(element);
    }

    /**
     * Add a weighting field.
     *
     * @param headline Headline for which to use the weighting
     *
     * @return The weighting combo box
     */
    protected JComboBox addWeighting(String headline) {
        JComboBox weightingElement = new JComboBox();
        weightingElement.setModel(new DefaultComboBoxModel(Weighting.values()));
        weightingElement.setSelectedIndex(0);

        if (!this.weightings.isEmpty()) {
            if (this.weightings.size() == 1) {
                weightingElement.setSelectedIndex(1);
            } else {
                weightingElement.setSelectedIndex(2);
            }
        }

        weightingElement.addActionListener((ActionEvent e) -> {
            JComboBox element = (JComboBox) e.getSource();
            JComboBox second, third;
            ArrayList<JComboBox> elements = new ArrayList<>(this.weightings.values());
            elements.remove(element);
            second = elements.get(0);
            third = elements.get(1);
            this.switchWeightings(
                element,
                second,
                third
            );
            Weighting weighting = (Weighting) element.getSelectedItem();
            this.getMaxPointsFields(headline).setText(Integer.toString(this.getWeightingMax(weighting)));
            this.getParentComponent().calculateUsedFreeAdditionalPoints();
        });
        this.getOrder().add(this.weightingCounter, weightingElement);
        this.weightingCounter++;
        this.weightings.put(headline, weightingElement);

        return weightingElement;
    }

    /**
     * Add the point fields for the column.
     *
     * @param type Identifier for the field
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     */
    protected void addPointFields(
        String type, HashMap<String, GroupLayout.Group> groups, GroupLayout layout
    ) {
        Dimension pointsDimension = new Dimension(36, 20);
        JTextField pointsField = new JTextField("0");
        pointsField.setEnabled(false);
        pointsField.setPreferredSize(pointsDimension);
        pointsField.setMinimumSize(pointsDimension);
        pointsField.setMaximumSize(pointsDimension);
        JTextField maxPointsField = this.getMaxPointsField(type);
        maxPointsField.setEnabled(false);
        maxPointsField.setPreferredSize(pointsDimension);
        maxPointsField.setMinimumSize(pointsDimension);
        maxPointsField.setMaximumSize(pointsDimension);
        this.pointFields.get(type).put("points", pointsField);
        this.pointFields.get(type).put("maxPoints", maxPointsField);
        GroupLayout.SequentialGroup listVerticalGroup = (GroupLayout.SequentialGroup) groups.get("listVerticalGroup");

        if (groups.containsKey("listOuterVerticalGroup")) {
            listVerticalGroup = (GroupLayout.SequentialGroup) groups.get("listOuterVerticalGroup");
        }

        listVerticalGroup.addGap(14, 14, 14)
            .addGroup(layout.createParallelGroup()
                .addComponent(pointsField)
                .addComponent(maxPointsField)
            )
            .addGap(10, 10, 10);
        groups.get("listHorizontalGroup").addGroup(
            layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
                .addComponent(pointsField)
                .addComponent(maxPointsField)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
        );
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     *
     * @param type Identifier for the field
     *
     * @return Maximum points value
     */
    protected int getMaxPointsForField(String type) {
        return this.getWeightingMax((Weighting) this.weightings.get(type).getSelectedItem());
    }

    /**
     * Switch the selection for the combo box with the same value to the
     * remaining value.
     * The first combo box will be treated as the one the change was made on.
     *
     * @param first The first weighting combo box
     * @param second The second weighting combo box
     * @param third The third weighting combo box
     */
    protected void switchWeightings(JComboBox first, JComboBox second, JComboBox third) {
        Weighting firstSelection = (Weighting) first.getSelectedItem();
        Weighting secondSelection = (Weighting) second.getSelectedItem();
        Weighting thirdSelection = (Weighting) third.getSelectedItem();

        if (firstSelection == null) {
            return;
        }

        if (firstSelection.equals(secondSelection)) {
            second.setSelectedItem(Weighting.getRemaining(firstSelection, thirdSelection));
        } else if (firstSelection.equals(thirdSelection)) {
            third.setSelectedItem(Weighting.getRemaining(firstSelection, secondSelection));
        }
    }

    /**
     * Add a change listener to the given spinner.
     *
     * @param field The field to add a change listener to
     */
    protected void addChangeListener(JSpinner field) {
        ComponentChangeListener attributesListener = this.createChangeListener();
        attributesListener.setComponent(field);
        DefaultFormatter attributesFormatter = (DefaultFormatter) ((JSpinner.DefaultEditor) field.getEditor()).getTextField().getFormatter();
        attributesFormatter.setCommitsOnValidEdit(true);
        field.addChangeListener(attributesListener);
    }

    /**
     * Get the points field for the given type.
     *
     * @param type Identifier for the field to get
     *
     * @return Fetched text field
     */
    protected JTextField getPointsField(String type) {
        return this.pointFields.get(type).get("points");
    }

    /**
     * Get the maximum points field for the given type.
     *
     * @param type Identifier for the field to get
     *
     * @return Fetched text field
     */
    protected JTextField getMaxPointsFields(String type) {
        return this.pointFields.get(type).get("maxPoints");
    }

    /**
     * Calculate the used talent points for the given type.
     *
     * @param type Identifier
     */
    protected void calculateUsedPoints(String type) {
        int sum = this.getPointsSum(type);

        if (this.checkPoints(type)) {
            this.getPointsField(type).setText(
                Integer.toString(this.getMaxPoints(type))
            );
        } else {
            this.getPointsField(type).setText(Integer.toString(sum));
        }
    }

    /**
     * Calculate and return the sum of points spent for the given type.
     *
     * @param type Identifier
     *
     * @return Sum of points
     */
    public int getPointsSum(String type) {
        int sum = 0;

        sum = this.getFields(type).stream().map((component) -> (JSpinner) component)
            .map((spinner) -> Integer.parseInt(spinner.getValue().toString()) - Integer.parseInt(((SpinnerNumberModel) spinner.getModel()).getMinimum().toString()))
            .reduce(sum, Integer::sum);

        return sum;
    }

    /**
     * Check if the spent points for talents is above its maximum.
     *
     * @param type Identifier
     *
     * @return True if spent points are above maximum
     */
    public boolean checkPoints(String type) {
        return this.getPointsSum(type) > this.getMaxPoints(type);
    }

    /**
     * Get the maximum points available for talents.
     *
     * @param type Identifier
     *
     * @return Maximum points value
     */
    public int getMaxPoints(String type) {
        return Integer.parseInt(this.getMaxPointsFields(type).getText());
    }

    /**
     * Set the spinner field maximum value.
     *
     * @param field The field to set the value for
     * @param maximum The maximum value that could be set
     */
    protected void setFieldMaximum(JSpinner field, int maximum) {
        int value = Integer.parseInt(field.getValue().toString());
        int minimum = Integer.parseInt(
            ((SpinnerNumberModel) field.getModel()).getMinimum().toString()
        );
        field.setModel(
            new SpinnerNumberModel(
                value > maximum ? maximum : value,
                minimum,
                maximum,
                1
            )
        );
    }

    /**
     * Get the point fields map.
     *
     * @return Map with the label and textfield
     */
    protected HashMap<String, JTextField> getPointFields(String type) {
        return this.pointFields.get(type);
    }

    /**
     * Get the point fields hashmap.
     *
     * @return
     */
    protected HashMap<String, HashMap<String, JTextField>> getPointFields() {
        return pointFields;
    }

    /**
     * Get the weightings list.
     *
     * @return
     */
    protected HashMap<String, JComboBox> getWeightings() {
        return weightings;
    }

    /**
     * Get the weighting counter.
     *
     * @return
     */
    protected int getWeightingCounter() {
        return weightingCounter;
    }

    /**
     * Set the weighting counter.
     *
     * @param weightingCounter
     */
    protected void setWeightingCounter(int weightingCounter) {
        this.weightingCounter = weightingCounter;
    }

    /**
     * Get the max points field with the properly weighting values set.
     *
     * @param type Identifier
     *
     * @return Text field for the max points
     */
    protected JTextField getMaxPointsField(String type) {
        return new JTextField(Integer.toString(this.getMaxPointsForField(type)));
    }

    /**
     * Create a change listener.
     *
     * @return Change listener for the component
     */
    abstract protected ComponentChangeListener createChangeListener();

    /**
     * Get the proper weighting value.
     *
     * @param weighting Enum to get the weighting value from
     *
     * @return The maximum weighting
     */
    abstract protected int getWeightingMax(Weighting weighting);

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    abstract public void setSpinnerMaximum(int maximum);
}
