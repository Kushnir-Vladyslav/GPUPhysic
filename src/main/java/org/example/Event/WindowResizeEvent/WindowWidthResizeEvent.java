package org.example.Event.WindowResizeEvent;

import org.example.Event.Event;
import org.example.Event.EventManager;

public class WindowWidthResizeEvent extends Event<Integer> {
    public static String EVENT_NAME = "windowWidthResizeEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new WindowWidthResizeEvent());
    }
}
