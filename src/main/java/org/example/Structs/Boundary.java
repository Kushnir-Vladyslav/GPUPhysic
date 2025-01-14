package org.example.Structs;

import org.example.GLOBAL_STATE;

public class Boundary {
    float width;
    float height;
    float borderThickness;
    float sphereX;
    float sphereY;
    float sphereRadius;

    public Boundary () {
        width = GLOBAL_STATE.WorkZoneWidth;
        height = GLOBAL_STATE.WorkZoneHeight;

        borderThickness = 2;

        sphereX = width * 0.5f;
        sphereY = height * 0.3f;

        sphereRadius = height * 0.71f;
    }

}
