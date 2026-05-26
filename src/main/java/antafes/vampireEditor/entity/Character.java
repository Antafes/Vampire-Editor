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
import antafes.vampireEditor.entity.exception.EntityException;
import antafes.vampireEditor.entity.storage.adapter.*;
import antafes.vampireEditor.utility.StringComparator;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * Character object.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
@XmlRootElement(name = "character")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {
    "name", "clan", "generation", "chronicle", "experience", "nature",
    "hideout", "player", "demeanor", "concept", "sire", "sect",
    "attributes", "abilities", "advantages", "merits", "flaws", "road", "path",
    "willpower", "usedWillpower", "bloodPool", "age", "apparentAge",
    "dayOfBirth", "dayOfDeath",
    "hairColor", "eyeColor", "skinColor", "nationality", "height", "weight",
    "sex", "story", "description"
})
public class Character extends BaseEntity {
    @XmlAttribute(name = "id", required = true)
    private UUID id;

    @XmlAttribute(name = "isNpc")
    private boolean npc;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "clan")
    @XmlJavaTypeAdapter(ClanKeyAdapter.class)
    private Clan clan;

    @XmlElement(name = "generation")
    @XmlJavaTypeAdapter(GenerationValueAdapter.class)
    private Generation generation;

    @XmlElement(name = "chronicle")
    private String chronicle;

    @XmlElement(name = "experience")
    private int experience;

    @XmlElement(name = "nature")
    @XmlJavaTypeAdapter(NatureKeyAdapter.class)
    private Nature nature;

    @XmlElement(name = "hideout")
    private String hideout;

    @XmlElement(name = "player")
    private String player;

    @XmlElement(name = "demeanor")
    private String demeanor;

    @XmlElement(name = "concept")
    private String concept;

    @XmlElement(name = "sire")
    private String sire;

    @XmlElement(name = "sect")
    private String sect;

    @XmlElement(name = "attributes")
    @XmlJavaTypeAdapter(AttributeMapAdapter.class)
    private HashMap<String, Attribute> attributes;

    @XmlElement(name = "abilities")
    @XmlJavaTypeAdapter(AbilityMapAdapter.class)
    private HashMap<String, Ability> abilities;

    @XmlElement(name = "advantages")
    @XmlJavaTypeAdapter(AdvantageMapAdapter.class)
    private HashMap<String, Advantage> advantages;

    @XmlElement(name = "merits")
    @XmlJavaTypeAdapter(MeritMapAdapter.class)
    private HashMap<String, Merit> merits;

    @XmlElement(name = "flaws")
    @XmlJavaTypeAdapter(FlawMapAdapter.class)
    private HashMap<String, Flaw> flaws;

    @XmlElement(name = "road")
    @XmlJavaTypeAdapter(RoadXmlAdapter.class)
    private Road road;

    @XmlElement(name = "path")
    @XmlJavaTypeAdapter(PathXmlAdapter.class)
    private Road path;

    @XmlElement(name = "willpower")
    private int willpower;

    @XmlElement(name = "usedWillpower")
    private int usedWillpower;

    @XmlElement(name = "bloodPool")
    private int bloodPool;

    @XmlElement(name = "age")
    private int age;

    @XmlElement(name = "apparentAge")
    private int apparentAge;

    @XmlElement(name = "dayOfBirth", nillable = true)
    @XmlJavaTypeAdapter(DateStringAdapter.class)
    private Date dayOfBirth;

    @XmlElement(name = "dayOfDeath", nillable = true)
    @XmlJavaTypeAdapter(DateStringAdapter.class)
    private Date dayOfDeath;

    @XmlElement(name = "hairColor")
    private String hairColor;

    @XmlElement(name = "eyeColor")
    private String eyeColor;

    @XmlElement(name = "skinColor")
    private String skinColor;

    @XmlElement(name = "nationality")
    private String nationality;

    @XmlElement(name = "height")
    private int height;

    @XmlElement(name = "weight")
    private int weight;

    @XmlElement(name = "sex")
    private Sex sex;

    @XmlElement(name = "story")
    private String story;

    @XmlElement(name = "description")
    private String description;

    protected Character()
    {
        super();
    }

    /**
     * Get a list of attributes by type.
     *
     * @param type Type of attributes to get
     *
     * @return List of attributes
     */
    public ArrayList<Attribute> getAttributesByType(AttributeInterface.AttributeType type) {
        ArrayList<Attribute> attributes = (ArrayList<Attribute>) this.attributes.values().stream()
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
        ArrayList<Ability> abilities = (ArrayList<Ability>) this.abilities.values().stream()
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
        ArrayList<Advantage> advantages = (ArrayList<Advantage>) this.advantages.values().stream()
            .filter((advantage) -> (advantage.getType() == type)).collect(Collectors.toList());
        advantages.sort(new StringComparator());

        return advantages;
    }

    public boolean isAttribute(String key) {
        return this.attributes.containsKey(key);
    }

    public boolean isAbility(String key) {
        return this.abilities.containsKey(key);
    }

    public boolean isAdvantage(String key) {
        return this.advantages.containsKey(key);
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
            this.attributes = new HashMap<>();
            this.abilities = new HashMap<>();
            this.advantages = new HashMap<>();
            this.merits = new HashMap<>();
            this.flaws = new HashMap<>();
        }

        public B addAttribute(Attribute attribute) {
            this.attributes.put(attribute.getKey(), attribute);

            return this.self();
        }

        public B addAbility(Ability ability) {
            this.abilities.put(ability.getKey(), ability);

            return this.self();
        }

        public B addAdvantage(Advantage advantage) {
            this.advantages.put(advantage.getKey(), advantage);

            return this.self();
        }

        public int getAdvantageValue(String key)
        {
            if (this.advantages == null) {
                return 0;
            }

            Advantage advantage = this.advantages.get(key);
            return advantage != null ? advantage.getValue() : 0;
        }

        public B initializeWillpowerFromCourage()
        {
            this.willpower = this.getAdvantageValue("courage");
            return this.self();
        }

        public int calculateInitialBloodPool(int dieRoll)
        {
            if (dieRoll < 1 || dieRoll > 6) {
                throw new IllegalArgumentException("Die roll must be between 1 and 6");
            }

            int domain = this.getAdvantageValue("domain");
            int herd = this.getAdvantageValue("herd");
            int rolledBloodPool = dieRoll + domain + herd;
            int generationMaximum = this.generation != null ? this.generation.getMaximumBloodPool() : Integer.MAX_VALUE;

            return Math.min(generationMaximum, rolledBloodPool);
        }

        public B initializeBloodPoolFromRoll(IntSupplier d6Supplier)
        {
            this.bloodPool = this.calculateInitialBloodPool(d6Supplier.getAsInt());

            return this.self();
        }

        public B addMerit(Merit merit) {
            this.merits.put(merit.getKey(), merit);

            return this.self();
        }

        public B addFlaw(Flaw flaw) {
            this.flaws.put(flaw.getKey(), flaw);

            return this.self();
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

            if (!this.npc && this.clan == null) {
                throw new EntityException("Missing clan");
            }

            if (this.generation == null) {
                throw new EntityException("Missing generation");
            }

            if (!this.npc && this.road == null) {
                throw new EntityException("Missing road");
            }

            this.checkAttributes();
            this.checkAbilities();
            this.checkAdvantages();
        }

        @Override
        protected void executeAdditionalCalculations()
        {
            if (this.road != null) {
                this.calculateRoadScore();
            }
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

        private void calculateRoadScore()
        {
            ArrayList<Advantage> advantages = (ArrayList<Advantage>) this.advantages.values().stream()
                .filter((advantage) -> (advantage.getType() == AdvantageInterface.AdvantageType.VIRTUE))
                .collect(Collectors.toList());
            int roadScore = Road.calculateRoadScore(advantages);

            Road.RoadBuilder<?, ?> roadBuilder = this.road.toBuilder();
            roadBuilder.setValue(roadScore);
            this.road = roadBuilder.build();

            if (this.path != null) {
                Road.RoadBuilder<?, ?> pathBuilder = this.path.toBuilder();
                pathBuilder.setValue(roadScore);
                this.path = pathBuilder.build();
            }
        }
    }
}
