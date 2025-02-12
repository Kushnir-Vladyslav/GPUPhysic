package org.example.Event.TimeTickEvent;

import org.example.Event.Event;
import org.example.Event.EventManager;

public class TimeTickEvent extends Event<Float> {
    public static String EVENT_NAME = "timeTickEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new TimeTickEvent());
    }
}
