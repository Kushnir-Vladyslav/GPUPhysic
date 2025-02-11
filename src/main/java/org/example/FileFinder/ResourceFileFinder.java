package org.example.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceFileFinder {

    /**
     * Шукає файл за ім'ям у всіх ресурсах.
     *
     * @param fileName Ім'я файлу (наприклад, "TestKernel.cl").
     * @return Список шляхів до знайдених файлів.
     */
    public static List<String> findFileInResources(String fileName) {
        List<String> foundFiles = new ArrayList<>();

        try {
            // Отримуємо кореневий URL ресурсів
            Enumeration<URL> resources = ResourceFileFinder.class.getClassLoader().getResources("");

            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                Path resourcePath;


                try {
                    resourcePath = Paths.get(resourceUrl.toURI());
                } catch (URISyntaxException e) {
                    continue; // Пропускаємо невалідні URL
                }

                // Рекурсивно обходимо всі файли в ресурсах
                try (var stream = Files.walk(resourcePath)) {
                    List<Path> matchingFiles = stream
                            .filter(Files::isRegularFile)
                            .filter(path -> path.getFileName().toString().equals(fileName))
                            .collect(Collectors.toList());

                    // Додаємо знайдені файли до результату
                    for (Path file : matchingFiles) {
                        String relativePath = resourcePath.relativize(file).toString();
                        foundFiles.add(relativePath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Помилка при обході ресурсів", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Помилка при отриманні ресурсів", e);
        }

        return foundFiles;
    }

    public static void main(String[] args) {
        String fileName = "TestKernel.cl"; // Шукаємо файл за ім'ям
        List<String> foundFiles = findFileInResources(fileName);

        if (foundFiles.isEmpty()) {
            System.out.println("Файл не знайдено: " + fileName);
        } else {
            System.out.println("Файл знайдено у наступних шляхах:");
            foundFiles.forEach(System.out::println);
        }
    }
}