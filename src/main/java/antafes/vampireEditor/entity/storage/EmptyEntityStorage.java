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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.BaseEntity;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;

import java.util.HashMap;

public class EmptyEntityStorage extends BaseStorage
{
    private EmptyEntity entity;

    @Override
    public void init()
    {
        try {
            this.entity = new EmptyEntity.Builder()
                .setKey("empty")
                .addName(Configuration.Language.ENGLISH, "")
                .addName(Configuration.Language.GERMAN, "")
                .build();
        } catch (EntityException ignored) {
        }
    }

    public EmptyEntity getEntity()
    {
        return this.entity;
    }

    @Override
    public EmptyEntity getEntity(String key) throws EntityStorageException
    {
        return this.entity;
    }

    /**
     * There is no list to return!
     */
    @Override
    public HashMap<String, BaseEntity> getList()
    {
        return null;
    }
}
