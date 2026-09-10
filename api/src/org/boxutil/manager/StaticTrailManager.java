package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.apache.log4j.Logger;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.container.Obj2ObjRHMap;
import org.boxutil.util.container.ObjRHSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * For how to simply add your trail on projectile or missile:
 * <pre>
 * {@code
 * public final class YourModPlugin extends BaseModPlugin {
 *     public void onApplicationLoad() {
 *         BoxUtilModPlugin.initPre();
 *         // StaticTrailManager.putCustomTracker("ABCD_efgh_tracker", ABCD_SomeTracker::new); // if you hava any custom tracker
 *         StaticTrailManager.loadTrailData("data/config/modFiles/ABCD_trail_data.csv"); // also the path can be anywhere
 *     }
 * }
 * }
 * </pre>
 */
@SuppressWarnings("UnusedReturnValue")
public final class StaticTrailManager {
    private final static Map<String, Set<String>> PROJ_TRAIL = new Obj2ObjRHMap<>(128);
    private final static Map<String, StaticTrailData> TRAILS = new Obj2ObjRHMap<>(128);
    private final static Map<String, BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker>> CUSTOM_TRACKER = new Obj2ObjRHMap<>(128);
    private final static Set<String> _CACHED_PATH = new ObjRHSet<>(32);
    private final static Set<String> _CACHED_MAGIC_LIB_LAYOUT_PATH = new ObjRHSet<>(32);

    private final static Logger _LOG = Global.getLogger(StaticTrailManager.class);

    /**
     * @param id the id of <b>projectile spec</b>.
     */
    public static boolean haveTrailDataConfig(final String id) {
        return PROJ_TRAIL.containsKey(id);
    }

    /**
     * @param id the id of <b>projectile spec</b>.
     */
    public static Set<String> getTrailDataConfig(final String id) {
        return PROJ_TRAIL.get(id);
    }

