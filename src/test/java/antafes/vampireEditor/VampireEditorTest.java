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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;

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
}