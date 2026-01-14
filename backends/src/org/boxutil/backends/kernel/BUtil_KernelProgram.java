package org.boxutil.backends.kernel;

import org.boxutil.base.BaseKernelData;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.opencl.CLDevice;

import java.nio.IntBuffer;

public class BUtil_KernelProgram extends BaseKernelData {
    public BUtil_KernelProgram(CLDevice device, @Nullable String loggerTag, String source, String programOptions, String mainFunctionName, IntBuffer programState) {
        super(device, loggerTag, source, programOptions, mainFunctionName, programState);
    }

    public BUtil_KernelProgram(@Nullable String loggerTag, String source, String mainFunctionName) {
        super(loggerTag, source, mainFunctionName);
    }
}
