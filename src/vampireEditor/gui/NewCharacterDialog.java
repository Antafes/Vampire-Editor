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
import vampireEditor.entity.character.Clan;
import vampireEditor.gui.newCharacter.*;
import vampireEditor.language.LanguageInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marian Pollzien
 */
public class NewCharacterDialog extends javax.swing.JDialog {

    private final LanguageInterface language;
    private int maxActiveTab = 0;
    private LooksPanel looksPanel;
    private AttributesPanel attributesPanel;
    private AbilitiesPanel abilitiesPanel;
    private AdvantagesPanel advantagesPanel;
    private LastStepsPanel lastStepsPanel;
    private BaseWindow parent;

    // List of created fields
    private javax.swing.JButton cancelButton;
    private javax.swing.JTabbedPane characterTabPane;
    private javax.swing.JTextField freeAdditionalMaxPointsTextField;
    private javax.swing.JLabel freeAdditionalPointsLabel;
    private javax.swing.JTextField freeAdditionalPointsTextField;

    /**
     * Creates new form NewCharacterDialog
     *
     * @param parent Parent element
     * @param modal Whether the dialog should be modal or not
     */
    public NewCharacterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        Configuration configuration = Configuration.getInstance();
        this.language = configuration.getLanguageObject();

        this.initComponents();
        this.init();
    }

    /**
     * Initialize every component that should be shown on the panel.
     */
    private void initComponents() {
        cancelButton = new javax.swing.JButton();
        characterTabPane = new javax.swing.JTabbedPane();
        freeAdditionalPointsTextField = new javax.swing.JTextField("0");
        freeAdditionalMaxPointsTextField = new javax.swing.JTextField("15");
        freeAdditionalPointsLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelButtonActionPerformed);

        this.looksPanel = new LooksPanel(this);
        this.looksPanel.init();
        characterTabPane.add(this.looksPanel);
        this.attributesPanel = new AttributesPanel(this);
        characterTabPane.add(this.attributesPanel);
        this.abilitiesPanel = new AbilitiesPanel(this);
        characterTabPane.add(this.abilitiesPanel);
        this.advantagesPanel = new AdvantagesPanel(this);
        JScrollPane advantagesScrollPane = new JScrollPane(this.advantagesPanel);
        advantagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        characterTabPane.add(advantagesScrollPane);
        this.lastStepsPanel = new LastStepsPanel(this);
        JScrollPane lastStepsScrollPane = new JScrollPane(this.lastStepsPanel);
        lastStepsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        characterTabPane.add(lastStepsScrollPane);

        characterTabPane.setEnabledAt(1, false);
        characterTabPane.setEnabledAt(2, false);
        characterTabPane.setEnabledAt(3, false);
        characterTabPane.setEnabledAt(4, false);

        freeAdditionalPointsTextField.setEnabled(false);

        freeAdditionalMaxPointsTextField.setEnabled(false);

        freeAdditionalPointsLabel.setText("Free additional points");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(characterTabPane)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(freeAdditionalPointsLabel)
                .addGap(18, 18, 18)
                .addComponent(freeAdditionalPointsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(freeAdditionalMaxPointsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(characterTabPane)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(freeAdditionalPointsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(freeAdditionalMaxPointsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(freeAdditionalPointsLabel))
                    .addComponent(cancelButton))
                .addGap(11, 11, 11))
        );

        pack();
    }

    /**
     * Action perfmored event for the cancel button.
     *
     * @param evt Event object
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Set the maximum value for the attribute spinners.
     *
     * @param maximum
     */
    public void setAttributeMaximum(int maximum) {
        this.attributesPanel.setSpinnerMaximum(maximum);
        this.abilitiesPanel.setSpinnerMaximum(maximum);
        this.advantagesPanel.setSpinnerMaximum(maximum);
    }

    /**
     * Get the tabbed pane inside this dialog.
     *
     * @return
     */
    public JTabbedPane getCharacterTabPane() {
        return characterTabPane;
    }

    /**
     * Init everything.
     */
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

    /**
     * Set the texts of every field.
     */
    private void setFieldTexts() {
        this.setTitle(this.language.translate("newCharacter"));
        this.cancelButton.setText(this.language.translate("cancel"));
        this.freeAdditionalPointsLabel.setText(this.language.translate("freeAdditionalPoints"));

        this.characterTabPane.setTitleAt(0, this.language.translate("looks"));
        this.characterTabPane.setTitleAt(1, this.language.translate("attributes"));
        this.characterTabPane.setTitleAt(2, this.language.translate("abilities"));
        this.characterTabPane.setTitleAt(3, this.language.translate("advantages"));
        this.characterTabPane.setTitleAt(4, this.language.translate("lastSteps"));
    }

    /**
     * Increase the current maximum active tab.
     */
    public void increaseMaxActiveTab() {
        this.maxActiveTab++;
    }

    /**
     * Get the current maximum active tab.
     *
     * @return
     */
    public int getMaxActiveTab() {
        return this.maxActiveTab;
    }

    /**
     * Calculate and return the sum of points spent for physical attributes.
     *
     * @return
     */
    private int getPhysicalPointsSum() {
        return this.attributesPanel.getPhysicalPointsSum();
    }

    /**
     * Check if the spent points for physical attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkPhysicalPoints() {
        return this.attributesPanel.checkPhyiscalPoints();
    }

    /**
     * Get the maximum points available for physical attributes.
     *
     * @return
     */
    private int getPhysicalMaxPoints() {
        return this.attributesPanel.getPhyiscalMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for social attributes.
     *
     * @return
     */
    private int getSocialPointsSum() {
        return this.attributesPanel.getSocialPointsSum();
    }

    /**
     * Check if the spent points for social attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkSocialPoints() {
        return this.attributesPanel.checkSocialPoints();
    }

    /**
     * Get the maximum points available for social attributes.
     *
     * @return
     */
    private int getSocialMaxPoints() {
        return this.attributesPanel.getSocialMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for mental attributes.
     *
     * @return
     */
    private int getMentalPointsSum() {
        return this.attributesPanel.getMentalPointsSum();
    }

    /**
     * Check if the spent points for mental attributes is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkMentalPoints() {
        return this.attributesPanel.checkMentalPoints();
    }

    /**
     * Get the maximum points available for mental attributes.
     *
     * @return
     */
    private int getMentalMaxPoints() {
        return this.attributesPanel.getMentalMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for talents.
     *
     * @return
     */
    private int getTalentPointsSum() {
        return this.abilitiesPanel.getTalentPointsSum();
    }

    /**
     * Check if the spent points for talents is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkTalentPoints() {
        return this.abilitiesPanel.checkTalentPoints();
    }

    /**
     * Get the maximum points available for talents.
     *
     * @return
     */
    private int getTalentMaxPoints() {
        return this.abilitiesPanel.getTalentMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for skills.
     *
     * @return
     */
    private int getSkillPointsSum() {
        return this.abilitiesPanel.getSkillPointsSum();
    }

    /**
     * Check if the spent points for skills is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkSkillPoints() {
        return this.abilitiesPanel.checkSkillPoints();
    }

    /**
     * Get the maximum points available for skills.
     *
     * @return
     */
    private int getSkillMaxPoints() {
        return this.abilitiesPanel.getSkillMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for knowledges.
     *
     * @return
     */
    private int getKnowledgePointsSum() {
        return this.abilitiesPanel.getKnowledgePointsSum();
    }

    /**
     * Check if the spent points for knowledges is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkKnowledgePoints() {
        return this.abilitiesPanel.checkKnowledgePoints();
    }

    /**
     * Get the maximum points available for knowledges.
     *
     * @return
     */
    private int getKnowledgeMaxPoints() {
        return this.abilitiesPanel.getKnowledgeMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for backgrounds.
     *
     * @return
     */
    private int getBackgroundPointsSum() {
        return this.advantagesPanel.getBackgroundPointsSum();
    }

    /**
     * Check if the spent points for backgrounds is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkBackgroundPoints() {
        return this.advantagesPanel.checkBackgroundPoints();
    }

    /**
     * Get the maximum points available for backgrounds.
     *
     * @return
     */
    private int getBackgroundMaxPoints() {
        return this.advantagesPanel.getBackgroundMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for disciplins.
     *
     * @return
     */
    private int getDisciplinPointsSum() {
        return this.advantagesPanel.getDisciplinPointsSum();
    }

    /**
     * Check if the spent points for disciplins is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkDisciplinPoints() {
        return this.advantagesPanel.checkDisciplinPoints();
    }

    /**
     * Get the maximum points available for disciplins.
     *
     * @return
     */
    private int getDisciplinMaxPoints() {
        return this.advantagesPanel.getDisciplinMaxPoints();
    }

    /**
     * Calculate and return the sum of points spent for virtues.
     *
     * @return
     */
    private int getVirtuePointsSum() {
        return this.advantagesPanel.getVirtuePointsSum();
    }

    /**
     * Check if the spent points for virtues is above its maximum.
     *
     * @return True if spent points are above maximum
     */
    private boolean checkVirtuePoints() {
        return this.advantagesPanel.checkVirtuePoints();
    }

    /**
     * Get the maximum points available for virtues.
     *
     * @return
     */
    private int getVirtueMaxPoints() {
        return this.advantagesPanel.getVirtueMaxPoints();
    }

    /**
     * Calculate the used free additional points.
     */
    public void calculateUsedFreeAdditionalPoints() {
        int freeSum = 0;

        if (this.checkPhysicalPoints()) {
            freeSum += (this.getPhysicalPointsSum() - this.getPhysicalMaxPoints()) * 5;
        }

        if (this.checkSocialPoints()) {
            freeSum += (this.getSocialPointsSum() - this.getSocialMaxPoints()) * 5;
        }

        if (this.checkMentalPoints()) {
            freeSum += (this.getMentalPointsSum() - this.getMentalMaxPoints()) * 5;
        }

        if (this.checkTalentPoints()) {
            freeSum += (this.getTalentPointsSum() - this.getTalentMaxPoints()) * 2;
        }

        if (this.checkSkillPoints()) {
            freeSum += (this.getSkillPointsSum() - this.getSkillMaxPoints()) * 2;
        }

        if (this.checkKnowledgePoints()) {
            freeSum += (this.getKnowledgePointsSum() - this.getKnowledgeMaxPoints()) * 2;
        }

        if (this.checkBackgroundPoints()) {
            freeSum += (this.getBackgroundPointsSum() - this.getBackgroundMaxPoints());
        }

        if (this.checkDisciplinPoints()) {
            freeSum += (this.getDisciplinPointsSum() - this.getDisciplinMaxPoints()) * 7;
        }

        if (this.checkVirtuePoints()) {
            freeSum += (this.getVirtuePointsSum() - this.getVirtueMaxPoints()) * 2;
        }

        freeSum += this.lastStepsPanel.getMeritPoints();

        this.freeAdditionalPointsTextField.setText(Integer.toString(freeSum));

        if (this.checkFreeAdditionalPoints()) {
            this.freeAdditionalPointsLabel.setForeground(Color.RED);
        } else {
            this.freeAdditionalPointsLabel.setForeground(Color.DARK_GRAY);
        }
    }

    /**
     * Check more free additional points are used than are available.
     *
     * @return True if the used points are higher than the maximum
     */
    public boolean checkFreeAdditionalPoints() {
        int usedPoints = Integer.parseInt(this.freeAdditionalPointsTextField.getText());
        int maxPoints = Integer.parseInt(this.freeAdditionalMaxPointsTextField.getText());

        return usedPoints > maxPoints;
    }

    /**
     * Adjust the attributes according to the selected clan.
     *
     * @param clan Clan object to fetch some values from
     */
    public void adjustAttributesToClan(Clan clan) {
        this.attributesPanel.adjustAttributesToClan(clan);
    }

    /**
     * Set the clan disciplins on the advantages panel.
     *
     * @param clan
     */
    public void setClanDisciplins(Clan clan) {
        this.advantagesPanel.setDisciplins(clan);
    }

    /**
     * Get the textfield for the maximum free additional points.
     *
     * @return
     */
    public JTextField getFreeAdditionalMaxPointsTextField() {
        return freeAdditionalMaxPointsTextField;
    }

    /**
     * Set the parent container.
     *
     * @param parent
     */
    public void setParent(BaseWindow parent) {
        this.parent = parent;
    }

    /**
     * Finish the character and send the new character object over to the BaseWindow.
     */
    public void finishCharacter() {
        if (this.checkAllInputs()) {
            return;
        }

        VampireEditor.log(new ArrayList<>(
            Collections.singletonList("finishing character")
        ));
        vampireEditor.entity.Character.Builder builder = new vampireEditor.entity.Character.Builder();
        this.looksPanel.fillCharacter(builder);
        this.attributesPanel.fillCharacter(builder);
        this.abilitiesPanel.fillCharacter(builder);
        this.advantagesPanel.fillCharacter(builder);
        this.lastStepsPanel.fillCharacter(builder);

        try {
            this.parent.addCharacter(builder.build());
            VampireEditor.log(new ArrayList<>(
                Collections.singletonList("closing window")
            ));
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } catch (EntityException ex) {
            Logger.getLogger(NewCharacterDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method checks every input made by the user for duplicate entries on any panel.
     *
     * @return Returns true if a duplicate entry has been found.
     */
    private boolean checkAllInputs() {
        VampireEditor.log(new ArrayList<>(
                Arrays.asList(
                    Boolean.toString(this.looksPanel.checkAllFields()),
                    Boolean.toString(this.attributesPanel.checkAllFields()),
                    Boolean.toString(this.abilitiesPanel.checkAllFields()),
                    Boolean.toString(this.advantagesPanel.checkAllFields()),
                    Boolean.toString(this.lastStepsPanel.checkAllFields())
                )
            ));

        return this.looksPanel.checkAllFields()
            || this.attributesPanel.checkAllFields()
            || this.abilitiesPanel.checkAllFields()
            || this.advantagesPanel.checkAllFields()
            || this.lastStepsPanel.checkAllFields();
    }
}
