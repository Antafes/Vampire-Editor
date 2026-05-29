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

package antafes.vampireEditor.utility;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.TestClanUtility;
import antafes.vampireEditor.entity.exception.EntityException;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.entity.storage.StorageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class ClanComparatorTest extends BaseTest {
    @BeforeMethod
    public void setUp() {
        super.setUp();
        StorageFactory.storageWarmUp();
    }

    public void testCompare() throws EntityException, EntityStorageException {
        ClanComparator comparator = new ClanComparator(this.configuration);
        Clan clan1 = TestClanUtility.createTestClan();
        Clan clan2 = TestClanUtility.createTestClan();

        final int expected = 0;
        final int actual = comparator.compare(clan1, clan2);

        Assert.assertEquals(actual, expected);
    }

    public void testCompareFirstGreater() throws EntityException, EntityStorageException {
        ClanComparator comparator = new ClanComparator(this.configuration);
        Clan clan1 = TestClanUtility.createTestClan().toBuilder()
            .setNames(new HashMap<>())
            .addName(Configuration.Language.ENGLISH, "First test clan")
            .build();
        Clan clan2 = TestClanUtility.createTestClan();

        final int expected = -14;
        final int actual = comparator.compare(clan1, clan2);

        Assert.assertEquals(actual, expected);
    }

    public void testCompareClanWithBloodline() throws EntityException, EntityStorageException {
        ClanComparator comparator = new ClanComparator(this.configuration);
        Clan clan = TestClanUtility.createTestClan();
        Clan bloodline = TestClanUtility.createTestClan().toBuilder()
            .setKey("testBloodline")
            .setBloodline(true)
            .build();

        final int expected = 0;
        final int actual = comparator.compare(clan, bloodline);

        Assert.assertEquals(actual, expected);
    }

    public void testCompareBloodlineWithBloodline() throws EntityException, EntityStorageException {
        ClanComparator comparator = new ClanComparator(this.configuration);
        Clan bloodline1 = TestClanUtility.createTestClan().toBuilder()
            .setBloodline(true)
            .build();
        Clan bloodline2 = TestClanUtility.createTestClan().toBuilder()
            .setKey("testBloodline2")
            .setNames(new HashMap<>())
            .addName(Configuration.Language.ENGLISH, "Another test bloodline")
            .setBloodline(true)
            .build();

        final int actual = comparator.compare(bloodline1, bloodline2);

        Assert.assertTrue(actual > 0);
    }
}
