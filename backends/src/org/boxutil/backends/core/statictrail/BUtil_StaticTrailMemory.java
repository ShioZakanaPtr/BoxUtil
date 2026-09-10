package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.define.GLWrapper;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector2f;

import java.nio.IntBuffer;

public class BUtil_StaticTrailMemory extends GPUMemoryPool.InternalMemory<BUtil_StaticTrailTrackerObject> {
    private boolean shouldLoopWrite = false;
    private boolean switchTrailSegment = true;
    private boolean canSwitchTrailSegment = false;
    private boolean cannotCut = true;
    private final byte rndID;
    private int totalNode = 0;
    private int piFirstAddress = 0;
    private int intBufAddress = 0;
    private int currPiFirstBufPos = 0;
    private int nextNodePtr = 1;
    private float timer = 0.0f;
    private BUtil_StaticTrailTrackerObject trackerObj;
    private final int[] loopWriteData = new int[12];

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
        final long in_address = this.address();
        this.piFirstAddress = Math.toIntExact(in_address / BUtil_StaticTrailMemoryPool.NODE_BYTE_SIZE);
        this.intBufAddress = Math.toIntExact(in_address >>> 2);
    }

    public void afterAllocatedFromFree(BUtil_StaticTrailTrackerObject meta) {
        this.trackerObj = meta;
    }

    public void afterFree() {
        this.trackerObj = null;
    }

    public int getPiFirstAddress() {
        return this.piFirstAddress;
    }

    public int getCurrPiFirstBufPos() {
        return currPiFirstBufPos;
    }

    public void setCurrPiFirstBufPos(int currPiFirstBufPos) {
        this.currPiFirstBufPos = currPiFirstBufPos;
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
            this.nextNodePtr = 1;
            this.trackerObj.callback().idleReset();
        }
    }

    private int makeUVBits(int bits) {
        bits &= 0x7fffffff;
        return this.switchTrailSegment ? bits | 0x80000000 : bits;
    }

    /**
     * Only subData/persistentMapping.
     *
     * @return any nodes to draw, exclude fill node.
     */
    public int computeData(final BUtil_StaticTrailMemoryPool pool, IntBuffer uploadBuffer, boolean notPersistentMapping, float amount, float elapsedTime) {
        final var callback = this.trackerObj.callback();
        this.trackerObj.tracker().advance(amount, elapsedTime, callback);
        if (callback.shouldDestroyImmediate()) return -1;

        final boolean isDestroyed = callback.shouldDestroy(), isPaused = callback.isPaused();
        if (isDestroyed || isPaused) {
            if (isDestroyed && this.timer <= 0.0f) {
                callback.destroyImmediate();
                return -1;
            }
            if (isPaused && this.canSwitchTrailSegment) {
                this.switchTrailSegment = !this.switchTrailSegment;
                this.canSwitchTrailSegment = false;
            }
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

        final int maxNodes = pool.getMaxFullNodes();
        this.timer = pool.getMaxDur();
        if (this.totalNode < (maxNodes - 2)) this.totalNode++;
        callback.nextFrame(nanDistSq ? 0.0f : (float) Math.sqrt(distSq) / pool.getTrailData().texturePixels);

        final Vector2f facing = callback.getCurrentFacing();
        final int a_position_x = Float.floatToRawIntBits(callback.getCurrentLocation().x),
                a_position_y = Float.floatToRawIntBits(callback.getCurrentLocation().y),
                a_facingVector = (CommonUtil.float16ToShort(facing.y) << 16 & 0xFFFF0000) | (CommonUtil.float16ToShort(facing.x) & 0xFFFF),
                a_nodeColor = callback.pickColor_vec4(),
                a_timeStampRaw = this.encodeTimestamp(elapsedTime),
                a_uv = this.makeUVBits(Float.floatToRawIntBits(callback.getLastUV()));
        int buffOffset = this.nextNodePtr * 6;

        final boolean loopWrite = this.shouldLoopWrite;
        if (notPersistentMapping) uploadBuffer = this.trackerObj.legacyBuf().clear(); else buffOffset += this.intBufAddress;
        if (loopWrite) {
            this.shouldLoopWrite = false;
            final int lastBufPos = maxNodes - 1;
            int lastOffset = lastBufPos * 6;
            if (!notPersistentMapping) lastOffset += this.intBufAddress;
            uploadBuffer.put(lastOffset, a_position_x);
            uploadBuffer.put(lastOffset + 1, a_position_y);
            uploadBuffer.put(lastOffset + 2, a_facingVector);
            uploadBuffer.put(lastOffset + 3, a_nodeColor);
            uploadBuffer.put(lastOffset + 4, a_timeStampRaw);
            uploadBuffer.put(lastOffset + 5, a_uv);
            if (notPersistentMapping) {
                uploadBuffer.position(lastOffset).limit(lastOffset + 6);
                final var l_uploadBuf = uploadBuffer.duplicate();
                final var l_currWritePtr = this.address() + ((long) lastBufPos * BUtil_StaticTrailMemoryPool.NODE_BYTE_SIZE);
                GLWrapper.Buffer.glBufferSubData(pool.getPoolBehavior().glTarget, l_currWritePtr, l_uploadBuf);

            }

            uploadBuffer.put(buffOffset - 6, this.loopWriteData[0]);
            uploadBuffer.put(buffOffset - 5, this.loopWriteData[1]);
            uploadBuffer.put(buffOffset - 4, this.loopWriteData[2]);
            uploadBuffer.put(buffOffset - 3, this.loopWriteData[3]);
            uploadBuffer.put(buffOffset - 2, this.loopWriteData[4]);
            uploadBuffer.put(buffOffset - 1, this.loopWriteData[5]);

            uploadBuffer.put(buffOffset, this.loopWriteData[6]);
            uploadBuffer.put(buffOffset + 1, this.loopWriteData[7]);
            uploadBuffer.put(buffOffset + 2, this.loopWriteData[8]);
            uploadBuffer.put(buffOffset + 3, this.loopWriteData[9]);
            uploadBuffer.put(buffOffset + 4, this.loopWriteData[10]);
            uploadBuffer.put(buffOffset + 5, this.loopWriteData[11]);
            buffOffset += 6;
            this.nextNodePtr++;
        }
        uploadBuffer.put(buffOffset, a_position_x);
        uploadBuffer.put(buffOffset + 1, a_position_y);
        uploadBuffer.put(buffOffset + 2, a_facingVector);
        uploadBuffer.put(buffOffset + 3, a_nodeColor);
        uploadBuffer.put(buffOffset + 4, a_timeStampRaw);
        uploadBuffer.put(buffOffset + 5, a_uv);
        if (notPersistentMapping) {
            if (loopWrite) buffOffset -= 12;
            uploadBuffer.position(buffOffset).limit(buffOffset + (loopWrite ? 18 : 6));
            final var l_uploadBuf = uploadBuffer.duplicate();
            final var l_currWritePtr = loopWrite ? this.address() : this.address() + ((long) this.nextNodePtr * BUtil_StaticTrailMemoryPool.NODE_BYTE_SIZE);
            GLWrapper.Buffer.glBufferSubData(pool.getPoolBehavior().glTarget, l_currWritePtr, l_uploadBuf);
        }

        this.loopWriteData[6] = a_position_x;
        this.loopWriteData[7] = a_position_y;
        this.loopWriteData[8] = a_facingVector;
        this.loopWriteData[9] = a_nodeColor;
        this.loopWriteData[10] = a_timeStampRaw;
        this.loopWriteData[11] = a_uv;

        this.canSwitchTrailSegment = true;
        if (this.nextNodePtr + 3 == maxNodes) {
            this.loopWriteData[0] = a_position_x;
            this.loopWriteData[1] = a_position_y;
            this.loopWriteData[2] = a_facingVector;
            this.loopWriteData[3] = a_nodeColor;
            this.loopWriteData[4] = a_timeStampRaw;
            this.loopWriteData[5] = a_uv;
        }
        this.nextNodePtr++;
        if (this.nextNodePtr + 1 >= maxNodes) {
            this.nextNodePtr = 1;
            this.shouldLoopWrite = true;
        }
        this.cannotCut = false;
        return this.totalNode;
    }

    public void cutTrail(final BUtil_StaticTrailMemoryPool pool, IntBuffer uploadBuffer, boolean notPersistentMapping) {
        if (this.cannotCut) return;
        this.cannotCut = true;

        if (this.canSwitchTrailSegment) {
            this.switchTrailSegment = !this.switchTrailSegment;
            this.canSwitchTrailSegment = false;
        }
        this.shouldLoopWrite = false;
        this.trackerObj.callback().onCutTrail();

        final int writePtr = this.nextNodePtr - 1,
                buffOffset = notPersistentMapping ? 0 : writePtr * 6 + 5 + this.intBufAddress;
        if (notPersistentMapping) uploadBuffer = this.trackerObj.legacyBuf().position(0).limit(1);
        uploadBuffer.put(buffOffset, this.loopWriteData[11] = this.makeUVBits(0));
        if (notPersistentMapping) GLWrapper.Buffer.glBufferSubData(pool.getPoolBehavior().glTarget, this.address() + ((long) writePtr * BUtil_StaticTrailMemoryPool.NODE_BYTE_SIZE) + 20L, uploadBuffer);
    }
}
