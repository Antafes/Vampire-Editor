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
package vampireEditor.print.element;

import vampireEditor.VampireEditor;
import vampireEditor.print.utility.Dot;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Marian Pollzien
 */
public class DotElement extends JPanel {
    private GridLayout layout;
    private final int dotSize = 14;

    public DotElement() {
        this.init();
    }

    private void init() {
        this.setBackground(Color.WHITE);
        this.layout = new GridLayout();
        this.layout.setHgap(1);
        this.layout.setRows(1);
        this.setLayout(this.layout);
    }

    public void createDots(int max, int filled, Dot style) {
        this.createDots(max, filled, style, true);
    }

    /**
     * Create the dots to show a value.
     *
     * @param max      The maximum amount of dots
     * @param filled   The dots that will be filled, starting from the left side
     * @param style    The style of the dots
     * @param resize   Whether the dots should be resized or not.
     */
    public void createDots(int max, int filled, Dot style, boolean resize) {
        Double sizePerDot;

        if (resize) {
            sizePerDot = this.getSizePerDot(max);
        } else {
            sizePerDot = (double) this.dotSize;
        }

        Dimension size = new Dimension(sizePerDot.intValue() * max, sizePerDot.intValue());
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.layout.setColumns(max);

        for (int i = 0; i < max; i++) {
            JLabel dot = new JLabel();
            String src;

            if (i < filled) {
                src = style.getFilled();
            } else {
                src = style.getEmpty();
            }

            ImageIcon image = new ImageIcon(VampireEditor.getResourceInJar(src));
            dot.setIcon(
                new ImageIcon(
                    image.getImage().getScaledInstance(
                        sizePerDot.intValue(),
                        sizePerDot.intValue(),
                        Image.SCALE_DEFAULT
                    )
                )
            );
            dot.setIconTextGap(0);
            dot.setText("");

            this.add(dot);
        }
    }

    /**
     * Get the size per dot according to the amount of dots.
     *
     * @param max The maximum size all dots together can have
     *
     * @return The size per dot
     */
    private Double getSizePerDot(int max) {
        double sizePerDot = (double) this.dotSize;

        if (max > 5) {
            sizePerDot -= max - 5;
        }

        if (max == 10) {
            sizePerDot = this.dotSize - 5.5;
        }

        return sizePerDot;
    }
}
