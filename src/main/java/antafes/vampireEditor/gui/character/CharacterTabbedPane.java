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
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.gui.TranslatableComponent;
import antafes.vampireEditor.gui.element.CloseableTabbedPane;
import antafes.vampireEditor.gui.event.CharacterChangedEvent;
import antafes.vampireEditor.gui.event.listener.CharacterChangedListener;
import antafes.vampireEditor.gui.modification.CharacterModificationTracker;
import antafes.vampireEditor.language.LanguageInterface;
import antafes.vampireEditor.print.General;
import antafes.vampireEditor.print.PaperA4;
import antafes.vampireEditor.print.PrintBase;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A tabbed panel for displaying a character.
 *
 * @author Marian Pollzien
 */
public class CharacterTabbedPane extends JTabbedPane implements TranslatableComponent {
    private final Configuration configuration;
    private LanguageInterface language;
    @Getter
    private antafes.vampireEditor.entity.Character character = null;
    @Getter
    private PrintPreviewPanel printPreview;
    @Getter
    private final ArrayList<PrintBase> printPages;
    @Getter
    private boolean isCharacterChanged = false;
    private final Map<String, Boolean> changedComponents = new HashMap<>();

    private final CharacterModificationTracker modificationTracker;

    /**
     * Creates new form CharacterFrame
     */
    public CharacterTabbedPane() {
        this.configuration = Configuration.getInstance();
        this.language = this.configuration.getLanguageObject();
        this.printPages = new ArrayList<>();
        this.modificationTracker = new CharacterModificationTracker();
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
        this.addCharacterChangedListener();
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

    private void addCharacterChangedListener()
    {
        VampireEditor.getDispatcher().addListener(
            CharacterChangedEvent.class,
            new CharacterChangedListener(this::handleCharacterChangedEvent)
        );
    }

    public void setCharacterChanged(boolean characterChanged)
    {
        this.isCharacterChanged = characterChanged;

        if (!characterChanged) {
            this.changedComponents.clear();
        }

        this.updateTabTitle();
    }

    public void setCharacter(antafes.vampireEditor.entity.Character character)
    {
        this.character = character;

        if (this.printPreview != null) {
            this.printPreview.setCharacter(character);
        }

        if (this.getTabCount() > 0) {
            this.rebuildPrintPages();
        }
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
        this.rebuildPrintPages();
    }

    private void rebuildPrintPages()
    {
        this.printPages.clear();

        if (this.character == null) {
            return;
        }

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

    /**
     * Check if the character has unsaved modifications.
     *
     * @return true if the character has unsaved modifications, false otherwise
     */
    public boolean isModified() {
        return this.modificationTracker.isModified();
    }

    /**
     * Clear the modification flag after a successful save operation.
     * This should be called by the save handler when the character is successfully saved.
     */
    public void resetModificationFlag() {
        this.modificationTracker.resetModified();
    }

    /**
     * Mark the character as having unsaved modifications.
     * This should be called by component listeners when the character is edited.
     */
    public void markModified() {
        this.modificationTracker.markModified();
    }

    void handleCharacterChangedEvent(CharacterChangedEvent event)
    {
        if (event.getCharacter() != this.character) {
            return;
        }

        String componentIdentifier = event.getComponentIdentifier();
        if (componentIdentifier != null) {
            this.changedComponents.put(componentIdentifier, event.isChanged());
            this.isCharacterChanged = this.changedComponents.values().stream().anyMatch(Boolean::booleanValue);
        } else {
            this.isCharacterChanged = event.isChanged();
        }

        this.updateTabTitle();
    }

    private void updateTabTitle()
    {
        CloseableTabbedPane tabbedPane = (CloseableTabbedPane) SwingUtilities
            .getAncestorOfClass(CloseableTabbedPane.class, this);

        if (tabbedPane == null) {
            return;
        }

        int tabIndex = tabbedPane.indexOfComponent(this);
        if (tabIndex < 0) {
            return;
        }

        String tabName = this.getCharacter().getName();

        if (this.isCharacterChanged) {
            tabName += "*";
        }

        tabbedPane.setTitleAt(tabIndex, tabName);
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }
}
