package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

public class BUtil_StaticTrailMemory extends GPUMemoryPool.InternalMemory<StaticTrailTracker> {
    private final byte rndID;
    private int totalNode = 0;
    private int realBufferAddress = 0;
    private int realDrawAddress = 0;
    private int nextNodePrt = 1;
    private float timer = 0.0f;
    private StaticTrailTracker tracker;
    private BUtil_StaticTrailCallback callback;

    public BUtil_StaticTrailMemory(StaticTrailTracker meta, long address, long size, int index, boolean isFree) {
        super(meta, address, size, index, isFree);
        int xorRnd = tracker.hashCode();
        xorRnd ^= xorRnd << 13;
        xorRnd ^= xorRnd >>> 17;
        xorRnd ^= xorRnd << 5;
        this.rndID = (byte) (xorRnd & 7);

        this.tracker = isFree ? null : meta;
        this.callback = isFree ? null : new BUtil_StaticTrailCallback();
    }

    public void afterAddressChanged() {
        this.realBufferAddress = Math.toIntExact((this.address() + BUtil_StaticTrailMemoryPool.RESERVED_SIZE) >>> 2);
    }

    public void afterAllocatedFromFree(StaticTrailTracker meta) {
        this.tracker = meta;
        this.callback = new BUtil_StaticTrailCallback();
    }

    public void afterFree() {
        this.tracker = null;
        this.callback = null;
    }

    private static float _calculateMix(final float a, final float b) {
        return (a != b) ? CalculateUtil.mix(a, b, (float) Math.random()) : a;
    }

    // may be something wrong in the first/second frame under very high fps(alien-tech computer), but no matter
    private float encodeTimestamp(final float elapsedTime) {
        return (this.rndID << 29) | (Float.floatToRawIntBits(elapsedTime) & 0x1fffffff);
    }

    private void framePass(final float amount) {
        this.timer = Math.max(this.timer - amount, 0.0f);
        if (this.timer <= 0.0f && this.totalNode != 0) {
            this.totalNode = 0;
            this.nextNodePrt = 0;
            this.callback.idleReset();
        }
    }

    /**
     * Only subData/persistentMapping.
     *
     * @return any nodes to draw, exclude fill node.
     */
    public int computeData(final BUtil_StaticTrailMemoryPool pool, final FloatBuffer uploadBuffer, boolean notPersistentMapping, float amount, float elapsedTime) {
        if (this.callback.shouldDestroyImmediate()) return -1;
        this.tracker.advance(amount, elapsedTime, this.callback);

        final boolean isDestroyed = this.callback.shouldDestroy(), isPaused = this.callback.isPaused();
        if (isDestroyed || isPaused) {
            if (isDestroyed && this.timer < 0.0f) this.callback.destroyImmediate();
            this.framePass(amount);
            return this.totalNode;
        }
        final float distSq = this.callback.distSq();
        if (distSq < StaticTrailTracker.MINIMAL_VALID_LENGTH_SQ) {
            this.framePass(amount);
            return this.totalNode;
        }

        final int maxWritePtr = pool.getMaxFullNodes(), realMaxNodes = pool.getMaxFullNodes() - 1;
        this.timer = pool.getMaxDur();
        if (this.totalNode < realMaxNodes) this.totalNode++;

        final Vector2f facing = this.callback.getCurrentFacing();
        final Vector4f spawnOffset = pool.getTrailData().getFixedSpawnOffsetRange();
        float uploadX = this.callback.getCurrentLocation().x, uploadY = this.callback.getCurrentLocation().y;
        if (spawnOffset != null) {
            final float mixX = _calculateMix(spawnOffset.x, spawnOffset.z),
                    mixY = _calculateMix(spawnOffset.y, spawnOffset.w);
            uploadX += mixX * facing.x - mixY * facing.y;
            uploadY += mixX * facing.y + mixY * facing.x;
        }
        this.callback.nextFrame((float) Math.sqrt(distSq));

        final float packingFacingVector = Float.intBitsToFloat(CommonUtil.float16ToShort(facing.y) << 16 & 0xFFFF | CommonUtil.float16ToShort(facing.x));
        int buffOffset = this.nextNodePrt * 5;
        if (!notPersistentMapping) buffOffset += this.realBufferAddress;

        if (notPersistentMapping) pool.getGPULock().lock();
        uploadBuffer.put(buffOffset, uploadX);
        uploadBuffer.put(buffOffset + 1, uploadY);
        uploadBuffer.put(buffOffset + 2, packingFacingVector);
        uploadBuffer.put(buffOffset + 3, this.encodeTimestamp(elapsedTime));
        uploadBuffer.put(buffOffset + 4, this.callback.getLength());
        if (notPersistentMapping) {
            uploadBuffer.position(this.nextNodePrt).limit(5);
            GL15.glBufferSubData(pool.getPoolBehavior().glTarget, this.address() + (this.nextNodePrt * 20L) + pool.getPoolBehavior().reservedSize, uploadBuffer);
            pool.getGPULock().unlock();
        }

        this.nextNodePrt++;
        if (this.nextNodePrt >= maxWritePtr) this.nextNodePrt = 0;
        return this.totalNode;
    }
}
