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
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.utility.StringComparator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseEditableListPanel {

    /**
     * Create a new advantages panel.
     *
     * @param parent Parent element
     */
    public AdvantagesPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.setUseWeightings(false);
        this.addBackgroundFields();
        this.addDisciplineFields();
        this.addVirtueFields();

        super.init();
    }

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields() {
        this.addFields("background", AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Add all discipline fields sorted by the translated name.
     */
    private void addDisciplineFields() {
        this.addFields("disciplines", AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        ArrayList<Advantage> advantages = this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name());
        ArrayList<String> list = new ArrayList<>();

        advantages.forEach((advantage) -> list.add(advantage.getKey()));

        this.addFields(
            "virtues", AdvantageInterface.AdvantageType.VIRTUE.name(), list, 3
        );
    }

    /**
     * Create the attributes document listener.
     *
     * @return Change listener for the component
     */
    @Override
    protected ComponentChangeListener createChangeListener() {
        return new ComponentChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).contains(this.getComponent())) {
                    calculateUsedBackgroundPoints();
                }

                if (getFields(AdvantageInterface.AdvantageType.DISCIPLINE.name()).contains(this.getComponent())) {
                    calculateUsedDisciplinePoints();
                }

                if (getFields(AdvantageInterface.AdvantageType.VIRTUE.name()).contains(this.getComponent())) {
                    calculateUsedVirtuePoints();
                }

                checkFieldsFilled();
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
    }

    /**
     * Calculate the used background points.
     */
    private void calculateUsedBackgroundPoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Calculate and return the sum of points spent for backgrounds.
     *
     * @return
     */
    public int getBackgroundPointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Check if the spent points for backgrounds is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkBackgroundPoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Get the maximum points available for talents.
     *
     * @return
     */
    public int getBackgroundMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Calculate the used discipline points.
     */
    private void calculateUsedDisciplinePoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Calculate and return the sum of points spent for disciplines.
     *
     * @return
     */
    public int getDisciplinePointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Check if the spent points for disciplines is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkDisciplinePoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Get the maximum points available for disciplines.
     *
     * @return
     */
    public int getDisciplineMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Calculate the used virtue points.
     */
    private void calculateUsedVirtuePoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Calculate and return the sum of points spent for virtues.
     *
     * @return
     */
    public int getVirtuePointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Check if the spent points for virtues is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkVirtuePoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Get the maximum points available for virtues.
     *
     * @return
     */
    public int getVirtueMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.setSpinnerMaximumValue(maximum);
        this.getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.DISCIPLINE.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.VIRTUE.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));

        this.calculateUsedVirtuePoints();
        this.calculateUsedDisciplinePoints();
        this.calculateUsedBackgroundPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int backgroundSum = this.getBackgroundPointsSum();
        int backgroundMax = this.getBackgroundMaxPoints();
        int disciplinesSum = this.getDisciplinePointsSum();
        int disciplinesMax = this.getDisciplineMaxPoints();
        int virtuesSum = this.getVirtuePointsSum();
        int virtuesMax = this.getVirtueMaxPoints();

        if (backgroundSum >= backgroundMax
            && disciplinesSum >= disciplinesMax
            && virtuesSum >= virtuesMax
            && this.getParentComponent().getMaxActiveTab() < 4
        ) {
            this.getParentComponent().increaseMaxActiveTab();
            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the properly weighting values set.
     * This isn't used for this kind of panel.
     *
     * @param weighting Enum to get the weighting value from
     *
     * @return Returns always 0 as it isn't used here
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return 0;
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type Identifier for the group of combo boxes
     *
     * @return List of values
     */
    @Override
    protected ArrayList<Advantage> getValues(String type) {
        ArrayList<Advantage> list = new ArrayList<>();
        VampireEditor.getAdvantages().forEach((String key, Advantage advantage) -> {
            if (type != null) {
                if (AdvantageInterface.AdvantageType.valueOf(type.toUpperCase()).equals(advantage.getType())) {
                    list.add(advantage);
                }
            } else {
                list.add(advantage);
            }
        });
        list.sort(new StringComparator());

        return list;
    }

    /**
     * Get an entity for the given type.
     *
     * @param type Identifier for the group of combo boxes
     * @param key Key for the object to get
     *
     * @return Returns an advantage object if found, otherwise null.
     */
    @Override
    protected Advantage getEntity(String type, String key) {
        for (Advantage advantage : this.getValues(type)) {
            if (advantage.getKey().equals(key)) {
                return advantage;
            }
        }

        return null;
    }

    /**
     * Set the disciplines of the given clan.
     *
     * @param clan
     */
    public void setDisciplines(Clan clan) {
        ArrayList<JComboBox> comboBoxes = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLINE.name());

        if (comboBoxes.size() > 1) {
            comboBoxes.subList(0, comboBoxes.size() - 1).clear();
        }

        clan.getDisciplines().forEach((discipline) -> {
            ArrayList<JComboBox> comboBoxList = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLINE.name());
            JComboBox comboBox = comboBoxList.get(comboBoxList.size() - 1);
            comboBox.setSelectedItem(discipline);
        });
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     *
     * @param type Identifier for the field
     *
     * @return Maximum points value
     */
    @Override
    protected int getMaxPointsForField(String type) {
        if (type.equals(AdvantageInterface.AdvantageType.BACKGROUND.name())) {
            return 5;
        } else if (type.equals(AdvantageInterface.AdvantageType.DISCIPLINE.name())) {
            return 4;
        } else if (type.equals(AdvantageInterface.AdvantageType.VIRTUE.name())) {
            return 7;
        }

        return 0;
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    @Override
    public boolean checkAllFields() {
        if (this.getPointsSum(AdvantageInterface.AdvantageType.BACKGROUND.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.BACKGROUND.name())) {
            return true;
        }

        if (this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLINE.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name())) {
            return true;
        }

        return this.getPointsSum(AdvantageInterface.AdvantageType.VIRTUE.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder object
     */
    @Override
    public void fillCharacter(antafes.vampireEditor.entity.Character.Builder builder) {
        this.getFields().forEach((key, fields) -> {
            for (int i = 0; i < fields.size(); i++) {
                JSpinner spinner = (JSpinner) fields.get(i);
                JComboBox comboBox = this.getComboBoxes(key).get(i);

                if (Objects.equals(comboBox.getSelectedItem(), "")) {
                    continue;
                }

                try {
                    builder.addAdvantage(
                        new Advantage.Builder()
                            .fillDataFromObject((Advantage) comboBox.getSelectedItem())
                            .setValue((int) spinner.getValue())
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(AdvantagesPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
