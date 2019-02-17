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
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor;

import antafes.vampireEditor.entity.EntityException;
import antafes.vampireEditor.entity.character.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

@Test
public class VampireEditorTest {
    public void testGetFileInJar() {
        String path = "antafes/vampireEditor/VampireEditor.java";
        final InputStream expected = VampireEditorTest.class.getResourceAsStream(path);
        final InputStream actual = VampireEditor.getFileInJar(path);

        Assert.assertEquals(actual, expected);
    }

    public void testGetResourceInJar() {
        String path = "antafes/vampireEditor/VampireEditor.java";
        final URL expected = VampireEditorTest.class.getResource(path);
        final URL actual = VampireEditor.getResourceInJar(path);

        Assert.assertEquals(actual, expected);
    }

    public void testGetGenerations() {
        new VampireEditor();
        final ArrayList actual = VampireEditor.getGenerations();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.get(0).getClass(), Generation.class);
    }

    public void testGetGeneration() throws EntityException {
        new VampireEditor();
        final Generation expected = new Generation.Builder()
            .setGeneration(9)
            .setBloodPerRound(2)
            .setMaximumAttributes(5)
            .setMaximumBloodPool(14)
            .build();
        final Generation actual = VampireEditor.getGeneration(9);

        Assert.assertEquals(actual, expected);
    }

    public void testGetAdvantages() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getAdvantages();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Advantage.class);
    }

    public void testGetClans() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getClans();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Clan.class);
    }

    public void testGetAttributes() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getAttributes();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Attribute.class);
    }

    public void testGetAttribute() throws EntityException {
        new VampireEditor();
        final Attribute expected = new Attribute.Builder()
            .setKey("strength")
            .setType(AttributeInterface.AttributeType.PHYSICAL)
            .addName(Configuration.Language.ENGLISH, "Strength")
            .addName(Configuration.Language.GERMAN, "Körperkraft")
            .build();
        final Attribute actual = VampireEditor.getAttribute("strength");

        Assert.assertEquals(actual, expected);
    }

    public void testGetAbilities() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getAbilities();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Ability.class);
    }

    public void testGetAbility() throws EntityException {
        new VampireEditor();
        final Ability expected = new Ability.Builder()
            .setKey("expression")
            .setType(AbilityInterface.AbilityType.TALENT)
            .addName(Configuration.Language.ENGLISH, "Expression")
            .addName(Configuration.Language.GERMAN, "Schauspielerei")
            .build();
        final Ability actual = VampireEditor.getAbility("expression");

        Assert.assertEquals(actual, expected);
    }

    public void testGetAdvantage() throws EntityException {
        new VampireEditor();
        final Advantage expected = new Advantage.Builder()
            .setKey("allies")
            .setType(AdvantageInterface.AdvantageType.BACKGROUND)
            .addName(Configuration.Language.ENGLISH, "Allies")
            .addName(Configuration.Language.GERMAN, "Verbündete")
            .build();
        final Advantage actual = VampireEditor.getAdvantage("allies");

        Assert.assertEquals(actual, expected);
    }

    public void testGetWeakness() throws EntityException {
        new VampireEditor();
        final Weakness expected = new Weakness.Builder()
            .setKey("bloodTithe")
            .addName(Configuration.Language.ENGLISH, "Blood tithe 20%")
            .addName(Configuration.Language.GERMAN, "Blutzehnt 20%")
            .build();
        final Weakness actual = VampireEditor.getWeakness("bloodTithe");

        Assert.assertEquals(actual, expected);
    }

    public void testGetClan() throws EntityException {
        new VampireEditor();
        final Clan expected = new Clan.Builder()
            .setKey("assamites")
            .addName(Configuration.Language.ENGLISH, "Assamites")
            .addName(Configuration.Language.GERMAN, "Assamiten")
            .addNickname(Configuration.Language.ENGLISH, "Saracens")
            .addNickname(Configuration.Language.GERMAN, "Sarazenen")
            .addAdvantage(VampireEditor.getAdvantage("auspex"))
            .addAdvantage(VampireEditor.getAdvantage("presence"))
            .addAdvantage(VampireEditor.getAdvantage("quietus"))
            .addWeakness(VampireEditor.getWeakness("bloodTithe"))
            .addWeakness(VampireEditor.getWeakness("diableryTraces"))
            .build();
        final Clan actual = VampireEditor.getClan("assamites");

        Assert.assertEquals(actual, expected);
    }

    public void testGetMerits() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getMerits();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Merit.class);
    }

    public void testGetFlaws() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getFlaws();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Flaw.class);
    }

    public void testGetRoads() {
        new VampireEditor();
        final HashMap actual = VampireEditor.getRoads();

        Assert.assertNotNull(actual);
        Assert.assertFalse(actual.isEmpty());
        Assert.assertEquals(actual.values().toArray()[0].getClass(), Road.class);
    }

    public void testGetRoad() throws EntityException {
        new VampireEditor();
        final Road expected = new Road.Builder()
            .setKey("roadOfBeast")
            .addName(Configuration.Language.ENGLISH, "Road of Beast")
            .build();
        final Road actual = VampireEditor.getRoad("roadOfBeast");

        Assert.assertEquals(actual, expected);
    }
}