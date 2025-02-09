package org.example.Event.MouseEvent;

import org.example.Event.Event;
import org.example.Event.EventDataStructs.MousePosition;
import org.example.Event.EventManager;

public class MoveMouseEvent extends Event<MousePosition> {
    public static String EVENT_NAME = "moveMouseEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new MoveMouseEvent());
    }
}
