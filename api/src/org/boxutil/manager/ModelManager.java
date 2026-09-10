package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.define.BoxDatabase;
import org.boxutil.units.legacy.LegacyModelData;
import org.boxutil.backends.struct.BUtil_Stack2f;
import org.boxutil.backends.struct.BUtil_Stack3f;
import org.boxutil.backends.struct.BUtil_TriIndex;
import org.boxutil.units.standard.attribute.ModelData;
import org.boxutil.util.container.Obj2ObjRHMap;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public final class ModelManager {
    private static final Map<String, ModelData> _MODEL_DATA = new Obj2ObjRHMap<>(32);
    private static final Map<String, LegacyModelData> _LEGACY_MODEL = new Obj2ObjRHMap<>(32);

    private static final Logger _LOG = Global.getLogger(ModelManager.class);

    /**
     * General method for reading model files, when game starting.<p>
     * Cannot import any files then bigger than <code>8 MiB</code> for user's device.<p>
     * DO NOT CONTAIN N-GONS
     */
    public static Map<String, ModelData> loadModelDataCSV(String path) {
        try {
            return wavefrontOBJCSVLoadCore(path);
        } catch (JSONException | IOException e) {
            _LOG.log(Level.ERROR, "'BoxUtil' models csv data loading failed at: '" + path + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Add one of model data by manual.<p>
     * Not recommended to use an existing ID, only when you know what you are doing.<p>
     * Cannot import any files then bigger than <code>8 MiB</code> for user's device.<p>
     * DO NOT CONTAIN N-GONS
     */
    public static ModelData addModelData(String initID, String objPath, @Nullable String diffusePath, @Nullable String normalPath, @Nullable String complexPath, @Nullable String emissivePath, @Nullable String tangentPath, boolean isAngleMap, int type) {
        try {
            return addOBJDataCore(initID, objPath, diffusePath, normalPath, complexPath, emissivePath, tangentPath, isAngleMap, type);
        } catch (IOException e) {
            _LOG.log(Level.ERROR, "'BoxUtil' loading '" + initID + "' failed at: '" + objPath + "': " + e.getMessage());
            return null;
        }
    }

    public static ModelData addModelData(String initID, String objPath, @Nullable String diffusePath, @Nullable String normalPath, @Nullable String complexPath, @Nullable String emissivePath, @Nullable String tangentPath, boolean isAngleMap) {
        return addModelData(initID, objPath, diffusePath, normalPath, complexPath, emissivePath, tangentPath, isAngleMap, GL30.GL_HALF_FLOAT);
    }

    public static ModelData addModelData(String initID, String objPath, @Nullable String diffusePath, @Nullable String normalPath, @Nullable String complexPath, @Nullable String emissivePath, @Nullable String tangentPath) {
        return addModelData(initID, objPath, diffusePath, normalPath, complexPath, emissivePath, tangentPath, true, GL30.GL_HALF_FLOAT);
    }

    /**
     * @see #addModelData(String, String, String, String, String, String, String, boolean, int)
     */
    public static ModelData addModelData(String id, ModelData obj) {
        return _MODEL_DATA.put(id, obj);
    }

    public static ModelData getModelData(String id) {
        return _MODEL_DATA.get(id);
    }

    public static boolean containsModelID(String id) {
        return _MODEL_DATA.containsKey(id);
    }

    public static boolean haveLegacyModelData(String file) {
        return _LEGACY_MODEL.containsKey(file);
    }

    public static LegacyModelData getLegacyModelData(String file) {
        return _LEGACY_MODEL.get(file);
    }

    public static LegacyModelData putLegacyModelData(String file, LegacyModelData data) {
        if (file == null || file.isBlank() || data == null) return null;
        return _LEGACY_MODEL.put(file, data);
    }

    public static LegacyModelData deleteLegacyModelData(String file) {
        return _LEGACY_MODEL.remove(file);
    }

    /**
     * For your custom shader, use {@link GL11#GL_TRIANGLES} mode to draw.<p>
     * Cannot load any mesh then bigger than 8 MiB for user's device.<p>
     * DO NOT CONTAIN N-GONS
     */
    public static LegacyModelData loadLegacyModelData(String file) {
        try {
            return putLegacyModelData(file, getLegacyModelDataCore(file));
        } catch (IOException e) {
            _LOG.log(Level.ERROR, "'BoxUtil' model data loading failed at: '" + file + "'." + e.getMessage());
            return null;
        }
    }

    public static LegacyModelData tryLegacyModelData(String file) {
        if (!haveLegacyModelData(file)) loadLegacyModelData(file);
        return _LEGACY_MODEL.get(file);
    }

    private static ModelData addOBJDataCore(String initID, String objPath, String diffusePath, String normalPath, String complexPath, String emissivePath, String tangentPath, boolean isAngleMap, int type) throws IOException {
        String objFile = Global.getSettings().loadText(objPath);
        if (objFile.getBytes().length > BoxDatabase.MAX_MODEL_FILE_SIZE) {
            _LOG.log(Level.WARN, "'BoxUtil' model ID '" + initID + "' at path '" + objPath + "' was too bigger than 8 MiB.");
            return null;
        }
        BufferedReader reader = new BufferedReader(new StringReader(objFile));
        List<Vector3f> vertex = new ArrayList<>();
        List<Vector3f> normal = new ArrayList<>();
        List<Vector2f> uv = new ArrayList<>();
        List<BUtil_TriIndex> tri = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] splitString = line.split(" ");
                float x = Float.parseFloat(splitString[1]);
                float y = Float.parseFloat(splitString[2]);
                float z = Float.parseFloat(splitString[3]);
                vertex.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] splitString = line.split(" ");
                float x = Float.parseFloat(splitString[1]);
                float y = Float.parseFloat(splitString[2]);
                float z = Float.parseFloat(splitString[3]);
                Vector3f normalTmp = new Vector3f(x, y, z).normalise(null);
                normal.add(normalTmp);
            } else if (line.startsWith("vt ")) {
                String[] splitString = line.split(" ");
                float u = Float.parseFloat(splitString[1]);
                float v = Float.parseFloat(splitString[2]);
                uv.add(new Vector2f(u, v));
            } else if (line.startsWith("f ")) {
                String[] splitString = line.split(" ");
                String p1 = splitString[1];
                String p2 = splitString[2];
                String p3 = splitString[3];
                String p4 = splitString[splitString.length - 1];
                if (p3.contains(p4)) {
                    tri.add(new BUtil_TriIndex(p1, p2, p3));
                } else {
                    tri.add(new BUtil_TriIndex(p1, p2, p3));
                    tri.add(new BUtil_TriIndex(p3, p4, p1));
                }
            }
        }

        int diffuse, normalMap, complex, emissive, tangent;
        if (diffusePath == null || diffusePath.isBlank()) diffuse = BoxDatabase.BUtil_ONE.getTextureId(); else diffuse = BUtil_MiscUtil.tryTexture(diffusePath, TextureManager::tryTexture);
        if (normalPath == null || normalPath.isBlank()) normalMap = BoxDatabase.BUtil_Z.getTextureId(); else normalMap = BUtil_MiscUtil.tryTexture(normalPath, TextureManager::tryTextureChannel3);
        if (complexPath == null || complexPath.isBlank()) complex = BoxDatabase.BUtil_COMPLEX_DEF.getTextureId(); else complex = BUtil_MiscUtil.tryTexture(complexPath, TextureManager::tryTextureChannel3);
        if (emissivePath == null || emissivePath.isBlank()) emissive = BoxDatabase.BUtil_NONE.getTextureId(); else emissive = BUtil_MiscUtil.tryTexture(emissivePath, TextureManager::tryTexture);
        if (tangentPath == null || tangentPath.isBlank()) tangent = BoxDatabase.BUtil_X.getTextureId(); else tangent = BUtil_MiscUtil.tryTangentTexture(tangentPath, isAngleMap, true, false);

        _LOG.info("'BoxUtil' loaded common OBJ data with ID: '" + initID + "', at path: '" + objPath + "'.");
        _LOG.info("'BoxUtil' OBJ data ID: '" + initID + "' have vertices count: " + vertex.size() + " and triangles count: " + tri.size() + ".");
        return _MODEL_DATA.put(initID, new ModelData(initID, vertex, normal, uv, tri, diffuse, normalMap, complex, emissive, tangent, type));
    }

    private static Map<String, ModelData> wavefrontOBJCSVLoadCore(String path) throws JSONException, IOException {
        Map<String, ModelData> map = new Obj2ObjRHMap<>(16);
        JSONArray objDataArray = Global.getSettings().loadCSV(path);
        JSONObject objData;
        String objID, objPath, typeString;
        for (int i = 0; i < objDataArray.length(); i++) {
            objData = objDataArray.getJSONObject(i);
            objID = objData.optString("obj_id");
            objPath = objData.optString("obj_path");
            if (BUtil_MiscUtil.csvSkipAnnotationID(objID) || objID.isBlank() || objPath.isBlank()) continue;
            typeString = objData.optString("type").toUpperCase();
            boolean isAngleMap = objData.optBoolean("isTangentAngleMap", true);
            int type = GL30.GL_HALF_FLOAT;
            if (!typeString.isBlank()) type = typeString.contentEquals("F8") ? GL11.GL_BYTE : typeString.contentEquals("F32") ? GL11.GL_FLOAT : GL30.GL_HALF_FLOAT;
            ModelData obj = addModelData(objID, objPath,
                    objData.optString("diffuse_path", null),
                    objData.optString("normal_path", null),
                    objData.optString("complex_path", null),
                    objData.optString("emissive_path", null),
                    objData.optString("tangent_path", null),
                    isAngleMap, type);
            if (obj != null) map.put(objID, obj);
        }
        return map;
    }

    private static LegacyModelData getLegacyModelDataCore(String modelPath) throws IOException {
        String objFile = Global.getSettings().loadText(modelPath);
        if (objFile.getBytes().length > BoxDatabase.MAX_MODEL_FILE_SIZE) {
            _LOG.log(Level.WARN, "'BoxUtil' model file at '" + modelPath + "' was too bigger than 8 MiB.");
            return null;
        }
        BufferedReader reader = new BufferedReader(new StringReader(objFile));
        List<BUtil_Stack3f> vertex = new ArrayList<>();
        List<BUtil_Stack3f> vNormal = new ArrayList<>();
        List<BUtil_Stack2f> vUV = new ArrayList<>();
        List<BUtil_TriIndex> tri = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] splitString = line.split(" ");
                float x = Float.parseFloat(splitString[1]);
                float y = Float.parseFloat(splitString[2]);
                float z = Float.parseFloat(splitString[3]);
                vertex.add(new BUtil_Stack3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] splitString = line.split(" ");
                float x = Float.parseFloat(splitString[1]);
                float y = Float.parseFloat(splitString[2]);
                float z = Float.parseFloat(splitString[3]);
                vNormal.add(new BUtil_Stack3f(x, y, z));
            } else if (line.startsWith("vt ")) {
                String[] splitString = line.split(" ");
                float u = Float.parseFloat(splitString[1]);
                float v = Float.parseFloat(splitString[2]);
                vUV.add(new BUtil_Stack2f(u, v));
            } else if (line.startsWith("f ")) {
                String[] splitString = line.split(" ");
                String p1 = splitString[1];
                String p2 = splitString[2];
                String p3 = splitString[3];
                String p4 = splitString[splitString.length - 1];
                if (p3.contains(p4)) {
                    tri.add(new BUtil_TriIndex(p1, p2, p3));
                } else {
                    tri.add(new BUtil_TriIndex(p1, p2, p3));
                    tri.add(new BUtil_TriIndex(p3, p4, p1));
                }
            }
        }

        _LOG.info("'BoxUtil' loaded legacy OBJ data at path: '" + modelPath + "'.");
        return new LegacyModelData(vertex.toArray(new BUtil_Stack3f[0]), vNormal.toArray(new BUtil_Stack3f[0]), vUV.toArray(new BUtil_Stack2f[0]), tri.toArray(new BUtil_TriIndex[0]));
    }

    private ModelManager() {}
}
