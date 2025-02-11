package org.example.Kernel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourcesFoldersList {
    private static List<String> listFolders;

    public static List<String> getList () {
        if (listFolders == null) {
            generateList();
        }

        return listFolders;
    }

    private static void generateList() {
        listFolders = new ArrayList<>();

        // Отримуємо URL до кореневого каталогу ресурсів
        URL resourceBaseUrl = ResourcesFoldersList.class.getClassLoader().getResource("org/example/Kernel");

        if (resourceBaseUrl == null) {
            throw new IllegalStateException("Cannot find resource base path.");
        }

        System.out.println(resourceBaseUrl);

        Path basePath;

        try {
            basePath = Paths.get(resourceBaseUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            listFolders = Files.walk(basePath, 1) // Обмежуємо глибину пошуку
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
