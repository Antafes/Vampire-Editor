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
package antafes.vampireEditor.print;

import org.apache.commons.lang3.StringUtils;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.gui.utility.Font;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.language.LanguageInterface;
import antafes.vampireEditor.print.element.BarLabel;
import antafes.vampireEditor.print.utility.Dot;
import antafes.vampireEditor.print.element.DotElement;
import antafes.vampireEditor.print.utility.StringProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Base class for the print page classes.
 *
 * @author Marian Pollzien
 */
public abstract class PrintBase extends JPanel implements TranslatableComponent {
    private final PageFormat pageFormat;
    private final JPanel innerPanel;
    private Character character;
    private final GridBagConstraints constraints;
    private GridBagLayout layout;
    private Configuration configuration;
    private LanguageInterface language;
    private Class followingPage = null;
    private Class previousPage = null;
    private int maxY = 0;

    public PrintBase(Character character) {
        this.configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();
        this.innerPanel = new JPanel();
        this.pageFormat = new PageFormat();
        this.pageFormat.setOrientation(PageFormat.PORTRAIT);
        this.pageFormat.setPaper(new PaperA4());
        this.character = character;
        this.constraints = new GridBagConstraints();

        this.initConstraints();
        this.init();
    }

    private void initConstraints() {
        this.constraints.fill = GridBagConstraints.HORIZONTAL;
        this.constraints.weightx = 1;
        this.constraints.weighty = 1;
        this.constraints.anchor = GridBagConstraints.PAGE_START;
        this.constraints.gridheight = 1;
        this.constraints.gridwidth = 1;
    }

