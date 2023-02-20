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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.utility;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.BaseTypedTranslatedEntity;
import antafes.vampireEditor.entity.character.Merit;
import antafes.vampireEditor.entity.character.SpecialFeatureInterface;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class SortingUtilityTest
{
    public void testSortAndStringifyEntityMapWithFiltering()
    {
        Merit m1 = Merit.builder()
            .setKey("test1")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "Test 1")
            .build();
        Merit m2 = Merit.builder()
            .setKey("abcTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "ABC Test")
            .build();
        Merit m3 = Merit.builder()
            .setKey("xyzTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "XYZ Test")
            .build();
        Merit m4 = Merit.builder()
            .setKey("xyzTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "XYZ Test")
            .build();
        HashMap<String, BaseTypedTranslatedEntity> unsortedMap = new HashMap<>();
        unsortedMap.put(m1.getKey(), m1);
        unsortedMap.put(m2.getKey(), m2);
        unsortedMap.put(m3.getKey(), m3);
        unsortedMap.put(m4.getKey(), m4);
        HashMap<String, String> sortedMap = new HashMap<>();
        sortedMap.put(m2.getKey(), m2.getName());
        sortedMap.put(m1.getKey(), m1.getName());
        sortedMap.put(m3.getKey(), m3.getName());

        Assert.assertEquals(
            SortingUtility.sortAndStringifyEntityMapWithFiltering(unsortedMap, SpecialFeatureInterface.SpecialFeatureType.MENTAL),
            sortedMap
        );
    }

    public void testSortAndStringifyEntityMap()
    {
        Merit m1 = Merit.builder()
            .setKey("test1")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "Test 1")
            .build();
        Merit m2 = Merit.builder()
            .setKey("abcTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "ABC Test")
            .build();
        Merit m3 = Merit.builder()
            .setKey("xyzTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "XYZ Test")
            .build();
        HashMap<String, BaseTypedTranslatedEntity> unsortedMap = new HashMap<>();
        unsortedMap.put(m1.getKey(), m1);
        unsortedMap.put(m2.getKey(), m2);
        unsortedMap.put(m3.getKey(), m3);
        HashMap<String, String> sortedMap = new HashMap<>();
        sortedMap.put(m2.getKey(), m2.getName());
        sortedMap.put(m1.getKey(), m1.getName());
        sortedMap.put(m3.getKey(), m3.getName());

        Assert.assertEquals(
            SortingUtility.sortAndStringifyEntityMap(unsortedMap),
            sortedMap
        );
    }

    @Test
    public void testSortEntityMap()
    {
        Merit m1 = Merit.builder()
            .setKey("test1")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "Test 1")
            .build();
        Merit m2 = Merit.builder()
            .setKey("abcTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "ABC Test")
            .build();
        Merit m3 = Merit.builder()
            .setKey("xyzTest")
            .setType(SpecialFeatureInterface.SpecialFeatureType.MENTAL)
            .addName(Configuration.Language.ENGLISH, "XYZ Test")
            .build();
        HashMap<String, BaseTypedTranslatedEntity> unsortedMap = new HashMap<>();
        unsortedMap.put(m1.getKey(), m1);
        unsortedMap.put(m2.getKey(), m2);
        unsortedMap.put(m3.getKey(), m3);
        HashMap<String, BaseTypedTranslatedEntity> sortedMap = new HashMap<>();
        sortedMap.put(m2.getKey(), m2);
        sortedMap.put(m1.getKey(), m1);
        sortedMap.put(m3.getKey(), m3);

        Assert.assertEquals(
            SortingUtility.sortEntityMap(unsortedMap),
            sortedMap
        );
    }
}
