package org.example.Kernel;

import java.util.HashMap;
import java.util.Map;

public class KernelManager {
    Map<String, Kernel> kernelMap;

    public KernelManager () {
        kernelMap = new HashMap<>();
    }

    public void addKernel (String name, Kernel kernel) {
        kernelMap.put(name, kernel);
    }

    public Kernel getKernel(String name) {
        return kernelMap.get(name);
    }

    public void destroy () {
        for(Kernel kernel : kernelMap.values()) {
            kernel.destroy();
        }
        kernelMap.clear();
    }
}
