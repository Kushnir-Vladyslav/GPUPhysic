package org.example.Library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class Library {
    protected String libraryCode;

    Library (Path libraryPath) {
        loadLibrary(libraryPath);
        modifyLibrary();
    }

    private void loadLibrary(Path libraryPath) {
        try {
            libraryCode = Files.readString(libraryPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed read: " + libraryPath, e);
        }
    }

    protected abstract void modifyLibrary();

    public String getLibraryCode () {
        return libraryCode;
    }
}
