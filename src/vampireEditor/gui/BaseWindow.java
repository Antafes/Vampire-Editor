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
package vampireEditor.gui;

import java.awt.Component;
import vampireEditor.gui.character.CharacterTabbedPane;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vampireEditor.Configuration;
import vampireEditor.VampireEditor;
import vampireEditor.character.Ability;
import vampireEditor.character.AbilityInterface;
import vampireEditor.character.Advantage;
import vampireEditor.character.Attribute;
import vampireEditor.character.AttributeInterface;
import vampireEditor.language.LanguageInterface;

/**
 *
 * @author Marian Pollzien
 */
public class BaseWindow extends javax.swing.JFrame {

    private final Configuration configuration;
    private LanguageInterface language;

    /**
     * Creates new form BaseWindow
     */
    public BaseWindow() {
        this.configuration = new Configuration();
        this.configuration.loadProperties();
        this.language = this.configuration.getLanguageObject();

        this.initComponents();
        this.init();
        this.setFieldTexts();
    }

    /**
     * !!! This is only for testing the character frames !!!
     *
     * @return
     */
    private vampireEditor.Character createTestCharacter() {
        vampireEditor.Character character = new vampireEditor.Character();
        character.setName("Test Character");
        character.setGeneration(VampireEditor.getGenerations().get(4));
        character.setNature("wise");
        character.setBehaviour("strict");
        character.setConcept("Really no concept!");
        character.setClan(VampireEditor.getClan("brujah"));
        character.setSex(vampireEditor.Character.Sex.MALE);

        character.getAttributes().add(new Attribute("strength", AttributeInterface.AttributeType.PHYSICAL, 4));
        character.getAttributes().add(new Attribute("dexterity", AttributeInterface.AttributeType.PHYSICAL, 3));
        character.getAttributes().add(new Attribute("stamina", AttributeInterface.AttributeType.PHYSICAL, 3));
        character.getAttributes().add(new Attribute("charisma", AttributeInterface.AttributeType.SOCIAL, 3));
        character.getAttributes().add(new Attribute("appearance", AttributeInterface.AttributeType.SOCIAL, 3));
        character.getAttributes().add(new Attribute("manipulation", AttributeInterface.AttributeType.SOCIAL, 2));
        character.getAttributes().add(new Attribute("intelligence", AttributeInterface.AttributeType.MENTAL, 2));
        character.getAttributes().add(new Attribute("perception", AttributeInterface.AttributeType.MENTAL, 3));
        character.getAttributes().add(new Attribute("wits", AttributeInterface.AttributeType.MENTAL, 1));

        character.getAbilities().add(new Ability("alertness", AbilityInterface.AbilityType.TALENT, 3));
        character.getAbilities().add(new Ability("brawl", AbilityInterface.AbilityType.TALENT, 3));
        character.getAbilities().add(new Ability("intimidation", AbilityInterface.AbilityType.TALENT, 2));
        character.getAbilities().add(new Ability("dodge", AbilityInterface.AbilityType.TALENT, 1));
        character.getAbilities().add(new Ability("melee", AbilityInterface.AbilityType.SKILL, 3));
        character.getAbilities().add(new Ability("ride", AbilityInterface.AbilityType.SKILL, 3));
        character.getAbilities().add(new Ability("survival", AbilityInterface.AbilityType.SKILL, 3));
        character.getAbilities().add(new Ability("archery", AbilityInterface.AbilityType.SKILL, 2));
        character.getAbilities().add(new Ability("crafts", AbilityInterface.AbilityType.SKILL, 2));
        character.getAbilities().add(new Ability("investigation", AbilityInterface.AbilityType.KNOWLEDGE, 3));
        character.getAbilities().add(new Ability("law", AbilityInterface.AbilityType.KNOWLEDGE, 2));

        Advantage allies = VampireEditor.getAdvantage("allies");
        allies.setValue(3);
        character.getAdvantages().add(allies);
        Advantage influence = VampireEditor.getAdvantage("influence");
        influence.setValue(2);
        character.getAdvantages().add(influence);
        Advantage auspex = VampireEditor.getAdvantage("auspex");
        auspex.setValue(2);
        character.getAdvantages().add(auspex);
        Advantage celerity = VampireEditor.getAdvantage("celerity");
        celerity.setValue(1);
        character.getAdvantages().add(celerity);
        Advantage dominate = VampireEditor.getAdvantage("dominate");
        dominate.setValue(1);
        character.getAdvantages().add(dominate);
        Advantage conscience = VampireEditor.getAdvantage("conscience");
        conscience.setValue(3);
        character.getAdvantages().add(conscience);
        Advantage courage = VampireEditor.getAdvantage("courage");
        courage.setValue(2);
        character.getAdvantages().add(courage);
        Advantage selfControl = VampireEditor.getAdvantage("self-control");
        selfControl.setValue(2);
        character.getAdvantages().add(selfControl);

        character.getFlaws().add(VampireEditor.getFlaws().get("monstrous"));
        character.getFlaws().add(VampireEditor.getFlaws().get("deepSleeper"));
        character.getMerits().add(VampireEditor.getMerits().get("commonSense"));
        character.getMerits().add(VampireEditor.getMerits().get("eideticMemory"));

        character.setRoad(VampireEditor.getRoads().get("roadOfHumanity"));
        character.getRoad().setValue(5);

        character.setWillpower(5);
        character.setBloodStock(3);

        return character;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        languageGroup = new javax.swing.ButtonGroup();
        aboutDialog = new javax.swing.JDialog(this, true);
        jPanel1 = new javax.swing.JPanel();
        closeAboutButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        aboutTextPane = new javax.swing.JTextPane();
        charactersTabPane = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        languageMenu = new javax.swing.JMenu();
        englishMenuItem = new javax.swing.JRadioButtonMenuItem();
        germanMenuItem = new javax.swing.JRadioButtonMenuItem();

        aboutDialog.setModal(true);
        aboutDialog.setResizable(false);
        aboutDialog.setSize(new java.awt.Dimension(300, 400));

        closeAboutButton.setText("Close");
        closeAboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAboutButtonActionPerformed(evt);
            }
        });

        aboutTextPane.setEditable(false);
        aboutTextPane.setText("This program was created by Marian Pollzien.");
        aboutTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                aboutTextPaneKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(aboutTextPane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(closeAboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeAboutButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        closeMenuItem.setText("Quit");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/english.png"))); // NOI18N

        englishMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageMenuItemActionPerformed(evt);
            }
        });
        languageGroup.add(englishMenuItem);
        englishMenuItem.setSelected(true);
        englishMenuItem.setText("English");
        englishMenuItem.setActionCommand("English");
        englishMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/english.png"))); // NOI18N
        languageMenu.add(englishMenuItem);

        germanMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageMenuItemActionPerformed(evt);
            }
        });
        languageGroup.add(germanMenuItem);
        germanMenuItem.setText("German");
        germanMenuItem.setActionCommand("German");
        germanMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/german.png"))); // NOI18N
        languageMenu.add(germanMenuItem);

        menuBar.add(Box.createHorizontalGlue());

        menuBar.add(languageMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(charactersTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(charactersTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        this.configuration.setWindowLocation(this.getLocationOnScreen());
        this.configuration.saveProperties();
        System.exit(0);
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        int x,
            y,
            width = this.aboutDialog.getWidth(),
            height = this.aboutDialog.getHeight();

        x = this.configuration.getWindowLocation().x + (this.getWidth() / 2 - width / 2);
        y = this.configuration.getWindowLocation().y + (this.getHeight() / 2 - height / 2);

        this.aboutDialog.setBounds(x, y, width, height);
        this.aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void closeAboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAboutButtonActionPerformed
        this.aboutDialog.setVisible(false);
    }//GEN-LAST:event_closeAboutButtonActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        int x, y, width, height;

        // Add the new character dialog.
        NewCharacterDialog newDialog = new NewCharacterDialog(this, true);
        newDialog.setVisible(false);
        newDialog.setParent(this);

        width = newDialog.getWidth();
        height = newDialog.getHeight();
        x = this.configuration.getWindowLocation().x + (this.getWidth() / 2 - width / 2);
        y = this.configuration.getWindowLocation().y + (this.getHeight() / 2 - height / 2);

        newDialog.setBounds(x, y, width, height);
        newDialog.setVisible(true);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void aboutTextPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aboutTextPaneKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.aboutDialog.setVisible(false);
        }
    }//GEN-LAST:event_aboutTextPaneKeyPressed

    /**
     * Action that will be performed on changing the language.
     *
     * @param evt
     */
    private void languageMenuItemActionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("English")) {
            this.configuration.setLanguage(Configuration.Language.ENGLISH);
        } else if (evt.getActionCommand().equals("German")) {
            this.configuration.setLanguage(Configuration.Language.GERMAN);
        }

        this.configuration.saveProperties();
        this.language = this.configuration.getLanguageObject();
        this.languageMenu.setIcon(this.configuration.getLanguage().getIcon());
        this.setFieldTexts();

        for (Component component : this.charactersTabPane.getComponents()) {
            CharacterTabbedPane pane = (CharacterTabbedPane) component;
            pane.updateTexts();
        }
    }

    /**
     * Initialize everything.
     */
    private void init() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setLocation(this.configuration.getWindowLocation());
        this.languageMenu.setIcon(this.configuration.getLanguage().getIcon());

        if (this.configuration.getLanguage() == Configuration.Language.ENGLISH) {
            this.languageGroup.setSelected(this.englishMenuItem.getModel(), true);
        } else if (this.configuration.getLanguage() == Configuration.Language.GERMAN) {
            this.languageGroup.setSelected(this.germanMenuItem.getModel(), true);
        }
    }

    /**
     * Set the texts of every field.
     */
    private void setFieldTexts() {
        this.setTitle(this.language.translate("title"));

        this.englishMenuItem.setText(this.language.translate("english"));
        this.germanMenuItem.setText(this.language.translate("german"));

        this.fileMenu.setText(this.language.translate("file"));
        this.fileMenu.setMnemonic(this.language.translate("fileMnemonic").charAt(0));
        this.closeMenuItem.setText(this.language.translate("quit"));
        this.closeMenuItem.setMnemonic(this.language.translate("quitMnemonic").charAt(0));
        this.helpMenu.setText(this.language.translate("help"));
        this.helpMenu.setMnemonic(this.language.translate("helpMnemonic").charAt(0));
        this.aboutMenuItem.setText(this.language.translate("about"));
        this.aboutMenuItem.setMnemonic(this.language.translate("aboutMnemonic").charAt(0));
        this.aboutDialog.setTitle(this.language.translate("about"));
        this.closeAboutButton.setText(this.language.translate("close"));
        this.aboutTextPane.setText(this.language.translate("aboutText"));
        this.newMenuItem.setText(this.language.translate("new"));
        this.newMenuItem.setMnemonic(this.language.translate("newMnemonic").charAt(0));
    }

    /**
     * Install a dialog wide escape handler.
     *
     * @param dialog
     */
    public static void installEscapeCloseOperation(JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dialog.dispatchEvent(new WindowEvent(
                    dialog, WindowEvent.WINDOW_CLOSING
                ));
            }
        };
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            escapeStroke, dispatchWindowClosingActionMapKey
        );
        root.getActionMap().put( dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    /**
     * Add a new character to the tabbed panel.
     *
     * @param character
     */
    public void addCharacter(vampireEditor.Character character) {
        try {
            CharacterTabbedPane characterTabbedPane = new CharacterTabbedPane();
            characterTabbedPane.setCharacter(character);
            characterTabbedPane.init();
            this.charactersTabPane.add(characterTabbedPane);
            this.charactersTabPane.setTitleAt(this.charactersTabPane.indexOfComponent(characterTabbedPane), character.getName());
        } catch (Exception ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JTextPane aboutTextPane;
    private javax.swing.JTabbedPane charactersTabPane;
    private javax.swing.JButton closeAboutButton;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JRadioButtonMenuItem englishMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JRadioButtonMenuItem germanMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.ButtonGroup languageGroup;
    private javax.swing.JMenu languageMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
