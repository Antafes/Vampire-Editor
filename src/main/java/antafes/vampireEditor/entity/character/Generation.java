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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Generation object.
 *
 * @author Marian Pollzien
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class Generation extends BaseEntity implements GenerationInterface {
    private final int generation;
    private final int maximumAttributes;
    private final int maximumBloodPool;
    private final int bloodPerRound;

    @Override
    public String toString()
    {
        return Integer.toString(this.generation);
    }

    /**
     * Builder for generation objects.
     */
    public abstract static class GenerationBuilder<C extends Generation, B extends GenerationBuilder<C, B>> extends BaseEntityBuilder<C, B> {
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

            if (this.maximumBloodPool == 0 || this.maximumBloodPool < -1) {
                throw new EntityException("Missing maximum blood pool");
            }

            if (this.bloodPerRound == 0 || this.bloodPerRound < -1) {
                throw new EntityException("Missing blood per round");
            }
        }

        @Override
        protected void executeAdditionalCalculations()
        {
        }
    }

    /**
     * Get the maximum for the blood pool.
     */
    @Override
    public int getMaximumBloodPool() {
        if (this.maximumBloodPool == -1) {
            return Integer.MAX_VALUE;
        }

        return this.maximumBloodPool;
    }

    /**
     * Get the maximum allowed usage of blood per round.
     */
    @Override
    public int getBloodPerRound() {
        if (this.bloodPerRound == -1) {
            return Integer.MAX_VALUE;
        }

        return this.bloodPerRound;
    }
}
