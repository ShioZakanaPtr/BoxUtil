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
        if (this.projectile.wasRemoved() || this.projectile.isExpired()) {
            callback.destroy();
            return;
        }

        final float brightness = this.projectile.getBrightness();
        final Vector2f loc = this.projectile.getLocation();
        if (callback.isNotRecommendedRecordsCurrent(loc) || !Float.isFinite(brightness)) {
            callback.pauseOnce();
            return;
        }
        callback.setCurrentLocation(loc);
        callback.setCurrentAlpha((byte) Math.max(Math.min((int) (brightness * 255.0f), 255), 0));

        boolean velForForward = this.trailData.velocityForForward;
        float offsetRotateC = 1.0f, offsetRotateS = 0.0f;
        if (velForForward) {
            final Vector2f velocity = this.projectile.getVelocity();
            final float velLength = velocity == null ? 0.0f : velocity.length();
            velForForward = velLength != 0.0f;
            if (velForForward) {
                offsetRotateC = velocity.x / velLength;
                offsetRotateS = velocity.y / velLength;
                callback.setCurrentFacing(offsetRotateC, offsetRotateS);
            }
        }
        if (!velForForward) {
            final float a = (float) Math.toRadians(this.projectile.getFacing());
            offsetRotateC = (float) Math.cos(a);
            offsetRotateS = TrigUtil.sinFormCosRadiansF(offsetRotateC, a);
            callback.setCurrentFacing(offsetRotateC, offsetRotateS);
        }

        final Vector4f spawnOffsetRange = this.trailData.fixedSpawnOffsetRange;
        if (spawnOffsetRange != null) {
            final float rnd = ThreadLocalRandom.current().nextFloat(),
                    offsetX = CalculateUtil.mix(spawnOffsetRange.x, spawnOffsetRange.z, rnd),
                    offsetY = CalculateUtil.mix(spawnOffsetRange.y, spawnOffsetRange.w, rnd);
            callback.getCurrentLocation().x += offsetX * offsetRotateC - offsetY * offsetRotateS;
            callback.getCurrentLocation().y += offsetX * offsetRotateS + offsetY * offsetRotateC;
        }
    }
}
