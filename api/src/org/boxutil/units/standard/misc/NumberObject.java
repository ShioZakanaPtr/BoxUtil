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
 * Easy way for display number value anywhere.<p>
 * Required <code>OpenGL 2.0</code> supported.
 */
public class NumberObject {
    // vec2(length)
    protected final float[] state = new float[2];
    protected byte invert = 0;
    protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    /**
     * General rendering method.<p>
     * Anchor at bottom-left.
     *
     * @param value the number what you want to see.
     */
    public void render(float value, Vector2f location, float facing, float width, float height, boolean isAdditiveBlend) {
        GL11.glPushMatrix();
        GL11.glTranslatef(location.x, location.y, 0.0f);
        GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw(value, width, height);
        GL11.glPopMatrix();
    }

    /**
     * General rendering method.
     *
     * @param value the number what you want to see.
     */
    public void renderAtCenter(float value, Vector2f location, float facing, float width, float height, boolean isAdditiveBlend) {
        GL11.glPushMatrix();
        GL11.glTranslatef(location.x - width * 0.5f, location.y - width * 0.5f, 0.0f);
        if (facing != 0.0f) GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw(value, width, height);
        GL11.glPopMatrix();
    }

    /**
     * @param value the number what you want to see.
     */
    public void glDraw(float value, float width, float height) {
        BaseShaderData program = ShaderCore.getNumberProgram();
        if (program == null || !program.isValid()) return;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(8);
        buffer.put(this.state[0] - 1);
        buffer.put(this.state[1]);
        buffer.put(value);
        buffer.put(this.invert);
        this.color.store(buffer);
        buffer.flip();
        program.active();
        GL20.glUniform4(program.location[0], buffer);
        float charLength = this.state[0] + this.state[1] + 1.0f;
        if (this.state[0] != 0.0f && this.state[1] != 0.0f) charLength++;
        GL20.glUniform1f(program.location[1], charLength);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glVertex2f(width, 0.0f);
        GL11.glVertex2f(0.0f, height);
        GL11.glVertex2f(width, height);
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

    public int getIntegerLength() {
        return (int) this.state[0];
    }

    /**
     * @param num must greater than or equal zero.
     */
    public void setIntegerLength(int num) {
        this.state[0] = num;
    }

    public int getDecimalLength() {
        return (int) this.state[1];
    }

    /**
     * @param num must greater than or equal zero.
     */
    public void setDecimalLength(int num) {
        this.state[1] = num;
    }

    public boolean isInvert() {
        return this.invert == 1;
    }

    public void setInvert(boolean invert) {
        this.invert = (byte) (invert ? 1 : 0);
    }
}
