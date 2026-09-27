package org.boxutil.util;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

/**
 * All angle and radians value must be under <code>[0, 360]</code> or <code>[0, 2π]</code>.<p>
 * For avoid some calculation of trigonometric functions.
 */
@SuppressWarnings("UnusedReturnValue")
public final class TrigUtil {
    public final static float PI_F = (float) Math.PI;
    public final static float PI2_F = PI_F + PI_F;
    public final static float PI_HALF_F = PI_F * 0.5f;
    public final static double RAD_270D = Math.PI * 1.5d;
    public final static float RAD_270F = (float) RAD_270D;
    public final static double RAD_180D = Math.PI;
    public final static float RAD_180F = (float) RAD_180D;
    public final static double RAD_90D = PI_HALF_F;
    public final static float RAD_90F = (float) RAD_90D;

    /**
     * @param x must be under [0.0, 0.5pi]
     */
    public static double approxSinFitD(double x) {
        return Math.fma(x, Math.fma(x, Math.fma(x, Math.fma(x, 0.028713815256377853d, -0.20358090709887086d), 0.019965253315485060d), 0.99615005303669568d), 0.00012052567744297745d);
    }

    /**
     * @param x must be under [0.0, 0.5pi]
     */
    public static float approxSinFitF(float x) {
        return (float) approxSinFitD(x);
    }

    /**
     * @param x must be under [0.0, 0.5pi]
     */
    public static double approxCosFitD(double x) {
        return Math.fma(x, Math.fma(x, Math.fma(x, Math.fma(x, 0.028713815256378741d, 0.023166684966924631d), -0.51429617377432668d), 0.0029202661377637799d), 0.99990758164524929d);
    }

    /**
     * @param x must be under [0.0, 0.5pi]
     */
    public static float approxCosFitF(float x) {
        return (float) approxCosFitD(x);
    }

