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
 * Road object.
 *
 * @author Marian Pollzien
 */
public class Road implements RoadInterface {
    private final String key;
    private final HashMap<Configuration.Language, String> name;
    private int value;

    /**
     * Create a new road object.
     *
     * @param key
     * @param name
     */
    public Road(String key, HashMap<Configuration.Language, String> name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Set the road value.
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get the key of the road.
     *
     * @return
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Get the name of the road.
     *
     * @return
     */
    @Override
    public String getName() {
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        if (!this.name.containsKey(configuration.getLanguage())) {
            return this.name.get(Configuration.Language.ENGLISH);
        }

        return this.name.get(configuration.getLanguage());
    }

    /**
     * Get the road value.
     *
     * @return
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get a string representation of the road.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
