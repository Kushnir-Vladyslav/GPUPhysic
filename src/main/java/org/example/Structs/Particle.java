package org.example.Structs;

public class Particle {
    public float xPosition;
    public float yPosition;

    public float radius;

    public float xSpeed;
    public float ySpeed;

    public float xAcceleration;
    public float yAcceleration;

    public Particle () {}

    public Particle (float x, float y) {
        xPosition = x;
        yPosition = y;

        radius = 1;

        xSpeed = 0;
        ySpeed = 0;

        xAcceleration = 0;
        yAcceleration = 0;
    }
}
