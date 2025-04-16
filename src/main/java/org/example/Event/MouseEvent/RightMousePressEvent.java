package org.example.Event.MouseEvent;

import com.jopencl.event.Event;
import org.example.Event.EventDataStructs.MousePosition;
import com.jopencl.event.EventManager;

public class RightMousePressEvent extends Event<MousePosition> {
    public static String EVENT_NAME = "rightMousePressEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new RightMousePressEvent());
    }
}
