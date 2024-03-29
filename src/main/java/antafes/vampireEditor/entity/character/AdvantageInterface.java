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
package antafes.vampireEditor.entity.character;

/**
 * Advantage interface.
 *
 * @author Marian Pollzien
 */
public interface AdvantageInterface {
    /**
     * List of advantage types.
     */
    public enum AdvantageType implements EntityTypeInterface {
        BACKGROUND,
        DISCIPLINE,
        VIRTUE
    }

    /**
     * Get the key for the advantage.
     *
     * @return
     */
    public String getKey();

    /**
     * Get the name of the advantage.
     *
     * @return
     */
    public String getName();

    /**
     * Get the type of advantage.
     *
     * @return
     */
    public AdvantageType getType();

    /**
     * Get the value of the advantage.
     *
     * @return
     */
    public int getValue();
}
