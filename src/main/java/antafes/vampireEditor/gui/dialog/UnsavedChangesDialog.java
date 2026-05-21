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
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.dialog;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.gui.BaseWindow;
import antafes.vampireEditor.language.LanguageInterface;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dialog for confirming unsaved changes when closing a character tab or window.
 *
 * This reusable dialog allows users to:
 * - Save changes and close
 * - Discard changes and close
 * - Cancel the close operation
 *
 * @author Marian Pollzien
 */
public class UnsavedChangesDialog extends JDialog {
    @Getter
    public enum Result {
        SAVE(0),
        DISCARD(1),
        CANCEL(2);

        private final int value;

        Result(int value) {
            this.value = value;
        }
    }

    private final LanguageInterface language;
    private Result userChoice = Result.CANCEL;
    private JTextArea messageArea;
    private JButton saveButton;
    private JButton discardButton;
    private JButton cancelButton;

    /**
     * Create a new UnsavedChangesDialog.
     *
     * @param owner The parent frame that owns this dialog
     * @param characterName The name of the character with unsaved changes
     */
    public UnsavedChangesDialog(Frame owner, String characterName) {
        super(owner, true);

        this.setResizable(false);
        this.setSize(new Dimension(450, 150));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Configuration configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();

        this.initComponents();
        this.setFieldTexts(characterName);
        this.init();
    }

    /**
     * Initialize the dialog components.
     */
    private void initComponents() {
        this.messageArea = new JTextArea();
        this.saveButton = new JButton();
        this.discardButton = new JButton();
        this.cancelButton = new JButton();

        this.messageArea.setLineWrap(true);
        this.messageArea.setWrapStyleWord(true);
        this.messageArea.setEditable(false);
        this.messageArea.setOpaque(false);

        this.saveButton.setMnemonic(this.language.translate("saveMnemonic").charAt(0));
        this.discardButton.setMnemonic(this.language.translate("dontSaveMnemonic").charAt(0));
        this.cancelButton.setMnemonic(this.language.translate("cancelMnemonic").charAt(0));

        this.saveButton.addActionListener(this::saveButtonActionPerformed);
        this.discardButton.addActionListener(this::discardButtonActionPerformed);
        this.cancelButton.addActionListener(this::cancelButtonActionPerformed);

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout contentPaneLayout = new GridBagLayout();
        this.getContentPane().setLayout(contentPaneLayout);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 20;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        this.getContentPane().add(this.messageArea, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.ipady = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        this.getContentPane().add(this.saveButton, constraints);

        constraints.gridx = 1;
        this.getContentPane().add(this.discardButton, constraints);

        constraints.gridx = 2;
        this.getContentPane().add(this.cancelButton, constraints);
    }

    /**
     * Initialize the dialog (look and feel, escape key handling).
     */
    private void init() {
        // Set look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UnsavedChangesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        BaseWindow.installEscapeCloseOperation(this);
    }

    /**
     * Set the text fields with proper language translations.
     *
     * @param characterName The name of the character with unsaved changes
     */
    private void setFieldTexts(String characterName) {
        this.setTitle(this.language.translate("unsavedChangesTitle"));

        // Format the message with the character name
        String messageTemplate = this.language.translate("unsavedChangesMessage");
        String message = MessageFormat.format(messageTemplate, characterName);
        this.messageArea.setText(message);

        this.saveButton.setText(this.language.translate("save"));
        this.discardButton.setText(this.language.translate("dontSave"));
        this.cancelButton.setText(this.language.translate("cancel"));
    }

    /**
     * Handle save button action.
     *
     * @param actionEvent The action event
     */
    private void saveButtonActionPerformed(ActionEvent actionEvent) {
        this.userChoice = Result.SAVE;
        this.closeDialog();
    }

    /**
     * Handle discard button action.
     *
     * @param actionEvent The action event
     */
    private void discardButtonActionPerformed(ActionEvent actionEvent) {
        this.userChoice = Result.DISCARD;
        this.closeDialog();
    }

    /**
     * Handle cancel button action.
     *
     * @param actionEvent The action event
     */
    private void cancelButtonActionPerformed(ActionEvent actionEvent) {
        this.userChoice = Result.CANCEL;
        this.closeDialog();
    }

    /**
     * Close the dialog.
     */
    private void closeDialog() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Get the user's dialog choice.
     *
     * @return The user's choice: SAVE, DISCARD, or CANCEL
     */
    public Result getUserChoice() {
        return this.userChoice;
    }
}

