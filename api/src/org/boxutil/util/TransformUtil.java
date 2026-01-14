package org.boxutil.util;

import com.fs.starfarer.api.combat.ViewportAPI;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.manager.ShaderCore;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

public final class TransformUtil {
    public static Matrix4f invertModelMatrix2D(Matrix4f src, Matrix4f result) {
        if (result == null) result = new Matrix4f();
        float det = src.m00 * src.m11 - src.m10 * src.m01;
        if (Math.abs(det) == 0.0f) {
            return null;
        }
        det = 1.0f / det;
        float k = src.m11 * src.m30 - src.m10 * src.m31, m = src.m00 * src.m31 - src.m01 * src.m30;

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
     * @return may with flopping errors.
     */
    public static Matrix4f invertModelMatrix3D(Matrix4f src, Matrix4f result) {
        if (result == null) result = new Matrix4f();
        Vector3f v = new Vector3f(src.m30, src.m31, src.m32);
        float det = src.m00 * (src.m11 * src.m22 - src.m21 * src.m12) - src.m10 * (src.m01 * src.m22 - src.m21 * src.m02) + src.m20 * (src.m01 * src.m12 - src.m11 * src.m02);
        if (Math.abs(det) == 0.0f) {
            return null;
        }
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
        result.m30 = -(result.m00 * v.x + result.m10 * v.y + result.m20 * v.z);
        result.m31 = -(result.m01 * v.x + result.m11 * v.y + result.m21 * v.z);
        result.m32 = -(result.m02 * v.x + result.m12 * v.y + result.m22 * v.z);

        result.m03 = result.m13 = result.m23 = 0.0f;
        result.m33 = 1.0f;

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

    public static Quaternion rotationFacingOnly(float yaw) {
        float yawHalf = yaw * 0.5f;
        float w = (float) Math.cos(Math.toRadians(yawHalf));
        return new Quaternion(0, 0, TrigUtil.sinFormCosF(w, yawHalf), w);
    }

    public static Quaternion rotationXOnly(float pitch) {
        float pitchHalf = pitch * 0.5f;
        float w = (float) Math.cos(Math.toRadians(pitchHalf));
        return new Quaternion(TrigUtil.sinFormCosF(w, pitchHalf), 0, 0, w);
    }

    public static Quaternion rotationYOnly(float roll) {
        float rollHalf = roll * 0.5f;
        float w = (float) Math.cos(Math.toRadians(rollHalf));
        return new Quaternion(0,  TrigUtil.sinFormCosF(w, rollHalf), 0, w);
    }

    public static Quaternion rotationZXY(Vector3f rotate) {
        return rotationZXY(rotate.z, rotate.x, rotate.y);
    }

    /**
     * ZXY order.
     *
     * @param yaw z's angle
     * @param pitch x's angle
     * @param roll y's angle
     */
    public static Quaternion rotationZXY(float yaw, float pitch, float roll) {
        float pitchHalf = pitch * 0.5f;
        float cp = (float) Math.cos((float) Math.toRadians(pitchHalf));
        float sp = TrigUtil.sinFormCosF(cp, pitchHalf);

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

        float w = cp * cr * cy - sp * sr * sy;
        float x = sp * cr * cy - cp * sr * sy;
        float y = cp * sr * cy + sp * cr * sy;
        float z = cp * cr * sy + sp * sr * cy;
        return new Quaternion(x, y, z, w);
    }

    public static Matrix2f createSimpleRotateMatrix(Vector2f vector, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f();
        Vector2f cosSin = new Vector2f(vector);
        float length = cosSin.length();
        if (length > 0.0f) {
            cosSin.scale(1.0f / length);
            result.m00 = cosSin.x;
            result.m10 = -cosSin.y;
            result.m01 = cosSin.y;
            result.m11 = cosSin.x;
        } else result.setIdentity();
        return result;
    }

    public static Matrix2f createSimpleRotateMatrix(Vector2f from, Vector2f to, @Nullable Matrix2f result) {
        return createSimpleRotateMatrix(Vector2f.sub(to, from, new Vector2f()), result);
    }

    public static Matrix2f createSimpleRadiansRotateMatrix(float angRad, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f();
        float c = (float) Math.cos(angRad);
        float s = TrigUtil.sinFormCosRadiansF(c, angRad);
        result.m00 = c;
        result.m10 = -s;
        result.m01 = s;
        result.m11 = c;
        return result;
    }

    public static Matrix2f createSimpleRotateMatrix(float angle, @Nullable Matrix2f result) {
        return createSimpleRadiansRotateMatrix((float) Math.toRadians(angle), result);
    }

    public static Matrix2f inverseRotate(Matrix2f rotate, @Nullable Matrix2f result) {
        if (result == null) result = new Matrix2f(rotate); else result.load(rotate);
        result.m10 = -result.m10;
        result.m01 = -result.m01;
        return result;
    }

    public static Matrix2f inverseRotate(Matrix2f rotate) {
        rotate.m10 = -rotate.m10;
        rotate.m01 = -rotate.m01;
        return rotate;
    }

    /**
     * Translate then rotate.
     */
    public static Vector2f pointTranslateRadiansRotate(Vector2f point, float angRad, Vector2f offset, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        Matrix2f.transform(createSimpleRadiansRotateMatrix(angRad, new Matrix2f()), Vector2f.add(point, offset, result), result);
        return result;
    }

    /**
     * Translate then rotate.
     */
    public static Vector2f pointTranslateRotate(Vector2f point, float angle, Vector2f offset, @Nullable Vector2f result) {
        return pointTranslateRadiansRotate(point, (float) Math.toRadians(angle), offset, result);
    }

    /**
     * Rotate then translate.
     */
    public static Vector2f pointRadiansRotateTranslate(Vector2f point, float angRad, Vector2f offset, @Nullable Vector2f result) {
        if (result == null) result = new Vector2f();
        Vector2f.add(Matrix2f.transform(createSimpleRadiansRotateMatrix(angRad, new Matrix2f()), point, result), offset, result);
        return result;
    }

    /**
     * Rotate then translate.
     */
    public static Vector2f pointRotateTranslate(Vector2f point, float angle, Vector2f offset, @Nullable Vector2f result) {
        return pointRadiansRotateTranslate(point, (float) Math.toRadians(angle), offset, result);
    }

    public static Matrix4f createModelMatrixLocationOnly(Vector3f location, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        return result;
    }

    public static Matrix4f createModelMatrixLocationOnly(Vector2f location, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    /**
     * Also Matrix4f.rotate();
     */
    public static Matrix4f createModelMatrixRotateOnly(Quaternion rotate, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float dqx = rotate.x + rotate.x;
        float dqy = rotate.y + rotate.y;
        float dqz = rotate.z + rotate.z;
        float q00 = dqx * rotate.x;
        float q11 = dqy * rotate.y;
        float q22 = dqz * rotate.z;
        float q01 = dqx * rotate.y;
        float q02 = dqx * rotate.z;
        float q03 = dqx * rotate.w;
        float q12 = dqy * rotate.z;
        float q13 = dqy * rotate.w;
        float q23 = dqz * rotate.w;
        result.m00 = 1.0f - (q11 + q22);
        result.m01 = (q01 + q23);
        result.m02 = (q02 - q13);
        result.m10 = (q01 - q23);
        result.m11 = 1.0f - (q22 + q00);
        result.m12 = (q12 + q03);
        result.m20 = (q02 + q13);
        result.m21 = (q12 - q03);
        result.m22 = 1.0f - (q11 + q00);
        return result;
    }

    public static Matrix4f createModelMatrixScaleOnly(Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m00 = scale.x;
        result.m11 = scale.y;
        result.m22 = scale.z;
        return result;
    }

    public static Matrix4f createModelMatrixLocationScale(Vector3f location, Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        result.m00 = scale.x;
        result.m11 = scale.y;
        result.m22 = scale.z;
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(Vector3f location, float facing, Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float angle = facing * 0.5f;
        float w = (float) Math.cos(Math.toRadians(angle));
        float z = TrigUtil.sinFormCosF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
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

    public static Matrix4f createModelMatrixVanilla(Vector2f location, Quaternion rotate, Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float dqx = rotate.x + rotate.x;
        float dqy = rotate.y + rotate.y;
        float dqz = rotate.z + rotate.z;
        float q00 = dqx * rotate.x;
        float q11 = dqy * rotate.y;
        float q22 = dqz * rotate.z;
        float q01 = dqx * rotate.y;
        float q02 = dqx * rotate.z;
        float q03 = dqx * rotate.w;
        float q12 = dqy * rotate.z;
        float q13 = dqy * rotate.w;
        float q23 = dqz * rotate.w;
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

    public static Matrix4f createModelMatrixVanilla(Vector2f location, Quaternion rotate, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float dqx = rotate.x + rotate.x;
        float dqy = rotate.y + rotate.y;
        float dqz = rotate.z + rotate.z;
        float q00 = dqx * rotate.x;
        float q11 = dqy * rotate.y;
        float q22 = dqz * rotate.z;
        float q01 = dqx * rotate.y;
        float q02 = dqx * rotate.z;
        float q03 = dqx * rotate.w;
        float q12 = dqy * rotate.z;
        float q13 = dqy * rotate.w;
        float q23 = dqz * rotate.w;
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

    public static Matrix4f createModelMatrixVanilla(Vector2f location, float facing, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float angle = facing * 0.5f;
        float w = (float) Math.cos(Math.toRadians(angle));
        float z = TrigUtil.sinFormCosF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
        result.m00 = 1.0f - q22;
        result.m01 = q23;
        result.m10 = -q23;
        result.m11 = 1.0f - q22;
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    public static Matrix3f createModelMatrixVanilla3f(Vector2f location, float facing, @Nullable Matrix3f result) {
        if (result == null) result = new Matrix3f();

        float angle = facing * 0.5f;
        float w = (float) Math.cos(Math.toRadians(angle));
        float z = TrigUtil.sinFormCosF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
        result.m00 = 1.0f - q22;
        result.m01 = q23;
        result.m10 = -q23;
        result.m11 = 1.0f - q22;
        result.m20 = location.x;
        result.m21 = location.y;
        return result;
    }

    public static Matrix4f createModelMatrixVanilla(Vector2f location, float facing, Vector2f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float angle = facing * 0.5f;
        float w = (float) Math.cos(Math.toRadians(angle));
        float z = TrigUtil.sinFormCosF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
        result.m00 = scale.x - q22 * scale.x;
        result.m01 = q23 * scale.x;
        result.m10 = -q23 * scale.y;
        result.m11 = scale.y - q22 * scale.y;
        result.m30 = location.x;
        result.m31 = location.y;
        return result;
    }

    public static Matrix3f createModelMatrixVanilla3f(Vector2f location, float facing, Vector2f scale, @Nullable Matrix3f result) {
        if (result == null) result = new Matrix3f();

        float angle = facing * 0.5f;
        float w = (float) Math.cos(Math.toRadians(angle));
        float z = TrigUtil.sinFormCosF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
        result.m00 = scale.x - q22 * scale.x;
        result.m01 = q23 * scale.x;
        result.m10 = -q23 * scale.y;
        result.m11 = scale.y - q22 * scale.y;
        result.m20 = location.x;
        result.m21 = location.y;
        return result;
    }

    public static float[] createModelMatrix(Vector3f location, Vector3f rotate, Vector3f scale) {
        return createModelMatrix(new float[]{location.x, location.y, location.z, rotate.x, rotate.y, rotate.z, scale.x, scale.y, scale.z});
    }

    public static float[] createModelMatrix(float[] state) {
        float[] matrix = new float[16];
        float pitchHalf = state[3] * 0.5f;
        float cp = (float) Math.cos(Math.toRadians(pitchHalf));
        float sp = TrigUtil.sinFormCosF(cp, pitchHalf);

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

        float w = cp * cr * cy - sp * sr * sy;
        float x = sp * cr * cy - cp * sr * sy;
        float y = cp * sr * cy + sp * cr * sy;
        float z = cp * cr * sy + sp * sr * cy;

        float dqx = x + x;
        float dqy = y + y;
        float dqz = z + z;
        float q00 = dqx * x;
        float q11 = dqy * y;
        float q22 = dqz * z;
        float q01 = dqx * y;
        float q02 = dqx * z;
        float q03 = dqx * w;
        float q12 = dqy * z;
        float q13 = dqy * w;
        float q23 = dqz * w;
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

    public static Matrix4f createModelMatrix(Vector3f location, Quaternion rotate, Vector3f scale, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();

        float dqx = rotate.x + rotate.x;
        float dqy = rotate.y + rotate.y;
        float dqz = rotate.z + rotate.z;
        float q00 = dqx * rotate.x;
        float q11 = dqy * rotate.y;
        float q22 = dqz * rotate.z;
        float q01 = dqx * rotate.y;
        float q02 = dqx * rotate.z;
        float q03 = dqx * rotate.w;
        float q12 = dqy * rotate.z;
        float q13 = dqy * rotate.w;
        float q23 = dqz * rotate.w;
        result.m00 = scale.x - (q11 + q22) * scale.x;
        result.m01 = (q01 + q23) * scale.x;
        result.m02 = (q02 - q13) * scale.x;
        result.m10 = (q01 - q23) * scale.y;
        result.m11 = scale.y - (q22 + q00) * scale.y;
        result.m12 = (q12 + q03) * scale.y;
        result.m20 = (q02 + q13) * scale.z;
        result.m21 = (q12 - q03) * scale.z;
        result.m22 = scale.z - (q11 + q00) * scale.z;
        result.m30 = location.x;
        result.m31 = location.y;
        result.m32 = location.z;
        return result;
    }

    public static Vector2f getWorldLocationAtViewportUV(Vector2f location, ViewportAPI viewport) {
        if (location == null) return new Vector2f(0.0f, 0.0f);
        else return new Vector2f((location.x - viewport.getLLX()) / viewport.getVisibleWidth(), (location.y - viewport.getLLY()) / viewport.getVisibleHeight());
    }

    public static Matrix4f createGameOrthoMatrix(ViewportAPI viewport, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final float width = ShaderCore.getScreenWidth();
        final float height = ShaderCore.getScreenHeight();
        float scale = viewport.getViewMult();
        result.m00 = 2.0f / width;
        result.m11 = 2.0f / height;
        result.m22 = -1.0f;
        result.m30 = result.m00 * (-viewport.getCenter().x / scale);
        result.m00 /= scale;
        result.m31 = result.m11 * (-viewport.getCenter().y / scale);
        result.m11 /= scale;
        return result;
    }

    /**
     * Unsatisfactory.
     */
    public static Matrix4f createGamePerspectiveMatrix(float fovAngle, ViewportAPI viewport, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        final float width = ShaderCore.getScreenWidth();
        final float height = ShaderCore.getScreenHeight();
        float scale = viewport.getViewMult();
        float rad = (float) Math.toRadians(fovAngle * 0.5f);
        float sin = (float) Math.sin(rad);
        if (sin == 0.0f) return result;
        float cos = TrigUtil.cosFormSinRadiansF(sin, rad);
        float tanInv = cos / sin;

        result.m00 = height / width * tanInv; // x

        result.m11 = tanInv;  // y

        result.m22 = 0.0f;

        result.m23 = -1.0f;  // w, not scale
        result.m30 = result.m00 * (-viewport.getCenter().x / scale);
        result.m00 /= scale;
        result.m31 = result.m11 * (-viewport.getCenter().y / scale);
        result.m11 /= scale;
        return result;
    }

    public static Matrix4f createWindowCenterOrthoMatrix(@Nullable Matrix4f result) {
        final float width = ShaderCore.getScreenWidth();
        final float height = ShaderCore.getScreenHeight();
        if (result == null) result = new Matrix4f();
        result.m00 = 2.0f / width;
        result.m11 = 2.0f / height;
        return result;
    }

    public static Matrix4f createWindowOrthoMatrix(@Nullable Matrix4f result) {
        final float width = ShaderCore.getScreenWidth();
        final float height = ShaderCore.getScreenHeight();
        if (result == null) result = new Matrix4f();
        result.m00 = 2.0f / width;
        result.m11 = 2.0f / height;
        result.m30 = -1.0f;
        result.m31 = -1.0f;
        return result;
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

    public static Matrix4f createLookAtMatrix3D(Vector3f camera, Vector3f target, Vector3f top, boolean offsetFromTarget, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        Vector3f cameraDirection = Vector3f.sub(target, camera, null);
        cameraDirection.normalise(cameraDirection);
        Vector3f cameraRoll = Vector3f.cross(cameraDirection, top, null);
        cameraRoll.normalise(cameraRoll);
        Vector3f cameraUp = Vector3f.cross(cameraRoll, cameraDirection, null);

        result.m00 = cameraRoll.x;
        result.m10 = cameraRoll.y;
        result.m20 = cameraRoll.z;
        result.m01 = cameraUp.x;
        result.m11 = cameraUp.y;
        result.m21 = cameraUp.z;
        result.m02 = -cameraDirection.x;
        result.m12 = -cameraDirection.y;
        result.m22 = -cameraDirection.z;
        result.m30 = -Vector3f.dot(cameraRoll, camera);
        result.m31 = -Vector3f.dot(cameraUp, camera);
        result.m32 = Vector3f.dot(cameraDirection, camera);
        if (offsetFromTarget) {
            result.m30 += target.x;
            result.m31 += target.y;
            result.m32 += target.z;
        }
        return result;
    }

    public static Matrix4f createLookAtMatrixFlat(Vector2f camera, Vector2f target, Vector2f up, boolean offsetFromTarget, @Nullable Matrix4f result) {
        return createLookAtMatrix3D(new Vector3f(camera.x, camera.y, 1.0f), new Vector3f(target.x, target.y, 0.0f), new Vector3f(up.x, up.y, 0.0f), offsetFromTarget, result);
    }

    /**
     * @param aspect width / height
     */
    public static Matrix4f createPerspectiveMatrix3D(float fovAngle, float aspect, float zNear, float zFar, @Nullable Matrix4f result) {
        if (result == null) result = new Matrix4f();
        if (aspect == 0.0f) return result;
        float angle = fovAngle * 0.5f;
        float sin = (float) Math.sin(Math.toRadians(angle));
        if (sin == 0.0f) return result;
        float cos = TrigUtil.cosFormSinF(sin, angle);
        float cot = cos / sin;
        float FN = zFar - zNear;

        result.m00 = 1.0f / (aspect * cot); // x

        result.m11 = 1.0f / cot;  // y

        result.m22 = -(zFar + zNear) / FN;  // z
        result.m32 = -(2.0f * zFar * zNear) / FN;

        result.m23 = -1.0f;  // w
        result.m33 = 0.0f;  // reset 1 to 0
        return result;
    }

    private TransformUtil() {}
}
