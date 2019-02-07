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
package vampireEditor.gui.newCharacter;

import vampireEditor.Configuration;
import vampireEditor.VampireEditor;
import vampireEditor.entity.character.Clan;
import vampireEditor.entity.character.Generation;
import vampireEditor.gui.ComponentDocumentListener;
import vampireEditor.gui.NewCharacterDialog;
import vampireEditor.gui.NewCharacterFocusTraversalPolicy;
import vampireEditor.language.LanguageInterface;
import vampireEditor.utility.ClanComparator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.DateFormatter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 *
 * @author Marian Pollzien
 */
public class LooksPanel extends javax.swing.JPanel {
    private final LanguageInterface language;
    private final HashMap<Component, Boolean> enteredFields;
    private final NewCharacterDialog parent;
    private final DateTimeFormatter dateTimeFormatter;
    private final LocalDate date;

    /**
     * Creates new form looksPanel
     *
     * @param parent Parent element
     */
    public LooksPanel(NewCharacterDialog parent) {
        super();
        this.parent = parent;
        Configuration configuration = Configuration.getInstance();
        this.enteredFields = new HashMap<>();
        this.language = configuration.getLanguageObject();

        this.date = LocalDate.of(1200, Month.JANUARY, 1);
        this.dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
    }

    /**
     * Init everything.
     */
    public void init() {
        this.initComponents();
        this.setFieldTexts();
        this.createFocusTraversalPolicy();
    }

