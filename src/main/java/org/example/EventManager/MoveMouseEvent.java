package org.example.EventManager;

import org.example.EventManager.EventDataStructs.MousePosition;

public class MoveMouseEvent extends Event<MousePosition>{
    public static String EVENT_NAME = "moveMouseEvent";

    static {
        EventManager.getEventManager().addEvent(
                EVENT_NAME,
                new MoveMouseEvent());
    }
}
