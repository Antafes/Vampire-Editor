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

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.exception.EntityException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import antafes.vampireEditor.entity.storage.adapter.LocalizedNamesAdapter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

/**
 * A base translated entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "set", toBuilder = true)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseTranslatedEntity extends BaseEntity {
    private String key;
    private HashMap<Configuration.Language, String> names;

    /** No-arg constructor for JAXB deserialisation. */
    protected BaseTranslatedEntity() { super(); }

    /**
     * Get the name of the entity according to the given configuration.
     */
    public String getName(Configuration configuration) {
        if (configuration == null) {
            return this.getName();
        }

        return this.getName(configuration.getLanguage());
    }

    /**
     * Get the name of the entity according to the given language.
     */
    public String getName(Configuration.Language language) {
        String name = this.names != null ? this.names.get(language) : null;

        if (name == null || name.isEmpty()) {
            name = this.names != null ? this.names.get(Configuration.Language.ENGLISH) : null;
        }

        if (name == null || name.isEmpty()) {
            name = this.key;
        }

        return name;
    }

    /**
     * Get the name of the entity using a stable non-localized fallback.
     */
    public String getName() {
        return this.getName(Configuration.Language.ENGLISH);
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    @XmlAttribute(name = "key")
    protected String getJaxbKey()
    {
        return this.key;
    }

    protected void setJaxbKey(String key)
    {
        this.key = key;
    }

    @XmlElement(name = "name")
    @XmlJavaTypeAdapter(LocalizedNamesAdapter.class)
    protected HashMap<Configuration.Language, String> getJaxbName()
    {
        return this.names;
    }

    protected void setJaxbName(HashMap<Configuration.Language, String> names)
    {
        this.names = names;
    }

    public abstract static class BaseTranslatedEntityBuilder<C extends BaseTranslatedEntity, B extends BaseTranslatedEntityBuilder<C, B>> extends BaseEntityBuilder<C, B> {
        @Override
        protected void checkValues() throws EntityException
        {
            if (this.key == null || this.key.isEmpty()) {
                throw new EntityException("Missing key for entity: " + this);
            }

            if (this.names == null || this.names.isEmpty()) {
                throw new EntityException("Missing names for entity: " + this);
            }
        }

        @Override
        protected void executeAdditionalCalculations()
        {
        }

        /**
         * Add a single translated name to the map.
         *
         * @param language The language for the name
         * @param name The translated name
         *
         * @return The builder object
         */
        public B addName(Configuration.Language language, String name) {
            if (this.names == null) {
                this.names = new HashMap<>();
            }

            this.names.put(language, name);

            return this.self();
        }
    }
}
