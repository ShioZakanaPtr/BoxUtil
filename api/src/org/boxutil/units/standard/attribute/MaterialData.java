package org.boxutil.units.standard.attribute;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.GLWrapper;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.define.BoxDatabase;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.IntBuffer;

// The material is only 2D-Texture supported.
public class MaterialData {
    protected byte cullFace = BoxEnum.MATERIAL_CULL_BACK;
    // diffuse, normal, complex, emissive, tangent
    protected final SpriteAPI[] textures = new SpriteAPI[5];
    protected final int[] glTex = new int[5];
    // vec4(color), vec4(emissive), vec4(alphaMix, colorMix, glowPower, anisotropic)
    protected final float[] state = new float[12];
    protected final boolean[] stateB = new boolean[2]; // additionEmissive, ignoreIllumination
    protected final IntBuffer bindTextureBuf = GLWrapper.Texture.valid_MultiBind() ? BufferUtils.createIntBuffer(5).clear() : null;

    public MaterialData() {
        this.reset();
        this.clearTextures();
    }

    public MaterialData(MaterialData material) {
        this.cullFace = material.cullFace;
        System.arraycopy(material.textures, 0, this.textures, 0, this.textures.length);
        System.arraycopy(material.glTex, 0, this.glTex, 0, this.glTex.length);
        System.arraycopy(material.state, 0, this.state, 0, this.state.length);
        System.arraycopy(material.stateB, 0, this.stateB, 0, this.stateB.length);
        if (this.bindTextureBuf != null && material.bindTextureBuf != null) {
            this.bindTextureBuf.put(material.bindTextureBuf.clear()).clear();
            material.bindTextureBuf.clear();
        }
    }

    public void clearTextures() {
        this.textures[0] = BoxDatabase.BUtil_ONE;
        this.glTex[0] = this.textures[0].getTextureId();
        this.textures[1] = BoxDatabase.BUtil_Z;
        this.glTex[1] = this.textures[1].getTextureId();
        this.textures[2] = BoxDatabase.BUtil_COMPLEX_DEF;
        this.glTex[2] = this.textures[2].getTextureId();
        this.textures[3] = BoxDatabase.BUtil_NONE;
        this.glTex[3] = this.textures[3].getTextureId();
        this.textures[4] = BoxDatabase.BUtil_X;
        this.glTex[4] = this.textures[4].getTextureId();
        if (GLWrapper.Texture.valid_MultiBind()) {
            this.bindTextureBuf.put(0, this.glTex[0]);
            this.bindTextureBuf.put(1, this.glTex[1]);
            this.bindTextureBuf.put(2, this.glTex[2]);
            this.bindTextureBuf.put(3, this.glTex[3]);
            this.bindTextureBuf.put(4, this.glTex[4]);
        }
    }

    public void syncTextures(ModelData entity) {
        this.glTex[0] = entity.getDiffuseID();
        this.glTex[1] = entity.getNormalID();
        this.glTex[2] = entity.getComplexID();
        this.glTex[3] = entity.getEmissiveID();
        this.glTex[4] = entity.getTangentID();
        if (GLWrapper.Texture.valid_MultiBind()) {
            this.bindTextureBuf.put(0, this.glTex[0]);
            this.bindTextureBuf.put(1, this.glTex[1]);
            this.bindTextureBuf.put(2, this.glTex[2]);
            this.bindTextureBuf.put(3, this.glTex[3]);
            this.bindTextureBuf.put(4, this.glTex[4]);
        }
    }

    /**
     * Without textures.
     */
    public void reset() {
        this.cullFace = BoxEnum.MATERIAL_CULL_BACK;
        this.state[0] = BoxEnum.ONE;
        this.state[1] = BoxEnum.ONE;
        this.state[2] = BoxEnum.ONE;
        this.state[3] = BoxEnum.ONE;
        this.state[4] = BoxEnum.ONE;
        this.state[5] = BoxEnum.ONE;
        this.state[6] = BoxEnum.ONE;
        this.state[7] = BoxEnum.ONE;
        this.state[8] = BoxEnum.ONE;
        this.state[9] = 0.0f;
        this.state[10] = BoxEnum.ONE;
        this.state[11] = 0.0f;
        this.stateB[0] = true;
        this.stateB[1] = false;
    }

