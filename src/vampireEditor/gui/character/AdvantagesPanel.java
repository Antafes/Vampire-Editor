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
import vampireEditor.character.AdvantageInterface;
import vampireEditor.gui.BaseListPanel;
import vampireEditor.gui.ComponentChangeListener;
import vampireEditor.gui.TranslatableComponent;
import vampireEditor.utility.StringComparator;

/**
 *
 * @author Marian Pollzien
 */
public class AdvantagesPanel extends BaseListPanel implements TranslatableComponent, CharacterPanelInterface {
    private vampireEditor.Character character = null;

    /**
     * Create a new advantages panel.
     *
     * @param configuration
     */
    public AdvantagesPanel(Configuration configuration) {
        super(configuration);
    }

    /**
     * Initialize everything.
     */
    @Override
    protected void init() {
        this.setTranslateFieldLabels(false);
        this.addBackgroundFields();
        this.addDisciplinFields();
        this.addVirtueFields();
        this.setSpinnerMaximum(this.character.getGeneration().getMaximumAttributes());
        this.fillCharacterData();

        super.init();
    }

    /**
     * Add all background fields sorted by the translated name.
     */
    private void addBackgroundFields() {
        ArrayList<String> backgrounds = new ArrayList<>();

        this.character.getAdvantages().stream()
            .filter((advantage) -> (advantage.getType().equals(AdvantageInterface.AdvantageType.BACKGROUND)))
            .forEachOrdered((advantage) -> {
                backgrounds.add(advantage.getName());
            });
        backgrounds.sort(new StringComparator());

        this.addFields("background", backgrounds);
    }

    /**
     * Add all disciplin fields sorted by the translated name.
     */
    private void addDisciplinFields() {
        ArrayList<String> disciplins = new ArrayList<>();

        this.character.getAdvantages().stream()
            .filter((advantage) -> (advantage.getType().equals(AdvantageInterface.AdvantageType.DISCIPLIN)))
            .forEachOrdered((advantage) -> {
                disciplins.add(advantage.getName());
            });
        disciplins.sort(new StringComparator());

        this.addFields("disciplins", disciplins);
    }

    /**
     * Add all virtue fields sorted by the translated name.
     */
    private void addVirtueFields() {
        ArrayList<String> virtues = new ArrayList<>();

        this.character.getAdvantages().stream()
            .filter((advantage) -> (advantage.getType().equals(AdvantageInterface.AdvantageType.VIRTUE)))
            .forEachOrdered((advantage) -> {
                virtues.add(advantage.getName());
            });
        virtues.sort(new StringComparator());

        this.addFields("virtues", virtues);
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
        this.getFields("background").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("disciplins").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
        this.getFields("virtues").stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
            this.setFieldMaximum(spinner, maximum);
        });
    }

    /**
     * Set the character used to prefill every field.
     *
     * @param character
     */
    @Override
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Fill in the character data. If no character is set, nothing will be added.
     */
    @Override
    public void fillCharacterData() {
        if (this.character == null) {
            return;
        }

        this.getFields().forEach((type, advantagesList) -> {
            advantagesList.stream().map((component) -> (JSpinner) component).forEachOrdered((spinner) -> {
                this.character.getAdvantages().stream()
                    .filter((advantage) -> (advantage.getName().equals(spinner.getName())))
                    .forEachOrdered((advantage) -> {
                        spinner.setValue(advantage.getValue());
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
