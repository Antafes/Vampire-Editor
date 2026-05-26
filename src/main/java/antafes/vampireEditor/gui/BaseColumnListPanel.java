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
 * @copyright (c) 2023, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.storage.EmptyEntityStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.event.UpdateFreeAdditionalPointsEvent;
import antafes.vampireEditor.gui.exception.ElementAlreadyExistsException;
import antafes.vampireEditor.gui.exception.LabelEmptyException;
import antafes.vampireEditor.gui.exception.TypeNotSupportedException;
import antafes.vampireEditor.gui.service.WeightingService;
import antafes.vampireEditor.gui.utility.FreeAdditionalPointsFields;
import antafes.vampireEditor.gui.utility.NewCharacterFocusTraversalPolicy;
import antafes.vampireEditor.gui.utility.Weighting;
import antafes.vampireEditor.language.LanguageInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

abstract public class BaseColumnListPanel extends JPanel implements antafes.vampireEditor.gui.TranslatableComponent
{
    @Getter(AccessLevel.PROTECTED)
    private final Configuration configuration;
    private final LanguageInterface language;
    private final WeightingService weightingService;
    private GridBagLayout layout;
    private final List<Component> extraFocusOrder = new ArrayList<>();
    private LinkedHashMap<String, List<Component>> groupFocusComponents;
    private TreeMap<Integer, HashMap<String, HashMap<String, ElementType>>> columns;
    private HashMap<Integer, HashMap<String, Boolean>> useWeightings;
    private HashMap<Integer, HashMap<String, Boolean>> useFreeAdditionalPoints;
    private HashMap<String, HashMap<String, JComboBox<BaseTranslatedEntity>>> comboBoxes;
    private HashMap<String, HashMap<String, Component>> elements;
    private HashMap<String, JComboBox<Weighting>> weightingElements;
    private HashMap<String, FreeAdditionalPointsFields> freeAdditionalPointsElements;
    private HashMap<String, Boolean> editableElementLabels;
    private HashMap<String, BaseTranslatedEntity> selectedComboBoxValues;
    private HashMap<String, Integer> spinnerMaximum;
    private HashMap<String, HashMap<String, JSpinner>> groupSpinners;

    @Getter @Setter @Accessors(fluent = true)
    private boolean translateFieldLabels = true;

    @Getter @Setter @Accessors(fluent = true)
    private boolean translateGroupLabels = true;

    private HashMap<String, JPanel> groupPanels;
    private HashMap<String, Integer> groupNextDynamicRow;
    private HashMap<String, Integer> groupBaseDynamicRow;

    public BaseColumnListPanel()
    {
        super();

        this.configuration = Configuration.getInstance();
        this.language = this.configuration.getLanguageObject();
        this.weightingService = new WeightingService();
    }

    public void start()
    {
        this.initComponents();
        this.init();
    }

    public void build() throws TypeNotSupportedException
    {
        this.createColumns();
        this.createFocusTraversalPolicy();
    }

    protected abstract void init();

    protected BaseColumnListPanel addGroup(@NonNull Integer column, String groupLabel)
    {
        return this.addGroup(column, groupLabel, false, false);
    }

    protected BaseColumnListPanel addGroup(
        @NonNull Integer column,
        String groupLabel,
        @NonNull Boolean useWeighting,
        @NonNull Boolean useFreeAdditionalPoints
    ) {
        if (!this.columns.containsKey(column)) {
            this.columns.put(column, new LinkedHashMap<>());
        }

        if (!this.columns.get(column).containsKey(groupLabel)) {
            this.columns.get(column).put(groupLabel, new HashMap<>());
        }

        if (!this.useWeightings.containsKey(column)) {
            this.useWeightings.put(column, new HashMap<>());
        }

        this.useWeightings.get(column).put(groupLabel, useWeighting);

        if (!this.useFreeAdditionalPoints.containsKey(column)) {
            this.useFreeAdditionalPoints.put(column, new HashMap<>());
        }

        this.useFreeAdditionalPoints.get(column).put(groupLabel, useFreeAdditionalPoints);

        return this;
    }

