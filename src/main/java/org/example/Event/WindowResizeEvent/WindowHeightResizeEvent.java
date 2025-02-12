package org.example.Event.WindowResizeEvent;

import org.example.Event.Event;
import org.example.Event.EventManager;

public class WindowHeightResizeEvent extends Event<Integer> {
    public static String EVENT_NAME = "windowHeightResizeEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new WindowHeightResizeEvent());
    }
}
