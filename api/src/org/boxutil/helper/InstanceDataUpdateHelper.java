package org.boxutil.helper;

import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.*;
import org.boxutil.manager.InstanceDataMemoryPool;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.locks.ReentrantLock;

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
    private boolean _mapMode = true;
    private int putOffset;
    private int _putOffsetReal;
    private  ReentrantLock _lock = null;
    private ByteBuffer _updateBuffer = null;
    private FloatBuffer _mappingBuffer = null;
    private long _memoryAddress = 0;

    /**
     * @return <code>true</code> when success.
     */
    public boolean glInitUpdate(InstanceType type) {
        int ssbo = InstanceDataMemoryPool.getBufferID(type);
        if (ssbo > 0) GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo);
        return ssbo > 0;
    }

    /**
     * @return <code>true</code> when success.
     */
    public boolean glLoadMemory(MemoryBlock memory, boolean mappingMode, boolean syncUpdate) {
        if (memory == null || memory.address() < 0 || memory.size() < 1 || memory.is_free()) return false;
        this._memoryAddress = memory.address();
        this._mapMode = mappingMode;
        if (this._mapMode) {
            this._lock = BUtil_InstanceDataMemoryPool.getGPULock(memory.type());
            final int _access = syncUpdate ? GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT : GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT;
            this._lock.lock();
            this._updateBuffer = GL30.glMapBufferRange(GL43.GL_SHADER_STORAGE_BUFFER, memory.address(), memory.size(), _access, null);
            if (this._updateBuffer == null) {
                GL15.glUnmapBuffer(GL43.GL_SHADER_STORAGE_BUFFER);
                this._lock.unlock();
                return false;
            }
            this._mappingBuffer = this._updateBuffer.asFloatBuffer();
        } else this._mappingBuffer = BufferUtils.createFloatBuffer((int) (memory.size() >> 2));
        this.putOffset = this._putOffsetReal = 0;
        return true;
    }

    public int glGetCurrentPutPosition() {
        return this.putOffset;
    }

    public void glSetCurrentPutPosition(InstanceType type, int instanceIndex) {
        this.putOffset = instanceIndex;
        this._putOffsetReal = instanceIndex * type.getComponent();
    }

    public void glProcessDynamic2D(Dynamic2DStruct data) {
        this._mappingBuffer.put(this._putOffsetReal + InstanceType.DYNAMIC_2D.getCompactOffset(), data.getDataCompact(), 0, InstanceType.DYNAMIC_2D.getCompactComponent());
        this._putOffsetReal += InstanceType.DYNAMIC_2D.getComponent();
    }

    public void glProcessFixed2D(Fixed2DStruct data) {
        this._mappingBuffer.put(this._putOffsetReal, data.getData(), 0, InstanceType.FIXED_2D.getComponent());
        this._putOffsetReal += InstanceType.FIXED_2D.getComponent();
    }

    public void glProcessDynamic3D(Dynamic3DStruct data) {
        this._mappingBuffer.put(this._putOffsetReal + InstanceType.DYNAMIC_3D.getCompactOffset(), data.getDataCompact(), 0, InstanceType.DYNAMIC_3D.getCompactComponent());
        this._putOffsetReal += InstanceType.DYNAMIC_3D.getComponent();
    }

    public void glProcessFixed3D(Fixed3DStruct data) {
        this._mappingBuffer.put(this._putOffsetReal, data.getData(), 0, InstanceType.FIXED_3D.getComponent());
        this._putOffsetReal += InstanceType.FIXED_3D.getComponent();
    }

    public void glSubmitData() {
        if (this._mapMode) {
            this._updateBuffer.position(0);
            this._updateBuffer.limit(this._updateBuffer.capacity());
            GL15.glUnmapBuffer(GL43.GL_SHADER_STORAGE_BUFFER);
            this._lock.unlock();
        } else {
            this._mappingBuffer.position(0);
            this._mappingBuffer.limit(this._mappingBuffer.capacity());
            GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, this._memoryAddress, this._mappingBuffer);
        }
    }

    public void glCleanup() {
        if (this._lock != null) {
            if (this._lock.isLocked()) this._lock.unlock();
            this._lock = null;
        }
        this._updateBuffer = null;
        this._mappingBuffer = null;
        this._mapMode = true;
        this._memoryAddress = this.putOffset = this._putOffsetReal = 0;
        if (BoxDatabase.getGLState().GL_SSBO) GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }
}
