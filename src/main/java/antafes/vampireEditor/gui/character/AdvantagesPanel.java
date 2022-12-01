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
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.SortingUtility;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseCharacterListPanel implements TranslatableComponent, CharacterPanelInterface {
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural()).stream().map((component) -> (JSpinner) component)
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
        this.getFields().forEach((type, advantagesList) -> advantagesList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                    this.getCharacter().getAdvantages().values().stream()
                        .filter((advantage) -> (advantage.getKey().equals(spinner.getName())))
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

    @Override
    public void updateCharacter(Character.CharacterBuilder<?, ?> characterBuilder)
    {
        for (AdvantageInterface.AdvantageType advantageType : AdvantageInterface.AdvantageType.values()) {
            //noinspection CodeBlock2Expr
            this.getCharacter().getAdvantagesByType(advantageType)
                .forEach(advantage -> {
                    this.getFields(advantageType.toString()).stream().map(component -> (JSpinner) component).forEachOrdered(component -> {
                        if (!Objects.equals(component.getName(), advantage.getKey())) {
                            return;
                        }

                        Advantage.AdvantageBuilder<?, ?> advantageBuilder = advantage.toBuilder();
                        advantageBuilder.setValue((int) component.getValue());
                        characterBuilder.addAdvantage(advantageBuilder.build());
                    });
                });
        }
    }

    @Override
    protected void init() {
        this.setTranslateFieldLabels(false);
        this.addBackgroundFields();
        this.addDisciplineFields();
        this.addVirtueFields();
        this.setSpinnerMaximum(this.getCharacter().getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
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

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields() {
        this.addAdvantageFields(AdvantageInterface.AdvantageType.BACKGROUND);
    }

    /**
     * Add all discipline fields sorted by the translated name.
     */
    private void addDisciplineFields() {
        this.addAdvantageFields(AdvantageInterface.AdvantageType.DISCIPLINE);
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        this.addAdvantageFields(AdvantageInterface.AdvantageType.VIRTUE);
    }

    /**
     * Add advantage fields with the given fieldName and for the given advantage type.
     *
     * @param type Advantage type to use
     */
    private void addAdvantageFields(AdvantageInterface.AdvantageType type) {
        this.addFields(
            type.getKeyPlural(),
            SortingUtility.sortAndStringifyEntityMapWithFiltering(new HashMap<>(this.getCharacter().getAdvantages()), type)
        );
    }
}
