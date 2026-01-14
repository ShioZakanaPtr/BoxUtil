package org.boxutil.base.api.shaderpacks;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;

import java.util.List;

public interface IlluminantInstanceDataLayoutAPI {
    /**
     * Will replace origin method.
     */
    void submitInstance(final BaseIlluminantData entity);

    /**
     * Will replace origin method.
     */
    void mallocInstance(final BaseIlluminantData entity, InstanceType target, int dataNum);

    /**
     * Will replace origin method.
     */
    void resetInstanceData(final BaseIlluminantData entity);

    void systemAdvance(final BaseIlluminantData entity, float amount, boolean isPaused, boolean isEntityRemoveLater, boolean ignoreInstanceCompute);
}
