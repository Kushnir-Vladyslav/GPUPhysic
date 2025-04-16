package org.example.Event.TimeTickEvent;

import com.jopencl.event.Event;
import com.jopencl.event.EventManager;

public class TimeTickEvent extends Event<Float> {
    public static String EVENT_NAME = "timeTickEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new TimeTickEvent());
    }
}
