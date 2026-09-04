package org.boxutil.units.standard;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.struct.memorypool.GPUMemory;
import org.boxutil.define.struct.memorypool.GPUPoolBehavior;
import org.boxutil.util.concurrent.SpinLock;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.OpenGLException;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A memory pool for managing OpenGL Buffer Objects, supporting multi‑threaded calls via two different granularities of locks.<p>
 * By default, to facilitate shader read/write access at given positions and to improve rendering performance,
 * memory blocks are always allocated in a compact layout from free space, without padding for misaligned byte offsets,
 * during defragmentation, larger blocks are moved to higher addresses.<p>
 * <b>REQUIRES:</b><p>
 * Only <strong>OpenGL 1.5</strong> as the minimum supported version.<p>
 * For optimal performance, it is recommended that the implementation requires at least <strong>OpenGL 3.1</strong> or the <code>ARB_copy_buffer</code> extension.<p>
 * <b>CAUTION:</b><p>
 * The thread to which this memory pool belongs <b>MUST</b> have an active OpenGL context, and that context <b>MUST</b> be shared with the main thread.<p>
 * Any exceptions thrown by some methods of this pool <b>MUST</b> not be caught and ignored or left unhandled; otherwise, fatal error may occur, so such defects <b>MUST</b> be resolved during modding.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GPUMemoryPool<T extends GPUMemoryPool.InternalMemory<D>, D> {

    protected boolean immutableBuffer = false;
    protected boolean persistentMapping = false;
    protected boolean init = false;
    protected boolean gpuInit = false;
    protected boolean invalid = true;
    protected boolean requireCompact = false;
    protected int glID = 0;

    protected long memSpace = 0;
    protected long memRightEdge = 0;
    protected long memTotal = 0;
    protected long lastCompactTime = 0;
    protected final GPUPoolBehavior<T, D> behavior;
    protected List<T> mem = null;
    protected List<T> memFree = null;
    protected final AtomicInteger memRef = new AtomicInteger(0);
    protected Lock clientLock = new SpinLock();
    protected ReadWriteLock gpuLock = new ReentrantReadWriteLock();
    protected ByteBuffer clientMapping = null;

    public GPUMemoryPool(final GPUPoolBehavior<T, D> behavior) {
        this.behavior = behavior;
        if (this.behavior.glTarget == 0) throw new IllegalArgumentException("Invalid GPU memory pool target.");
        if (this.behavior.reservedSize < 0) throw new IllegalArgumentException("Invalid GPU memory pool reversed size: less than zero.");
        if (this.behavior.defaultBufferSize < 0) throw new IllegalArgumentException("Invalid GPU memory pool default buffer size: less than zero.");
        if (this.behavior.maxBufferSize < 0) throw new IllegalArgumentException("Invalid GPU memory pool maximum buffer size: less than zero.");
    }

    @FunctionalInterface
    public interface MemoryBuilder<T extends GPUMemoryPool.InternalMemory<D>, D> {
        T make(D oldMeta, long address, long size, int index, GPUMemoryPool<T, D> pool);
    }

    /**
     * Do not forget override the {@link GPUMemory#meta()} if you need.
     */
    public static class InternalMemory<D> implements GPUMemory<D> {
        protected int _index;
        protected long address;
        protected long size;
        protected final AtomicInteger ref;

        public InternalMemory(final D meta, long address, long size, int index, boolean isFree) {
            if (address < 0 || size < 1) throw new IllegalArgumentException("Illegal memory range: address = '" + address + "' size = '" + size + '\'');
            this._index = index;
            this.ref = new AtomicInteger(isFree ? 0 : 1);
            this.address = address;
            this.size = size;
        }

        public int reference() {
            return this.ref.get();
        }

        public long address() {
            return this.address;
        }

        public long size() {
            return this.size;
        }

        public int _getIndex() {
            return this._index;
        }

        public void _setIndex(int value) {
            this._index = value;
        }

        public void _indexIncrement() {
            ++_index;
        }

        public void _indexDecrement() {
            --_index;
        }

        public void _indexAdd(int value) {
            _index += value;
        }

        public void _indexSub(int value) {
            _index -= value;
        }

        public void _setAddress(long value) {
            this.address = value;
        }

        public void _setSize(long value) {
            this.size = value;
        }

        public void _setRef(int value) {
            this.ref.set(value);
        }

        public void _refIncrement() {
            this.ref.getAndIncrement();
        }

        public void _refDecrement() {
            this.ref.getAndDecrement();
        }

        public void afterAddressChanged() {}

        public void afterSizeChanged() {}

        public void afterAllocatedFromFree(final D meta) {}

        public void afterSplitFrom(final InternalMemory<D> src) {}

        public void afterFree() {}
    }

    /**
     * Initializes the environment; the actual vRAM for the buffer object is formally allocated only on the first {@linkplain GPUMemoryPool#malloc(Object, long) malloc} call.
     */
    public void init() {
        this.getClientLock().lock();
        this.poolGPULock().lock();
        if (this.init) {
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return;
        }
        this.init = true;

        final var req = this.behavior.glContextRequirements;
        if (!GLWrapper.Buffer.valid() || (req != null && !req.apply(this))) {
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return;
        }
        this.immutableBuffer = GLWrapper.Buffer.valid_MapRange() && GLWrapper.Buffer.valid_BufferStorage() && this.behavior.immutableBufferAllow;
        this.persistentMapping = this.immutableBuffer && (this.behavior.glBufferAccessBits & GLWrapper.Buffer.GL_MAP_PERSISTENT_BIT) > 0;
        this.invalid = false;
        if (this.behavior.glPoolInit != null) this.behavior.glPoolInit.accept(this);
        this.poolGPULock().unlock();
        this.getClientLock().unlock();
    }

    public void destroy() {
        this.getClientLock().lock();
        this.poolGPULock().lock();
        this._cleanupClientMem();
        this._invalidateBuffer();
        if (this.behavior.glPoolDestroy != null) this.behavior.glPoolDestroy.accept(this);
        this.init = false;
        this.invalid = true;
        this.poolGPULock().unlock();
        this.getClientLock().unlock();
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    public boolean isImmutableBuffer() {
        return this.immutableBuffer;
    }

    public boolean isPersistentMapping() {
        return this.persistentMapping;
    }

    /**
     * @return the buffer object name, <code>0</code> if never have call {@linkplain GPUMemoryPool#malloc(Object, long) malloc} with target, or pool was unsupported.<p>
     *     Without mutex.
     */
    public int getBufferID() {
        return this.glID;
    }

    public GPUPoolBehavior<T, D> getPoolBehavior() {
        return this.behavior;
    }

    public long getBufferSpace() {
        return this.memSpace;
    }

    public long getBufferRightEdge() {
        return this.memRightEdge;
    }

    public long getBufferTotal() {
        return this.memTotal;
    }

    public int getBufferReference() {
        return this.memRef.get();
    }

    public Lock getClientLock() {
        return this.clientLock;
    }

    /**
     * For read or write on pool's OpenGL buffer object.
     */
    public Lock getGPULock() {
        return this.gpuLock.readLock();
    }

    protected Lock poolGPULock() {
        return this.gpuLock.writeLock();
    }

    public boolean isPoolRequireCompact() {
        return this.requireCompact;
    }

    public long getLastCompactTimestampNano() {
        return this.lastCompactTime;
    }

    /**
     * @return only valid when {@link GPUMemoryPool#isPersistentMapping()} returns <code>true</code>, included reserved space if it has.
     */
    public @Nullable ByteBuffer getMappingBuffer() {
        return this.clientMapping;
    }

    protected void _runtimeBufferIDCheck() {
        if (this.glID < 1) throw new OpenGLException("Fatal: cannot generate valid new buffer object on current thread '" + Thread.currentThread().getName() + '\'');
    }

    protected T _makeMemory(final MemoryBuilder<T, D> builder, final D meta, long address, long size, int index) {
        final T result = builder.make(meta, address, size, index, this);
        result.afterAddressChanged();
        result.afterSizeChanged();
        return result;
    }

    protected void _invalidateBuffer() {
        if (this.glID > 0) {
            if (this.persistentMapping && this.clientMapping != null) {
                GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
                GLWrapper.Buffer.glUnmapBuffer(this.behavior.glTarget);
                GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
                this.clientMapping = null;
            }
            if (GLWrapper.Buffer.valid_Invalidate()) GLWrapper.Buffer.glInvalidateBufferData(this.glID);
            GLWrapper.Buffer.glDeleteBuffers(this.glID);
            this.glID = 0;
            this.gpuInit = false;
        }
    }

    protected void _eraseMemory(final D meta) {
        final T freeBlock = this._makeMemory(this.behavior.newEmptyMemory, meta, 0, this.memTotal, 0);
        if (this.mem == null) this.mem = new ArrayList<>(); else this.mem.clear();
        if (this.memFree == null) this.memFree = new ArrayList<>(); else this.memFree.clear();
        this.mem.add(freeBlock);
        this.memFree.add(freeBlock);
        this.memRightEdge = 0;
        this.memSpace = this.memTotal;
        this.memRef.set(0);
    }

    protected void _allocateBuffer(long size) {
        final long realSize = this.behavior.reservedSize + size;
        if (this.immutableBuffer) {
            final int access_mapping = this.behavior.glBufferAccessBits,
                    access_buffer = this.behavior.useDynamicStorageBuffer ? GLWrapper.Buffer.GL_DYNAMIC_STORAGE_BIT | access_mapping : access_mapping;
            GLWrapper.Buffer.glBufferStorage(this.behavior.glTarget, realSize, access_buffer);
            if (this.persistentMapping) {
                this.clientMapping = GLWrapper.Buffer.glMapBufferRange(this.behavior.glTarget, 0, realSize, access_mapping, null);
                this.clientMapping.clear();
            }
        } else GLWrapper.Buffer.glBufferData(this.behavior.glTarget, realSize, GLWrapper.Buffer.GL_DYNAMIC_DRAW);
    }

    protected void _initBuffer(final D meta, long size) {
        this.poolGPULock().lock();
        this._invalidateBuffer();
        this.memTotal = this.behavior.bufferInitRule.apply(size, this);

        this.glID = GLWrapper.Buffer.glGenBuffers();
        this._runtimeBufferIDCheck();
        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
        this._allocateBuffer(this.memTotal);
        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
        this.memRef.set(0);

        if (this.behavior.glRebindBuffer != null) this.behavior.glRebindBuffer.accept(this);
        this.poolGPULock().unlock();

        this._eraseMemory(meta);
        this.lastCompactTime = System.nanoTime();
    }

    protected void _cleanupClientMem() {
        if (this.mem != null) {
            for (T block : this.mem) block._setRef(0);
            this.mem.clear();
            this.mem = null;
        }
        if (this.memFree != null) {
            this.memFree.clear();
            this.memFree = null;
        }
        this.requireCompact = false;
        this.memSpace = this.memRightEdge = this.memTotal = 0L;
        this.memRef.set(0);
    }

    protected void _copyRangeBufferOverlap(long srcAddress, long dstAddress, long size) {
        this.poolGPULock().lock();
        final long realSrcAddress = srcAddress + this.behavior.reservedSize, realDstAddress = dstAddress + this.behavior.reservedSize;
        if (GLWrapper.Buffer.valid_CopyBuffer()) {
            final int tmpBuffer = GLWrapper.Buffer.glGenBuffers();
            this._runtimeBufferIDCheck();
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, tmpBuffer);
            if (this.immutableBuffer) GLWrapper.Buffer.glBufferStorage(this.behavior.glTarget, size, 0);
            else GLWrapper.Buffer.glBufferData(this.behavior.glTarget, size, GLWrapper.Buffer.GL_DYNAMIC_DRAW);

            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.glID);
            GLWrapper.Buffer.glCopyBufferSubData(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.behavior.glTarget, realSrcAddress, 0, size);

            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, tmpBuffer);
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
            GLWrapper.Buffer.glCopyBufferSubData(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.behavior.glTarget, 0, realDstAddress, size);

            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, 0);
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
            if (GLWrapper.Buffer.valid_Invalidate()) GLWrapper.Buffer.glInvalidateBufferData(tmpBuffer);
            GLWrapper.Buffer.glDeleteBuffers(tmpBuffer);
        } else {
            final ByteBuffer legacyCopyBuf = BufferUtils.createByteBuffer((int) size).clear();
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
            GLWrapper.Buffer.glGetBufferSubData(this.behavior.glTarget, realSrcAddress, legacyCopyBuf);

            GLWrapper.Buffer.glBufferSubData(this.behavior.glTarget, realDstAddress, legacyCopyBuf);
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
        }
        this.poolGPULock().unlock();
    }

    protected void _expandBuffer(long reqSize) {
        if (this.memTotal < 1) throw new OpenGLException("Failed to expand SSBO: not initialization yet.");

        this.poolGPULock().lock();
        final long oldSize = this.memTotal;
        do {
            this.memTotal = this.behavior.bufferExpendRule.apply(this.memTotal, this);
        } while (this.memTotal - this.memRightEdge <= reqSize);


        if (this.behavior.maxBufferSize > 0 && this.memTotal > this.behavior.maxBufferSize) throw new OutOfMemoryError("Failed to allocate buffer object with over " + this.behavior.maxBufferSize + " Byte.");
        if (this.memTotal < 1) throw new OutOfMemoryError("Failed to allocate buffer object with over 8_388_608 TiB.");
        final long diff = this.memTotal - oldSize;
        this.memSpace += diff;

        T block = this.mem.get(this.mem.size() - 1);
        if (block.is_free()) {
            block._setSize(block.size() + diff);
            block.afterSizeChanged();
        } else {
            block = this._makeMemory(this.behavior.newEmptyMemory, block.meta(), block.address() + block.size(), diff, block._getIndex() + 1);
            this.mem.add(block);
            this.memFree.add(block);
        }

        if (this.persistentMapping && this.clientMapping != null) {
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
            GLWrapper.Buffer.glUnmapBuffer(this.behavior.glTarget);
            this.clientMapping = null;
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
        }

        final int newBuffer = GLWrapper.Buffer.glGenBuffers();
        this._runtimeBufferIDCheck();
        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, newBuffer);
        this._allocateBuffer(this.memTotal);

        final long realMoveMem = this.memRightEdge + this.behavior.reservedSize;
        if (GLWrapper.Buffer.valid_CopyBuffer()) {
            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.glID);
            GLWrapper.Buffer.glCopyBufferSubData(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.behavior.glTarget, 0, 0, realMoveMem);
            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, 0);
        } else {
            final ByteBuffer legacyCopyBuf = BufferUtils.createByteBuffer((int) realMoveMem).clear();
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
            GLWrapper.Buffer.glGetBufferSubData(this.behavior.glTarget, 0, legacyCopyBuf);

            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, newBuffer);
            GLWrapper.Buffer.glBufferSubData(this.behavior.glTarget, 0, legacyCopyBuf);
        }

        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
        if (this.glID > 0) {
            if (GLWrapper.Buffer.valid_Invalidate()) GLWrapper.Buffer.glInvalidateBufferData(this.glID);
            GLWrapper.Buffer.glDeleteBuffers(this.glID);
        }
        this.glID = newBuffer;

        if (this.behavior.glRebindBuffer != null) this.behavior.glRebindBuffer.accept(this);
        this.poolGPULock().unlock();
    }

    protected T _scanEndsFreeBlock(long foundSize) {
        T result = null;
        final int memArraySize = this.memFree.size();

        final int forwardLimit = Math.min(memArraySize, 5);
        for (int i = 0; i < forwardLimit; ++i) {
            result = this.memFree.get(i);
            if (result.size() < foundSize) {
                result = null;
            } else break;
        }
        if (result != null || memArraySize < 6) return result;

        final int forwardStart = Math.max(memArraySize - 5, 5);
        for (int i = forwardStart; i < memArraySize; ++i) {
            result = this.memFree.get(i);
            if (result.size() < foundSize) {
                result = null;
            } else break;
        }
        if (result != null || memArraySize < 11) return result;

        for (int i = forwardLimit; i < forwardStart; ++i) { // scan all between ends
            result = this.memFree.get(i);
            if (result.size() < foundSize) {
                result = null;
            } else break;
        }
        return result;
    }

    protected void _mergeFreeBlock(T memory) {
        this.memSpace += memory.size();
        int memIndex = memory._getIndex(), memArraySize = this.mem.size();
        int checkIndex;
        boolean mergePre = false, mergeNext = false;
        T check;

        checkIndex = memIndex - 1;
        if (checkIndex > -1) {
            check = this.mem.get(checkIndex);
            mergePre = check.is_free();
            if (mergePre) {
                check._setSize(check.size() + memory.size());
                check.afterSizeChanged();
                this.mem.remove(memory);
                memory = check;
                memIndex = checkIndex;
                --memArraySize;
            }
        }
        checkIndex = memIndex + 1;
        if (checkIndex < memArraySize) {
            check = this.mem.get(checkIndex);
            mergeNext = check.is_free();
            if (mergeNext) {
                memory._setSize(memory.size() + check.size());
                memory.afterSizeChanged();
                this.mem.remove(check);
                this.memFree.remove(check);
                --memArraySize;
            }
        }

        if (mergePre || mergeNext) {
            final byte offset = (byte) (mergePre && mergeNext ? 2 : 1);
            for (int i = memIndex + 1; i < memArraySize; ++i) this.mem.get(i)._indexSub(offset);
        }

        if (!mergePre) {
            this.memFree.add(memory);
            this.memFree.sort(Comparator.comparingLong(InternalMemory::address));
        }

        check = this.mem.get(memArraySize - 1);
        if (check.is_free()) this.memRightEdge = check.address();
    }

    protected boolean _sizeOverRange(long size) {
        return this.behavior.maxBufferSize > 0 && this.memRightEdge + size > this.behavior.maxBufferSize;
    }

    /**
     * @return <code>null</code> when allocation failed.
     */
    public T malloc(@Nullable final D meta, long size) {
        if (this.invalid) return null;
        if (size == 0) throw new IllegalArgumentException("Undefined behavior, cannot call malloc() with '0' size");
        if (size < 1) throw new IllegalArgumentException("Unable to allocate memory block with size: '" + size + '\'');

        this.getClientLock().lock();
        if (!this.gpuInit) {
            this._initBuffer(meta, size);
            this.gpuInit = true;
        }

        if (this._sizeOverRange(size)) {
            this.getClientLock().unlock();
            return null;
        }
        T result, freeBlock = this._scanEndsFreeBlock(size);

        boolean allocateAtEnd = freeBlock == null;
        int memIndexLimit;
        if (allocateAtEnd) {
            this._expandBuffer(size); // keep last free block, resize the space
            memIndexLimit = this.mem.size() - 1;
            freeBlock = this.mem.get(memIndexLimit);
            if (freeBlock.size() < size) {
                this.getClientLock().unlock();
                return null;
            }
        } else memIndexLimit = this.mem.size() - 1;

        this.memSpace -= size;
        allocateAtEnd = freeBlock._getIndex() == memIndexLimit;
        if (freeBlock.size() == size) {
            freeBlock._setRef(1);
            freeBlock.afterAllocatedFromFree(meta);
            result = freeBlock;
            this.memFree.remove(freeBlock);
        } else {
            result = this._makeMemory(this.behavior.newNotEmptyMemory, meta, freeBlock.address(), size, freeBlock._getIndex());
            freeBlock._setAddress(freeBlock.address() + size);
            freeBlock._setSize(freeBlock.size() - size);
            freeBlock.afterAddressChanged();
            freeBlock.afterSizeChanged();
            this.mem.add(freeBlock._getIndex(), result);
            memIndexLimit = this.mem.size();
            for (int i = result._getIndex() + 1; i < memIndexLimit; ++i) this.mem.get(i)._indexIncrement();
        }
        if (allocateAtEnd) this.memRightEdge += size;

        this.requireCompact = true;
        this.memRef.incrementAndGet();
        this.getClientLock().unlock();
        return result;
    }

    /**
     * Bad for performance, so recommend to allocation the large enough memory block before all.
     *
     * @param memory different from C/C++, will not change the object pointer, only remapping and then copy data if needed.
     *
     * @return <code>false</code> when re-allocation failed.
     */
    public boolean realloc(@NotNull final T memory, long newSize) {
        if (this.invalid) return false;
        if (newSize == 0) throw new IllegalCallerException("Undefined behavior, cannot call realloc() with '0' size, should use free()");
        if (newSize < 1) throw new IllegalArgumentException("Unable to call realloc() with size: '" + newSize + '\'');
        this.getClientLock().lock();
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block: realloc() for free block.");
        if (memory.size() == newSize) {
            this.getClientLock().unlock();
            return true;
        }

        final int indexI = memory._getIndex();
        T nextBlock = null;
        final long diff = newSize - memory.size();
        if (this._sizeOverRange(newSize)) {
            this.getClientLock().unlock();
            return false;
        }
        int memArraySize = this.mem.size(), indexMax = memArraySize - 1, nextIndex = indexI + 1;
        boolean nextBlockAtEnd = false;
        if (indexI < indexMax) { // found next
            nextBlockAtEnd = nextIndex == indexMax;
            nextBlock = this.mem.get(nextIndex);
        }

        if (memory.size() > newSize) { // contracting
            boolean haveNextBlock = nextBlock != null;
            memory._setSize(newSize);
            memory.afterSizeChanged();
            this.memSpace -= diff;

            if (haveNextBlock && nextBlock.is_free()) {
                nextBlock._setAddress(nextBlock.address() + diff);
                nextBlock._setSize(nextBlock.size() - diff);
                nextBlock.afterAddressChanged();
                nextBlock.afterSizeChanged();
                this.memRightEdge += diff;

                this.requireCompact = true;
                this.getClientLock().unlock();
                return true;
            }

            nextBlock = this._makeMemory(this.behavior.newEmptyMemory, memory.meta(), memory.address() + memory.size(), -diff, indexI);
            this.memFree.add(nextBlock);
            if (haveNextBlock) {
                this.mem.add(nextIndex, nextBlock);
                ++memArraySize;
                for (int i = nextIndex + 1; i < memArraySize; ++i) this.mem.get(i)._indexIncrement();
            } else {
                this.memRightEdge += diff;
                this.mem.add(nextBlock);
            }

            this.requireCompact = true;
            this.getClientLock().unlock();
            return true;
        } // else expanding:

        T preBlock = null, globalFreeBlock;
        final long oldAddress = memory.address(), oldSize = memory.size();
        boolean nextBlockFree = false, freeBlockNotEnough, callExpand;
        if (nextBlock != null && nextBlock.is_free()) {
            nextBlockFree = true;
            freeBlockNotEnough = nextBlock.size() < diff;

            if (!freeBlockNotEnough) {
                if (nextBlock.size() == diff) {
                    this.mem.remove(nextBlock);
                    this.memFree.remove(nextBlock);
                    --memArraySize;
                    for (int i = nextIndex; i < memArraySize; ++i) this.mem.get(i)._indexDecrement();
                } else {
                    nextBlock._setAddress(nextBlock.address() + diff);
                    nextBlock._setSize(nextBlock.size() - diff);
                    nextBlock.afterAddressChanged();
                    nextBlock.afterSizeChanged();
                }
                memory._setSize(newSize);
                memory.afterSizeChanged();
                this.memSpace -= diff;
                if (nextBlockAtEnd) this.memRightEdge += diff;

                this.requireCompact = true;
                this.getClientLock().unlock();
                return true;
            }
        }

        // found pre
        final int preIndex = indexI - 1;
        if (indexI > 0) preBlock = this.mem.get(preIndex);
        if (preBlock != null && preBlock.is_free()) {
            long _freeSpace = nextBlockFree ? preBlock.size() + nextBlock.size() : preBlock.size();
            freeBlockNotEnough = _freeSpace < diff;

            if (!freeBlockNotEnough) {
                byte _subValue = 0;
                if (_freeSpace == diff) {
                    this.mem.remove(preBlock);
                    this.memFree.remove(preBlock);
                    if (nextBlockFree) ++_subValue;
                    --nextIndex; // lost pre block
                } else {
                    Collections.swap(this.mem, preIndex, indexI);
                    preBlock._indexIncrement();
                }
                if (nextBlockFree) {
                    this.mem.remove(nextBlock);
                    this.memFree.remove(nextBlock);
                    ++_subValue;
                }

                if (_subValue > 0) {
                    memArraySize -= _subValue;
                    for (int i = nextIndex; i < memArraySize; ++i) this.mem.get(i)._indexSub(_subValue);
                }

                this._copyRangeBufferOverlap(oldAddress, preBlock.address(), oldSize);
                memory._setAddress(preBlock.address());
                memory._setSize(newSize);
                memory.afterAddressChanged();
                memory.afterSizeChanged();
                memory._indexDecrement();
                preBlock._setAddress(memory.address() + memory.size());
                preBlock._setSize(diff);
                preBlock.afterAddressChanged();
                preBlock.afterSizeChanged();
                this.memSpace -= diff;
                if (nextBlockAtEnd) this.memRightEdge += diff;

                this.requireCompact = true;
                this.getClientLock().unlock();
                return true;
            }
        }

        // found ends
        globalFreeBlock = this._scanEndsFreeBlock(newSize);
        callExpand = globalFreeBlock == null || globalFreeBlock.size() < newSize;

        if (callExpand) {
            this._expandBuffer(newSize); // expand at end
            memArraySize = this.mem.size();
            indexMax = memArraySize - 1;
            globalFreeBlock = this.mem.get(indexMax);
            if (globalFreeBlock.size() < newSize) {
                this.getClientLock().unlock();
                return false;
            }
        }

        final boolean freeBlockAtEnd = globalFreeBlock._getIndex() == indexMax, notAllocateFromExpand = globalFreeBlock._getIndex() - 1 != indexI, removeFreeBlock = globalFreeBlock.size() == diff;
        if (notAllocateFromExpand) {
            nextBlock = this._makeMemory(this.behavior.newEmptyMemory, memory.meta(), memory.address(), memory.size(), indexI);
            this.mem.set(indexI, nextBlock);
            this.memFree.add(nextBlock);

            this.mem.add(globalFreeBlock._getIndex(), memory);
            memory._setIndex(globalFreeBlock._getIndex());
            memory._setAddress(globalFreeBlock.address());
            ++memArraySize;
            if (!removeFreeBlock) for (int i = globalFreeBlock._getIndex() + 1; i < memArraySize; ++i) this.mem.get(i)._indexIncrement();

            // assert not overlap
            this.poolGPULock().lock();
            final long realReadAddress = oldAddress + this.behavior.reservedSize,
                    realWriteAddress = memory.address() + this.behavior.reservedSize;
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
            if (GLWrapper.Buffer.valid_CopyBuffer()) {
                GLWrapper.Buffer.glCopyBufferSubData(this.behavior.glTarget, this.behavior.glTarget, realReadAddress, realWriteAddress, oldSize);
            } else {
                final ByteBuffer legacyCopyBuf = BufferUtils.createByteBuffer((int) oldSize).clear();
                GLWrapper.Buffer.glGetBufferSubData(this.behavior.glTarget, realReadAddress, legacyCopyBuf);

                GLWrapper.Buffer.glBufferSubData(this.behavior.glTarget, realWriteAddress, legacyCopyBuf);
            }
            GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);
            this.poolGPULock().unlock();
        }
        memory._setSize(newSize);
        memory.afterAddressChanged();
        memory.afterSizeChanged();

        if (removeFreeBlock) {
            this.mem.remove(globalFreeBlock);
            this.memFree.remove(globalFreeBlock);
        } else {
            globalFreeBlock._setAddress(globalFreeBlock.address() + diff);
            globalFreeBlock._setSize(globalFreeBlock.size() - diff);
            globalFreeBlock.afterAddressChanged();
            globalFreeBlock.afterSizeChanged();
        }

        this.memSpace -= diff;
        if (freeBlockAtEnd) this.memRightEdge = memory.address() + memory.size();
        this.requireCompact = true;
        this.getClientLock().unlock();
        return true;
    }

    /**
     * @param newSize cannot be equal to the size of the old memory.
     */
    public T split(@NotNull final T memory, long newSize, boolean fromStartOrEnd) {
        if (this.invalid) return null;
        if (newSize == 0) return null;
        if (newSize < 0) throw new IllegalArgumentException("Unable to call split() for memory block with size: '" + newSize + '\'');

        this.getClientLock().lock();
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block: split() for free block.");
        if (memory.size() == newSize) throw new IllegalArgumentException("Undefined behavior, cannot split memory block as same size.");

        final long oriBlockSize = memory.size() - newSize;
        final T result = this._makeMemory(this.behavior.newNotEmptyMemory, memory.meta(), memory.address(), newSize, memory._getIndex());
        memory._setSize(oriBlockSize);
        memory.afterSizeChanged();

        final int index = memory._getIndex(), memArraySize = this.mem.size();
        final boolean noAtEnd = index != memArraySize - 1;
        if (fromStartOrEnd) {
            memory._setAddress(memory.address() + newSize);
            memory.afterAddressChanged();
            this.mem.add(index, result);
        } else {
            result._setAddress(result.address() + oriBlockSize);
            result.afterAddressChanged();
            if (noAtEnd) this.mem.add(index + 1, result); else this.mem.add(result);
        }
        result.afterSplitFrom(memory);
        for (int i = index + 1; i < memArraySize; ++i) this.mem.get(i)._indexIncrement();

        this.requireCompact = true;
        this.memRef.incrementAndGet();
        this.getClientLock().unlock();
        return result;
    }

    /**
     * Even if memory was free, the operation is safe.
     *
     * @return <code>false</code> when still have reference after call.
     */
    public boolean free(@NotNull final T memory) {
        if (this.invalid) return false;
        boolean result = true;
        this.getClientLock().lock();
        if (memory.reference() > 0) {
            if (this.memRef.get() < 1) throw new IllegalStateException("Failed to free memory: SSBO was free but memory block still have reference.");
            memory._refDecrement();
            final boolean cleanup = this.memRef.getAndDecrement() < 1;
            result = memory.is_free();
            if (result) {
                this._mergeFreeBlock(memory);
                memory.afterFree();
                this.requireCompact = !cleanup;
                if (cleanup) this._eraseMemory(memory.meta());
            }
        }
        this.getClientLock().unlock();
        return result;
    }

    public T share(@NotNull final T memory) {
        if (this.invalid) return null;
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block.");
        this.getClientLock().lock();
        memory._refIncrement();
        this.memRef.getAndIncrement();
        this.getClientLock().unlock();
        return memory;
    }

    /**
     * @return times elapsed during defragmentation in nanosecond.
     */
    public long compact() {
        if (this.invalid) return 0;
        this.getClientLock().lock();
        this.poolGPULock().lock();
        this.lastCompactTime = System.nanoTime();

        if (this.glID < 1 || this.memSpace == this.memTotal || this.memTotal < 1) {
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return 0;
        }
        if (!this.requireCompact) {
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return 0;
        }
        if (this.memRef.get() < 1 || this.mem == null) {
            this._cleanupClientMem();
            this._invalidateBuffer();
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return 0;
        }
        this.requireCompact = false;

        final boolean modernCopyBuf = GLWrapper.Buffer.valid_CopyBuffer();
        final int newBuffer = GLWrapper.Buffer.glGenBuffers();
        this._runtimeBufferIDCheck();
        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, newBuffer);
        this._allocateBuffer(this.memTotal);

        Collections.sort(this.mem);
        if (modernCopyBuf) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.glID);

        int index = 0;
        long currOffset = 0L, realReadAddress, realWriteAddress;
        T block, firstBlock = this.memFree.get(0);
        final ByteBuffer legacyCopyBuf = modernCopyBuf ? null : BufferUtils.createByteBuffer((int) this.memTotal);
        for (Iterator<T> iterator = this.mem.iterator(); iterator.hasNext();) {
            block = iterator.next();
            if (block == null || block.is_free() || block.address() < 0 || block.size() < 1) {
                iterator.remove();
                continue;
            }
            if (firstBlock == null) firstBlock = block;

            realReadAddress = block.address() + this.behavior.reservedSize;
            realWriteAddress = currOffset + this.behavior.reservedSize;
            if (modernCopyBuf) {
                GLWrapper.Buffer.glCopyBufferSubData(GLWrapper.Buffer.GL_COPY_READ_BUFFER, this.behavior.glTarget, realReadAddress, realWriteAddress, block.size());
            } else {
                legacyCopyBuf.position(0).limit((int) block.size());
                GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, this.glID);
                GLWrapper.Buffer.glGetBufferSubData(this.behavior.glTarget, realReadAddress, legacyCopyBuf);

                GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, newBuffer);
                GLWrapper.Buffer.glBufferSubData(this.behavior.glTarget, realWriteAddress, legacyCopyBuf);
            }

            block._setAddress(currOffset);
            block.afterAddressChanged();
            currOffset += block.size();
            block._setIndex(index);
            ++index;
        }
        this.memSpace = this.memTotal - currOffset;
        this.memRightEdge = currOffset;
        this.memFree.clear();
        if (this.memSpace > 0) {
            block = this._makeMemory(this.behavior.newEmptyMemory, firstBlock.meta(), currOffset, this.memSpace, index);
            this.mem.add(block);
            this.memFree.add(block);
        }

        if (modernCopyBuf) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.GL_COPY_READ_BUFFER, 0);
        GLWrapper.Buffer.glBindBuffer(this.behavior.glTarget, 0);

        if (GLWrapper.Buffer.valid_Invalidate()) GLWrapper.Buffer.glInvalidateBufferData(this.glID);
        GLWrapper.Buffer.glDeleteBuffers(this.glID);
        this.glID = newBuffer;

        if (this.behavior.glRebindBuffer != null) this.behavior.glRebindBuffer.accept(this);
        
        final long ts = System.nanoTime(), compactTime = ts - this.lastCompactTime;
        this.lastCompactTime = ts;
        this.poolGPULock().unlock();
        this.getClientLock().unlock();
        return compactTime;
    }
}
