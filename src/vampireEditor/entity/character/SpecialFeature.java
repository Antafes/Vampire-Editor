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
package vampireEditor.entity.character;

import vampireEditor.entity.BaseTranslatedEntity;
import vampireEditor.entity.EntityException;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * SpecialFeature object.
 *
 * @author Marian Pollzien
 */
public abstract class SpecialFeature extends BaseTranslatedEntity implements SpecialFeatureInterface {
    private final int cost;
    private final SpecialFeatureType type;

    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        private SpecialFeatureType type;
        private int cost = 0;
        private SpecialFeatureClass specialFeatureClass;

        /**
         * List of special feature types.
         */
        public enum SpecialFeatureClass {
            MERIT,
            FLAW
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

            if (this.type == null) {
                throw new EntityException("Missing type");
            }

            if (this.specialFeatureClass == null) {
                throw new EntityException("Missing special feature classe");
            }
        }

        /**
         * Build a new special feature object.
         *
         * @return The created special feature entity
         * @throws vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
         *                                              of the entity
         */
        @Override
        public SpecialFeature build() throws EntityException {
            this.checkValues();

            if (this.specialFeatureClass == SpecialFeatureClass.MERIT) {
                return new Merit(this);
            } else {
                return new Flaw(this);
            }
        }

        /**
         * Get an instance of itself.
         *
         * @return The object itself
         */
        @Override
        protected SpecialFeature.Builder self() {
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

            for (Method declaredMethod : SpecialFeature.class.getDeclaredMethods()) {
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
         * Set the special feature type.
         *
         * @param type
         *
         * @return
         */
        public SpecialFeature.Builder setType(SpecialFeatureType type) {
            this.type = type;

            return this.self();
        }

        /**
         * Set the special feature cost.
         *
         * @param cost
         *
         * @return
         */
        public SpecialFeature.Builder setCost(int cost) {
            this.cost = cost;

            return this.self();
        }

        /**
         * Set the special feature class that should be built.
         *
         * @param specialFeatureClass
         *
         * @return
         */
        public SpecialFeature.Builder setSpecialFeatureClass(SpecialFeatureClass specialFeatureClass) {
            this.specialFeatureClass = specialFeatureClass;

            return this.self();
        }
    }

    /**
     * Create a new special feature with type and cost.
     *
     * @param builder The builder object
     */
    public SpecialFeature(Builder builder) {
        super(builder);

        this.cost = builder.cost;
        this.type = builder.type;
    }

    /**
     * Get the type of the special feature.
     *
     * @return
     */
    @Override
    public SpecialFeatureType getType() {
        return type;
    }

    /**
     * Get the cost of the special feature.
     *
     * @return
     */
    @Override
    public int getCost() {
        return cost;
    }

    /**
     * Get a string representation of the special feature.
     *
     * @return A string representation of the special feature
     */
    @Override
    public String toString() {
        return this.getName() + " (" + this.getCost() + ")";
    }
}
