package org.boxutil.units.standard.misc;

import org.boxutil.base.BaseShaderData;
import org.boxutil.manager.ShaderCore;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Easy way for rendering arc with thinness anywhere.<p>
 * Required <strong>OpenGL 2.0</strong> supported.
 */
public class ArcObject {
    // vec2(inner), ringHardness, innerHardness
    protected final float[] state = new float[4];
    protected float arcValue = 0.0f;
    protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

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
        if (this.arcValue >= 1.0f) return;
        BaseShaderData program = ShaderCore.getArcProgram();
        if (program == null || !program.isValid()) return;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(8);
        buffer.put(this.state);
        this.color.store(buffer);
        buffer.flip();
        program.active();
        GL20.glUniform4(program.location[0], buffer);
        GL20.glUniform1f(program.location[1], this.arcValue);
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

    public float[] getInnerRatioArray() {
        return new float[]{this.state[0], state[1]};
    }

    public Vector2f getInnerRatio() {
        return new Vector2f(this.state[0], state[1]);
    }

    public void setInner(float widthRatio, float heightRatio) {
        this.state[0] = widthRatio;
        this.state[1] = heightRatio;
    }

    public void setInner(Vector2f ratio) {
        this.setInner(ratio.x, ratio.y);
    }

    public float getRingHardness() {
        return this.state[2];
    }

    public void setRingHardness(float hardness) {
        this.state[2] = hardness;
    }

    public float getInnerHardness() {
        return this.state[3];
    }

    public void setInnerHardness(float hardness) {
        this.state[3] = hardness;
    }

    public float getArc() {
        return (float) Math.toDegrees(Math.acos(this.arcValue));
    }

    public void setArc(float angle) {
        this.arcValue = (float) Math.cos(Math.toRadians(angle / 2.0f));
    }

    public float getArcDirect() {
        return this.arcValue;
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setArcDirect(float angleCosValue) {
        this.arcValue = angleCosValue;
    }
}
