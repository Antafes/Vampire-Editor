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
package antafes.vampireEditor.gui.element;

import antafes.vampireEditor.Configuration;
import antafes.vampireEditor.entity.character.Clan;
import antafes.vampireEditor.language.LanguageInterface;
import antafes.vampireEditor.utility.ClanComparator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Combo box for clan selection with group headlines for clans and bloodlines.
 */
public class ClanComboBox extends GroupedComboBox<Clan>
{
    private static final String CLANS_HEADER = "Clans";
    private static final String BLOODLINES_HEADER = "Bloodlines";

    private final Configuration configuration;
    private LanguageInterface language;
    private Collection<Clan> clans = new ArrayList<>();

    public ClanComboBox(Configuration configuration)
    {
        super();
        this.configuration = configuration;
        this.setItemTextProvider(clan -> clan != null ? clan.getName(this.configuration) : "");
    }

    /**
     * Set language used for group header labels.
     *
     * @param language Language object
     */
    public void setLanguage(LanguageInterface language)
    {
        this.language = language;
        this.setClans(this.clans);
    }

    /**
     * Populate the combo box with grouped clan entries.
     *
     * @param clans Clans and bloodlines
     */
    public void setClans(Collection<Clan> clans)
    {
        this.clans = new ArrayList<>(clans);

        ArrayList<Clan> mainClans = new ArrayList<>();
        ArrayList<Clan> bloodlines = new ArrayList<>();
        this.clans.forEach(clan -> {
            if (clan.isBloodline()) {
                bloodlines.add(clan);
            } else {
                mainClans.add(clan);
            }
        });

        mainClans.sort(new ClanComparator(this.configuration));
        bloodlines.sort(new ClanComparator(this.configuration));

        GroupedComboBoxModel<Clan> groupedModel = this.createGroupedModel(mainClans, bloodlines);
        super.setModel(groupedModel);
    }

    /**
     * Get the selected clan or null.
     *
     * @return Selected clan
     */
    public Clan getSelectedClan()
    {
        return this.getSelectedItem();
    }

    private GroupedComboBoxModel<Clan> createGroupedModel(ArrayList<Clan> mainClans, ArrayList<Clan> bloodlines)
    {
        GroupedComboBoxModel<Clan> groupedModel = new GroupedComboBoxModel<>();
        groupedModel.setUngroupedEmptyEntry("");

        if (!mainClans.isEmpty()) {
            groupedModel.addGroup(this.getClansHeader(), mainClans);
        }

        if (!bloodlines.isEmpty()) {
            groupedModel.addGroup(this.getBloodlinesHeader(), bloodlines);
        }

        return groupedModel;
    }

    private String getClansHeader()
    {
        return this.language != null ? this.language.translate("clan.group.clans") : CLANS_HEADER;
    }

    private String getBloodlinesHeader()
    {
        return this.language != null ? this.language.translate("clan.group.bloodlines") : BLOODLINES_HEADER;
    }
}
