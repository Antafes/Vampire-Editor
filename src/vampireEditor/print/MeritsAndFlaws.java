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

import org.apache.commons.lang3.StringUtils;
import vampireEditor.VampireEditor;
import vampireEditor.entity.Character;
import vampireEditor.entity.EntityException;
import vampireEditor.entity.character.Flaw;
import vampireEditor.entity.character.Merit;
import vampireEditor.gui.utility.Font;
import vampireEditor.print.model.WeaponTableModel;
import vampireEditor.print.utility.StringProperties;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides everything that is needed for showing or printing the merits and flaws page.
 *
 * @author Marian Pollzien
 */
public class MeritsAndFlaws extends PrintBase {
    /**
     * Create a new looks print object.
     *
     * @param character The character to display
     */
    public MeritsAndFlaws(Character character) {
        super(character);

        this.setPreviousPage(General.class);
        this.setFollowingPage(Backgrounds.class);
    }

    /**
     * Create the print html.
     */
    @Override
    public void create() {
        try {
            StringProperties.Builder builder = new StringProperties.Builder();
            builder.setFontType(Font.HEADLINE);
            builder.setSize(30f);
            builder.setPosX(0);
            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);

            this.addBar(
                this.getLanguage().translate("merits") + " & " + this.getLanguage().translate("flaws"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addMeritAndFlawInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("rituals") + " & " + this.getLanguage().translate("paths"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addRitualsAndPaths();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("advantages"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addDisciplineTechniqueInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("combat"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addCombatInformation();
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add the characters attributes.
     */
    protected void addMeritAndFlawInformation() {
        String[] headlines = {"merit", "flaw"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yMerit = this.getMaxY(), yFlaw = this.getMaxY();

        for (Merit merit : this.getCharacter().getMerits()) {
            this.addText(
                merit.getName(),
                PositionX.LEFT1.getPosition(),
                yMerit,
                3
            );
            this.addText(
                Integer.toString(merit.getCost()),
                PositionX.MIDDLE1.getPosition(),
                yMerit++
            );
            this.setMaxY(this.getMaxY() + 1);
        }

        for (Flaw flaw : this.getCharacter().getFlaws()) {
            this.addValueEntry(
                flaw.getName(),
                PositionX.RIGHT1.getPosition(),
                yFlaw++,
                PositionX.RIGHT2.getPosition(),
                Integer.toString(flaw.getCost())
            );
        }
    }

    /**
     * Add the characters abilities.
     */
    protected void addRitualsAndPaths() {
        String[] headlines = {"rituals", "paths"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yRituals = this.getMaxY(), ySkills = this.getMaxY();
        StringProperties.Builder propertiesBuilder = new StringProperties.Builder()
            .setMarginRight(4);

        for (int i = 0; i < 8; i++) {
            try {
                this.addString(
                    StringUtils.repeat('_', 45),
                    propertiesBuilder
                        .setColumnWidth(3)
                        .setPosX(PositionX.LEFT1.getPosition())
                        .setPosY(yRituals)
                        .build()
                );
                this.addString(
                    "_____",
                    propertiesBuilder
                        .setPosX(PositionX.MIDDLE2.getPosition())
                        .setPosY(yRituals++)
                        .setColumnWidth(1)
                        .build()
                );
            } catch (EntityException e) {
                e.printStackTrace();
            }
            this.setMaxY(this.getMaxY() + 1);
        }

        for (int i = 0; i < 8; i++) {
            this.addValueEntry(
                "___________________________________",
                PositionX.RIGHT1.getPosition(),
                ySkills++,
                PositionX.RIGHT2.getPosition(),
                0,
                5
            );
        }
    }

    /**
     * Add the characters advantages.
     */
    protected void addDisciplineTechniqueInformation() {
        String[] headlines = {"name", "source", "system"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yName = this.getMaxY(), ySource = this.getMaxY(), ySystem = this.getMaxY();
        StringProperties.Builder propertiesBuilder = new StringProperties.Builder()
            .setMarginRight(4)
            .setColumnWidth(2);

        for (int i = 0; i < 8; i++) {
            try {
                this.addString(
                    StringUtils.repeat('_', 30),
                    propertiesBuilder
                        .setPosX(PositionX.LEFT1.getPosition())
                        .setPosY(yName++)
                        .build()
                );
            } catch (EntityException e) {
                e.printStackTrace();
            }
            this.setMaxY(this.getMaxY() + 1);
        }

        for (int i = 0; i < 8; i++) {
            try {
                this.addString(
                    StringUtils.repeat('_', 30),
                    propertiesBuilder
                        .setPosX(PositionX.MIDDLE1.getPosition())
                        .setPosY(ySource++)
                        .build()
                );
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 8; i++) {
            try {
                this.addString(
                    StringUtils.repeat('_', 55),
                    propertiesBuilder
                        .setPosX(PositionX.RIGHT1.getPosition())
                        .setPosY(ySystem++)
                        .build()
                );
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add additional information like traits, the selected road etc.
     */
    protected void addCombatInformation() {
        int yRight = this.getMaxY();

        this.addWeaponTable();
        this.addHeadline(this.getLanguage().translate("armor"), PositionX.RIGHT2.getPosition(), yRight++);
        this.addText(
            StringUtils.rightPad(
                this.getLanguage().translate("class") + ":",
                20,
                '_'
            ),
            PositionX.RIGHT2.getPosition(),
            yRight++
        );
        this.addText(
            StringUtils.rightPad(
                this.getLanguage().translate("rating") + ":",
                20,
                '_'
            ),
            PositionX.RIGHT2.getPosition(),
            yRight++
        );
        this.addText(
            StringUtils.rightPad(
                this.getLanguage().translate("penalty") + ":",
                20,
                '_'
            ),
            PositionX.RIGHT2.getPosition(),
            yRight++
        );
        this.addText(
            this.getLanguage().translate("description") + ":",
            PositionX.RIGHT2.getPosition(),
            yRight++
        );

        for (int i = 0; i < 3; i++) {
            this.addText(
                StringUtils.repeat("_", 20),
                PositionX.RIGHT2.getPosition(),
                yRight++,
                2
            );
        }
    }

    /**
     * Add a table used for showing weapons.
     */
    private void addWeaponTable() {
        JTable table = new JTable(new WeaponTableModel());
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoscrolls(false);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(10);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);

        GridBagConstraints constraints = this.getConstraints();
        constraints.gridwidth = 5;
        constraints.gridheight = 7;
        Insets oldInset = constraints.insets;
        constraints.insets = new Insets(1, 1, 1, 10);
        constraints.gridx = PositionX.LEFT1.getPosition();
        constraints.gridy = this.getMaxY();
        this.addToInner(table, constraints);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = oldInset;
    }
}
