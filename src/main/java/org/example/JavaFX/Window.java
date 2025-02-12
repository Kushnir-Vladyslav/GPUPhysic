package org.example.JavaFX;

import org.example.Event.EventManager;
import org.example.Event.WindowResizeEvent.WindowHeightResizeEvent;
import org.example.Event.WindowResizeEvent.WindowWidthResizeEvent;

public class Window {
    private static Window window;
    
    //початковий розмір вікна, може змінюватись користувачем
    private int ScreenWidth;
    private int ScreenHeight;

    //масив пікселів що відображаються на екрані
    public int[] pixels;

    public static Window getInstance() {
        if (window == null) {
            window = new Window();
        }

        return window;
    }

    private Window () {
        ScreenWidth = 256 * 4;
        ScreenHeight = 256 * 3;

        WindowWidthResizeEvent windowWidthResizeEvent =
                EventManager.
                        getInstance().
                        getEvent(WindowWidthResizeEvent.EVENT_NAME);

        windowWidthResizeEvent.subscribe(
                this,
                (event) -> {
                    ScreenWidth = event;
                }
        );

        WindowHeightResizeEvent windowHeightResizeEvent =
                EventManager.
                        getInstance().
                        getEvent(WindowHeightResizeEvent.EVENT_NAME);

        windowHeightResizeEvent.subscribe(
                this,
                (event) -> {
                    ScreenHeight = event;
                }
        );
    }

    public int getScreenWidth() {
        return ScreenWidth;
    }

    public int getScreenHeight() {
        return ScreenHeight;
    }

    public void setScreenWidth(int Width) {
        ScreenWidth = Width;
    }

    public void setScreenHeight(int Height) {
        ScreenHeight = Height;
    }

}
