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

import antafes.vampireEditor.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Handles application shutdown and resource cleanup.
 * This listener is triggered when the Spring application context is closed.
 */
@Component
@RequiredArgsConstructor
public class ApplicationShutdownHandler
{
    private final Configuration configuration;

    /**
     * Perform cleanup and resource release on application shutdown.
     */
    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown()
    {
        // Save configuration on shutdown
        this.configuration.saveProperties();
    }
}

