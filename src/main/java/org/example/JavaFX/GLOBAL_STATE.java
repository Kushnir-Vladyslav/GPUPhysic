package org.example.JavaFX;

import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.Boundary;
import org.example.OpenCL.OpenCL;
import org.example.OpenCL.OpenClContext;
import org.example.Structs.*;

public class GLOBAL_STATE {
    //початковий розмір вікна, може змінюватись користувачем
    private static int ScreenWidth = 256 * 4;
    private static int ScreenHeight = 256 * 3;

    public static boolean IsUpdateWait = false;

    public static OpenCL OpenClTask;
    public static Thread OpenClThread;


    //масив пікселів що відображаються на екрані
    public static int[] Pixels;

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
