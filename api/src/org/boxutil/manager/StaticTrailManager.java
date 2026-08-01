package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.units.standard.attribute.StaticTrailData;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.util.CommonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
public final class StaticTrailManager {
    private final static HashMap<String, HashSet<StaticTrailData>> _PROJ_TRAIL = new HashMap<>(128);
    private final static Set<String> _CACHED_PATH = new HashSet<>(32);
    private final static Set<String> _CACHED_MAGIC_LIB_LAYOUT_PATH = new HashSet<>(32);

    private final static Logger _LOG = Global.getLogger(StaticTrailManager.class);

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static boolean haveTrailData(final String id) {
        return _PROJ_TRAIL.containsKey(id);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static HashSet<StaticTrailData> getTrailData(final String id) {
        return _PROJ_TRAIL.get(id);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     *
     * @return <code>true</code> if not contain this trail data.
     */
    public static boolean putTrailData(final String id, final StaticTrailData trailData) {
        return _PROJ_TRAIL.computeIfAbsent(id, k -> new HashSet<>(2)).add(trailData);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static HashSet<StaticTrailData> removeTrailData(final String id) {
        return _PROJ_TRAIL.remove(id);
    }

    private static boolean _invalidDuration(float fadeIn, float full, float fadeOut) {
        return fadeIn < 0.0f || full < 0.0f || fadeOut < 0.0f || (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) || fadeIn + full + fadeOut < StaticTrailData.MINIMUM_TOTAL_DURATION;
    }

    private static boolean _invalidWidth(float sizeIn, float sizeOut) {
        return sizeIn == 0.0f || sizeOut == 0.0f;
    }

    /**
     * For example: {@code StaticTrailManager.loadTrailData("data/config/modFiles/BUtil_trail_data.csv");}
     */
    public static void loadTrailData(final String path) {
        _CACHED_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;

            String diffusePath, normalPath, complexPath, emissivePath, tangentPath;
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);
                final String projID = objData.optString("proj_id"), trailID = objData.optString("trail_id");
                if (projID.isBlank() || trailID.isBlank()) {
                    _LOG.warn("'BoxUtil' static trail csv data have empty id or trail-id at lines '" + i + "' in: '" + path + "'.");
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

                final float fadeIn = (float) objData.optDouble("fade_in", 0.1d),
                        full = (float) objData.optDouble("full", 0.4d),
                        fadeOut = (float) objData.optDouble("fade_out", 1.0d),
                        sizeIn = (float) objData.optDouble("size_in", 16.0d),
                        sizeOut = (float) objData.optDouble("size_out", 8.0d),
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
                final Vector4f colorIn = new Vector4f(), colorOut = new Vector4f(), velIn = new Vector4f(), velOut = new Vector4f(), spawnOffset = new Vector4f();
                final Vector2f angularIn = new Vector2f(), angularOut = new Vector2f();

                BUtil_MiscUtil.getVec4Color(objData.getString("color_in"), colorIn);
                BUtil_MiscUtil.getVec4Color(objData.getString("color_out"), colorOut);
                BUtil_MiscUtil.getVec4(objData.getString("velocity_in_range"), velIn);
                BUtil_MiscUtil.getVec4(objData.getString("velocity_out_range"), velOut);
                BUtil_MiscUtil.getVec4(objData.getString("spawn_offset_range"), spawnOffset);
                BUtil_MiscUtil.getVec2(objData.getString("angular_in_range"), angularIn);
                BUtil_MiscUtil.getVec2(objData.getString("angular_out_range"), angularOut);

                final MaterialData material = new MaterialData();
                material.setDiffuse(diffusePath.isBlank() ? 0 : BUtil_MiscUtil.tryTexture(diffusePath, TextureManager::tryTexture));
                if (!normalPath.isBlank()) material.setNormal(BUtil_MiscUtil.tryTexture(normalPath, TextureManager::tryTextureChannel3));
                if (!complexPath.isBlank()) material.setComplex(BUtil_MiscUtil.tryTexture(complexPath, TextureManager::tryTextureChannel3));
                material.setEmissive(emissivePath.isBlank() ? 0 : BUtil_MiscUtil.tryTexture(emissivePath, TextureManager::tryTexture));
                if (!tangentPath.isBlank()) material.setTangent(BUtil_MiscUtil.tryTangentTexture(tangentPath, objData.optBoolean("isTangentAngleMap", true), true, false));

                final StaticTrailData data = new StaticTrailData() {
                    public String id() {
                        return projID;
                    }

                    public MaterialData getMaterial() {
                        return material;
                    }

                    public float getFadeInTime() {
                        return fadeIn;
                    }

                    public float getFullTime() {
                        return full;
                    }

                    public float getFadeOutTime() {
                        return fadeOut;
                    }

                    public float getSizeIn() {
                        return sizeIn;
                    }

                    public float getSizeOut() {
                        return sizeOut;
                    }

                    public Vector4f getColorIn() {
                        return colorIn;
                    }

                    public Vector4f getColorOut() {
                        return colorOut;
                    }

                    public float getTexturePixels() {
                        return texPixels;
                    }

                    public float getTextureSpeed() {
                        return texSpeed;
                    }

                    public boolean isRandomUVStartOffset() {
                        return randomUV;
                    }

                    public Vector4f getVelocityInRange() {
                        return velIn;
                    }

                    public Vector4f getVelocityOutRange() {
                        return velOut;
                    }

                    public Vector2f getAngularInRange() {
                        return angularIn;
                    }

                    public Vector2f getAngularOutRange() {
                        return angularOut;
                    }

                    public boolean isAdditiveBlend() {
                        return additiveBlend;
                    }

                    public Vector4f getFixedSpawnOffsetRange() {
                        return spawnOffset;
                    }

                    public boolean isVelocityForForward() {
                        return velocityForward;
                    }

                    public boolean isRenderBelowExplosions() {
                        return renderBelowExplosions;
                    }
                };
                _PROJ_TRAIL.computeIfAbsent(projID, key -> new HashSet<>(2)).add(data);
            }
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' static trail csv data loading failed at: '" + path + "': ", e);
        }
    }

    /**
     * For example: {@code StaticTrailManager.loadTrailData("data/config/modFiles/BUtil_trail_data_magiclib.csv");}
     */
    public static void loadMagicLibLayoutTrailData(final String path) {
        _CACHED_MAGIC_LIB_LAYOUT_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;

            String diffuseKey;
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);
                final String projID = objData.optString("projectile"), trailID = objData.optString("trail");
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
                final Vector4f colorIn = new Vector4f(), colorOut = new Vector4f(), velIn = new Vector4f(), velOut = new Vector4f(), spawnOffset = new Vector4f();
                final Vector2f angularIn = new Vector2f(), angularOut = new Vector2f();

                final float distance = (float) objData.optDouble("distance", 0.0d),
                        dispersion = (float) objData.optDouble("dispersion", 0.0d),
                        velocityIn = (float) objData.optDouble("velocityIn", 0.0d),
                        velocityOut = (float) objData.optDouble("velocityOut", 0.0d),
                        randomVelocity = (float) objData.optDouble("randomVelocity", 0.0d),
                        rotationIn = (float) objData.optDouble("rotationIn", 0.0d),
                        rotationOut = (float) objData.optDouble("rotationOut", 0.0d);
                final boolean randomRotation_not = !objData.optBoolean("randomRotation", false);
                BUtil_MiscUtil.getVec4Color(objData.getString("colorIn"), colorIn);
                BUtil_MiscUtil.getVec4Color(objData.getString("colorOut"), colorOut);
                colorIn.w *= opacityMultiply;
                colorOut.w *= opacityMultiply;
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

                final MaterialData material = new MaterialData();
                material.setDiffuse(Global.getSettings().getSprite("fx", diffuseKey));

                final StaticTrailData data = new StaticTrailData() {
                    public String id() {
                        return projID;
                    }

                    public MaterialData getMaterial() {
                        return material;
                    }

                    public float getFadeInTime() {
                        return fadeIn;
                    }

                    public float getFullTime() {
                        return full;
                    }

                    public float getFadeOutTime() {
                        return fadeOut;
                    }

                    public float getSizeIn() {
                        return sizeIn;
                    }

                    public float getSizeOut() {
                        return sizeOut;
                    }

                    public Vector4f getColorIn() {
                        return colorIn;
                    }

                    public Vector4f getColorOut() {
                        return colorOut;
                    }

                    public float getTexturePixels() {
                        return texPixels;
                    }

                    public float getTextureSpeed() {
                        return texSpeed;
                    }

                    public boolean isRandomUVStartOffset() {
                        return randomUV;
                    }

                    public Vector4f getVelocityInRange() {
                        return velIn;
                    }

                    public Vector4f getVelocityOutRange() {
                        return velOut;
                    }

                    public Vector2f getAngularInRange() {
                        return angularIn;
                    }

                    public Vector2f getAngularOutRange() {
                        return angularOut;
                    }

                    public boolean isAdditiveBlend() {
                        return additiveBlend;
                    }

                    public Vector4f getFixedSpawnOffsetRange() {
                        return spawnOffset;
                    }

                    public boolean isVelocityForForward() {
                        return velocityForward;
                    }

                    public boolean isRenderBelowExplosions() {
                        return renderBelowExplosions;
                    }
                };
                _PROJ_TRAIL.computeIfAbsent(projID, key -> new HashSet<>(2)).add(data);
            }
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' static trail csv data loading failed at: '" + path + "': ", e);
        }
    }

    /**
     * Will overwrite all the data or add ones with (new or existed)id, but never clear them.<p>
     * Must be only call it by game dev mode F8 reload, do not use it in mod.
     */
    public static void _devModeReload() {
        for (String path : _CACHED_PATH) loadTrailData(path);
        for (String path : _CACHED_MAGIC_LIB_LAYOUT_PATH) loadMagicLibLayoutTrailData(path);
    }

    private StaticTrailManager() {}
}
