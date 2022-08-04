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
package antafes.vampireEditor.gui.element;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * A combo box with independent size for the popup.
 *
 * @author Marian Pollzien
 */
public class WideComboBox<E> extends JComboBox<E> {

    /**
     * Create a new wide combo box.
     */
    public WideComboBox() {
    }

    /**
     * Creates a WideComboBox that contains the elements in the specified array. By default the first item in the array
     * (and therefore the data model) becomes selected.
     *
     * @param items An array of objects to insert into the combo box
     * @see DefaultComboBoxModel
     */
    public WideComboBox(final E[] items){
        super(items);
    }

    /**
     * Creates a WideComboBox that contains the elements in the specified Vector. By default the first item in the
     * vector (and therefore the data model) becomes selected.
     *
     * @param items An array of vectors to insert into the combo box
     * @see DefaultComboBoxModel
     */
    public WideComboBox(Vector items) {
        super(items);
    }

    /**
     * Creates a WideComboBox that takes its items from an existing ComboBoxModel. Since the ComboBoxModel is provided,
     * a combo box created using this constructor does not create a default combo box model and may impact how the
     * insert, remove and add methods behave.
     *
     * @param aModel The ComboBoxModel that provides the displayed list of items
     * @see DefaultComboBoxModel
     */
    public WideComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    private boolean layingOut = false;

    /**
     * Causes this container to lay out its components. Most programs should not call this method directly, but should
     * invoke the validate method instead.
     *
     * @see LayoutManager#layoutContainer
     * @see #setLayout
     * @see #validate
     * @since JDK1.1
     */
    @Override
    public void doLayout(){
        try{
            layingOut = true;
            super.doLayout();
        }finally{
            layingOut = false;
        }
    }

    /**
     * Returns the size of this component in the form of a Dimension object. The height field of the Dimension object
     * contains this component's height, and the width field of the Dimension object contains this component's width.
     *
     * @return A Dimension object that indicates the size of this component
     * @see #setSize
     * @since JDK1.1
     */
    @Override
    public Dimension getSize(){
        Dimension dim = super.getSize();
        if(!layingOut)
            dim.width = Math.max(dim.width, getPreferredSize().width);
        return dim;
    }
}
