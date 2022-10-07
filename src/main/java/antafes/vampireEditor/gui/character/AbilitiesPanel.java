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
package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.entity.character.AbilityInterface;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.TranslatableComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marian Pollzien
 */
public class AbilitiesPanel extends BaseCharacterListPanel implements TranslatableComponent, CharacterPanelInterface {
    @Override
    protected void init() {
        this.addTalentFields();
        this.addSkillFields();
        this.addKnowledgeFields();
        this.setSpinnerMaximum(this.getCharacter().getGeneration().getMaximumAttributes());
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
        this.addAbilityFields("knowledge", AbilityInterface.AbilityType.KNOWLEDGE);
    }

    /**
     * Add ability fields with the given fieldName and for the given ability type.
     *
     * @param fieldName Name of the field
     * @param type Ability type
     */
    private void addAbilityFields(String fieldName, AbilityInterface.AbilityType type) {
        HashMap<String, String> list = new HashMap<>();

        this.getCharacter().getAbilities().values().stream()
            .filter((ability) -> (ability.getType().equals(type)))
            .forEachOrdered((ability) -> list.put(ability.getKey(), ability.getName()));
        list.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(String::compareTo));

        this.addFields(fieldName, list);
    }

    @Override
    protected void addChangeListener(JSpinner field)
    {
        super.addChangeListener(field);
        this.addChangeListenerForCharacterChanged(field);
    }

    @Override
    protected ComponentChangeListener createChangeListener() {
        return new ComponentChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        };
    }

    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("talents").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("skills").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("knowledge").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.getCharacter() == null) {
            return;
        }

        //noinspection CodeBlock2Expr
        this.getFields().forEach((type, abilitiesList) -> abilitiesList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                this.getCharacter().getAbilities().values().stream()
                    .filter((ability) -> (ability.getKey().equals(spinner.getName())))
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
