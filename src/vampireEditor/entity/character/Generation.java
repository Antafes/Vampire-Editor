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

import vampireEditor.entity.BaseEntity;
import vampireEditor.entity.EntityException;

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
         * @throws vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
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
            if (this.generation == 0) {
                throw new EntityException("Missing generation");
            }

            if (this.maximumAttributes == 0) {
                throw new EntityException("Missing maximum attributes");
            }

            if (this.maximumBloodStock == 0) {
                throw new EntityException("Missing maximum blood stock");
            }

            if (this.bloodPerRound == 0) {
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

        /**
         * Returns a string representation of the generation.
         *
         * @return A string representation of the generation
         */
        @Override
        public String toString() {
            return "Generation " + generation;
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
        return generation;
    }

    /**
     * Get the maximum for attributes.
     *
     * @return
     */
    @Override
    public int getMaximumAttributes() {
        return maximumAttributes;
    }

    /**
     * Get the maximum for the blood stock.
     *
     * @return
     */
    @Override
    public int getMaximumBloodStock() {
        return maximumBloodStock;
    }

    /**
     * Get the maximum allowed usage of blood per round.
     *
     * @return
     */
    @Override
    public int getBloodPerRound() {
        return bloodPerRound;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        return Integer.toString(generation);
    }
}
