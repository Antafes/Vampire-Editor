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
import vampireEditor.entity.BaseTranslatedEntity;
import vampireEditor.entity.EntityException;

/**
 * Road object.
 *
 * @author Marian Pollzien
 */
public class Road extends BaseTranslatedEntity implements RoadInterface {
    private final int value;

    /**
     * Builder for Ability objects.
     */
    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        private int value = 0;

        /**
         * Build a new Ability object.
         *
         * @return
         * @throws EntityException
         */
        @Override
        public Road build() throws EntityException {
            this.checkValues();

            return new Road(this);
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

            for (Method declaredMethod : Road.class.getDeclaredMethods()) {
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

                return Road.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
            }
        }

        public Builder setValue(int value) {
            this.value = value;

            return this.self();
        }
    }

    /**
     * Create a new road object.
     *
     * @param builder
     */
    public Road(Builder builder) {
        super(builder);

        this.value = builder.value;
    }

    /**
     * Get the road value.
     *
     * @return
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get a string representation of the road.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
