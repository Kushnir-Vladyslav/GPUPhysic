package org.example.Kernel.Physic;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Event.EventManager;
import org.example.Event.NumberParticlesEvent.NumParticlesEvent;
import org.example.Kernel.Kernel;
import org.example.Structs.Particles;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

public class PhysicCalculationKernel extends Kernel {

    final int LOCAL_WORK_SIZE = 16;

    GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public PhysicCalculationKernel () {
        super("PhysicCalculation", "PhysicCalculation.cl", "Structs", "Constants");

        if (!bufferManager.isExist("ParticlesBuffer") || !bufferManager.isExist("NumParticlesBuffer")) {
            Particles.getInstance().update();
        }

        particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
        numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);

        particlesBuffer.addKernel(kernel, 0);
        numParticlesBuffer.addKernel(kernel, 1);

        global = MemoryUtil.memAllocPointer(2).
                put((long) Math.ceil(Particles.getNumOfParticle() /
                        (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE).
                put((long) Math.ceil(Particles.getNumOfParticle() /
                        (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
        local = MemoryUtil.memAllocPointer(2).
                put(LOCAL_WORK_SIZE).put(LOCAL_WORK_SIZE);

        NumParticlesEvent numParticlesEvent = EventManager
                .getInstance().
                getEvent(NumParticlesEvent.EVENT_NAME);

        numParticlesEvent.subscribe(
                this,
                (event) -> {
                    global.rewind().put((long) Math.ceil(Particles.getNumOfParticle() /
                                    (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE).
                            put((long) Math.ceil(Particles.getNumOfParticle() /
                                    (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
                }
        );
    }

    @Override
    public void run() {
        err = CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 2, null,
                global.rewind(), local.rewind(),
                null, null
        );
        checkError();
    }

    @Override
    public void destroy () {
        super.destroy();

        NumParticlesEvent numParticlesEvent = EventManager
                .getInstance().
                getEvent(NumParticlesEvent.EVENT_NAME);

        numParticlesEvent.removeHandler(this);
    }
}
