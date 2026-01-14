package org.boxutil.units.standard.light;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.base.BaseIlluminantData;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

/**
 * Highest performance cost in all illuminates.<p>
 * Default rectangle normal direction is <code>vec3(0.0, 0.0, -1.0)</code><p>
 * For textured area lighting: <strong>OpenGL 4.3 required, compute shader required, texture storage supported required.</strong><p>
 * For instanced data: scaleX and scaleY to controls rectangle size (larger value will be the attenuation radius scale).
 */
public class AreaLight extends BaseIlluminantData {
    protected SpriteAPI lightTex = null;
    protected float[] size = new float[]{32.0f, 32.0f};
    protected int[] texturedLightingState = new int[]{0, 0}; // oriTex, filteringTex
    protected float[] texturedLightingMapping = new float[]{0.0f, 0.0f, 1.0f, 1.0f}; // mappingXOffset, mappingYOffset, mappingWidth, mappingHeight
    protected byte textureMaxLevel = 1;
    protected boolean[] stateB = new boolean[]{false, false, false}; // texturedLighting, directSet, twoSided

    protected int _StatePackageStack() {
        return 3;
    }

    public void delete() {
        super.delete();
        if (this.texturedLightingState[1] > 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL11.glDeleteTextures(this.texturedLightingState[1]);
            this.texturedLightingState[1] = 0;
        }
    }

    public void reset() {
        super.reset();
        this.size[0] = 32.0f;
        this.size[1] = 32.0f;
        this.texturedLightingMapping[0] = 0.0f;
        this.texturedLightingMapping[1] = 0.0f;
        this.texturedLightingMapping[2] = 1.0f;
        this.texturedLightingMapping[3] = 1.0f;
        this.stateB[2] = false;
    }

    public @Nullable SpriteAPI getLightTexture() {
        return this.lightTex;
    }

    public int getLightTextureID() {
        return texturedLightingState[0];
    }

    /**
     * RGBA8 texture only.<p>
     * <strong>OpenGL 4.3 required, compute shader required.</strong>
     */
    public void setLightTexture(@Nullable SpriteAPI texture) {
        if (texture == null) {
            this.lightTex = null;
            this.texturedLightingState[0] = 0;
        } else {
            this.lightTex = texture;
            this.texturedLightingState[0] = texture.getTextureId();
        }
    }

    /**
     * RGBA8 texture only.<p>
     * <strong>OpenGL 4.3 required, compute shader required.</strong>
     */
    public void setLightTexture(int texture) {
        this.texturedLightingState[0] = Math.max(texture, 0);
    }

