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

package antafes.vampireEditor.print;

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.gui.utility.Font;
import antafes.vampireEditor.print.utility.StringProperties;

/**
 * This class provides everything that is needed for showing or printing the backgrounds page.
 *
 * @author Marian Pollzien
 */
public class Backgrounds extends PrintBase {

    /**
     * Constructor
     *
     * @param character The character to display
     */
    public Backgrounds(Character character) {
        super(character);

        this.setPreviousPage(MeritsAndFlaws.class);
        this.setFollowingPage(Looks.class);
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
                this.getLanguage().translate("backgrounds"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addExpandedBackgroundsInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("possessions"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addPossessionsInformation();

            builder.setPosY(this.getMaxY());
            this.setMaxY(this.getMaxY() + 1);
            this.addBar(
                this.getLanguage().translate("haven"),
                this.getImageableWidth(),
                builder.build()
            );
            this.addHavenInformation();
        } catch (EntityException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }

    /**
     * Add the expanded backgrounds.
     */
    protected void addExpandedBackgroundsInformation() {
        this.addBlock(new String[]{"allies", "mentor"});
        this.addBlock(new String[]{"contacts", "resources"});
        this.addBlock(new String[]{"domains", "retainers"});
        this.addBlock(new String[]{"herd", "status"});
        this.addBlock(new String[]{"influence", "other"});
    }

    /**
     * Add the possessions.
     */
    protected void addPossessionsInformation() {
        this.addBlock(new String[]{"gear", "equipment"}, 5);
        this.addBlock(new String[]{"feedingGrounds", "miscellaneous"}, 5);
    }

    /**
     * Add the haven information.
     */
    protected void addHavenInformation() {
        this.addBlock(new String[]{"location", "description"});
    }
}
