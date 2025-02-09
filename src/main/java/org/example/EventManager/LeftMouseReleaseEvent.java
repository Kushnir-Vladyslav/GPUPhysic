package org.example.EventManager;

import org.example.EventManager.EventDataStructs.MousePosition;

public class LeftMouseReleaseEvent extends Event<MousePosition>{
    public static String EVENT_NAME = "leftMouseReleaseEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new LeftMouseReleaseEvent());
    }
}
