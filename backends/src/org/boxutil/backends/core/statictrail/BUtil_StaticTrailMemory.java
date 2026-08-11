package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;

import java.nio.IntBuffer;

public class BUtil_StaticTrailMemory extends GPUMemoryPool.InternalMemory<BUtil_StaticTrailTrackerObject> {
    private boolean shouldLoopWrite = false;
    private final byte rndID;
    private int totalNode = 0;
    private int realBufferAddress = 0;
    private int realDrawAddress = 0;
    private int nextNodePrt = 0;
    private float timer = 0.0f;
    private BUtil_StaticTrailTrackerObject trackerObj;
    private final int[] loopWriteData = new int[5];

    public BUtil_StaticTrailMemory(BUtil_StaticTrailTrackerObject meta, long address, long size, int index, boolean isFree) {
        super(meta, address, size, index, isFree);
        int xorRnd = this.hashCode();
        xorRnd ^= xorRnd << 13;
        xorRnd ^= xorRnd >>> 17;
        xorRnd ^= xorRnd << 5;
        this.rndID = (byte) (xorRnd & 7);

        this.trackerObj = isFree ? null : meta;
    }

    public BUtil_StaticTrailTrackerObject meta() {
        return this.trackerObj;
    }

    public void afterAddressChanged() {
        this.realDrawAddress = Math.toIntExact(this.address() >>> 2);
        this.realBufferAddress = Math.toIntExact((this.address() + BUtil_StaticTrailMemoryPool.NODE_BYTE_SIZE) >>> 2);
    }

    public void afterAllocatedFromFree(BUtil_StaticTrailTrackerObject meta) {
        this.trackerObj = meta;
    }

    public void afterFree() {
        this.trackerObj = null;
    }

    public int getDrawAddress() {
        return this.realDrawAddress;
    }

    private static float calculateMix(final float a, final float b) {
        return (a != b) ? CalculateUtil.mix(a, b, (float) Math.random()) : a;
    }

    // may be something wrong in the first/second frame under very high fps(alien-tech computer), but no matter
    private int encodeTimestamp(final float elapsedTime) {
        return (this.rndID << 29) | (Float.floatToRawIntBits(elapsedTime) & 0x1fffffff);
    }

    private void framePass(final float amount) {
        this.shouldLoopWrite = false;
        if (this.timer > 0.0f) this.timer = Math.max(this.timer - amount, 0.0f);
        if (this.totalNode != 0 && this.timer <= 0.0f) {
            this.totalNode = 0;
            this.nextNodePrt = 0;
            this.trackerObj.callback().idleReset();
        }
    }

    /**
     * Only subData/persistentMapping.
     *
     * @return any nodes to draw, exclude fill node.
     */
    public int computeData(final BUtil_StaticTrailMemoryPool pool, final IntBuffer uploadBuffer, boolean notPersistentMapping, float amount, float elapsedTime) {
        final var callback = this.trackerObj.callback();
        if (callback.shouldDestroyImmediate()) return -1;
        this.trackerObj.tracker().advance(amount, elapsedTime, callback);

        final boolean isDestroyed = callback.shouldDestroy(), isPaused = callback.isPaused();
        if (isDestroyed || isPaused) {
            if (isDestroyed && this.timer < 0.0f) callback.destroyImmediate();
            callback.nextFrame(0.0f);
            this.framePass(amount);
            return this.totalNode;
        }

        final float distSq = callback.getPreviousRecordsDistanceSq();
        final boolean nanDistSq = Float.isNaN(distSq);
        if (!nanDistSq && distSq < StaticTrailTracker.MINIMAL_VALID_LENGTH_SQ) {
            callback.nextFrame(0.0f);
            this.framePass(amount);
            return this.totalNode;
        }
        float segLength = nanDistSq ? 0.0f : (float) Math.sqrt(distSq);
        final float trailLength = callback.getElapsedLength() + segLength;

        final int realMaxNodes = pool.getMaxFullNodes() - 2; // minus 2: one for fill, another for loop draw
        this.timer = pool.getMaxDur();
        if (this.totalNode < realMaxNodes) this.totalNode++;
        callback.nextFrame(segLength);

        final Vector2f facing = callback.getCurrentFacing();
        final int a_position_x = Float.floatToRawIntBits(callback.getCurrentLocation().x),
                a_position_y = Float.floatToRawIntBits(callback.getCurrentLocation().y),
                a_facingVector = (CommonUtil.float16ToShort(facing.y) << 16 & 0xFFFF0000) | CommonUtil.float16ToShort(facing.x),
                a_timeStampRaw = this.encodeTimestamp(elapsedTime),
                a_distance = Float.floatToRawIntBits(trailLength);
        int buffOffset = this.nextNodePrt * 5, setBufLimit = 5;
        if (!notPersistentMapping) buffOffset += this.realBufferAddress;

        if (notPersistentMapping) pool.getGPULock().lock();
        if (this.shouldLoopWrite) {
            this.shouldLoopWrite = false;
            uploadBuffer.put(buffOffset, this.loopWriteData[0]);
            uploadBuffer.put(buffOffset + 1, this.loopWriteData[1]);
            uploadBuffer.put(buffOffset + 2, this.loopWriteData[2]);
            uploadBuffer.put(buffOffset + 3, this.loopWriteData[3]);
            uploadBuffer.put(buffOffset + 4, this.loopWriteData[4]);
            this.nextNodePrt++;
            buffOffset += 5;
            setBufLimit = 10;
        }
        uploadBuffer.put(buffOffset, a_position_x);
        uploadBuffer.put(buffOffset + 1, a_position_y);
        uploadBuffer.put(buffOffset + 2, a_facingVector);
        uploadBuffer.put(buffOffset + 3, a_timeStampRaw);
        uploadBuffer.put(buffOffset + 4, a_distance);
        if (notPersistentMapping) {
            uploadBuffer.position(buffOffset).limit(setBufLimit);
            GL15.glBufferSubData(pool.getPoolBehavior().glTarget, this.address() + (this.nextNodePrt * 20L) + pool.getPoolBehavior().reservedSize, uploadBuffer);
            pool.getGPULock().unlock();
        }

        this.nextNodePrt++;
        if (this.nextNodePrt >= realMaxNodes) {
            this.nextNodePrt = 0;
            this.shouldLoopWrite = true;
            this.loopWriteData[0] = a_position_x;
            this.loopWriteData[1] = a_position_y;
            this.loopWriteData[2] = a_facingVector;
            this.loopWriteData[3] = a_timeStampRaw;
            this.loopWriteData[4] = a_distance;
        }
        return this.totalNode;
    }

    public void cutTrail(final BUtil_StaticTrailMemoryPool pool, final IntBuffer uploadBuffer, boolean notPersistentMapping) {
        int buffOffset = this.nextNodePrt * 5;
        if (!notPersistentMapping) buffOffset += this.realBufferAddress;
        buffOffset += 4; // move a_distance

        if (notPersistentMapping) pool.getGPULock().lock();
        uploadBuffer.put(buffOffset, Float.floatToRawIntBits(0.0f));
        if (notPersistentMapping) {
            uploadBuffer.position(buffOffset).limit(1);
            GL15.glBufferSubData(pool.getPoolBehavior().glTarget, this.address() + (this.nextNodePrt * 20L) + pool.getPoolBehavior().reservedSize + 16L, uploadBuffer);
            pool.getGPULock().unlock();
        }

        this.trackerObj.callback().idleReset();
        this.loopWriteData[4] = 0;
    }
}
