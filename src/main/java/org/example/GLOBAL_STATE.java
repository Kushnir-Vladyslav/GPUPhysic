package org.example;

import org.example.Structs.CursorPosition;
import org.example.Structs.OpenClContext;
import org.example.Structs.Particle;

public class GLOBAL_STATE {
    //початковий розмір вікна, може змінюватись користувачем
    private static int ScreenWidth = 400;
    private static int ScreenHeight = 400;

    //розмір робочої областіж
    public static int WorkZoneWidth = 400;
    public static int WorkZoneHeight = 400;

    public static Particle[] particles;
    public static CursorPosition cursorPosition = new CursorPosition();

    public static boolean IsUpdateWait = false;

    public static OpenCL OpenClTask;
    public static Thread OpenClThread;

    public static OpenClContext openClContext = new OpenClContext();

    //масив пікселів що відображаються на екрані
    public static int[] Pixels;

    //масив глибин пікселів
    public static float [] DepthBuffer;

    //Час від початку запуску програми
    public static float Time = 0.f;

    //натисненні клавіші
    public static boolean IsUp = false;
    public static boolean IsLeft = false;
    public static boolean IsRight = false;
    public static boolean IsDown = false;

    public static int getScreenWidth() {
        return ScreenWidth;
    }

    public static int getScreenHeight() {
        return ScreenHeight;
    }

    public static void setScreenWidth(int Width) {
        ScreenWidth = Width;
    }

    public static void setScreenHeight(int Height) {
        ScreenHeight = Height;
    }

}
