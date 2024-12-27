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

    public abstract void run ();
    public abstract void destroy ();
}
