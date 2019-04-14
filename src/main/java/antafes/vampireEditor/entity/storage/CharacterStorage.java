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
package antafes.vampireEditor.entity.storage;

import antafes.myXML.XMLParser;
import antafes.myXML.XMLValidator;
import antafes.myXML.XMLWriter;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.*;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.character.*;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Storage for characters
 *
 * @author Marian Pollzien
 */
public class CharacterStorage extends BaseStorage {
    private final Configuration configuration;
    private final XMLWriter xw;
    private final XMLParser xp;
    private final XMLValidator xv;

    /**
     * Create a new character storage.
     */
    CharacterStorage() {
        super();

        HashMap<String, String> rootAttributes = new HashMap<>();
        rootAttributes.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        this.configuration = Configuration.getInstance();
        String characterSchemaPath = "character.xsd";
        this.xw = new XMLWriter("character");
        this.xw.addRootNodeAttributes(rootAttributes);
        this.xp = new XMLParser(VampireEditor.getFileInJar(characterSchemaPath));
        this.xv = new XMLValidator(VampireEditor.getFileInJar(characterSchemaPath));
    }

    /**
     * Initializes the storage and pre-loads available data.
     *
     * @TODO This might be used in the future to preload previously opened characters.
     */
    @Override
    public void init() {
    }

    /**
     * Fetch a single ability for a given key.
     *
     * @param key The key under which to find the entity
     *
     * @return The entity
     */
    @Override
    public Character getEntity(String key) throws EntityStorageException {
        return (Character) super.getEntity(key);
    }

    /**
     * Save the given character.
     *
     * @param character The character to save
     * @param filename The filename to use for saving
     */
    public void save(antafes.vampireEditor.entity.Character character, String filename) {
        this.addRequiredFields(character);
        this.xw.write(this.configuration.getSaveDirPath(filename));
        this.getList().put(character.getId().toString(), character);
    }

    /**
     * Load a character from the given file.
     *
     * @param filename The file to load
     *
     * @return The loaded character
     * @throws EntityStorageException Thrown if character couldn't be loaded
     */
    public antafes.vampireEditor.entity.Character load(String filename) throws EntityStorageException  {
        if (this.xp.parse(this.configuration.getOpenDirPath() + "/" + filename)) {
            Character character = this.fillValues();

            if (character != null) {
                this.getList().put(character.getId().toString(), character);

                return character;
            }
        }

        EntityStorageException ex = new EntityStorageException("Could not load character '" + filename + "'!");

        this.xp.getExceptionList().forEach(ex::addSuppressed);

        throw ex;
    }

    /**
     * Add all fields that are required to generate a new character object out of the stored data.
     *
     * @param character The character to get the data from
     */
    private void addRequiredFields(antafes.vampireEditor.entity.Character character) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, String> dateNilAttributes = new HashMap<>();
        dateNilAttributes.put("xsi:nil", "true");
        HashMap<String, String> rootAttributes = new HashMap<>();
        rootAttributes.put("id", character.getId().toString());

        this.xw.addRootNodeAttributes(rootAttributes);
        this.xw.addChild("name", character.getName());
        this.xw.addChild("clan", character.getClan().getKey());
        this.xw.addChild("generation", character.getGeneration().toString());
        this.xw.addChild("chronicle", character.getChronicle());
        this.xw.addChild("experience", Integer.toString(character.getExperience()));
        this.xw.addChild("nature", character.getNature());
        this.xw.addChild("hideout", character.getHideout());
        this.xw.addChild("player", character.getPlayer());
        this.xw.addChild("demeanor", character.getDemeanor());
        this.xw.addChild("concept", character.getConcept());
        this.xw.addChild("sire", character.getSire());
        this.xw.addChild("sect", character.getSect());

        Element attributes = this.xw.addChild("attributes");
        character.getAttributes().forEach((attribute) -> {
            HashMap<String, String> attributeList = new HashMap<>();
            attributeList.put("key", attribute.getKey());
            this.xw.addChild(attributes, "attribute", Integer.toString(attribute.getValue()), attributeList);
        });

