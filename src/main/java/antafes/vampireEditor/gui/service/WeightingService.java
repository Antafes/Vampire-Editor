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
 * @copyright (c) 2023, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.service;

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.gui.event.CalculateUsedFreeAdditionalPointsEvent;
import antafes.vampireEditor.gui.event.UpdateMaxFreeAdditionalPointsEvent;
import antafes.vampireEditor.gui.utility.Weighting;
import lombok.NonNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class WeightingService
{
    private final HashMap<String, JComboBox<Weighting>> weightingElements;

    public WeightingService()
    {
        this.weightingElements = new HashMap<>();
    }

    public @NonNull JComboBox<Weighting> createWeighting(String headline, String parent) {
        JComboBox<Weighting> weightingElement = new JComboBox<>();
        weightingElement.setModel(new DefaultComboBoxModel<>(Weighting.values()));
        weightingElement.setSelectedIndex(0);

        if (!this.weightingElements.isEmpty()) {
            if (this.weightingElements.size() == 1) {
                weightingElement.setSelectedIndex(1);
            } else {
                weightingElement.setSelectedIndex(2);
            }
        }

        weightingElement.addActionListener((ActionEvent e) -> {
            JComboBox<Weighting> element = (JComboBox<Weighting>) e.getSource();
            JComboBox<Weighting> second, third;
            ArrayList<JComboBox<Weighting>> elements = new ArrayList<>(this.weightingElements.values());
            elements.remove(element);
            second = elements.get(0);
            third = elements.get(1);
            this.switchWeightings(
                element,
                second,
                third
            );
            Weighting weighting = (Weighting) element.getSelectedItem();
            VampireEditor.getDispatcher()
                .dispatch(new UpdateMaxFreeAdditionalPointsEvent(weighting, headline, parent));
            VampireEditor.getDispatcher()
                .dispatch(new CalculateUsedFreeAdditionalPointsEvent(headline, parent));
        });
        this.weightingElements.put(headline, weightingElement);

        return weightingElement;
    }

    /**
     * Switch the selection for the combo box with the same value to the
     * remaining value.
     * The first combo box will be treated as the one the change was made on.
     *
     * @param first The first weighting combo box
     * @param second The second weighting combo box
     * @param third The third weighting combo box
     */
    protected void switchWeightings(JComboBox<Weighting> first, JComboBox<Weighting> second, JComboBox<Weighting> third) {
        Weighting firstSelection = (Weighting) first.getSelectedItem();
        Weighting secondSelection = (Weighting) second.getSelectedItem();
        Weighting thirdSelection = (Weighting) third.getSelectedItem();

        if (firstSelection == null) {
            return;
        }

        if (firstSelection.equals(secondSelection)) {
            second.setSelectedItem(Weighting.getRemaining(firstSelection, thirdSelection));
        } else if (firstSelection.equals(thirdSelection)) {
            third.setSelectedItem(Weighting.getRemaining(firstSelection, secondSelection));
        }
    }
}
