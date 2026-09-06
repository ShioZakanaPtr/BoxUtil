package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.apache.log4j.Logger;
import org.boxutil.backends.shader.BUtil_ShaderProgramManager;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.backends.buffer.BUtil_RenderingBuffer;
import org.boxutil.backends.core.thread.BUtil_BoxUtilBackgroundThread;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.define.GLWrapper;
import org.boxutil.units.standard.misc.LineObject;
import org.boxutil.units.standard.misc.PointObject;
import org.boxutil.units.standard.misc.PublicFBO;
import org.boxutil.units.standard.misc.QuadObject;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.TransformUtil;
import org.boxutil.util.concurrent.SpinLock;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.concurrent.locks.Lock;

@SuppressWarnings("UnusedReturnValue")
public final class ShaderCore {
    private final static byte _GLSL_MATRIX_UBO_BINDING = 0;
    private final static Lock _UPLOAD_MATRIX_LOCK = new SpinLock();
    private final static FloatBuffer _NONE_GAME_MATRIX;
    private final static FloatBuffer _UPLOAD_MATRIX_BUFFER;
    private final static int _GL_ATTRIB_BITS;
    private static BUtil_RenderingBuffer renderingBuffer = null;
    private static PublicFBO publicFBO = null;
    private static SimpleVAOAPI defaultPointObject = null;
    private static SimpleVAOAPI defaultLineObject = null;
    private static SimpleVAOAPI defaultQuadObject = null;
    private static int matrixUBO = 0;
    private static boolean glValid = false;
    private static boolean glGlobalDataUBOValid = false;
    private static final int[] screenSize = new int[2];
    private static final int[] screenSizeScale = new int[2];
    private static final int[] screenSizeFix = new int[2];
    private static final float[] screenSizeUV = new float[2];

    static {
        _NONE_GAME_MATRIX = CommonUtil.createIdentityMatrix4x4f();
        _UPLOAD_MATRIX_BUFFER = CommonUtil.createFloatBuffer(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);;
        _GL_ATTRIB_BITS = GLWrapper.Operation.GL_COLOR_BUFFER_BIT |
                GLWrapper.Operation.GL_DEPTH_BUFFER_BIT |
                GLWrapper.Operation.GL_ENABLE_BIT |
                GLWrapper.Operation.GL_POLYGON_BIT |
                GLWrapper.Operation.GL_SCISSOR_BIT |
                GLWrapper.Operation.GL_STENCIL_BUFFER_BIT |
                GLWrapper.Operation.GL_VIEWPORT_BIT;
    }

    private final static BUtil_ShaderProgramManager PROGRAM_MANAGER = new BUtil_ShaderProgramManager();

    private static final Logger _LOG = Global.getLogger(ShaderCore.class);

    public static void initCore() {
        _LOG.info("'BoxUtil' OpenGL context running on: '" + BoxDatabase.getGLState().GL_CURRENT_DEVICE_NAME + "' with drive version: '" + BoxDatabase.getGLState().GL_CURRENT_DEVICE_VERSION + "'.");
        PROGRAM_MANAGER.initCore();
        refreshRenderingBuffer();
        refreshDefaultVAO();

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

        if (!PROGRAM_MANAGER.isCoreValid() || !isRenderingFramebufferValid() || !isDefaultVAOValid()) {
            closeShader();
            _LOG.error("'BoxUtil' base shader resource init failed, core program: " + PROGRAM_MANAGER.isCoreValid() + ", rendering framebuffer: " + isRenderingFramebufferValid() + ", default VAO: " + isDefaultVAOValid() + ".");
            return;
        }
        glValid = true;
    }

    public static void initOptional() {
        PROGRAM_MANAGER.initOptional();
        PROGRAM_MANAGER.initStaticTrailSystemProgram(glValid);
        initGlobalDataUBO();
    }

    public static void initGlobalDataUBO() {
        if (!GLWrapper.Buffer.UBO.valid()) {
            closeShader();
            _LOG.error("'BoxUtil' platform is not supported Uniform buffer object. ");
            return;
        }
        matrixUBO = GLWrapper.Buffer.glGenBuffers();
        if (matrixUBO == 0) {
            closeShader();
            _LOG.error("'BoxUtil' shader UBO init failed. ");
            return;
        }

        final int bufTarget = GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER;
        GLWrapper.Buffer.glBindBuffer(bufTarget, matrixUBO);
        GLWrapper.Buffer.glBufferData(bufTarget, 20 * BoxDatabase.FLOAT_SIZE, GLWrapper.Buffer.GL_DYNAMIC_DRAW);
        GLWrapper.Buffer.glBindBuffer(bufTarget, 0);
        GLWrapper.Buffer.UBO.glBindBufferBase(bufTarget, getMatrixUBOBinding(), matrixUBO);
        glGlobalDataUBOValid = true;
    }

