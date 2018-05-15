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
import java.util.HashMap;
import vampireEditor.Configuration;

/**
 * Clan object.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class Clan implements ClanInterface {
    private String key;
    private HashMap<Configuration.Language, String> name;
    private HashMap<Configuration.Language, String> nickname;
    private ArrayList<Advantage> disciplins;
    private ArrayList<Weakness> weaknesses;

    /**
     * Create a new clan object.
     *
     * @param key        The key for the clan so it can be identified.
     * @param name       The name of the clan.
     * @param disciplins A list of disciplines every clan member has.
     * @param weaknesses A list of weaknesses every clan member has.
     */
    public Clan(
        String key,
        HashMap<Configuration.Language, String> name,
        ArrayList<Advantage> disciplins,
        ArrayList<Weakness> weaknesses
    ) {
        this(key, name, new HashMap<Configuration.Language, String>(), disciplins, weaknesses);
    }

    /**
     * Create a new clan object.
     *
     * @param key        The key for the clan so it can be identified.
     * @param name       The name of the clan.
     * @param nickname   The nickname of the clan.
     * @param disciplins A list of disciplines every clan member has.
     * @param weaknesses A list of weaknesses every clan member has.
     */
    public Clan(
        String key,
        HashMap<Configuration.Language, String> name,
        HashMap<Configuration.Language, String> nickname,
        ArrayList<Advantage> disciplins,
        ArrayList<Weakness> weaknesses
    ) {
        this.key = key;
        this.name = name;
        this.nickname = nickname;
        this.disciplins = disciplins;
        this.weaknesses = weaknesses;
    }

    /**
     * Get the key over which the clan can be identified.
     *
     * @return
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Get the clan name.
     *
     * @return
     */
    @Override
    public String getName() {
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        return this.name.get(configuration.getLanguage());
    }

    /**
     * Get the clan nickname.
     *
     * @return
     */
    @Override
    public String getNickname() {
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        return this.nickname.get(configuration.getLanguage());
    }

    /**
     * Get the list of disciplins every clan member has.
     *
     * @return
     */
    @Override
    public ArrayList<Advantage> getDisciplins() {
        return this.disciplins;
    }

    /**
     * Get the list of weaknesses every clan member has.
     *
     * @return
     */
    @Override
    public ArrayList<Weakness> getWeaknesses() {
        return this.weaknesses;
    }

    /**
     * Returns a string representation of the clan.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
