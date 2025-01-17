package org.example.Structs;

import static org.example.GLOBAL_STATE.WorkZoneHeight;
import static org.example.GLOBAL_STATE.WorkZoneWidth;

public class Particle {
    float xPosition;
    float yPosition;

    float xSpeed;
    float ySpeed;

    float radius;

    public Particle (float x, float y) {
        xPosition = (float) Math.random() * WorkZoneWidth;
        yPosition = (float) Math.random() * WorkZoneHeight;

        xSpeed = (float) Math.random();
        ySpeed = (float) Math.random();

        radius = 2;
    }
}
