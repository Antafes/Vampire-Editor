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

package antafes.vampireEditor.gui.event.listener;

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.gui.event.AddGenerationItemListenerEvent;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class AdvantagesComboBoxItemListener implements ItemListener
{
    private static final String GENERATION_CHANGE_LISTENER = "generationChangeListener";
    private final JSpinner spinner;

    public AdvantagesComboBoxItemListener(JSpinner spinner)
    {
        this.spinner = spinner;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        JComboBox<BaseTranslatedEntity> element = (JComboBox<BaseTranslatedEntity>) e.getSource();
        ChangeListener generationChangeListener = (ChangeListener) this.spinner.getClientProperty(GENERATION_CHANGE_LISTENER);

        if (generationChangeListener == null) {
            generationChangeListener = changeEvent -> VampireEditor.getDispatcher().dispatch(
                new AddGenerationItemListenerEvent((int) this.spinner.getValue())
            );
            this.spinner.putClientProperty(GENERATION_CHANGE_LISTENER, generationChangeListener);
        }

        if (this.isGenerationSelected(element)) {
            if (!Arrays.asList(this.spinner.getChangeListeners()).contains(generationChangeListener)) {
                this.spinner.addChangeListener(generationChangeListener);
            }
        } else {
            this.spinner.removeChangeListener(generationChangeListener);
        }

        if (this.isGenerationSelected(element)) {
            VampireEditor.getDispatcher()
                .dispatch(new AddGenerationItemListenerEvent((int) this.spinner.getValue())
        );
        }
    }

    private boolean isGenerationSelected(JComboBox<BaseTranslatedEntity> element)
    {
        Object selectedItem = element.getSelectedItem();

        return selectedItem instanceof BaseTranslatedEntity
            && "generation".equals(((BaseTranslatedEntity) selectedItem).getKey());
    }
}
