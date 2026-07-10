package org.boxutil.backends.units;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.units.standard.attribute.StaticTrailData;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.concurrent.ReentrantSpinLock;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Deque;
import java.util.function.Function;

public record BUtil_StaticTrailSysObject(StaticTrailData trailData, float maxDur, Deque<TrackerMemory> trackerQueue) {
    public int hashCode() {
        return this.trailData().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof BUtil_StaticTrailSysObject trail) return this.trailData().equals(trail.trailData());
        else return false;
    }

    private final static class _TrailCallback implements StaticTrailTracker.ResultCallback {
        private boolean togglePaused = false;
        private boolean toggleDestroy = false;
        private float length = 0.0f;
        private final Vector2f lastLoc = new Vector2f();
        private final Vector2f location = new Vector2f();
        private final Vector2f facing = new Vector2f();

        public float getLength() {
            return this.length;
        }

        public Vector2f getPreviousFrameLocation() {
            return this.lastLoc;
        }

        public Vector2f getCurrentLocation() {
            return this.location;
        }

        public void setCurrentLocation(final float x, final float y) {
            this.location.set(x, y);
        }

        public void setCurrentLocation(final Vector2f location) {
            this.setCurrentLocation(location.x, location.y);
        }

        public Vector2f getCurrentFacing() {
            return this.facing;
        }

        public void setCurrentFacing(final float x, final float y) {
            this.facing.set(x, y);
        }

        public void setCurrentFacing(final Vector2f facingVector) {
            this.setCurrentFacing(facingVector.x, facingVector.y);
        }

        public void pauseOnce() {
            this.togglePaused = true;
        }

        public void destroy() {
            this.toggleDestroy = true;
        }

        private float _distSq() {
            final float x_diff = this.lastLoc.x - this.location.x, y_diff = this.lastLoc.y - this.location.y;
            return x_diff * x_diff + y_diff * y_diff;
        }

        private void _nextFrame(final float segLength) {
            this.lastLoc.set(this.location);
            this.togglePaused = false;
            this.length += segLength;
        }

        private boolean _isPaused() {
            return this.togglePaused;
        }

        private boolean _isDestroyed() {
            return this.toggleDestroy;
        }
    }

    public final static class TrackerMemory {
        private boolean isInvalid = false;
        private final byte rndID;
        private int nodeAddress;
        private int totalNode = 0;
        private float timer = 0.0f;
        private final float maxDur;
        private long address;
        private final Vector2f uploadLoc = new Vector2f();
        private final _TrailCallback callback = new _TrailCallback();
        private final StaticTrailTracker tracker;
        private final FloatBuffer uploadMem;

        private TrackerMemory() {
            this.rndID = 0;
            this.isInvalid = true;
            this.timer = -1.0f;
            this.maxDur = 0.0f;
            this.tracker = null;
            this.uploadMem = null;
        }

        public TrackerMemory(final StaticTrailData trailData, final StaticTrailTracker tracker) {
            int xorRnd = tracker.hashCode();
            xorRnd ^= xorRnd << 13;
            xorRnd ^= xorRnd >>> 17;
            xorRnd ^= xorRnd << 5;
            this.rndID = (byte) (xorRnd & 7);
            this.maxDur = Math.max(trailData.getFadeInTime(), 0.0f) + Math.max(trailData.getFullTime(), 0.0f) + Math.max(trailData.getFadeOutTime(), 0.0f);
            this.tracker = tracker;
            this.uploadMem = BufferUtils.createFloatBuffer(4);
            this.uploadMem.position(0);
            this.uploadMem.limit(4);
        }

        private static float _calculateMix(final float a, final float b) {
            return (a != b) ? CalculateUtil.mix(a, b, (float) Math.random()) : a;
        }

        // may be something wrong in the first/second frame under very high fps(alien-tech computer), but no matter
        private float _encodeTimestamp(final float elapsedTime) {
            return (this.rndID << 29) | (Float.floatToRawIntBits(elapsedTime) & 0x1fffffff);
        }

        private void _framePass(final float amount) {
            this.timer -= amount;
            this.uploadMem.limit(0);
        }

        public void computeData(final ReentrantSpinLock lock, final IntBuffer piFirst, final IntBuffer piCount, final StaticTrailData trailData, final float elapsedTime, final float amount) {
            if (this.isInvalid) return;
            this.tracker.advance(amount, this.callback);

            final boolean isDestroyed = this.callback._isDestroyed(), isPaused = this.callback._isPaused();
            if (isDestroyed || isPaused) {
                if (isDestroyed && this.timer < 0.0f) this.isInvalid = true;
                this._framePass(amount);
                return;
            }
            final float distSq = this.callback._distSq();
            if (distSq < StaticTrailTracker.MINIMAL_VALID_LENGTH_SQ) {
                this._framePass(amount);
                return;
            }
            this.timer = this.maxDur;
            this.totalNode++;

            final Vector2f facing = this.callback.getCurrentFacing();
            final Vector4f spawnOffset = trailData.getFixedSpawnOffsetRange();
            this.uploadLoc.set(this.callback.getCurrentLocation());
            if (spawnOffset != null) {
                final float mixX = _calculateMix(spawnOffset.x, spawnOffset.z),
                        mixY = _calculateMix(spawnOffset.y, spawnOffset.w);
                this.uploadLoc.x += mixX * facing.x - mixY * facing.y;
                this.uploadLoc.y += mixX * facing.y + mixY * facing.x;
            }

            this.callback._nextFrame((float) Math.sqrt(distSq));
            final float packingFacingVector = Float.intBitsToFloat(CommonUtil.float16ToShort(facing.y) << 16 & 0xFFFF | CommonUtil.float16ToShort(facing.x));
            this.uploadMem.put(0, this.uploadLoc.x);
            this.uploadMem.put(1, this.uploadLoc.y);
            this.uploadMem.put(2, packingFacingVector);
            this.uploadMem.put(3, this._encodeTimestamp(elapsedTime));
            this.uploadMem.put(4, this.callback.getLength());
            this.uploadMem.position(0);
            this.uploadMem.limit(4);
        }

        public boolean isInvalid() {
            return this.isInvalid;
        }
    }
}
