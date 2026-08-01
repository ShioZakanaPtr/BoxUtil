package org.boxutil.base.api.resource;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector2f;

/**
 * For example:
 * <pre>{@code
 * final CombatEntityAPI target;
 * StaticTrailTracker tracker = (amount, callback) -> {
 *     if (target == null || target.getHitpoints() <= 0.0f) callback.destroy();
 *     callback.setCurrentLocation(target.getLocation());
 *     callback.setCurrentFacing(target.getVelocity().normalise(new Vector2f()));
 * };
 * }</pre>
 */
@FunctionalInterface
public interface StaticTrailTracker {
    float MINIMAL_VALID_LENGTH_SQ = 0.01f;

    /**
     * @param elapsedTime exclude paused.
     */
    void advance(float amount, float elapsedTime, final ResultCallback result);

    interface ResultCallback {
        float getLength();

        /**
         * Should not change it, just for get the position.
         */
        Vector2f getPreviousFrameLocation();

        Vector2f getCurrentLocation();

        void setCurrentLocation(float x, float y);

        void setCurrentLocation(final Vector2f location);

        Vector2f getCurrentFacing();

        /**
         * Should be a normalized vector.
         */
        void setCurrentFacing(float x, float y);

        /**
         * Should be a normalized vector.
         */
        void setCurrentFacing(final Vector2f facingVector);

        /**
         * To prevent spawns node in current frame.
         */
        void pauseOnce();

        /**
         * <b>REQUIRED</b>, call when the target expires, it waits for each node to finish playing before performing free.
         */
        void destroy();

        /**
         * Same as {@link ResultCallback#destroy()}, but ignores the current state and directly notifies the system to free.
         */
        void destroyImmediate();
    }
}
