package org.example;

import org.example.Structs.Particle;

import static org.example.GLOBAL_STATE.*;

public class Main {
    public static void main(String[] args) {
        particles = new Particle[256];

        for(int i = 0; i < 256; i++) {
            particles[i] = new Particle();
        }

        JavaFX.main(args);
    }
}