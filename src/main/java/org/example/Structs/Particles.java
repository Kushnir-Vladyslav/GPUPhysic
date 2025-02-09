package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.Particle;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Event.EventManager;
import org.example.Event.MouseEvent.RightMousePressEvent;
import org.lwjgl.opencl.CL10;


public class Particles {
    BufferManager bufferManager;

    private final int[] numOfParticle = new int[1];

    private GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    private SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public Particles() {
        bufferManager = BufferManager.getInstance();

        RightMousePressEvent rightMousePressEvent = EventManager.
                getInstance().
                getEvent(RightMousePressEvent.EVENT_NAME);

        rightMousePressEvent.subscribe(this, (event) -> {
            float x = event.x;
            float y = event.y;
            int num = 1;
            float randM = (float) num / 10.f;
            Particle[] particles = new Particle[num];
            for (int i = 0; i < num; i++) {
                particles[i] = new Particle(
                        x + randM * (float) Math.random() - randM / 2,
                        y + randM * (float) Math.random() - randM / 2
                );
            }
            addNewParticle(particles);
        });

//        int xMin = (int) boundary.borderThickness;
//        int xMax = (int) (boundary.width - boundary.borderThickness);
//
//        int yMin = (int) (boundary.height / 5 * 2);
//        int yMax = yMin + 0;
//
//        numOfParticle[0] = (yMax - yMin) * (xMax - xMin);

        update();

//        Particle[] particles = new Particle[numOfParticle[0]];
//
//        int pos = 0;
//        for (int x = xMin; x < xMax; x++) {
//            for (int y = yMin; y < yMax; y++) {
//                particles[pos++] = new Particle(x, y);
//            }
//        }
//        particlesBuffer.setData(particles);
    }

    private void addNewParticle (Particle[] particles) {
        particlesBuffer.addData(particles);
        numOfParticle[0] += particles.length;
        numParticlesBuffer.setData(numOfParticle);
    }

    public int getNumOfParticle() {
        return numOfParticle[0];
    }

    public void update () {
        if (particlesBuffer == null) {
            if (bufferManager.isExist("ParticlesBuffer")) {
                particlesBuffer = bufferManager.getBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
            } else {
                particlesBuffer = bufferManager.createBuffer("ParticlesBuffer", GlobalDynamicBuffer.class, ParticlesBuffer.class);
                particlesBuffer.init((numOfParticle[0] == 0) ? 100 : numOfParticle[0] , CL10.CL_MEM_READ_WRITE);
            }
        }

        if (numParticlesBuffer == null) {
            if (bufferManager.isExist("NumParticlesBuffer")) {
                numParticlesBuffer = bufferManager.getBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);
            } else {
                numParticlesBuffer = bufferManager.createBuffer("NumParticlesBuffer", SingleValueBuffer.class, IntBufferType.class);
                numParticlesBuffer.init();
            }

            numParticlesBuffer.setData(numOfParticle);
        }
    }
}
