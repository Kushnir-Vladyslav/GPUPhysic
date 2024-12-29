package org.example;

import javafx.concurrent.Task;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.example.GLOBAL_STATE.*;


public class OpenCL extends Task<Void> {
    static public FloatBuffer particlesBuffer;

    static public Long clParticlesBuffer;

//    static public long device;
//    static public long context;
//    static public long commandQueue;

    final static public int LOCAL_WORK_SIZE = 256; // Оптимальний розмір локальної групи

    @Override
    public Void call () {
        org.lwjgl.system.Configuration.OPENCL_EXPLICIT_INIT.set(true);
        CL.create();



        FloatBuffer aBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);
        FloatBuffer bBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);
        FloatBuffer resultBuffer = MemoryUtil.memAllocFloat(VECTOR_SIZE);

        particlesBufferFiller();

//        long startTime = System.nanoTime();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Отримання платформ та пристроїв (без змін)
            IntBuffer platformCount = stack.mallocInt(1);
            CL10.clGetPlatformIDs(null, platformCount);

            PointerBuffer platforms = stack.mallocPointer(platformCount.get(0));
            CL10.clGetPlatformIDs(platforms, (IntBuffer) null);

            // Отримання інформації про платформи
//            for (int i = 0; i < platformCount.get(0); i++) {
//                long platformId = platforms.get(i);
//
//                // Запитуємо розмір буфера для зберігання назви платформи
//                PointerBuffer sizeBuffer = stack.mallocPointer(1);
//                CL10.clGetPlatformInfo(platformId, CL10.CL_PLATFORM_NAME, (ByteBuffer) null, sizeBuffer);
//
//                // Створюємо буфер для зчитування назви
//                ByteBuffer nameBuffer = stack.malloc((int) sizeBuffer.get(0));
//                CL10.clGetPlatformInfo(platformId, CL10.CL_PLATFORM_NAME, nameBuffer, null);
//
//                // Перетворюємо буфер у строку
//                String platformName = MemoryUtil.memUTF8(nameBuffer);
//                System.out.println("Platform " + i + ": " + platformName);
//            }

            long platform = platforms.get(0);

            IntBuffer deviceCount = stack.mallocInt(1);
            CL10.clGetDeviceIDs(platform, CL10.CL_DEVICE_TYPE_GPU, null, deviceCount);

            PointerBuffer devices = stack.mallocPointer(deviceCount.get(0));
            CL10.clGetDeviceIDs(platform, CL10.CL_DEVICE_TYPE_GPU, devices, (IntBuffer) null);

            openClContext.device = devices.get(0);

            // Додаткова діагностика пристрою
//            try (MemoryStack infoStack = MemoryStack.stackPush()) {
//                PointerBuffer paramSize = infoStack.mallocPointer(1);
//                CL10.clGetDeviceInfo(device, CL10.CL_DEVICE_MAX_COMPUTE_UNITS, (IntBuffer)null, paramSize);
//
//                IntBuffer paramValue = infoStack.mallocInt((int) paramSize.get(0));
//                CL10.clGetDeviceInfo(device, CL10.CL_DEVICE_MAX_COMPUTE_UNITS, paramValue, null);
//
//                System.out.printf("Максимальна кількість обчислювальних блоків: %d%n", paramValue.get(0));
//            }

            // Створення контексту та черги
            PointerBuffer contextProperties = stack.mallocPointer(3)
                    .put(CL10.CL_CONTEXT_PLATFORM)
                    .put(platform)
                    .put(0)
                    .rewind();

            openClContext.context = CL10.clCreateContext(contextProperties, openClContext.device, null, 0, null);
            openClContext.commandQueue = CL10.clCreateCommandQueue(openClContext.context, openClContext.device, CL10.CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE,(IntBuffer) null);

            if (openClContext.context == 0 || openClContext.commandQueue == 0) {
                throw new IllegalStateException("Failed to create OpenCL context or command queue.");
            }

            // Створення буферів
//            createRWHostBuffer(particlesBuffer, clParticlesBuffer);
//
            float[] af = new float[256];
            long clABuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                    particlesBuffer, null);
            long clBBuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                    af, null);
            long clResultBuffer = CL10.clCreateBuffer(openClContext.context, CL10.CL_MEM_WRITE_ONLY,
                    VECTOR_SIZE * Float.BYTES, null);

//            if (clABuffer == 0 || clBBuffer == 0 || clResultBuffer == 0) {
//                throw new IllegalStateException("Failed to create OpenCL memory buffers.");
//            }

            // Завантаження OpenCL kernel
            long localMemSizeInfo = CL10.clGetDeviceInfo(openClContext.device, CL10.CL_DEVICE_LOCAL_MEM_SIZE, (IntBuffer) null, (PointerBuffer) null );

            //створення буферу частинок
            createRWHostBuffer(particlesBuffer, clParticlesBuffer);

            // Локальна пам'ять
            long localMemSize = LOCAL_WORK_SIZE * Float.BYTES * 2;

            // Встановлення аргументів
