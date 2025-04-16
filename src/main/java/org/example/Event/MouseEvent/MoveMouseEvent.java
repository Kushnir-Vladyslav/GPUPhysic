package org.example.Event.MouseEvent;

import com.jopencl.event.Event;
import org.example.Event.EventDataStructs.MousePosition;
import com.jopencl.event.EventManager;

public class MoveMouseEvent extends Event<MousePosition> {
    public static String EVENT_NAME = "moveMouseEvent";

    static {
        EventManager.getInstance().addEvent(
                EVENT_NAME,
                new MoveMouseEvent());
    }
}
