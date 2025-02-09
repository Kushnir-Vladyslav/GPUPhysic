package org.example.EventManager;

import org.example.EventManager.EventDataStructs.NewParticle;

public class AddParticlesEvent extends Event<NewParticle> {
    public static String EVENT_NAME = "addParticlesEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new AddParticlesEvent());
    }
}


