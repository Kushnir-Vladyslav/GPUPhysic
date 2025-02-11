package org.example.FileFinder;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class LibraryFinder extends FileFinder{
    private static List<String> listOfExtensions = Arrays.asList("", ".h", ".txt");

    public static List<Path> findFile(String fileName){
        return findFileWithExtension(fileName, listOfExtensions);
    }
}
