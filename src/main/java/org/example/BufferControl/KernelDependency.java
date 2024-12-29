package org.example.BufferControl;

import org.example.Kernel.Kernel;

public class KernelDependency {
    Kernel targetKernel;
    int numberArg;

    KernelDependency (Kernel targetKernel, int numberArg) {
        this.targetKernel = targetKernel;
        this.numberArg = numberArg;
    }
}
