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
 * Advantage object.
 *
 * @author Marian Pollzien
 */
public class Advantage implements AdvantageInterface {
    private String key;
    private HashMap<Configuration.Language, String> names;
    private AdvantageType type;
    private int value;

    /**
     * Create a new advantage object.
     *
     * @param key
     * @param name
     * @param type
     */
    public Advantage(String key, HashMap<Configuration.Language, String> name, AdvantageType type) {
        this(key, name, type, 0);
    }

    /**
     * Create a new advantage object with value.
     *
     * @param key
     * @param name
     * @param type
     * @param value
     */
    public Advantage(String key, HashMap<Configuration.Language, String> name, AdvantageType type, int value) {
        this.key = key;
        this.names = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Get the key for the advantage.
     *
     * @return
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Get the names of the advantage.
     *
     * @return
     */
    public HashMap<Configuration.Language, String> getNames() {
        return names;
    }

    /**
     * Get the name of the advantage according to the currently set language.
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
     * Get the type of advantage.
     *
     * @return
     */
    @Override
    public AdvantageType getType() {
        return type;
    }

    /**
     * Get the value of the advantage.
     *
     * @return
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Set the current value of the advantage.
     *
     * @param value
     */
    @Override
    public void setValue(int value) {
        this.value = value;
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
