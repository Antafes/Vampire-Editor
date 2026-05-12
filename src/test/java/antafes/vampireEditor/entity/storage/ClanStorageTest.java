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
import antafes.vampireEditor.entity.character.Clan;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Test
public class ClanStorageTest extends BaseTest
{
    private ClanStorage clanStorage;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        Configuration.getInstance().loadProperties();
        StorageFactory.storageWarmUp();
        this.clanStorage = (ClanStorage) StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
    }

    public void testClanStorageContainsBloodlineAfterPhase2()
    {
        // After Phase 2, at least one bloodline (Ahrimanes) should be present
        ArrayList<Clan> bloodlines = new ArrayList<>();

        this.clanStorage.getList().values().forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            }
        });

        Assert.assertFalse(bloodlines.isEmpty(), "Storage should contain at least one bloodline");
        Assert.assertTrue(bloodlines.size() >= 1, "There should be at least one bloodline in storage");
    }

    public void testClanStorageMainClansAreNotBloodlines()
    {
        // All original/main clans should have isBloodline() == false
        ArrayList<Clan> mainClans = new ArrayList<>();
        ArrayList<Clan> bloodlineClans = new ArrayList<>();

        this.clanStorage.getList().values().forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlineClans.add(clan);
            } else {
                mainClans.add(clan);
            }
        });

        // All main clans should return false for isBloodline()
        mainClans.forEach(clan -> {
            Assert.assertFalse(clan.isBloodline(),
                "Main clan '" + clan.getKey() + "' should not be a bloodline");
        });

        // There should be more main clans than bloodlines
        Assert.assertTrue(mainClans.size() >= bloodlineClans.size(),
            "There should be more or equal main clans compared to bloodlines");
    }

    public void testClanStorageBloodlineCanBeIdentified()
    {
        // There should be a way to identify bloodlines from the storage
        ArrayList<Clan> bloodlines = new ArrayList<>();

        this.clanStorage.getList().values().forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            }
        });

        // Verify we can find Ahrimanes (the example bloodline from Phase 2)
        boolean ahrimanesFound = bloodlines.stream()
            .anyMatch(clan -> "ahrimanes".equals(clan.getKey()));

        Assert.assertTrue(ahrimanesFound, "Ahrimanes bloodline should be identifiable from storage");
    }

    public void testClanStorageContainsCorrectNumberOfClans()
    {
        // Total should be 13 main clans + at least 1 bloodline/example
        ArrayList<Clan> allClans = new ArrayList<>(this.clanStorage.getList().values());

        Assert.assertTrue(allClans.size() == 14,
            "Storage should contain exactly 13 main clans + 1 bloodline");
    }
}






