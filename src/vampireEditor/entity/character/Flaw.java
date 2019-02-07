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
package vampireEditor.entity.character;

/**
 * Flaw object.
 *
 * @author Marian Pollzien
 */
public class Flaw extends SpecialFeature {
    /**
     * Flaw constructor
     *
     * @param builder The builder object
     */
    public Flaw(Builder builder) {
        super(builder);
    }

    /**
     * Returns a string representation of the flaw.
     *
     * @return A string representation of the flaw
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