    /**
     * Must call it after changed light texture if with different texture size.
     */
    public void resetLightTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(this.texturedLightingState[1]);
        this.stateB[0] = false;
        this.stateB[1] = false;
        this.lightTex = null;
        this.texturedLightingState[0] = 0;
        this.texturedLightingState[1] = 0;
        this.textureMaxLevel = 1;
    }

    public float getLightTextureXOffset() {
        return this.texturedLightingMapping[2];
    }

    public float getLightTextureYOffset() {
        return this.texturedLightingMapping[3];
    }

    public float getLightTextureWidth() {
        return this.texturedLightingMapping[4];
    }

    public float getLightTextureHeight() {
        return this.texturedLightingMapping[5];
    }

    public void setLightTextureRange(float uvXOffset, float uvYOffset, float uvWidth, float uvHeight) {
        this.texturedLightingMapping[2] = uvXOffset;
        this.texturedLightingMapping[3] = uvYOffset;
        this.texturedLightingMapping[4] = uvWidth;
        this.texturedLightingMapping[5] = uvHeight;
    }

    public byte getPreFilteringTextureMaxLevel() {
        return this.textureMaxLevel;
    }

    public boolean isDirectAllocatePreFilteringTexture() {
        return this.stateB[1];
    }

    public void directAllocatePreFilteringTexture(int texture, byte maxLevel) {
        this.stateB[1] = true;
        this.texturedLightingState[1] = texture;
        this.textureMaxLevel = maxLevel;
    }

    /**
     * Call it if you want to use textured area light.<p>
     * High performance cost in once pre-filtering, use it carefully.<p>
     * The pre-filtering texture will cost VRAM about <strong>133.333~%</strong> of origin texture (in the case of performance priority).
     * RGBA8 texture only.<p>
     * <strong>Normally: OpenGL 4.3 required, compute shader required.</strong>
     *
     * @param refreshCurrFilteringTex recommend set to false if the size of new texture is equal to old texture.
     *
     * @return returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when context or shader packs not supported.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when use a direct allocate pre-filtering texture.
     */
    public byte submitLightTexture(boolean refreshCurrFilteringTex) {
        if (this.isDirectAllocatePreFilteringTexture()) return BoxEnum.STATE_FAILED_OTHER;
        final boolean haveFilteringTex = this.texturedLightingState[1] > 0;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        if (this.texturedLightingState[0] == 0 && haveFilteringTex) {
            GL11.glDeleteTextures(this.texturedLightingState[1]);
            this.texturedLightingState[1] = 0;
            return BoxEnum.STATE_SUCCESS;
        }
        if (haveFilteringTex && refreshCurrFilteringTex) {
            GL11.glDeleteTextures(this.texturedLightingState[1]);
            this.texturedLightingState[1] = GL11.glGenTextures();
        } else if (!haveFilteringTex) this.texturedLightingState[1] = GL11.glGenTextures();
        BaseShaderPacksContext context = BoxConfigs.getCurrShaderPacksContext();
        if (!context.isTexturedAreaLightSupported()) return BoxEnum.STATE_FAILED;
        byte[] state = context.applyTexturedAreaLightPreFiltering(this.texturedLightingState[0], this.texturedLightingState[1], !haveFilteringTex || refreshCurrFilteringTex);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        this.textureMaxLevel = state[1];
        if (state[0] != BoxEnum.STATE_SUCCESS) {
            GL11.glDeleteTextures(this.texturedLightingState[1]);
            this.texturedLightingState[1] = 0;
            this.textureMaxLevel = 0;
        }
        return state[0];
    }

    /**
     * The pre-filtering texture after submit.
     */
    public int getPreFilteringLightTextureID() {
        return this.texturedLightingState[1];
    }

    public boolean havePreFilteringLightTexture() {
        return this.texturedLightingState[1] > 0;
    }

    public boolean isTexturedLighting() {
        return this.stateB[0];
    }

    public void setTexturedLighting(boolean use) {
        this.stateB[0] = use;
    }

    public boolean isTwoSided() {
        return this.stateB[2];
    }

    public void setTwoSided(boolean twoSided) {
        this.stateB[2] = twoSided;
    }

    public float getWidth() {
        return this.size[0] + this.size[0];
    }

    public float getHeight() {
        return this.size[1] + this.size[1];
    }

    public void setWidth(float width) {
        this.size[0] = width * 0.5f;
    }

    public void setHeight(float height) {
        this.size[1] = height * 0.5f;
    }

    public void setWidthDirect(float widthHalf) {
        this.size[0] = widthHalf;
    }

    public void setHeightDirect(float heightHalf) {
        this.size[1] = heightHalf;
    }

    public void setSize(float width, float height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public void setSizeDirect(float widthHalf, float heightHalf) {
        this.setWidthDirect(widthHalf);
        this.setHeightDirect(heightHalf);
    }

    public FloatBuffer pickDataPackage_vec4() {
        FloatBuffer buffer = super.pickDataPackage_vec4();
        buffer.put(6, this.size[0]);
        buffer.put(7, this.size[1]);
        buffer.put(8, this.texturedLightingMapping, 0, 4);
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public Object entityType() {
        return DirectEntityType.AREA_LIGHT;
    }
}
