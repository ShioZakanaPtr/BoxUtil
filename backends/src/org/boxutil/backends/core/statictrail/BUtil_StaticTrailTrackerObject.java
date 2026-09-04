package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;

import java.nio.IntBuffer;

public record BUtil_StaticTrailTrackerObject(byte layerLoc, StaticTrailTracker tracker, BUtil_StaticTrailCallback callback, BUtil_StaticTrailMemoryPool pool, IntBuffer legacyBuf) {}
