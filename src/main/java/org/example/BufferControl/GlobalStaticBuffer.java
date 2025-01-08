package org.example.BufferControl;

public class GlobalStaticBuffer<K extends TypeOfBuffer> extends BufferContext <K>{
    protected int size;
    protected long flags;

    public GlobalStaticBuffer(Class<K> type) {
        super(type);
    }

    public void init (int length, long flags) {
        size = length;
        this.flags = flags;

        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(length);
        } catch (Exception e) {
            throw new  IllegalStateException(e);
        }

        clBuffer = nativeBuffer.createClBuffer(flags);
        checkClBuffer();
    }

    public void setData(Object arr) {
        if (nativeBuffer.getSize(arr) > size) {
            throw new IllegalArgumentException("Invalid array size.");
        } else {
            nativeBuffer.set(arr);
            nativeBuffer.rewriteClBuffer(clBuffer, 0);
        }
        checkClBuffer();
        setNewArgs();
    }

    public Object readData() {
        nativeBuffer.readClBuffer(clBuffer, 0);
        return nativeBuffer.getArr(size);
    }
}
