package org.example.Structs;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.Particle;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.EventManager.EventManager;
import org.example.EventManager.RightMousePressEvent;
import org.lwjgl.opencl.CL10;

import static org.example.JavaFX.GLOBAL_STATE.*;


public class Particles {


    private final int[] numOfParticle = new int[1];

    private GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    private SingleValueBuffer<IntBufferType> numParticlesBuffer;

    public Particles() {
        RightMousePressEvent rightMousePressEvent = EventManager.
                getEventManager().
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

        int xMin = (int) boundary.borderThickness;
        int xMax = (int) (boundary.width - boundary.borderThickness);

        int yMin = (int) (boundary.height / 5 * 2);
        int yMax = yMin + 0;

        numOfParticle[0] = (yMax - yMin) * (xMax - xMin);

        update();

        Particle[] particles = new Particle[numOfParticle[0]];

        int pos = 0;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                particles[pos++] = new Particle(x, y);
            }
        }
        particlesBuffer.setData(particles);
    }

    public void createNewParticle (float x, float y) {
        createNewParticle(x, y, 1);
    }

    public void createNewParticle (float x, float y, int num) {
        Particle[] particles = new Particle[num];
        for (int i = 0; i < num; i++) {
            particles[i] = new Particle(x, y);
        }
        addNewParticle(particles);
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
