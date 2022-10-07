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
import antafes.vampireEditor.entity.character.Attribute;
import antafes.vampireEditor.entity.character.AttributeInterface;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Marian Pollzien
 */
public class AttributesPanel extends BaseCharacterListPanel implements TranslatableComponent, CharacterPanelInterface {
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields(AttributeInterface.AttributeType.PHYSICAL.toString()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AttributeInterface.AttributeType.SOCIAL.toString()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AttributeInterface.AttributeType.MENTAL.toString()).stream().map((component) -> (JSpinner) component)
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
        this.getFields().forEach((type, attributeList) -> attributeList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                    this.getCharacter().getAttributes().values().stream()
                        .filter((attribute) -> (attribute.getKey().equals(spinner.getName())))
                        .forEachOrdered((attribute) -> spinner.setValue(attribute.getValue()));
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
        for (AttributeInterface.AttributeType attributeType : AttributeInterface.AttributeType.values()) {
            //noinspection CodeBlock2Expr
            this.getCharacter().getAttributesByType(attributeType)
                .forEach(attribute -> {
                    this.getFields(attributeType.toString()).stream().map(component -> (JSpinner) component).forEachOrdered(component -> {
                        if (!Objects.equals(component.getName(), attribute.getKey())) {
                            return;
                        }

                        Attribute.AttributeBuilder<?, ?> attributeBuilder = attribute.toBuilder();
                        attributeBuilder.setValue((int) component.getValue());
                        characterBuilder.addAttribute(attributeBuilder.build());
                    });
                });
        }
    }

    @Override
    protected void init() {
        this.addPhysicalFields();
        this.addSocialFields();
        this.addMentalFields();
        this.setSpinnerMaximum(this.getCharacter().getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
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
     * Add all talent fields sorted by the translated name.
     */
    private void addPhysicalFields() {
        this.addAttributeFields(
            AttributeInterface.AttributeType.PHYSICAL.toString(),
            AttributeInterface.AttributeType.PHYSICAL
        );
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSocialFields() {
        this.addAttributeFields(
            AttributeInterface.AttributeType.SOCIAL.toString(),
            AttributeInterface.AttributeType.SOCIAL
        );
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addMentalFields() {
        this.addAttributeFields(
            AttributeInterface.AttributeType.MENTAL.toString(),
            AttributeInterface.AttributeType.MENTAL
        );
    }

    private void addAttributeFields(String fieldName, AttributeInterface.AttributeType type) {
        HashMap<String, String> list = new HashMap<>();

        this.getCharacter().getAttributes().values().stream()
            .filter((attribute) -> (attribute.getType().equals(type)))
            .forEachOrdered((attribute) -> list.put(attribute.getKey(), attribute.getName()));
        list.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(new StringComparator()));

        this.addFields(fieldName, list);
    }
}
