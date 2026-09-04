package org.boxutil.helper;

import org.boxutil.backends.core.instancedrendering.BUtil_InstanceDataMemoryPool;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.*;
import org.boxutil.manager.InstanceDataMemoryPool;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.locks.Lock;

/**
 * For example:
 * <pre>
 * {@code
 * Dynamic2DStruct[] todoData;
 * InstanceDataUpdateHelper helper;
 * MemoryBlock targetMemory; // not null
 *
 * if (helper.glInitUpdate(targetMemory.type()) && helper.glLoadMemory(targetMemory, true, false)) { // ordered
 *     for (Dynamic2DStruct data : todoData) helper.glProcessDynamic2D(data);
 *     helper.glSubmitData();
 * }
 * helper.glCleanup();
 * }
 * </pre>
 */
public class InstanceDataUpdateHelper {
    private boolean persistentMapping = false;
    private boolean mapMode = true;
    private boolean shouldUnlockAfter = true;
    private int putOffset;
    private int putOffsetReal;
    private Lock lock = null;
    private ByteBuffer updateBuffer = null;
    private FloatBuffer mappingBuffer = null;
    private long realMemAddress = 0;

    /**
     * @return <code>true</code> when success.
     */
    public boolean glInitUpdate(InstanceType type) {
        int ssbo = InstanceDataMemoryPool.getBufferID(type);
        if (ssbo > 0) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER, ssbo);
        return ssbo > 0;
    }

    /**
     * @return <code>true</code> when success.
     */
    public boolean glLoadMemory(MemoryBlock memory, boolean mappingMode, boolean syncUpdate) {
        if (memory == null || memory.address() < 0 || memory.size() < 1 || memory.is_free()) return false;
        final var pool = BUtil_InstanceDataMemoryPool.getPool(memory.meta());

        this.mapMode = mappingMode;
        this.persistentMapping = pool.isPersistentMapping() && pool.getMappingBuffer() != null;
        this.putOffset = 0;
        this.putOffsetReal = pool.getPoolBehavior().reservedSize;
        this.realMemAddress = memory.address() + this.putOffsetReal;
        this.lock = pool.getGPULock();

        if (this.persistentMapping) {
            this.lock.lock();
            this.shouldUnlockAfter = true;
            this.updateBuffer = pool.getMappingBuffer();
            return true;
        }

        if (this.mapMode) {
            final int _access = syncUpdate ? GLWrapper.Buffer.GL_MAP_WRITE_BIT | GLWrapper.Buffer.GL_MAP_INVALIDATE_RANGE_BIT : GLWrapper.Buffer.GL_MAP_WRITE_BIT | GLWrapper.Buffer.GL_MAP_UNSYNCHRONIZED_BIT | GLWrapper.Buffer.GL_MAP_INVALIDATE_RANGE_BIT;
            this.lock.lock();
            this.shouldUnlockAfter = true;
            this.updateBuffer = GLWrapper.Buffer.glMapBufferRange(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER, this.realMemAddress, memory.size(), _access, null);
            if (this.updateBuffer == null) {
                this.shouldUnlockAfter = false;
                GLWrapper.Buffer.glUnmapBuffer(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER);
                this.lock.unlock();
                return false;
            }
            this.mappingBuffer = this.updateBuffer.asFloatBuffer();
        } else this.mappingBuffer = BufferUtils.createFloatBuffer((int) (this.realMemAddress >> 2));
        return true;
    }

    public int glGetCurrentPutPosition() {
        return this.putOffset;
    }

    public void glSetCurrentPutPosition(InstanceType type, int instanceIndex) {
        this.putOffset = instanceIndex;
        this.putOffsetReal = instanceIndex * type.getComponent();
    }

    public void glProcessDynamic2D(Dynamic2DStruct data) {
        this.mappingBuffer.put(this.putOffsetReal + InstanceType.DYNAMIC_2D.getCompactOffset(), data.getDataCompact(), 0, InstanceType.DYNAMIC_2D.getCompactComponent());
        this.putOffsetReal += InstanceType.DYNAMIC_2D.getComponent();
    }

    public void glProcessFixed2D(Fixed2DStruct data) {
        this.mappingBuffer.put(this.putOffsetReal, data.getData(), 0, InstanceType.FIXED_2D.getComponent());
        this.putOffsetReal += InstanceType.FIXED_2D.getComponent();
    }

    public void glProcessDynamic3D(Dynamic3DStruct data) {
        this.mappingBuffer.put(this.putOffsetReal + InstanceType.DYNAMIC_3D.getCompactOffset(), data.getDataCompact(), 0, InstanceType.DYNAMIC_3D.getCompactComponent());
        this.putOffsetReal += InstanceType.DYNAMIC_3D.getComponent();
    }

    public void glProcessFixed3D(Fixed3DStruct data) {
        this.mappingBuffer.put(this.putOffsetReal, data.getData(), 0, InstanceType.FIXED_3D.getComponent());
        this.putOffsetReal += InstanceType.FIXED_3D.getComponent();
    }

    public void glSubmitData() {
        if (this.persistentMapping) return;

        if (this.mapMode) {
            this.updateBuffer.position(0);
            this.updateBuffer.limit(this.updateBuffer.capacity());
            GLWrapper.Buffer.glUnmapBuffer(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER);
            this.lock.unlock();
        } else {
            this.mappingBuffer.position(0);
            this.mappingBuffer.limit(this.mappingBuffer.capacity());
            GLWrapper.Buffer.glBufferSubData(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER, this.realMemAddress, this.mappingBuffer);
        }
    }

    public void glCleanup() {
        if (this.lock != null) {
            if (this.shouldUnlockAfter) {
                this.shouldUnlockAfter = false;
                this.lock.unlock();
            }
            this.lock = null;
        }
        this.updateBuffer = null;
        this.mappingBuffer = null;
        this.mapMode = true;
        this.realMemAddress = this.putOffset = this.putOffsetReal = 0;
        if (BoxDatabase.getGLState().GL_SSBO) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER, 0);
    }
}