    /**
     * Initialize every component that should be shown on the panel.
     */
    private void initComponents() {
        sireField = new javax.swing.JTextField();
        sizeLabel = new javax.swing.JLabel();
        clanLabel = new javax.swing.JLabel();
        weightField = new javax.swing.JTextField();
        clanComboBox = new javax.swing.JComboBox<>();
        sexField = new javax.swing.JComboBox<>();
        sectLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        sectField = new javax.swing.JTextField();
        backButton = new javax.swing.JButton();
        dayOfDeathLabel = new javax.swing.JLabel();
        requiredLabel = new javax.swing.JLabel();
        sizeField = new javax.swing.JTextField();
        weightLabel = new javax.swing.JLabel();
        dayOfBirthField = new vampireEditor.gui.PlaceholderFormattedTextField();
        dayOfDeathField = new vampireEditor.gui.PlaceholderFormattedTextField();
        hairColorLabel = new javax.swing.JLabel();
        hairColorField = new javax.swing.JTextField();
        generationComboBox = new javax.swing.JComboBox<>();
        sexLabel = new javax.swing.JLabel();
        natureLabel = new javax.swing.JLabel();
        eyeColorLabel = new javax.swing.JLabel();
        natureField = new javax.swing.JTextField();
        eyeColorField = new javax.swing.JTextField();
        hideoutLabel = new javax.swing.JLabel();
        skinColorLabel = new javax.swing.JLabel();
        hideoutField = new javax.swing.JTextField();
        skinColorField = new javax.swing.JTextField();
        playerLabel = new javax.swing.JLabel();
        nationalityField = new javax.swing.JTextField();
        playerField = new javax.swing.JTextField();
        ageField = new javax.swing.JTextField();
        demeanorLabel = new javax.swing.JLabel();
        ageLabel = new javax.swing.JLabel();
        demeanorField = new javax.swing.JTextField();
        conceptField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        chronicleLabel = new javax.swing.JLabel();
        chronicleField = new javax.swing.JTextField();
        generationLabel = new javax.swing.JLabel();
        looksLikeAgeLabel = new javax.swing.JLabel();
        looksLikeAgeField = new javax.swing.JTextField();
        conceptLabel = new javax.swing.JLabel();
        dayOfBirthLabel = new javax.swing.JLabel();
        sireLabel = new javax.swing.JLabel();
        nationalityLabel = new javax.swing.JLabel();

        sireField.setName("maker"); // NOI18N

        sizeLabel.setLabelFor(sizeField);
        sizeLabel.setText("Size");

        clanLabel.setLabelFor(clanComboBox);
        clanLabel.setText("Clan*");

        weightField.setName("weight"); // NOI18N

        this.enteredFields.put(clanComboBox, Boolean.FALSE);
        clanComboBox.setModel(this.getClans());
        clanComboBox.setName("clan"); // NOI18N
        clanComboBox.addActionListener(this::clanComboBoxActionPerformed);

        sexField.setModel(this.getSexes());
        sexField.setName("sex"); // NOI18N

        sectLabel.setLabelFor(sectField);
        sectLabel.setText("Sect");

        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(this::nextButtonActionPerformed);

        sectField.setName("sect"); // NOI18N

        backButton.setText("Back");
        backButton.setEnabled(false);

        dayOfDeathLabel.setLabelFor(dayOfDeathField);
        dayOfDeathLabel.setText("Day of death");

        requiredLabel.setText("Required: *");

        sizeField.setName("size"); // NOI18N

        weightLabel.setLabelFor(weightField);
        weightLabel.setText("Weight");

        DateFormatter dateFormatter = new DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM));
        dayOfBirthField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(dateFormatter));
        dayOfBirthField.setPlaceholder(this.date.format(this.dateTimeFormatter));
        dayOfBirthField.setName("dayOfBirth"); // NOI18N

        dateFormatter = new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM));
        dayOfDeathField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(dateFormatter));
        dayOfDeathField.setPlaceholder(this.date.format(this.dateTimeFormatter));
        dayOfDeathField.setName("dayOfDeath"); // NOI18N

        hairColorLabel.setLabelFor(hairColorField);
        hairColorLabel.setText("Hair color");

        hairColorField.setName("hairColor"); // NOI18N

        this.enteredFields.put(generationComboBox, Boolean.FALSE);
        generationComboBox.setModel(this.getGenerations());
        generationComboBox.setName("generation"); // NOI18N
        generationComboBox.addActionListener(this::generationComboBoxActionPerformed);

        sexLabel.setLabelFor(sexField);
        sexLabel.setText("Sex");

        natureLabel.setLabelFor(natureField);
        natureLabel.setText("Nature*");

        eyeColorLabel.setLabelFor(eyeColorField);
        eyeColorLabel.setText("Eye color");

        this.enteredFields.put(natureField, Boolean.FALSE);
        ComponentDocumentListener documentListener = this.createDocumentListener();
        documentListener.setComponent(natureField);
        natureField.getDocument().addDocumentListener(documentListener);
        natureField.setName("nature"); // NOI18N

        eyeColorField.setName("eyeColor"); // NOI18N

        hideoutLabel.setLabelFor(hideoutField);
        hideoutLabel.setText("Hideout");

        skinColorLabel.setLabelFor(skinColorField);
        skinColorLabel.setText("Skin color");

        hideoutField.setName("hideout"); // NOI18N

        skinColorField.setName("skinColor"); // NOI18N

        playerLabel.setLabelFor(playerField);
        playerLabel.setText("Player");

        nationalityField.setName("nationality"); // NOI18N

        playerField.setName("player"); // NOI18N

        ageField.setName("age"); // NOI18N

        demeanorLabel.setLabelFor(demeanorField);
        demeanorLabel.setText("Demeanor*");

        ageLabel.setLabelFor(ageField);
        ageLabel.setText("Age");

        this.enteredFields.put(demeanorField, Boolean.FALSE);
        documentListener = this.createDocumentListener();
        documentListener.setComponent(demeanorField);
        demeanorField.getDocument().addDocumentListener(documentListener);
        demeanorField.setName("demeanor"); // NOI18N

        this.enteredFields.put(conceptField, Boolean.FALSE);
        documentListener = this.createDocumentListener();
        documentListener.setComponent(conceptField);
        conceptField.getDocument().addDocumentListener(documentListener);
        conceptField.setName("concept"); // NOI18N

        this.enteredFields.put(nameField, Boolean.FALSE);
        documentListener = this.createDocumentListener();
        documentListener.setComponent(nameField);
        nameField.getDocument().addDocumentListener(documentListener);
        nameField.setName("name"); // NOI18N

        nameLabel.setLabelFor(nameField);
        nameLabel.setText("Name*");

        chronicleLabel.setLabelFor(chronicleField);
        chronicleLabel.setText("Chronicle");

        chronicleField.setName("chronicle"); // NOI18N

        generationLabel.setLabelFor(generationComboBox);
        generationLabel.setText("Generation*");

        looksLikeAgeLabel.setLabelFor(looksLikeAgeField);
        looksLikeAgeLabel.setText("Looks like age");

        looksLikeAgeField.setName("looksLikeAge"); // NOI18N

        conceptLabel.setLabelFor(conceptField);
        conceptLabel.setText("Concept*");

        dayOfBirthLabel.setLabelFor(dayOfBirthField);
        dayOfBirthLabel.setText("Day of birth");

        sireLabel.setLabelFor(sireField);
        sireLabel.setText("Sire");

        nationalityLabel.setLabelFor(nationalityField);
        nationalityLabel.setText("Nationality");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chronicleLabel)
                    .addComponent(nameLabel)
                    .addComponent(generationLabel)
                    .addComponent(natureLabel)
                    .addComponent(hideoutLabel)
                    .addComponent(playerLabel)
                    .addComponent(demeanorLabel)
                    .addComponent(conceptLabel)
                    .addComponent(sireLabel)
                    .addComponent(clanLabel)
                    .addComponent(sectLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hideoutField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(demeanorField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conceptField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sireField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sectField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(natureField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chronicleField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 53, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(looksLikeAgeLabel)
                    .addComponent(ageLabel)
                    .addComponent(dayOfBirthLabel)
                    .addComponent(dayOfDeathLabel)
                    .addComponent(hairColorLabel)
                    .addComponent(eyeColorLabel)
                    .addComponent(skinColorLabel)
                    .addComponent(nationalityLabel)
                    .addComponent(sizeLabel)
                    .addComponent(weightLabel)
                    .addComponent(sexLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hairColorField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(eyeColorField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(skinColorField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(nationalityField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(sizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(looksLikeAgeField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(weightField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(ageField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(sexField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dayOfBirthField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dayOfDeathField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(requiredLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ageLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(looksLikeAgeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(looksLikeAgeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dayOfBirthLabel)
                            .addComponent(dayOfBirthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dayOfDeathLabel)
                            .addComponent(dayOfDeathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hairColorLabel)
                            .addComponent(hairColorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eyeColorLabel)
                            .addComponent(eyeColorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(skinColorLabel)
                            .addComponent(skinColorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nationalityLabel)
                            .addComponent(nationalityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sizeLabel)
                            .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weightLabel)
                            .addComponent(weightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sexLabel)
                            .addComponent(sexField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chronicleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chronicleLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(generationLabel)
                            .addComponent(generationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(natureLabel)
                            .addComponent(natureField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hideoutLabel)
                            .addComponent(hideoutField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playerLabel)
                            .addComponent(playerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(demeanorLabel)
                            .addComponent(demeanorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(conceptLabel)
                            .addComponent(conceptField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sireLabel)
                            .addComponent(sireField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clanLabel)
                            .addComponent(clanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sectLabel)
                            .addComponent(sectField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(backButton)
                    .addComponent(requiredLabel))
                .addGap(11, 11, 11))
        );
    }

    /**
     * Action performed event for the clan combo box.
     *
     * @param evt Event object
     */
    private void clanComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (Objects.equals(this.clanComboBox.getSelectedItem(), "")) {
            this.enteredFields.replace(this.clanComboBox, Boolean.FALSE);
        } else {
            this.enteredFields.replace(this.clanComboBox, Boolean.TRUE);
            this.checkEnteredFields();
            Clan clan = (Clan) ((JComboBox) evt.getSource()).getSelectedItem();
            this.parent.setClanDisciplins(clan);
            this.parent.adjustAttributesToClan(clan);
        }
    }

    /**
     * Action performed event for the next button.
     *
     * @param evt Event object
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JTabbedPane pane = this.parent.getCharacterTabPane();
        pane.setSelectedIndex(pane.getSelectedIndex() + 1);
    }

    /**
     * Action performed event for the generation combo box.
     *
     * @param evt Event object
     */
    private void generationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (Objects.equals(this.generationComboBox.getSelectedItem(), "")) {
            this.enteredFields.replace(this.generationComboBox, Boolean.FALSE);
        } else {
            this.enteredFields.replace(this.generationComboBox, Boolean.TRUE);
            this.checkEnteredFields();
        }

        int maximumAttributes = ((Generation) this.generationComboBox.getSelectedItem()).getMaximumAttributes();
        this.parent.setAttributeMaximum(maximumAttributes);
        this.parent.calculateUsedFreeAdditionalPoints();
    }

    /**
     * Set the translated texts for the fields and labels of the looks tab.
     */
    private void setFieldTexts() {
        this.nameLabel.setText(this.language.translate("name") + "*");
        this.chronicleLabel.setText(this.language.translate("chronicle"));
        this.generationLabel.setText(this.language.translate("generation") + "*");
        this.natureLabel.setText(this.language.translate("nature") + "*");
        this.hideoutLabel.setText(this.language.translate("hideout"));
        this.playerLabel.setText(this.language.translate("player"));
        this.demeanorLabel.setText(this.language.translate("demeanor") + "*");
        this.conceptLabel.setText(this.language.translate("concept") + "*");
        this.sireLabel.setText(this.language.translate("sire"));
        this.clanLabel.setText(this.language.translate("clan") + "*");
        this.sectLabel.setText(this.language.translate("sect"));

        this.ageLabel.setText(this.language.translate("age"));
        this.looksLikeAgeLabel.setText(this.language.translate("looksLikeAge"));
        this.dayOfBirthLabel.setText(this.language.translate("dayOfBirth"));
        this.dayOfDeathLabel.setText(this.language.translate("dayOfDeath"));
        this.hairColorLabel.setText(this.language.translate("hairColor"));
        this.eyeColorLabel.setText(this.language.translate("eyeColor"));
        this.skinColorLabel.setText(this.language.translate("skinColor"));
        this.nationalityLabel.setText(this.language.translate("nationality"));
        this.sizeLabel.setText(this.language.translate("size"));
        this.weightLabel.setText(this.language.translate("weight"));
        this.sexLabel.setText(this.language.translate("sex"));

        this.requiredLabel.setText(this.language.translate("required"));
        this.nextButton.setText(this.language.translate("next"));
        this.backButton.setText(this.language.translate("back"));
    }

    /**
     * Create the looks document listener.
     *
     * @return Component document listener for this panel
     */
    private ComponentDocumentListener createDocumentListener() {
        return new ComponentDocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                if (this.getComponent() instanceof JTextComponent
                    && ((JTextComponent) this.getComponent()).getText().equals("")
                ) {
                    enteredFields.replace(this.getComponent(), Boolean.FALSE);
                } else if (this.getComponent() instanceof JComboBox
                    && Objects.equals(((JComboBox) this.getComponent()).getSelectedItem(), "")
                ) {
                    enteredFields.replace(this.getComponent(), Boolean.FALSE);
                } else {
                    enteredFields.replace(this.getComponent(), Boolean.TRUE);
                    checkEnteredFields();
                }
            }
        };
    }

    /**
     * Get the generations for showing them in the form.
     *
     * @return
     */
    public DefaultComboBoxModel getGenerations() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");

        VampireEditor.getGenerations().forEach(model::addElement);

        return model;
    }

    /**
     * Get the sexes for showing them in the form.
     *
     * @return
     */
    public DefaultComboBoxModel getSexes() {
        return new DefaultComboBoxModel(vampireEditor.entity.Character.Sex.values());
    }

    /**
     * Get the generations for showing them in the form.
     *
     * @return
     */
    public DefaultComboBoxModel getClans() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");
        ArrayList<Clan> sortedClans = new ArrayList<>(VampireEditor.getClans().values());
        sortedClans.sort(new ClanComparator());

        sortedClans.forEach(model::addElement);

        return model;
    }

    /**
     * Check if the next tab can be activated.
     */
    private void checkEnteredFields() {
            if (!this.enteredFields.containsValue(Boolean.FALSE) && this.parent.getMaxActiveTab() < 1) {
            this.parent.increaseMaxActiveTab();
            this.parent.getCharacterTabPane().setEnabledAt(this.parent.getMaxActiveTab(), true);
            this.nextButton.setEnabled(true);
        }
    }

    /**
     * Create the focus traversal policy for the looks tab.
     */
    private void createFocusTraversalPolicy() {
        Vector<Component> order = new Vector<>();
        order.add(this.nameField);
        order.add(this.chronicleField);
        order.add(this.generationComboBox);
        order.add(this.natureField);
        order.add(this.hideoutField);
        order.add(this.playerField);
        order.add(this.demeanorField);
        order.add(this.conceptField);
        order.add(this.sireField);
        order.add(this.clanComboBox);
        order.add(this.sectField);
        order.add(this.ageField);
        order.add(this.looksLikeAgeField);
        order.add(this.dayOfBirthField);
        order.add(this.dayOfDeathField);
        order.add(this.hairColorField);
        order.add(this.eyeColorField);
        order.add(this.skinColorField);
        order.add(this.nationalityField);
        order.add(this.sizeField);
        order.add(this.weightField);
        order.add(this.sexField);
        order.add(this.nextButton);
        this.setFocusTraversalPolicy(new NewCharacterFocusTraversalPolicy(order));
        this.setFocusTraversalPolicyProvider(true);
    }

    /**
     * This method checks every input made by the user for duplicate entries.
     * This method is only implemented to achive consistency for every panel.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    public boolean checkAllFields() {
        return false;
    }

    /**
     * Get a list with all field values.
     *
     * @param builder Character builder object
     */
    public void fillCharacter(vampireEditor.entity.Character.Builder builder) {
        builder.setName(this.nameField.getText());
        builder.setChronicle(this.chronicleField.getText());
        builder.setGeneration((Generation) this.generationComboBox.getSelectedItem());
        builder.setNature(this.natureField.getText());
        builder.setHideout(this.hideoutField.getText());
        builder.setPlayer(this.playerField.getText());
        builder.setDemeanor(this.demeanorField.getText());
        builder.setConcept(this.conceptField.getText());
        builder.setSire(this.sireField.getText());
        builder.setClan((Clan) this.clanComboBox.getSelectedItem());
        builder.setSect(this.sectField.getText());
        builder.setAge(!"".equals(this.ageField.getText()) ? Integer.parseInt(this.ageField.getText()) : 0);
        builder.setLooksLikeAge(!"".equals(this.looksLikeAgeField.getText()) ? Integer.parseInt(this.looksLikeAgeField.getText()) : 0);
        builder.setDayOfBirth(!"".equals(this.dayOfBirthField.getText()) ? (Date) this.dayOfBirthField.getValue() : null);
        builder.setDayOfDeath(!"".equals(this.dayOfDeathField.getText()) ? (Date) this.dayOfDeathField.getValue() : null);
        builder.setHairColor(this.hairColorField.getText());
        builder.setEyeColor(this.eyeColorField.getText());
        builder.setSkinColor(this.skinColorField.getText());
        builder.setNationality(this.nationalityField.getText());
        builder.setSize(!this.sizeField.getText().equals("") ? Integer.parseInt(this.sizeField.getText()) : 0);
        builder.setWeight(!this.weightField.getText().equals("") ? Integer.parseInt(this.weightField.getText()) : 0);
        builder.setSex((vampireEditor.entity.Character.Sex) this.sexField.getSelectedItem());
    }

// <editor-fold defaultstate="collapsed" desc="Generated variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ageField;
    private javax.swing.JLabel ageLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JTextField chronicleField;
    private javax.swing.JLabel chronicleLabel;
    private javax.swing.JComboBox<String> clanComboBox;
    private javax.swing.JLabel clanLabel;
    private javax.swing.JTextField conceptField;
    private javax.swing.JLabel conceptLabel;
    private vampireEditor.gui.PlaceholderFormattedTextField dayOfBirthField;
    private javax.swing.JLabel dayOfBirthLabel;
    private vampireEditor.gui.PlaceholderFormattedTextField dayOfDeathField;
    private javax.swing.JLabel dayOfDeathLabel;
    private javax.swing.JTextField demeanorField;
    private javax.swing.JLabel demeanorLabel;
    private javax.swing.JTextField eyeColorField;
    private javax.swing.JLabel eyeColorLabel;
    private javax.swing.JComboBox<String> generationComboBox;
    private javax.swing.JLabel generationLabel;
    private javax.swing.JTextField hairColorField;
    private javax.swing.JLabel hairColorLabel;
    private javax.swing.JTextField hideoutField;
    private javax.swing.JLabel hideoutLabel;
    private javax.swing.JTextField looksLikeAgeField;
    private javax.swing.JLabel looksLikeAgeLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nationalityField;
    private javax.swing.JLabel nationalityLabel;
    private javax.swing.JTextField natureField;
    private javax.swing.JLabel natureLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField playerField;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JLabel requiredLabel;
    private javax.swing.JTextField sectField;
    private javax.swing.JLabel sectLabel;
    private javax.swing.JComboBox<String> sexField;
    private javax.swing.JLabel sexLabel;
    private javax.swing.JTextField sireField;
    private javax.swing.JLabel sireLabel;
    private javax.swing.JTextField sizeField;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JTextField skinColorField;
    private javax.swing.JLabel skinColorLabel;
    private javax.swing.JTextField weightField;
    private javax.swing.JLabel weightLabel;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
