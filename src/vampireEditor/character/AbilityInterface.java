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
 * Skill interface.
 *
 * @author Marian Pollzien
 */
public interface AbilityInterface {
    /**
     * List of ability types.
     */
    public static enum AbilityType {
        TALENT,
        SKILL,
        KNOWLEDGE;

        private ArrayList<String> abilities;

        /**
         * Create a new AbilityType enum.
         */
        private AbilityType() {
            this.fillAbilities();
        }

        /**
         * Fill all available abilities.
         */
        private void fillAbilities() {
            String[] elements;

            switch (this.name()) {
                case "TALENT":
                    elements = new String[] {"acting", "alertness", "athletics", "brawl", "burglary", "dodge", "empathy", "intimidation", "leadership", "subterfuge"};
                    break;
                case "SKILL":
                    elements = new String[] {"animalKen", "archery", "crafts", "etiquette", "herbalism", "melee", "music", "ride", "stealth", "survival"};
                    break;
                case "KNOWLEDGE":
                default:
                    elements = new String[] {"academicKnowledge", "administration", "folklore", "investigation", "law", "linguistics", "medicine", "occult", "politics", "science"};
                    break;
            }

            this.abilities = new ArrayList<>(Arrays.asList(elements));
        }

        /**
         * Get the list of abilities.
         *
         * @return
         */
        public ArrayList<String> getAbilities() {
            return abilities;
        }
    }

    /**
     * Get the name of the ability.
     *
     * @return
     */
    public String getName();

    /**
     * Get the type of ability.
     *
     * @return
     */
    public AbilityType getType();

    /**
     * Get the value of the ability.
     *
     * @return
     */
    public int getValue();

    /**
     * Set the current value of the ability.
     *
     * @param value
     */
    public void setValue(int value);
}
