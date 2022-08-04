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
package antafes.vampireEditor.gui.utility;

import antafes.vampireEditor.Configuration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Marian Pollzien
 */
@Getter
public enum Weighting {
    PRIMARY (7, 13),
    SECONDARY (5, 9),
    TERTIARY (3, 5);

    private final int attributeMax;
    private final int abilitiesMax;

    Weighting(int attributeMax, int abilitiesMax) {
        this.attributeMax = attributeMax;
        this.abilitiesMax = abilitiesMax;
    }

    /**
     * Get a string representation of the weighting.
     *
     * @return A string representation of the weighting
     */
    @Override
    public String toString() {
        Configuration configuration = Configuration.getInstance();

        return configuration.getLanguageObject().translate(this.name());
    }

    /**
     * Returns the remaining weighting after removing both given entries from the list.
     *
     * @param first First weighting enum to remove
     * @param second Second weighting enum to remove
     *
     * @return Remaining weighting enum
     */
    public static Weighting getRemaining(Weighting first, Weighting second) {
        ArrayList<Weighting> weightings = new ArrayList<>(Arrays.asList(Weighting.values()));
        weightings.remove(first);
        weightings.remove(second);

        return weightings.get(0);
    }
}