    public void putShaderTexture() {
        if (GLWrapper.Texture.valid_MultiBind()) GLWrapper.Texture.glBindTextures(0, 5, this.bindTextureBuf);
        else if (GLWrapper.Drawcall.MultiTex.valid()) {
            GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.glTex[0]);
            GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE1);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.glTex[1]);
            GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE2);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.glTex[2]);
            GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE3);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.glTex[3]);
            GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE4);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.glTex[4]);
        }
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI[] getTextures() {
        return this.textures;
    }

    public int[] getTexturesID() {
        return this.glTex;
    }

    protected void writeBindTexture(int target, SpriteAPI sprite, SpriteAPI defaultSprite) {
        if (sprite == null) {
            this.textures[target] = defaultSprite;
            this.glTex[target] = this.textures[target].getTextureId();
        } else {
            this.textures[0] = sprite;
            this.glTex[target] = sprite.getTextureId();
        }
    }

    protected void writeBindTextureDirect(int target, int texture) {
        this.glTex[target] = Math.max(texture, 0);
    }

    protected void writeBindTextureBuf(int target) {
        if (GLWrapper.Texture.valid_MultiBind()) this.bindTextureBuf.put(target, this.glTex[target]);
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI getDiffuse() {
        return textures[0];
    }

    public int getDiffuseID() {
        return glTex[0];
    }

    /**
     * Set null for default.
     */
    public void setDiffuse(@Nullable SpriteAPI diffuse) {
        this.writeBindTexture(0, diffuse, BoxDatabase.BUtil_ONE);
        this.writeBindTextureBuf(0);
    }

    public void setDiffuse(int diffuse) {
        this.writeBindTextureDirect(0, diffuse);
        this.writeBindTextureBuf(0);
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI getNormal() {
        return textures[1];
    }

    public int getNormalID() {
        return glTex[1];
    }

    /**
     * Set null for default.
     */
    public void setNormal(@Nullable SpriteAPI normal) {
        this.writeBindTexture(1, normal, BoxDatabase.BUtil_Z);
        this.writeBindTextureBuf(1);
    }

    public void setNormal(int normal) {
        this.writeBindTextureDirect(1, normal);
        this.writeBindTextureBuf(1);
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI getComplex() {
        return this.textures[2];
    }

    public int getComplexID() {
        return this.glTex[2];
    }

    /**
     * Texture layout:
     * <pre>
     * {@code
     * channel   |    attribute
     * ------------------------------
     * x/red    ==>   emissive mask
     * y/green  ==>   roughness
     * z/blue   ==>   metalness
     * w/alpha  ==>   *ignored
     * }
     * </pre>
     */
    public void setComplex(@Nullable SpriteAPI complex) {
        this.writeBindTexture(2, complex, BoxDatabase.BUtil_COMPLEX_DEF);
        this.writeBindTextureBuf(2);
    }

    /**
     * Texture layout:
     * <pre>
     * {@code
     * channel   |    attribute
     * ------------------------------
     * x/red    ==>   emissive mask
     * y/green  ==>   roughness
     * z/blue   ==>   metalness
     * w/alpha  ==>   *ignored
     * }
     * </pre>
     */
    public void setComplex(int complex) {
        this.writeBindTextureDirect(2, complex);
        this.writeBindTextureBuf(2);
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI getEmissive() {
        return textures[3];
    }

    public int getEmissiveID() {
        return glTex[3];
    }

    /**
     * Set null for default.
     */
    public void setEmissive(@Nullable SpriteAPI emissive) {
        this.writeBindTexture(3, emissive, BoxDatabase.BUtil_NONE);
        this.writeBindTextureBuf(3);
    }

    public void setEmissive(int emissive) {
        this.writeBindTextureDirect(3, emissive);
        this.writeBindTextureBuf(3);
    }

    /**
     * May incorrect returns if set by texture id.
     */
    public SpriteAPI getTangent() {
        return textures[4];
    }

    public int getTangentID() {
        return glTex[4];
    }

    /**
     * Set null for default.
     */
    public void setTangent(@Nullable SpriteAPI tangent) {
        this.writeBindTexture(4, tangent, BoxDatabase.BUtil_X);
        this.writeBindTextureBuf(4);
    }

    public void setTangent(int tangent) {
        this.writeBindTextureDirect(4, tangent);
        this.writeBindTextureBuf(4);
    }

    public byte getCullFace() {
        return this.cullFace;
    }

    public void setCullBack() {
        this.cullFace = BoxEnum.MATERIAL_CULL_BACK;
    }

    public void setCullFront() {
        this.cullFace = BoxEnum.MATERIAL_CULL_FRONT;
    }

    public void setCullFrontAndBack() {
        this.cullFace = BoxEnum.MATERIAL_CULL_FRONT_BACK;
    }

    public void setDisableCullFace() {
        this.cullFace = BoxEnum.MATERIAL_CULL_DISABLED;
    }

    public float[] getColorArray() {
        return new float[]{this.state[0], this.state[1], this.state[2], this.state[3]};
    }

    public Color getColorC() {
        return CommonUtil.toCommonColor(this.getColor());
    }

    public Vector4f getColor() {
        return new Vector4f(this.state[0], this.state[1], this.state[2], this.state[3]);
    }

    public float getColorAlpha() {
        return this.state[3];
    }

    public int getColorAlphaI() {
        return Math.max(Math.min(Math.round(this.state[3] * 255.0f), 255), 0);
    }

    public void setColor(@NotNull Vector4f color) {
        this.state[0] = color.x;
        this.state[1] = color.y;
        this.state[2] = color.z;
        this.state[3] = color.w;
    }

    public void setColor(float r, float g, float b, float a) {
        this.state[0] = r;
        this.state[1] = g;
        this.state[2] = b;
        this.state[3] = a;
    }

    public void setColor(Color color) {
        this.state[0] = color.getRed() / 255.0f;
        this.state[1] = color.getGreen() / 255.0f;
        this.state[2] = color.getBlue() / 255.0f;
        this.state[3] = color.getAlpha() / 255.0f;
    }

    public void setColorAlpha(float alpha) {
        this.state[3] = alpha;
    }

    public void setColorAlphaI(int alpha) {
        this.state[3] = alpha / 255.0f;
    }

    public float[] getEmissiveColorArray() {
        return new float[]{this.state[4], this.state[5], this.state[6], this.state[7]};
    }

    public Color getEmissiveColorC() {
        return CommonUtil.toCommonColor(this.getEmissiveColor());
    }

    public Vector4f getEmissiveColor() {
        return new Vector4f(this.state[4], this.state[5], this.state[6], this.state[7]);
    }

    public float getEmissiveColorAlpha() {
        return this.state[7];
    }

    public int getEmissiveColorAlphaI() {
        return Math.max(Math.min(Math.round(this.state[7] * 255.0f), 255), 0);
    }

    public void setEmissiveColor(@NotNull Vector4f color) {
        this.state[4] = color.x;
        this.state[5] = color.y;
        this.state[6] = color.z;
        this.state[7] = color.w;
    }

    public void setEmissiveColor(float r, float g, float b, float a) {
        this.state[4] = r;
        this.state[5] = g;
        this.state[6] = b;
        this.state[7] = a;
    }

    public void setEmissiveColor(Color color) {
        this.state[4] = color.getRed() / 255.0f;
        this.state[5] = color.getGreen() / 255.0f;
        this.state[6] = color.getBlue() / 255.0f;
        this.state[7] = color.getAlpha() / 255.0f;
    }

    public void setEmissiveColorAlpha(float alpha) {
        this.state[7] = alpha;
    }

    public void setEmissiveColorAlphaI(int alpha) {
        this.state[7] = alpha / 255.0f;
    }

    public float[] getEmissiveStateArray() {
        return new float[]{this.state[8], this.state[9], this.state[10]};
    }

    public Vector3f getEmissiveState() {
        return new Vector3f(this.state[8], this.state[9], this.state[10]);
    }

    public void setEmissiveState(float alphaToEmissive, float colorToEmissive, float glowPower) {
        this.state[8] = alphaToEmissive;
        this.state[9] = colorToEmissive;
        this.state[10] = glowPower;
    }

    public float getAlphaToEmissive() {
        return this.state[8];
    }

    /**
     * @param alphaToEmissive mix level, value: 0.0 to 1.0
     */
    public void setAlphaToEmissive(float alphaToEmissive) {
        this.state[8] = alphaToEmissive;
    }

    public void setAlphaToEmissiveDefault() {
        this.state[8] = BoxEnum.ONE;
    }

    /**
     * Use alpha mix when false.
     * Vanilla rendering: usually is addition.
     */
    public boolean isAdditionEmissive() {
        return this.stateB[0];
    }

    public void setAdditionEmissive(boolean addition) {
        this.stateB[0] = addition;
    }

    public boolean isIgnoreIllumination() {
        return this.stateB[1];
    }

    /**
     * Will only draws diffuse map and emissive if true.
     */
    public void setIgnoreIllumination(boolean ignore) {
        this.stateB[1] = ignore;
    }

    public float isColorToEmissive() {
        return this.state[9];
    }

    /**
     * @param colorToEmissive mix level, value: 0.0 to 1.0
     */
    public void setColorToEmissive(float colorToEmissive) {
        this.state[9] = colorToEmissive;
    }

    public void setColorToEmissiveDefault() {
        this.state[9] = 0.0f;
    }

    public float getGlowPower() {
        return this.state[10];
    }

    /**
     * @param glowPower Decided by final of emissive level; 0 to 1.0f
     */
    public void setGlowPower(float glowPower) {
        this.state[10] = glowPower;
    }

    public void setGlowPowerDefault() {
        this.state[10] = BoxEnum.ONE;
    }

    public float getAnisotropic() {
        return this.state[11];
    }

    public void setAnisotropic(float anisotropic) {
        this.state[11] = anisotropic;
    }

    public float[] getState() {
        return this.state;
    }
}
