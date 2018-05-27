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

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import vampireEditor.Configuration;
import vampireEditor.character.AttributeInterface;
import vampireEditor.character.Clan;
import vampireEditor.utility.TranslatedComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AttributesPanel extends BaseListPanel {

    public AttributesPanel(NewCharacterDialog parent, Configuration configuration) {
        super(parent, configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        super.init();

        this.addPhyiscalFields();
        this.addSocialFields();
        this.addMentalFields();
    }

    /**
     * Add all talent fields sorted by the translated name.
     */
    private void addPhyiscalFields() {
        ArrayList<String> physical = AttributeInterface.AttributeType.PHYSICAL.getAttributes();
        physical.sort(new TranslatedComparator());
        this.addFields("physical", physical, 1);
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSocialFields() {
        ArrayList<String> social = AttributeInterface.AttributeType.SOCIAL.getAttributes();
        social.sort(new TranslatedComparator());
        this.addFields("social", social, 1);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addMentalFields() {
        ArrayList<String> mental = AttributeInterface.AttributeType.MENTAL.getAttributes();
        mental.sort(new TranslatedComparator());
        this.addFields("mental", mental, 1);
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    @Override
    protected ComponentChangeListener createChangeListener() {
        return new ComponentChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getFields("physical").contains(this.getComponent())) {
                    calculateUsedPhyiscalPoints();
                }

                if (getFields("social").contains(this.getComponent())) {
                    calculateUsedSocialPoints();
                }

                if (getFields("mental").contains(this.getComponent())) {
                    calculateUsedMentalPoints();
                }

                checkFieldsFilled();
                getParentComponent().calculateUsedFreeAdditionalPoints();
            }
        };
    }

    /**
     * Calculate the used physical attribute points.
     */
    private void calculateUsedPhyiscalPoints() {
        this.calculateUsedPoints("physical");
    }

    /**
     * Calculate and return the sum of points spent for physical attributes.
     *
     * @return
     */
    public int getPhysicalPointsSum() {
        return this.getPointsSum("physical");
    }

    /**
     * Check if the spent points for physical attributes is above its maximum.
     *
     * @return
     */
    public boolean checkPhyiscalPoints() {
        return this.checkPoints("physical");
    }

    /**
     * Get the maximum points available for physical attributes.
     *
     * @return
     */
    public int getPhyiscalMaxPoints() {
        return this.getMaxPoints("physical");
    }

    /**
     * Calculate the used social attribute points.
     */
    private void calculateUsedSocialPoints() {
        this.calculateUsedPoints("social");
    }

    /**
     * Calculate and return the sum of points spent for social attributes.
     *
     * @return
     */
    public int getSocialPointsSum() {
        return this.getPointsSum("social");
    }

    /**
     * Check if the spent points for social attributes is above its maximum.
     *
     * @return
     */
    public boolean checkSocialPoints() {
        return this.checkPoints("social");
    }

    /**
     * Get the maximum points available for social attributes.
     *
     * @return
     */
    public int getSocialMaxPoints() {
        return this.getMaxPoints("social");
    }

    /**
     * Calculate the used mental attribute points.
     */
    private void calculateUsedMentalPoints() {
        this.calculateUsedPoints("mental");
    }

    /**
     * Calculate and return the sum of points spent for mental attributes.
     *
     * @return
     */
    public int getMentalPointsSum() {
        return this.getPointsSum("mental");
    }

    /**
     * Check if the spent points for mental attributes is above its maximum.
     *
     * @return
     */
    public boolean checkMentalPoints() {
        return this.checkPoints("mental");
    }

    /**
     * Get the maximum points available for mental attributes.
     *
     * @return
     */
    public int getMentalMaxPoints() {
        return this.getMaxPoints("mental");
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("physical").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("social").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("mental").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });

        this.calculateUsedMentalPoints();
        this.calculateUsedSocialPoints();
        this.calculateUsedPhyiscalPoints();
    }

    /**
     * Check if every attribute has been set.
     */
    @Override
    protected void checkFieldsFilled() {
        int physicalSum = this.getPhysicalPointsSum();
        int physicalMax = this.getPhyiscalMaxPoints();
        int socialSum = this.getSocialPointsSum();
        int socialMax = this.getSocialMaxPoints();
        int mentalSum = this.getMentalPointsSum();
        int mentalMax = this.getMentalMaxPoints();

        if (physicalSum >= physicalMax
            && socialSum >= socialMax
            && mentalSum >= mentalMax
            && this.getParentComponent().getMaxActiveTab() < 3
        ) {
            this.getParentComponent().increaseMaxActiveTab();
            this.getParentComponent().getCharacterTabPane().setEnabledAt(this.getParentComponent().getMaxActiveTab(), true);
            this.enableNextButton();
        }
    }

    /**
     * Get the max points field with the propery weighting values set.
     *
     * @param weighting
     *
     * @return
     */
    @Override
    protected int getWeightingMax(Weighting weighting) {
        return weighting.getAttributeMax();
    }

    /**
     * Adjust the attribute spinners to the clans.
     *
     * @param clan
     */
    public void adjustAttributesToClan(Clan clan) {
        ArrayList<Component> fields = this.getFields("social");

        fields.stream().filter((field) -> (field.getName().equals("appearance"))).map((field) -> (JSpinner) field).forEachOrdered((spinner) -> {
            int value = (int) spinner.getValue(),
                maximum = (int) ((SpinnerNumberModel) spinner.getModel()).getMaximum(),
                minimum = 1;

            if (clan.getKey().equals("nosferatu")) {
                value = 0;
                minimum = 0;
                spinner.setEnabled(false);
            } else {
                if (value == 0) {
                    value = minimum;
                }

                spinner.setEnabled(true);
            }

            spinner.setModel(
                new SpinnerNumberModel(
                    value > maximum ? maximum : value,
                    minimum,
                    maximum,
                    1
                )
            );
        });
    }
}
