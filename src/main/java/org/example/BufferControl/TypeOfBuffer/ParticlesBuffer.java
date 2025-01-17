package org.example.BufferControl.TypeOfBuffer;

import org.example.Structs.Particle;

public class ParticlesBuffer extends TypeOfBuffer{
    @Override
    public void set(Object arr, int startPosition) {

    }

    @Override
    public int getSize(Object arr) {
        if (!Particle[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        Particle[] castedArr = (Particle[]) arr;
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return 0;
    }

    @Override
    public Object getArr() {
        return null;
    }
}
