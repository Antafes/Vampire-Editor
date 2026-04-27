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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package antafes.vampireEditor.entity;

import antafes.vampireEditor.entity.character.EntityTypeInterface;
import antafes.vampireEditor.entity.exception.EntityException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Ability object.
 *
 * @author Marian Pollzien
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "set", toBuilder = true)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseTypedTranslatedEntity extends BaseTranslatedEntity {
    private EntityTypeInterface type;

    /** No-arg constructor for JAXB deserialisation. */
    protected BaseTypedTranslatedEntity() { super(); }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @XmlElement(name = "type")
    protected String getJaxbType()
    {
        return this.type == null ? null : ((Enum<?>) this.type).name();
    }

    protected void setJaxbType(String type)
    {
        this.type = type == null ? null : this.parseJaxbType(type);
    }

    protected abstract EntityTypeInterface parseJaxbType(String type);

    public static abstract class BaseTypedTranslatedEntityBuilder<C extends BaseTypedTranslatedEntity, B extends BaseTypedTranslatedEntityBuilder<C, B>> extends BaseTranslatedEntityBuilder<C, B> {
        @Override
        protected void checkValues() throws EntityException
        {
            if (this.type == null) {
                throw new EntityException("Missing type for entity: " + this);
            }

            super.checkValues();
        }
    }
}
