package org.example.Kernel.Physic;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.FloatBufferType;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Event.EventManager;
import org.example.Event.NumberParticlesEvent.NumParticlesEvent;
import org.example.Kernel.Kernel;
import org.example.Structs.Particles;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

public class UpdatePositionParticlesKernel extends Kernel {

    final int LOCAL_WORK_SIZE = 256;

    private long time;
    private float[] deltaTime = new float[1];

    GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    SingleValueBuffer<IntBufferType> numParticlesBuffer;
    SingleValueBuffer<FloatBufferType> timeMoveParticleBuffer;

    public UpdatePositionParticlesKernel () {
        super("UpdatePositionParticles", "UpdatePositionParticles.cl","Structs", "Constants");

        if (!bufferManager.isExist("ParticlesBuffer") || !bufferManager.isExist("NumParticlesBuffer")) {
            Particles.getInstance().update();
        }

        particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
        numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);
        timeMoveParticleBuffer = bufferManager.createBuffer("TimeMoveParticleBuffer", SingleValueBuffer.class, FloatBufferType.class);
        timeMoveParticleBuffer.init();

        particlesBuffer.addKernel(kernel, 0);
        numParticlesBuffer.addKernel(kernel, 1);
        timeMoveParticleBuffer.addKernel(kernel, 2);

        global = MemoryUtil.memAllocPointer(1).put(0,
                (long) Math.ceil(Particles.getNumOfParticle() /
                        (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
        local = MemoryUtil.memAllocPointer(1).put(LOCAL_WORK_SIZE);

        time = System.nanoTime();

        NumParticlesEvent numParticlesEvent = EventManager
                .getInstance().
                getEvent(NumParticlesEvent.EVENT_NAME);

        numParticlesEvent.subscribe(
                this,
                (event) -> {
                    global.put(0,
                            (long) Math.ceil(Particles.getNumOfParticle() /
                                    (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
                }
        );
    }

    @Override
    public void run() {
        long newTime = System.nanoTime();;
        deltaTime[0] = (float) ((newTime - time) / 1e9);

        timeMoveParticleBuffer.setData(deltaTime);
        time = newTime;

        err = CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 1, null,
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
