package com.jopencl.core.memory.Buffer;

import com.jopencl.core.memory.Data.Data;

import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;


public abstract class Buffer {
    private static int counter = 0;

    private boolean initiated = false;

    private String bufferName = "UnnamedBuffer" + counter++;
    private boolean readable = false;
    private boolean projectionToHost = false;
    private boolean dynamic = false;
    private Class<Data> clazz = null;

    private Data dataObject;

    private long clBuffer;
    private ByteBuffer nativeBuffer;
    private Object[] hostBuffer;

    private int clBufferSize;
    private int nativeBufferSize;

    public Buffer setBufferName(String name) {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        } else {
            bufferName = name;
        }

        return this;
    }

    public Buffer setReadable(boolean isRead) {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        } else {
            readable = isRead;
        }

        return this;
    }

    public Buffer setProjectionToHost(boolean isProjection) {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        } else {
            projectionToHost = isProjection;
        }

        return this;
    }

    public Buffer setDynamic(boolean isDynamic) {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        } else {
            dynamic = isDynamic;
        }

        return this;
    }

    public Buffer setDataClass(Class<Data> newClass) {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        } else {
            clazz = newClass;
        }

        return this;
    }



}

