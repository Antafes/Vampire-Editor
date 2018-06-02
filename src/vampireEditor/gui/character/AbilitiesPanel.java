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
package vampireEditor.gui.character;

import java.util.ArrayList;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import vampireEditor.Character;
import vampireEditor.Configuration;
import vampireEditor.character.AbilityInterface;
import vampireEditor.gui.BaseListPanel;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.utility.TranslatedComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AbilitiesPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface {
    private vampireEditor.Character character = null;

    /**
     * Create a new abilities panel.
     *
     * @param configuration
     */
    public AbilitiesPanel(Configuration configuration) {
        super(configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addTalentFields();
        this.addSkillFields();
        this.addKnowledgeFields();
        this.setSpinnerMaximum(this.character.getGeneration().getMaximumAttributes());
        this.fillCharacterData();

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
            }
        };
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
    }

    /**
     * Set the character used to prefill every field.
     *
     * @param character
     */
    @Override
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.character == null) {
            return;
        }

        this.getFields().forEach((type, abilitiesList) -> {
            abilitiesList.stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
                this.character.getAbilities().stream()
                    .filter((ability) -> (ability.getName().equals(spinner.getName())))
                    .forEachOrdered((ability) -> {
                        spinner.setValue(ability.getValue());
                    });
            });
        });
    }

    @Override
    public void updateTexts() {
        this.getConfiguration().loadProperties();
        this.setLanguage(this.getConfiguration().getLanguageObject());
        this.removeAll();
        this.initComponents();
        this.init();
        this.invalidate();
        this.repaint();
    }
}
