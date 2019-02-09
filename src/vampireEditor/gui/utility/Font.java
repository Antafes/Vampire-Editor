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
 * @copyright (c) $year, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package vampireEditor.gui.utility;

import vampireEditor.VampireEditor;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enum for the fonts used in Vampire Editor.
 *
 * @author Marian Pollzien
 */
public enum Font {
    TEXT ("fonts/GoudyMediaevalRegular.ttf"),
    HEADLINE ("fonts/GoudyTextMT-LombardicCapitals.ttf");

    private final String path;

    Font(String path) {
        this.path = path;
    }

    /**
     * Create a java.awt.Font object from the path of this font.
     *
     * @return
     */
    public java.awt.Font getFont() {
        try {
            return java.awt.Font.createFont(
                java.awt.Font.TRUETYPE_FONT,
                VampireEditor.getFileInJar(this.path)
            );
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Font.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
