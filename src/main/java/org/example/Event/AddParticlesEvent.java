package org.example.Event;

import org.example.Event.EventDataStructs.NewParticle;

public class AddParticlesEvent extends Event<NewParticle> {
    public static String EVENT_NAME = "addParticlesEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new AddParticlesEvent());
    }
}


