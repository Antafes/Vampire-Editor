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
import antafes.vampireEditor.entity.character.Clan;
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
import java.util.*;
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

    /**
     * Returns all top-level roads available for the given clan.
     * Universal roads (no clan restrictions) are always included.
     * Clan-specific roads are included only if the clan key matches.
     *
     * @param clan the selected clan, must not be null
     * @return list of roads available to clan
     */
    public ArrayList<Road> getRoadsForClan(@NonNull Clan clan) {
        return this.getList().values().stream()
            .filter(road -> road.getParent() == null)
            .filter(road -> road.isUniversal() || road.isRestrictedToClan(clan.getKey()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Load available data.
     */
    private void loadData() {
        try (InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "roads.xml")) {
            JAXBContext context = JaxbBindingSupport.createContext(RoadsDocument.class);
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(context);
            RoadsDocument doc = (RoadsDocument) unmarshaller.unmarshal(is);

            doc.roads.forEach(road -> this.getList().put(road.getKey(), road));
            this.resolveAndValidateParents();
        } catch (Exception e) {
            throw new RuntimeException("Could not load roads data", e);
        }
    }

    /**
     * Resolve parent key strings to Road objects and validate parent references.
     * Checks for missing parent references, self-parenting, and circular parent chains.
     */
    private void resolveAndValidateParents() {
        this.getList().values().forEach(this::validateParentReference);
        this.getList().values().forEach(road -> this.validateNoCircularParent(road.getKey(), new HashSet<>()));

        Map<String, Road> resolvedRoads = new HashMap<>();
        this.getList().values().forEach(road -> resolvedRoads.put(road.getKey(), this.buildResolvedRoad(road, resolvedRoads)));

        this.getList().clear();
        this.getList().putAll(resolvedRoads);
    }

    private void validateParentReference(Road road) {
        if (road.getParentKey() == null || road.getParentKey().isEmpty()) {
            return;
        }

        if (!this.getList().containsKey(road.getParentKey())) {
            throw new RuntimeException(
                "Road '" + road.getKey() + "' has invalid parent key '" + road.getParentKey() + "': parent road not found in roads.xml"
            );
        }

        if (road.getKey().equals(road.getParentKey())) {
            throw new RuntimeException("Road '" + road.getKey() + "' cannot be its own parent");
        }
    }

    /**
     * Recursively check for circular parent references by following parent keys.
     *
     * @param roadKey The road key to check
     * @param visited Set of already-visited road keys
     */
    private void validateNoCircularParent(String roadKey, Set<String> visited) {
        if (!visited.add(roadKey)) {
            throw new RuntimeException("Circular parent reference detected involving road '" + roadKey + "'");
        }

        Road road = this.getList().get(roadKey);
        if (road != null && road.getParentKey() != null && !road.getParentKey().isEmpty()) {
            this.validateNoCircularParent(road.getParentKey(), visited);
        }

        visited.remove(roadKey);
    }

    private Road buildResolvedRoad(Road road, Map<String, Road> resolvedRoads) {
        if (resolvedRoads.containsKey(road.getKey())) {
            return resolvedRoads.get(road.getKey());
        }

        Road parentRoad = null;
        if (road.getParentKey() != null && !road.getParentKey().isEmpty()) {
            parentRoad = this.buildResolvedRoad(this.getList().get(road.getParentKey()), resolvedRoads);
        }

        Road resolvedRoad = road.toBuilder().setParent(parentRoad).build();
        resolvedRoads.put(road.getKey(), resolvedRoad);
        return resolvedRoad;
    }

    @XmlRootElement(name = "roads")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class RoadsDocument {
        @XmlElement(name = "road")
        public List<Road> roads = new ArrayList<>();
    }
}
