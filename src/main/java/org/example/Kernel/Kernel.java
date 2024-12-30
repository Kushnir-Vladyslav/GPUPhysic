package org.example.Kernel;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.example.GLOBAL_STATE.*;
import static org.example.OpenCL.*;

public abstract class Kernel {
    public long kernel;

    public long getKernel() {
        return kernel;
    }

    public void createKernel (String kernelName) {
        URL URLKernelSource = getClass().getResource(kernelName + ".cl");

        assert URLKernelSource != null;
        String kernelSource;
        try {
            kernelSource = Files.readString(Paths.get(URLKernelSource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Компіляція та створення kernel
        long program = CL10.clCreateProgramWithSource(openClContext.context, kernelSource, null);
        CL10.clBuildProgram(program, openClContext.device, "", null, 0);

        kernel = CL10.clCreateKernel(program, kernelName, (IntBuffer) null);

        // Перевірка чи правельно пройшла уомпіляція
        if (kernel == 0) {
            int buildStatus = CL10.clBuildProgram(program, openClContext.device, "", null, 0);
            if (buildStatus != CL10.CL_SUCCESS) {
                // Отримання журналу компіляції
                PointerBuffer sizeBuffer = MemoryStack.stackMallocPointer(1);
                CL10.clGetProgramBuildInfo(program, openClContext.device, CL10.CL_PROGRAM_BUILD_LOG, (ByteBuffer) null, sizeBuffer);

                ByteBuffer buildLogBuffer = MemoryStack.stackMalloc((int) sizeBuffer.get(0));
                CL10.clGetProgramBuildInfo(program, openClContext.device, CL10.CL_PROGRAM_BUILD_LOG, buildLogBuffer, null);

                String buildLog = MemoryUtil.memUTF8(buildLogBuffer);
                System.err.println("Build log:\n" + buildLog);
                throw new RuntimeException("Failed to build OpenCL program.");
            }
        }
    }

    public abstract void run ();
    public abstract void destroy ();
}
