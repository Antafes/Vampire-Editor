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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clan object.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class Clan extends BaseTranslatedEntity implements ClanInterface {
    private final HashMap<Configuration.Language, String> nicknames;
    private final ArrayList<Advantage> disciplines;
    private final ArrayList<Weakness> weaknesses;

    /**
     * Builder for Ability objects.
     */
    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        private HashMap<Configuration.Language, String> nicknames;
        private ArrayList<Advantage> disciplines;
        private ArrayList<Weakness> weaknesses;

        public Builder() {
            this.nicknames = new HashMap<>();
            this.disciplines = new ArrayList<>();
            this.weaknesses = new ArrayList<>();
        }

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            super.checkValues();

            if (this.self().nicknames.isEmpty()) {
                throw new EntityException("Missing nicknames for entity: " + this);
            }

            if (this.self().disciplines.isEmpty()) {
                throw new EntityException("Missing disciplines for entity: " + this);
            }

            if (this.self().weaknesses.isEmpty()) {
                throw new EntityException("Missing weaknesses for entity: " + this);
            }
        }

        /**
         * Build a new Ability object.
         *
         * @return The created clan entity
         * @throws antafes.vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
         *                                              of the entity
         */
        @Override
        public Clan build() throws EntityException {
            this.checkValues();

            return new Clan(this);
        }

        /**
         * Get an instance of itself.
         *
         * @return The object itself
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Get the list of methods from which data can be fetched.
         *
         * @return
         */
        @Override
        protected ArrayList getDataMethods() {
            ArrayList<Method> methodList = super.getDataMethods();

            for (Method declaredMethod : Clan.class.getDeclaredMethods()) {
                if (this.checkMethod(declaredMethod)) {
                    continue;
                }

                methodList.add(declaredMethod);
            }

            return methodList;
        }

        /**
         * Get a setter method from the given getter.
         *
         * @param getter The getter to build the setter out of
         *
         * @return Setter method object
         * @throws NoSuchMethodException Exception thrown if no method of that name exists
         */
        @Override
        protected Method getSetter(Method getter) throws NoSuchMethodException {
            try {
                return super.getSetter(getter);
            } catch (NoSuchMethodException ex) {
                Class[] parameterTypes = new Class[1];
                parameterTypes[0] = getter.getReturnType();

                return Clan.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
            }
        }

        /**
         * Set a map of translated nicknames.
         *
         * @param nicknames
         *
         * @return
         */
        public Builder setNicknames(HashMap<Configuration.Language, String> nicknames) {
            this.nicknames = nicknames;

            return this.self();
        }

        /**
         * Add a single translated nickname.
         *
         * @param language The language for the nickname
         * @param nickname The nickname
         *
         * @return The builder object
         */
        public Builder addNickname(Configuration.Language language, String nickname) {
            this.nicknames.put(language, nickname);

            return this.self();
        }

        /**
         * Set the list of disciplines.
         *
         * @param disciplines
         *
         * @return The builder object
         */
        public Builder setDisciplines(ArrayList<Advantage> disciplines) {
            this.disciplines = disciplines;

            return this.self();
        }

        /**
         * Add a single discipline.
         *
         * @param discipline The discipline to add
         *
         * @return The builder object
         */
        public Builder addDiscipline(Advantage discipline) {
            this.disciplines.add(discipline);

            return this.self();
        }

        /**
         * Set the list of weaknesses.
         *
         * @param weaknesses
         *
         * @return
         */
        public Builder setWeaknesses(ArrayList<Weakness> weaknesses) {
            this.weaknesses = weaknesses;

            return this.self();
        }

        /**
         * Add a single weakness.
         *
         * @param weakness The weakness to add
         *
         * @return The builder object
         */
        public Builder addWeakness(Weakness weakness) {
            this.weaknesses.add(weakness);

            return this.self();
        }
    }

    /**
     * Create a new clan object.
     *
     * @param builder The builder object
     */
    public Clan(Builder builder) {
        super(builder);

        this.nicknames = builder.nicknames;
        this.disciplines = builder.disciplines;
        this.weaknesses = builder.weaknesses;
    }

    /**
     * Get the nicknames of the clan.
     *
     * @return
     */
    public HashMap<Configuration.Language, String> getNicknames() {
        return nicknames;
    }

    /**
     * Get the clan nicknames.
     *
     * @return
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

    /**
     * Get the list of disciplines every clan member has.
     *
     * @return
     */
    @Override
    public ArrayList<Advantage> getDisciplines() {
        return this.disciplines;
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
     * @return A string representation of the clan
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