    protected BaseColumnListPanel addRow(
        @NonNull Integer column,
        String groupLabel,
        @NonNull String elementLabel,
        @NonNull ElementType elementType
    ) throws ElementAlreadyExistsException, LabelEmptyException
    {
        return this.addRow(column, groupLabel, elementLabel, elementType, 0);
    }

    protected BaseColumnListPanel addRow(
        @NonNull Integer column,
        String groupLabel,
        @NonNull String elementLabel,
        @NonNull ElementType elementType,
        int spinnerMaximum
    ) throws ElementAlreadyExistsException, LabelEmptyException
    {
        return this.addRow(column, groupLabel, elementLabel, elementType, false, spinnerMaximum, null);
    }

    protected BaseColumnListPanel addRow(
        @NonNull Integer column,
        String groupLabel,
        @NonNull String elementLabel,
        @NonNull ElementType elementType,
        @NonNull Boolean editableElementLabel,
        int spinnerMaximum,
        BaseTranslatedEntity selectedComboBoxValue
    ) throws ElementAlreadyExistsException, LabelEmptyException
    {
        if (!this.groupExists(column, groupLabel)) {
            this.addGroup(column, groupLabel);
        }

        if (elementLabel.isEmpty()) {
            throw new LabelEmptyException();
        }

        if (this.columns.get(column).get(groupLabel).containsKey(elementLabel)) {
            throw new ElementAlreadyExistsException(elementLabel);
        }

        this.columns.get(column).get(groupLabel).put(elementLabel, elementType);
        this.editableElementLabels.put(elementLabel, editableElementLabel);
        this.spinnerMaximum.put(elementLabel, spinnerMaximum);
        this.selectedComboBoxValues.put(elementLabel, selectedComboBoxValue);

        return this;
    }

    protected void createFocusTraversalPolicy() {
        Vector<Component> fullOrder = new Vector<>();
        this.groupFocusComponents.values().stream()
            .flatMap(List::stream)
            .filter(Component::isEnabled)
            .forEach(fullOrder::add);
        this.extraFocusOrder.stream()
            .filter(Component::isEnabled)
            .forEach(fullOrder::add);
        this.setFocusTraversalPolicy(new NewCharacterFocusTraversalPolicy(fullOrder));
        this.setFocusTraversalPolicyProvider(true);
    }

    abstract protected int fetchMaxFreeAdditionalPoints(String groupLabelText, JComboBox<Weighting> weightingField);

    protected HashMap<String, BaseTranslatedEntity> getComboBoxLabelValues(String key)
    {
        return new HashMap<>();
    }

    private void initComponents()
    {
        this.layout = new GridBagLayout();
        this.columns = new TreeMap<>();
        this.groupFocusComponents = new LinkedHashMap<>();
        this.useWeightings = new HashMap<>();
        this.useFreeAdditionalPoints = new HashMap<>();
        this.freeAdditionalPointsElements = new HashMap<>();
        this.elements = new HashMap<>();
        this.comboBoxes = new HashMap<>();
        this.weightingElements = new HashMap<>();
        this.editableElementLabels = new HashMap<>();
        this.selectedComboBoxValues = new HashMap<>();
        this.spinnerMaximum = new HashMap<>();
        this.groupPanels = new HashMap<>();
        this.groupNextDynamicRow = new HashMap<>();
        this.groupBaseDynamicRow = new HashMap<>();
        this.groupSpinners = new HashMap<>();

        this.setLayout(this.layout);
    }

    private boolean groupExists(Integer column, String groupLabel)
    {
        return this.columns.containsKey(column) && this.columns.get(column).containsKey(groupLabel);
    }

