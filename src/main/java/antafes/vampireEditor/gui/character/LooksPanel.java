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

import antafes.vampireEditor.gui.BasePanel;
import antafes.vampireEditor.gui.TranslatableComponent;
import lombok.Setter;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Marian Pollzien
 */
public class LooksPanel extends BasePanel implements TranslatableComponent, CharacterPanelInterface {
    @Setter
    private antafes.vampireEditor.entity.Character character = null;

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addLooksFields();
        this.fillCharacterData();
        // Add an empty column for nicer styling.
        this.addFields("empty", false, new HashMap<>());

        super.init();
    }

    /**
     * Add all looks fields.
     */
    protected void addLooksFields() {
        this.addFields(new HashMap<>(), "looks", false);
    }

    /**
     * Add fields by the given list and under the given headline.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of elements
     */
    @Override
    protected void addFields(HashMap<String, String> elementList, String headline, boolean addHeadline) {
        LinkedHashMap<String, JComponent> fieldNames = new LinkedHashMap<>();

        fieldNames.put("age", this.generateTextField("age", false));
        fieldNames.put("apparentAge", this.generateTextField("apparentAge", false));
        fieldNames.put("dayOfBirth", this.generateDateFormattedTextField("dayOfBirth", false));
        fieldNames.put("dayOfDeath", this.generateDateFormattedTextField("dayOfDeath", false));
        fieldNames.put("hairColor", this.generateTextField("hairColor", false));
        fieldNames.put("eyeColor", this.generateTextField("eyeColor", false));
        fieldNames.put("skinColor", this.generateTextField("skinColor", false));
        fieldNames.put("nationality", this.generateTextField("nationality", false));
        fieldNames.put("height", this.generateTextField("height", false));
        fieldNames.put("weight", this.generateTextField("weight", false));
        fieldNames.put("sex", this.generateTextField("sex", false));

        this.addFields(headline, addHeadline, fieldNames);
    }

    /**
     * Generate a JTextField with name and editable set.
     *
     * @param name Name of the text field
     * @param editable Whether it should be editable or not
     *
     * @return The generated text field
     */
    private JTextField generateTextField(String name, boolean editable) {
        JTextField textField = new JTextField();
        textField.setName(name);
        textField.setEditable(editable);
        textField.setSize(167, GroupLayout.DEFAULT_SIZE);

        return textField;
    }

    /**
     * Generate a JFormattedTextField with name and editable set.
     *
     * @param name Name of the date formatted text field
     * @param editable Whether it should be editable or not
     *
     * @return The generated formatted text field
     */
    private JFormattedTextField generateDateFormattedTextField(String name, boolean editable) {
        JFormattedTextField dateField = new JFormattedTextField();
        dateField.setFormatterFactory(
            new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM))
            )
        );
        dateField.setEditable(editable);
        dateField.setName(name);
        dateField.setSize(167, GroupLayout.DEFAULT_SIZE);

        return dateField;
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.character == null) {
            return;
        }

        this.getFields("looks").stream().map((field) -> (JTextField) field).forEachOrdered((element) -> {
            if (null != element.getName()) switch (element.getName()) {
                case "age":
                    if (this.character.getAge() > 0) {
                        element.setText(Integer.toString(this.character.getAge()));
                    }

                    break;
                case "looksLikeAge":
                    if (this.character.getApparentAge() > 0) {
                        element.setText(Integer.toString(this.character.getApparentAge()));
                    }

                    break;
                case "dayOfBirth":
                    if (this.character.getDayOfBirth() != null) {
                        ((JFormattedTextField) element).setValue(this.character.getDayOfBirth());
                    }

                    break;
                case "dayOfDeath":
                    if (this.character.getDayOfDeath() != null) {
                        ((JFormattedTextField) element).setValue(this.character.getDayOfDeath());
                    }

                    break;
                case "hairColor":
                    element.setText(this.character.getHairColor());
                    break;
                case "eyeColor":
                    element.setText(this.character.getEyeColor());
                    break;
                case "skinColor":
                    element.setText(this.character.getSkinColor());
                    break;
                case "nationality":
                    element.setText(this.character.getNationality());
                    break;
                case "size":
                    if (this.character.getHeight() > 0) {
                        element.setText(Integer.toString(this.character.getHeight()));
                    }

                    break;
                case "weight":
                    if (this.character.getWeight() > 0) {
                        element.setText(Integer.toString(this.character.getWeight()));
                    }

                    break;
                case "sex":
                    element.setText(this.character.getSex().toString());
                    break;
                default:
                    break;
            }
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
