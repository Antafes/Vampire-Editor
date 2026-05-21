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
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.ValuedEntityInterface;
import antafes.vampireEditor.entity.character.Generation;
import antafes.vampireEditor.entity.character.SpecialFeature;
import antafes.vampireEditor.gui.event.listener.ComponentChangeListener;
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CharacterComponentChangeListener extends ComponentChangeListener
{
    @Setter
    private Character character;

    @Override
    public void stateChanged(ChangeEvent e)
    {
        Character currentCharacter = this.resolveCharacter();
        if (currentCharacter == null) {
            return;
        }

        int componentValue = (int) ((JSpinner) this.getComponent()).getValue();
        String componentName = this.getComponent().getName();
        String methodName = "get" + StringUtils.capitalize(componentName);
        Method method;
        Object returnValue;
        int value;

        try {
            if (currentCharacter.isAttribute(componentName)) {
                returnValue = currentCharacter.getAttributes().get(componentName).getValue();
            } else if (currentCharacter.isAbility(componentName)) {
                returnValue = currentCharacter.getAbilities().get(componentName).getValue();
            } else if (currentCharacter.isAdvantage(componentName)) {
                returnValue = currentCharacter.getAdvantages().get(componentName).getValue();
            } else {
                method = currentCharacter.getClass().getMethod(methodName);
                returnValue = method.invoke(currentCharacter);
            }

            value = switch (returnValue) {
                case Generation generation -> generation.getGeneration();
                case ValuedEntityInterface valuedEntityInterface -> valuedEntityInterface.getValue();
                case SpecialFeature specialFeature -> specialFeature.getCost();
                case null -> 0;
                default -> (int) returnValue;
            };

            CharacterChangedEvent event = new CharacterChangedEvent();
            event.setCharacter(currentCharacter);
            event.setComponentIdentifier(this.resolveComponentIdentifier());
            event.setChanged(value != componentValue);

            VampireEditor.getDispatcher().dispatch(event);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
    }

    private String resolveComponentIdentifier()
    {
        String componentName = this.getComponent().getName();

        if (StringUtils.isNotBlank(componentName)) {
            return componentName;
        }

        return this.getComponent().getClass().getName() + "@" + System.identityHashCode(this.getComponent());
    }

    private Character resolveCharacter()
    {
        Container panel = SwingUtilities.getAncestorOfClass(BaseCharacterPanel.class, this.getComponent());

        if (panel instanceof BaseCharacterPanel baseCharacterPanel && baseCharacterPanel.getCharacter() != null) {
            return baseCharacterPanel.getCharacter();
        }

        return this.character;
    }
}
