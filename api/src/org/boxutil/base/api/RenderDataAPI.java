package org.boxutil.base.api;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public interface RenderDataAPI {
    void delete();

    /**
     * @return Should not use this entity when true.
     */
    boolean hasDelete();

    void glDraw();

    @Deprecated
    boolean isUseCustomDrawShader();

    @Deprecated
    byte getUseCustomDrawShader();

    @Deprecated
    void setUseCustomDrawShader(boolean enable);

    void reset();

    boolean isAutoSubmitEntityData();

    /**
     * Enabling auto-submit incurs a slight memory copy overhead (default behavior, also with potential heap memory allocation).<p>
     * If data changes are infrequent, you may disable this feature and use {@link RenderDataAPI#submitEntityData()} for manual submits instead, which can reduce unnecessary memory copies to a certain extent.
     */
    void setAutoSubmitEntityData(boolean toggleAuto);

    /**
     * Without matrix data or timer settings.
     */
    void submitEntityData();

    ControlDataAPI getControlData();

    /**
     * For custom your rendering entity.<p>
     * Always execute {@link ControlDataAPI#controlInit(RenderDataAPI)} when call this method.<p>
     * More than one {@link RenderDataAPI} entities always can be using a same data entity.
     */
    void setControlData(@Nullable ControlDataAPI data);

    Object getCustomData();

    Object setCustomData(Object value);

    float[] getGlobalTimer();

    void setGlobalTimer(float fadeIn, float full, float fadeOut);

    /**
     * Automatic running, not required to call it.
     */
    void advanceGlobalTimer(float amount, boolean isPausedNow);

    byte getGlobalTimerState();

    float getGlobalTimerAlpha();

    boolean isGlobalTimerOver();

    boolean isGlobalTimerOnce();

    /**
     * For once frame rendering instance data, should use fixed type instance data.
     */
    void setGlobalTimerOnce();

    boolean isTimingWhenPaused();

    /**
     * Affects entity timer, and instance data.
     */
    void setTimingWhenPaused(boolean timing);

    boolean isTimerPaused();

    /**
     * Block timer even if not in paused.
     */
    void setTimerPaused(boolean paused);

    Matrix4f getPrimeMatrix();

    /**
     * When upload a custom prime matrix, call {@link RenderDataAPI#setCustomPrimeMatrix()} with this method.
     *
     * @return 4x4 matrix in buffer with 16 capacity.
     */
    FloatBuffer getPrimeMatrixBuffer();

    boolean isAutoSubmitPrimeMatrix();

    /**
     * Enabling auto-submit incurs a slight memory copy overhead (default behavior, also with potential heap memory allocation).<p>
     * If data changes are infrequent, you may disable this feature and use {@link RenderDataAPI#submitPrimeMatrix()} for manual submits instead, which can reduce unnecessary memory copies to a certain extent.<p>
     * Alternatively, use {@link RenderDataAPI#getPrimeMatrixBuffer()} to directly operate on the corresponding buffer after this feature was disabled;
     */
    void setAutoSubmitPrimeMatrix(boolean toggleAuto);

    /**
     * Only required when use a custom prime matrix.
     */
    void submitPrimeMatrix();

    void initIdentityPrimeMatrix();

    /**
     * Generally, in there it is viewport and camera matrix, means 'main' not 'prime matrix'.<p>
     * In shader program: <strong>[this matrix * model matrix * vertex]</strong><p>
     * Must call {@link RenderDataAPI#setCustomPrimeMatrix()} if use it.
     *
     * @param primeMatrix for usual, this is a matrix as <strong>[Projection * Look-at]</strong>.
     */
    void setPrimeMatrix(Matrix4f primeMatrix);

    byte getPrimeMatrixState();

    void setVanillaPrimeMatrix();

    void setPerspectivePrimeMatrix();

    void setCustomPrimeMatrix();

    void setNonePrimeMatrix();

    Matrix4f getModelMatrix();

    /**
     * @return 4x4 matrix in buffer with 16 capacity.
     */
    FloatBuffer getModelMatrixBuffer();

    boolean isAutoSubmitModelMatrix();

    /**
     * Enabling auto-submit incurs a slight memory copy overhead (default behavior, also with potential heap memory allocation).<p>
     * If data changes are infrequent, you may disable this feature and use {@link RenderDataAPI#submitModelMatrix()} for manual submits instead, which can reduce unnecessary memory copies to a certain extent.<p>
     * Alternatively, use {@link RenderDataAPI#getModelMatrixBuffer()} to directly operate on the corresponding buffer after this feature was disabled;
     */
    void setAutoSubmitModelMatrix(boolean toggleAuto);

    void submitModelMatrix();

    void initIdentityModelMatrix();

    /**
     * @param modelMatrix for usual, this is a model matrix.
     */
    void setModelMatrix(@NotNull Matrix4f modelMatrix);

    void setLocation(float x, float y);

    void setLocation(float x, float y, float z);

    void setLocation(Vector2f location);

    void setLocation(Vector3f location);

    void setFacingScale(float facing, float scaleX, float scaleY);

    void setRotateScale(Vector3f rotate, Vector3f scale);

    void setStateVanilla(Vector2f location, float facing, Vector2f scale);

    void setStateVanilla(Vector2f location, float facing);

    void appendToEntity(CombatEntityAPI target, float offsetAngle, Vector3f scale);

    void appendToEntity(CombatEntityAPI target, float offsetAngle, Vector2f scale);

    void appendToEntity(CombatEntityAPI target, float offsetAngle);

    void appendToEntity(CombatEntityAPI target);

    /**
     * Only for system rendering calls, not applicable to developer.
     */
    FloatBuffer pickPrimeMatrixPackage_mat4();

    /**
     * Only for system rendering calls, not applicable to developer.
     */
    FloatBuffer pickModelMatrixPackage_mat4();

    /**
     * Only for system rendering calls, not applicable to developer.
     */
    FloatBuffer pickDataPackage_vec4();

    int getBlendColorSRC();

    int getBlendAlphaSRC();

    int getBlendColorDST();

    int getBlendAlphaDST();

    int getBlendEquation();

    byte getBlendState();

    /**
     * @see org.lwjgl.opengl.GL11#glBlendFunc(int, int)
     */
    void setBlendFunc(int srcFactor, int dstFactor);

    /**
     * @see org.lwjgl.opengl.GL14#glBlendFuncSeparate(int, int, int, int)
     */
    void setBlendFuncSeparate(int srcColorFactor, int dstColorFactor, int srcAlphaFactor, int dstAlphaFactor);

    /**
     * @see org.lwjgl.opengl.GL14#glBlendEquation(int)
     */
    void setBlendEquation(int mode);

    void setAdditiveBlend();

    /**
     * Default blend mode.
     */
    void setNormalBlend();

    /**
     * As custom blend mode.
     */
    void setNegativeBlend();

    void setDisableBlend();

    Object getLayer();

    CombatEngineLayers getCombatLayer();

    CampaignEngineLayers getCampaignLayer();

    void setLayer(Object layer);

    void setDefaultCombatLayer();

    void setDefaultCampaignLayer();

    Object entityType();
}
