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
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import antafes.vampireEditor.gui.event.listener.CharacterChangedListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.util.ArrayList;

@Test
public class CharacterPanelUpdateCharacterTest extends BaseTest
{
    private Character character;
    private ArrayList<CharacterChangedEvent> capturedCharacterChangedEvents;
    private boolean characterChangedListenerRegistered;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        new VampireEditor();
        this.character = TestCharacterUtility.createTestCharacter();
        Assert.assertNotNull(this.character);

        if (this.capturedCharacterChangedEvents == null) {
            this.capturedCharacterChangedEvents = new ArrayList<>();
        }

        if (!this.characterChangedListenerRegistered) {
            VampireEditor.getDispatcher().addListener(
                CharacterChangedEvent.class,
                new CharacterChangedListener(event -> this.capturedCharacterChangedEvents.add(event))
            );
            this.characterChangedListenerRegistered = true;
        }

        this.capturedCharacterChangedEvents.clear();
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

    public void testGeneralPanelUpdateCharacterPersistsDemeanorConceptAndNature()
    {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();

        this.findBaseTextField(panel, "demeanor").setText("new demeanor");
        this.findBaseTextField(panel, "concept").setText("new concept");
        this.findBaseTextField(panel, "nature").setText("Architect");

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);
        Character updated = builder.build();

        Assert.assertEquals(updated.getDemeanor(), "new demeanor");
        Assert.assertEquals(updated.getConcept(), "new concept");
        Assert.assertNotNull(updated.getNature());
        Assert.assertEquals(updated.getNature().getKey(), "architect");
        Assert.assertEquals(updated.getNature().getName(), "Architect");
        Assert.assertFalse(updated.getNature().isManual());
    }

    public void testGeneralPanelUpdateCharacterTrimsDemeanorAndConcept()
    {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();

        this.findBaseTextField(panel, "demeanor").setText("  new demeanor  ");
        this.findBaseTextField(panel, "concept").setText("  new concept  ");

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);
        Character updated = builder.build();

        Assert.assertEquals(updated.getDemeanor(), "new demeanor");
        Assert.assertEquals(updated.getConcept(), "new concept");
    }

    public void testGeneralPanelUpdateCharacterClearsNatureWhenEmpty()
    {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();

        this.findBaseTextField(panel, "nature").setText(" ");

        Character.CharacterBuilder<?, ?> builder = this.character.toBuilder();
        panel.updateCharacter(builder);

        Assert.assertNull(builder.build().getNature());
    }

    public void testGeneralPanelUpdateCharacterStoresNullForBlankDemeanorAndConcept()
    {
        GeneralPanel panel = new GeneralPanel();
        Character testCharacterWithoutOptionalValues = this.character.toBuilder()
            .setDemeanor(null)
            .setConcept(null)
            .build();
        panel.setCharacter(testCharacterWithoutOptionalValues);
        panel.start();

        this.findBaseTextField(panel, "demeanor").setText("   ");
        this.findBaseTextField(panel, "concept").setText(" ");

        Character.CharacterBuilder<?, ?> builder = testCharacterWithoutOptionalValues.toBuilder();
        panel.updateCharacter(builder);
        Character updated = builder.build();

        Assert.assertNull(updated.getDemeanor());
        Assert.assertNull(updated.getConcept());
    }

    public void testGeneralPanelNatureFieldDispatchesCharacterChangedEvent()
    {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();

        this.findBaseTextField(panel, "nature").setText("Architect");

        Assert.assertFalse(this.capturedCharacterChangedEvents.isEmpty());
        CharacterChangedEvent event = this.capturedCharacterChangedEvents.getLast();
        Assert.assertTrue(event.isChanged());
    }

    public void testGeneralPanelNullFieldTransitionDispatchesCharacterChangedEvent()
    {
        GeneralPanel panel = new GeneralPanel();
        Character testCharacterNullNature = this.character.toBuilder()
            .setNature(null)
            .build();
        panel.setCharacter(testCharacterNullNature);
        panel.start();

        this.findBaseTextField(panel, "nature").setText("Architect");

        Assert.assertFalse(this.capturedCharacterChangedEvents.isEmpty());
        CharacterChangedEvent event = this.capturedCharacterChangedEvents.getLast();
        Assert.assertTrue(event.isChanged());
    }

    public void testGeneralPanelWhitespaceOnlyOptionalFieldsDoNotReportChanged()
    {
        GeneralPanel panel = new GeneralPanel();
        Character testCharacterWithoutOptionalValues = this.character.toBuilder()
            .setDemeanor(null)
            .setConcept(null)
            .setNature(null)
            .build();
        panel.setCharacter(testCharacterWithoutOptionalValues);
        panel.start();
        this.capturedCharacterChangedEvents.clear();

        this.findBaseTextField(panel, "demeanor").setText("   ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.isEmpty());
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());

        this.findBaseTextField(panel, "concept").setText(" ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());

        this.findBaseTextField(panel, "nature").setText("  ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());
    }

    public void testGeneralPanelTrimEquivalentOptionalFieldsDoNotReportChanged()
    {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.capturedCharacterChangedEvents.clear();
        String currentNatureText = this.findBaseTextField(panel, "nature").getText();

        this.findBaseTextField(panel, "demeanor").setText("  strict  ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.isEmpty());
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());

        this.findBaseTextField(panel, "concept").setText("  Really no concept!  ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());

        this.findBaseTextField(panel, "nature").setText("  " + currentNatureText + "  ");
        Assert.assertFalse(this.capturedCharacterChangedEvents.getLast().isChanged());
    }

    private int incrementWithinBounds(JSpinner spinner, int currentValue)
    {
        int maximum = ((Number) ((SpinnerNumberModel) spinner.getModel()).getMaximum()).intValue();
        if (currentValue < maximum) {
            return currentValue + 1;
        }

        return currentValue > 1 ? currentValue - 1 : currentValue;
    }

    private JTextField findBaseTextField(GeneralPanel panel, String fieldName)
    {
        ArrayList<Component> fields = panel.getFields("base");
        for (Component field : fields) {
            if (field instanceof JTextField && fieldName.equals(field.getName())) {
                return (JTextField) field;
            }
        }

        throw new IllegalStateException("Could not find base text field: " + fieldName);
    }
}


