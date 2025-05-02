package com.jopencl.core.memory.Buffer;

import com.jopencl.core.memory.BufferContext;
import com.jopencl.core.memory.Data.ConvertFromByteBuffer;
import com.jopencl.core.memory.Data.ConvertToByteBuffer;
import com.jopencl.core.memory.Data.Data;
import org.example.OpenCL.OpenClContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

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
    private OpenClContext openClContext;

    private PointerBuffer transmitter = null;

    private long clBuffer = 0;
    private ByteBuffer nativeBuffer = null;
    private Object[] hostBuffer = null;

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

    public AbstractBuffer setOpenClContext(OpenClContext clContext) {
        initCheck();
        openClContext = clContext;

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
        if (bufferName == null) {
            initErr("Name of buffer cannot be \"null\"");
        }

        if (clazz == null) {
            initErr("Data class is invalid.");
        }
        try {
            dataObject = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            initErr("Data class could not be initialized.");
        }

        if (dynamic) {
            capacity *= 1.5;
        }

        if (capacity < 1) {
            initErr("Initiate buffer's size, mast be positive.");
        }

        if (readable) {
            if (!(this instanceof Readable)) {
                initErr("Doesn't extends of Readable interface.");
            }

            if (!(dataObject instanceof ConvertFromByteBuffer)) {
                initErr("Data class doesn't extends of \"ConvertFromByteBuffer\" interface.");
            }

            hostBuffer = new Object[capacity];
        }

        if (writable) {
            if (!(this instanceof Writable)) {
                initErr("Doesn't extends of Writable interface.");
            }

            if (!(dataObject instanceof ConvertToByteBuffer)) {
                initErr("Data class doesn't extends of \"ConvertToByteBuffer\" interface.");
            }

            transmitter = PointerBuffer.allocateDirect(1);
        }

        if (openClContext == null) {
            initErr("OpenCL context for buffer cannot be \"null\"");
        }

        if (projectionToHost) {
            nativeBuffer = MemoryUtil.memAlloc(capacity);
        }

        if (this instanceof AdditionalInitiation additionalInitiation) {
            additionalInitiation.addInit(this);
        }

        initiated = true;
    }

    public long createClBuffer(long flags, int len) {
        if (len < 1) {
            throw new IllegalStateException("Length of OpenCl buffer must be positive.");
        }
        return CL10.clCreateBuffer(
                openClContext.context,
                flags,
                len,
                null
        );
    }

    //public abstract void bindingToKernel (KernelDependency KD);

    public void destroy () {
        if (initiated) {
            if (nativeBuffer != null) {
                MemoryUtil.memFree(nativeBuffer);
                nativeBuffer = null;
            }

            if (readable) {
                hostBuffer = null;
            }

            capacity = -1;
            bufferSize = -1;

            initiated = false;
        }
    }
}

