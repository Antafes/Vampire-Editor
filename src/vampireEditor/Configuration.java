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

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import vampireEditor.language.LanguageInterface;

/**
 * Configuration data for the vampire character editor.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class Configuration
{
    public static String PATH = System.getProperty("user.home") + "/.vampire/";
    private final Properties properties;
    private final File propertiesFile;

    /**
     * A list of available languages.
     */
    public static enum Language {
        ENGLISH ("vampireEditor.language.English", "English", "images/english.png"),
        GERMAN ("vampireEditor.language.German", "German", "images/german.png");

        private final String languageString;
        private final String name;
        private final ImageIcon icon;

        /**
         * Create a new language enum.
         *
         * @param languageString
         */
        private Language(String languageString, String name, String path) {
            this.languageString = languageString;
            this.name = name;

            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(
                this.getClass().getClassLoader().getResource(path)
            );
            this.icon = new ImageIcon(img);
        }

        /**
         * Get the language string, which is needed to create the language
         * object.
         *
         * @return
         */
        public String getLanguageString() {
            return languageString;
        }

        /**
         * Get the language name.
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Get the image icon for the language.
         *
         * @return
         */
        public ImageIcon getIcon() {
            return icon;
        }
    }

    /**
     * constructor
     */
    public Configuration()
    {
        this.propertiesFile = new File(PATH + "gui.xml");
        this.properties = new Properties();
    }

    /**
     * load all saved properties
     */
    public void loadProperties()
    {
        if (this.propertiesFile.exists())
        {
            try
            {
                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(this.propertiesFile))) {
                    this.properties.loadFromXML(inputStream);
                }
            }
            catch (FileNotFoundException ex)
            {}
            catch (InvalidPropertiesFormatException ex)
            {}
            catch (IOException ex)
            {}
        }
        else
        {
            this.properties.setProperty("openDirPath", new File(PATH + "../Documents/").getPath());
            this.properties.setProperty("saveDirPath", new File(PATH + "../Documents/").getPath());
            this.properties.setProperty("language", Language.ENGLISH.toString());
        }
    }

    /**
     * save all properties
     */
    public void saveProperties()
    {
        BufferedOutputStream outputStream;
        try
        {
            if (!this.propertiesFile.exists())
            {
                File path = new File(this.propertiesFile.getParent());
                path.mkdirs();
                this.propertiesFile.createNewFile();
            }

            outputStream = new BufferedOutputStream(new FileOutputStream(this.propertiesFile));
            this.properties.storeToXML(outputStream, null);
        }
        catch (FileNotFoundException ex)
        {}
        catch (InvalidPropertiesFormatException ex)
        {}
        catch (IOException ex)
        {}
    }

    /**
     * Get the open dir path.
     *
     * @return
     */
    public File getOpenDirPath()
    {
        return new File(this.properties.getProperty("openDirPath"));
    }

    /**
     * Get the save dir path including the new file.
     *
     * @param filename
     * @return
     */
    public File getSaveDirPath(String filename)
    {
        if (!filename.endsWith(".xml")) {
            filename += ".xml";
        }

        return new File(this.properties.getProperty("saveDirPath") + "/" + filename);
    }

    /**
     * Get the save dir path.
     *
     * @return A path
     */
    public File getSaveDirPath()
    {
        return new File(this.properties.getProperty("saveDirPath"));
    }

    /**
     * Get the windows location on screen.
     * This will default to 0:0 if no position is saved.
     *
     * @return A Point object with the x and y coordinates.
     */
    public Point getWindowLocation()
    {
        Point point = new Point(0, 0);
        String pointX = this.properties.getProperty("windowLocationX");
        String pointY = this.properties.getProperty("windowLocationY");

        if (pointX != null && !pointX.isEmpty() && pointY != null && !pointY.isEmpty())
        {
            point.setLocation(Double.parseDouble(pointX), Double.parseDouble(pointY));
        }

        return point;
    }

    /**
     * Get the selected language.
     *
     * @return
     */
    public Language getLanguage()
    {
        return Language.valueOf(this.properties.getProperty("language"));
    }

    /**
     * Get a language object from the currently selected language.
     *
     * @return
     */
    public LanguageInterface getLanguageObject()
    {
        LanguageInterface language = null;

        try {
            language = (LanguageInterface) Class.forName(this.getLanguage().getLanguageString()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        return language;
    }

    /**
     * Set the open dir path.
     *
     * @param path
     */
    public void setOpenDirPath(String path)
    {
        if (new File(path).isFile())
            path = new File(path).getParent();

        this.properties.setProperty("openDirPath", path);
    }

    /**
     * Set the save dir path.
     *
     * @param path
     */
    public void setSaveDirPath(String path)
    {
        if (new File(path).isFile())
            path = new File(path).getParent();

        this.properties.setProperty("saveDirPath", path);
    }

    /**
     * Set the windows position on the screen.
     *
     * @param point
     */
    public void setWindowLocation(Point point)
    {
        this.properties.setProperty("windowLocationX", String.valueOf(point.getX()));
        this.properties.setProperty("windowLocationY", String.valueOf(point.getY()));
    }

    /**
     * Set the selected language.
     *
     * @param language
     */
    public void setLanguage(Language language) {
        this.properties.setProperty("language", language.toString());
    }
}