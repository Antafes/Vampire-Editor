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
import antafes.vampireEditor.entity.character.Flaw;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Storage for flaws.
 */
public class FlawStorage extends BaseStorage<Flaw> {
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
        try (InputStream is = VampireEditor.getFileInJar(VampireEditor.getDataPath() + "flaws.xml")) {
            JAXBContext context = JaxbBindingSupport.createContext(FlawsDocument.class);
            Unmarshaller unmarshaller = JaxbBindingSupport.createUnmarshaller(context);
            FlawsDocument doc = (FlawsDocument) unmarshaller.unmarshal(is);

            doc.flaws.forEach((flaw) -> this.getList().put(flaw.getKey(), flaw));
        } catch (Exception e) {
            Logger.getLogger(FlawStorage.class.getName()).log(Level.SEVERE, "Could not load flaws", e);
        }
    }

    @XmlRootElement(name = "flaws")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class FlawsDocument {
        @XmlElement(name = "flaw")
        public List<Flaw> flaws = new ArrayList<>();
    }
}
