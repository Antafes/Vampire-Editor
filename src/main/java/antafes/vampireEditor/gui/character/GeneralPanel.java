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

import antafes.vampireEditor.entity.character.Flaw;
import antafes.vampireEditor.entity.character.Merit;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Marian Pollzien
 */
public class GeneralPanel extends BaseCharacterPanel implements TranslatableComponent, CharacterPanelInterface {
    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addBaseFields();
        this.addMeritAndFlawFields();
        this.addOtherFields();
        this.fillCharacterData();

        super.init();
    }

    /**
     * Add all base fields.
     */
    private void addBaseFields() {
        LinkedHashMap<String, JComponent> fieldNames = new LinkedHashMap<>();

        fieldNames.put("name", this.generateTextField("name", false));
        fieldNames.put("chronicle", this.generateTextField("chronicle", false));
        fieldNames.put("generation", this.generateTextField("generation", false));
        fieldNames.put("nature", this.generateTextField("nature", false));
        fieldNames.put("hideout", this.generateTextField("hideout"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("hideout"));
        fieldNames.put("player", this.generateTextField("player"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("player"));
        fieldNames.put("demeanor", this.generateTextField("demeanor", false));
        fieldNames.put("concept", this.generateTextField("concept", false));
        fieldNames.put("sire", this.generateTextField("sire"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("sire"));
        fieldNames.put("clan", this.generateTextField("clan", false));
        fieldNames.put("sect", this.generateTextField("sect"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("sect"));

        this.addFields("base", false, fieldNames);
    }

    /**
     * Unused in this panel.
     *
     * @param headline The headline of the element group
     * @param addHeadline Whether to add a headline
     * @param elementList List of elements
     */
    @Override
    protected void addFields(HashMap<String, String> elementList, String headline, boolean addHeadline) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Generate a JTextField with name set.
     * This field will be editable by default.
     *
     * @param name Name of the text field
     *
     * @return The generated text field
     */
    private JTextField generateTextField(String name) {
        return this.generateTextField(name, true);
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
     * Add all merit and flaw fields sorted by the translated name.
     */
    private void addMeritAndFlawFields() {
        LinkedHashMap<String, JComponent> elementList = new LinkedHashMap<>();
        ArrayList<Merit> merits = new ArrayList<>(this.getCharacter().getMerits().values());
        ArrayList<Flaw> flaws = new ArrayList<>(this.getCharacter().getFlaws().values());
        merits.sort(new StringComparator());
        flaws.sort(new StringComparator());

        merits.forEach((merit) -> {
            JLabel label = new JLabel();
            label.setText(merit.toString());
            label.setSize(167, GroupLayout.DEFAULT_SIZE);
            elementList.put(merit.getKey(), label);
        });

        flaws.forEach((flaw) -> {
            JLabel label = new JLabel();
            label.setText(flaw.toString());
            label.setSize(167, GroupLayout.DEFAULT_SIZE);
            elementList.put(flaw.getKey(), label);
        });

        this.addFields("meritsAndFlaws", true, elementList, false);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addOtherFields() {
        LinkedHashMap<String, JComponent> elementList = new LinkedHashMap<>();
        Dimension spinnerDimension = new Dimension(36, 20);

        JSpinner road = new JSpinner();
        road.setModel(new SpinnerNumberModel(0, 0, 10, 1));
        road.setSize(spinnerDimension);
        road.setName("road");
        elementList.put(this.getCharacter().getRoad().getName(), road);
        this.addChangeListenerForCharacterChanged(road);

        JSpinner willpower = new JSpinner();
        willpower.setModel(new SpinnerNumberModel(0, 0, 10, 1));
        willpower.setSize(spinnerDimension);
        willpower.setName("willpower");
        elementList.put("willpower", willpower);
        this.addChangeListenerForCharacterChanged(willpower);

        JSpinner bloodPool = new JSpinner();
        bloodPool.setModel(new SpinnerNumberModel(0, 0, this.getCharacter().getGeneration().getMaximumBloodPool(), 1));
        bloodPool.setSize(spinnerDimension);
        bloodPool.setName("bloodPool");
        elementList.put("bloodPool", bloodPool);
        this.addChangeListenerForCharacterChanged(bloodPool);

        this.addFields("other", false, elementList);
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.getCharacter() == null) {
            return;
        }

        this.getFields("base").stream().map((field) -> (JTextField) field).forEachOrdered((element) -> {
            if (null != element.getName()) switch (element.getName()) {
                case "name":
                    element.setText(this.getCharacter().getName());
                    break;
                case "chronicle":
                    element.setText(this.getCharacter().getChronicle());
                    break;
                case "generation":
                    element.setText(this.getCharacter().getGeneration().toString());
                    break;
                case "nature":
                    element.setText(this.getCharacter().getNature());
                    break;
                case "hideout":
                    element.setText(this.getCharacter().getHideout());
                    break;
                case "player":
                    element.setText(this.getCharacter().getPlayer());
                    break;
                case "demeanor":
                    element.setText(this.getCharacter().getDemeanor());
                    break;
                case "concept":
                    element.setText(this.getCharacter().getConcept());
                    break;
                case "sire":
                    element.setText(this.getCharacter().getSire());
                    break;
                case "clan":
                    element.setText(this.getCharacter().getClan().getName());
                    break;
                case "sect":
                    element.setText(this.getCharacter().getSect());
                    break;
                default:
                    break;
            }
        });

        this.getFields("other").stream().map((field) -> (JSpinner) field).forEachOrdered((element) -> {
            if (null != element.getName()) {
                switch (element.getName()) {
                    case "willpower":
                        element.setValue(this.getCharacter().getWillpower());
                        break;
                    case "bloodStock":
                        element.setValue(this.getCharacter().getBloodPool());
                        break;
                    default:
                        element.setValue(this.getCharacter().getRoad().getValue());
                        break;
                }
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
