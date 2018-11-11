/**
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
package vampireEditor.entity;

import vampireEditor.entity.character.Generation;
import vampireEditor.entity.character.Attribute;
import vampireEditor.entity.character.Flaw;
import vampireEditor.entity.character.Advantage;
import vampireEditor.entity.character.Clan;
import vampireEditor.entity.character.Road;
import vampireEditor.entity.character.Merit;
import vampireEditor.entity.character.Ability;
import java.util.ArrayList;
import java.util.Date;
import vampireEditor.Configuration;

/**
 * Character object.
 */
public class Character extends BaseEntity {
    private final String name;
    private final Clan clan;
    private final Generation generation;
    private final String chronicle;
    private int experience = 0;
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
    private final int bloodStock;
    private final int age;
    private final int looksLikeAge;
    private final Date dayOfBirth;
    private final Date dayOfDeath;
    private final String hairColor;
    private final String eyeColor;
    private final String skinColor;
    private final String nationality;
    private final int size;
    private final int weight;
    private final Sex sex;
    private final String story;
    private final String description;

    /**
     * List of sexes for the character.
     */
    public static enum Sex {
        MALE,
        FEMALE;

        @Override
        public String toString() {
            Configuration configuration = new Configuration();
            configuration.loadProperties();

            return configuration.getLanguageObject().translate(this.name());
        }
    }

    /**
     * Builder for character objects.
     */
    public static class Builder extends BaseEntity.Builder<Builder> {
        private String name;
        private Clan clan;
        private Generation generation;
        private String chronicle;
        private int experience = 0;
        private String nature;
        private String hideout;
        private String player;
        private String demeanor;
        private String concept;
        private String sire;
        private String sect;
        private ArrayList<Attribute> attributes;
        private ArrayList<Ability> abilities;
        private ArrayList<Advantage> advantages;
        private ArrayList<Merit> merits;
        private ArrayList<Flaw> flaws;
        private Road road;
        private int willpower;
        private int bloodStock;
        private int age;
        private int looksLikeAge;
        private Date dayOfBirth;
        private Date dayOfDeath;
        private String hairColor;
        private String eyeColor;
        private String skinColor;
        private String nationality;
        private int size;
        private int weight;
        private Sex sex;
        private String story;
        private String description;

        /**
         * Create a new character builder.
         */
        public Builder() {
            this.attributes = new ArrayList<>();
            this.abilities = new ArrayList<>();
            this.advantages = new ArrayList<>();
            this.merits = new ArrayList<>();
            this.flaws = new ArrayList<>();
        }

        /**
         * Build a new character object.
         *
         * @return
         * @throws vampireEditor.entity.EntityException
         */
        @Override
        public Character build() throws EntityException {
            this.checkValues();

            return new Character(this);
        }

        /**
         * Check if all necessary values are set.
         *
         * @throws EntityException
         */
        @Override
        protected void checkValues() throws EntityException {
            if (this.name.isEmpty()) {
                throw new EntityException("Missing name");
            }

            if (this.clan == null) {
                throw new EntityException("Missing clan");
            }

            if (this.generation == null) {
                throw new EntityException("Missing generation");
            }

            if (this.nature.isEmpty()) {
                throw new EntityException("Missing nature");
            }

            if (this.demeanor.isEmpty()) {
                throw new EntityException("Missing demeanor");
            }

            if (this.concept.isEmpty()) {
                throw new EntityException("Missing concept");
            }

            this.checkAttributes();
            this.checkAbilities();
            this.checkAdvantages();
        }

