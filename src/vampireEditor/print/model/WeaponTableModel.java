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
 * @copyright (c) $year, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package vampireEditor.print.model;

import vampireEditor.Configuration;
import vampireEditor.language.LanguageInterface;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class WeaponTableModel implements TableModel {
    private LanguageInterface language;
    private String[] columnNames;

    /**
     * Constructor
     */
    public WeaponTableModel() {
        Configuration configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();

        this.columnNames = new String[]{
            this.language.translate("weapon/attack"),
            this.language.translate("diff."),
            this.language.translate("damage"),
            this.language.translate("range"),
            this.language.translate("rate"),
            this.language.translate("ammo"),
            this.language.translate("conceal")
        };
    }

    /**
     * Get the amount of rows.
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return 7;
    }

    /**
     * Get the amount of columns.
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return 7;
    }

    /**
     * Get the names of the column.
     *
     * @param columnIndex The column
     *
     * @return Name of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    /**
     * Class to use for columns.
     *
     * @param columnIndex The column
     *
     * @return TableColumn class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return TableColumn.class;
    }

    /**
     * Whether the cell should be editable. Returns always false.
     *
     * @param rowIndex The row where to find the cell
     * @param columnIndex The column where to find the cell
     *
     * @return Always false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Get the value of a cell.
     *
     * @param rowIndex The row where to find the cell
     * @param columnIndex The column where to find the cell
     *
     * @return The column name for the first row, otherwise null
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) {
            return columnNames[columnIndex];
        }

        return null;
    }

    /**
     * Setter for values. Unused!
     *
     * @param aValue The value to set
     * @param rowIndex The row where to find the cell
     * @param columnIndex The column where to find the cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    /**
     * Adds a listener to the list that is notified each time a change
     * to the data model occurs.
     * Unused!
     *
     * @param l The TableModelListener
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    /**
     * Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     * Unused!
     *
     * @param l The TableModelListener
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
