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

import antafes.vampireEditor.entity.BaseEntity;
import antafes.vampireEditor.entity.EntityException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Generation object.
 *
 * @author Marian Pollzien
 */
public class Generation extends BaseEntity implements GenerationInterface {
    private final int generation;
    private final int maximumAttributes;
    private final int maximumBloodStock;
    private final int bloodPerRound;

    /**
     * Builder for generation objects.
     */
    public static class Builder extends BaseEntity.Builder<Builder> {
        private int generation;
        private int maximumAttributes;
        private int maximumBloodStock;
        private int bloodPerRound;

        /**
         * Build a new character object.
         *
         * @return The generation created entity
         * @throws antafes.vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
         *                                              of the entity
         */
        @Override
        public Generation build() throws EntityException {
            this.checkValues();

            return new Generation(this);
        }

        /**
         * Check if all necessary values are set.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            if (this.generation <= 0) {
                throw new EntityException("Missing generation");
            }

            if (this.maximumAttributes <= 0) {
                throw new EntityException("Missing maximum attributes");
            }

            if (this.maximumBloodStock == 0 || this.maximumBloodStock < -1) {
                throw new EntityException("Missing maximum blood stock");
            }

            if (this.bloodPerRound == 0 || this.bloodPerRound < -1) {
                throw new EntityException("Missing blood per round");
            }
        }

        /**
         * Get the current instance.
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
         * @return A list of getter methods
         */
        @Override
        protected ArrayList<Method> getDataMethods() {
            ArrayList<Method> methodList = super.getDataMethods();

            for (Method declaredMethod : Generation.class.getDeclaredMethods()) {
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

                return Generation.Builder.class.getDeclaredMethod(
                    "set" + getter.getName().substring(3),
                    parameterTypes
                );
            }
        }

        /**
         * Set the generation.
         *
         * @param generation
         *
         * @return The builder object
         */
        public Builder setGeneration(int generation) {
            this.generation = generation;

            return this.self();
        }

        /**
         * Set the maximum attribute value.
         *
         * @param maximumAttributes
         *
         * @return The builder object
         */
        public Builder setMaximumAttributes(int maximumAttributes) {
            this.maximumAttributes = maximumAttributes;

            return this.self();
        }

        /**
         * Set the maximum blood stock value.
         *
         * @param maximumBloodStock
         *
         * @return The builder object
         */
        public Builder setMaximumBloodStock(int maximumBloodStock) {
            this.maximumBloodStock = maximumBloodStock;

            return this.self();
        }

        /**
         * Set the blood per round value.
         *
         * @param bloodPerRound
         *
         * @return The builder object
         */
        public Builder setBloodPerRound(int bloodPerRound) {
            this.bloodPerRound = bloodPerRound;

            return this.self();
        }
    }

    /**
     * Create a new generation object.
     *
     * @param builder The builder object
     */
    protected Generation(Builder builder) {
        super(builder);

        this.generation = builder.generation;
        this.maximumAttributes = builder.maximumAttributes;
        this.maximumBloodStock = builder.maximumBloodStock;
        this.bloodPerRound = builder.bloodPerRound;
    }

    /**
     * Get the generation number.
     *
     * @return
     */
    @Override
    public int getGeneration() {
        return this.generation;
    }

    /**
     * Get the maximum for attributes.
     *
     * @return
     */
    @Override
    public int getMaximumAttributes() {
        return this.maximumAttributes;
    }

    /**
     * Get the maximum for the blood stock.
     *
     * @return
     */
    @Override
    public int getMaximumBloodStock() {
        if (this.maximumBloodStock == -1) {
            return Integer.MAX_VALUE;
        }

        return this.maximumBloodStock;
    }

    /**
     * Get the maximum allowed usage of blood per round.
     *
     * @return
     */
    @Override
    public int getBloodPerRound() {
        if (this.bloodPerRound == -1) {
            return Integer.MAX_VALUE;
        }

        return this.bloodPerRound;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        return Integer.toString(this.generation);
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

        Generation that = (Generation) obj;

        return generation == that.generation &&
            maximumAttributes == that.maximumAttributes &&
            maximumBloodStock == that.maximumBloodStock &&
            bloodPerRound == that.bloodPerRound;
    }

    /**
     * Generate a hash code.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            generation,
            maximumAttributes,
            maximumBloodStock,
            bloodPerRound
        );
    }
}
