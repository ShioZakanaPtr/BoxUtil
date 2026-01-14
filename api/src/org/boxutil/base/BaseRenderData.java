package org.boxutil.base;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.concurrent.ReentrantSpinLock;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.api.ControlDataAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.TransformUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public abstract class BaseRenderData implements RenderDataAPI {
    protected byte primeMatrixState = 0;
    protected byte _blendState = 0;
    protected boolean timingWhenPaused = false;
    protected boolean isTimerPaused = false;
    protected volatile boolean _hasDelete;

    protected final SpinLock _sync_lock = new SpinLock();
    protected final FloatBuffer _primeMatBuffer;
    protected final FloatBuffer _matrixBuffer;
    protected final FloatBuffer _statePackageBuffer;
    protected Matrix4f primeMatrix = new Matrix4f();
    protected Matrix4f modelMatrix = new Matrix4f();

    protected final int[] blendConfig = new int[]{GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL14.GL_FUNC_ADD};
    protected final float[] globalTimer = new float[]{-512.0f, -512.0f, -512.0f, -512.0f}; // time, fade in, full, fade out.

    protected ControlDataAPI controlData = null;
    protected Object customData = null;
    protected Object layer = null;

    protected int _StatePackageStack() {
        return 1;
    }

    public BaseRenderData() {
        this._primeMatBuffer = BufferUtils.createFloatBuffer(16);
        this._matrixBuffer = BufferUtils.createFloatBuffer(16);
        this._statePackageBuffer = BufferUtils.createFloatBuffer(this._StatePackageStack() << 2);
    }

    protected void _deleteExc() {
        this._hasDelete = true;
        this._primeMatBuffer.clear();
        this._matrixBuffer.clear();
        this._statePackageBuffer.clear();
        if (this.getControlData() != null) this.getControlData().controlRemove(this);
    }

    public void delete() {
        this._sync_lock.lock();
        this._deleteExc();
        this._sync_lock.unlock();
    }

    /**
     * @return Should not use this entity when true.
     */
    public boolean hasDelete() {
        return this._hasDelete;
    }

    public void glDraw() {
        ShaderCore.getDefaultQuadObject().glDraw();
    }

    @Deprecated
    public boolean isUseCustomDrawShader() {
        return false;
    }

    @Deprecated
    public byte getUseCustomDrawShader() {
        return 0;
    }

    @Deprecated
    public void setUseCustomDrawShader(boolean enable) {}

    protected void _resetExc() {
        this.globalTimer[0] = -512.0f;
        this.globalTimer[1] = -512.0f;
        this.globalTimer[2] = -512.0f;
        this.globalTimer[3] = -512.0f;
        Matrix4f.setIdentity(this.primeMatrix);
        this.primeMatrixState = 0;
        Matrix4f.setIdentity(this.modelMatrix);
        this.blendConfig[0] = GL11.GL_SRC_ALPHA;
        this.blendConfig[1] = GL11.GL_SRC_ALPHA;
        this.blendConfig[2] = GL11.GL_ONE_MINUS_SRC_ALPHA;
        this.blendConfig[3] = GL11.GL_ONE_MINUS_SRC_ALPHA;
        this.blendConfig[4] = GL14.GL_FUNC_ADD;
        this._blendState = 0;
        this.layer = null;
    }

    public void reset() {
        this._sync_lock.lock();
        this._resetExc();
        this._sync_lock.unlock();
    }

    public ControlDataAPI getControlData() {
        return this.controlData;
    }

    /**
     * For custom your rendering entity.<p>
     * Always execute {@link ControlDataAPI#controlInit(RenderDataAPI)} when call this method.<p>
     * More than one {@link RenderDataAPI} entities always can be using a same data entity.
     */
    public void setControlData(@Nullable ControlDataAPI data) {
        this.controlData = data;
        if (this.controlData != null) this.controlData.controlInit(this);
    }

    public Object getCustomData() {
        return this.customData;
    }

    public Object setCustomData(Object value) {
        this.customData = value;
        return this.customData;
    }

    public float[] getGlobalTimer() {
        return this.globalTimer;
    }

    public void setGlobalTimer(float fadeIn, float full, float fadeOut) {
        if (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) {
            this.setGlobalTimerOnce();
            return;
        }
        this.globalTimer[0] = 3.0f;
        if (fadeIn <= 0.0f) {
            this.globalTimer[1] = -512.0f;
            this.globalTimer[0] = 2.0f;
            if (full <= 0.0f) this.globalTimer[0] = 1.0f;
        } else this.globalTimer[1] = 1.0f / fadeIn;
        this.globalTimer[2] = full <= 0.0f ? -512.0f : 1.0f / full;
        this.globalTimer[3] = fadeOut <= 0.0f ? -512.0f : 1.0f / fadeOut;
    }

    /**
     * Automatic running, not required to call it.
     */
    public void advanceGlobalTimer(float amount, boolean isPausedNow) {
        if (this.isTimerPaused() || this.isGlobalTimerOnce() || (!this.isTimingWhenPaused() && isPausedNow)) return;
        float[] tmpTimer = new float[]{-512.0f, 0.0f};
        if (this.globalTimer[0] > 2.0f) {
            tmpTimer[0] = this.globalTimer[1];
            tmpTimer[1] = 2.0f;
        } else if (this.globalTimer[0] > 1.0f) {
            tmpTimer[0] = this.globalTimer[2];
            tmpTimer[1] = 1.0f;
        } else if (this.globalTimer[0] > 0.0f) {
            tmpTimer[0] = this.globalTimer[3];
        }
        this.globalTimer[0] = tmpTimer[0] > -500.0f ? this.globalTimer[0] - tmpTimer[0] * amount : tmpTimer[1];
        this.globalTimer[0] = Math.max(this.globalTimer[0], 0.0f);
    }

    public byte getGlobalTimerState() {
        if (this.globalTimer[0] > 2.0f) return BoxEnum.TIMER_IN;
        else if (this.globalTimer[0] > 1.0f) return BoxEnum.TIMER_FULL;
        else if (this.globalTimer[0] > 0.0f) return BoxEnum.TIMER_OUT;
        else return this.isGlobalTimerOnce() ? BoxEnum.TIMER_ONCE : BoxEnum.TIMER_INVALID;
    }

    public float getGlobalTimerAlpha() {
        if (this.getControlData() != null && !this.getControlData().controlAlphaBasedTimer(this)) return 1.0f;
        float alpha = 1.0f;
        if (this.globalTimer[0] > 2.0f) alpha = Math.abs(this.globalTimer[0] - 3.0f);
        if (this.globalTimer[0] < 1.0f && this.globalTimer[0] > -500.0f) alpha = this.globalTimer[0];
        return alpha;
    }

    public boolean isGlobalTimerOver() {
        return this.globalTimer[0] <= 0.0f && this.globalTimer[0] > -500.0f;
    }

    public boolean isGlobalTimerOnce() {
        return this.globalTimer[0] < -500.0f;
    }

    /**
     * For once frame rendering instance data, should use fixed type instance data.
     */
    public void setGlobalTimerOnce() {
        this.globalTimer[0] = this.globalTimer[1] = this.globalTimer[2] = this.globalTimer[3] = -512.0f;
    }

    public boolean isTimingWhenPaused() {
        return this.timingWhenPaused;
    }

    /**
     * Affects entity timer, and instance data.
     */
    public void setTimingWhenPaused(boolean timing) {
        this.timingWhenPaused = timing;
    }

    public boolean isTimerPaused() {
        return this.isTimerPaused;
    }

    /**
     * Block timer even if not in paused.
     */
    public void setTimerPaused(boolean paused) {
        this.isTimerPaused = paused;
    }

    public Matrix4f getPrimeMatrix() {
        return this.primeMatrix;
    }

    public void initIdentityPrimeMatrix() {
        Matrix4f.setIdentity(this.primeMatrix);
    }

    /**
     * Generally, it is viewport and camera matrix.<p>
     * In shader program: <strong>[this matrix * model matrix * vertex]</strong><p>
     * Must call {@link RenderDataAPI#setCustomPrimeMatrix()} if use it.
     *
     * @param primeMatrix for usual, this is a matrix as <strong>[Projection * Look-at]</strong>.
     */
    public void setPrimeMatrix(Matrix4f primeMatrix) {
        if (primeMatrix == null) this.primeMatrixState = 0;
        this.primeMatrix = primeMatrix;
    }

    public byte getPrimeMatrixState() {
        return this.primeMatrixState;
    }

    public void setVanillaPrimeMatrix() {
        this.primeMatrixState = 0;
    }

    public void setPerspectivePrimeMatrix() {
        this.primeMatrixState = 1;
    }

    public void setCustomPrimeMatrix() {
        this.primeMatrixState = 2;
    }

    public void setNonePrimeMatrix() {
        this.primeMatrixState = 3;
    }

    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }

    public void initIdentityModelMatrix() {
        Matrix4f.setIdentity(this.modelMatrix);
    }

    /**
     * @param modelMatrix for usual, this is a model matrix.
     */
    public void setModelMatrix(@NotNull Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void setLocation(float x, float y) {
        this.modelMatrix.m30 = x;
        this.modelMatrix.m31 = y;
    }

    public void setLocation(float x, float y, float z) {
        this.setLocation(x, y);
        this.modelMatrix.m32 = z;
    }

    public void setLocation(Vector2f location) {
        this.modelMatrix.m30 = location.x;
        this.modelMatrix.m31 = location.y;
    }

    public void setLocation(Vector3f location) {
        this.setLocation(location.x, location.y, location.z);
    }

    public void setFacingScale(float facing, float scaleX, float scaleY) {
        Matrix2f rotate = TransformUtil.createSimpleRotateMatrix(facing, new Matrix2f());
        this.modelMatrix.m00 = rotate.m00 * scaleX;
        this.modelMatrix.m01 = rotate.m01 * scaleX;
        this.modelMatrix.m10 = rotate.m10 * scaleY;
        this.modelMatrix.m11 = rotate.m11 * scaleY;
    }

    public void setRotateScale(Vector3f rotate, Vector3f scale) {
        float[] mat = TransformUtil.createModelMatrix(new Vector3f(), rotate, scale);
        this.modelMatrix.m00 = mat[0];
        this.modelMatrix.m10 = mat[1];
        this.modelMatrix.m20 = mat[2];
        this.modelMatrix.m01 = mat[4];
        this.modelMatrix.m11 = mat[5];
        this.modelMatrix.m21 = mat[6];
        this.modelMatrix.m02 = mat[8];
        this.modelMatrix.m12 = mat[9];
        this.modelMatrix.m22 = mat[10];
    }

    public void setStateVanilla(Vector2f location, float facing, Vector2f scale) {
        TransformUtil.createModelMatrixVanilla(location, facing, scale, this.modelMatrix);
    }

    public void setStateVanilla(Vector2f location, float facing) {
        TransformUtil.createModelMatrixVanilla(location, facing, this.modelMatrix);
    }

    public void appendToEntity(CombatEntityAPI target, float offsetAngle, Vector3f scale) {
        TransformUtil.createModelMatrix(new Vector3f(target.getLocation().getX(), target.getLocation().getY(), 0.0f), TransformUtil.rotationFacingOnly(target.getFacing() + offsetAngle), scale, this.modelMatrix);
    }

    public void appendToEntity(CombatEntityAPI target, float offsetAngle, Vector2f scale) {
        TransformUtil.createModelMatrixVanilla(target.getLocation(), target.getFacing() + offsetAngle, scale, this.modelMatrix);
    }

    public void appendToEntity(CombatEntityAPI target, float offsetAngle) {
        TransformUtil.createModelMatrixVanilla(target.getLocation(), target.getFacing() + offsetAngle, this.modelMatrix);
    }

    public void appendToEntity(CombatEntityAPI target) {
        this.setLocation(target.getLocation());
    }

    public FloatBuffer pickPrimeMatrixPackage_mat4() {
        this._primeMatBuffer.put(0, CommonUtil.getMatrix4fArray(this.primeMatrix), 0, 16);
        this._primeMatBuffer.position(0);
        this._primeMatBuffer.limit(16);
        return this._primeMatBuffer;
    }

    public FloatBuffer pickModelMatrixPackage_mat4() {
        this._matrixBuffer.put(0, CommonUtil.getMatrix4fArray(this.modelMatrix), 0, 16);
        this._matrixBuffer.position(0);
        this._matrixBuffer.limit(16);
        return this._matrixBuffer;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.getGlobalTimerAlpha());
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public int getBlendColorSRC() {
        return this.blendConfig[0];
    }

    public int getBlendColorDST() {
        return this.blendConfig[1];
    }

    public int getBlendAlphaSRC() {
        return this.blendConfig[2];
    }

    public int getBlendAlphaDST() {
        return this.blendConfig[3];
    }

    public int getBlendEquation() {
        return this.blendConfig[4];
    }

    public byte getBlendState() {
        return this._blendState;
    }

    /**
     * @see org.lwjgl.opengl.GL11#glBlendFunc(int, int)
     */
    public void setBlendFunc(int srcFactor, int dstFactor) {
        this.blendConfig[0] = srcFactor;
        this.blendConfig[1] = dstFactor;
        this.blendConfig[2] = GL11.GL_ZERO;
        this.blendConfig[3] = GL11.GL_ONE;
        this._blendState = 2;
    }

    /**
     * @see org.lwjgl.opengl.GL14#glBlendFuncSeparate(int, int, int, int)
     */
    public void setBlendFuncSeparate(int srcColorFactor, int dstColorFactor, int srcAlphaFactor, int dstAlphaFactor) {
        this.blendConfig[0] = srcColorFactor;
        this.blendConfig[1] = dstColorFactor;
        this.blendConfig[2] = srcAlphaFactor;
        this.blendConfig[3] = dstAlphaFactor;
        this._blendState = 2;
    }

    /**
     * @see org.lwjgl.opengl.GL14#glBlendEquation(int)
     */
    public void setBlendEquation(int mode) {
        this.blendConfig[4] = mode;
        this._blendState = 2;
    }

    public void setAdditiveBlend() {
        this.blendConfig[0] = GL11.GL_SRC_ALPHA;
        this.blendConfig[1] = GL11.GL_ONE;
        this.blendConfig[2] = GL11.GL_ZERO;
        this.blendConfig[3] = GL11.GL_ONE;
        this.blendConfig[4] = GL14.GL_FUNC_ADD;
        this._blendState = 1;
    }

    /**
     * Default blend mode.
     */
    public void setNormalBlend() {
        this.blendConfig[0] = GL11.GL_SRC_ALPHA;
        this.blendConfig[1] = GL11.GL_ONE_MINUS_SRC_ALPHA;
        this.blendConfig[2] = GL11.GL_ZERO;
        this.blendConfig[3] = GL11.GL_ONE;
        this.blendConfig[4] = GL14.GL_FUNC_ADD;
        this._blendState = 0;
    }

    /**
     * As custom blend mode.
     */
    public void setNegativeBlend() {
        this.blendConfig[0] = GL11.GL_SRC_ALPHA;
        this.blendConfig[1] = GL11.GL_ONE;
        this.blendConfig[2] = GL11.GL_ZERO;
        this.blendConfig[3] = GL11.GL_ONE;
        this.blendConfig[4] = GL14.GL_FUNC_REVERSE_SUBTRACT;
        this._blendState = 2;
    }

    public void setDisableBlend() {
        this._blendState = 3;
    }

    public Object getLayer() {
        return this.layer;
    }

    public CombatEngineLayers getCombatLayer() {
        if (this.layer instanceof CombatEngineLayers) return (CombatEngineLayers) this.layer;
        else return null;
    }

    public CampaignEngineLayers getCampaignLayer() {
        if (this.layer instanceof CampaignEngineLayers) return (CampaignEngineLayers) this.layer;
        else return null;
    }

    public void setLayer(Object layer) {
        this.layer = layer;
    }

    public void setDefaultCombatLayer() {
        this.layer = CombatEngineLayers.ABOVE_SHIPS_LAYER;
    }

    public void setDefaultCampaignLayer() {
        this.layer = CampaignEngineLayers.ABOVE_STATIONS;
    }

    public Object entityType() {
        return null;
    }
}
