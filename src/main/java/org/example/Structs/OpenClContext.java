package org.example.Structs;

import org.lwjgl.opencl.CL10;

public class OpenClContext {
    public long device;
    public long context;
    public long commandQueue;

    public void destroy () {
        if(commandQueue != 0) {
            CL10.clReleaseCommandQueue(commandQueue);
            commandQueue = 0;
        }

        if(context != 0) {
            CL10.clReleaseContext(context);
            context = 0;
        }
    }
}
