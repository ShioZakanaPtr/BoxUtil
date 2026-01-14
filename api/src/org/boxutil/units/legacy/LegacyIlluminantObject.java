package org.boxutil.units.legacy;

import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

public class LegacyIlluminantObject {
    private final FloatBuffer uploadBuffer = BufferUtils.createFloatBuffer(4);
    private final Vector3f position = new Vector3f();
    private Vector4f ambientColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private Vector4f diffuseColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private Vector4f specularColor = null;
    private boolean isInfiniteLight = false;
    private Vector3f spotDir = null;
    private float spotCutoff = 90.0f;
    private float spotExp = 0.0f;
    private final Vector3f attenuationFactor = new Vector3f(1.0f, 0.0f, 0.0f);

    protected void putColorData(Vector4f color, float alphaMulti) {
        this.uploadBuffer.position(0);
        this.uploadBuffer.put(0, color.x);
        this.uploadBuffer.put(0, color.y);
        this.uploadBuffer.put(0, color.z);
        this.uploadBuffer.put(0, color.w * alphaMulti);
        this.uploadBuffer.position(0);
        this.uploadBuffer.limit(4);
    }

    /**
     * You should push the attrib to server stack before, use {@link GL11#GL_LIGHTING_BIT}.
     *
     * @param index only <strong>0 to 7</strong>.
     */
    public void glApplyIlluminant(float alphaMulti, byte index) {
        final int lightIndex = GL11.GL_LIGHT0 + index;
        GL11.glEnable(lightIndex);
        this.uploadBuffer.position(0);
        this.position.store(this.uploadBuffer);
        this.uploadBuffer.put(this.isInfiniteLight ? 0.0f : 1.0f);
        this.uploadBuffer.position(0);
        this.uploadBuffer.limit(4);
        GL11.glLight(lightIndex, GL11.GL_POSITION, this.uploadBuffer);
        if (this.attenuationFactor.x != 1.0f) GL11.glLightf(lightIndex, GL11.GL_CONSTANT_ATTENUATION, this.attenuationFactor.x);
        if (this.attenuationFactor.y != 0.0f) GL11.glLightf(lightIndex, GL11.GL_LINEAR_ATTENUATION, this.attenuationFactor.y);
        if (this.attenuationFactor.z != 0.0f) GL11.glLightf(lightIndex, GL11.GL_QUADRATIC_ATTENUATION, this.attenuationFactor.z);
        if (this.spotDir != null) {
            this.uploadBuffer.position(0);
            this.spotDir.store(this.uploadBuffer);
            this.uploadBuffer.position(0);
            this.uploadBuffer.limit(3);
            GL11.glLight(lightIndex, GL11.GL_SPOT_DIRECTION, this.uploadBuffer);
            GL11.glLightf(lightIndex, GL11.GL_SPOT_CUTOFF, this.spotCutoff);
            GL11.glLightf(lightIndex, GL11.GL_SPOT_EXPONENT, this.spotExp);
        }

        if (this.ambientColor != null) {
            this.putColorData(this.ambientColor, alphaMulti);
            GL11.glLight(lightIndex, GL11.GL_AMBIENT, this.uploadBuffer);
        }
        if (this.diffuseColor != null) {
            this.putColorData(this.diffuseColor, alphaMulti);
            GL11.glLight(lightIndex, GL11.GL_DIFFUSE, this.uploadBuffer);
        }
        if (this.specularColor != null) {
            this.putColorData(this.specularColor, alphaMulti);
            GL11.glLight(lightIndex, GL11.GL_SPECULAR, this.uploadBuffer);
        }
    }

    public boolean isInfiniteLight() {
        return this.isInfiniteLight;
    }

    public void setInfiniteLight(boolean infiniteLight) {
        this.isInfiniteLight = infiniteLight;
    }

    public void setAttenuationDisabled() {
        this.attenuationFactor.set(1.0f, 0.0f, 0.0f);
    }

    public void setAttenuationConstantFactor(float factor) {
        this.attenuationFactor.x = factor;
    }

    public void setAttenuationLinearFactor(float factor) {
        this.attenuationFactor.y = factor;
    }

    public void setAttenuationQuadraticFactor(float factor) {
        this.attenuationFactor.z = factor;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f position) {
        if (position == null) this.position.set(0.0f, 0.0f, 0.0f);
        else this.position.set(position);
    }

    public Vector3f getSpotDirection() {
        return this.spotDir;
    }

    /**
     * @param direction set to not-null for use spotlight.
     */
    public void setSpotDirection(Vector3f direction) {
        if (direction == null) this.spotDir = null;
        else this.spotDir.set(direction).normalise();
    }

