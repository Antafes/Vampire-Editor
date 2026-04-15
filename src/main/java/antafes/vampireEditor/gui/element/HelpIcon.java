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
package antafes.vampireEditor.gui.element;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HelpIcon extends ImageIcon {
    private static final int SIZE = 14;

    public HelpIcon() {
        super(createImage());
    }

    private static Image createImage() {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Color foreground = UIManager.getColor("Label.foreground");

        if (foreground == null) {
            foreground = Color.DARK_GRAY;
        }

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(foreground);
        graphics.drawOval(1, 1, SIZE - 3, SIZE - 3);
        graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, 10f));
        graphics.drawString("?", 4, 10);
        graphics.dispose();

        return image;
    }
}