    /**
     * Initialize everything needed for a print page.
     */
    private void init() {
        this.layout = new GridBagLayout();
        this.innerPanel.setLayout(this.layout);
        Dimension size = new Dimension(this.getImageableWidth(), 0);
        this.innerPanel.setSize(size);
        this.innerPanel.setPreferredSize(size);
        this.innerPanel.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(10)
                .addComponent(this.innerPanel, size.width, size.width, size.width)
                .addContainerGap(300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(this.innerPanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    /**
     * Add the component to the inner panel.
     *
     * @param comp Component to add
     * @param constraints An object expressing layout constraints for this component
     * @param index The position on where to add the component
     */
    public void addToInner(Component comp, Object constraints, int index) {
        this.innerPanel.add(comp, constraints, index);
    }

    /**
     * Add the component to the inner panel.
     *
     * @param comp Component to add
     * @param constraints An object expressing layout constraints for this component
     */
    public void addToInner(Component comp, Object constraints) {
        this.innerPanel.add(comp, constraints);
    }

    /**
     * Add the component to the inner panel at the given index.
     *
     * @param comp Component to add
     * @param index The position on where to add the component
     *
     * @return The added component
     */
    public Component addToInner(Component comp, int index) {
        return this.innerPanel.add(comp, index);
    }

    /**
     * Add the component to the inner panel.
     *
     * @param name Name of the component
     * @param comp Component to add
     *
     * @return The added component
     */
    public Component addToInner(String name, Component comp) {
        return this.innerPanel.add(name, comp);
    }

    /**
     * Add the component to the inner panel.
     *
     * @param comp Component to add
     *
     * @return The added component
     */
    public Component addToInner(Component comp) {
        return this.innerPanel.add(comp);
    }

    /**
     * Get the character for which to create the print page.
     *
     * @return
     */
    protected Character getCharacter() {
        return this.character;
    }

    /**
     * Get the configuration object.
     *
     * @return
     */
    protected Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Get the selected language.
     *
     * @return
     */
    protected LanguageInterface getLanguage() {
        return this.language;
    }

    /**
     * Get the grid bag constraints object.
     *
     * @return
     */
    protected GridBagConstraints getConstraints() {
        return constraints;
    }

    /**
     * Get the maximum y in the grid that is used.
     *
     * @return
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Set the language.
     *
     * @param language
     */
    protected void setLanguage(LanguageInterface language) {
        this.language = language;
    }

    /**
     * Set the maximum y in the grid that is used.
     *
     * @param maxY
     */
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    /**
     * Add the horizontal bar without title.
     *
     * @param width Width of the bar
     */
    protected void addBar(int width) {
        StringProperties.Builder builder = new StringProperties.Builder();
        builder.setFontType(Font.HEADLINE);
        builder.setSize(24);

        try {
            this.addBar(null, width, builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add the horizontal bar with title.
     *
     * @param title Title to display above the bar
     * @param width Width of the bar
     */
    protected void addBar(String title, int width) {
        StringProperties.Builder builder = new StringProperties.Builder();
        builder.setFontType(Font.HEADLINE);
        builder.setSize(30f);

        try {
            this.addBar(title, width, builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add the horizontal bar.
     *
     * @param width Width of the bar
     * @param properties Properties for the label
     */
    protected void addBar(int width, StringProperties properties) {
        this.addBar(null, width, properties);
    }

    /**
     * Get the horizontal bar.
     * The title is optional, if set to null, no title will be added and only the bar will show up.
     *
     * @param title Title to display above the bar
     * @param width Width of the bar
     * @param properties Properties for the label
     */
    protected void addBar(String title, int width, StringProperties properties) {
        BarLabel barLabel = new BarLabel();
        ImageIcon bar = new ImageIcon(VampireEditor.getResourceInJar("images/bar.png"));
        int iconHeight = bar.getIconHeight();
        float factor = (float) bar.getIconWidth() / width;
        barLabel.setForeground(Color.BLACK);
        barLabel.setFont(
            Objects.requireNonNull(properties.getFontType().getFont())
                .deriveFont(properties.getFontStyle(), properties.getSize())
        );
        barLabel.setIcon(new ImageIcon(bar.getImage().getScaledInstance(width, (int)(iconHeight / factor), Image.SCALE_DEFAULT)));
        barLabel.setIconTextGap(0);
        barLabel.setHorizontalTextPosition(JLabel.CENTER);
        Dimension size = new Dimension(barLabel.getIcon().getIconWidth(), barLabel.getIcon().getIconHeight());
        barLabel.setSize(size);
        barLabel.setPreferredSize(size);

        if (title != null) {
            barLabel.setText(title);
        }

        this.constraints.insets = new Insets(properties.getMarginTop(), 0, 0, 0);
        this.constraints.gridx = properties.getPosX();
        this.constraints.gridy = properties.getPosY();
        this.constraints.gridwidth = 6;
        this.layout.setConstraints(barLabel, this.constraints);
        this.addToInner(barLabel);
        this.constraints.gridwidth = 1;
    }

    /**
     * Create the dots to show a value.
     * This will create a circular dot in a one wide column.
     *
     * @param posX   The X position of the dot list
     * @param posY   The Y position of the dot list
     * @param max    The maximum amount of dots
     * @param filled The dots that will be filled, starting from the left side
     */
    protected void createDots(int posX, int posY, int max, int filled) {
        this.createDots(posX, posY, max, filled, Dot.CIRCLE);
    }

    /**
     * Create the dots to show a value.This will create a circular dot in a one wide column.
     *
     * @param posX        The X position of the dot list
     * @param posY        The Y position of the dot list
     * @param max         The maximum amount of dots
     * @param filled      The dots that will be filled, starting from the left side
     * @param columnWidth The width of the column this list of dots should be added
     */
    protected void createDots(int posX, int posY, int max, int filled, int columnWidth) {
        this.createDots(posX, posY, max, filled, columnWidth, Dot.CIRCLE);
    }

    /**
     * Create the dots to show a value.
     * This will use a one wide column.
     *
     * @param posX   The X position of the dot list
     * @param posY   The Y position of the dot list
     * @param max    The maximum amount of dots
     * @param filled The dots that will be filled, starting from the left side
     * @param style  The style of the dots
     */
    protected void createDots(int posX, int posY, int max, int filled, Dot style) {
        this.createDots(posX, posY, max, filled, 1, style);
    }

    /**
     * Create the dots to show a value.
     *
     * @param posX        The X position of the dot list
     * @param posY        The Y position of the dot list
     * @param max         The maximum amount of dots
     * @param filled      The dots that will be filled, starting from the left side
     * @param columnWidth The width of the column this list of dots should be added
     * @param style       The style of the dots
     */
    protected void createDots(int posX, int posY, int max, int filled, int columnWidth, Dot style) {
        DotElement dot = new DotElement();

        if (columnWidth == 1) {
            dot.createDots(max, filled, style);
        } else {
            dot.createDots(max, filled, style, false);
        }

        this.constraints.gridx = posX;
        this.constraints.gridy = posY;
        this.constraints.insets = new Insets(2, 0, 0, 0);
        this.constraints.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
        this.constraints.gridwidth = columnWidth;
        this.layout.setConstraints(dot, this.constraints);
        this.addToInner(dot);
        this.constraints.insets = new Insets(0, 0, 0, 0);
        this.constraints.anchor = GridBagConstraints.PAGE_START;
        this.constraints.gridwidth = 1;
    }

    /**
     * Update the texts of every component in the component.
     */
    @Override
    public void updateTexts() {
        this.getConfiguration().loadProperties();
        this.setLanguage(this.getConfiguration().getLanguageObject());

        this.innerPanel.removeAll();
        this.initConstraints();
        this.create();
        this.invalidate();
        this.repaint();
    }

    /**
     * Add a text to be shown, with column width 2 as default.
     *
     * @param text Label text
     * @param posX X position
     * @param posY Y position
     */
    protected void addText(String text, int posX, int posY) {
        this.addText(text, posX, posY, 1);
    }

    /**
     * Add a text to be shown.
     *
     * @param text Label text
     * @param posX X position
     * @param posY Y position
     * @param columnWidth Number of columns to use
     */
    protected void addText(String text, int posX, int posY, int columnWidth) {
        StringProperties.Builder builder = new StringProperties.Builder();
        builder.setPosX(posX);
        builder.setPosY(posY);
        builder.setColumnWidth(columnWidth);

        try {
            this.addString(text, builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add a headline with a given StringProperties object.
     *
     * @param text Label text
     * @param properties Properties for the label
     */
    protected void addHeadline(String text, StringProperties properties) {
        StringProperties.Builder builder = new StringProperties.Builder();
        builder.fillDataFromObject(properties);
        builder.setFontType(Font.HEADLINE);
        builder.setSize(24f);
        builder.setAlignment(SwingConstants.CENTER);
        builder.setFontStyle(java.awt.Font.BOLD);

        try {
            this.addString(text, builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add a headline to be shown.
     *
     * @param text Headline text
     * @param posX X position
     * @param posY Y position
     */
    protected void addHeadline(String text, int posX, int posY) {
        this.addHeadline(text, posX, posY, 24f, 2);
    }

    /**
     * Add a headline to be shown.
     *
     * @param text Headline text
     * @param posX X position
     * @param posY Y position
     * @param size Font size
     */
    protected void addHeadline(String text, int posX, int posY, float size) {
        this.addHeadline(text, posX, posY, size, 2);
    }

    /**
     * Add a headline to be shown.
     *
     * @param text Headline text
     * @param posX X position
     * @param posY Y position
     * @param columnWidth Number of columns to use
     */
    protected void addHeadline(String text, int posX, int posY, int columnWidth) {
        this.addHeadline(text, posX, posY, 24f, columnWidth);
    }

    /**
     * Add a headline to be shown.
     *
     * @param text Headline text
     * @param posX X position
     * @param posY Y position
     * @param size Font size
     * @param columnWidth Number of columns to use
     */
    protected void addHeadline(String text, int posX, int posY, float size, int columnWidth) {
        StringProperties.Builder builder = new StringProperties.Builder();
        builder.setPosX(posX);
        builder.setPosY(posY);
        builder.setSize(size);
        builder.setColumnWidth(columnWidth);
        builder.setAlignment(SwingConstants.CENTER);
        builder.setFontStyle(java.awt.Font.BOLD);

        try {
            this.addString(text, builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add a string to be shown.
     *
     * @param text Label text
     * @param properties Properties for the label
     */
    protected void addString(String text, StringProperties properties) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(properties.getFontColor());
        label.setFont(
            Objects.requireNonNull(properties.getFontType().getFont())
                .deriveFont(properties.getFontStyle(), properties.getSize())
        );
        label.setSize(100, (int) properties.getSize());
        label.setBackground(properties.getBackgroundColor());
        label.setHorizontalAlignment(properties.getAlignment());

        this.constraints.insets = new Insets(
            properties.getMarginTop(),
            properties.getMarginLeft(),
            properties.getMarginBottom(),
            properties.getMarginRight()
        );
        this.constraints.gridx = properties.getPosX();
        this.constraints.gridy = properties.getPosY();
        this.constraints.gridwidth = properties.getColumnWidth();
        this.layout.setConstraints(label, this.constraints);
        this.addToInner(label);
        this.constraints.gridwidth = 1;
    }

    /**
     * Add 2 or 3 headlines to the grid.
     * Has defaults set for either 2 or 3 headlines x positions.
     *
     * @param headlines Array of two or three headlines
     */
    protected void addHeadlines(String[] headlines) {
        int[] xPositions = {};

        if (headlines.length == 2) {
            xPositions = new int[]{
                PositionX.LEFT1.getPosition(),
                PositionX.RIGHT1.getPosition()
            };
        } else if (headlines.length == 3) {
            xPositions = new int[]{
                PositionX.LEFT1.getPosition(),
                PositionX.MIDDLE1.getPosition(),
                PositionX.RIGHT1.getPosition()
            };
        }

        this.addHeadlines(headlines, xPositions);
    }

    /**
     * Add 2 or 3 headlines to the grid.
     *
     * @param headlines Array of two or three headlines
     * @param xPositions Array of x positions to use for the headlines
     */
    protected void addHeadlines(String[] headlines, int[] xPositions) {
        int columnWidth = 2;

        if (headlines.length == 2) {
            columnWidth = 3;
        }

        for (int i = 0; i < headlines.length; i++) {
            this.addHeadline(
                this.getLanguage().translate(headlines[i]),
                xPositions[i],
                this.maxY,
                20f,
                columnWidth
            );
        }
    }

    /**
     * Add a two column block with the given headlines.
     * Will use 3 as the default block height.
     *
     * @param headlines Array of headlines
     */
    protected void addBlock(String[] headlines) {
        this.addBlock(headlines, 3);
    }

    /**
     * Add a two column block with the given headlines.
     *
     * @param headlines Array of headlines
     * @param blockHeight Height of the block
     */
    protected void addBlock(String[] headlines, int blockHeight) {
        int[] xPositions = {
            PositionX.LEFT1.getPosition(),
            PositionX.MIDDLE2.getPosition()
        };
        this.addHeadlines(
            headlines,
            xPositions
        );
        this.setMaxY(this.getMaxY() + 1);
        for (int i = 0; i < blockHeight; i++) {
            this.addText(
                StringUtils.repeat('_', 58),
                PositionX.LEFT1.getPosition(),
                this.getMaxY(),
                3
            );
            this.addText(
                StringUtils.repeat('_', 58),
                PositionX.MIDDLE2.getPosition(),
                this.getMaxY(),
                3
            );
            this.setMaxY(this.getMaxY() + 1);
        }
    }

    /**
     * Add a value entry with a row of dots.
     * This will fetch the maximum dots value from the character.
     *
     * @param title   Title of the entry
     * @param xTitle  X Position in the grid for the title
     * @param yTitle  Y Position in the grid for the title
     * @param xDot    X position in the grid for the dot
     * @param value   Value for the entries dots
     */
    protected void addValueEntry(String title, int xTitle, int yTitle, int xDot, int value) {
        this.addValueEntry(
            title,
            xTitle,
            yTitle,
            xDot,
            value,
            this.getCharacter().getGeneration().getMaximumAttributes()
        );
    }

    /**
     * Add a value entry with a row of dots.
     *
     * @param title   Title of the entry
     * @param xTitle  X Position in the grid for the title
     * @param yTitle  Y Position in the grid for the title
     * @param xDot    X position in the grid for the dot
     * @param value   Value for the entries dots
     * @param maxDots The maximum amount of dots
     */
    protected void addValueEntry(String title, int xTitle, int yTitle, int xDot, int value, int maxDots) {
        this.addText(
            this.getLanguage().translate(title),
            xTitle,
            yTitle
        );
        this.createDots(
            xDot,
            yTitle,
            maxDots,
            value
        );
    }

    /**
     * Add a value entry with a row of dots.
     *
     * @param title  Title of the entry
     * @param xTitle X Position in the grid for the title
     * @param row    Y Position in the grid for the title
     * @param xValue X position in the grid for the value
     * @param value  Value for the entry to display
     */
    protected void addValueEntry(String title, int xTitle, int row, int xValue, String value) {
        this.addText(
            this.getLanguage().translate(title),
            xTitle,
            row
        );
        this.addText(
            value,
            xValue,
            row
        );
    }

    /**
     * Create the print html.
     */
    abstract public void create();

    /**
     * Get the imageable width of this container.
     *
     * @return
     */
    protected int getImageableWidth() {
        Double imageableWidth = this.pageFormat.getWidth();

        return imageableWidth.intValue();
    }

    /**
     * Get the following page object.
     *
     * @return
     */
    public PrintBase getFollowingPageObject() {
        if (this.followingPage == null) {
            return null;
        }

        try {
            return (PrintBase) this.followingPage.getConstructor(Character.class)
                .newInstance(this.getCharacter());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            VampireEditor.log("Failed to fetch the next page object.");
        }

        return null;
    }

    /**
     * Get the previous page object.
     *
     * @return
     */
    public PrintBase getPreviousPageObject() {
        if (this.previousPage == null) {
            return null;
        }

        try {
            return (PrintBase) this.previousPage.getConstructor(Character.class)
                .newInstance(this.getCharacter());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            VampireEditor.log("Failed to fetch the previous page object.");
        }

        return null;
    }

    /**
     * The the following page class.
     *
     * @param followingPage
     */
    protected void setFollowingPage(Class followingPage) {
        this.followingPage = followingPage;
    }

    /**
     * Set the previous page class.
     *
     * @param previousPage
     */
    protected void setPreviousPage(Class previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * Enum for the 3 columns in display.
     */
    public enum PositionX {
        LEFT1 (0),
        LEFT2 (1),
        MIDDLE1 (2),
        MIDDLE2 (3),
        RIGHT1 (4),
        RIGHT2 (5);

        private final int position;

        PositionX(int position) {
            this.position = position;
        }

        public int getPosition() {
            return this.position;
        }
    }
}
