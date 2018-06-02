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

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import vampireEditor.Character;
import vampireEditor.Configuration;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.language.LanguageInterface;

/**
 * A tabbed panel for displaying a character.
 *
 * @author Marian Pollzien
 */
public class CharacterTabbedPane extends JTabbedPane implements TranslatableComponent {
    private final Configuration configuration;
    private LanguageInterface language;
    private vampireEditor.Character character = null;

    /**
     * Creates new form CharacterFrame
     */
    public CharacterTabbedPane() {
        this.configuration = new Configuration();
        this.configuration.loadProperties();
        this.language = this.configuration.getLanguageObject();
    }

    /**
     * Initialize the frame.
     *
     * @throws java.lang.Exception
     */
    public void init() throws Exception {
        if (this.character == null) {
            throw new Exception("No character has been set.");
        }

        this.setSelectedIndex(-1);
        this.initComponents();
    }

    /**
     * Initialize the components that should be added.
     */
    private void initComponents() {
        this.addLooksPanel();
        this.addAttributesPanel();
        this.addAbilitiesPanel();
        this.addAdvantagesPanel();
    }

    /**
     * Add the looks panel.
     */
    private void addLooksPanel() {
        LooksPanel panel = new LooksPanel(this.configuration);
        panel.setCharacter(this.character);
        panel.init();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("looks"));
    }

    /**
     * Add the attributes panel.
     */
    private void addAttributesPanel() {
        AttributesPanel panel = new AttributesPanel(this.configuration);
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("attributes"));
    }

    /**
     * Add the abilities panel.
     */
    private void addAbilitiesPanel() {
        AbilitiesPanel panel = new AbilitiesPanel(this.configuration);
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("abilities"));
    }

    /**
     * Add the advantages panel.
     */
    private void addAdvantagesPanel() {
        AdvantagesPanel panel = new AdvantagesPanel(this.configuration);
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("advantages"));
    }

    /**
     * Get the character from which all data is fetched.
     *
     * @return
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Set the character from which to fetch all data.
     *
     * @param character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Update the texts of every component in the tabbed pane.
     */
    @Override
    public void updateTexts() {
        this.configuration.loadProperties();
        this.language = this.configuration.getLanguageObject();

        for (Component component : this.getComponents()) {
            JComponent tab = (JComponent) component;

            if (tab.getClass().equals(LooksPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("looks"));
                ((LooksPanel) tab).updateTexts();
            } else if (tab.getClass().equals(AttributesPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("attributes"));
                ((AttributesPanel) tab).updateTexts();
            } else if (tab.getClass().equals(AbilitiesPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("abilities"));
                ((AbilitiesPanel) tab).updateTexts();
            } else if (tab.getClass().equals(AdvantagesPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("advantages"));
                ((AdvantagesPanel) tab).updateTexts();
            }
        }
    }
}
