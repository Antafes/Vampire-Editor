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

import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.EntityException;

/**
 *
 * @author Marian Pollzien
 */
public class Weakness extends BaseTranslatedEntity implements WeaknessInterface {
    /**
     * Builder for Ability objects.
     */
    public static class Builder extends BaseTranslatedEntity.Builder<Builder> {
        /**
         * Build a new weakness object.
         *
         * @return The created weakness entity
         * @throws antafes.vampireEditor.entity.EntityException Throws an EntityException if something went wrong during build
         *                                              of the entity
         */
        @Override
        public Weakness build() throws EntityException {
            this.checkValues();

            return new Weakness(this);
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
    }

    /**
     * Create a new weakness object.
     *
     * @param builder The builder object
     */
    public Weakness(Builder builder) {
        super(builder);
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
