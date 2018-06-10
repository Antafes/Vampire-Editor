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
package vampireEditor.utility;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import myXML.*;
import org.w3c.dom.Element;
import vampireEditor.*;

/**
 *
 * @author Marian Pollzien
 */
public class CharacterStorage {
    private final Configuration configuration;
    private final XMLWriter xw;
    private final XMLParser xp;
    private final XMLValidator xv;

    /**
     * Create a new character storage.
     */
    public CharacterStorage() {
        HashMap<String, String> rootAttributes = new HashMap<>();
        rootAttributes.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        this.configuration = new Configuration();
        this.configuration.loadProperties();
        String characterSchemaPath = "vampireEditor/character.xsd";
        this.xw = new XMLWriter("character");
        this.xw.addRootNodeAttributes(rootAttributes);
        this.xp = new XMLParser(VampireEditor.getFileInJar(characterSchemaPath));
        this.xv = new XMLValidator(VampireEditor.getFileInJar(characterSchemaPath));
    }

    /**
     * Save the given character.
     *
     * @param character
     * @param filename
     */
    public void save(vampireEditor.Character character, String filename) {
        this.addRequiredFields(character);
        this.xw.write(this.configuration.getSaveDirPath(filename));
    }

    /**
     * Add all fields that are required to generate a new character object out of the stored data.
     *
     * @param character
     */
    private void addRequiredFields(vampireEditor.Character character) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        HashMap<String, String> dateNilAttributes = new HashMap<>();
        dateNilAttributes.put("xsi:nil", "true");

        this.xw.addChild("name", character.getName());
        this.xw.addChild("clan", character.getClan().getKey());
        this.xw.addChild("generation", character.getGeneration().toString());
        this.xw.addChild("chronicle", character.getChronicle());
        this.xw.addChild("experience", Integer.toString(character.getExperience()));
        this.xw.addChild("nature", Integer.toString(character.getExperience()));
        this.xw.addChild("hideout", character.getHideout());
        this.xw.addChild("player", character.getPlayer());
        this.xw.addChild("behaviour", character.getBehaviour());
        this.xw.addChild("concept", character.getConcept());
        this.xw.addChild("sire", character.getSire());
        this.xw.addChild("sect", character.getSect());

        Element attributes = this.xw.addChild("attributes");
        character.getAttributes().forEach((attribute) -> {
            HashMap<String, String> attributeList = new HashMap<>();
            attributeList.put("key", attribute.getName());
            this.xw.addChild(attributes, "attribute", Integer.toString(attribute.getValue()), attributeList);
        });

        Element abilities = this.xw.addChild("abilities");
        character.getAbilities().forEach((ability) -> {
            HashMap<String, String> abilitiesList = new HashMap<>();
            abilitiesList.put("key", ability.getName());
            this.xw.addChild(abilities, "ability", Integer.toString(ability.getValue()), abilitiesList);
        });

        Element advantages = this.xw.addChild("advantages");
        character.getAdvantages().forEach((advantage) -> {
            HashMap<String, String> advantagesList = new HashMap<>();
            advantagesList.put("key", advantage.getKey());
            this.xw.addChild(advantages, "advantage", Integer.toString(advantage.getValue()), advantagesList);
        });

        Element merits = this.xw.addChild("merits");
        character.getMerits().forEach((merit) -> {
            this.xw.addChild(merits, "merit", merit.getKey());
        });

        Element flaws = this.xw.addChild("flaws");
        character.getFlaws().forEach((flaw) -> {
            this.xw.addChild(flaws, "flaw", flaw.getKey());
        });

        HashMap<String, String> roadAttributes = new HashMap<>();
        roadAttributes.put("key", character.getRoad().getKey());
        this.xw.addChild("road", Integer.toString(character.getRoad().getValue()), roadAttributes);
        this.xw.addChild("willpower", Integer.toString(character.getWillpower()));
        this.xw.addChild("bloodStock", Integer.toString(character.getBloodStock()));
        this.xw.addChild("age", Integer.toString(character.getAge()));
        this.xw.addChild("looksLikeAge", Integer.toString(character.getLooksLikeAge()));

        if (character.getDayOfBirth() != null) {
            this.xw.addChild("dayOfBirth",format.format(character.getDayOfBirth()));
        } else {
            this.xw.addChild("dayOfBirth", dateNilAttributes);
        }

        if (character.getDayOfDeath() != null) {
            this.xw.addChild("dayOfDeath", format.format(character.getDayOfDeath()));
        } else {
            this.xw.addChild("dayOfDeath", dateNilAttributes);
        }

        this.xw.addChild("hairColor", character.getHairColor());
        this.xw.addChild("eyeColor", character.getEyeColor());
        this.xw.addChild("skinColor", character.getSkinColor());
        this.xw.addChild("nationality", character.getNationality());
        this.xw.addChild("size", Integer.toString(character.getSize()));
        this.xw.addChild("weight", Integer.toString(character.getWeight()));
        this.xw.addChild("sex", character.getSex().name());
        this.xw.addChild("story", character.getStory());
        this.xw.addChild("description", character.getDescription());
    }
}
