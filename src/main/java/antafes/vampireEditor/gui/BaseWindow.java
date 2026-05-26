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
package antafes.vampireEditor.gui;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.entity.Character;
import antafes.vampireEditor.entity.exception.CharacterInvalidXmlException;
import antafes.vampireEditor.entity.exception.CharacterMissingGenerationException;
import antafes.vampireEditor.entity.exception.CharacterMissingIdException;
import antafes.vampireEditor.entity.exception.CharacterValidationUnavailableException;
import antafes.vampireEditor.entity.exception.MissingClanException;
import antafes.vampireEditor.entity.exception.MissingRoadException;
import antafes.vampireEditor.entity.storage.CharacterStorage;
import antafes.vampireEditor.entity.storage.StorageFactory;
import antafes.vampireEditor.gui.character.CharacterPanelInterface;
import antafes.vampireEditor.gui.character.CharacterTabbedPane;
import antafes.vampireEditor.gui.element.CloseableTabbedPane;
import antafes.vampireEditor.gui.event.CharacterTabClosedEvent;
import antafes.vampireEditor.gui.event.CloseProgrammeEvent;
import antafes.vampireEditor.gui.event.CloseSelectedCharacterTabEvent;
import antafes.vampireEditor.gui.event.OpenCharacterEvent;
import antafes.vampireEditor.gui.event.SaveAllCharactersEvent;
import antafes.vampireEditor.gui.event.listener.CharacterTabClosedListener;
import antafes.vampireEditor.gui.event.listener.CloseProgrammeListener;
import antafes.vampireEditor.gui.event.listener.OpenCharacterListener;
import antafes.vampireEditor.gui.event.listener.SaveAllCharactersListener;
import antafes.vampireEditor.gui.exception.SaveCancelledException;
import antafes.vampireEditor.language.LanguageInterface;
import antafes.vampireEditor.print.PaperA4;
import antafes.vampireEditor.print.PrintBase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
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
    private JDialog aboutDialog;
    private JMenuItem aboutMenuItem;
    private JTextPane aboutTextPane;
    private CloseableTabbedPane charactersTabPane;
    private JButton closeAboutButton;
    private JMenuItem closeMenuItem;
    private JMenu editMenu;
    private JRadioButtonMenuItem englishMenuItem;
    private JMenu fileMenu;
    private JRadioButtonMenuItem germanMenuItem;
    private JMenu helpMenu;
    private ButtonGroup languageGroup;
    private JMenu languageMenu;
    private JMenuItem newMenuItem;
    private JMenuItem newNpcMenuItem;
    private JFileChooser openFileChooser;
    private JFileChooser saveFileChooser;
    private JMenuItem saveMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem printMenuItem;
    private JSeparator recentFilesTopSeparator;
    private JSeparator recentFilesBottomSeparator;
    private final java.util.List<JMenuItem> recentFileMenuItems = new java.util.ArrayList<>();

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
     * Initialize every component that should be shown on the panel.
     */
    private void initComponents() {

        languageGroup = new javax.swing.ButtonGroup();
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
        openFileChooser.setAcceptAllFileFilterUsed(false);
        charactersTabPane = new CloseableTabbedPane();
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        newNpcMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        undoMenuItem = new JMenuItem();
        redoMenuItem = new JMenuItem();
        printMenuItem = new JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        editMenu = new JMenu();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        languageMenu = new javax.swing.JMenu();
        englishMenuItem = new javax.swing.JRadioButtonMenuItem();
        germanMenuItem = new javax.swing.JRadioButtonMenuItem();

        this.createAboutDialog();

        saveFileChooser.setCurrentDirectory(null);

        openFileChooser.setCurrentDirectory(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(this::newMenuItemActionPerformed);
        fileMenu.add(newMenuItem);

        newNpcMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        newNpcMenuItem.setText("New NPC");
        newNpcMenuItem.setMnemonic(this.language.translate("newNpcMnemonic").charAt(0));
        newNpcMenuItem.addActionListener(this::newNpcMenuItemActionPerformed);
        fileMenu.add(newNpcMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(this::openMenuItemActionPerformed);
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(this::saveMenuItemActionPerformed);
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);

        printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        printMenuItem.setText("Print");
        printMenuItem.addActionListener(this::printMenuItemActionPerformed);
        printMenuItem.setEnabled(false);
        fileMenu.add(printMenuItem);

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        closeMenuItem.setText("Quit");
        closeMenuItem.addActionListener(this::closeMenuItemActionPerformed);
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoMenuItem.setText("Undo");
        undoMenuItem.addActionListener(this::undoMenuItemActionPerformed);
        editMenu.add(undoMenuItem);

        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoMenuItem.setText("Redo");
        redoMenuItem.addActionListener(this::redoMenuItemActionPerformed);
        editMenu.add(redoMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(this::aboutMenuItemActionPerformed);
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        languageMenu.setIcon(new ImageIcon(VampireEditor.getResourceInJar("images/english.png"))); // NOI18N

        englishMenuItem.addActionListener(this::languageMenuItemActionPerformed);
        languageGroup.add(englishMenuItem);
        englishMenuItem.setSelected(true);
        englishMenuItem.setText("English");
        englishMenuItem.setActionCommand("English");
        englishMenuItem.setIcon(new ImageIcon(VampireEditor.getResourceInJar("images/english.png"))); // NOI18N
        languageMenu.add(englishMenuItem);

        germanMenuItem.addActionListener(this::languageMenuItemActionPerformed);
        languageGroup.add(germanMenuItem);
        germanMenuItem.setText("German");
        germanMenuItem.setActionCommand("German");
        germanMenuItem.setIcon(new ImageIcon(VampireEditor.getResourceInJar("images/german.png"))); // NOI18N
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

        this.installCloseCurrentCharacterShortcut();
        pack();
    }

    private void installCloseCurrentCharacterShortcut()
    {
        KeyStroke closeCurrentCharacterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
        String actionKey = "closeCurrentCharacter";
        JRootPane rootPane = this.getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeCurrentCharacterStroke, actionKey);
        rootPane.getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeCurrentCharacterTab();
            }
        });
    }

    private void closeCurrentCharacterTab()
    {
        VampireEditor.getDispatcher().dispatch(new CloseSelectedCharacterTabEvent());
    }

    private void createAboutDialog()
    {
        aboutDialog = new javax.swing.JDialog(this, true);
        JPanel jPanel1 = new JPanel();
        closeAboutButton = new javax.swing.JButton();
        JScrollPane jScrollPane2 = new JScrollPane();
        aboutTextPane = new javax.swing.JTextPane();
        JLabel darkPackLogo = new JLabel(new ImageIcon(VampireEditor.getResourceInJar("images/darkPackLogo.png")));

        aboutDialog.setModal(true);
        aboutDialog.setResizable(false);
        aboutDialog.setSize(new Dimension(300, 400));

        closeAboutButton.setText("Close");
        closeAboutButton.addActionListener(this::closeAboutButtonActionPerformed);

        aboutTextPane.setEditable(false);
        aboutTextPane.setText("This program was created by Marian Pollzien.");
        aboutTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                aboutTextPaneKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(aboutTextPane);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(darkPackLogo, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(closeAboutButton, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(darkPackLogo)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeAboutButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        GroupLayout aboutDialogLayout = new GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    /**
     * Action performed event for the close menu entry.
     *
     * @param evt Event object
     */
    private void closeMenuItemActionPerformed(ActionEvent evt) {
        this.requestCloseProgramme();
    }

    /**
     * Returns true if one or more unsaved characters are found.
     */
    private boolean checkForUnsavedCharacters() {
        return !this.getUnsavedCharacterTabs().isEmpty();
    }

    private void showUnsavedCharactersDialog() {
        this.showUnsavedCharactersDialog(this.getUnsavedCharacterTabs());
    }

    private void showUnsavedCharactersDialog(java.util.List<CharacterTabbedPane> unsavedTabs) {
        UnsavedCharactersDialog dialog = new UnsavedCharactersDialog(this, this.getUnsavedCharacterNames(unsavedTabs));
        int x,
            y,
            width = dialog.getWidth(),
            height = dialog.getHeight();

        x = this.configuration.getWindowLocation().x + (this.getWidth() / 2 - width / 2);
        y = this.configuration.getWindowLocation().y + (this.getHeight() / 2 - height / 2);

        dialog.setBounds(x, y, width, height);
        dialog.setVisible(true);

        switch (dialog.getUserChoice()) {
            case SAVE_ALL -> {
                if (this.saveAllCharacters(unsavedTabs)) {
                    this.closeProgramme();
                }
            }
            case DISCARD_ALL -> this.closeProgramme();
            case CANCEL -> {
            }
        }
    }

    /**
     * Action performed event for the about menu entry.
     *
     * @param evt Event object
     */
    private void aboutMenuItemActionPerformed(ActionEvent evt) {
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
    private void closeAboutButtonActionPerformed(ActionEvent evt) {
        this.aboutDialog.setVisible(false);
    }

    /**
     * Action performed event for the create new character menu entry.
     *
     * @param evt Event object
     */
    private void newMenuItemActionPerformed(ActionEvent evt) {
        this.showNewCharacterDialog(false);
    }

    /**
     * Action performed event for the create new npc menu entry.
     *
     * @param evt Event object
     */
    private void newNpcMenuItemActionPerformed(ActionEvent evt) {
        this.showNewCharacterDialog(true);
    }

    private void showNewCharacterDialog(boolean npcCreation) {
        int x, y, width, height;

        // Add the new character dialog.
        NewCharacterDialog newDialog = new NewCharacterDialog(this, true, npcCreation);
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
    private void saveMenuItemActionPerformed(ActionEvent evt) {
        try {
            this.saveCurrentCharacter();
        } catch (SaveCancelledException ignored) {}
    }

    private void undoMenuItemActionPerformed(ActionEvent evt)
    {
        this.performTextEditAction("undo");
    }

    private void redoMenuItemActionPerformed(ActionEvent evt)
    {
        this.performTextEditAction("redo");
    }

    private void performTextEditAction(String actionKey)
    {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (!(focusOwner instanceof JComponent focusedComponent)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        Action action = focusedComponent.getActionMap().get(actionKey);
        if (action == null || !action.isEnabled()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        action.actionPerformed(new ActionEvent(focusedComponent, ActionEvent.ACTION_PERFORMED, actionKey));
    }

    private void saveCurrentCharacter() throws SaveCancelledException
    {
        if (this.isNoCharacterLoaded()) {
            return;
        }

        Character character = this.getActiveCharacter();
        this.saveFileChooser.setCurrentDirectory(this.configuration.getSaveDirPath());
        this.saveFileChooser.setSelectedFile(this.configuration.getSaveDirPath(character.getName()));
        this.saveFileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        int result = this.saveFileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            VampireEditor.log(String.format("Updating character %s from form fields", character.getName()));
            Character.CharacterBuilder<?, ?> characterBuilder = character.toBuilder();
            for (Component component : ((CharacterTabbedPane) this.charactersTabPane.getSelectedComponent()).getComponents()) {
                if (component instanceof CharacterPanelInterface) {
                    ((CharacterPanelInterface) component).updateCharacter(characterBuilder);
                }
            }
            character = characterBuilder.build();
            VampireEditor.log(String.format("Saving character %s", character.getName()));
            this.configuration.setSaveDirPath(this.saveFileChooser.getSelectedFile().getParent());
            this.configuration.saveProperties();
            CharacterStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.CHARACTER);

            try {
                storage.save(character, this.saveFileChooser.getSelectedFile().getName());
            } catch (Exception ex) {
                Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
                this.runOnEdt(() -> JOptionPane.showMessageDialog(
                    this,
                    this.language.translate("couldNotSaveCharacter"),
                    this.language.translate("couldNotSave"),
                    JOptionPane.ERROR_MESSAGE
                ));
                ArrayList<String> list = new ArrayList<>(Collections.singletonList(ex.getMessage()));
                for (Throwable throwable : ex.getSuppressed()) {
                    list.add(throwable.getMessage());
                }
                VampireEditor.log(list);
                return;
            }
            CharacterTabbedPane selectedTab = (CharacterTabbedPane) this.charactersTabPane.getSelectedComponent();
            selectedTab.setCharacter(character);
            for (Component component : selectedTab.getComponents()) {
                if (component instanceof CharacterPanelInterface panel) {
                    panel.setCharacter(character);
                }
            }
            selectedTab.setCharacterChanged(false);
            selectedTab.resetModificationFlag();
            this.charactersTabPane.setTitleAt(this.charactersTabPane.getSelectedIndex(), character.getName());
        }

        if (result == JFileChooser.CANCEL_OPTION) {
            throw new SaveCancelledException();
        }
    }

    /**
     * Save a specific character tab and return whether the save finished successfully.
     *
     * @param tab The tab whose character should be saved
     * @return true if the character was saved, false if save was cancelled or failed
     */
    public boolean saveCharacterTab(CharacterTabbedPane tab)
    {
        int tabIndex = this.charactersTabPane.indexOfComponent(tab);
        if (tabIndex < 0) {
            return false;
        }

        int selectedIndex = this.charactersTabPane.getSelectedIndex();
        this.charactersTabPane.setSelectedIndex(tabIndex);

        try {
            this.saveCurrentCharacter();
            return !tab.isCharacterChanged();
        } catch (SaveCancelledException ignored) {
            return false;
        } finally {
            if (selectedIndex >= 0 && selectedIndex < this.charactersTabPane.getTabCount()) {
                this.charactersTabPane.setSelectedIndex(selectedIndex);
            }
        }
    }

    private java.util.List<String> getUnsavedCharacterNames(java.util.List<CharacterTabbedPane> unsavedTabs)
    {
        java.util.List<String> characterNames = new ArrayList<>();

        for (CharacterTabbedPane unsavedTab : unsavedTabs) {
            characterNames.add(unsavedTab.getCharacter().getName());
        }

        return characterNames;
    }

    private java.util.List<CharacterTabbedPane> getUnsavedCharacterTabs()
    {
        java.util.List<CharacterTabbedPane> unsavedTabs = new ArrayList<>();

        for (int i = 0; i < this.charactersTabPane.getTabCount(); i++) {
            CharacterTabbedPane tab = (CharacterTabbedPane) this.charactersTabPane.getComponentAt(i);

            if (this.hasUnsavedChanges(tab)) {
                unsavedTabs.add(tab);
            }
        }

        return unsavedTabs;
    }

    private boolean hasUnsavedChanges(CharacterTabbedPane tab)
    {
        return tab.isCharacterChanged();
    }

    private void installWindowCloseHandler()
    {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event)
            {
                requestCloseProgramme();
            }
        });
    }

    private void requestCloseProgramme()
    {
        if (this.checkForUnsavedCharacters()) {
            this.showUnsavedCharactersDialog();
            return;
        }

        this.closeProgramme();
    }

    /**
     * Action performed event for the open character menu entry.
     *
     * @param evt Event object
     */
    private void openMenuItemActionPerformed(ActionEvent evt) {
        this.openFileChooser.setCurrentDirectory(this.configuration.getOpenDirPath());
        this.openFileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        int result = this.openFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = this.openFileChooser.getSelectedFile();
            VampireEditor.getDispatcher().dispatch(new OpenCharacterEvent(selectedFile.getAbsolutePath()));
        }
    }

    static String getCouldNotLoadCharacterMessage(LanguageInterface language, Exception ex)
    {
        String message = language.translate("couldNotLoadCharacter");
        String details = findTranslatedLoadDetailMessage(language, ex);
        if (details != null && !details.trim().isEmpty()) {
            return message + "\n" + details;
        }

        return message;
    }

    private static String findTranslatedLoadDetailMessage(LanguageInterface language, Throwable throwable)
    {
        switch (throwable) {
            case null -> {
                return null;
            }
            case MissingRoadException ignored -> {
                return language.translate("couldNotLoadCharacterMissingRoad");
            }
            case MissingClanException ignored -> {
                return language.translate("couldNotLoadCharacterMissingClan");
            }
            case FileNotFoundException ignored -> {
                return language.translate("couldNotLoadCharacterFileNotFound") + ": " + throwable.getMessage();
            }
            case CharacterInvalidXmlException ignored -> {
                return language.translate("couldNotLoadCharacterInvalidXml");
            }
            case CharacterMissingIdException ignored -> {
                return language.translate("couldNotLoadCharacterMissingId");
            }
            case CharacterMissingGenerationException ignored -> {
                return language.translate("couldNotLoadCharacterMissingGeneration");
            }
            case CharacterValidationUnavailableException ignored -> {
                return language.translate("couldNotLoadCharacterValidationUnavailable");
            }
            default -> {
            }
        }

        for (Throwable suppressed : throwable.getSuppressed()) {
            String suppressedMessage = findTranslatedLoadDetailMessage(language, suppressed);
            if (suppressedMessage != null && !suppressedMessage.trim().isEmpty()) {
                return suppressedMessage;
            }
        }

        return findTranslatedLoadDetailMessage(language, throwable.getCause());
    }

    /**
     * Action performed event for the print menu entry.
     *
     * @param evt Event object
     */
    private void printMenuItemActionPerformed(ActionEvent evt) {
        if (this.isNoCharacterLoaded()) {
            return;
        }

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        PaperA4 paper = new PaperA4();
        pageFormat.setPaper(paper);
        printerJob.setJobName(this.language.translate("printCharacter"));

        Book book = new Book();
        ArrayList<PrintBase> pages = ((CharacterTabbedPane) this.charactersTabPane.getSelectedComponent()).getPrintPages();
        for (PrintBase page: pages) {
            book.append(page, pageFormat);
        }

        printerJob.setPageable(book);

        if (!printerJob.printDialog()) {
            return;
        }

        try {
            printerJob.print();
        } catch (PrinterException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Disable the save menu item.
     */
    public void disableSaveMenuItem()
    {
        this.saveMenuItem.setEnabled(false);
    }

    /**
     * Disable the print menu item.
     */
    public void disablePrintMenuItem()
    {
        this.printMenuItem.setEnabled(false);
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
            if (!(component instanceof CharacterTabbedPane)) {
                continue;
            }

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
        this.setExtendedState(this.configuration.getExtendedState());
        this.languageMenu.setIcon(this.configuration.getLanguage().getIcon());

        if (this.configuration.getLanguage() == Configuration.Language.ENGLISH) {
            this.languageGroup.setSelected(this.englishMenuItem.getModel(), true);
        } else if (this.configuration.getLanguage() == Configuration.Language.GERMAN) {
            this.languageGroup.setSelected(this.germanMenuItem.getModel(), true);
        }

        this.installWindowCloseHandler();
        this.registerGlobalEvents();
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
        this.editMenu.setText(this.language.translate("edit"));
        this.editMenu.setMnemonic(this.language.translate("editMnemonic").charAt(0));
        this.undoMenuItem.setText(this.language.translate("undo"));
        this.undoMenuItem.setMnemonic(this.language.translate("undoMnemonic").charAt(0));
        this.redoMenuItem.setText(this.language.translate("redo"));
        this.redoMenuItem.setMnemonic(this.language.translate("redoMnemonic").charAt(0));
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
        this.newNpcMenuItem.setText(this.language.translate("newNpc"));
        this.newNpcMenuItem.setMnemonic(this.language.translate("newNpcMnemonic").charAt(0));
        this.openMenuItem.setText(this.language.translate("open"));
        this.openMenuItem.setMnemonic(this.language.translate("openMnemonic").charAt(0));
        this.saveMenuItem.setText(this.language.translate("save"));
        this.saveMenuItem.setMnemonic(this.language.translate("saveMnemonic").charAt(0));
        this.printMenuItem.setText(this.language.translate("print"));
        this.printMenuItem.setMnemonic(this.language.translate("printMnemonic").charAt(0));
        this.refreshRecentFilesMenu();
    }

    /**
     * Rebuild the recent files section in the File menu.
     * The section is placed between the Print and Quit menu items.
     * When the list is empty the section (including separators) is hidden.
     */
    private void refreshRecentFilesMenu() {
        // Remove previously added dynamic items and separators
        if (recentFilesTopSeparator != null) {
            fileMenu.remove(recentFilesTopSeparator);
        }
        for (JMenuItem item : recentFileMenuItems) {
            fileMenu.remove(item);
        }
        if (recentFilesBottomSeparator != null) {
            fileMenu.remove(recentFilesBottomSeparator);
        }
        recentFileMenuItems.clear();

        java.util.List<Configuration.RecentFileEntry> recentFiles = this.configuration.getRecentFiles();
        if (recentFiles.isEmpty()) {
            return;
        }

        // Determine the index of the Quit menu item so we can insert before it
        int quitIndex = -1;
        for (int i = 0; i < fileMenu.getPopupMenu().getComponentCount(); i++) {
            if (fileMenu.getPopupMenu().getComponent(i) == closeMenuItem) {
                quitIndex = i;
                break;
            }
        }
        if (quitIndex == -1) {
            quitIndex = fileMenu.getPopupMenu().getComponentCount();
        }

        // Build label map to detect duplicate character names (show parent folder then)
        java.util.Map<String, Long> nameCount = new java.util.HashMap<>();
        for (Configuration.RecentFileEntry entry : recentFiles) {
            String name = entry.getCharacterName().isEmpty() ? new File(entry.getPath()).getName() : entry.getCharacterName();
            nameCount.merge(name, 1L, Long::sum);
        }

        recentFilesTopSeparator = new JSeparator();
        fileMenu.getPopupMenu().insert(recentFilesTopSeparator, quitIndex);

        for (int i = 0; i < recentFiles.size(); i++) {
            Configuration.RecentFileEntry entry = recentFiles.get(i);
            String displayName = entry.getCharacterName().isEmpty()
                ? new File(entry.getPath()).getName()
                : entry.getCharacterName();

            if (nameCount.getOrDefault(displayName, 0L) > 1) {
                File entryFile = new File(entry.getPath());
                String parent = entryFile.getParent();
                String disambiguator = (parent == null || parent.trim().isEmpty())
                    ? entry.getPath()
                    : parent;
                displayName += " (" + disambiguator + ")";
            }

            JMenuItem item = new JMenuItem((i + 1) + "  " + displayName);
            item.setToolTipText(entry.getPath());
            final String filePath = entry.getPath();
            item.addActionListener(e -> VampireEditor.getDispatcher().dispatch(new OpenCharacterEvent(filePath)));
            recentFileMenuItems.add(item);
            fileMenu.insert(item, quitIndex + 1 + i);
        }

        recentFilesBottomSeparator = new JSeparator();
        fileMenu.getPopupMenu().insert(recentFilesBottomSeparator, quitIndex + 1 + recentFiles.size());
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
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    /**
     * Add a new character to the tabbed panel.
     *
     * @param character Character to add
     */
    public void addCharacter(Character character, boolean isCharacterChanged) {
        try {
            CharacterTabbedPane characterTabbedPane = new CharacterTabbedPane();
            characterTabbedPane.setCharacter(character);
            characterTabbedPane.init();
            characterTabbedPane.setCharacterChanged(isCharacterChanged);
            String tabName = character.getName();

            if (isCharacterChanged) {
                tabName += "*";
            }

            this.charactersTabPane.add(tabName, characterTabbedPane);
            this.charactersTabPane.setSelectedIndex(this.charactersTabPane.indexOfComponent(characterTabbedPane));
            this.printMenuItem.setEnabled(true);
            this.saveMenuItem.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCharacter(Character character) {
        this.addCharacter(character, false);
    }

    /**
     * Get the character of the currently selected tab.
     */
    private antafes.vampireEditor.entity.Character getActiveCharacter() {
        return ((CharacterTabbedPane) this.charactersTabPane.getSelectedComponent()).getCharacter();
    }

    /**
     * Check if a character already has been loaded.
     */
    public boolean isNoCharacterLoaded() {
        try {
            ((CharacterTabbedPane) this.charactersTabPane.getSelectedComponent()).getCharacter();
            return false;
        } catch (NullPointerException ex) {
            return true;
        }
    }

    /**
     * Check if a character already has been loaded.
     *
     * @return Returns the position of the character tab if found, otherwise -1
     */
    private int isCharacterLoaded(Character character) {
        for (int i = 0; i < this.charactersTabPane.getTabCount(); i++) {
            CharacterTabbedPane pane = (CharacterTabbedPane) this.charactersTabPane.getComponentAt(i);

            if (character.getId().equals(pane.getCharacter().getId())) {
                return i;
            }
        }

        return -1;
    }

    private void registerGlobalEvents()
    {
        VampireEditor.getDispatcher().addListener(
            CloseProgrammeEvent.class,
            new CloseProgrammeListener((event) -> this.closeProgramme())
        );
        VampireEditor.getDispatcher().addListener(
            SaveAllCharactersEvent.class,
            new SaveAllCharactersListener((event) -> this.saveAllCharacters())
        );
        VampireEditor.getDispatcher().addListener(
            OpenCharacterEvent.class,
            new OpenCharacterListener((event) -> this.openCharacter(event.getFilePath()))
        );
        VampireEditor.getDispatcher().addListener(
            CharacterTabClosedEvent.class,
            new CharacterTabClosedListener((event) -> this.handleCharacterTabClosed())
        );
    }

    private void handleCharacterTabClosed()
    {
        if (this.isNoCharacterLoaded()) {
            this.disablePrintMenuItem();
            this.disableSaveMenuItem();
        }
    }

    /**
     * Handle an OpenCharacterEvent by loading the character file, updating the MRU list
     * and refreshing the recent-files menu.
     *
     * @param filePath Absolute path to the character XML file
     */
    private void openCharacter(String filePath)
    {
        if (filePath == null || filePath.trim().isEmpty()) {
            return;
        }

        File file = new File(filePath);

        ShowWaitAction waitAction = new ShowWaitAction(this);
        waitAction.show(aVoid -> {
            CharacterStorage storage = StorageFactory.getStorage(StorageFactory.StorageType.CHARACTER);

            try {
                if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".xml")) {
                    throw new java.io.FileNotFoundException(filePath);
                }

                if (!file.exists() || !file.isFile() || !file.canRead()) {
                    throw new java.io.FileNotFoundException(filePath);
                }

                File parentDir = file.getAbsoluteFile().getParentFile();
                if (parentDir == null) {
                    throw new java.io.FileNotFoundException(filePath);
                }

                this.configuration.setOpenDirPath(parentDir.getPath());
                Character character = storage.load(file.getName());
                this.configuration.addRecentFile(filePath, character.getName());
                this.configuration.saveProperties();
                this.runOnEdtAndWait(() -> {
                    this.refreshRecentFilesMenu();

                    int characterTab = this.isCharacterLoaded(character);
                    if (characterTab != -1) {
                        this.charactersTabPane.setSelectedIndex(characterTab);
                        VampireEditor.log("Character was already open, switched to tab.");
                        return;
                    }

                    this.addCharacter(character);
                    this.printMenuItem.setEnabled(true);
                    this.saveMenuItem.setEnabled(true);
                    VampireEditor.log("Loaded character " + character.getName());
                });
            } catch (Exception ex) {
                Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
                this.configuration.removeRecentFile(filePath);
                this.configuration.saveProperties();
                this.runOnEdt(() -> {
                    JOptionPane.showMessageDialog(
                        this,
                        getCouldNotLoadCharacterMessage(this.language, ex),
                        this.language.translate("couldNotLoad"),
                        JOptionPane.ERROR_MESSAGE
                    );
                    this.refreshRecentFilesMenu();
                });

                ArrayList<String> list = new ArrayList<>(
                    Collections.singletonList(ex.getMessage())
                );
                for (Throwable throwable : ex.getSuppressed()) {
                    list.add(throwable.getMessage());
                }
                VampireEditor.log(list);
            }

            return null;
        });
    }

    private void runOnEdtAndWait(Runnable runnable)
    {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
            return;
        }

        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    private void runOnEdt(Runnable runnable)
    {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
            return;
        }

        SwingUtilities.invokeLater(runnable);
    }

    private void closeProgramme()
    {
        this.configuration.setWindowLocation(this.getLocationOnScreen());
        this.configuration.setExtendedState(this.getExtendedState());
        this.configuration.saveProperties();
        System.exit(0);
    }

    private void saveAllCharacters()
    {
        if (this.saveAllCharacters(this.getUnsavedCharacterTabs())) {
            this.closeProgramme();
        }
    }

    private boolean saveAllCharacters(java.util.List<CharacterTabbedPane> tabs)
    {
        int selectedIndex = this.charactersTabPane.getSelectedIndex();

        for (CharacterTabbedPane tab : tabs) {
            if (!this.hasUnsavedChanges(tab)) {
                continue;
            }

            if (!this.saveCharacterTab(tab)) {
                int failedTabIndex = this.charactersTabPane.indexOfComponent(tab);
                if (failedTabIndex >= 0) {
                    this.charactersTabPane.setSelectedIndex(failedTabIndex);
                }
                return false;
            }
        }

        if (selectedIndex >= 0 && selectedIndex < this.charactersTabPane.getTabCount()) {
            this.charactersTabPane.setSelectedIndex(selectedIndex);
        }

        return true;
    }
}

