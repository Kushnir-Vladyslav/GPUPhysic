package org.example.Library;

import java.util.HashMap;
import java.util.Map;

public class LibraryManager {
    private static LibraryManager libraryManager;

    Map<String, Library> libraryMap;

    public static LibraryManager getInstance() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager();
        }

        return libraryManager;
    }

    private LibraryManager () {
        libraryMap = new HashMap<>();
    }

    public String getLibrary (String nameLibrary) {
        return libraryMap.computeIfAbsent(nameLibrary, this::loadLibrary).getLibraryCode();
    }

    private Library loadLibrary(String libraryName) {
        // Спочатку шукаємо клас бібліотеки
        String className = libraryName + "Library";
        try {
            Class<?> clazz = Class.forName(className);
            if (Library.class.isAssignableFrom(clazz)) {
                return (Library) clazz.getDeclaredConstructor().newInstance();
            }
        } catch (ClassNotFoundException e) {
            // Клас не знайдено, створюємо динамічну бібліотеку
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate library class: " + className, e);
        }

        // Якщо клас не знайдено, створюємо динамічну бібліотеку
        return new DynamicLibrary(libraryName);
    }
}
