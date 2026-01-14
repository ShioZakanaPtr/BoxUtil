package org.boxutil.base;

import com.fs.starfarer.api.Global;
import de.unkrig.commons.nullanalysis.NotNull;
import org.apache.log4j.Level;
import org.boxutil.manager.KernelCore;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;

import java.nio.IntBuffer;

public abstract class BaseKernelData {
    public final CLProgram program;
    public final CLKernel kernel;

    public BaseKernelData(CLDevice device, @Nullable String loggerTag, String source, @NotNull String programOptions, String mainFunctionName, IntBuffer programState) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        Global.getLogger(KernelCore.class).info("'BoxUtil' kernel creating tag: '" + tag + "'.");

        if (!KernelCore.isValid()) {
            this.program = null;
            this.kernel = null;
            Global.getLogger(KernelCore.class).info("'BoxUtil' kernel create failed because CL context invalid.");
        } else if (mainFunctionName == null || mainFunctionName.isEmpty() || source == null || source.isEmpty()) {
            this.program = null;
            this.kernel = null;
            Global.getLogger(KernelCore.class).info("'BoxUtil' kernel name '" + mainFunctionName + "' or kernel source invalid.");
        } else {
            IntBuffer errorProgram = BufferUtils.createIntBuffer(128);
            IntBuffer errorKernel = BufferUtils.createIntBuffer(128);
            this.program = CL10.clCreateProgramWithSource(KernelCore.getClContext(), source, errorProgram);
            int state = CL10.clBuildProgram(program, device, programOptions, new CLBuildProgramCallback() {
                protected void handleMessage(CLProgram clProgram) {
                    Global.getLogger(KernelCore.class).info(clProgram.getBuildInfoString(device, CL10.CL_PROGRAM_BUILD_LOG));
                }
            });
            if (programState != null) programState.put(state);

            this.kernel = CL10.clCreateKernel(this.program, mainFunctionName, errorKernel);
            this.kernel.checkValid();
            this.program.checkValid();
            if (!this.kernel.isValid() || !this.program.isValid() || state != CL10.CL_SUCCESS) {
                Global.getLogger(KernelCore.class).log(Level.ERROR, "'BoxUtil' kernel name '" + mainFunctionName + "' create failed, program code: '" + errorProgram.get(0) + "', kernel code: '" + errorKernel.get(0) + "'.");
                if (this.kernel.isValid()) CL10.clReleaseKernel(this.kernel);
                if (this.program.isValid()) CL10.clReleaseProgram(this.program);
            } else Global.getLogger(KernelCore.class).info("'BoxUtil' kernel program '" + mainFunctionName + "' has created.");
        }
    }

    public BaseKernelData(@Nullable String loggerTag, String source, String mainFunctionName) {
        this(KernelCore.getCurrentCLDevice(), loggerTag, source, "", mainFunctionName, null);
    }

    public boolean isValid() {
        return this.program != null && this.kernel != null && this.program.isValid() && this.kernel.isValid();
    }

    public void release() {
        if (this.isValid()) {
            CL10.clReleaseKernel(this.kernel);
            CL10.clReleaseProgram(this.program);
        }
    }

    public void enqueueND(int dim, long[] globalSize, long[] localSize) {
        PointerBuffer gs = CommonUtil.createPointerBuffer(globalSize);
        PointerBuffer ls = CommonUtil.createPointerBuffer(localSize);
        CL10.clEnqueueNDRangeKernel(KernelCore.getCurrentCLQueue(), this.kernel, dim, null, gs, ls, null, null);
    }

    public void enqueueND(int dim, long[] globalOffset, long[] globalSize, long[] localSize) {
        PointerBuffer go = CommonUtil.createPointerBuffer(globalOffset);
        PointerBuffer gs = CommonUtil.createPointerBuffer(globalSize);
        PointerBuffer ls = CommonUtil.createPointerBuffer(localSize);
        CL10.clEnqueueNDRangeKernel(KernelCore.getCurrentCLQueue(), this.kernel, dim, go, gs, ls, null, null);
    }

    public void enqueueND(int dim, long[] globalOffset, long[] globalSize, long[] localSize, CLEvent event) {
        PointerBuffer go = CommonUtil.createPointerBuffer(globalOffset);
        PointerBuffer gs = CommonUtil.createPointerBuffer(globalSize);
        PointerBuffer ls = CommonUtil.createPointerBuffer(localSize);
        PointerBuffer e = CommonUtil.createPointerBuffer(1);
        e.put(event.getPointer());
        CL10.clEnqueueNDRangeKernel(KernelCore.getCurrentCLQueue(), this.kernel, dim, go, gs, ls, null, e);
    }
}
