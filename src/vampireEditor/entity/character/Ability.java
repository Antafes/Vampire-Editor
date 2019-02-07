/**
 * This file is part of Vampire_Editor.
 *
 * Vampire_Editor is free software: you can redistribute it and/or modify
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
 * @package Vampire_Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2018, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package vampireEditor.entity.character;

import vampireEditor.entity.BaseTranslatedEntity;
import vampireEditor.entity.EntityException;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Ability object.
 *
 * @author Marian Pollzien
 */
public class Ability extends BaseTranslatedEntity implements AbilityInterface {
    private final AbilityType type;
    private final int value;

    /**
     * Builder for Ability objects.
     */
    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        private AbilityType type;
        private int value = 0;

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            super.checkValues();

            if (this.type == null) {
                throw new EntityException("Missing type");
            }
        }

        /**
         * Build a new Ability object.
         *
         * @return The created ability entity
         * @throws vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
         *                                              of the entity
         */
        @Override
        public Ability build() throws EntityException {
            this.checkValues();

            return new Ability(this);
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

            for (Method declaredMethod : Ability.class.getDeclaredMethods()) {
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

                return Ability.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
            }
        }

        /**
         * Set the ability type.
         *
         * @param type
         *
         * @return The builder object
         */
        public Builder setType(AbilityType type) {
            this.type = type;

            return this.self();
        }

        /**
         * Set the abilities value.
         *
         * @param value
         *
         * @return The builder object
         */
        public Builder setValue(int value) {
            this.value = value;

            return this.self();
        }
    }

    /**
     * Create a new ability with type and value.
     *
     * @param builder The builder object
     */
    protected Ability(Builder builder) {
        super(builder);

        this.type = builder.type;
        this.value = builder.value;
    }

    /**
     * Get the type of ability.
     *
     * @return
     */
    @Override
    public AbilityType getType() {
        return this.type;
    }

    /**
     * Get the value of the ability.
     *
     * @return
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
