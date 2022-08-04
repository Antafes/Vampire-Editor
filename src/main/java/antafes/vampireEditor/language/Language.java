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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.language;

import java.util.HashMap;
import java.util.Objects;

public abstract class Language implements LanguageInterface {
    private final HashMap<String, String> translations = new HashMap<>();

    /**
     * Get the translations map.
     *
     * @return
     */
    protected HashMap<String, String> getTranslations() {
        return this.translations;
    }

    /**
     * Get the translation for the given key.
     *
     * @param key The key to translate.
     *
     * @return The translated string
     */
    @Override
    public String translate(String key) {
        return this.getTranslations().getOrDefault(key, key);
    }

    /**
     * Check if the given object equals this object.
     *
     * @param o The object to check
     *
     * @return True if both are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Language language = (Language) o;

        return Objects.equals(this.translations, language.getTranslations());
    }

    /**
     * Generate a hash code.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(translations);
    }
}
