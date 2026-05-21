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
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.language.LanguageInterface;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnsavedCharactersDialog extends JDialog
{
    @Getter
    public enum Result {
        SAVE_ALL,
        DISCARD_ALL,
        CANCEL
    }

    private final LanguageInterface language;
    @Getter
    private Result userChoice = Result.CANCEL;
    private JTextArea infoTextArea;
    private JButton saveButton;
    private JButton exitButton;
    private JButton cancelButton;

    public UnsavedCharactersDialog(Frame owner, List<String> characterNames)
    {
        super(owner, true);

        this.setResizable(false);
        this.setSize(new Dimension(450, 220));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Configuration configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();

        this.initComponents();
        this.setFieldTexts(characterNames);
        this.init();
    }

    private void initComponents()
    {
        this.infoTextArea = new JTextArea();
        this.saveButton = new JButton();
        this.exitButton = new JButton();
        this.cancelButton = new JButton();

        this.infoTextArea.setLineWrap(true);
        this.infoTextArea.setWrapStyleWord(true);
        this.infoTextArea.setEditable(false);
        this.infoTextArea.setOpaque(false);

        this.saveButton.setMnemonic(this.language.translate("saveMnemonic").charAt(0));
        this.exitButton.setMnemonic(this.language.translate("dontSaveMnemonic").charAt(0));
        this.cancelButton.setMnemonic(this.language.translate("cancelMnemonic").charAt(0));

        this.cancelButton.addActionListener(this::cancelButtonActionPerformed);
        this.exitButton.addActionListener(this::exitButtonActionPerformed);
        this.saveButton.addActionListener(this::saveButtonActionPerformed);

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout contentPaneLayout = new GridBagLayout();
        this.getContentPane().setLayout(contentPaneLayout);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 20;
        constraints.fill = GridBagConstraints.BOTH;
        this.getContentPane().add(this.infoTextArea, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.ipady = 0;
        this.getContentPane().add(this.saveButton, constraints);

        constraints.gridx = 1;
        this.getContentPane().add(this.exitButton, constraints);

        constraints.gridx = 2;
        this.getContentPane().add(this.cancelButton, constraints);
    }

    private void init() {
        // Set look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(NewCharacterDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        BaseWindow.installEscapeCloseOperation(this);
    }

    private void setFieldTexts(List<String> characterNames)
    {
        this.setTitle(this.language.translate("unsavedCharactersTitle"));
        this.infoTextArea.setText(this.buildInfoMessage(characterNames));
        this.saveButton.setText(this.language.translate("saveAllButton"));
        this.exitButton.setText(this.language.translate("discardAllButton"));
        this.cancelButton.setText(this.language.translate("cancel"));
    }

    private void cancelButtonActionPerformed(ActionEvent actionEvent)
    {
        this.userChoice = Result.CANCEL;
        this.closeDialog();
    }

    private void exitButtonActionPerformed(ActionEvent actionEvent)
    {
        this.userChoice = Result.DISCARD_ALL;
        this.closeDialog();
    }

    private void saveButtonActionPerformed(ActionEvent actionEvent)
    {
        this.userChoice = Result.SAVE_ALL;
        this.closeDialog();
    }

    private void closeDialog()
    {
        this.setVisible(false);
        this.dispose();
    }

    private String buildInfoMessage(List<String> characterNames)
    {
        StringBuilder builder = new StringBuilder(this.language.translate("unsavedChangesMultipleMessage"));

        if (characterNames == null || characterNames.isEmpty()) {
            return builder.toString();
        }

        builder.append("\n\n");
        builder.append(this.language.translate("multipleCharactersList"));

        for (String characterName : characterNames) {
            builder.append("\n- ");
            builder.append(characterName);
        }

        return builder.toString();
    }
}
