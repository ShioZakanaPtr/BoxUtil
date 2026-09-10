package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.apache.log4j.Logger;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.container.Obj2ObjRHMap;
import org.boxutil.util.container.ObjRHSet;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * For fetch key of weapon, use {@link EntityShadingDataManager#getWeaponKey(String, WeaponPartType, byte)}.<p>
 * For asteroid, in general managed by each shader packs, and use {@link SpriteAPI#getTextureId()} as <code>key</code>.<p>
 * For battle objective, use {@link BattleObjectiveAPI#getType()} as <code>key</code>.
 */
@SuppressWarnings("UnusedReturnValue")
public final class EntityShadingDataManager {
    public final static String WEAPON_BARREL_SUFFIX = "_BARREL";
    public final static String WEAPON_UNDER_SUFFIX = "_UNDER";

    private final static Map<String, TextureSet> _ENTITY = new Obj2ObjRHMap<>(32);
    private final static Map<String, TextureSet> _MISSILE = new Obj2ObjRHMap<>(32);
    private final static Map<String, TextureSet>[] _WEAPON = new Map[]{new Obj2ObjRHMap<String, TextureSet>(32), new Obj2ObjRHMap<String, TextureSet>(32)};
    private final static Map<String, TextureSet>[][] _WEAPON_COVER = new Map[][]{
            new Map[]{new Obj2ObjRHMap<String, TextureSet>(8), new Obj2ObjRHMap<String, TextureSet>(8), new Obj2ObjRHMap<String, TextureSet>(8)},
            new Map[]{new Obj2ObjRHMap<String, TextureSet>(8), new Obj2ObjRHMap<String, TextureSet>(8), new Obj2ObjRHMap<String, TextureSet>(8)}
    };

    private final static Map<String, ProjectileIlluminantData> _PROJ_ILLUM = new Obj2ObjRHMap<>(32);
    private final static Map<String, IsoIlluminantData> _BEAM_ILLUM = new Obj2ObjRHMap<>(32);
    private final static Map<String, IsoIlluminantData> _ENGINE_ILLUM = new Obj2ObjRHMap<>(8);

    private final static Set<String> _CACHED_TEXTURE_PATH = new ObjRHSet<>(32);
    private final static Set<String> _CACHED_GRAPHICS_LIB_LAYOUT_TEXTURE_PATH = new ObjRHSet<>(32);
    private final static Set<String> _CACHED_ILLUMINANT_PATH = new ObjRHSet<>(32);

    private static final Logger _LOG = Global.getLogger(EntityShadingDataManager.class);

    public enum WeaponPartType {
        BASE,
        BARREL,
        UNDER;
    }

    /**
     * @param frameIndex less than 0 for none animation.
     */
    public static String getWeaponKey(String weaponID, WeaponPartType weaponPart, byte frameIndex) {
        String key = weaponID;
        switch (weaponPart) {
            case BARREL -> key += WEAPON_BARREL_SUFFIX;
            case UNDER -> key += WEAPON_UNDER_SUFFIX;
            default -> {}
        }
        if (frameIndex >= 0) {
            key += '_';
            if (frameIndex < 10) key += '0';
            key += Math.min(frameIndex, 99);
        }
        return key;
    }

    public static class TextureSet {
        private final int[] glTex = new int[3];

        public TextureSet(int normal, int complex, int emissive) {
            this.glTex[0] = normal < 1 ? BoxDatabase.BUtil_Z.getTextureId() : normal;
            this.glTex[1] = complex < 1 ? BoxDatabase.BUtil_COMPLEX_DEF.getTextureId() : complex;
            this.glTex[2] = emissive < 1 ? BoxDatabase.BUtil_ZERO.getTextureId() : emissive;
        }

        public int getNormal() {
            return this.glTex[0];
        }

        public void setNormal(int tex) {
            this.glTex[0] = tex < 1 ? BoxDatabase.BUtil_Z.getTextureId() : tex;
        }

        public int getComplex() {
            return this.glTex[1];
        }

        public void setComplex(int tex) {
            this.glTex[1] = tex < 1 ? BoxDatabase.BUtil_COMPLEX_DEF.getTextureId() : tex;
        }

        public int getEmissive() {
            return this.glTex[2];
        }

        public void setEmissive(int tex) {
            this.glTex[2] = tex < 1 ? BoxDatabase.BUtil_ZERO.getTextureId() : tex;
        }

        public String toString() {
            return "TextureSet: Normal = '" + this.glTex[0] + "', Complex = '" + this.glTex[1] + "', Emissive = '" + this.glTex[2] + '\'';
        }

        public int hashCode() {
            int result = 1;
            for (int tex : this.glTex) {
                result = 31 * result + tex;
            }
            return result;
        }

        public boolean equals(Object obj) {
            if (obj instanceof TextureSet) return this.hashCode() == obj.hashCode();
            else return false;
        }
    }

    public static class IsoIlluminantData {
        private final byte[] color = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};
        private float radius;

        public IsoIlluminantData(byte red, byte green, byte blue, byte strengthScale, float radius) {
            this.color[0] = red;
            this.color[1] = green;
            this.color[2] = blue;
            this.color[3] = strengthScale;
            this.radius = radius;
        }

        public byte[] getColor() {
            return this.color;
        }

        public byte getRed() {
            return this.color[0];
        }

        public void setRed(byte red) {
            this.color[0] = red;
        }

        public byte getGreen() {
            return this.color[1];
        }

        public void setGreen(byte green) {
            this.color[1] = green;
        }

        public byte getBlue() {
            return this.color[2];
        }

        public void setBlue(byte blue) {
            this.color[2] = blue;
        }

        public byte getStrengthScale() {
            return this.color[3];
        }

        public void setStrengthScale(byte strengthScale) {
            this.color[3] = strengthScale;
        }

        public float getRadius() {
            return this.radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public int hashCode() {
            int result = Float.hashCode(this.radius) + 31;
            for (byte colorByte : this.color) result = 31 * result + colorByte;
            return result;
        }

        public boolean equals(Object obj) {
            if (obj instanceof IsoIlluminantData) return this.hashCode() == obj.hashCode();
            else return false;
        }
    }

    public static class ProjectileIlluminantData {
        private final byte[] spawnColor = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};
        private final byte[] bodyColor = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};
        private final byte[] hitColor = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};
        private final float[] state = new float[5];

        public ProjectileIlluminantData(byte[] spawn, byte[] body, byte[] hit, float spawnRadius, float bodyRadius, float hitRadius, float spawnDur, float hitDur) {
            this.spawnColor[0] = spawn[0];
            this.spawnColor[1] = spawn[1];
            this.spawnColor[2] = spawn[2];
            this.spawnColor[3] = spawn[3];
            this.bodyColor[0] = body[0];
            this.bodyColor[1] = body[1];
            this.bodyColor[2] = body[2];
            this.bodyColor[3] = body[3];
            this.hitColor[0] = hit[0];
            this.hitColor[1] = hit[1];
            this.hitColor[2] = hit[2];
            this.hitColor[3] = hit[3];
            this.state[0] = spawnRadius;
            this.state[1] = bodyRadius;
            this.state[2] = hitRadius;
            this.state[3] = spawnDur;
            this.state[4] = hitDur;
        }

        public byte[] getSpawnColor() {
            return this.spawnColor;
        }

        public void setSpawnColor(byte red, byte green, byte blue, byte strengthScale) {
            this.spawnColor[0] = red;
            this.spawnColor[1] = green;
            this.spawnColor[2] = blue;
            this.spawnColor[3] = strengthScale;
        }

        public byte[] getBodyColor() {
            return this.bodyColor;
        }

        public void setBodyColor(byte red, byte green, byte blue, byte strengthScale) {
            this.bodyColor[0] = red;
            this.bodyColor[1] = green;
            this.bodyColor[2] = blue;
            this.bodyColor[3] = strengthScale;
        }

        public byte[] getHitColor() {
            return this.hitColor;
        }

        public void setHitColor(byte red, byte green, byte blue, byte strengthScale) {
            this.hitColor[0] = red;
            this.hitColor[1] = green;
            this.hitColor[2] = blue;
            this.hitColor[3] = strengthScale;
        }

        public float getSpawnRadius() {
            return this.state[0];
        }

        public void setSpawnRadius(float radius) {
            this.state[0] = radius;
        }

        public float getBodyRadius() {
            return this.state[1];
        }

        public void setBodyRadius(float radius) {
            this.state[1] = radius;
        }

        public float getHitRadius() {
            return this.state[2];
        }

        public void setHitRadius(float radius) {
            this.state[2] = radius;
        }

        public float getSpawnDuration() {
            return this.state[3];
        }

        public void setSpawnDuration(float duration) {
            this.state[3] = duration;
        }

        public float getHitDuration() {
            return this.state[4];
        }

        public void setHitDuration(float duration) {
            this.state[4] = duration;
        }

        public int hashCode() {
            int result = 1;
            for (byte colorByte : this.spawnColor) result = 31 * result + colorByte;
            for (byte colorByte : this.bodyColor) result = 31 * result + colorByte;
            for (byte colorByte : this.hitColor) result = 31 * result + colorByte;
            for (float stateFloat : this.state) result = 31 * result + Float.hashCode(stateFloat);
            return result;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ProjectileIlluminantData) return this.hashCode() == obj.hashCode();
            else return false;
        }
    }

    /**
     * @param id the spec id of <strong>ship</strong> and <strong>battle objective</strong>.
     */
    public static boolean haveEntityTextureSet(String id) {
        return _ENTITY.containsKey(id);
    }

    /**
     * @param id the spec id of <strong>ship</strong> and <strong>battle objective</strong>.
     */
    public static TextureSet getEntityTextureSet(String id) {
        return _ENTITY.get(id);
    }

    /**
     * @param id the spec id of <strong>ship</strong> and <strong>battle objective</strong>.
     */
    public static TextureSet putEntityTextureSet(@NotNull String id, TextureSet textureSet) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (textureSet == null) return null;
        return _ENTITY.put(id, textureSet);
    }

    /**
     * @param id the spec id of <strong>ship</strong> and <strong>battle objective</strong>.
     */
    public static TextureSet removeEntityTextureSet(String id) {
        return _ENTITY.remove(id);
    }

    /**
     * @param id the spec id of <strong>missile
     */
    public static boolean haveMissileTextureSet(String id) {
        return _MISSILE.containsKey(id);
    }

    /**
     * @param id the spec id of <strong>missile
     */
    public static TextureSet getMissileTextureSet(String id) {
        return _MISSILE.get(id);
    }

    /**
     * @param id the spec id of <strong>missile
     */
    public static TextureSet putMissileTextureSet(@NotNull String id, TextureSet textureSet) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (textureSet == null) return null;
        return _MISSILE.put(id, textureSet);
    }

    /**
     * @param id the spec id of <strong>missile</strong>.
     */
    public static TextureSet removeMissileTextureSet(String id) {
        return _MISSILE.remove(id);
    }

    /**
     * @param id the spec id of <strong>weapon</strong> and <strong>missile projectile</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static boolean haveWeaponTextureSet(String id, byte isHardpoint) {
        return _WEAPON[isHardpoint].containsKey(id);
    }

    /**
     * @param id the spec id of <strong>weapon</strong> and <strong>missile projectile</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet getWeaponTextureSet(String id, byte isHardpoint) {
        return _WEAPON[isHardpoint].get(id);
    }

    /**
     * @param id the spec id of <strong>weapon</strong> and <strong>missile projectile</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet putWeaponTextureSet(@NotNull String id, byte isHardpoint, TextureSet textureSet) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (textureSet == null) return null;
        return _WEAPON[isHardpoint].put(id, textureSet);
    }

    /**
     * @param id the spec id of <strong>weapon</strong> and <strong>missile projectile</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet removeWeaponTextureSet(String id, byte isHardpoint) {
        return _WEAPON[isHardpoint].remove(id);
    }

    /**
     * @param id the id of <strong>hull style</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static boolean haveWeaponCoverTextureSet(String id, byte isHardpoint, WeaponAPI.WeaponSize size) {
        return _WEAPON_COVER[isHardpoint][size.ordinal()].containsKey(id);
    }

    /**
     * @param id the id of <strong>hull style</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet getWeaponCoverTextureSet(String id, byte isHardpoint, WeaponAPI.WeaponSize size) {
        return _WEAPON_COVER[isHardpoint][size.ordinal()].get(id);
    }

    /**
     * @param id the id of <strong>hull style</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet putWeaponCoverTextureSet(@NotNull String id, byte isHardpoint, WeaponAPI.WeaponSize size, TextureSet textureSet) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (textureSet == null) return null;
        return _WEAPON_COVER[isHardpoint][size.ordinal()].put(id, textureSet);
    }

    /**
     * @param id the id of <strong>hull style</strong>.
     * @param isHardpoint values {@link BoxEnum#TRUE} and {@link BoxEnum#FALSE}.
     */
    public static TextureSet removeWeaponCoverTextureSet(String id, byte isHardpoint, WeaponAPI.WeaponSize size) {
        return _WEAPON_COVER[isHardpoint][size.ordinal()].remove(id);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static boolean haveProjectileIlluminantData(String id) {
        return _PROJ_ILLUM.containsKey(id);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static ProjectileIlluminantData getProjectileIlluminantData(String id) {
        return _PROJ_ILLUM.get(id);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static ProjectileIlluminantData putProjectileIlluminantData(@NotNull String id, ProjectileIlluminantData illuminantData) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (illuminantData == null) return null;
        return _PROJ_ILLUM.put(id, illuminantData);
    }

    /**
     * @param id the id of <strong>projectile spec</strong>.
     */
    public static ProjectileIlluminantData removeProjectileIlluminantData(String id) {
        return _PROJ_ILLUM.remove(id);
    }

    /**
     * @param id the id of <strong>beam weapon</strong>.
     */
    public static boolean haveBeamIlluminantData(String id) {
        return _BEAM_ILLUM.containsKey(id);
    }

    /**
     * @param id the id of <strong>beam weapon</strong>.
     */
    public static IsoIlluminantData getBeamIlluminantData(String id) {
        return _BEAM_ILLUM.get(id);
    }

    /**
     * @param id the id of <strong>beam weapon</strong>.
     */
    public static IsoIlluminantData putBeamIlluminantData(@NotNull String id, IsoIlluminantData illuminantData) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (illuminantData == null) return null;
        return _BEAM_ILLUM.put(id, illuminantData);
    }

    /**
     * @param id the id of <strong>beam weapon</strong>.
     */
    public static IsoIlluminantData removeBeamIlluminantData(String id) {
        return _BEAM_ILLUM.remove(id);
    }

    /**
     * @param id the id of <strong>engine style</strong>.
     */
    public static boolean haveEngineIlluminantData(String id) {
        return _ENGINE_ILLUM.containsKey(id);
    }

    /**
     * @param id the id of <strong>engine style</strong>.
     */
    public static IsoIlluminantData getEngineIlluminantData(String id) {
        return _ENGINE_ILLUM.get(id);
    }

    /**
     * @param id the id of <strong>engine style</strong>.
     */
    public static IsoIlluminantData putEngineIlluminantData(@NotNull String id, IsoIlluminantData illuminantData) {
        if (id.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        if (illuminantData == null) return null;
        return _ENGINE_ILLUM.put(id, illuminantData);
    }

    /**
     * @param id the id of <strong>engine style</strong>.
     */
    public static IsoIlluminantData removeEngineIlluminantData(String id) {
        return _ENGINE_ILLUM.remove(id);
    }

    public static void loadTextureData(String path) {
        _CACHED_TEXTURE_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            Map<String, TextureSet> picker;
            String texKey, texType, texSubType, normalPath, complexPath, emissivePath;
            TextureSet texSet;
            boolean isHardpoint, valid;
            int normal, complex, emissive;
            byte frame, weaponSlotPicker;
            for (int i = 0; i < objDataArray.length(); i++) {
                picker = null;
                objData = objDataArray.getJSONObject(i);
                texKey = objData.optString("id");
                texType = objData.optString("type");
                if (BUtil_MiscUtil.csvSkipAnnotationID(texKey)) continue;
                if (texKey.isBlank() || texType.isBlank()) {
                    _LOG.warn("'BoxUtil' shading texture csv data have empty id/type at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }

                texType = texType.toUpperCase();
                normal = complex = emissive = -1;

                valid = false;
                if (texType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_TYPE[0])) {
                    valid = true;
                    picker = _ENTITY;
                } else if (texType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_TYPE[1])) {
                    valid = true;
                    picker = _MISSILE;
                } else {
                    isHardpoint = texType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_TYPE[3]);
                    texSubType = objData.optString("subType").toUpperCase();
                    if ((texType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_TYPE[2]) || isHardpoint) && !texSubType.isBlank()) {
                        valid = true;
                        frame = (byte) objData.optInt("frame", -1);
                        weaponSlotPicker = isHardpoint ? BoxEnum.TRUE : BoxEnum.FALSE;
                        if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[0])) {
                            texKey = getWeaponKey(texKey, WeaponPartType.BASE, frame);
                            picker = _WEAPON[weaponSlotPicker];
                        } else if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[1])) {
                            texKey = getWeaponKey(texKey, WeaponPartType.BARREL, frame);
                            picker = _WEAPON[weaponSlotPicker];
                        } else if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[2])) {
                            texKey = getWeaponKey(texKey, WeaponPartType.UNDER, frame);
                            picker = _WEAPON[weaponSlotPicker];
                        } else if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[3])) {
                            picker = _WEAPON_COVER[weaponSlotPicker][0];
                        } else if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[4])) {
                            picker = _WEAPON_COVER[weaponSlotPicker][1];
                        } else if (texSubType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_SUBTYPE[5])) {
                            picker = _WEAPON_COVER[weaponSlotPicker][2];
                        } else valid = false;
                    }
                }
                if (valid) {
                    normalPath = objData.optString("normal_path");
                    complexPath = objData.optString("complex_path");
                    emissivePath = objData.optString("emissive_path");
                    if (!normalPath.isBlank()) normal = BUtil_MiscUtil.tryTexture(normalPath, TextureManager::tryTextureChannel3);
                    if (!complexPath.isBlank()) complex = BUtil_MiscUtil.tryTexture(complexPath, TextureManager::tryTextureChannel3);
                    if (!emissivePath.isBlank()) emissive = BUtil_MiscUtil.tryTexture(emissivePath, TextureManager::tryTexture);

                    texSet = new TextureSet(normal, complex, emissive);
                    picker.merge(texKey, texSet, (oldValue, newValue) -> newValue);
                } else _LOG.warn("'BoxUtil' shading texture csv data have invalid type at lines '" + i + "' in: '" + path + "'.");
            }
            _LOG.info("'BoxUtil' texture csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' texture csv data loading failed at: '" + path + "': ", e);
        }
    }

    public static void loadGraphicsLibLayoutTextureData(String path) {
        _CACHED_GRAPHICS_LIB_LAYOUT_TEXTURE_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            Map<String, TextureSet> mapPicker;
            String texKey, texType, texDataType, texPath;
            TextureSet texSet;
            boolean valid, isNormalMap, isComplexMap;
            int texID;
            byte frame;
            for (int i = 0; i < objDataArray.length(); i++) {
                mapPicker = null;
                objData = objDataArray.getJSONObject(i);
                texKey = objData.optString("id");
                texType = objData.optString("type");
                texPath = objData.optString("path");
                if (BUtil_MiscUtil.csvSkipAnnotationID(texKey)) continue;
                if (texKey.isBlank() || texType.isBlank() || texPath.isBlank()) {
                    _LOG.warn("'BoxUtil' shading texture csv data have empty id/type/path at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }

                texDataType = objData.optString("map").toUpperCase();
                isNormalMap = "NORMAL".contentEquals(texDataType);
                isComplexMap = "SURFACE".contentEquals(texDataType);
                if (texDataType.isBlank() || !(isNormalMap || isComplexMap)) {
                    _LOG.warn("'BoxUtil' shading texture csv data have invalid type at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }

                frame = (byte) objData.optInt("frame", -1);
                texType = texType.toUpperCase();

                valid = true;
                switch (texType) {
                    case "SHIP" -> mapPicker = _ENTITY;
                    case "MISSILE" -> mapPicker = _MISSILE;
                    case "TURRET" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.BASE, frame);
                        mapPicker = _WEAPON[0];
                    }
                    case "TURRETBARREL" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.BARREL, frame);
                        mapPicker = _WEAPON[0];
                    }
                    case "TURRETUNDER" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.UNDER, frame);
                        mapPicker = _WEAPON[0];
                    }
                    case "HARDPOINT" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.BASE, frame);
                        mapPicker = _WEAPON[1];
                    }
                    case "HARDPOINTBARREL" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.BARREL, frame);
                        mapPicker = _WEAPON[1];
                    }
                    case "HARDPOINTUNDER" -> {
                        texKey = getWeaponKey(texKey, WeaponPartType.UNDER, frame);
                        mapPicker = _WEAPON[1];
                    }
                    case "TURRETCOVERSMALL" -> mapPicker = _WEAPON_COVER[0][0];
                    case "TURRETCOVERMEDIUM" -> mapPicker = _WEAPON_COVER[0][1];
                    case "TURRETCOVERLARGE" -> mapPicker = _WEAPON_COVER[0][2];
                    case "HARDPOINTCOVERSMALL" -> mapPicker = _WEAPON_COVER[1][0];
                    case "HARDPOINTCOVERMEDIUM" -> mapPicker = _WEAPON_COVER[1][1];
                    case "HARDPOINTCOVERLARGE" -> mapPicker = _WEAPON_COVER[1][2];
                    default -> valid = false;
                }
                if (valid) {
                    texSet = mapPicker.get(texKey);
                    if (texSet == null) {
                        texSet = new TextureSet(-1, -1, -1);
                        mapPicker.merge(texKey, texSet, (oldValue, newValue) -> newValue);
                    }

                    texID = BUtil_MiscUtil.tryTexture(texPath, TextureManager::tryTextureChannel3);
                    if (isNormalMap) texSet.setNormal(texID); else texSet.setComplex(texID);
                } else _LOG.warn("'BoxUtil' shading texture csv data have invalid type at lines '" + i + "' in: '" + path + "'.");
            }
            _LOG.info("'BoxUtil' shading texture csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' shading texture csv data loading failed at: '" + path + "': ", e);
        }
    }

    public static void loadIlluminantData(String path) {
        _CACHED_ILLUMINANT_PATH.add(path);
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            String illuminantKey, illuminantType;
            byte[] spawnColor = new byte[4], bodyColor = new byte[4], hitColor = new byte[4];
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);

                illuminantKey = objData.optString("id");
                illuminantType = objData.optString("type");
                if (illuminantKey.isBlank() || illuminantType.isBlank()) {
                    _LOG.warn("'BoxUtil' illuminant csv data have empty id or type at lines '" + i + "' in: '" + path + "'.");
                    continue;
                }
                illuminantType = illuminantType.toUpperCase();

                if (illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[0])) {
                    BUtil_MiscUtil.getColorArray(objData.optString("spawnColor"), spawnColor);
                    BUtil_MiscUtil.getColorArray(objData.optString("bodyColor"), bodyColor);
                    BUtil_MiscUtil.getColorArray(objData.optString("hitColor"), hitColor);

                    final ProjectileIlluminantData data = new ProjectileIlluminantData(spawnColor, bodyColor, hitColor, (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d));
                    _PROJ_ILLUM.merge(illuminantKey, data, (oldValue, newValue) -> newValue);
                } else {
                    final boolean typeBeam = illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[1]),
                            typeEngine = illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[2]);
                    if (!(typeBeam || typeEngine)) {
                        _LOG.warn("'BoxUtil' illuminant csv data have invalid type at lines '" + i + "' in: '" + path + "'.");
                        continue;
                    }

                    BUtil_MiscUtil.getColorArray(objData.optString("bodyColor"), bodyColor);

                    final IsoIlluminantData data = new IsoIlluminantData(bodyColor[0], bodyColor[1], bodyColor[2], bodyColor[3], (float) objData.optDouble("bodyRadius", 0.0d));

                    final Map<String, IsoIlluminantData> map = typeBeam ? _BEAM_ILLUM : _ENGINE_ILLUM;
                    map.merge(illuminantKey, data, (oldValue, newValue) -> newValue);
                }
            }
            _LOG.info("'BoxUtil' illuminant csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' illuminant csv data loading failed at: '" + path + "': ", e);
        }
    }

    /**
     * Will overwrite all the data or add ones with (new or existed)id, but never clear them.<p>
     * Must be only call it by game dev mode F8 reload, do not use it in mod.
     */
    public static void _devModeReload() {
        for (String path : _CACHED_TEXTURE_PATH) loadTextureData(path);
        for (String path : _CACHED_GRAPHICS_LIB_LAYOUT_TEXTURE_PATH) loadGraphicsLibLayoutTextureData(path);
        for (String path : _CACHED_ILLUMINANT_PATH) loadIlluminantData(path);
    }

    private EntityShadingDataManager() {}
}
