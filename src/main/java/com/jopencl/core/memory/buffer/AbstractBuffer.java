package com.jopencl.core.memory.buffer;

import com.jopencl.core.memory.data.ConvertFromByteBuffer;
import com.jopencl.core.memory.data.ConvertToByteBuffer;
import com.jopencl.core.memory.data.Data;
import org.example.OpenCL.OpenClContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class AbstractBuffer {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private boolean initiated = false;

    private String bufferName = "UnnamedBuffer" + counter.getAndIncrement();
    private boolean readable = false;
    private boolean writable = false;
    protected boolean copyNativeBuffer = false;
    protected boolean copyHostBuffer = false;
    protected boolean dynamic = false;
    private Class<Data> clazz = null;

    protected long flags = 0;

    protected Data dataObject;
    protected OpenClContext openClContext;

    protected long clBuffer = 0;
    protected ByteBuffer nativeBuffer = null;
    protected Object hostBuffer = null;

    protected int size = -1;
    protected int capacity = -1;

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

    protected AbstractBuffer setReadable(boolean isRead) {
        initCheck();
        readable = isRead;

        return this;
    }

    protected AbstractBuffer setWritable(boolean isRead) {
        initCheck();
        writable = isRead;

        return this;
    }

    public AbstractBuffer setCopyNativeBuffer(boolean isProjection) {
        initCheck();
        copyNativeBuffer = isProjection;

        return this;
    }

    public AbstractBuffer setFlags(long newFlags) {
        initCheck();
        flags = newFlags;

        return this;
    }

    public AbstractBuffer setCopyHostBuffer(boolean isProjection) {
        initCheck();
        copyHostBuffer = isProjection;

        return this;
    }

    protected AbstractBuffer setDynamic(boolean isDynamic) {
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

//    public OpenClContext getOpenClContext() {
//        return openClContext;
//    }

//    public long getClBuffer() {
//        return clBuffer;
//    }

//    public ByteBuffer getNativeBuffer() {
//        return nativeBuffer;
//    }

//    public Data getDataObject() {
//        return dataObject;
//    }

    protected void initErr(String message) {
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

        size = 0;

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
        }

        if (writable) {
            if (!(this instanceof Writable)) {
                initErr("Doesn't extends of Writable interface.");
            }

            if (!(dataObject instanceof ConvertToByteBuffer)) {
                initErr("Data class doesn't extends of \"ConvertToByteBuffer\" interface.");
            }
        }

        if (openClContext == null) {
            initErr("OpenCL context for buffer cannot be \"null\"");
        }

        if (copyNativeBuffer) {
            nativeBuffer = MemoryUtil.memAlloc(capacity);
        }

        if (copyHostBuffer) {
            hostBuffer = new Object[capacity];
        }

        if (this instanceof AdditionalInitiation additionalInitiation) {
            additionalInitiation.addInit();
        }

        initiated = true;
    }

    //public abstract void bindingToKernel (KernelDependency KD);

    protected long createClBuffer() {
        if (capacity < 1) {
            throw new IllegalStateException("Length of OpenCl buffer must be positive.");
        }

        long newClBuffer = CL10.clCreateBuffer(
                openClContext.context,
                flags,
                capacity,
                null
        );

        if (newClBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }

        return newClBuffer;
    }

    public void destroy () {
        if (initiated) {
            if (nativeBuffer != null) {
                MemoryUtil.memFree(nativeBuffer);
                nativeBuffer = null;
            }

            if (hostBuffer != null) {
                hostBuffer = null;
            }

            capacity = -1;
            size = -1;

            flags = 0;

            if (clBuffer != 0) {
                CL10.clReleaseMemObject(clBuffer);
                clBuffer = 0;
            }

            initiated = false;
        }
    }
}

