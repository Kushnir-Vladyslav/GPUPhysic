package org.example.BufferControl;

import org.lwjgl.opencl.CL10;

public enum MemoryAccessControl {
    HOST_RW_DEVICE_RW {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_READ_WRITE;
        }
    },
    HOST_W_DEVICE_RW {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_READ_WRITE;
        }
    },
    HOST_R_DEVICE_RW {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_READ_WRITE;
        }
    },
    HOST_RW_DEVICE_W {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_WRITE_ONLY;
        }
    },
    HOST_W_DEVICE_W {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_WRITE_ONLY;
        }
    },
    HOST_R_DEVICE_W {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_WRITE_ONLY;
        }
    },
    HOST_RW_DEVICE_R {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_READ_ONLY;
        }
    },
    HOST_W_DEVICE_R {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_USE_HOST_PTR | CL10.CL_MEM_READ_ONLY;
        }
    },
    HOST_R_DEVICE_R {
        @Override
        public int getFlags() {
            return CL10.CL_MEM_READ_ONLY;
        }
    };

    public abstract int getFlags ();
}
