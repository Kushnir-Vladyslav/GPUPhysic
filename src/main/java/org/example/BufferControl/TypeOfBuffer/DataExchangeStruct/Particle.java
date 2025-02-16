package org.example.BufferControl.TypeOfBuffer.DataExchangeStruct;

public class Particle {
    public float xPosition;
    public float yPosition;

    public float radius;

    public float xSpeed;
    public float ySpeed;

    public int isSleep;
    public float sleepTimer;

    public Particle () {}

    public Particle (float x, float y) {
        xPosition = x;
        yPosition = y;

        radius = 2;

        xSpeed = 0;
        ySpeed = 0;

        isSleep = 0;
        sleepTimer = 0;
    }
}
