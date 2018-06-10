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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Marian Pollzien
 */
class ExtensionFileFilter extends FileFilter {
    private String description;
    private String extensions[];

    /**
     * Create a new extension file filter.
     *
     * @param description
     * @param extension
     */
    public ExtensionFileFilter(String description, String extension) {
        this(description, new String[]{extension});
    }

    /**
     * Create a new extension file filter with multiple extension.
     *
     * @param description
     * @param extensions
     */
    public ExtensionFileFilter(String description, String extensions[]) {
        if (description == null) {
            this.description = extensions[0];
        } else {
            this.description = description;
        }

        this.extensions = (String[]) extensions.clone();
        toLower(this.extensions);
    }

    /**
     * Format the strings of the given array to lower case.
     *
     * @param array
     */
    private void toLower(String array[]) {
        for (int i = 0, n = array.length; i < n; i++) {
            array[i] = array[i].toLowerCase();
        }
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Whether the given file is accepted by this filter.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        } else {
            String path = file.getAbsolutePath().toLowerCase();

            for (int i = 0, n = extensions.length; i < n; i++) {
                String extension = extensions[i];
                if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                    return true;
                }
            }
        }

        return false;
    }
}
