package org.boxutil.base.api.resource;

import org.lwjgl.util.vector.Vector2f;

/**
 * For example:
 * <pre>{@code
 * final CombatEntityAPI target;
 * StaticTrailTracker tracker = (amount, elapsedTime, callback) -> {
 *     if (target == null || proj.wasRemoved() || proj.isExpired()) callback.destroy();
 *     final Vector2f loc = target.getLocation();
 *
 *     // // For this tracker have complex or high-overhead logical on execution.
 *     // if (callback.isNotRecommendedRecordsCurrent(loc)) {
 *     //     callback.pauseOnce();
 *     //     return;
 *     // }
 *
 *     callback.setCurrentLocation(target);
 *     callback.setCurrentFacing(target.getVelocity().normalise(new Vector2f()));
 * };
 * }</pre>
 */
@FunctionalInterface
public interface StaticTrailTracker {
    /**
     * Will prevents node spawn when total current-previous location distance square is less than it.
     */
    float MINIMAL_VALID_LENGTH_SQ = 0.01f;

    /**
     * Will not record any nodes when game paused.
     *
     * @param amount frame time since last advance call.
     * @param elapsedTime exclude paused.
     */
    void advance(float amount, float elapsedTime, final ResultCallback callback);

    interface ResultCallback {
        // TBA
        /**
         * @return the total length of path covered since current trail full-segment has spawned.
         */
        float getElapsedLength();

        /**
         * Should not change it, just for get the position.<p>
         * In the first records or all the nodes was playing finished, it is <code>{{@link Float#NaN NaN}, {@link Float#NaN NaN}}</code>.
         */
        Vector2f getPreviousRecordsLocation();

        /**
         * @return {@link Float#NaN NaN} if in the first records or all the nodes was playing finished.
         */
        float getPreviousRecordsDistanceSq(float targetX, float targetY);

        /**
         * @return {@link Float#NaN NaN} if in the first records or all the nodes was playing finished.
         */
        float getPreviousRecordsDistanceSq(final Vector2f targetLocation);

        /**
         * About current location.
         *
         * @return {@link Float#NaN NaN} if in the first records or all the nodes was playing finished.
         */
        float getPreviousRecordsDistanceSq();

        /**
         * Check the current-previous location distance square whether to should not record.
         */
        boolean isNotRecommendedRecordsCurrent(float targetX, float targetY);

        /**
         * Check the current-previous location distance square whether to should not record.
         */
        boolean isNotRecommendedRecordsCurrent(final Vector2f targetLocation);

        /**
         * Check the current-previous location distance square whether to should not record.<p>
         * About current location.
         */
        boolean isNotRecommendedRecordsCurrent();

        /**
         * @return <code>{1.0f, 0.0f}</code> for default.
         */
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
         * Prevents node spawn in current frame, and then next records will be a new trail segment.
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

        /**
         * @return <code>true</code> if called {@link ResultCallback#destroy()} or {@link ResultCallback#destroyImmediate()}, or it was invalid.
         */
        boolean isExpired();
    }
}
