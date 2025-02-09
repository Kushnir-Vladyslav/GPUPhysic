package org.example.EventManager;

import org.example.EventManager.EventDataStructs.MousePosition;

public class LeftMousePressEvent extends Event<MousePosition>{
    public static String EVENT_NAME = "leftMousePressEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new LeftMousePressEvent());
    }
}
