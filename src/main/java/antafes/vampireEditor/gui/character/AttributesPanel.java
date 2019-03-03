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
import antafes.vampireEditor.entity.character.AttributeInterface;
import antafes.vampireEditor.gui.BaseListPanel;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

/**
 *
 * @author Marian Pollzien
 */
public class AttributesPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface {
    private antafes.vampireEditor.entity.Character character = null;

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addPhysicalFields();
        this.addSocialFields();
        this.addMentalFields();
        this.setSpinnerMaximum(this.character.getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
    }

    /**
     * Add all talent fields sorted by the translated name.
     */
    private void addPhysicalFields() {
        this.addAttributeFields("physical", AttributeInterface.AttributeType.PHYSICAL);
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSocialFields() {
        this.addAttributeFields("social", AttributeInterface.AttributeType.SOCIAL);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addMentalFields() {
        this.addAttributeFields("mental", AttributeInterface.AttributeType.MENTAL);
    }

    /**
     * Add attribute fields with the given fieldName and for the given attribute type.
     *
     * @param fieldName Name of the field
     * @param type Attribute type to use
     */
    private void addAttributeFields(String fieldName, AttributeInterface.AttributeType type) {
        ArrayList<String> list = new ArrayList<>();

        this.character.getAttributes().stream()
            .filter((attribute) -> (attribute.getType().equals(type)))
            .forEachOrdered((attribute) -> list.add(attribute.getName()));
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
        this.getFields("physical").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("social").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("mental").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
    }

    /**
     * Set the character used to pre-fill every field.
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

        this.getFields().forEach((type, attributeList) -> attributeList.stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> {
                this.character.getAttributes().stream()
                    .filter((attribute) -> (attribute.getName().equals(spinner.getName())))
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
}
