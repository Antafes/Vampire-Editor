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

package antafes.vampireEditor.utility;

import antafes.vampireEditor.entity.character.Nature;
import antafes.vampireEditor.entity.exception.EntityStorageException;
import antafes.vampireEditor.entity.storage.NatureStorage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NatureResolutionUtility
{
    public String normalizeOptionalText(String inputText)
    {
        if (inputText == null) {
            return null;
        }

        String normalizedText = inputText.trim();

        return normalizedText.isEmpty() ? null : normalizedText;
    }

    public String resolveNatureKey(NatureStorage natureStorage, String inputText) throws EntityStorageException
    {
        Nature nature = resolveNature(natureStorage, inputText);

        return nature == null ? null : nature.getKey();
    }

    public Nature resolveNature(NatureStorage natureStorage, String inputText) throws EntityStorageException
    {
        String natureText = normalizeOptionalText(inputText);
        if (natureText == null) {
            return null;
        }

        Nature nature = natureStorage.getList().get(natureText);
        if (nature != null) {
            return nature;
        }

        String normalizedKey = StringUtility.toCamelCase(natureText);
        nature = natureStorage.getList().get(normalizedKey);
        if (nature != null) {
            return nature;
        }

        for (Nature storedNature : natureStorage.getList().values()) {
            if (storedNature.getKey().equalsIgnoreCase(natureText)) {
                return storedNature;
            }

            if (storedNature.getNames() == null) {
                continue;
            }

            boolean hasNameMatch = storedNature.getNames().values().stream()
                .anyMatch((name) -> name != null && name.equalsIgnoreCase(natureText));
            if (hasNameMatch) {
                return storedNature;
            }
        }

        return natureStorage.getEntity(natureText);
    }
}

