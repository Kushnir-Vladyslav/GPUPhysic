package org.example.Event.NumberParticlesEvent;

import org.example.Event.Event;
import org.example.Event.EventDataStructs.NewParticle;
import org.example.Event.EventManager;

public class AddParticlesEvent extends Event<NewParticle> {
    public static String EVENT_NAME = "addParticlesEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new AddParticlesEvent());
    }
}


