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
 * @copyright (c) $year, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor;

import antafes.vampireEditor.language.English;
import antafes.vampireEditor.language.German;
import antafes.vampireEditor.language.LanguageInterface;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@Test
public class ConfigurationTest extends BaseTest
{
    @AfterMethod
    public void tearDown() {
        this.configuration = null;
    }

    public void testGetOpenDirPath() {
        final File expected = new File("test/open/dir/path");
        final File actual = this.configuration.getOpenDirPath();

        Assert.assertEquals(actual, expected);
    }

    public void testGetSaveDirPath() {
        final File expected = new File("test/save/dir/path");
        final File actual = this.configuration.getSaveDirPath();

        Assert.assertEquals(actual, expected);
    }

    public void testGetSaveDirPathWithParam() {
        String filename = "filename.xml";
        final File expected = new File("test/save/dir/path/" + filename);
        final File actual = this.configuration.getSaveDirPath(filename);

        Assert.assertEquals(actual, expected);
    }

    public void testGetSaveDirPathWithParam2() {
        final File expected = new File("test/save/dir/path/filename.xml");
        final File actual = this.configuration.getSaveDirPath("filename");

        Assert.assertEquals(actual, expected);
    }

    public void testGetWindowLocation() {
        final Point expected = new Point(10, 10);
        final Point actual = this.configuration.getWindowLocation();

        Assert.assertEquals(actual, expected);
    }

    public void testGetExtendedState() {
        final int expected = JFrame.NORMAL;
        final int actual = this.configuration.getExtendedState();

        Assert.assertEquals(actual, expected);
    }

    public void testGetLanguage() {
        final Configuration.Language expected = Configuration.Language.ENGLISH;
        final Configuration.Language actual = this.configuration.getLanguage();

        Assert.assertEquals(actual, expected);
    }

    public void testGetLanguageObject() {
        final LanguageInterface expected = new English();
        final LanguageInterface actual = this.configuration.getLanguageObject();

        Assert.assertEquals(actual, expected);
    }

    public void testGetLanguageObjectWithSet() {
        final LanguageInterface expected = new German();

        this.configuration.setLanguage(Configuration.Language.GERMAN);

        final LanguageInterface actual = this.configuration.getLanguageObject();

        Assert.assertEquals(actual, expected);
    }

    public void testSetOpenDirPath() {
        final File notExpected = new File("test/open/dir/path");
        final File expected = new File("test/new/open/path");

        this.configuration.setOpenDirPath("test/new/open/path");

        final File actual = this.configuration.getOpenDirPath();

        Assert.assertNotEquals(actual, notExpected);
        Assert.assertEquals(actual, expected);
    }

    public void testSetSaveDirPath() {
        final File notExpected = new File("test/save/dir/path");
        final File expected = new File("test/new/save/path");

        this.configuration.setSaveDirPath("test/new/save/path");

        final File actual = this.configuration.getSaveDirPath();

        Assert.assertNotEquals(actual, notExpected);
        Assert.assertEquals(actual, expected);
    }

    public void testSetWindowLocation() {
        final Point notExpected = new Point(10, 10);
        final Point expected = new Point(22, 22);

        this.configuration.setWindowLocation(expected);

        final Point actual = this.configuration.getWindowLocation();

        Assert.assertNotEquals(actual, notExpected);
        Assert.assertEquals(actual, expected);
    }

    public void testSetExtendedState() {
        final int notExpected = JFrame.NORMAL;
        final int expected = JFrame.MAXIMIZED_BOTH;

        this.configuration.setExtendedState(JFrame.MAXIMIZED_BOTH);

        final int actual = this.configuration.getExtendedState();

        Assert.assertNotEquals(actual, notExpected);
        Assert.assertEquals(actual, expected);
    }

    public void testSetLanguage() {
        final Configuration.Language notExpected = Configuration.Language.ENGLISH;
        final Configuration.Language expected = Configuration.Language.GERMAN;

        this.configuration.setLanguage(Configuration.Language.GERMAN);

        final Configuration.Language actual = this.configuration.getLanguage();

        Assert.assertNotEquals(actual, notExpected);
        Assert.assertEquals(actual, expected);
    }
}
