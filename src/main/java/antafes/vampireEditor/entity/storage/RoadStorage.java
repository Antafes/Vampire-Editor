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
package antafes.vampireEditor.entity.storage;

import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.character.Road;
import antafes.vampireEditor.xml.jaxb.JaxbBindingSupport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.NonNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Storage for roads.
 */
public class RoadStorage extends BaseStorage<Road> {
    /**
     * Initializes the storage and pre-loads available data.
     */
    @Override
    public void init() {
        this.loadData();
    }

    /**
     * Load available data.
     */
    private void loadData() {
        try (InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "roads.xml")) {
            JAXBContext context = JaxbBindingSupport.createContext(RoadsDocument.class);
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(context);
            RoadsDocument doc = (RoadsDocument) unmarshaller.unmarshal(is);

            doc.roads.forEach((road) -> this.getList().put(road.getKey(), road));

            // Resolve parent references and validate
            this.resolveAndValidateParents();
        } catch (Exception e) {
            throw new RuntimeException("Could not load roads data", e);
        }
    }

    /**
     * Resolve parent key strings to Road objects and validate parent references.
     * Checks for:
     * - Missing parent references (throws exception)
     * - Self-parent (throws exception)
     * - Circular parent chains (throws exception)
     *
     * @throws RuntimeException if validation fails
     */
    private void resolveAndValidateParents() {
        for (Road road : this.getList().values()) {
            if (road.getParentKey() == null || road.getParentKey().isEmpty()) {
                continue;
            }

            Road parentRoad = this.getList().get(road.getParentKey());
            if (parentRoad == null) {
                throw new RuntimeException(
                    "Road '" + road.getKey() + "' has invalid parent key '" + road.getParentKey() +
                    "': parent road not found in roads.xml"
                );
            }

            if (road.getKey().equals(road.getParentKey())) {
                throw new RuntimeException(
                    "Road '" + road.getKey() + "' cannot be its own parent"
                );
            }

            this.validateNoCircularParent(road, new HashSet<>());

            Road.RoadBuilder<?, ?> builder = road.toBuilder();
            builder.setParent(parentRoad);
            Road updatedRoad = builder.build();
            this.getList().put(road.getKey(), updatedRoad);
        }
    }

    /**
     * Recursively check for circular parent references.
     * Throws exception if a cycle is detected.
     *
     * @param road The road to check
     * @param visited Set of already-visited road keys
     * @throws RuntimeException if a circular parent chain is detected
     */
    private void validateNoCircularParent(Road road, Set<String> visited) {
        if (visited.contains(road.getKey())) {
            throw new RuntimeException(
                "Circular parent reference detected involving road '" + road.getKey() + "'"
            );
        }

        Road parent = road.getParent();
        if (parent == null) {
            return;
        }

        visited.add(road.getKey());
        this.validateNoCircularParent(parent, visited);
        visited.remove(road.getKey());
    }

    /**
     * Returns all top-level roads that can be selected as a primary road.
     * Paths (roads with a parent) are excluded.
     *
     * @return list of roads without parent reference
     */
    public ArrayList<Road> getRoads() {
        return this.getList().values().stream()
            .filter(road -> road.getParent() == null)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns all child paths for the given parent road.
     *
     * @param parentRoad selected parent road, must not be null
     * @return list of roads whose parent is the given road
     * @throws NullPointerException if parentRoad is null
     */
    public ArrayList<Road> getPathsForRoad(@NonNull Road parentRoad) {
        return this.getList().values().stream()
            .filter(road -> road.getParent() != null)
            .filter(road -> parentRoad.getKey().equals(road.getParent().getKey()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @XmlRootElement(name = "roads")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class RoadsDocument {
        @XmlElement(name = "road")
        public List<Road> roads = new ArrayList<>();
    }
}
