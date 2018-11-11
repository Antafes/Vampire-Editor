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
package vampireEditor.entity.character;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import vampireEditor.Configuration;
import vampireEditor.entity.BaseTranslatedEntity;
import vampireEditor.entity.EntityException;

/**
 * Clan object.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class Clan extends BaseTranslatedEntity implements ClanInterface {
    private final HashMap<Configuration.Language, String> nicknames;
    private final ArrayList<Advantage> disciplins;
    private final ArrayList<Weakness> weaknesses;

    /**
     * Builder for Ability objects.
     */
    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        private HashMap<Configuration.Language, String> nicknames;
        private ArrayList<Advantage> disciplins;
        private ArrayList<Weakness> weaknesses;

        public Builder() {
            this.nicknames = new HashMap<>();
            this.disciplins = new ArrayList<>();
            this.weaknesses = new ArrayList<>();
        }

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException
         */
        @Override
        protected void checkValues() throws EntityException {
            super.checkValues();

            if (this.self().nicknames.isEmpty()) {
                throw new EntityException("Missing nicknames for entity: " + this);
            }

            if (this.self().disciplins.isEmpty()) {
                throw new EntityException("Missing disciplins for entity: " + this);
            }

            if (this.self().weaknesses.isEmpty()) {
                throw new EntityException("Missing weaknesses for entity: " + this);
            }
        }

        /**
         * Build a new Ability object.
         *
         * @return
         * @throws EntityException
         */
        @Override
        public Clan build() throws EntityException {
            this.checkValues();

            return new Clan(this);
        }

        /**
         * Get an instance of itself.
         *
         * @return
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
         * @param getter
         *
         * @return
         * @throws NoSuchMethodException
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

        public Builder setNicknames(HashMap<Configuration.Language, String> nicknames) {
            this.nicknames = nicknames;

            return this.self();
        }

        public Builder addNickname(Configuration.Language language, String nickname) {
            this.nicknames.put(language, nickname);

            return this.self();
        }

        public Builder setDisciplins(ArrayList<Advantage> disciplins) {
            this.disciplins = disciplins;

            return this.self();
        }

        public Builder addDisciplin(Advantage disciplin) {
            this.disciplins.add(disciplin);

            return this.self();
        }

        public Builder setWeaknesses(ArrayList<Weakness> weaknesses) {
            this.weaknesses = weaknesses;

            return this.self();
        }

        public Builder addWeakness(Weakness weakness) {
            this.weaknesses.add(weakness);

            return this.self();
        }
    }

    /**
     * Create a new clan object.
     *
     * @param builder
     */
    public Clan(Builder builder) {
        super(builder);

        this.nicknames = builder.nicknames;
        this.disciplins = builder.disciplins;
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
        Configuration configuration = new Configuration();
        configuration.loadProperties();

        String nickname = this.nicknames.get(configuration.getLanguage());

        if (nickname == null || nickname.isEmpty()) {
            nickname = this.nicknames.get(Configuration.Language.ENGLISH);
        }

        return nickname;
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
