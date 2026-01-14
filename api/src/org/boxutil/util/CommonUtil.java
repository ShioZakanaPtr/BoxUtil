package org.boxutil.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.boxutil.define.BoxDatabase;
import org.boxutil.backends.reflect.BUtil_RefMethod;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.nio.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CommonUtil {
    private final static double[] _LINEAR = new double[]{0.2126729d, 0.7151522d, 0.0721750d};
    private final static int[] _IMAGE_SAVE_MASK = new int[]{0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000};
    private final static byte[] _IMAGE_SAVE_MOVE = new byte[]{16, 8, 0, 24};
    private final static ThreadLocal<Integer> _GL_TRANSFER_FBO = new ThreadLocal<>();

    public final static class Kelvin {
        public final static Color K_1000 = new Color(255, 68, 0);
        public final static Color K_1930 = new Color(255, 133, 3);
        public final static Color K_3000 = new Color(255, 177, 109);
        public final static Color K_4000 = new Color(255, 205, 166);
        public final static Color K_5600 = new Color(255, 239, 225);
        public final static Color K_6000 = new Color(255, 246, 236);
        public final static Color K_6500 = new Color(255, 254, 250);
        public final static Color K_6700 = new Color(254, 248, 255);
        public final static Color K_7000 = new Color(242, 242, 250);
        public final static Color K_10000 = new Color(201, 218, 255);
        public final static Color K_15000 = new Color(181, 205, 255);
        public final static Color K_40000 = new Color(151, 185, 255);
    }

    private static int colorClamp(int value) {
        return Math.max(Math.min(value, 255), 0);
    }

    private static float colorClamp(float value) {
        return Math.max(Math.min(value, 1.0f), 0.0f);
    }

    /**
     * Not accurate numbers.<p>
     * The value at <code>[1000, 40000]</code>, unit is <code>K</code>(Kelvin).
     */
    public static Color getKelvinColor(float temperature) {
        float k = temperature;
        if (k <= 1000.0f) return Kelvin.K_1000;
        else if (k == 6500.0f) return Kelvin.K_6500;
        else if (k >= 40000.0f) return Kelvin.K_40000;

        int red, green, blue;
        k *= 0.01f;
        if (k <= 66.0f) {
            red = 0;

            float toClamp = 99.4708025861f * (float) Math.log(k) - 161.1195681661f;
            green = colorClamp((int) toClamp);

            if (k <= 19.0f) {
                blue = 0;
            } else {
                toClamp = 138.5177312231f * (float) Math.log(k - 10.0f) - 305.0447927307f;
                blue = colorClamp((int) toClamp);
            }
        } else {
            float km = k - 60.0f;
            float toClamp = 329.698727446f * (float) Math.pow(km, -0.1332047592f);
            red = colorClamp((int) toClamp);

            toClamp = 288.1221695283f * (float) Math.pow(km, -0.0755148492f);
            green = colorClamp((int) toClamp);

            blue = 255;
        }

        return new Color(red, green, blue);
    }

    public static float[] RGBToHSVArray(float r, float g, float b) {
        float[] p = g > b ? new float[]{g, b, 0.0f, -0.33333334f} : new float[]{b, g, -1.0f, 0.6666667f};
        float[] q = r > p[0] ? new float[]{r, p[1], p[2], p[0]} : new float[]{p[0], p[1], p[3], r};
        float d = q[0] - Math.min(q[3], q[1]);
        return new float[]{Math.abs(q[2] + (q[3] - q[1]) / (6.0f * d + 1e-6f)), d / (q[0] + 1e-6f), q[0]};
    }

    public static Vector3f RGBToHSV(Vector3f color, Vector3f out) {
        if (out == null) out = new Vector3f();
        float[] array = RGBToHSVArray(color.x, color.y, color.z);
        out.set(array[0], array[1], array[2]);
        return out;
    }

    public static Vector3f RGBToHSV(Color color, Vector3f out) {
        return RGBToHSV(colorNormalization3f(color, out), out);
    }

    public static float[] HSVToRGBArray(float h, float s, float v) {
        float inX = h * 6.0f;
        float[] result = new float[]{inX, inX + 4.0f, inX + 2.0f};
        result[0] %= 6.0f;
        result[0] = Math.max(Math.min(Math.abs(result[0] - 3.0f) - 1.0f, 1.0f), 0.0f);
        result[1] %= 6.0f;
        result[1] = Math.max(Math.min(Math.abs(result[1] - 3.0f) - 1.0f, 1.0f), 0.0f);
        result[2] %= 6.0f;
        result[2] = Math.max(Math.min(Math.abs(result[2] - 3.0f) - 1.0f, 1.0f), 0.0f);
        float factorOM = 1.0f - s;
        result[0] = v * (factorOM + result[0] * s);
        result[1] = v * (factorOM + result[1] * s);
        result[2] = v * (factorOM + result[2] * s);
        return result;
    }

    public static Vector3f HSVToRGB(Vector3f color, Vector3f out) {
        if (out == null) out = new Vector3f();
        float[] array = HSVToRGBArray(color.x, color.y, color.z);
        out.set(array[0], array[1], array[2]);
        return out;
    }

    public static Color HSVToRGB(Vector3f color) {
        return toCommonColor(HSVToRGB(color, null));
    }

    public static Color HSVToRGB(Vector3f color, float alpha) {
        Vector3f out = HSVToRGB(color, null);
        return toCommonColor(new Vector4f(out.x, out.y, out.z, colorClamp(Math.round(alpha * 255.0f))));
    }

    public static Color toCommonColor(@NotNull Vector3f color) {
        int r = colorClamp(Math.round(color.x * 255.0f));
        int g = colorClamp(Math.round(color.y * 255.0f));
        int b = colorClamp(Math.round(color.z * 255.0f));
        return new Color(r, g, b);
    }

    public static Vector3f clampColor3f(@NotNull Vector3f color, Vector3f out) {
        if (out == null) out = new Vector3f();
        out.x = colorClamp(color.x);
        out.y = colorClamp(color.y);
        out.z = colorClamp(color.z);
        return out;
    }

    public static Color toCommonColor(@NotNull Vector4f color) {
        int r = colorClamp(Math.round(color.x * 255.0f));
        int g = colorClamp(Math.round(color.y * 255.0f));
        int b = colorClamp(Math.round(color.z * 255.0f));
        int a = colorClamp(Math.round(color.w * 255.0f));
        return new Color(r, g, b, a);
    }

    public static Vector4f clampColor4f(@NotNull Vector4f color, Vector4f out) {
        if (out == null) out = new Vector4f();
        out.x = colorClamp(color.x);
        out.y = colorClamp(color.y);
        out.z = colorClamp(color.z);
        out.w = colorClamp(color.w);
        return out;
    }

    public static Vector3f colorNormalization3f(@NotNull Color color, Vector3f out) {
        if (out == null) out = new Vector3f();
        out.x = color.getRed() / 255.0f;
        out.y = color.getGreen() / 255.0f;
        out.z = color.getBlue() / 255.0f;
        return out;
    }

    public static Vector4f colorNormalization4f(@NotNull Color color, Vector4f out) {
        if (out == null) out = new Vector4f();
        out.x = color.getRed() / 255.0f;
        out.y = color.getGreen() / 255.0f;
        out.z = color.getBlue() / 255.0f;
        out.w = color.getAlpha() / 255.0f;
        return out;
    }

    public static float[] colorNormalization3fArray(@NotNull Color color, float[] out) {
        if (out == null) out = new float[3];
        Vector3f tmp = colorNormalization3f(color, new Vector3f());
        out[0] = tmp.x;
        out[1] = tmp.y;
        out[2] = tmp.z;
        return out;
    }

    public static float[] colorNormalization4fArray(@NotNull Color color, float[] out) {
        if (out == null) out = new float[4];
        Vector4f tmp = colorNormalization4f(color, new Vector4f());
        out[0] = tmp.x;
        out[1] = tmp.y;
        out[2] = tmp.z;
        out[3] = tmp.w;
        return out;
    }

    public static byte[] colorToByteArray(Color color) {
        return new byte[]{(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha()};
    }

    public static byte[] color4fToByteArray(Vector4f color) {
        return new byte[]{(byte) (color.x * 255.0f), (byte) (color.y * 255.0f), (byte) (color.z * 255.0f), (byte) (color.w * 255.0f)};
    }

    /**
     * @param color length is 4
     */
    public static Color byteArrayToColor(byte... color) {
        return new Color(color[0] & 0xFF, color[1] & 0xFF, color[2] & 0xFF, color[3] & 0xFF);
    }

    /**
     * @param color length is 4
     */
    public static Vector4f byteArrayToColor4f(byte... color) {
        return new Vector4f((color[0] & 0xFF) / 255.0f, (color[1] & 0xFF) / 255.0f, (color[2] & 0xFF) / 255.0f, (color[3] & 0xFF) / 255.0f);
    }

    public static Vector4f colorNormalization4f(@NotNull Color color, float alpha) {
        return new Vector4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
    }

    public static boolean isZeroVector3f(@NotNull Vector3f check) {
        return check.x == 0.0f && check.y == 0.0f && check.z == 0.0f;
    }

    public static boolean isZeroVector4f(@NotNull Vector4f check) {
        return check.x == 0.0f && check.y == 0.0f && check.z == 0.0f && check.w == 0.0f;
    }

    public static float[] getVector2fArray(@NotNull Vector2f vector) {
        return new float[]{vector.x, vector.y};
    }

    public static float[] getVector3fArray(@NotNull Vector3f vector) {
        return new float[]{vector.x, vector.y, vector.z};
    }

    public static float[] getVector4fArray(@NotNull Vector4f vector) {
        return new float[]{vector.x, vector.y, vector.z, vector.w};
    }

    public static float[] getMatrix2fArray(@NotNull Matrix2f matrix) {
        return new float[]{
                matrix.m00, matrix.m01,
                matrix.m10, matrix.m11};
    }

    public static float[] getMatrix3fArray(@NotNull Matrix3f matrix) {
        return new float[]{
                matrix.m00, matrix.m01, matrix.m02,
                matrix.m10, matrix.m11, matrix.m12,
                matrix.m20, matrix.m21, matrix.m22};
    }

    public static float[] getMatrix4fArray(@NotNull Matrix4f matrix) {
        return new float[]{
                matrix.m00, matrix.m01, matrix.m02, matrix.m03,
                matrix.m10, matrix.m11, matrix.m12, matrix.m13,
                matrix.m20, matrix.m21, matrix.m22, matrix.m23,
                matrix.m30, matrix.m31, matrix.m32, matrix.m33};
    }

    public static Matrix2f toMatrix2f(float[] array) {
        Matrix2f matrix = new Matrix2f();
        if (array.length < 4) return matrix;
        matrix.m00 = array[0];  matrix.m01 = array[1];
        matrix.m10 = array[2];  matrix.m11 = array[3];
        return matrix;
    }

    public static Matrix3f toMatrix3f(float[] array) {
        Matrix3f matrix = new Matrix3f();
        if (array == null || array.length < 9) return matrix;
        matrix.m00 = array[0];  matrix.m01 = array[1];  matrix.m02 = array[2];
        matrix.m10 = array[3];  matrix.m11 = array[4];  matrix.m12 = array[5];
        matrix.m20 = array[6];  matrix.m21 = array[7];  matrix.m22 = array[8];
        return matrix;
    }

    public static Matrix4f toMatrix4f(float[] array) {
        Matrix4f matrix = new Matrix4f();
        if (array == null || array.length < 16) return matrix;
        matrix.m00 = array[0];  matrix.m01 = array[1];  matrix.m02 = array[2];  matrix.m03 = array[3];
        matrix.m10 = array[4];  matrix.m11 = array[5];  matrix.m12 = array[6];  matrix.m13 = array[7];
        matrix.m20 = array[8];  matrix.m21 = array[9];  matrix.m22 = array[10];  matrix.m23 = array[11];
        matrix.m30 = array[12];  matrix.m31 = array[13];  matrix.m32 = array[14];  matrix.m33 = array[15];
        return matrix;
    }

    public static FloatBuffer createFloatBuffer(Vector2f vector) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
        buffer.put(0, vector.x);
        buffer.put(1, vector.y);
        buffer.position(0);
        buffer.limit(2);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Vector2f... vectors) {
        final int length = vectors.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length << 1);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i << 1) {
            buffer.put(ptr, vectors[i].x);
            buffer.put(ptr + 1, vectors[i].y);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Vector3f vector) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
        buffer.put(0, vector.x);
        buffer.put(1, vector.y);
        buffer.put(2, vector.z);
        buffer.position(0);
        buffer.limit(3);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Vector3f... vectors) {
        final int length = vectors.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length * 3);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i * 3) {
            buffer.put(ptr, vectors[i].x);
            buffer.put(ptr + 1, vectors[i].y);
            buffer.put(ptr + 2, vectors[i].z);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Vector4f vector) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(0, vector.x);
        buffer.put(1, vector.y);
        buffer.put(2, vector.z);
        buffer.put(3, vector.w);
        buffer.position(0);
        buffer.limit(4);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Vector4f... vectors) {
        final int length = vectors.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vectors.length << 2);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i << 2) {
            buffer.put(ptr, vectors[i].x);
            buffer.put(ptr + 1, vectors[i].y);
            buffer.put(ptr + 2, vectors[i].z);
            buffer.put(ptr + 3, vectors[i].w);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix2f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(0, getMatrix2fArray(matrix), 0, 4);
        buffer.position(0);
        buffer.limit(4);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix2f... matrices) {
        final int length = matrices.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length << 2);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i << 2) {
            buffer.put(ptr, matrices[i].m00);
            buffer.put(ptr + 1, matrices[i].m01);
            buffer.put(ptr + 2, matrices[i].m10);
            buffer.put(ptr + 3, matrices[i].m11);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix3f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        buffer.put(0, getMatrix3fArray(matrix), 0, 9);
        buffer.position(0);
        buffer.limit(9);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix3f... matrices) {
        final int length = matrices.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length * 9);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i * 9) {
            buffer.put(ptr, matrices[i].m00);
            buffer.put(ptr + 1, matrices[i].m01);
            buffer.put(ptr + 2, matrices[i].m02);
            buffer.put(ptr + 3, matrices[i].m10);
            buffer.put(ptr + 4, matrices[i].m11);
            buffer.put(ptr + 5, matrices[i].m12);
            buffer.put(ptr + 6, matrices[i].m20);
            buffer.put(ptr + 7, matrices[i].m21);
            buffer.put(ptr + 8, matrices[i].m22);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(0, getMatrix4fArray(matrix), 0, 16);
        buffer.position(0);
        buffer.limit(16);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix4f... matrices) {
        final int length = matrices.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length << 4);
        for (int i = 0, ptr = 0; i < length; ++i, ptr = i << 4) {
            buffer.put(ptr, matrices[i].m00);
            buffer.put(ptr + 1, matrices[i].m01);
            buffer.put(ptr + 2, matrices[i].m02);
            buffer.put(ptr + 3, matrices[i].m03);
            buffer.put(ptr + 4, matrices[i].m10);
            buffer.put(ptr + 5, matrices[i].m11);
            buffer.put(ptr + 6, matrices[i].m12);
            buffer.put(ptr + 7, matrices[i].m13);
            buffer.put(ptr + 8, matrices[i].m20);
            buffer.put(ptr + 9, matrices[i].m21);
            buffer.put(ptr + 10, matrices[i].m22);
            buffer.put(ptr + 11, matrices[i].m23);
            buffer.put(ptr + 12, matrices[i].m30);
            buffer.put(ptr + 13, matrices[i].m31);
            buffer.put(ptr + 14, matrices[i].m32);
            buffer.put(ptr + 15, matrices[i].m33);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public static PointerBuffer createPointerBuffer(long... array) {
        final int length = array.length;
        PointerBuffer buffer = BufferUtils.createPointerBuffer(length);
        buffer.put(array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static DoubleBuffer createFloatBuffer(double... array) {
        final int length = array.length;
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(float... array) {
        final int length = array.length;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static LongBuffer createLongBuffer(long... array) {
        final int length = array.length;
        LongBuffer buffer = BufferUtils.createLongBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static IntBuffer createIntBuffer(int... array) {
        final int length = array.length;
        IntBuffer buffer = BufferUtils.createIntBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static ShortBuffer createShortBuffer(short... array) {
        final int length = array.length;
        ShortBuffer buffer = BufferUtils.createShortBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static CharBuffer createShortBuffer(char... array) {
        final int length = array.length;
        CharBuffer buffer = BufferUtils.createCharBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    public static ByteBuffer createByteBuffer(byte... array) {
        final int length = array.length;
        ByteBuffer buffer = BufferUtils.createByteBuffer(length);
        buffer.put(0, array, 0, length);
        buffer.position(0);
        buffer.limit(length);
        return buffer;
    }

    /**
     * Just for fast, ignore special values.
     */
    public static float float16BitsToFloat(short src) {
        int exp = (( (src >>> 10) & 0x1f) + 112 & 0xff) << 23;
        return Float.intBitsToFloat((src & 0b1000000000000000) << 16 | exp | (src & 0b1111111111) << 13);
    }

    /**
     * Just for fast, ignore special values.
     */
    public static short float16ToShort(float src) {
        int bits = Float.floatToRawIntBits(src) >>> 13;
        return (short) ((bits & 0b1100000000000000000) >>> 3 | (bits & 0b11111111111111));
    }

    /**
     * Just for fast, ignore special values.
     */
    public static short[] float16ToShort(float... array) {
        final int length = array.length;
        short[] result = new short[length];
        for (int i = 0; i < length; i++) result[i] = float16ToShort(array[i]);
        return result;
    }

    /**
     * Just for fast, ignore special values.
     */
    public static ShortBuffer putFloat16(@NotNull ShortBuffer buffer, float src) {
        buffer.put(float16ToShort(src));
        return buffer;
    }

    /**
     * Just for fast, ignore special values.
     */
    public static ShortBuffer putFloat16(@NotNull ShortBuffer buffer, float... array) {
        for (float num : array) buffer.put(float16ToShort(num));
        return buffer;
    }

    public static short packingBytesToShort(byte i1, byte i0) {
        return (short) ((i1 << 8) | (i0 & 0xff));
    }

    public static short[] packingBytesToShort(byte[] i1, byte[] i0) {
        final int length = i1.length;
        short[] result = new short[length];
        for (int i = 0; i < length; i++) result[i] = packingBytesToShort(i1[i], i0[i]);
        return result;
    }

    public static ShortBuffer putPackingBytes(@NotNull ShortBuffer buffer, int index, byte i1, byte i0) {
        buffer.put(index, packingBytesToShort(i1, i0));
        return buffer;
    }

    public static ShortBuffer putPackingBytes(@NotNull ShortBuffer buffer, byte i1, byte i0) {
        buffer.put(packingBytesToShort(i1, i0));
        return buffer;
    }

    public static ShortBuffer putPackingBytes(@NotNull ShortBuffer buffer, byte[] i1, byte[] i0) {
        for (int i = 0; i < i1.length; i++) buffer.put(packingBytesToShort(i1[i], i0[i]));
        return buffer;
    }

    public static int packingBytesToInt(byte i3, byte i2, byte i1, byte i0) {
        return (i3 << 24) | ((i2 << 16) & 0xff0000) | ((i1 << 8) & 0xff00) | (i0 & 0xff);
    }

    public static int[] packingBytesToInt(byte[] i3, byte[] i2, byte[] i1, byte[] i0) {
        final int length = i3.length;
        int[] result = new int[length];
        for (int i = 0; i < length; i++) result[i] = packingBytesToInt(i3[i], i2[i], i1[i], i0[i]);
        return result;
    }

    public static IntBuffer putPackingBytes(@NotNull IntBuffer buffer, int index, byte i3, byte i2, byte i1, byte i0) {
        buffer.put(index, packingBytesToInt(i3, i2, i1, i0));
        return buffer;
    }

    public static IntBuffer putPackingBytes(@NotNull IntBuffer buffer, byte i3, byte i2, byte i1, byte i0) {
        buffer.put(packingBytesToInt(i3, i2, i1, i0));
        return buffer;
    }

    public static IntBuffer putPackingBytes(@NotNull IntBuffer buffer, byte[] i3, byte[] i2, byte[] i1, byte[] i0) {
        for (int i = 0; i < i3.length; i++) buffer.put(packingBytesToInt(i3[i], i2[i], i1[i], i0[i]));
        return buffer;
    }

    public static float packingBytesToFloat(byte i3, byte i2, byte i1, byte i0) {
        return Float.intBitsToFloat(packingBytesToInt(i3, i2, i1, i0));
    }

    public static float[] packingBytesToFloat(byte[] i3, byte[] i2, byte[] i1, byte[] i0) {
        final int length = i3.length;
        float[] result = new float[length];
        for (int i = 0; i < length; i++) result[i] = packingBytesToFloat(i3[i], i2[i], i1[i], i0[i]);
        return result;
    }

    public static FloatBuffer putPackingBytes(FloatBuffer buffer, int index, byte i3, byte i2, byte i1, byte i0) {
        buffer.put(index,packingBytesToFloat(i3, i2, i1, i0));
        return buffer;
    }

    public static FloatBuffer putPackingBytes(@NotNull FloatBuffer buffer, byte i3, byte i2, byte i1, byte i0) {
        buffer.put(packingBytesToFloat(i3, i2, i1, i0));
        return buffer;
    }

    public static FloatBuffer putPackingBytes(@NotNull FloatBuffer buffer, byte[] i3, byte[] i2, byte[] i1, byte[] i0) {
        for (int i = 0; i < i3.length; i++) buffer.put(packingBytesToFloat(i3[i], i2[i], i1[i], i0[i]));
        return buffer;
    }

    public static byte normalizedFloatToByte(float src) {
        return src < 0 ? (byte) (Byte.MIN_VALUE * -src) : (byte) (Byte.MAX_VALUE * src);
    }

    public static byte[] normalizedFloatToByte(float... array) {
        final int length = array.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
            result[i] = array[i] < 0 ? (byte) (Byte.MIN_VALUE * -array[i]) : (byte) (Byte.MAX_VALUE * array[i]);
        return result;
    }

    public static ByteBuffer putNormalizedByte(@NotNull ByteBuffer buffer, float src) {
        buffer.put(normalizedFloatToByte(src));
        return buffer;
    }

    public static ByteBuffer putNormalizedByte(@NotNull ByteBuffer buffer, float... array) {
        for (float num : array) buffer.put(normalizedFloatToByte(num));
        return buffer;
    }

    public static byte normalizedFloatToUnsignedByte(float src) {
        return (byte) colorClamp(Math.round(src * 255));
    }

    public static byte[] normalizedFloatToUnsignedByte(float... array) {
        final int length = array.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
            result[i] = (byte) colorClamp(Math.round(array[i] * 255));
        return result;
    }

    public static ByteBuffer putNormalizedUnsignedByte(@NotNull ByteBuffer buffer, float src) {
        buffer.put(normalizedFloatToUnsignedByte(src));
        return buffer;
    }

    public static ByteBuffer putNormalizedUnsignedByte(@NotNull ByteBuffer buffer, float... array) {
        for (float num : array) buffer.put(normalizedFloatToUnsignedByte(num));
        return buffer;
    }

    public static Object[] reverseArray(Object[] array) {
        final int length = array.length;
        final int midI = length >>> 1;
        Object tmp;
        for (int i = 0, j = length - 1; i < midI; ++i, --j) {
            tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /**
     * @return int[] = {sizeX, sizeY, channelNum, pixelPreImage, ivec3(averageColor), ivec3(averageBrightColor)};<p>
     *     ByteBuffer = pixels value;
     */
    public static Pair<int[], ByteBuffer> getRawPixels(String file, int channelNum) {
        final byte channel = (byte) Math.max(Math.min(channelNum, 4), 1);
        Pair<int[], ByteBuffer> result = new Pair<>(new int[10], null);
        double[] avc = new double[channel];
        if (file == null || file.isEmpty()) return result;
        try (InputStream inputStream = Global.getSettings().openStream(file)) {
            BufferedImage imageBuffer = ImageIO.read(new BufferedInputStream(inputStream));
            Raster data = imageBuffer.getData();

            String[] formatA = file.split("\\.");
            if (formatA.length < 1) return result;
            String format = formatA[formatA.length - 1].toLowerCase();
            final boolean isPNG = format.contentEquals("png");
            final boolean formatCheck = format.contentEquals("bmp") || format.contentEquals("jpg") || format.contentEquals("jpeg") || isPNG;
            if (!formatCheck) {
                Global.getLogger(CommonUtil.class).error("'BoxUtil' cannot loading file: '" + file + "', only support to reading 'bmp/jpg/jpeg/png'");
                return result;
            }
            result.one[0] = data.getWidth();
            result.one[1] = data.getHeight();
            result.one[2] = channel;
            result.one[3] = result.one[0] * result.one[1];
            result.two = BufferUtils.createByteBuffer(result.one[3] * channel);

            Global.getLogger(CommonUtil.class).info("'BoxUtil' loading sprite file: '" + file + "', with width " + result.one[0] + " and height " + result.one[1] + ", pixel have " + channel + " channels.");

            int[] pixels = new int[4];
            for (int y = result.one[1] - 1; y >= 0; --y) {
                for (int x = 0; x < result.one[0]; ++x) {
                    data.getPixel(x, y, pixels);
                    if (pixels[3] == 0) {
                        if (isPNG) pixels[0] = pixels[1] = pixels[2] = 0; else pixels[3] = 255;
                    }
                    for (int c = 0; c < channel; ++c) {
                        avc[c] += pixels[c];
                        result.two.put((byte) pixels[c]);
                    }
                }
            }

            result.two.position(0);
            result.two.limit(result.two.capacity());
            for (int i = 0; i < Math.min(avc.length, 3); ++i) {
                avc[i] = Math.round(avc[i] / result.one[3]);
                double gray = Math.round(avc[i] * _LINEAR[i]);
                result.one[4 + i] = Math.max(Math.min((int) avc[i], 255), 0);
                result.one[7 + i] = Math.max(Math.min((int) gray, 255), 0);
            }
        } catch (IOException e) {
            printThrowable(CommonUtil.class, "'BoxUtil' loading image '" + file + "' failed: ", e);
            return result;
        }
        return result;
    }

    /**
     * Requires OpenGL 3.0 or higher and Framebuffer support.<p>
     * Get the level 0 texture object bytes data.
     *
     * @param target {@link GL11#GL_TEXTURE_1D} and {@link GL11#GL_TEXTURE_2D} only.
     * @param texture only support the 8bit per channel ubyte texture, and format must be {@link GL11#GL_RED}, {@link GL30#GL_RG}, {@link GL11#GL_RGB}, {@link GL11#GL_RGBA}.
     *
     * @return int[] = {width, height, internalFormat};<p>
     *     ByteBuffer = pixels value, null if format not support or failed;<p>
     *     channelNum returns 0 if failed or format not support or failed.
     */
    public static Pair<int[], ByteBuffer> getGLTexture(int target, int texture, byte channelNum) {
        final int[] format = new int[]{GL11.GL_RED, GL30.GL_RG, GL11.GL_RGB, GL11.GL_RGBA};
        final byte[] alignment = new byte[]{1, 2, 1, 4};
        final byte picker = (byte) Math.min(channelNum - 1, 3);
        Pair<int[], ByteBuffer> result = new Pair<>(new int[3], null);
        if (!BoxDatabase.getGLState().GL_FBO) return result;
        Integer _fbo = _GL_TRANSFER_FBO.get();
        if (_fbo == null) {
            _fbo = GL30.glGenFramebuffers();
            _GL_TRANSFER_FBO.set(_fbo);
            if (_fbo > 0) {
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, _fbo);
                GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            }
        }
        if (_fbo < 0 || texture == 0 || channelNum < 1) return result;
        GL11.glBindTexture(target, texture);
        result.one[0] = GL11.glGetTexLevelParameteri(target, 0, GL11.GL_TEXTURE_WIDTH);
        result.one[1] = GL11.glGetTexLevelParameteri(target, 0, GL11.GL_TEXTURE_HEIGHT);
        result.one[2] = GL11.glGetTexLevelParameteri(target, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
        result.two = BufferUtils.createByteBuffer(result.one[0] * result.one[1] * channelNum);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, alignment[picker]);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, _fbo);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, target, texture, 0);
        GL11.glReadPixels(0, 0, result.one[0], result.one[1], format[picker], GL11.GL_UNSIGNED_BYTE, result.two);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 4);
        return result;
    }

    /**
     * General method for save OpenGL texture object to local ARGB png format image.<p>
     * Root path is in <code>'gameRootDirectory'\</code> for default.
     *
     * @param path <strong>unneeded</strong> add a <code>\</code> at the start; set to null that save to <code>'gameRootDirectory'\saves\images\</code>.
     * @param saveFileName the name without format, length less than or equal 100; set to null that save to <code>"SavedTexture_'System.currentTimeMillis()'.png"</code>.
     * @param buffer default OpenGL order, LB = positionZero, TR = positionMax.
     * @param width the texture width.
     * @param height the texture height.
     * @param bufferChannel the OpenGL texture object channel num: <strong>1, 2, 3, 4</strong>. if less than <strong>4</strong>, will set alpha channel to <strong>255</strong>.
     *
     * @return true if success, false if failed.
     */
    public static boolean saveBytesImage(@Nullable String path, @Nullable String saveFileName, ByteBuffer buffer, int width, int height, byte bufferChannel) {
        if (!BUtil_RefMethod.VALID) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' image save failed, method is invalid.");
            return false;
        }
        if (width < 1 || height < 1 || bufferChannel < 1) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' image save failed, no a valid parameters: width= " + width + ", height= " + height + ", bufferChannel= " + bufferChannel);
            return false;
        }
        try {
            final char[] _checkChar = new char[]{'\\', '/', ':', '*', '?', '"', '<', '>', '|', '\'', '.', '\n', '\r', '\t', '\0', '\f', '\b'};

            Path gameRootCore = Paths.get("").toAbsolutePath(), gameRoot;
            gameRoot = gameRootCore.getParent();
            String absolutePath = gameRoot == null ? gameRootCore.toString() : gameRoot.toString();
            String fixedSavePath = path;
            if (fixedSavePath == null) absolutePath += BUtil_RefMethod.java_io_File_SEPARATOR + "saves" + BUtil_RefMethod.java_io_File_SEPARATOR + "images";
            else if (!fixedSavePath.isEmpty()) {
                for (char check : _checkChar) fixedSavePath = fixedSavePath.replace(check, '_');
                absolutePath += BUtil_RefMethod.java_io_File_SEPARATOR + fixedSavePath;
            }
            Object file = BUtil_RefMethod.java_io_File__newFile(absolutePath);
            if (!(boolean) BUtil_RefMethod.java_io_File_exists(file)) BUtil_RefMethod.java_io_File_mkdirs(file);
            String fixedSaveFileName = saveFileName;
            long currTime = System.currentTimeMillis();
            if (fixedSaveFileName == null || fixedSaveFileName.isEmpty()) {
                fixedSaveFileName = "";
            } else {
                for (char check : _checkChar) fixedSaveFileName = fixedSaveFileName.replace(check, '_');
            }
            if (fixedSaveFileName.isEmpty()) fixedSaveFileName = "SavedTexture_" + currTime;
            else if (fixedSaveFileName.length() > 100) fixedSaveFileName = fixedSaveFileName.substring(0, 100);
            absolutePath += BUtil_RefMethod.java_io_File_SEPARATOR + fixedSaveFileName + ".png";
            file = BUtil_RefMethod.java_io_File__newFile(absolutePath);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            int bufferPose = 0, pixel;
            boolean setAlpha = bufferChannel < 4;
            for (int y = height - 1; y >= 0; --y) {
                for (int x = 0; x < width; ++x) {
                    pixel = 0x0;
                    for (byte c = 0; c < bufferChannel; ++c) {
                        pixel |= (buffer.get(bufferPose) << _IMAGE_SAVE_MOVE[c]) & _IMAGE_SAVE_MASK[c];
                        ++bufferPose;
                    }
                    if (setAlpha) pixel |= 0xFF000000;
                    image.setRGB(x, y, pixel);
                }
            }
            boolean success = BUtil_RefMethod._javax_imageio_ImageIO_write(image, "png", file);
            if (success) Global.getLogger(CommonUtil.class).info("'BoxUtil' image saved: " + absolutePath);
            else Global.getLogger(CommonUtil.class).error("'BoxUtil' image save failed, no appropriate writer is found: " + absolutePath);
            return success;
        } catch (Throwable e) {
            printThrowable(CommonUtil.class, "'BoxUtil' image save failed: ", e);
            return false;
        }
    }

    /**
     * Requires OpenGL 3.0 or higher and Framebuffer support.<p>
     * Save OpenGL texture object to local ARGB png format image.<p>
     * Root path is in <code>'gameRootDirectory'\</code> for default.
     *
     * @param path <strong>unneeded</strong> add a <code>\</code> at the start; set to null that save to <code>'gameRootDirectory'\saves\images\</code>.
     * @param saveFileName the name without format, length less than or equal 100; set to null that save to <code>"SavedTexture_'System.currentTimeMillis()'.png"</code>.
     * @param target {@link GL11#GL_TEXTURE_1D} and {@link GL11#GL_TEXTURE_2D} only.
     * @param texture only support the 8bit per channel unsigned byte texture, and format must be {@link GL11#GL_RED}, {@link GL30#GL_RG}, {@link GL11#GL_RGB}, {@link GL11#GL_RGBA}.
     *
     * @return true if success, false if failed.
     */
    public static boolean saveGLTexture(@Nullable String path, @Nullable String saveFileName, int target, int texture, byte channelNum) {
        Pair<int[], ByteBuffer> result = getGLTexture(target, texture, channelNum);
        if (result.one[0] < 1 || result.one[1] < 1 || result.two == null) return false;
        return saveBytesImage(path, saveFileName, result.two, result.one[0], result.one[1], channelNum);
    }

    public static void glDebug(String tags, Object info) throws OpenGLException {
        Global.getLogger(CommonUtil.class).info(tags + info);
        Global.getLogger(CommonUtil.class).info(GLU.gluErrorString(GL11.glGetError()));
        Util.checkGLError();
    }

    public static void printThrowable(Logger logger, Level level, String tips, @NotNull Throwable e) {
        StringBuilder str = new StringBuilder(e.getClass().getName() + ": " + e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            str.append("\n\tat ");
            if (stackTraceElement.isNativeMethod()) str.append("[native] ");
            str.append(stackTraceElement);
        }
        logger.log(level, tips + str);
    }

    public static void printThrowable(Logger logger, String tips, @NotNull Throwable e) {
        printThrowable(logger, Level.ERROR, tips, e);
    }

    public static void printThrowable(Class clazz, String tips, @NotNull Throwable e) {
        printThrowable(Global.getLogger(clazz), tips, e);
    }

    private CommonUtil() {}
}
