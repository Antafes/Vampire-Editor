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

import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.gui.BaseListPanel;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.StringComparator;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface {
    @Setter
    private Character character = null;

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.setTranslateFieldLabels(false);
        this.addBackgroundFields();
        this.addDisciplineFields();
        this.addVirtueFields();
        this.setSpinnerMaximum(this.character.getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
    }

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields() {
        this.addAdvantageFields("background", AdvantageInterface.AdvantageType.BACKGROUND);
    }

    /**
     * Add all discipline fields sorted by the translated name.
     */
    private void addDisciplineFields() {
        this.addAdvantageFields("disciplines", AdvantageInterface.AdvantageType.DISCIPLINE);
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        this.addAdvantageFields("virtues", AdvantageInterface.AdvantageType.VIRTUE);
    }

    /**
     * Add advantage fields with the given fieldName and for the given advantage type.
     *
     * @param fieldName Name of the field
     * @param type Advantage type to use
     */
    private void addAdvantageFields(String fieldName, AdvantageInterface.AdvantageType type) {
        ArrayList<String> list = new ArrayList<>();

        this.character.getAdvantages().stream()
            .filter((advantage) -> (advantage.getType().equals(type)))
            .forEachOrdered((advantage) -> list.add(advantage.getName()));
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
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("background").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("disciplines").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("virtues").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.character == null) {
            return;
        }

        this.getFields().forEach((type, advantagesList) -> advantagesList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                this.character.getAdvantages().stream()
                    .filter((advantage) -> (advantage.getName().equals(spinner.getName())))
                    .forEachOrdered((advantage) -> spinner.setValue(advantage.getValue()));
            }
        ));
    }

    /**
     * Reload the language object and update the texts of every component in the component.
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