        /**
         * Get the current instance.
         *
         * @return
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Check if the attributes are set correctly.
         *
         * @throws EntityException
         */
        protected void checkAttributes() throws EntityException {
            if (this.attributes.isEmpty()) {
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
         * @throws EntityException
         */
        protected void checkAbilities() throws EntityException {
            if (this.abilities.isEmpty()) {
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
         * @throws EntityException
         */
        protected void checkAdvantages() throws EntityException {
            if (this.advantages.isEmpty()) {
                throw new EntityException("Advantages are empty");
            }

            if (this.advantages.size() < 6) {
                throw new EntityException("Missing advantages");
            }
        }

        public Builder setName(String name) {
            this.name = name;

            return this.self();
        }

        public Builder setClan(Clan clan) {
            this.clan = clan;

            return this.self();
        }

        public Builder setGeneration(Generation generation) {
            this.generation = generation;

            return this.self();
        }

        public Builder setChronicle(String chronicle) {
            this.chronicle = chronicle;

            return this.self();
        }

        public Builder setExperience(int experience) {
            this.experience = experience;

            return this.self();
        }

        public Builder setNature(String nature) {
            this.nature = nature;

            return this.self();
        }

        public Builder setHideout(String hideout) {
            this.hideout = hideout;

            return this.self();
        }

        public Builder setPlayer(String player) {
            this.player = player;

            return this.self();
        }

        public Builder setDemeanor(String demeanor) {
            this.demeanor = demeanor;

            return this.self();
        }

        public Builder setConcept(String concept) {
            this.concept = concept;

            return this.self();
        }

        public Builder setSire(String sire) {
            this.sire = sire;

            return this.self();
        }

        public Builder setSect(String sect) {
            this.sect = sect;

            return this.self();
        }

        public Builder setAttributes(ArrayList<Attribute> attributes) {
            this.attributes = attributes;

            return this.self();
        }

        public Builder setAbilities(ArrayList<Ability> abilities) {
            this.abilities = abilities;

            return this.self();
        }

        public Builder setAdvantages(ArrayList<Advantage> advantages) {
            this.advantages = advantages;

            return this.self();
        }

        public Builder setMerits(ArrayList<Merit> merits) {
            this.merits = merits;

            return this.self();
        }

        public Builder setFlaws(ArrayList<Flaw> flaws) {
            this.flaws = flaws;

            return this.self();
        }

        public Builder setRoad(Road road) {
            this.road = road;

            return this.self();
        }

        public Builder setWillpower(int willpower) {
            this.willpower = willpower;

            return this.self();
        }

        public Builder setBloodStock(int bloodStock) {
            this.bloodStock = bloodStock;

            return this.self();
        }

        public Builder setAge(int age) {
            this.age = age;

            return this.self();
        }

        public Builder setLooksLikeAge(int looksLikeAge) {
            this.looksLikeAge = looksLikeAge;

            return this.self();
        }

        public Builder setDayOfBirth(Date dayOfBirth) {
            this.dayOfBirth = dayOfBirth;

            return this.self();
        }

        public Builder setDayOfDeath(Date dayOfDeath) {
            this.dayOfDeath = dayOfDeath;

            return this.self();
        }

        public Builder setHairColor(String hairColor) {
            this.hairColor = hairColor;

            return this.self();
        }

        public Builder setEyeColor(String eyeColor) {
            this.eyeColor = eyeColor;

            return this.self();
        }

        public Builder setSkinColor(String skinColor) {
            this.skinColor = skinColor;

            return this.self();
        }

        public Builder setNationality(String nationality) {
            this.nationality = nationality;

            return this.self();
        }

        public Builder setSize(int size) {
            this.size = size;

            return this.self();
        }

        public Builder setWeight(int weight) {
            this.weight = weight;

            return this.self();
        }

        public Builder setSex(Sex sex) {
            this.sex = sex;

            return this.self();
        }

        public Builder setStory(String story) {
            this.story = story;

            return this.self();
        }

        public Builder setDescription(String description) {
            this.description = description;

            return this.self();
        }

        public Builder addAttribute(Attribute attribute) {
            this.attributes.add(attribute);

            return this.self();
        }

        public Builder addAbility(Ability ability) {
            this.abilities.add(ability);

            return this.self();
        }

        public Builder addAdvantage(Advantage advantage) {
            this.advantages.add(advantage);

            return this.self();
        }

        public Builder addMerit(Merit merit) {
            this.merits.add(merit);

            return this.self();
        }

        public Builder addFlaw(Flaw flaw) {
            this.flaws.add(flaw);

            return this.self();
        }
    }

    /**
     * Create a new character.
     *
     * @param builder
     */
    protected Character(Builder builder) {
        super(builder);

        this.name = builder.name;
        this.clan = builder.clan;
        this.generation = builder.generation;
        this.chronicle = builder.chronicle;
        this.experience = builder.experience;
        this.nature = builder.nature;
        this.hideout = builder.hideout;
        this.player = builder.player;
        this.demeanor = builder.demeanor;
        this.concept = builder.concept;
        this.sire = builder.sire;
        this.sect = builder.sect;
        this.attributes = builder.attributes;
        this.abilities = builder.abilities;
        this.advantages = builder.advantages;
        this.merits = builder.merits;
        this.flaws = builder.flaws;
        this.road = builder.road;
        this.willpower = builder.willpower;
        this.bloodStock = builder.bloodStock;
        this.age = builder.age;
        this.looksLikeAge = builder.looksLikeAge;
        this.dayOfBirth = builder.dayOfBirth;
        this.dayOfDeath = builder.dayOfDeath;
        this.hairColor = builder.hairColor;
        this.eyeColor = builder.eyeColor;
        this.skinColor = builder.skinColor;
        this.nationality = builder.nationality;
        this.size = builder.size;
        this.weight = builder.weight;
        this.sex = builder.sex;
        this.story = builder.story;
        this.description = builder.description;
    }

    /**
     * Get the name of the character.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the clan the character is in.
     *
     * @return
     */
    public Clan getClan() {
        return this.clan;
    }

    /**
     * Get the generation in which the character is created into.
     *
     * @return
     */
    public Generation getGeneration() {
        return this.generation;
    }

    /**
     * Get the chronicle where this is playing.
     *
     * @return
     */
    public String getChronicle() {
        return this.chronicle;
    }

    /**
     * Get the amount of experience the character has.
     *
     * @return
     */
    public int getExperience() {
        return this.experience;
    }

    /**
     * Get the nature of the character.
     *
     * @return
     */
    public String getNature() {
        return this.nature;
    }

    /**
     * Get the hideout of the character.
     *
     * @return
     */
    public String getHideout() {
        return this.hideout;
    }

    /**
     * Get the player of the character.
     *
     * @return
     */
    public String getPlayer() {
        return this.player;
    }

    /**
     * Get the demeanor of the character.
     *
     * @return
     */
    public String getDemeanor() {
        return this.demeanor;
    }

    /**
     * Get the concept on which the character is based.
     *
     * @return
     */
    public String getConcept() {
        return this.concept;
    }

    /**
     * Get the sire of the character.
     *
     * @return
     */
    public String getSire() {
        return this.sire;
    }

    /**
     * Get the sect the character is in.
     *
     * @return
     */
    public String getSect() {
        return this.sect;
    }

    /**
     * Get the list of attributes of the character.
     *
     * @return
     */
    public ArrayList<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Get the list of abilities the character has.
     *
     * @return
     */
    public ArrayList<Ability> getAbilities() {
        return this.abilities;
    }

    /**
     * Get the list of advantages the character has.
     *
     * @return
     */
    public ArrayList<Advantage> getAdvantages() {
        return this.advantages;
    }

    /**
     * Get the list of merits the character has.
     *
     * @return
     */
    public ArrayList<Merit> getMerits() {
        return this.merits;
    }

    /**
     * Get the list of flaws the character has.
     *
     * @return
     */
    public ArrayList<Flaw> getFlaws() {
        return this.flaws;
    }

    /**
     * Get the amount humanity of the character.
     *
     * @return
     */
    public Road getRoad() {
        return this.road;
    }

    /**
     * Get the amount of willpower the character has left.
     *
     * @return
     */
    public int getWillpower() {
        return this.willpower;
    }

    /**
     * Get the characters current blood stock. The maximum amount for the blood
     * stock is limited by the generation.
     *
     * @return
     */
    public int getBloodStock() {
        return this.bloodStock;
    }

    /**
     * Get the actual age of the character.
     *
     * @return
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Get the age what the character looks like.
     *
     * @return
     */
    public int getLooksLikeAge() {
        return this.looksLikeAge;
    }

    /**
     * Get the day of birth of the character.
     *
     * @return
     */
    public Date getDayOfBirth() {
        return this.dayOfBirth;
    }

    /**
     * Get the day of the death of the character.
     *
     * @return
     */
    public Date getDayOfDeath() {
        return this.dayOfDeath;
    }

    /**
     * Get the characters hair color.
     *
     * @return
     */
    public String getHairColor() {
        return this.hairColor;
    }

    /**
     * Get the characters eye color.
     *
     * @return
     */
    public String getEyeColor() {
        return this.eyeColor;
    }

    /**
     * Get the characters skin color.
     *
     * @return
     */
    public String getSkinColor() {
        return this.skinColor;
    }

    /**
     * Get the nationality of the character.
     *
     * @return
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * Get the size of the character in cm.
     *
     * @return
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Get the weight of the character in kg.
     *
     * @return
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Get the sex of the character, this might either be MALE or FEMALE.
     *
     * @return
     */
    public Sex getSex() {
        return this.sex;
    }

    /**
     * Get the story of the character.
     *
     * @return
     */
    public String getStory() {
        return this.story;
    }

    /**
     * Get the characters description.
     *
     * @return
     */
    public String getDescription() {
        return this.description;
    }
}
