package org.example.BufferControl;

import org.example.Kernel.Kernel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.util.Vector;

public abstract class BufferContext<T, K extends Buffer> {
    T hostBuffer;
    K nativeBuffer;
    long clBuffer;
    MemoryAccessControl memoryAccessControl;
    PointerBuffer pointerBuffer = MemoryUtil.memAllocPointer(1);
    Vector<KernelDependency> kernels = new Vector<>();

    public void addKernel (Kernel kernel, int numberArg) {
        kernels.add(new KernelDependency(kernel, numberArg));
        setNewArg(kernels.lastElement());
    }

    public void destroy () {
        if (clBuffer != 0) {
            CL10.clReleaseMemObject(clBuffer);
            clBuffer = 0;
        }
        if (nativeBuffer != null) {
            MemoryUtil.memFree(nativeBuffer);
            nativeBuffer = null;
        }
        if (pointerBuffer != null) {
            MemoryUtil.memFree(pointerBuffer);
            pointerBuffer = null;
        }
    }

    public void setNewArgs() {
        for (KernelDependency KD : kernels) {
            setNewArg(KD);
        }
    }

    public void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(KD.targetKernel.getKernel(), KD.numberArg, pointerBuffer.put(0, clBuffer).rewind());
    }

    public abstract void update (T newHostBuffer);
    public abstract void resize (int newSize);
}
