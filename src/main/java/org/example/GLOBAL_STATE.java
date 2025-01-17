package org.example;

import org.example.BufferControl.BufferManager;
import org.example.Kernel.KernelManager;
import org.example.Structs.*;

public class GLOBAL_STATE {
    //початковий розмір вікна, може змінюватись користувачем
    private static int ScreenWidth = 256 * 2;
    private static int ScreenHeight = 256 * 2;

    //розмір робочої областіж
    public static int WorkZoneWidth = 400;
    public static int WorkZoneHeight = 400;

    public static OpenClContext openClContext = new OpenClContext();
    public static BufferManager bufferManager = new BufferManager();
    public static KernelManager kernelManager = new KernelManager();

    public static Particle[] particles;
    public static Canvas canvas;
    public static Boundary boundary;
    public static CursorPosition cursorPosition;

    public static boolean IsUpdateWait = false;

    public static OpenCL OpenClTask;
    public static Thread OpenClThread;


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
