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

package antafes.vampireEditor.gui.event.listener;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.gui.event.AddGenerationItemListenerEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Test
public class AdvantagesComboBoxItemListenerTest extends BaseTest
{
    static {
        System.setProperty("java.awt.headless", "true");
    }

    private final List<AddGenerationItemListenerEvent> capturedEvents = new ArrayList<>();
    private BaseTranslatedEntity generation;
    private BaseTranslatedEntity allies;
    private JComboBox<BaseTranslatedEntity> comboBox;
    private JSpinner spinner;

    @BeforeClass
    public void classSetUp()
    {
        VampireEditor.getDispatcher().addListener(
            AddGenerationItemListenerEvent.class,
            new AddGenerationEventListener(capturedEvents::add)
        );
    }

    @BeforeMethod
    @Override
    public void setUp()
    {
        super.setUp();
        this.generation = EmptyEntity.builder()
            .setKey("generation")
            .addName(Configuration.Language.ENGLISH, "Generation")
            .build();
        this.allies = EmptyEntity.builder()
            .setKey("allies")
            .addName(Configuration.Language.ENGLISH, "Allies")
            .build();
        this.spinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        this.comboBox = new JComboBox<>(new BaseTranslatedEntity[] {this.allies, this.generation});
        this.comboBox.addItemListener(new AdvantagesComboBoxItemListener(this.spinner));
        this.capturedEvents.clear();
    }

    public void testSelectingGenerationDispatchesSpinnerValue()
    {
        this.spinner.setValue(3);

        this.comboBox.setSelectedItem(this.generation);

        Assert.assertFalse(this.capturedEvents.isEmpty());
        Assert.assertEquals(this.getLastAdjustment(), 3);

        this.capturedEvents.clear();
        this.spinner.setValue(4);

        Assert.assertFalse(this.capturedEvents.isEmpty());
        Assert.assertEquals(this.getLastAdjustment(), 4);
    }

    public void testDeselectingGenerationDispatchesReset()
    {
        this.spinner.setValue(2);
        this.comboBox.setSelectedItem(this.generation);
        this.capturedEvents.clear();

        this.comboBox.setSelectedItem(this.allies);

        Assert.assertFalse(this.capturedEvents.isEmpty());
        Assert.assertEquals(this.getLastAdjustment(), 0);

        this.capturedEvents.clear();
        this.spinner.setValue(5);

        Assert.assertTrue(this.capturedEvents.isEmpty());
    }

    private int getLastAdjustment()
    {
        return this.capturedEvents.get(this.capturedEvents.size() - 1).getAdjustment();
    }
}
