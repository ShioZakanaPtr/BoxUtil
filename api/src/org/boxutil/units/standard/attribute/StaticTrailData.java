package org.boxutil.units.standard.attribute;

import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Recommended to use const function for all.
 */
public abstract class StaticTrailData {
    /**
     * <b>MUST BE the const function.</b>
     */
    public abstract String id();
    
    public abstract MaterialData getMaterial();

    /**
     * <b>MUST BE the const function.</b>
     */
    public float getFadeInTime() {
        return 0.1f;
    }

    /**
     * <b>MUST BE the const function.</b>
     */
    public float getFullTime() {
        return 0.4f;
    }

    /**
     * <b>MUST BE the const function.</b>
     */
    public float getFadeOutTime() {
        return 1.0f;
    }

    public float getSizeIn() {
        return 16.0f;
    }

    public float getSizeOut() {
        return 8.0f;
    }

    /**
     * @return returns <code>null</code> for white color with full opacity.
     */
    public @Nullable Vector4f getColorIn() {
        return null;
    }

    /**
     * @return returns <code>null</code> for white color with full opacity.
     */
    public @Nullable Vector4f getColorOut() {
        return null;
    }

    public float getTexturePixels() {
        return 256.0f;
    }

    public float getTextureSpeed() {
        return -256.0f;
    }

    public boolean isRandomUVStartOffset() {
        return true;
    }

    /**
     * Based on facing.
     *
     * @return <code>{minVelocity.xy, maxVelocity.xy}</code>, returns <code>null</code> for none of velocity applied
     */
    public @Nullable Vector4f getVelocityInRange() {
        return null;
    }

    /**
     * Based on facing.
     *
     * @return <code>{minVelocity.xy, maxVelocity.xy}</code>, returns <code>null</code> for none of velocity applied
     */
    public @Nullable Vector4f getVelocityOutRange() {
        return null;
    }

    /**
     * Based on facing.
     *
     * @return <code>{minAngular, maxAngular}</code>, returns <code>null</code> for none of angular applied
     */
    public @Nullable Vector2f getAngularInRange() {
        return null;
    }

    /**
     * Based on facing.
     *
     * @return <code>{minAngular, maxAngular}</code>, returns <code>null</code> for none of angular applied
     */
    public @Nullable Vector2f getAngularOutRange() {
        return null;
    }

    /**
     * <b>Must be const value.</b>
     */
    public boolean isAdditiveBlend() {
        return true;
    }

    /**
     * Based on facing.
     *
     * @return <code>{minOffset.xy, maxOffset.xy}</code>, returns <code>null</code> for none of offset
     */
    public @Nullable Vector4f getFixedSpawnOffsetRange() {
        return null;
    }

    /**
     * Only for system-gen trail, invalid for custom trail.
     *
     * @return use the facing of entity for forward direction when <code>false</code>, use the velocity of entity when <code>true</code>
     */
    public boolean isVelocityForForward() {
        return false;
    }

    /**
     * Only for system-gen trail, invalid for custom trail.
     *
     * @return rendering at {@link CombatEngineLayers#BELOW_INDICATORS_LAYER} when <code>false</code>, at {@link CombatEngineLayers#ABOVE_SHIPS_LAYER} when <code>true</code>
     */
    public boolean isRenderBelowExplosions() {
        return false;
    }

    public final int hashCode() {
        return this.id().hashCode();
    }

    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof StaticTrailData data) return this.id().equals(data.id());
        else return false;
    }

    public final String toString() {
        return this.id();
    }
}
