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

package vampireEditor.print;

import org.apache.commons.lang3.StringUtils;
import vampireEditor.VampireEditor;
import vampireEditor.entity.Character;
import vampireEditor.entity.EntityException;
import vampireEditor.gui.utility.Font;
import vampireEditor.print.utility.StringProperties;

import java.text.DateFormat;

public class Looks extends PrintBase {
    /**
     * Constructor
     *
     * @param character The character to display
     */
    public Looks(Character character) {
        super(character);

        this.setPreviousPage(Backgrounds.class);
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
                this.getLanguage().translate("history"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addHistoryInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("description"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addDescription();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("visuals"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addVisualsInformation();
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add the expanded backgrounds.
     */
    protected void addHistoryInformation() {
        for (int i = 0; i < 11; i++) {
            this.addText(
                StringUtils.repeat('_', 115),
                PositionX.LEFT1.getPosition(),
                this.getMaxY(),
                6
            );
            this.setMaxY(this.getMaxY() + 1);
        }

        this.addBlock(new String[]{"momentsOfTruth", "goalsAndPlots"}, 4);
    }

    /**
     * Add the description and the looks values
     */
    protected void addDescription() {
        int yLeft = this.getMaxY();
        this.addValueEntry(
            "age",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            Integer.toString(this.getCharacter().getAge())
        );
        this.addValueEntry(
            "apparentAge",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            Integer.toString(this.getCharacter().getLooksLikeAge())
        );
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        this.addValueEntry(
            "dayOfBirth",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            formatter.format(this.getCharacter().getDayOfBirth())
        );
        this.addValueEntry(
            "dayOfDeath",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getDayOfDeath() != null ? formatter.format(this.getCharacter().getDayOfBirth()) : ""
        );
        this.addValueEntry(
            "hairColor",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getHairColor()
        );
        this.addValueEntry(
            "eyeColor",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getEyeColor()
        );
        this.addValueEntry(
            "skinColor",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getSkinColor()
        );
        this.addValueEntry(
            "nationality",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getNationality()
        );
        this.addValueEntry(
            "size",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            Integer.toString(this.getCharacter().getSize())
        );
        this.addValueEntry(
            "weight",
            PositionX.LEFT1.getPosition(),
            yLeft++,
            PositionX.LEFT2.getPosition(),
            Integer.toString(this.getCharacter().getWeight())
        );
        this.addValueEntry(
            "sex",
            PositionX.LEFT1.getPosition(),
            yLeft,
            PositionX.LEFT2.getPosition(),
            this.getCharacter().getSex().toString()
        );

        for (int i = 0; i < 11; i++) {
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
     * Add the visuals part
     */
    protected void addVisualsInformation() {
        this.addHeadlines(new String[]{"coterieChart", "characterSketch"});

        for (int i = 0; i < 11; i++) {
            this.addText(
                " ",
                PositionX.LEFT1.getPosition(),
                this.getMaxY(),
                6
            );
            this.setMaxY(this.getMaxY() + 1);
        }
    }
}
