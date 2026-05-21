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
import antafes.vampireEditor.entity.character.Flaw;
import antafes.vampireEditor.entity.character.Merit;
import antafes.vampireEditor.entity.character.Nature;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.NatureStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.utility.NatureResolutionUtility;
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
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.getCharacter() == null) {
            return;
        }

        this.getFields("base").stream().map((field) -> (JTextField) field).forEachOrdered((element) -> {
            if (null != element.getName()) {
                switch (element.getName()) {
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
                        element.setText(this.getCharacter().getNature() == null ? "" : this.getCharacter().getNature().toString());
                        break;
                    case "hideout":
                        element.setText(this.getCharacter().getHideout());
                        break;
                    case "player":
                        element.setText(this.getCharacter().getPlayer());
                        break;
                    case "demeanor":
                        element.setText(this.getCharacter().getDemeanor() == null ? "" : this.getCharacter().getDemeanor());
                        break;
                    case "concept":
                        element.setText(this.getCharacter().getConcept() == null ? "" : this.getCharacter().getConcept());
                        break;
                    case "sire":
                        element.setText(this.getCharacter().getSire());
                        break;
                    case "clan":
                        element.setText(this.getCharacter().getClan() == null ? "" : this.getCharacter().getClan().getName());
                        break;
                    case "sect":
                        element.setText(this.getCharacter().getSect());
                        break;
                    default:
                        break;
                }
            }
        });

        this.getFields("other").stream().map((field) -> (JSpinner) field).forEachOrdered((element) -> {
            if (null != element.getName()) {
                switch (element.getName()) {
                    case "willpower":
                        element.setValue(this.getCharacter().getWillpower());
                        break;
                    case "bloodPool":
                        element.setValue(this.getCharacter().getBloodPool());
                        break;
                    default:
                        Road effectiveRoad = this.getEffectiveRoad();
                        element.setValue(effectiveRoad == null ? 2 : effectiveRoad.getValue());
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

    @Override
    public void updateCharacter(Character.CharacterBuilder<?, ?> characterBuilder)
    {
        NatureStorage natureStorage = StorageFactory.getStorage(StorageFactory.StorageType.NATURE);
        this.getFields("base").stream().map((field) -> (JTextField) field).forEachOrdered((element) -> {
            switch (element.getName()) {
                case "nature":
                    try {
                        characterBuilder.setNature(this.resolveNatureFromText(natureStorage, element.getText()));
                    } catch (EntityStorageException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "hideout":
                    characterBuilder.setHideout(element.getText());
                    break;
                case "player":
                    characterBuilder.setPlayer(element.getText());
                    break;
                case "demeanor":
                    characterBuilder.setDemeanor(this.normalizeOptionalText(element.getText()));
                    break;
                case "concept":
                    characterBuilder.setConcept(this.normalizeOptionalText(element.getText()));
                    break;
                case "sire":
                    characterBuilder.setSire(element.getText());
                    break;
                case "sect":
                    characterBuilder.setSect(element.getText());
                    break;
                default:
                    break;
            }
        });
        this.getFields("other").stream().map((field) -> (JSpinner) field).forEachOrdered((element) -> {
            switch (element.getName()) {
                case "willpower":
                    characterBuilder.setWillpower((int) element.getValue());
                    break;
                case "bloodPool":
                    characterBuilder.setBloodPool((int) element.getValue());
                    break;
                default:
                    Road effectiveRoad = this.getEffectiveRoad();
                    if (effectiveRoad == null) {
                        break;
                    }

                    Road.RoadBuilder<?, ?> roadBuilder = effectiveRoad.toBuilder();
                    roadBuilder.setValue((int) element.getValue());
                    if (this.getCharacter().getPath() != null) {
                        characterBuilder.setRoad(effectiveRoad.getParent())
                            .setPath(roadBuilder.build());
                    } else {
                        characterBuilder.setRoad(roadBuilder.build());
                    }
                    break;
            }
        });
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addBaseFields();
        this.addMeritAndFlawFields();
        this.addOtherFields();
        this.fillCharacterData();
        this.clearUndoHistory();

        super.init();
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
     * Add all base fields.
     */
    private void addBaseFields() {
        LinkedHashMap<String, JComponent> fieldNames = new LinkedHashMap<>();

        fieldNames.put("name", this.generateTextField("name", false));
        fieldNames.put("chronicle", this.generateTextField("chronicle", false));
        fieldNames.put("generation", this.generateTextField("generation", false));
        fieldNames.put("nature", this.generateTextField("nature"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("nature"));
        fieldNames.put("hideout", this.generateTextField("hideout"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("hideout"));
        fieldNames.put("player", this.generateTextField("player"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("player"));
        fieldNames.put("demeanor", this.generateTextField("demeanor"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("demeanor"));
        fieldNames.put("concept", this.generateTextField("concept"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("concept"));
        fieldNames.put("sire", this.generateTextField("sire"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("sire"));
        fieldNames.put("clan", this.generateTextField("clan", false));
        fieldNames.put("sect", this.generateTextField("sect"));
        this.addChangeListenerForCharacterChanged(fieldNames.get("sect"));

        this.addFields("base", false, fieldNames);
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
        Road effectiveRoad = this.getEffectiveRoad();
        elementList.put(
            effectiveRoad == null ? this.getLanguage().translate("road") : effectiveRoad.getName(),
            road
        );
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

    private Road getEffectiveRoad() {
        if (this.getCharacter().getPath() != null) {
            return this.getCharacter().getPath();
        }

        return this.getCharacter().getRoad();
    }

    private Nature resolveNatureFromText(NatureStorage natureStorage, String inputText) throws EntityStorageException
    {
        return NatureResolutionUtility.resolveNature(natureStorage, inputText);
    }

    private String normalizeOptionalText(String inputText)
    {
        return NatureResolutionUtility.normalizeOptionalText(inputText);
    }
}
