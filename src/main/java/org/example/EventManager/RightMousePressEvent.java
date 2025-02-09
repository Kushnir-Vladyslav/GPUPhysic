package org.example.EventManager;

import org.example.EventManager.EventDataStructs.MousePosition;

public class RightMousePressEvent extends Event<MousePosition>{
    public static String EVENT_NAME = "rightMousePressEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new RightMousePressEvent());
    }
}
