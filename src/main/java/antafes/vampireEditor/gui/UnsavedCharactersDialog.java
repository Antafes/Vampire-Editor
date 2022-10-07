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
import antafes.vampireEditor.VampireEditor;
import antafes.vampireEditor.gui.event.CloseProgrammeEvent;
import antafes.vampireEditor.gui.event.SaveAllCharactersEvent;
import antafes.vampireEditor.language.LanguageInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnsavedCharactersDialog extends JDialog
{
    private final LanguageInterface language;
    private JTextArea infoTextArea;
    private JButton saveButton;
    private JButton exitButton;
    private JButton cancelButton;

    public UnsavedCharactersDialog(Frame owner)
    {
        super(owner, true);

        this.setResizable(false);
        this.setSize(new Dimension(400, 150));

        Configuration configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();

        this.initComponents();
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
        this.setFieldTexts();
    }

    private void setFieldTexts()
    {
        this.setTitle(this.language.translate("unsavedCharactersTitle"));
        this.infoTextArea.setText(this.language.translate("unsavedCharacters"));
        this.saveButton.setText(this.language.translate("save"));
        this.exitButton.setText(this.language.translate("dontSave"));
        this.cancelButton.setText(this.language.translate("cancel"));
    }

    private void cancelButtonActionPerformed(ActionEvent actionEvent)
    {
        this.closeDialog();
    }

    private void exitButtonActionPerformed(ActionEvent actionEvent)
    {
        VampireEditor.getDispatcher().dispatch(new CloseProgrammeEvent());
    }

    private void saveButtonActionPerformed(ActionEvent actionEvent)
    {
        this.closeDialog();
        VampireEditor.getDispatcher().dispatch(new SaveAllCharactersEvent());
    }

    private void closeDialog()
    {
        this.setVisible(false);
        this.dispose();
    }
}
