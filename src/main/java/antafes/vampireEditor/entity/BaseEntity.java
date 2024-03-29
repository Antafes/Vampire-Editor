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
package antafes.vampireEditor.entity;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

/**
 * A base entity.
 */
@Data
@SuperBuilder(setterPrefix = "set", toBuilder = true)
public abstract class BaseEntity {
    @SneakyThrows(EntityException.class)
    protected BaseEntity(BaseEntityBuilder<?, ?> b)
    {
        b.checkValues();
    }

    public static abstract class BaseEntityBuilder<C extends BaseEntity, B extends BaseEntityBuilder<C, B>>
    {
        protected abstract void checkValues() throws EntityException;
    }
}
