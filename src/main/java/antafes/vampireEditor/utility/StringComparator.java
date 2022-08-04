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
package antafes.vampireEditor.utility;

import java.util.Comparator;

/**
 *
 * @author Marian Pollzien
 */
public class StringComparator implements Comparator {

    /**
     * Compare two objects by name.
     *
     * @param o1 First object
     * @param o2 Second object
     *
     * @return The value {@code 0} if the argument string is equal to this string; a value less than {@code 0} if this
     *         string is lexicographically less than the string argument; and a value greater than {@code 0} if this
     *         string is lexicographically greater than the string argument.
     */
    @Override
    public int compare(Object o1, Object o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();

        return s1.compareTo(s2);
    }
}
