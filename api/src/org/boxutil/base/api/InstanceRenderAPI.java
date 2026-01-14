package org.boxutil.base.api;

import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.MaterialData;

import java.nio.FloatBuffer;
import java.util.List;

public interface InstanceRenderAPI {
    /**
     * When call it, will reset data and delete related objects.<p>
     * A.K.A. <code>free()</code> for instance data resources.
     */
    void resetInstanceData();

    List<InstanceDataAPI> getInstanceData();

    /**
     * For rendering a lots of entities (such as 10k+ entities) at once.<p>
     * For all render objects, location/facing/size/color/emissive/alpha data all form {@link InstanceDataAPI}, and overlay to {@link org.boxutil.base.api.RenderDataAPI} implements.<p>
     * Render count decided by <code>List.size()</code>.<p>
     * Have size limit, pick smallest from {@link BoxConfigs#getMaxInstanceDataSize()}.<p>
     * This entity attribute, {@link MaterialData#getColor()} and {@link MaterialData#getEmissiveColor()} both apply to each instance when rendering.<p>
     * For model matrix, each instance is also derived from this entity.
     *
     * @param instanceData set to 'null' or empty 'List' -> clear, or a 'List' what isn't empty.
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_SUCCESS} when null list that set to empty list.
     */
    byte setInstanceData(@Nullable List<InstanceDataAPI> instanceData);

    /**
     * Reset global timer by manual.<p>
     * Similar to {@link #setInstanceData(List)}.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit.
     */
    byte setInstanceData(@Nullable List<InstanceDataAPI> instanceData, float fadeIn, float full, float fadeOut);

    /**
     * Have size limit {@link BoxConfigs#getMaxInstanceDataSize()}.<p>
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit, return {@link BoxEnum#STATE_FAILED_OTHER} when added a null object.
     */
    byte addInstanceData(@NotNull InstanceDataAPI instanceData);

    /**
     * Use it when you changed the instance data.<p>
     * Put all data, but without calculating.<p>
     * For calculating, if running on JVM mode, it will not create any tmp-TBO, will get a matrix-TBO finally.<p>
     * <p>
     * <strong>Should have reserved size for instance-list, if adds more data later. If not, will clear previous data of all in texture objects when submit.</strong><p>
     * If list size has increased, will force to submit for all data.<p>
     * <p>
     * In half-float mode, Cost <code>64 Byte</code> of vRAM each 2D-instance, when running on GL mode, entirely GPU calculate; In other words, rendering <code>32768</code> instances will cost <code>2 MiB</code> of vRAM.<p>
     * For 3D-instance cost <code>88 Byte</code> and <code>32768</code> instances cost <code>2.75 MiB</code> of vRAM.<p>
     * Cost <code>96 Byte</code>(2D) and <code>144 Byte</code>(3D) vRAM in normal-float(fp32) mode for each instance data.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when an empty instance data list or refresh count is zero.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    byte submitInstanceData();

    /**
     * Optional.<p>
     * Just <code>malloc()</code> without any submit call.
     *
     * @param dataNum must be positive integer.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    byte mallocInstanceData(int dataNum);

    /**
     * For instance data if it is closely related to CPU data or calculating.<p>
     * Use it when you changed the instance data.<p>
     * Put all data, and it is unneeded to calculate, so <strong>don't</strong> call any refresh method, just submit it and set rendering count.<p>
     * <p>
     * <strong>Should have reserved size for instance-list, if adds more data later. If not, will clear previous data of all in texture objects when submit.</strong><p>
     * If list size has increased, will force to submit for all data.<p>
     * <p>
     * In half-float mode, Cost <code>20 Byte</code> of vRAM each 2D-instance, when running on GL mode, entirely GPU calculate; In other words, rendering <code>32768</code> instances will cost <code>640 KiB</code> of vRAM.<p>
     * For 3D-instance cost <code>28 Byte</code> and <code>32768</code> instances cost <code>896 KiB</code> of vRAM.<p>
     * Cost <code>32 Byte</code>(2D) and <code>48 Byte</code>(3D) vRAM in normal-float(fp32) mode for each instance data.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit, return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    byte submitFixedInstanceData();

    /**
     * Optional.<p>
     * Just <code>malloc()</code> without any submit call.
     *
     * @param dataNum must be positive integer.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    byte mallocFixedInstanceData(int dataNum);

    /**
     * Default value is <code>false</code>.
     */
    boolean isMappingInstanceSubmit();

    /**
     * For some very slight data, use <code>glBufferSubData()</code> may faster.<p>
     * Besides, some devices(some ARM SoC) may slower with <code>glMapBufferRange()</code>, decided by the drive how implements it.
     *
     * @param mappingMode to controls whether submit use <code>glMapBufferRange()</code>, else use <code>glBufferSubData()</code>.
     */
    void setMappingInstanceSubmit(boolean mappingMode);

