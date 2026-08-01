package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.lwjgl.util.vector.Vector2f;

public class BUtil_StaticTrailCallback implements StaticTrailTracker.ResultCallback {
    private boolean triggerPaused = false;
    private boolean triggerDestroy = false;
    private boolean triggerDestroyImmediate = false;
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
        this.triggerPaused = true;
    }

    public void destroy() {
        this.triggerDestroy = true;
    }

    public void destroyImmediate() {
        this.triggerDestroyImmediate = true;
    }

    public float distSq() {
        final float x_diff = this.lastLoc.x - this.location.x, y_diff = this.lastLoc.y - this.location.y;
        return x_diff * x_diff + y_diff * y_diff;
    }

    public void nextFrame(final float segLength) {
        this.lastLoc.set(this.location);
        this.triggerPaused = false;
        this.length += segLength;
    }

    public void idleReset() {
        this.length = 0.0f;
        this.lastLoc.set(0.0f, 0.0f);
        this.location.set(0.0f, 0.0f);
        this.facing.set(0.0f, 0.0f);
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
