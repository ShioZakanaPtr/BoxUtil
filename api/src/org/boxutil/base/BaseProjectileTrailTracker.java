package org.boxutil.base;

import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.TrigUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The standard trail tracker for all the projectiles that BoxUtil built-in autogen static trail system will use it.
 */
@SuppressWarnings("ClassCanBeRecord")
public class BaseProjectileTrailTracker implements StaticTrailTracker {
    protected final DamagingProjectileAPI projectile;
    protected final StaticTrailData trailData;

    public BaseProjectileTrailTracker(@NotNull final DamagingProjectileAPI projectile, @NotNull final StaticTrailData trailData) {
        this.projectile = projectile;
        this.trailData = trailData;
    }

    public void advance(float amount, float elapsedTime, Result callback) {
        if (callback.isExpired()) return;
        final var projectileL = this.projectile;
        if (projectileL.wasRemoved() || projectileL.isExpired() || projectileL.didDamage()) {
            callback.destroy();
            return;
        }

        final float brightness = projectileL.getBrightness();
        final Vector2f loc = projectileL.getLocation();
        if (callback.isNotRecommendedRecordsCurrent(loc) || !Float.isFinite(brightness)) {
            callback.pauseOnce();
            return;
        }
        callback.setCurrentLocation(loc);
        callback.setCurrentAlpha((byte) Math.max(Math.min((int) (brightness * 255.0f), 255), 0));

        final var trailDataL = this.trailData;
        boolean velForForward = trailDataL.velocityForForward;
        final float[] offsetRotatePtr = new float[]{1.0f, 0.0f};
        if (velForForward) {
            final Vector2f velocity = projectileL.getVelocity();
            velForForward = velocity != null && velocity.x != 0.0f && velocity.y != 0.0f;
            if (velForForward) {
                final float velLength = 1.0f / (float) Math.sqrt(velocity.lengthSquared());
                offsetRotatePtr[0] = velocity.x * velLength;
                offsetRotatePtr[1] = velocity.y * velLength;
            }
        }
        if (!velForForward) TrigUtil.approxCosSinF(projectileL.getFacing() * 0.017453292519943295f, offsetRotatePtr);
        callback.setCurrentFacing(offsetRotatePtr[0], offsetRotatePtr[1]);

        final Vector4f spawnOffsetRange = trailDataL.fixedSpawnOffsetRange;
        if (spawnOffsetRange != null) {
            final float rnd = ThreadLocalRandom.current().nextFloat(),
                    offsetX = CalculateUtil.mix(spawnOffsetRange.x, spawnOffsetRange.z, rnd),
                    offsetY = CalculateUtil.mix(spawnOffsetRange.y, spawnOffsetRange.w, rnd);
            callback.getCurrentLocation().x += offsetX * offsetRotatePtr[0] - offsetY * offsetRotatePtr[1];
            callback.getCurrentLocation().y += offsetX * offsetRotatePtr[1] + offsetY * offsetRotatePtr[0];
        }
    }
}
