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
package vampireEditor;

import java.util.ArrayList;
import java.util.Date;
import vampireEditor.character.*;

/**
 * Character object.
 *
 * @author Marian Pollzien
 */
public class Character {
    private String name;
    private Clan clan;
    private Generation generation;
    private String chronicle;
    private int experience;
    private String nature;
    private String sire;
    private String player;
    private String hideout;
    private String concept;
    private String sect;
    private final ArrayList<Attribute> attributes;
    private final ArrayList<Ability> abilities;
    private final ArrayList<Advantage> benefits;
    private final ArrayList<Merit> advantages;
    private final ArrayList<Flaw> disadvantages;
    /**
     * List of additional attributes/traits.
     */
    private final ArrayList<Trait> traits;
    private int humanity;
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
     * Create a new character with every possible property.
     *
     * @param name
     * @param clan
     * @param generation
     * @param chronicle
     * @param experience
     * @param nature
     * @param sire
     * @param player
     * @param hideout
     * @param concept
     * @param sect
     * @param attributes
     * @param skills
     * @param benefits
     * @param advantages
     * @param disadvantages
     * @param traits
     * @param humanity
     * @param willpower
     * @param bloodStock
     * @param age
     * @param looksLikeAge
     * @param dayOfBirth
     * @param dayOfDeath
     * @param hairColor
     * @param eyeColor
     * @param skinColor
     * @param nationality
     * @param size
     * @param weight
     * @param sex
     * @param story
     * @param description
     */
    public Character(
        String name, Clan clan, Generation generation, String chronicle,
        int experience, String nature, String sire, String player,
        String hideout, String concept, String sect,
        ArrayList<Attribute> attributes, ArrayList<Ability> skills,
        ArrayList<Advantage> benefits, ArrayList<Merit> advantages,
        ArrayList<Flaw> disadvantages, ArrayList<Trait> traits,
        int humanity, int willpower, int bloodStock, int age, int looksLikeAge,
        Date dayOfBirth, Date dayOfDeath, String hairColor, String eyeColor,
        String skinColor, String nationality, int size, int weight, Sex sex,
        String story, String description
    ) {
        this.name = name;
        this.clan = clan;
        this.generation = generation;
        this.chronicle = chronicle;
        this.experience = experience;
        this.nature = nature;
        this.sire = sire;
        this.player = player;
        this.hideout = hideout;
        this.concept = concept;
        this.sect = sect;
        this.attributes = attributes;
        this.abilities = skills;
        this.benefits = benefits;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.traits = traits;
        this.humanity = humanity;
        this.willpower = willpower;
        this.bloodStock = bloodStock;
        this.age = age;
        this.looksLikeAge = looksLikeAge;
        this.dayOfBirth = dayOfBirth;
        this.dayOfDeath = dayOfDeath;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.skinColor = skinColor;
        this.nationality = nationality;
        this.size = size;
        this.weight = weight;
        this.sex = sex;
        this.story = story;
        this.description = description;
    }

    /**
     * Get the name of the character.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get the clan the character is in.
     *
     * @return
     */
    public Clan getClan() {
        return clan;
    }

    /**
     * Get the generation in which the character is created into.
     *
     * @return
     */
    public Generation getGeneration() {
        return generation;
    }

    /**
     * Get the chronicle where this is playing.
     *
     * @return
     */
    public String getChronicle() {
        return chronicle;
    }

    /**
     * Get the amount of experience the character has.
     *
     * @return
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Get the nature of the character.
     *
     * @return
     */
    public String getNature() {
        return nature;
    }

    /**
     * Get the sire of the character.
     *
     * @return
     */
    public String getSire() {
        return sire;
    }

    /**
     * Get the player of the character.
     *
     * @return
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Get the hideout of the character.
     *
     * @return
     */
    public String getHideout() {
        return hideout;
    }

    /**
     * Get the concept on which the character is based.
     *
     * @return
     */
    public String getConcept() {
        return concept;
    }

    /**
     * Get the sect the character is in.
     *
     * @return
     */
    public String getSect() {
        return sect;
    }

    /**
     * Get the list of attributes of the character.
     *
     * @return
     */
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Get the list of abilities the character has.
     *
     * @return
     */
    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    /**
     * Get the list of benefits the character has.
     *
     * @return
     */
    public ArrayList<Advantage> getBenefits() {
        return benefits;
    }

    /**
     * Get the list of advantages the character has.
     *
     * @return
     */
    public ArrayList<Merit> getAdvantages() {
        return advantages;
    }

    /**
     * Get the list of disadvantages the character has.
     *
     * @return
     */
    public ArrayList<Flaw> getDisadvantages() {
        return disadvantages;
    }

