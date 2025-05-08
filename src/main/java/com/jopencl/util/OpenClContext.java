package com.jopencl.util;

import org.lwjgl.opencl.CL10;

public class OpenClContext {
    public long device;
    public long context;
    public long commandQueue;

//    public static OpenClContext getInstance(){
//        if (openClContext == null) {
//            openClContext = new OpenClContext();
//        }
//
//        return openClContext;
//    }

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
