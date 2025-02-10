package org.example.Library;

public abstract class Library {
    protected String libraryCode;

    Library (String libraryPath) {
        loadLibrary(libraryPath);
        modifyLibrary();
    }

    private void loadLibrary(String libraryPath) {

    }

    protected abstract void modifyLibrary();

    public String getLibraryCode () {
        return libraryCode;
    }
}
