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
package vampireEditor.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import vampireEditor.Configuration;

/**
 *
 * @author Marian Pollzien
 */
abstract public class BaseListPanel extends BasePanel {

    /**
     * Creates new form AbilitiesPanel
     *
     * @param configuration
     */
    public BaseListPanel(Configuration configuration) {
        super(configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        super.init();
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline
     * @param elementList
     */
    @Override
    protected void addFields(String headline, ArrayList<String> elementList) {
        this.addFields(headline, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param elementList
     * @param spinnerMinimum
     */
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum) {
        if (!this.getFields().containsKey(headline)) {
            this.getFields().put(headline, new ArrayList<>());
        }

        GroupLayout layout = (GroupLayout) this.getLayout();
        JLabel groupLabel = new JLabel(this.getLanguage().translate(headline));
        groupLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GroupLayout.ParallelGroup listHorizontalGroup = layout.createParallelGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        this.getOuterSequentialHorizontalGroup()
            .addGroup(
                layout.createSequentialGroup().addGroup(listHorizontalGroup)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

        GroupLayout.SequentialGroup listVerticalGroup = layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(groupLabel)
            .addGap(11, 11, 11);
        this.getOuterParallelVerticalGroup()
            .addGroup(listVerticalGroup);

        GroupLayout.SequentialGroup outerLabelHorizontalGroup = layout.createSequentialGroup();
        outerLabelHorizontalGroup.addGap(11, 11, 11);
        GroupLayout.SequentialGroup outerElementHorizontalGroup = layout.createSequentialGroup();
        outerElementHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        GroupLayout.ParallelGroup labelHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup elementHorizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        elementList.forEach((element) -> {
            HashMap<String, GroupLayout.Group> groups = new HashMap<>();
            groups.put("labelHorizontalGroup", labelHorizontalGroup);
            groups.put("elementHorizontalGroup", elementHorizontalGroup);
            groups.put("listVerticalGroup", listVerticalGroup);
            this.addRow(element, spinnerMinimum, this.getFields(headline), groups, layout);
        });

        outerLabelHorizontalGroup.addGroup(labelHorizontalGroup);
        listHorizontalGroup.addGroup(outerLabelHorizontalGroup);
        outerElementHorizontalGroup.addGroup(elementHorizontalGroup);
        listHorizontalGroup.addGroup(outerElementHorizontalGroup);
        outerLabelHorizontalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    @Override
    protected HashMap<String, Component> addRow(
        String element, ArrayList<Component> fields, HashMap<String, GroupLayout.Group> groups, GroupLayout layout
    ) {
        return this.addRow(element, 0, fields, groups, layout);
    }

    /**
     * Add a single row to the current column.
     *
     * @param element
     * @param spinnerMinimum
     * @param fields
     * @param groups
     * @param layout
     *
     * @return
     */
    protected HashMap<String, Component> addRow(
        String element,
        int spinnerMinimum,
        ArrayList<Component> fields,
        HashMap<String, GroupLayout.Group> groups,
        GroupLayout layout
    ) {
        JLabel elementLabel = new JLabel(this.getLanguage().translate(element));
        JSpinner spinner = new JSpinner();
        elementLabel.setLabelFor(spinner);
        spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, 10, 1));
        Dimension spinnerDimension = new Dimension(36, 20);
        spinner.setPreferredSize(spinnerDimension);
        spinner.setMinimumSize(spinnerDimension);
        spinner.setMaximumSize(spinnerDimension);
        spinner.setName(element);
        this.addChangeListener(spinner);
        this.getOrder().add(spinner);
        fields.add(spinner);

        groups.get("labelHorizontalGroup").addComponent(elementLabel);
        groups.get("elementHorizontalGroup").addComponent(spinner);
        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        verticalGroup.addComponent(elementLabel)
            .addComponent(spinner);
        groups.get("listVerticalGroup").addGroup(verticalGroup)
            .addGap(6, 6, 6);

        HashMap<String, Component> elements = new HashMap<>();
        elements.put("label", elementLabel);
        elements.put("spinner", spinner);

        return elements;
    }

    /**
     * Add a change listener to the given spinner.
     *
     * @param field
     */
    protected void addChangeListener(JSpinner field) {
        ComponentChangeListener attributesListener = this.createChangeListener();
        attributesListener.setComponent(field);
        DefaultFormatter attributesFormatter = (DefaultFormatter) ((JSpinner.DefaultEditor) field.getEditor()).getTextField().getFormatter();
        attributesFormatter.setCommitsOnValidEdit(true);
        field.addChangeListener(attributesListener);
    }

    /**
     * Set the spinner field maximum value.
     *
     * @param field
     * @param maximum
     */
    protected void setFieldMaximum(JSpinner field, int maximum) {
        int value = Integer.parseInt(field.getValue().toString());
        int minimum = Integer.parseInt(
            ((SpinnerNumberModel) field.getModel()).getMinimum().toString()
        );
        field.setModel(
            new SpinnerNumberModel(
                value > maximum ? maximum : value,
                minimum,
                maximum,
                1
            )
        );
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    abstract protected ComponentChangeListener createChangeListener();

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    abstract public void setSpinnerMaximum(int maximum);
}
