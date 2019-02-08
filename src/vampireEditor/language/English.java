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
        this.languages();
        this.general();
        this.menu();
        this.about();
        this.character();
        this.health();
        this.print();
    }

    /**
     * List of available languages.
     */
    private void languages() {
        this.translations.put("english", "English");
        this.translations.put("german", "German");
    }

    /**
     * General texts like the programs title.
     */
    private void general() {
        this.translations.put("title", "Vampire Editor");
        this.translations.put("darkAgesVampire", "Dark Ages: Vampire");
        this.translations.put("cancel", "Cancel");
        this.translations.put("next", "Next");
        this.translations.put("ok", "Ok");
        this.translations.put("back", "Back");
        this.translations.put("finish", "Finish");
        this.translations.put("other", "Other");
        this.translations.put("description", "Description");
    }

    /**
     * Texts for the menu entries.
     */
    private void menu() {
        this.translations.put("file", "File");
        this.translations.put("fileMnemonic", "F");
        this.translations.put("quit", "Quit");
        this.translations.put("quitMnemonic", "Q");
        this.translations.put("help", "Help");
        this.translations.put("helpMnemonic", "H");
        this.translations.put("about", "About");
        this.translations.put("aboutMnemonic", "A");
        this.translations.put("new", "New");
        this.translations.put("newMnemonic", "N");
        this.translations.put("open", "Open");
        this.translations.put("openMnemonic", "S");
        this.translations.put("save", "Save");
        this.translations.put("saveMnemonic", "S");
        this.translations.put("existingFile", "Existing file");
        this.translations.put("fileExists", "The selected file already exists, overwrite?");
        this.translations.put("couldNotLoad", "Could not load");
        this.translations.put("couldNotLoadCharacter", "Could not load the character.");
        this.translations.put("close", "Close");
    }

    /**
     * Content of the about dialog.
     */
    private void about() {
        this.translations.put("aboutText", "This tool was created by Marian Pollzien.");
    }

    /**
     * Texts for character creation and display.
     */
    private void character() {
        this.translations.put("newCharacter", "New character");
        this.translations.put("looks", "Looks");
        this.translations.put("name", "Name");
        this.translations.put("chronicle", "Chronicle");
        this.translations.put("generation", "Generation");
        this.translations.put("nature", "Nature");
        this.translations.put("hideout", "Hideout");
        this.translations.put("player", "Player");
        this.translations.put("demeanor", "Demeanor");
        this.translations.put("concept", "Concept");
        this.translations.put("sire", "Sire");
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
        this.translations.put("PRIMARY", "Primary");
        this.translations.put("SECONDARY", "Secondary");
        this.translations.put("TERTIARY", "Tertiary");
        this.translations.put("freeAdditionalPoints", "Free additional points");
        this.translations.put("attributes", "Attributes");
        this.translations.put("physical", "Physical");
        this.translations.put("social", "Social");
        this.translations.put("mental", "Mental");
        this.translations.put("abilities", "Abilities");
        this.translations.put("talents", "Talents");
        this.translations.put("skills", "Skills");
        this.translations.put("knowledges", "Knowledges");
        this.translations.put("advantages", "Advantages");
        this.translations.put("background", "Background");
        this.translations.put("disciplins", "Disciplins");
        this.translations.put("virtues", "Virtues");
        this.translations.put("lastSteps", "Last steps");
        this.translations.put("merits", "Merits");
        this.translations.put("flaws", "Flaws");
        this.translations.put("road", "Road");
        this.translations.put("humanity", "Humanity");
        this.translations.put("roadOf", "Road of ...");
        this.translations.put("flawInfoTooMany", "Your selected flaws exceed the maximum of 7 points.");
        this.translations.put("general", "General");
        this.translations.put("meritsAndFlaws", "Merits and flaws");
        this.translations.put("willpower", "Willpower");
        this.translations.put("bloodStock", "Blood pool");
        this.translations.put("printPreview", "Print preview");
        this.translations.put("otherTraits", "Other traits");
        this.translations.put("experience", "Experience");
    }

    /**
     * Add texts only used on the print and print preview
     */
    private void print() {
        this.translations.put("merit", "Merit");
        this.translations.put("flaw", "Flaw");
        this.translations.put("rituals", "Rituals");
        this.translations.put("paths", "Paths");
        this.translations.put("ritual", "Ritual");
        this.translations.put("path", "Path");
        this.translations.put("source", "Source");
        this.translations.put("system", "System");
        this.translations.put("combat", "Combat");
        this.translations.put("armor", "Armor");
        this.translations.put("weapon/attack", "Weapon/Attack");
        this.translations.put("diff.", "Diff.");
        this.translations.put("damage", "Damage");
        this.translations.put("range", "Range");
        this.translations.put("rate", "Rate");
        this.translations.put("ammo", "Ammo");
        this.translations.put("conceal", "Conceal");
        this.translations.put("class", "Class");
        this.translations.put("rating", "Rating");
        this.translations.put("penalty", "Penalty");
        this.translations.put("backgrounds", "Backgrounds");
        this.translations.put("possessions", "Possessions");
        this.translations.put("haven", "Haven");
        this.translations.put("allies", "Allies");
        this.translations.put("mentor", "Mentor");
        this.translations.put("contacts", "Contacts");
        this.translations.put("resources", "Resources");
        this.translations.put("domains", "Domains");
        this.translations.put("retainers", "Retainers");
        this.translations.put("herd", "Herd");
        this.translations.put("status", "Status");
        this.translations.put("influence", "Influence");
        this.translations.put("gear", "Gear");
        this.translations.put("equipment", "Equipment");
        this.translations.put("feedingGrounds", "Feeding grounds");
        this.translations.put("miscellaneous", "Miscellaneous");
        this.translations.put("location", "Location");
        this.translations.put("history", "History");
        this.translations.put("momentsOfTruth", "Moments of truth");
        this.translations.put("goalsAndPlots", "Goals & plots");
        this.translations.put("visuals", "Visuals");
        this.translations.put("coterieChart", "Coterie chart");
        this.translations.put("characterSketch", "Character sketch");
    }

    /**
     * Add texts for health
     */
    private void health() {
        this.translations.put("health", "Health");
        this.translations.put("bruised", "Bruised");
        this.translations.put("hurt", "Hurt");
        this.translations.put("injured", "Injured");
        this.translations.put("wounded", "Wounded");
        this.translations.put("mauled", "Mauled");
        this.translations.put("crippled", "Crippled");
        this.translations.put("incapacitated", "Incapacitated");
    }

    /**
     * Get the translation for the given key.
     *
     * @param key The key to translate.
     *
     * @return The translated string
     */
    @Override
    public String translate(String key) {
        return this.translations.getOrDefault(key, key);
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
