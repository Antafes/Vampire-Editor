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
import vampireEditor.entity.character.*;
import vampireEditor.gui.Font;
import vampireEditor.print.utility.Dot;
import vampireEditor.print.utility.StringProperties;

import javax.swing.*;

/**
 * This class provides everything that is needed for showing or printing the looks page.
 *
 * @author Marian Pollzien
 */
public class General extends PrintBase {
    /**
     * Create a new looks print object.
     *
     * @param character The character to display
     */
    public General(Character character) {
        super(character);

        this.setFollowingPage(MeritsAndFlaws.class);
    }

    /**
     * Create the print html.
     */
    @Override
    public void create() {
        try {
            this.addGeneralInformation();
            StringProperties.Builder builder = new StringProperties.Builder();
            builder.setFontType(Font.HEADLINE);
            builder.setSize(30f);
            builder.setPosX(0);
            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);

            this.addBar(
                this.getLanguage().translate("attributes"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addAttributeInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("abilities"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addAbilityInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("advantages"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addAdvantageInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getImageableWidth(),
                builder.build()
            );
            this.addAdditionalInformation();
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add general information like name of the character.
     */
    protected void addGeneralInformation() {
        String[] texts = {
            this.getLanguage().translate("name") + ": " + this.getCharacter().getName(),
            this.getLanguage().translate("player") + ": " + this.getCharacter().getPlayer(),
            this.getLanguage().translate("chronicle") + ": " + this.getCharacter().getChronicle(),
            this.getLanguage().translate("nature") + ": " + this.getCharacter().getNature(),
            this.getLanguage().translate("demeanor") + ": " + this.getCharacter().getDemeanor(),
            this.getLanguage().translate("concept") + ": " + this.getCharacter().getConcept(),
            this.getLanguage().translate("clan") + ": " + this.getCharacter().getClan().getName(),
            this.getLanguage().translate("generation") + ": " + this.getCharacter().getGeneration().toString(),
            this.getLanguage().translate("sire") + ": " + this.getCharacter().getSire()
        };
        int[] xPositions = {
            PositionX.LEFT1.getPosition(),
            PositionX.LEFT1.getPosition(),
            PositionX.LEFT1.getPosition(),
            PositionX.MIDDLE1.getPosition(),
            PositionX.MIDDLE1.getPosition(),
            PositionX.MIDDLE1.getPosition(),
            PositionX.RIGHT1.getPosition(),
            PositionX.RIGHT1.getPosition(),
            PositionX.RIGHT1.getPosition()
        };

        int y = this.getMaxY();
        for (int i = 0; i < 9; i++) {
            if (y == this.getMaxY() + 3) {
                y = this.getMaxY();
            }

            this.addText(
                texts[i],
                xPositions[i],
                y,
                2
            );
            y++;
        }
        this.setMaxY(this.getMaxY() + ++y);
    }

    /**
     * Add the characters attributes.
     */
    protected void addAttributeInformation() {
        String[] headlines = {"physical", "social", "mental"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yPhysical = this.getMaxY(), ySocial = this.getMaxY(), yMental = this.getMaxY();

        for (Attribute attribute : this.getCharacter().getAttributesByType(AttributeInterface.AttributeType.PHYSICAL)) {
            this.addValueEntry(
                attribute.getName(),
                PositionX.LEFT1.getPosition(),
                yPhysical++,
                PositionX.LEFT2.getPosition(),
                attribute.getValue()
            );
            this.setMaxY(this.getMaxY() + 1);
        }

        for (Attribute attribute : this.getCharacter().getAttributesByType(AttributeInterface.AttributeType.SOCIAL)) {
            this.addValueEntry(
                attribute.getName(),
                PositionX.MIDDLE1.getPosition(),
                ySocial++,
                PositionX.MIDDLE2.getPosition(),
                attribute.getValue()
            );
        }

        for (Attribute attribute : this.getCharacter().getAttributesByType(AttributeInterface.AttributeType.MENTAL)) {
            this.addValueEntry(
                attribute.getName(),
                PositionX.RIGHT1.getPosition(),
                yMental++,
                PositionX.RIGHT2.getPosition(),
                attribute.getValue()
            );
        }
    }

    /**
     * Add the characters abilities.
     */
    protected void addAbilityInformation() {
        String[] headlines = {"talents", "skills", "knowledges"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yTalents = this.getMaxY(), ySkills = this.getMaxY(), yKnowledges = this.getMaxY();

        for (Ability ability : this.getCharacter().getAbilitiesByType(AbilityInterface.AbilityType.TALENT)) {
            this.addValueEntry(
                ability.getName(),
                PositionX.LEFT1.getPosition(),
                yTalents++,
                PositionX.LEFT2.getPosition(),
                ability.getValue()
            );
            this.setMaxY(this.getMaxY() + 1);
        }

        for (Ability ability : this.getCharacter().getAbilitiesByType(AbilityInterface.AbilityType.SKILL)) {
            this.addValueEntry(
                ability.getName(),
                PositionX.MIDDLE1.getPosition(),
                ySkills++,
                PositionX.MIDDLE2.getPosition(),
                ability.getValue()
            );
        }

        for (Ability ability : this.getCharacter().getAbilitiesByType(AbilityInterface.AbilityType.KNOWLEDGE)) {
            this.addValueEntry(
                ability.getName(),
                PositionX.RIGHT1.getPosition(),
                yKnowledges++,
                PositionX.RIGHT2.getPosition(),
                ability.getValue()
            );
        }
    }

    /**
     * Add the characters advantages.
     */
    protected void addAdvantageInformation() {
        String[] headlines = {"disciplins", "background", "virtues"};
        this.addHeadlines(headlines);
        this.setMaxY(this.getMaxY() + 1);
        int yDisciplins = this.getMaxY(), yBackground = this.getMaxY(), yVirtues = this.getMaxY();

        for (Advantage advantage : this.getCharacter().getAdvantagesByType(AdvantageInterface.AdvantageType.DISCIPLINE)) {
            this.addValueEntry(
                advantage.getName(),
                PositionX.LEFT1.getPosition(),
                yDisciplins++,
                PositionX.LEFT2.getPosition(),
                advantage.getValue()
            );
            this.setMaxY(this.getMaxY() + 1);
        }

        for (Advantage advantage : this.getCharacter().getAdvantagesByType(AdvantageInterface.AdvantageType.BACKGROUND)) {
            this.addValueEntry(
                advantage.getName(),
                PositionX.MIDDLE1.getPosition(),
                yBackground++,
                PositionX.MIDDLE2.getPosition(),
                advantage.getValue()
            );
        }

        for (Advantage advantage : this.getCharacter().getAdvantagesByType(AdvantageInterface.AdvantageType.VIRTUE)) {
            this.addValueEntry(
                advantage.getName(),
                PositionX.RIGHT1.getPosition(),
                yVirtues++,
                PositionX.RIGHT2.getPosition(),
                advantage.getValue()
            );
        }
    }

    /**
     * Add additional information like traits, the selected road etc.
     */
    protected void addAdditionalInformation() {
        int yLeft = this.getMaxY(), yMiddle = this.getMaxY(), yRight = this.getMaxY();

        this.addHeadline(this.getLanguage().translate("otherTraits"), PositionX.LEFT1.getPosition(), yLeft, 20f);
        yLeft++;

        for (int i = 0; i < 9; i++) {
            this.addText(StringUtils.repeat('_', 35), PositionX.LEFT1.getPosition(), yLeft++, 2);
        }

        this.addHeadline(this.getLanguage().translate("road"), PositionX.MIDDLE1.getPosition(), yMiddle++, 20f);
        this.addText(this.getCharacter().getRoad().getName(), PositionX.MIDDLE1.getPosition(), yMiddle++, 2);
        this.createDots(PositionX.MIDDLE1.getPosition(), yMiddle++, 10, this.getCharacter().getRoad().getValue(), 2);
        this.addHeadline(this.getLanguage().translate("willpower"), PositionX.MIDDLE1.getPosition(), yMiddle++, 20f);
        this.createDots(PositionX.MIDDLE1.getPosition(), yMiddle++, 10, this.getCharacter().getWillpower(), 2);
        this.createDots(PositionX.MIDDLE1.getPosition(), yMiddle++, 10, this.getCharacter().getUsedWillpower(), 2, Dot.SQUARE);
        this.addHeadline(this.getLanguage().translate("bloodStock"), PositionX.MIDDLE1.getPosition(), yMiddle++, 20f);

        int bloodStock = this.getCharacter().getBloodStock();
        for (int i = 0; i < 3; i++) {
            this.createDots(PositionX.MIDDLE1.getPosition(), yMiddle++, 10, bloodStock, 2, Dot.SQUARE);
            bloodStock -= 10;

            if (bloodStock < 0) {
                bloodStock = 0;
            }
        }

        this.addHeadline(this.getLanguage().translate("health"), PositionX.RIGHT1.getPosition(), yRight++, 20f);
        this.addText(this.getLanguage().translate("bruised"), PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("hurt") + " (-1)", PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("injured") + " (-1)", PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("wounded") + " (-2)", PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("mauled") + " (-2)", PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("crippled") + " (-5)", PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addText(this.getLanguage().translate("incapacitated"), PositionX.RIGHT1.getPosition(), yRight);
        this.createDots(PositionX.RIGHT2.getPosition(), yRight++, 1, 0, Dot.SQUARE);
        this.addHeadline(this.getLanguage().translate("experience"), PositionX.RIGHT1.getPosition(), yRight++, 20f);
        try {
            StringProperties.Builder builder = new StringProperties.Builder();
            builder.setPosX(PositionX.RIGHT1.getPosition());
            builder.setPosY(yRight);
            builder.setColumnWidth(2);
            builder.setSize(18f);
            builder.setFontStyle(java.awt.Font.BOLD);
            builder.setAlignment(SwingConstants.CENTER);
            this.addString(Integer.toString(this.getCharacter().getExperience()), builder.build());
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }
}
