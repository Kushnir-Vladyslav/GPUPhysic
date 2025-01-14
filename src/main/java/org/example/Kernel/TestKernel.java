package org.example.Kernel;

import org.example.BufferControl.*;
import org.example.Structs.OpenClContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.lang.management.MemoryManagerMXBean;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.bufferManager;
import static org.example.GLOBAL_STATE.openClContext;

public class TestKernel extends Kernel{

    final int VECTOR_SIZE = 16_000_000; // Збільшено розмір
    final int LOCAL_WORK_SIZE = 256; // Оптимальний розмір локальної групи

    float[] fb = new float[VECTOR_SIZE];
    float[] sb = new float[VECTOR_SIZE];

    GlobalStaticBuffer<FloatBufferType> fBuffer;
    GlobalStaticBuffer<FloatBufferType> sBuffer;
    GlobalStaticBuffer<FloatBufferType> rBuffer;

    LocalBuffer<FloatBufferType> fLocal;
    LocalBuffer<FloatBufferType> sLocal;

    SingleValueBuffer<IntBufferType> singleV;

    PointerBuffer global;
    PointerBuffer local;

    public TestKernel () {
        createKernel("TestKernel", "Structs");

        for (int i = 0; i < VECTOR_SIZE; i++) {
            fb[i] = (float) Math.random();
            sb[i] = (float) Math.random();
        }

        // Глобальна память
        fBuffer = bufferManager.createBuffer("fBuffer", GlobalStaticBuffer.class, FloatBufferType.class);
        sBuffer = bufferManager.createBuffer("sBuffer", GlobalStaticBuffer.class, FloatBufferType.class);
        rBuffer = bufferManager.createBuffer("rBuffer", GlobalStaticBuffer.class, FloatBufferType.class);

        fBuffer.init(VECTOR_SIZE, CL10.CL_MEM_READ_ONLY);
        sBuffer.init(VECTOR_SIZE, CL10.CL_MEM_READ_ONLY);
        rBuffer.init(VECTOR_SIZE, CL10.CL_MEM_WRITE_ONLY);

        fBuffer.setData(fb);
        sBuffer.setData(sb);

        fBuffer.addKernel(kernel, 0);
        sBuffer.addKernel(kernel, 1);
        rBuffer.addKernel(kernel, 2);

        // Локальна пам'ять
        long localMemSize = LOCAL_WORK_SIZE * Float.BYTES;

        // Локальна пам'ять як аргументи

        fLocal = bufferManager.createBuffer("fLocal", LocalBuffer.class, FloatBufferType.class);
        sLocal = bufferManager.createBuffer("sLocal", LocalBuffer.class, FloatBufferType.class);

        fLocal.init(localMemSize);
        sLocal.init(localMemSize);

        fLocal.addKernel(kernel, 3);
        sLocal.addKernel(kernel, 4);

        // Одинарний параметр
        singleV = bufferManager.createBuffer("singleV", SingleValueBuffer.class, IntBufferType.class);

        singleV.init(new int[] {5});

        singleV.addKernel(kernel, 5);

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
        float[] rb = (float[]) rBuffer.readData();

        // Перевірка результату
        for (int i = 1; i < 10; i++) {
            System.out.printf("%.2f + %.2f = %.2f%n",
                    fb[VECTOR_SIZE - i], sb[VECTOR_SIZE - i], rb[VECTOR_SIZE - i]);
        }
    }

    @Override
    public void destroy() {
        MemoryUtil.memFree(local);
        MemoryUtil.memFree(global);

        super.destroy();
    }
}
