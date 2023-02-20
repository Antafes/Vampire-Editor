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

import lombok.Getter;

/**
 * Attribute interface.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public interface AttributeInterface {
    /**
     * List of possible attribute types.
     */
    @Getter
    public enum AttributeType implements EntityTypeInterface {
        PHYSICAL("physical", "physical"),
        SOCIAL("social", "social"),
        MENTAL("mental", "mental");

        private final String keySingular;
        private final String keyPlural;

        AttributeType(String keySingular, String keyPlural)
        {
            this.keySingular = keySingular;
            this.keyPlural = keyPlural;
        }
    }

    /**
     * Get the name of the attribute.
     *
     * @return
     */
    public String getName();

    /**
     * Get the type of attribute.
     *
     * @return
     */
    public AttributeType getType();

    /**
     * Get the current value of the attribute.
     *
     * @return
     */
    public int getValue();
}