    /**
     * Get the list of additional attributes/traits the character has.
     *
     * @return
     */
    public ArrayList<Trait> getTraits() {
        return traits;
    }

    /**
     * Get the amount humanity of the character.
     *
     * @return
     */
    public int getHumanity() {
        return humanity;
    }

    /**
     * Get the amount of willpower the character has left.
     *
     * @return
     */
    public int getWillpower() {
        return willpower;
    }

    /**
     * Get the characters current blood stock. The maximum amount for the blood
     * stock is limited by the generation.
     *
     * @return
     */
    public int getBloodStock() {
        return bloodStock;
    }

    /**
     * Get the actual age of the character.
     *
     * @return
     */
    public int getAge() {
        return age;
    }

    /**
     * Get the age what the character looks like.
     *
     * @return
     */
    public int getLooksLikeAge() {
        return looksLikeAge;
    }

    /**
     * Get the day of birth of the character.
     *
     * @return
     */
    public Date getDayOfBirth() {
        return dayOfBirth;
    }

    /**
     * Get the day of the death of the character.
     *
     * @return
     */
    public Date getDayOfDeath() {
        return dayOfDeath;
    }

    /**
     * Get the characters hair color.
     *
     * @return
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * Get the characters eye color.
     *
     * @return
     */
    public String getEyeColor() {
        return eyeColor;
    }

    /**
     * Get the characters skin color.
     *
     * @return
     */
    public String getSkinColor() {
        return skinColor;
    }

    /**
     * Get the nationality of the character.
     *
     * @return
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Get the size of the character in cm.
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the weight of the character in kg.
     *
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get the sex of the character, this might either be MALE or FEMALE.
     *
     * @return
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Get the story of the character.
     *
     * @return
     */
    public String getStory() {
        return story;
    }

    /**
     * Get the characters description.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the name of the character.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the clan of the character.
     *
     * @param clan
     */
    public void setClan(Clan clan) {
        this.clan = clan;
    }

    /**
     * Set the generation the character is made into.
     *
     * @param generation
     */
    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

    /**
     * Set the chronicle in which the character is played.
     *
     * @param chronicle
     */
    public void setChronicle(String chronicle) {
        this.chronicle = chronicle;
    }

    /**
     * Set the amount of experience for the character.
     *
     * @param experience
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Set the nature of the character.
     *
     * @param nature
     */
    public void setNature(String nature) {
        this.nature = nature;
    }

    /**
     * Set the characters sire.
     *
     * @param sire
     */
    public void setSire(String sire) {
        this.sire = sire;
    }

    /**
     * Set the player of the character.
     *
     * @param player
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Set the hideout of the character.
     *
     * @param hideout
     */
    public void setHideout(String hideout) {
        this.hideout = hideout;
    }

    /**
     * Set the concept the character is based upon.
     *
     * @param concept
     */
    public void setConcept(String concept) {
        this.concept = concept;
    }

    /**
     * Set the sect the character is in.
     *
     * @param sect
     */
    public void setSect(String sect) {
        this.sect = sect;
    }

    /**
     * Set the amount of humanity that's left in the character.
     *
     * @param humanity
     */
    public void setHumanity(int humanity) {
        this.humanity = humanity;
    }

    /**
     * Set the characters willpower.
     *
     * @param willpower
     */
    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    /**
     * Set the characters blood stock. This value can't be bigger than the
     * maximum given by the generation.
     *
     * @param bloodStock
     */
    public void setBloodStock(int bloodStock) {
        if (bloodStock > this.getGeneration().getMaximumBloodStock()) {
            this.bloodStock = this.getGeneration().getMaximumBloodStock();
            return;
        }

        this.bloodStock = bloodStock;
    }

    /**
     * Set the actual age of the character.
     *
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Set the age the character looks like.
     *
     * @param looksLikeAge
     */
    public void setLooksLikeAge(int looksLikeAge) {
        this.looksLikeAge = looksLikeAge;
    }

    /**
     * Set the characters day of birth.
     *
     * @param dayOfBirth
     */
    public void setDayOfBirth(Date dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    /**
     * Set the day of death of the character.
     *
     * @param dayOfDeath
     */
    public void setDayOfDeath(Date dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    /**
     * Set the characters hair color.
     *
     * @param hairColor
     */
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * Set the characters eye color.
     *
     * @param eyeColor
     */
    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    /**
     * Set the characters skin color.
     *
     * @param skinColor
     */
    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    /**
     * Set the nationality of the character.
     *
     * @param nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Set the size of the character in cm.
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Set the weight of the character in kg.
     *
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Set the sex of the Character. This might either be MALE or FEMALE.
     *
     * @param sex
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * Set the characters story.
     *
     * @param story
     */
    public void setStory(String story) {
        this.story = story;
    }

    /**
     * Set the description of the character.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
