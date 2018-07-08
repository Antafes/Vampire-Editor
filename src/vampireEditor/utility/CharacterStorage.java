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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import myXML.*;
import org.w3c.dom.Element;
import vampireEditor.*;
import vampireEditor.character.Ability;
import vampireEditor.character.AbilityInterface;
import vampireEditor.character.Advantage;
import vampireEditor.character.Attribute;
import vampireEditor.character.AttributeInterface;

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
     * Load a character from the given file.
     *
     * @param filename
     *
     * @return
     * @throws java.lang.Exception
     */
    public vampireEditor.Character load(String filename) throws Exception  {
        if (this.xp.parse(this.configuration.getOpenDirPath() + "/" + filename)) {
            return this.fillValues();
        }

        throw new Exception("Could not load character '" + filename + "'!");
    }

    /**
     * Add all fields that are required to generate a new character object out of the stored data.
     *
     * @param character
     */
    private void addRequiredFields(vampireEditor.Character character) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
        this.xw.addChild("demeanor", character.getDemeanor());
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

    /**
     * Create a new character object and fill it with values.
     *
     * @return
     */
    private vampireEditor.Character fillValues() {
        vampireEditor.Character character = new vampireEditor.Character();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Element root = this.xp.getRootElement();

        character.setName(XMLParser.getTagValue("name", root));
        character.setClan(VampireEditor.getClan(XMLParser.getTagValue("clan", root)));
        character.setGeneration(VampireEditor.getGeneration(XMLParser.getTagValueInt("generation", root)));
        character.setChronicle(XMLParser.getTagValue("chronicle", root));
        character.setExperience(XMLParser.getTagValueInt("experience", root));
        character.setNature(XMLParser.getTagValue("nature", root));
        character.setHideout(XMLParser.getTagValue("hideout", root));
        character.setPlayer(XMLParser.getTagValue("player", root));
        character.setDemeanor(XMLParser.getTagValue("demeanor", root));
        character.setConcept(XMLParser.getTagValue("concept", root));
        character.setSire(XMLParser.getTagValue("sire", root));
        character.setSect(XMLParser.getTagValue("sect", root));

        Element attributes = XMLParser.getTagElement("attributes", root);
        XMLParser.getAllChildren(attributes).stream().map((element) -> {
            String key = element.getAttribute("key");
            Attribute attribute = new Attribute(
                key, AttributeInterface.AttributeType.getTypeForAttribute(key), XMLParser.getElementValueInt(element)
            );
            return attribute;
        }).forEachOrdered((attribute) -> {
            character.getAttributes().add(attribute);
        });

        Element abilities = XMLParser.getTagElement("abilities", root);
        XMLParser.getAllChildren(abilities).stream().map((element) -> {
            String key = element.getAttribute("key");
            Ability attribute = new Ability(
                key, AbilityInterface.AbilityType.getTypeForAbility(key), XMLParser.getElementValueInt(element)
            );
            return attribute;
        }).forEachOrdered((ability) -> {
            character.getAbilities().add(ability);
        });

        Element advantages = XMLParser.getTagElement("advantages", root);
        XMLParser.getAllChildren(advantages).stream().map((element) -> {
            String key = element.getAttribute("key");
            Advantage advantage = VampireEditor.getAdvantage(key);
            advantage.setValue(XMLParser.getElementValueInt(element));
            return advantage;
        }).forEachOrdered((advantage) -> {
            character.getAdvantages().add(advantage);
        });

        XMLParser.getAllChildren(XMLParser.getTagElement("merits", root)).forEach((element) -> {
            character.getMerits().add(VampireEditor.getMerits().get(XMLParser.getElementValue(element)));
        });

        XMLParser.getAllChildren(XMLParser.getTagElement("flaws", root)).forEach((element) -> {
            character.getFlaws().add(VampireEditor.getFlaws().get(XMLParser.getElementValue(element)));
        });

        Element road = XMLParser.getTagElement("road", root);
        character.setRoad(VampireEditor.getRoads().get(road.getAttribute("key")));
        character.getRoad().setValue(XMLParser.getTagValueInt("road", root));
        character.setWillpower(XMLParser.getTagValueInt("willpower", root));
        character.setBloodStock(XMLParser.getTagValueInt("bloodStock", root));
        character.setAge(XMLParser.getTagValueInt("age", root));
        character.setLooksLikeAge(XMLParser.getTagValueInt("looksLikeAge", root));

        if (XMLParser.tagExists("dayOfBirth", root)
            && XMLParser.getTagValue("dayOfBirth", root) != null
            && !XMLParser.getTagValue("dayOfBirth", root).equals("")
        ) {
            try {
                character.setDayOfBirth(format.parse(XMLParser.getTagValue("dayOfBirth", root)));
            } catch (ParseException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (XMLParser.tagExists("dayOfDeath", root)
            && XMLParser.getTagValue("dayOfDeath", root) != null
            && !XMLParser.getTagValue("dayOfDeath", root).equals("")
        ) {
            try {
                character.setDayOfDeath(format.parse(XMLParser.getTagValue("dayOfDeath", root)));
            } catch (ParseException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        character.setHairColor(XMLParser.getTagValue("hairColor", root));
        character.setEyeColor(XMLParser.getTagValue("eyeColor", root));
        character.setSkinColor(XMLParser.getTagValue("skinColor", root));
        character.setNationality(XMLParser.getTagValue("nationality", root));
        character.setSize(XMLParser.getTagValueInt("size", root));
        character.setWeight(XMLParser.getTagValueInt("weight", root));
        character.setSex(vampireEditor.Character.Sex.valueOf(XMLParser.getTagValue("sex", root)));
        character.setStory(XMLParser.getTagValue("story", root));
        character.setDescription(XMLParser.getTagValue("description", root));

        return character;
    }
}
