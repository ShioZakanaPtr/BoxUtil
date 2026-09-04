package org.boxutil.base.api.resource;

import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.BaseProjectileTrailTracker;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.manager.CombatRenderingManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

/**
 * For example, in combat:
 * <pre>{@code
 * final CombatEntityAPI target;
 * StaticTrailTracker tracker = (amount, elapsedTime, callback) -> {
 *     if (target.wasRemoved() || target.isExpired()) callback.destroy(); // condition is for reference only
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
 * Or in campaign:
 * <pre>{@code
 * final SectorEntityToken target;
 * final LocationAPI playerLocation;
 * StaticTrailTracker tracker = (amount, elapsedTime, callback) -> {
 *     if (playerLocation != target.getContainingLocation() || target.isExpired()) callback.destroy(); // condition is for reference only
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
 * And then, prepare a unique {@link StaticTrailData} instance and chose a rendering layer,
 * call {@link CombatRenderingManager#addStaticTrail(StaticTrailData, CombatEntityAPI, CombatEngineLayers, StaticTrailTracker)} in combat, add to campaign is similar.<p>
 * For what the tracker the autogen static trail system using: {@link BaseProjectileTrailTracker}
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
     * @param amount frame time since last advance call, exclude paused.
     * @param elapsedTime exclude paused.
     * @param callback use it for adjust the current trail node.
     */
    void advance(float amount, float elapsedTime, final Result callback);

    interface Result {
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
         * The color is represented internally as a <code>byte</code> array,
         * therefore recommended to perform read/write operations using the <code>byte</code> type,
         * <code>int</code> is the next preferred type.
         *
         * @return the direct reference to the internal array in the order <code>{red, green, blue, alpha}</code>.<p>
         *     The default value is opaque white, expressed as <code>{{@link BoxEnum#ONE_COLOR}, {@link BoxEnum#ONE_COLOR}, {@link BoxEnum#ONE_COLOR}, {@link BoxEnum#ONE_COLOR}}</code>.
         */
        byte[] getCurrentColor();

        Color getCurrentColorC();

        Vector4f getCurrentColorV();

        void setCurrentColor(byte r, byte g, byte b, byte a);

        void setCurrentColor(int r, int g, int b, int a);

        void setCurrentColor(float r, float g, float b, float a);

        void setCurrentColor(@NotNull final Color color);

        void setCurrentColor(@NotNull final Vector4f color);

        byte getCurrentAlpha();

        int getCurrentAlphaI();

        float getCurrentAlphaF();

        void setCurrentAlpha(byte a);

        void setCurrentAlpha(int a);

        void setCurrentAlpha(float a);

        /**
         * Prevents node spawn in current frame, and then next records will be a new trail segment.<p>
         * Also can use to 'cut' the trail.
         */
        void pauseOnce();

        /**
         * <b>REQUIRED</b>, call when the target expires or was removed, it waits for all the node to finish playing before performing free, and {@link StaticTrailTracker#advance(float, float, Result)} is still running during this time.
         */
        void destroy();

        /**
         * Same as {@link Result#destroy()}, but ignores the current state and directly notifies the system to free, and remove this tracker immediate.
         */
        void destroyImmediate();

        /**
         * This method is only effective when the trail is destroyed solely via {@link Result#destroy()}.
         *
         * @return <code>true</code> if the trail has entered the destruction process, and the trail memory on vRAM will be released after the trail has completely finished playing.
         */
        boolean isExpired();
    }
}
