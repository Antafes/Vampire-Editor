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

package antafes.vampireEditor.lifecycle;

import antafes.vampireEditor.VampireEditor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Handles application startup and GUI initialization.
 * This listener is triggered when the Spring application context is ready.
 */
@Component
public class ApplicationReadyListener
{
    /**
     * Initialize GUI components after Spring application is ready.
     * This ensures all beans are available before opening the main window.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady()
    {
        // The GUI window is opened by VampireEditor.main() after Spring context starts,
        // but this hook is available for additional post-startup initialization if needed.
        VampireEditor.log("Application is ready");
    }
}

