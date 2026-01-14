package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.graphics.SpriteAPI;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.BaseShaderData;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.ShaderCore;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Easy way for rendering texture in arc with thinness anywhere.<p>
 * Required <code>OpenGL 2.0</code> supported.
 */
public class TexArcObject {
    // ringHardness, innerHardness, innerFactor, arc
    protected final float[] state = new float[4];
    protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    protected SpriteAPI sprite = BoxDatabase.BUtil_ONE;
    protected int glTex = 0;

    /**
     * General rendering method.<p>
     * Anchor at bottom-left.
     */
    public void render(Vector2f location, float facing, float radiusWidth, float radiusHeight, boolean isAdditiveBlend) {
        GL11.glPushMatrix();
        GL11.glTranslatef(location.x + radiusWidth, location.y + radiusHeight, 0.0f);
        if (facing != 0.0f) GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw(radiusWidth, radiusHeight);
        GL11.glPopMatrix();
    }

    /**
     * General rendering method.
     */
    public void renderAtCenter(Vector2f location, float facing, float radiusWidth, float radiusHeight, boolean isAdditiveBlend) {
        GL11.glPushMatrix();
        GL11.glTranslatef(location.x, location.y, 0.0f);
        GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw(radiusWidth, radiusHeight);
        GL11.glPopMatrix();
    }

    public void glDraw(float radiusWidth, float radiusHeight) {
        if (Math.abs(this.state[3]) == 0.0f) return;
        BaseShaderData program = ShaderCore.getTexArcProgram();
        if (program == null || !program.isValid()) return;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(8);
        buffer.put(this.state);
        this.color.store(buffer);
        buffer.flip();
        program.active();
        GL20.glUniform4(program.location[0], buffer);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glTex);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f(-radiusWidth, -radiusHeight);
        GL11.glVertex2f(radiusWidth, -radiusHeight);
        GL11.glVertex2f(-radiusHeight, radiusHeight);
        GL11.glVertex2f(radiusWidth, radiusHeight);
        GL11.glEnd();
        program.close();
    }

    public Vector4f getColor() {
        return this.color;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public void setColor(Vector3f color) {
        this.setColor(color.x, color.y, color.z);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

    public void setColor(Vector4f color) {
        this.setColor(color.x, color.y, color.z, color.w);
    }

    public void setColor(Color color) {
        this.setColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public void setColorWithoutAlpha(Color color) {
        this.setColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
    }

    public float getAlpha() {
        return this.color.w;
    }

    public void setAlpha(float alpha) {
        this.color.w = alpha;
    }

    public SpriteAPI getSprite() {
        return this.sprite;
    }

    public int getSpriteID() {
        return this.glTex;
    }

    public void setSprite(@NotNull SpriteAPI sprite) {
        this.sprite = sprite;
        this.glTex = this.sprite.getTextureId();
    }

    public void setSprite(int sprite) {
        this.glTex = sprite;
    }

    public float getInnerRatio() {
        return this.state[2];
    }

    public void setInnerRatio(float ratio) {
        this.state[2] = ratio;
    }

    public float getRingHardness() {
        return this.state[1];
    }

    public void setRingHardness(float hardness) {
        this.state[1] = hardness;
    }

    public float getInnerHardness() {
        return this.state[0];
    }

    public void setInnerHardness(float hardness) {
        this.state[0] = hardness;
    }

    /**
     * @return single tile when greater than zero
     */
    public float getArcRatio() {
        return this.state[3];
    }

    /**
     * @param ratio the value 0.0~1.0 => degree 0.0~360.0
     */
    public void setArcRatio(float ratio, boolean singleTile) {
        this.state[3] = singleTile ? ratio : -ratio;
    }

    public boolean isSingleTile() {
        return this.state[3] > 0.0f;
    }
}