    private static void closeShader() {
        glValid = false;
    }

    public static void glBeginDraw() {
        GLWrapper.Operation.glPushAttrib(_GL_ATTRIB_BITS);
        GLWrapper.Operation.glViewport(0, 0, screenSizeScale[0], screenSizeScale[1]);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_ALPHA_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_SCISSOR_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_STENCIL_TEST);
        if (GLWrapper.Operation.valid_DepthClamp()) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_DEPTH_CLAMP);
        GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
        GLWrapper.Operation.glCullFace(GLWrapper.Operation.GL_BACK);
        GLWrapper.Operation.glFrontFace(GLWrapper.Operation.GL_CCW);
        GLWrapper.Operation.glDepthFunc(GLWrapper.Operation.GL_LESS);
        GLWrapper.Operation.glDepthRange(-1.0d, 1.0d);
        GLWrapper.Operation.glDepthMask(true);
        GLWrapper.Operation.glColorMask(true, true, true, true);
        if (GLWrapper.Operation.valid_BlendFuncSeparate()) GLWrapper.Operation.glBlendFuncSeparate(GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA, GLWrapper.Operation.GL_ZERO, GLWrapper.Operation.GL_ONE); // but changes color attachment 0 and 1
        if (GLWrapper.Operation.valid_BlendEquation()) GLWrapper.Operation.glBlendEquation(GLWrapper.Operation.GL_FUNC_ADD);
        if (BoxConfigs.isShaderEnable()) BUtil_GLImpl.resetGLAttrib();
    }

    public static void glEndDraw() {
        if (GLWrapper.VAO.valid()) GLWrapper.VAO.glBindVertexArray(0);
        if (GLWrapper.Buffer.VBO.valid()) GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        if (GLWrapper.Buffer.TBO.valid()) GLWrapper.Buffer.TBO.glBindBuffer(GLWrapper.Buffer.TBO.GL_TEXTURE_BUFFER, 0);
        if (GLWrapper.Drawcall.MultiTex.valid()) GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0);
        GLWrapper.Operation.glPopAttrib();
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
        final var program = PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.DIRECT);
        program.active();
        GLWrapper.Shader.glUniform1f(program.location[0], 1.0f);
        program.bindTexture2D(0, texture);
        defaultQuadObject.glBind();
        defaultQuadObject.glDraw();
        defaultQuadObject.glReleaseBind();
        program.close();
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
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.COMMON);
    }

    public static BaseShaderData getSpriteProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SPRITE);
    }

    public static BaseShaderData getCurveProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.CURVE);
    }

    public static BaseShaderData getSegmentProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SEGMENT);
    }

    public static BaseShaderData getTrailProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.TRAIL);
    }

    public static BaseShaderData getFlareProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.FLARE);
    }

    public static BaseShaderData getTextProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.TEXT);
    }

    public static BaseShaderData getDistortionProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.DIST);
    }

    public static BaseShaderData getDirectDrawProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.DIRECT);
    }

    public static BaseShaderData getInstanceMatrix2DProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.MATRIX_2D);
    }

    public static BaseShaderData getInstanceMatrix3DProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.MATRIX_3D);
    }

    public static BaseShaderData getSDFInitProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SDF_INIT);
    }

    public static BaseShaderData getSDFProcessProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SDF_PROCESS);
    }

    public static BaseShaderData getSDFResultProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SDF_RESULT);
    }

    public static BaseShaderData getRadialBlurProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.RADIAL_BLUR);
    }

    public static BaseShaderData getCompGaussianBlurProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.GAUSSIAN_BLUR);
    }

    public static BaseShaderData getCompGaussianBlurRedProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.GAUSSIAN_BLUR_RED);
    }

    public static BaseShaderData getCompBilateralFilterProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.BILATERAL_FILTER);
    }

    public static BaseShaderData getCompBilateralFilterRedProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.BILATERAL_FILTER_RED);
    }

    public static BaseShaderData getDFTProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.DFT);
    }

    public static BaseShaderData getDFTRedProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.DFT_RED);
    }

    public static BaseShaderData getNormalMapGenInitProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.NORMAL_GEN_INIT);
    }

    public static BaseShaderData getNormalMapGenResultProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.NORMAL_GEN_RESULT);
    }

    public static BaseShaderData getNumberProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SIMPLE_NUMBER);
    }

    public static BaseShaderData getArcProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SIMPLE_ARC);
    }

    public static BaseShaderData getTexArcProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.SIMPLE_TEX_ARC);
    }

    public static BaseShaderData getTestMissionProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.MISSION_BG);
    }

    public static BaseShaderData getFXAAConsoleProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.FXAA_C);
    }

    public static BaseShaderData getFXAAQualityProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.FXAA_Q);
    }

    public static BaseShaderData getBloomProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.BLOOM);
    }

    public static BaseShaderData getAreaLightTex() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.AREA_LIGHT_PRE_FILTERING);
    }

    public static BaseShaderData getLegacyNormalGenBlurProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.LEGACY_NORMAL_BLUR);
    }

    public static BaseShaderData getLegacyNormalGenResultProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.LEGACY_NORMAL_RESULT);
    }

    public static BaseShaderData getStaticTrailProgram() {
        return PROGRAM_MANAGER.program(BUtil_ShaderProgramManager.STATIC_TRAIL_SYSTEM);
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
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0, _UPLOAD_MATRIX_BUFFER);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
        _UPLOAD_MATRIX_LOCK.unlock();
    }

    public static void refreshGameViewportMatrix(FloatBuffer matrix) {
        if (matrixUBO == 0) return;
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0, matrix);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
    }

    public static void refreshGameViewportMatrixNone() {
        if (matrixUBO == 0) return;
        _NONE_GAME_MATRIX.position(0);
        _NONE_GAME_MATRIX.limit(_NONE_GAME_MATRIX.capacity());
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0, _NONE_GAME_MATRIX);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
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

        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 16, _UPLOAD_MATRIX_BUFFER);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
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

        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0, _UPLOAD_MATRIX_BUFFER);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
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

        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, matrixUBO);
        GLWrapper.Buffer.UBO.glBufferSubData(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0, buffer);
        GLWrapper.Buffer.UBO.glBindBuffer(GLWrapper.Buffer.UBO.GL_UNIFORM_BUFFER, 0);
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
        BUtil_GLImpl.refreshFBOResource(renderingBuffer);
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

    @Deprecated
    public static boolean isInitialized() {
        return glValid;
    }

    public static boolean isValid() {
        return glValid;
    }

    public static boolean isCoreProgramValid() {
        return PROGRAM_MANAGER.isCoreValid();
    }

    public static boolean isGlobalDataUBOValid() {
        return glGlobalDataUBOValid;
    }

    public static boolean isBloomValid() {
        return PROGRAM_MANAGER.isBloomValid();
    }

    public static boolean isAreaLightTexValid() {
        return PROGRAM_MANAGER.isAreaLightTexValid();
    }

    public static boolean isFXAACValid() {
        return PROGRAM_MANAGER.isFXAACValid();
    }

    public static boolean isFXAAQValid() {
        return PROGRAM_MANAGER.isFXAAQValid();
    }

    public static boolean isFXAAValid() {
        return PROGRAM_MANAGER.isFXAABothValid();
    }

    public static boolean isSDFGenValid() {
        return PROGRAM_MANAGER.isSDFGenValid();
    }

    public static boolean isRadialBlurValid() {
        return PROGRAM_MANAGER.isRadialBlurValid();
    }

    public static boolean isCompGaussianBlurValid() {
        return PROGRAM_MANAGER.isCompGaussianBlurValid();
    }

    public static boolean isCompBilateralFilterValid() {
        return PROGRAM_MANAGER.isCompBilateralFilterValid();
    }

    public static boolean isDiscreteFourierValid() {
        return PROGRAM_MANAGER.isDiscreteFourierValid();
    }

    public static boolean isNormalMapGenValid() {
        return PROGRAM_MANAGER.isNormalMapGenValid();
    }

    public static boolean isLegacyNormalMapGenValid() {
        return PROGRAM_MANAGER.isLegacyNormalMapGenValid();
    }

    public static boolean isStaticTrailShaderValid() {
        return PROGRAM_MANAGER.isStaticTrailShaderValid();
    }

    private ShaderCore() {}
}
