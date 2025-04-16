package org.example.Event.WindowResizeEvent;

import com.jopencl.event.Event;
import com.jopencl.event.EventManager;

public class WindowWidthResizeEvent extends Event<Integer> {
    public static String EVENT_NAME = "windowWidthResizeEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new WindowWidthResizeEvent());
    }
}
