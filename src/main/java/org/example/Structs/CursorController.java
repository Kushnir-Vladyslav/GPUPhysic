package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;
import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.CursorPosition;
import org.example.Event.EventManager;
import org.example.Event.MouseEvent.LeftMousePressEvent;
import org.example.Event.MouseEvent.LeftMouseReleaseEvent;
import org.example.Event.MouseEvent.MoveMouseEvent;

public class CursorController {
    private static CursorController cursorController;

    private final float sizeOfCursor = Canvas.getCanvasHeight() / 25.f;

    SingleValueBuffer<CursorPositionBuffer> cursorBuffer;

    CursorPosition cursorPosition;

    BufferManager bufferManager;

    public static CursorController getInstance() {
        if (cursorController == null) {
            cursorController = new CursorController();
        }

        return cursorController;
    }

    private CursorController () {
        cursorPosition = new CursorPosition();

        cursorPosition.x = 0;
        cursorPosition.y = 0;
        cursorPosition.radius = 0;

        bufferManager = BufferManager.getInstance();

        LeftMousePressEvent leftMousePressEvent = EventManager.
                getInstance().
                getEvent(LeftMousePressEvent.EVENT_NAME);

        leftMousePressEvent.subscribe(
                this,
                (event) -> {
                    cursorPosition.x = event.x;
                    cursorPosition.y = event.y;
                    cursorPosition.radius = sizeOfCursor;

                    update();
                }
        );

        MoveMouseEvent moveMouseEvent = EventManager.
                getInstance().
                getEvent(MoveMouseEvent.EVENT_NAME);

        moveMouseEvent.subscribe(
                this,
                (event) -> {
                    if (cursorPosition.radius != 0) {
                        cursorPosition.x = event.x;
                        cursorPosition.y = event.y;

                        update();
                    }
                }
        );

        LeftMouseReleaseEvent leftMouseReleaseEvent = EventManager.
                getInstance().
                getEvent(LeftMouseReleaseEvent.EVENT_NAME);

        leftMouseReleaseEvent.subscribe(
                this,
                (event) -> {
                    if (cursorPosition.radius != 0) {
                        cursorPosition.radius = 0;

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

        cursorBuffer.setData(cursorPosition);
    }
}
