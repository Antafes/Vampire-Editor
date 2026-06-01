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

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher
{
    private final SimpleApplicationEventMulticaster multicaster;
    private final Map<EventListener<?>, ApplicationListener<?>> listenerAdapters;

    public Dispatcher()
    {
        this.multicaster = new SimpleApplicationEventMulticaster();
        this.listenerAdapters = new ConcurrentHashMap<>();
    }

    public static Dispatcher getInstance()
    {
        return new Dispatcher();
    }

    public <T extends Event> void addListener(Class<T> eventClass, EventListener<T> listener)
    {
        GenericApplicationListener adapter = new GenericApplicationListener()
        {
            @Override
            public void onApplicationEvent(ApplicationEvent event)
            {
                if (!(event instanceof PayloadApplicationEvent<?> payloadEvent)) {
                    return;
                }

                Object payload = payloadEvent.getPayload();
                if (!eventClass.isInstance(payload)) {
                    return;
                }

                listener.onEvent(eventClass.cast(payload));
            }

            @Override
            public boolean supportsEventType(ResolvableType eventType)
            {
                return PayloadApplicationEvent.class.isAssignableFrom(eventType.toClass());
            }

            @Override
            public int getOrder()
            {
                return listener.getPriority();
            }
        };

        ApplicationListener<?> existingAdapter = this.listenerAdapters.get(listener);
        if (existingAdapter != null) {
            this.multicaster.removeApplicationListener(existingAdapter);
        }

        this.listenerAdapters.put(listener, adapter);
        this.multicaster.addApplicationListener(adapter);
    }

    public <T extends Event> void removeListener(Class<T> eventClass, EventListener<T> listener)
    {
        ApplicationListener<?> adapter = this.listenerAdapters.remove(listener);
        if (adapter != null) {
            this.multicaster.removeApplicationListener(adapter);
        }
    }

    public void dispatch(Event event)
    {
        this.multicaster.multicastEvent(new PayloadApplicationEvent<>(this, event));
    }

    public void destroy()
    {
        this.listenerAdapters.values().forEach(this.multicaster::removeApplicationListener);
        this.listenerAdapters.clear();
    }
}

