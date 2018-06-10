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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JComponent;
import javax.swing.JSpinner;
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
        this.addFields(headline, true, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline
     * @param addHeadline
     * @param elementList
     */
    @Override
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList) {
        this.addFields(headline, addHeadline, elementList, 0);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     * This will use 0 as minimum value for the spinners.
     *
     * @param headline
     * @param spinnerMinimum
     * @param elementList
     */
    protected void addFields(String headline, ArrayList<String> elementList, int spinnerMinimum) {
        this.addFields(headline, true, elementList, spinnerMinimum);
    }

    /**
     * Add labels and spinners by the given list and under the given headline.
     *
     * @param headline
     * @param addHeadline
     * @param elementList
     * @param spinnerMinimum
     */
    protected void addFields(String headline, boolean addHeadline, ArrayList<String> elementList, int spinnerMinimum) {
        LinkedHashMap<String, JComponent> elements = new LinkedHashMap<>();

        elementList.forEach((string) -> {
            JSpinner spinner = new JSpinner();
            spinner.setModel(new SpinnerNumberModel(spinnerMinimum, spinnerMinimum, 10, 1));
            Dimension spinnerDimension = new Dimension(36, 20);
            spinner.setSize(spinnerDimension);
            spinner.setName(string);
            this.addChangeListener(spinner);

            elements.put(string, spinner);
        });

        this.addFields(headline, addHeadline, elements);
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
