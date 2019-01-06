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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import vampireEditor.entity.Character;
import vampireEditor.gui.BasePanel;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.utility.StringComparator;

/**
 *
 * @author Marian Pollzien
 */
public class GeneralPanel extends BasePanel implements TranslatableComponent, CharacterPanelInterface {
    private vampireEditor.entity.Character character = null;

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
        fieldNames.put("player", this.generateTextField("player"));
        fieldNames.put("demeanor", this.generateTextField("demeanor", false));
        fieldNames.put("concept", this.generateTextField("concept", false));
        fieldNames.put("sire", this.generateTextField("sire"));
        fieldNames.put("clan", this.generateTextField("clan", false));
        fieldNames.put("sect", this.generateTextField("sect"));

        this.addFields("base", false, fieldNames);
    }

    /**
     * Unused in this panel.
     *
     * @param headline
     * @param addHeadline
     * @param elementList
     */
    @Override
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Generate a JTextField with name and editable set.
     *
     * @param name
     *
     * @return
     */
    private JTextField generateTextField(String name) {
        return this.generateTextField(name, true);
    }

    /**
     * Generate a JTextField with name and editable set.
     *
     * @param name
     * @param editable
     *
     * @return
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
        this.character.getMerits().sort(new StringComparator());
        this.character.getFlaws().sort(new StringComparator());

        this.character.getMerits().forEach((merit) -> {
            JLabel label = new JLabel();
            label.setText(merit.toString());
            label.setSize(167, GroupLayout.DEFAULT_SIZE);
            elementList.put(merit.getKey(), label);
        });

        this.character.getFlaws().forEach((flaw) -> {
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
        elementList.put(this.character.getRoad().getName(), road);

        JSpinner willpower = new JSpinner();
        willpower.setModel(new SpinnerNumberModel(0, 0, 10, 1));
        willpower.setSize(spinnerDimension);
        willpower.setName("willpower");
        elementList.put("willpower", willpower);

        JSpinner bloodStock = new JSpinner();
        bloodStock.setModel(new SpinnerNumberModel(0, 0, this.character.getGeneration().getMaximumBloodStock(), 1));
        bloodStock.setSize(spinnerDimension);
        bloodStock.setName("bloodStock");
        elementList.put("bloodStock", bloodStock);

        this.addFields("other", false, elementList);
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

        this.getFields("base").stream().map((field) -> (JTextField) field).forEachOrdered((element) -> {
            if (null != element.getName()) switch (element.getName()) {
                case "name":
                    element.setText(this.character.getName());
                    break;
                case "chronicle":
                    element.setText(this.character.getChronicle());
                    break;
                case "generation":
                    element.setText(this.character.getGeneration().toString());
                    break;
                case "nature":
                    element.setText(this.character.getNature());
                    break;
                case "hideout":
                    element.setText(this.character.getHideout());
                    break;
                case "player":
                    element.setText(this.character.getPlayer());
                    break;
                case "demeanor":
                    element.setText(this.character.getDemeanor());
                    break;
                case "concept":
                    element.setText(this.character.getConcept());
                    break;
                case "sire":
                    element.setText(this.character.getSire());
                    break;
                case "clan":
                    element.setText(this.character.getClan().getName());
                    break;
                case "sect":
                    element.setText(this.character.getSect());
                    break;
                default:
                    break;
            }
        });

        this.getFields("other").stream().map((field) -> (JSpinner) field).forEachOrdered((element) -> {
            if (null != element.getName()) {
                switch (element.getName()) {
                    case "willpower":
                        element.setValue(this.character.getWillpower());
                        break;
                    case "bloodStock":
                        element.setValue(this.character.getBloodStock());
                        break;
                    default:
                        element.setValue(this.character.getRoad().getValue());
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
