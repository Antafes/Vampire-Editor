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
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import vampireEditor.Configuration;

/**
 *
 * @author Marian Pollzien
 */
abstract public class BaseListPanel extends BasePanel {

    private HashMap<String, HashMap<String, JTextField>> pointFields;
    private HashMap<String, JComboBox> weightings;
    private int weightingCounter = 0;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param parent
     * @param configuration
     */
    public BaseListPanel(NewCharacterDialog parent, Configuration configuration) {
        super(parent, configuration);
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
     * @param headline
     * @param elementList
     */
    @Override
    protected void addFields(String headline, ArrayList<String> elementList) {
        this.addFields(headline, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param elementList
     * @param spinnerMinimum
     */
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum) {
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
            .addGap(11, 11, 11)
            .addComponent(groupLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel)
            .addGap(6, 6, 6)
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
     * @param element
     * @param fields
     * @param groups
     * @param layout
     */
    @Override
    protected HashMap<String, Component> addRow(
        String element, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout
    ) {
        return this.addRow(element, 0, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     */
    protected HashMap<String, Component> addRow(
        String element,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        JLabel elementLabel = new JLabel(this.getLanguage().translate(element));
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
     * Add a weighting field.
     *
     * @param headline
     *
     * @return
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
     * @param type
     * @param groups
     * @param layout
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
     * @param type
     *
     * @return
     */
    protected int getMaxPointsForField(String type) {
        return this.getWeightingMax((Weighting) this.weightings.get(type).getSelectedItem());
    }

    /**
     * Switch the selection for the combo box with the same value to the
     * remaining value.
     * The first combo box will be treated as the one the change was made on.
     *
     * @param first
     * @param second
     * @param third
     */
    protected void switchWeightings(JComboBox first, JComboBox second, JComboBox third) {
        Weighting firstSelection = (Weighting) first.getSelectedItem();
        Weighting secondSelection = (Weighting) second.getSelectedItem();
        Weighting thirdSelection = (Weighting) third.getSelectedItem();

        if (firstSelection.equals(secondSelection)) {
            second.setSelectedItem(Weighting.getRemaining(firstSelection, thirdSelection));
        } else if (firstSelection.equals(thirdSelection)) {
            third.setSelectedItem(Weighting.getRemaining(firstSelection, secondSelection));
        }
    }

    /**
     * Add a change listener to the given spinner.
     *
     * @param field
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
     * @param type
     *
     * @return
     */
    protected JTextField getPointsField(String type) {
        return (JTextField) this.pointFields.get(type).get("points");
    }

    /**
     * Get the maximum points field for the given type.
     *
     * @param type
     *
     * @return
     */
    protected JTextField getMaxPointsFields(String type) {
        return (JTextField) this.pointFields.get(type).get("maxPoints");
    }

    /**
     * Calculate the used talent points for the given type.
     *
     * @param type
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
     * @param type
     *
     * @return
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
     * @param type
     *
     * @return
     */
    public boolean checkPoints(String type) {
        return this.getPointsSum(type) > this.getMaxPoints(type);
    }

    /**
     * Get the maximum points available for talents.
     *
     * @param type
     *
     * @return
     */
    public int getMaxPoints(String type) {
        return Integer.parseInt(this.getMaxPointsFields(type).getText());
    }

    /**
     * Set the spinner field maximum value.
     *
     * @param field
     * @param maximum
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
     * @return
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
     * Get the max points field with the propery weighting values set.
     *
     * @param type
     *
     * @return
     */
    protected JTextField getMaxPointsField(String type) {
        return new JTextField(Integer.toString(this.getMaxPointsForField(type)));
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    abstract protected ComponentChangeListener createChangeListener();

    /**
     * Get the proper weighting value.
     *
     * @param weighting
     *
     * @return
     */
    abstract protected int getWeightingMax(Weighting weighting);

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    abstract public void setSpinnerMaximum(int maximum);
}
