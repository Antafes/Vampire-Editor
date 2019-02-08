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

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Marian Pollzien
 */
public class BarLabel extends JLabel {
    private JLabel textLabel;

    /**
     *
     * @param text The text to be displayed by the label.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>:
     *                            <code>LEFT</code>,
     *                            <code>CENTER</code>,
     *                            <code>RIGHT</code>,
     *                            <code>LEADING</code> or
     *                            <code>TRAILING</code>.
     */
    public BarLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        this.initLabel(text);
    }

    /**
     * Constructor with text and horizontal alignment
     *
     * @param text The text to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>:
     *                            <code>LEFT</code>,
     *                            <code>CENTER</code>,
     *                            <code>RIGHT</code>,
     *                            <code>LEADING</code> or
     *                            <code>TRAILING</code>.
     */
    public BarLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);

        this.initLabel(text);
    }

    /**
     * Constructor with label text.
     *
     * @param text The text to be displayed by the label.
     */
    public BarLabel(String text) {
        super(text);

        this.initLabel(text);
    }

    /**
     * Constructor with image and horizontal alignment.
     *
     * @param image The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>:
     *                            <code>LEFT</code>,
     *                            <code>CENTER</code>,
     *                            <code>RIGHT</code>,
     *                            <code>LEADING</code> or
     *                            <code>TRAILING</code>.
     */
    public BarLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);

        this.initLabel();
    }

    /**
     * Constructor with image.
     *
     * @param image The image to be displayed by the label.
     */
    public BarLabel(Icon image) {
        super(image);

        this.initLabel();
    }

    /**
     * Constructor
     */
    public BarLabel() {
        this.initLabel();
    }

    /**
     * Initialize label with an empty string.
     */
    private void initLabel() {
        this.initLabel("");
    }

    /**
     * Initialize a label with the given text.
     *
     * @param text Label text
     */
    private void initLabel(String text) {
        this.initLabel(text, JLabel.CENTER);
    }

    /**
     * Initialize a label with the given text and use the given horizontal alignment.
     *
     * @param text Label text
     * @param horizontalAlignment Alignment value according to
     */
    private void initLabel(String text, int horizontalAlignment) {
        if (this.textLabel != null) {
            return;
        }

        this.textLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
                int textWidth = fontMetrics.stringWidth(this.getText());
                int textHeight = fontMetrics.getHeight();
                int x = this.getPreferredSize().width / 2 - textWidth / 2;
                g.setColor(Color.WHITE);
                g.fillRect(x, TOP, textWidth + 4, textHeight + 4);

                super.paintComponent(g);
            }
        };
        this.textLabel.setText(text);
        this.textLabel.setHorizontalAlignment(horizontalAlignment);
        this.textLabel.setForeground(Color.BLACK);
        this.add(this.textLabel);

        this.setForeground(Color.LIGHT_GRAY);
        this.add(this.textLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.textLabel.setSize(this.getSize());
        this.textLabel.setPreferredSize(this.getPreferredSize());

        super.paintComponent(g);
    }

    @Override
    public void setText(String text) {
        if (this.textLabel == null) {
            this.initLabel();
        }

        super.setText("");
        this.textLabel.setText(text);
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);

        if (icon == null) {
            return;
        }

        ImageIcon bar = new ImageIcon(VampireEditor.getResourceInJar("images/barEmpty.png"));
        this.textLabel.setIcon(
            new ImageIcon(
                bar.getImage().getScaledInstance(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    Image.SCALE_DEFAULT
                )
            )
        );
    }

    @Override
    public void setHorizontalTextPosition(int textPosition) {
        super.setHorizontalTextPosition(textPosition);

        this.textLabel.setHorizontalTextPosition(textPosition);
    }

    @Override
    public void setVerticalTextPosition(int textPosition) {
        super.setVerticalTextPosition(textPosition);

        this.textLabel.setVerticalTextPosition(textPosition);
    }

    @Override
    public void setHorizontalAlignment(int alignment) {
        super.setHorizontalAlignment(alignment);

        this.textLabel.setHorizontalAlignment(alignment);
    }

    @Override
    public void setVerticalAlignment(int alignment) {
        super.setVerticalAlignment(alignment);

        this.textLabel.setVerticalAlignment(alignment);
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(false);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);

        this.textLabel.setFont(font.deriveFont(Font.BOLD));
    }

    @Override
    public void setBackground(Color bg) {
    }

    @Override
    public void setForeground(Color fg) {
        this.textLabel.setForeground(fg);

        super.setForeground(Color.LIGHT_GRAY);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        this.textLabel.setVisible(aFlag);
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        super.setMinimumSize(minimumSize);

        this.textLabel.setMinimumSize(minimumSize);
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        super.setMaximumSize(maximumSize);

        this.textLabel.setMaximumSize(maximumSize);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);

        this.textLabel.setPreferredSize(preferredSize);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(mgr);

        this.textLabel.setLayout(mgr);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);

        this.textLabel.setSize(d);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        this.textLabel.setSize(width, height);
    }
}
