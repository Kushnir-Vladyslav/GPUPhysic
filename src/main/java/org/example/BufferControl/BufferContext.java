package org.example.BufferControl;

import org.example.Kernel.Kernel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;
import static org.example.GLOBAL_STATE.openClContext;

public abstract class BufferContext<T, K extends Buffer> {
    protected T hostBuffer;
    protected K nativeBuffer;
    protected long clBuffer;
    protected int length;

    MemoryAccessControl memoryAccessControl;
    PointerBuffer pointerBuffer = MemoryUtil.memAllocPointer(1);
    Vector<KernelDependency> kernels = new Vector<>();

    public void addKernel (Kernel kernel, int numberArg) {
        kernels.add(new KernelDependency(kernel, numberArg));
        setNewArg(kernels.lastElement());
    }

    protected void destroy () {
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

    protected void setNewArgs() {
        for (KernelDependency KD : kernels) {
            setNewArg(KD);
        }
    }

    protected void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(KD.targetKernel.getKernel(), KD.numberArg, pointerBuffer.put(0, clBuffer).rewind());
    }

    public T getData() {
        return hostBuffer;
    }
    public int getLength() {
        return length;
    }

    public void readBuffer() {
        if (nativeBuffer instanceof FloatBuffer nB) {
            float[] hB = (float[]) hostBuffer;
            CL10.clEnqueueReadBuffer(openClContext.commandQueue, clBuffer, true, 0L,
                    nB, null, null);
            nB.get(hB);
        } else if (nativeBuffer instanceof IntBuffer nB) {
            int[] hB = (int[]) hostBuffer;
            CL10.clEnqueueReadBuffer(openClContext.commandQueue, clBuffer, true, 0L,
                    nB, null, null);
            nB.get(hB);
        }
    }

    public void appendData(T data) {
        if (!(this.getClass().equals(GlobalBuffer.class) && ((GlobalBuffer) this).isDynamic)) {
            throw new IllegalStateException("Cannot append to static buffer");
        }
        ((GlobalBuffer) this).addToEnd(data);
    }

    protected void checkClBuffer() {
        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }
    }

    public abstract void update (T newDats);
    public abstract void update ();
    public abstract void resize (int newSize);
}
