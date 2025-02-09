package org.example.BufferControl.TypeOfBuffer;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.example.JavaFX.GLOBAL_STATE.openClContext;

/**
 * Абстрактний клас, який представляє буфер даних.
 * Відповідає за створення, оновлення та звільнення пам'яті буфера.
 */
public abstract class TypeOfBuffer {
    protected ByteBuffer buffer;    // Буфер для зберігання даних
    protected Object array;         // Масив даних

    /**
     * Створює новий буфер заданого розміру.
     *
     * @param length Розмір буфера в елементах.
     */
    protected void create (int length){
        if (buffer != null){
            MemoryUtil.memFree(buffer);
        }
        buffer = MemoryUtil.memAlloc(length * getByteSize());
        updateArray(length);
    }

    /**
     * Встановлює дані в буфер.
     *
     * @param arr Масив даних.
     */
    public void set (Object arr) {
        set(arr, 0);
    }

    /**
     * Абстрактний метод для встановлення даних у буфер з певної позиції.
     *
     * @param arr           Масив даних.
     * @param startPosition Початкова позиція в буфері.
     */
    public abstract void set (Object arr, int startPosition);

    /**
     * Абстрактний метод для отримання розміру масиву.
     *
     * @param arr Масив даних.
     * @return Розмір масиву.
     */
    public abstract int getSize(Object arr);

    /**
     * Абстрактний метод для отримання розміру одного елемента в байтах.
     *
     * @return Розмір одного елемента в байтах.
     */
    public abstract int getByteSize();

    /**
     * Абстрактний метод для отримання масиву даних.
     *
     * @return Масив даних.
     */
    public abstract Object getArr();

    /**
     * Створює OpenCL буфер.
     *
     * @param flags Прапорці для створення буфера.
     * @return Ідентифікатор OpenCL буфера.
     */
    public long createClBuffer(long flags) {
        return CL10.clCreateBuffer(
                openClContext.context,
                flags,
                buffer.capacity(),
                null
        );
    }

    /**
     * Перезаписує дані в OpenCL буфер.
     *
     * @param clBuffer Ідентифікатор OpenCL буфера.
     */
    public void rewriteClBuffer(long clBuffer){
        rewriteClBuffer(clBuffer, 0);
    }

    /**
     * Перезаписує дані в OpenCL буфер з певного зміщення.
     *
     * @param clBuffer Ідентифікатор OpenCL буфера.
     * @param offset   Зміщення в байтах.
     */
    public void rewriteClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueWriteBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * getByteSize(),
                buffer,
                null,
                null
        );
    }

    /**
     * Читає дані з OpenCL буфера.
     *
     * @param clBuffer Ідентифікатор OpenCL буфера.
     */
    public void readClBuffer(long clBuffer) {
        readClBuffer(clBuffer, 0);
    }

    /**
     * Читає дані з OpenCL буфера з певного зміщення.
     *
     * @param clBuffer Ідентифікатор OpenCL буфера.
     * @param offset   Зміщення в байтах.
     */
    public void readClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueReadBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * getByteSize(),
                buffer,
                null,
                null
        );
    }

    /**
     * Змінює розмір буфера.
     *
     * @param newLength Новий розмір буфера.
     */
    public void reSize (int newLength) {
        destroy();
        create(newLength);
    }

    /**
     * Абстрактний метод для оновлення розміру масиву даних.
     *
     * @param length Новий розмір масиву.
     */
    protected abstract void updateArray(int length);

    /**
     * Звільняє ресурси буфера.
     */
    public void destroy (){
        if (buffer != null) {
            MemoryUtil.memFree(buffer);
            buffer = null;
        }
    }

    /**
     * Повертає буфер в вигляді ByteBuffer.
     *
     * @return Поточний ByteBuffer.
     */
    public ByteBuffer getByteBuffer() {
        return buffer;
    }
}
