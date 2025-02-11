package org.example;


import org.example.FileFinder.ClassFinder;
import org.example.FileFinder.LibraryFinder;
import org.example.JavaFX.JavaFX;
import org.example.Library.LibraryManager;

public class Main {
    public static void main(String[] args) {
//        particles = new Particle[256];
//
//        for(int i = 0; i < 256; i++) {
//            particles[i] = new Particle();
//        }


        try {
            System.out.println(ClassFinder.findClass("StructsLibrary"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//        System.out.println(LibraryManager.getInstance().getLibrary("Structs"));

        JavaFX.main(args);

//        OpenCL oCL = new OpenCL();
//        oCL.call();
    }


}