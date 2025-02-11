package org.example.Event.NumberParticlesEvent;

import org.example.Event.Event;
import org.example.Event.EventManager;

public class NumParticlesEvent extends Event<Integer> {
    public static String EVENT_NAME = "numParticlesEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new NumParticlesEvent());
    }
}
