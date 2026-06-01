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
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.Clan;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Test
public class ClanStorageTest extends BaseTest
{
    private ClanStorage clanStorage;

    @BeforeMethod
    public void setUp()
    {
        super.setUp();
        this.configuration.loadProperties();
        StorageFactory.storageWarmUp();
        this.clanStorage = StorageFactory.getStorage(StorageFactory.StorageType.CLAN);
    }

    public void testClanStorageContainsBloodlineAfterPhase2()
    {
        ArrayList<Clan> bloodlines = new ArrayList<>();

        this.clanStorage.getList().values().forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            }
        });

        Assert.assertFalse(bloodlines.isEmpty(), "Storage should contain at least one bloodline");
        Assert.assertTrue(bloodlines.size() >= 1, "There should be at least one bloodline in storage");
    }

    public void testClanStorageMainClansAreNotBloodlines() throws Exception
    {
        Map<String, Boolean> expectedBloodlineFlags = this.getClanBloodlineFlagsFromXml();
        long expectedMainClanCount = expectedBloodlineFlags.values().stream().filter(isBloodline -> !isBloodline).count();
        ArrayList<Clan> actualMainClans = new ArrayList<>();

        this.clanStorage.getList().forEach((key, clan) -> {
            Boolean expectedBloodlineFlag = expectedBloodlineFlags.get(key);
            Assert.assertNotNull(expectedBloodlineFlag,
                "Clan '" + key + "' should be defined in clans.xml");

            if (!expectedBloodlineFlag) {
                Assert.assertFalse(clan.isBloodline(),
                    "Main clan '" + key + "' should not be marked as bloodline in storage");
                actualMainClans.add(clan);
            }
        });

        Assert.assertEquals(actualMainClans.size(), (int) expectedMainClanCount,
            "Storage should contain the same number of main clans as defined in clans.xml");
    }

    public void testClanStorageBloodlineCanBeIdentified()
    {
        ArrayList<Clan> bloodlines = new ArrayList<>();

        this.clanStorage.getList().values().forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            }
        });

        boolean ahrimanesFound = bloodlines.stream()
            .anyMatch(clan -> "ahrimanes".equals(clan.getKey()));

        Assert.assertTrue(ahrimanesFound, "Ahrimanes bloodline should be identifiable from storage");
    }

    public void testClanStorageContainsCorrectNumberOfClans() throws Exception
    {
        Set<String> expectedKeys = this.getClanKeysFromXml();
        Set<String> actualKeys = new HashSet<>(this.clanStorage.getList().keySet());

        Assert.assertEquals(actualKeys.size(), expectedKeys.size(),
            "ClanStorage should contain the same number of clans as defined in clans.xml");
        Assert.assertEquals(actualKeys, expectedKeys,
            "ClanStorage keys should exactly match the clan keys defined in clans.xml");
    }

    private Map<String, Boolean> getClanBloodlineFlagsFromXml() throws Exception
    {
        try (InputStream inputStream = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "clans.xml")) {
            Assert.assertNotNull(inputStream, "clans.xml resource should be available for tests");

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            NodeList clanNodes = document.getElementsByTagName("clan");
            Map<String, Boolean> bloodlineFlags = new HashMap<>();

            for (int i = 0; i < clanNodes.getLength(); i++) {
                Element clanElement = (Element) clanNodes.item(i);
                String key = clanElement.getAttribute("key");
                String bloodlineValue = clanElement.getElementsByTagName("bloodline").item(0).getTextContent();
                bloodlineFlags.put(key, Boolean.parseBoolean(bloodlineValue.trim()));
            }

            return bloodlineFlags;
        }
    }

    private Set<String> getClanKeysFromXml() throws Exception
    {
        return new HashSet<>(this.getClanBloodlineFlagsFromXml().keySet());
    }
}
