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

import antafes.vampireEditor.entity.BaseTypedTranslatedEntity;
import antafes.vampireEditor.entity.character.EntityTypeInterface;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SortingUtility
{
    public static HashMap<String, String> sortAndStringifyEntityMapWithFiltering(
        HashMap<String, BaseTypedTranslatedEntity> entityMap,
        EntityTypeInterface filterType
    ) {
        HashMap<String, BaseTypedTranslatedEntity> list = new HashMap<>();
        entityMap.values().stream()
            .filter((entity) -> (entity.getType().equals(filterType)))
            .forEachOrdered((entity) -> list.put(entity.getKey(), entity));

        return sortAndStringifyEntityMap(list);
    }

    public static HashMap<String, String> sortAndStringifyEntityMap(HashMap<String, BaseTypedTranslatedEntity> entityMap)
    {
        return (HashMap<String, String>) transformMap(entityMap).entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(new StringComparator()))
            .collect(
                Collectors.toMap(
                    o -> ((Map.Entry<?, ?>) o).getKey(),
                    o -> ((Map.Entry<?, ?>) o).getValue(),
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                )
            );
    }

    public static HashMap<String, BaseTypedTranslatedEntity> sortEntityMap(HashMap<String, BaseTypedTranslatedEntity> entityMap)
    {
        return (HashMap<String, BaseTypedTranslatedEntity>) entityMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(new StringComparator()))
            .collect(
                Collectors.toMap(
                    o -> ((Map.Entry<?, ?>) o).getKey(),
                    o -> (BaseTypedTranslatedEntity) ((Map.Entry<?, ?>) o).getValue(),
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                )
            );
    }

    private static HashMap<String, String> transformMap(HashMap<String, BaseTypedTranslatedEntity> entityMap)
    {
        HashMap<String, String> list = new HashMap<>();
        entityMap.values()
            .forEach((entity) -> list.put(entity.getKey(), entity.getName()));

        return list;
    }
}
