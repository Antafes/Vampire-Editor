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
public class German implements LanguageInterface {
    private final HashMap<String, String> translations = new HashMap<>();

    /**
     * Create a new english language object.
     */
    public German() {
        this.languages();
        this.general();
        this.menu();
        this.about();
        this.character();
        this.health();
    }

    /**
     * List of available languages.
     */
    private void languages() {
        this.translations.put("english", "Englisch");
        this.translations.put("german", "Deutsch");
    }

    /**
     * General texts like the programs title.
     */
    private void general() {
        this.translations.put("title", "Vampire Editor");
        this.translations.put("darkAgesVampire", "Vampire aus der alten Welt");
        this.translations.put("cancel", "Abbrechen");
        this.translations.put("next", "Weiter");
        this.translations.put("ok", "Ok");
        this.translations.put("back", "Zurück");
        this.translations.put("finish", "Fertig");
    }

    /**
     * Texts for the menu entries.
     */
    private void menu() {
        this.translations.put("file", "Datei");
        this.translations.put("fileMnemonic", "D");
        this.translations.put("quit", "Beenden");
        this.translations.put("quitMnemonic", "B");
        this.translations.put("help", "Hilfe");
        this.translations.put("helpMnemonic", "H");
        this.translations.put("about", "Über");
        this.translations.put("aboutMnemonic", "b");
        this.translations.put("new", "Neu");
        this.translations.put("newMnemonic", "N");
        this.translations.put("open", "Öffnen");
        this.translations.put("openMnemonic", "f");
        this.translations.put("save", "Speichern");
        this.translations.put("saveMnemonic", "S");
        this.translations.put("existingFile", "Existierende Datei");
        this.translations.put("fileExists", "Die ausgewählte Datei existiert bereits, überschreiben?");
        this.translations.put("couldNotLoad", "Laden fehlgeschlagen");
        this.translations.put("couldNotLoadCharacter", "Konnte den Charakter nicht laden.");
        this.translations.put("close", "Schließen");
    }

    /**
     * Content of the about dialog.
     */
    private void about() {
        this.translations.put("aboutText", "Dieses Programm wurde von Marian Pollzien erstellt.");
    }

    /**
     * Texts for character creation and display.
     */
    private void character() {
        this.translations.put("newCharacter", "Neuer Charakter");
        this.translations.put("looks", "Aussehen");
        this.translations.put("name", "Name");
        this.translations.put("chronicle", "Chronik");
        this.translations.put("generation", "Generation");
        this.translations.put("nature", "Wesen");
        this.translations.put("hideout", "Zuflucht");
        this.translations.put("player", "Spieler");
        this.translations.put("demeanor", "Verhalten");
        this.translations.put("concept", "Konzept");
        this.translations.put("sire", "Erzeuger");
        this.translations.put("clan", "Clan");
        this.translations.put("sect", "Sekte");
        this.translations.put("age", "Alter");
        this.translations.put("looksLikeAge", "Sieht aus wie");
        this.translations.put("dayOfBirth", "Geburtstag");
        this.translations.put("dayOfDeath", "Todestag");
        this.translations.put("hairColor", "Haarfarbe");
        this.translations.put("eyeColor", "Augenfarbe");
        this.translations.put("skinColor", "Hautfarbe");
        this.translations.put("nationality", "Nationalität");
        this.translations.put("size", "Größe");
        this.translations.put("weight", "Gewicht");
        this.translations.put("sex", "Geschlecht");
        this.translations.put("MALE", "männlich");
        this.translations.put("FEMALE", "weiblich");
        this.translations.put("required", "Felder mit * sind Pflichtfelder.");
        this.translations.put("PRIMARY", "Primär");
        this.translations.put("SECONDARY", "Sekundär");
        this.translations.put("TERTIARY", "Tertiär");
        this.translations.put("freeAdditionalPoints", "Freie Zusatzpunkte");
        this.translations.put("attributes", "Attribute");
        this.translations.put("physical", "Körperlich");
        this.translations.put("social", "Gesellschaftlich");
        this.translations.put("mental", "Geistig");
        this.translations.put("abilities", "Fähigkeiten");
        this.translations.put("talents", "Talente");
        this.translations.put("skills", "Fertigkeiten");
        this.translations.put("knowledges", "Kenntnisse");
        this.translations.put("advantages", "Vorteile");
        this.translations.put("background", "Hintergrund");
        this.translations.put("disciplins", "Disziplinen");
        this.translations.put("virtues", "Tugenden");
        this.translations.put("lastSteps", "Letzte Schritte");
        this.translations.put("merits", "Vorzüge");
        this.translations.put("flaws", "Schwächen");
        this.translations.put("road", "Pfad");
        this.translations.put("humanity", "Menschlichkeit");
        this.translations.put("roadOf", "Pfad der ...");
        this.translations.put("flawInfoTooMany", "Deine ausgewählten Schwächen übersteigen das Maximum von 7 Punkten.");
        this.translations.put("general", "Allgemein");
        this.translations.put("meritsAndFlaws", "Vor- und Nachteile");
        this.translations.put("willpower", "Willenskraft");
        this.translations.put("bloodStock", "Blutvorrat");
        this.translations.put("printPreview", "Druckvorschau");
        this.translations.put("otherTraits", "Andere Eigenschaften");
        this.translations.put("experience", "Erfahrung");
    }

    private void health() {
        this.translations.put("health", "Gesundheit");
        this.translations.put("bruised", "Zerschrammt");
        this.translations.put("hurt", "Versehrt");
        this.translations.put("injured", "Verletzt");
        this.translations.put("wounded", "Verwundet");
        this.translations.put("mauled", "Übel zugerichtet");
        this.translations.put("crippled", "Verkrüppelt");
        this.translations.put("incapacitated", "Außer Gefecht");
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
        return "German";
    }
}
