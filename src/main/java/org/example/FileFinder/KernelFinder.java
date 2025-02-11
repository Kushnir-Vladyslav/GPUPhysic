package org.example.FileFinder;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class KernelFinder extends FileFinder{
    private static List<String> listOfExtensions = Arrays.asList("", ".c", ".cl", ".txt");

    public static List<Path> findFile(String fileName){
        return findFileWithExtension(fileName, listOfExtensions);
    }
}
