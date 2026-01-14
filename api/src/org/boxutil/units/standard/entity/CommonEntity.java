package org.boxutil.units.standard.entity;

import com.fs.starfarer.api.Global;
import org.boxutil.define.LayeredEntityType;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.BaseMIRenderData;
import org.boxutil.define.BoxGeometry;
import org.boxutil.manager.ModelManager;
import org.boxutil.units.standard.attribute.ModelData;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * For any 3d model.
 */
public class CommonEntity extends BaseMIRenderData {
    protected ModelData _entity;
    protected boolean commonDraw = true;
    // vec4(lightColor), vec4(shadowColor), vec3(lightDirection)
    protected final float[] lightState = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f, 0.0f, -1.0f};
    protected final float[] baseSize = new float[]{1.0f, 1.0f, 1.0f};

    protected int _StatePackageStack() {
        return 6;
    }

    public CommonEntity() {
        this._entity = ModelManager.getModelData(BoxGeometry.HEXAHEDRON_FULL);
    }

    public CommonEntity(@NotNull String id, boolean syncTextures) {
        this(ModelManager.getModelData(id), syncTextures);
    }

    public CommonEntity(@NotNull ModelData entity, boolean syncTextures) {
        this._entity = entity;
        if (syncTextures) this.getMaterialData().syncTextures(entity);
    }

    public ModelData getModel() {
        return this._entity;
    }

    public void setModel(String modelID, boolean syncTextures) {
        this.setModel(ModelManager.getModelData(modelID), syncTextures);
    }

    public void setModel(ModelData model, boolean syncTextures) {
        this._entity = model;
        if (syncTextures) this.getMaterialData().syncTextures(model);
    }

    public void glDraw() {
        if (this._entity == null || !this._entity.isValid()) return;
        GL30.glBindVertexArray(this._entity.getVAO());
        GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, this._entity.getPatchCount(), Math.max(Math.min(this.getValidInstanceDataCount(), this.getRenderingCount()), 1));
    }

    protected void _resetExc() {
        super._resetExc();
        this.commonDraw = true;
        this.lightState[0] = 1.0f;
        this.lightState[1] = 1.0f;
        this.lightState[2] = 1.0f;
        this.lightState[3] = 1.0f;
        this.lightState[4] = 0.0f;
        this.lightState[5] = 0.0f;
        this.lightState[6] = 0.0f;
        this.lightState[7] = 0.9f;
        this.lightState[8] = 0.0f;
        this.lightState[9] = 0.0f;
        this.lightState[10] = -1.0f;
        this.baseSize[0] = 1.0f;
        this.baseSize[1] = 1.0f;
        this.baseSize[2] = 1.0f;
    }

    public float[] getBaseSizePerTiles() {
        return this.baseSize;
    }

    public float getBaseSizeX() {
        return this.baseSize[0];
    }

    public float getBaseSizeY() {
        return this.baseSize[1];
    }

    public float getBaseSizeZ() {
        return this.baseSize[2];
    }

    public void setBaseSizeX(float value) {
        this.baseSize[0] = value;
    }

    public void setBaseSizeY(float value) {
        this.baseSize[1] = value;
    }

    public void setBaseSize2D(float width, float height) {
        this.baseSize[0] = width;
        this.baseSize[1] = height;
    }

    public void setBaseSizeZ(float value) {
        this.baseSize[2] = value;
    }

    public void setBaseSize3D(float x, float y, float z) {
        this.setBaseSize2D(x, y);
        this.baseSize[2] = z;
    }

    public float[] getLightState() {
        return this.lightState;
    }

    public float[] getLightColorArray() {
        return new float[]{this.lightState[0], this.lightState[1], this.lightState[2], this.lightState[3]};
    }

    public Vector4f getLightColor() {
        return new Vector4f(this.lightState[0], this.lightState[1], this.lightState[2], this.lightState[3]);
    }

    public void setLightColor(@NotNull Vector4f lightColor) {
        this.lightState[0] = lightColor.x;
        this.lightState[1] = lightColor.y;
        this.lightState[2] = lightColor.z;
        this.lightState[3] = lightColor.w;
    }

    public void setLightColor(float r, float g, float b, float power) {
        this.lightState[0] = r;
        this.lightState[1] = g;
        this.lightState[2] = b;
        this.lightState[3] = power;
    }

    public void setLightColor(Color lightColor) {
        this.lightState[0] = lightColor.getRed() / 255.0f;
        this.lightState[1] = lightColor.getGreen() / 255.0f;
        this.lightState[2] = lightColor.getBlue() / 255.0f;
        this.lightState[3] = lightColor.getAlpha() / 255.0f;
    }

    public void setDefaultLight() {
        this.lightState[0] = 1.0f;
        this.lightState[1] = 1.0f;
        this.lightState[2] = 1.0f;
        this.lightState[3] = 1.0f;
    }

    public void syncStarSystemLight() {
        if (Global.getSector() != null && Global.getSector().getPlayerFleet() != null && Global.getSector().getPlayerFleet().getLightSource() != null) {
            Vector4f systemColor = CommonUtil.colorNormalization4f(Global.getSector().getPlayerFleet().getLightSource().getLightColor(), null);
            this.setLightColor(systemColor);
            systemColor.scale(0.1f);
            this.setShadowColor(systemColor.x, systemColor.y, systemColor.z, this.lightState[7]);
        }
    }

    public Vector3f getLightDirection() {
        return new Vector3f(this.lightState[8], this.lightState[9], this.lightState[10]);
    }

    /**
     * Global infinite light for this _entity.
     * @param lightDirection topLight = vec3(0.0f, 0.0f, -1.0f); set to vec3(0.0) may be looks strange or crash.
     */
    public void setLightDirection(@NotNull Vector3f lightDirection) {
        if (lightDirection == null) {
            this.lightState[8] = 0.0f;
            this.lightState[9] = 0.0f;
            this.lightState[10] = -1.0f;
        } else {
            this.lightState[8] = lightDirection.x;
            this.lightState[9] = lightDirection.y;
            this.lightState[10] = lightDirection.z;
        }
    }

    public void setDefaultLightDirection() {
        this.lightState[8] = 0.0f;
        this.lightState[9] = 0.0f;
        this.lightState[10] = -1.0f;
    }

    public float[] getShadowColorArray() {
        return new float[]{this.lightState[4], this.lightState[5], this.lightState[6], this.lightState[7]};
    }

    public Vector4f getShadowColor() {
        return new Vector4f(this.lightState[4], this.lightState[5], this.lightState[6], this.lightState[7]);
    }

    public void setShadowColor(@NotNull Vector4f shadowColor) {
        this.lightState[4] = shadowColor.x;
        this.lightState[5] = shadowColor.y;
        this.lightState[6] = shadowColor.z;
        this.lightState[7] = shadowColor.w;
    }

    public void setShadowColor(float r, float g, float b, float power) {
        this.lightState[4] = r;
        this.lightState[5] = g;
        this.lightState[6] = b;
        this.lightState[7] = power;
    }

    public void setShadowColor(Color shadowColor) {
        this.lightState[4] = shadowColor.getRed() / 255.0f;
        this.lightState[5] = shadowColor.getGreen() / 255.0f;
        this.lightState[6] = shadowColor.getBlue() / 255.0f;
        this.lightState[7] = shadowColor.getAlpha() / 255.0f;
    }

    public void setDefaultShadow() {
        this.lightState[4] = 0.0f;
        this.lightState[5] = 0.0f;
        this.lightState[6] = 0.0f;
        this.lightState[7] = 0.9f;
    }

    public boolean isCommonDraw() {
        return this.commonDraw;
    }

    /**
     * Set to true with first Lambert shading, set to false for standard 3D shading.
     */
    public void setCommonDraw(boolean common) {
        this.commonDraw = common;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.material.getState(), 0, 12);
        this._statePackageBuffer.put(12, this.lightState, 0, 11);
        this._statePackageBuffer.put(23, this.haveValidInstanceData() ? this.getInstanceTimerOverride() : this.getGlobalTimerAlpha());
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.COMMON;
    }
}
