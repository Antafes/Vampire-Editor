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

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

@UtilityClass
public class StringUtility
{
    /**
     * This will change the entered string into camel-case format. In addition, it'll also strip all accents, german
     * umlauts and all special characters, except - and _
     */
    public String toCamelCase(String string)
    {
        string = string.replace("Ä", "Ae");
        string = string.replace("Ö", "Oe");
        string = string.replace("Ü", "Ue");
        string = string.replace("ä", "ae");
        string = string.replace("ö", "oe");
        string = string.replace("ü", "ue");
        string = string.replace("ß", "ss");
        string = string.replaceAll("[,\\.;:\\?!\"§\\$%&/\\(\\)=\\\\\\*\\+'#<>\\|@~\\{}\\[\\]^]", "");
        return CaseUtils.toCamelCase(StringUtils.stripAccents(string), false, ' ', '-', '_');
    }
}