    /**
     * @param projID the id of <b>projectile spec</b>.
     * @param trailID same as {@link StaticTrailData#id}.
     *
     * @return <code>true</code> if not contain this trail data.
     */
    public static boolean putTrailDataConfig(@NotNull final String projID, final String trailID) {
        if (projID.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        return PROJ_TRAIL.computeIfAbsent(projID, k -> new ObjRHSet<>(2)).add(trailID);
    }

    /**
     * @param id the id of <b>projectile spec</b>.
     */
    public static Set<String> removeTrailDataConfig(final String id) {
        return PROJ_TRAIL.remove(id);
    }

    /**
     * @param id same as {@link StaticTrailData#id}.
     */
    public static boolean haveTrailData(final String id) {
        return TRAILS.containsKey(id);
    }

    /**
     * @param id same as {@link StaticTrailData#id}.
     */
    public static StaticTrailData getTrailData(final String id) {
        return TRAILS.get(id);
    }

    /**
     * @return see {@link Map#put(Object, Object)}
     */
    public static StaticTrailData putTrailData(@NotNull final StaticTrailData trailData) {
        if (trailData.id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        return TRAILS.put(trailData.id, trailData);
    }

    /**
     * @param id same as {@link StaticTrailData#id}.
     */
    public static StaticTrailData removeTrailData(final String id) {
        return TRAILS.remove(id);
    }

    public static boolean haveCustomTracker(final String trackerID) {
        return CUSTOM_TRACKER.containsKey(trackerID);
    }

    public static BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker> getCustomTracker(final String trackerID) {
        return CUSTOM_TRACKER.get(trackerID);
    }

    /**
     * @param trackerID the unique tracker ID for your mod's, for example <code>ABCD_efgh_tracker</code>;
     * @param customTracker a standard trail tracker for all the projectiles that BoxUtil built-in autogen static trail system using {@link org.boxutil.base.BaseProjectileTrailTracker}.<p>
     *                      must be returned a <code>NotNull</code> tracker, for example <code>BaseProjectileTrailTracker::new</code>
     *
     * @return see {@link Map#put(Object, Object)}
     */
    public static BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker> putCustomTracker(final String trackerID, @NotNull final BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker> customTracker) {
        if (trackerID.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        return CUSTOM_TRACKER.put(trackerID, customTracker);
    }

    public static BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker> removeCustomTracker(final String trackerID) {
        return CUSTOM_TRACKER.remove(trackerID);
    }

    private static boolean _invalidDuration(float fadeIn, float full, float fadeOut) {
        return fadeIn < 0.0f || full < 0.0f || fadeOut < 0.0f || (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) || fadeIn + full + fadeOut < StaticTrailData.MINIMUM_TOTAL_DURATION;
    }

    private static boolean _invalidWidth(float sizeIn, float sizeOut) {
        return sizeIn == 0.0f || sizeOut == 0.0f;
    }

    private static @Nullable <V> V writeVec(final JSONObject json, final String key, final BiConsumer<String, V> fun, final Supplier<V> make) {
        final String tmpStr = json.optString(key);
        V result = null;
        if (!tmpStr.isBlank()) {
            result = make.get();
            fun.accept(tmpStr, result);
        }
        return result;
    }

    /**
     * For example: {@code StaticTrailManager.loadTrailData("data/config/modFiles/BUtil_trail_data.csv");}
     */
    public static void loadTrailData(@NotNull final String path) {
        _CACHED_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;

            String diffusePath, normalPath, complexPath, emissivePath, tangentPath;
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);
                final String projID = objData.optString("proj_id"), trailID = objData.optString("trail_id");
                if (BUtil_MiscUtil.csvSkipAnnotationID(projID)) continue;
                if (projID.isBlank() || trailID.isBlank()) {
                    _LOG.warn("'BoxUtil' static trail csv data have empty id or trail-id at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }

                final boolean isExistingTrail = objData.optBoolean("use_existing_trail", false);
                if (isExistingTrail) {
                    PROJ_TRAIL.computeIfAbsent(projID, key -> new ObjRHSet<>(2)).add(trailID);
                    continue;
                }

                diffusePath = objData.optString("diffuse_path");
                emissivePath = objData.optString("emissive_path");
                if (diffusePath.isBlank() && emissivePath.isBlank()) {
                    _LOG.warn("'BoxUtil' static trail csv data have empty diffuse map and empty emissive map at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }

                normalPath = objData.optString("normal_path");
                complexPath = objData.optString("complex_path");
                tangentPath = objData.optString("tangent_path");

                final short initCapacity = (short) objData.optInt("init_capacity", 1024);
                final float fadeIn = (float) objData.optDouble("fade_in", 0.1d),
                        full = (float) objData.optDouble("full", 0.4d),
                        fadeOut = (float) objData.optDouble("fade_out", 1.0d),
                        sizeIn = (float) objData.optDouble("size_in", 16.0d),
                        sizeOut = (float) objData.optDouble("size_out", 8.0d),
                        smoothEnds = (float) objData.optDouble("smooth_ends", 1.0d),
                        texPixels = (float) objData.optDouble("tex_pixels", 256.0d),
                        texSpeed = (float) objData.optDouble("tex_speed", -256.0d);
                if (_invalidDuration(fadeIn, full, fadeOut)) {
                    _LOG.warn("'BoxUtil' static trail csv data have illegal duration at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }
                if (_invalidWidth(sizeIn, sizeOut)) {
                    _LOG.warn("'BoxUtil' static trail csv data have zero size at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }
                final boolean randomUV = objData.optBoolean("random_start_uv", true),
                        additiveBlend = objData.optBoolean("additive_blend", true),
                        velocityForward = objData.optBoolean("velocity_for_forward", false),
                        renderBelowExplosions = objData.optBoolean("render_below_explosions", false);
                final Vector4f colorIn = writeVec(objData, "color_in", BUtil_MiscUtil::getVec4Color, Vector4f::new),
                        colorOut = writeVec(objData, "color_out", BUtil_MiscUtil::getVec4Color, Vector4f::new),
                        velIn = writeVec(objData, "velocity_in_range", BUtil_MiscUtil::getVec4, Vector4f::new),
                        velOut = writeVec(objData, "velocity_out_range", BUtil_MiscUtil::getVec4, Vector4f::new),
                        spawnOffset = writeVec(objData, "spawn_offset_range", BUtil_MiscUtil::getVec4, Vector4f::new);
                final Vector2f angularIn = writeVec(objData, "angular_in_range", BUtil_MiscUtil::getVec2, Vector2f::new),
                        angularOut = writeVec(objData, "angular_out_range", BUtil_MiscUtil::getVec2, Vector2f::new);
                final String customTrackerID = objData.optString("custom_tracker_id", null);

                final StaticTrailData data = new StaticTrailData(trailID, initCapacity)
                        .setDurFadeIn(fadeIn)
                        .setDurFull(full)
                        .setDurFadeOut(fadeOut)
                        .setSizeIn(sizeIn)
                        .setSizeOut(sizeOut)
                        .setSmoothEnds(smoothEnds)
                        .setColorIn(colorIn)
                        .setColorOut(colorOut)
                        .setTexturePixels(texPixels)
                        .setTextureSpeed(texSpeed)
                        .setRandomUVStartOffset(randomUV)
                        .setVelocityInRange(velIn)
                        .setVelocityOutRange(velOut)
                        .setAngularInRange(angularIn)
                        .setAngularOutRange(angularOut)
                        .setAdditiveBlend(additiveBlend)
                        .setFixedSpawnOffsetRange(spawnOffset)
                        .setVelocityForForward(velocityForward)
                        .setRenderBelowExplosions(renderBelowExplosions)
                        .setCustomTrackerID(customTrackerID);
                final boolean rotateTex = objData.optBoolean("is_vertical_tex", false);
                final MaterialData material = data.material;
                material.setDiffuse(diffusePath.isBlank() ? 0 : BUtil_MiscUtil.tryTexture(diffusePath, rotateTex, TextureManager::tryTexture));
                if (!normalPath.isBlank()) material.setNormal(BUtil_MiscUtil.tryTexture(normalPath, TextureManager::tryTextureChannel3));
                if (!complexPath.isBlank()) material.setComplex(BUtil_MiscUtil.tryTexture(complexPath, TextureManager::tryTextureChannel3));
                material.setEmissive(emissivePath.isBlank() ? 0 : BUtil_MiscUtil.tryTexture(emissivePath, rotateTex, TextureManager::tryTexture));
                if (!tangentPath.isBlank()) material.setTangent(BUtil_MiscUtil.tryTangentTexture(tangentPath, objData.optBoolean("is_tangent_angle_map", true), true, false));
                material.setGlowPower((float) objData.optDouble("glow_power", 1.0d));

                PROJ_TRAIL.computeIfAbsent(projID, key -> new ObjRHSet<>(2)).add(trailID);
                TRAILS.put(trailID, data);
            }
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' static trail csv data loading failed at: '" + path + "': ", e);
        }
    }

    /**
     * For example: {@code StaticTrailManager.loadTrailData("data/config/modFiles/BUtil_trail_data_magiclib.csv");}
     */
    public static void loadMagicLibLayoutTrailData(@NotNull final String path) {
        _CACHED_MAGIC_LIB_LAYOUT_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;

            String diffuseKey;
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);
                final String projID = objData.optString("projectile"), trailID = objData.optString("trail");
                if (BUtil_MiscUtil.csvSkipAnnotationID(projID)) continue;
                if (projID.isBlank() || trailID.isBlank()) {
                    _LOG.warn("'BoxUtil' static trail csv data have empty id or trail-id at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }

                diffuseKey = objData.optString("sprite");
                if (diffuseKey.isBlank()) {
                    _LOG.warn("'BoxUtil' static trail csv data have empty sprite at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }

                final float fadeIn = (float) objData.optDouble("fadeIn", 0.1d),
                        full = (float) objData.optDouble("duration", 0.4d),
                        fadeOut = (float) objData.optDouble("fadeOut", 1.0d),
                        sizeIn = (float) objData.optDouble("sizeIn", 16.0d),
                        sizeOut = (float) objData.optDouble("sizeOut", 8.0d),
                        texPixels = (float) objData.optDouble("textLength", 256.0d),
                        texSpeed = (float) objData.optDouble("textScroll", -256.0d),
                        opacityMultiply = (float) objData.optDouble("opacity", 1.0d);
                if (_invalidDuration(fadeIn, full, fadeOut)) {
                    _LOG.warn("'BoxUtil' static trail csv data have illegal duration at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }
                if (_invalidWidth(sizeIn, sizeOut)) {
                    _LOG.warn("'BoxUtil' static trail csv data have zero size at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }
                if (opacityMultiply <= 0.0f) {
                    _LOG.warn("'BoxUtil' static trail csv data have completely transparent trail at id '" + trailID + "' in: '" + path + "'.");
                    continue;
                }
                final boolean randomUV = objData.optBoolean("randomTextureOffset", true),
                        additiveBlend = objData.optBoolean("additive", true),
                        velocityForward = objData.optBoolean("angleAdjustment", false),
                        renderBelowExplosions = objData.optBoolean("renderBelowExplosions", false);
                final Vector4f colorIn = writeVec(objData, "colorIn", BUtil_MiscUtil::getVec4Color, Vector4f::new),
                        colorOut = writeVec(objData, "colorOut", BUtil_MiscUtil::getVec4Color, Vector4f::new),
                        velIn = new Vector4f(), velOut = new Vector4f(), spawnOffset = new Vector4f();
                final Vector2f angularIn = new Vector2f(), angularOut = new Vector2f();

                final float distance = (float) objData.optDouble("distance", 0.0d),
                        dispersion = (float) objData.optDouble("dispersion", 0.0d),
                        velocityIn = (float) objData.optDouble("velocityIn", 0.0d),
                        velocityOut = (float) objData.optDouble("velocityOut", 0.0d),
                        randomVelocity = (float) objData.optDouble("randomVelocity", 0.0d),
                        rotationIn = (float) objData.optDouble("rotationIn", 0.0d),
                        rotationOut = (float) objData.optDouble("rotationOut", 0.0d);
                final boolean randomRotation_not = !objData.optBoolean("randomRotation", false);
                if (colorIn != null) colorIn.w *= opacityMultiply;
                if (colorOut != null) colorOut.w *= opacityMultiply;
                spawnOffset.x = spawnOffset.z = -distance;

                velIn.x = velIn.z = -velocityIn;
                velIn.z += velIn.z * randomVelocity;
                velOut.x = velOut.z = -velocityOut;
                velOut.z += velOut.z * randomVelocity;
                velOut.x -= dispersion;
                velOut.y = -dispersion;
                velOut.z += dispersion;
                velOut.w = dispersion;

                if (randomRotation_not) angularIn.x = rotationIn;
                angularIn.y = rotationIn;
                if (randomRotation_not) angularOut.x = rotationOut;
                angularOut.y = rotationOut;

                final StaticTrailData data = new StaticTrailData(trailID, (short) 1024)
                        .setDurFadeIn(fadeIn)
                        .setDurFull(full)
                        .setDurFadeOut(fadeOut)
                        .setSizeIn(sizeIn)
                        .setSizeOut(sizeOut)
                        .setColorIn(colorIn)
                        .setColorOut(colorOut)
                        .setTexturePixels(texPixels)
                        .setTextureSpeed(texSpeed)
                        .setRandomUVStartOffset(randomUV)
                        .setVelocityInRange(velIn)
                        .setVelocityOutRange(velOut)
                        .setAngularInRange(angularIn)
                        .setAngularOutRange(angularOut)
                        .setAdditiveBlend(additiveBlend)
                        .setFixedSpawnOffsetRange(spawnOffset)
                        .setVelocityForForward(velocityForward)
                        .setRenderBelowExplosions(renderBelowExplosions);
                final MaterialData material = data.material;
                final String sprite = Global.getSettings().getSpriteName("fx", diffuseKey);
                material.setDiffuse((sprite == null || sprite.isBlank()) ? 0 : BUtil_MiscUtil.tryTexture(sprite, true, TextureManager::tryTexture));

                PROJ_TRAIL.computeIfAbsent(projID, key -> new ObjRHSet<>(2)).add(trailID);
                TRAILS.put(trailID, data);
            }
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' static trail csv data loading failed at: '" + path + "': ", e);
        }
    }

    /**
     * @param projID the id of <b>projectile spec</b>.
     */
    public static void registerTrail(final String projID, final StaticTrailData trailData) {
        if (projID.isBlank()) throw new IllegalArgumentException("Illegal projID: a white space");
        PROJ_TRAIL.computeIfAbsent(projID, key -> new ObjRHSet<>(2)).add(trailData.id);
        TRAILS.put(trailData.id, trailData);
    }

    /**
     * @param trackerID the unique tracker ID for your mod's, for example <code>ABCD_efgh_tracker</code>;
     * @param customTracker a standard trail tracker for all the projectiles that BoxUtil built-in autogen static trail system using {@link org.boxutil.base.BaseProjectileTrailTracker}.<p>
     *                      must be returned a <code>NotNull</code> tracker, for example <code>BaseProjectileTrailTracker::new</code>
     */
    public static void registerCustomTracker(final String trackerID, @NotNull final BiFunction<DamagingProjectileAPI, StaticTrailData, StaticTrailTracker> customTracker) {
        if (trackerID.isBlank()) throw new IllegalArgumentException("Illegal trackerID: a white space");
        CUSTOM_TRACKER.put(trackerID, customTracker);
    }

    /**
     * Will overwrite all the data or add ones with (new or existed)id, but never clear them.<p>
     * Must be only call it by game dev mode F8 reload, do not use it in mod.<p>
     * Normally, only effective after scene switched, for example end the mission back to title.
     */
    public static void _devModeReload() {
        for (String path : _CACHED_PATH) loadTrailData(path);
        for (String path : _CACHED_MAGIC_LIB_LAYOUT_PATH) loadMagicLibLayoutTrailData(path);
    }

    private StaticTrailManager() {}
}
