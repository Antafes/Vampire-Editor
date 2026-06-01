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

package scripts.laniax.framework.event_dispatcher;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public class EventListener<T extends Event>
{
    private final Consumer<T> consumer;
    private final int priority;

    public EventListener()
    {
        this((event) -> {
        }, 0);
    }

    public EventListener(Consumer<T> consumer)
    {
        this(consumer, 0);
    }


    public int getPriority()
    {
        return this.priority;
    }

    public void onEvent(T event)
    {
        this.consumer.accept(event);
    }
}

