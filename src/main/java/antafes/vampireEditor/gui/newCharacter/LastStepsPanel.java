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

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.character.*;
import antafes.vampireEditor.entity.storage.*;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.element.WideComboBox;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Marian Pollzien
 */
public class LastStepsPanel extends BasePanel {
    private JLabel flawInfoLabel;

    /**
     * Create the last steps panel.
     *
     * @param parent Parent element
     * @param configuration The configuration object
     */
    public LastStepsPanel(NewCharacterDialog parent, Configuration configuration) {
        super(parent, configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addMeritAndFlawFields();
        this.adjustNextButton();

        super.init();
    }

    /**
     * Add all merit and flaw fields sorted by the translated name.
     */
    private void addMeritAndFlawFields() {
        String headlineMerits = "merits", headlineFlaws = "flaws";
        GroupLayout layout = (GroupLayout) this.getLayout();
        GroupLayout.ParallelGroup meritsHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
        GroupLayout.ParallelGroup flawsHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
        GroupLayout.SequentialGroup centeredFieldsHorizontalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addGroup(meritsHorizontalGroup)
            .addGap(18, 18, 18)
            .addGroup(flawsHorizontalGroup)
            .addGap(11, 11, 11);
        this.getOuterSequentialHorizontalGroup()
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(centeredFieldsHorizontalGroup)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        HashMap<String, GroupLayout.Group> meritGroups = new HashMap<>();
        meritGroups.put("listHorizontalGroup", meritsHorizontalGroup);
        meritGroups.put("listVerticalGroup", layout.createSequentialGroup());
        this.addSpecialFeatureFields(headlineMerits, "merit", meritGroups);

        HashMap<String, GroupLayout.Group> flawGroups = new HashMap<>();
        flawGroups.put("listHorizontalGroup", flawsHorizontalGroup);
        flawGroups.put("listVerticalGroup", layout.createSequentialGroup());
        this.addSpecialFeatureFields(headlineFlaws, "flaw", flawGroups);
        this.flawInfoLabel = new JLabel();
        this.flawInfoLabel.setText("<html>" + this.getLanguage().translate("flawInfoTooMany") + "</html>");
        this.flawInfoLabel.setVisible(false);
        flawsHorizontalGroup.addComponent(this.flawInfoLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        ((GroupLayout.SequentialGroup) flawGroups.get("listVerticalGroup")).addComponent(this.flawInfoLabel);
        this.linkSpecialFeatureFieldWidths(layout);
    }

    /**
     * Not used in this panel.
     */
    @Override
    protected void addFields(HashMap<String, String> elementList, String headline, boolean addHeadline) {
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
                0,
                GroupLayout.DEFAULT_SIZE,
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

        GroupLayout.ParallelGroup comboBoxHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
        innerGroups.put("comboBoxHorizontalGroup", comboBoxHorizontalGroup);
        innerGroups.put("listOuterVerticalGroup", listOuterVerticalGroup);

        HashMap<String, Component> newElements = this.addRow(
            type, this.getFields(type), innerGroups
        );
        ((JComboBox<BaseTranslatedEntity>) newElements.get("comboBox")).addItemListener(
            this.getComboBoxItemListener(type, this.getFields(type), innerGroups)
        );

        listHorizontalGroup.addGroup(comboBoxHorizontalGroup);
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
        WideComboBox<BaseTranslatedEntity> elementComboBox = new WideComboBox<>();
        DefaultComboBoxModel<BaseTranslatedEntity> model = new DefaultComboBoxModel<>();
        model.addElement(this.getEmptyEntity());
        this.getSpecialFeatureValues(type).forEach(model::addElement);
        elementComboBox.setModel(model);
        groups.get("comboBoxHorizontalGroup").addComponent(
            elementComboBox,
            0,
            GroupLayout.DEFAULT_SIZE,
            Short.MAX_VALUE
        );
        groups.get("listOuterVerticalGroup")
            .addComponent(elementComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(6, 6, 6);

        HashMap<String, Component> elements = new HashMap<>();
        fields.add(elementComboBox);
        elements.put("comboBox", elementComboBox);
        this.linkSpecialFeatureFieldWidths((GroupLayout) this.getLayout());

        return elements;
    }

    private void linkSpecialFeatureFieldWidths(GroupLayout layout)
    {
        List<Component> components = new ArrayList<>();
        List<Component> meritFields = this.getFields("merit");
        List<Component> flawFields = this.getFields("flaw");

        if (meritFields != null) {
            components.addAll(meritFields);
        }

        if (flawFields != null) {
            components.addAll(flawFields);
        }

        if (this.flawInfoLabel != null) {
            components.add(this.flawInfoLabel);
        }

        if (components.size() > 1) {
            layout.linkSize(SwingConstants.HORIZONTAL, components.toArray(new Component[0]));
        }
    }

    /**
     * Get a list of special features.
     *
     * @param type Identifier for the special features to get
     *
     * @return List of special features
     */
    private ArrayList<SpecialFeature> getSpecialFeatureValues(String type) {
        MeritStorage meritStorage = StorageFactory.getStorage(StorageFactory.StorageType.MERIT);
        FlawStorage flawStorage = StorageFactory.getStorage(StorageFactory.StorageType.FLAW);

        ArrayList<SpecialFeature> list = new ArrayList<>(
            ("merit".equals(type) ? meritStorage.getList() : flawStorage.getList()).values()
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
            JComboBox<BaseTranslatedEntity> element = (JComboBox<BaseTranslatedEntity>) e.getSource();

            if (element.getSelectedItem() == null || element.getSelectedItem().equals("")) {
                return;
            }

            HashMap<String, Component> newElements = this.addRow(
                type, fields, groups
            );

            ((JComboBox<BaseTranslatedEntity>) newElements.get("comboBox")).addItemListener(
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
        nextButton.setEnabled(true);

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

        sum = this.getFields("flaw").stream().map((field) -> (JComboBox<BaseTranslatedEntity>) field)
            .filter((comboBox) -> (!Objects.equals(comboBox.getSelectedItem(), this.getEmptyEntity())))
            .map((comboBox) -> ((Flaw) Objects.requireNonNull(comboBox.getSelectedItem())).getCost())
            .reduce(sum, Integer::sum);

        if (this.getParentComponent().isNpcCreation()) {
            this.flawInfoLabel.setVisible(false);
            this.getParentComponent().getFreeAdditionalMaxPointsTextField().setText(Integer.toString(sum + 15));
            this.getParentComponent().calculateUsedFreeAdditionalPoints();
            this.getNextButton().setEnabled(true);
            return;
        }

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
            this.getNextButton().setEnabled(!this.getParentComponent().checkFreeAdditionalPoints());
        }
    }

    /**
     * Get the points used for merits.
     */
    public int getMeritPoints() {
        int sum = 0;

        sum = this.getFields("merit").stream().map((field) -> (JComboBox<BaseTranslatedEntity>) field)
            .filter((comboBox) -> (!Objects.equals(comboBox.getSelectedItem(), this.getEmptyEntity())))
            .map((comboBox) -> ((Merit) Objects.requireNonNull(comboBox.getSelectedItem())).getCost())
            .reduce(sum, Integer::sum);

        return sum;
    }

    private EmptyEntity getEmptyEntity()
    {
        return ((EmptyEntityStorage) StorageFactory.getStorage(StorageFactory.StorageType.EMPTY)).getEntity();
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
    public void fillCharacter(Character.CharacterBuilder<?, ?> builder) {
        this.getFields("merit").stream().map((field) -> (JComboBox<BaseTranslatedEntity>) field)
            .filter((comboBox) -> !(Objects.equals(comboBox.getSelectedItem(), this.getEmptyEntity())))
            .forEachOrdered((comboBox) -> builder.addMerit((Merit) comboBox.getSelectedItem()));
        this.getFields("flaw").stream().map((field) -> (JComboBox<BaseTranslatedEntity>) field)
            .filter((comboBox) -> !(Objects.equals(comboBox.getSelectedItem(), this.getEmptyEntity())))
            .forEachOrdered((comboBox) -> builder.addFlaw((Flaw) comboBox.getSelectedItem()));
    }
}
