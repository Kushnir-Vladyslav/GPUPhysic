package org.example.Event.WindowResizeEvent;

import com.jopencl.event.Event;
import com.jopencl.event.EventManager;

public class WindowHeightResizeEvent extends Event<Integer> {
    public static String EVENT_NAME = "windowHeightResizeEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new WindowHeightResizeEvent());
    }
}
