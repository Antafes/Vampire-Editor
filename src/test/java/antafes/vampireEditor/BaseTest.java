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

package antafes.vampireEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

import javax.swing.*;
import java.awt.*;

@SpringBootTest(classes = VampireEditor.class)
@ActiveProfiles("test")
public abstract class BaseTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected Configuration configuration;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeMethod
    public void setUp()
    {
        if (this.configuration == null) {
            this.configuration = this.applicationContext.getBean(Configuration.class);
        }

        this.configuration.loadProperties();
        this.configuration.clearRecentFiles();
        this.configuration.setOpenDirPath("test/open/dir/path");
        this.configuration.setSaveDirPath("test/save/dir/path");
        this.configuration.setLanguage(Configuration.Language.ENGLISH);
        this.configuration.setWindowLocation(new Point(10, 10));
        this.configuration.setExtendedState(JFrame.NORMAL);
    }
}
