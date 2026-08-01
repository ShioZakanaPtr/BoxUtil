package org.boxutil.units.standard.attribute;

import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Recommended to use const returns for all.<p>
 * For each object, a fixed amount of video memory is pre‑allocated for storing its rendering data,
 * this amount depends on the trail <b>duration(fadeIn + full + fadeOut)</b> and the trail quality configuration.<p>
 * Therefore, the duration should not be set too long, as doing so may consume excessive video memory.
 */
public abstract class StaticTrailData {
    public final static float MINIMUM_TOTAL_DURATION = 0.0334f;

    /**
     * <b>IMPORTANT</b>, the unique ID in game.<p>
     * <b>MUST</b> be the const returns.<p>
     * <b>CANNOT</b> be empty or contains only {@linkplain Character#isWhitespace(int) white space} codepoints.
     */
    public abstract String id();

    /**
     * @return should be a class or static field, or a variable held by a manager – that is, an object that can be persistently referenced.
     */
    public abstract MaterialData getMaterial();

    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
     */
    public float getFadeInTime() {
        return 0.1f;
    }

    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
     */
    public float getFullTime() {
        return 0.4f;
    }

    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
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

    /**
     * Should be a nonnegative number.
     */
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
     * Based on forward direction.
     *
     * @return <code>{minVelocity.xy, maxVelocity.xy}</code>, returns <code>null</code> if without velocity applied
     */
    public @Nullable Vector4f getVelocityInRange() {
        return null;
    }

    /**
     * Based on forward direction.
     *
     * @return <code>{minVelocity.xy, maxVelocity.xy}</code>, returns <code>null</code> if without velocity applied
     */
    public @Nullable Vector4f getVelocityOutRange() {
        return null;
    }

    /**
     * Based on forward direction.
     *
     * @return <code>{minAngular, maxAngular}</code>, returns <code>null</code> if without angular applied
     */
    public @Nullable Vector2f getAngularInRange() {
        return null;
    }

    /**
     * Based on forward direction.
     *
     * @return <code>{minAngular, maxAngular}</code>, returns <code>null</code> if without angular applied
     */
    public @Nullable Vector2f getAngularOutRange() {
        return null;
    }

    /**
     * <b>MUST be the const returns.</b>
     */
    public boolean isAdditiveBlend() {
        return true;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required to override.</b><p>
     * Based on forward direction.
     *
     * @return <code>{minOffset.xy, maxOffset.xy}</code>, returns <code>null</code> if without offset
     */
    public @Nullable Vector4f getFixedSpawnOffsetRange() {
        return null;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required to override.</b>
     *
     * @return use the facing of entity for forward direction when <code>false</code>, use the velocity of entity when <code>true</code>
     */
    public boolean isVelocityForForward() {
        return false;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required to override.</b>
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
