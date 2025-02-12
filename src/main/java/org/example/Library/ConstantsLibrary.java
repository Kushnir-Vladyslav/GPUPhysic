package org.example.Library;

import org.example.FileFinder.LibraryFinder;
import org.example.Structs.Canvas;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConstantsLibrary extends Library{

    public ConstantsLibrary () {
        super(getPath());
    }

    private static Path getPath () {
        URL URLLibrary = StructsLibrary.class.getResource("../Kernel/Library/Constants.h");


        try {
            return Paths.get(URLLibrary.toURI());
        } catch (URISyntaxException e) {
            System.err.println("Library \"Constants.h\" not found");
        }

        try {
            return LibraryFinder.findFile("Constants.h").get(0);
        } catch (Exception e) {
            throw new RuntimeException("Library \"Constants.h\" not found", e);
        }
    }

    @Override
    protected void modifyLibrary() {
        libraryCode = libraryCode.replace("VALUE_OF_GRAVITY", (Canvas.getCanvasHeight() / 200000.f) + "f");
    }
}
