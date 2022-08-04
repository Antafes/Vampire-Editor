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
package antafes.vampireEditor.entity;

import antafes.vampireEditor.Configuration;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

/**
 * A base translated entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "set", toBuilder = true)
public abstract class BaseTranslatedEntity extends BaseEntity {
    private final String key;
    private final HashMap<Configuration.Language, String> names;

    /**
     * Get the name of the entity according to the currently used language.
     */
    public String getName() {
        Configuration configuration = Configuration.getInstance();

        String name = this.names.get(configuration.getLanguage());

        if (name == null || name.isEmpty()) {
            name = this.names.get(Configuration.Language.ENGLISH);
        }

        return name;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    public abstract static class BaseTranslatedEntityBuilder<C extends BaseTranslatedEntity, B extends BaseTranslatedEntityBuilder<C, B>> extends BaseEntityBuilder<C, B> {
        @Override
        protected void checkValues() throws EntityException
        {
            if (this.key == null || this.key.isEmpty()) {
                throw new EntityException("Missing key for entity: " + this);
            }

            if (this.names == null || this.names.isEmpty()) {
                throw new EntityException("Missing names for entity: " + this);
            }
        }

        /**
         * Add a single translated name to the map.
         *
         * @param language The language for the name
         * @param name The translated name
         *
         * @return The builder object
         */
        public B addName(Configuration.Language language, String name) {
            if (this.names == null) {
                this.names = new HashMap<>();
            }

            this.names.put(language, name);

            return this.self();
        }
    }
}
