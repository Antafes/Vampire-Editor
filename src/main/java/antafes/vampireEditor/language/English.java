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
package antafes.vampireEditor.language;

/**
 * Language class for english.
 *
 * @author Marian Pollzien
 */
public class English extends Language {
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
        this.unsavedCharacters();
    }

    /**
     * List of available languages.
     */
    private void languages() {
        this.getTranslations().put("english", "English");
        this.getTranslations().put("german", "German");
    }

    /**
     * General texts like the programs title.
     */
    private void general() {
        this.getTranslations().put("title", "Vampire Editor");
        this.getTranslations().put("darkAgesVampire", "Dark Ages: Vampire");
        this.getTranslations().put("cancel", "Cancel");
        this.getTranslations().put("next", "Next");
        this.getTranslations().put("ok", "Ok");
        this.getTranslations().put("back", "Back");
        this.getTranslations().put("finish", "Finish");
        this.getTranslations().put("other", "Other");
        this.getTranslations().put("description", "Description");
    }

    /**
     * Texts for the menu entries.
     */
    private void menu() {
        this.getTranslations().put("file", "File");
        this.getTranslations().put("fileMnemonic", "F");
        this.getTranslations().put("quit", "Quit");
        this.getTranslations().put("quitMnemonic", "Q");
        this.getTranslations().put("help", "Help");
        this.getTranslations().put("helpMnemonic", "H");
        this.getTranslations().put("about", "About");
        this.getTranslations().put("aboutMnemonic", "A");
        this.getTranslations().put("new", "New");
        this.getTranslations().put("newMnemonic", "N");
        this.getTranslations().put("open", "Open");
        this.getTranslations().put("openMnemonic", "O");
        this.getTranslations().put("save", "Save");
        this.getTranslations().put("saveMnemonic", "S");
        this.getTranslations().put("print", "Print");
        this.getTranslations().put("printMnemonic", "P");
        this.getTranslations().put("existingFile", "Existing file");
        this.getTranslations().put("fileExists", "The selected file already exists, overwrite?");
        this.getTranslations().put("couldNotLoad", "Could not load");
        this.getTranslations().put("couldNotLoadCharacter", "Could not load the character.");
        this.getTranslations().put("close", "Close");
        this.getTranslations().put("loadingCharacter", "Loading character, please wait");
    }

    /**
     * Content of the about dialog.
     */
    private void about() {
        this.getTranslations().put("aboutText", "This tool was created by Marian Pollzien.\n\nPortions of the materials are the copyrights and trademarks of Paradox Interactive AB, and are used with permission. All rights reserved. For more information please visit worldofdarkness.com.");
    }

    /**
     * Texts for character creation and display.
     */
    private void character() {
        this.getTranslations().put("newCharacter", "New character");
        this.getTranslations().put("looks", "Looks");
        this.getTranslations().put("name", "Name");
        this.getTranslations().put("chronicle", "Chronicle");
        this.getTranslations().put("generation", "Generation");
        this.getTranslations().put("nature", "Nature");
        this.getTranslations().put("hideout", "Hideout");
        this.getTranslations().put("player", "Player");
        this.getTranslations().put("demeanor", "Demeanor");
        this.getTranslations().put("concept", "Concept");
        this.getTranslations().put("sire", "Sire");
        this.getTranslations().put("clan", "Clan");
        this.getTranslations().put("sect", "Sect");
        this.getTranslations().put("age", "Age");
        this.getTranslations().put("apparentAge", "Apparent age");
        this.getTranslations().put("dayOfBirth", "Day of birth");
        this.getTranslations().put("dayOfDeath", "R.I.P.");
        this.getTranslations().put("hairColor", "Hair");
        this.getTranslations().put("eyeColor", "Eye");
        this.getTranslations().put("skinColor", "Race");
        this.getTranslations().put("nationality", "Nationality");
        this.getTranslations().put("height", "Height");
        this.getTranslations().put("weight", "Weight");
        this.getTranslations().put("sex", "Sex");
        this.getTranslations().put("MALE", "male");
        this.getTranslations().put("FEMALE", "female");
        this.getTranslations().put("required", "Fields marked with * are required.");
        this.getTranslations().put("PRIMARY", "Primary");
        this.getTranslations().put("SECONDARY", "Secondary");
        this.getTranslations().put("TERTIARY", "Tertiary");
        this.getTranslations().put("freeAdditionalPoints", "Free additional points");
        this.getTranslations().put("attributes", "Attributes");
        this.getTranslations().put("physical", "Physical");
        this.getTranslations().put("social", "Social");
        this.getTranslations().put("mental", "Mental");
        this.getTranslations().put("abilities", "Abilities");
        this.getTranslations().put("talents", "Talents");
        this.getTranslations().put("talent", "Talent");
        this.getTranslations().put("skills", "Skills");
        this.getTranslations().put("skill", "Skill");
        this.getTranslations().put("knowledge", "Knowledge");
        this.getTranslations().put("advantages", "Advantages");
        this.getTranslations().put("background", "Background");
        this.getTranslations().put("disciplines", "Disciplines");
        this.getTranslations().put("discipline", "Discipline");
        this.getTranslations().put("virtues", "Virtues");
        this.getTranslations().put("virtue", "Virtue");
        this.getTranslations().put("lastSteps", "Last steps");
        this.getTranslations().put("merits", "Merits");
        this.getTranslations().put("flaws", "Flaws");
        this.getTranslations().put("road", "Road");
        this.getTranslations().put("humanity", "Humanity");
        this.getTranslations().put("roadOf", "Road of ...");
        this.getTranslations().put("flawInfoTooMany", "Your selected flaws exceed the maximum of 7 points.");
        this.getTranslations().put("general", "General");
        this.getTranslations().put("meritsAndFlaws", "Merits and flaws");
        this.getTranslations().put("willpower", "Willpower");
        this.getTranslations().put("bloodPool", "Blood pool");
        this.getTranslations().put("printPreview", "Print preview");
        this.getTranslations().put("otherTraits", "Other traits");
        this.getTranslations().put("experience", "Experience");
        this.getTranslations().put("roadScore", "Score");
    }

    /**
     * Add texts only used on the print and print preview
     */
    private void print() {
        this.getTranslations().put("printCharacter", "Print character");

        this.getTranslations().put("merit", "Merit");
        this.getTranslations().put("flaw", "Flaw");
        this.getTranslations().put("rituals", "Rituals");
        this.getTranslations().put("paths", "Paths");
        this.getTranslations().put("ritual", "Ritual");
        this.getTranslations().put("path", "Path");
        this.getTranslations().put("source", "Source");
        this.getTranslations().put("system", "System");
        this.getTranslations().put("combat", "Combat");
        this.getTranslations().put("armor", "Armor");
        this.getTranslations().put("weapon/attack", "Weapon/Attack");
        this.getTranslations().put("diff.", "Diff.");
        this.getTranslations().put("damage", "Damage");
        this.getTranslations().put("range", "Range");
        this.getTranslations().put("rate", "Rate");
        this.getTranslations().put("ammo", "Ammo");
        this.getTranslations().put("conceal", "Conceal");
        this.getTranslations().put("class", "Class");
        this.getTranslations().put("rating", "Rating");
        this.getTranslations().put("penalty", "Penalty");
        this.getTranslations().put("backgrounds", "Backgrounds");
        this.getTranslations().put("possessions", "Possessions");
        this.getTranslations().put("haven", "Haven");
        this.getTranslations().put("allies", "Allies");
        this.getTranslations().put("mentor", "Mentor");
        this.getTranslations().put("contacts", "Contacts");
        this.getTranslations().put("resources", "Resources");
        this.getTranslations().put("domains", "Domains");
        this.getTranslations().put("retainers", "Retainers");
        this.getTranslations().put("herd", "Herd");
        this.getTranslations().put("status", "Status");
        this.getTranslations().put("influence", "Influence");
        this.getTranslations().put("gear", "Gear");
        this.getTranslations().put("equipment", "Equipment");
        this.getTranslations().put("feedingGrounds", "Feeding grounds");
        this.getTranslations().put("miscellaneous", "Miscellaneous");
        this.getTranslations().put("location", "Location");
        this.getTranslations().put("history", "History");
        this.getTranslations().put("momentsOfTruth", "Moments of truth");
        this.getTranslations().put("goalsAndPlots", "Goals & plots");
        this.getTranslations().put("visuals", "Visuals");
        this.getTranslations().put("coterieChart", "Coterie chart");
        this.getTranslations().put("characterSketch", "Character sketch");
    }

    /**
     * Add texts for health
     */
    private void health() {
        this.getTranslations().put("health", "Health");
        this.getTranslations().put("bruised", "Bruised");
        this.getTranslations().put("hurt", "Hurt");
        this.getTranslations().put("injured", "Injured");
        this.getTranslations().put("wounded", "Wounded");
        this.getTranslations().put("mauled", "Mauled");
        this.getTranslations().put("crippled", "Crippled");
        this.getTranslations().put("incapacitated", "Incapacitated");
    }

    private void unsavedCharacters()
    {
        this.getTranslations().put("unsavedCharactersTitle", "Unsaved characters");
        this.getTranslations().put("dontSave", "Don't save");
        this.getTranslations().put("dontSaveMnemonic", "d");
        this.getTranslations().put("unsavedCharacters", "There are unsaved characters open. Do you want to save them now?");
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
