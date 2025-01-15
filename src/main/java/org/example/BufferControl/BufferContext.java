package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.util.Vector;

public abstract class BufferContext <K extends TypeOfBuffer> {
    protected final Class<K> type;
    protected long clBuffer;

    PointerBuffer pointerBuffer = MemoryUtil.memAllocPointer(1);
    Vector<KernelDependency> kernels = new Vector<>();

    protected K nativeBuffer;

    public BufferContext (Class<K> type) {
        this.type = type;
    }

    public void addKernel (long kernel, int numberArg) {
        kernels.add(new KernelDependency(kernel, numberArg));
        setNewArg(kernels.lastElement());
    }

    protected void setNewArgs() {
        for (KernelDependency KD : kernels) {
            setNewArg(KD);
        }
    }

    protected void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(
                KD.targetKernel,
                KD.numberArg,
                pointerBuffer.put(0, clBuffer).rewind()
        );
    }

    public Class<K> getType() {
        return type;
    }

    protected void checkClBuffer() {
        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }
    }

    protected class KernelDependency {
        public long targetKernel;
        public int numberArg;

        public KernelDependency (long targetKernel, int numberArg) {
            this.targetKernel = targetKernel;
            this.numberArg = numberArg;
        }
    }

    protected void removeKernel(long kernel) {
        kernels.removeIf(value -> value.targetKernel == kernel);
    }

    protected void destroy () {
        if (clBuffer != 0) {
            CL10.clReleaseMemObject(clBuffer);
            clBuffer = 0;
        }
        if (nativeBuffer != null) {
            nativeBuffer.destroy();
            nativeBuffer = null;
        }
        if (pointerBuffer != null)  {
            MemoryUtil.memFree(pointerBuffer);
            pointerBuffer = null;
        }
        if (kernels != null) {
            kernels.clear();
        }
    }
}
