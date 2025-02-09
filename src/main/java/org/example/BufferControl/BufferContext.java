package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.util.Vector;

/**
 * Клас, який представляє контекст буфера для OpenCL.
 * Відповідає за взаємодію з OpenCL буферами та ядрами.
 *
 * @param <K> Тип буфера, який повинен бути підкласом TypeOfBuffer.
 */
public abstract class BufferContext <K extends TypeOfBuffer> {
    protected final Class<K> type;  // Тип буфера
    protected long clBuffer;        // Ідентифікатор OpenCL буфера

    // Буфер для передачі індексу OpenCl буферу ядру
    PointerBuffer pointerBuffer = MemoryUtil.memAllocPointer(1);
    // Список ядер до якого привязаний даний буфер
    Vector<KernelDependency> kernels = new Vector<>();

    protected K nativeBuffer; // Нативний буфер, через який відбувається передача даних

    /**
     * Конструктор.
     *
     * @param type Тип буфера.
     */
    public BufferContext (Class<K> type) {
        this.type = type;
    }

    /**
     * Додає ядро до списку залежностей.
     *
     * @param kernel    Ідентифікатор ядра.
     * @param numberArg Номер аргументу ядра.
     */
    public void addKernel (long kernel, int numberArg) {
        kernels.add(new KernelDependency(kernel, numberArg));
        setNewArg(kernels.lastElement());
    }

    /**
     * Оновлює аргументи для всіх залежних ядер, після оновлення буфера.
     */
    protected void setNewArgs() {
        for (KernelDependency KD : kernels) {
            setNewArg(KD);
        }
    }

    /**
     * Встановлює нового буфера для ядра.
     *
     * @param KD Залежність ядра.
     */
    protected void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(
                KD.targetKernel,
                KD.numberArg,
                pointerBuffer.put(0, clBuffer).rewind()
        );
    }

    /**
     * Повертає тип буфера.
     *
     * @return Тип буфера.
     */
    public Class<K> getType() {
        return type;
    }

    /**
     * Перевіряє, чи був успішно створений OpenCL буфер.
     *
     * @throws IllegalStateException Якщо буфер не був створений.
     */
    protected void checkClBuffer() {
        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }
    }

    /**
     * Внутрішній клас, який представляє залежність ядра.
     */
    protected class KernelDependency {
        public long targetKernel;   // Ідентифікатор ядра
        public int numberArg;       // Номер аргументу

        public KernelDependency (long targetKernel, int numberArg) {
            this.targetKernel = targetKernel;
            this.numberArg = numberArg;
        }
    }

    /**
     * Видаляє ядро зі списку залежностей.
     *
     * @param kernel Ідентифікатор ядра.
     */
    protected void removeKernel(long kernel) {
        kernels.removeIf(value -> value.targetKernel == kernel);
    }

    /**
     * Звільняє ресурси буфера.
     */
    protected void destroy () {
        if (clBuffer != 0) {
            CL10.clReleaseMemObject(clBuffer);
            clBuffer = 0;
        }
        if (nativeBuffer != null) {
            nativeBuffer.destroy();
            nativeBuffer = null;
        }
        if (pointerBuffer != null)  {
            MemoryUtil.memFree(pointerBuffer);
            pointerBuffer = null;
        }
        if (kernels != null) {
            kernels.clear();
        }
    }
}
