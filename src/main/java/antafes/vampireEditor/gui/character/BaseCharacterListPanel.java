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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.gui.BaseListPanel;
import antafes.vampireEditor.gui.ComponentDocumentListener;
import antafes.vampireEditor.gui.TranslatableComponent;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

abstract public class BaseCharacterListPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface
{
    @Getter
    @Setter
    private antafes.vampireEditor.entity.Character character = null;

    protected void addChangeListenerForCharacterChanged(JComponent component)
    {
        if (component instanceof JTextComponent) {
            ((JTextComponent) component).getDocument().addDocumentListener(this.createDocumentListener(component));
        }

        if (component instanceof JSpinner) {
            ((JSpinner) component).addChangeListener(this.createChangeListener(component));
        }
    }

    private ComponentDocumentListener createDocumentListener(JComponent component)
    {
        CharacterComponentDocumentListener listener = new CharacterComponentDocumentListener();
        listener.setCharacter(this.character);
        listener.setComponent(component);

        return listener;
    }

    private ChangeListener createChangeListener(JComponent component)
    {
        CharacterComponentChangeListener listener = new CharacterComponentChangeListener();
        listener.setCharacter(this.character);
        listener.setComponent(component);

        return listener;
    }

    @Override
    protected void addChangeListener(JSpinner field)
    {
        super.addChangeListener(field);
        this.addChangeListenerForCharacterChanged(field);
    }
}
