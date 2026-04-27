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

import antafes.vampireEditor.entity.BaseTypedTranslatedEntity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * SpecialFeature object.
 *
 * @author Marian Pollzien
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class SpecialFeature extends BaseTypedTranslatedEntity implements SpecialFeatureInterface {
    @ToString.Include
    @XmlElement(name = "cost")
    private int cost;

    /** No-arg constructor for JAXB deserialisation. */
    protected SpecialFeature() { super(); }

    @Override
    public String toString()
    {
        return super.toString() + " (" + this.cost + ")";
    }

    @Override
    public SpecialFeatureType getType()
    {
        return (SpecialFeatureType) super.getType();
    }

    @Override
    protected EntityTypeInterface parseJaxbType(String type)
    {
        return SpecialFeatureType.valueOf(type);
    }
}
