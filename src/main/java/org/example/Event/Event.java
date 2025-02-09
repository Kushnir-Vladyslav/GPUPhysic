package org.example.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Event <T> {
    Map<Object, EventHandler<T>> eventHandlers;

    public Event () {
        eventHandlers = new HashMap<>();
    }

    /**
     * Підписати обробник події.
     *
     * @param obj         Об'єкт, який підписується на подію (використовується як ключ для видалення).
     * @param eventHandler Обробник події.
     */
    public void subscribe(Object obj, EventHandler<T> eventHandler) {
        Objects.requireNonNull(obj, "Об'єкт не може бути null");
        Objects.requireNonNull(eventHandler, "Обробник події не може бути null");
        eventHandlers.put(obj, eventHandler);
    }

    /**
     * Викликати всі підписані обробники події.
     *
     * @param eventData Дані події.
     */
    public void invoke (T eventData) {
        for (EventHandler<T> eventHandler : eventHandlers.values()) {
            eventHandler.handle(eventData);
        }
    }

    /**
     * Видалити обробник події.
     *
     * @param obj Об'єкт, який був підписаний на подію.
     */
    public void removeHandler (Object obj) {
        if (!eventHandlers.containsKey(obj)) {
            throw new IllegalArgumentException("A event handler with name \"" + obj + "\" does not exist.");
        } else {
            eventHandlers.remove(obj);
        }
    }

    /**
     * Очистити всі обробники події.
     */
    public void destroy () {
        eventHandlers.clear();
    }
}
