package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.InstanceDataMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.opengl.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("UnusedReturnValue")
public final class BUtil_InstanceDataMemoryPool {
    private final static int _DEFAULT_INSTANCE_COUNT = 65536;
    private final static byte _INFO_SPACE= 0;
    private final static byte _INFO_EDGE= 1;
    private final static byte _INFO_TOTAL= 2;

    private final static List<BUtil_MemoryBlock>[] _MEM;
    private final static List<BUtil_MemoryBlock>[] _MEM_FREE;
    private final static boolean[] _COMPACT_CHECK;
    private final static long[][] _MEM_INFO;
    private final static AtomicInteger[] _MEM_REF;
    private final static SpinLock[] _LOCK;
    private final static ReentrantLock[] _GPU_LOCK;
    private final static int[] _SSBO;
    private final static byte[] _SSBO_BINDING;
    private final static long[] _LAST_COMPACT_NANO;
    private static boolean _IMMUTABLE;
    
    private static boolean _INIT = false;
    private static boolean _INVALID = true;

    static {
        final byte length = (byte) InstanceType.values().length;
        _MEM = new List[length];
        _MEM_FREE = new List[length];
        _COMPACT_CHECK = new boolean[length];
        _MEM_INFO = new long[length][3];
        _MEM_REF = new AtomicInteger[length];
        _LOCK = new SpinLock[length];
        _GPU_LOCK = new ReentrantLock[length];
        _SSBO = new int[length];
        _SSBO_BINDING = new byte[length];
        _LAST_COMPACT_NANO = new long[length];
        for (byte i = 0; i < length; i++) {
            _COMPACT_CHECK[i] = true;
            _LOCK[i] = new SpinLock();
            _GPU_LOCK[i] = new ReentrantLock();
            _MEM_REF[i] = new AtomicInteger(0);
            _SSBO_BINDING[i] = (byte) (4 + i); // 4, 5, 6, 7
            _LAST_COMPACT_NANO[i] = System.nanoTime();
        }
    }

    private final static class IndexI implements Comparable<IndexI> {
        private int i = -1;

        private IndexI() {}

        public IndexI(int i) {
            this.i = i;
        }

        public void increment() {
            ++i;
        }

        public void decrement() {
            --i;
        }

        public void add(int value) {
            i += value;
        }

        public void sub(int value) {
            i -= value;
        }

        public int compareTo(@NotNull IndexI o) {
            return Integer.compare(i, o.i);
        }

        public int hashCode() {
            return Integer.hashCode(i);
        }

        public boolean equals(Object obj) {
            if (obj instanceof IndexI) return hashCode() == obj.hashCode();
            else return false;
        }
    }

    private final static class BUtil_MemoryBlock implements MemoryBlock, Comparable<BUtil_MemoryBlock> {
        private final IndexI _index;
        private final InstanceType _type;
        private final AtomicInteger ref;
        private final boolean _isType2D;
        private final boolean _isTypeFixed;
        private long address;
        private long size;
        private int instanceAddress = 0;
        private int instanceCount = 0;

        public BUtil_MemoryBlock(InstanceType type, long offset, long length, int index, boolean isFree) {
            if (type == null) throw new IllegalArgumentException("Illegal memory type: null");
            if (offset < 0 || length < 1) throw new IllegalArgumentException("Illegal memory range: address = '" + offset + "' size = '" + length + '\'');
            this._index = new IndexI(index);
            this._type = type;
            this.ref = new AtomicInteger(isFree ? 0 : 1);
            this._isType2D = type == InstanceType.DYNAMIC_2D || type == InstanceType.FIXED_2D;
            this._isTypeFixed = type == InstanceType.FIXED_2D || type == InstanceType.FIXED_3D;
            this.address = offset;
            this.size = length;
            this.computeInstanceAddress();
            this.computeInstanceCount();
        }

        public InstanceType type() {
            return this._type;
        }

        public int reference() {
            return this.ref.get();
        }

        public long address() {
            return this.address;
        }

        public int address_instance() {
            return this.instanceAddress;
        }

        public long size() {
            return this.size;
        }

        public int instance_count() {
            return this.instanceCount;
        }

        private void computeInstanceAddress() {
            this.instanceAddress = Math.toIntExact(this.address / this._type.getSize());
        }

        private void computeInstanceCount() {
            this.instanceCount = Math.toIntExact(this.size / this._type.getSize());
        }

        public boolean is_free() {
            return this.ref.get() < 1;
        }

        public boolean is_type_2D() {
            return this._isType2D;
        }

        public boolean is_type_fixed() {
            return this._isTypeFixed;
        }

        public int compareTo(@NotNull BUtil_MemoryBlock o) {
            return is_free() ? 1 : Integer.compare(this.instanceCount, o.instanceCount);
        }

