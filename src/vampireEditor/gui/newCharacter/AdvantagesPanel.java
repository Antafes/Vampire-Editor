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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import vampireEditor.VampireEditor;
import vampireEditor.entity.EntityException;
import vampireEditor.entity.character.Advantage;
import vampireEditor.entity.character.AdvantageInterface;
import vampireEditor.entity.character.Clan;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.NewCharacterDialog;
import vampireEditor.gui.Weighting;
import vampireEditor.utility.StringComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseEditableListPanel {

    /**
     * Create a new advantages panel.
     *
     * @param parent
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
        this.addDisciplinFields();
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
     * Add all disciplin fields sorted by the translated name.
     */
    private void addDisciplinFields() {
        this.addFields("disciplins", AdvantageInterface.AdvantageType.DISCIPLIN.name());
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        ArrayList<Advantage> advantages = this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name());
        ArrayList<String> list = new ArrayList<>();

        advantages.forEach((advantage) -> {
            list.add(advantage.getKey());
        });

        this.addFields(
            "virtues", AdvantageInterface.AdvantageType.VIRTUE.name(), list, 3
        );
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    @Override
    protected ComponentChangeListener createChangeListener() {
        return new ComponentChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).contains(this.getComponent())) {
                    calculateUsedBackgroundPoints();
                }

                if (getFields(AdvantageInterface.AdvantageType.DISCIPLIN.name()).contains(this.getComponent())) {
                    calculateUsedDisciplinPoints();
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
     * @return
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
     * Calculate the used disciplin points.
     */
    private void calculateUsedDisciplinPoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.DISCIPLIN.name());
    }

    /**
     * Calculate and return the sum of points spent for disciplins.
     *
     * @return
     */
    public int getDisciplinPointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLIN.name());
    }

    /**
     * Check if the spent points for disciplins is above its maximum.
     *
     * @return
     */
    public boolean checkDisciplinPoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.DISCIPLIN.name());
    }

    /**
     * Get the maximum points available for disciplins.
     *
     * @return
     */
    public int getDisciplinMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLIN.name());
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
     * @return
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
        this.getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields(AdvantageInterface.AdvantageType.DISCIPLIN.name()).stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields(AdvantageInterface.AdvantageType.VIRTUE.name()).stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });

        this.calculateUsedVirtuePoints();
        this.calculateUsedDisciplinPoints();
        this.calculateUsedBackgroundPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int backgroundSum = this.getBackgroundPointsSum();
        int backgroundMax = this.getBackgroundMaxPoints();
        int disciplinsSum = this.getDisciplinPointsSum();
        int disciplinsMax = this.getDisciplinMaxPoints();
        int virtuesSum = this.getVirtuePointsSum();
        int virtuesMax = this.getVirtueMaxPoints();

        if (backgroundSum >= backgroundMax
            && disciplinsSum >= disciplinsMax
            && virtuesSum >= virtuesMax
            && this.getParentComponent().getMaxActiveTab() < 4
        ) {
            this.getParentComponent().increaseMaxActiveTab();
            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the propery weighting values set.
     * This isn't used for this kind of panel.
     *
     * @param weighting
     *
     * @return
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return 0;
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type
     *
     * @return
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
     * @param type
     * @param key
     *
     * @return Will return an advantage object if found, otherwise null.
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
     * Set the disciplins of the given clan.
     *
     * @param clan
     */
    public void setDisciplins(Clan clan) {
        ArrayList<JComboBox> comboBoxes = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLIN.name());

        if (comboBoxes.size() > 1) {
            for (int i = 0; i < comboBoxes.size() - 1; i++) {
                comboBoxes.remove(0);
            }
        }

        clan.getDisciplins().forEach((disciplin) -> {
            ArrayList<JComboBox> comboBoxList = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLIN.name());
            JComboBox comboBox = comboBoxList.get(comboBoxList.size() - 1);
            comboBox.setSelectedItem(disciplin);
        });
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     *
     * @param type
     *
     * @return
     */
    @Override
    protected int getMaxPointsForField(String type) {
        if (type.equals(AdvantageInterface.AdvantageType.BACKGROUND.name())) {
            return 5;
        } else if (type.equals(AdvantageInterface.AdvantageType.DISCIPLIN.name())) {
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

        if (this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLIN.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLIN.name())) {
            return true;
        }

        return this.getPointsSum(AdvantageInterface.AdvantageType.VIRTUE.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Get a list with all field values.
     *
     * @param builder
     */
    @Override
    public void fillCharacter(vampireEditor.entity.Character.Builder builder) {
        this.getFields().forEach((key, fields) -> {
            for (int i = 0; i < fields.size(); i++) {
                JSpinner spinner = (JSpinner) fields.get(i);
                JComboBox combobox = this.getComboBoxes(key).get(i);

                if (combobox.getSelectedItem().equals("")) {
                    continue;
                }

                try {
                    builder.addAdvantage(
                        new Advantage.Builder()
                            .fillDataFromObject((Advantage) combobox.getSelectedItem())
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
