package org.example.FileFinder;

import org.example.Main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class FileFinder {
    public static List<Path> findFile(String fileName){
        return findFileWithExtension(fileName, Arrays.asList(""));
    }


    public static List<Path> findFileWithExtension(String fileName, List<String> extensions) {
        List<Path> foundFiles = new ArrayList<>();

        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("\\");

            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                Path resourcePath;

                try {
                    resourcePath = Paths.get(resourceUrl.toURI());
                } catch (Exception e) {
                    continue;
                }

                for (String extension : extensions) {
                    foundFiles.addAll(walkResource(fileName + extension, resourcePath));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting resources.", e);
        }

        return foundFiles;
    }

    private static List<Path> walkResource(String fileName, Path resourcePath) {
        List<Path> foundFiles = new ArrayList<>();

        try (var stream = Files.walk(resourcePath)) {
            foundFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error while walk resources.", e);
        }

        return foundFiles;
    }
}
