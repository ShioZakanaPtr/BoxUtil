package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.util.CommonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * For fetch key of weapon, use {@link EntityShadingDataManager#getWeaponKey(String, WeaponPartType, byte)}.<p>
 * For asteroid, in general managed by each shader packs, and use {@link SpriteAPI#getTextureId()} as <code>key</code>.<p>
 * For battle objective, use {@link BattleObjectiveAPI#getType()} as <code>key</code>.
 */
public final class EntityShadingDataManager {
    public final static String WEAPON_BARREL_SUFFIX = "_BARREL";
    public final static String WEAPON_UNDER_SUFFIX = "_UNDER";

    private final static HashMap<String, TextureSet> _ENTITY = new HashMap<>(32);
    private final static HashMap<String, TextureSet> _MISSILE = new HashMap<>(32);
    private final static HashMap<String, TextureSet>[] _WEAPON = new HashMap[]{new HashMap<String, TextureSet>(32), new HashMap<String, TextureSet>(32)};
    private final static HashMap<String, TextureSet>[][] _WEAPON_COVER = new HashMap[][]{
            new HashMap[]{new HashMap<String, TextureSet>(8), new HashMap<String, TextureSet>(8), new HashMap<String, TextureSet>(8)},
            new HashMap[]{new HashMap<String, TextureSet>(8), new HashMap<String, TextureSet>(8), new HashMap<String, TextureSet>(8)}
    };

    private final static HashMap<String, ProjectileIlluminantData> _PROJ_ILLUM = new HashMap<>(32);
    private final static HashMap<String, IsoIlluminantData> _BEAM_ILLUM = new HashMap<>(32);
    private final static HashMap<String, IsoIlluminantData> _ENGINE_ILLUM = new HashMap<>(8);

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
    public static TextureSet putEntityTextureSet(String id, TextureSet textureSet) {
        if (id == null || id.isEmpty() || textureSet == null) return null;
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
    public static TextureSet putMissileTextureSet(String id, TextureSet textureSet) {
        if (id == null || id.isEmpty() || textureSet == null) return null;
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
    public static TextureSet putWeaponTextureSet(String id, byte isHardpoint, TextureSet textureSet) {
        if (id == null || id.isEmpty() || textureSet == null) return null;
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
    public static TextureSet putWeaponCoverTextureSet(String id, byte isHardpoint, WeaponAPI.WeaponSize size, TextureSet textureSet) {
        if (id == null || id.isEmpty() || textureSet == null) return null;
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
    public static ProjectileIlluminantData putProjectileIlluminantData(String id, ProjectileIlluminantData illuminantData) {
        if (id == null || id.isEmpty() || illuminantData == null) return null;
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
    public static IsoIlluminantData putBeamIlluminantData(String id, IsoIlluminantData illuminantData) {
        if (id == null || id.isEmpty() || illuminantData == null) return null;
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
    public static IsoIlluminantData putEngineIlluminantData(String id, IsoIlluminantData illuminantData) {
        if (id == null || id.isEmpty() || illuminantData == null) return null;
        return _ENGINE_ILLUM.put(id, illuminantData);
    }

    /**
     * @param id the id of <strong>engine style</strong>.
     */
    public static IsoIlluminantData removeEngineIlluminantData(String id) {
        return _ENGINE_ILLUM.remove(id);
    }

    public static void loadTextureData(String path) {
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            HashMap<String, TextureSet> picker;
            String texKey, texType, texSubType, normalPath, complexPath, emissivePath;
            TextureSet texSet;
            boolean isHardpoint, valid;
            int normal, complex, emissive;
            byte frame, weaponSlotPicker;
            for (int i = 0; i < objDataArray.length(); i++) {
                picker = null;
                objData = objDataArray.getJSONObject(i);
                texKey = objData.optString("id");
                texType = objData.optString("type").toUpperCase();
                normal = complex = emissive = -1;
                if (!texKey.isEmpty() && !texType.isEmpty()) {
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
                        if ((texType.contentEquals(BoxDatabase.SHADING_TEXTURE_DATA_TYPE[2]) || isHardpoint) && !texSubType.isEmpty()) {
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
                        SpriteAPI vanillaSprite;
                        if (!normalPath.isEmpty()) {
                            vanillaSprite = Global.getSettings().getSprite(normalPath);
                            normal = vanillaSprite != null ? vanillaSprite.getTextureId() : TextureManager.tryTextureChannel3(normalPath);
                        }
                        if (!complexPath.isEmpty()) {
                            vanillaSprite = Global.getSettings().getSprite(complexPath);
                            complex = vanillaSprite != null ? vanillaSprite.getTextureId() : TextureManager.tryTextureChannel3(complexPath);
                        }
                        if (!emissivePath.isEmpty()) {
                            vanillaSprite = Global.getSettings().getSprite(emissivePath);
                            emissive = vanillaSprite != null ? vanillaSprite.getTextureId() : TextureManager.tryTexture(emissivePath);
                        }
                        texSet = new TextureSet(normal, complex, emissive);
                        picker.put(texKey, texSet);
                    }
                }
            }
            Global.getLogger(EntityShadingDataManager.class).info("'BoxUtil' texture csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' texture csv data loading failed at: '" + path + "': ", e);
        }
    }

    public static void loadGraphicsLibLayoutTextureData(String path) {
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            HashMap<String, TextureSet> mapPicker;
            String texKey, texType, texDataType, texPath;
            TextureSet texSet;
            boolean valid, isNormalMap, isComplexMap;
            int texID;
            byte frame;
            for (int i = 0; i < objDataArray.length(); i++) {
                mapPicker = null;
                objData = objDataArray.getJSONObject(i);
                texKey = objData.optString("id");
                texType = objData.optString("type").toUpperCase();
                texDataType = objData.optString("map").toUpperCase();
                isNormalMap = "NORMAL".contentEquals(texDataType);
                isComplexMap = "SURFACE".contentEquals(texDataType);
                texPath = objData.optString("path").toUpperCase();
                frame = (byte) objData.optInt("frame", -1);
                if (!texKey.isEmpty() && !texType.isEmpty() && !texPath.isEmpty() && (isNormalMap || isComplexMap)) {
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
                            mapPicker.put(texKey, texSet);
                        }

                        SpriteAPI vanillaSprite = Global.getSettings().getSprite(texPath);
                        texID = vanillaSprite != null ? vanillaSprite.getTextureId() : TextureManager.tryTextureChannel3(texPath);
                        if (isNormalMap) texSet.setNormal(texID); else texSet.setComplex(texID);
                    }
                }
            }
            Global.getLogger(EntityShadingDataManager.class).info("'BoxUtil' texture csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' texture csv data loading failed at: '" + path + "': ", e);
        }
    }

    private static void getColorArray(String colorArray, byte[] array) {
        if (colorArray.length() < 9) return;
        byte index = 0;
        for (String str : colorArray.substring(1, colorArray.length() - 1).split(",")) {
            array[index] = Byte.parseByte(str);
            ++index;
        }
    }

    public static void loadIlluminantData(String path) {
        try {
            JSONArray objDataArray = Global.getSettings().loadCSV(path);
            JSONObject objData;
            String illuminantKey, illuminantType;
            byte[] spawnColor = new byte[4], bodyColor = new byte[4], hitColor = new byte[4];
            for (int i = 0; i < objDataArray.length(); i++) {
                objData = objDataArray.getJSONObject(i);
                illuminantKey = objData.optString("id");
                illuminantType = objData.optString("type").toUpperCase();
                if (!illuminantKey.isEmpty() && !illuminantType.isEmpty()) {
                    if (illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[0])) {
                        getColorArray(objData.optString("spawnColor"), spawnColor);
                        getColorArray(objData.optString("bodyColor"), bodyColor);
                        getColorArray(objData.optString("hitColor"), hitColor);
                        _PROJ_ILLUM.put(illuminantKey, new ProjectileIlluminantData(spawnColor, bodyColor, hitColor, (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d), (float) objData.optDouble("", 0.0d)));
                    } else if (illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[1])) {
                        getColorArray(objData.optString("bodyColor"), bodyColor);
                        _BEAM_ILLUM.put(illuminantKey, new IsoIlluminantData(bodyColor[0], bodyColor[1], bodyColor[2], bodyColor[3], (float) objData.optDouble("bodyRadius", 0.0d)));
                    } else if (illuminantType.contentEquals(BoxDatabase.SHADING_ILLUMINANT_DATA_TYPE[2])) {
                        getColorArray(objData.optString("bodyColor"), bodyColor);
                        _ENGINE_ILLUM.put(illuminantKey, new IsoIlluminantData(bodyColor[0], bodyColor[1], bodyColor[2], bodyColor[3], (float) objData.optDouble("bodyRadius", 0.0d)));
                    }
                }
            }
            Global.getLogger(EntityShadingDataManager.class).info("'BoxUtil' illuminant csv data loading finished: '" + path + "'.");
        } catch (JSONException | IOException e) {
            CommonUtil.printThrowable(EntityShadingDataManager.class, "'BoxUtil' illuminant csv data loading failed at: '" + path + "': ", e);
        }
    }

    private EntityShadingDataManager() {}
}
