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
import java.util.Vector;
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
import vampireEditor.language.LanguageInterface;

/**
 *
 * @author Marian Pollzien
 */
abstract class BaseListPanel extends javax.swing.JPanel {

    private final Configuration configuration;
    private final LanguageInterface language;
    private final NewCharacterDialog parent;
    private GroupLayout.ParallelGroup outerParallelHorizontalGroup;
    private GroupLayout.ParallelGroup outerParallelVerticalGroup;
    private GroupLayout.SequentialGroup outerSequentialHorizontalGroup;
    private GroupLayout.SequentialGroup outerSequentialVerticalGroup;
    private final Vector<Component> order;
    private final HashMap<String, ArrayList<Component>> fields;
    private final HashMap<String, HashMap<String, JTextField>> pointFields;
    private final ArrayList<JComboBox> weightings;
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;
    private int weightingCounter = 0;

    /**
     * Creates new form AbilitiesPanel
     *
     * @param parent
     * @param configuration
     */
    public BaseListPanel(NewCharacterDialog parent, Configuration configuration) {
        super();
        this.parent = parent;
        this.configuration = configuration;
        this.language = this.configuration.getLanguageObject();
        this.order = new Vector<>();
        this.fields = new HashMap<>();
        this.pointFields = new HashMap<>();
        this.weightings = new ArrayList<>();

        this.initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        backButton.setText("Back");
        backButton.addActionListener((ActionEvent evt) -> {
            backButtonActionPerformed(evt);
        });

        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener((ActionEvent evt) -> {
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
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(nextButton)
                        .addComponent(backButton)
                    )
                    .addContainerGap()
                )
            )
        );

        this.init();
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
        this.setFieldTexts();
        this.createFocusTraversalPolicy();
    }

    /**
     * Set the translated texts for the fields and labels on the skills tab.
     */
    private void setFieldTexts() {
        this.nextButton.setText(this.language.translate("next"));
        this.backButton.setText(this.language.translate("back"));
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline
     * @param elementList
     */
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
        if (!this.fields.containsKey(headline)) {
            this.fields.put(headline, new ArrayList<>());
        }

        if (!this.pointFields.containsKey(headline)) {
            this.pointFields.put(headline, new HashMap<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.language.translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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
            ArrayList<JComboBox> elements = new ArrayList<>(this.weightings);
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
            this.parent.calculateUsedFreeAdditionalPoints();
        });
        this.order.add(this.weightingCounter, weightingElement);
        this.weightingCounter++;
        this.weightings.add(weightingElement);
        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
                .addComponent(weightingElement, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
            );
        this.outerSequentialHorizontalGroup
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
        this.outerParallelVerticalGroup
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        outerLabelHorizontalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup outerElementHorizontalGroup = layout.createSequentialGroup();
        outerElementHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        GroupLayout.ParallelGroup labelHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup elementHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup verticalGroup;

        for (String talent : elementList) {
            JLabel elementLabel = new JLabel(this.language.translate(talent));
            JSpinner spinner = new JSpinner();
            spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, 10, 1));
            Dimension spinnerDimension = new Dimension(36, 20);
            spinner.setPreferredSize(spinnerDimension);
            spinner.setMinimumSize(spinnerDimension);
            spinner.setMaximumSize(spinnerDimension);
            spinner.setName(talent);
            this.addChangeListener(spinner);
            this.order.add(spinner);
            this.fields.get(headline).add(spinner);
            labelHorizontalGroup.addComponent(elementLabel);
            elementHorizontalGroup.addComponent(spinner);
            verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            verticalGroup.addComponent(elementLabel)
                .addComponent(spinner);
            listVerticalGroup.addGroup(verticalGroup)
                .addGap(6, 6, 6);
        }

        Dimension pointsDimension = new Dimension(36, 20);
        JTextField pointsField = new JTextField("0");
        pointsField.setEnabled(false);
        pointsField.setPreferredSize(pointsDimension);
        pointsField.setMinimumSize(pointsDimension);
        pointsField.setMaximumSize(pointsDimension);
        JTextField maxPointsField = this.getMaxPointsField(((Weighting) weightingElement.getSelectedItem()));
        maxPointsField.setEnabled(false);
        maxPointsField.setPreferredSize(pointsDimension);
        maxPointsField.setMinimumSize(pointsDimension);
        maxPointsField.setMaximumSize(pointsDimension);
        this.pointFields.get(headline).put("points", pointsField);
        this.pointFields.get(headline).put("maxPoints", maxPointsField);
        listVerticalGroup.addGap(14, 14, 14)
            .addGroup(layout.createParallelGroup()
                .addComponent(pointsField)
                .addComponent(maxPointsField)
            )
            .addGap(10, 10, 10);
        listHorizontalGroup.addGroup(
            layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
                .addComponent(pointsField)
                .addComponent(maxPointsField)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, 100)
        );

        outerLabelHorizontalGroup.addGroup(labelHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
        outerElementHorizontalGroup.addGroup(elementHorizontalGroup);
        listHorizontalGroup.addGroup(outerElementHorizontalGroup);
        outerLabelHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE);
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
     * Switch the selection for the combo box with the same value to the
     * remaining value.
     * The first combo box will be treated as the one the change was made on.
     *
     * @param first
     * @param second
     * @param third
     */
    private void switchWeightings(JComboBox first, JComboBox second, JComboBox third) {
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
    private void addChangeListener(JSpinner field) {
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
    private JTextField getPointsField(String type) {
        return (JTextField) this.pointFields.get(type).get("points");
    }

    /**
     * Get the maximum points field for the given type.
     *
     * @param type
     *
     * @return
     */
    private JTextField getMaxPointsFields(String type) {
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

        sum = this.fields.get(type).stream().map((component) -> (JSpinner) component)
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
        return this.getPointsSum(type)> this.getMaxPoints(type);
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
     * Enable the next button.
     */
    protected void enableNextButton() {
        this.nextButton.setEnabled(true);
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
     * Get the max points field with the propery weighting values set.
     *
     * @param weighting
     *
     * @return
     */
    protected JTextField getMaxPointsField(Weighting weighting) {
        return new JTextField(Integer.toString(this.getWeightingMax(weighting)));
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    abstract protected ComponentChangeListener createChangeListener();

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    abstract public void setSpinnerMaximum(int maximum);

    /**
     * Check if every attribute has been set.
     */
    abstract protected void checkFieldsFilled();

    /**
     * Get the proper weighting value.
     *
     * @param weighting
     *
     * @return
     */
    abstract protected int getWeightingMax(Weighting weighting);
}
