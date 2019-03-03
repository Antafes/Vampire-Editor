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

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.Flaw;
import antafes.vampireEditor.entity.character.Merit;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.character.SpecialFeature;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.element.WideComboBox;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 *
 * @author Marian Pollzien
 */
public class LastStepsPanel extends BasePanel {
    private JLabel flawInfoLabel;
    private JComboBox roadComboBox;

    /**
     * Create the last steps panel.
     *
     * @param parent Parent element
     */
    public LastStepsPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addMeritAndFlawFields();
        this.addRoadFields();
        this.adjustNextButton();

        super.init();
    }

    /**
     * Add all merit and flaw fields sorted by the translated name.
     */
    private void addMeritAndFlawFields() {
        String headlineMerits = "merits", headlineFlaws = "flaws";
        GroupLayout layout = (GroupLayout) this.getLayout();
        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup();
        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup();
        HashMap<String, GroupLayout.Group> groups = new HashMap<>();
        groups.put("listHorizontalGroup", listHorizontalGroup);
        groups.put("listVerticalGroup", listVerticalGroup);
        this.addSpecialFeatureFields(headlineMerits, "merit", groups);
        this.addSpecialFeatureFields(headlineFlaws, "flaw", groups);
        this.flawInfoLabel = new JLabel();
        this.flawInfoLabel.setText("<html>" + this.getLanguage().translate("flawInfoTooMany") + "</html>");
        this.flawInfoLabel.setVisible(false);
        listHorizontalGroup.addComponent(this.flawInfoLabel, GroupLayout.PREFERRED_SIZE, 150, 300);
        listVerticalGroup.addComponent(this.flawInfoLabel);
    }

    /**
     * Add all road fields sorted by the translated name.
     */
    private void addRoadFields() {
        this.addFields("road");
    }

    /**
     * Add fields by the given list and under the given headline.
     * This is going to be used to add the road and humanity fields.
     *
     * @param headline The headline of the element group
     */
    protected void addFields(String headline) {
        this.addFields(headline, new ArrayList<>());
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of elements
     */
    @Override
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList) {
        if (!this.getFields().containsKey(headline)) {
            this.getFields().put(headline, new ArrayList<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.getLanguage().translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup()
            .addComponent(
                groupLabel,
                GroupLayout.Alignment.LEADING,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE
            );
        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel);

        listVerticalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup listOuterVerticalGroup = layout.createSequentialGroup();
        listVerticalGroup.addGroup(listOuterVerticalGroup);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup comboBoxHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        this.roadComboBox = new JComboBox();
        DefaultComboBoxModel roadModel = new DefaultComboBoxModel();
        roadModel.addElement("");
        this.roadComboBox.setModel(roadModel);
        this.getRoadValues().forEach(roadModel::addElement);
        this.roadComboBox.addItemListener((ItemEvent e) -> {
            if (Objects.equals(this.roadComboBox.getSelectedItem(), "")) {
                this.disableNextButton();
            } else {
                this.enableNextButton();
            }
        });
        this.roadComboBox.setMaximumRowCount(this.roadComboBox.getModel().getSize() < 20 ? this.roadComboBox.getModel().getSize() : 20);

        comboBoxHorizontalGroup.addComponent(this.roadComboBox, GroupLayout.PREFERRED_SIZE, 150, 300);
        listOuterVerticalGroup
            .addComponent(this.roadComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);

        outerLabelHorizontalGroup.addGroup(comboBoxHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
    }

    /**
     * Get the values for the road combo box.
     *
     * @return
     */
    protected ArrayList<Road> getRoadValues() {
        ArrayList<Road> list = new ArrayList<>();
        VampireEditor.getRoads().forEach((String key, Road road) -> list.add(road));
        list.sort(new StringComparator());

        return list;
    }

    /**
     * Add the fields for the special features.
     *
     * @param headline The headline of the element group
     * @param type Identifier for the group of fields
     * @param groups Groups the element should be added to
     */
    private void addSpecialFeatureFields(String headline, String type, HashMap<String, GroupLayout.Group> groups) {
        HashMap<String, GroupLayout.Group> innerGroups = new HashMap<>(groups);

        if (!this.getFields().containsKey(type)) {
            this.getFields().put(type, new ArrayList<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.getLanguage().translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        GroupLayout.ParallelGroup listHorizontalGroup = ((GroupLayout.ParallelGroup) groups.get("listHorizontalGroup"));
        listHorizontalGroup
            .addComponent(
                groupLabel,
                GroupLayout.Alignment.LEADING,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE,
                Short.MAX_VALUE
            );

        GroupLayout.SequentialGroup listVerticalGroup = ((GroupLayout.SequentialGroup) groups.get("listVerticalGroup"))
            .addGap(11, 11, 11)
            .addComponent(groupLabel);

        listVerticalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup listOuterVerticalGroup = layout.createSequentialGroup();
        listVerticalGroup.addGroup(listOuterVerticalGroup);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup comboBoxHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        innerGroups.put("comboBoxHorizontalGroup", comboBoxHorizontalGroup);
        innerGroups.put("listOuterVerticalGroup", listOuterVerticalGroup);

        HashMap<String, Component> newElements = this.addRow(
            type, this.getFields(type), innerGroups
        );
        ((JComboBox) newElements.get("comboBox")).addItemListener(
            this.getComboBoxItemListener(type, this.getFields(type), innerGroups)
        );

        outerLabelHorizontalGroup.addGroup(comboBoxHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
    }

    /**
     * Add a single row to the current column.
     *
     * @param type Identifier for the field
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     *
     * @return Map with the label and component
     */
    protected HashMap<String, Component> addRow(
        String type,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups
    ) {
        WideComboBox elementComboBox = new WideComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");
        this.getSpecialFeatureValues(type).forEach(model::addElement);
        elementComboBox.setModel(model);
        groups.get("comboBoxHorizontalGroup").addComponent(elementComboBox, GroupLayout.PREFERRED_SIZE, 150, 300);
        groups.get("listOuterVerticalGroup")
            .addComponent(elementComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(6, 6, 6);

        HashMap<String, Component> elements = new HashMap<>();
        fields.add(elementComboBox);
        elements.put("comboBox", elementComboBox);

        return elements;
    }

    /**
     * Get a list of special features.
     *
     * @param type Identifier for the special features to get
     *
     * @return List of special features
     */
    private ArrayList<SpecialFeature> getSpecialFeatureValues(String type) {

        ArrayList<SpecialFeature> list = new ArrayList<>(
            ("merit".equals(type) ? VampireEditor.getMerits() : VampireEditor.getFlaws()).values()
        );
        list.sort(new StringComparator());

        return list;
    }

    /**
     * Get the combo box item listener for adding new fields.
     *
     * @param type Identifier for the field
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     *
     * @return Item listener for the combo box
     */
    private ItemListener getComboBoxItemListener(
        String type, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups
    ) {
        return (ItemEvent e) -> {
            JComboBox element = (JComboBox) e.getSource();

            if (element.getSelectedItem() == null || element.getSelectedItem().equals("")) {
                return;
            }

            HashMap<String, Component> newElements = this.addRow(
                type, fields, groups
            );

            ((JComboBox) newElements.get("comboBox")).addItemListener(
                this.getComboBoxItemListener(type, fields, groups)
            );

            element.removeItemListener(element.getItemListeners()[0]);
            this.calculateFreeAdditionalPoints();
            element.addItemListener((ItemEvent e1) -> this.calculateFreeAdditionalPoints());

            // The below method calls are needed to show the newly added components
            this.revalidate();
            this.repaint();
        };
    }

    /**
     * Adjust the next button to act as the finish character creation button.
     */
    private void adjustNextButton() {
        JButton nextButton = this.getNextButton();
        nextButton.setText(this.getConfiguration().getLanguageObject().translate("finish"));

        for (ActionListener actionListener : nextButton.getActionListeners()) {
            nextButton.removeActionListener(actionListener);
        }

        nextButton.addActionListener((ActionEvent e) -> {
            VampireEditor.log(new ArrayList<>(
                Collections.singletonList(
                    "clicked finish"
                )
            ));
            this.getParentComponent().finishCharacter();
        });
    }

    /**
     * Check if every attribute has been set.
     * Nothing to do here, as everything that has to be filled is already filled.
     */
    @Override
    protected void checkFieldsFilled() {
    }

    /**
     * Calculate the free additional points.
     */
    private void calculateFreeAdditionalPoints() {
        int sum = 0;

        sum = this.getFields("flaw").stream().map((field) -> (JComboBox) field)
            .filter((comboBox) -> (!Objects.equals(comboBox.getSelectedItem(), "")))
            .map((comboBox) -> ((Flaw) comboBox.getSelectedItem()).getCost())
            .reduce(sum, Integer::sum);

        if (sum > 7) {
            this.flawInfoLabel.setVisible(true);
            this.getNextButton().setEnabled(false);
        } else {
            this.flawInfoLabel.setVisible(false);
            this.getNextButton().setEnabled(true);
        }

        this.getParentComponent().getFreeAdditionalMaxPointsTextField().setText(Integer.toString(sum + 15));
        this.getParentComponent().calculateUsedFreeAdditionalPoints();

        if (sum <= 7) {
            if (this.getParentComponent().checkFreeAdditionalPoints()) {
                this.getNextButton().setEnabled(false);
            } else {
                this.getNextButton().setEnabled(true);
            }
        }
    }

    /**
     * Get the points used for merits.
     *
     * @return
     */
    public int getMeritPoints() {
        int sum = 0;

        sum = this.getFields("merit").stream().map((field) -> (JComboBox) field)
            .filter((comboBox) -> (!Objects.equals(comboBox.getSelectedItem(), "")))
            .map((comboBox) -> ((Merit) comboBox.getSelectedItem()).getCost())
            .reduce(sum, Integer::sum);

        return sum;
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    @Override
    public boolean checkAllFields() {
        return false;
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder object
     */
    @Override
    public void fillCharacter(antafes.vampireEditor.entity.Character.Builder builder) {
        this.getFields("merit").stream().map((field) -> (JComboBox) field)
            .filter((comboBox) -> !(Objects.equals(comboBox.getSelectedItem(), "")))
            .forEachOrdered((comboBox) -> builder.addMerit((Merit) comboBox.getSelectedItem()));
        this.getFields("flaw").stream().map((field) -> (JComboBox) field)
            .filter((comboBox) -> !(Objects.equals(comboBox.getSelectedItem(), "")))
            .forEachOrdered((comboBox) -> builder.addFlaw((Flaw) comboBox.getSelectedItem()));
        builder.setRoad((Road) this.roadComboBox.getSelectedItem());
    }
}
