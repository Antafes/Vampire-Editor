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
package antafes.vampireEditor.entity;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.character.EntityTypeInterface;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Ability object.
 *
 * @author Marian Pollzien
 */
public abstract class BaseTypedTranslatedEntity extends BaseTranslatedEntity {
    private final EntityTypeInterface type;

    /**
     * Builder for typed translated entities.
     */
    public abstract static class Builder<T extends Builder<T>> extends BaseTranslatedEntity.Builder {
        private EntityTypeInterface type;

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            super.checkValues();

            if (this.self().getType() == null) {
                throw new EntityException("Missing type for entity: " + this);
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
         * Get the list of methods from which data can be fetched.
         *
         * @return
         */
        @Override
        protected ArrayList getDataMethods() {
            ArrayList<Method> methodList = super.getDataMethods();

            for (Method declaredMethod : BaseTypedTranslatedEntity.class.getDeclaredMethods()) {
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

                return BaseTypedTranslatedEntity.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
            }
        }

        /**
         * Get the entity type.
         *
         * @return
         */
        public EntityTypeInterface getType() {
            return type;
        }

        /**
         * Set the entity type.
         *
         * @param type
         *
         * @return
         */
        public T setType(EntityTypeInterface type) {
            this.type = type;

            return this.self();
        }

        /**
         * Set the key of the object.
         *
         * @param key The value
         *
         * @return The builder object
         */
        @Override
        public T setKey(String key) {
            return (T) super.setKey(key);
        }

        /**
         * Set the map of translated names.
         *
         * @param names Map of names
         *
         * @return The builder object
         */
        @Override
        public T setNames(HashMap names) {
            return (T) super.setNames(names);
        }

        /**
         * Add a single translated name to the map.
         *
         * @param language The language for the name
         * @param name The translated name
         *
         * @return The builder object
         */
        @Override
        public T addName(Configuration.Language language, String name) {
            return (T) super.addName(language, name);
        }
    }

    /**
     * Create a new ability with type and value.
     *
     * @param builder The builder object
     */
    protected BaseTypedTranslatedEntity(Builder builder) {
        super(builder);

        this.type = builder.type;
    }

    /**
     * Get the type of ability.
     *
     * @return
     */
    public EntityTypeInterface getType() {
        return this.type;
    }

    /**
     * Check if the given object equals this object.
     *
     * @param obj The object to check
     *
     * @return True if both are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (!super.equals(obj)) {
            return false;
        }

        BaseTypedTranslatedEntity entity = (BaseTypedTranslatedEntity) obj;

        return this.type == entity.type;
    }

    /**
     * Generate a hash code.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.type);
    }
}
