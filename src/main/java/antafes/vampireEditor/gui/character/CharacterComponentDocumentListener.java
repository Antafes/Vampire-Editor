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
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.NatureStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import antafes.vampireEditor.gui.event.listener.ComponentDocumentListener;
import antafes.vampireEditor.utility.NatureResolutionUtility;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

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
            if (entity == null) {
                return;
            }

            componentValue = entity.getKey();
        }

        if (componentValue == null) {
            return;
        }

        componentValue = this.normalizeComparedValue(componentValue);

        String methodName = "get" + StringUtils.capitalize(this.getComponent().getName());
        Method method;
        try {
            method = character.getClass().getMethod(methodName);
            Object value = method.invoke(character);

            switch (value) {
                case String s -> {
                    String normalizedValue = this.normalizeComparedValue(s);
                    CharacterChangedEvent event = new CharacterChangedEvent();
                    event.setChanged(!Objects.equals(normalizedValue, componentValue));

                    VampireEditor.getDispatcher().dispatch(event);
                }
                case BaseTranslatedEntity translatedEntity -> {
                    String normalizedValue = this.normalizeTranslatedEntityValue(translatedEntity);
                    String normalizedComponentValue = this.normalizeTranslatedEntityInput(componentValue);
                    CharacterChangedEvent event = new CharacterChangedEvent();
                    event.setChanged(!Objects.equals(normalizedValue, normalizedComponentValue));

                    VampireEditor.getDispatcher().dispatch(event);
                }
                case null -> {
                    CharacterChangedEvent event = new CharacterChangedEvent();
                    event.setChanged(componentValue != null);

                    VampireEditor.getDispatcher().dispatch(event);
                }
                default -> {
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
    }

    private String normalizeComparedValue(String value)
    {
        if (this.isOptionalNormalizedField()) {
            return NatureResolutionUtility.normalizeOptionalText(value);
        }

        return value;
    }

    private String normalizeTranslatedEntityInput(String componentValue)
    {
        if ("nature".equals(this.getComponent().getName())) {
            return this.resolveNatureKeyFromText(componentValue);
        }

        return this.normalizeComparedValue(componentValue);
    }

    private String normalizeTranslatedEntityValue(BaseTranslatedEntity entity)
    {
        if ("nature".equals(this.getComponent().getName())) {
            return entity.getKey();
        }

        return this.normalizeComparedValue(entity.toString());
    }

    private boolean isOptionalNormalizedField()
    {
        String componentName = this.getComponent().getName();

        return "nature".equals(componentName)
            || "demeanor".equals(componentName)
            || "concept".equals(componentName);
    }

    private String resolveNatureKeyFromText(String inputText)
    {
        NatureStorage natureStorage = StorageFactory.getStorage(StorageFactory.StorageType.NATURE);
        try {
            return NatureResolutionUtility.resolveNatureKey(natureStorage, inputText);
        } catch (EntityStorageException e) {
            throw new RuntimeException(e);
        }
    }
}
