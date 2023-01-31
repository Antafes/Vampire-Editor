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
package antafes.vampireEditor.gui.newCharacter;

import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Attribute;
import antafes.vampireEditor.entity.character.AttributeInterface;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.storage.AttributeStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.utility.StringComparator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class AttributesPanel extends BaseListPanel {

    public AttributesPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addPhysicalFields();
        this.addSocialFields();
        this.addMentalFields();

        super.init();
    }

    /**
     * Return the translated name of the element.
     *
     * @param element The element name to translate
     *
     * @return The translated name
     */
    @Override
    protected String getElementLabelText(String element) {
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);

        try {
            return storage.getEntity(element).getName();
        } catch (EntityStorageException ignored) {
        }

        return element;
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
     * @param type Attribute type
     */
    private void addAttributeFields(String fieldName, AttributeInterface.AttributeType type) {
        HashMap<String, String> list = new HashMap<>();

        this.getValues(type.name()).stream()
            .filter((attribute) -> (attribute.getType().equals(type)))
            .forEachOrdered((attribute) -> list.put(attribute.getKey(), attribute.getKey()));
        list.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue());

        this.addFields(fieldName, list, 1);
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type The attribute type
     *
     * @return List of attribute objects
     */
    protected ArrayList<Attribute> getValues(String type) {
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        ArrayList<Attribute> list = storage.getEntityListByType(AttributeInterface.AttributeType.valueOf(type.toUpperCase()));
        list.sort(new StringComparator());

        return list;
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
                if (getFields("physical").contains(this.getComponent())) {
                    calculateUsedPhysicalPoints();
                }

                if (getFields("social").contains(this.getComponent())) {
                    calculateUsedSocialPoints();
                }

                if (getFields("mental").contains(this.getComponent())) {
                    calculateUsedMentalPoints();
                }

                checkFieldsFilled();
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
    }

    /**
     * Calculate the used physical attribute points.
     */
    private void calculateUsedPhysicalPoints() {
        this.calculateUsedPoints("physical");
    }

    /**
     * Calculate and return the sum of points spent for physical attributes.
     */
    public int getPhysicalPointsSum() {
        return this.getPointsSum("physical");
    }

    /**
     * Check if the spent points for physical attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkPhysicalPoints() {
        return this.checkPoints("physical");
    }

    /**
     * Get the maximum points available for physical attributes.
     */
    public int getPhysicalMaxPoints() {
        return this.getMaxPoints("physical");
    }

    /**
     * Calculate the used social attribute points.
     */
    private void calculateUsedSocialPoints() {
        this.calculateUsedPoints("social");
    }

    /**
     * Calculate and return the sum of points spent for social attributes.
     */
    public int getSocialPointsSum() {
        return this.getPointsSum("social");
    }

    /**
     * Check if the spent points for social attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkSocialPoints() {
        return this.checkPoints("social");
    }

    /**
     * Get the maximum points available for social attributes.
     */
    public int getSocialMaxPoints() {
        return this.getMaxPoints("social");
    }

    /**
     * Calculate the used mental attribute points.
     */
    private void calculateUsedMentalPoints() {
        this.calculateUsedPoints("mental");
    }

    /**
     * Calculate and return the sum of points spent for mental attributes.
     */
    public int getMentalPointsSum() {
        return this.getPointsSum("mental");
    }

    /**
     * Check if the spent points for mental attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkMentalPoints() {
        return this.checkPoints("mental");
    }

    /**
     * Get the maximum points available for mental attributes.
     */
    public int getMentalMaxPoints() {
        return this.getMaxPoints("mental");
    }

    /**
     * Set the maximum value for the attribute spinners.
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("physical").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("social").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields("mental").stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));

        this.calculateUsedMentalPoints();
        this.calculateUsedSocialPoints();
        this.calculateUsedPhysicalPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int physicalSum = this.getPhysicalPointsSum();
        int physicalMax = this.getPhysicalMaxPoints();
        int socialSum = this.getSocialPointsSum();
        int socialMax = this.getSocialMaxPoints();
        int mentalSum = this.getMentalPointsSum();
        int mentalMax = this.getMentalMaxPoints();

        if (physicalSum >= physicalMax
            && socialSum >= socialMax
            && mentalSum >= mentalMax
        ) {
            if (this.getParentComponent().getMaxActiveTab() < 2) {
                this.getParentComponent().increaseMaxActiveTab();
            }

            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the properly weighting values set.
     *
     * @param weighting Enum to get the weighting value from
     *
     * @return The maximum weighting
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return weighting.getAttributeMax();
    }

    /**
     * Adjust the attribute spinners to the clans.
     *
     * @param clan Clan object to fetch some values from
     */
    public void adjustAttributesToClan(Clan clan) {
        ArrayList<Component> fields = this.getFields("social");

        fields.stream().filter((field) -> (field.getName().equals("appearance"))).map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            int value = (int) spinner.getValue(),
                maximum = Integer.parseInt(((SpinnerNumberModel) spinner.getModel()).getMaximum().toString()),
                minimum = 1;

            if (clan.getKey().equals("nosferatu")) {
                value = 0;
                minimum = 0;
                spinner.setEnabled(false);
            } else {
                if (value == 0) {
                    value = minimum;
                }

                spinner.setEnabled(true);
            }

            spinner.setModel(
                new SpinnerNumberModel(
                    Math.min(value, maximum),
                    minimum,
                    maximum,
                    1
                )
            );
        });
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    @Override
    public boolean checkAllFields() {
        if (this.getPointsSum("physical") < this.getMaxPoints("physical")) {
            return true;
        }

        if (this.getPointsSum("social") < this.getMaxPoints("social")) {
            return true;
        }

        return this.getPointsSum("mental") < this.getMaxPoints("mental");
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder object
     */
    @Override
    public void fillCharacter(Character.CharacterBuilder<?, ?> builder) {
        AttributeStorage storage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        this.getFields("physical").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            try {
                Attribute attribute = storage.getEntity(spinner.getName());

                builder.addAttribute(
                    attribute.toBuilder()
                        .setValue((int) spinner.getValue())
                        .build()
                );
            } catch (EntityStorageException ex) {
                Logger.getLogger(AttributesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.getFields("social").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            try {
                Attribute attribute = storage.getEntity(spinner.getName());

                builder.addAttribute(
                    attribute.toBuilder()
                        .setValue((int) spinner.getValue())
                        .build()
                );
            } catch (EntityStorageException ex) {
                Logger.getLogger(AttributesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.getFields("mental").stream().map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            try {
                Attribute attribute = storage.getEntity(spinner.getName());

                builder.addAttribute(
                    attribute.toBuilder()
                        .setValue((int) spinner.getValue())
                        .build()
                );
            } catch (EntityStorageException ex) {
                Logger.getLogger(AttributesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
