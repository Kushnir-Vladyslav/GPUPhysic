package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.Particle;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.BufferControl.TypeOfBuffer.ParticlesBuffer;
import org.example.Event.EventDataStructs.MousePosition;
import org.example.Event.EventManager;
import org.example.Event.MouseEvent.RightMousePressEvent;
import org.example.Event.NumberParticlesEvent.NumParticlesEvent;
import org.lwjgl.opencl.CL10;


public class Particles {
    private static Particles particles;
    BufferManager bufferManager;

    private final int[] numOfParticle = new int[1];

    private GlobalDynamicBuffer<ParticlesBuffer> particlesBuffer;
    private SingleValueBuffer<IntBufferType> numParticlesBuffer;

    private NumParticlesEvent numParticlesEvent;

    public static Particles getInstance() {
        if (particles == null) {
            particles = new Particles();
        }

        return particles;
    }

    private Particles() {
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

        numParticlesEvent = EventManager
                .getInstance().
                getEvent(NumParticlesEvent.EVENT_NAME);

        update();
    }

    public void spawnALot() {

        RightMousePressEvent rightMousePressEvent = EventManager.
                getInstance().
                getEvent(RightMousePressEvent.EVENT_NAME);

        int xMin = (int) BoundaryController.getBoundary().borderThickness;
        int xMax = (int) (Canvas.getCanvasWidth()- BoundaryController.getBoundary().borderThickness);



        int yMin = (int) Canvas.getCanvasHeight() * 2 / 5;
        int yMax = yMin + 25;

        Particle[] particles1 = new Particle[(xMax - xMin) * (yMax - yMin)];

        int pos = 0;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
               rightMousePressEvent.invoke(new MousePosition(x, y));
            }
        }

//        addNewParticle(particles1);
    }

    private void addNewParticle (Particle[] particles) {
        particlesBuffer.addData(particles);
        numOfParticle[0] += particles.length;
        numParticlesBuffer.setData(numOfParticle);

        numParticlesEvent.invoke(numOfParticle[0]);
    }

    public static int getNumOfParticle() {
        return getInstance().numOfParticle[0];
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
