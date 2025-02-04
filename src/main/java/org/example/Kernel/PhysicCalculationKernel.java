package org.example.Kernel;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Structs.Particles;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import static org.example.GLOBAL_STATE.*;

public class PhysicCalculationKernel extends Kernel{

    final int LOCAL_WORK_SIZE = 16;

    GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public PhysicCalculationKernel () {
        createKernel("PhysicCalculation", "Constants", "Structs", "Math");

        if (!bufferManager.isExist("ParticlesBuffer") || !bufferManager.isExist("NumParticlesBuffer")) {
            if (particles == null) {
                particles = new Particles();
            } else {
                particles.update();
            }
        }

        particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
        numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);

        particlesBuffer.addKernel(kernel, 0);
        numParticlesBuffer.addKernel(kernel, 1);

        global = MemoryUtil.memAllocPointer(2);
        local = MemoryUtil.memAllocPointer(2).put(LOCAL_WORK_SIZE).put(LOCAL_WORK_SIZE);
    }

    @Override
    public void run() {
        global.put((long) Math.ceil(particles.getNumOfParticle() / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE).
                put((long) Math.ceil(particles.getNumOfParticle() / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);

        err = CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 2, null,
                global.rewind(), local.rewind(),
                null, null
        );
        checkError();
    }
}
