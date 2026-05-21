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

package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.gui.BasePanel;
import antafes.vampireEditor.gui.event.listener.ComponentDocumentListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

abstract public class BaseCharacterPanel extends BasePanel implements TranslatableComponent, antafes.vampireEditor.gui.character.CharacterPanelInterface
{
    @Getter
    @Setter
    private antafes.vampireEditor.entity.Character character = null;

    private final ArrayList<UndoManager> undoManagers = new ArrayList<>();

    protected void addChangeListenerForCharacterChanged(JComponent component)
    {
        if (component instanceof JTextComponent textComponent) {
            textComponent.getDocument().addDocumentListener(this.createDocumentListener(component));

            if (textComponent.isEditable()) {
                this.installUndoSupport(textComponent);
            }
        }

        if (component instanceof JSpinner) {
            ((JSpinner) component).addChangeListener(this.createChangeListener(component));
        }
    }

    /**
     * Discard all recorded undo/redo history for every tracked text field.
     * Call this after filling fields with initial character data so the user
     * cannot undo past the loaded state.
     */
    protected void clearUndoHistory()
    {
        this.undoManagers.forEach(UndoManager::discardAllEdits);
    }

    private void installUndoSupport(JTextComponent textComponent)
    {
        UndoManager undoManager = new UndoManager();
        this.undoManagers.add(undoManager);
        textComponent.getDocument().addUndoableEditListener(undoManager);

        textComponent.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo"
        );
        textComponent.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo"
        );

        textComponent.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });
        textComponent.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }

    private ComponentDocumentListener createDocumentListener(JComponent component)
    {
        antafes.vampireEditor.gui.character.CharacterComponentDocumentListener listener = new antafes.vampireEditor.gui.character.CharacterComponentDocumentListener();
        listener.setCharacter(this.character);
        listener.setComponent(component);

        return listener;
    }

    private ChangeListener createChangeListener(JComponent component)
    {
        antafes.vampireEditor.gui.character.CharacterComponentChangeListener listener = new antafes.vampireEditor.gui.character.CharacterComponentChangeListener();
        listener.setCharacter(this.character);
        listener.setComponent(component);

        return listener;
    }
}
