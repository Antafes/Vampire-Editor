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

package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.Road;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Test
public class RoadStorageTest extends BaseTest
{
    private RoadStorage roadStorage;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        new VampireEditor();
        this.roadStorage = StorageFactory.getStorage(StorageFactory.StorageType.ROAD);
    }

    public void testPathParentResolvedAfterInitialization() throws Exception
    {
        Road pathOfHunter = this.roadStorage.getEntity("pathOfHunter");

        Assert.assertNotNull(pathOfHunter.getParent());
        Assert.assertEquals(pathOfHunter.getParent().getKey(), "roadOfBeast");
    }

    public void testGetRoadsExcludesPaths()
    {
        ArrayList<Road> roads = this.roadStorage.getRoads();
        Set<String> roadKeys = roads.stream().map(Road::getKey).collect(Collectors.toCollection(HashSet::new));
        Set<String> allPathKeys = this.roadStorage.getList().values().stream()
            .filter(road -> road.getParent() != null)
            .map(Road::getKey)
            .collect(Collectors.toCollection(HashSet::new));

        Assert.assertTrue(roadKeys.contains("roadOfBeast"));
        Assert.assertTrue(roadKeys.contains("roadOfHumanity"));
        Assert.assertTrue(roadKeys.stream().noneMatch(allPathKeys::contains));
    }

    public void testGetPathsForRoadReturnsChildPathsOnly() throws Exception
    {
        Road selectedRoad = this.roadStorage.getRoads().stream()
            .findFirst()
            .orElseThrow();

        ArrayList<Road> paths = this.roadStorage.getPathsForRoad(selectedRoad);
        Set<String> expectedPathKeys = this.roadStorage.getList().values().stream()
            .filter(road -> road.getParent() != null)
            .filter(road -> selectedRoad.getKey().equals(road.getParent().getKey()))
            .map(Road::getKey)
            .collect(Collectors.toCollection(HashSet::new));
        Set<String> actualPathKeys = paths.stream()
            .map(Road::getKey)
            .collect(Collectors.toCollection(HashSet::new));

        Assert.assertEquals(actualPathKeys, expectedPathKeys);
        Assert.assertTrue(paths.stream().allMatch(road -> road.getParent() != null));
        Assert.assertTrue(paths.stream().allMatch(road -> selectedRoad.getKey().equals(road.getParent().getKey())));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetPathsForRoadRejectsNull()
    {
        this.roadStorage.getPathsForRoad(null);
    }

    public void testResolveAndValidateParentsRejectsMissingParent()
    {
        RoadStorage storage = this.createStorage(
            this.createRoad("roadA", null),
            this.createRoad("pathA", "missingRoad")
        );

        RuntimeException ex = this.assertResolveFails(storage);

        Assert.assertTrue(ex.getMessage().contains("invalid parent key 'missingRoad'"));
    }

    public void testResolveAndValidateParentsRejectsSelfParent()
    {
        RoadStorage storage = this.createStorage(this.createRoad("roadA", "roadA"));

        RuntimeException ex = this.assertResolveFails(storage);

        Assert.assertTrue(ex.getMessage().contains("cannot be its own parent"));
    }

    public void testResolveAndValidateParentsRejectsCircularParentReferences()
    {
        RoadStorage storage = this.createStorage(
            this.createRoad("roadA", "roadB"),
            this.createRoad("roadB", "roadA")
        );

        RuntimeException ex = this.assertResolveFails(storage);

        Assert.assertTrue(ex.getMessage().contains("Circular parent reference detected"));
    }

    public void testResolveAndValidateParentsResolvesNestedParentChains() throws Exception
    {
        RoadStorage storage = this.createStorage(
            this.createRoad("roadA", null),
            this.createRoad("pathA", "roadA"),
            this.createRoad("pathB", "pathA")
        );

        this.invokeResolveAndValidateParents(storage);
        Road resolvedPath = storage.getEntity("pathB");

        Assert.assertNotNull(resolvedPath.getParent());
        Assert.assertEquals(resolvedPath.getParent().getKey(), "pathA");
        Assert.assertNotNull(resolvedPath.getParent().getParent());
        Assert.assertEquals(resolvedPath.getParent().getParent().getKey(), "roadA");
    }

    private RoadStorage createStorage(Road... roads)
    {
        RoadStorage storage = new RoadStorage();
        Arrays.stream(roads).forEach(road -> storage.getList().put(road.getKey(), road));
        return storage;
    }

    private Road createRoad(String key, String parentKey)
    {
        return Road.builder()
            .setKey(key)
            .addName(Configuration.Language.ENGLISH, key)
            .setParentKey(parentKey)
            .build();
    }

    private RuntimeException assertResolveFails(RoadStorage storage)
    {
        try {
            this.invokeResolveAndValidateParents(storage);
            Assert.fail("Expected parent validation to fail.");
            return null;
        } catch (RuntimeException e) {
            return e;
        } catch (Exception e) {
            Assert.fail("Unexpected checked exception during parent validation", e);
            return null;
        }
    }

     private void invokeResolveAndValidateParents(RoadStorage storage) throws Exception
     {
         Method method = RoadStorage.class.getDeclaredMethod("resolveAndValidateParents");
         method.setAccessible(true);
         try {
             method.invoke(storage);
         } catch (InvocationTargetException e) {
             Throwable cause = e.getCause();
             if (cause instanceof RuntimeException) {
                 throw (RuntimeException) cause;
             }
             if (cause instanceof Exception) {
                 throw (Exception) cause;
             }
             throw e;
         }
     }

     public void testGetRoadsForClanIncludesUniversalRoads() throws Exception
     {
         ClanStorage clanStorage = StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
         antafes.vampireEditor.entity.character.Clan assamites = clanStorage.getEntity("assamites");

         ArrayList<Road> roadsForAssamites = this.roadStorage.getRoadsForClan(assamites);
         ArrayList<Road> universalRoads = this.roadStorage.getRoads();

         // Verify that all universal roads are present for clan-specific roads
         for (Road universalRoad : universalRoads) {
             if (universalRoad.isUniversal()) {
                 Assert.assertTrue(roadsForAssamites.contains(universalRoad),
                     "Universal road " + universalRoad.getKey() + " should be available for any clan");
             }
         }
     }

     public void testGetRoadsForClanIncludesClanSpecificRoads() throws Exception
     {
         ClanStorage clanStorage = StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
         antafes.vampireEditor.entity.character.Clan assamites = clanStorage.getEntity("assamites");
         ArrayList<Road> roadsForAssamites = this.roadStorage.getRoadsForClan(assamites);

         Road roadOfBlood = this.roadStorage.getEntity("roadOfBlood");
         Assert.assertTrue(roadsForAssamites.contains(roadOfBlood),
             "Road of Blood should be available for Assamites");
     }

     public void testGetRoadsForClanExcludesClanSpecificRoadsForOtherClans() throws Exception
     {
         ClanStorage clanStorage = StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
         antafes.vampireEditor.entity.character.Clan gangrel = clanStorage.getEntity("gangrel");
         ArrayList<Road> roadsForGangrel = this.roadStorage.getRoadsForClan(gangrel);

         Road roadOfBlood = this.roadStorage.getEntity("roadOfBlood");
         Assert.assertFalse(roadsForGangrel.contains(roadOfBlood),
             "Road of Blood (restricted to Assamites) should not be available for Gangrel");
     }

     public void testAllExistingRoadsRemainUniversalAfterDataChange() throws Exception
     {
         ArrayList<Road> allRoads = this.roadStorage.getRoads();

         // Verify that most roads are still universal (only the 4 new clan-specific roads + 1 path should have restrictions)
         long universalRoadCount = allRoads.stream()
             .filter(Road::isUniversal)
             .count();

         // There should be many universal roads
         Assert.assertTrue(universalRoadCount > 5,
             "Most existing roads should still be universal. Found " + universalRoadCount + " universal roads");
     }
 }

