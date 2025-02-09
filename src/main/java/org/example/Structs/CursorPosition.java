package org.example.Structs;

import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;
import org.example.EventManager.*;

import static org.example.JavaFX.GLOBAL_STATE.WorkZoneWidth;
import static org.example.JavaFX.GLOBAL_STATE.bufferManager;

public class CursorPosition {
    public float x;
    public float y;
    public float radius;

    private final float sizeOfCursor = WorkZoneWidth / 25.f;

    SingleValueBuffer<CursorPositionBuffer> cursorBuffer;

    public CursorPosition () {
        x = 0;
        y = 0;
        radius = 0;

        LeftMousePressEvent leftMousePressEvent = EventManager.
                getEventManager().
                getEvent(LeftMousePressEvent.EVENT_NAME);

        leftMousePressEvent.subscribe(
                this,
                (event) -> {
                    this.x = event.x;
                    this.y = event.y;
                    radius = sizeOfCursor;

                    update();
                }
        );

        MoveMouseEvent moveMouseEvent = EventManager.
                getEventManager().
                getEvent(MoveMouseEvent.EVENT_NAME);

        moveMouseEvent.subscribe(
                this,
                (event) -> {
                    if (radius != 0) {
                        this.x = event.x;
                        this.y = event.y;

                        update();
                    }
                }
        );

        LeftMouseReleaseEvent leftMouseReleaseEvent = EventManager.
                getEventManager().
                getEvent(LeftMouseReleaseEvent.EVENT_NAME);

        leftMouseReleaseEvent.subscribe(
                this,
                (event) -> {
                    if (radius != 0) {
                        radius = 0;

                        update();
                    }
                }
        );

        update();
    }

    public void update () {
        if (cursorBuffer == null) {
            if (bufferManager.isExist("CursorBuffer")) {
                cursorBuffer = bufferManager.getBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);
            } else {
                cursorBuffer = bufferManager.createBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);
                cursorBuffer.init();
            }
        }

        cursorBuffer.setData(this);
    }
}
