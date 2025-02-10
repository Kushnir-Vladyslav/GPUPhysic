package org.example.FileFinder;

import org.example.Main;

import java.util.Arrays;
import java.util.List;

public class FileFinder {
    private  List<String> DEFAULT_EXTENSIONS;
    private  List<String> DEFAULT_SEARCH_PATHS;

    public FileFinder () {
        DEFAULT_EXTENSIONS = Arrays.asList("");
        DEFAULT_SEARCH_PATHS = Arrays.asList("");
    }

    public FileFinder (String[] extension, String[] paths) {
        DEFAULT_EXTENSIONS = Arrays.asList(extension);
        DEFAULT_SEARCH_PATHS = Arrays.asList(paths);

        Main.class.getResource("");
    }


}
