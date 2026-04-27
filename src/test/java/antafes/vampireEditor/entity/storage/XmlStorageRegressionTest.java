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
import antafes.vampireEditor.TestCharacterUtility;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

@Test
public class XmlStorageRegressionTest extends BaseTest {
    private Configuration configuration;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        new VampireEditor();
        this.configuration = Configuration.getInstance();
        this.configuration.loadProperties();
    }

    public void testAllStaticStoragesLoadData() {
        StorageFactory.StorageType[] dataStorages = new StorageFactory.StorageType[] {
            StorageFactory.StorageType.ABILITY,
            StorageFactory.StorageType.ADVANTAGE,
            StorageFactory.StorageType.ATTRIBUTE,
            StorageFactory.StorageType.WEAKNESS,
            StorageFactory.StorageType.CLAN,
            StorageFactory.StorageType.MERIT,
            StorageFactory.StorageType.FLAW,
            StorageFactory.StorageType.GENERATION,
            StorageFactory.StorageType.ROAD,
            StorageFactory.StorageType.NATURE
        };

        for (StorageFactory.StorageType type : dataStorages) {
            BaseStorage<?> storage = StorageFactory.getStorage(type);
            Assert.assertFalse(storage.getList().isEmpty(), "Expected non-empty storage: " + type.name());
        }
    }

    public void testPerformanceSmokeStorageWarmupAndCharacterRoundtrip() throws Exception {
        Path saveDir = Files.createTempDirectory("ve-xml-perf-");
        this.configuration.setSaveDirPath(saveDir.toString());
        this.configuration.setOpenDirPath(saveDir.toString());

        long warmupStart = System.nanoTime();
        StorageFactory.storageWarmUp();
        long warmupMillis = (System.nanoTime() - warmupStart) / 1_000_000;

        CharacterStorage characterStorage = new CharacterStorage();
        Character character = TestCharacterUtility.createTestCharacter();
        String filename = "performance-character.xml";

        long saveStart = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            characterStorage.save(character, filename);
        }
        long saveMillis = (System.nanoTime() - saveStart) / 1_000_000;

        long loadStart = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            Character loaded = characterStorage.load(filename);
            Assert.assertEquals(loaded, character);
        }
        long loadMillis = (System.nanoTime() - loadStart) / 1_000_000;

        // Keep timings visible in CI logs as a migration reference point.
        System.out.printf("XML migration performance smoke (ms): warmup=%d, save50=%d, load50=%d%n",
            warmupMillis,
            saveMillis,
            loadMillis);

        Assert.assertTrue(warmupMillis >= 0);
        Assert.assertTrue(saveMillis >= 0);
        Assert.assertTrue(loadMillis >= 0);
    }
}

