package org.example.Library;

import org.example.FileFinder.LibraryFinder;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StructsLibrary extends Library{
    public StructsLibrary() {
        super(getPath());
    }

    private static Path getPath () {
        URL URLLibrary = StructsLibrary.class.getResource("../Kernel/Library/Structs.h");


        try {
            return Paths.get(URLLibrary.toURI());
        } catch (URISyntaxException e) {
            System.err.println("Library \"Struct.h\" not found");
        }

        try {
            return LibraryFinder.findFile("Struct.h").get(0);
        } catch (Exception e) {
            throw new RuntimeException("Library \"Struct.h\" not found", e);
        }
    }

    @Override
    protected void modifyLibrary() {

    }
}
