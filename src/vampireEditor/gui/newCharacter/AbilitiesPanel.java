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
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import vampireEditor.Configuration;
import vampireEditor.character.Ability;
import vampireEditor.character.AbilityInterface;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.NewCharacterDialog;
import vampireEditor.gui.Weighting;
import vampireEditor.utility.TranslatedComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AbilitiesPanel extends BaseListPanel {

    public AbilitiesPanel(NewCharacterDialog parent, Configuration configuration) {
        super(parent, configuration);
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
     * Add all talent fields sorted by the translated name.
     */
    private void addTalentFields() {
        ArrayList<String> talents = AbilityInterface.AbilityType.TALENT.getAbilities();
        talents.sort(new TranslatedComparator());
        this.addFields("talents", talents);
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSkillFields() {
        ArrayList<String> skills = AbilityInterface.AbilityType.SKILL.getAbilities();
        skills.sort(new TranslatedComparator());
        this.addFields("skills", skills);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addKnowledgeFields() {
        ArrayList<String> knowledges = AbilityInterface.AbilityType.KNOWLEDGE.getAbilities();
        knowledges.sort(new TranslatedComparator());
        this.addFields("knowledges", knowledges);
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
                if (getFields("talents").contains(this.getComponent())) {
                    calculateUsedTalentPoints();
                }

                if (getFields("skills").contains(this.getComponent())) {
                    calculateUsedSkillPoints();
                }

                if (getFields("knowledges").contains(this.getComponent())) {
                    calculateUsedKnowledgePoints();
                }

                checkFieldsFilled();
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
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
     * @return
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
     * @return
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
        this.calculateUsedPoints("knowledges");
    }

    /**
     * Calculate and return the sum of points spent for knowledges.
     *
     * @return
     */
    public int getKnowledgePointsSum() {
        return this.getPointsSum("knowledges");
    }

    /**
     * Check if the spent points for knowledges is above its maximum.
     *
     * @return
     */
    public boolean checkKnowledgePoints() {
        return this.checkPoints("knowledges");
    }

    /**
     * Get the maximum points available for knowledges.
     *
     * @return
     */
    public int getKnowledgeMaxPoints() {
        return this.getMaxPoints("knowledges");
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("talents").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("skills").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("knowledges").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });

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
            && this.getParentComponent().getMaxActiveTab() < 3
        ) {
            this.getParentComponent().increaseMaxActiveTab();
            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the propery weighting values set.
     *
     * @param weighting
     *
     * @return
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
        if (this.getPointsSum("knowledges") < this.getMaxPoints("knowledges")) {
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
     * @param character
     */
    @Override
    public void fillCharacter(vampireEditor.Character character) {
        this.getFields("talents").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            character.getAbilities().add(
                new Ability(spinner.getName(), AbilityInterface.AbilityType.TALENT, (int) spinner.getValue())
            );
        });
        this.getFields("skills").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            character.getAbilities().add(
                new Ability(spinner.getName(), AbilityInterface.AbilityType.SKILL, (int) spinner.getValue())
            );
        });
        this.getFields("knowledges").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            character.getAbilities().add(
                new Ability(spinner.getName(), AbilityInterface.AbilityType.KNOWLEDGE, (int) spinner.getValue())
            );
        });
    }
}
