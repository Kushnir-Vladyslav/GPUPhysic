package org.example.OpenCL;

import org.lwjgl.opencl.CL10;

public class OpenClContext {
    private static OpenClContext openClContext;

    public long device;
    public long context;
    public long commandQueue;

    public static OpenClContext getInstance(){
        if (openClContext == null) {
            openClContext = new OpenClContext();
        }

        return openClContext;
    }

    private OpenClContext() {}

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
