package org.boxutil.base.api.resource;

import org.lwjgl.util.vector.Vector2f;

@FunctionalInterface
public interface StaticTrailTracker {
    float MINIMAL_VALID_LENGTH_SQ = 0.01f;

    void advance(final float amount, final ResultCallback result);

    interface ResultCallback {
        float getLength();

        Vector2f getPreviousFrameLocation();

        Vector2f getCurrentLocation();

        void setCurrentLocation(final float x, final float y);

        void setCurrentLocation(final Vector2f location);

        Vector2f getCurrentFacing();

        /**
         * Should be a normalized vector.
         */
        void setCurrentFacing(final float x, final float y);

        /**
         * Should be a normalized vector.
         */
        void setCurrentFacing(final Vector2f facingVector);

        void pauseOnce();

        /**
         * <b>REQUIRED</b> when target was expired.
         */
        void destroy();
    }
}
