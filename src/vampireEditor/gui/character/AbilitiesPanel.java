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
package vampireEditor.gui.character;

import vampireEditor.entity.Character;
import vampireEditor.entity.character.AbilityInterface;
import vampireEditor.gui.BaseListPanel;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.utility.StringComparator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

/**
 *
 * @author Marian Pollzien
 */
public class AbilitiesPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface {
    private vampireEditor.entity.Character character = null;

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
        this.addAbilityFields("knowledges", AbilityInterface.AbilityType.KNOWLEDGE);
    }

    /**
     * Add ability fields with the given fieldName and for the given ability type.
     *
     * @param fieldName Name of the field
     * @param type Ability type
     */
    private void addAbilityFields(String fieldName, AbilityInterface.AbilityType type) {
        ArrayList<String> list = new ArrayList<>();

        this.character.getAbilities().stream()
            .filter((ability) -> (ability.getType().equals(type)))
            .forEachOrdered((ability) -> list.add(ability.getName()));
        list.sort(new StringComparator());

        this.addFields(fieldName, list);
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
        this.getFields("talents").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("skills").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("knowledges").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
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

        this.getFields().forEach((type, abilitiesList) -> abilitiesList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                this.character.getAbilities().stream()
                    .filter((ability) -> (ability.getName().equals(spinner.getName())))
                    .forEachOrdered((ability) -> spinner.setValue(ability.getValue()));
            }
        ));
    }

    /**
     * Reload the language object and update all texts.
     */
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
