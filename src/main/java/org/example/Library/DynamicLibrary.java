package org.example.Library;

import org.example.FileFinder.FileFinder;
import org.example.FileFinder.LibraryFinder;

public class DynamicLibrary extends Library{

    public DynamicLibrary (String libraryName) {
        super(LibraryFinder.findFile(libraryName).get(0));
    }

    @Override
    protected void modifyLibrary() {

    }
}
