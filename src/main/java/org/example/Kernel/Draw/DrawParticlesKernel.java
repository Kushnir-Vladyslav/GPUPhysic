package org.example.Kernel.Draw;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.GlobalStaticBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.BoundaryBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Kernel.Kernel;
import org.example.Structs.BoundaryController;
import org.example.Structs.Canvas;
import org.example.Structs.Particles;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import static org.example.JavaFX.GLOBAL_STATE.*;

public class DrawParticlesKernel extends Kernel {

    final int LOCAL_WORK_SIZE = 256;

    GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    GlobalStaticBuffer<IntBufferType> canvasBuffer;
    SingleValueBuffer<BoundaryBuffer> boundaryBuffer;
    SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public DrawParticlesKernel() {
        super("DrawParticles", "DrawParticles.cl", "Structs");

        if (!bufferManager.isExist("ParticlesBuffer") || !bufferManager.isExist("NumParticlesBuffer")) {
            if (particles == null) {
                particles = new Particles();
            } else {
                particles.update();
            }
        }

        if (!bufferManager.isExist("CanvasBuffer")) {
            if (canvas == null) {
                canvas = new Canvas();
            } else {
                canvas.update();
            }
        }

        if (!bufferManager.isExist("BoundaryBuffer")) {
            BoundaryController.getInstance().update();
        }

        particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
        canvasBuffer = bufferManager.getBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
        boundaryBuffer = bufferManager.getBuffer("BoundaryBuffer", SingleValueBuffer.class, BoundaryBuffer.class);
        numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);

        particlesBuffer.addKernel(kernel, 0);
        canvasBuffer.addKernel(kernel, 1);
        boundaryBuffer.addKernel(kernel, 2);
        numParticlesBuffer.addKernel(kernel, 3);

        global = MemoryUtil.memAllocPointer(1);
        local = MemoryUtil.memAllocPointer(1).put(LOCAL_WORK_SIZE);

    }

    @Override
    public void run() {
        global.put(0, (long) Math.ceil(particles.getNumOfParticle() / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
        err = CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 1, null,
                global.rewind(), local.rewind(),
                null, null
        );
        checkError();
        CL10.clFinish(openClContext.commandQueue);
    }
}
