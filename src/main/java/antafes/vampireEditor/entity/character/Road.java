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
 * @copyright (c) 2018, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package antafes.vampireEditor.entity.character;

import antafes.vampireEditor.entity.BaseValuedTranslatedEntity;
import antafes.vampireEditor.entity.storage.adapter.AdvantageListAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Road object.
 *
 * @author Marian Pollzien
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "set")
@XmlRootElement(name = "road")
@XmlAccessorType(XmlAccessType.NONE)
public class Road extends BaseValuedTranslatedEntity implements RoadInterface {
    @XmlElement(name = "advantages")
    @XmlJavaTypeAdapter(AdvantageListAdapter.class)
    private List<Advantage> merits;

    /**
     * Parent road key read from XML (<parent>...</parent>) for path entries.
     * Used during road loading/validation to resolve the actual parent Road.
     * Because it is part of the model, it is available to Lombok-generated
     * builder/toBuilder and may remain populated in-memory after loading.
     */
    @XmlElement(name = "parent")
    private String parentKey;

    /**
     * Optional parent road (for paths).
     * Only set for path entries that inherit from a parent road.
     * Resolved from parentKey during road data loading and validation.
     * Field is excluded from XML serialization (@XmlTransient) and from
     * equals/hashCode (@EqualsAndHashCode.Exclude), but is included in the
     * Lombok-generated builder via @SuperBuilder(toBuilder = true).
     */
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private Road parent;

    protected Road()
    {
        super();
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    public static int calculateRoadScore(ArrayList<Advantage> virtues)
    {
        int roadScore = 0;

        // If there's more than 3 virtues, it probably means the road hasn't been selected.
        if (virtues.size() > 3) {
            return 2;
        }

        for (Advantage virtue : virtues) {
            if (virtue.getKey().equals("courage")) {
                continue;
            }

            roadScore += virtue.getValue();
        }

        return roadScore;
    }
}
