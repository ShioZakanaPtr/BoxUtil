package org.boxutil.define;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.apache.log4j.Logger;
import org.boxutil.manager.KernelCore;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lazywizard.console.Console;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLDeviceCapabilities;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

public final class BoxDatabase {
    public final static String MOD_ID = "BoxUtil";
    public final static String CAMPAIGN_MANAGE_ID = "BUtil_CampaignRenderingManager";
    public final static String CAMPAIGN_MANAGE_TAG = "BUtil_CampaignRenderingManager";
    public final static String BUILTIN_OBJ_CSV = "data/config/modFiles/BUtil_obj_data.csv";
    public final static String BUILTIN_TEXTURE_CSV = "data/config/modFiles/BUtil_texture_data.csv";
    public final static String BUILTIN_ILLUMINANT_CSV = "data/config/modFiles/BUtil_illuminant_data.csv";
    public final static String CONFIG_FILE_PATH = "BUtil_Configs.json";
    public final static String SHADERPACKS_CONFIG_FILE_PATH = "shaderpacks/";
    public final static String SHADERPACKS_SELECT_FILE_PATH = "shaderpacks/BUtil_SelectedShaderPacks.json";
    public final static String[] AnisotropicLTC = new String[]{"data/luts/BUtil_AnisotropicLTC_M0.lut", "data/luts/BUtil_AnisotropicLTC_M1.lut", "data/luts/BUtil_AnisotropicLTC_M2.lut"};
    public final static String[] IsotropicLTC = new String[]{"data/luts/BUtil_IsotropicLTC_InvMatrix.lut", "data/luts/BUtil_IsotropicLTC_Integral.lut", "data/luts/BUtil_IsotropicLTC_Clipping.lut", "data/luts/BUtil_IsotropicLTC_Magnitude.lut"};
    public final static String[] KullaContyBRDF = new String[]{"data/luts/BUtil_KullaContyBRDF_EMu.lut", "data/luts/BUtil_KullaContyBRDF_EAvg.lut"};
    public final static byte DOUBLE_SIZE = 8;
    public final static byte LONG_SIZE = 8;
    public final static byte FLOAT_SIZE = 4;
    public final static byte INT_SIZE = 4;
    public final static byte FIXED_SIZE = 4;
    public final static byte HALF_FLOAT_SIZE = 2;
    public final static byte SHORT_SIZE = 2;
    public final static byte BYTE_SIZE = 1;
    public final static int MAX_MODEL_FILE_SIZE = 0x800000; // bytes, 8MB limited for default.
    public final static String NONE_LANG = "_NL";
    public final static String ZH_CN = "_zh_cn";
    public final static String EN_US = "_en_us";

    public final static byte ENTITY_TOTAL_TYPE_NORMAL = 8;
    public final static byte ENTITY_TOTAL_TYPE_DIRECT = 6;
    public final static byte ENTITY_TOTAL_TYPE_DIRECT_COMMON = 1;
    public final static byte ENTITY_TOTAL_TYPE_ILLUMINANT = 5;

    public final static Color DEFAULT_GREEN = Global.getSettings().getColor("BUtil_DefaultGreen");
    public final static Vector4f DEFAULT_GREEN_VEC = CommonUtil.colorNormalization4f(DEFAULT_GREEN, null);
    public final static Color DEFAULT_BACKGROUND = Global.getSettings().getColor("BUtil_DefaultBG");
    public final static Vector4f DEFAULT_BACKGROUND_VEC = CommonUtil.colorNormalization4f(DEFAULT_BACKGROUND, null);

