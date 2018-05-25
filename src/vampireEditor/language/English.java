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
package vampireEditor.language;

import java.util.HashMap;

/**
 * Language class for english.
 *
 * @author Marian Pollzien
 */
public class English implements LanguageInterface {
    private final HashMap<String, String> translations = new HashMap<>();

    /**
     * Create a new english language object.
     */
    public English() {
        this.translations.put("english", "English");
        this.translations.put("german", "German");

        this.translations.put("title", "Vampire Editor");
        this.translations.put("file", "File");
        this.translations.put("fileMnemonic", "F");
        this.translations.put("quit", "Quit");
        this.translations.put("quitMnemonic", "Q");
        this.translations.put("help", "Help");
        this.translations.put("helpMnemonic", "H");
        this.translations.put("about", "About");
        this.translations.put("aboutMnemonic", "A");
        this.translations.put("close", "Close");
        this.translations.put("aboutText", "This tool was created by Marian Pollzien.");
        this.translations.put("cancel", "Cancel");
        this.translations.put("new", "New");
        this.translations.put("newMnemonic", "N");
        this.translations.put("newCharacter", "New character");
        this.translations.put("looks", "Looks");
        this.translations.put("name", "Name");
        this.translations.put("chronicle", "Chronicle");
        this.translations.put("generation", "Generation");
        this.translations.put("nature", "Nature");
        this.translations.put("hideout", "Hideout");
        this.translations.put("player", "Player");
        this.translations.put("behaviour", "Behaviour");
        this.translations.put("concept", "Concept");
        this.translations.put("maker", "Maker");
        this.translations.put("clan", "Clan");
        this.translations.put("sect", "Sect");
        this.translations.put("age", "Age");
        this.translations.put("looksLikeAge", "Looks like age");
        this.translations.put("dayOfBirth", "Day of birth");
        this.translations.put("dayOfDeath", "Day of death");
        this.translations.put("hairColor", "Hair color");
        this.translations.put("eyeColor", "Eye color");
        this.translations.put("skinColor", "Skin color");
        this.translations.put("nationality", "Nationality");
        this.translations.put("size", "Size");
        this.translations.put("weight", "Weight");
        this.translations.put("sex", "Sex");
        this.translations.put("MALE", "male");
        this.translations.put("FEMALE", "female");
        this.translations.put("required", "Fields marked with * are required.");
        this.translations.put("next", "Next");
        this.translations.put("ok", "Ok");
        this.translations.put("back", "Back");
        this.translations.put("PRIMARY", "Primary");
        this.translations.put("SECONDARY", "Secondary");
        this.translations.put("TERTIARY", "Tertiary");
        this.translations.put("freeAdditionalPoints", "Free additional points");
        this.translations.put("attributes", "Attributes");
        this.translations.put("physical", "Physical");
        this.translations.put("strength", "Strength");
        this.translations.put("dexterity", "Dexterity");
        this.translations.put("stamina", "Stamina");
        this.translations.put("social", "Social");
        this.translations.put("charisma", "Charisma");
        this.translations.put("manipulation", "Manipulation");
        this.translations.put("appearance", "Appearance");
        this.translations.put("mental", "Mental");
        this.translations.put("intelligence", "Intelligence");
        this.translations.put("perception", "Perception");
        this.translations.put("wits", "Wits");
        this.translations.put("abilities", "Abilities");
        this.translations.put("talents", "Talents");
        this.translations.put("acting", "Acting");
        this.translations.put("alertness", "Alertness");
        this.translations.put("athletics", "Athletics");
        this.translations.put("brawl", "Brawl");
        this.translations.put("burglary", "Burglary");
        this.translations.put("dodge", "Dodge");
        this.translations.put("empathy", "Empathy");
        this.translations.put("intimidation", "Intimidation");
        this.translations.put("leadership", "Leadership");
        this.translations.put("subterfuge", "Subterfuge");
        this.translations.put("skills", "Skills");
        this.translations.put("animalKen", "Animal Ken");
        this.translations.put("archery", "Archery");
        this.translations.put("crafts", "Crafts");
        this.translations.put("etiquette", "Etiquette");
        this.translations.put("herbalism", "Herbalism");
        this.translations.put("melee", "Melee");
        this.translations.put("music", "Music");
        this.translations.put("ride", "Ride");
        this.translations.put("stealth", "Stealth");
        this.translations.put("survival", "Survival");
        this.translations.put("knowledges", "Knowledges");
        this.translations.put("academicKnowledge", "Academic knowledge");
        this.translations.put("administration", "Administration");
        this.translations.put("folklore", "Folklore");
        this.translations.put("investigation", "Investigation");
        this.translations.put("law", "Law");
        this.translations.put("linguistics", "Linguistics");
        this.translations.put("medicine", "Medicine");
        this.translations.put("occult", "Occult");
        this.translations.put("politics", "Politics");
        this.translations.put("science", "Science");
        this.translations.put("advantages", "Advantages");
        this.translations.put("background", "Background");
        this.translations.put("disciplins", "Disciplins");
        this.translations.put("virtues", "Virtues");
    }

    /**
     * Get the translation for the given key.
     *
     * @param key The key to translate.
     *
     * @return
     */
    @Override
    public String translate(String key) {
        return this.translations.get(key);
    }

    /**
     * Get the language.
     *
     * @return
     */
    @Override
    public String getLanguage() {
        return "English";
    }
}
