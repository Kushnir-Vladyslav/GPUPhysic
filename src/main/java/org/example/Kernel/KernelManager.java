package org.example.Kernel;

import java.util.HashMap;
import java.util.Map;

/**
 * Клас для управління OpenCL ядрами.
 * Дозволяє додавати, отримувати та видаляти ядра.
 */
public class KernelManager {
    private static KernelManager kernelManager;

    Map<String, Kernel> kernelMap; // Мапа для зберігання ядер

    public static KernelManager getInstance() {
        if (kernelManager == null) {
            kernelManager = new KernelManager();
        }

        return kernelManager;
    }

    private KernelManager () {
        kernelMap = new HashMap<>();
    }

    /**
     * Додає ядро до менеджера.
     *
     * @param name   Назва ядра.
     * @param kernel Об'єкт ядра.
     */
    public void addKernel (String name, Kernel kernel) {
        kernelMap.put(name, kernel);
    }

    /**
     * Повертає ядро за назвою.
     *
     * @param name Назва ядра.
     * @return Об'єкт ядра.
     */
    public Kernel getKernel(String name) {
        return kernelMap.get(name);
    }

    /**
     * Видаляє ядро за назвою.
     *
     * @param name Назва ядра.
     * @throws IllegalArgumentException Якщо ядро з такою назвою не існує.
     */
    public void removeKernel (String name) {
        if (!kernelMap.containsKey(name)) {
            throw new IllegalArgumentException("A kernel with name \"" + name + "\" does not exist.");
        } else {
            kernelMap.remove(name);
        }
    }

    /**
     * Звільняє всі ресурси ядер.
     */
    public void destroy () {
        for(Kernel kernel : kernelMap.values()) {
            kernel.destroy();
        }
        kernelMap.clear();
    }
}
