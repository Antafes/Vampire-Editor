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
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.BaseTypedTranslatedEntity;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.GenerationStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.BaseColumnListPanel;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.event.AddGenerationItemListenerEvent;
import antafes.vampireEditor.gui.event.ClanSelectedEvent;
import antafes.vampireEditor.gui.event.FillCharacterEvent;
import antafes.vampireEditor.gui.event.RoadSelectedEvent;
import antafes.vampireEditor.gui.event.UpdateFreeAdditionalPointsEvent;
import antafes.vampireEditor.gui.event.VirtueValueSetEvent;
import antafes.vampireEditor.gui.event.listener.AdvantagesComboBoxItemListener;
import antafes.vampireEditor.gui.event.listener.AddGenerationEventListener;
import antafes.vampireEditor.gui.event.listener.ClanSelectedListener;
import antafes.vampireEditor.gui.event.listener.FillCharacterListener;
import antafes.vampireEditor.gui.event.listener.RoadSelectedListener;
import antafes.vampireEditor.gui.exception.ElementAlreadyExistsException;
import antafes.vampireEditor.gui.exception.LabelEmptyException;
import antafes.vampireEditor.gui.exception.TypeNotSupportedException;
import antafes.vampireEditor.gui.utility.FreeAdditionalPointsFields;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.utility.SortingUtility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseColumnListPanel
{
    private static final int VIRTUE_MAXIMUM = 5;
    private final NewCharacterDialog parent;
    private final HashMap<String, Integer> dynamicRowCounters = new HashMap<>();
    /** Maps advantage key (e.g. "conscience") to the translated label used as the spinner map key, in sorted order. */
    private final LinkedHashMap<String, String> virtueKeyToLabel = new LinkedHashMap<>();
    private int generationMaximum;
    private JButton backButton;
    private JButton nextButton;

    /**
     * Creates a new AdvantagesPanel.
     * {@link #start()} and {@link #build()} must be called separately by the owner.
     *
     * @param parent The owning NewCharacterDialog
     */
    public AdvantagesPanel(NewCharacterDialog parent)
    {
        super();
        this.parent = parent;
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init()
    {
        this.generationMaximum = this.getMaximumFromGeneration(0);
        this.addBackgroundFields();
        this.addDisciplineFields();
        this.addVirtueFields();
        this.initButtons();

        this.parent.getDialogDispatcher().addListener(
            ClanSelectedEvent.class,
            new ClanSelectedListener(event -> this.onClanSelected(event.getClan()))
        );
        this.parent.getDialogDispatcher().addListener(
            RoadSelectedEvent.class,
            new RoadSelectedListener(event -> this.onRoadSelected(event.getRoad()))
        );
        this.parent.getDialogDispatcher().addListener(
            AddGenerationItemListenerEvent.class,
            new AddGenerationEventListener(event -> this.adjustGeneration(event.getAdjustment()))
        );
        this.parent.getDialogDispatcher().addListener(
            FillCharacterEvent.class,
            new FillCharacterListener(event -> this.fillCharacter(event.getBuilder()))
        );
    }

    /**
     * Builds the columns and appends the navigation button row directly to this panel.
     */
    @Override
    public void build() throws TypeNotSupportedException
    {
        super.build();
        this.configureVirtueSpinners();
        this.registerGenerationListeners(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural());
        this.addButtonPanel();
    }

    @Override
    public void updateTexts()
    {
    }

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields()
    {
        this.addGroup(1, AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural(), false, true);
        try {
            this.addRow(
                1,
                AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural(),
                AdvantageInterface.AdvantageType.BACKGROUND.name(),
                ElementType.SPINNER,
                true,
                this.generationMaximum,
                null
            );
        } catch (ElementAlreadyExistsException | LabelEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add all discipline fields sorted by the translated name.
     * Rows are populated dynamically when a clan is selected via {@link #onClanSelected(Clan)}.
     */
    private void addDisciplineFields()
    {
        this.addGroup(2, AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural(), false, true);
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields()
    {
        this.addGroup(3, AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural(), false, true);
        HashMap<String, BaseTypedTranslatedEntity> values = SortingUtility.sortEntityMap(
            new HashMap<>(this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name()))
        );
        values.forEach((key, background) -> {
            try {
                this.addRow(
                    3,
                    AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural(),
                    background.getName(),
                    ElementType.SPINNER,
                    VIRTUE_MAXIMUM
                );
                this.virtueKeyToLabel.put(key, background.getName());
            } catch (ElementAlreadyExistsException | LabelEmptyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Called when a road is selected. Clears the virtue group and re-adds only the virtue spinners
     * that belong to the road's merits list; courage is always included.
     * Pass {@code null} to show all virtues.
     *
     * @param road The selected road, or {@code null} when the selection is cleared
     */
    private void onRoadSelected(Road road)
    {
        String virtueGroup = AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural();
        this.clearDynamicRows(virtueGroup);

        HashSet<String> allowedKeys;
        if (road == null || road.getMerits() == null || road.getMerits().isEmpty()) {
            allowedKeys = new HashSet<>(this.virtueKeyToLabel.keySet());
        } else {
            allowedKeys = new HashSet<>();
            allowedKeys.add("courage");
            road.getMerits().forEach(merit -> allowedKeys.add(merit.getKey()));
        }

        this.virtueKeyToLabel.entrySet().stream()
            .filter(e -> allowedKeys.contains(e.getKey()))
            .sorted(Map.Entry.comparingByValue())
            .forEach(e -> this.addDynamicLabelSpinnerRow(
                virtueGroup,
                e.getValue(),
                e.getValue(),
                VIRTUE_MAXIMUM
            ));
        this.configureVirtueSpinners();
    }

    /**
     * When a background or discipline is selected from an editable combo box, add a new empty row of the same
     * type below.
     */
    @Override
    protected void onEditableComboBoxSelected(String groupLabel, JComboBox<BaseTranslatedEntity> comboBox)
    {
        if (!AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural().equals(groupLabel)
            && !AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural().equals(groupLabel)) {
            return;
        }

        Object selected = comboBox.getSelectedItem();
        if (selected == null || selected instanceof EmptyEntity) {
            return;
        }

        if (Boolean.TRUE.equals(comboBox.getClientProperty("dynamicRowAdded"))) {
            return;
        }

        AdvantageInterface.AdvantageType type =
            AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural().equals(groupLabel)
                ? AdvantageInterface.AdvantageType.BACKGROUND
                : AdvantageInterface.AdvantageType.DISCIPLINE;

        comboBox.putClientProperty("dynamicRowAdded", true);
        int counter = this.dynamicRowCounters.merge(groupLabel, 1, Integer::sum);
        String rowKey = type.name() + "_" + counter;
        this.addDynamicRow(groupLabel, rowKey, type.name(), this.generationMaximum);
        if (AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural().equals(groupLabel)) {
            this.registerGenerationListener(groupLabel, rowKey);
        }
    }

    /**
     * Called when a clan is selected. Replaces any existing discipline rows with one locked row per
     * clan discipline (pre-selected and disabled) followed by one empty row for user selection.
     *
     * @param clan The selected clan
     */
    private void onClanSelected(Clan clan)
    {
        String disciplineGroup = AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural();
        this.clearDynamicRows(disciplineGroup);
        this.dynamicRowCounters.remove(disciplineGroup);

        int max = this.generationMaximum;

        clan.getAdvantages().stream()
            .filter(a -> a.getType() == AdvantageInterface.AdvantageType.DISCIPLINE)
            .forEach(discipline -> {
                int counter = this.dynamicRowCounters.merge(disciplineGroup, 1, Integer::sum);
                String uniqueLabel = AdvantageInterface.AdvantageType.DISCIPLINE.name() + "_" + counter;
                JComboBox<BaseTranslatedEntity> cb = this.addDynamicRow(
                    disciplineGroup, uniqueLabel,
                    AdvantageInterface.AdvantageType.DISCIPLINE.name(),
                    max, discipline
                );
                if (cb != null) {
                    cb.setEnabled(false);
                    this.applyLockedStyle(cb);
                }
            });

        int counter = this.dynamicRowCounters.merge(disciplineGroup, 1, Integer::sum);
        this.addDynamicRow(
            disciplineGroup,
            AdvantageInterface.AdvantageType.DISCIPLINE.name() + "_" + counter,
            AdvantageInterface.AdvantageType.DISCIPLINE.name(),
            max
        );
    }

    /**
     * Creates and configures the back and next navigation buttons.
     */
    private void initButtons()
    {
        this.backButton = new JButton(this.getConfiguration().getLanguageObject().translate("back"));
        this.backButton.addActionListener(e ->
            this.parent.getCharacterTabPane().setSelectedIndex(
                this.parent.getCharacterTabPane().getSelectedIndex() - 1
            )
        );

        this.nextButton = new JButton(this.getConfiguration().getLanguageObject().translate("next"));
        this.nextButton.setEnabled(false);
        this.nextButton.addActionListener(e ->
            this.parent.getCharacterTabPane().setSelectedIndex(
                this.parent.getCharacterTabPane().getSelectedIndex() + 1
            )
        );
    }

    /**
     * Appends a button row to the bottom of this panel, matching the layout of the abilities panel.
     */
    private void addButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        GroupLayout btnLayout = new GroupLayout(buttonPanel);
        buttonPanel.setLayout(btnLayout);
        btnLayout.setHorizontalGroup(
            btnLayout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
                .addComponent(this.backButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.nextButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
        );
        btnLayout.setVerticalGroup(
            btnLayout.createSequentialGroup()
                .addGroup(btnLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.backButton)
                    .addComponent(this.nextButton)
                )
                .addContainerGap()
        );

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        this.add(buttonPanel, c);
    }

    /**
     * Adds the navigation buttons to the focus traversal order before creating the policy.
     */
    @Override
    protected void createFocusTraversalPolicy()
    {
        this.addToFocusTraversalOrder(this.nextButton);
        this.addToFocusTraversalOrder(this.backButton);
        super.createFocusTraversalPolicy();
    }

    /**
     * Reacts to spinner changes by checking whether virtue points are fully spent.
     */
    @Override
    protected void afterFreeAdditionalPointsUpdated(String groupLabel)
    {
        if (AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural().equals(groupLabel)) {
            this.dispatchVirtueValues();
        }
        this.checkFieldsFilled();
    }

    @Override
    protected int getUsedGroupSpinnerSum(String groupLabel)
    {
        if (!AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural().equals(groupLabel)) {
            return super.getUsedGroupSpinnerSum(groupLabel);
        }

        int baseVirtuePoints = this.getSpinnersForGroup(groupLabel).size();
        return Math.max(0, super.getGroupSpinnerSum(groupLabel) - baseVirtuePoints);
    }

    /**
     * Enables the next button and unlocks the last-steps tab once all advantage points are spent.
     */
    protected void checkFieldsFilled()
    {
        boolean allGroupsFilled = this.isGroupFilled(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural())
            && this.isGroupFilled(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural())
            && this.isGroupFilled(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural());

        if (allGroupsFilled) {
            if (this.parent.getMaxActiveTab() < 4) {
                this.parent.increaseMaxActiveTab();
            }
            this.parent.getCharacterTabPane().setEnabledAt(this.parent.getMaxActiveTab(), true);
            this.enableNextButton();
        } else if (this.parent.getMaxActiveTab() < 4) {
            this.disableNextButton();
        }
    }

    private boolean isGroupFilled(String groupLabel)
    {
        FreeAdditionalPointsFields fields = this.getFreeAdditionalPointsElementsForGroup(groupLabel);
        if (fields == null) {
            return false;
        }

        int used = Integer.parseInt(fields.getFreeAdditionalPointsField().getText());
        int max = Integer.parseInt(fields.getMaxFreeAdditionalPointsField().getText());

        return used >= max;
    }

    protected void enableNextButton()
    {
        this.nextButton.setEnabled(true);
        this.createFocusTraversalPolicy();
    }

    protected void disableNextButton()
    {
        this.nextButton.setEnabled(false);
        this.createFocusTraversalPolicy();
    }

    /**
     * Applies the standard disabled-field foreground colour to a locked combo box so its text remains readable.
     */
    private void applyLockedStyle(JComboBox<BaseTranslatedEntity> comboBox)
    {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setForeground(Color.BLACK);
                super.paint(g);
            }
        });
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type Identifier for the group of combo boxes
     * @return Map of values
     */
    protected HashMap<String, BaseTranslatedEntity> getComboBoxLabelValues(String type)
    {
        AdvantageInterface.AdvantageType advantageType = AdvantageInterface.AdvantageType.valueOf(type);
        AdvantageStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);

        return new LinkedHashMap<>(SortingUtility.sortEntityMap(new HashMap<>(
            storage.getEntityMapByType(advantageType))));
    }

    /**
     * Get the values for the element combo box.
     *
     * @param type Identifier for the group of combo boxes
     * @return Map of values
     */
    protected HashMap<String, Advantage> getValues(String type)
    {
        AdvantageInterface.AdvantageType advantageType = AdvantageInterface.AdvantageType.valueOf(type);
        AdvantageStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        LinkedHashMap<String, Advantage> list = new LinkedHashMap<>();

        SortingUtility.sortEntityMap(
            new HashMap<>(storage.getEntityMapByType(advantageType))
        ).forEach((key, value) -> list.put(key, (Advantage) value));

        return list;
    }

    /**
     * Get the maximum available points for setting them in the max points field.
     */
    @Override
    protected int fetchMaxFreeAdditionalPoints(String groupLabelText, JComboBox<Weighting> weightingField)
    {
        if (groupLabelText.equals(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural())) {
            return 5;
        } else if (groupLabelText.equals(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural())) {
            return 4;
        } else if (groupLabelText.equals(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural())) {
            return 7;
        }

        return 0;
    }

    /**
     * This method checks every input made by the user for duplicate entries or other inconsistencies.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    public boolean checkAllFields()
    {
        return this.hasDuplicateSelections(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural())
            || this.hasDuplicateSelections(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural());
    }

    /**
     * Returns true if two or more combo boxes in the given group have the same non-empty selection.
     */
    private boolean hasDuplicateSelections(String groupLabel)
    {
        HashSet<String> seen = new HashSet<>();
        for (JComboBox<BaseTranslatedEntity> comboBox : this.getComboBoxesForGroup(groupLabel).values()) {
            Object selected = comboBox.getSelectedItem();
            if (selected == null || selected instanceof EmptyEntity) {
                continue;
            }
            if (!seen.add(((BaseTranslatedEntity) selected).getKey())) {
                return true;
            }
        }
        return false;
    }

    private int getMaximumFromGeneration(int adjustment)
    {
        GenerationStorage generationStorage = StorageFactory.getStorage(StorageFactory.StorageType.GENERATION);
        int maximum;
        try {
            maximum = generationStorage.clampGeneration(
                generationStorage.getDefaultGeneration().getGeneration() - adjustment
            ).getMaximumAttributes();
        } catch (EntityStorageException e) {
            throw new RuntimeException(e);
        }
        return maximum;
    }

    private void adjustGeneration(int adjustment)
    {
        this.generationMaximum = this.getMaximumFromGeneration(adjustment);
        this.setSpinnerMaximum(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural(), this.generationMaximum);
        this.setSpinnerMaximum(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural(), this.generationMaximum);
        this.updateFreeAdditionalPoints(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural());
        this.updateFreeAdditionalPoints(AdvantageInterface.AdvantageType.DISCIPLINE.getKeyPlural());
        this.updateFreeAdditionalPoints(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural());
    }

    private void setSpinnerMaximum(String groupLabel, int maximum)
    {
        this.getSpinnersForGroup(groupLabel).values().forEach(spinner -> {
            SpinnerNumberModel currentModel = (SpinnerNumberModel) spinner.getModel();
            int value = ((Number) spinner.getValue()).intValue();
            currentModel.setMaximum(maximum);
            if (value > maximum) {
                spinner.setValue(maximum);
            }
        });
    }

    private void registerGenerationListeners(String groupLabel)
    {
        this.getComboBoxesForGroup(groupLabel).keySet()
            .forEach(rowKey -> this.registerGenerationListener(groupLabel, rowKey));
    }

    private void registerGenerationListener(String groupLabel, String rowKey)
    {
        JComboBox<BaseTranslatedEntity> comboBox = this.getComboBoxesForGroup(groupLabel).get(rowKey);
        JSpinner spinner = this.getSpinnersForGroup(groupLabel).get(rowKey);

        if (comboBox == null || spinner == null || Boolean.TRUE.equals(comboBox.getClientProperty("generationListenerRegistered"))) {
            return;
        }

        comboBox.addItemListener(new AdvantagesComboBoxItemListener(spinner, this.parent.getDialogDispatcher()));
        comboBox.putClientProperty("generationListenerRegistered", true);
    }

    private void configureVirtueSpinners()
    {
        String virtueGroup = AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural();
        this.getSpinnersForGroup(virtueGroup).values().forEach(spinner -> {
            SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
            model.setMinimum(1);
            if (((Number) spinner.getValue()).intValue() < 1) {
                spinner.setValue(1);
            }
        });
        this.updateFreeAdditionalPoints(virtueGroup);
    }

    private void dispatchVirtueValues()
    {
        HashMap<String, JSpinner> virtueSpinners = this.getSpinnersForGroup(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural());
        HashMap<String, Advantage> virtueValues = this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name());
        ArrayList<Advantage> virtues = new ArrayList<>();

        this.virtueKeyToLabel.forEach((key, label) -> {
            JSpinner spinner = virtueSpinners.get(label);
            Advantage virtue = virtueValues.get(key);

            if (spinner == null || virtue == null) {
                return;
            }

            virtues.add(virtue.toBuilder().setValue(((Number) spinner.getValue()).intValue()).build());
        });

        this.parent.getDialogDispatcher().dispatch(new VirtueValueSetEvent().setVirtues(virtues));
    }

    @Override
    protected void dispatchUpdateFreeAdditionalPointsEvent(UpdateFreeAdditionalPointsEvent event)
    {
        this.parent.getDialogDispatcher().dispatch(event);
    }

    private void fillCharacter(Character.CharacterBuilder<?, ?> builder)
    {
        this.fillSelectedAdvantages(builder, AdvantageInterface.AdvantageType.BACKGROUND);
        this.fillSelectedAdvantages(builder, AdvantageInterface.AdvantageType.DISCIPLINE);
        this.fillVirtues(builder);
    }

    private void fillSelectedAdvantages(Character.CharacterBuilder<?, ?> builder, AdvantageInterface.AdvantageType type)
    {
        String groupLabel = type.getKeyPlural();
        HashMap<String, JSpinner> spinners = this.getSpinnersForGroup(groupLabel);

        this.getComboBoxesForGroup(groupLabel).forEach((rowKey, comboBox) -> {
            Object selectedItem = comboBox.getSelectedItem();
            if (!(selectedItem instanceof Advantage)) {
                return;
            }

            JSpinner spinner = spinners.get(rowKey);
            if (spinner == null) {
                return;
            }

            builder.addAdvantage(
                ((Advantage) selectedItem).toBuilder()
                    .setValue(((Number) spinner.getValue()).intValue())
                    .build()
            );
        });
    }

    private void fillVirtues(Character.CharacterBuilder<?, ?> builder)
    {
        HashMap<String, JSpinner> virtueSpinners = this.getSpinnersForGroup(AdvantageInterface.AdvantageType.VIRTUE.getKeyPlural());
        HashMap<String, Advantage> virtueValues = this.getValues(AdvantageInterface.AdvantageType.VIRTUE.name());

        this.virtueKeyToLabel.forEach((key, label) -> {
            JSpinner spinner = virtueSpinners.get(label);
            Advantage virtue = virtueValues.get(key);

            if (spinner == null || virtue == null) {
                return;
            }

            builder.addAdvantage(
                virtue.toBuilder()
                    .setValue(((Number) spinner.getValue()).intValue())
                    .build()
            );
        });
    }
}
