package org.boxutil.units.standard.attribute;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.define.BoxDatabase;
import org.boxutil.util.TrigUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

/**
 * Max float value is {@link NodeData#MAX_VALUE}, used half-float in GL.<p>
 * Color used unsigned byte in GL.<p>
 * Tangent is vector value from (0, 0).<p>
 * Location based on entity space, it is not a world location.
 */
public class NodeData {
    public static final float MAX_VALUE = BoxDatabase.HALF_FLOAT_MAX_VALUE;
    protected final float[] state = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f}; // vec2(loc), vec2(tangent left), vec2(tangent right), width, mixFactor
    protected final byte[] colorStateA = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}; // vec4(color)
    protected final byte[] colorStateB = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}; // vec4(emissiveColor)

    /**
     * Origin point.<p>
     * Point at x-axis.
     */
    public NodeData() {
        this.state[2] = -1.0f;
        this.state[4] = 1.0f;
    }

    public NodeData(NodeData node) {
        System.arraycopy(node.state, 0, this.state, 0, this.state.length);
        System.arraycopy(node.colorStateA, 0, this.colorStateA, 0, this.colorStateA.length);
        System.arraycopy(node.colorStateB, 0, this.colorStateB, 0, this.colorStateB.length);
    }

    public NodeData(Vector2f location, Vector2f tangentL, Vector2f tangentR) {
        this(location.x, location.y, tangentL.x, tangentL.y, tangentR.x, tangentR.y);
    }

    public NodeData(float locationX, float locationY, float tangentLX, float tangentLY, float tangentRX, float tangentRY) {
        this.state[0] = locationX;
        this.state[1] = locationY;
        this.state[2] = tangentLX;
        this.state[3] = tangentLY;
        this.state[4] = tangentRX;
        this.state[5] = tangentRY;
    }

    /**
     * @param angle [0.0f, 360.0f]
     */
    public NodeData(Vector2f location, float angle) {
        this(location, angle, 8.0f);
    }

    /**
     * @param angle [0.0f, 360.0f]
     */
    public NodeData(Vector2f location, float angle, float length) {
        this(location.x, location.y, angle, length, length);
    }

    public NodeData(Vector2f location) {
        this.state[0] = location.x;
        this.state[1] = location.y;
    }

    /**
     * @param angle [0.0f, 360.0f]
     */
    public NodeData(float locationX, float locationY, float angle, float lengthLeft, float lengthRight) {
        this.state[0] = locationX;
        this.state[1] = locationY;
        final float x = (float) Math.cos(Math.toRadians(angle));
        final float y = TrigUtil.sinFormCosF(x, angle);
        this.state[4] = x;
        this.state[5] = y;
        this.state[2] = -this.state[4] * lengthLeft;
        this.state[3] = -this.state[5] * lengthLeft;
        this.state[4] *= lengthRight;
        this.state[5] *= lengthRight;
    }

    public byte[] pickColor_RGBA8_A() {
        return new byte[]{this.colorStateA[1], this.colorStateA[3], this.colorStateB[1], this.colorStateB[3]};
    }

    public byte[] pickColor_RGBA8_B() {
        return new byte[]{this.colorStateA[0], this.colorStateA[2], this.colorStateB[0], this.colorStateB[2]};
    }

    public float[] getState() {
        return this.state;
    }

    public float[] getLocationArray() {
        return new float[]{this.state[0], this.state[1]};
    }

    public Vector2f getLocation() {
        return new Vector2f(this.state[0], this.state[1]);
    }

    public void setLocation(float x, float y) {
        this.state[0] = x;
        this.state[1] = y;
    }

    public void setLocation(Vector2f location) {
        this.setLocation(location.x, location.y);
    }

    public float[] getTangentLeftArray() {
        return new float[]{this.state[2], this.state[3]};
    }

    public Vector2f getTangentLeft() {
        return new Vector2f(this.state[2], this.state[3]);
    }

    public void setTangentLeft(float x, float y) {
        this.state[2] = x;
        this.state[3] = y;
    }

    public void setTangentLeft(Vector2f tangent) {
        this.setTangentLeft(tangent.x, tangent.y);
    }

    public float[] getTangentRightArray() {
        return new float[]{this.state[4], this.state[5]};
    }

    public Vector2f getTangentRight() {
        return new Vector2f(this.state[4], this.state[5]);
    }

    public void setTangentRight(float x, float y) {
        this.state[4] = x;
        this.state[5] = y;
    }

    public void setTangentRight(Vector2f tangent) {
        this.setTangentRight(tangent.x, tangent.y);
    }

    public byte[] getColorState() {
        return new byte[]{this.colorStateA[0], this.colorStateA[1], this.colorStateA[2], this.colorStateA[3], this.colorStateB[0], this.colorStateB[1], this.colorStateB[2], this.colorStateB[3]};
    }

    public byte[] getColorArray() {
        return this.colorStateA;
    }

    public Color getColorC() {
        return new Color(
                this.colorStateA[0] & 0xFF,
                this.colorStateA[1] & 0xFF,
                this.colorStateA[2] & 0xFF,
                this.colorStateA[3] & 0xFF);
    }

    public Vector4f getColor() {
        return new Vector4f(
                (this.colorStateA[0] & 0xFF) / 255.0f,
                (this.colorStateA[1] & 0xFF) / 255.0f,
                (this.colorStateA[2] & 0xFF) / 255.0f,
                (this.colorStateA[3] & 0xFF) / 255.0f);
    }

    public void setColor(@NotNull Color color) {
        this.colorStateA[0] = (byte) color.getRed();
        this.colorStateA[1] = (byte) color.getGreen();
        this.colorStateA[2] = (byte) color.getBlue();
        this.colorStateA[3] = (byte) color.getAlpha();
    }

    public void setColor(byte r, byte g, byte b, byte a) {
        this.colorStateA[0] = r;
        this.colorStateA[1] = g;
        this.colorStateA[2] = b;
        this.colorStateA[3] = a;
    }

    public void setColor(int r, int g, int b, int a) {
        this.colorStateA[0] = (byte) r;
        this.colorStateA[1] = (byte) g;
        this.colorStateA[2] = (byte) b;
        this.colorStateA[3] = (byte) a;
    }

    /**
     * The value 0.0 to 1.0
     */
    public void setColor(float r, float g, float b, float a) {
        this.colorStateA[0] = (byte) (r * 255.0f);
        this.colorStateA[1] = (byte) (g * 255.0f);
        this.colorStateA[2] = (byte) (b * 255.0f);
        this.colorStateA[3] = (byte) (a * 255.0f);
    }

    /**
     * The value 0.0 to 1.0
     */
    public void setAlpha(float a) {
        this.colorStateA[3] = (byte) (a * 255.0f);
    }

    public byte[] getEmissiveColorArray() {
        return this.colorStateB;
    }

    public Color getEmissiveColorC() {
        return new Color(
                this.colorStateB[0] & 0xFF,
                this.colorStateB[1] & 0xFF,
                this.colorStateB[2] & 0xFF,
                this.colorStateB[3] & 0xFF);
    }

    public Vector4f getEmissiveColor() {
        return new Vector4f(
                (this.colorStateB[0] & 0xFF) / 255.0f,
                (this.colorStateB[1] & 0xFF) / 255.0f,
                (this.colorStateB[2] & 0xFF) / 255.0f,
                (this.colorStateB[3] & 0xFF) / 255.0f);
    }

    public void setEmissiveColor(@NotNull Color color) {
        this.colorStateB[0] = (byte) color.getRed();
        this.colorStateB[1] = (byte) color.getGreen();
        this.colorStateB[2] = (byte) color.getBlue();
        this.colorStateB[3] = (byte) color.getAlpha();
    }

    public void setEmissiveColor(byte r, byte g, byte b, byte a) {
        this.colorStateB[0] = r;
        this.colorStateB[1] = g;
        this.colorStateB[2] = b;
        this.colorStateB[3] = a;
    }

    public void setEmissiveColor(int r, int g, int b, int a) {
        this.colorStateB[0] = (byte) r;
        this.colorStateB[1] = (byte) g;
        this.colorStateB[2] = (byte) b;
        this.colorStateB[3] = (byte) a;
    }

    /**
     * The value 0.0 to 1.0
     */
    public void setEmissiveColor(float r, float g, float b, float a) {
        this.colorStateB[0] = (byte) (r * 255.0f);
        this.colorStateB[1] = (byte) (g * 255.0f);
        this.colorStateB[2] = (byte) (b * 255.0f);
        this.colorStateB[3] = (byte) (a * 255.0f);
    }

    /**
     * The value 0.0 to 1.0
     */
    public void setEmissiveAlpha(float a) {
        this.colorStateB[3] = (byte) (a * 255.0f);
    }

    public float getWidth() {
        return this.state[6] * 2.0f;
    }

    public float getWidthDirect() {
        return this.state[6];
    }

    public void setWidth(float width) {
        this.state[6] = width / 2.0f;
    }

    /**
     * @param widthHalf half width
     */
    public void setWidthDirect(float widthHalf) {
        this.state[6] = widthHalf;
    }

    public void tangentAlignLeft() {
        float lengthLeft = (float) Math.sqrt(this.state[2] * this.state[2] + this.state[3] * this.state[3]);
        float lengthRight = (float) Math.sqrt(this.state[4] * this.state[4] + this.state[5] * this.state[5]);
        float scale = lengthRight / lengthLeft;
        this.state[4] = -this.state[2] * scale;
        this.state[5] = -this.state[3] * scale;
    }

    public void tangentAlignRight() {
        float lengthLeft = (float) Math.sqrt(this.state[2] * this.state[2] + this.state[3] * this.state[3]);
        float lengthRight = (float) Math.sqrt(this.state[4] * this.state[4] + this.state[5] * this.state[5]);
        float scale = lengthLeft / lengthRight;
        this.state[2] = -this.state[4] * scale;
        this.state[3] = -this.state[5] * scale;
    }

    /**
     * Will return 1.0 by default.
     */
    public float getMixFactor() {
        return this.state[7];
    }

    /**
     * For control color, emissive color and width.
     */
    public void setMixFactor(float factor) {
        this.state[7] = factor;
    }
}
