package org.example.Structs;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.lwjgl.opencl.CL10;

import static org.example.GLOBAL_STATE.*;


public class Particles {


    private int numOfParticle = 0;

    private GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    private SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public Particles() {
        update();
    }

    public void createNewParticle (float x, float y) {
        createNewParticle(x, y, 0);
    }

    public void createNewParticle (float x, float y, int num) {

    }

    private void addNewParticle () {

    }

    public void update () {
        if (particlesBuffer == null) {
            if (bufferManager.isExist("ParticlesBuffer")) {
                particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
            } else {
                particlesBuffer = bufferManager.createBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
                particlesBuffer.init(100, CL10.CL_MEM_READ_WRITE);
            }
        }
        if (numParticlesBuffer == null) {
            if (bufferManager.isExist("NumParticlesBuffer")) {
                numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);
            } else {
                numParticlesBuffer = bufferManager.createBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);
                numParticlesBuffer.init();
            }
        }
    }
}
