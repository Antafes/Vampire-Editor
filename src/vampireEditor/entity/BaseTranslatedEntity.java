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
package vampireEditor.entity;

import vampireEditor.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A base translated entity.
 */
public abstract class BaseTranslatedEntity extends BaseEntity {
    private final String key;
    private final HashMap<Configuration.Language, String> names;

    public abstract static class Builder<T extends Builder<T>> extends BaseEntity.Builder {
        private String key;
        private HashMap<Configuration.Language, String> names;

        public Builder() {
            this.names = new HashMap<>();
        }

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            if (this.self().getKey() == null || this.self().getKey().isEmpty()) {
                throw new EntityException("Missing key for entity: " + this);
            }

            if (this.self().getNames().isEmpty()) {
                throw new EntityException("Missing names for entity: " + this);
            }
        }

        /**
         * Get an instance of itself.
         *
         * @return The object itself
         */
        @Override
        protected abstract T self();

        /**
         * Fill every property from the given object into this builder.
         *
         * @param object A BaseEntity to fetch values from
         *
         * @return The builder object
         */
        @Override
        public T fillDataFromObject(BaseEntity object) {
            super.fillDataFromObject(object);

            return this.self();
        }

        /**
         * Check whether the given method can be used to fill in data.
         *
         * @param method The method to check.
         *
         * @return True if the method is not a getter or is the method "getDataMethods", otherwise false
         */
        @Override
        protected boolean checkMethod(Method method) {
            return super.checkMethod(method) || method.getName().equals("getName");
        }

        /**
         * Get the list of methods from which data can be fetched.
         *
         * @return A list of getter methods
         */
        @Override
        protected ArrayList getDataMethods() {
            ArrayList<Method> methodList = super.getDataMethods();

            for (Method declaredMethod : BaseTranslatedEntity.class.getDeclaredMethods()) {
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

                return BaseTranslatedEntity.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
            }
        }

        /**
         * Set the key of the object.
         *
         * @param key The value
         *
         * @return The builder object
         */
        public T setKey(String key) {
            this.key = key;

            return this.self();
        }

        /**
         * Set the map of translated names.
         *
         * @param names Map of names
         *
         * @return The builder object
         */
        public T setNames(HashMap<Configuration.Language, String> names) {
            this.names = names;

            return this.self();
        }

        /**
         * Add a single translated name to the map.
         *
         * @param language The language for the name
         * @param name The translated name
         *
         * @return The builder object
         */
        public T addName(Configuration.Language language, String name) {
            this.names.put(language, name);

            return this.self();
        }

        /**
         * Get the key for the entity.
         *
         * @return The key value
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Get the map of translated names.
         *
         * @return Map of names
         */
        public HashMap<Configuration.Language, String> getNames() {
            return this.names;
        }

        /**
         * String representation of the builder
         *
         * @return The key property
         */
        @Override
        public String toString() {
            return key;
        }
    }

    /**
     * Create new base entity.
     *
     * @param builder The builder object to fetch the data from
     */
    protected BaseTranslatedEntity(Builder builder) {
        super(builder);

        this.key = builder.key;
        this.names = builder.names;
    }

    /**
     * Get the key of the entity.
     *
     * @return
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Get the names of the entity.
     *
     * @return
     */
    public HashMap<Configuration.Language, String> getNames() {
        return names;
    }

    /**
     * Get the name of the entity according to the currently used language.
     *
     * @return
     */
    public String getName() {
        Configuration configuration = Configuration.getInstance();

        String name = this.names.get(configuration.getLanguage());

        if (name == null || name.isEmpty()) {
            name = this.names.get(Configuration.Language.ENGLISH);
        }

        return name;
    }
}
