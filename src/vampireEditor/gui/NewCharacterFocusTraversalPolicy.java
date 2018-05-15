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
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Vector;

/**
 *
 * @author Marian Pollzien
 */
class NewCharacterFocusTraversalPolicy extends FocusTraversalPolicy
{
    /**
     * The order in which the components will gain focus.
     */
    private final Vector<Component> order;

    /**
     * Create a new focus traversal policy.
     *
     * @param order
     */
    public NewCharacterFocusTraversalPolicy(Vector<Component> order) {
        this.order = new Vector<>(order.size());
        this.order.addAll(order);
    }

    /**
     * Get the next component after the given one.
     *
     * @param focusCycleRoot
     * @param aComponent
     *
     * @return
     */
    @Override
    public Component getComponentAfter(
        Container focusCycleRoot, Component aComponent
    ) {
        int idx = (order.indexOf(aComponent) + 1) % order.size();
        return order.get(idx);
    }

    /**
     * Get the previous component of the given one.
     *
     * @param focusCycleRoot
     * @param aComponent
     *
     * @return
     */
    @Override
    public Component getComponentBefore(
        Container focusCycleRoot, Component aComponent
    ) {
        int idx = order.indexOf(aComponent) - 1;
        if (idx < 0) {
            idx = order.size() - 1;
        }
        return order.get(idx);
    }

    /**
     * Get the component that will gain focus by default.
     *
     * @param focusCycleRoot
     *
     * @return
     */
    @Override
    public Component getDefaultComponent(Container focusCycleRoot) {
        return order.get(0);
    }

    /**
     * Get the last component in the list.
     *
     * @param focusCycleRoot
     *
     * @return
     */
    @Override
    public Component getLastComponent(Container focusCycleRoot) {
        return order.lastElement();
    }

    /**
     * Get the first component in the list.
     *
     * @param focusCycleRoot
     *
     * @return
     */
    @Override
    public Component getFirstComponent(Container focusCycleRoot) {
        return order.get(0);
    }
}
