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
package antafes.vampireEditor.print.utility;

import antafes.vampireEditor.entity.BaseEntity;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.gui.utility.Font;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A string properties object to use for the print and print preview.
 *
 * @author Marian Pollzien
 */
public class StringProperties {
    private final int posX;
    private final int posY;
    private final Font fontType;
    private final float size;
    private final Color fontColor;
    private final int fontStyle;
    private final Color backgroundColor;
    private final int marginTop;
    private final int marginRight;
    private final int marginBottom;
    private final int marginLeft;
    private final int columnWidth;
    private final int alignment;

    /**
     * Build for the StringProperties object.
     */
    public static class Builder {
        private int posX;
        private int posY;
        private Font fontType;
        private float size;
        private Color fontColor;
        private int fontStyle;
        private Color backgroundColor;
        private int marginTop;
        private int marginRight;
        private int marginBottom;
        private int marginLeft;
        private int columnWidth;
        private int alignment;

        /**
         * Constructor
         */
        public Builder() {
            this.posX = 0;
            this.posY = 0;
            this.fontType = Font.TEXT;
            this.fontColor = Color.BLACK;
            this.fontStyle = java.awt.Font.PLAIN;
            this.backgroundColor = Color.WHITE;
            this.size = 14f;
            this.marginTop = 0;
            this.marginRight = 0;
            this.marginBottom = 0;
            this.marginLeft = 0;
            this.columnWidth = 1;
            this.alignment = SwingConstants.LEFT;
        }

        /**
         * Build a new StringProperties object with the given values.
         *
         * @return The created StringProperties object
         * @throws antafes.vampireEditor.entity.EntityException Thrown if a required field is not set
         */
        public StringProperties build() throws EntityException {
            List<Integer> styleList = Arrays.asList(java.awt.Font.BOLD, java.awt.Font.ITALIC, java.awt.Font.PLAIN);
            if (!styleList.contains(this.fontStyle)) {
                throw new EntityException("Wrong font style.");
            }

            List<Integer> alignmentList = Arrays.asList(SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT);
            if (!alignmentList.contains(this.alignment)) {
                throw new EntityException("Wrong alignment value.");
            }

            return new StringProperties(this);
        }

