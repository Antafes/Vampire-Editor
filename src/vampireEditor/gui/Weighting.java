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

import java.util.ArrayList;
import java.util.Arrays;
import vampireEditor.Configuration;

/**
 *
 * @author Marian Pollzien
 */
public enum Weighting {
    PRIMARY (7, 13),
    SECONDARY (5, 9),
    TERTIARY (3, 5);

    private final int attributeMax;
    private final int abilitiesMax;

    private Weighting(int attributeMax, int abilitiesMax) {
        this.attributeMax = attributeMax;
        this.abilitiesMax = abilitiesMax;
    }

    /**
     * Get a string representation of the weighting.
     *
     * @return
     */
    @Override
    public String toString() {
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        return configuration.getLanguageObject().translate(this.name());
    }

    /**
     * Get the maximum attribute points available for the weighting.
     *
     * @return
     */
    public int getAttributeMax() {
        return attributeMax;
    }

    /**
     * Get the maximum ability points available for the weighting.
     *
     * @return
     */
    public int getAbilitiesMax() {
        return abilitiesMax;
    }

    /**
     * Returns the remaining weighting after removing both given entries from
     * the list.
     *
     * @param first
     * @param second
     *
     * @return
     */
    public static Weighting getRemaining(Weighting first, Weighting second) {
        ArrayList<Weighting> weightings = new ArrayList<>(Arrays.asList(Weighting.values()));
        weightings.remove(first);
        weightings.remove(second);

        return weightings.get(0);
    }
}
