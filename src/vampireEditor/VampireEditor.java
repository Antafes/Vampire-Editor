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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import myXML.XMLParser;
import org.w3c.dom.Element;
import vampireEditor.character.*;
import vampireEditor.gui.BaseWindow;

/**
 * The base class that starts everything.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class VampireEditor {
    private static final ArrayList<Generation> GENERATIONS = new ArrayList<>();
    private static final HashMap<String, Advantage> ADVANTAGES = new HashMap<>();
    private static final HashMap<String, Weakness> WEAKNESSES = new HashMap<>();
    private static final HashMap<String, Clan> CLANS = new HashMap<>();
    private static final HashMap<String, Font> FONTS = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VampireEditor ve = new VampireEditor();
    }

    /**
     * Create the Vampire Editor main class.
     */
    public VampireEditor() {
        this.loadGenerations();
        this.loadAdvantages();
        this.loadWeaknesses();
        this.loadClans();
        this.loadFonts();
        this.openBaseWindow();
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
     * Load every possible generation.
     */
    private void loadGenerations() {
        Element root;
        InputStream is = VampireEditor.getFileInJar("vampireEditor/data/generations.xml");
        XMLParser xp = new XMLParser();

        if (xp.parse(is)) {
            root = xp.getRootElement();
            ArrayList<Element> elements = XMLParser.getAllChildren(root);
            elements.forEach((element) -> {
                VampireEditor.GENERATIONS.add(
                    new Generation(
                        Integer.parseInt(element.getAttribute("value")),
                        XMLParser.getTagValueInt("maximumAttributes", element),
                        XMLParser.getTagValueInt("maximumBloodStock", element),
                        XMLParser.getTagValueInt("bloodPerRound", element)
                    )
                );
            });
        }
    }

    /**
     * Load all weaknesses of the clans.
     */
    private void loadWeaknesses() {
        InputStream is = VampireEditor.getFileInJar("vampireEditor/data/weaknesses.xml");
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

                VampireEditor.WEAKNESSES.put(
                    element.getAttribute("key"),
                    new Weakness(element.getAttribute("key"), names)
                );
            });
        }
    }

    /**
     * Load all benefits.
     */
    private void loadAdvantages() {
        InputStream is = VampireEditor.getFileInJar("vampireEditor/data/advantages.xml");
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
                VampireEditor.ADVANTAGES.put(
                    element.getAttribute("key"),
                    new Advantage(
                        element.getAttribute("key"),
                        names,
                        AdvantageInterface.AdvantageType.valueOf(
                            XMLParser.getTagValue("type", element)
                        )
                    )
                );
            });
        }
    }

    /**
     * Load all clans.
     */
    private void loadClans() {
        InputStream is = VampireEditor.getFileInJar("vampireEditor/data/clans.xml");
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

                VampireEditor.CLANS.put(
                    element.getAttribute("key"),
                    new Clan(
                        element.getAttribute("key"),
                        names,
                        nicknames,
                        this.getDisciplins(XMLParser.getTagElement("advantages", element)),
                        this.getWeaknesses(XMLParser.getTagElement("weaknesses", element))
                    )
                );
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
     * Get the list of GENERATIONS.
     *
     * @return
     */
    public static ArrayList<Generation> getGenerations() {
        return VampireEditor.GENERATIONS;
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
}
