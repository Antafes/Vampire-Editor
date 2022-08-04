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
package antafes.vampireEditor.print.utility;

/**
 * Enum for the dot images on the character sheet.
 *
 * @author Marian Pollzien
 */
public enum Dot {
    CIRCLE ("images/circleEmpty.png", "images/circleFilled.png"),
    SQUARE ("images/squareEmpty.png", "images/squareFilled.png");

    private final String empty;
    private final String filled;

    Dot(String empty, String filled) {
        this.empty = empty;
        this.filled = filled;
    }

    /**
     * Get the empty dot path.
     *
     * @return
     */
    public String getEmpty() {
        return this.empty;
    }

    /**
     * Get the filled dot path.
     *
     * @return
     */
    public String getFilled() {
        return this.filled;
    }
}
