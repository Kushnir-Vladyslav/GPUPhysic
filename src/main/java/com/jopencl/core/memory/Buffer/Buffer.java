package com.jopencl.core.memory.Buffer;

import com.jopencl.core.memory.Data.Data;

import java.lang.reflect.ParameterizedType;


public abstract class Buffer <T extends Data>{
    private static int counter = 0;

    private boolean initiated = false;
    private String bufferName = "UnnamedBuffer" + counter++;
    private boolean readable = false;
    private boolean projectionToHost = false;
    private boolean dynamic = false;

    private Data dataObject;

    Buffer() {
        Class<T> clazz = (Class<T>) ((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        try {
            dataObject = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of " + clazz.getName(), e);
        }
    }

}

public class GlobalBuffer<T extends Data> extends Buffer<T> {

}
