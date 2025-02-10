package org.example.Kernel.Physic;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.BoundaryBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Kernel.Kernel;
import org.example.Structs.BoundaryController;
import org.example.Structs.CursorController;
import org.example.Structs.Particles;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import static org.example.JavaFX.GLOBAL_STATE.*;

public class BoundaryCollisionKernel extends Kernel {

    final int LOCAL_WORK_SIZE = 256;

    GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    SingleValueBuffer<BoundaryBuffer> boundaryBuffer;
    SingleValueBuffer<CursorPositionBuffer> cursorPositionBuffer;
    SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public BoundaryCollisionKernel () {
        super("BoundaryCollision", "BoundaryCollision.cl", "../Structs.h", "../Constants.h");

        if (!bufferManager.isExist("ParticlesBuffer") || !bufferManager.isExist("NumParticlesBuffer")) {
            if (particles == null) {
                particles = new Particles();
            } else {
                particles.update();
            }
        }

        if (!bufferManager.isExist("BoundaryBuffer")) {
            BoundaryController.getInstance().update();
        }

        if (!bufferManager.isExist("CursorBuffer")) {
            CursorController.getInstance().update();
        }

        particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
        boundaryBuffer = bufferManager.getBuffer("BoundaryBuffer", SingleValueBuffer.class, BoundaryBuffer.class);
        cursorPositionBuffer = bufferManager.getBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);
        numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);

        particlesBuffer.addKernel(kernel, 0);
        boundaryBuffer.addKernel(kernel, 1);
        cursorPositionBuffer.addKernel(kernel, 2);
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
    }
}