        /**
         * Fill every property from the given object into this builder.
         *
         * @param object StringProperties object to get data from
         *
         * @return Builder object to create a new StringProperties object with
         */
        public Builder fillDataFromObject(Object object) {
            this.getDataMethods().forEach((declaredMethod) -> {
                try {
                    Method setter = this.getSetter(declaredMethod);
                    setter.invoke(this, declaredMethod.invoke(object, (Object[])null));
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(BaseEntity.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return this;
        }

        /**
         * Set the X position.
         *
         * @param posX
         *
         * @return
         */
        public Builder setPosX(int posX) {
            this.posX = posX;

            return this;
        }

        /**
         * Set the Y position.
         *
         * @param posY
         *
         * @return
         */
        public Builder setPosY(int posY) {
            this.posY = posY;

            return this;
        }

        /**
         * Set the font.
         *
         * @param fontType
         *
         * @return
         */
        public Builder setFontType(Font fontType) {
            this.fontType = fontType;

            return this;
        }

        /**
         * Set the font size.
         *
         * @param size
         *
         * @return
         */
        public Builder setSize(float size) {
            this.size = size;

            return this;
        }

        /**
         * Set the font color.
         *
         * @param fontColor
         *
         * @return
         */
        public Builder setFontColor(Color fontColor) {
            this.fontColor = fontColor;

            return this;
        }

        /**
         * Set the font style.
         * This uses the java.awt.Font constant values.
         *
         * @param fontStyle
         *
         * @return
         */
        public Builder setFontStyle(int fontStyle) {
            this.fontStyle = fontStyle;

            return this;
        }

        /**
         * Set the background color.
         *
         * @param backgroundColor
         *
         * @return
         */
        public Builder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;

            return this;
        }

        /**
         * Set the top margin.
         *
         * @param marginTop
         *
         * @return
         */
        public Builder setMarginTop(int marginTop) {
            this.marginTop = marginTop;

            return this;
        }

        /**
         * Set the right margin.
         *
         * @param marginRight
         *
         * @return
         */
        public Builder setMarginRight(int marginRight) {
            this.marginRight = marginRight;

            return this;
        }

        /**
         * Set the bottom margin.
         *
         * @param marginBottom
         *
         * @return
         */
        public Builder setMarginBottom(int marginBottom) {
            this.marginBottom = marginBottom;

            return this;
        }

        /**
         * Set the left margin.
         *
         * @param marginLeft
         *
         * @return
         */
        public Builder setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;

            return this;
        }

        /**
         * Set the grid bag column width.
         *
         * @param columnWidth
         *
         * @return
         */
        public Builder setColumnWidth(int columnWidth) {
            this.columnWidth = columnWidth;

            return this;
        }

        /**
         * Set the alignment of the text.
         * This uses the SwingConstants values.
         *
         * @param alignment
         *
         * @return
         */
        public Builder setAlignment(int alignment) {
            this.alignment = alignment;

            return this;
        }

        /**
         * Check whether the given method can be used to fill in data.
         *
         * @param method The method to check.
         *
         * @return True if the method is not a getter
         */
        protected boolean checkMethod(Method method) {
            return !method.getName().startsWith("get") || method.getName().equals("getDataMethods");
        }

        /**
         * Get the list of methods from which data can be fetched.
         *
         * @return
         */
        private ArrayList<Method> getDataMethods() {
            ArrayList<Method> methodList = new ArrayList<>();

            for (Method declaredMethod : StringProperties.class.getDeclaredMethods()) {
                if (this.checkMethod(declaredMethod)) {
                    continue;
                }

                methodList.add(declaredMethod);
            }

            return methodList;
        }

        /**
         * Get a setter method from the given getter.
         *
         * @param getter The getter method object
         *
         * @return The setter method
         * @throws NoSuchMethodException Thrown if a setter by that name couldn't be found
         */
        private Method getSetter(Method getter) throws NoSuchMethodException {
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = getter.getReturnType();

            return StringProperties.Builder.class.getDeclaredMethod("set" + getter.getName().substring(3), parameterTypes);
        }
    }

    /**
     * Constructor
     *
     * @param builder Builder object
     */
    private StringProperties(Builder builder) {
        this.posX = builder.posX;
        this.posY = builder.posY;
        this.fontType = builder.fontType;
        this.size = builder.size;
        this.fontColor = builder.fontColor;
        this.fontStyle = builder.fontStyle;
        this.backgroundColor = builder.backgroundColor;
        this.marginTop = builder.marginTop;
        this.marginRight = builder.marginRight;
        this.marginBottom = builder.marginBottom;
        this.marginLeft = builder.marginLeft;
        this.columnWidth = builder.columnWidth;
        this.alignment = builder.alignment;
    }

    /**
     * Get the X position.
     *
     * @return
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Get the Y position.
     *
     * @return
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Get the font. Default will be Font.TEXT
     *
     * @return
     */
    public Font getFontType() {
        return fontType;
    }

    /**
     * Get the font size. Default will be 14.
     *
     * @return
     */
    public float getSize() {
        return size;
    }

    /**
     * Get the font color.
     * If none is set, will default to black.
     *
     * @return
     */
    public Color getFontColor() {
        return fontColor;
    }

    /**
     * Get the font style according to the java.awt.Font constants.
     *
     * @return
     */
    public int getFontStyle() {
        return fontStyle;
    }

    /**
     * Get the background color.
     * If none is set, will default to white.
     *
     * @return
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Get the top margin.
     *
     * @return
     */
    public int getMarginTop() {
        return marginTop;
    }

    /**
     * Get the right margin.
     *
     * @return
     */
    public int getMarginRight() {
        return marginRight;
    }

    /**
     * Get the bottom margin.
     *
     * @return
     */
    public int getMarginBottom() {
        return marginBottom;
    }

    /**
     * Get the left margin.
     *
     * @return
     */
    public int getMarginLeft() {
        return marginLeft;
    }

    /**
     * Get the grid bag column width.
     *
     * @return
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /**
     * Get the alignment according to the SwingConstants.
     *
     * @return
     */
    public int getAlignment() {
        return alignment;
    }
}
