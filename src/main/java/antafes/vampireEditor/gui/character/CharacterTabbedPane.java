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
package antafes.vampireEditor.gui.character;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.language.LanguageInterface;
import antafes.vampireEditor.print.General;
import antafes.vampireEditor.print.PaperA4;
import antafes.vampireEditor.print.PrintBase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A tabbed panel for displaying a character.
 *
 * @author Marian Pollzien
 */
public class CharacterTabbedPane extends JTabbedPane implements TranslatableComponent {
    private final Configuration configuration;
    private LanguageInterface language;
    private antafes.vampireEditor.entity.Character character = null;
    private PrintPreviewPanel printPreview;
    private ArrayList<PrintBase> printPages;

    /**
     * Creates new form CharacterFrame
     */
    public CharacterTabbedPane() {
        this.configuration = Configuration.getInstance();
        this.language = this.configuration.getLanguageObject();
        this.printPages = new ArrayList<>();
    }

    /**
     * Initialize the frame.
     *
     * @throws java.lang.Exception Thrown if no character has been set
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
        this.addGeneralPanel();
        this.addAttributesPanel();
        this.addAbilitiesPanel();
        this.addAdvantagesPanel();
        this.addLooksPanel();
        this.addPrintPreviewPanel();
    }

    /**
     * Add the general panel.
     */
    private void addGeneralPanel() {
        GeneralPanel panel = new GeneralPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("general"));
    }

    /**
     * Add the looks panel.
     */
    private void addLooksPanel() {
        LooksPanel panel = new LooksPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("looks"));
    }

    /**
     * Add the attributes panel.
     */
    private void addAttributesPanel() {
        AttributesPanel panel = new AttributesPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("attributes"));
    }

    /**
     * Add the abilities panel.
     */
    private void addAbilitiesPanel() {
        AbilitiesPanel panel = new AbilitiesPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("abilities"));
    }

    /**
     * Add the advantages panel.
     */
    private void addAdvantagesPanel() {
        AdvantagesPanel panel = new AdvantagesPanel();
        panel.setCharacter(this.character);
        panel.start();
        this.add(panel);
        this.setTitleAt(this.indexOfComponent(panel), this.language.translate("advantages"));
    }

    /**
     * Add the print preview panel.
     */
    private void addPrintPreviewPanel() {
        this.printPreview = new PrintPreviewPanel();
        this.printPreview.setCharacter(this.character);
        this.printPreview.start();
        this.add(this.printPreview);
        this.setTitleAt(this.indexOfComponent(this.printPreview), this.language.translate("printPreview"));
        this.fillPrintPages();
    }

    /**
     * Fill in every available print page.
     */
    private void fillPrintPages()
    {
        PaperA4 paper = new PaperA4();
        Dimension dimension = new Dimension((int) paper.getImageableWidth(), (int) paper.getImageableHeight());
        General generalPage = new General(this.character);
        generalPage.setSize(dimension);
        generalPage.create();
        this.layoutComponent(generalPage);
        this.printPages.add(generalPage);
        PrintBase page = generalPage.getFollowingPageObject();

        do {
            page.setSize(dimension);
            page.create();
            this.layoutComponent(page);
            this.printPages.add(page);
            page = page.getFollowingPageObject();
        } while (page != null);
    }

    /**
     * Layout the component and each child element in it.
     *
     * @param component The component to layout
     */
    private void layoutComponent(Component component)
    {
        synchronized (component.getTreeLock())
        {
            component.doLayout();

            if (component instanceof Container)
            {
                for (Component child : ((Container)component).getComponents())
                {
                    this.layoutComponent(child);
                }
            }
        }
    }

    /**
     * Get the print preview panel.
     *
     * @return
     */
    public PrintPreviewPanel getPrintPreview() {
        return printPreview;
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
     * Get the list of print pages.
     *
     * @return
     */
    public ArrayList<PrintBase> getPrintPages() {
        return this.printPages;
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

            if (tab.getClass().equals(GeneralPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("general"));
                ((GeneralPanel) tab).updateTexts();
            } else if (tab.getClass().equals(LooksPanel.class)) {
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
            } else if (tab.getClass().equals(PrintPreviewPanel.class)) {
                this.setTitleAt(this.indexOfComponent(tab), this.language.translate("printPreview"));
                ((PrintPreviewPanel) tab).updateTexts();
            }
        }
    }
}
