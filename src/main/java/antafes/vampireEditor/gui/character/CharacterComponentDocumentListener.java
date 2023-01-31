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

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.gui.ComponentDocumentListener;
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CharacterComponentDocumentListener extends ComponentDocumentListener
{
    @Setter
    private Character character;

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        this.changed();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        this.changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        this.changed();
    }

    private void changed()
    {
        String componentValue = null;
        if (this.getComponent() instanceof JTextComponent) {
            componentValue = ((JTextComponent) this.getComponent()).getText();
        } else if (this.getComponent() instanceof JComboBox) {
            BaseTranslatedEntity entity = (BaseTranslatedEntity) ((JComboBox<BaseTranslatedEntity>) this.getComponent()).getSelectedItem();
            componentValue = entity.getKey();
        }

        if (componentValue == null) {
            return;
        }

        String methodName = "get" + StringUtils.capitalize(this.getComponent().getName());
        Method method;
        try {
            method = character.getClass().getMethod(methodName);
            Object value = method.invoke(character);

            if (value instanceof String) {
                CharacterChangedEvent event = new CharacterChangedEvent();
                event.setChanged(((String) value).compareTo(componentValue) != 0);

                VampireEditor.getDispatcher().dispatch(event);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
    }
}