        public String toString() {
            return "'BoxUtil' Memory block: Type = '" + this._type.name() + "' Reference = '" + this.ref.get() + "' Address = '0x" + Long.toHexString(this.address).toUpperCase() + "' Size = '" + this.size + "'";
        }

        public int hashCode() {
            int result = 31 + this._type.hashCode();
            result = 31 * result + Long.hashCode(this.address);
            return 31 * result + Long.hashCode(this.size);
        }

        public boolean equals(Object obj) {
            if (obj instanceof BUtil_MemoryBlock) return hashCode() == obj.hashCode();
            else return false;
        }
    }

    public static void init() {
        if (_INIT || !BoxDatabase.getGLState().GL_SSBO || BoxDatabase.getGLState().MAX_VERTEX_SHADER_STORAGE_BLOCKS < 8L) return;
        _IMMUTABLE = BoxDatabase.getGLState().GL_GL44;
        _INVALID = false;
        _INIT = true;
    }

    public static boolean isNotSupported() {
        return _INVALID;
    }

    static void _PoolDestroy() {
        if (BoxDatabase.getGLState().GL_SSBO) GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        int t;
        for (InstanceType type : InstanceType.values()) {
            t = type.ordinal();
            _LOCK[t].lock();
            cleanupClientSSBO(t);
            if (_SSBO[t] > 0) GL43.glInvalidateBufferData(_SSBO[t]);
            GL15.glDeleteBuffers(_SSBO[t]);
            _LOCK[t].unlock();
        }
    }

    private static void runtimeBufferIDCheck(int target, int buffer) {
        if (buffer < 1) {
            if (_GPU_LOCK[target].isLocked()) _GPU_LOCK[target].unlock();
            if (_LOCK[target].isLocked()) _LOCK[target].unlock();
            throw new OpenGLException("Fatal: cannot generate valid new buffer object on current thread '" + Thread.currentThread().getName() + '\'');
        }
    }

    private static void cleanupClientSSBO(int target) {
        for (BUtil_MemoryBlock block : _MEM[target]) block.ref.set(0);
        if (_MEM[target] != null) {
            _MEM[target].clear();
            _MEM[target] = null;
        }
        if (_MEM_FREE[target] != null) {
            _MEM_FREE[target].clear();
            _MEM_FREE[target] = null;
        }
        _COMPACT_CHECK[target] = true;
        _MEM_INFO[target][_INFO_SPACE] = _MEM_INFO[target][_INFO_EDGE] = _MEM_INFO[target][_INFO_TOTAL] = 0L;
        _MEM_REF[target].set(0);
    }

    private static void eraseMemory(InstanceType targetEnum, int target) {
        BUtil_MemoryBlock freeBlock = new BUtil_MemoryBlock(targetEnum, 0, _MEM_INFO[target][_INFO_TOTAL], 0, true);
        _MEM[target].add(freeBlock);
        _MEM_FREE[target].add(freeBlock);
        _MEM_INFO[target][_INFO_EDGE] = 0;
        _MEM_INFO[target][_INFO_SPACE] = _MEM_INFO[target][_INFO_TOTAL];
        _MEM_REF[target].set(0);
    }

    private static void invalidateSSBO(int target) {
        _GPU_LOCK[target].lock();
        if (_SSBO[target] > 0) {
            GL43.glInvalidateBufferData(_SSBO[target]);
            GL15.glDeleteBuffers(_SSBO[target]);
            _SSBO[target] = 0;
        }
        _GPU_LOCK[target].unlock();
    }

