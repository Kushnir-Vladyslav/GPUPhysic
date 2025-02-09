package org.example.EventManager;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private static volatile EventManager eventManager;

    private final Map <String, Event<?>> events;

    /**
     * Отримати єдиний екземпляр EventManager (Singleton).
     *
     * @return Екземпляр EventManager.
     */
    public static EventManager getEventManager () {
        if (eventManager == null) {
            synchronized (EventManager.class) {
                if (eventManager == null) {
                    eventManager = new EventManager();
                }
            }
        }
        return eventManager;
    }

    private EventManager () {
        events = new HashMap<>();
    }


    /**
     * Додати подію до менеджера.
     *
     * @param name  Назва події.
     * @param event Об'єкт події.
     */
    public void addEvent (String name, Event<?> event) {
        events.put(name, event);
    }

    /**
     * Отримати подію за назвою.
     *
     * @param name Назва події.
     * @return Об'єкт події.
     */
    public <T extends Event<?>> T getEvent(String name) {
        if (!events.containsKey(name)) {
            throw new IllegalArgumentException("A event \"" + name + "\" does not exist.");
        }
        return (T) events.get(name);
    }

    /**
     * Видалити подію з менеджера.
     *
     * @param name Назва події.
     */
    public void removeEvent (String name) {
        if (!events.containsKey(name)) {
            throw new IllegalArgumentException("A event \"" + name + "\" does not exist.");
        } else {
            events.remove(name);
        }
    }

    /**
     * Очистити всі події та їх обробники.
     */
    public void destroy () {
        for(Event<?> event : events.values()) {
            event.destroy();
        }
        events.clear();
    }
}
