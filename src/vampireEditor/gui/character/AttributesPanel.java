/**
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
package vampireEditor.gui.character;

import java.util.ArrayList;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import vampireEditor.Character;
import vampireEditor.Configuration;
import vampireEditor.character.AttributeInterface;
import vampireEditor.gui.BaseListPanel;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.utility.TranslatedComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AttributesPanel extends BaseListPanel implements TranslatableComponent {
    private vampireEditor.Character character = null;

    /**
     * Create a new attributes panel.
     *
     * @param configuration
     */
    public AttributesPanel(Configuration configuration) {
        super(configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.addPhyiscalFields();
        this.addSocialFields();
        this.addMentalFields();
        this.setSpinnerMaximum(this.character.getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
    }

    /**
     * Add all talent fields sorted by the translated name.
     */
    private void addPhyiscalFields() {
        ArrayList<String> physical = AttributeInterface.AttributeType.PHYSICAL.getAttributes();
        physical.sort(new TranslatedComparator());
        this.addFields("physical", physical, 1);
    }

    /**
     * Add all skill fields sorted by the translated name.
     */
    private void addSocialFields() {
        ArrayList<String> social = AttributeInterface.AttributeType.SOCIAL.getAttributes();
        social.sort(new TranslatedComparator());
        this.addFields("social", social, 1);
    }

    /**
     * Add all knowledge fields sorted by the translated name.
     */
    private void addMentalFields() {
        ArrayList<String> mental = AttributeInterface.AttributeType.MENTAL.getAttributes();
        mental.sort(new TranslatedComparator());
        this.addFields("mental", mental, 1);
    }

    /**
     * Create the attributes document listener.
     *
     * @return
     */
    @Override
    protected ComponentChangeListener createChangeListener() {
        return new ComponentChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        };
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    @Override
    public void setSpinnerMaximum(int maximum) {
        this.getFields("physical").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("social").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("mental").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
    }

    /**
     * Set the character used to prefill every field.
     *
     * @param character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    protected void fillCharacterData() {
        if (this.character == null) {
            return;
        }

        this.getFields().forEach((type, attributeList) -> {
            attributeList.stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
                this.character.getAttributes().stream()
                    .filter((attribute) -> (attribute.getName().equals(spinner.getName())))
                    .forEachOrdered((attribute) -> {
                        spinner.setValue(attribute.getValue());
                    });
            });
        });
    }

    /**
     * Update the texts of every component in the component.
     */
    @Override
    public void updateTexts() {
        this.getConfiguration().loadProperties();
        this.setLanguage(this.getConfiguration().getLanguageObject());
        this.removeAll();
        this.initComponents();
        this.init();
        this.invalidate();
        this.repaint();
    }
}
