package org.boxutil.util;

/**
 * All angle value must be <code>[0, 360]</code> or <code>[0, 2π]</code>.<p>
 * For avoid some calculation of trigonometric functions.
 */
public final class TrigUtil {
    public final static float PI_F = (float) Math.PI;
    public final static float PI2_F = PI_F + PI_F;
    public final static float PI_HALF_F = PI_F * 0.5f;
    public final static double RAD_270D = Math.toRadians(270.0d);
    public final static float RAD_270F = (float) RAD_270D;
    public final static double RAD_180D = Math.toRadians(180.0d);
    public final static float RAD_180F = (float) RAD_180D;
    public final static double RAD_90D = Math.toRadians(90.0d);
    public final static float RAD_90F = (float) RAD_90D;

    public static double sinFormCosD(double cosValue, double angle) {
        double out = Math.sqrt(1.0d - (cosValue * cosValue));
        if (angle > 180.0d) out = -out;
        return out;
    }

    public static double sinFormCosRadiansD(double cosValue, double angRad) {
        double out = Math.sqrt(1.0d - (cosValue * cosValue));
        if (angRad > RAD_180D) out = -out;
        return out;
    }

    public static float sinFormCosF(float cosValue, float angle) {
        float out = (float) Math.sqrt(1.0f - (cosValue * cosValue));
        if (angle > 180.0f) out = -out;
        return out;
    }

    public static float sinFormCosRadiansF(float cosValue, float angRad) {
        float out = (float) Math.sqrt(1.0f - (cosValue * cosValue));
        if (angRad > RAD_180F) out = -out;
        return out;
    }

    public static double sinFormTanD(double tanValue, double angle) {
        if (angle == 90.0d) return 1.0d;
        if (angle == 270.0d) return -1.0d;
        double out = tanValue / Math.sqrt(1.0d + (tanValue * tanValue));
        if (angle > 90.0d && angle < 270.0d) out = -out;
        return out;
    }

    public static double sinFormTanRadiansD(double tanValue, double angRad) {
        if (angRad == RAD_90D) return 1.0d;
        if (angRad == RAD_270D) return -1.0d;
        double out = tanValue / Math.sqrt(1.0d + (tanValue * tanValue));
        if (angRad > RAD_90D && angRad < RAD_270D) out = -out;
        return out;
    }

    public static float sinFormTanF(float tanValue, float angle) {
        if (angle == 90.0f) return 1.0f;
        if (angle == 270.0f) return -1.0f;
        float out = tanValue / (float) Math.sqrt(1.0f + (tanValue * tanValue));
        if (angle > 90.0f && angle < 270.0f) out = -out;
        return out;
    }

    public static float sinFormTanRadiansF(float tanValue, float angRad) {
        if (angRad == RAD_90F) return 1.0f;
        if (angRad == RAD_270F) return -1.0f;
        float out = tanValue / (float) Math.sqrt(1.0f + (tanValue * tanValue));
        if (angRad > RAD_90F && angRad < RAD_270F) out = -out;
        return out;
    }

    public static double cosFormSinD(double sinValue, double angle) {
        double out = Math.sqrt(1.0d - (sinValue * sinValue));
        if (angle > 90.0d && angle < 270.0d) out = -out;
        return out;
    }

    public static double cosFormSinRadiansD(double sinValue, double angRad) {
        double out = Math.sqrt(1.0d - (sinValue * sinValue));
        if (angRad > RAD_90D && angRad < RAD_270D) out = -out;
        return out;
    }

    public static float cosFormSinF(float sinValue, float angle) {
        float out = (float) Math.sqrt(1.0f - (sinValue * sinValue));
        if (angle > 90.0f && angle < 270.0f) out = -out;
        return out;
    }

    public static float cosFormSinRadiansF(float sinValue, float angRad) {
        float out = (float) Math.sqrt(1.0f - (sinValue * sinValue));
        if (angRad > RAD_90F && angRad < RAD_270F) out = -out;
        return out;
    }

    public static double cosFormTanD(double tanValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        double out = 1.0d / Math.sqrt(1.0d + (tanValue * tanValue));
        if (angle > 90.0d && angle < 270.0d) out = -out;
        return out;
    }

    public static double cosFormTanRadiansD(double tanValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        double out = 1.0d / Math.sqrt(1.0d + (tanValue * tanValue));
        if (angRad > RAD_90D && angRad < RAD_270D) out = -out;
        return out;
    }

    public static float cosFormTanF(float tanValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        float out = 1.0f / (float) Math.sqrt(1.0f + (tanValue * tanValue));
        if (angle > 90.0f && angle < 270.0f) out = -out;
        return out;
    }

    public static float cosFormTanRadiansF(float tanValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        float out = 1.0f / (float) Math.sqrt(1.0f + (tanValue * tanValue));
        if (angRad > RAD_90F && angRad < RAD_270F) out = -out;
        return out;
    }


    public static double tanFormSinD(double sinValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        double out = sinValue / Math.sqrt(1.0d - (sinValue * sinValue));
        if (angle > 90.0d && angle < 270.0d) out = -out;
        return out;
    }

    public static double tanFormSinRadiansD(double sinValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        double out = sinValue / Math.sqrt(1.0d - (sinValue * sinValue));
        if (angRad > RAD_90D && angRad < RAD_270D) out = -out;
        return out;
    }

    public static float tanFormSinF(float sinValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        float out = sinValue / (float) Math.sqrt(1.0f - (sinValue * sinValue));
        if (angle > 90.0f && angle < 270.0f) out = -out;
        return out;
    }

    public static float tanFormSinRadiansF(float sinValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        float out = sinValue / (float) Math.sqrt(1.0f - (sinValue * sinValue));
        if (angRad > RAD_90F && angRad < RAD_270F) out = -out;
        return out;
    }

    public static double tanFormCosD(double cosValue, double angle) {
        if (angle == 90.0d || angle == 270.0d) return 0.0d;
        double out = Math.sqrt(1.0d - (cosValue * cosValue)) / cosValue;
        if (angle > 180.0d) out = -out;
        return out;
    }

    public static double tanFormCosRadiansD(double cosValue, double angRad) {
        if (angRad == RAD_90D || angRad == RAD_270D) return 0.0d;
        double out = Math.sqrt(1.0d - (cosValue * cosValue)) / cosValue;
        if (angRad > RAD_180D) out = -out;
        return out;
    }

    public static float tanFormCosF(float cosValue, float angle) {
        if (angle == 90.0f || angle == 270.0f) return 0.0f;
        float out = (float) Math.sqrt(1.0f - (cosValue * cosValue)) / cosValue;
        if (angle > 180.0f) out = -out;
        return out;
    }

    public static float tanFormCosRadiansF(float cosValue, float angRad) {
        if (angRad == RAD_90F || angRad == RAD_270F) return 0.0f;
        float out = (float) Math.sqrt(1.0f - (cosValue * cosValue)) / cosValue;
        if (angRad > RAD_180F) out = -out;
        return out;
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
