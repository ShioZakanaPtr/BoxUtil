package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.apache.log4j.Logger;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.backends.buffer.BUtil_RenderingBuffer;
import org.boxutil.backends.core.BUtil_BoxUtilBackgroundThread;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.units.standard.ShaderProgram;
import org.boxutil.units.standard.misc.LineObject;
import org.boxutil.units.standard.misc.PointObject;
import org.boxutil.units.standard.misc.PublicFBO;
import org.boxutil.units.standard.misc.QuadObject;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.TransformUtil;
import org.boxutil.util.concurrent.SpinLock;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.nio.FloatBuffer;

@SuppressWarnings("UnusedReturnValue")
public final class ShaderCore {
    private final static byte _SHADER_COUNT = 34;
    private final static byte _COMMON = 0;
    private final static byte _SPRITE = 1;
    private final static byte _CURVE = 2;
    private final static byte _SEGMENT = 3;
    private final static byte _TRAIL = 4;
    private final static byte _FLARE = 5;
    private final static byte _TEXT = 6;
    private final static byte _DIST = 7;
    private final static byte _DIRECT = 8;
    private final static byte _MATRIX_2D = 9;
    private final static byte _MATRIX_3D = 10;
    private final static byte _SDF_INIT = 11;
    private final static byte _SDF_PROCESS = 12;
    private final static byte _SDF_RESULT = 13;
    private final static byte _RADIAL_BLUR = 14;
    private final static byte _GAUSSIAN_BLUR = 15;
    private final static byte _GAUSSIAN_BLUR_RED = 16;
    private final static byte _BILATERAL_FILTER = 17;
    private final static byte _BILATERAL_FILTER_RED = 18;
    private final static byte _DFT = 19;
    private final static byte _DFT_RED = 20;
    private final static byte _NORMAL_GEN_INIT = 21;
    private final static byte _NORMAL_GEN_RESULT = 22;
    private final static byte _SIMPLE_NUMBER = 23;
    private final static byte _SIMPLE_ARC = 24;
    private final static byte _SIMPLE_TEX_ARC = 25;
    private final static byte _MISSION_BG = 26;
    private final static byte _FXAA_C = 27;
    private final static byte _FXAA_Q = 28;
    private final static byte _BLOOM = 29;
    private final static byte _AREA_LIGHT_PRE_FILTERING = 30;
    private final static byte _LEGACY_NORMAL_BLUR = 31;
    private final static byte _LEGACY_NORMAL_RESULT = 32;
    private final static byte _STATIC_TRAIL_SYSTEM = 33;
    private final static String _SHADER_PATH_ROOT = "data/shaders/";
    private final static String _GLSL_VERSION = "430";
    private final static String _GLSL_VERSION_TITLE = "OVERWRITE_VERSION";
    private final static String _GLSL_PRECISION = "highp";
    private final static String _GLSL_PRECISION_TITLE = "OVERWRITE_PRECISION";
    private final static String _GLSL_MATRIX_UBO_TITLE = "OVERWRITE_MATRIX_UBO";
    private final static String _GLSL_WORKGROUP_SIZE_TITLE = "WORKGROUP_SIZE_VALUE";
    private final static String _GLSL_WEIGHTED_LUMINANCE_TITLE = "LINEAR_VALUES";
    private final static String _GLSL_COMPUTE_DIM_REPLACE_TITLE = "RESET_VALUE";
    private final static String _GLSL_INCLUDE_INSTANCE_DATA_TITLE = "#include \"BUtil_InstanceDataSSBO.h\"";
    private final static byte _GLSL_MATRIX_UBO_BINDING = 0;
    private final static SpinLock _UPLOAD_MATRIX_LOCK = new SpinLock();
    private final static FloatBuffer _NONE_GAME_MATRIX = CommonUtil.createIdentityMatrix4x4f();
    private final static FloatBuffer _UPLOAD_MATRIX_BUFFER = CommonUtil.createFloatBuffer(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private final static ShaderProgram[] _SHADER_PROGRAM = new ShaderProgram[_SHADER_COUNT];
    private final static int _GL_ATTRIB_BITS = GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BITS | GL11.GL_ENABLE_BIT | GL11.GL_POLYGON_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_VIEWPORT_BIT;
    private static BUtil_RenderingBuffer renderingBuffer = null;
    private static PublicFBO publicFBO = null;
    private static SimpleVAOAPI defaultPointObject = null;
    private static SimpleVAOAPI defaultLineObject = null;
    private static SimpleVAOAPI defaultQuadObject = null;
    private static int matrixUBO = 0;
    private static boolean _miscShaderInit = false;
    private static boolean glValid = false;
    private static boolean glFinished = false;
    private static boolean glMainProgramValid = true;
    private static boolean glDistortionValid = false;
    private static boolean glFXAACValid = false;
    private static boolean glFXAAQValid = false;
    private static boolean glBloomValid = false;
    private static boolean glAreaLightTexValid = false;
    private static boolean glInstanceMatrixValid = false;
    private static boolean glSDFGenValid = false;
    private static boolean glRadialBlurValid = false;
    private static boolean glCompGaussianBlurValid = false;
    private static boolean glCompBilateralFilterValid = false;
    private static boolean glDiscreteFourierValid = false;
    private static boolean glNormalMapGenValid = false;
    private static boolean glLegacyNormalMapGenValid = false;
    private static boolean glStaticTrailSystemValid = false;
    private static final int[] screenSize = new int[2];
    private static final int[] screenSizeScale = new int[2];
    private static final int[] screenSizeFix = new int[2];
    private static final float[] screenSizeUV = new float[2];

    private static final Logger _LOG = Global.getLogger(ShaderCore.class);

    private static String _loadLocalFile(final String path) {
        String result;
        try {
            result = Global.getSettings().loadText(_SHADER_PATH_ROOT + path);
        } catch (IOException e) {
            throw new RuntimeException("File load failed in '" + _SHADER_PATH_ROOT + path + "' cause: " + e.getMessage());
        }
        return result;
    }

    /**
     * Loading after {@link BoxConfigs#init()}.
     */
    public static void init() {
        if (glFinished) return;
        glFinished = true;
        _LOG.info("'BoxUtil' OpenGL context running on: '" + BoxDatabase.getGLState().GL_CURRENT_DEVICE_NAME + "' with drive version: '" + BoxDatabase.getGLState().GL_CURRENT_DEVICE_VERSION + "'.");
        if (!BUtil_BoxUtilBackgroundThread.initWithFailedCheck()) {
            _LOG.warn("'BoxUtil' logical thread gl context failed.");
            closeShader();
            return;
        }
        if (!BoxConfigs.isShaderEnable()) {
            _LOG.warn("'BoxUtil' shader core has been disabled.");
            closeShader();
            return;
        }
        if (!BoxConfigs.isBaseGL43Supported()) {
            _LOG.warn("'BoxUtil' platform is not supported 'OpenGL4.3'.");
            closeShader();
            return;
        }
        String gBufferToolH, instanceDataH,
                vertCommon, fragCommon,
                vertSprite, fragSprite,
                vertCurve, tescCurve, teseCurve, geomCurve, fragCurve,
                vertSeg, tescSeg, teseSeg,
                vertTrail, geomTrail, fragTrail,
                vertFlare, fragFlare,
                vertText, geomText, fragText,
                vertDist, fragDist,
                vertPost, fragDirect,
                compMatrix2D, compMatrix3D,
                compSDFInit, compSDFProcess, compSDFResult,
                compGaussianBlur, compGaussianBlurRed,
                compBilateralFilter, compBilateralFilterRed,
                compDFT, compDFTRed,
                compNormalInit, compNormalResult,
                vertPostSimple, fragFXAAC, fragFXAAQ, compBloom, compAreaTex;
        final String gl_linear = "0.2126729, 0.7151522, 0.0721750";
        final boolean vendorCheckRed = BoxDatabase.isGLDeviceAMD();
        final String gl_localWorkDim = vendorCheckRed ? "2" : "1";
        final String gl_localWorkDimSDF = vendorCheckRed ? "8" : "4";
        final String gl_localWorkSize = vendorCheckRed ? "64" : "32";
        final String gl_matrixUBO = Byte.toString(_GLSL_MATRIX_UBO_BINDING);
        final String gl_screenXStep = String.format("%.7f", 1.0f / (float) ShaderCore.getScreenScaleWidth());
        final String gl_screenYStep = String.format("%.7f", 1.0f / (float) ShaderCore.getScreenScaleHeight());
        final String gl_bloomRadius = Float.toString(Global.getSettings().getScreenScaleMult());
        gBufferToolH = _loadLocalFile("include/BUtil_GBufferTool.h");
        instanceDataH = _loadLocalFile("include/BUtil_InstanceDataSSBO.h");
        vertCommon = _loadLocalFile("BUtil_CommonShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO).replace(_GLSL_INCLUDE_INSTANCE_DATA_TITLE, instanceDataH);
        fragCommon = _loadLocalFile("BUtil_CommonShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        vertSprite = _loadLocalFile("BUtil_SpriteShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO).replace(_GLSL_INCLUDE_INSTANCE_DATA_TITLE, instanceDataH);
        fragSprite = _loadLocalFile("BUtil_SpriteShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        vertCurve = _loadLocalFile("BUtil_CurveShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO).replace(_GLSL_INCLUDE_INSTANCE_DATA_TITLE, instanceDataH);
        tescCurve = _loadLocalFile("BUtil_CurveShader.tesc").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        teseCurve = _loadLocalFile("BUtil_CurveShader.tese").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        geomCurve = _loadLocalFile("BUtil_CurveShader.geom").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        fragCurve = _loadLocalFile("BUtil_CurveShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        vertSeg = _loadLocalFile("BUtil_SegmentShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        tescSeg = _loadLocalFile("BUtil_SegmentShader.tesc").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        teseSeg = _loadLocalFile("BUtil_SegmentShader.tese").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        vertTrail = _loadLocalFile("BUtil_TrailShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        geomTrail = _loadLocalFile("BUtil_TrailShader.geom").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        fragTrail = _loadLocalFile("BUtil_TrailShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        vertFlare = _loadLocalFile("BUtil_FlareShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO).replace(_GLSL_INCLUDE_INSTANCE_DATA_TITLE, instanceDataH);
        fragFlare = _loadLocalFile("BUtil_FlareShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        vertText = _loadLocalFile("BUtil_TextFieldShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO);
        geomText = _loadLocalFile("BUtil_TextFieldShader.geom").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        fragText = _loadLocalFile("BUtil_TextFieldShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        vertDist = _loadLocalFile("BUtil_DistortionShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION).replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO).replace(_GLSL_INCLUDE_INSTANCE_DATA_TITLE, instanceDataH);
        fragDist = _loadLocalFile("BUtil_DistortionShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        vertPost = _loadLocalFile("misc/BUtil_PostShader.vert").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        fragDirect = _loadLocalFile("misc/BUtil_DirectShader.frag").replace(_GLSL_VERSION_TITLE, _GLSL_VERSION)
                .replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION);
        compMatrix2D = _loadLocalFile("BUtil_Instance2DMatrix.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDim).replace(_GLSL_WORKGROUP_SIZE_TITLE, gl_localWorkSize);
        compMatrix3D = _loadLocalFile("BUtil_Instance3DMatrix.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDim).replace(_GLSL_WORKGROUP_SIZE_TITLE, gl_localWorkSize);
        compSDFInit = _loadLocalFile("sdf/BUtil_SDFGenInit.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF).replace(_GLSL_WEIGHTED_LUMINANCE_TITLE, gl_linear);
        compSDFProcess = _loadLocalFile("sdf/BUtil_SDFGenProcess.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compSDFResult = _loadLocalFile("sdf/BUtil_SDFGenResult.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compGaussianBlur = _loadLocalFile("filter/BUtil_GaussianBlur.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compGaussianBlurRed = _loadLocalFile("filter/BUtil_GaussianBlurRed.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compBilateralFilter = _loadLocalFile("filter/BUtil_BilateralFilter.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compBilateralFilterRed = _loadLocalFile("filter/BUtil_BilateralFilterRed.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compDFT = _loadLocalFile("fourier/BUtil_DFT.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compDFTRed = _loadLocalFile("fourier/BUtil_DFTRed.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        compNormalInit = _loadLocalFile("normalMap/BUtil_NormalMapInit.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF).replace(_GLSL_WEIGHTED_LUMINANCE_TITLE, gl_linear);
        compNormalResult = _loadLocalFile("normalMap/BUtil_NormalMapResult.comp").replace(_GLSL_PRECISION_TITLE, _GLSL_PRECISION)
                .replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);

        vertPostSimple = _loadLocalFile("misc/BUtil_PostSimpleShader.vert");
        fragFXAAC = _loadLocalFile("shaderpacks/BUtil_FXAACShader.frag").replace("OVERWRITE_SCREEN_X", gl_screenXStep).replace("OVERWRITE_SCREEN_Y", gl_screenYStep);
        fragFXAAQ = _loadLocalFile("shaderpacks/BUtil_FXAAQShader.frag").replace("OVERWRITE_SCREEN_X", gl_screenXStep).replace("OVERWRITE_SCREEN_Y", gl_screenYStep);
        compBloom = _loadLocalFile("shaderpacks/BUtil_BloomShader.comp").replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF).replace("OVERWRITE_RADIUS_SCALE", gl_bloomRadius);
        compAreaTex = _loadLocalFile("shaderpacks/BUtil_AreaLightPreFilteringShader.comp").replace(_GLSL_COMPUTE_DIM_REPLACE_TITLE, gl_localWorkDimSDF);
        _SHADER_PROGRAM[_COMMON] = new ShaderProgram("BoxUtil-CommonShader", vertCommon, fragCommon);
        _SHADER_PROGRAM[_SPRITE] = new ShaderProgram("BoxUtil-SpriteShader", vertSprite, fragSprite);
        _SHADER_PROGRAM[_CURVE] = new ShaderProgram("BoxUtil-CurveShader", vertCurve, tescCurve, teseCurve, geomCurve, fragCurve);
        _SHADER_PROGRAM[_SEGMENT] = new ShaderProgram("BoxUtil-SegmentShader", vertSeg, tescSeg, teseSeg, geomCurve, fragCurve);
        _SHADER_PROGRAM[_TRAIL] = new ShaderProgram("BoxUtil-TrailShader", vertTrail, geomTrail, fragTrail);
        _SHADER_PROGRAM[_FLARE] = new ShaderProgram("BoxUtil-FlareShader", vertFlare, fragFlare);
        _SHADER_PROGRAM[_TEXT] = new ShaderProgram("BoxUtil-TextShader", vertText, geomText, fragText);
        _SHADER_PROGRAM[_DIST] = new ShaderProgram("BoxUtil-DistortionShader", vertDist, fragDist);
        _SHADER_PROGRAM[_DIRECT] = new ShaderProgram("BoxUtil-DirectShader", vertPost, fragDirect);
        _SHADER_PROGRAM[_FXAA_C] = new ShaderProgram("BoxUtil-FXAA-ConsoleShader", vertPostSimple, fragFXAAC);
        _SHADER_PROGRAM[_FXAA_Q] = new ShaderProgram("BoxUtil-FXAA-QualityShader", vertPostSimple, fragFXAAQ);

        _SHADER_PROGRAM[_MATRIX_2D] = new ShaderProgram("BoxUtil-MatrixComputeShader-2D", compMatrix2D);
        _SHADER_PROGRAM[_MATRIX_3D] = new ShaderProgram("BoxUtil-MatrixComputeShader-3D", compMatrix3D);
        _SHADER_PROGRAM[_SDF_INIT] = new ShaderProgram("BoxUtil-SDFGenInitShader", compSDFInit);
        _SHADER_PROGRAM[_SDF_PROCESS] = new ShaderProgram("BoxUtil-SDFGenProcessShader", compSDFProcess);
        _SHADER_PROGRAM[_SDF_RESULT] = new ShaderProgram("BoxUtil-SDFGenResultShader", compSDFResult);

        _SHADER_PROGRAM[_GAUSSIAN_BLUR] = new ShaderProgram("BoxUtil-CompGaussianBlurShader", compGaussianBlur);
        _SHADER_PROGRAM[_GAUSSIAN_BLUR_RED] = new ShaderProgram("BoxUtil-CompGaussianBlurRedShader", compGaussianBlurRed);
        _SHADER_PROGRAM[_BILATERAL_FILTER] = new ShaderProgram("BoxUtil-CompBilateralFilterShader", compBilateralFilter);
        _SHADER_PROGRAM[_BILATERAL_FILTER_RED] = new ShaderProgram("BoxUtil-CompBilateralFilterRedShader", compBilateralFilterRed);
        _SHADER_PROGRAM[_DFT] = new ShaderProgram("BoxUtil-DFTShader", compDFT);
        _SHADER_PROGRAM[_DFT_RED] = new ShaderProgram("BoxUtil-DFTRedShader", compDFTRed);
        _SHADER_PROGRAM[_NORMAL_GEN_INIT] = new ShaderProgram("BoxUtil-NormalMapInitShader", compNormalInit);
        _SHADER_PROGRAM[_NORMAL_GEN_RESULT] = new ShaderProgram("BoxUtil-NormalMapResultShader", compNormalResult);

        _SHADER_PROGRAM[_BLOOM] = new ShaderProgram("BoxUtil-BloomShader", compBloom);
        _SHADER_PROGRAM[_AREA_LIGHT_PRE_FILTERING] = new ShaderProgram("BoxUtil-AreaLightTexPreFiltering", compAreaTex);

        initShaderPrograms();
        initMatrixProgram();
        refreshRenderingBuffer();
        refreshDefaultVAO();
        if (!isMainProgramValid() || !isMatrixProgramValid() || !isRenderingFramebufferValid() || !isDefaultVAOValid()) {
            closeShader();
            _LOG.error("'BoxUtil' base shader resource init failed, main program: " + isMainProgramValid() + ", instance computing program: " + isMatrixProgramValid() + ", rendering framebuffer: " + isRenderingFramebufferValid() + ", default VAO: " + isDefaultVAOValid() + ".");
            return;
        }
        glValid = true;

        initDistortionProgram();
        initSDFGenProgram();
        initCompGaussianBlurProgram();
        initCompBilateralFilterProgram();
        initDiscreteFourierProgram();
        initNormalMapGenProgram();

        initFXAAProgram();
        initBloomProgram();
        initAreaLightTex();
    }

    private static ShaderProgram _createShaderProgramVF(final String name, final String file) {
        return new ShaderProgram(true, name, _SHADER_PATH_ROOT + file + ".vert", _SHADER_PATH_ROOT + file + ".frag");
    }

    private static ShaderProgram _createShaderProgramVGF(final String name, final String file) {
        return new ShaderProgram(true, name, _SHADER_PATH_ROOT + file + ".vert", _SHADER_PATH_ROOT + file + ".geom", _SHADER_PATH_ROOT + file + ".frag");
    }

    public static void initGlobalDataUBO() {
        if (!BoxDatabase.getGLState().GL_GL31) {
            closeShader();
            _LOG.error("'BoxUtil' platform is not supported Uniform buffer object. ");
            return;
        }
        matrixUBO = GL15.glGenBuffers();
        if (matrixUBO == 0) {
            closeShader();
            _LOG.error("'BoxUtil' shader UBO init failed. ");
            return;
        }

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, 20 * BoxDatabase.FLOAT_SIZE, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, _GLSL_MATRIX_UBO_BINDING, matrixUBO);
        glValid = true;
    }

    public static void initMiscShaderPrograms() {
        if (_miscShaderInit) return;
        _miscShaderInit = true;
        _SHADER_PROGRAM[_SIMPLE_NUMBER] = _createShaderProgramVF("BoxUtil-NumberShader", "BUtil_NumberShader");
        _SHADER_PROGRAM[_SIMPLE_ARC] = _createShaderProgramVF("BoxUtil-ArcShader", "BUtil_ArcShader");
        _SHADER_PROGRAM[_SIMPLE_TEX_ARC] = new ShaderProgram(true, "BoxUtil-TexArcShader", _SHADER_PATH_ROOT + "BUtil_ArcShader.vert", _SHADER_PATH_ROOT + "BUtil_TexArcShader.frag");
        _SHADER_PROGRAM[_MISSION_BG] = _createShaderProgramVF("BoxUtil-TestMissionShader", "misc/BUtil_TestMissionShader");
        if (_SHADER_PROGRAM[_SIMPLE_NUMBER].isValid()) {
            _SHADER_PROGRAM[_SIMPLE_NUMBER].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_charLength");
        }

        if (_SHADER_PROGRAM[_SIMPLE_ARC].isValid()) {
            _SHADER_PROGRAM[_SIMPLE_ARC].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_arcValue");
        }

        if (_SHADER_PROGRAM[_SIMPLE_TEX_ARC].isValid()) {
            _SHADER_PROGRAM[_SIMPLE_TEX_ARC].initUniformSize(1)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage");
        }

        if (_SHADER_PROGRAM[_MISSION_BG].isValid()) {
            _SHADER_PROGRAM[_MISSION_BG].initUniformSize(1)
                    .beginUniform()
                    .loadUniformIndex("u_time");
        }

        initRadialBlurProgram();
        initLegacyNormalGen();
        initStaticTrailSystem();
    }

    private static void closeShader() {
        glValid = false;
    }

    private static void initShaderPrograms() {
        if (_SHADER_PROGRAM[_COMMON].isValid()) {
            _SHADER_PROGRAM[_COMMON].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_baseSize")
                    .loadUniformIndex("u_additionEmissive_DataBit_InstanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(5, 2)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")
                    .beginSubroutine(1, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_commonMode")
                    .loadSubroutineIndex("p_colorMode")

                    .initSubroutineUniformSize(1, 1)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .beginSubroutineUniform(1, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_surfaceState")
                    .computeSubroutineUniformRoute();
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_SPRITE].isValid()) {
            _SHADER_PROGRAM[_SPRITE].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_additionEmissive_DataBit_InstanceOffset")
                    .loadUniformIndex("u_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(8)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_commonUV")
                    .loadSubroutineIndex("p_tileUV")
                    .loadSubroutineIndex("p_tileRUV")
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_uvMapping")
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_CURVE].isValid()) {
            _SHADER_PROGRAM[_CURVE].initUniformSize(6)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_totalNodes")
                    .loadUniformIndex("u_additionEmissive_DataBit")
                    .loadUniformIndex("u_globalTimerAlpha")
                    .loadUniformIndex("u_instanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_SEGMENT].isValid()) {
            _SHADER_PROGRAM[_SEGMENT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_additionEmissive_DataBit")
                    .loadUniformIndex("u_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING);
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_TRAIL].isValid()) {
            _SHADER_PROGRAM[_TRAIL].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_extraData")
                    .loadUniformIndex("u_additionEmissive_DataBit")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_lineStripMode")
                    .loadSubroutineIndex("p_linesMode")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_lineModeState")
                    .computeSubroutineUniformRoute();
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_FLARE].isValid()) {
            _SHADER_PROGRAM[_FLARE].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_dataBit")
                    .loadUniformIndex("u_instanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(5, 4)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")
                    .beginSubroutine(1, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_smoothMode")
                    .loadSubroutineIndex("p_sharpMode")
                    .loadSubroutineIndex("p_smoothDiscMode")
                    .loadSubroutineIndex("p_sharpDiscMode")

                    .initSubroutineUniformSize(1, 1)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .beginSubroutineUniform(1, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_flareState")
                    .computeSubroutineUniformRoute();
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_TEXT].isValid()) {
            _SHADER_PROGRAM[_TEXT].initUniformSize(9)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_fontMap[0]")
                    .loadUniformIndex("u_fontMap[1]")
                    .loadUniformIndex("u_fontMap[2]")
                    .loadUniformIndex("u_fontMap[3]")
                    .loadUniformIndex("u_italicFactor")
                    .loadUniformIndex("u_globalColor")
                    .loadUniformIndex("u_dataBit")
                    .loadUniformIndex("u_blendBloom_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING);
            _SHADER_PROGRAM[_TEXT].putDefaultTextureUnit(_SHADER_PROGRAM[_TEXT].location[1], 0);
            _SHADER_PROGRAM[_TEXT].putDefaultTextureUnit(_SHADER_PROGRAM[_TEXT].location[2], 1);
            _SHADER_PROGRAM[_TEXT].putDefaultTextureUnit(_SHADER_PROGRAM[_TEXT].location[3], 2);
            _SHADER_PROGRAM[_TEXT].putDefaultTextureUnit(_SHADER_PROGRAM[_TEXT].location[4], 3);
        } else glMainProgramValid = false;

        if (_SHADER_PROGRAM[_DIRECT].isValid()) {
            _SHADER_PROGRAM[_DIRECT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_alphaFix")
                    .loadUniformIndex("u_level")
                    .loadUniformIndex("u_uvStart")
                    .loadUniformIndex("u_uvEnd");
            GL41.glProgramUniform1f(_SHADER_PROGRAM[_DIRECT].getId(), _SHADER_PROGRAM[_DIRECT].location[1], 0.0f);
            GL41.glProgramUniform2f(_SHADER_PROGRAM[_DIRECT].getId(), _SHADER_PROGRAM[_DIRECT].location[2], 0.0f, 0.0f);
            GL41.glProgramUniform2f(_SHADER_PROGRAM[_DIRECT].getId(), _SHADER_PROGRAM[_DIRECT].location[3], 1.0f, 1.0f);
        } else glMainProgramValid = false;
    }

    private static void initDistortionProgram() {
        if (_SHADER_PROGRAM[_DIST].isValid()) {
            _SHADER_PROGRAM[_DIST].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_instanceDataOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", _GLSL_MATRIX_UBO_BINDING)

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
            glDistortionValid = true;
        } else _LOG.warn("'BoxUtil' distortion program init failed.");
    }

    private static void initMatrixProgram() {
        if (_SHADER_PROGRAM[_MATRIX_2D].isValid() && _SHADER_PROGRAM[_MATRIX_3D].isValid()) {
            _SHADER_PROGRAM[_MATRIX_2D].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_amount")
                    .loadUniformIndex("u_instanceRange");

            _SHADER_PROGRAM[_MATRIX_3D].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_amount")
                    .loadUniformIndex("u_instanceRange");
            glInstanceMatrixValid = true;
        } else _LOG.warn("'BoxUtil' matrix program init failed.");
    }

    private static void initSDFGenProgram() {
        if (_SHADER_PROGRAM[_SDF_INIT].isValid() && _SHADER_PROGRAM[_SDF_PROCESS].isValid() && _SHADER_PROGRAM[_SDF_RESULT].isValid()) {
            _SHADER_PROGRAM[_SDF_INIT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeState")
                    .loadUniformIndex("u_border")
                    .loadUniformIndex("u_threshold")

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_fromRed")
                    .loadSubroutineIndex("p_fromGreen")
                    .loadSubroutineIndex("p_fromBlue")
                    .loadSubroutineIndex("p_fromAlpha")
                    .loadSubroutineIndex("p_fromRGB")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .computeSubroutineUniformRoute();

            _SHADER_PROGRAM[_SDF_PROCESS].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_step");

            _SHADER_PROGRAM[_SDF_RESULT].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_preMultiply")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
            glSDFGenValid = true;
        } else _LOG.warn("'BoxUtil' SDF-generate program init failed.");
    }

    private static void initRadialBlurProgram() {
        _SHADER_PROGRAM[_RADIAL_BLUR] = _createShaderProgramVF("BoxUtil-RadialBlurShader", "filter/BUtil_RadialBlurShader");
        if (_SHADER_PROGRAM[_RADIAL_BLUR].isValid()) {
            _SHADER_PROGRAM[_RADIAL_BLUR].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_alphaStrength")
                    .loadUniformIndex("u_tex");
            glRadialBlurValid = true;
        }
    }

    private static void initCompGaussianBlurProgram() {
        if (_SHADER_PROGRAM[_GAUSSIAN_BLUR].isValid() && _SHADER_PROGRAM[_GAUSSIAN_BLUR_RED].isValid()) {
            _SHADER_PROGRAM[_GAUSSIAN_BLUR].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_perStep")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_filterMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .loadSubroutineUniformIndex("f_workModeState")
                    .computeSubroutineUniformRoute();

            _SHADER_PROGRAM[_GAUSSIAN_BLUR_RED].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_perStep")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_filterMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .loadSubroutineUniformIndex("f_workModeState")
                    .computeSubroutineUniformRoute();
            glCompGaussianBlurValid = true;
        } else _LOG.warn("'BoxUtil' comp-gaussian blur program init failed.");
    }

    private static void initCompBilateralFilterProgram() {
        if (_SHADER_PROGRAM[_BILATERAL_FILTER].isValid() && _SHADER_PROGRAM[_BILATERAL_FILTER_RED].isValid()) {
            _SHADER_PROGRAM[_BILATERAL_FILTER].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_gSigmaSRInv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();

            _SHADER_PROGRAM[_BILATERAL_FILTER_RED].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_gSigmaSRInv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
            glCompBilateralFilterValid = true;
        } else _LOG.warn("'BoxUtil' comp-bilateral filter program init failed.");
    }

    private static void initDiscreteFourierProgram() {
        if (_SHADER_PROGRAM[_DFT].isValid() && _SHADER_PROGRAM[_DFT_RED].isValid()) {
            _SHADER_PROGRAM[_DFT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_sizeDiv")

                    .initSubroutineSize(3)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_bit32Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();

            _SHADER_PROGRAM[_DFT_RED].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_sizeDiv")

                    .initSubroutineSize(3)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_bit32Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
            glDiscreteFourierValid = true;
        } else _LOG.warn("'BoxUtil' discrete fourier program init failed.");
    }

    private static void initNormalMapGenProgram() {
        if (_SHADER_PROGRAM[_NORMAL_GEN_INIT].isValid() && _SHADER_PROGRAM[_NORMAL_GEN_RESULT].isValid()) {
            _SHADER_PROGRAM[_NORMAL_GEN_INIT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_rampMix")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_texOnly")
                    .loadSubroutineIndex("p_withVolume")
                    .loadSubroutineIndex("p_withDetails")
                    .loadSubroutineIndex("p_withBoth")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_texInputState")
                    .computeSubroutineUniformRoute();

            _SHADER_PROGRAM[_NORMAL_GEN_RESULT].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_sizeState")
                    .loadUniformIndex("u_normalStrength");
            glNormalMapGenValid = true;
        } else _LOG.warn("'BoxUtil' normal map generate program init failed.");
    }

    private static void initFXAAProgram() {
        if (_SHADER_PROGRAM[_FXAA_C].isValid()) {
            _SHADER_PROGRAM[_FXAA_C].initSubroutineSize(4)
                    .beginSubroutine(0, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_fromRaw")
                    .loadSubroutineIndex("p_fromDepth")
                    .loadSubroutineIndex("p_commonDisplay")
                    .loadSubroutineIndex("p_edgeDisplay")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .loadSubroutineUniformIndex("f_displayMethodState")
                    .computeSubroutineUniformRoute();
            glFXAACValid = true;
        }

        if (_SHADER_PROGRAM[_FXAA_Q].isValid()) {
            _SHADER_PROGRAM[_FXAA_Q].initSubroutineSize(4)
                    .beginSubroutine(0, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_fromRaw")
                    .loadSubroutineIndex("p_fromDepth")
                    .loadSubroutineIndex("p_commonDisplay")
                    .loadSubroutineIndex("p_edgeDisplay")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GL20.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .loadSubroutineUniformIndex("f_displayMethodState")
                    .computeSubroutineUniformRoute();
            glFXAAQValid = true;
        }
    }

    private static void initBloomProgram() {
        if (_SHADER_PROGRAM[_BLOOM] != null && _SHADER_PROGRAM[_BLOOM].isValid()) {
            _SHADER_PROGRAM[_BLOOM].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_targetUVStepDiv")
                    .loadUniformIndex("u_initPass")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_sampleInit")
                    .loadSubroutineIndex("p_downSampleFirst")
                    .loadSubroutineIndex("p_downSample")
                    .loadSubroutineIndex("p_upSample")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_sampleModeState")
                    .computeSubroutineUniformRoute();
            glBloomValid = true;
        }
    }

    public static void initAreaLightTex() {
        if (_SHADER_PROGRAM[_AREA_LIGHT_PRE_FILTERING] != null && _SHADER_PROGRAM[_AREA_LIGHT_PRE_FILTERING].isValid()) {
            _SHADER_PROGRAM[_AREA_LIGHT_PRE_FILTERING].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_uvStepDiv_Lod_ADiv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_normalMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GL43.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_filteringModeState")
                    .computeSubroutineUniformRoute();
            glAreaLightTexValid = true;
        }
    }

    public static void initLegacyNormalGen() {
        _SHADER_PROGRAM[_LEGACY_NORMAL_BLUR] = new ShaderProgram(true, "BoxUtil-LegacyNormalMapBlurShader", _SHADER_PATH_ROOT + "normalMap/BUtil_NormalMapSharedLegacy.vert", _SHADER_PATH_ROOT + "normalMap/BUtil_NormalMapBlurLegacy.frag");
        _SHADER_PROGRAM[_LEGACY_NORMAL_RESULT] = new ShaderProgram(true, "BoxUtil-LegacyNormalMapResultShader", _SHADER_PATH_ROOT + "normalMap/BUtil_NormalMapSharedLegacy.vert", _SHADER_PATH_ROOT + "normalMap/BUtil_NormalMapResultLegacy.frag");
        if (_SHADER_PROGRAM[_LEGACY_NORMAL_BLUR].isValid() && _SHADER_PROGRAM[_LEGACY_NORMAL_RESULT].isValid()) {
            _SHADER_PROGRAM[_LEGACY_NORMAL_BLUR].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_stepUV_srcUVDiv")
                    .loadUniformIndex("u_vertical");

            _SHADER_PROGRAM[_LEGACY_NORMAL_RESULT].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_stepUV");
            glLegacyNormalMapGenValid = true;
        }
    }

    public static void initStaticTrailSystem() {
        final boolean fullFeatures = isValid();
        final String extensionName;
        if (BoxDatabase.getGLState().ARB_SHADER_BIT_ENCODING) {
            extensionName = "GL_ARB_shader_bit_encoding";
        } else if (BoxDatabase.getGLState().ARB_GPU_SHADER5) {
            extensionName = "GL_ARB_gpu_shader5";
        } else if (fullFeatures) {
            extensionName = "GL_ARB_shader_bit_encoding";
        } else return;

        final String gl_arbExtensionTitle = "ARB_EXTENSION_TITLE",
                gl_trailVersionTitle = "TRAIL_VERSION_TITLE",
                gl_trailVersion = fullFeatures ? "MODERN_TRAIL_MODE" : "LEGACY_TRAIL_MODE",
                gl_glslVersion = fullFeatures ? _GLSL_VERSION : "150",
                gl_matrixUBO = Byte.toString(_GLSL_MATRIX_UBO_BINDING);

        final String vert = _loadLocalFile("trailSystem/BUtil_StaticTrail.vert").replace(_GLSL_VERSION_TITLE, gl_glslVersion)
                .replace(gl_arbExtensionTitle, extensionName)
                .replace(gl_trailVersionTitle, gl_trailVersion),

                geom = _loadLocalFile("trailSystem/BUtil_StaticTrail.geom").replace(_GLSL_VERSION_TITLE, gl_glslVersion)
                        .replace(_GLSL_MATRIX_UBO_TITLE, gl_matrixUBO)
                        .replace(gl_trailVersionTitle, gl_trailVersion),

                frag = _loadLocalFile("trailSystem/BUtil_StaticTrail.frag").replace(_GLSL_VERSION_TITLE, gl_glslVersion)
                        .replace(gl_trailVersionTitle, gl_trailVersion);

        if (fullFeatures) {
            _SHADER_PROGRAM[_STATIC_TRAIL_SYSTEM] = new ShaderProgram("BoxUtil-StaticTrailSystemShader", vert, geom, frag);
        } else {
            final var makeBinding = ShaderUtil.makeShaderBindingLocation(
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 0, "a_position"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 1, "a_facingVector"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 2, "a_timeStampRaw"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 3, "a_distance")
            );

            final var shaderID = ShaderUtil.createShaderVGF("BoxUtil-StaticTrailSystemShader", makeBinding, vert, geom, frag);
            _SHADER_PROGRAM[_STATIC_TRAIL_SYSTEM] = new ShaderProgram(shaderID);
        }

        final var program = _SHADER_PROGRAM[_STATIC_TRAIL_SYSTEM];
        if (program.isValid()) {
            program.initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_time")
                    .loadUniformIndex("u_additionEmissive")
                    .loadUniformIndex("u_dataBit");

            if (!fullFeatures) {
                final int u_diffuse = GL20.glGetUniformLocation(program.getId(), "u_diffuseMap"),
                        u_complex = GL20.glGetUniformLocation(program.getId(), "u_complexMap"),
                        u_emissive = GL20.glGetUniformLocation(program.getId(), "u_emissiveMap");

                program.active();
                GL20.glUniform1i(u_diffuse, 0);
                GL20.glUniform1i(u_complex, 2);
                GL20.glUniform1i(u_emissive, 3);
                program.close();

                final int programID = program.getId(),
                        blockIndex = GL31.glGetUniformBlockIndex(programID, "BUtilGlobalData");
                GL31.glUniformBlockBinding(programID, blockIndex, _GLSL_MATRIX_UBO_BINDING);
            }
            glStaticTrailSystemValid = true;
        }
    }

    public static void glBeginDraw() {
        GL11.glPushAttrib(_GL_ATTRIB_BITS);
        GL11.glViewport(0, 0, screenSizeScale[0], screenSizeScale[1]);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        if (BoxDatabase.getGLState().GL_GL32) GL11.glEnable(GL32.GL_DEPTH_CLAMP);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glFrontFace(GL11.GL_CCW);
        GL11.glDepthFunc(GL11.GL_LESS);
        if (BoxDatabase.getGLState().GL_GL41) GL41.glDepthRangef(-1.0f, 1.0f);
        GL11.glDepthMask(true);
        GL11.glColorMask(true, true, true, true);
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE); // but changes color attachment 0 and 1
        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
    }

    public static void glEndDraw() {
        if (BoxDatabase.getGLState().GL_GL30) GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        if (BoxDatabase.getGLState().GL_GL31) GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glPopAttrib();
    }

    public static boolean glMultiPass() {
        int texture;
        switch (BoxConfigs.getMultiPassMode()) {
            case BoxEnum.MP_EMISSIVE: {
                texture = renderingBuffer.getEmissiveResult();
                break;
            }
            case BoxEnum.MP_POSITION: {
                texture = renderingBuffer.getWorldPosResult();
                break;
            }
            case BoxEnum.MP_NORMAL: {
                texture = renderingBuffer.getNormalResult();
                break;
            }
            case BoxEnum.MP_TANGENT: {
                texture = renderingBuffer.getTangentResult();
                break;
            }
            case BoxEnum.MP_MATERIAL: {
                texture = renderingBuffer.getMaterialResult();
                break;
            }
            case BoxEnum.MP_BEAUTY:
            case BoxEnum.MP_COLOR:
            default: return false;
        }
        _SHADER_PROGRAM[_DIRECT].active();
        GL20.glUniform1f(_SHADER_PROGRAM[_DIRECT].location[0], 1.0f);
        _SHADER_PROGRAM[_DIRECT].bindTexture2D(0, texture);
        defaultQuadObject.glBind();
        defaultQuadObject.glDraw();
        defaultQuadObject.glReleaseBind();
        _SHADER_PROGRAM[_DIRECT].close();
        return true;
    }

    public static void initScreenSize() {
        screenSize[0] = (int) (Global.getSettings().getScreenWidth() * Display.getPixelScaleFactor());
        screenSizeScale[0] = (int) (screenSize[0] * Global.getSettings().getScreenScaleMult());
        screenSize[1] = (int) (Global.getSettings().getScreenHeight() * Display.getPixelScaleFactor());
        screenSizeScale[1] = (int) (screenSize[1] * Global.getSettings().getScreenScaleMult());
        screenSizeFix[0] = CalculateUtil.getPOTMax(screenSize[0]);
        screenSizeFix[1] = CalculateUtil.getPOTMax(screenSize[1]);
        screenSizeUV[0] = (float) ((double) screenSize[0] / (double) screenSizeFix[0]);
        screenSizeUV[1] = (float) ((double) screenSize[1] / (double) screenSizeFix[1]);
    }

    public static int getScreenWidth() {
        return screenSize[0];
    }

    public static int getScreenHeight() {
        return screenSize[1];
    }

    public static int getScreenScaleWidth() {
        return screenSizeScale[0];
    }

    public static int getScreenScaleHeight() {
        return screenSizeScale[1];
    }

    /**
     * Needless usual.
     */
    public static int getScreenFixWidth() {
        return screenSizeFix[0];
    }

    /**
     * Needless usual.
     */
    public static int getScreenFixHeight() {
        return screenSizeFix[1];
    }

    /**
     * Needless usual.
     */
    public static float getScreenFixU() {
        return screenSizeUV[0];
    }

    /**
     * Needless usual.
     */
    public static float getScreenFixV() {
        return screenSizeUV[1];
    }

    public static BaseShaderData getCommonProgram() {
        return _SHADER_PROGRAM[_COMMON];
    }

    public static BaseShaderData getSpriteProgram() {
        return _SHADER_PROGRAM[_SPRITE];
    }

    public static BaseShaderData getCurveProgram() {
        return _SHADER_PROGRAM[_CURVE];
    }

    public static BaseShaderData getSegmentProgram() {
        return _SHADER_PROGRAM[_SEGMENT];
    }

    public static BaseShaderData getTrailProgram() {
        return _SHADER_PROGRAM[_TRAIL];
    }

    public static BaseShaderData getFlareProgram() {
        return _SHADER_PROGRAM[_FLARE];
    }

    public static BaseShaderData getTextProgram() {
        return _SHADER_PROGRAM[_TEXT];
    }

    public static BaseShaderData getDistortionProgram() {
        return _SHADER_PROGRAM[_DIST];
    }

    public static BaseShaderData getDirectDrawProgram() {
        return _SHADER_PROGRAM[_DIRECT];
    }

    public static BaseShaderData getInstanceMatrix2DProgram() {
        return _SHADER_PROGRAM[_MATRIX_2D];
    }

    public static BaseShaderData getInstanceMatrix3DProgram() {
        return _SHADER_PROGRAM[_MATRIX_3D];
    }

    public static BaseShaderData getSDFInitProgram() {
        return _SHADER_PROGRAM[_SDF_INIT];
    }

    public static BaseShaderData getSDFProcessProgram() {
        return _SHADER_PROGRAM[_SDF_PROCESS];
    }

    public static BaseShaderData getSDFResultProgram() {
        return _SHADER_PROGRAM[_SDF_RESULT];
    }

    public static BaseShaderData getRadialBlurProgram() {
        return _SHADER_PROGRAM[_RADIAL_BLUR];
    }

    public static BaseShaderData getCompGaussianBlurProgram() {
        return _SHADER_PROGRAM[_GAUSSIAN_BLUR];
    }

    public static BaseShaderData getCompGaussianBlurRedProgram() {
        return _SHADER_PROGRAM[_GAUSSIAN_BLUR_RED];
    }

    public static BaseShaderData getCompBilateralFilterProgram() {
        return _SHADER_PROGRAM[_BILATERAL_FILTER];
    }

    public static BaseShaderData getCompBilateralFilterRedProgram() {
        return _SHADER_PROGRAM[_BILATERAL_FILTER_RED];
    }

    public static BaseShaderData getDFTProgram() {
        return _SHADER_PROGRAM[_DFT];
    }

    public static BaseShaderData getDFTRedProgram() {
        return _SHADER_PROGRAM[_DFT_RED];
    }

    public static BaseShaderData getNormalMapGenInitProgram() {
        return _SHADER_PROGRAM[_NORMAL_GEN_INIT];
    }

    public static BaseShaderData getNormalMapGenResultProgram() {
        return _SHADER_PROGRAM[_NORMAL_GEN_RESULT];
    }

    public static BaseShaderData getNumberProgram() {
        return _SHADER_PROGRAM[_SIMPLE_NUMBER];
    }

    public static BaseShaderData getArcProgram() {
        return _SHADER_PROGRAM[_SIMPLE_ARC];
    }

    public static BaseShaderData getTexArcProgram() {
        return _SHADER_PROGRAM[_SIMPLE_ARC];
    }

    public static BaseShaderData getTestMissionProgram() {
        return _SHADER_PROGRAM[_MISSION_BG];
    }

    public static BaseShaderData getFXAAConsoleProgram() {
        return _SHADER_PROGRAM[_FXAA_C];
    }

    public static BaseShaderData getFXAAQualityProgram() {
        return _SHADER_PROGRAM[_FXAA_Q];
    }

    public static BaseShaderData getBloomProgram() {
        return _SHADER_PROGRAM[_BLOOM];
    }

    public static BaseShaderData getAreaLightTex() {
        return _SHADER_PROGRAM[_AREA_LIGHT_PRE_FILTERING];
    }

    public static BaseShaderData getLegacyNormalGenBlurProgram() {
        return _SHADER_PROGRAM[_LEGACY_NORMAL_BLUR];
    }

    public static BaseShaderData getLegacyNormalGenResultProgram() {
        return _SHADER_PROGRAM[_LEGACY_NORMAL_RESULT];
    }

    public static BaseShaderData getStaticTrailProgram() {
        return _SHADER_PROGRAM[_STATIC_TRAIL_SYSTEM];
    }

    public static BUtil_RenderingBuffer getRenderingBuffer() {
        return renderingBuffer;
    }

    public static PublicFBO getPublicFBO() {
        return publicFBO;
    }

    public static void refreshGameViewportMatrix(ViewportAPI viewport) {
        if (matrixUBO == 0) return;
        _UPLOAD_MATRIX_LOCK.lock();
        TransformUtil.createGameOrthoMatrix(viewport, false, _UPLOAD_MATRIX_BUFFER).position(0).limit(16);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, _UPLOAD_MATRIX_BUFFER);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        _UPLOAD_MATRIX_LOCK.unlock();
    }

    public static void refreshGameViewportMatrix(FloatBuffer matrix) {
        if (matrixUBO == 0) return;
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, matrix);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    public static void refreshGameViewportMatrixNone() {
        if (matrixUBO == 0) return;
        _NONE_GAME_MATRIX.position(0);
        _NONE_GAME_MATRIX.limit(_NONE_GAME_MATRIX.capacity());
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, _NONE_GAME_MATRIX);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    public static void refreshGameScreenState(ViewportAPI viewport) {
        if (matrixUBO == 0) return;
        final float llx = viewport.getLLX(), lly = viewport.getLLY();

        _UPLOAD_MATRIX_LOCK.lock();
        _UPLOAD_MATRIX_BUFFER.put(0, llx);
        _UPLOAD_MATRIX_BUFFER.put(1, lly);
        _UPLOAD_MATRIX_BUFFER.put(2, viewport.getVisibleWidth());
        _UPLOAD_MATRIX_BUFFER.put(3, viewport.getVisibleHeight());
        _UPLOAD_MATRIX_BUFFER.position(0);
        _UPLOAD_MATRIX_BUFFER.limit(4);

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 16, _UPLOAD_MATRIX_BUFFER);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        _UPLOAD_MATRIX_LOCK.unlock();
    }

    public static void refreshGameVanillaViewportUBOAll(ViewportAPI viewport) {
        if (matrixUBO == 0) return;
        final float llx = viewport.getLLX(), lly = viewport.getLLY();

        _UPLOAD_MATRIX_LOCK.lock();
        TransformUtil.createGameOrthoMatrix(viewport, false, _UPLOAD_MATRIX_BUFFER);
        _UPLOAD_MATRIX_BUFFER.put(16, llx);
        _UPLOAD_MATRIX_BUFFER.put(17, lly);
        _UPLOAD_MATRIX_BUFFER.put(18, viewport.getVisibleWidth());
        _UPLOAD_MATRIX_BUFFER.put(19, viewport.getVisibleHeight());
        _UPLOAD_MATRIX_BUFFER.position(0);
        _UPLOAD_MATRIX_BUFFER.limit(20);

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, _UPLOAD_MATRIX_BUFFER);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        _UPLOAD_MATRIX_LOCK.unlock();
    }

    public static void refreshGameVanillaViewportUBOAll(FloatBuffer matrix, ViewportAPI viewport) {
        if (matrixUBO == 0) return;
        final float llx = viewport.getLLX(), lly = viewport.getLLY();
        final FloatBuffer buffer;

        _UPLOAD_MATRIX_LOCK.lock();
        if (matrix.capacity() > 19) {
            buffer = matrix;
        } else {
            buffer = _UPLOAD_MATRIX_BUFFER;
            buffer.position(0);
            buffer.put(0, matrix, 0, 16);
        }
        buffer.put(16, llx);
        buffer.put(17, lly);
        buffer.put(18, viewport.getVisibleWidth());
        buffer.put(19, viewport.getVisibleHeight());
        buffer.position(0);
        buffer.limit(20);

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, matrixUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        _UPLOAD_MATRIX_LOCK.unlock();
    }

    public static boolean isFramebufferValid() {
        return renderingBuffer != null && renderingBuffer.isFinished(0) && renderingBuffer.isFinished(1);
    }

    public static void refreshRenderingBuffer() {
        if (renderingBuffer != null) {
            for (byte i = 0; i < BUtil_RenderingBuffer.getBufferCount(); i++) {
                if (renderingBuffer.isFinished(i)) renderingBuffer.delete(i);
            }
            renderingBuffer.deleteBloomPingPongTex();
        }
        renderingBuffer = new BUtil_RenderingBuffer();
        for (byte i = 0; i < BUtil_RenderingBuffer.getBufferCount(); i++) {
            if (renderingBuffer.isFinished(i))
                _LOG.info("'BoxUtil' rendering framebuffer-" + i + " has refreshed.");
            else _LOG.error("'BoxUtil' rendering framebuffer-" + i + " refresh failed.");
        }
        BUtil_GLImpl.Operations.refreshFBOResource(renderingBuffer);
    }

    public static boolean isRenderingFramebufferValid() {
        return renderingBuffer != null && renderingBuffer.isFinished(0) && renderingBuffer.isFinished(1);
    }

    public static boolean isPublicFBOValid() {
        return publicFBO != null && publicFBO.isFinished();
    }

    public static void refreshPublicFBO() {
        if (isPublicFBOValid()) publicFBO.delete();

        publicFBO = new PublicFBO();
        int instance = publicFBO.hashCode();
        if (publicFBO.isFinished()) _LOG.info("'BoxUtil' public framebuffer \"" + instance + "\" has refreshed.");
        else _LOG.error("'BoxUtil' public framebuffer \"" + instance + "\" refresh failed.");
    }

    public static PublicFBO tryPublicFBO() {
        if (publicFBO == null || !publicFBO.isFinished()) refreshPublicFBO();
        return publicFBO;
    }

    public static SimpleVAOAPI getDefaultPointObject() {
        return defaultPointObject;
    }

    public static void refreshDefaultPointObject() {
        if (defaultPointObject != null) defaultPointObject.destroy();
        defaultPointObject = new PointObject();
        if (defaultPointObject.isValid())
            _LOG.info("'BoxUtil' default point object has refreshed.");
        else _LOG.error("'BoxUtil' default point object refresh failed.");
    }

    public static SimpleVAOAPI getDefaultLineObject() {
        return defaultLineObject;
    }

    public static void refreshDefaultLineObject() {
        if (defaultLineObject != null) defaultLineObject.destroy();
        defaultLineObject = new LineObject();
        if (defaultLineObject.isValid())
            _LOG.info("'BoxUtil' default line object has refreshed.");
        else _LOG.error("'BoxUtil' default line object refresh failed.");
    }

    public static SimpleVAOAPI getDefaultQuadObject() {
        return defaultQuadObject;
    }

    public static void refreshDefaultQuadObject() {
        if (defaultQuadObject != null) defaultQuadObject.destroy();
        defaultQuadObject = new QuadObject();
        if (defaultQuadObject.isValid())
            _LOG.info("'BoxUtil' default quad object has refreshed.");
        else _LOG.error("'BoxUtil' default quad object refresh failed.");
    }

    public static boolean isDefaultVAOValid() {
        if (defaultPointObject == null || defaultLineObject == null || defaultQuadObject == null) return false;
        return defaultPointObject.isValid() && defaultLineObject.isValid() && defaultQuadObject.isValid();
    }

    public static void refreshDefaultVAO() {
        refreshDefaultPointObject();
        refreshDefaultLineObject();
        refreshDefaultQuadObject();
    }

    public static int getMatrixUBO() {
        return matrixUBO;
    }

    public static byte getMatrixUBOBinding() {
        return _GLSL_MATRIX_UBO_BINDING;
    }

    public static boolean isInitialized() {
        return glFinished;
    }

    public static boolean isValid() {
        return glValid;
    }

    public static boolean isMainProgramValid() {
        return glMainProgramValid;
    }

    public static boolean isDistortionValid() {
        return glDistortionValid;
    }

    public static boolean isBloomValid() {
        return glBloomValid;
    }

    public static boolean isAreaLightTexValid() {
        return glAreaLightTexValid;
    }

    public static boolean isFXAACValid() {
        return glFXAACValid;
    }

    public static boolean isFXAAQValid() {
        return glFXAAQValid;
    }

    public static boolean isFXAAValid() {
        return glFXAACValid || glFXAAQValid;
    }

    public static boolean isMatrixProgramValid() {
        return glInstanceMatrixValid;
    }

    public static boolean isSDFGenValid() {
        return glSDFGenValid;
    }

    public static boolean isRadialBlurValid() {
        return glRadialBlurValid;
    }

    public static boolean isCompGaussianBlurValid() {
        return glCompGaussianBlurValid;
    }

    public static boolean isCompBilateralFilterValid() {
        return glCompBilateralFilterValid;
    }

    public static boolean isDiscreteFourierValid() {
        return glDiscreteFourierValid;
    }

    public static boolean isNormalMapGenValid() {
        return glNormalMapGenValid;
    }

    public static boolean isLegacyNormalMapGenValid() {
        return glLegacyNormalMapGenValid;
    }

    public static boolean isStaticTrailSystemValid() {
        return glStaticTrailSystemValid;
    }

    private ShaderCore() {}
}