    public final static float HALF_FLOAT_MAX_VALUE = 65504.0f;
    public final static float HALF_FLOAT_MIN_VALUE = 0x1.0p-14f;
    public final static short[] HALF_FLOAT_MAX_BIT_ARRAY = new short[]{0b11111111, 0b1111011};
    public final static short[] HALF_FLOAT_MIN_NORMAL_BIT_ARRAY = new short[]{0, 0b100};
    public final static short[] HALF_FLOAT_MIN_SUB_NORMAL_BIT_ARRAY = new short[]{0b1, 0};
    public final static int HALF_FLOAT_SIGN_MASK = 0b10000000000000000000000000000000;
    public final static int HALF_FLOAT_EXP_MASK = 0b1000111100000000000000000000000;
    public final static int HALF_FLOAT_FRACTION_MASK = 0b11111111110000000000000;
    public final static int HALF_FLOAT_FULL_MASK = HALF_FLOAT_SIGN_MASK | HALF_FLOAT_EXP_MASK | HALF_FLOAT_FRACTION_MASK;
    public final static int HALF_FLOAT_BYTE_LEFT_A_MASK = 0b11000000000000000000000000000000;
    public final static int HALF_FLOAT_BYTE_LEFT_B_MASK = 0b111111000000000000000000000;
    public final static int HALF_FLOAT_BYTE_RIGHT_MASK = 0b111111110000000000000;

    public final static SpriteAPI BUtil_TILES = Global.getSettings().getSprite("textures", "BUtil_TestTiles");
    public final static SpriteAPI BUtil_NONE = Global.getSettings().getSprite("textures", "BUtil_NONE");
    public final static SpriteAPI BUtil_ONE = Global.getSettings().getSprite("textures", "BUtil_ONE");
    public final static SpriteAPI BUtil_MID = Global.getSettings().getSprite("textures", "BUtil_MID");
    public final static SpriteAPI BUtil_COMPLEX_DEF = Global.getSettings().getSprite("textures", "BUtil_COMPLEX_DEF");
    public final static SpriteAPI BUtil_ZERO = Global.getSettings().getSprite("textures", "BUtil_ZERO");
    public final static SpriteAPI BUtil_X = Global.getSettings().getSprite("textures", "BUtil_X");
    public final static SpriteAPI BUtil_Z = Global.getSettings().getSprite("textures", "BUtil_Z");

    public final static SpriteAPI BUtil_DefaultShaderPacksIcon = Global.getSettings().getSprite("icons", "BUtil_DefaultShaderPacksIcon");
    public final static String DEFAULT_SHADERPACKS_ID = "BUtil_NotSelectedShaderPacks";

    public final static String[] PASS_SHADING_FLAG = new String[]{"ogl_PassShading", "dweller"};
    public final static String PASS_SHADING_FLAG_DEFAULT = PASS_SHADING_FLAG[0];
    public final static String[] NORMAL_NO_AUTOGEN_FLAG = new String[]{"ogl_NoAutoNormal", "graphicslib_no_autogen"};
    public final static String NORMAL_NO_AUTOGEN_FLAG_DEFAULT = NORMAL_NO_AUTOGEN_FLAG[0];
    public final static String[] NONE_ILLUMINANT_FLAG = new String[]{"ogl_NoneIlluminant", "dweller"};
    public final static String NONE_ILLUMINANT_FLAG_DEFAULT = NONE_ILLUMINANT_FLAG[0];

    public final static String[] SHADING_TEXTURE_DATA_TYPE = new String[]{"ENTITY", "MISSILE", "TURRET", "HARDPOINT"};
    public final static String[] SHADING_TEXTURE_DATA_SUBTYPE = new String[]{"BASE", "BARREL", "UNDER", "COVERSMALL", "COVERMEDIUM", "COVERLARGE"};
    public final static String[] SHADING_ILLUMINANT_DATA_TYPE = new String[]{"PROJECTILE", "BEAM", "ENGINE"};

    private static GLState _glState = null;

    public static void initGLState() {
        if (_glState == null) _glState = new GLState();
    }

    public static GLState getGLState() {
        return _glState;
    }

    public final static class GLState {
        private final static Logger _LOG = Global.getLogger(GLState.class);

