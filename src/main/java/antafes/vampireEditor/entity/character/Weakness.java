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

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

/**
 *
 * @author Marian Pollzien
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
@XmlRootElement(name = "weakness")
@XmlAccessorType(XmlAccessType.NONE)
public class Weakness extends BaseTranslatedEntity implements WeaknessInterface {
    protected Weakness()
    {
        super();
    }

    @Override
    @XmlTransient
    protected HashMap<Configuration.Language, String> getJaxbName()
    {
        return super.getJaxbName();
    }

    @XmlElement(name = "English")
    protected String getJaxbEnglish()
    {
        return getNames() == null ? null : getNames().get(Configuration.Language.ENGLISH);
    }

    protected void setJaxbEnglish(String value)
    {
        if (value == null) {
            return;
        }

        if (getNames() == null) {
            setNames(new HashMap<>());
        }

        getNames().put(Configuration.Language.ENGLISH, value);
    }

    @XmlElement(name = "German")
    protected String getJaxbGerman()
    {
        return getNames() == null ? null : getNames().get(Configuration.Language.GERMAN);
    }

    protected void setJaxbGerman(String value)
    {
        if (value == null) {
            return;
        }

        if (getNames() == null) {
            setNames(new HashMap<>());
        }

        getNames().put(Configuration.Language.GERMAN, value);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
