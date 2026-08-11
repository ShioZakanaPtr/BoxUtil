package org.boxutil.define.struct.statictrail;

import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Recommended to use const returns for all.<p>
 * For each object, a fixed amount of video memory is pre‑allocated for storing its rendering data,
 * this amount depends on the trail <b>duration(fadeIn + full + fadeOut)</b> and the trail quality configuration.<p>
 * Therefore, the duration should not be set too long, as doing so may consume excessive video memory.<p>
 * For VP matrix, always is {@link BoxEnum#ENTITY_VANILLA_PRIME_MATRIX}.
 */
public class StaticTrailData {
    public final static float MINIMUM_TOTAL_DURATION = 0.0334f;

    public boolean randomUVStartOffset = true;
    public boolean additiveBlend = true;
    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Use the facing of entity for forward direction when <code>false</code>, use the velocity of entity when <code>true</code>.<p>
     * For any stationary projectiles (velocity is a zero vector), also use facing for forward.
     */
    public boolean velocityForForward = false;
    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Rendering at {@link CombatEngineLayers#BELOW_INDICATORS_LAYER} when <code>false</code>, at {@link CombatEngineLayers#ABOVE_SHIPS_LAYER} when <code>true</code>
     */
    public boolean renderBelowExplosions = false;
    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
     */
    public float durFadeIn = 0.1f;
    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
     */
    public float durFull = 0.4f;
    /**
     * <b>MUST be the const returns, and greater than or equal to zero.</b>
     */
    public float durFadeOut = 1.0f;
    public float sizeIn = 16.0f;
    public float sizeOut = 8.0f;
    /**
     * Should be a nonnegative number.
     */
    public float texturePixels = 256.0f;
    public float textureSpeed = -256.0f;
    public final @NotNull String id;
    /**
     * Should be a class or static field, or a variable held by a manager – that is, an object that can be persistently referenced.<p>
     * For cull face state, always is {@link BoxEnum#MATERIAL_CULL_DISABLED}.
     */
    public @NotNull MaterialData material = new MaterialData();
    /**
     * <code>null</code> for white color with full opacity.
     */
    public @Nullable Vector4f colorIn = null;
    /**
     * <code>null</code> for white color with full opacity.
     */
    public @Nullable Vector4f colorOut = null;
    /**
     * Based on forward direction.<p>
     * <code>{minVelocity.xy, maxVelocity.xy}</code>, <code>null</code> if without velocity applied
     */
    public @Nullable Vector4f velocityInRange = null;
    /**
     * Based on forward direction.<p>
     * <code>{minVelocity.xy, maxVelocity.xy}</code>, <code>null</code> if without velocity applied
     */
    public @Nullable Vector4f velocityOutRange = null;
    /**
     * Based on forward direction.<p>
     * <code>{minAngular, maxAngular}</code>, <code>null</code> if without angular applied
     */
    public @Nullable Vector2f angularInRange = null;
    /**
     * Based on forward direction.<p>
     * <code>{minAngular, maxAngular}</code>, <code>null</code> if without angular applied
     */
    public @Nullable Vector2f angularOutRange = null;
    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Based on forward direction.<p>
     * <code>{minOffset.xy, maxOffset.xy}</code>, <code>null</code> if without offset
     */
    public @Nullable Vector4f fixedSpawnOffsetRange = null;

    /**
     * @param id <b>IMPORTANT</b>, the unique ID in game.<p>
     * <b>MUST</b> be the const field.<p>
     * <b>CANNOT</b> be empty or contains only {@linkplain Character#isWhitespace(int) white space} codepoints.
     */
    public StaticTrailData(@NotNull final String id) {
        this.id = id;
    }

    public final int hashCode() {
        return this.id.hashCode();
    }

    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof StaticTrailData data) return this.id.equals(data.id);
        else return false;
    }

    public String toString() {
        return this.id;
    }
}
