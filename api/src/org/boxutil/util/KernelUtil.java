package org.boxutil.util;

import com.fs.starfarer.api.Global;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.apache.log4j.Level;
import org.boxutil.manager.KernelCore;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.*;
import java.util.List;

public final class KernelUtil {
    public static CLMem ioBuffer(long flags, DoubleBuffer buffer, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, buffer, errorReport);
    }

    public static CLMem ioBuffer(long flags, FloatBuffer buffer, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, buffer, errorReport);
    }

    public static CLMem ioBuffer(long flags, IntBuffer buffer, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, buffer, errorReport);
    }

    public static CLMem ioBuffer(long flags, LongBuffer buffer, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, buffer, errorReport);
    }

    public static CLMem ioBuffer(long flags, ShortBuffer buffer, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, buffer, errorReport);
    }

    public static CLMem ioBuffer(long flags, long dataSize, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10.clCreateBuffer(KernelCore.getClContext(), flags, dataSize, errorReport);
    }

    public static CLMem shareBuffer(long flags, int obj, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10GL.clCreateFromGLBuffer(KernelCore.getClContext(), flags, obj, errorReport);
    }

    public static CLMem shareRBO(long flags, int obj, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10GL.clCreateFromGLRenderbuffer(KernelCore.getClContext(), flags, obj, errorReport);
    }

    /**
     * @param isWrite set ture is read only, else write only. Not supported read-write for image before OpenCL2.0
     */
    public static CLMem shareTexture2D(int textureID, int mipLevel, boolean isWrite, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10GL.clCreateFromGLTexture2D(KernelCore.getClContext(), isWrite ? CL10.CL_MEM_WRITE_ONLY : CL10.CL_MEM_READ_ONLY, GL11.GL_TEXTURE_2D, mipLevel, textureID, errorReport);
    }

    /**
     * @param isWrite set ture is read only, else write only. Not supported read-write for image before OpenCL2.0
     */
    public static CLMem shareTexture3D(int texture, int mipLevel, boolean isWrite, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized()) return null;
        return CL10GL.clCreateFromGLTexture3D(KernelCore.getClContext(), isWrite ? CL10.CL_MEM_WRITE_ONLY : CL10.CL_MEM_READ_ONLY, GL12.GL_TEXTURE_3D, mipLevel, texture, errorReport);
    }

    /**
     * Include 1D\2D\3D and more.
     * Required OpenCL1.2 support.
     *
     * @param isWrite set ture is read only, else write only. Not supported read-write for image before OpenCL2.0
     */
    public static CLMem shareTextureAny(int target, int textureID, int mipLevel, boolean isWrite, @Nullable IntBuffer errorReport) {
        if (!KernelCore.isInitialized() || !KernelCore.getClPlatformCapabilities().OpenCL12) return null;
        return CL12GL.clCreateFromGLTexture(KernelCore.getClContext(), isWrite ? CL10.CL_MEM_WRITE_ONLY : CL10.CL_MEM_READ_ONLY, target, mipLevel, textureID, errorReport);
    }

    public static PointerBuffer createEventPointerBuffer(List<CLEvent> array) {
        PointerBuffer pointerBuffer = new PointerBuffer(array.size());
        for (CLEvent object : array) pointerBuffer.put(object.getPointer());
        return pointerBuffer.flip();
    }

    public static PointerBuffer createProgramPointerBuffer(List<CLProgram> array) {
        PointerBuffer pointerBuffer = new PointerBuffer(array.size());
        for (CLProgram object : array) pointerBuffer.put(object.getPointer());
        return pointerBuffer.flip();
    }

    public static PointerBuffer createKernelPointerBuffer(List<CLKernel> array) {
        PointerBuffer pointerBuffer = new PointerBuffer(array.size());
        for (CLKernel object : array) pointerBuffer.put(object.getPointer());
        return pointerBuffer.flip();
    }

    public static PointerBuffer createMemoryPointerBuffer(List<CLMem> array) {
        PointerBuffer pointerBuffer = new PointerBuffer(array.size());
        for (CLMem object : array) pointerBuffer.put(object.getPointer());
        return pointerBuffer.flip();
    }

    public static PointerBuffer createDevicePointerBuffer(List<CLDevice> array) {
        PointerBuffer pointerBuffer = new PointerBuffer(array.size());
        for (CLDevice object : array) pointerBuffer.put(object.getPointer());
        return pointerBuffer.flip();
    }

    private KernelUtil() {}
}
