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

import antafes.eventDispatcher.Application;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.BaseWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The base class that starts everything.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class VampireEditor extends Application
{
    private static final boolean DEBUG = false;

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
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(VampireEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Open up the editors base window.
     */
    private void openBaseWindow() {
        SwingUtilities.invokeLater(() -> {
            BaseWindow baseWindow = new BaseWindow();
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(
                VampireEditor.getResourceInJar("images/logo16.png")
            );
            baseWindow.setIconImage(img);
            baseWindow.setVisible(true);
        });
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
     * Get a file inside the generated JAR.
     *
     * @param path Path of the file
     *
     * @return InputStream of the fetched file
     */
    public static InputStream getFileInJar(String path) {
        return VampireEditor.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * Get a resource inside the generated JAR.
     *
     * @param path Path of the file
     *
     * @return URL object of the file
     */
    public static URL getResourceInJar(String path) {
        return VampireEditor.class.getClassLoader().getResource(path);
    }
}