    /**
     * Use it when you changed the instance data.<p>
     * <strong>Should have reserved size for it, memory expand is an expensive operation.<p>
     *
     * Make sure has call {@link InstanceRenderAPI#mallocInstance(InstanceType, int)} when without valid memory.</strong><p>
     */
    void submitInstance();

    /**
     * <strong>Make sure call it before call {@link InstanceRenderAPI#submitInstance()} when without valid memory.</strong>
     */
    void mallocInstance(InstanceType target, int dataNum);

    @Deprecated
    void sysRefreshInstanceData(float amount, boolean isPaused);

    boolean isNeedRefreshInstanceData();

    boolean isAlwaysRefreshInstanceData();

    /**
     * When changed the instance data(Not fixed instance data), call it.
     */
    void callRefreshInstanceData(boolean refresh);

    void setAlwaysRefreshInstanceData(boolean refresh);

    /**
     * @return true if after called {@link InstanceRenderAPI#submitFixedInstanceData()} or {@link InstanceRenderAPI#mallocFixedInstanceData(int)}.
     */
    @Deprecated
    boolean isCalledFixedSubmit();

    @Deprecated
    boolean haveInstanceData();

    boolean haveValidInstanceData();

    int getValidInstanceDataCount();

    @Deprecated
    FloatBuffer[][] getInstanceDataTmpBufferJVM();

    @Deprecated
    int[][] getInstanceDataTBO();

    @Deprecated
    int[][] getInstanceDataTBOTex();

//    CLMem[][] getInstanceDataMemory();

    @Deprecated
    void putShaderInstanceData();

    int getInstanceDataRefreshIndex();

    int getInstanceDataRefreshOffset();

    int getInstanceDataRefreshSize();

    /**
     * @param index will refresh instance data start from this index.
     */
    void setInstanceDataRefreshIndex(int index);

    /**
     * @param targetIndex will refresh instance data to instance data index of memory.
     */
    void setInstanceDataRefreshOffset(int targetIndex);

    /**
     * @param size Will refresh instance data count.
     */
    void setInstanceDataRefreshSize(int size);

    void setInstanceDataRefreshAllFromCurrentIndex();

    int getRenderingCount();

    void setRenderingCount(int num);

    int getRenderingOffset();

    /**
     * Should reset the rendering count after that.
     */
    void setRenderingOffset(int index);

    void setRenderingAllInstanceFromCurrentOffset();

    @Deprecated
    byte getInstanceDataType();

    @Deprecated
    boolean isInstanceData2D();

    /**
     * Must call it before {@link InstanceRenderAPI#submitInstanceData()}, or after {@link InstanceRenderAPI#resetInstanceData} has called.
     */
    @Deprecated
    void setUseInstanceData2D();

    @Deprecated
    boolean isInstanceData3D();

    /**
     * Must call it before {@link InstanceRenderAPI#submitInstanceData()}, or after {@link InstanceRenderAPI#resetInstanceData} has called.
     */
    @Deprecated
    void setUseInstanceData3D();

    @Deprecated
    boolean isInstanceDataCustom();

    @Deprecated
    void setUseInstanceDataCustom();

    /**
     * Use 2D-data for default.<p>
     * Must call it before {@link InstanceRenderAPI#submitInstanceData()}, or after {@link InstanceRenderAPI#resetInstanceData} has called.
     */
    @Deprecated
    void setUseDefaultInstanceData();

    MemoryBlock getInstanceDataMemory();

    /**
     * Will always computes once for the same memory range.<p>
     * And if any rendering entity with not-full-range refresh, will only refresh the minimum range for them.
     */
    void setSharedInstanceData(InstanceRenderAPI renderData);

    /**
     * Equivalent to:
     * <pre>
     * {@code
     * MemoryBlock old_mem = this->memory;
     * if (old_mem) free(old_mem);
     * this->memory = new_mem;
     * }
     * </pre>
     */
    void resetMemory(MemoryBlock memory);

    void resetMemory();

    /**
     * Also it is byte size.
     */
    @Deprecated
    byte getInstanceDataFormat();

    @Deprecated
    boolean isNormalFloatFormatInstanceData();

    @Deprecated
    void setUseNormalFormatFloatInstanceData();

    @Deprecated
    boolean isHalfFloatFormatInstanceData();

    @Deprecated
    void setUseHalfFloatFormatInstanceData();

    @Deprecated
    void setUseDefaultFormatInstanceData();

    float getInstanceTimerOverride();

    /**
     * Override timer of all in-vRAM instance data to this value.
     *
     * @param alpha set less than 0 to disable override.
     * @param state valid state: {@link BoxEnum#TIMER_IN}, {@link BoxEnum#TIMER_FULL}, {@link BoxEnum#TIMER_OUT}.
     */
    void setInstanceTimerOverride(float alpha, byte state);

    void copyInstanceTimerOverride(InstanceRenderAPI renderData);
}
