package org.boxutil.util;

import com.fs.starfarer.api.combat.ViewportAPI;
import org.boxutil.manager.ShaderCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.*;

import java.nio.FloatBuffer;

public final class TransformUtil {
    /**
     * @return null when determinants got zero.
     */
    public static Matrix4f invertModelMatrix2D(final Matrix4f src, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        float det = src.m00 * src.m11 - src.m10 * src.m01;
        if (Math.abs(det) == 0.0f) return null;

        det = 1.0f / det;
        final float k = src.m11 * src.m30 - src.m10 * src.m31, m = src.m00 * src.m31 - src.m01 * src.m30;

        result.m00 = src.m11 * det;
        result.m10 = -src.m10 * det;
        result.m30 = -k * det;

        result.m01 = -src.m01 * det;
        result.m11 = src.m00 * det;
        result.m31 = -m * det;

        result.m20 = result.m21 = result.m02 = result.m12 = result.m32 = result.m03 = result.m13 = result.m23 = 0.0f;
        result.m22 = result.m33 = 1.0f;

        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 9 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     *
     * @return will not write anything when determinants got zero.
     */
    public static FloatBuffer invertModelMatrix2D(final Matrix4f src, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        float det = src.m00 * src.m11 - src.m10 * src.m01;
        if (Math.abs(det) == 0.0f) return result;

        det = 1.0f / det;
        final float k = src.m11 * src.m30 - src.m10 * src.m31, m = src.m00 * src.m31 - src.m01 * src.m30;

        result.put(writePosition, src.m11 * det);            // m00
        result.put(writePosition + 4, -src.m10 * det); // m10
        result.put(writePosition + 12, -k * det);      // m30

        result.put(writePosition + 1, -src.m01 * det); // m01
        result.put(writePosition + 5, src.m00 * det);  // m11
        result.put(writePosition + 13, -m * det);      // m31

        if (eraseAndWrite) {
            result.put(writePosition + 2, 0.0f);  // m02
            result.put(writePosition + 3, 0.0f);  // m03
            result.put(writePosition + 6, 0.0f);  // m12
            result.put(writePosition + 7, 0.0f);  // m13
            result.put(writePosition + 8, 0.0f);  // m20
            result.put(writePosition + 9, 0.0f);  // m21
            result.put(writePosition + 10, 1.0f); // m22
            result.put(writePosition + 11, 0.0f); // m23
            result.put(writePosition + 14, 0.0f); // m12
            result.put(writePosition + 15, 1.0f); // m33
        }
        return result;
    }

    /**
     * @return null when determinants got zero.
     */
    public static Matrix4f invertModelMatrix3D(final Matrix4f src, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final float vX = src.m30, vY = src.m31, vZ = src.m32;
        float det = src.m00 * (src.m11 * src.m22 - src.m21 * src.m12) - src.m10 * (src.m01 * src.m22 - src.m21 * src.m02) + src.m20 * (src.m01 * src.m12 - src.m11 * src.m02);
        if (Math.abs(det) == 0.0f) return null;

        det = 1.0f / det;
        result.m00 = (src.m11 * src.m22 - src.m12 * src.m21) * det;
        result.m10 = (src.m20 * src.m12 - src.m10 * src.m22) * det;
        result.m20 = (src.m10 * src.m21 - src.m20 * src.m11) * det;

        result.m01 = (src.m21 * src.m02 - src.m01 * src.m22) * det;
        result.m11 = (src.m00 * src.m22 - src.m20 * src.m02) * det;
        result.m21 = (src.m20 * src.m01 - src.m00 * src.m21) * det;

        result.m02 = (src.m01 * src.m12 - src.m11 * src.m02) * det;
        result.m12 = (src.m10 * src.m02 - src.m00 * src.m12) * det;
        result.m22 = (src.m00 * src.m11 - src.m10 * src.m01) * det;
        result.m30 = -(result.m00 * vX + result.m10 * vY + result.m20 * vZ);
        result.m31 = -(result.m01 * vX + result.m11 * vY + result.m21 * vZ);
        result.m32 = -(result.m02 * vX + result.m12 * vY + result.m22 * vZ);

        result.m03 = result.m13 = result.m23 = 0.0f;
        result.m33 = 1.0f;

        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 9 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     *
     * @return will not write anything when determinants got zero.
     */
    public static FloatBuffer invertModelMatrix3D(final Matrix4f src, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        final float vX = src.m30, vY = src.m31, vZ = src.m32;
        float det = src.m00 * (src.m11 * src.m22 - src.m21 * src.m12) - src.m10 * (src.m01 * src.m22 - src.m21 * src.m02) + src.m20 * (src.m01 * src.m12 - src.m11 * src.m02);
        if (Math.abs(det) == 0.0f) return null;

        det = 1.0f / det;
        final float m00 = (src.m11 * src.m22 - src.m12 * src.m21) * det,
                m10 = (src.m20 * src.m12 - src.m10 * src.m22) * det,
                m20 = (src.m10 * src.m21 - src.m20 * src.m11) * det,
                m01 = (src.m21 * src.m02 - src.m01 * src.m22) * det,
                m11 = (src.m00 * src.m22 - src.m20 * src.m02) * det,
                m21 = (src.m20 * src.m01 - src.m00 * src.m21) * det,
                m02 = (src.m01 * src.m12 - src.m11 * src.m02) * det,
                m12 = (src.m10 * src.m02 - src.m00 * src.m12) * det,
                m22 = (src.m00 * src.m11 - src.m10 * src.m01) * det;

        result.put(writePosition, m00);            // m00
        result.put(writePosition + 4, m10);  // m10
        result.put(writePosition + 8, m20);  // m20

        result.put(writePosition + 1, m01);  // m01
        result.put(writePosition + 5, m11);  // m11
        result.put(writePosition + 9, m21);  // m21

        result.put(writePosition + 2, m02);  // m02
        result.put(writePosition + 6, m12);  // m12
        result.put(writePosition + 10, m22); // m22

        result.put(writePosition + 12, -(m00 * vX + m10 * vY + m20 * vZ)); // m30
        result.put(writePosition + 13, -(m01 * vX + m11 * vY + m21 * vZ)); // m31
        result.put(writePosition + 14, -(m02 * vX + m12 * vY + m22 * vZ)); // m32

        if (eraseAndWrite) {
            result.put(writePosition + 3, 0.0f);  // m03
            result.put(writePosition + 7, 0.0f);  // m13
            result.put(writePosition + 11, 0.0f); // m23
            result.put(writePosition + 15, 1.0f); // m33
        }
        return result;
    }

    /**
     * Order is m1 * m2 * m3 ... and more.<p>
     * For usual 3d game, perspectiveMatrix * viewMatrix * modelMatrix
     */
    public static Matrix4f makeStateMatrix(@Nullable Matrix4f result, Matrix4f... matrices) {
        if (result == null) result = new Matrix4f();
        for (Matrix4f matrix : matrices) {
            Matrix4f.mul(result, matrix, result);
        }
        return result;
    }

    /**
     * z-axis
     */
    public static Quaternion rotationFacingOnly(float yaw, @Nullable Quaternion result) {
        if (result == null) result = new Quaternion();
        float yawHalf = yaw * 0.5f;
        float w = (float) Math.cos(Math.toRadians(yawHalf));
        result.set(0.0f, 0.0f, TrigUtil.sinFormCosF(w, yawHalf), w);
        return result;
    }

    /**
     * z-axis
     */
    public static Quaternion rotationFacingOnly(float yaw) {
        return rotationFacingOnly(yaw, new Quaternion());
    }

    public static Quaternion rotationXOnly(float pitch, @Nullable Quaternion result) {
        if (result == null) result = new Quaternion();
        float pitchHalf = pitch * 0.5f;
        float w = (float) Math.cos(Math.toRadians(pitchHalf));
        result.set(TrigUtil.sinFormCosF(w, pitchHalf), 0.0f, 0.0f, w);
        return result;
    }

    public static Quaternion rotationXOnly(float pitch) {
        return rotationXOnly(pitch, new Quaternion());
    }

    public static Quaternion rotationYOnly(float roll, @Nullable Quaternion result) {
        if (result == null) result = new Quaternion();
        float rollHalf = roll * 0.5f;
        float w = (float) Math.cos(Math.toRadians(rollHalf));
        result.set(0.0f,  TrigUtil.sinFormCosF(w, rollHalf), 0.0f, w);
        return result;
    }

    public static Quaternion rotationYOnly(float roll) {
        return rotationYOnly(roll, new Quaternion());
    }

    /**
     * ZXY order.
     *
     * @param yaw z's angle
     * @param pitch x's angle
     * @param roll y's angle
     */
    public static Quaternion rotationZXY(float yaw, float pitch, float roll, @Nullable Quaternion result) {
        if (result == null) result = new Quaternion();
        final float pitchHalf = pitch * 0.5f,
                cp = (float) Math.cos((float) Math.toRadians(pitchHalf)),
                sp = TrigUtil.sinFormCosF(cp, pitchHalf);

        float sr, cr;
        if (roll == pitch) {
            cr = cp;
            sr = sp;
        } else {
            float rollHalf = roll * 0.5f;
            cr = (float) Math.cos((float) Math.toRadians(rollHalf));
            sr = TrigUtil.sinFormCosF(cr, rollHalf);
        }

        float sy, cy;
        if (yaw == pitch) {
            cy = cp;
            sy = sp;
        } else if (yaw == roll) {
            cy = cr;
            sy = sr;
        } else {
            float yawHalf = yaw * 0.5f;
            cy = (float) Math.cos((float) Math.toRadians(yawHalf));
            sy = TrigUtil.sinFormCosF(cy, yawHalf);
        }

        result.set(sp * cr * cy - cp * sr * sy,
                cp * sr * cy + sp * cr * sy,
                cp * cr * sy + sp * sr * cy,
                cp * cr * cy - sp * sr * sy
        );
        return result;
    }

    /**
     * ZXY order.
     *
     * @param yaw z's angle
     * @param pitch x's angle
     * @param roll y's angle
     */
    public static Quaternion rotationZXY(float yaw, float pitch, float roll) {
        return rotationZXY(yaw, pitch, roll, new Quaternion());
    }

    public static Quaternion rotationZXY(final Vector3f rotate, @Nullable Quaternion result) {
        return rotationZXY(rotate.z, rotate.x, rotate.y, result);
    }

    public static Quaternion rotationZXY(final Vector3f rotate) {
        return rotationZXY(rotate, new Quaternion());
    }

    public static Vector2f getRadiansRotateBase(float angRad, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        result.x = (float) Math.cos(angRad);
        result.y = TrigUtil.sinFormCosRadiansF(result.x, angRad);
        return result;
    }

    public static Vector2f getRotateBase(float angle, @Nullable Vector2f result) {
        return getRadiansRotateBase((float) Math.toRadians(angle), result);
    }

    public static Matrix2f createSimpleRotateMatrix(final Vector2f vector, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f();
        Vector2f cosSin = new Vector2f(vector);
        final float length = cosSin.length();
        if (length > 0.0f) {
            cosSin.scale(1.0f / length);
            result.m00 = cosSin.x;
            result.m10 = -cosSin.y;
            result.m01 = cosSin.y;
            result.m11 = cosSin.x;
        } else result.setIdentity();
        return result;
    }

    public static Matrix2f createSimpleRotateMatrix(final Vector2f from, final Vector2f to, @Nullable Matrix2f result) {
        return createSimpleRotateMatrix(Vector2f.sub(to, from, new Vector2f()), result);
    }

    public static Matrix2f createSimpleRadiansRotateMatrix(float angRad, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f();
        result.m00 = result.m11 = (float) Math.cos(angRad);
        result.m01 = TrigUtil.sinFormCosRadiansF(result.m00, angRad);
        result.m10 = -result.m01;
        return result;
    }

    public static Matrix2f createSimpleRotateMatrix(float angle, @Nullable Matrix2f result) {
        return createSimpleRadiansRotateMatrix((float) Math.toRadians(angle), result);
    }

    public static Matrix2f inverseRotate(final Matrix2f rotate, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f(rotate); else result.load(rotate);
        result.m10 = -result.m10;
        result.m01 = -result.m01;
        return result;
    }

    public static Matrix2f inverseRotate(final Matrix2f rotate) {
        rotate.m10 = -rotate.m10;
        rotate.m01 = -rotate.m01;
        return rotate;
    }

    /**
     * Translate then rotate.
     */
    public static Vector2f pointTranslateRadiansRotate(final Vector2f point, float angRad, final Vector2f offset, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        Matrix2f.transform(createSimpleRadiansRotateMatrix(angRad, new Matrix2f()), Vector2f.add(point, offset, result), result);
        return result;
    }

    /**
     * Translate then rotate.
     */
    public static Vector2f pointTranslateRotate(final Vector2f point, float angle, final Vector2f offset, @Nullable Vector2f result) {
        return pointTranslateRadiansRotate(point, (float) Math.toRadians(angle), offset, result);
    }

    /**
     * Rotate then translate.
     */
    public static Vector2f pointRadiansRotateTranslate(final Vector2f point, float angRad, final Vector2f offset, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        Vector2f.add(Matrix2f.transform(createSimpleRadiansRotateMatrix(angRad, new Matrix2f()), point, result), offset, result);
        return result;
    }

    /**
     * Rotate then translate.
     */
    public static Vector2f pointRotateTranslate(final Vector2f point, float angle, final Vector2f offset, @Nullable Vector2f result) {
        return pointRadiansRotateTranslate(point, (float) Math.toRadians(angle), offset, result);
    }

    public static Matrix4f createModelMatrixLocationOnly(final Vector3f location, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixLocationOnly(final Vector3f location, final int writePosition, @NotNull final FloatBuffer result) {
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        result.put(writePosition + 14, location.z);
        return result;
    }

    public static Matrix4f createModelMatrixLocationOnly(final Vector2f location, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixLocationOnly(final Vector2f location, final int writePosition, @NotNull final FloatBuffer result) {
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    /**
     * Better than {@link Matrix4f#rotate(float, Vector3f, Matrix4f)}.
     */
    public static Matrix4f createModelMatrixRotateOnly(final Quaternion rotate, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.m00 = 1.0f - (q11 + q22);
        result.m01 = q01 + q23;
        result.m02 = q02 - q13;
        result.m10 = q01 - q23;
        result.m11 = 1.0f - (q22 + q00);
        result.m12 = q12 + q03;
        result.m20 = q02 + q13;
        result.m21 = q12 - q03;
        result.m22 = 1.0f - (q11 + q00);
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixRotateOnly(final Quaternion rotate, final int writePosition, @NotNull final FloatBuffer result) {
        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.put(writePosition, 1.0f - (q11 + q22));
        result.put(writePosition + 1, q01 + q23);
        result.put(writePosition + 2, q02 - q13);
        result.put(writePosition + 4, q01 - q23);
        result.put(writePosition + 5, 1.0f - (q22 + q00));
        result.put(writePosition + 6, q12 + q03);
        result.put(writePosition + 8, q02 + q13);
        result.put(writePosition + 9, q12 - q03);
        result.put(writePosition + 10, 1.0f - (q11 + q00));
        return result;
    }

    public static Matrix4f createModelMatrixScaleOnly(final Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m00 = scale.x;
        result.m11 = scale.y;
        result.m22 = scale.z;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixScaleOnly(final Vector3f scale, final int writePosition, @NotNull final FloatBuffer result) {
        result.put(writePosition, scale.x);
        result.put(writePosition + 5, scale.y);
        result.put(writePosition + 10, scale.z);
        return result;
    }

    public static Matrix4f createModelMatrixLocationScale(final Vector3f location, final Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        result.m00 = scale.x;
        result.m11 = scale.y;
        result.m22 = scale.z;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixLocationScale(final Vector3f location, final Vector3f scale, final int writePosition, @NotNull final FloatBuffer result) {
        result.put(writePosition, scale.x);
        result.put(writePosition + 5, scale.y);
        result.put(writePosition + 10, scale.z);
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        result.put(writePosition + 14, location.z);
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(final Vector3f location, float facing, final Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.m00 = scale.x - q22 * scale.x;
        result.m01 = q23 * scale.x;
        result.m10 = -q23 * scale.y;
        result.m11 = scale.y - q22 * scale.y;
        result.m22 = scale.z;
        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector3f location, float facing, final Vector3f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.put(writePosition, scale.x - q22 * scale.x);
        result.put(writePosition + 1, q23 * scale.x);
        result.put(writePosition + 4, -q23 * scale.y);
        result.put(writePosition + 5, scale.y - q22 * scale.y);
        result.put(writePosition + 10, scale.z);
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        result.put(writePosition + 14, location.z);
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(final Vector2f location, final Quaternion rotate, final Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.m00 = scale.x - (q11 + q22) * scale.x;
        result.m01 = (q01 + q23) * scale.x;
        result.m02 = (q02 - q13) * scale.x;
        result.m10 = (q01 - q23) * scale.y;
        result.m11 = scale.y - (q22 + q00) * scale.y;
        result.m12 = (q12 + q03) * scale.y;
        result.m20 = (q02 + q13);
        result.m21 = (q12 - q03);
        result.m22 = 1.0f - (q11 + q00);
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector2f location, final Quaternion rotate, final Vector2f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.put(writePosition, scale.x - (q11 + q22) * scale.x);
        result.put(writePosition + 1, (q01 + q23) * scale.x);
        result.put(writePosition + 2, (q02 - q13) * scale.x);

        result.put(writePosition + 4, (q01 - q23) * scale.y);
        result.put(writePosition + 5, scale.y - (q22 + q00) * scale.y);
        result.put(writePosition + 6, (q12 + q03) * scale.y);

        result.put(writePosition + 8, q02 + q13);
        result.put(writePosition + 9, q12 - q03);
        result.put(writePosition + 10, 1.0f - (q11 + q00));

        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    /**
     * With default override scale value <code>1.0f</code>.
     */
    public static Matrix4f createModelMatrixVanilla(final Vector2f location, final Quaternion rotate, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.m00 = 1.0f - (q11 + q22);
        result.m01 = (q01 + q23);
        result.m02 = (q02 - q13);
        result.m10 = (q01 - q23);
        result.m11 = 1.0f - (q22 + q00);
        result.m12 = (q12 + q03);
        result.m20 = (q02 + q13);
        result.m21 = (q12 - q03);
        result.m22 = 1.0f - (q11 + q00);
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.<p>
     * With default override scale value <code>1.0f</code>.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector2f location, final Quaternion rotate, final int writePosition, @NotNull final FloatBuffer result) {
        final float dqx = rotate.x + rotate.x,
                dqy = rotate.y + rotate.y,
                dqz = rotate.z + rotate.z,
                q00 = dqx * rotate.x,
                q11 = dqy * rotate.y,
                q22 = dqz * rotate.z,
                q01 = dqx * rotate.y,
                q02 = dqx * rotate.z,
                q03 = dqx * rotate.w,
                q12 = dqy * rotate.z,
                q13 = dqy * rotate.w,
                q23 = dqz * rotate.w;

        result.put(writePosition, 1.0f - (q11 + q22));
        result.put(writePosition + 1, q01 + q23);
        result.put(writePosition + 2, q02 - q13);

        result.put(writePosition + 4, q01 - q23);
        result.put(writePosition + 5, 1.0f - (q22 + q00));
        result.put(writePosition + 6, q12 + q03);

        result.put(writePosition + 8, q02 + q13);
        result.put(writePosition + 9, q12 - q03);
        result.put(writePosition + 10, 1.0f - (q11 + q00));

        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(final Vector2f location, float facing, final Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.m00 = scale.x - q22 * scale.x;
        result.m01 = q23 * scale.x;
        result.m10 = -q23 * scale.y;
        result.m11 = scale.y - q22 * scale.y;
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector2f location, float facing, final Vector2f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.put(writePosition, scale.x - q22 * scale.x);
        result.put(writePosition + 1, q23 * scale.x);
        result.put(writePosition + 4, -q23 * scale.y);
        result.put(writePosition + 5, scale.y - q22 * scale.y);
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    /**
     * With default override scale value <code>1.0f</code>.
     */
    public static Matrix4f createModelMatrixVanilla(final Vector2f location, float facing, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.m00 = 1.0f - q22;
        result.m01 = q23;
        result.m10 = -q23;
        result.m11 = 1.0f - q22;
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.<p>
     * With default override scale value <code>1.0f</code>.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector2f location, float facing, final int writePosition, @NotNull final FloatBuffer result) {
        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.put(writePosition, 1.0f - q22);
        result.put(writePosition + 1, q23);
        result.put(writePosition + 4, -q23);
        result.put(writePosition + 5, 1.0f - q22);
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(float facing, final Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.m00 = scale.x - q22 * scale.x;
        result.m01 = q23 * scale.x;
        result.m10 = -q23 * scale.y;
        result.m11 = scale.y - q22 * scale.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(float facing, final Vector2f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final float angle = facing * 0.5f,
                w = (float) Math.cos(Math.toRadians(angle)),
                z = TrigUtil.sinFormCosF(w, angle),
                dqz = z + z,
                q22 = dqz * z,
                q23 = dqz * w;

        result.put(writePosition, scale.x - q22 * scale.x);
        result.put(writePosition + 1, q23 * scale.x);
        result.put(writePosition + 4, -q23 * scale.y);
        result.put(writePosition + 5, scale.y - q22 * scale.y);
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(final Vector2f location, final Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        result.m00 = scale.x;
        result.m11 = scale.y;
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrixVanilla(final Vector2f location, final Vector2f scale, final int writePosition, @NotNull final FloatBuffer result) {
        result.put(writePosition, scale.x);
        result.put(writePosition + 5, scale.y);
        result.put(writePosition + 12, location.x);
        result.put(writePosition + 13, location.y);
        return result;
    }

    /**
     * @param facing without rotate when {@link Float#NaN}.
     * @param scale hava default value <code>{1.0f, 1.0f}</code> if this parameter is null but <code>facing</code> not a NaN.
     */
    public static Matrix3f createModelMatrix3f(@Nullable final Vector2f location, float facing, @Nullable final Vector2f scale, @Nullable Matrix3f result) {
        if (result == null) result = new Matrix3f();
        final boolean notRotate = Float.isNaN(facing), notScale = scale == null;
        if (location == null && notRotate && notScale) return result; // ?

        if (!notScale) {
            result.m00 = scale.x;
            result.m11 = scale.y;
        }

        if (!notRotate) {
            final float angle = facing * 0.5f,
                    w = (float) Math.cos(Math.toRadians(angle)),
                    z = TrigUtil.sinFormCosF(w, angle),
                    dqz = z + z,
                    q22 = dqz * z,
                    q23 = dqz * w;

            if (notScale) {
                result.m00 = 1.0f;
                result.m11 = 1.0f;
            }

            result.m01 = q23 * result.m00;
            result.m00 -= q22 * result.m00;

            result.m10 = -q23 * result.m11;
            result.m11 -= q22 * result.m11;
        }

        if (location != null) {
            result.m20 = location.x;
            result.m21 = location.y;
        }
        return result;
    }

    /**
     * NOTE: rename, go use {@link TransformUtil#createModelMatrix3f(Vector2f, float, Vector2f, Matrix3f)}.
     *
     * @param facing without rotate when {@link Float#NaN}.
     * @param scale hava default value <code>{1.0f, 1.0f}</code> if this parameter is null but <code>facing</code> not a NaN.
     */
    @Deprecated
    public static Matrix3f createModelMatrixVanilla3f(@Nullable final Vector2f location, float facing, @Nullable final Vector2f scale, @Nullable Matrix3f result) {
        return createModelMatrix3f(location, facing, scale, result);
    }

    @Deprecated
    public static Matrix3f createModelMatrixVanilla3f(@Nullable final Vector2f location, float facing, @Nullable Matrix3f result) {
        return createModelMatrix3f(location, facing, null, result);
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param facing without rotate when {@link Float#NaN}.
     * @param scale hava default value <code>{1.0f, 1.0f}</code> if this parameter is null but <code>facing</code> not a NaN.
     * @param result the writable size must be 9 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrix3f(@Nullable final Vector2f location, float facing, @Nullable final Vector2f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final boolean withLocation = location != null, withRotate = !Float.isNaN(facing), withScale = scale != null, rotateOrScale = withRotate || withScale;
        if (!(withLocation || rotateOrScale)) return result; // ?

        float m00 = 1.0f, m11 = 1.0f;

        if (withScale) {
            m00 = scale.x;
            m11 = scale.y;
        }

        if (withRotate) {
            final float angle = facing * 0.5f,
                    w = (float) Math.cos(Math.toRadians(angle)),
                    z = TrigUtil.sinFormCosF(w, angle),
                    dqz = z + z,
                    q22 = dqz * z,
                    q23 = dqz * w;

            result.put(writePosition + 1, q23 * m00);
            result.put(writePosition + 3, -q23 * m11);

            m00 -= q22 * m00;
            m11 -= q22 * m11;
        }

        if (rotateOrScale) {
            result.put(writePosition, m00);
            result.put(writePosition + 4, m11);
        }

        if (withLocation) {
            result.put(writePosition + 6, location.x);
            result.put(writePosition + 7, location.y);
        }
        return result;
    }

    @Deprecated
    public static float[] createModelMatrix(final Vector3f location, final Vector3f rotate, final Vector3f scale) {
        return createModelMatrix(new float[]{location.x, location.y, location.z, rotate.x, rotate.y, rotate.z, scale.x, scale.y, scale.z});
    }

    @Deprecated
    public static float[] createModelMatrix(float[] state) {
        float[] matrix = new float[16];
        final float pitchHalf = state[3] * 0.5f,
                cp = (float) Math.cos(Math.toRadians(pitchHalf)),
                sp = TrigUtil.sinFormCosF(cp, pitchHalf);

        float sr, cr;
        if (state[4] == state[3]) {
            cr = cp;
            sr = sp;
        } else {
            float rollHalf = state[4] * 0.5f;
            cr = (float) Math.cos(Math.toRadians(rollHalf));
            sr = TrigUtil.sinFormCosF(cr, rollHalf);
        }

        float sy, cy;
        if (state[5] == state[3]) {
            cy = cp;
            sy = sp;
        } else if (state[5] == state[4]) {
            cy = cr;
            sy = sr;
        } else {
            float yawHalf = state[5] * 0.5f;
            cy = (float) Math.cos(Math.toRadians(yawHalf));
            sy = TrigUtil.sinFormCosF(cy, yawHalf);
        }

        final float w = cp * cr * cy - sp * sr * sy,
                x = sp * cr * cy - cp * sr * sy,
                y = cp * sr * cy + sp * cr * sy,
                z = cp * cr * sy + sp * sr * cy;

        final float dqx = x + x,
                dqy = y + y,
                dqz = z + z,
                q00 = dqx * x,
                q11 = dqy * y,
                q22 = dqz * z,
                q01 = dqx * y,
                q02 = dqx * z,
                q03 = dqx * w,
                q12 = dqy * z,
                q13 = dqy * w,
                q23 = dqz * w;
        matrix[0] = state[6] - (q11 + q22) * state[6];
        matrix[1] = (q01 + q23) * state[6];
        matrix[2] = (q02 - q13) * state[6];
        matrix[4] = (q01 - q23) * state[7];
        matrix[5] = state[7] - (q22 + q00) * state[7];
        matrix[6] = (q12 + q03) * state[7];
        matrix[8] = (q02 + q13) * state[8];
        matrix[9] = (q12 - q03) * state[8];
        matrix[10] = state[8] - (q11 + q00) * state[8];
        matrix[12] = state[0];
        matrix[13] = state[1];
        matrix[14] = state[2];
        matrix[15] = 1.0f;
        return matrix;
    }

    /**
     * @param scale hava default value <code>{1.0f, 1.0f, 1.0f}</code> if this parameter is null but <code>rotate</code> not null.
     */
    public static Matrix4f createModelMatrix(@Nullable final Vector3f location, @Nullable final Quaternion rotate, @Nullable final Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final boolean notRotate = rotate == null, notScale = scale == null;
        if (location == null && notRotate && notScale) return result; // ?

        if (!notScale) {
            result.m00 = scale.x;
            result.m11 = scale.y;
            result.m22 = scale.z;
        }

        if (!notRotate) {
            final float dqx = rotate.x + rotate.x,
                    dqy = rotate.y + rotate.y,
                    dqz = rotate.z + rotate.z,
                    q00 = dqx * rotate.x,
                    q11 = dqy * rotate.y,
                    q22 = dqz * rotate.z,
                    q01 = dqx * rotate.y,
                    q02 = dqx * rotate.z,
                    q03 = dqx * rotate.w,
                    q12 = dqy * rotate.z,
                    q13 = dqy * rotate.w,
                    q23 = dqz * rotate.w;

            if (notScale) {
                result.m00 = 1.0f;
                result.m11 = 1.0f;
                result.m22 = 1.0f;
            }

            result.m01 = (q01 + q23) * result.m00;
            result.m02 = (q02 - q13) * result.m00;
            result.m00 -= (q11 + q22) * result.m00;

            result.m10 = (q01 - q23) * result.m11;
            result.m12 = (q12 + q03) * result.m11;
            result.m11 -= (q22 + q00) * result.m11;

            result.m20 = (q02 + q13) * result.m22;
            result.m21 = (q12 - q03) * result.m22;
            result.m22 -= (q11 + q00) * result.m22;
        }

        if (location != null) {
            result.m30 = location.x;
            result.m31 = location.y;
            result.m32 = location.z;
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it, also will not erase buffer.
     *
     * @param scale hava default value <code>{1.0f, 1.0f, 1.0f}</code> if this parameter is null but <code>rotate</code> not null.
     * @param result the writable size must be 16 at least, and recommended to create it from an <b><i>identity matrix</i></b>.
     */
    public static FloatBuffer createModelMatrix(@Nullable final Vector3f location, @Nullable final Quaternion rotate, @Nullable final Vector3f scale, final int writePosition, @NotNull final FloatBuffer result) {
        final boolean withLocation = location != null, withRotate = rotate != null, withScale = scale != null, rotateOrScale = withRotate || withScale;
        if (!(withLocation || rotateOrScale)) return result; // ?

        float m00 = 1.0f, m11 = 1.0f, m22 = 1.0f;

        if (withScale) {
            m00 = scale.x;
            m11 = scale.y;
            m22 = scale.z;
        }

        if (withRotate) {
            final float dqx = rotate.x + rotate.x,
                    dqy = rotate.y + rotate.y,
                    dqz = rotate.z + rotate.z,
                    q00 = dqx * rotate.x,
                    q11 = dqy * rotate.y,
                    q22 = dqz * rotate.z,
                    q01 = dqx * rotate.y,
                    q02 = dqx * rotate.z,
                    q03 = dqx * rotate.w,
                    q12 = dqy * rotate.z,
                    q13 = dqy * rotate.w,
                    q23 = dqz * rotate.w;

            result.put(writePosition + 1, (q01 + q23) * m00);
            result.put(writePosition + 2, (q02 - q13) * m00);

            result.put(writePosition + 4, (q01 - q23) * m11);
            result.put(writePosition + 6, (q12 + q03) * m11);

            result.put(writePosition + 9, (q02 + q13) * m22);
            result.put(writePosition + 11, (q12 - q03) * m22);

            m00 -= (q11 + q22) * m00;
            m11 -= (q22 + q00) * m11;
            m22 -= (q11 + q00) * m22;
        }

        if (rotateOrScale) {
            result.put(writePosition, m00);
            result.put(writePosition + 5, m11);
            result.put(writePosition + 10, m22);
        }

        if (withLocation) {
            result.put(writePosition + 12, location.x);
            result.put(writePosition + 13, location.y);
            result.put(writePosition + 14, location.z);
        }
        return result;
    }

    public static Vector2f getWorldLocationAtViewportUV(final Vector2f location, final ViewportAPI viewport, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        result.set((location.x - viewport.getLLX()) / viewport.getVisibleWidth(), (location.y - viewport.getLLY()) / viewport.getVisibleHeight());
        return result;
    }

    public static Vector2f getWorldLocationAtViewportUV(final Vector2f location, final ViewportAPI viewport) {
        return getWorldLocationAtViewportUV(location, viewport, new Vector2f());
    }

    public static Matrix4f createGameOrthoMatrix(final ViewportAPI viewport, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final var center = viewport.getCenter();
        result.m00 = 2.0f / viewport.getVisibleWidth();
        result.m11 = 2.0f / viewport.getVisibleHeight();
        result.m22 = -1.0f;
        result.m30 = result.m00 * (-center.x);
        result.m31 = result.m11 * (-center.y);
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createGameOrthoMatrix(final ViewportAPI viewport, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        final var center = viewport.getCenter();
        final float m00 = 2.0f / viewport.getVisibleWidth(), m11 = 2.0f / viewport.getVisibleHeight();
        result.put(writePosition, m00);
        result.put(writePosition + 5, m11);
        result.put(writePosition + 10, -1.0f); // m22
        result.put(writePosition + 12, m00 * (-center.x)); // m30
        result.put(writePosition + 13, m11 * (-center.y)); // m31
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 11, 0.0f);
            result.put(writePosition + 14, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createGameOrthoMatrix(final ViewportAPI viewport, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createGameOrthoMatrix(viewport, 0, eraseAndWrite, result);
    }

    public static FloatBuffer createGameOrthoMatrix(final ViewportAPI viewport) {
        return createGameOrthoMatrix(viewport, false, CommonUtil.createIdentityMatrix4x4f());
    }

    public static Matrix4f createGamePerspectiveMatrix(float fovAngle, final ViewportAPI viewport, @Nullable Matrix4f result) {
        if (fovAngle == 0.0f) throw new IllegalArgumentException("If you really want to use '0.0f' for fov angle, just make an <b><i>identity matrix</i></b>.");
        if (result == null) result = new Matrix4f();
        final float width = viewport.getVisibleWidth(), height = viewport.getVisibleHeight(),
                rad = (float) Math.toRadians(fovAngle * 0.5f),
                sin = (float) Math.sin(rad),
                cos = TrigUtil.cosFormSinRadiansF(sin, rad),
                tanInv = cos / sin;

        result.m00 = height / width * tanInv; // x

        result.m11 = tanInv;  // y

        result.m22 = 0.0f;

        result.m23 = -1.0f;  // w, not scale
        result.m30 = result.m00 * (-viewport.getCenter().x);
        result.m31 = result.m11 * (-viewport.getCenter().y);
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createGamePerspectiveMatrix(float fovAngle, final ViewportAPI viewport, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        if (fovAngle == 0.0f) throw new IllegalArgumentException("If you really want to use '0.0f' for fov angle, just make an <b><i>identity matrix</i></b>.");
        final var center = viewport.getCenter();
        final float tanInv = 1.0f / (float) Math.tan(Math.toRadians(fovAngle * 0.5f)),
                m00 = viewport.getVisibleHeight() / viewport.getVisibleWidth() * tanInv;

        result.put(writePosition, m00);
        result.put(writePosition + 5, tanInv); // m11
        result.put(writePosition + 10, 0.0f); // m22
        result.put(writePosition + 11, -1.0f); // m23
        result.put(writePosition + 12, m00 * (-center.x)); // m30
        result.put(writePosition + 13, tanInv * (-center.y)); // m31
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 14, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createGamePerspectiveMatrix(float fovAngle, final ViewportAPI viewport, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createGamePerspectiveMatrix(fovAngle, viewport, 0, eraseAndWrite, result);
    }

    public static FloatBuffer createGamePerspectiveMatrix(float fovAngle, final ViewportAPI viewport) {
        return createGamePerspectiveMatrix(fovAngle, viewport, false, CommonUtil.createIdentityMatrix4x4f());
    }

    public static Matrix4f createWindowCenterOrthoMatrix(@Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m00 = 2.0f / ShaderCore.getScreenWidth();
        result.m11 = 2.0f / ShaderCore.getScreenHeight();
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createWindowCenterOrthoMatrix(int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        result.put(writePosition, 2.0f / ShaderCore.getScreenWidth());
        result.put(writePosition + 5, 2.0f / ShaderCore.getScreenHeight()); // m11
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 10, 1.0f);
            result.put(writePosition + 11, 0.0f);
            result.put(writePosition + 12, 0.0f);
            result.put(writePosition + 13, 0.0f);
            result.put(writePosition + 14, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createWindowCenterOrthoMatrix(final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createWindowCenterOrthoMatrix(0, eraseAndWrite, result);
    }

    public static FloatBuffer createWindowCenterOrthoMatrix() {
        return createWindowCenterOrthoMatrix(false, CommonUtil.createIdentityMatrix4x4f());
    }

    public static Matrix4f createWindowOrthoMatrix(@Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m00 = 2.0f / ShaderCore.getScreenWidth();
        result.m11 = 2.0f / ShaderCore.getScreenHeight();
        result.m30 = -1.0f;
        result.m31 = -1.0f;
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createWindowOrthoMatrix(final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        result.put(writePosition, 2.0f / ShaderCore.getScreenWidth());
        result.put(writePosition + 5, 2.0f / ShaderCore.getScreenHeight()); // m11
        result.put(writePosition + 12, -1.0f); // m30
        result.put(writePosition + 13, -1.0f); // m31
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 10, 1.0f);
            result.put(writePosition + 11, 0.0f);
            result.put(writePosition + 14, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createWindowOrthoMatrix(final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createWindowOrthoMatrix(0, eraseAndWrite, result);
    }

    public static FloatBuffer createWindowOrthoMatrix() {
        return createWindowOrthoMatrix(false, CommonUtil.createIdentityMatrix4x4f());
    }

    public static Matrix4f createOrthoMatrix(float left, float right, float bottom, float top, float zNear, float zFar, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m00 = 2.0f / (right - left);
        result.m11 = 2.0f / (top - bottom);
        result.m22 = 1.0f / (zFar - zNear);
        result.m30 = - (right + left) / (right - left);
        result.m31 = - (top + bottom) / (top - bottom);
        result.m32 = - zNear / (zFar - zNear);
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createOrthoMatrix(float left, float right, float bottom, float top, float zNear, float zFar, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        result.put(writePosition, 2.0f / (right - left));
        result.put(writePosition + 5, 2.0f / (top - bottom)); // m11
        result.put(writePosition + 10, 1.0f / (zFar - zNear)); // m22
        result.put(writePosition + 12, - (right + left) / (right - left)); // m30
        result.put(writePosition + 13, - (top + bottom) / (top - bottom)); // m31
        result.put(writePosition + 14, - zNear / (zFar - zNear)); // m32
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 11, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createOrthoMatrix(float left, float right, float bottom, float top, float zNear, float zFar, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createOrthoMatrix(left, right, bottom, top, zNear, zFar, 0, eraseAndWrite, result);
    }

    public static FloatBuffer createOrthoMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        return createOrthoMatrix(left, right, bottom, top, zNear, zFar, false, CommonUtil.createIdentityMatrix4x4f());
    }

    public static Matrix4f createLookAtMatrix3D(final Vector3f camera, final Vector3f target, final Vector3f up, boolean offsetFromTarget, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final Vector3f tmp = new Vector3f();
        Vector3f.sub(target, camera, tmp).normalise(tmp); // tmp => cameraDirection
        // put negative
        result.m02 = -tmp.x;
        result.m12 = -tmp.y;
        result.m22 = -tmp.z;
        Vector3f.cross(tmp, up, tmp).normalise(tmp); // tmp => cameraRoll
        result.m00 = tmp.x;
        result.m10 = tmp.y;
        result.m20 = tmp.z;
        // cameraRoll X cameraDirection = cameraUp
        // un-negative
        result.m01 = result.m20 * result.m12 - result.m10 * result.m22;
        result.m11 = result.m22 * result.m00 - result.m02 * result.m20;
        result.m21 = result.m10 * result.m02 - result.m00 * result.m12;

        result.m30 = -(result.m00 * camera.x + result.m10 * camera.y + result.m20 * camera.z);
        result.m31 = -(result.m01 * camera.x + result.m11 * camera.y + result.m21 * camera.z);
        result.m32 = -(result.m02 * camera.x + result.m12 * camera.y + result.m22 * camera.z); // un-negative
        if (offsetFromTarget) {
            result.m30 += target.x;
            result.m31 += target.y;
            result.m32 += target.z;
        }
        return result;
    }

    public static Matrix4f createLookAtMatrixFlat(final Vector2f camera, final Vector2f target, final Vector2f up, boolean offsetFromTarget, @Nullable Matrix4f result) {
        return createLookAtMatrix3D(new Vector3f(camera.x, camera.y, 1.0f), new Vector3f(target.x, target.y, 0.0f), new Vector3f(up.x, up.y, 0.0f), offsetFromTarget, result);
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createLookAtMatrix3D(final Vector3f camera, final Vector3f target, final Vector3f up, boolean offsetFromTarget, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        final Vector3f tmp = new Vector3f();
        float m00, m10, m20, m01, m11, m21, m02, m12, m22, m30, m31, m32;
        Vector3f.sub(target, camera, tmp).normalise(tmp); // tmp => cameraDirection
        // put negative
        m02 = -tmp.x;
        m12 = -tmp.y;
        m22 = -tmp.z;
        Vector3f.cross(tmp, up, tmp).normalise(tmp); // tmp => cameraRoll
        m00 = tmp.x;
        m10 = tmp.y;
        m20 = tmp.z;
        // cameraRoll X cameraDirection = cameraUp
        // un-negative
        m01 = m20 * m12 - m10 * m22;
        m11 = m22 * m00 - m02 * m20;
        m21 = m10 * m02 - m00 * m12;

        m30 = -(m00 * camera.x + m10 * camera.y + m20 * camera.z);
        m31 = -(m01 * camera.x + m11 * camera.y + m21 * camera.z);
        m32 = -(m02 * camera.x + m12 * camera.y + m22 * camera.z); // un-negative
        if (offsetFromTarget) {
            m30 += target.x;
            m31 += target.y;
            m32 += target.z;
        }

        result.put(writePosition, m00);
        result.put(writePosition + 1, m01);
        result.put(writePosition + 2, m02);
        result.put(writePosition + 4, m10);
        result.put(writePosition + 5, m11);
        result.put(writePosition + 6, m12);
        result.put(writePosition + 8, m20);
        result.put(writePosition + 9, m21);
        result.put(writePosition + 10, m22);
        result.put(writePosition + 12, m30);
        result.put(writePosition + 13, m31);
        result.put(writePosition + 14, m32);
        if (eraseAndWrite) {
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 11, 0.0f);
            result.put(writePosition + 15, 1.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createLookAtMatrix3D(final Vector3f camera, final Vector3f target, final Vector3f up, boolean offsetFromTarget, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createLookAtMatrix3D(camera, target, up, offsetFromTarget, 0, eraseAndWrite, result);
    }

    public static FloatBuffer createLookAtMatrix3D(final Vector3f camera, final Vector3f target, final Vector3f up, boolean offsetFromTarget) {
        return createLookAtMatrix3D(camera, target, up, offsetFromTarget, false, CommonUtil.createIdentityMatrix4x4f());
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createLookAtMatrixFlat(final Vector2f camera, final Vector2f target, final Vector2f up, boolean offsetFromTarget, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createLookAtMatrix3D(new Vector3f(camera.x, camera.y, 1.0f), new Vector3f(target.x, target.y, 0.0f), new Vector3f(up.x, up.y, 0.0f), offsetFromTarget, writePosition, eraseAndWrite, result);
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createLookAtMatrixFlat(final Vector2f camera, final Vector2f target, final Vector2f up, boolean offsetFromTarget, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createLookAtMatrixFlat(camera, target, up, offsetFromTarget, 0, eraseAndWrite, result);
    }

    public static FloatBuffer createLookAtMatrixFlat(final Vector2f camera, final Vector2f target, final Vector2f up, boolean offsetFromTarget) {
        return createLookAtMatrixFlat(camera, target, up, offsetFromTarget, false, CommonUtil.createIdentityMatrix4x4f());
    }

    /**
     * @param aspect width / height
     */
    public static Matrix4f createPerspectiveMatrix3D(float fovAngle, float aspect, float zNear, float zFar, @Nullable Matrix4f result) {
        if (fovAngle == 0.0f) throw new IllegalArgumentException("If you really want to use '0.0f' for fov angle, just make an <b><i>identity matrix</i></b>.");
        if (aspect == 0.0f) throw new IllegalArgumentException("Aspect cannot be zero.");
        if (result == null) result = new Matrix4f();
        final float tanInv = 1.0f / (float) Math.tan(Math.toRadians(fovAngle * 0.5f)),
                FN = zFar - zNear;

        result.m00 = tanInv / aspect; // x

        result.m11 = tanInv;  // y

        result.m22 = -(zFar + zNear) / FN;  // z
        result.m32 = -(2.0f * zFar * zNear) / FN;

        result.m23 = -1.0f;  // w
        result.m33 = 0.0f;  // reset 1 to 0
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param aspect width / height
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createPerspectiveMatrix3D(float fovAngle, float aspect, float zNear, float zFar, final int writePosition, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        if (fovAngle == 0.0f) throw new IllegalArgumentException("If you really want to use '0.0f' for fov angle, just make an <b><i>identity matrix</i></b>.");
        if (aspect == 0.0f) throw new IllegalArgumentException("Aspect cannot be zero.");
        final float tanInv = 1.0f / (float) Math.tan(Math.toRadians(fovAngle * 0.5f)),
                FN = zFar - zNear;

        result.put(writePosition, tanInv / aspect);
        result.put(writePosition + 5, tanInv); // m11
        result.put(writePosition + 10, -(zFar + zNear) / FN); // m22
        result.put(writePosition + 11, -1.0f); // m23
        result.put(writePosition + 14, -(2.0f * zFar * zNear) / FN); // m32
        result.put(writePosition + 15, 0.0f); // m33
        if (eraseAndWrite) {
            result.put(writePosition + 1, 0.0f);
            result.put(writePosition + 2, 0.0f);
            result.put(writePosition + 3, 0.0f);
            result.put(writePosition + 4, 0.0f);
            result.put(writePosition + 6, 0.0f);
            result.put(writePosition + 7, 0.0f);
            result.put(writePosition + 8, 0.0f);
            result.put(writePosition + 9, 0.0f);
            result.put(writePosition + 12, 0.0f);
            result.put(writePosition + 13, 0.0f);
        }
        return result;
    }

    /**
     * Use for directly write into buffer, and will not change the position of it.
     *
     * @param aspect width / height
     * @param result the writable size must be 16 at least.
     */
    public static FloatBuffer createPerspectiveMatrix3D(float fovAngle, float aspect, float zNear, float zFar, final boolean eraseAndWrite, @NotNull final FloatBuffer result) {
        return createPerspectiveMatrix3D(fovAngle, aspect, zNear, zFar, 0, eraseAndWrite, result);
    }

    /**
     * @param aspect width / height
     */
    public static FloatBuffer createPerspectiveMatrix3D(float fovAngle, float aspect, float zNear, float zFar) {
        return createPerspectiveMatrix3D(fovAngle, aspect, zNear, zFar, false, CommonUtil.createIdentityMatrix4x4f());
    }

    private TransformUtil() {}
}
