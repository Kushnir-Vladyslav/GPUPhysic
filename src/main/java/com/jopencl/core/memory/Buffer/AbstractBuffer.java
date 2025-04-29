package com.jopencl.core.memory.Buffer;

import com.jopencl.core.memory.Data.Data;
import org.lwjgl.PointerBuffer;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;


public abstract class AbstractBuffer {
    private static int counter = 0;

    private boolean initiated = false;

    private String bufferName = "UnnamedBuffer" + counter++;
    private boolean readable = false;
    private boolean writable = false;
    private boolean projectionToHost = false;
    private boolean dynamic = false;
    private Class<Data> clazz = null;

    private Data dataObject;

    private PointerBuffer transmitter = null;

    private long clBuffer;
    private ByteBuffer nativeBuffer;
    private Object[] hostBuffer;

    private int bufferSize = -1;
    private int capacity = -1;

    private void initCheck () {
        if (initiated) {
            System.err.println("Buffer " + bufferName + "has been already initiated.");
        }
    }

    public AbstractBuffer setBufferName(String name) {
        initCheck();
        bufferName = name;

        return this;
    }

    public AbstractBuffer setReadable(boolean isRead) {
        initCheck();
        readable = isRead;

        return this;
    }

    public AbstractBuffer setWritable(boolean isRead) {
        initCheck();
        writable = isRead;

        return this;
    }

    public AbstractBuffer setProjectionToHost(boolean isProjection) {
        initCheck();
        projectionToHost = isProjection;

        return this;
    }

    public AbstractBuffer setDynamic(boolean isDynamic) {
        initCheck();
        dynamic = isDynamic;

        return this;
    }

    public AbstractBuffer setInitSize(int newSize) {
        initCheck();
        if (newSize <= 0) {
            throw new IllegalStateException("Buffer's size mast be positive.");
        }
        capacity = newSize;

        return this;
    }

    public AbstractBuffer setDataClass(Class<Data> newClass) {
        initCheck();
        clazz = newClass;

        return this;
    }

    private void initErr(String message) {
        throw new IllegalStateException(
                "Initiated error.\n" +
                "Buffer's name: \"" + bufferName + "\"\n" +
                message);
    }

    public void init () {
        initiated = true;

        if (clazz == null) {
            initErr("Data class is invalid.");
        }
        try {
            dataObject = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            initErr("Data class could not be initialized.");
        }

        if (readable) {
            if (!(this instanceof Readable)) {
                initErr("Doesn't extends of Readable interface.");
            }
        }

        if (writable) {
            if (!(this instanceof Writable)) {
                initErr("Doesn't extends of Writable interface.");
            }

            transmitter = PointerBuffer.allocateDirect(1);
        }

        if (capacity < 1) {
            initErr("Initiate buffer's size, mast be positive.");
        }

        if (projectionToHost) {
            hostBuffer
        }
    }


}

