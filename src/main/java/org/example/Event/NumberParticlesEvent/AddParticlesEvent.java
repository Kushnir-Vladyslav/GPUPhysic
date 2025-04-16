package org.example.Event.NumberParticlesEvent;

import com.jopencl.event.Event;
import org.example.Event.EventDataStructs.NewParticle;
import com.jopencl.event.EventManager;

public class AddParticlesEvent extends Event<NewParticle> {
    public static String EVENT_NAME = "addParticlesEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new AddParticlesEvent());
    }
}


