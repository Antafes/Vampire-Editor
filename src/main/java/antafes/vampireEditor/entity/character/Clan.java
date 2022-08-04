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
package antafes.vampireEditor.entity.character;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.EntityException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clan object.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class Clan extends BaseTranslatedEntity implements ClanInterface {
    private final HashMap<Configuration.Language, String> nicknames;
    private final ArrayList<Advantage> advantages;
    private final ArrayList<Weakness> weaknesses;

    /**
     * Get the clan nicknames.
     */
    @Override
    public String getNickname() {
        Configuration configuration = Configuration.getInstance();

        String nickname = this.nicknames.get(configuration.getLanguage());

        if (nickname == null || nickname.isEmpty()) {
            nickname = this.nicknames.get(Configuration.Language.ENGLISH);
        }

        return nickname;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    /**
     * Builder for Ability objects.
     */
    public abstract static class ClanBuilder<C extends Clan, B extends ClanBuilder<C, B>> extends BaseTranslatedEntityBuilder<C, B> {
        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            super.checkValues();

            if (this.nicknames == null || this.nicknames.isEmpty()) {
                throw new EntityException("Missing nicknames for entity: " + this);
            }

            if (this.advantages == null || this.advantages.isEmpty()) {
                throw new EntityException("Missing advantages for entity: " + this);
            }

            if (this.weaknesses == null || this.weaknesses.isEmpty()) {
                throw new EntityException("Missing weaknesses for entity: " + this);
            }
        }

        /**
         * Add a single translated nickname.
         *
         * @param language The language for the nickname
         * @param nickname The nickname
         *
         * @return The builder object
         */
        public B addNickname(Configuration.Language language, String nickname) {
            if (this.nicknames == null) {
                this.nicknames = new HashMap<>();
            }

            this.nicknames.put(language, nickname);

            return this.self();
        }

        /**
         * Add a single discipline.
         *
         * @param discipline The discipline to add
         *
         * @return The builder object
         */
        public B addAdvantage(Advantage discipline) {
            if (this.advantages == null) {
                this.advantages = new ArrayList<>();
            }

            this.advantages.add(discipline);

            return this.self();
        }

        /**
         * Add a single weakness.
         *
         * @param weakness The weakness to add
         *
         * @return The builder object
         */
        public B addWeakness(Weakness weakness) {
            if (this.weaknesses == null) {
                this.weaknesses = new ArrayList<>();
            }

            this.weaknesses.add(weakness);

            return this.self();
        }
    }
}
