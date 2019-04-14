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
package antafes.vampireEditor;

import antafes.myXML.XMLParser;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.character.*;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.BaseWindow;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Collections.singletonList(
                "start of log (" + (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(LocalDateTime.now()) + ")"
            )
        ));

        StorageFactory.storageWarmUp();
        this.loadGenerations();
        this.loadRoads();
    }

    /**
     * Log a single line into the log file.
     *
     * @param line The line to add
     */
    public static void log(String line) {
        VampireEditor.log(new ArrayList<>(Collections.singletonList(line)));
    }

    /**
     * Log something into the log file.
     *
     * @param lines The lines to log
     */
    public static void log(ArrayList<String> lines) {
        if (!VampireEditor.DEBUG) {
            return;
        }

        Path file = Paths.get(Configuration.PATH + "log.txt");

        if (!file.toFile().exists()) {
            try {
                if (!file.toFile().createNewFile()) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Something went terribly wrong, as the file already exists.",
                        "Error creating log file",
                        JOptionPane.ERROR_MESSAGE
                    );
                    Logger.getLogger(VampireEditor.class.getName()).log(
                        Level.SEVERE,
                        "File already exists, but check said it doesn't!"
                    );
                }
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
            VampireEditor.getResourceInJar("images/logo16.png")
        );
        baseWindow.setIconImage(img);
        baseWindow.setVisible(true);
    }

    /**
     * Get the path to the data directory.
     *
     * @return Path to the data directory
     */
    public static String getDataPath() {
        return "data/";
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
                    Generation generation = new Generation.Builder()
                        .setGeneration(Integer.parseInt(element.getAttribute("value")))
                        .setMaximumAttributes(XMLParser.getTagValueInt("maximumAttributes", element))
                        .setMaximumBloodPool(XMLParser.getTagValueInt("maximumBloodPool", element))
                        .setBloodPerRound(XMLParser.getTagValueInt("bloodPerRound", element))
                        .build();

                    if (VampireEditor.getGeneration(generation.getGeneration()) == null) {
                        VampireEditor.GENERATIONS.add(generation);
                    }
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

                XMLParser.getAllChildren(XMLParser.getTagElement("name", element)).forEach((name) -> names.put(
                    Configuration.Language.valueOf(name.getNodeName().toUpperCase()),
                    name.getFirstChild().getNodeValue()
                ));

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
     * Get a file inside of the generated JAR.
     *
     * @param path Path of the file
     *
     * @return InputStream of the fetched file
     */
    public static InputStream getFileInJar(String path) {
        return VampireEditor.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * Get a resource inside of the generated JAR.
     *
     * @param path Path of the file
     *
     * @return URL object of the file
     */
    public static URL getResourceInJar(String path) {
        return VampireEditor.class.getClassLoader().getResource(path);
    }

    /**
     * Get the list of GENERATIONS.
     *
     * @return List of generation objects
     */
    public static ArrayList<Generation> getGenerations() {
        return VampireEditor.GENERATIONS;
    }

    /**
     * Get a single generation object from the list of generations.
     * Returns null if no matching generation was found.
     *
     * @param generation Number representation of a generation
     *
     * @return Generation object or null for the given number
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
     * Get every road that is available.
     *
     * @return Map of road objects with the road key as key of the map
     */
    public static HashMap<String, Road> getRoads() {
        return VampireEditor.ROADS;
    }

    /**
     * Get the road for the given key.
     *
     * @param key The road to fetch
     *
     * @return Road object for the given key or null if none found
     */
    public static Road getRoad(String key) {
        return VampireEditor.ROADS.get(key);
    }
}
