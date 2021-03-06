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

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import myXML.XMLParser;
import org.w3c.dom.Element;
import vampireEditor.entity.EntityException;
import vampireEditor.entity.character.*;
import vampireEditor.gui.BaseWindow;

/**
 * The base class that starts everything.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class VampireEditor {
    private static final boolean DEBUG = false;
    private static final ArrayList<Generation> GENERATIONS = new ArrayList<>();
    private static final HashMap<String, Advantage> ADVANTAGES = new HashMap<>();
    private static final HashMap<String, Weakness> WEAKNESSES = new HashMap<>();
    private static final HashMap<String, Clan> CLANS = new HashMap<>();
    private static final HashMap<String, Attribute> ATTRIBUTES = new HashMap<>();
    private static final HashMap<String, Ability> ABILITIES = new HashMap<>();
    private static final HashMap<String, Font> FONTS = new HashMap<>();
    private static final HashMap<String, Merit> MERITS = new HashMap<>();
    private static final HashMap<String, Flaw> FLAWS = new HashMap<>();
    private static final HashMap<String, Road> ROADS = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configuration configuration = Configuration.getInstance();
        configuration.loadProperties();
        VampireEditor ve = new VampireEditor();
        ve.openBaseWindow();
    }

    /**
     * Create the Vampire Editor main class.
     */
    public VampireEditor() {
        VampireEditor.log(new ArrayList<>(
            Arrays.asList(
                "start of log (" + (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(LocalDateTime.now()) + ")"
            )
        ));

        this.loadGenerations();
        this.loadAdvantages();
        this.loadWeaknesses();
        this.loadClans();
        this.loadAttributes();
        this.loadAbilities();
        this.loadMerits();
        this.loadFlaws();
        this.loadRoads();
        this.loadFonts();
    }

    /**
     * Log something into the log file.
     * @param lines
     */
    public static void log(ArrayList<String> lines) {
        if (!VampireEditor.DEBUG) {
            return;
        }

        Path file = Paths.get(Configuration.PATH + "log.txt");

        if (!file.toFile().exists()) {
            try {
                file.toFile().createNewFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error creating log file",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Open up the editors base window.
     */
    private void openBaseWindow() {
        BaseWindow baseWindow = new BaseWindow();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(
            this.getClass().getClassLoader().getResource("images/logo16.png")
        );
        baseWindow.setIconImage(img);
        baseWindow.setVisible(true);
    }

    /**
     * Get the path to the data directory.
     *
     * @return
     */
    private String getDataPath() {
        return "vampireEditor/data/";
    }

    /**
     * Load every possible generation.
     */
    private void loadGenerations() {
        Element root;
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "generations.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            root = xp.getRootElement();
            ArrayList<Element> elements = XMLParser.getAllChildren(root);
            elements.forEach((element) -> {
                try {
                    VampireEditor.GENERATIONS.add(
                        new Generation.Builder()
                            .setGeneration(Integer.parseInt(element.getAttribute("value")))
                            .setMaximumAttributes(XMLParser.getTagValueInt("maximumAttributes", element))
                            .setMaximumBloodStock(XMLParser.getTagValueInt("maximumBloodStock", element))
                            .setBloodPerRound(XMLParser.getTagValueInt("bloodPerRound", element))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load all weaknesses of the clans.
     */
    private void loadWeaknesses() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "weaknesses.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(element).forEach((name) -> {
                    names.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.WEAKNESSES.put(
                        element.getAttribute("key"),
                        new Weakness.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load all attributes
     */
    private void loadAttributes() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "attributes.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                Element name = XMLParser.getTagElement("name", element);
                XMLParser.getAllChildren(name).forEach((translatedName) -> {
                    names.put(
                        Configuration.Language.valueOf(translatedName.getNodeName().toUpperCase()),
                        translatedName.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.ATTRIBUTES.put(
                        element.getAttribute("key"),
                        new Attribute.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setType(AttributeInterface.AttributeType.valueOf(
                                XMLParser.getTagValue("type", element)
                            ))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load all benefits.
     */
    private void loadAdvantages() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "advantages.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                Element name = XMLParser.getTagElement("name", element);
                XMLParser.getAllChildren(name).forEach((translatedName) -> {
                    names.put(
                        Configuration.Language.valueOf(translatedName.getNodeName().toUpperCase()),
                        translatedName.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.ADVANTAGES.put(
                        element.getAttribute("key"),
                        new Advantage.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setType(AdvantageInterface.AdvantageType.valueOf(
                                XMLParser.getTagValue("type", element)
                            ))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load all clans.
     */
    private void loadClans() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "clans.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                HashMap<Configuration.Language, String> nicknames = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> {
                    names.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                XMLParser.getAllChildren(XMLParser.getTagElement("nickname", element)).forEach((name) -> {
                    nicknames.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.CLANS.put(
                        element.getAttribute("key"),
                        new Clan.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setNicknames(nicknames)
                            .setDisciplins(this.getDisciplins(XMLParser.getTagElement("advantages", element)))
                            .setWeaknesses(this.getWeaknesses(XMLParser.getTagElement("weaknesses", element)))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load all abilities.
     */
    private void loadAbilities() {
        Element root;
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "abilities.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            root = xp.getRootElement();
            ArrayList<Element> elements = XMLParser.getAllChildren(root);
            elements.forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();
                Element name = XMLParser.getTagElement("name", element);
                XMLParser.getAllChildren(name).forEach((translatedName) -> {
                    names.put(
                        Configuration.Language.valueOf(translatedName.getNodeName().toUpperCase()),
                        translatedName.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.ABILITIES.put(
                        element.getAttribute("key"),
                        new Ability.Builder()
                            .setKey(element.getAttribute("key"))
                            .setNames(names)
                            .setType(
                                AbilityInterface.AbilityType.valueOf(XMLParser.getTagValue("type", element))
                            )
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load the available merits.
     */
    private void loadMerits() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "merits.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> {
                    names.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.MERITS.put(
                        element.getAttribute("key"),
                        (Merit) new SpecialFeature.Builder()
                            .setSpecialFeatureClass(SpecialFeature.Builder.SpecialFeatureClass.MERIT)
                            .setNames(names)
                            .setKey(element.getAttribute("key"))
                            .setCost(XMLParser.getTagValueInt("cost", element))
                            .setType(SpecialFeatureInterface.SpecialFeatureType.valueOf(XMLParser.getTagValue("type", element)))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load the available merits.
     */
    private void loadFlaws() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "flaws.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> {
                    names.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.FLAWS.put(
                        element.getAttribute("key"),
                        (Flaw) new SpecialFeature.Builder()
                            .setSpecialFeatureClass(SpecialFeature.Builder.SpecialFeatureClass.FLAW)
                            .setNames(names)
                            .setKey(element.getAttribute("key"))
                            .setCost(XMLParser.getTagValueInt("cost", element))
                            .setType(SpecialFeatureInterface.SpecialFeatureType.valueOf(XMLParser.getTagValue("type", element)))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Load the available roads.
     */
    private void loadRoads() {
        InputStream is = VampireEditor.getFileInJar(this.getDataPath() + "roads.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            XMLParser.getAllChildren(xp.getRootElement()).forEach((element) -> {
                HashMap<Configuration.Language, String> names = new HashMap<>();

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> {
                    names.put(
                        Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                        name.getFirstChild().getNodeValue()
                    );
                });

                try {
                    VampireEditor.ROADS.put(
                        element.getAttribute("key"),
                        new Road.Builder()
                            .setNames(names)
                            .setKey(element.getAttribute("key"))
                            .build()
                    );
                } catch (EntityException ex) {
                    Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Get the disciplins of the given clan element.
     *
     * @param element
     *
     * @return
     */
    private ArrayList<Advantage> getDisciplins(Element element) {
        ArrayList<Advantage> disciplins = new ArrayList<>();
        ArrayList<Element> advantages = XMLParser.getAllChildren(element);

        advantages.forEach((listElement) -> {
            disciplins.add(VampireEditor.getAdvantage(listElement.getChildNodes().item(0).getNodeValue()));
        });

        return disciplins;
    }

    /**
     * Get the disciplins of the given clan element.
     *
     * @param element
     *
     * @return
     */
    private ArrayList<Weakness> getWeaknesses(Element element) {
        ArrayList<Weakness> weaknesses = new ArrayList<>();
        ArrayList<Element> advantages = XMLParser.getAllChildren(element);

        advantages.forEach((listElement) -> {
            weaknesses.add(VampireEditor.getWeakness(listElement.getNodeValue()));
        });

        return weaknesses;
    }

    /**
     * Load the fonts used for the character sheet.
     */
    private void loadFonts() {
        try {
            VampireEditor.FONTS.put(
                "headlines",
                Font.createFont(
                    Font.TRUETYPE_FONT,
                    VampireEditor.getFileInJar("fonts/GoudyTextMT-LombardicCapitals.ttf")
                )
            );
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get a file inside of the generated JAR.
     *
     * @param path
     *
     * @return
     */
    public static InputStream getFileInJar(String path) {
        return VampireEditor.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * Get a resource inside of the generated JAR.
     *
     * @param path
     *
     * @return
     */
    public static URL getResourceInJar(String path) {
        return VampireEditor.class.getClassLoader().getResource(path);
    }

    /**
     * Get the list of GENERATIONS.
     *
     * @return
     */
    public static ArrayList<Generation> getGenerations() {
        return VampireEditor.GENERATIONS;
    }

    /**
     * Get a single generation object from the list of generations.
     * Returns null if no matching generation was found.
     *
     * @param generation
     *
     * @return
     */
    public static Generation getGeneration(int generation) {
        for (Generation generationObject : VampireEditor.GENERATIONS) {
            if (generationObject.getGeneration() == generation) {
                return generationObject;
            }
        }

        return null;
    }

    /**
     * Get the list of ADVANTAGES.
     *
     * @return
     */
    public static HashMap<String, Advantage> getAdvantages() {
        return VampireEditor.ADVANTAGES;
    }

    /**
     * Get the list of clans.
     *
     * @return
     */
    public static HashMap<String, Clan> getClans() {
        return VampireEditor.CLANS;
    }

    /**
     * Get the list of attributes.
     *
     * @return
     */
    public static HashMap<String, Attribute> getAttributes() {
        return VampireEditor.ATTRIBUTES;
    }

    /**
     * Get an attribute by its key.
     *
     * @param key
     *
     * @return
     */
    public static Attribute getAttribute(String key) {
        return VampireEditor.ATTRIBUTES.get(key);
    }

    /**
     * Get the list of abilities.
     *
     * @return
     */
    public static HashMap<String, Ability> getAbilities() {
        return VampireEditor.ABILITIES;
    }

    /**
     * Get an ability by its key.
     *
     * @param key
     *
     * @return
     */
    public static Ability getAbility(String key) {
        return VampireEditor.ABILITIES.get(key);
    }

    /**
     * Get the font for the given type.
     *
     * @param type
     *
     * @return
     */
    public static Font getFont(String type) {
        return VampireEditor.FONTS.get(type);
    }

    /**
     * Get an advantage by its key.
     *
     * @param key
     *
     * @return
     */
    public static Advantage getAdvantage(String key) {
        return VampireEditor.ADVANTAGES.get(key);
    }

    /**
     * Get a weakness by its key.
     *
     * @param key
     *
     * @return
     */
    public static Weakness getWeakness(String key) {
        return VampireEditor.WEAKNESSES.get(key);
    }

    /**
     * Get a clan by its key.
     *
     * @param key
     *
     * @return
     */
    public static Clan getClan(String key) {
        return VampireEditor.CLANS.get(key);
    }

    /**
     * Get every merit that is available.
     *
     * @return
     */
    public static HashMap<String, Merit> getMerits() {
        return VampireEditor.MERITS;
    }

    /**
     * Get every flaw that is available.
     *
     * @return
     */
    public static HashMap<String, Flaw> getFlaws() {
        return VampireEditor.FLAWS;
    }

    /**
     * Get every road that is available.
     *
     * @return
     */
    public static HashMap<String, Road> getRoads() {
        return VampireEditor.ROADS;
    }

    /**
     * Get the road for the given key.
     *
     * @param key
     *
     * @return
     */
    public static Road getRoad(String key) {
        return VampireEditor.ROADS.get(key);
    }
}
