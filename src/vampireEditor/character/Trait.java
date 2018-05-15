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
package vampireEditor.character;

/**
 * Trait object.
 *
 * @author Marian Pollzien
 */
public class Trait implements TraitInterface {
    private String name;
    private int value;

    /**
     * Create a new trait object.
     *
     * @param name
     * @param value
     */
    public Trait(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name of the trait.
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the value of the trait.
     *
     * @return
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Set the current value of the trait.
     *
     * @param value
     */
    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
