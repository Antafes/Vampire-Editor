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
package vampireEditor.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFormattedTextField;

/**
 * Extends the JFormattedTextField with the ability to add a placeholder.
 *
 * @author Marian Pollzien <map@wafriv.de>
 */
public class PlaceholderFormattedTextField extends JFormattedTextField {
    private String placeholder = "";

    /**
     * Get the placeholder string.
     *
     * @return
     */
    public String getPlaceholder() {
        return this.placeholder;
    }

    /**
     * Set the placeholder for the field.
     *
     * @param placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Calls the parent paintComponent method and afterwards adds the
     * placeholder text if no text is added to the field.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.placeholder.length() == 0 || this.getText().length() > 0) {
            return;
        }

        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getDisabledTextColor());
        g2d.drawString(this.placeholder, getInsets().left, g.getFontMetrics()
            .getMaxAscent() + getInsets().top);
    }
}