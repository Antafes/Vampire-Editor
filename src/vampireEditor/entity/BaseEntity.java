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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A base entity.
 */
public abstract class BaseEntity {
    public abstract static class Builder<T extends Builder<T>> {
        /**
         * Build a new base entity.
         *
         * @return
         * @throws vampireEditor.entity.EntityException
         */
        public abstract BaseEntity build() throws EntityException;

        /**
         * Check if all necessary values are set.
         * This has to be called in the build method.
         *
         * @throws EntityException
         */
        protected abstract void checkValues() throws EntityException;

        /**
         * Get an instance of itself.
         *
         * @return
         */
        protected abstract T self();

        /**
         * Fill every property from the given object into this builder.
         *
         * @param object
         *
         * @return
         */
        public T fillDataFromObject(Object object) {
            this.self().getDataMethods().forEach((declaredMethod) -> {
                try {
                    Method setter = this.getSetter(declaredMethod);
                    setter.invoke(this, declaredMethod.invoke(object, (Object[])null));
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(BaseEntity.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return this.self();
        }

        /**
         * Check whether the given method can be used to fill in data.
         *
         * @param method The method to check.
         *
         * @return
         */
        protected boolean checkMethod(Method method) {
            return !method.getName().startsWith("get") || method.getName().equals("getDataMethods");
        }

        /**
         * Get the list of methods from which data can be fetched.
         *
         * @return
         */
        protected ArrayList<Method> getDataMethods() {
            ArrayList<Method> methodList = new ArrayList<>();

            for (Method declaredMethod : BaseEntity.class.getDeclaredMethods()) {
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
        protected Method getSetter(Method getter) throws NoSuchMethodException {
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = getter.getReturnType();

            return BaseEntity.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
        }
    }

    /**
     * Create new base entity.
     *
     * @param builder
     */
    protected BaseEntity(Builder<?> builder) {
    }
}
