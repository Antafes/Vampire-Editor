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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Attribute interface.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public interface AttributeInterface {
    /**
     * List of possible attribute types.
     */
    public static enum AttributeType {
        PHYSICAL,
        SOCIAL,
        MENTAL;

        private ArrayList<String> attributes;

        /**
         * Create a new AttributeType enum.
         */
        private AttributeType() {
            this.fillAttributes();
        }

        /**
         * Fill all available attributes.
         */
        private void fillAttributes() {
            String[] elements;

            switch (this.name()) {
                case "PHYSICAL":
                    elements = new String[] {"strength", "dexterity", "stamina"};
                    break;
                case "SOCIAL":
                    elements = new String[] {"charisma", "appearance", "manipulation"};
                    break;
                case "MENTAL":
                default:
                    elements = new String[] {"intelligence", "perception", "wits"};
                    break;
            }

            this.attributes = new ArrayList<>(Arrays.asList(elements));
        }

        /**
         * Get the list of attributes.
         *
         * @return
         */
        public ArrayList<String> getAttributes() {
            return attributes;
        }

        /**
         * Get the attribute type for a given attribute name.
         *
         * @param attributeName
         *
         * @return
         */
        public static AttributeType getTypeForAttribute(String attributeName) {
            switch (attributeName) {
                case "strength":
                case "dexterity":
                case "stamina":
                    return AttributeType.PHYSICAL;
                case "charisma":
                case "appearance":
                case "manipulation":
                    return AttributeType.SOCIAL;
                case "intelligence":
                case "perception":
                case "wits":
                default:
                    return AttributeType.MENTAL;
            }
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

    /**
     * Set the current value of the attribute.
     *
     * @param value
     */
    public void setValue(int value);
}
