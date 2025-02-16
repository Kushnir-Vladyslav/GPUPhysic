package org.example.OpenCL;

import javafx.concurrent.Task;
import org.example.BufferControl.BufferManager;
import org.example.JavaFX.Window;
import org.example.Kernel.*;
import org.example.Kernel.Draw.DrawBackgroundKernel;
import org.example.Kernel.Draw.DrawParticlesKernel;
import org.example.Kernel.Physic.BoundaryCollisionKernel;
import org.example.Kernel.Physic.PhysicCalculationKernel;
import org.example.Kernel.Physic.UpdatePositionParticlesKernel;
import org.example.Structs.Canvas;
import org.example.Structs.Particles;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.time.Duration;
import java.time.Instant;


public class OpenCL extends Task<Void> {
    OpenClContext openClContext;

    protected volatile boolean isRead = false;
    public volatile boolean isRun = true;

    KernelManager kernelManager;

//    Kernel kernel;

    Kernel drawBackground;
    Kernel drawParticles;
    Kernel boundaryCollision;
    Kernel physicCalculation;
    Kernel updatePositionParticles;

    public OpenCL() {
        org.lwjgl.system.Configuration.OPENCL_EXPLICIT_INIT.set(true);
        CL.create();

        openClContext = OpenClContext.getInstance();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Отримання платформ та пристроїв (без змін)
            IntBuffer platformCount = stack.mallocInt(1);
            CL10.clGetPlatformIDs(null, platformCount);

            PointerBuffer platforms = stack.mallocPointer(platformCount.get(0));
            CL10.clGetPlatformIDs(platforms, (IntBuffer) null);

//            platformInfo (platformCount,  platforms);

            long platform = platforms.get(0);

            IntBuffer deviceCount = stack.mallocInt(1);
            CL10.clGetDeviceIDs(platform, CL10.CL_DEVICE_TYPE_GPU, null, deviceCount);

            PointerBuffer devices = stack.mallocPointer(deviceCount.get(0));
            CL10.clGetDeviceIDs(platform, CL10.CL_DEVICE_TYPE_GPU, devices, (IntBuffer) null);

            openClContext.device = devices.get(0);

//            deviceDiagnostic ();

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

            kernelManager = KernelManager.getInstance();

//            kernel = new TestKernel();
//
//            kernelManager.addKernel("TestKernel", kernel);

//            kernel.run();

            drawBackground = new DrawBackgroundKernel();
            kernelManager.addKernel("DrawBackgroundKernel", drawBackground);

            drawParticles = new DrawParticlesKernel();
            kernelManager.addKernel("DrawParticlesKernel", drawParticles);

            boundaryCollision = new BoundaryCollisionKernel();
            kernelManager.addKernel("BoundaryCollision", boundaryCollision);

            physicCalculation = new PhysicCalculationKernel();
            kernelManager.addKernel("PhysicCalculation", physicCalculation);

            updatePositionParticles = new UpdatePositionParticlesKernel();
            kernelManager.addKernel("UpdatePositionParticles", updatePositionParticles);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void read () {
        isRead = true;
//        notifyAll();
    }

    @Override
    public Void call () {

        Particles.getInstance().spawnALot();
//        kernel.run();
//        Instant start = Instant.now();
        while (isRun) {
            for (int i = 0; i < 10; i++) {
                physicCalculation.run();
                boundaryCollision.run();
            }
            updatePositionParticles.run();

            synchronized(this) {
                if (isRead) {
                    drawBackground.run();
                    drawParticles.run();
//        Instant end = Instant.now();

//        printExecutionTime(start, end);
                    Window.getInstance().pixels = Canvas.getInstance().getCanvas();
                    isRead = false;
                }
            }
            CL10.clFinish(openClContext.commandQueue);
        }
        return null;
    }

    private static void printExecutionTime(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        System.out.println("Час виконання: " + duration.toSeconds() + " sec");
        System.out.println("Час виконання: " + duration.toMillis() + " millisec");
    }

    public void destroy () {
        kernelManager.destroy();
        BufferManager.getInstance().destroy();
        openClContext.destroy();

        CL.destroy();
    }

    protected void platformInfo(IntBuffer platformCount,  PointerBuffer platforms) {
        // Отримання інформації про платформи
        for (int i = 0; i < platformCount.get(0); i++) {
            long platformId = platforms.get(i);

            // Запитуємо розмір буфера для зберігання назви платформи
            PointerBuffer sizeBuffer = MemoryUtil.memAllocPointer(1);
            CL10.clGetPlatformInfo(platformId, CL10.CL_PLATFORM_NAME, (ByteBuffer) null, sizeBuffer);

            // Створюємо буфер для зчитування назви
            ByteBuffer nameBuffer = MemoryUtil.memAlloc((int) sizeBuffer.get(0));
            CL10.clGetPlatformInfo(platformId, CL10.CL_PLATFORM_NAME, nameBuffer, null);

            // Перетворюємо буфер у строку
            String platformName = MemoryUtil.memUTF8(nameBuffer);
            System.err.println("Platform " + i + ": " + platformName);

            MemoryUtil.memFree(sizeBuffer);
            MemoryUtil.memFree(nameBuffer);
        }
    }

    protected void deviceDiagnostic() {
        // Додаткова діагностика пристрою
        try (MemoryStack infoStack = MemoryStack.stackPush()) {
            PointerBuffer paramSize = infoStack.mallocPointer(1);
            CL10.clGetDeviceInfo(openClContext.device, CL10.CL_DEVICE_MAX_COMPUTE_UNITS, (IntBuffer) null, paramSize);

            IntBuffer paramValue = infoStack.mallocInt((int) paramSize.get(0));
            CL10.clGetDeviceInfo(openClContext.device, CL10.CL_DEVICE_MAX_COMPUTE_UNITS, paramValue, null);

            System.err.printf("Максимальна кількість обчислювальних блоків: %d%n", paramValue.get(0));
        }
    }
}