    private void createColumns() throws TypeNotSupportedException
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        for (Map.Entry<Integer, HashMap<String, HashMap<String, ElementType>>> e : this.columns.entrySet()) {
            Integer column = e.getKey();
            constraints.gridx = column - 1;
            HashMap<String, HashMap<String, ElementType>> groups = e.getValue();
            for (Map.Entry<String, HashMap<String, ElementType>> entry : groups.entrySet()) {
                String groupLabel = entry.getKey();
                HashMap<String, ElementType> value = entry.getValue();
                this.createGroup(groupLabel, value, column, constraints);
                constraints.gridy++;
            }
            constraints.gridy = 0;
        }
    }

    private void createGroup(
        String groupLabelText,
        HashMap<String, ElementType> elements,
        int column,
        GridBagConstraints constraints
    ) throws TypeNotSupportedException
    {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new GridBagLayout());
        JComboBox<Weighting> weightingElement = null;
        GridBagConstraints groupConstraints = new GridBagConstraints();
        groupConstraints.gridwidth = 2;
        groupConstraints.gridx = 0;
        groupConstraints.gridy = 0;
        groupConstraints.ipady = 5;
        groupConstraints.insets.set(2, 2, 2, 2);
        groupConstraints.fill = GridBagConstraints.BOTH;
        groupConstraints.anchor = GridBagConstraints.NORTHWEST;

        // Register this group in focus-order map immediately (preserves column ordering).
        if (groupLabelText != null) {
            this.groupFocusComponents.putIfAbsent(groupLabelText, new ArrayList<>());
        }

        if (groupLabelText != null) {
            groupConstraints.gridwidth = 4;
            String translatedGroupLabelText = groupLabelText;

            if (this.translateGroupLabels) {
                translatedGroupLabelText = this.language.translate(groupLabelText);
            }

            JLabel groupLabel = new JLabel(translatedGroupLabelText);
            groupLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            groupPanel.add(groupLabel, groupConstraints);
            groupConstraints.gridwidth = 2;
            groupConstraints.gridy++;
        }

        if (this.useWeightings.get(column).get(groupLabelText)) {
            weightingElement = this.weightingService.createWeighting(
                groupLabelText,
                this.getName()
            );
            groupConstraints.gridwidth = 4;
            groupPanel.add(weightingElement, constraints);
            groupConstraints.gridwidth = 2;
            groupConstraints.gridy++;
        }

        // Capture the row immediately after the header/weighting, before any elements are added.
        // This is the correct base for dynamic-row insertion after clearDynamicRows().
        int headerEndRow = groupConstraints.gridy;

        for (Map.Entry<String, ElementType> entry : elements.entrySet()) {
            BaseTranslatedEntity selected = null;
            if (this.selectedComboBoxValues.containsKey(entry.getKey())) {
                selected = this.selectedComboBoxValues.get(entry.getKey());
            }

            this.createElement(groupPanel, groupConstraints, groupLabelText, entry.getKey(), entry.getValue(), selected);
        }

        if (groupLabelText != null) {
            this.groupPanels.put(groupLabelText, groupPanel);
            this.groupNextDynamicRow.put(groupLabelText, groupConstraints.gridy);
            this.groupBaseDynamicRow.put(groupLabelText, headerEndRow);
        }

        if (this.useFreeAdditionalPoints.get(column).get(groupLabelText)) {
            JTextField freeAdditionPointsField = new JTextField();
            JTextField maxFreeAdditionPointsField = new JTextField();

            Dimension pointsDimension = new Dimension(36, 20);
            freeAdditionPointsField.setEnabled(false);
            freeAdditionPointsField.setPreferredSize(pointsDimension);
            freeAdditionPointsField.setMinimumSize(pointsDimension);
            freeAdditionPointsField.setMaximumSize(pointsDimension);
            freeAdditionPointsField.setText("0");
            maxFreeAdditionPointsField.setEnabled(false);
            maxFreeAdditionPointsField.setPreferredSize(pointsDimension);
            maxFreeAdditionPointsField.setMinimumSize(pointsDimension);
            maxFreeAdditionPointsField.setMaximumSize(pointsDimension);
            maxFreeAdditionPointsField.setText(
                Integer.toString(this.fetchMaxFreeAdditionalPoints(groupLabelText, weightingElement))
            );

            FreeAdditionalPointsFields additionalPointsFields = new FreeAdditionalPointsFields(
                freeAdditionPointsField,
                maxFreeAdditionPointsField
            );
            this.freeAdditionalPointsElements.put(groupLabelText, additionalPointsFields);

            groupConstraints.gridwidth = 1;
            groupConstraints.gridx = 1;
            groupConstraints.anchor = GridBagConstraints.LINE_END;
            groupPanel.add(freeAdditionPointsField, groupConstraints);
            groupConstraints.gridx = 2;
            groupConstraints.anchor = GridBagConstraints.LINE_START;
            groupPanel.add(maxFreeAdditionPointsField, groupConstraints);

            groupConstraints.gridwidth = 2;
            groupConstraints.gridx = 0;
            groupConstraints.gridy++;
        }

        this.add(groupPanel, constraints);
    }

    private void createElement(
        JPanel groupPanel,
        GridBagConstraints groupConstraints,
        String groupLabel,
        String label,
        ElementType elementType,
        BaseTranslatedEntity selected
    ) throws TypeNotSupportedException {
        this.createElementLabel(groupPanel, groupConstraints, groupLabel, label, selected);

        Component element;
        switch (elementType) {
            case TEXT:
                element = new JTextField();
                break;
            case SPINNER:
                element = new JSpinner();
                ((JSpinner) element).setModel(
                    new SpinnerNumberModel(
                        0,
                        0,
                        this.spinnerMaximum.get(label).intValue(),
                        1
                    )
                );
                break;
            default:
                throw new TypeNotSupportedException(elementType.name());
        }

        element.setName(label);
        groupPanel.add(element, groupConstraints);
        this.groupFocusComponents.computeIfAbsent(groupLabel, k -> new ArrayList<>()).add(element);

        if (elementType == ElementType.SPINNER && groupLabel != null) {
            this.groupSpinners.computeIfAbsent(groupLabel, k -> new HashMap<>()).put(label, (JSpinner) element);
            ((JSpinner) element).addChangeListener(e -> this.updateFreeAdditionalPoints(groupLabel));
        }

        groupConstraints.gridx = 0;
        groupConstraints.gridy++;
    }

    private void createElementLabel(
        JPanel groupPanel,
        GridBagConstraints groupConstraints,
        String groupLabel,
        String label,
        BaseTranslatedEntity selected
    ) {
        if (label.isEmpty()) {
            return;
        }

        if (!this.editableElementLabels.get(label)) {
            JLabel elementLabel = new JLabel(label);
            groupPanel.add(elementLabel, groupConstraints);
        } else {
            JComboBox<BaseTranslatedEntity> comboBoxLabel = new JComboBox<>();
            DefaultComboBoxModel<BaseTranslatedEntity> model = new DefaultComboBoxModel<>();

            if (selected == null) {
                model.addElement(
                    ((EmptyEntityStorage) StorageFactory.getStorage(StorageFactory.StorageType.EMPTY)).getEntity()
                );
            }

            this.getComboBoxLabelValues(label).forEach((key, value) -> model.addElement(value));
            comboBoxLabel.setModel(model);

            if (selected != null) {
                comboBoxLabel.setSelectedItem(selected);
            }

            if (groupLabel != null) {
                this.comboBoxes.computeIfAbsent(groupLabel, k -> new HashMap<>()).put(label, comboBoxLabel);
            }

            comboBoxLabel.putClientProperty("dynamicRowAdded", false);
            comboBoxLabel.addActionListener(e -> this.onEditableComboBoxSelected(groupLabel, comboBoxLabel));

            groupPanel.add(comboBoxLabel, groupConstraints);
            if (groupLabel != null) {
                this.groupFocusComponents.computeIfAbsent(groupLabel, k -> new ArrayList<>()).add(comboBoxLabel);
            }
        }

        groupConstraints.gridx += 2;
    }

    protected HashMap<String, JComboBox<BaseTranslatedEntity>> getComboBoxesForGroup(String groupLabel)
    {
        return this.comboBoxes.getOrDefault(groupLabel, new HashMap<>());
    }

    protected HashMap<String, JSpinner> getSpinnersForGroup(String groupLabel)
    {
        return new HashMap<>(this.groupSpinners.getOrDefault(groupLabel, new HashMap<>()));
    }

    protected FreeAdditionalPointsFields getFreeAdditionalPointsElementsForGroup(String groupLabel)
    {
        return this.freeAdditionalPointsElements.get(groupLabel);
    }

    protected void updateFreeAdditionalPoints(String groupLabel)
    {
        if (!this.freeAdditionalPointsElements.containsKey(groupLabel)) {
            return;
        }

        int total = this.getUsedGroupSpinnerSum(groupLabel);
        int max = this.getGroupMax(groupLabel);
        int displayed = Math.min(total, max);

        this.freeAdditionalPointsElements.get(groupLabel)
            .getFreeAdditionalPointsField()
            .setText(Integer.toString(displayed));

        this.dispatchUpdateFreeAdditionalPointsEvent(
            new UpdateFreeAdditionalPointsEvent(groupLabel, this.getName(), Math.max(0, total - max))
        );

        this.afterFreeAdditionalPointsUpdated(groupLabel);
    }

    protected void dispatchUpdateFreeAdditionalPointsEvent(UpdateFreeAdditionalPointsEvent event)
    {
        VampireEditor.getDispatcher().dispatch(event);
    }

    /**
     * Hook called after free additional points are recalculated for a group.
     * Subclasses can override to react (e.g. enable/disable navigation buttons).
     *
     * @param groupLabel The group whose points were updated
     */
    protected void afterFreeAdditionalPointsUpdated(String groupLabel)
    {
    }

    /**
     * Appends a component to the end of the focus traversal order.
     * Call from an overridden {@link #createFocusTraversalPolicy()} before calling super.
     *
     * @param component The component to add
     */
    protected void addToFocusTraversalOrder(Component component)
    {
        if (!this.extraFocusOrder.contains(component)) {
            this.extraFocusOrder.add(component);
        }
    }

    protected int getGroupSpinnerSum(String groupLabel)
    {
        return this.groupSpinners.getOrDefault(groupLabel, new HashMap<>())
            .values().stream()
            .mapToInt(spinner -> (int) spinner.getValue())
            .sum();
    }

    protected int getUsedGroupSpinnerSum(String groupLabel)
    {
        return this.getGroupSpinnerSum(groupLabel);
    }

    protected int getGroupMax(String groupLabel)
    {
        FreeAdditionalPointsFields fields = this.freeAdditionalPointsElements.get(groupLabel);
        if (fields == null) {
            return 0;
        }
        try {
            return Integer.parseInt(fields.getMaxFreeAdditionalPointsField().getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Called when an editable combo box label has a selection made. Override in subclasses to react.
     *
     * @param groupLabel The label of the group containing the combo box
     * @param comboBox   The combo box that was changed
     */
    protected void onEditableComboBoxSelected(String groupLabel, JComboBox<BaseTranslatedEntity> comboBox)
    {
    }

    /**
     * Dynamically adds a new row (combo box + spinner) to an existing group, shifting any free-additional-points
     * fields down to make room.
     *
     * @param groupLabel    The group to add the row to
     * @param uniqueLabel   A unique key for the new row's elements
     * @param comboBoxType  The type key passed to {@link #getComboBoxLabelValues} to populate the combo box
     * @param spinnerMax    Maximum value for the spinner
     */
    protected JComboBox<BaseTranslatedEntity> addDynamicRow(
        String groupLabel, String uniqueLabel, String comboBoxType, int spinnerMax)
    {
        JPanel groupPanel = this.groupPanels.get(groupLabel);
        if (groupPanel == null) {
            return null;
        }

        int insertRow = this.groupNextDynamicRow.getOrDefault(groupLabel, 0);
        GridBagLayout groupLayout = (GridBagLayout) groupPanel.getLayout();

        if (this.freeAdditionalPointsElements.containsKey(groupLabel)) {
            FreeAdditionalPointsFields fields = this.freeAdditionalPointsElements.get(groupLabel);

            GridBagConstraints newFreeConstraints = new GridBagConstraints();
            newFreeConstraints.gridwidth = 1;
            newFreeConstraints.gridx = 1;
            newFreeConstraints.gridy = insertRow + 1;
            newFreeConstraints.ipady = 5;
            newFreeConstraints.insets = new Insets(2, 2, 2, 2);
            newFreeConstraints.fill = GridBagConstraints.BOTH;
            newFreeConstraints.anchor = GridBagConstraints.LINE_END;
            groupLayout.setConstraints(fields.getFreeAdditionalPointsField(), newFreeConstraints);

            GridBagConstraints newMaxConstraints = new GridBagConstraints();
            newMaxConstraints.gridwidth = 1;
            newMaxConstraints.gridx = 2;
            newMaxConstraints.gridy = insertRow + 1;
            newMaxConstraints.ipady = 5;
            newMaxConstraints.insets = new Insets(2, 2, 2, 2);
            newMaxConstraints.fill = GridBagConstraints.BOTH;
            newMaxConstraints.anchor = GridBagConstraints.LINE_START;
            groupLayout.setConstraints(fields.getMaxFreeAdditionalPointsField(), newMaxConstraints);
        }

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridwidth = 2;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = insertRow;
        labelConstraints.ipady = 5;
        labelConstraints.insets = new Insets(2, 2, 2, 2);
        labelConstraints.fill = GridBagConstraints.BOTH;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;

        JComboBox<BaseTranslatedEntity> comboBoxLabel = new JComboBox<>();
        DefaultComboBoxModel<BaseTranslatedEntity> model = new DefaultComboBoxModel<>();
        model.addElement(
            ((EmptyEntityStorage) StorageFactory.getStorage(StorageFactory.StorageType.EMPTY)).getEntity()
        );
        this.getComboBoxLabelValues(comboBoxType).forEach((key, value) -> model.addElement(value));
        comboBoxLabel.setModel(model);
        comboBoxLabel.putClientProperty("dynamicRowAdded", false);
        comboBoxLabel.addActionListener(e -> this.onEditableComboBoxSelected(groupLabel, comboBoxLabel));
        groupPanel.add(comboBoxLabel, labelConstraints);
        this.groupFocusComponents.computeIfAbsent(groupLabel, k -> new ArrayList<>()).add(comboBoxLabel);

        this.comboBoxes.computeIfAbsent(groupLabel, k -> new HashMap<>()).put(uniqueLabel, comboBoxLabel);
        this.editableElementLabels.put(uniqueLabel, true);
        this.selectedComboBoxValues.put(uniqueLabel, null);
        this.spinnerMaximum.put(uniqueLabel, spinnerMax);

        GridBagConstraints spinnerConstraints = new GridBagConstraints();
        spinnerConstraints.gridwidth = 2;
        spinnerConstraints.gridx = 2;
        spinnerConstraints.gridy = insertRow;
        spinnerConstraints.ipady = 5;
        spinnerConstraints.insets = new Insets(2, 2, 2, 2);
        spinnerConstraints.fill = GridBagConstraints.BOTH;
        spinnerConstraints.anchor = GridBagConstraints.NORTHWEST;

        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(0, 0, spinnerMax, 1));
        spinner.setName(uniqueLabel);
        groupPanel.add(spinner, spinnerConstraints);
        this.groupFocusComponents.computeIfAbsent(groupLabel, k -> new ArrayList<>()).add(spinner);
        this.groupSpinners.computeIfAbsent(groupLabel, k -> new HashMap<>()).put(uniqueLabel, spinner);
        spinner.addChangeListener(e -> this.updateFreeAdditionalPoints(groupLabel));

        this.groupNextDynamicRow.put(groupLabel, insertRow + 1);

        groupPanel.revalidate();
        groupPanel.repaint();
        this.revalidate();
        this.repaint();

        this.createFocusTraversalPolicy();

        return comboBoxLabel;
    }

    /**
     * Adds a new row with a pre-selected item, marking it so no additional empty row is spawned.
     *
     * @param groupLabel    The group to add the row to
     * @param uniqueLabel   A unique key for the new row's elements
     * @param comboBoxType  The type key passed to {@link #getComboBoxLabelValues} to populate the combo box
     * @param spinnerMax    Maximum value for the spinner
     * @param preSelected   Entity to pre-select in the combo box (may be null)
     */
    protected JComboBox<BaseTranslatedEntity> addDynamicRow(
        String groupLabel, String uniqueLabel, String comboBoxType, int spinnerMax,
        BaseTranslatedEntity preSelected)
    {
        JComboBox<BaseTranslatedEntity> comboBox =
            this.addDynamicRow(groupLabel, uniqueLabel, comboBoxType, spinnerMax);
        if (comboBox == null) {
            return null;
        }
        if (preSelected != null) {
            comboBox.putClientProperty("dynamicRowAdded", true);
            comboBox.setSelectedItem(preSelected);
        }
        return comboBox;
    }

    /**
     * Dynamically adds a new row with a static label and spinner to an existing group, shifting any
     * free-additional-points fields down to make room.
     *
     * @param groupLabel  The group to add the row to
     * @param uniqueLabel A unique key for the spinner in {@code groupSpinners}
     * @param rowLabel    The text to display in the row label
     * @param spinnerMax  Maximum value for the spinner
     */
    protected void addDynamicLabelSpinnerRow(String groupLabel, String uniqueLabel, String rowLabel, int spinnerMax)
    {
        JPanel groupPanel = this.groupPanels.get(groupLabel);
        if (groupPanel == null) {
            return;
        }

        int insertRow = this.groupNextDynamicRow.getOrDefault(groupLabel, 0);
        GridBagLayout groupLayout = (GridBagLayout) groupPanel.getLayout();

        if (this.freeAdditionalPointsElements.containsKey(groupLabel)) {
            FreeAdditionalPointsFields fields = this.freeAdditionalPointsElements.get(groupLabel);

            GridBagConstraints newFreeConstraints = new GridBagConstraints();
            newFreeConstraints.gridwidth = 1;
            newFreeConstraints.gridx = 1;
            newFreeConstraints.gridy = insertRow + 1;
            newFreeConstraints.ipady = 5;
            newFreeConstraints.insets = new Insets(2, 2, 2, 2);
            newFreeConstraints.fill = GridBagConstraints.BOTH;
            newFreeConstraints.anchor = GridBagConstraints.LINE_END;
            groupLayout.setConstraints(fields.getFreeAdditionalPointsField(), newFreeConstraints);

            GridBagConstraints newMaxConstraints = new GridBagConstraints();
            newMaxConstraints.gridwidth = 1;
            newMaxConstraints.gridx = 2;
            newMaxConstraints.gridy = insertRow + 1;
            newMaxConstraints.ipady = 5;
            newMaxConstraints.insets = new Insets(2, 2, 2, 2);
            newMaxConstraints.fill = GridBagConstraints.BOTH;
            newMaxConstraints.anchor = GridBagConstraints.LINE_START;
            groupLayout.setConstraints(fields.getMaxFreeAdditionalPointsField(), newMaxConstraints);
        }

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridwidth = 2;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = insertRow;
        labelConstraints.ipady = 5;
        labelConstraints.insets = new Insets(2, 2, 2, 2);
        labelConstraints.fill = GridBagConstraints.BOTH;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;
        groupPanel.add(new JLabel(rowLabel), labelConstraints);

        GridBagConstraints spinnerConstraints = new GridBagConstraints();
        spinnerConstraints.gridwidth = 2;
        spinnerConstraints.gridx = 2;
        spinnerConstraints.gridy = insertRow;
        spinnerConstraints.ipady = 5;
        spinnerConstraints.insets = new Insets(2, 2, 2, 2);
        spinnerConstraints.fill = GridBagConstraints.BOTH;
        spinnerConstraints.anchor = GridBagConstraints.NORTHWEST;

        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(0, 0, spinnerMax, 1));
        spinner.setName(uniqueLabel);
        groupPanel.add(spinner, spinnerConstraints);
        this.groupFocusComponents.computeIfAbsent(groupLabel, k -> new ArrayList<>()).add(spinner);
        this.groupSpinners.computeIfAbsent(groupLabel, k -> new HashMap<>()).put(uniqueLabel, spinner);
        spinner.addChangeListener(e -> this.updateFreeAdditionalPoints(groupLabel));

        this.groupNextDynamicRow.put(groupLabel, insertRow + 1);

        groupPanel.revalidate();
        groupPanel.repaint();
        this.revalidate();
        this.repaint();

        this.createFocusTraversalPolicy();
    }

    /**
     * Removes all dynamically added rows from the given group and resets the group's tracking state
     * back to what it was immediately after {@code build()}.
     * Static rows (those added via {@code addRow()} before {@code build()}) are also removed.
     * The group-label {@code JLabel} and the free-additional-points fields are preserved.
     *
     * @param groupLabel The group to clear
     */
    protected void clearDynamicRows(String groupLabel)
    {
        JPanel groupPanel = this.groupPanels.get(groupLabel);
        if (groupPanel == null) {
            return;
        }

        FreeAdditionalPointsFields fields = this.freeAdditionalPointsElements.get(groupLabel);

        Set<Component> toKeep = new HashSet<>();
        if (fields != null) {
            toKeep.add(fields.getFreeAdditionalPointsField());
            toKeep.add(fields.getMaxFreeAdditionalPointsField());
        }
        for (Component c : groupPanel.getComponents()) {
            if (c instanceof JLabel) {
                toKeep.add(c);
                break;
            }
        }

        // Clean up per-label tracking maps for every combo box belonging to this group.
        HashMap<String, JComboBox<BaseTranslatedEntity>> groupComboBoxes = this.comboBoxes.get(groupLabel);
        if (groupComboBoxes != null) {
            for (String label : groupComboBoxes.keySet()) {
                this.editableElementLabels.remove(label);
                this.selectedComboBoxValues.remove(label);
                this.spinnerMaximum.remove(label);
            }
            groupComboBoxes.clear();
        }

        // Remove all focus-traversal components belonging to this group (combo boxes and spinners).
        HashMap<String, JSpinner> groupSpinnersMap = this.groupSpinners.get(groupLabel);
        if (groupSpinnersMap != null) {
            groupSpinnersMap.clear();
        }
        List<Component> groupComponents = this.groupFocusComponents.get(groupLabel);
        if (groupComponents != null) {
            groupComponents.clear();
        }

        // Collect and remove all non-preserved components.
        List<Component> toRemove = new ArrayList<>();
        for (Component c : groupPanel.getComponents()) {
            if (!toKeep.contains(c)) {
                toRemove.add(c);
            }
        }
        toRemove.forEach(groupPanel::remove);

        // Reset the dynamic-row insertion pointer to the base position.
        int baseRow = this.groupBaseDynamicRow.getOrDefault(groupLabel, 0);
        this.groupNextDynamicRow.put(groupLabel, baseRow);

        // Move the free-points fields back to the base row.
        if (fields != null) {
            GridBagLayout groupLayout = (GridBagLayout) groupPanel.getLayout();

            GridBagConstraints freeConstraints = new GridBagConstraints();
            freeConstraints.gridwidth = 1;
            freeConstraints.gridx = 1;
            freeConstraints.gridy = baseRow;
            freeConstraints.ipady = 5;
            freeConstraints.insets = new Insets(2, 2, 2, 2);
            freeConstraints.fill = GridBagConstraints.BOTH;
            freeConstraints.anchor = GridBagConstraints.LINE_END;
            groupLayout.setConstraints(fields.getFreeAdditionalPointsField(), freeConstraints);

            GridBagConstraints maxConstraints = new GridBagConstraints();
            maxConstraints.gridwidth = 1;
            maxConstraints.gridx = 2;
            maxConstraints.gridy = baseRow;
            maxConstraints.ipady = 5;
            maxConstraints.insets = new Insets(2, 2, 2, 2);
            maxConstraints.fill = GridBagConstraints.BOTH;
            maxConstraints.anchor = GridBagConstraints.LINE_START;
            groupLayout.setConstraints(fields.getMaxFreeAdditionalPointsField(), maxConstraints);
        }

        this.createFocusTraversalPolicy();

        groupPanel.revalidate();
        groupPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    public enum ElementType
    {
        TEXT,
        SPINNER
    }
}
