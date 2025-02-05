package org.example.EventManager;

import org.example.EventManager.EventStructs.MousePosition;

public class RightMouseReleaseEvent extends Event<MousePosition>{
    public static String EVENT_NAME = "rightMouseReleaseEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new RightMouseReleaseEvent());
    }
}
