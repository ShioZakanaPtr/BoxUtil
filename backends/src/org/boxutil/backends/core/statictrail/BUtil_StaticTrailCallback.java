package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.lwjgl.util.vector.Vector2f;

public class BUtil_StaticTrailCallback implements StaticTrailTracker.ResultCallback {
    private boolean triggerPaused = false;
    private boolean triggerDestroy = false;
    private boolean triggerDestroyImmediate = false;
    private float length = 0.0f;
    private final Vector2f lastLoc = new Vector2f(Float.NaN, Float.NaN);
    private final Vector2f location = new Vector2f();
    private final Vector2f facing = new Vector2f(1.0f, 0.0f);

    public float getElapsedLength() {
        return this.length;
    }

    public Vector2f getPreviousRecordsLocation() {
        return this.lastLoc;
    }

    public float getPreviousRecordsDistanceSq(float targetX, float targetY) {
        if (Float.isNaN(this.lastLoc.x) || Float.isNaN(this.lastLoc.y)) return Float.NaN;
        final float x_diff = this.lastLoc.x - targetX, y_diff = this.lastLoc.y - targetY;
        return x_diff * x_diff + y_diff * y_diff;
    }

    public float getPreviousRecordsDistanceSq(final Vector2f targetLocation) {
        return this.getPreviousRecordsDistanceSq(targetLocation.x, targetLocation.y);
    }

    public float getPreviousRecordsDistanceSq() {
        return this.getPreviousRecordsDistanceSq(this.location.x, this.location.y);
    }

    public boolean isNotRecommendedRecordsCurrent(float targetX, float targetY) {
        final float preDist = this.getPreviousRecordsDistanceSq(targetX, targetY);
        return !Float.isNaN(preDist) && preDist < StaticTrailTracker.MINIMAL_VALID_LENGTH_SQ;
    }

    public boolean isNotRecommendedRecordsCurrent(Vector2f targetLocation) {
        return false;
    }

    public boolean isNotRecommendedRecordsCurrent() {
        return false;
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
        this.triggerPaused = true;
    }

    public void destroy() {
        this.triggerDestroy = true;
    }

    public void destroyImmediate() {
        this.triggerDestroyImmediate = true;
    }

    public boolean isExpired() {
        return this.triggerDestroy || this.triggerDestroyImmediate;
    }

    public void nextFrame(final float segLength) {
        this.lastLoc.set(this.location);
        this.triggerPaused = false;
        this.length += segLength;
    }

    public void idleReset() {
        this.length = 0.0f;
        this.lastLoc.set(Float.NaN, Float.NaN);
        this.location.set(0.0f, 0.0f);
        this.facing.set(1.0f, 0.0f);
    }

    public boolean isPaused() {
        return this.triggerPaused;
    }

    public boolean shouldDestroy() {
        return this.triggerDestroy;
    }

    public boolean shouldDestroyImmediate() {
        return this.triggerDestroyImmediate;
    }
}
