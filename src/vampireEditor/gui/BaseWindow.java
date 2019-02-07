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

import vampireEditor.Configuration;
import vampireEditor.VampireEditor;
import vampireEditor.entity.EntityException;
import vampireEditor.entity.character.Ability;
import vampireEditor.entity.character.Advantage;
import vampireEditor.entity.character.Attribute;
import vampireEditor.entity.character.Road;
import vampireEditor.entity.storage.CharacterStorage;
import vampireEditor.gui.character.CharacterTabbedPane;
import vampireEditor.language.LanguageInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class BaseWindow extends javax.swing.JFrame {

    private final Configuration configuration;
    private LanguageInterface language;

    // List of components in the window
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
    private javax.swing.ButtonGroup languageGroup;
    private javax.swing.JMenu languageMenu;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JMenuItem saveMenuItem;

    /**
     * Creates new form BaseWindow
     */
    public BaseWindow() {
        this.configuration = Configuration.getInstance();
        this.language = this.configuration.getLanguageObject();

        this.initComponents();
        this.init();
        this.setFieldTexts();
    }

    /**
     * !!! This is only for testing the character frames !!!
     *
     * @return A test character
     */
    private vampireEditor.entity.Character createTestCharacter() {
        try {
            vampireEditor.entity.Character.Builder builder = new vampireEditor.entity.Character.Builder()
                .setName("Test Character")
                .setGeneration(VampireEditor.getGenerations().get(4))
                .setNature("wise")
                .setDemeanor("strict")
                .setConcept("Really no concept!")
                .setClan(VampireEditor.getClan("brujah"))
                .setSex(vampireEditor.entity.Character.Sex.MALE)
                .setRoad(VampireEditor.getRoads().get("roadOfHumanity"))
                .setWillpower(5)
                .setBloodStock(3);

            Attribute.Builder attributeBuilder = new Attribute.Builder();
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("strength")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("dexterity")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("stamina")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("charisma")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("appearance")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("manipulation")).setValue(2).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("intelligence")).setValue(2).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("perception")).setValue(3).build());
            builder.addAttribute(attributeBuilder.fillDataFromObject(VampireEditor.getAttribute("wits")).setValue(1).build());

            Ability.Builder abilityBuilder = new Ability.Builder();
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("alertness")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("brawl")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("intimidation")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("dodge")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("melee")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("ride")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("survival")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("archery")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("crafts")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("investigation")).setValue(3).build());
            builder.addAbility(abilityBuilder.fillDataFromObject(VampireEditor.getAbility("law")).setValue(3).build());

            Advantage.Builder advantageBuilder = new Advantage.Builder();
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("allies"))
                .setValue(3);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("influence"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("auspex"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("celerity"))
                .setValue(1);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("dominate"))
                .setValue(1);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("conscience"))
                .setValue(3);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("courage"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());
            advantageBuilder.fillDataFromObject(VampireEditor.getAdvantage("self-control"))
                .setValue(2);
            builder.addAdvantage(advantageBuilder.build());

            builder.addFlaw(VampireEditor.getFlaws().get("monstrous"));
            builder.addFlaw(VampireEditor.getFlaws().get("deepSleeper"));
            builder.addMerit(VampireEditor.getMerits().get("commonSense"));
            builder.addMerit(VampireEditor.getMerits().get("eideticMemory"));
            Road.Builder roadBuilder = new Road.Builder()
                .fillDataFromObject(VampireEditor.getRoad("humanity"))
                .setValue(5);
            builder.setRoad(roadBuilder.build());

            return builder.build();
        } catch (EntityException ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Initialize every component that should be shown on the panel.
     */
    private void initComponents() {

        languageGroup = new javax.swing.ButtonGroup();
        aboutDialog = new javax.swing.JDialog(this, true);
        JPanel jPanel1 = new JPanel();
        closeAboutButton = new javax.swing.JButton();
        JScrollPane jScrollPane2 = new JScrollPane();
        aboutTextPane = new javax.swing.JTextPane();
        saveFileChooser = new javax.swing.JFileChooser(){
            @Override
            public void approveSelection(){
                File f = getSelectedFile();

                if(f.exists() && getDialogType() == SAVE_DIALOG){
                    int result = JOptionPane.showConfirmDialog(
                        this,
                        language.translate("fileExists"),
                        language.translate("existingFile"),
                        JOptionPane.YES_NO_CANCEL_OPTION
                    );
                    switch(result){
                        case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                        case JOptionPane.NO_OPTION:
                        return;
                        case JOptionPane.CLOSED_OPTION:
                        return;
                        case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                    }
                }
                super.approveSelection();
            }
        };
        openFileChooser = new javax.swing.JFileChooser();
        charactersTabPane = new javax.swing.JTabbedPane();
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        JMenuItem openMenuItem = new JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
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
        closeAboutButton.addActionListener(this::closeAboutButtonActionPerformed);

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

        saveFileChooser.setCurrentDirectory(null);

        openFileChooser.setCurrentDirectory(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(this::newMenuItemActionPerformed);
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(this::openMenuItemActionPerformed);
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(this::saveMenuItemActionPerformed);
        fileMenu.add(saveMenuItem);

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        closeMenuItem.setText("Quit");
        closeMenuItem.addActionListener(this::closeMenuItemActionPerformed);
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(this::aboutMenuItemActionPerformed);
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/english.png"))); // NOI18N

        englishMenuItem.addActionListener(this::languageMenuItemActionPerformed);
        languageGroup.add(englishMenuItem);
        englishMenuItem.setSelected(true);
        englishMenuItem.setText("English");
        englishMenuItem.setActionCommand("English");
        englishMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/english.png"))); // NOI18N
        languageMenu.add(englishMenuItem);

        germanMenuItem.addActionListener(this::languageMenuItemActionPerformed);
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
    }

    /**
     * Action performed event for the close menu entry.
     *
     * @param evt Event object
     */
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        this.configuration.setWindowLocation(this.getLocationOnScreen());
        this.configuration.saveProperties();
        System.exit(0);
    }

    /**
     * Action performed event for the about menu entry.
     *
     * @param evt Event object
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int x,
            y,
            width = this.aboutDialog.getWidth(),
            height = this.aboutDialog.getHeight();

        x = this.configuration.getWindowLocation().x + (this.getWidth() / 2 - width / 2);
        y = this.configuration.getWindowLocation().y + (this.getHeight() / 2 - height / 2);

        this.aboutDialog.setBounds(x, y, width, height);
        this.aboutDialog.setVisible(true);
    }

    /**
     * Action performed event for the close about dialog button.
     *
     * @param evt Event object
     */
    private void closeAboutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.aboutDialog.setVisible(false);
    }

    /**
     * Action performed event for the create new character menu entry.
     *
     * @param evt Event object
     */
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
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
    }

    /**
     * Key pressed event for the about dialog to close it on pressing ESC.
     *
     * @param evt Event object
     */
    private void aboutTextPaneKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.aboutDialog.setVisible(false);
        }
    }

    /**
     * Action performed event for the save menu entry.
     *
     * @param evt Event object
     */
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        vampireEditor.entity.Character character = this.getActiveCharacter();
        this.saveFileChooser.setCurrentDirectory(this.configuration.getSaveDirPath());
        this.saveFileChooser.setSelectedFile(this.configuration.getSaveDirPath(character.getName()));
        this.saveFileChooser.setFileFilter(new ExtensionFileFilter("XML", "xml"));
        int result = this.saveFileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            VampireEditor.log(new ArrayList<>(
                Collections.singletonList("Saving character " + character.getName())
            ));
            this.configuration.setSaveDirPath(this.saveFileChooser.getSelectedFile().getParent());
            this.configuration.saveProperties();
            CharacterStorage storage = new CharacterStorage();

            storage.save(character, this.saveFileChooser.getSelectedFile().getName());
        }
    }

    /**
     * Action performed event for the open character menu entry.
     *
     * @param evt Event object
     */
    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        this.openFileChooser.setCurrentDirectory(this.configuration.getOpenDirPath());
        this.openFileChooser.setFileFilter(new ExtensionFileFilter("XML", "xml"));
        int result = this.openFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            this.configuration.setOpenDirPath(this.openFileChooser.getSelectedFile().getParent());
            this.configuration.saveProperties();
            CharacterStorage storage = new CharacterStorage();

            try {
                vampireEditor.entity.Character character = storage.load(this.openFileChooser.getSelectedFile().getName());
                this.addCharacter(character);
                VampireEditor.log(new ArrayList<>(
                    Collections.singletonList("Loaded character " + character.getName())
                ));
            } catch (Exception ex) {
                Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    this.language.translate("couldNotLoadCharacter"),
                    this.language.translate("couldNotLoad"),
                    JOptionPane.ERROR_MESSAGE
                );
                ArrayList<String> list = new ArrayList<>(
                    Collections.singletonList(ex.getMessage())
                );

                for (Throwable throwable : ex.getSuppressed()) {
                    list.add(throwable.getMessage());
                }

                VampireEditor.log(list);
            }
        }
    }

    /**
     * Action that will be performed on changing the language.
     *
     * @param evt Event object
     */
    private void languageMenuItemActionPerformed(ActionEvent evt) {
        String message = "Switching language to ";

        if (evt.getActionCommand().equals("English")) {
            this.configuration.setLanguage(Configuration.Language.ENGLISH);
            message += "English";
        } else if (evt.getActionCommand().equals("German")) {
            this.configuration.setLanguage(Configuration.Language.GERMAN);
            message += "German";
        }

        VampireEditor.log(new ArrayList<>(Collections.singletonList(message)));
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
        this.saveMenuItem.setText(this.language.translate("save"));
        this.saveMenuItem.setMnemonic(this.language.translate("saveMnemonic").charAt(0));
    }

    /**
     * Install a dialog wide escape handler.
     *
     * @param dialog The dialog element to close
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
     * @param character Character to add
     */
    public void addCharacter(vampireEditor.entity.Character character) {
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

    /**
     * Get the character of the currently selected tab.
     *
     * @return
     */
    private vampireEditor.entity.Character getActiveCharacter() {
        return ((CharacterTabbedPane) this.charactersTabPane.getSelectedComponent()).getCharacter();
    }
}