        public final String GL_CURRENT_DEVICE_NAME;
        public final String GL_CURRENT_DEVICE_VERSION;
        public final String GL_CURRENT_DEVICE_VENDOR_NAME;
        public final byte GL_CURRENT_DEVICE_VENDOR_BYTE;
        public final boolean GL_GL15;
        public final boolean GL_GL20;
        public final boolean GL_GL30;
        public final boolean GL_GL42;
        public final boolean GL_GL43;
        public final boolean GL_GL44;
        public final boolean GL_GL45;
        public final boolean GL_VBO;
        public final boolean GL_FBO;
        public final boolean GL_TBO;
        public final boolean GL_UBO;
        public final boolean GL_SSBO;
        public final boolean GL_SUBROUTINE;
        public final boolean GL_COMPUTE_SHADER;
        public final boolean GL_IMAGE_LOAD_STORE;
        public final boolean GL_BINDLESS_TEXTURE; // since opengl4.0, not core
        public final boolean NV_BINDLESS_TEXTURE; // since opengl4.0, not core
        public final boolean GL_NPOT_TEXTURE;
        public final boolean GL_TEXTURE_STORAGE;
        public final boolean GL_CL_EVENT;
        public final boolean GL_SYNC;
        public final boolean GL_CORE_SYNC;
        public final boolean ARB_shading_language_include;

        public final int MAX_TEXTURE_BUFFER_SIZE;
        public final int MAX_UNIFORM_BLOCK_SIZE;
        public final long MAX_SHADER_STORAGE_BLOCK_SIZE;
        public final long MAX_SHADER_STORAGE_BUFFER_BINDINGS;
        public final long MAX_VERTEX_SHADER_STORAGE_BLOCKS;
        public final int MAX_TEXTURE_SIZE; // component, or 1D total
        public final long MAX_TEXTURE2D_PIXELS; // total
        public final int MAX_TEXTURE3D_SIZE; // component
        public final long MAX_TEXTURE3D_PIXELS; // total
        public final int MAX_TEXTURE_UNITS; // 32 for modern gpu, in fragment shader; GL11.GL_MAX_TEXTURE_UNITS for the fixed pipeline which is deprecated.
        public final int MAX_IMAGE_UNITS;
        public final int MAX_VERTEX_IMAGE_UNIFORMS;
        public final int MAX_TESS_CONTROL_IMAGE_UNIFORMS;
        public final int MAX_TESS_EVALUATION_IMAGE_UNIFORMS;
        public final int MAX_GEOMETRY_IMAGE_UNIFORMS;
        public final int MAX_FRAGMENT_IMAGE_UNIFORMS;
        public final int MAX_COMPUTE_IMAGE_UNIFORMS;
        public final long GL_MAX_WORK_GROUP_SIZE_X; // for CPU
        public final long GL_MAX_WORK_GROUP_SIZE_Y; // for CPU
        public final long GL_MAX_WORK_GROUP_SIZE_Z; // for CPU
        public final int GL_MAX_LOCAL_SIZE_X; // Local, component
        public final int GL_MAX_LOCAL_SIZE_Y; // Local, component
        public final int GL_MAX_LOCAL_SIZE_Z; // Local, component
        public final int GL_MAX_WORK_ITEMS; // Local total, x * y * z

        public final int DEVICE_MAX_INSTANCE_DATA_SIZE;
        public final boolean BOXUTIL_VALID;

