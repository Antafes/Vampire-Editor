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

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.EmptyEntityStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.ComponentChangeListener;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.event.VirtueValueSetEvent;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.utility.SortingUtility;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseEditableListPanel {

    /**
     * Create a new advantages panel.
     *
     * @param parent Parent element
     */
    public AdvantagesPanel(NewCharacterDialog parent) {
        super(parent);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.setUseWeightings(false);
        this.addBackgroundFields();
        this.addDisciplineFields();
        this.addVirtueFields();

        super.init();
    }

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields() {
        this.addFields(
            AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural(),
            AdvantageInterface.AdvantageType.BACKGROUND.name()
        );
    }

    /**
     * Add all discipline fields sorted by the translated name.
     */
    private void addDisciplineFields() {
        this.addFields(
            AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural(),
            AdvantageInterface.AdvantageType.DISCIPLINE.name()
        );
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        this.addFields(
            AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural(),
            AdvantageInterface.AdvantageType.VIRTUE.name(),
            SortingUtility.sortAndStringifyEntityMap(new HashMap<>(this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name()))),
            true,
            1,
            3
        );
    }

    /**
     * This will translate the element name.
     *
     * @param element The element name to translate
     *
     * @return The translated name
     */
    @Override
    protected String getElementLabelText(String element) {
        HashMap<String, Advantage> advantages = this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name());

        if (advantages.containsKey(element)) {
            return advantages.get(element).getName();
        }

        return super.getElementLabelText(element);
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
                if (getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).contains(this.getComponent())) {
                    calculateUsedBackgroundPoints();
                }

                if (getFields(AdvantageInterface.AdvantageType.DISCIPLINE.name()).contains(this.getComponent())) {
                    calculateUsedDisciplinePoints();
                }

                if (getFields(AdvantageInterface.AdvantageType.VIRTUE.name()).contains(this.getComponent())) {
                    calculateUsedVirtuePoints();
                    ArrayList<Component> fields = getFields(AdvantageInterface.AdvantageType.VIRTUE.name());
                    ArrayList<Advantage> virtues = new ArrayList<>();
                    AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
                    fields.forEach(component -> {
                        try {
                            Advantage.AdvantageBuilder<?, ?> builder = storage.getEntity(component.getName()).toBuilder();
                            builder.setValue(Integer.parseInt(((JSpinner) component).getValue().toString()));
                            virtues.add(builder.build());
                        } catch (EntityStorageException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    VampireEditor.getDispatcher().dispatch((new VirtueValueSetEvent()).setVirtues(virtues));
                }

                checkFieldsFilled();
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
    }

    /**
     * Calculate the used background points.
     */
    private void calculateUsedBackgroundPoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Calculate and return the sum of points spent for backgrounds.
     */
    public int getBackgroundPointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Check if the spent points for backgrounds is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkBackgroundPoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Get the maximum points available for talents.
     */
    public int getBackgroundMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.BACKGROUND.name());
    }

    /**
     * Calculate the used discipline points.
     */
    private void calculateUsedDisciplinePoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Calculate and return the sum of points spent for disciplines.
     */
    public int getDisciplinePointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Check if the spent points for disciplines is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkDisciplinePoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Get the maximum points available for disciplines.
     */
    public int getDisciplineMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name());
    }

    /**
     * Calculate the used virtue points.
     */
    private void calculateUsedVirtuePoints() {
        this.calculateUsedPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Calculate and return the sum of points spent for virtues.
     */
    public int getVirtuePointsSum() {
        return this.getPointsSum(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Check if the spent points for virtues is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    public boolean checkVirtuePoints() {
        return this.checkPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Get the maximum points available for virtues.
     */
    public int getVirtueMaxPoints() {
        return this.getMaxPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Set the maximum value for the attribute spinners.
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.setSpinnerMaximumValue(maximum);
        this.getFields(AdvantageInterface.AdvantageType.BACKGROUND.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.DISCIPLINE.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, maximum));
        this.getFields(AdvantageInterface.AdvantageType.VIRTUE.name()).stream().map((component) -> (JSpinner) component)
            .forEachOrdered((spinner) -> this.setFieldMaximum(spinner, 5));

        this.calculateUsedVirtuePoints();
        this.calculateUsedDisciplinePoints();
        this.calculateUsedBackgroundPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int backgroundSum = this.getBackgroundPointsSum();
        int backgroundMax = this.getBackgroundMaxPoints();
        int disciplinesSum = this.getDisciplinePointsSum();
        int disciplinesMax = this.getDisciplineMaxPoints();
        int virtuesSum = this.getVirtuePointsSum();
        int virtuesMax = this.getVirtueMaxPoints();

        if (backgroundSum >= backgroundMax
            && disciplinesSum >= disciplinesMax
            && virtuesSum >= virtuesMax
        ) {
            if (this.getParentComponent().getMaxActiveTab() < 4) {
                this.getParentComponent().increaseMaxActiveTab();
            }

            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the properly weighting values set.
     * This isn't used for this kind of panel.
     *
     * @param weighting Enum to get the weighting value from
     *
     * @return Returns always 0 as it isn't used here
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return 0;
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type Identifier for the group of combo boxes
     *
     * @return Map of values
     */
    @Override
    protected HashMap<String, Advantage> getValues(String type)
    {
        AdvantageInterface.AdvantageType advantageType = AdvantageInterface.AdvantageType.valueOf(type);
        AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        LinkedHashMap<String, Advantage> list = new LinkedHashMap<>();

        SortingUtility.sortEntityMap(
            new HashMap<>(storage.getEntityMapByType(advantageType))
        ).forEach((key, value) -> list.put(key, (Advantage) value));

        return list;
    }

    /**
     * Get an entity for the given type.
     *
     * @param type Identifier for the group of combo boxes
     * @param key Key for the object to get
     *
     * @return Returns an advantage object if found, otherwise null.
     */
    @Override
    protected Advantage getEntity(String type, String key) {
        if (this.getValues(type).containsKey(key)) {
            return this.getValues(type).get(key);
        }

        return null;
    }

    /**
     * Set the disciplines of the given clan.
     */
    public void setDisciplines(Clan clan) {
        if (this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLINE.name()).size() == 1) {
            clan.getAdvantages().forEach((discipline) -> {
                ArrayList<JComboBox<BaseTranslatedEntity>> comboBoxList = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLINE.name());

                JComboBox<BaseTranslatedEntity> comboBox = comboBoxList.get(comboBoxList.size() - 1);
                comboBox.setSelectedItem(discipline);
                comboBox.setEditable(false);
                comboBox.setEnabled(false);
                comboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
            });
        } else {
            AtomicInteger counter = new AtomicInteger();
            clan.getAdvantages().forEach((discipline) -> {
                ArrayList<JComboBox<BaseTranslatedEntity>> comboBoxList = this.getComboBoxes(AdvantageInterface.AdvantageType.DISCIPLINE.name());
                int boxCounter = 0;
                for (JComboBox<BaseTranslatedEntity> comboBox : comboBoxList) {
                    if (!comboBox.isEnabled() && !comboBox.isEditable()) {
                        if (counter.get() == boxCounter) {
                            comboBox.setSelectedItem(discipline);
                            counter.getAndIncrement();
                            break;
                        }

                        boxCounter++;
                    }
                }
            });
        }
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     *
     * @param type Identifier for the field
     *
     * @return Maximum points value
     */
    @Override
    protected int getMaxPointsForField(String type) {
        if (type.equals(AdvantageInterface.AdvantageType.BACKGROUND.name())) {
            return 5;
        } else if (type.equals(AdvantageInterface.AdvantageType.DISCIPLINE.name())) {
            return 4;
        } else if (type.equals(AdvantageInterface.AdvantageType.VIRTUE.name())) {
            return 7;
        }

        return 0;
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    @Override
    public boolean checkAllFields() {
        if (this.getPointsSum(AdvantageInterface.AdvantageType.BACKGROUND.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.BACKGROUND.name())) {
            return true;
        }

        if (this.getPointsSum(AdvantageInterface.AdvantageType.DISCIPLINE.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.DISCIPLINE.name())) {
            return true;
        }

        return this.getPointsSum(AdvantageInterface.AdvantageType.VIRTUE.name()) < this.getMaxPoints(AdvantageInterface.AdvantageType.VIRTUE.name());
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder object
     */
    @Override
    public void fillCharacter(Character.CharacterBuilder<?, ?> builder) {
        this.getFields().forEach((key, fields) -> {
            for (int i = 0; i < fields.size(); i++) {
                JSpinner spinner = (JSpinner) fields.get(i);
                Advantage advantage;

                try {
                    if (this.getComboBoxes(key).size() > 0) {
                        JComboBox<BaseTranslatedEntity> comboBox = this.getComboBoxes(key).get(i);

                        if (Objects.equals(comboBox.getSelectedItem(), "")) {
                            continue;
                        }

                        if (Objects.equals(comboBox.getSelectedItem(), this.getEmptyEntity())) {
                            continue;
                        }

                        advantage = (Advantage) comboBox.getSelectedItem();
                    } else {
                        AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
                        advantage = storage.getEntity(spinner.getName());
                    }

                    builder.addAdvantage(
                        Objects.requireNonNull(advantage).toBuilder()
                            .setValue((int) spinner.getValue())
                            .build()
                    );
                } catch (EntityStorageException ex) {
                    Logger.getLogger(AdvantagesPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private EmptyEntity getEmptyEntity()
    {
        return ((EmptyEntityStorage) StorageFactory.getStorage(StorageFactory.StorageType.EMPTY)).getEntity();
    }

    /**
     * Add an item listener for the combo-box.
     *
     * @param elements Map with the combo-box and the spinner
     * @param type Identifier for the field
     * @param spinnerMinimum Minimum value for the spinner
     * @param fields List of all fields
     * @param groups Groups the element should be added to
     * @param layout GroupLayout object
     * @param maxFields Maximum amount of fields
     */
    @Override
    protected void addComboBoxItemListener(
        HashMap<String, Component> elements,
        String type,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout,
        int maxFields
    ) {
        super.addComboBoxItemListener(elements, type, spinnerMinimum, fields, groups, layout, maxFields);

        JComboBox<BaseTranslatedEntity> comboBox = (JComboBox<BaseTranslatedEntity>) elements.get("comboBox");
        JSpinner spinner = (JSpinner) elements.get("spinner");

        // Fetching the second element, as the first is only an empty string.
        if (((Advantage) comboBox.getItemAt(1)).getType().equals(AdvantageInterface.AdvantageType.BACKGROUND)) {
            comboBox.addItemListener((ItemEvent e) -> {
                JComboBox<BaseTranslatedEntity> element = (JComboBox<BaseTranslatedEntity>) e.getSource();
                AdvantageStorage storage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);

                try {
                    if (storage.getEntity("generation").equals(element.getSelectedItem())) {
                        this.addGenerationSpinnerItemListener(spinner);
                    } else {
                        // Remove the generation bonus
                        if (spinner.getChangeListeners().length > 1) {
                            for (ChangeListener listener : spinner.getChangeListeners()) {
                                if (listener.toString().contains(AdvantagesPanel.class.toString())) {
                                    spinner.removeChangeListener(listener);
                                    break;
                                }
                            }
                            ((LooksPanel) this.getParentComponent().getCharacterTabPane().getComponentAt(0))
                                .adjustGeneration(0);
                        }
                    }
                } catch (EntityStorageException ex) {
                    VampireEditor.log(ex.getMessage());
                }
            });
        }
    }

    /**
     * Add a change listener to the generation spinner element.
     *
     * @param spinner The generation spinner
     */
    protected void addGenerationSpinnerItemListener(JSpinner spinner) {
        LooksPanel panel = (LooksPanel) this.getParentComponent().getCharacterTabPane().getComponentAt(0);
        spinner.addChangeListener(
            (ChangeEvent e) -> panel.adjustGeneration((int) ((JSpinner) e.getSource()).getValue())
        );
        panel.adjustGeneration((int) spinner.getValue());
    }
}
