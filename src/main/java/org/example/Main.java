package org.example;

import org.example.Structs.Particles;

import static org.example.GLOBAL_STATE.*;

public class Main {
    public static void main(String[] args) {
        particles = new Particles[256];

        for(int i = 0; i < 256; i++) {
            particles[i] = new Particles();
        }

        JavaFX.main(args);
    }
}