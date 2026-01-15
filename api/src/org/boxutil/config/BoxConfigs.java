package org.boxutil.config;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.backends.core.BUtil_ThreadResource;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.KernelCore;
import org.boxutil.manager.ShaderCore;
import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import org.boxutil.backends.gui.BUtil_BaseConfigPanel;
import org.boxutil.backends.gui.BUtil_BaseTrackbar;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.backends.shader.BUtil_NotSelectedShaderPacks;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class BoxConfigs {
    // Global config values.
    private static boolean BUtil_EnableShader = true;
    private static boolean BUtil_EnableShaderLocal = true;
    private static boolean BUtil_EnableShaderDisplay = true;
    private static boolean BUtil_EnableCL = false;
    private static boolean BUtil_EnableCLLocal = false;
    private static boolean BUtil_EnableCLDisplay = false;
    private static short BUtil_CLDevice = 0;
    private static short BUtil_CLDeviceLocal = 0;
    private static short BUtil_CLDeviceDisplay = 0;

    // Dynamic config values.
    private static int BUtil_InstanceClamp = 8192;
    private static short BUtil_CurveNode = 32;
    private static short BUtil_CurveInterpolation = 32;
    private static boolean BUtil_EnableDistortion = true;
    private static boolean BUtil_EnableDistortionDisplay = true;

    // Misc config values.
    private final static boolean _SHOW_CN_TRANSLATION_CREDITS = Global.getSettings().getBoolean("showCNTranslationCredits");
    private static String BUtil_Language = _SHOW_CN_TRANSLATION_CREDITS ? BoxDatabase.ZH_CN :BoxDatabase.EN_US;
    private static boolean BUtil_EnableDebug = false;
    private static boolean BUtil_EnableDebugLocal = false;
    private static boolean BUtil_EnableDebugDisplay = false;

    // Dev config values.
    private static boolean BUtil_AAStatus = false;
    private static byte BUtil_MultiPassMode = BoxEnum.MP_BEAUTY;
    private static boolean BUtil_ShowMemoryPool = false;

    // Shader Packs values.
    private final static Set<BaseShaderPacksContext> _SHADER_PACKS_CONTEXTS = new LinkedHashSet<>(8);
    private final static BaseShaderPacksContext _BUtil_NotSelectedShadePacks = new BUtil_NotSelectedShaderPacks();
    private static BaseShaderPacksContext BUtil_ShaderPacksContext = _BUtil_NotSelectedShadePacks;

    private static boolean BUtil_BaseGL42Supported = false;
    private static boolean BUtil_BaseGL43Supported = false;
    private static boolean BUtil_GLDebugOutputSupported = false;
    private static boolean BUtil_TBOSupported = false;
    private static boolean BUtil_VAOSupported = false;

    private static boolean configInit = false;
    private static JSONObject data = null;

    static {
        _SHADER_PACKS_CONTEXTS.add(_BUtil_NotSelectedShadePacks);
    }

    public static String getRebootValueRealString(byte master, byte item) {
        String result = "BUtil_ConfigPanel_";
        boolean direct = false;
        switch (master) {
            case 0: {
                result += "Global_";
                switch (item) {
                    case 0: {
                        result = BUtil_EnableShader ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";
                        break;
                    }
                    case 1: {
                        result = BUtil_EnableCL ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";
                        break;
                    }
                    case 2: {
                        List<CLDevice> deviceList = KernelCore.getAllCLDevice();
                        result = (!deviceList.isEmpty() && KernelCore.isValid()) ? deviceList.get(Math.min(BUtil_CLDeviceDisplay, deviceList.size() - 1)).getInfoString(CL10.CL_DEVICE_NAME) : "NULL";
                        direct = true;
                    }
                }
                break;
            }
            case 2: {
                result += "Misc_";
                switch (item) {
                    case 1: {
                        result = BUtil_EnableDebug ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";
                    }
                }
            }
        }
        return direct ? result : getString(result);
    }

    public static Pair<String, Boolean> getValueString(byte master, byte item) {
        String result = "BUtil_ConfigPanel_";
        boolean valid = true;
        boolean direct = false;
        switch (master) {
            case 0: {
                result += "Global_";
                switch (item) {
                    case 0: {
                        result = BUtil_EnableShaderDisplay ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";

                        valid = isBaseGL43Supported();
                        break;
                    }
                    case 1: {
                        result = BUtil_EnableCLDisplay ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";
                        break;
                    }
                    case 2: {
                        List<CLDevice> deviceList = KernelCore.getAllCLDevice();
                        valid = !deviceList.isEmpty() && KernelCore.isValid();
                        result = valid ? deviceList.get(Math.min(BUtil_CLDeviceDisplay, deviceList.size() - 1)).getInfoString(CL10.CL_DEVICE_NAME) : "NULL";
                        direct = true;
                    }
                }
                break;
            }
            case 1: {
                result += "Common_";
                switch (item) {
                    case 0: {
                        direct = true;
                        result = String.valueOf(BUtil_InstanceClamp);
                        break;
                    }
                    case 1: {
                        direct = true;
                        result = String.valueOf(BUtil_CurveNode);
                        break;
                    }
                    case 2: {
                        direct = true;
                        result = String.valueOf(BUtil_CurveInterpolation);
                        break;
                    }
                    case 3: {
                        result = BUtil_EnableDistortionDisplay ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";

                        valid = ShaderCore.isDistortionValid();
                    }
                }
                break;
            }
            case 2: {
                result += "Misc_";
                switch (item) {
                    case 0: {
                        result += "00V";
                        break;
                    }
                    case 1: {
                        result = BUtil_EnableDebugDisplay ? "BUtil_ConfigPanel_ValueValid" : "BUtil_ConfigPanel_ValueInvalid";

                        valid = isGLDebugOutputSupported();
                    }
                }
            }
        }
        return new Pair<>(direct ? result : getString(result), valid);
    }

    public static void setValue(byte master, byte item, boolean right, BUtil_BaseTrackbar trackbar) {
        switch (master) {
            case 0: {
                switch (item) {
                    case 0: {
                        BUtil_EnableShaderDisplay = !BUtil_EnableShaderDisplay;
                        break;
                    }
                    case 1: {
                        BUtil_EnableCLDisplay = !BUtil_EnableCLDisplay;
                        break;
                    }
                    case 2: {
                        final short range = KernelCore.isValid() ? (short) KernelCore.getAllCLDevice().size() : 0;
                        if (range <= 1) {
                            BUtil_CLDeviceDisplay = 0;
                        } else {
                            if (right) ++BUtil_CLDeviceDisplay; else --BUtil_CLDeviceDisplay;
                            if (BUtil_CLDeviceDisplay < 0) BUtil_CLDeviceDisplay = (short) (range - 1);
                            if (BUtil_CLDeviceDisplay >= range) BUtil_CLDeviceDisplay = 0;
                        }
                    }
                }
                break;
            }
            case 1: {
                switch (item) {
                    case 0: {
                        if (trackbar != null) {
                            if (trackbar.getCurrStep() == trackbar.getMaxStep()) BUtil_InstanceClamp = -1;
                            else if (trackbar.getCurrStep() == 0) BUtil_InstanceClamp = 0;
                            else BUtil_InstanceClamp = 128 << trackbar.getCurrStep();
                        }
                        break;
                    }
                    case 1: {
                        if (trackbar != null) {
                            if (trackbar.getCurrStep() == trackbar.getMaxStep()) BUtil_CurveNode = -1;
                            else if (trackbar.getCurrStep() == 0) BUtil_CurveNode = 0;
                            else BUtil_CurveNode = (short) (4 << trackbar.getCurrStep());
                        }
                        break;
                    }
                    case 2: {
                        if (trackbar != null) {
                            if (trackbar.getCurrStep() == trackbar.getMaxStep()) BUtil_CurveInterpolation = -1;
                            else if (trackbar.getCurrStep() == 0) BUtil_CurveInterpolation = 0;
                            else BUtil_CurveInterpolation = (short) (8 << trackbar.getCurrStep());
                        }
                        break;
                    }
                    case 3: {
                        BUtil_EnableDistortionDisplay = !BUtil_EnableDistortionDisplay;
                    }
                }
                break;
            }
            case 2: {
                switch (item) {
                    case 0: {
                        if (BUtil_Language.contentEquals(BoxDatabase.ZH_CN)) BUtil_Language = BoxDatabase.EN_US;
                        else BUtil_Language = BoxDatabase.ZH_CN;
                        break;
                    }
                    case 1: {
                        BUtil_EnableDebugDisplay = !BUtil_EnableDebugDisplay;
                    }
                }
            }
        }
    }

    /**
     * Loading before all.
     */
    public static void init() {
        if (configInit) return;
        final ContextCapabilities cap = GLContext.getCapabilities();
        BUtil_BaseGL42Supported = cap.OpenGL42;
//        BUtil_BaseGL42Supported = cap.OpenGL42 && cap.GL_ARB_texture_non_power_of_two && cap.GL_ARB_texture_buffer_object && cap.GL_ARB_uniform_buffer_object && cap.GL_ARB_shader_subroutine && cap.GL_ARB_texture_storage;
        BUtil_BaseGL43Supported = cap.OpenGL43;
//        BUtil_GLParallelSupported = cap.OpenGL43 && cap.GL_ARB_compute_shader && cap.GL_ARB_shader_image_load_store && cap.GL_ARB_shader_storage_buffer_object;
        BUtil_GLDebugOutputSupported = cap.OpenGL43;
//        BUtil_GLDebugOutputSupported = cap.OpenGL43 && cap.GL_KHR_debug;
        BUtil_TBOSupported = cap.OpenGL31;
//        BUtil_TBOSupported = cap.OpenGL31 && cap.GL_ARB_texture_buffer_object;
        BUtil_VAOSupported = cap.OpenGL30;
//        BUtil_VAOSupported = cap.OpenGL30 && cap.GL_ARB_vertex_array_object && cap.GL_ARB_vertex_buffer_object;
        boolean haveData = true;
        try {
            data = new JSONObject(Global.getSettings().readTextFileFromCommon(BoxDatabase.CONFIG_FILE_PATH));
        } catch (IOException | JSONException ignored) {}
        if (data == null || data.length() <= 0) {
            haveData = false;
            data = new JSONObject();
            save();
        }
        if (haveData) {
            BUtil_EnableShader = data.optBoolean("BUtil_EnableShader", true);
            BUtil_EnableShaderLocal = BUtil_EnableShader;
            BUtil_EnableCL = data.optBoolean("BUtil_EnableCL", false);
            BUtil_EnableCLLocal = BUtil_EnableCL;
            BUtil_CLDevice = (short) data.optInt("BUtil_CLDevice", 0);
            BUtil_CLDeviceLocal = BUtil_CLDevice;

            BUtil_EnableDebug = data.optBoolean("BUtil_EnableDebug", false);
            BUtil_EnableDebugLocal = BUtil_EnableDebug;
            load();
        }
        configInit = true;
    }

    public static String getShaderPacksConfigFilePath(BaseShaderPacksContext context) {
        return BoxDatabase.SHADERPACKS_CONFIG_FILE_PATH + context.getID() + ".json";
    }

    public synchronized static void loadShaderPacksConfig(BaseShaderPacksContext context) {
        JSONObject file = null;
        try {
            file = new JSONObject(Global.getSettings().readTextFileFromCommon(getShaderPacksConfigFilePath(context)));
        } catch (IOException | JSONException ignored) {}
        if (file == null) saveShaderPacksConfig(context);
        context.configLoad(file);
    }

    public synchronized static void saveShaderPacksConfig(BaseShaderPacksContext context) {
        try {
            JSONObject file = new JSONObject();
            context.configSave(file);
            Global.getSettings().writeTextFileToCommon(getShaderPacksConfigFilePath(context), file.toString(4));
        } catch (IOException | JSONException ignored) {}
    }

    public synchronized static void initShaderPacks() {
        JSONObject file = null;
        try {
            file = new JSONObject(Global.getSettings().readTextFileFromCommon(BoxDatabase.SHADERPACKS_SELECT_FILE_PATH));
        } catch (IOException | JSONException ignored) {}

        if (file != null && file.length() > 0) {
            String id = file.optString("id", _BUtil_NotSelectedShadePacks.getID());
            if (BUtil_ShaderPacksContext == _BUtil_NotSelectedShadePacks && id.contentEquals(BUtil_ShaderPacksContext.getID())) {
                loadShaderPacksConfig(BUtil_ShaderPacksContext);
            } else for (BaseShaderPacksContext context : _SHADER_PACKS_CONTEXTS) {
                if (id.contentEquals(context.getID())) {
                    setShaderPacksContext(context);
                    break;
                }
            }
        } else saveShaderPacksID();
    }

    public synchronized static void saveShaderPacksID() {
        try {
            JSONObject file = new JSONObject();
            file.put("id", BUtil_ShaderPacksContext.getID());
            Global.getSettings().writeTextFileToCommon(BoxDatabase.SHADERPACKS_SELECT_FILE_PATH, file.toString(4));
        } catch (IOException | JSONException ignored) {}
    }

    public synchronized static void sysCheck() {
        if (!ShaderCore.isValid() || BUtil_InstanceDataMemoryPool.isNotSupported()) {
            BUtil_EnableShader = false;
        }
        if (!KernelCore.isValid()) {
            BUtil_EnableCL = false;
        }
    }

    /**
     * After all.
     */
    public synchronized static void check() {
        if (!ShaderCore.isDistortionValid()) {
            BUtil_EnableDistortion = false;
            BUtil_EnableDistortionDisplay = false;
        }
        if (!isGLDebugOutputSupported()) {
            BUtil_EnableDebug = false;
            BUtil_EnableDebugDisplay = false;
        }
    }

    public synchronized static void setDefault() {
        BUtil_EnableShaderDisplay = true;
        BUtil_EnableCLDisplay = false;
        BUtil_CLDeviceDisplay = 0;

        BUtil_InstanceClamp = 8192;
        BUtil_CurveNode = 32;
        BUtil_CurveInterpolation = 32;
        BUtil_EnableDistortionDisplay = true;

        BUtil_Language = _SHOW_CN_TRANSLATION_CREDITS ? BoxDatabase.ZH_CN : BoxDatabase.EN_US;
        BUtil_EnableDebugDisplay = false;
    }

    public synchronized static void load() {
        try {
            BUtil_EnableShaderDisplay = BUtil_EnableShaderLocal;
            BUtil_EnableCLDisplay = BUtil_EnableCLLocal;
            BUtil_CLDeviceDisplay = BUtil_CLDeviceLocal;

            {
                int value = data.optInt("BUtil_InstanceClamp", 8192);
                if (value > 65536 || value < -1) value = 8192;
                BUtil_InstanceClamp = value;
            }
            {
                short value = (short) data.optInt("BUtil_CurveNode", 32);
                if (value > 2048 || value < -1) value = 32;
                BUtil_CurveNode = value;
            }
            {
                short value = (short) data.optInt("BUtil_CurveInterpolation", 32);
                if (value > 4096 || value < -1) value = 32;
                BUtil_CurveInterpolation = value;
            }
            BUtil_EnableDistortionDisplay = data.getBoolean("BUtil_EnableDistortion");

            { // language check
                String rawValue = data.optString("BUtil_Language", BoxDatabase.EN_US);
                if (rawValue.contentEquals(BoxDatabase.ZH_CN)) {
                    BUtil_Language = BoxDatabase.ZH_CN;
                } else BUtil_Language = BoxDatabase.EN_US;
            }
            BUtil_EnableDebugDisplay = BUtil_EnableDebugLocal;
        } catch (JSONException e) {
            CommonUtil.printThrowable(BoxConfigs.class, "'BoxUtil' config load failed: ", e);
        }
    }

    public synchronized static void save() {
        try {
            data.put("BUtil_EnableShader", BUtil_EnableShaderDisplay);
            data.put("BUtil_EnableCL", BUtil_EnableCLDisplay);
            data.put("BUtil_CLDevice", BUtil_CLDeviceDisplay);

            data.put("BUtil_InstanceClamp", BUtil_InstanceClamp);
            data.put("BUtil_CurveNode", BUtil_CurveNode);
            data.put("BUtil_CurveInterpolation", BUtil_CurveInterpolation);
            data.put("BUtil_EnableDistortion", BUtil_EnableDistortionDisplay);
            BUtil_EnableDistortion = BUtil_EnableDistortionDisplay;

            data.put("BUtil_Language", BUtil_Language);
            data.put("BUtil_EnableDebug", BUtil_EnableDebugDisplay);
            Global.getSettings().writeTextFileToCommon(BoxDatabase.CONFIG_FILE_PATH, data.toString(4));
        } catch (IOException | JSONException e) {
            CommonUtil.printThrowable(BoxConfigs.class, "'BoxUtil' config save failed: ", e);
        }
    }

    public static boolean isConfigInit() {
        return configInit;
    }

    @Deprecated
    public static boolean isBaseGL42Supported() {
        return BUtil_BaseGL42Supported;
    }

    @Deprecated
    public static boolean isGLParallelSupported() {
        return BUtil_BaseGL43Supported;
    }

    public static boolean isBaseGL43Supported() {
        return BUtil_BaseGL43Supported;
    }

    public static boolean isGLDebugOutputSupported() {
        return BUtil_GLDebugOutputSupported;
    }

    public static boolean isTBOSupported() {
        return BUtil_TBOSupported;
    }

    public static boolean isVAOSupported() {
        return BUtil_VAOSupported;
    }

    public static int getMaxInstanceDataSize() {
        return BUtil_InstanceClamp == -1 ? BoxDatabase.getGLState().DEVICE_MAX_INSTANCE_DATA_SIZE : Math.min(BoxDatabase.getGLState().DEVICE_MAX_INSTANCE_DATA_SIZE, BUtil_InstanceClamp);
    }

    public static int getMaxSegmentNodeSize() {
        return getMaxInstanceDataSize() / 2;
    }

    public static boolean isShaderEnable() {
        return BUtil_EnableShader;
    }

    public static boolean isCLEnable() {
        return BUtil_EnableCL;
    }

    public static short getCLDeviceIndex() {
        return BUtil_CLDevice;
    }

    public static short getMaxCurveNodeSize() {
        return BUtil_CurveNode == -1 ? Short.MAX_VALUE : BUtil_CurveNode;
    }

    public static short getMaxCurveInterpolation() {
        return BUtil_CurveInterpolation == -1 ? Short.MAX_VALUE : BUtil_CurveInterpolation;
    }

    public static boolean isDistortionEnable() {
        return BUtil_EnableDistortion;
    }

    @Deprecated
    public static boolean isGLParallel() {
        return true;
    }

    @Deprecated
    public static boolean isJVMParallel() {
        return false;
    }

    public static String getLanguage() {
        return BUtil_Language;
    }

    public static boolean isGLDebugEnable() {
        return BUtil_EnableDebug;
    }

    public static byte getMultiPassMode() {
        return BUtil_MultiPassMode;
    }

    public static boolean isMultiPassBeauty() {
        return BUtil_MultiPassMode == BoxEnum.MP_BEAUTY;
    }

    public static boolean isMultiPassColor() {
        return BUtil_MultiPassMode == BoxEnum.MP_COLOR;
    }

    public static boolean isMultiPassEmissive() {
        return BUtil_MultiPassMode == BoxEnum.MP_EMISSIVE;
    }

    public static boolean isMultiPassPosition() {
        return BUtil_MultiPassMode == BoxEnum.MP_POSITION;
    }

    public static boolean isMultiPassNormal() {
        return BUtil_MultiPassMode == BoxEnum.MP_NORMAL;
    }

    public static boolean isMultiPassTangent() {
        return BUtil_MultiPassMode == BoxEnum.MP_TANGENT;
    }

    public static boolean isMultiPassMaterial() {
        return BUtil_MultiPassMode == BoxEnum.MP_MATERIAL;
    }

    public static boolean isMultiPassBloom() {
        return BUtil_MultiPassMode == BoxEnum.MP_BLOOM;
    }

    public static void setMultiPassMode(byte mode) {
        BUtil_MultiPassMode = mode;
    }

    public static boolean isAAShowEdge() {
        return BUtil_AAStatus;
    }

    public static void setAAShowEdge(boolean value) {
        BUtil_AAStatus = value;
    }

    public static boolean isShowInstanceMemoryUsage() {
        return BUtil_ShowMemoryPool;
    }

    public static void setShowInstanceMemoryUsage(boolean value) {
        BUtil_ShowMemoryPool = value;
    }

    public static BaseShaderPacksContext getCurrShaderPacksContext() {
        return BUtil_ShaderPacksContext;
    }

    public synchronized static boolean setShaderPacksContext(BaseShaderPacksContext context) {
        if (BUtil_ShaderPacksContext == context || !_SHADER_PACKS_CONTEXTS.contains(context) || !context.isUsable(GLContext.getCapabilities())) return true;
        BUtil_ThreadResource.Rendering.Combat._cleanupIlluminant();
        BUtil_ThreadResource.Rendering.Campaign._cleanupIlluminant();
        BUtil_ShaderPacksContext.destroy();
        loadShaderPacksConfig(context);
        BUtil_GLImpl.Operations.callIlluminantCleanupForShaderPacks(BUtil_ShaderPacksContext, context);
        final boolean success = context.init();
        if (success) {
            BUtil_ShaderPacksContext = context;
        } else {
            BUtil_ShaderPacksContext = _BUtil_NotSelectedShadePacks;
            BUtil_ShaderPacksContext.init();
            context.destroy();
            Global.getLogger(BoxConfigs.class).error("'BoxUtil' shader packs: '" + context.getDisplayName() + "' with id: '" + context.getID() + "' init failed.");
        }
        if (Global.getCurrentState() != GameState.CAMPAIGN || (Global.getCombatEngine() != null && Global.getCombatEngine().isSimulation())) {
            BUtil_ShaderPacksContext.initCombat(Global.getCombatEngine());
            if (!success) BUtil_BaseConfigPanel.callShaderPacksInitFailed(BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Falied").replace("%s", context.getDisplayName()));
        }
        return success;
    }

    public static Set<BaseShaderPacksContext> getLoadedShaderPacksSetCopy() {
        return new LinkedHashSet<>(_SHADER_PACKS_CONTEXTS);
    }

    public static boolean containsShaderPacks(BaseShaderPacksContext context) {
        return _SHADER_PACKS_CONTEXTS.contains(context);
    }

    /**
     * For add your shader packs (recommend):
     * <pre>
     * {@code
     * public void onApplicationLoad() {
     *     // init call for BoxUtil...
     *
     *     BoxConfigs.addShaderPacks(new YourShaderPacksContextImpl());
     * }
     * }
     * </pre>
     */
    public synchronized static void addShaderPacks(@NotNull BaseShaderPacksContext context) {
        if (context == null || _SHADER_PACKS_CONTEXTS.contains(context)) return;
        if (context.haveCustomInstanceDataLayout() && context.getCustomInstanceDataLayout() == null) throw new IllegalStateException("'BoxUtil' the shader packs ID: '" + context.getID() + "' and name: '" + context.getDisplayName() + "' have custom instance data layout for illuminant, but returns a illegal layout API.");
        _SHADER_PACKS_CONTEXTS.add(context);
        BUtil_BaseConfigPanel.refreshShaderPacksList();
    }

    /**
     * Pick string by current language suffix.<p>
     * Pick suffix: {@link BoxDatabase#ZH_CN}, {@link BoxDatabase#EN_US}.<p>
     * Fixed suffix: {@link BoxDatabase#NONE_LANG}.
     */
    public static String getString(String category, String id) {
        return Global.getSettings().getString(category, !id.endsWith(BoxDatabase.NONE_LANG) ? id + BoxConfigs.getLanguage() : id);
    }

    /**
     * Pick string by current language suffix.<p>
     * {@link BoxDatabase#ZH_CN}, {@link BoxDatabase#EN_US}
     */
    public static String getString(String id) {
        return getString("ui", id);
    }

    private BoxConfigs() {}
}
