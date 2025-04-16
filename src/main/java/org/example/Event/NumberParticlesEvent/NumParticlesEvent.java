package org.example.Event.NumberParticlesEvent;

import com.jopencl.event.Event;
import com.jopencl.event.EventManager;

public class NumParticlesEvent extends Event<Integer> {
    public static String EVENT_NAME = "numParticlesEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new NumParticlesEvent());
    }
}
