package org.example.Structs;

import static org.example.GLOBAL_STATE.*;

public class Particles {


    public class Particle {
        float xPosition;
        float yPosition;

        float xSpeed;
        float ySpeed;

        float radius;

        public Particle() {
            xPosition = (float) Math.random() * WorkZoneWidth;
            yPosition = (float) Math.random() * WorkZoneHeight;

            xSpeed = (float) Math.random();
            ySpeed = (float) Math.random();

            radius = 5;
        }
    }
}
