package org.example.Event.MouseEvent;

import com.jopencl.event.Event;
import org.example.Event.EventDataStructs.MousePosition;
import com.jopencl.event.EventManager;

public class RightMouseReleaseEvent extends Event<MousePosition> {
    public static String EVENT_NAME = "rightMouseReleaseEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new RightMouseReleaseEvent());
    }
}