    public static double approxSinD(double radians) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(approxSinFitD(Math.abs(r)), r);
        return ((long) n) == 1 ? -sinR : sinR;
    }

    public static float approxSinF(float radians) {
        return (float) approxSinD(radians);
    }

    public static double approxCosD(double radians) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                cosR = approxCosFitD(Math.abs(r));
        return ((long) n) == 1 ? -cosR : cosR;
    }

    public static float approxCosF(float radians) {
        return (float) approxCosD(radians);
    }

    /**
     * @param ptr {cos, sin}
     */
    public static void approxCosSinD(double radians, final double[] ptr) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(approxSinFitD(Math.abs(r)), r),
                cosR = approxCosFitD(Math.abs(r));
        final boolean negative = ((long) n) == 1;
        ptr[0] = negative ? -cosR : cosR;
        ptr[1] = negative ? -sinR : sinR;
    }

    /**
     * @param ptr {cos, sin}
     */
    public static void approxCosSinF(float radians, final float[] ptr) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(approxSinFitD(Math.abs(r)), r),
                cosR = approxCosFitD(Math.abs(r));
        final boolean negative = ((long) n) == 1;
        ptr[0] = (float) (negative ? -cosR : cosR);
        ptr[1] = (float) (negative ? -sinR : sinR);
    }

    /**
     * @param ptr {cos, sin}
     */
    public static void approxCosSinF(float radians, final Vector2f ptr) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(approxSinFitD(Math.abs(r)), r),
                cosR = approxCosFitD(Math.abs(r));
        final boolean negative = ((long) n) == 1;
        ptr.x = (float) (negative ? -cosR : cosR);
        ptr.y = (float) (negative ? -sinR : sinR);
    }

    /**
     * @param ptr rotate matrix
     */
    public static void approxCosSinF(float radians, final Matrix2f ptr) {
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(approxSinFitD(Math.abs(r)), r),
                cosR = approxCosFitD(Math.abs(r));
        final boolean negative = ((long) n) == 1;
        ptr.m00 = ptr.m11 = (float) (negative ? -cosR : cosR);
        ptr.m01 = (float) (negative ? -sinR : sinR);
        ptr.m10 = -ptr.m01;
    }

    public static double sinFormCosD(double cosValue, double angle) {
        cosValue = Math.sqrt(1.0d - (cosValue * cosValue));
        return angle > 180.0d ? -cosValue : cosValue;
    }

    public static double sinFormCosRadiansD(double cosValue, double angRad) {
        cosValue = Math.sqrt(1.0d - (cosValue * cosValue));
        return angRad > Math.PI ? -cosValue : cosValue;
    }

    public static float sinFormCosF(float cosValue, float angle) {
        cosValue = (float) Math.sqrt(1.0d - (cosValue * cosValue));
        return angle > 180.0f ? -cosValue : cosValue;
    }

    public static float sinFormCosRadiansF(float cosValue, float angRad) {
        cosValue = (float) Math.sqrt(1.0d - (cosValue * cosValue));
        return angRad > RAD_180F ? -cosValue : cosValue;
    }

    public static double sinFormTanD(double tanValue, double angle) {
        if (angle == 90.0d) return 1.0d;
        if (angle == 270.0d) return -1.0d;
        tanValue /= Math.sqrt(1.0d + (tanValue * tanValue));
        return angle > 90.0d && angle < 270.0d ? -tanValue : tanValue;
    }

    public static double sinFormTanRadiansD(double tanValue, double angRad) {
        if (angRad == RAD_90D) return 1.0d;
        if (angRad == RAD_270D) return -1.0d;
        tanValue /= Math.sqrt(1.0d + (tanValue * tanValue));
        return angRad > RAD_90D && angRad < RAD_270D ? -tanValue : tanValue;
    }

    public static float sinFormTanF(float tanValue, float angle) {
        if (angle == 90.0f) return 1.0f;
        if (angle == 270.0f) return -1.0f;
        tanValue /= (float) Math.sqrt(1.0f + (tanValue * tanValue));
        return angle > 90.0f && angle < 270.0f ? -tanValue : tanValue;
    }

    public static float sinFormTanRadiansF(float tanValue, float angRad) {
        if (angRad == RAD_90F) return 1.0f;
        if (angRad == RAD_270F) return -1.0f;
        tanValue /= (float) Math.sqrt(1.0f + (tanValue * tanValue));
        return angRad > RAD_90F && angRad < RAD_270F ? -tanValue : tanValue;
    }

    public static double cosFormSinD(double sinValue, double angle) {
        sinValue = Math.sqrt(1.0d - (sinValue * sinValue));
        return angle > 90.0d && angle < 270.0d ? -sinValue : sinValue;
    }

    public static double cosFormSinRadiansD(double sinValue, double angRad) {
        sinValue = Math.sqrt(1.0d - (sinValue * sinValue));
        return angRad > RAD_90D && angRad < RAD_270D ? -sinValue : sinValue;
    }

    public static float cosFormSinF(float sinValue, float angle) {
        sinValue = (float) Math.sqrt(1.0f - (sinValue * sinValue));
        return angle > 90.0f && angle < 270.0f ? -sinValue : sinValue;
    }

    public static float cosFormSinRadiansF(float sinValue, float angRad) {
        sinValue = (float) Math.sqrt(1.0f - (sinValue * sinValue));
        return angRad > RAD_90F && angRad < RAD_270F ? -sinValue : sinValue;
    }

    public static double cosFormTanD(double tanValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        tanValue = 1.0d / Math.sqrt(1.0d + (tanValue * tanValue));
        return angle > 90.0d && angle < 270.0d ? -tanValue : tanValue;
    }

    public static double cosFormTanRadiansD(double tanValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        tanValue = 1.0d / Math.sqrt(1.0d + (tanValue * tanValue));
        return angRad > RAD_90D && angRad < RAD_270D ? -tanValue : tanValue;
    }

    public static float cosFormTanF(float tanValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        tanValue = 1.0f / (float) Math.sqrt(1.0d + (tanValue * tanValue));
        return angle > 90.0f && angle < 270.0f ? -tanValue : tanValue;
    }

    public static float cosFormTanRadiansF(float tanValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        tanValue = 1.0f / (float) Math.sqrt(1.0d + (tanValue * tanValue));
        return angRad > RAD_90F && angRad < RAD_270F ? -tanValue : tanValue;
    }


    public static double tanFormSinD(double sinValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        sinValue /= Math.sqrt(1.0d - (sinValue * sinValue));
        return angle > 90.0d && angle < 270.0d ? -sinValue : sinValue;
    }

    public static double tanFormSinRadiansD(double sinValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        sinValue /= Math.sqrt(1.0d - (sinValue * sinValue));
        return angRad > RAD_90D && angRad < RAD_270D ? -sinValue : sinValue;
    }

    public static float tanFormSinF(float sinValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        sinValue /= (float) Math.sqrt(1.0f - (sinValue * sinValue));
        return angle > 90.0f && angle < 270.0f ? -sinValue : sinValue;
    }

    public static float tanFormSinRadiansF(float sinValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        sinValue /= (float) Math.sqrt(1.0f - (sinValue * sinValue));
        return angRad > RAD_90F && angRad < RAD_270F ? -sinValue : sinValue;
    }

    public static double tanFormCosD(double cosValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        cosValue = Math.sqrt(1.0d - (cosValue * cosValue)) / cosValue;
        return angle > 180.0d ? -cosValue : cosValue;
    }

    public static double tanFormCosRadiansD(double cosValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        cosValue = Math.sqrt(1.0d - (cosValue * cosValue)) / cosValue;
        return angRad > RAD_180D ? -cosValue : cosValue;
    }

    public static float tanFormCosF(float cosValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        cosValue = (float) Math.sqrt(1.0f - (cosValue * cosValue)) / cosValue;
        return angle > 180.0f ? -cosValue : cosValue;
    }

    public static float tanFormCosRadiansF(float cosValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        cosValue = (float) Math.sqrt(1.0f - (cosValue * cosValue)) / cosValue;
        return angRad > RAD_180F ? -cosValue : cosValue;
    }

    public static double tanD(double sinValue, double cosValue) {
        if (cosValue == 0.0d) return sinValue > 0.0d ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        return sinValue / cosValue;
    }

    public static float tanF(float sinValue, float cosValue) {
        if (cosValue == 0.0f) return sinValue > 0.0f ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        return sinValue / cosValue;
    }

    private TrigUtil() {};
}
