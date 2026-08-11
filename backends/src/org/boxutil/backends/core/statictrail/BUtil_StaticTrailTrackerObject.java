package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;

public record BUtil_StaticTrailTrackerObject(byte layerLoc, StaticTrailTracker tracker, BUtil_StaticTrailCallback callback) {
    public int hashCode() {
        int result = 31 + this.layerLoc;
        result = 31 * result + this.tracker.hashCode();
        return 31 * result + this.callback.hashCode();
    }
}
