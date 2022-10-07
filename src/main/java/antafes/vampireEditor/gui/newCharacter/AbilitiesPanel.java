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
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Ability;
import antafes.vampireEditor.entity.character.AbilityInterface;
import antafes.vampireEditor.entity.storage.AbilityStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class AbilitiesPanel extends BaseListPanel {
    /**
     * The maximum of points per ability that can be issued with ability points instead of additional points.
     */
    private static final int MAX_CREATION_POINTS = 3;

    /**
     * The amount of points above the maximum defined in this.MAX_CREATION_POINTS
     */
    private int amountAboveMaximum = 0;

    /**
     * Constructor
     *
     * @param parent Parent element
     */
    public AbilitiesPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addTalentFields();
        this.addSkillFields();
        this.addKnowledgeFields();

        super.init();
    }

    /**
     * Return the translated name of the element.
     *
     * @param element The element to get the name from
     *
     * @return The translated name
     */
    @Override
    protected String getElementLabelText(String element) {
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);

        try {
            return storage.getEntity(element).getName();
        } catch (EntityStorageException e) {
            VampireEditor.log(e.getMessage());
        }

        return element;
    }

    /**
     * Add all talent fields sorted by the translated name.
     */
    private void addTalentFields() {
        this.addAbilityFields("talents", AbilityInterface.AbilityType.TALENT);
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSkillFields() {
        this.addAbilityFields("skills", AbilityInterface.AbilityType.SKILL);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addKnowledgeFields() {
        this.addAbilityFields("knowledge", AbilityInterface.AbilityType.KNOWLEDGE);
    }

    /**
     * Add ability fields with the given fieldName and for the given ability type.
     *
     * @param fieldName Name of the field
     * @param type Ability type to use
     */
    private void addAbilityFields(String fieldName, AbilityInterface.AbilityType type) {
        HashMap<String, String> list = new HashMap<>();

        this.getValues(type.name()).stream()
            .filter((ability) -> (ability.getType().equals(type)))
            .forEachOrdered((ability) -> list.put(ability.getKey(), ability.getKey()));
        list.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue());

        this.addFields(fieldName, list);
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type String representation of the ability type
     *
     * @return List of abilities
     */
    protected ArrayList<Ability> getValues(String type) {
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);
        ArrayList<Ability> list = storage.getEntityListByType(AbilityInterface.AbilityType.valueOf(type.toUpperCase()));
        list.sort(new StringComparator());

        return list;
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
                if (getFields("talents").contains(this.getComponent())) {
                    calculateUsedTalentPoints();
                }

                if (getFields("skills").contains(this.getComponent())) {
                    calculateUsedSkillPoints();
                }

                if (getFields("knowledge").contains(this.getComponent())) {
                    calculateUsedKnowledgePoints();
                }

                checkFieldsFilled();
                // Resetting this right before the calculation of free additional points to get the correct value.
                amountAboveMaximum = 0;
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
    }

    /**
     * Calculate and return the sum of points spent for the given type.
     * This will also check for values above the maximum points during creation.
     *
     * @param type Identifier
     *
     * @return Sum of points
     */
    @Override
    public int getPointsSum(String type) {
        int sum = 0;

        for (Component component: this.getFields(type)) {
            JSpinner spinner = (JSpinner) component;
            int value = Integer.parseInt(spinner.getValue().toString());

            if (value > MAX_CREATION_POINTS) {
                this.amountAboveMaximum += value - MAX_CREATION_POINTS;
                value = MAX_CREATION_POINTS;
            }

            sum += value;
        }

        return sum;
    }

    /**
     * The amount of points above the maximum defined in this.MAX_CREATION_POINTS
     *
     * @return
     */
    public int getAmountAboveMaximum() {
        return this.amountAboveMaximum;
    }

    /**
     * Calculate the used talent points.
     */
    private void calculateUsedTalentPoints() {
        this.calculateUsedPoints("talents");
    }

    /**
     * Calculate and return the sum of points spent for talents.
     *
     * @return
     */
    public int getTalentPointsSum() {
        return this.getPointsSum("talents");
    }

    /**
     * Check if the spent points for talents is above its maximum.
     *
     * @return True if above maximum
     */
    public boolean checkTalentPoints() {
        return this.checkPoints("talents");
    }

    /**
     * Get the maximum points available for talents.
     *
     * @return
     */
    public int getTalentMaxPoints() {
        return this.getMaxPoints("talents");
    }

    /**
     * Calculate the used skill points.
     */
    private void calculateUsedSkillPoints() {
        this.calculateUsedPoints("skills");
    }

    /**
     * Calculate and return the sum of points spent for skills.
     *
     * @return
     */
    public int getSkillPointsSum() {
        return this.getPointsSum("skills");
    }

    /**
     * Check if the spent points for skills is above its maximum.
     *
     * @return True if above maximum
     */
    public boolean checkSkillPoints() {
        return this.checkPoints("skills");
    }

    /**
     * Get the maximum points available for skills.
     *
     * @return
     */
    public int getSkillMaxPoints() {
        return this.getMaxPoints("skills");
    }

    /**
     * Calculate the used knowledge points.
     */
    private void calculateUsedKnowledgePoints() {
        this.calculateUsedPoints("knowledge");
    }

    /**
     * Calculate and return the sum of points spent for knowledge.
     *
     * @return
     */
    public int getKnowledgePointsSum() {
        return this.getPointsSum("knowledge");
    }

    /**
     * Check if the spent points for knowledge is above its maximum.
     *
     * @return True if above maximum
     */
    public boolean checkKnowledgePoints() {
        return this.checkPoints("knowledge");
    }

    /**
     * Get the maximum points available for knowledge.
     *
     * @return
     */
    public int getKnowledgeMaxPoints() {
        return this.getMaxPoints("knowledge");
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("talents").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("skills").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("knowledge").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));

        this.calculateUsedKnowledgePoints();
        this.calculateUsedSkillPoints();
        this.calculateUsedTalentPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int talentsSum = this.getTalentPointsSum();
        int talentsMax = this.getTalentMaxPoints();
        int skillsSum = this.getSkillPointsSum();
        int skillsMax = this.getSkillMaxPoints();
        int knowledgeSum = this.getKnowledgePointsSum();
        int knowledgeMax = this.getKnowledgeMaxPoints();

        if (talentsSum >= talentsMax
            && skillsSum >= skillsMax
            && knowledgeSum >= knowledgeMax
        ) {
            if (this.getParentComponent().getMaxActiveTab() < 3) {
                this.getParentComponent().increaseMaxActiveTab();
            }

            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the properly weighting values set.
     *
     * @param weighting Weighting enum
     *
     * @return Maximum value for abilities
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return weighting.getAbilitiesMax();
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    @Override
    public boolean checkAllFields() {
        if (this.getPointsSum("knowledge") < this.getMaxPoints("knowledge")) {
            return true;
        }

        if (this.getPointsSum("skills") < this.getMaxPoints("skills")) {
            return true;
        }

        return this.getPointsSum("talents") < this.getMaxPoints("talents");
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder
     */
    @Override
    public void fillCharacter(Character.CharacterBuilder<?, ?> builder) {
        AbilityStorage storage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);
        this.getFields().forEach((key, fields) -> {
            for (Component field : fields) {
                JSpinner spinner = (JSpinner) field;

                try {
                    Ability ability = storage.getEntity(spinner.getName());
                    builder.addAbility(
                        ability.toBuilder()
                            .setValue((int) spinner.getValue())
                            .build()
                    );
                } catch (EntityStorageException ex) {
                    Logger.getLogger(AbilitiesPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