        Element abilities = this.xw.addChild("abilities");
        character.getAbilities().forEach((ability) -> {
            HashMap<String, String> abilitiesList = new HashMap<>();
            abilitiesList.put("key", ability.getKey());
            this.xw.addChild(abilities, "ability", Integer.toString(ability.getValue()), abilitiesList);
        });

        Element advantages = this.xw.addChild("advantages");
        character.getAdvantages().forEach((advantage) -> {
            HashMap<String, String> advantagesList = new HashMap<>();
            advantagesList.put("key", advantage.getKey());
            this.xw.addChild(advantages, "advantage", Integer.toString(advantage.getValue()), advantagesList);
        });

        Element merits = this.xw.addChild("merits");
        character.getMerits().forEach((merit) -> this.xw.addChild(merits, "merit", merit.getKey()));

        Element flaws = this.xw.addChild("flaws");
        character.getFlaws().forEach((flaw) -> this.xw.addChild(flaws, "flaw", flaw.getKey()));

        HashMap<String, String> roadAttributes = new HashMap<>();
        roadAttributes.put("key", character.getRoad().getKey());
        this.xw.addChild("road", Integer.toString(character.getRoad().getValue()), roadAttributes);
        this.xw.addChild("willpower", Integer.toString(character.getWillpower()));
        this.xw.addChild("usedWillpower", Integer.toString(character.getUsedWillpower()));
        this.xw.addChild("bloodPool", Integer.toString(character.getBloodPool()));
        this.xw.addChild("age", Integer.toString(character.getAge()));
        this.xw.addChild("apparentAge", Integer.toString(character.getApparentAge()));

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
        this.xw.addChild("height", Integer.toString(character.getHeight()));
        this.xw.addChild("weight", Integer.toString(character.getWeight()));
        this.xw.addChild("sex", character.getSex().name());
        this.xw.addChild("story", character.getStory());
        this.xw.addChild("description", character.getDescription());
    }

    /**
     * Create a new character object and fill it with values.
     *
     * @return The newly created character
     */
    private antafes.vampireEditor.entity.Character fillValues() {
        antafes.vampireEditor.entity.Character.Builder builder = new antafes.vampireEditor.entity.Character.Builder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Element root = this.xp.getRootElement();
        String id = root.getAttribute("id");

        if (id == null || id.equals("")) {
            return null;
        }

        ClanStorage clanStorage = (ClanStorage) StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
        builder.setId(UUID.fromString(id));
        builder.setName(XMLParser.getTagValue("name", root));

        try {
            builder.setClan(clanStorage.getEntity(XMLParser.getTagValue("clan", root)));
        } catch (EntityStorageException e) {
            e.printStackTrace();
        }

        builder.setGeneration(VampireEditor.getGeneration(XMLParser.getTagValueInt("generation", root)));
        builder.setChronicle(XMLParser.getTagValue("chronicle", root));
        builder.setExperience(XMLParser.getTagValueInt("experience", root));
        builder.setNature(XMLParser.getTagValue("nature", root));
        builder.setHideout(XMLParser.getTagValue("hideout", root));
        builder.setPlayer(XMLParser.getTagValue("player", root));
        builder.setDemeanor(XMLParser.getTagValue("demeanor", root));
        builder.setConcept(XMLParser.getTagValue("concept", root));
        builder.setSire(XMLParser.getTagValue("sire", root));
        builder.setSect(XMLParser.getTagValue("sect", root));

        Element attributes = XMLParser.getTagElement("attributes", root);
        AttributeStorage attributeStorage = (AttributeStorage) StorageFactory.getStorage(StorageFactory.StorageType.ATTRIBUTE);
        XMLParser.getAllChildren(attributes).stream().map((element) -> {
            try {
                String key = element.getAttribute("key");
                Attribute attribute = attributeStorage.getEntity(key);
                Attribute.Builder attributeBuilder = new Attribute.Builder()
                    .fillDataFromObject(attribute);

                return attributeBuilder
                    .setValue(XMLParser.getElementValueInt(element))
                    .build();
            } catch (EntityException | EntityStorageException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }).forEachOrdered(builder::addAttribute);

        Element abilities = XMLParser.getTagElement("abilities", root);
        AbilityStorage abilityStorage = (AbilityStorage) StorageFactory.getStorage(StorageFactory.StorageType.ABILITY);
        XMLParser.getAllChildren(abilities).stream().map((element) -> {
            try {
                String key = element.getAttribute("key");
                Ability ability = abilityStorage.getEntity(key);
                Ability.Builder abilityBuilder = new Ability.Builder()
                    .fillDataFromObject(ability);

                return abilityBuilder
                    .setValue(XMLParser.getElementValueInt(element))
                    .build();
            } catch (EntityException | EntityStorageException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }).forEachOrdered(builder::addAbility);

        Element advantages = XMLParser.getTagElement("advantages", root);
        AdvantageStorage advantageStorage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        XMLParser.getAllChildren(advantages).stream().map((element) -> {
            try {
                String key = element.getAttribute("key");
                Advantage advantage = advantageStorage.getEntity(key);
                Advantage.Builder advantageBuilder = new Advantage.Builder()
                    .fillDataFromObject(advantage);

                return advantageBuilder
                    .setValue(XMLParser.getElementValueInt(element))
                    .build();
            } catch (EntityException | EntityStorageException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }).forEachOrdered(builder::addAdvantage);

        XMLParser.getAllChildren(XMLParser.getTagElement("merits", root))
            .forEach((element) -> builder.addMerit(VampireEditor.getMerits().get(XMLParser.getElementValue(element))));

        XMLParser.getAllChildren(XMLParser.getTagElement("flaws", root))
            .forEach((element) -> builder.addFlaw(VampireEditor.getFlaws().get(XMLParser.getElementValue(element))));

        Element road = XMLParser.getTagElement("road", root);
        Road.Builder roadBuilder = new Road.Builder()
            .fillDataFromObject(VampireEditor.getRoad(road.getAttribute("key")))
            .setValue(XMLParser.getTagValueInt("road", root));

        try {
            builder.setRoad(roadBuilder.build());
        } catch (EntityException ex) {
            Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

        builder.setWillpower(XMLParser.getTagValueInt("willpower", root));

        if (XMLParser.tagExists("usedWillpower", root) && XMLParser.getTagValue("usedWillpower", root) != null) {
            builder.setUsedWillpower(XMLParser.getTagValueInt("usedWillpower", root));
        }

        builder.setBloodPool(XMLParser.getTagValueInt("bloodPool", root));
        builder.setAge(XMLParser.getTagValueInt("age", root));
        builder.setApparentAge(XMLParser.getTagValueInt("apparentAge", root));

        if (XMLParser.tagExists("dayOfBirth", root)
            && XMLParser.getTagValue("dayOfBirth", root) != null
            && !XMLParser.getTagValue("dayOfBirth", root).equals("")
        ) {
            try {
                builder.setDayOfBirth(format.parse(XMLParser.getTagValue("dayOfBirth", root)));
            } catch (ParseException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (XMLParser.tagExists("dayOfDeath", root)
            && XMLParser.getTagValue("dayOfDeath", root) != null
            && !XMLParser.getTagValue("dayOfDeath", root).equals("")
        ) {
            try {
                builder.setDayOfDeath(format.parse(XMLParser.getTagValue("dayOfDeath", root)));
            } catch (ParseException ex) {
                Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        builder.setHairColor(XMLParser.getTagValue("hairColor", root));
        builder.setEyeColor(XMLParser.getTagValue("eyeColor", root));
        builder.setSkinColor(XMLParser.getTagValue("skinColor", root));
        builder.setNationality(XMLParser.getTagValue("nationality", root));
        builder.setHeight(XMLParser.getTagValueInt("height", root));
        builder.setWeight(XMLParser.getTagValueInt("weight", root));
        builder.setSex(antafes.vampireEditor.entity.Character.Sex.valueOf(XMLParser.getTagValue("sex", root)));
        builder.setStory(XMLParser.getTagValue("story", root));
        builder.setDescription(XMLParser.getTagValue("description", root));

        try {
            return builder.build();
        } catch (EntityException ex) {
            Logger.getLogger(CharacterStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
