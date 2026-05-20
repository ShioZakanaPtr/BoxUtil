package org.boxutil.backends.struct;

import org.boxutil.units.standard.attribute.StaticTrailData;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.concurrent.ReentrantSpinLock;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Deque;
import java.util.function.Function;
import java.util.function.Supplier;

public record BUtil_StaticTrailSysObject(StaticTrailData trailData, Deque<TrackerMemory> trackerQueue) {
    public int hashCode() {
        return this.trailData().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof BUtil_StaticTrailSysObject trail) return this.trailData().equals(trail.trailData());
        else return false;
    }

    public final static class TrackerMemory {
        private boolean isValid = true;
        private int nodeAddress;
        private int totalNode = 0;
        private float timer = 0.0f;
        private final float maxDur;
        private long address;
        private final Function<Float, Vector4f> tracker;

        private TrackerMemory() {
            this.isValid = false;
            this.timer = -1.0f;
            this.maxDur = 0.0f;
            this.tracker = null;
        }

        public TrackerMemory(final StaticTrailData trailData, final Function<Float, Vector4f> tracker) {
            this.maxDur = Math.max(trailData.getFadeInTime(), 0.0f) + Math.max(trailData.getFullTime(), 0.0f) + Math.max(trailData.getFadeOutTime(), 0.0f);
            this.tracker = tracker;
        }

        public void computeData(final FloatBuffer buffer, final ReentrantSpinLock lock, final IntBuffer piFirst, final IntBuffer piCount, final float elapsedTime, final float amount) {
            final Vector4f node = this.tracker.apply(amount);
            if (node == null) {
                this.timer -= amount;
                return;
            }
            this.timer = this.maxDur;
            this.totalNode++;

            final float packingFacingVector = Float.intBitsToFloat(CommonUtil.float16ToShort(node.w) << 16 & 0xFFFF | CommonUtil.float16ToShort(node.z));
            lock.lock();
            buffer.put(node.x);
            buffer.put(node.y);
            buffer.put(packingFacingVector);
            buffer.put(elapsedTime);
            piFirst.put(this.nodeAddress);
            piCount.put(this.totalNode);
            lock.unlock();
        }

    }
}
