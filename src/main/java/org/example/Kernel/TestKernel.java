package org.example.Kernel;

import org.example.Structs.OpenClContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class TestKernel extends Kernel{

    final int VECTOR_SIZE = 16_000_000; // Збільшено розмір
    final int LOCAL_WORK_SIZE = 256; // Оптимальний розмір локальної групи

    FloatBuffer aBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);
    FloatBuffer bBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);
    FloatBuffer resultBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);

    long clABuffer;
    long clBBuffer;
    long clResultBuffer;

    PointerBuffer global;
    PointerBuffer local;

    public TestKernel () {
        createKernel("TestKernel");

        for (int i = 0; i < VECTOR_SIZE; i++) {
            aBuffer.put(i, (float) Math.random());
            bBuffer.put(i, (float) Math.random());
        }
        aBuffer.rewind();
        bBuffer.rewind();

        clABuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                aBuffer, null);
        clBBuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                bBuffer, null);
        clResultBuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_WRITE_ONLY,
                VECTOR_SIZE * Float.BYTES, null);

        // Локальна пам'ять
        long localMemSize = LOCAL_WORK_SIZE * Float.BYTES * 2;

        // Встановлення аргументів
        PointerBuffer clMemBuffer = MemoryUtil.memAllocPointer(1);

        clMemBuffer.put(0, clABuffer);
        CL10.clSetKernelArg(kernel, 0, clMemBuffer);

        clMemBuffer.put(0, clBBuffer);
        CL10.clSetKernelArg(kernel, 1, clMemBuffer);

        clMemBuffer.put(0, clResultBuffer);
        CL10.clSetKernelArg(kernel, 2, clMemBuffer);

        // Локальна пам'ять як аргументи
        CL10.clSetKernelArg(kernel, 3, localMemSize);
        CL10.clSetKernelArg(kernel, 4, localMemSize);

        long globalWorkSize = (long) Math.ceil(VECTOR_SIZE / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE;

        global = MemoryUtil.memAllocPointer(1).put(globalWorkSize).rewind();
        local = MemoryUtil.memAllocPointer(1).put(LOCAL_WORK_SIZE).rewind();
    }

    @Override
    public void run() {
        CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 1, null,
                global, local,
                null, null
        );

        // Читання результату
        CL10.clEnqueueReadBuffer(openClContext.commandQueue, clResultBuffer, true, 0,
                resultBuffer, null, null);

        // Перевірка результату
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.2f + %.2f = %.2f%n",
                    aBuffer.get(i), bBuffer.get(i), resultBuffer.get(i));
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        CL10.clReleaseMemObject(clABuffer);
        CL10.clReleaseMemObject(clBBuffer);
        CL10.clReleaseMemObject(clResultBuffer);

        MemoryUtil.memFree(aBuffer);
        MemoryUtil.memFree(bBuffer);
        MemoryUtil.memFree(resultBuffer);
    }
}
