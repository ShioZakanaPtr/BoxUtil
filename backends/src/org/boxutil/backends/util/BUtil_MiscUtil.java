package org.boxutil.backends.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.manager.TextureManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class BUtil_MiscUtil {
    public static String fetchFilePostfix(final String file, final String suffix) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        if (suffix == null) return file;

        final int dotIdx = file.lastIndexOf('.');
        if (dotIdx == -1) throw new IllegalArgumentException("Illegal file path: format error");

        return file.substring(0, dotIdx) + suffix + file.substring(dotIdx);
    }

    public static String getMemoryNumStr(long size) {
        if (size < 1) return "    0 Byte";
        final boolean isByte = size < 1024;
        final String[] unit = new String[]{"Byte", " KiB", " MiB", " GiB", " TiB", " PiB", " EiB"};

        byte pick = 0;
        byte decimal = 0;
        double result = size;
        if (!isByte) {
            result = Math.log10(size);
            pick = (byte) (result / 3.010299956639812d);
            result = (pick < 4) ? (1 << (pick * 10)) : Math.pow(1024.0d, pick);
            result = (double) size / result;
            if (result >= 1.0d) decimal = (byte) (4 - (byte) Math.log10(result));
        }

        String resultStr;
        if (isByte) {
            resultStr = String.format("%5s", size) + ' ';
        } else {
            String formatStr = "%." + Math.max(decimal, 0) + "f";
            resultStr = String.format(formatStr, result);
        }
        return resultStr + unit[pick];
    }

    public static String getPercentageStr(double div) {
        if (Double.isNaN(div)) return " - %";
        double result = div * 100.0d;
        byte decimal = 2;
        if (result > 1.0d) decimal = (byte) (2 - (byte) Math.log10(result));
        final String formatStr = "%4." + Math.max(decimal, 0) + 'f';
        return String.format(formatStr, result) + '%';
    }

    public static boolean csvSkipAnnotationID(final String str) {
        return str.startsWith("#");
    }

    public static void getColorArray(final String colorArray, byte[] array) {
        if (colorArray.length() < 9) return;
        byte index = 0;
        for (String str : colorArray.substring(1, colorArray.length() - 1).split(",")) {
            array[index] = Byte.parseByte(str);
            ++index;
        }
    }

    public static void getVec4(String vecArray, Vector4f vec) {
        if (vecArray.length() < 9) return;
        final float[] vecC = new float[4];
        byte count = 0;
        for (String str : vecArray.substring(1, vecArray.length() - 1).split(",")) {
            vecC[count] = Float.parseFloat(str);
            ++count;
            if (count > 3) break;
        }
        vec.x = vecC[0];
        vec.y = vecC[1];
        vec.z = vecC[2];
        vec.w = vecC[3];
    }

    public static void getVec4Color(String colorArray, Vector4f color) {
        getVec4(colorArray, color);
        color.scale(1.0f / 255.0f);
    }

    public static void getVec2(String vecArray, Vector2f vec) {
        if (vecArray.length() < 9) return;
        final float[] vecC = new float[2];
        byte count = 0;
        for (String str : vecArray.substring(1, vecArray.length() - 1).split(",")) {
            vecC[count] = Float.parseFloat(str);
            ++count;
            if (count > 1) break;
        }
        vec.x = vecC[0];
        vec.y = vecC[1];
    }

    public static int tryTexture(final String path, boolean alignForward, final BiFunction<String, Boolean, Integer> customLoad) {
        if (alignForward) return customLoad.apply(path, true);
        final SpriteAPI vanillaSprite = Global.getSettings().getSprite(path);
        return (vanillaSprite != null && vanillaSprite.getTextureId() > 0) ? vanillaSprite.getTextureId() : customLoad.apply(path, false);
    }

    public static int tryTexture(final String path, final Function<String, Integer> customLoad) {
        final SpriteAPI vanillaSprite = Global.getSettings().getSprite(path);
        return (vanillaSprite != null && vanillaSprite.getTextureId() > 0) ? vanillaSprite.getTextureId() : customLoad.apply(path);
    }

    public static int tryTangentTexture(final String path, final boolean isAngleMap, final boolean useTextureStorage, final boolean potAligned) {
        final SpriteAPI vanillaSprite = Global.getSettings().getSprite(path);
        return (vanillaSprite != null && vanillaSprite.getTextureId() > 0) ? vanillaSprite.getTextureId() : TextureManager.tryTangent(path, isAngleMap, useTextureStorage, potAligned);
    }

    private BUtil_MiscUtil() {}
}
