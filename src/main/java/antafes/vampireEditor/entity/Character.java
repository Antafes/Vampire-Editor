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
import antafes.vampireEditor.entity.character.*;
import antafes.vampireEditor.utility.StringComparator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Character object.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class Character extends BaseEntity {
    private final UUID id;
    private final String name;
    private final Clan clan;
    private final Generation generation;
    private final String chronicle;
    private final int experience;
    private final String nature;
    private final String hideout;
    private final String player;
    private final String demeanor;
    private final String concept;
    private final String sire;
    private final String sect;
    private final ArrayList<Attribute> attributes;
    private final ArrayList<Ability> abilities;
    private final ArrayList<Advantage> advantages;
    private final ArrayList<Merit> merits;
    private final ArrayList<Flaw> flaws;
    private final Road road;
    private final int willpower;
    private final int usedWillpower;
    private final int bloodPool;
    private final int age;
    private final int apparentAge;
    private final Date dayOfBirth;
    private final Date dayOfDeath;
    private final String hairColor;
    private final String eyeColor;
    private final String skinColor;
    private final String nationality;
    private final int height;
    private final int weight;
    private final Sex sex;
    private final String story;
    private final String description;

    /**
     * Get a list of attributes by type.
     *
     * @param type Type of attributes to get
     *
     * @return List of attributes
     */
    public ArrayList<Attribute> getAttributesByType(AttributeInterface.AttributeType type) {
        ArrayList<Attribute> attributes = (ArrayList<Attribute>) this.attributes.stream()
            .filter((attribute) -> (attribute.getType() == type)).collect(Collectors.toList());
        attributes.sort(new StringComparator());

        return attributes;
    }

    /**
     * Get a list of abilities by type.
     *
     * @param type Type of abilities to get
     *
     * @return List of abilities
     */
    public ArrayList<Ability> getAbilitiesByType(AbilityInterface.AbilityType type) {
        ArrayList<Ability> abilities = (ArrayList<Ability>) this.abilities.stream()
            .filter((ability) -> (ability.getType() == type)).collect(Collectors.toList());
        abilities.sort(new StringComparator());

        return abilities;
    }

    /**
     * Get a list of advantages by type.
     *
     * @param type Type of advantages to get
     *
     * @return List of advantages
     */
    public ArrayList<Advantage> getAdvantagesByType(AdvantageInterface.AdvantageType type) {
        ArrayList<Advantage> advantages = (ArrayList<Advantage>) this.advantages.stream()
            .filter((advantage) -> (advantage.getType() == type)).collect(Collectors.toList());
        advantages.sort(new StringComparator());

        return advantages;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    /**
     * List of sexes for the character.
     */
    public enum Sex {
        MALE,
        FEMALE;

        @Override
        public String toString() {
            Configuration configuration = Configuration.getInstance();

            return configuration.getLanguageObject().translate(this.name());
        }
    }

    /**
     * Builder for character objects.
     */
    public abstract static class CharacterBuilder<C extends Character, B extends CharacterBuilder<C, B>> extends BaseEntityBuilder<C, B> {
        public CharacterBuilder()
        {
            this.attributes = new ArrayList<>();
            this.abilities = new ArrayList<>();
            this.advantages = new ArrayList<>();
            this.merits = new ArrayList<>();
            this.flaws = new ArrayList<>();
        }

        /**
         * Check if all necessary values are set.
         *
         * @throws EntityException If something is missing but required
         */
        @Override
        protected void checkValues() throws EntityException {
            this.checkId();

            if (this.name == null || this.name.isEmpty()) {
                throw new EntityException("Missing name");
            }

            if (this.clan == null) {
                throw new EntityException("Missing clan");
            }

            if (this.generation == null) {
                throw new EntityException("Missing generation");
            }

            if (this.nature == null || this.nature.isEmpty()) {
                throw new EntityException("Missing nature");
            }

            if (this.demeanor == null || this.demeanor.isEmpty()) {
                throw new EntityException("Missing demeanor");
            }

            if (this.concept == null || this.concept.isEmpty()) {
                throw new EntityException("Missing concept");
            }

            this.checkAttributes();
            this.checkAbilities();
            this.checkAdvantages();
        }

        /**
         * Check whether the id has been set. If not, set it to a random UUID.
         */
        private void checkId()
        {
            if (this.id == null) {
                this.id = UUID.randomUUID();
            }
        }

        /**
         * Check if the attributes are set correctly.
         *
         * @throws EntityException Thrown if attributes are empty, some are missing or there are too many
         */
        private void checkAttributes() throws EntityException {
            if (this.attributes == null || this.attributes.isEmpty()) {
                throw new EntityException("Attributes are empty");
            }

            if (this.attributes.size() < 9) {
                throw new EntityException("Missing attributes");
            }

            if (this.attributes.size() > 9) {
                throw new EntityException("Too many attributes");
            }
        }

        /**
         * Check if the abilities are set correctly.
         *
         * @throws EntityException Thrown if abilities are empty, some are missing or there are too many
         */
        private void checkAbilities() throws EntityException {
            if (this.abilities == null || this.abilities.isEmpty()) {
                throw new EntityException("Abilities are empty");
            }

            if (this.abilities.size() < 30) {
                throw new EntityException("Missing abilities");
            }

            if (this.abilities.size() > 30) {
                throw new EntityException("Too many abilities");
            }
        }

        /**
         * Check if the advantages are set correctly.
         *
         * @throws EntityException Thrown if advantages are empty or some are missing
         */
        private void checkAdvantages() throws EntityException {
            if (this.advantages == null || this.advantages.isEmpty()) {
                throw new EntityException("Advantages are empty");
            }

            if (this.advantages.size() < 6) {
                throw new EntityException("Missing advantages");
            }
        }

        public B addAttribute(Attribute attribute) {
            this.attributes.add(attribute);

            return this.self();
        }

        public B addAbility(Ability ability) {
            this.abilities.add(ability);

            return this.self();
        }

        public B addAdvantage(Advantage advantage) {
            this.advantages.add(advantage);

            return this.self();
        }

        public B addMerit(Merit merit) {
            this.merits.add(merit);

            return this.self();
        }

        public B addFlaw(Flaw flaw) {
            this.flaws.add(flaw);

            return this.self();
        }
    }
}
