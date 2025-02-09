package org.example.Event.MouseEvent;

import org.example.Event.Event;
import org.example.Event.EventDataStructs.MousePosition;
import org.example.Event.EventManager;

public class RightMousePressEvent extends Event<MousePosition> {
    public static String EVENT_NAME = "rightMousePressEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new RightMousePressEvent());
    }
}
