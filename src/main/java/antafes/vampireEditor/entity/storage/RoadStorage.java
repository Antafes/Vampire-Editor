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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            throw new RuntimeException("Could not load roads data", e);
        }
    }

    @XmlRootElement(name = "roads")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class RoadsDocument {
        @XmlElement(name = "road")
        public List<Road> roads = new ArrayList<>();
    }
}
