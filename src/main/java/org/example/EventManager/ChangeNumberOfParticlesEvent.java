package org.example.EventManager;

public class ChangeNumberOfParticlesEvent extends Event <Integer> {

    static {
        EventManager.getEventManager().addEvent(
                "changeNumberOfParticlesEvent",
                new ChangeNumberOfParticlesEvent());
    }
}
