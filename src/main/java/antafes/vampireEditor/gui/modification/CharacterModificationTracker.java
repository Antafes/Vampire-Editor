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

package antafes.vampireEditor.gui.modification;

/**
 * Tracks whether a character has unsaved modifications.
 *
 * This class provides centralized tracking of character modification state,
 * allowing the UI to prompt the user before discarding unsaved changes.
 *
 * @author Marian Pollzien
 */
public class CharacterModificationTracker {
    private boolean modified = false;

    /**
     * Create a new modification tracker.
     * Initially, the character is not marked as modified.
     */
    public CharacterModificationTracker() {
        this.modified = false;
    }

    /**
     * Mark the character as having unsaved modifications.
     */
    public void markModified() {
        this.modified = true;
    }

    /**
     * Clear the modification flag after a successful save operation.
     */
    public void resetModified() {
        this.modified = false;
    }

    /**
     * Check if the character has unsaved modifications.
     *
     * @return true if the character has unsaved modifications, false otherwise
     */
    public boolean isModified() {
        return this.modified;
    }
}


