package org.example.Structs;

import org.example.GLOBAL_STATE;

public class Boundary {
    public float width;
    public float height;
    public float borderThickness;
    public float sphereX;
    public float sphereY;
    public float sphereRadius;

    public Boundary () {
        width = GLOBAL_STATE.WorkZoneWidth;
        height = GLOBAL_STATE.WorkZoneHeight;

        borderThickness = 2;

        sphereX = width * 0.5f;
        sphereY = height * 0.3f;

        sphereRadius = height * 0.71f;
    }

}