    private static void initSSBO(InstanceType targetEnum, int target, long count) {
        _GPU_LOCK[target].lock();
        invalidateSSBO(target);
        final long newSize = count < _DEFAULT_INSTANCE_COUNT ? _DEFAULT_INSTANCE_COUNT : CalculateUtil.getPOTMax(count << 1);
        _MEM_INFO[target][_INFO_TOTAL] = newSize * targetEnum.getSize();

        _SSBO[target] = GL15.glGenBuffers();
        runtimeBufferIDCheck(target, _SSBO[target]);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO[target]);
        if (_IMMUTABLE) GL44.glBufferStorage(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[target][_INFO_TOTAL], GL44.GL_DYNAMIC_STORAGE_BIT | GL30.GL_MAP_READ_BIT | GL30.GL_MAP_WRITE_BIT);
        else GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[target][_INFO_TOTAL], GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO_BINDING[target], _SSBO[target]);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        _MEM_REF[target].set(0);
        _GPU_LOCK[target].unlock();
    }

    private static void copyRangeSSBO(int target, final long srcOffset, final long dstOffset, final long length) {
        _GPU_LOCK[target].lock();
        final int tmpBuffer = GL15.glGenBuffers();
        runtimeBufferIDCheck(target, tmpBuffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, tmpBuffer);
        if (_IMMUTABLE) GL44.glBufferStorage(GL43.GL_SHADER_STORAGE_BUFFER, length, 0);
        else GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, length, GL15.GL_DYNAMIC_DRAW);

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, _SSBO[target]);
        GL31.glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER, srcOffset, 0, length);

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, tmpBuffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO[target]);
        GL31.glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER, 0, dstOffset, length);

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, 0);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        GL43.glInvalidateBufferData(tmpBuffer);
        GL15.glDeleteBuffers(tmpBuffer);
        _GPU_LOCK[target].unlock();
    }

    private static void expandSSBO(int target, final long check) {
        if (_MEM_INFO[target][_INFO_TOTAL] == 0) throw new OpenGLException("Failed to expand SSBO: not initialization yet.");

        _GPU_LOCK[target].lock();
        final long oldSize = _MEM_INFO[target][_INFO_TOTAL];
        do {
            _MEM_INFO[target][_INFO_TOTAL] <<= 1;
        } while (_MEM_INFO[target][_INFO_TOTAL] - _MEM_INFO[target][_INFO_EDGE] <= check);

        if (_MEM_INFO[target][_INFO_TOTAL] > BoxDatabase.getGLState().MAX_SHADER_STORAGE_BLOCK_SIZE) {
            _GPU_LOCK[target].unlock();
            throw new OutOfMemoryError("Failed to allocate SSBO with over " + BoxDatabase.getGLState().MAX_SHADER_STORAGE_BLOCK_SIZE + " Byte.");
        }
        if (_MEM_INFO[target][_INFO_TOTAL] < 1) {
            _GPU_LOCK[target].unlock();
            throw new OutOfMemoryError("Failed to allocate SSBO with over 8_388_608 TiB.");
        }
        final long diff = _MEM_INFO[target][_INFO_TOTAL] - oldSize;
        _MEM_INFO[target][_INFO_SPACE] += diff;

        BUtil_MemoryBlock lastBlock = _MEM[target].get(_MEM[target].size() - 1);
        if (lastBlock.is_free()) {
            lastBlock.size += diff;
            lastBlock.computeInstanceCount();
        } else {
            BUtil_MemoryBlock newFree = new BUtil_MemoryBlock(lastBlock._type, lastBlock.address + lastBlock.size, diff, lastBlock._index.i + 1, true);
            _MEM[target].add(newFree);
            _MEM_FREE[target].add(newFree);
        }

        final int newBuffer = GL15.glGenBuffers();
        runtimeBufferIDCheck(target, newBuffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, newBuffer);
        if (_IMMUTABLE) GL44.glBufferStorage(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[target][_INFO_TOTAL], GL44.GL_DYNAMIC_STORAGE_BIT | GL30.GL_MAP_READ_BIT | GL30.GL_MAP_WRITE_BIT);
        else GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[target][_INFO_TOTAL], GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO_BINDING[target], newBuffer);

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, _SSBO[target]);
        GL31.glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER, 0, 0, _MEM_INFO[target][_INFO_EDGE]);

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, 0);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        if (_SSBO[target] > 0) {
            GL43.glInvalidateBufferData(_SSBO[target]);
            GL15.glDeleteBuffers(_SSBO[target]);
        }
        _SSBO[target] = newBuffer;
        _GPU_LOCK[target].unlock();
    }

    private static BUtil_MemoryBlock scanEndsFreeBlock(final int target, final long foundSize) {
        BUtil_MemoryBlock result = null;
        final int memArraySize = _MEM_FREE[target].size();

        final int forwardLimit = Math.min(memArraySize, 5);
        for (int i = 0; i < forwardLimit; ++i) {
            result = _MEM_FREE[target].get(i);
            if (result.size < foundSize) {
                result = null;
            } else break;
        }
        if (result != null || memArraySize < 6) return result;

        final int forwardStart = Math.max(memArraySize - 5, 5);
        for (int i = forwardStart; i < memArraySize; ++i) {
            result = _MEM_FREE[target].get(i);
            if (result.size < foundSize) {
                result = null;
            } else break;
        }
        if (result != null || memArraySize < 11) return result;

        for (int i = forwardLimit; i < forwardStart; ++i) { // scan all between ends
            result = _MEM_FREE[target].get(i);
            if (result.size < foundSize) {
                result = null;
            } else break;
        }
        return result;
    }

    private static boolean sizeOverRange(int target, long size) {
        return _MEM_INFO[target][_INFO_EDGE] + size > BoxDatabase.getGLState().MAX_SHADER_STORAGE_BLOCK_SIZE;
    }

    public static MemoryBlock malloc(@NotNull InstanceType target, int count) {
        if (_INVALID) return null;
        if (count == 0) throw new IllegalArgumentException("Undefined behavior, cannot call malloc() with '0' count");
        if (count < 1) throw new IllegalArgumentException("Unable to allocate memory block with instance count: '" + count + '\'');
        final int t = target.ordinal();
        _LOCK[t].lock();
        if (_MEM[t] == null) {
            _MEM[t] = new ArrayList<>();
            _MEM_FREE[t] = new ArrayList<>();
            initSSBO(target, t, count);
            eraseMemory(target, t);
        }

        final long newBlockSize = (long) target.getSize() * count;
        if (sizeOverRange(t, newBlockSize)) {
            _LOCK[t].unlock();
            return null;
        }
        BUtil_MemoryBlock result, freeBlock = scanEndsFreeBlock(t, newBlockSize);

        boolean allocateAtEnd = freeBlock == null;
        int memIndexLimit;
        if (allocateAtEnd) {
            expandSSBO(t, newBlockSize); // keep last free block, resize the space
            memIndexLimit = _MEM[t].size() - 1;
            freeBlock = _MEM[t].get(memIndexLimit);
            if (freeBlock.size < newBlockSize) {
                _LOCK[t].unlock();
                return null;
            }
        } else memIndexLimit = _MEM[t].size() - 1;

        _MEM_INFO[t][_INFO_SPACE] -= newBlockSize;
        allocateAtEnd = freeBlock._index.i == memIndexLimit;
        if (freeBlock.size == newBlockSize) {
            freeBlock.ref.set(1);
            result = freeBlock;
            _MEM_FREE[t].remove(freeBlock);
        } else {
            result = new BUtil_MemoryBlock(target, freeBlock.address, newBlockSize, freeBlock._index.i, false);
            freeBlock.address += newBlockSize;
            freeBlock.size -= newBlockSize;
            freeBlock.computeInstanceAddress();
            freeBlock.computeInstanceCount();
            _MEM[t].add(freeBlock._index.i, result);
            memIndexLimit = _MEM[t].size();
            for (int i = result._index.i + 1; i < memIndexLimit; ++i) _MEM[t].get(i)._index.increment();
        }
        if (allocateAtEnd) _MEM_INFO[t][_INFO_EDGE] += newBlockSize;

        _COMPACT_CHECK[t] = false;
        _MEM_REF[t].incrementAndGet();
        _LOCK[t].unlock();
        return result;
    }

    public static boolean realloc(@NotNull MemoryBlock memoryInterface, int newCount) {
        if (_INVALID) return false;
        if (!(memoryInterface instanceof BUtil_MemoryBlock memory)) throw new IllegalArgumentException("Undefined memory block: not a illegal block.");
        if (newCount == 0) throw new IllegalCallerException("Undefined behavior, cannot call realloc() with '0' count, should use free()");
        if (newCount < 1) throw new IllegalArgumentException("Unable to call realloc() with instance count: '" + newCount + '\'');
        final int t = memory._type.ordinal();
        _LOCK[t].lock();
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block: realloc() for free block.");
        final long newBlockSize = (long) memory._type.getSize() * newCount;
        if (memory.size == newBlockSize) {
            _LOCK[t].unlock();
            return true;
        }

        final int indexI = memory._index.i;
        if (indexI < 0) throw new IllegalStateException("Target memory block is does not exist in SSBO with type '" + memory._type.name() + '\'');

        BUtil_MemoryBlock nextBlock = null;
        final long diff = newBlockSize - memory.size;
        if (sizeOverRange(t, newBlockSize)) {
            _LOCK[t].unlock();
            return false;
        }
        int memArraySize = _MEM[t].size(), indexMax = memArraySize - 1, nextIndex = indexI + 1;
        boolean nextBlockAtEnd = false;
        if (indexI < indexMax) { // found next
            nextBlockAtEnd = nextIndex == indexMax;
            nextBlock = _MEM[t].get(nextIndex);
        }

        if (memory.size > newBlockSize) { // contracting
            boolean haveNextBlock = nextBlock != null;
            memory.size = newBlockSize;
            memory.computeInstanceCount();
            _MEM_INFO[t][_INFO_SPACE] -= diff;

            if (haveNextBlock && nextBlock.is_free()) {
                nextBlock.address += diff;
                nextBlock.size -= diff;
                nextBlock.computeInstanceAddress();
                nextBlock.computeInstanceCount();
                _MEM_INFO[t][_INFO_EDGE] += diff;

                _COMPACT_CHECK[t] = false;
                _LOCK[t].unlock();
                return true;
            }

            nextBlock = new BUtil_MemoryBlock(memory._type, memory.address + memory.size, -diff, nextIndex, true);
            _MEM_FREE[t].add(nextBlock);
            if (haveNextBlock) {
                _MEM[t].add(nextIndex, nextBlock);
                ++memArraySize;
                for (int i = nextIndex + 1; i < memArraySize; ++i) _MEM[t].get(i)._index.increment();
            } else {
                _MEM_INFO[t][_INFO_EDGE] += diff;
                _MEM[t].add(nextBlock);
            }

            _COMPACT_CHECK[t] = false;
            _LOCK[t].unlock();
            return true;
        } // else expanding:

        BUtil_MemoryBlock preBlock = null, globalFreeBlock;
        final long oldAddress = memory.address, oldSize = memory.size;
        boolean nextBlockFree = false, freeBlockNotEnough, callExpand;
        if (nextBlock != null && nextBlock.is_free()) {
            nextBlockFree = true;
            freeBlockNotEnough = nextBlock.size < diff;

            if (!freeBlockNotEnough) {
                if (nextBlock.size == diff) {
                    _MEM[t].remove(nextBlock);
                    _MEM_FREE[t].remove(nextBlock);
                    --memArraySize;
                    for (int i = nextIndex; i < memArraySize; ++i) _MEM[t].get(i)._index.decrement();
                } else {
                    nextBlock.address += diff;
                    nextBlock.size -= diff;
                    nextBlock.computeInstanceAddress();
                    nextBlock.computeInstanceCount();
                }
                memory.size = newBlockSize;
                memory.computeInstanceCount();
                _MEM_INFO[t][_INFO_SPACE] -= diff;
                if (nextBlockAtEnd) _MEM_INFO[t][_INFO_EDGE] += diff;

                _COMPACT_CHECK[t] = false;
                _LOCK[t].unlock();
                return true;
            }
        }

        // found pre
        final int preIndex = indexI - 1;
        if (indexI > 0) preBlock = _MEM[t].get(preIndex);
        if (preBlock != null && preBlock.is_free()) {
            long _freeSpace = nextBlockFree ? preBlock.size + nextBlock.size : preBlock.size;
            freeBlockNotEnough = _freeSpace < diff;

            if (!freeBlockNotEnough) {
                byte _subValue = 0;
                if (_freeSpace == diff) {
                    _MEM[t].remove(preBlock);
                    _MEM_FREE[t].remove(preBlock);
                    if (nextBlockFree) ++_subValue;
                    --nextIndex; // lost pre block
                } else {
                    Collections.swap(_MEM[t], preIndex, indexI);
                    preBlock._index.increment();
                }
                if (nextBlockFree) {
                    _MEM[t].remove(nextBlock);
                    _MEM_FREE[t].remove(nextBlock);
                    ++_subValue;
                }

                if (_subValue > 0) {
                    memArraySize -= _subValue;
                    for (int i = nextIndex; i < memArraySize; ++i) _MEM[t].get(i)._index.sub(_subValue);
                }

                copyRangeSSBO(t, oldAddress, preBlock.address, oldSize);
                memory.address = preBlock.address;
                memory.size = newBlockSize;
                memory.computeInstanceAddress();
                memory.computeInstanceCount();
                memory._index.decrement();
                preBlock.address = memory.address + memory.size;
                preBlock.size = diff;
                preBlock.computeInstanceAddress();
                preBlock.computeInstanceCount();
                _MEM_INFO[t][_INFO_SPACE] -= diff;
                if (nextBlockAtEnd) _MEM_INFO[t][_INFO_EDGE] += diff;

                _COMPACT_CHECK[t] = false;
                _LOCK[t].unlock();
                return true;
            }
        }

        // found ends
        globalFreeBlock = scanEndsFreeBlock(t, newBlockSize);
        callExpand = globalFreeBlock == null || globalFreeBlock.size < newBlockSize;

        if (callExpand) {
            expandSSBO(t, newBlockSize); // expand at end
            memArraySize = _MEM[t].size();
            indexMax = memArraySize - 1;
            globalFreeBlock = _MEM[t].get(indexMax);
            if (globalFreeBlock.size < newBlockSize) {
                _LOCK[t].unlock();
                return false;
            }
        }

        final boolean freeBlockAtEnd = globalFreeBlock._index.i == indexMax, notAllocateFromExpand = globalFreeBlock._index.i - 1 != indexI, removeFreeBlock = globalFreeBlock.size == diff;
        if (notAllocateFromExpand) {
            nextBlock = new BUtil_MemoryBlock(memory._type, memory.address, memory.size, indexI, true);
            _MEM[t].set(indexI, nextBlock);
            _MEM_FREE[t].add(nextBlock);

            _MEM[t].add(globalFreeBlock._index.i, memory);
            memory._index.i = globalFreeBlock._index.i;
            memory.address = globalFreeBlock.address;
            ++memArraySize;
            if (!removeFreeBlock) for (int i = globalFreeBlock._index.i + 1; i < memArraySize; ++i) _MEM[t].get(i)._index.increment();

            // assert not overlap
            _GPU_LOCK[t].lock();
            GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO[t]);
            GL31.glCopyBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER, oldAddress, memory.address, oldSize);
            GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
            _GPU_LOCK[t].unlock();
        }
        memory.size = newBlockSize;
        memory.computeInstanceAddress();
        memory.computeInstanceCount();

        if (removeFreeBlock) {
            _MEM[t].remove(globalFreeBlock);
            _MEM_FREE[t].remove(globalFreeBlock);
        } else {
            globalFreeBlock.address += diff;
            globalFreeBlock.size -= diff;
            globalFreeBlock.computeInstanceAddress();
            globalFreeBlock.computeInstanceCount();
        }

        _MEM_INFO[t][_INFO_SPACE] -= diff;
        if (freeBlockAtEnd) _MEM_INFO[t][_INFO_EDGE] = memory.address + memory.size;
        _COMPACT_CHECK[t] = false;
        _LOCK[t].unlock();
        return true;
    }

    public static MemoryBlock split(@NotNull MemoryBlock memoryInterface, int newCount, boolean fromStartOrEnd) {
        if (_INVALID) return null;
        if (newCount == 0) return null;
        if (!(memoryInterface instanceof BUtil_MemoryBlock memory)) throw new IllegalArgumentException("Undefined memory block: not a illegal block.");
        if (newCount < 0) throw new IllegalArgumentException("Unable to call split() from memory block with instance count: '" + newCount + '\'');
        final int t = memory._type.ordinal();
        _LOCK[t].lock();
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block: split() for free block.");

        final long newBlockSize = (long) memory._type.getSize() * newCount, oriBlockSize = memory.size - newBlockSize;
        if (memory.size == newBlockSize) throw new IllegalArgumentException("Undefined behavior, cannot split memory block as same size.");

        BUtil_MemoryBlock result = new BUtil_MemoryBlock(memory._type, memory.address, newBlockSize, memory._index.i, false);
        memory.size = oriBlockSize;
        memory.computeInstanceCount();

        int index = memory._index.i, memArraySize = _MEM[t].size();
        boolean noAtEnd = index != memArraySize - 1;
        if (fromStartOrEnd) {
            memory.address += newBlockSize;
            memory.computeInstanceAddress();
            _MEM[t].add(index, result);
        } else {
            result.address += oriBlockSize;
            result.computeInstanceAddress();
            if (noAtEnd) _MEM[t].add(index + 1, result); else _MEM[t].add(result);
        }
        for (int i = index + 1; i < memArraySize; ++i) _MEM[t].get(i)._index.increment();

        _COMPACT_CHECK[t] = false;
        _MEM_REF[t].incrementAndGet();
        _LOCK[t].unlock();
        return result;
    }

    private static void mergeFreeBlock(BUtil_MemoryBlock memory, int target) {
        _MEM_INFO[target][_INFO_SPACE] += memory.size;
        int memIndex = memory._index.i, memArraySize = _MEM[target].size();
        int checkIndex;
        boolean mergePre = false, mergeNext = false;
        BUtil_MemoryBlock check;

        checkIndex = memIndex - 1;
        if (checkIndex > -1) {
            check = _MEM[target].get(checkIndex);
            mergePre = check.is_free();
            if (mergePre) {
                check.size += memory.size;
                check.computeInstanceCount();
                _MEM[target].remove(memory);
                memory = check;
                memIndex = checkIndex;
                --memArraySize;
            }
        }
        checkIndex = memIndex + 1;
        if (checkIndex < memArraySize) {
            check = _MEM[target].get(checkIndex);
            mergeNext = check.is_free();
            if (mergeNext) {
                memory.size += check.size;
                memory.computeInstanceCount();
                _MEM[target].remove(check);
                _MEM_FREE[target].remove(check);
                --memArraySize;
            }
        }

        if (mergePre || mergeNext) {
            final byte offset = (byte) (mergePre && mergeNext ? 2 : 1);
            for (int i = memIndex + 1; i < memArraySize; ++i) _MEM[target].get(i)._index.sub(offset);
        }

        if (!mergePre) {
            _MEM_FREE[target].add(memory);
            _MEM_FREE[target].sort(Comparator.comparingLong(o -> o.address));
        }

        check = _MEM[target].get(memArraySize - 1);
        if (check.is_free()) _MEM_INFO[target][_INFO_EDGE] = check.address;
    }

    public static boolean free(@NotNull MemoryBlock memoryInterface) {
        if (_INVALID) return false;
        if (!(memoryInterface instanceof BUtil_MemoryBlock memory)) throw new IllegalArgumentException("Undefined memory block: not a illegal block.");
        final int t = memory._type.ordinal();
        boolean result = true;
        _LOCK[t].lock();
        if (memory.ref.get() > 0) {
            if (_MEM_REF[t].get() < 1) throw new IllegalStateException("Failed to free memory: SSBO was free but memory block still have reference.");
            memory.ref.getAndDecrement();
            boolean cleanup = _MEM_REF[t].getAndDecrement() < 1;
            result = memory.is_free();
            if (result) {
                mergeFreeBlock(memory, t);
                _COMPACT_CHECK[t] = cleanup;
                if (cleanup) {
                    _MEM[t].clear();
                    _MEM_FREE[t].clear();
                    eraseMemory(memory._type, t);
                }
            }
        }
        _LOCK[t].unlock();
        return result;
    }

    public static MemoryBlock share(@NotNull MemoryBlock memoryInterface) {
        if (_INVALID) return null;
        if (!(memoryInterface instanceof BUtil_MemoryBlock memory)) throw new IllegalArgumentException("Undefined memory block: not a illegal block.");
        if (memory.is_free()) throw new IllegalArgumentException("Undefined memory block.");
        final int t = memory._type.ordinal();
        _LOCK[t].lock();
        memory.ref.getAndIncrement();
        _MEM_REF[memory._type.ordinal()].getAndIncrement();
        _LOCK[t].unlock();
        return memory;
    }

    static void _compact(InstanceType target) {
        if (_INVALID) return;
        final int t = target.ordinal();
        final Logger logger = Global.getLogger(InstanceDataMemoryPool.class);
        _LOCK[t].lock();
        _GPU_LOCK[t].lock();
        _LAST_COMPACT_NANO[t] = System.nanoTime();

        if (_SSBO[t] < 1 || _MEM_INFO[t][_INFO_SPACE] == _MEM_INFO[t][_INFO_TOTAL] || _MEM_INFO[t][_INFO_TOTAL] < 1) {
            _GPU_LOCK[t].unlock();
            _LOCK[t].unlock();
//            logger.info("'BoxUtil' instance memory pool '" + target.name() + "' need not compact: not initialize yet.");
            return;
        }
        if (_COMPACT_CHECK[t]) {
            _GPU_LOCK[t].unlock();
            _LOCK[t].unlock();
//            logger.info("'BoxUtil' instance memory pool '" + target.name() + "' need not compact.");
            return;
        }
        if (_MEM_REF[t].get() < 1) {
            cleanupClientSSBO(t);
            invalidateSSBO(t);
            _GPU_LOCK[t].unlock();
            _LOCK[t].unlock();
//            logger.info("'BoxUtil' instance memory pool '" + target.name() + "' need not compact: empty memory pool.");
            return;
        }
        _COMPACT_CHECK[t] = true;

        final int newBuffer = GL15.glGenBuffers();
        runtimeBufferIDCheck(t, newBuffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, newBuffer);
        if (_IMMUTABLE) GL44.glBufferStorage(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[t][_INFO_TOTAL], GL44.GL_DYNAMIC_STORAGE_BIT | GL30.GL_MAP_READ_BIT | GL30.GL_MAP_WRITE_BIT);
        else GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, _MEM_INFO[t][_INFO_TOTAL], GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO_BINDING[t], newBuffer);

        Collections.sort(_MEM[t]);
        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, _SSBO[t]);

        BUtil_MemoryBlock block;
        long currOffset = 0L;
        int index = 0;
        for (Iterator<BUtil_MemoryBlock> iterator = _MEM[t].iterator(); iterator.hasNext();) {
            block = iterator.next();
            if (block == null || block.is_free() || block.address < 0 || block.size < 1) {
                iterator.remove();
                continue;
            }
            GL31.glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER, block.address, currOffset, block.size);
            block.address = currOffset;
            block.computeInstanceAddress();
            currOffset += block.size;
            block._index.i = index;
            ++index;
        }
        _MEM_INFO[t][_INFO_SPACE] = _MEM_INFO[t][_INFO_TOTAL] - currOffset;
        _MEM_INFO[t][_INFO_EDGE] = currOffset;
        _MEM_FREE[t].clear();
        if (_MEM_INFO[t][_INFO_SPACE] > 0) {
            block = new BUtil_MemoryBlock(target, currOffset, _MEM_INFO[t][_INFO_SPACE], index, true);
            _MEM[t].add(block);
            _MEM_FREE[t].add(block);
        }

        GL15.glBindBuffer(GL31.GL_COPY_READ_BUFFER, 0);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        GL43.glInvalidateBufferData(_SSBO[t]);
        GL15.glDeleteBuffers(_SSBO[t]);
        GL42.glMemoryBarrier(GL42.GL_BUFFER_UPDATE_BARRIER_BIT);
        _SSBO[t] = newBuffer;
        final long ts = System.nanoTime();
        double time = (ts - _LAST_COMPACT_NANO[t]) * 0.001d;
        _LAST_COMPACT_NANO[t] = ts;
        _GPU_LOCK[t].unlock();
        _LOCK[t].unlock();

        String timeStr = " us'"; // µs
        if (time > 1000.0d) {
            time *= 0.001d;
            timeStr = " ms'";
        }
        logger.info("'BoxUtil' [" + target.name() + "] instance memory pool compact elapsed: '" + String.format("%.3f", time) + timeStr);
    }

    static void _invalidate(InstanceType target) {
        if (_INVALID) return;
        final int t = target.ordinal();
        _LOCK[t].lock();
        cleanupClientSSBO(t);
        invalidateSSBO(t);
        _LOCK[t].unlock();
    }

    public static void rebindSSBO(InstanceType target) {
        final int t = target.ordinal();
        _LOCK[t].lock();
        _GPU_LOCK[t].lock();
        if (_SSBO[t] < 1 || _MEM_INFO[t][_INFO_TOTAL] < 1) {
            _GPU_LOCK[t].unlock();
            _LOCK[t].unlock();
            return;
        }

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO[t]);
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, _SSBO_BINDING[t], _SSBO[t]);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        _GPU_LOCK[t].unlock();
        _LOCK[t].unlock();
    }

    public static int getBufferID(InstanceType target) {
        return _SSBO[target.ordinal()];
    }

    public static byte getBufferBindingBase(InstanceType target) {
        return _SSBO_BINDING[target.ordinal()];
    }

    public static long getBufferSpace(InstanceType target) {
        return _MEM_INFO[target.ordinal()][_INFO_SPACE];
    }

    public static long getBufferEdge(InstanceType target) {
        return _MEM_INFO[target.ordinal()][_INFO_EDGE];
    }

    public static long getBufferTotal(InstanceType target) {
        return _MEM_INFO[target.ordinal()][_INFO_TOTAL];
    }

    public static int getBufferRef(InstanceType target) {
        return _MEM_REF[target.ordinal()].get();
    }

    public static SpinLock getLock(InstanceType target) {
        return _LOCK[target.ordinal()];
    }

    public static ReentrantLock getGPULock(InstanceType target) {
        return _GPU_LOCK[target.ordinal()];
    }

    /**
     * Anchor at bottom-left, default size <code>1.0d * 1.0d</code>
     *
     * @return {space, total}
     */
    public static Pair<Long, Long> glDrawMemoryUsage(InstanceType target) {
        final int t = target.ordinal();
        Pair<Long, Long> values = new Pair<>(0L, 0L);
        _LOCK[t].lock();
        if (_SSBO[t] < 1) {
            GL11.glColor4ub(BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ONE_COLOR);
            GL11.glRectf(0.0f, 0.0f, 1.0f, 1.0f);
        } else {
            double div = 1.0d / _MEM_INFO[t][_INFO_TOTAL];
            int colorBits, alphaBits;
            byte[] rgb = new byte[3];
            for (BUtil_MemoryBlock block : _MEM[t]) {
                if (block == null) continue;
                if (block.is_free()) {
                    rgb[0] = rgb[1] = rgb[2] = BoxEnum.ZERO;
                } else {
                    colorBits = Long.hashCode(block.address) ^ Long.hashCode(block.size);
                    alphaBits = (colorBits >>> 24) ^ 0b10001000;
                    rgb[0] = (byte) ((colorBits >>> 16 ^ alphaBits) & 0xFF);
                    rgb[1] = (byte) ((colorBits >>> 8 ^ alphaBits) & 0xFF);
                    rgb[2] = (byte) ((colorBits ^ alphaBits) & 0xFF);
                }
                GL11.glColor4ub(rgb[0], rgb[1], rgb[2], BoxEnum.ONE_COLOR);
                GL11.glRectd(block.address * div, 0.0d, (block.address + block.size) * div, 1.0d);
            }
            values.one = _MEM_INFO[t][_INFO_SPACE];
            values.two = _MEM_INFO[t][_INFO_TOTAL];
        }
        _LOCK[t].unlock();
        return values;
    }

    public static long getLastCompactTimeStampNano(InstanceType target) {
        return _LAST_COMPACT_NANO[target.ordinal()];
    }

    public static boolean isImmutableBuffer() {
        return _IMMUTABLE;
    }

    private BUtil_InstanceDataMemoryPool() {}
}
