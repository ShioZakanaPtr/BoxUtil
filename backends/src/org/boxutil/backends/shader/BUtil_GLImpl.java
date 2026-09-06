package org.boxutil.backends.shader;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.boxutil.backends.core.instancedrendering.BUtil_InstanceDataMemoryPool;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.base.StandardShaderpacksPass;
import org.boxutil.base.api.*;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.*;
import org.boxutil.manager.ShaderCore;
import org.boxutil.backends.buffer.BUtil_RenderingBuffer;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.util.TransformUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public final class BUtil_GLImpl {
    private final static BUtil_GLImpl INST = new BUtil_GLImpl();

    private boolean isPaused = false;
    private boolean isInCampaign = false;
    private boolean trailSystemDoCompute = false;
    private boolean shaderpacksIlluminantCleanup = false;
    private byte lastMatrixState = 0;
    private byte lastBlendState = BoxEnum.ENTITY_NORMAL_BLEND;
    private byte lastCullState = BoxEnum.MATERIAL_CULL_BACK;
    private float trailSystemLastAmount = 0.0f;
    private GameState lastGameState = GameState.TITLE;
    private GameState lastTitleGameState = GameState.TITLE;
    private final Matrix4f[] vanillaMat = new Matrix4f[]{new Matrix4f(), new Matrix4f()};
    private final Matrix4f[] perspectiveMat = new Matrix4f[]{new Matrix4f(), new Matrix4f()};
    private final FloatBuffer vanillaMatBuf = BufferUtils.createFloatBuffer(20);
    private final FloatBuffer perspectiveMatBuf = BufferUtils.createFloatBuffer(20);
    private final float[] timer = new float[6]; // lastFrameAmount, frameTime, frameTimePausedCheck, fractionIncludePaused, fractionIncludePaused, staticTrailComputeCycle
    private final int[] fboResource = new int[10];
    private final int[] mousePos = new int[4];

    public static Matrix4f getGameOrthoViewport(byte isCampaign) {
        return INST.vanillaMat[isCampaign];
    }

    public static Matrix4f getGamePerspectiveViewport(byte isCampaign) {
        return INST.perspectiveMat[isCampaign];
    }

    public static void resetTimer() {
        INST.timer[0] = INST.timer[1] = INST.timer[2] = INST.timer[3] = INST.timer[4] = INST.timer[5] = 0.0f;
    }

    public static void advanceTimer(float amount, boolean paused) {
        INST.isPaused = paused;
        INST.timer[0] = amount;
        INST.timer[1] += amount;
        if (!paused) {
            INST.timer[2] += amount;
            if (INST.timer[5] >= BoxConfigs.getTrailSystemNodesRecordsCycle()) {
                INST.trailSystemLastAmount = INST.timer[5] + amount;
                INST.timer[5] = amount;
                INST.trailSystemDoCompute = true;
            } else INST.timer[5] += amount;
        }

        if (!paused) {
            INST.timer[3] += amount;
            INST.timer[3] -= (byte) INST.timer[3];
        }
        INST.timer[4] += amount;
        INST.timer[4] -= (byte) INST.timer[4];
    }

    public static boolean isPaused() {
        return INST.isPaused;
    }

    public static boolean doStaticTrailCompute() {
        return INST.trailSystemDoCompute;
    }

    public static float getStaticTrailFrameAmount() {
        return INST.trailSystemLastAmount;
    }

    public static float getLastFrameAmount() {
        return INST.timer[0];
    }

    public static float getElapsedTime() {
        return INST.timer[1];
    }

    public static float getElapsedTimeWithoutPaused() {
        return INST.timer[2];
    }

    public static float getElapsedTimeFraction() {
        return INST.timer[3];
    }

    public static float getElapsedTimeFractionIncludePaused() {
        return INST.timer[4];
    }

    public static int getMouseX() {
        return INST.mousePos[0];
    }

    public static int getMouseY() {
        return INST.mousePos[1];
    }

    public static int getMouseRawX() {
        return INST.mousePos[2];
    }

    public static int getMouseRawY() {
        return INST.mousePos[3];
    }

    public static void setCampaignFlag() {
        INST.isInCampaign = true;
    }

    public static boolean isInCampaignSector() {
        return INST.isInCampaign;
    }

    public static boolean checkCampaignCleanup() {
        final GameState curr = Global.getCurrentState();
        boolean result = curr != INST.lastGameState && INST.isInCampaign;

        if (INST.isInCampaign && Global.getCombatEngine() != null && Global.getCombatEngine().isInCampaignSim()) {
            INST.lastGameState = GameState.COMBAT;
            return false;
        } else {
            if (INST.isInCampaign) {
                if (curr == GameState.COMBAT) INST.lastTitleGameState = GameState.COMBAT;
                if (curr == GameState.TITLE) {
                    INST.isInCampaign = false;
                    result = true;
                }
            }
            INST.lastGameState = curr;
        }
        return result;
    }

    public static boolean checkTitleCleanup() {
        if (INST.isInCampaign) return false;
        INST.lastGameState = GameState.TITLE;
        final GameState curr = Global.getCurrentState();
        if (curr == GameState.TITLE && Global.getCombatEngine() != null && Global.getCombatEngine().isSimulation()) {
            INST.lastTitleGameState = GameState.COMBAT;
            return true;
        }
        if (curr != GameState.CAMPAIGN && INST.lastTitleGameState != curr) {
            INST.lastTitleGameState = curr;
            return true;
        } else return false;
    }

    public static void refreshFBOResource(BUtil_RenderingBuffer renderingBuffer) {
        if (renderingBuffer != null) {
            INST.fboResource[0] = renderingBuffer.getFBO(0);
            INST.fboResource[1] = renderingBuffer.getColorResult();
            INST.fboResource[2] = renderingBuffer.getEmissiveResult();
            INST.fboResource[3] = renderingBuffer.getWorldPosResult();
            INST.fboResource[4] = renderingBuffer.getNormalResult();
            INST.fboResource[5] = renderingBuffer.getTangentResult();
            INST.fboResource[6] = renderingBuffer.getMaterialResult();
            INST.fboResource[7] = renderingBuffer.getDataResult();
            INST.fboResource[8] = renderingBuffer.getFBO(1);
            INST.fboResource[9] = renderingBuffer.getAuxEmissiveResult();
        }
    }

    public static void refreshCurrFrameState(boolean shaderEnable, ViewportAPI viewport, BaseShaderPacksContext context, final byte isCampaign) {
        INST.mousePos[0] = Global.getSettings().getMouseX();
        INST.mousePos[1] = Global.getSettings().getMouseY();
        INST.mousePos[2] = Mouse.getX();
        INST.mousePos[3] = Mouse.getY();
        TransformUtil.createGameOrthoMatrix(viewport, INST.vanillaMat[isCampaign]);
        TransformUtil.createGamePerspectiveMatrix(40.0f, viewport, INST.perspectiveMat[isCampaign]);
        INST.vanillaMat[isCampaign].store(INST.vanillaMatBuf);
        INST.perspectiveMat[isCampaign].store(INST.perspectiveMatBuf);
        INST.vanillaMatBuf.clear();
        INST.perspectiveMatBuf.clear();
        if (shaderEnable || BoxConfigs.isTrailSystemEnable()) {
            ShaderCore.refreshGameVanillaViewportUBOAll(INST.vanillaMatBuf, viewport);
        }
        if (shaderEnable) {
            GLWrapper.FBO.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLWrapper.FBO.glClearDepth(1.0d);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(1));
            GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT | GLWrapper.Operation.GL_DEPTH_BUFFER_BIT);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));
            GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT | GLWrapper.Operation.GL_DEPTH_BUFFER_BIT);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
            for (InstanceType instanceType : InstanceType.values()) BUtil_InstanceDataMemoryPool.getPool(instanceType).rebindBase();
        }
        StandardShaderpacksPass.resetBloomPass();
        if (BoxConfigs.isTrailSystemEnable() && BUtil_StaticTrailMemoryPool.getTrailTypes() > 0) {
            final var staticTrailProgram = ShaderCore.getStaticTrailProgram();
            if (GLWrapper.Shader.valid_ProgramUniform()) GLWrapper.Shader.glProgramUniform1f(staticTrailProgram.getId(), staticTrailProgram.location[1], INST.timer[2]);
            else {
                staticTrailProgram.active();
                GLWrapper.Shader.glUniform1f(staticTrailProgram.location[1], INST.timer[2]);
                staticTrailProgram.close();
            }
        }
        INST.trailSystemDoCompute = false;
        context.applyBeforeLowestLayerRender(viewport, isCampaign == BoxEnum.TRUE,
                INST.fboResource[0],
                INST.fboResource[1],
                INST.fboResource[2],
                INST.fboResource[3],
                INST.fboResource[4],
                INST.fboResource[5],
                INST.fboResource[6],
                INST.fboResource[7],
                INST.fboResource[8],
                INST.fboResource[9]);
    }

    public static void applyBeforeIlluminationPass(BaseShaderPacksContext context, ViewportAPI viewport, final boolean isCampaign) {
        context.applyBeforeIlluminationPass(viewport, isCampaign,
                INST.fboResource[0],
                INST.fboResource[1],
                INST.fboResource[2],
                INST.fboResource[3],
                INST.fboResource[4],
                INST.fboResource[5],
                INST.fboResource[6],
                INST.fboResource[7],
                INST.fboResource[8],
                INST.fboResource[9]);
    }

    public static void applyAfterIlluminationPass(BaseShaderPacksContext context, ViewportAPI viewport, final boolean isCampaign) {
        context.applyAfterIlluminationPass(viewport, isCampaign,
                INST.fboResource[0],
                INST.fboResource[1],
                INST.fboResource[2],
                INST.fboResource[3],
                INST.fboResource[4],
                INST.fboResource[5],
                INST.fboResource[6],
                INST.fboResource[7],
                INST.fboResource[8],
                INST.fboResource[9]);
    }

    public static void applyLayeredRenderingPlugin(LayeredRenderingPlugin plugin, Object layer, int layerBit, boolean framebufferValid, ViewportAPI viewport) {
        plugin.render(layer, layerBit, framebufferValid, viewport,
                INST.fboResource[0],
                INST.fboResource[1],
                INST.fboResource[2],
                INST.fboResource[3],
                INST.fboResource[4],
                INST.fboResource[5],
                INST.fboResource[6],
                INST.fboResource[7]);
    }

    public static void glMaterialEntityDraw(RenderDataAPI entity, MaterialData material) {
        cullCheck(material.getCullFace());
        glEntityDraw(entity);
    }

    public static void glMaterialEntityFlatDraw(RenderDataAPI entity, MaterialData material) {
        cullCheck(material.getCullFace());
        glEntityDraw(entity);
    }

    public static void glEntityDraw(RenderDataAPI entity) {
        matrixCheck(entity.getPrimeMatrixState(), entity.pickPrimeMatrixPackage_mat4());
        blendCheck(entity.getBlendState(), entity.getBlendColorSRC(), entity.getBlendColorDST(), entity.getBlendAlphaSRC(), entity.getBlendAlphaDST(), entity.getBlendEquation());
        entity.glDraw();
    }

    /**
     * @param mat only req {@link BoxEnum#ENTITY_CUSTOM_PRIME_MATRIX}
     */
    public static void matrixCheck(byte state, final FloatBuffer mat) {
        if (state == INST.lastMatrixState && state != BoxEnum.ENTITY_CUSTOM_PRIME_MATRIX) return;
        switch (state) {
            case BoxEnum.ENTITY_VANILLA_PRIME_MATRIX: {
                ShaderCore.refreshGameViewportMatrix(INST.vanillaMatBuf);
                break;
            }
            case BoxEnum.ENTITY_PERSPECTIVE_PRIME_MATRIX: {
                ShaderCore.refreshGameViewportMatrix(INST.perspectiveMatBuf);
                break;
            }
            case BoxEnum.ENTITY_CUSTOM_PRIME_MATRIX: {
                ShaderCore.refreshGameViewportMatrix(mat);
                break;
            }
            default: { // BoxEnum.ENTITY_NONE_PRIME_MATRIX
                ShaderCore.refreshGameViewportMatrixNone();
            }
        }
        INST.lastMatrixState = state;
    }

    /**
     * GLenum only for {@link BoxEnum#ENTITY_OTHER_BLEND}
     */
    public static void blendCheck(byte state, int srcColor, int dstColor, int srcAlpha, int dstAlpha, int equation) {
        if (state == INST.lastBlendState && state != BoxEnum.ENTITY_OTHER_BLEND) return;
        if (INST.lastBlendState != BoxEnum.ENTITY_DISABLED_BLEND) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
        if (INST.lastBlendState == BoxEnum.ENTITY_OTHER_BLEND) {
            GLWrapper.Operation.glBlendEquationi(0, GLWrapper.Operation.GL_FUNC_ADD);
            GLWrapper.Operation.glBlendEquationi(1, GLWrapper.Operation.GL_FUNC_ADD);
        }
        switch (state) {
            case BoxEnum.ENTITY_NORMAL_BLEND: {
                GLWrapper.Operation.glBlendFunci(0, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
                GLWrapper.Operation.glBlendFunci(1, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
                break;
            }
            case BoxEnum.ENTITY_ADDITIVE_BLEND: {
                GLWrapper.Operation.glBlendFunci(0, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE);
                GLWrapper.Operation.glBlendFunci(1, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE);
                break;
            }
            case BoxEnum.ENTITY_OTHER_BLEND: {
                GLWrapper.Operation.glBlendFuncSeparatei(0, srcColor, dstColor, srcAlpha, dstAlpha);
                GLWrapper.Operation.glBlendEquationi(0, equation);
                GLWrapper.Operation.glBlendFuncSeparatei(1, srcColor, dstColor, srcAlpha, dstAlpha);
                GLWrapper.Operation.glBlendEquationi(1, equation);
                break;
            }
            default: { // BoxEnum.ENTITY_DISABLED_BLEND
                GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_BLEND);
            }
        }
        INST.lastBlendState = state;
    }

    public static void cullCheck(byte state) {
        if (state == INST.lastCullState) return;
        if (INST.lastCullState != BoxEnum.MATERIAL_CULL_DISABLED) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_CULL_FACE);
        switch (state) {
            case BoxEnum.MATERIAL_CULL_BACK: {
                GLWrapper.Operation.glCullFace(GLWrapper.Operation.GL_BACK);
                break;
            }
            case BoxEnum.MATERIAL_CULL_FRONT: {
                GLWrapper.Operation.glCullFace(GLWrapper.Operation.GL_FRONT);
                break;
            }
            case BoxEnum.MATERIAL_CULL_FRONT_BACK: {
                GLWrapper.Operation.glCullFace(GLWrapper.Operation.GL_FRONT_AND_BACK);
                break;
            }
            default: { // BoxEnum.MATERIAL_CULL_DISABLED
                GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_CULL_FACE);
            }
        }
        INST.lastCullState = state;
    }

    public static void resetGLAttrib() {
        INST.lastMatrixState = BoxEnum.ENTITY_VANILLA_PRIME_MATRIX;
        if (INST.lastBlendState != BoxEnum.ENTITY_NORMAL_BLEND) {
            if (INST.lastBlendState == BoxEnum.ENTITY_DISABLED_BLEND) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
            if (INST.lastBlendState == BoxEnum.ENTITY_OTHER_BLEND) {
                GLWrapper.Operation.glBlendEquationi(0, GLWrapper.Operation.GL_FUNC_ADD);
                GLWrapper.Operation.glBlendEquationi(1, GLWrapper.Operation.GL_FUNC_ADD);
            }
            GLWrapper.Operation.glBlendFunci(0, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
            GLWrapper.Operation.glBlendFunci(1, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
            INST.lastBlendState = BoxEnum.ENTITY_NORMAL_BLEND;
        }
        if (INST.lastCullState != BoxEnum.MATERIAL_CULL_BACK) {
            if (INST.lastCullState == BoxEnum.MATERIAL_CULL_DISABLED) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_CULL_FACE);
            GLWrapper.Operation.glCullFace(GLWrapper.Operation.GL_BACK);
            INST.lastCullState = BoxEnum.MATERIAL_CULL_BACK;
        }
    }

    public static void removeCheck(Iterator<RenderDataAPI> iterator, ControlDataAPI data, RenderDataAPI entity) {
        boolean toRemove = false;
        if (data != null) {
            data.controlAfterRenderingAdvance(entity, getLastFrameAmount());
            if (data.controlIsOnceRender(entity)) {
                entity.delete();
                toRemove = true;
            }
        } else if (entity.isGlobalTimerOnce()) {
            toRemove = true;
        }
        if (toRemove) iterator.remove();
    }

    public static void glDisabledIterator(List<RenderDataAPI> list) {
        if (list.isEmpty()) return;
        RenderDataAPI entity;
        for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
            entity = entitiesI.next();
            if (entity == null) {
                entitiesI.remove();
                continue;
            }
            if (entity.hasDelete()) continue;
            ControlDataAPI data = entity.getControlData();
            if (data != null) {
                data.controlBeforeRenderingAdvance(entity, getLastFrameAmount());
                if (!data.controlCanRenderNow(entity)) continue;
            }
            removeCheck(entitiesI, data, entity);
        }
    }

    public static void glScreenBlit() {
        final int blitWidth = ShaderCore.getScreenScaleWidth();
        final int blitHeight = ShaderCore.getScreenScaleHeight();
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));
        GLWrapper.FBO.glDrawBuffers(GLWrapper.FBO.GL_COLOR_ATTACHMENT0);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_READ_FRAMEBUFFER, 0);
        GLWrapper.FBO.glBlitFramebuffer(0, 0, blitWidth, blitHeight, 0, 0, blitWidth, blitHeight, GLWrapper.Operation.GL_COLOR_BUFFER_BIT, GLWrapper.Texture.GL_NEAREST);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));

        IntBuffer drawBuffer = ShaderCore.getRenderingBuffer().getDrawBufferConfig((byte) 0);
        drawBuffer.position(0);
        drawBuffer.limit(drawBuffer.capacity());
        GLWrapper.FBO.glDrawBuffers(drawBuffer);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
    }

    public static boolean checkSkipMeshCurrentLayout(EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, Set<LayeredRenderingPlugin> renderingPlugins) {
        boolean result = true;
        if (meshMap != null) {
            for (List<RenderDataAPI> list : meshMap.values()) {
                if (list != null && !list.isEmpty()) {
                    result = false;
                    break;
                }
            }
        }
        return result && (renderingPlugins == null || renderingPlugins.isEmpty());
    }

    public static void callIlluminantCleanupForShaderPacks(BaseShaderPacksContext curr, BaseShaderPacksContext toSwitch) {
        if (curr.haveCustomInstanceDataLayout() || toSwitch.haveCustomInstanceDataLayout()) INST.shaderpacksIlluminantCleanup = true;
    }

    public static boolean checkIlluminantCleanupForShaderPacks() {
        final boolean result = INST.shaderpacksIlluminantCleanup;
        INST.shaderpacksIlluminantCleanup = false;
        return result;
    }

    public static void processShaderpacksResultPass(boolean shaderEnable, ViewportAPI viewport, boolean isCampaign, BaseShaderPacksContext context) {
        if (shaderEnable) {
            GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_DEPTH_TEST);
            GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_CULL_FACE);
            GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_BLEND);
            ShaderCore.glMultiPass();
        }
        if (BoxConfigs.isMultiPassBeauty() && context.isAASupported()) {
            context.applyAAPass(viewport, isCampaign,
                    INST.fboResource[0],
                    INST.fboResource[1],
                    INST.fboResource[2],
                    INST.fboResource[3],
                    INST.fboResource[4],
                    INST.fboResource[5],
                    INST.fboResource[6],
                    INST.fboResource[7]);
        }
        if (BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassBloom()) {
            if (context.isBloomSupported()) {
                context.applyBloomPass(viewport, isCampaign, BoxConfigs.isMultiPassBloom(),
                        INST.fboResource[0],
                        INST.fboResource[1],
                        INST.fboResource[2],
                        INST.fboResource[3],
                        INST.fboResource[4],
                        INST.fboResource[5],
                        INST.fboResource[6],
                        INST.fboResource[7],
                        INST.fboResource[8],
                        INST.fboResource[9]);
            }
        }
    }

    private BUtil_GLImpl() {}
}
