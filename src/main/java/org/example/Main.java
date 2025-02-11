package org.example;


import org.example.FileFinder.ResourceFileFinder;
import org.example.JavaFX.JavaFX;

public class Main {
    public static void main(String[] args) {
//        particles = new Particle[256];
//
//        for(int i = 0; i < 256; i++) {
//            particles[i] = new Particle();
//        }

        ResourceFileFinder.main(args);

        JavaFX.main(args);

//        OpenCL oCL = new OpenCL();
//        oCL.call();
    }


}