    public float getSpotCutoff() {
        return this.spotCutoff;
    }

    public void setSpotCutoff(float coneAngle) {
        final float angleAbs = Math.abs(coneAngle);
        this.spotCutoff = angleAbs > 90.0f ? 180.0f : angleAbs;
    }

    public void setSpotExponent(float exponent) {
        final float exponentAbs = Math.abs(exponent);
        this.spotExp = Math.min(exponentAbs, 128.0f);
    }

    public float getSpotExponent() {
        return this.spotExp;
    }

    public Color getAmbientColorC() {
        return this.ambientColor == null ? null : CommonUtil.toCommonColor(this.getAmbientColor());
    }

    public Vector4f getAmbientColor() {
        return this.ambientColor;
    }

    public float getAmbientColorAlpha() {
        return this.ambientColor == null ? 0.0f : this.ambientColor.w;
    }

    public int getAmbientColorAlphaI() {
        return this.ambientColor == null ? 0 : Math.max(Math.min(Math.round(this.ambientColor.w * 255.0f), 255), 0);
    }

    public void setAmbientColor(float red, float green, float blue) {
        if (this.ambientColor == null) this.ambientColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.ambientColor.x = red;
        this.ambientColor.x = green;
        this.ambientColor.x = blue;
    }

    public void setAmbientColor(Vector4f color) {
        if (color != null) this.ambientColor.set(color);
        else this.ambientColor = null;
    }

    public void setAmbientColor(Color color) {
        if (color != null) CommonUtil.colorNormalization4f(color, this.ambientColor);
        else this.ambientColor = null;
    }

    public void setAmbientColorAlpha(float alpha) {
        if (this.ambientColor == null) this.ambientColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.ambientColor.w = alpha;
    }

    public void setAmbientColorAlphaI(int alpha) {
        this.setAmbientColorAlpha(alpha * 0.0039215f);
    }

    public Color getDiffuseColorC() {
        return this.diffuseColor == null ? null : CommonUtil.toCommonColor(this.getDiffuseColor());
    }

    public Vector4f getDiffuseColor() {
        return this.diffuseColor;
    }

    public float getDiffuseColorAlpha() {
        return this.diffuseColor == null ? 0.0f : this.diffuseColor.w;
    }

    public int getDiffuseColorAlphaI() {
        return this.diffuseColor == null ? 0 : Math.max(Math.min(Math.round(this.diffuseColor.w * 255.0f), 255), 0);
    }

    public void setDiffuseColor(float red, float green, float blue) {
        if (this.diffuseColor == null) this.diffuseColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.diffuseColor.x = red;
        this.diffuseColor.x = green;
        this.diffuseColor.x = blue;
    }

    public void setDiffuseColor(Vector4f color) {
        if (color != null) this.diffuseColor.set(color);
        else this.diffuseColor = null;
    }

    public void setDiffuseColor(Color color) {
        if (color != null) CommonUtil.colorNormalization4f(color, this.diffuseColor);
        else this.diffuseColor = null;
    }

    public void setDiffuseColorAlpha(float alpha) {
        if (this.diffuseColor == null) this.diffuseColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.diffuseColor.w = alpha;
    }

    public void setDiffuseColorAlphaI(int alpha) {
        this.setDiffuseColorAlpha(alpha * 0.0039215f);
    }

    public Color getSpecularColorC() {
        return this.specularColor == null ? null : CommonUtil.toCommonColor(this.getSpecularColor());
    }

    public Vector4f getSpecularColor() {
        return this.specularColor;
    }

    public float getSpecularColorAlpha() {
        return this.specularColor == null ? 0.0f : this.specularColor.w;
    }

    public int getSpecularColorAlphaI() {
        return this.specularColor == null ? 0 : Math.max(Math.min(Math.round(this.specularColor.w * 255.0f), 255), 0);
    }

    public void setSpecularColor(float red, float green, float blue) {
        if (this.specularColor == null) this.specularColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.specularColor.x = red;
        this.specularColor.x = green;
        this.specularColor.x = blue;
    }

    public void setSpecularColor(Vector4f color) {
        if (color != null) this.specularColor.set(color);
        else this.specularColor = null;
    }

    public void setSpecularColor(Color color) {
        if (color != null) CommonUtil.colorNormalization4f(color, this.specularColor);
        else this.specularColor = null;
    }

    public void setSpecularColorAlpha(float alpha) {
        if (this.specularColor == null) this.specularColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.specularColor.w = alpha;
    }

    public void setSpecularColorAlphaI(int alpha) {
        this.setSpecularColorAlpha(alpha * 0.0039215f);
    }
}
