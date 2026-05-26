package org.boxutil.backends.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.manager.TextureManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.util.function.Function;

public final class BUtil_MiscUtil {
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
