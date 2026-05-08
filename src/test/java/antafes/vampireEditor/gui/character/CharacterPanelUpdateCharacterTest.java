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
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.character.AbilityInterface;
import antafes.vampireEditor.entity.character.AdvantageInterface;
import antafes.vampireEditor.entity.character.AttributeInterface;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.util.ArrayList;

@Test
public class CharacterPanelUpdateCharacterTest extends BaseTest
{
    private Character character;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        new VampireEditor();
        this.character = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(this.character);
    }

    public void testAttributesPanelUpdateCharacterUsesAttributeGroupKeys()
    {
        AttributesPanel panel = new AttributesPanel();
        panel.setCharacter(this.character);
        panel.start();

        ArrayList<Component> fields = panel.getFields(AttributeInterface.AttributeType.PHYSICAL.getKeyPlural());
        Assert.assertFalse(fields.isEmpty());

        JSpinner spinner = (JSpinner) fields.getFirst();
        int originalValue = ((Number) spinner.getValue()).intValue();
        int newValue = this.incrementWithinBounds(spinner, originalValue);
        spinner.setValue(newValue);

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);

        Assert.assertEquals(builder.build().getAttributes().get(spinner.getName()).getValue(), newValue);
    }

    public void testAbilitiesPanelUpdateCharacterUsesAbilityGroupKeys()
    {
        AbilitiesPanel panel = new AbilitiesPanel();
        panel.setCharacter(this.character);
        panel.start();

        ArrayList<Component> fields = panel.getFields(AbilityInterface.AbilityType.TALENT.getKeyPlural());
        Assert.assertFalse(fields.isEmpty());

        JSpinner spinner = (JSpinner) fields.getFirst();
        int originalValue = ((Number) spinner.getValue()).intValue();
        int newValue = this.incrementWithinBounds(spinner, originalValue);
        spinner.setValue(newValue);

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);

        Assert.assertEquals(builder.build().getAbilities().get(spinner.getName()).getValue(), newValue);
    }

    public void testAdvantagesPanelUpdateCharacterUsesAdvantageGroupKeys()
    {
        AdvantagesPanel panel = new AdvantagesPanel();
        panel.setCharacter(this.character);
        panel.start();

        ArrayList<Component> fields = panel.getFields(AdvantageInterface.AdvantageType.BACKGROUND.getKeyPlural());
        Assert.assertFalse(fields.isEmpty());

        JSpinner spinner = (JSpinner) fields.getFirst();
        int originalValue = ((Number) spinner.getValue()).intValue();
        int newValue = this.incrementWithinBounds(spinner, originalValue);
        spinner.setValue(newValue);

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);

        Assert.assertEquals(builder.build().getAdvantages().get(spinner.getName()).getValue(), newValue);
    }

    private int incrementWithinBounds(JSpinner spinner, int currentValue)
    {
        int maximum = ((Number) ((SpinnerNumberModel) spinner.getModel()).getMaximum()).intValue();
        if (currentValue < maximum) {
            return currentValue + 1;
        }

        return currentValue > 1 ? currentValue - 1 : currentValue;
    }
}


