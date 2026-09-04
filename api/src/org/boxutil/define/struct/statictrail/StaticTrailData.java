package org.boxutil.define.struct.statictrail;

import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Recommended to use const field for all.<p>
 * For each trail object, a fixed amount of vRAM is pre‑allocated for storing its rendering data,
 * this amount depends on the trail <code>duration(fadeIn + full + fadeOut)</code> and the trail quality configuration.<p>
 * Therefore, the <code>duration</code> should not be set too long, as doing so may consume excessive vRAM.<p>
 * For (M)VP matrix, always is {@link BoxEnum#ENTITY_VANILLA_PRIME_MATRIX}.<p>
 * To reduce overhead and permit special values for custom effects,
 * the trail system does not heavily validate parameters,
 * so you must ensure the correctness of the values they supply.<p>
 *
 * <b>TECHNICAL DETAILS</b><p>
 * When a trail is created, the trail system assumes that after it starts generating,
 * it will continue generating for at least the given <code>duration</code> before stopping (or still running);
 * accordingly, a sufficiently large fixed‑size vRAM block is allocated.<p>
 *
 * Each recorded node in the trail occupies <code>24 bytes</code> of vRAM.
 * In addition to the minimum number of nodes required to form a trail,
 * an extra fixed <b>3</b> fill-nodes are allocated for proper rendering.<p>
 *
 * Based on the configured maximum number of nodes recordable per second(<code>nodePerSeconds</code>),
 * and provided that all components of the <code>duration</code> are <b>non‑negative</b>, the allocated vRAM for one trail is:
 * <pre>
 * <code>24 * (ceil(duration * nodePerSeconds) + 3)  bytes</code>
 * </pre>
 */
public class StaticTrailData {
    public final static float MINIMUM_TOTAL_DURATION = 0.1f;

    public boolean randomUVStartOffset = true;
    public boolean additiveBlend = true;
    public boolean velocityForForward = false;
    public boolean renderBelowExplosions = false;
    /**
     * How many trails have in a typical scene.
     */
    public final short initCapacity;
    public float durFadeIn = 0.1f;
    public float durFull = 0.4f;
    public float durFadeOut = 1.0f;
    public float sizeIn = 16.0f;
    public float sizeOut = 8.0f;
    public float smoothEnds = 1.0f;
    public float texturePixels = 256.0f;
    public float textureSpeed = -256.0f;
    /**
     * The unique ID in game.
     */
    public final @NotNull String id;
    public @NotNull MaterialData material;
    public @Nullable Vector2f angularInRange = null;
    public @Nullable Vector2f angularOutRange = null;
    public @Nullable Vector4f colorIn = null;
    public @Nullable Vector4f colorOut = null;
    public @Nullable Vector4f velocityInRange = null;
    public @Nullable Vector4f velocityOutRange = null;
    public @Nullable Vector4f fixedSpawnOffsetRange = null;

    /**
     * @param id <b>IMPORTANT</b>, the unique ID in game, to define the unique trail style type and memory pool.<p>
     * <b>MUST</b> be the const field.<p>
     * <b>CANNOT</b> be empty or contains only {@linkplain Character#isWhitespace(int) white space} codepoints.
     * @param initCapacity how many trails have in a typical scene, for example: 2048 for the weapon [vulkan]
     */
    public StaticTrailData(@NotNull final String id, short initCapacity) {
        if (id.isBlank()) throw new IllegalArgumentException("The id of static trail data cannot be white space.");
        if (initCapacity < 1) throw new IllegalArgumentException("The init capacity of static trail data must be positive integer.");
        this.id = id;
        this.initCapacity = initCapacity;
        this.material = new MaterialData();
    }

    /**
     * Deep copy constructor.
     *
     * @param id <b>IMPORTANT</b>, the unique ID in game, to define the unique trail style type and memory pool.<p>
     * <b>MUST</b> be the const field.<p>
     * <b>CANNOT</b> be empty or contains only {@linkplain Character#isWhitespace(int) white space} codepoints.
     * @param initCapacity how many trails have in a typical scene, for example: 2048 for the weapon [vulkan]
     */
    public StaticTrailData(@NotNull final StaticTrailData src, @NotNull final String id, short initCapacity) {
        if (id.isBlank()) throw new IllegalArgumentException("The id of static trail data cannot be white space.");
        if (initCapacity < 1) throw new IllegalArgumentException("The init capacity of static trail data must be positive integer.");
        this.id = id;
        this.initCapacity = initCapacity;

        this.randomUVStartOffset = src.randomUVStartOffset;
        this.additiveBlend = src.additiveBlend;
        this.velocityForForward = src.velocityForForward;
        this.renderBelowExplosions = src.renderBelowExplosions;
        this.durFadeIn = src.durFadeIn;
        this.durFull = src.durFull;
        this.durFadeOut = src.durFadeOut;
        this.sizeIn = src.sizeIn;
        this.sizeOut = src.sizeOut;
        this.smoothEnds = src.smoothEnds;
        this.texturePixels = src.texturePixels;
        this.textureSpeed = src.textureSpeed;
        this.material = new MaterialData(src.material);
        if (src.angularInRange != null) this.angularInRange = new Vector2f(src.angularInRange);
        if (src.angularOutRange != null) this.angularOutRange = new Vector2f(src.angularOutRange);
        if (src.colorIn != null) this.colorIn = new Vector4f(src.colorIn);
        if (src.colorOut != null) this.colorOut = new Vector4f(src.colorOut);
        if (src.velocityInRange != null) this.velocityInRange = new Vector4f(src.velocityInRange);
        if (src.velocityOutRange != null) this.velocityOutRange = new Vector4f(src.velocityOutRange);
        if (src.fixedSpawnOffsetRange != null) this.fixedSpawnOffsetRange = new Vector4f(src.fixedSpawnOffsetRange);
    }

    /**
     * Whether to apply a random offset to the UV coordinates for texture sampling, may make the trail more naturally.
     */
    public StaticTrailData setRandomUVStartOffset(boolean randomUVStartOffset) {
        this.randomUVStartOffset = randomUVStartOffset;
        return this;
    }

    /**
     * Use additive blend when <code>true</code>, <code>false</code> for normal blend.
     */
    public StaticTrailData setAdditiveBlend(boolean additiveBlend) {
        this.additiveBlend = additiveBlend;
        return this;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Use the facing of entity for forward direction when <code>false</code>, use the velocity of entity when <code>true</code>.<p>
     * For any stationary projectiles (velocity is a zero vector), also use facing for forward.
     */
    public StaticTrailData setVelocityForForward(boolean velocityForForward) {
        this.velocityForForward = velocityForForward;
        return this;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Rendering at {@link CombatEngineLayers#BELOW_INDICATORS_LAYER} when <code>false</code>, at {@link CombatEngineLayers#ABOVE_SHIPS_LAYER} when <code>true</code>
     */
    public StaticTrailData setRenderBelowExplosions(boolean renderBelowExplosions) {
        this.renderBelowExplosions = renderBelowExplosions;
        return this;
    }

    /**
     * <b>MUST be the const field, and greater than or equal to zero.</b>
     */
    public StaticTrailData setDurFadeIn(float durFadeIn) {
        this.durFadeIn = durFadeIn;
        return this;
    }

    /**
     * <b>MUST be the const field, and greater than or equal to zero.</b>
     */
    public StaticTrailData setDurFull(float durFull) {
        this.durFull = durFull;
        return this;
    }

    /**
     * <b>MUST be the const field, and greater than or equal to zero.</b>
     */
    public StaticTrailData setDurFadeOut(float durFadeOut) {
        this.durFadeOut = durFadeOut;
        return this;
    }

    /**
     * The head width of trail in pixels.
     */
    public StaticTrailData setSizeIn(float sizeIn) {
        this.sizeIn = sizeIn;
        return this;
    }

    /**
     * The tail width of trail in pixels.
     */
    public StaticTrailData setSizeOut(float sizeOut) {
        this.sizeOut = sizeOut;
        return this;
    }

    /**
     * Controls the transparency fade at both ends of a trail to smooth truncation.
     * The fade only affects the first two and last two nodes; larger values stretch the faded area.
     * The effective range also depends on the variable node‑per‑second recording rate.
     */
    public StaticTrailData setSmoothEnds(float duration) {
        this.smoothEnds = duration;
        return this;
    }

    /**
     * <b>MUST be the const field</b>, and should be a nonnegative number.
     */
    public StaticTrailData setTexturePixels(float texturePixels) {
        this.texturePixels = texturePixels;
        return this;
    }

    /**
     * The texture scrolling length in one seconds.<p>
     * Use negative value will look like away from the spawn location.
     */
    public StaticTrailData setTextureSpeed(float textureSpeed) {
        this.textureSpeed = textureSpeed;
        return this;
    }

    /**
     * For cull face state, always used {@link BoxEnum#MATERIAL_CULL_DISABLED}.
     */
    public StaticTrailData setMaterial(@NotNull MaterialData material) {
        this.material = material;
        return this;
    }

    /**
     * Based on forward direction.<p>
     * <code>{minAngular, maxAngular}</code>, <code>null</code> if without angular applied
     */
    public StaticTrailData setAngularInRange(@Nullable Vector2f angularInRange) {
        this.angularInRange = angularInRange;
        return this;
    }

    /**
     * Based on forward direction.<p>
     * <code>{minAngular, maxAngular}</code>, <code>null</code> if without angular applied
     */
    public StaticTrailData setAngularOutRange(@Nullable Vector2f angularOutRange) {
        this.angularOutRange = angularOutRange;
        return this;
    }

    /**
     * <code>null</code> for white color with full opacity.
     */
    public StaticTrailData setColorIn(@Nullable Vector4f colorIn) {
        this.colorIn = colorIn;
        return this;
    }

    /**
     * <code>null</code> for white color with full opacity.
     */
    public StaticTrailData setColorOut(@Nullable Vector4f colorOut) {
        this.colorOut = colorOut;
        return this;
    }

    /**
     * Based on forward direction.<p>
     * <code>{minVelocity.xy, maxVelocity.xy}</code>, <code>null</code> if without velocity applied
     */
    public StaticTrailData setVelocityInRange(@Nullable Vector4f velocityInRange) {
        this.velocityInRange = velocityInRange;
        return this;
    }

    /**
     * Based on forward direction.<p>
     * <code>{minVelocity.xy, maxVelocity.xy}</code>, <code>null</code> if without velocity applied
     */
    public StaticTrailData setVelocityOutRange(@Nullable Vector4f velocityOutRange) {
        this.velocityOutRange = velocityOutRange;
        return this;
    }

    /**
     * <b>Only for system-gen trail, invalid for custom trail, not required.</b><p>
     * Based on forward direction.<p>
     * <code>{minOffset.xy, maxOffset.xy}</code>, <code>null</code> if without offset
     */
    public StaticTrailData setFixedSpawnOffsetRange(@Nullable Vector4f fixedSpawnOffsetRange) {
        this.fixedSpawnOffsetRange = fixedSpawnOffsetRange;
        return this;
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
