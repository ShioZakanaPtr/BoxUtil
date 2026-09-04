package org.boxutil.base;

import org.boxutil.backends.core.instancedrendering.BUtil_InstanceDataMemoryPool;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.InstanceDataMemoryPool;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.attribute.MaterialData;
import org.lwjgl.BufferUtils;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseInstanceRenderData extends BaseRenderData implements InstanceRenderAPI {
    protected int instanceRefreshIndex = 0;
    protected int instanceRefreshOffset = 0;
    protected int instanceRefreshSize = 0;
    protected int instanceRenderingCount = 0;
    protected int instanceRenderingOffset = 0;
    protected float instanceTimerOverride = -1.0f;
    protected volatile MemoryBlock memory = null;
    protected final boolean[] needRefreshInstanceData = new boolean[]{false, false}; // once, always
    protected boolean mappingSubmit = false;
    protected List<InstanceDataAPI> instanceData = null;

    public BaseInstanceRenderData() {}

    public void delete() {
        super.delete();
        this.resetInstanceData();
    }

    public void glDraw() {
        ShaderCore.getDefaultQuadObject().glDraw(Math.min(this.getValidInstanceDataCount(), this.getRenderingCount()));
    }

    protected void resetInstanceDataResource() {
        if (this.memory != null) InstanceDataMemoryPool.free(this.memory);
        this.memory = null;
    }

    protected void resetInstanceDataClient() {
        if (this.instanceData != null) this.instanceData.clear();
        this.instanceData = null;
        this.instanceRefreshIndex = 0;
        this.instanceRefreshOffset = 0;
        this.instanceRefreshSize = 0;
        this.instanceRenderingCount = 0;
        this.instanceRenderingOffset = 0;
        this.needRefreshInstanceData[0] = false;
        this.needRefreshInstanceData[1] = false;
        this.mappingSubmit = false;
    }

    /**
     * When call it, will reset data and delete related objects.<p>
     * A.K.A. <code>free()</code> for instance data resources.
     */
    public void resetInstanceData() {
        this.sync_lock.lock();
        this.resetInstanceDataResource();
        this.resetInstanceDataClient();
        this.sync_lock.unlock();
    }

    public List<InstanceDataAPI> getInstanceData() {
        return this.instanceData;
    }

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
    public byte setInstanceData(@Nullable List<InstanceDataAPI> instanceData) {
        this.sync_lock.lock();
        if (instanceData == null) {
            this.instanceData = new ArrayList<>(8);
            this.sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        List<InstanceDataAPI> check = instanceData;
        int limit = BoxConfigs.getMaxInstanceDataSize();
        if (instanceData.size() > limit) check = check.subList(limit - 1, instanceData.size());
        float[] checkTimer = new float[3];
        float[] tmp;
        for (InstanceDataAPI data : check) {
            tmp = data.getTimer();
            if (checkTimer[0] > -500.0f && tmp[1] < checkTimer[0]) checkTimer[0] = tmp[1];
            if (checkTimer[1] > -500.0f && tmp[2] < checkTimer[1]) checkTimer[1] = tmp[2];
            if (checkTimer[2] > -500.0f && tmp[3] < checkTimer[2]) checkTimer[2] = tmp[3];
        }
        this.globalTimer[1] = checkTimer[0];
        this.globalTimer[2] = checkTimer[1];
        this.globalTimer[3] = checkTimer[2];
        this.instanceData = check;
        this.sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Reset global timer by manual.<p>
     * Similar to {@link #setInstanceData(List)}.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when null list that set to empty list.
     */
    public byte setInstanceData(@Nullable List<InstanceDataAPI> instanceData, float fadeIn, float full, float fadeOut) {
        this.sync_lock.lock();
        if (instanceData == null) {
            this.instanceData = new ArrayList<>(8);
            this.sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        List<InstanceDataAPI> check = instanceData;
        int limit = BoxConfigs.getMaxInstanceDataSize();
        if (instanceData.size() > limit) check = check.subList(limit - 1, instanceData.size());
        this.setGlobalTimer(fadeIn, full, fadeOut);
        this.instanceData = check;
        this.sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Have size limit {@link BoxConfigs#getMaxInstanceDataSize()}.<p>
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit.
     */
    public byte addInstanceData(@NotNull InstanceDataAPI instanceData) {
        this.sync_lock.lock();
        if (this.instanceData == null) this.instanceData = new ArrayList<>();
        if (this.instanceData.size() > BoxConfigs.getMaxInstanceDataSize()) {
            this.sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        float[] tmp = instanceData.getTimer();
        if (tmp[1] < this.globalTimer[1] && tmp[1] > -500.0f)
            this.globalTimer[1] = tmp[1];
        if (tmp[2] < this.globalTimer[2] && tmp[2] > -500.0f)
            this.globalTimer[2] = tmp[2];
        if (tmp[3] < this.globalTimer[3] && tmp[3] > -500.0f)
            this.globalTimer[3] = tmp[3];
        this.instanceData.add(instanceData);
        this.sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Recommend to use {@link InstanceRenderAPI#submitInstance()}.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit, return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    public byte submitInstanceData() {
        if (this.instanceData == null || this.instanceData.isEmpty()) return BoxEnum.STATE_FAILED;
        final boolean newBuf = this.memory == null;
        if (newBuf) this.mallocInstanceData(this.instanceData.size());
        if (this.memory == null || this.memory.is_type_fixed()) return BoxEnum.STATE_FAILED_OTHER;
        if (newBuf) {
            this.setInstanceDataRefreshIndex(0);
            this.setInstanceDataRefreshOffset(0);
            this.setInstanceDataRefreshAllFromCurrentIndex();
        }
        this.submitInstance();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Recommend to use {@link InstanceRenderAPI#mallocInstance(InstanceType, int)}.
     *
     * @param dataNum must be positive integer.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    public byte mallocInstanceData(int dataNum) {
        if (dataNum < 1) return BoxEnum.STATE_FAILED;
        this.mallocInstance(this.isInstanceData2D() ? InstanceType.DYNAMIC_2D : InstanceType.DYNAMIC_3D, dataNum);
        return BoxEnum.STATE_SUCCESS;
    }

    protected void _packingInstanceData(final ByteBuffer rawBuffer, int offset, final InstanceType type, int index, int limit, boolean isFixed) {
        final FloatBuffer buffer = rawBuffer.asFloatBuffer();
        InstanceDataAPI data;
        float[] ptr;
        int pos = offset;
        for (int i = index; i < limit; ++i) {
            data = this.instanceData.get(i);
            if (data == null) ptr = new float[type.getCompactComponent()];
            else ptr = isFixed ? data._pickFixed_ssbo() : data._pickDynamic_ssbo();

            buffer.put(pos + type.getCompactOffset(), ptr, 0, type.getCompactComponent());
            pos += type.getComponent();
        }
    }

    public void submitInstance() {
        this.sync_lock.lock();
        final int refreshSize = this.instanceRefreshSize;
        if (!this.haveValidInstanceData() || refreshSize < 1) {
            this.sync_lock.unlock();
            return;
        }
        if (refreshSize > this.memory.instance_count()) InstanceDataMemoryPool.realloc(this.memory, refreshSize);

        final int refreshIndex = this.instanceRefreshIndex, refreshOffset = this.instanceRefreshOffset;
        final boolean mappingBuffer = this.mappingSubmit;
        this.sync_lock.unlock();

        BUtil_ResourceStorage.sharedResource().offerSubmitInstance(() -> {
            if (this.hasDelete()) return;
            this.sync_lock.lock();
            if (this.memory == null || this.memory.is_free()) {
                this.sync_lock.unlock();
                return;
            }

            final boolean l_isFixed = this.memory.is_type_fixed();
            final var l_type = this.memory.meta();
            final var l_pool = BUtil_InstanceDataMemoryPool.getPool(l_type);
            final var l_lock = l_pool.getGPULock();

            l_lock.lock();
            final boolean l_persistentMapping = l_pool.isPersistentMapping() && l_pool.getMappingBuffer() != null;
            final int l_bufferTarget = l_pool.getPoolBehavior().glTarget,
                    l_refreshLimit = refreshIndex + refreshSize,
                    l_uploadOffset = l_pool.getPoolBehavior().reservedSize;
            final long l_refreshByteSize = (long) l_type.getSize() * refreshSize;

            ByteBuffer l_rawBuffer = null;
            if (!mappingBuffer && !l_persistentMapping) {
                l_rawBuffer = BufferUtils.createByteBuffer((int) l_refreshByteSize);
                this._packingInstanceData(l_rawBuffer, 0, l_type, refreshIndex, l_refreshLimit, l_isFixed);
            }

            final int l_ssbo = InstanceDataMemoryPool.getBufferID(l_type);
            if (l_ssbo < 1) {
                l_lock.unlock();
                this.sync_lock.unlock();
                return;
            }
            final long l_refreshByteOffset = this.memory.address() + (long) l_type.getSize() * refreshOffset + l_uploadOffset;

            if (l_persistentMapping) {
                this._packingInstanceData(l_pool.getMappingBuffer(), (int) (l_refreshByteOffset >> 2), l_type, refreshIndex, l_refreshLimit, l_isFixed);
                l_lock.unlock();
                this.sync_lock.unlock();
                return;
            }

            GLWrapper.Buffer.glBindBuffer(l_bufferTarget, l_ssbo);
            if (mappingBuffer) {
                final int l_access = GLWrapper.Buffer.GL_MAP_WRITE_BIT | GLWrapper.Buffer.GL_MAP_UNSYNCHRONIZED_BIT | GLWrapper.Buffer.GL_MAP_INVALIDATE_RANGE_BIT;
                l_rawBuffer = GLWrapper.Buffer.glMapBufferRange(l_bufferTarget, l_refreshByteOffset, l_refreshByteSize, l_access, null);
                if (l_rawBuffer == null || l_rawBuffer.capacity() < l_refreshByteSize) {
                    GLWrapper.Buffer.glUnmapBuffer(l_bufferTarget);
                    l_lock.unlock();
                    this.sync_lock.unlock();
                    return;
                }

                this._packingInstanceData(l_rawBuffer, 0, l_type, refreshIndex, l_refreshLimit, l_isFixed);
            }

            l_rawBuffer.position(0).limit(l_rawBuffer.capacity()); // assert not null
            if (mappingBuffer) GLWrapper.Buffer.glUnmapBuffer(l_bufferTarget);
            else GLWrapper.Buffer.glBufferSubData(l_bufferTarget, l_refreshByteOffset, l_rawBuffer);
            l_lock.unlock();
            this.sync_lock.unlock();
        });
    }

    public void mallocInstance(InstanceType target, int dataNum) {
        if (dataNum < 1 || !BoxConfigs.isShaderEnable()) return;
        this.resetMemory(InstanceDataMemoryPool.malloc(target, dataNum));
    }

    /**
     * Recommend to use {@link InstanceRenderAPI#submitInstance()}.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success, return {@link BoxEnum#STATE_FAILED} when over limit, return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    public byte submitFixedInstanceData() {
        if (this.instanceData == null || this.instanceData.isEmpty()) return BoxEnum.STATE_FAILED;
        final boolean newBuf = this.memory == null;
        if (newBuf) this.mallocFixedInstanceData(this.instanceData.size());
        if (this.memory == null || !this.memory.is_type_fixed()) return BoxEnum.STATE_FAILED_OTHER;
        if (newBuf) {
            this.setInstanceDataRefreshIndex(0);
            this.setInstanceDataRefreshOffset(0);
            this.setInstanceDataRefreshAllFromCurrentIndex();
        }
        this.submitInstance();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Recommend to use {@link InstanceRenderAPI#mallocInstance(InstanceType, int)}.
     *
     * @param dataNum must be positive integer.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    @Deprecated
    public byte mallocFixedInstanceData(int dataNum) {
        if (dataNum < 1) return BoxEnum.STATE_FAILED;
        this.mallocInstance(this.isInstanceData2D() ? InstanceType.FIXED_2D : InstanceType.FIXED_3D, dataNum);
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Default value is <code>false</code>.
     */
    public boolean isMappingInstanceSubmit() {
        return this.mappingSubmit;
    }

    /**
     * <b>NOTE:</b> Ineffective if device was <b>OpenGL 4.4+</b> supported, will force updating to persistent mapping buffer.<p>
     * For some very slight data, use <code>glBufferSubData()</code> may faster.<p>
     * Besides, some devices(some ARM SoC) may slower with <code>glMapBufferRange()</code>, decided by the drive how implements it.
     *
     * @param mappingMode to controls whether submit use <code>glMapBufferRange()</code>, else use <code>glBufferSubData()</code>.
     */
    public void setMappingInstanceSubmit(boolean mappingMode) {
        this.sync_lock.lock();
        this.mappingSubmit = mappingMode;
        this.sync_lock.unlock();
    }

    @Deprecated
    public void sysRefreshInstanceData(float amount, boolean isPaused) {}

    public boolean isNeedRefreshInstanceData() {
        return this.needRefreshInstanceData[0] || this.needRefreshInstanceData[1];
    }

    public boolean isAlwaysRefreshInstanceData() {
        return this.needRefreshInstanceData[1];
    }

    /**
     * When changed the instance data(Not fixed instance data), call it.
     */
    public void callRefreshInstanceData(boolean refresh) {
        this.needRefreshInstanceData[0] = refresh;
    }

    public void setAlwaysRefreshInstanceData(boolean refresh) {
        this.needRefreshInstanceData[1] = refresh;
    }

    /**
     * @return true if after called {@link InstanceRenderAPI#submitFixedInstanceData()} or {@link InstanceRenderAPI#mallocFixedInstanceData(int)}.
     */
    @Deprecated
    public boolean isCalledFixedSubmit() {
        return this.needRefreshInstanceData[2];
    }

    @Deprecated
    public boolean haveInstanceData() {
        return this.instanceData != null && !this.instanceData.isEmpty();
    }

    public boolean haveValidInstanceData() {
        return this.memory != null && this.memory.reference() > 0;
    }

    public int getValidInstanceDataCount() {
        return this.haveValidInstanceData() ? this.memory.instance_count() : 0;
    }

    @Deprecated
    public FloatBuffer[][] getInstanceDataTmpBufferJVM() {
        return null;
    }

    @Deprecated
    public int[][] getInstanceDataTBO() {
        return null;
    }

    @Deprecated
    public int[][] getInstanceDataTBOTex() {
        return null;
    }

    @Deprecated
    public void putShaderInstanceData() {}

    public int getInstanceDataRefreshIndex() {
        return this.instanceRefreshIndex;
    }

    public int getInstanceDataRefreshOffset() {
        return this.instanceRefreshOffset;
    }

    public int getInstanceDataRefreshSize() {
        return this.instanceRefreshSize;
    }

    /**
     * @param index will refresh instance data start from this index.
     */
    public void setInstanceDataRefreshIndex(int index) {
        this.sync_lock.lock();
        if (this.instanceData == null) {
            this.sync_lock.unlock();
            return;
        }
        this.instanceRefreshIndex = Math.min(Math.max(index, 0), Math.max(this.instanceData.size() - 1, 0));
        this.sync_lock.unlock();
    }

    /**
     * @param targetIndex will refresh instance data to instance data index of memory.
     */
    public void setInstanceDataRefreshOffset(int targetIndex) {
        this.sync_lock.lock();
        if (this.memory == null) {
            this.sync_lock.unlock();
            return;
        }
        this.instanceRefreshOffset = Math.min(Math.max(targetIndex, 0), this.memory.instance_count() - 1);
        this.sync_lock.unlock();
    }

    /**
     * @param size Will refresh instance data count.
     */
    public void setInstanceDataRefreshSize(int size) {
        this.sync_lock.lock();
        if (this.instanceData == null) {
            this.sync_lock.unlock();
            return;
        }
        final int listSize = this.instanceData.size();
        this.instanceRefreshSize = this.instanceRefreshIndex + size > listSize ? listSize - this.instanceRefreshIndex : Math.max(size, 0);
        this.sync_lock.unlock();
    }

    public void setInstanceDataRefreshAllFromCurrentIndex() {
        this.sync_lock.lock();
        if (this.instanceData == null) {
            this.sync_lock.unlock();
            return;
        }
        this.instanceRefreshSize = this.instanceData.size() - this.instanceRefreshIndex;
        this.sync_lock.unlock();
    }

    public int getRenderingCount() {
        return this.instanceRenderingCount;
    }

    public void setRenderingCount(int num) {
        this.sync_lock.lock();
        if (this.memory == null) {
            this.sync_lock.unlock();
            return;
        }
        final int instanceCount = this.memory.instance_count();
        this.instanceRenderingCount = this.instanceRenderingOffset + num > instanceCount ? instanceCount - this.instanceRenderingOffset : Math.max(num, 0);
        this.sync_lock.unlock();
    }

    public int getRenderingOffset() {
        return this.instanceRenderingOffset;
    }

    /**
     * Should reset the rendering count after that.
     */
    public void setRenderingOffset(int index) {
        this.sync_lock.lock();
        if (this.memory == null) {
            this.sync_lock.unlock();
            return;
        }
        this.instanceRenderingOffset = Math.min(Math.max(index, 0), this.memory.instance_count());
        this.sync_lock.unlock();
    }

    public void setRenderingAllInstanceFromCurrentOffset() {
        this.sync_lock.lock();
        if (this.memory == null) {
            this.sync_lock.unlock();
            return;
        }
        this.instanceRenderingCount = this.memory.instance_count() - this.instanceRenderingOffset;
        this.sync_lock.unlock();
    }

    @Deprecated
    public byte getInstanceDataType() {
        return 0;
    }

    @Deprecated
    public boolean isInstanceData2D() {
        return this.memory == null || this.memory.is_type_2D();
    }

    /**
     * Must call it before {@link InstanceRenderAPI#submitInstanceData()}, or after {@link InstanceRenderAPI#resetInstanceData} has called.
     */
    @Deprecated
    public void setUseInstanceData2D() {}

    @Deprecated
    public boolean isInstanceData3D() {
        return this.memory != null && !this.memory.is_type_2D();
    }

    /**
     * Must call it before {@link InstanceRenderAPI#submitInstanceData()}, or after {@link InstanceRenderAPI#resetInstanceData} has called.
     */
    @Deprecated
    public void setUseInstanceData3D() {}

    public MemoryBlock getInstanceDataMemory() {
        return this.memory;
    }

    /**
     * Will always computes once for the same memory range.<p>
     * And if any rendering entity with not-full-range refresh, will only refresh the minimum range for them.
     */
    public void setSharedInstanceData(InstanceRenderAPI renderData) {
        this.sync_lock.lock();
        final MemoryBlock renderDataMemory = renderData.getInstanceDataMemory();
        if (!BoxConfigs.isShaderEnable() || this.memory == renderDataMemory || !renderData.haveValidInstanceData()) {
            this.sync_lock.unlock();
            return;
        }
        if (this.memory != null) InstanceDataMemoryPool.free(this.memory);
        this.memory = InstanceDataMemoryPool.share(renderDataMemory);
        this.sync_lock.unlock();
    }

    /**
     * Unsafe operation.<p>
     * Equivalent to:
     * <pre>
     * {@code
     * MemoryBlock old_mem = this.memory;
     * if (old_mem) free(old_mem);
     * this.memory = new_mem;
     * }
     * </pre>
     */
    public void resetMemory(MemoryBlock memory) {
        this.sync_lock.lock();
        if (this.memory != null) InstanceDataMemoryPool.free(this.memory);
        this.memory = memory;
        this.sync_lock.unlock();
    }

    public void resetMemory() {
        this.resetMemory(null);
    }

    @Deprecated
    public boolean isInstanceDataCustom() {
        return false;
    }

    @Deprecated
    public void setUseInstanceDataCustom() {}

    @Deprecated
    public void setUseDefaultInstanceData() {}

    @Deprecated
    public byte getInstanceDataFormat() {
        return 0;
    }

    @Deprecated
    public boolean isNormalFloatFormatInstanceData() {
        return true;
    }

    @Deprecated
    public void setUseNormalFormatFloatInstanceData() {}

    @Deprecated
    public boolean isHalfFloatFormatInstanceData() {
        return false;
    }

    @Deprecated
    public void setUseHalfFloatFormatInstanceData() {}

    @Deprecated
    public void setUseDefaultFormatInstanceData() {}

    public float getInstanceTimerOverride() {
        return this.instanceTimerOverride;
    }

    /**
     * Override timer of all in-vRAM instance data to this value.
     *
     * @param alpha set less than 0 to disable override.
     * @param state valid state: {@link BoxEnum#TIMER_IN}, {@link BoxEnum#TIMER_FULL}, {@link BoxEnum#TIMER_OUT}.
     */
    public void setInstanceTimerOverride(float alpha, byte state) {
        this.instanceTimerOverride = Math.max(Math.min(alpha, 1.0f), 0.0f);
        if (this.instanceTimerOverride >= 0.0f) {
            if (state == BoxEnum.TIMER_FULL) this.instanceTimerOverride += 1.0f;
            else if (state != BoxEnum.TIMER_IN) this.instanceTimerOverride += 2.0f;
        }
    }

    public void copyInstanceTimerOverride(InstanceRenderAPI renderData) {
        this.instanceTimerOverride = renderData.getInstanceTimerOverride();
    }

    public byte getGlobalTimerState() {
        if (this.haveValidInstanceData() && this.globalTimer[0] > 0.0f) return BoxEnum.TIMER_FULL;
        else if (this.globalTimer[0] > 2.0f) return BoxEnum.TIMER_IN;
        else if (this.globalTimer[0] > 1.0f) return BoxEnum.TIMER_FULL;
        else if (this.globalTimer[0] > 0.0f) return BoxEnum.TIMER_OUT;
        else return this.isGlobalTimerOnce() ? BoxEnum.TIMER_ONCE : BoxEnum.TIMER_INVALID;
    }
}
