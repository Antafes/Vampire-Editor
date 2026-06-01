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

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.character.Clan;

import java.util.Comparator;

/**
 *
 * @author Marian Pollzien
 */
public class ClanComparator implements Comparator<Clan> {
    private final Configuration configuration;

    public ClanComparator(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Compare two clans by name.
     *
     * @param c1 First clan
     * @param c2 Second clan
     *
     * @return A negative integer, zero, or a positive integer as the specified String is greater than, equal to, or
     *         less than this String, ignoring case considerations.
     */
    @Override
    public int compare(Clan c1, Clan c2) {
        return c1.getName(this.configuration).compareToIgnoreCase(c2.getName(this.configuration));
    }
}
