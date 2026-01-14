package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;
import org.boxutil.config.BoxConfigs;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.*;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public final class KernelCore {
    private static CLPlatform clPlatform = null;
    private static CLPlatformCapabilities clPlatformCap = null;
    private final static List<CLDevice> CL_DEVICES = new LinkedList<>();
    private final static List<CLDeviceCapabilities> CL_DEVICE_CAPS = new LinkedList<>();
    private static CLContext clContext = null;
    private static CLCommandQueue clQueue = null;
    private static boolean clValid = false;
    private static boolean clFinished = false;

    /**
     * Loading after {@link BoxConfigs#init()}.
     */
    public static void init() {
        if (clFinished) return;
        clFinished = true;
        if (!BoxConfigs.isCLEnable()) {
            Global.getLogger(KernelCore.class).warn("'BoxUtil' OpenCL module was disabled.");
            return;
        }
        if (CL.isCreated()) {
            Global.getLogger(KernelCore.class).error("'BoxUtil' OpenCL was created.");
            return;
        }
        try {
            IntBuffer errorCode = BufferUtils.createIntBuffer(1);
            CL.create();
            Global.getLogger(KernelCore.class).info("'BoxUtil' CL program has created.");
            for (CLPlatform checkPlatform : CLPlatform.getPlatforms()) {
                if (checkPlatform != null && checkPlatform.isValid()) {
                    CLPlatformCapabilities cap = CLCapabilities.getPlatformCapabilities(checkPlatform);
                    if (cap.majorVersion < 1 || !(cap.CL_KHR_gl_sharing || cap.CL_APPLE_gl_sharing)) continue;
                    clPlatformCap = cap;
                    clPlatform = checkPlatform;
                    Global.getLogger(KernelCore.class).info("'BoxUtil' CL platform init: '" + checkPlatform.getInfoString(CL10.CL_PLATFORM_NAME) + "', platform version: '" + checkPlatform.getInfoString(CL10.CL_PLATFORM_VERSION) + "'.");
                    Global.getLogger(KernelCore.class).info("'BoxUtil' CL platform cl version: '" + cap.majorVersion + "." + cap.minorVersion +  "'.");
                    break;
                }
            }
            if (clPlatform == null) {
                Global.getLogger(KernelCore.class).error("'BoxUtil' platform cannot support OpenCL.");
                destroy();
                return;
            }

            Global.getLogger(KernelCore.class).info("===== 'BoxUtil' CL device init stage =====");
            final Filter<CLDevice> glSharingFilter = new Filter<CLDevice>() {
                public boolean accept(final CLDevice device) {
                    final CLDeviceCapabilities cap = CLCapabilities.getDeviceCapabilities(device);
                    return device.getInfoBoolean(CL10.CL_DEVICE_IMAGE_SUPPORT) && (cap.CL_KHR_gl_sharing || cap.CL_APPLE_gl_sharing);
                }
            };
            List<CLDevice> clDeviceCheck = clPlatform.getDevices(CL10.CL_DEVICE_TYPE_GPU, glSharingFilter);
            List<CLDevice> otherDeviceCheck = clPlatform.getDevices(CL10.CL_DEVICE_TYPE_CPU, glSharingFilter);
            if (otherDeviceCheck != null && !otherDeviceCheck.isEmpty()) clDeviceCheck.addAll(otherDeviceCheck);
            otherDeviceCheck = clPlatform.getDevices(CL10.CL_DEVICE_TYPE_ACCELERATOR, glSharingFilter);
            if (otherDeviceCheck != null && !otherDeviceCheck.isEmpty()) clDeviceCheck.addAll(otherDeviceCheck);
            for (CLDevice checkDevice : clDeviceCheck) {
                if (checkDevice != null && checkDevice.isValid()) {
                    CLDeviceCapabilities cap = CLCapabilities.getDeviceCapabilities(checkDevice);
                    CL_DEVICES.add(checkDevice);
                    CL_DEVICE_CAPS.add(CLCapabilities.getDeviceCapabilities(checkDevice));
                    Global.getLogger(KernelCore.class).info("'BoxUtil' CL device found: '" + checkDevice.getInfoString(CL10.CL_DEVICE_NAME) + "', driver version: '" + checkDevice.getInfoString(CL10.CL_DRIVER_VERSION) + "'.");
                    Global.getLogger(KernelCore.class).info("'BoxUtil' CL device cl version: '" + cap.majorVersion + "." + cap.minorVersion +  "'.");
                }
            }
            if (CL_DEVICES.isEmpty()) {
                Global.getLogger(KernelCore.class).log(Level.ERROR, "'BoxUtil' CL device cannot found a valid device.");
                destroy();
                return;
            }

            Global.getLogger(KernelCore.class).info("===== 'BoxUtil' CL context init stage =====");
            Drawable drawable = Display.getDrawable();
            if (drawable == null) {
                Global.getLogger(KernelCore.class).log(Level.ERROR, "'BoxUtil' CL found drawable failed.");
                destroy();
                return;
            }
            clContext = CLContext.create(clPlatform, CL_DEVICES, new CLContextCallback() {
                protected void handleMessage(String s, ByteBuffer byteBuffer) {

                }
            }, drawable, errorCode);
            if (clContext == null || !clContext.isValid()) {
                Global.getLogger(KernelCore.class).log(Level.ERROR, "'BoxUtil' CL context create error, error code: '" + errorCode.get(0) + "'.");
                destroy();
                return;
            }
            Global.getLogger(KernelCore.class).info("'BoxUtil' CL context has created.");
            errorCode.position(0);

            Global.getLogger(KernelCore.class).info("===== 'BoxUtil' CL command queue init stage =====");
            CLDevice selectedDevice = CL_DEVICES.get(Math.min(BoxConfigs.getCLDeviceIndex(), CL_DEVICES.size() - 1));
            CLCommandQueue queue;
            if (selectedDevice == null || !selectedDevice.isValid()) {
                queue = null;
            } else {
                queue = CL10.clCreateCommandQueue(clContext, selectedDevice, CL10.CL_QUEUE_PROFILING_ENABLE, errorCode);
                queue.checkValid();
            }
            if (queue == null || !queue.isValid()) {
                Global.getLogger(KernelCore.class).log(Level.ERROR, "'BoxUtil' CL command queue create error for selected device, error code: '" + errorCode.get(0) + "'.");
                destroy();
                return;
            }
            clQueue = queue;
            Global.getLogger(KernelCore.class).info("'BoxUtil' CL command queue on device: '" + selectedDevice.getInfoString(CL10.CL_DEVICE_NAME) + "' has created.");
            Global.getLogger(KernelCore.class).info("'BoxUtil' OpenCL context create finished.");
            clValid = true;
        } catch (LWJGLException e) {
            destroy();
            CommonUtil.printThrowable(KernelCore.class, "'BoxUtil' OpenCL Context create error: ", e);
        }
    }

    public static void destroy() {
        clValid = false;
        if (clQueue != null && clQueue.isValid()) {
            CL10.clReleaseCommandQueue(clQueue);
            clQueue = null;
        }
        if (clContext != null && clContext.isValid()) {
            CL10.clReleaseContext(clContext);
            clContext = null;
        }
        clPlatform = null;
        clPlatformCap = null;
        CL_DEVICES.clear();
        CL_DEVICE_CAPS.clear();
        if (CL.isCreated()) CL.destroy();
    }

    public static boolean isInitialized() {
        return clFinished;
    }

    public static boolean isValid() {
        return clValid;
    }

    public static CLPlatform getClPlatform() {
        return clPlatform;
    }

    public static CLPlatformCapabilities getClPlatformCapabilities() {
        return clPlatformCap;
    }

    public static List<CLDevice> getAllCLDevice() {
        return new LinkedList<>(CL_DEVICES);
    }

    public static CLDevice getCurrentCLDevice() {
        if (CL_DEVICES.isEmpty() || !isValid()) return null;
        return CL_DEVICES.get(Math.min(BoxConfigs.getCLDeviceIndex(), CL_DEVICES.size() - 1));
    }

    public static List<CLDeviceCapabilities> getAllCLDeviceCapabilities() {
        return new LinkedList<>(CL_DEVICE_CAPS);
    }

    public static CLDeviceCapabilities getCurrentCLDeviceCapabilities() {
        if (CL_DEVICE_CAPS.isEmpty() || !isValid()) return null;
        return CL_DEVICE_CAPS.get(Math.min(BoxConfigs.getCLDeviceIndex(), CL_DEVICES.size() - 1));
    }

    public static CLContext getClContext() {
        return clContext;
    }

    public static CLCommandQueue getCurrentCLQueue() {
        return clQueue;
    }

    private KernelCore() {}
}