        public GLState() {
            final ContextCapabilities cap = GLContext.getCapabilities();
            this.GL_CURRENT_DEVICE_NAME = GL11.glGetString(GL11.GL_RENDERER);
            this.GL_CURRENT_DEVICE_VERSION = GL11.glGetString(GL11.GL_VERSION);
            this.GL_CURRENT_DEVICE_VENDOR_NAME = GL11.glGetString(GL11.GL_VENDOR);
            final String checkVendor = this.GL_CURRENT_DEVICE_VENDOR_NAME;
            if (checkVendor.contains("AMD") || checkVendor.contains("ATI")) this.GL_CURRENT_DEVICE_VENDOR_BYTE = BoxEnum.GL_DEVICE_AMD_ATI;
            else if (checkVendor.contains("NVIDIA")) this.GL_CURRENT_DEVICE_VENDOR_BYTE = BoxEnum.GL_DEVICE_NVIDIA;
            else if (checkVendor.contains("Intel")) this.GL_CURRENT_DEVICE_VENDOR_BYTE = BoxEnum.GL_DEVICE_INTEL;
            else this.GL_CURRENT_DEVICE_VENDOR_BYTE = BoxEnum.GL_DEVICE_OTHER;
            this.GL_GL15 = cap.OpenGL15;
            this.GL_GL20 = cap.OpenGL20;
            this.GL_GL30 = cap.OpenGL30;
            this.GL_GL42 = cap.OpenGL42;
            this.GL_GL43 = cap.OpenGL43;
            this.GL_GL44 = cap.OpenGL44;
            this.GL_GL45 = cap.OpenGL45;
            this.GL_VBO = cap.OpenGL15;
            this.GL_FBO = cap.OpenGL30;
            this.GL_TBO = cap.OpenGL31;
            this.GL_UBO = cap.OpenGL31;
            this.GL_SSBO = cap.OpenGL43;
            this.GL_SUBROUTINE = cap.OpenGL40;
            this.GL_COMPUTE_SHADER = cap.OpenGL43;
            this.GL_IMAGE_LOAD_STORE = cap.OpenGL42;
//            this.GL_VBO = cap.GL_ARB_vertex_buffer_object && cap.OpenGL15;
//            this.GL_FBO = cap.GL_ARB_framebuffer_object && cap.OpenGL30;
//            this.GL_TBO = cap.GL_ARB_texture_buffer_object && cap.OpenGL31;
//            this.GL_UBO = cap.GL_ARB_uniform_buffer_object && cap.OpenGL31;
//            this.GL_SSBO = cap.GL_ARB_shader_storage_buffer_object && cap.OpenGL43;
//            this.GL_SUBROUTINE = cap.GL_ARB_shader_subroutine && cap.OpenGL40;
//            this.GL_COMPUTE_SHADER = cap.GL_ARB_compute_shader && cap.OpenGL43;
//            this.GL_IMAGE_LOAD_STORE = cap.GL_ARB_shader_image_load_store && cap.OpenGL42;
            this.GL_BINDLESS_TEXTURE = cap.GL_ARB_bindless_texture || cap.GL_NV_bindless_texture;
            this.NV_BINDLESS_TEXTURE = cap.GL_NV_bindless_texture;
            this.GL_NPOT_TEXTURE = cap.OpenGL20;
            this.GL_TEXTURE_STORAGE = cap.OpenGL42;
//            this.GL_NPOT_TEXTURE = cap.GL_ARB_texture_non_power_of_two && cap.OpenGL20;
//            this.GL_TEXTURE_STORAGE = cap.GL_ARB_texture_storage && cap.OpenGL42;
            this.GL_CL_EVENT = cap.GL_ARB_cl_event;
            this.GL_SYNC = cap.GL_ARB_sync && cap.OpenGL32;
            this.GL_CORE_SYNC = cap.OpenGL32;
            this.ARB_shading_language_include = cap.GL_ARB_shading_language_include;

            this.MAX_TEXTURE_BUFFER_SIZE = cap.OpenGL31 ? GL11.glGetInteger(GL31.GL_MAX_TEXTURE_BUFFER_SIZE) : 0;
            this.MAX_UNIFORM_BLOCK_SIZE = cap.OpenGL31 ? GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BLOCK_SIZE) : 0;
            this.MAX_SHADER_STORAGE_BLOCK_SIZE = cap.OpenGL43 ? GL32.glGetInteger64(GL43.GL_MAX_SHADER_STORAGE_BLOCK_SIZE) : 0;
            this.MAX_SHADER_STORAGE_BUFFER_BINDINGS = cap.OpenGL43 ? GL11.glGetInteger(GL43.GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS) : 0;
            this.MAX_VERTEX_SHADER_STORAGE_BLOCKS = cap.OpenGL43 ? GL11.glGetInteger(GL43.GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS) : 0;
            this.MAX_TEXTURE_SIZE = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
            this.MAX_TEXTURE2D_PIXELS = ((long) this.MAX_TEXTURE_SIZE) * ((long) this.MAX_TEXTURE_SIZE);
            this.MAX_TEXTURE3D_SIZE = cap.OpenGL12 ? GL11.glGetInteger(GL12.GL_MAX_3D_TEXTURE_SIZE) : 0;
            this.MAX_TEXTURE3D_PIXELS = ((long) this.MAX_TEXTURE3D_SIZE) * ((long) this.MAX_TEXTURE3D_SIZE) * ((long) this.MAX_TEXTURE3D_SIZE);
            this.MAX_TEXTURE_UNITS = cap.OpenGL20 ? GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS) : 0;
            this.MAX_IMAGE_UNITS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_IMAGE_UNITS) : 0;
            this.MAX_VERTEX_IMAGE_UNIFORMS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_VERTEX_IMAGE_UNIFORMS) : 0;
            this.MAX_TESS_CONTROL_IMAGE_UNIFORMS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS) : 0;
            this.MAX_TESS_EVALUATION_IMAGE_UNIFORMS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS) : 0;
            this.MAX_GEOMETRY_IMAGE_UNIFORMS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_GEOMETRY_IMAGE_UNIFORMS) : 0;
            this.MAX_FRAGMENT_IMAGE_UNIFORMS = cap.OpenGL42 ? GL11.glGetInteger(GL42.GL_MAX_FRAGMENT_IMAGE_UNIFORMS) : 0;
            this.MAX_COMPUTE_IMAGE_UNIFORMS = cap.OpenGL43 ? GL11.glGetInteger(GL43.GL_MAX_COMPUTE_IMAGE_UNIFORMS) : 0;
            this.GL_MAX_WORK_GROUP_SIZE_X = cap.OpenGL43 ? GL32.glGetInteger64(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0) : 0;
            this.GL_MAX_WORK_GROUP_SIZE_Y = cap.OpenGL43 ? GL32.glGetInteger64(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1) : 0;
            this.GL_MAX_WORK_GROUP_SIZE_Z = cap.OpenGL43 ? GL32.glGetInteger64(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2) : 0;
            this.GL_MAX_LOCAL_SIZE_X = cap.OpenGL43 ? GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0) : 0;
            this.GL_MAX_LOCAL_SIZE_Y = cap.OpenGL43 ? GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1) : 0;
            this.GL_MAX_LOCAL_SIZE_Z = cap.OpenGL43 ? GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2) : 0;
            this.GL_MAX_WORK_ITEMS = cap.OpenGL43 ? GL11.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS) : 0;

            int maxInstanceDataSize = 0;
            for (InstanceType type : InstanceType.values()) maxInstanceDataSize = Math.max(maxInstanceDataSize, type.getSize());
            if (maxInstanceDataSize < 1) maxInstanceDataSize = 1;
            this.DEVICE_MAX_INSTANCE_DATA_SIZE = Math.min(Integer.MAX_VALUE / maxInstanceDataSize, Math.toIntExact(MAX_SHADER_STORAGE_BLOCK_SIZE / maxInstanceDataSize));
            this.BOXUTIL_VALID = this.GL_GL43 && this.GL_GL42 && cap.OpenGL41 && cap.OpenGL40 && cap.OpenGL32 && cap.OpenGL31 && cap.OpenGL30 && cap.OpenGL20;
        }

        public String getPrintInfo() {
            return "Current device OpenGL context capabilities:"
                    + "\nGL_CURRENT_DEVICE_NAME: " + this.GL_CURRENT_DEVICE_NAME
                    + "\nGL_CURRENT_DEVICE_VERSION: " + this.GL_CURRENT_DEVICE_VERSION
                    + "\nGL_CURRENT_DEVICE_VENDOR_NAME: " + this.GL_CURRENT_DEVICE_VENDOR_NAME
                    + "\nGL_CURRENT_DEVICE_VENDOR_BYTE: " + this.GL_CURRENT_DEVICE_VENDOR_BYTE
                    + "\nGL_GL15: " + this.GL_GL15
                    + "\nGL_GL20: " + this.GL_GL20
                    + "\nGL_GL30: " + this.GL_GL30
                    + "\nGL_GL42: " + this.GL_GL42
                    + "\nGL_GL43: " + this.GL_GL43
                    + "\nGL_GL44: " + this.GL_GL44
                    + "\nGL_GL45: " + this.GL_GL45
                    + "\nGL_CORE_vertex_buffer_object: " + this.GL_VBO
                    + "\nGL_CORE_framebuffer_object: " + this.GL_FBO
                    + "\nGL_CORE_texture_buffer_object: " + this.GL_TBO
                    + "\nGL_CORE_uniform_buffer_object: " + this.GL_UBO
                    + "\nGL_CORE_shader_storage_buffer_object: " + this.GL_SSBO
                    + "\nGL_CORE_shader_subroutine: " + this.GL_SUBROUTINE
                    + "\nGL_CORE_COMPUTE_SHADER: " + this.GL_COMPUTE_SHADER
                    + "\nGL_CORE_IMAGE_LOAD_STORE: " + this.GL_IMAGE_LOAD_STORE
                    + "\nGL_ARB_OR_NV_BINDLESS_TEXTURE: " + this.GL_BINDLESS_TEXTURE
                    + "\nNV_BINDLESS_TEXTURE: " + this.NV_BINDLESS_TEXTURE
                    + "\nGL_CORE_NPOT_TEXTURE: " + this.GL_NPOT_TEXTURE
                    + "\nGL_CORE_TEXTURE_STORAGE: " + this.GL_TEXTURE_STORAGE
                    + "\nGL_ARB_CL_EVENT: " + this.GL_CL_EVENT
                    + "\nGL_SYNC: " + this.GL_SYNC
                    + "\nGL_CORE_SYNC: " + this.GL_CORE_SYNC
                    + "\nGL_ARB_shading_language_include: " + this.ARB_shading_language_include + '\n'

                    + "\nMAX_TEXTURE_BUFFER_SIZE: " + this.MAX_TEXTURE_BUFFER_SIZE
                    + "\nMAX_UNIFORM_BLOCK_SIZE: " + this.MAX_UNIFORM_BLOCK_SIZE
                    + "\nMAX_SHADER_STORAGE_BLOCK_SIZE: " + this.MAX_SHADER_STORAGE_BLOCK_SIZE
                    + "\nMAX_SHADER_STORAGE_BUFFER_BINDINGS: " + this.MAX_SHADER_STORAGE_BUFFER_BINDINGS
                    + "\nMAX_VERTEX_SHADER_STORAGE_BLOCKS: " + this.MAX_VERTEX_SHADER_STORAGE_BLOCKS
                    + "\nMAX_TEXTURE_SIZE: " + this.MAX_TEXTURE_SIZE
                    + "\nMAX_TEXTURE2D_PIXELS: " + this.MAX_TEXTURE2D_PIXELS
                    + "\nMAX_TEXTURE3D_SIZE: " + this.MAX_TEXTURE3D_SIZE
                    + "\nMAX_TEXTURE3D_PIXELS: " + this.MAX_TEXTURE3D_PIXELS
                    + "\nMAX_TEXTURE_UNITS: " + this.MAX_TEXTURE_UNITS
                    + "\nMAX_IMAGE_UNITS: " + this.MAX_IMAGE_UNITS
                    + "\nMAX_VERTEX_IMAGE_UNIFORMS: " + this.MAX_VERTEX_IMAGE_UNIFORMS
                    + "\nMAX_TESS_CONTROL_IMAGE_UNIFORMS: " + this.MAX_TESS_CONTROL_IMAGE_UNIFORMS
                    + "\nMAX_TESS_EVALUATION_IMAGE_UNIFORMS: " + this.MAX_TESS_EVALUATION_IMAGE_UNIFORMS
                    + "\nMAX_GEOMETRY_IMAGE_UNIFORMS: " + this.MAX_GEOMETRY_IMAGE_UNIFORMS
                    + "\nMAX_FRAGMENT_IMAGE_UNIFORMS: " + this.MAX_FRAGMENT_IMAGE_UNIFORMS
                    + "\nMAX_COMPUTE_IMAGE_UNIFORMS: " + this.MAX_COMPUTE_IMAGE_UNIFORMS
                    + "\nGL_MAX_WORK_GROUP_SIZE_X: " + this.GL_MAX_WORK_GROUP_SIZE_X
                    + "\nGL_MAX_WORK_GROUP_SIZE_Y: " + this.GL_MAX_WORK_GROUP_SIZE_Y
                    + "\nGL_MAX_WORK_GROUP_SIZE_Z: " + this.GL_MAX_WORK_GROUP_SIZE_Z
                    + "\nGL_MAX_LOCAL_SIZE_X: " + this.GL_MAX_LOCAL_SIZE_X
                    + "\nGL_MAX_LOCAL_SIZE_Y: " + this.GL_MAX_LOCAL_SIZE_Y
                    + "\nGL_MAX_LOCAL_SIZE_Z: " + this.GL_MAX_LOCAL_SIZE_Z
                    + "\nGL_MAX_WORK_ITEMS: " + this.GL_MAX_WORK_ITEMS + '\n'

                    + "\nMAX_INSTANCE_DATA_TEX_SIZE: " + this.DEVICE_MAX_INSTANCE_DATA_SIZE
                    + "\nBOXUTIL_VALID: " + this.BOXUTIL_VALID;
        }

        public void print() {
            _LOG.info(this.getPrintInfo());
        }
    }

    private static CLState _clState = null;

    public static void initCLState() {
        if (_clState == null && KernelCore.isValid()) _clState = new CLState();
    }

    public static @Nullable CLState getCLState() {
        return _clState;
    }

    public static final class CLState {
        private final static Logger _LOG = Global.getLogger(CLState.class);

        public final String CL_CURRENT_DEVICE_NAME;
        public final String CL_CURRENT_DEVICE_VERSION;
        public final String CL_CURRENT_DEVICE_VENDOR_NAME;
        public final int CL_CURRENT_DEVICE_TYPE;
        public final boolean CL_CL11;
        public final boolean CL_CL12;
        public final boolean KHR_FP16;
        public final boolean KHR_FP64;

        public final int MAX_IMAGE2D_WIDTH;
        public final int MAX_IMAGE2D_HEIGHT;
        public final long MAX_IMAGE2D_PIXELS;
        public final int MAX_IMAGE3D_WIDTH;
        public final int MAX_IMAGE3D_HEIGHT;
        public final int MAX_IMAGE3D_DEPTH;
        public final long MAX_IMAGE3D_PIXELS;
        public final int CL_MAX_WORK_GROUP_SIZE;
        public final int CL_MAX_WORK_ITEM_SIZE;
        public final int CL_MAX_LOCAL_DIMENSIONS;
        public final int CL_MAX_COMPUTE_UNITS;

        public CLState() {
            CLDevice device = KernelCore.getCurrentCLDevice();
            CLDeviceCapabilities cap = CLCapabilities.getDeviceCapabilities(device);
            this.CL_CURRENT_DEVICE_NAME = device.getInfoString(CL10.CL_DEVICE_NAME);
            this.CL_CURRENT_DEVICE_VERSION = device.getInfoString(CL10.CL_DEVICE_VERSION);
            this.CL_CURRENT_DEVICE_VENDOR_NAME = device.getInfoString(CL10.CL_DEVICE_VENDOR);
            this.CL_CURRENT_DEVICE_TYPE = device.getInfoInt(CL10.CL_DEVICE_TYPE);
            this.CL_CL11 = cap.OpenCL11;
            this.CL_CL12 = cap.OpenCL12;
            this.KHR_FP16 = cap.CL_KHR_fp16;
            this.KHR_FP64 = cap.CL_KHR_fp64;

            this.MAX_IMAGE2D_WIDTH = device.getInfoInt(CL10.CL_DEVICE_IMAGE2D_MAX_WIDTH);
            this.MAX_IMAGE2D_HEIGHT = device.getInfoInt(CL10.CL_DEVICE_IMAGE2D_MAX_HEIGHT);
            this.MAX_IMAGE2D_PIXELS = (long) this.MAX_IMAGE2D_WIDTH * this.MAX_IMAGE2D_HEIGHT;
            this.MAX_IMAGE3D_WIDTH = device.getInfoInt(CL10.CL_DEVICE_IMAGE3D_MAX_WIDTH);
            this.MAX_IMAGE3D_HEIGHT = device.getInfoInt(CL10.CL_DEVICE_IMAGE3D_MAX_HEIGHT);
            this.MAX_IMAGE3D_DEPTH = device.getInfoInt(CL10.CL_DEVICE_IMAGE3D_MAX_DEPTH);
            this.MAX_IMAGE3D_PIXELS = (long) this.MAX_IMAGE3D_WIDTH * this.MAX_IMAGE3D_HEIGHT * this.MAX_IMAGE3D_DEPTH;
            this.CL_MAX_WORK_GROUP_SIZE = device.getInfoInt(CL10.CL_DEVICE_MAX_WORK_GROUP_SIZE);
            this.CL_MAX_WORK_ITEM_SIZE = device.getInfoInt(CL10.CL_DEVICE_MAX_WORK_ITEM_SIZES);
            this.CL_MAX_LOCAL_DIMENSIONS = device.getInfoInt(CL10.CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS);
            this.CL_MAX_COMPUTE_UNITS = device.getInfoInt(CL10.CL_DEVICE_MAX_COMPUTE_UNITS);
        }

        public String getPrintInfo() {
            return "Current device OpenCL context capabilities:"
                    + "\nCL_CURRENT_DEVICE_NAME: " + this.CL_CURRENT_DEVICE_NAME
                    + "\nCL_CURRENT_DEVICE_VERSION: " + this.CL_CURRENT_DEVICE_VERSION
                    + "\nCL_CURRENT_DEVICE_VENDOR_NAME: " + this.CL_CURRENT_DEVICE_VENDOR_NAME
                    + "\nCL_CURRENT_DEVICE_TYPE: " + this.CL_CURRENT_DEVICE_TYPE
                    + "\nCL_CL11: " + this.CL_CL11
                    + "\nCL_CL12: " + this.CL_CL12
                    + "\nKHR_FP16: " + this.KHR_FP16
                    + "\nKHR_FP64: " + this.KHR_FP64 + '\n'

                    + "\nMAX_IMAGE2D_WIDTH: " + this.MAX_IMAGE2D_WIDTH
                    + "\nMAX_IMAGE2D_HEIGHT: " + this.MAX_IMAGE2D_HEIGHT
                    + "\nMAX_IMAGE2D_PIXELS: " + this.MAX_IMAGE2D_PIXELS
                    + "\nMAX_IMAGE3D_WIDTH: " + this.MAX_IMAGE3D_WIDTH
                    + "\nMAX_IMAGE3D_HEIGHT: " + this.MAX_IMAGE3D_HEIGHT
                    + "\nMAX_IMAGE3D_DEPTH: " + this.MAX_IMAGE3D_DEPTH
                    + "\nMAX_IMAGE3D_PIXELS: " + this.MAX_IMAGE3D_PIXELS
                    + "\nCL_MAX_WORK_GROUP_SIZE: " + this.CL_MAX_WORK_GROUP_SIZE
                    + "\nCL_MAX_WORK_ITEM_SIZE: " + this.CL_MAX_WORK_ITEM_SIZE
                    + "\nCL_MAX_LOCAL_DIMENSIONS: " + this.CL_MAX_LOCAL_DIMENSIONS
                    + "\nCL_MAX_COMPUTE_UNITS: " + this.CL_MAX_COMPUTE_UNITS;
        }

        public void print() {
            _LOG.info(this.getPrintInfo());
        }
    }

    /**
     * @return ture if AMD or ATI.
     */
    public static boolean isGLDeviceAMD() {
        return _glState.GL_CURRENT_DEVICE_VENDOR_BYTE == BoxEnum.GL_DEVICE_AMD_ATI;
    }

    public static boolean isGLDeviceNVIDIA() {
        return _glState.GL_CURRENT_DEVICE_VENDOR_BYTE == BoxEnum.GL_DEVICE_NVIDIA;
    }

    public static boolean isGLDeviceIntel() {
        return _glState.GL_CURRENT_DEVICE_VENDOR_BYTE == BoxEnum.GL_DEVICE_INTEL;
    }

    public static boolean isGLDeviceOther() {
        return _glState.GL_CURRENT_DEVICE_VENDOR_BYTE == BoxEnum.GL_DEVICE_OTHER;
    }

    private BoxDatabase() {}
}
