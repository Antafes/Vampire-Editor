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

import java.util.HashMap;
import vampireEditor.Configuration;

/**
 *
 * @author Marian Pollzien
 */
public class Weakness implements WeaknessInterface {
    private final String key;
    private final HashMap<Configuration.Language, String> names;

    /**
     * Create a new weakness object.
     *
     * @param key
     * @param names
     */
    public Weakness(String key, HashMap<Configuration.Language, String> names) {
        this.key = key;
        this.names = names;
    }

    /**
     * Get the key of the weakness.
     *
     * @return
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Get the translated name of the weakness.
     *
     * @return
     */
    @Override
    public String getName() {
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        return this.names.get(configuration.getLanguage());
    }

    /**
     * Get the list of names for the weakness.
     *
     * @return
     */
    public HashMap<Configuration.Language, String> getNames() {
        return names;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
