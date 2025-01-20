package org.example.BufferControl.TypeOfBuffer;

import org.example.Structs.Particle;

public class ParticlesBuffer extends TypeOfBuffer{

    public ParticlesBuffer (int length) {
        create(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof Particle[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }

        buffer.position(startPosition * getByteSize());
        for(Particle particle : castedArr) {
            buffer.putFloat(particle.xPosition).
                    putFloat(particle.yPosition).
                    putFloat(particle.radius).
                    putFloat(particle.xSpeed).
                    putFloat(particle.ySpeed).
                    putFloat(particle.xSpeed).
                    putFloat(particle.ySpeed);
        }
        buffer.rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (buffer == null || !(arr instanceof Particle[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES * 7;
    }

    @Override
    public Object getArr() {
        if (array == null) {
            array = new Particle[buffer.capacity() / getByteSize()];
        }

        Particle[] arr = (Particle[]) array;

        buffer.rewind();
        for (Particle particle : arr) {
            particle.xPosition = buffer.getFloat();
            particle.yPosition = buffer.getFloat();

            particle.radius = buffer.getFloat();

            particle.xSpeed = buffer.getFloat();
            particle.ySpeed = buffer.getFloat();

            particle.xAcceleration = buffer.getFloat();
            particle.yAcceleration = buffer.getFloat();
        }
        buffer.rewind();
        return arr;
    }

    @Override
    protected void updateArray(int length) {
        array = new Particle[length];

        for (int i = 0; i < length; i++) {
            ((Particle[])array)[i] = new Particle();
        }
    }
}
