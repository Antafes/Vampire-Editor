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
public class German extends Language {
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
        this.print();
    }

    /**
     * List of available languages.
     */
    private void languages() {
        this.getTranslations().put("english", "Englisch");
        this.getTranslations().put("german", "Deutsch");
    }

    /**
     * General texts like the programs title.
     */
    private void general() {
        this.getTranslations().put("title", "Vampire Editor");
        this.getTranslations().put("darkAgesVampire", "Vampire aus der alten Welt");
        this.getTranslations().put("cancel", "Abbrechen");
        this.getTranslations().put("next", "Weiter");
        this.getTranslations().put("ok", "Ok");
        this.getTranslations().put("back", "Zurück");
        this.getTranslations().put("finish", "Fertig");
        this.getTranslations().put("other", "Anderes");
        this.getTranslations().put("description", "Beschreibung");
    }

    /**
     * Texts for the menu entries.
     */
    private void menu() {
        this.getTranslations().put("file", "Datei");
        this.getTranslations().put("fileMnemonic", "D");
        this.getTranslations().put("quit", "Beenden");
        this.getTranslations().put("quitMnemonic", "B");
        this.getTranslations().put("help", "Hilfe");
        this.getTranslations().put("helpMnemonic", "H");
        this.getTranslations().put("about", "Über");
        this.getTranslations().put("aboutMnemonic", "b");
        this.getTranslations().put("new", "Neu");
        this.getTranslations().put("newMnemonic", "N");
        this.getTranslations().put("open", "Öffnen");
        this.getTranslations().put("openMnemonic", "f");
        this.getTranslations().put("save", "Speichern");
        this.getTranslations().put("saveMnemonic", "S");
        this.getTranslations().put("existingFile", "Existierende Datei");
        this.getTranslations().put("fileExists", "Die ausgewählte Datei existiert bereits, überschreiben?");
        this.getTranslations().put("couldNotLoad", "Laden fehlgeschlagen");
        this.getTranslations().put("couldNotLoadCharacter", "Konnte den Charakter nicht laden.");
        this.getTranslations().put("close", "Schließen");
    }

    /**
     * Content of the about dialog.
     */
    private void about() {
        this.getTranslations().put("aboutText", "Dieses Programm wurde von Marian Pollzien erstellt.");
    }

    /**
     * Texts for character creation and display.
     */
    private void character() {
        this.getTranslations().put("newCharacter", "Neuer Charakter");
        this.getTranslations().put("looks", "Aussehen");
        this.getTranslations().put("name", "Name");
        this.getTranslations().put("chronicle", "Chronik");
        this.getTranslations().put("generation", "Generation");
        this.getTranslations().put("nature", "Wesen");
        this.getTranslations().put("hideout", "Zuflucht");
        this.getTranslations().put("player", "Spieler");
        this.getTranslations().put("demeanor", "Verhalten");
        this.getTranslations().put("concept", "Konzept");
        this.getTranslations().put("sire", "Erzeuger");
        this.getTranslations().put("clan", "Clan");
        this.getTranslations().put("sect", "Sekte");
        this.getTranslations().put("age", "Alter");
        this.getTranslations().put("apparentAge", "Sieht aus wie");
        this.getTranslations().put("dayOfBirth", "Geburtstag");
        this.getTranslations().put("dayOfDeath", "Todestag");
        this.getTranslations().put("hairColor", "Haarfarbe");
        this.getTranslations().put("eyeColor", "Augenfarbe");
        this.getTranslations().put("skinColor", "Hautfarbe");
        this.getTranslations().put("nationality", "Nationalität");
        this.getTranslations().put("height", "Größe");
        this.getTranslations().put("weight", "Gewicht");
        this.getTranslations().put("sex", "Geschlecht");
        this.getTranslations().put("MALE", "männlich");
        this.getTranslations().put("FEMALE", "weiblich");
        this.getTranslations().put("required", "Felder mit * sind Pflichtfelder.");
        this.getTranslations().put("PRIMARY", "Primär");
        this.getTranslations().put("SECONDARY", "Sekundär");
        this.getTranslations().put("TERTIARY", "Tertiär");
        this.getTranslations().put("freeAdditionalPoints", "Freie Zusatzpunkte");
        this.getTranslations().put("attributes", "Attribute");
        this.getTranslations().put("physical", "Körperlich");
        this.getTranslations().put("social", "Gesellschaftlich");
        this.getTranslations().put("mental", "Geistig");
        this.getTranslations().put("abilities", "Fähigkeiten");
        this.getTranslations().put("talents", "Talente");
        this.getTranslations().put("skills", "Fertigkeiten");
        this.getTranslations().put("knowledge", "Kenntnisse");
        this.getTranslations().put("advantages", "Vorteile");
        this.getTranslations().put("background", "Hintergrund");
        this.getTranslations().put("disciplines", "Disziplinen");
        this.getTranslations().put("virtues", "Tugenden");
        this.getTranslations().put("lastSteps", "Letzte Schritte");
        this.getTranslations().put("merits", "Vorzüge");
        this.getTranslations().put("flaws", "Schwächen");
        this.getTranslations().put("road", "Pfad");
        this.getTranslations().put("humanity", "Menschlichkeit");
        this.getTranslations().put("roadOf", "Pfad der ...");
        this.getTranslations().put("flawInfoTooMany", "Deine ausgewählten Schwächen übersteigen das Maximum von 7 Punkten.");
        this.getTranslations().put("general", "Allgemein");
        this.getTranslations().put("meritsAndFlaws", "Vor- und Nachteile");
        this.getTranslations().put("willpower", "Willenskraft");
        this.getTranslations().put("bloodPool", "Blutvorrat");
        this.getTranslations().put("printPreview", "Druckvorschau");
        this.getTranslations().put("otherTraits", "Andere Eigenschaften");
        this.getTranslations().put("experience", "Erfahrung");
    }

    /**
     * Add texts only used on the print and print preview
     */
    private void print() {
        this.getTranslations().put("merit", "Vorzug");
        this.getTranslations().put("flaw", "Schwäche");
        this.getTranslations().put("rituals", "Rituale");
        this.getTranslations().put("paths", "Pfade");
        this.getTranslations().put("ritual", "Ritual");
        this.getTranslations().put("path", "Pfad");
        this.getTranslations().put("source", "Quelle");
        this.getTranslations().put("system", "System");
        this.getTranslations().put("combat", "Kampf");
        this.getTranslations().put("armor", "Rüstung");
        this.getTranslations().put("weapon/attack", "Waffe/Attacke");
        this.getTranslations().put("diff.", "Diff.");
        this.getTranslations().put("damage", "Schaden");
        this.getTranslations().put("range", "Reichweite");
        this.getTranslations().put("rate", "Rate");
        this.getTranslations().put("ammo", "Munition");
        this.getTranslations().put("conceal", "Verbergen");
        this.getTranslations().put("class", "Klasse");
        this.getTranslations().put("rating", "Einstufung");
        this.getTranslations().put("penalty", "Strafe");
        this.getTranslations().put("backgrounds", "Backgrounds");
        this.getTranslations().put("possessions", "Besitztümer");
        this.getTranslations().put("haven", "Zuflucht");
        this.getTranslations().put("allies", "Verbündete");
        this.getTranslations().put("mentor", "Mentor");
        this.getTranslations().put("contacts", "Kontakte");
        this.getTranslations().put("resources", "Ressourcen");
        this.getTranslations().put("domains", "Domänen");
        this.getTranslations().put("retainers", "Gefolge");
        this.getTranslations().put("herd", "Herde");
        this.getTranslations().put("status", "Status");
        this.getTranslations().put("influence", "Einfluss");
        this.getTranslations().put("gear", "Ausrüstung");
        this.getTranslations().put("equipment", "Ausstattung");
        this.getTranslations().put("feedingGrounds", "Nahrungsgründe");
        this.getTranslations().put("miscellaneous", "Verschiedenes");
        this.getTranslations().put("location", "Ort");
        this.getTranslations().put("history", "Geschichte");
        this.getTranslations().put("momentsOfTruth", "Momente der Wahrheit");
        this.getTranslations().put("goalsAndPlots", "Ziele & Pläne");
        this.getTranslations().put("visuals", "Aussehen");
        this.getTranslations().put("coterieChart", "Gesellschaft");
        this.getTranslations().put("characterSketch", "Charakterskizze");
    }

    /**
     * Add texts for health
     */
    private void health() {
        this.getTranslations().put("health", "Gesundheit");
        this.getTranslations().put("bruised", "Zerschrammt");
        this.getTranslations().put("hurt", "Versehrt");
        this.getTranslations().put("injured", "Verletzt");
        this.getTranslations().put("wounded", "Verwundet");
        this.getTranslations().put("mauled", "Übel zugerichtet");
        this.getTranslations().put("crippled", "Verkrüppelt");
        this.getTranslations().put("incapacitated", "Außer Gefecht");
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
