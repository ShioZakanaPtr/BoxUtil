package org.boxutil.backends.core.instancedrendering;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.GPUPoolBehavior;
import org.boxutil.manager.InstanceDataMemoryPool;
import org.boxutil.units.standard.GPUMemoryPool;
import org.lwjgl.opengl.*;

@SuppressWarnings("UnusedReturnValue")
public final class BUtil_InstanceDataMemoryPool extends GPUMemoryPool<BUtil_InstanceMemory, InstanceType> {
    private final static BUtil_InstanceDataMemoryPool[] POOL = new BUtil_InstanceDataMemoryPool[InstanceType.values().length];
    private static boolean INVALID = true;

    private final byte binding;
    private final InstanceType target;

    public BUtil_InstanceDataMemoryPool(final GPUPoolBehavior<BUtil_InstanceMemory, InstanceType> behavior, final InstanceType target, byte binding) {
        super(behavior);
        this.binding = (byte) (4 + binding);
        this.target = target;
    }

    private static boolean poolReq(GPUMemoryPool<BUtil_InstanceMemory, InstanceType> ignore) {
        return BoxDatabase.getGLState().BOXUTIL_VALID && BoxConfigs.isBackgroundThreadGLValid() && BoxDatabase.getGLState().MAX_VERTEX_SHADER_STORAGE_BLOCKS > 7L;
    }

    private static void poolRebind(GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        final var poolCast = (BUtil_InstanceDataMemoryPool) pool;
        GL30.glBindBufferBase(pool.getPoolBehavior().glTarget, poolCast.binding, poolCast.glID);
    }

    private static BUtil_InstanceMemory makeEmptyMem(InstanceType meta, long address, long size, int index, GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        return new BUtil_InstanceMemory(meta, address, size, index, true);
    }

    private static BUtil_InstanceMemory makeNotEmptyMem(InstanceType meta, long address, long size, int index, GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        return new BUtil_InstanceMemory(meta, address, size, index, false);
    }

    public static void initPool() {
        final var poolSize = (byte) POOL.length;

        final InstanceType[] target = InstanceType.values();
        for (byte i = 0; i < poolSize; i++) {
            final var behavior = new GPUPoolBehavior<>(GL43.GL_SHADER_STORAGE_BUFFER, BUtil_InstanceDataMemoryPool::makeEmptyMem, BUtil_InstanceDataMemoryPool::makeNotEmptyMem);
            behavior.glContextRequirements = BUtil_InstanceDataMemoryPool::poolReq;
            behavior.glRebindBuffer = BUtil_InstanceDataMemoryPool::poolRebind;
            behavior.defaultBufferSize = target[i].getSize() * 65536L;
            behavior.persistentMappingAllow = false;

            POOL[i] = new BUtil_InstanceDataMemoryPool(behavior, target[i], i);
            POOL[i].init();
            INVALID &= POOL[i].isInvalid();
        }
    }

    public static boolean isPoolInvalid() {
        return INVALID;
    }

    public static BUtil_InstanceDataMemoryPool getPool(InstanceType target) {
        return POOL[target.ordinal()];
    }

    public long compact() {
        final long timeNano = super.compact();
        if (timeNano > 0) {
            final double time = super.compact() * 0.001d;
            final boolean toMS = time > 1000.0d;
            final String timeStr = String.format("%.3f", toMS ? time * 0.001d : time) + (toMS ? " ms'" : " us'");
            Global.getLogger(InstanceDataMemoryPool.class).info("'BoxUtil' [" + this.target.name() + "] instance memory pool compact elapsed: '" + timeStr);
        }
        return super.compact();
    }

    public void rebindBase() {
        this.clientLock.lock();
        this.gpuLock.lock();
        if (this.glID < 1 || this.memTotal < 1) {
            this.gpuLock.unlock();
            this.clientLock.unlock();
            return;
        }

        GL15.glBindBuffer(this.behavior.glTarget, this.glID);
        GL30.glBindBufferBase(this.behavior.glTarget, this.binding, this.glID);
        GL15.glBindBuffer(this.behavior.glTarget, 0);
        this.gpuLock.unlock();
        this.clientLock.unlock();
    }

    /**
     * Anchor at bottom-left, default size <code>1.0d * 1.0d</code>
     *
     * @return {space, total}
     */
    public Pair<Long, Long> glDrawMemoryUsage() {
        Pair<Long, Long> values = new Pair<>(0L, 0L);
        this.clientLock.lock();
        if (this.glID < 1) {
            GL11.glColor4ub(BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ONE_COLOR);
            GL11.glRectf(0.0f, 0.0f, 1.0f, 1.0f);
        } else {
            final byte[] rgb = new byte[3];
            final double div = 1.0d / this.memTotal;
            int colorSeed;

            for (BUtil_InstanceMemory block : this.mem) {
                if (block == null) continue;
                if (block.is_free()) {
                    rgb[0] = rgb[1] = rgb[2] = BoxEnum.ZERO;
                } else {
                    colorSeed = Long.hashCode(block.address()) ^ Long.hashCode(block.size());
                    colorSeed ^= colorSeed >> 16;
                    colorSeed *= 0x85ebca6b;
                    colorSeed ^= colorSeed >> 13;
                    colorSeed *= 0xc2b2ae35;
                    colorSeed ^= colorSeed >> 16;
                    rgb[0] = (byte) (colorSeed >>> 16 & 0xff);
                    rgb[1] = (byte) (colorSeed >>> 8 & 0xff);
                    rgb[2] = (byte) (colorSeed & 0xff);
                }
                GL11.glColor4ub(rgb[0], rgb[1], rgb[2], BoxEnum.ONE_COLOR);
                GL11.glRectd(block.address() * div, 0.0d, (block.address() + block.size()) * div, 1.0d);
            }
            values.one = this.memSpace;
            values.two = this.memTotal;
        }
        this.clientLock.unlock();
        return values;
    }
}
