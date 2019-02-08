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
package vampireEditor.print;

import java.awt.print.Paper;

/**
 *
 * @author Marian Pollzien
 */
public class PaperA4 extends Paper {
    private static final int INCH = 72;
    private static final double A4_WIDTH = 8.27 * INCH;
    private static final double A4_HEIGHT = 11.69 * INCH;

    /**
     * Constructor
     */
    public PaperA4() {
        super();
        this.init();
    }

    /**
     * Initialize fixed values.
     */
    private void init() {
        this.setSize(A4_WIDTH, A4_HEIGHT);
        this.setImageableArea(INCH, INCH, A4_WIDTH - 2 * INCH, A4_HEIGHT - 2 * INCH);
    }
}
