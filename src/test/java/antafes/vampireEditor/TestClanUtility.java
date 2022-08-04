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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor;

import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.EntityStorageException;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.storage.AdvantageStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.entity.storage.WeaknessStorage;
import org.testng.annotations.BeforeMethod;

/**
 * Test clan utility class.
 */
public class TestClanUtility {
    @BeforeMethod
    public void setUp() {
        StorageFactory.storageWarmUp();
    }

    /**
     * Create a test clan.
     *
     * @return A clan object
     * @throws EntityException Thrown if something happened while building
     */
    public static Clan createTestClan() throws EntityException, EntityStorageException {
        AdvantageStorage advantageStorage = (AdvantageStorage) StorageFactory.getStorage(StorageFactory.StorageType.ADVANTAGE);
        WeaknessStorage weaknessStorage = (WeaknessStorage) StorageFactory.getStorage(StorageFactory.StorageType.WEAKNESS);
        return Clan.builder()
            .setKey("testClan")
            .addName(Configuration.Language.ENGLISH, "Test clan")
            .addNickname(Configuration.Language.ENGLISH, "Test nickname")
            .addAdvantage(advantageStorage.getEntity("allies"))
            .addWeakness(weaknessStorage.getEntity("obsession"))
            .build();
    }
}
