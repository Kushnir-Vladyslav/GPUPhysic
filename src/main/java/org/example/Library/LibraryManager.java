package org.example.Library;

import org.example.FileFinder.ClassFinder;

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

        String className = libraryName + "Library";
        try {
            Class<?> clazz = ClassFinder.findClass(className);
            if (Library.class.isAssignableFrom(clazz)) {
                return (Library) clazz.getDeclaredConstructor().newInstance();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class of " + className + " not found, creating a dynamic library.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate library class: " + className, e);
        }

        try {
            return new DynamicLibrary(libraryName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create library named: " + libraryName, e);
        }
    }
}
