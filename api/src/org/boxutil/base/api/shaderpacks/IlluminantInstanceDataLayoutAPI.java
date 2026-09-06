package org.boxutil.base.api.shaderpacks;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.InstanceType;

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