//            CL10.clSetKernelArg(kernel, 0, PointerBuffer.allocateDirect(1).put(0, clABuffer));
//            CL10.clSetKernelArg(kernel, 1, PointerBuffer.allocateDirect(1).put(0, clBBuffer));
//            CL10.clSetKernelArg(kernel, 2, PointerBuffer.allocateDirect(1).put(0, clResultBuffer));
//
//            // Локальна пам'ять як аргументи
            if (localMemSizeInfo > 0) {
                CL10.clSetKernelArg(kernel, 3, localMemSize);
                CL10.clSetKernelArg(kernel, 4, localMemSize);
                CL10.clSetKernelArg(13l, 3, 12.5f);
            }

            // Виконання kernel з явним розміром локальної групи
            long globalWorkSize = (long) Math.ceil(VECTOR_SIZE / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE;

            PointerBuffer global = stack.mallocPointer(1).put(globalWorkSize).rewind();
            PointerBuffer local = stack.mallocPointer(1).put(LOCAL_WORK_SIZE).rewind();



            CL10.clEnqueueNDRangeKernel(
                    commandQueue, kernel, 1, null,
                    global, local,
                    null, null
            );


            // Читання результату
            CL10.clEnqueueReadBuffer(commandQueue, clResultBuffer, true, 0,
                    resultBuffer, null, null);

//            long endTime = System.nanoTime();
//            System.out.printf("Час виконання: %.3f мс%n", (endTime - startTime) / 1_000_000.0);
//
            // Перевірка результату
//            for (int i = 0; i < 10; i++) {
//                System.out.printf("%.2f + %.2f = %.2f%n",
//                        aBuffer.get(i), bBuffer.get(i), resultBuffer.get(i));
//            }

            // Очищення ресурсів (без змін)
            CL10.clReleaseKernel(kernel);
            CL10.clReleaseProgram(program);
            CL10.clReleaseMemObject(clABuffer);
            CL10.clReleaseMemObject(clBBuffer);
            CL10.clReleaseMemObject(clResultBuffer);
            CL10.clReleaseCommandQueue(commandQueue);
            CL10.clReleaseContext(context);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MemoryUtil.memFree(aBuffer);
            MemoryUtil.memFree(bBuffer);
            MemoryUtil.memFree(resultBuffer);
            CL.destroy();
        }

        return null;
    }

    public void particlesBufferFiller () {
        if (particlesBuffer != null) {
            MemoryUtil.memFree(particlesBuffer);
        }
        particlesBuffer = MemoryUtil.memAllocFloat(particles.length * 7);
        for (int i = 0; i < particles.length; i++) {
            particlesBuffer.put(particles[i].xPosition);
            particlesBuffer.put(particles[i].yPosition);
            particlesBuffer.put(particles[i].radius);
            particlesBuffer.put(particles[i].xSpeed);
            particlesBuffer.put(particles[i].ySpeed);
            particlesBuffer.put(0);
            particlesBuffer.put(0);
        }
        particlesBuffer.rewind();
    }


    public void createRWHostBuffer(FloatBuffer hostBuffer, Long kernelBuffer) {
        if (kernelBuffer != null) {
            CL10.clReleaseMemObject(kernelBuffer);
        }
        kernelBuffer = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_WRITE | CL10.CL_MEM_USE_HOST_PTR , hostBuffer, null);
        if (kernelBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }
    }
    public void createRHostBuffer(FloatBuffer hostBuffer, Long kernelBuffer) {
        if (kernelBuffer != null) {
            CL10.clReleaseMemObject(kernelBuffer);
        }
        kernelBuffer = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_USE_HOST_PTR , hostBuffer, null);
        if (kernelBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }
    }

    public void createKernel (String kernelName, Long kernel) {
        URL URLKernelSource = getClass().getResource(kernelName);

        assert URLKernelSource != null;
        String kernelSource = null;
        try {
            kernelSource = Files.readString(Paths.get(URLKernelSource.toURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Компіляція та створення kernel
        long program = CL10.clCreateProgramWithSource(context, kernelSource, null);
        CL10.clBuildProgram(program, device, "", null, 0);

        kernel = CL10.clCreateKernel(program, "physicCalculation", (IntBuffer) null);

        // Перевірка чи правельно пройшла уомпіляція
        if (kernel == 0) {
            int buildStatus = CL10.clBuildProgram(program, device, "", null, 0);
            if (buildStatus != CL10.CL_SUCCESS) {
                // Отримання журналу компіляції
                PointerBuffer sizeBuffer = MemoryStack.stackMallocPointer(1);
                CL10.clGetProgramBuildInfo(program, device, CL10.CL_PROGRAM_BUILD_LOG, (ByteBuffer) null, sizeBuffer);

                ByteBuffer buildLogBuffer = MemoryStack.stackMalloc((int) sizeBuffer.get(0));
                CL10.clGetProgramBuildInfo(program, device, CL10.CL_PROGRAM_BUILD_LOG, buildLogBuffer, null);

                String buildLog = MemoryUtil.memUTF8(buildLogBuffer);
                System.err.println("Build log:\n" + buildLog);
                throw new RuntimeException("Failed to build OpenCL program.");
            }
        }
    }



}
