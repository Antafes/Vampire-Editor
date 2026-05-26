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

package antafes.vampireEditor.gui.newCharacter;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.BaseTranslatedEntity;
import antafes.vampireEditor.entity.EmptyEntity;
import antafes.vampireEditor.entity.character.Advantage;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.RoadStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.BaseColumnListPanel;
import antafes.vampireEditor.gui.NewCharacterDialog;
import antafes.vampireEditor.gui.event.PathSelectedEvent;
import antafes.vampireEditor.gui.event.RoadSelectedEvent;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Test
public class RoadPathSelectionTest extends BaseTest
{
    private NewCharacterDialog dialog;
    private LooksPanel looksPanel;
    private AdvantagesPanel advantagesPanel;
    private Road roadOfBeast;
    private Road roadOfHumanity;
    private Road pathOfHunter;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        new VampireEditor();

        if (GraphicsEnvironment.isHeadless()) {
            throw new SkipException("Road/path UI integration tests require a non-headless AWT environment.");
        }

        try {
            this.dialog = new NewCharacterDialog(null, false, false);
            this.looksPanel = this.getField(this.dialog, "looksPanel", LooksPanel.class);
            this.advantagesPanel = this.getField(this.dialog, "advantagesPanel", AdvantagesPanel.class);

            RoadStorage roadStorage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
            this.roadOfBeast = roadStorage.getEntity("roadOfBeast");
            this.roadOfHumanity = roadStorage.getEntity("roadOfHumanity");
            this.pathOfHunter = roadStorage.getEntity("pathOfHunter");
        } catch (Exception e) {
            Assert.fail("Could not initialize road/path UI integration test", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        if (this.dialog != null) {
            this.dialog.dispose();
        }
    }

    public void testPathSelectorIsDisabledUntilRoadIsSelected() throws Exception
    {
        JComboBox<BaseTranslatedEntity> pathComboBox = this.getField(this.looksPanel, "pathComboBox", JComboBox.class);

        Assert.assertFalse(pathComboBox.isEnabled());
        Assert.assertEquals(pathComboBox.getItemCount(), 0);
    }

    public void testSelectingRoadLoadsOnlyChildPathsAndChangingRoadClearsSelection() throws Exception
    {
        JComboBox<BaseTranslatedEntity> roadComboBox = this.getField(this.looksPanel, "roadComboBox", JComboBox.class);
        JComboBox<BaseTranslatedEntity> pathComboBox = this.getField(this.looksPanel, "pathComboBox", JComboBox.class);

        roadComboBox.setSelectedItem(this.roadOfBeast);

        Set<String> expectedBeastPathKeys = this.getExpectedPathKeysForRoad(this.roadOfBeast);
        Set<String> actualBeastPathKeys = this.getComboBoxNonEmptyKeys(pathComboBox);
        Assert.assertEquals(actualBeastPathKeys, expectedBeastPathKeys);
        Assert.assertEquals(pathComboBox.isEnabled(), !expectedBeastPathKeys.isEmpty());
        Assert.assertTrue(actualBeastPathKeys.contains("pathOfHunter"));

        pathComboBox.setSelectedItem(this.pathOfHunter);
        Assert.assertNotNull(pathComboBox.getSelectedItem());
        Assert.assertEquals(((Road) pathComboBox.getSelectedItem()).getKey(), "pathOfHunter");

        roadComboBox.setSelectedItem(this.roadOfHumanity);

        Set<String> expectedHumanityPathKeys = this.getExpectedPathKeysForRoad(this.roadOfHumanity);
        Set<String> actualHumanityPathKeys = this.getComboBoxNonEmptyKeys(pathComboBox);
        Assert.assertEquals(actualHumanityPathKeys, expectedHumanityPathKeys);
        Assert.assertEquals(pathComboBox.isEnabled(), !expectedHumanityPathKeys.isEmpty());
        Assert.assertTrue(pathComboBox.getSelectedItem() instanceof EmptyEntity);
    }

    public void testPathSelectionOverridesRoadVirtuesAndClearingPathRestoresRoadVirtues() throws Exception
    {
        AdvantageStorage advantageStorage = StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        Road road = this.createRoad(
            "testRoad",
            advantageStorage.getEntity("conscience"),
            advantageStorage.getEntity("self-control")
        );
        Road path = this.createRoad(
            "testPath",
            advantageStorage.getEntity("conviction"),
            advantageStorage.getEntity("instinct")
        );

        this.dialog.getDialogDispatcher().dispatch(new RoadSelectedEvent(road));
        Set<String> roadVirtues = this.getVirtueSpinnerLabels();

        Assert.assertTrue(roadVirtues.contains(advantageStorage.getEntity("conscience").getName()));
        Assert.assertTrue(roadVirtues.contains(advantageStorage.getEntity("self-control").getName()));
        Assert.assertTrue(roadVirtues.contains(advantageStorage.getEntity("courage").getName()));
        Assert.assertFalse(roadVirtues.contains(advantageStorage.getEntity("conviction").getName()));
        Assert.assertFalse(roadVirtues.contains(advantageStorage.getEntity("instinct").getName()));

        this.dialog.getDialogDispatcher().dispatch(new PathSelectedEvent(path));
        Set<String> pathVirtues = this.getVirtueSpinnerLabels();

        Assert.assertTrue(pathVirtues.contains(advantageStorage.getEntity("conviction").getName()));
        Assert.assertTrue(pathVirtues.contains(advantageStorage.getEntity("instinct").getName()));
        Assert.assertTrue(pathVirtues.contains(advantageStorage.getEntity("courage").getName()));
        Assert.assertFalse(pathVirtues.contains(advantageStorage.getEntity("conscience").getName()));
        Assert.assertFalse(pathVirtues.contains(advantageStorage.getEntity("self-control").getName()));

        this.dialog.getDialogDispatcher().dispatch(new PathSelectedEvent(null));
        Set<String> restoredRoadVirtues = this.getVirtueSpinnerLabels();

        Assert.assertEquals(restoredRoadVirtues, roadVirtues);
    }

    private Road createRoad(String key, Advantage... virtues)
    {
        return Road.builder()
            .setKey(key)
            .addName(Configuration.Language.ENGLISH, key)
            .setMerits(List.of(virtues))
            .build();
    }

    private List<String> getComboBoxKeys(JComboBox<BaseTranslatedEntity> comboBox)
    {
        return IntStream.range(0, comboBox.getItemCount())
            .mapToObj(comboBox::getItemAt)
            .map(item -> item instanceof EmptyEntity ? "" : item.getKey())
            .collect(Collectors.toList());
    }

    private Set<String> getVirtueSpinnerLabels() throws Exception
    {
        Map<String, Map<String, JSpinner>> groupSpinners = this.getField(this.advantagesPanel, BaseColumnListPanel.class, "groupSpinners", Map.class);
        Map<String, JSpinner> virtueSpinners = groupSpinners.get("virtues");
        return virtueSpinners.keySet();
    }

    private Set<String> getExpectedPathKeysForRoad(Road road)
    {
        RoadStorage roadStorage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
        return roadStorage.getPathsForRoad(road).stream()
            .map(Road::getKey)
            .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<String> getComboBoxNonEmptyKeys(JComboBox<BaseTranslatedEntity> comboBox)
    {
        return this.getComboBoxKeys(comboBox).stream()
            .filter(key -> !key.isEmpty())
            .collect(Collectors.toCollection(HashSet::new));
    }

    private <T> T getField(Object target, String fieldName, Class<T> type) throws Exception
    {
        return this.getField(target, target.getClass(), fieldName, type);
    }

    private <T> T getField(Object target, Class<?> owner, String fieldName, Class<T> type) throws Exception
    {
        Field field = owner.getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(target));
    }
}



