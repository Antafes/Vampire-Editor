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
package vampireEditor.utility;

import java.util.Comparator;
import vampireEditor.entity.character.Clan;

/**
 *
 * @author Marian Pollzien
 */
public class ClanComparator implements Comparator {

    /**
     * Compare two clans by name.
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    @Override
    public int compare(Object o1, Object o2) {
        Clan c1 = (Clan) o1;
        Clan c2 = (Clan) o2;

        return c1.getName().compareToIgnoreCase(c2.getName());
    }
}
