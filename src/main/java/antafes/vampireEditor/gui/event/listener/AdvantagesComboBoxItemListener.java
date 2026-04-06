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
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.event.AddGenerationItemListenerEvent;
import antafes.vampireEditor.gui.newCharacter.AdvantagesPanel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AdvantagesComboBoxItemListener implements ItemListener
{
    private JSpinner spinner;
    private AdvantagesPanel panel;

    public AdvantagesComboBoxItemListener(JSpinner spinner)
    {
        this.spinner = spinner;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        JComboBox<BaseTranslatedEntity> element = (JComboBox<BaseTranslatedEntity>) e.getSource();
        AdvantageStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);

        try {
            if (storage.getEntity("generation").equals(element.getSelectedItem())) {
                VampireEditor.getDispatcher().dispatch(new AddGenerationItemListenerEvent(spinner));
            } else {
                // Remove the generation bonus
                if (spinner.getChangeListeners().length > 1) {
                    for (ChangeListener listener : spinner.getChangeListeners()) {
                        if (listener.toString().contains(AdvantagesPanel.class.getName())) {
                            spinner.removeChangeListener(listener);
                            break;
                        }
                    }
//                    ((LooksPanel) this.panel.getParentComponent().getCharacterTabPane().getComponentAt(0))
//                        .adjustGeneration(0);
                }
            }
        } catch (EntityStorageException ex) {
            VampireEditor.log(ex.getMessage());
        }
    }
}
