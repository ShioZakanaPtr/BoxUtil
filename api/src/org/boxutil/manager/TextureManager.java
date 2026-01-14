package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.define.BoxDatabase;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.util.HashMap;

public final class TextureManager {
    private final static int[][] _INTERNAL_FORMAT = new int[][]{new int[]{GL30.GL_R8, GL30.GL_RG8, GL11.GL_RGB8, GL11.GL_RGBA8}, new int[]{GL30.GL_R8UI, GL30.GL_RG8UI, GL30.GL_RGB8UI, GL30.GL_RGBA8UI}};
    private final static int[][] _FORMAT = new int[][]{new int[]{GL11.GL_RED, GL30.GL_RG, GL11.GL_RGB, GL11.GL_RGBA}, new int[]{GL30.GL_RED_INTEGER, GL30.GL_RG_INTEGER, GL30.GL_RGB_INTEGER, GL30.GL_RGBA_INTEGER}};
    private final static HashMap<String, Integer> _PATH_TEX = new HashMap<>(64);
    private final static HashMap<Integer, Integer> _AUTO_NORMAL = new HashMap<>(64);
    public final static ShaderUtil.NormalMapGenParam DEFAULT_AUTO_NORMAL_PARAM = new ShaderUtil.NormalMapGenParam();

    public static boolean haveTexture(String file) {
        return _PATH_TEX.containsKey(file);
    }

    public static int getTexture(String file) {
        return _PATH_TEX.get(file);
    }

    public static int putTexture(String file, int texture) {
        if (file == null || file.isEmpty() || texture < 1) return 0;
        Integer result = _PATH_TEX.put(file, texture);
        return result == null ? 0 : result;
    }

    public static int deleteTexture(String file) {
        GL11.glDeleteTextures(getTexture(file));
        return _PATH_TEX.remove(file);
    }

    public static boolean haveAutoGenNormal(int srcTexture) {
        return _AUTO_NORMAL.containsKey(srcTexture);
    }

    public static int getAutoGenNormal(int srcTexture) {
        return _AUTO_NORMAL.get(srcTexture);
    }

    public static int putAutoGenNormal(int srcTexture, int normal) {
        if (srcTexture < 1 || normal < 1) return 0;
        Integer result = _AUTO_NORMAL.put(srcTexture, normal);
        return result == null ? 0 : result;
    }

    public static int deleteAutoGenNormal(int srcTexture) {
        GL11.glDeleteTextures(getAutoGenNormal(srcTexture));
        return _AUTO_NORMAL.remove(srcTexture);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTexture(String file, int channelNum, boolean is1DTexture, boolean uintTexture, boolean useTextureStorage, boolean nearestSampler, boolean potAligned) {
        final byte clampChannel = (byte) Math.max(Math.min(channelNum, 4), 1);
        final boolean isPOT = potAligned || !BoxDatabase.getGLState().GL_NPOT_TEXTURE;
        final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
        Pair<int[], ByteBuffer> rawData = CommonUtil.getRawPixels(file, clampChannel);
        int[] result = new int[12];
        result[1] = rawData.one[3];
        result[2] = isPOT ? CalculateUtil.getPOTMax(rawData.one[0]) : rawData.one[0];
        result[3] = isPOT ? CalculateUtil.getPOTMax(rawData.one[1]) : rawData.one[1];
        result[4] = rawData.one[0];
        result[5] = rawData.one[1];
        result[6] = rawData.one[4];
        result[7] = rawData.one[5];
        result[8] = rawData.one[6];
        result[9] = rawData.one[7];
        result[10] = rawData.one[8];
        result[11] = rawData.one[9];
        if (rawData.two != null) {
            final int target = is1DTexture ? GL11.GL_TEXTURE_1D : GL11.GL_TEXTURE_2D;
            final int sampler = nearestSampler ? GL11.GL_NEAREST : GL11.GL_LINEAR;
            final byte typePicker = (byte) (uintTexture ? 1 : 0);
            final byte channelPicker = (byte) (clampChannel - 1);

            result[0] = GL11.glGenTextures();
            GL11.glBindTexture(target, result[0]);
            if (is1DTexture) {
                if (texStorage) GL42.glTexStorage1D(GL11.GL_TEXTURE_1D, 1, _INTERNAL_FORMAT[typePicker][channelPicker], result[2]);
                else GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], 0, _FORMAT[typePicker][channelPicker], GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GL11.glTexSubImage1D(GL11.GL_TEXTURE_1D, 0, 0, result[4], _FORMAT[typePicker][channelPicker], GL11.GL_UNSIGNED_BYTE, rawData.two);
            } else {
                if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], result[3]);
                else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], result[3], 0, _FORMAT[typePicker][channelPicker], GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, result[4], result[5], _FORMAT[typePicker][channelPicker], GL11.GL_UNSIGNED_BYTE, rawData.two);
            }
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, sampler);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, sampler);
            GL11.glBindTexture(target, 0);
            Global.getLogger(TextureManager.class).info("'BoxUtil' OpenGL texture loading finished: '" + file + "' with ID: " + result[0]);
        }
        putTexture(file, result[0]);
        return result;
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTexture(String file) {
        return loadTexture(file, 4, false, false, true, false, true);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTextureChannel3(String file) {
        return loadTexture(file, 3, false, false, true, false, true);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize)}
     */
    public static int[] loadTangentMap(String file, boolean isAngleMap, boolean useTextureStorage, boolean potAligned) {
        final byte clampChannel = (byte) (isAngleMap ? 1 : 3);
        final boolean isPOT = potAligned || !BoxDatabase.getGLState().GL_NPOT_TEXTURE;
        final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
        Pair<int[], ByteBuffer> rawData = CommonUtil.getRawPixels(file, clampChannel);
        int[] result = new int[6];
        result[1] = isAngleMap ? rawData.one[3] * 3 : rawData.one[3];
        result[2] = isPOT ? CalculateUtil.getPOTMax(rawData.one[0]) : rawData.one[0];
        result[3] = isPOT ? CalculateUtil.getPOTMax(rawData.one[1]) : rawData.one[1];
        result[4] = rawData.one[0];
        result[5] = rawData.one[1];
        if (rawData.two != null) {
            ByteBuffer putBuffer;
            if (isAngleMap) {
                putBuffer = BufferUtils.createByteBuffer(rawData.one[0] * rawData.one[1] * 3);
                float c, s, rad;
                for (int i = 0; i < rawData.two.capacity(); ++i) {
                    rad = rawData.two.get(i) / 255.0f;
                    rad = rad * TrigUtil.PI2_F - TrigUtil.PI_F;
                    c = (float) Math.cos(rad);
                    s = TrigUtil.sinFormCosRadiansF(c, rad);
                    putBuffer.put((byte) Math.max(Math.min(Math.round((c * 0.5f + 0.5f) * 255.0f), 255), 0));
                    putBuffer.put((byte) Math.max(Math.min(Math.round((s * 0.5f + 0.5f) * 255.0f), 255), 0));
                    putBuffer.put(Byte.MAX_VALUE);
                }
                putBuffer.position(0);
                putBuffer.limit(putBuffer.capacity());
            } else putBuffer = rawData.two;

            result[0] = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result[0]);
            if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL11.GL_RGB8, result[2], result[3]);
            else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, result[2], result[3], 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, result[4], result[5], GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, putBuffer);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            Global.getLogger(TextureManager.class).info("'BoxUtil' OpenGL tangent texture loading finished: '" + file + "' with ID: " + result[0]);
        }
        putTexture(file, result[0]);
        return result;
    }

    public static int tryTexture(String file, int channelNum, boolean is1DTexture, boolean uintTexture, boolean useTextureStorage, boolean nearestSampler, boolean potAligned) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTexture(file, channelNum, is1DTexture, uintTexture, useTextureStorage, nearestSampler, potAligned)[0];
    }

    public static int tryTexture(String file) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTexture(file)[0];
    }

    public static int tryTextureChannel3(String file) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTextureChannel3(file)[0];
    }

    public static int tryTangent(String file, boolean isAngleMap, boolean useTextureStorage, boolean potAligned) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTangentMap(file, isAngleMap, useTextureStorage, potAligned)[0];
    }

    private static int genNormalForTextureCore(int srcTexture, int width, int height, boolean keyCheck, ShaderUtil.NormalMapGenParam param, boolean findSize) {
        if (keyCheck && haveAutoGenNormal(srcTexture)) return getAutoGenNormal(srcTexture);
        if (findSize) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, srcTexture);
            width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        int normal = ShaderUtil.genNormalMapFromRGB(srcTexture, width, height, param);
        putAutoGenNormal(srcTexture, normal);
        return normal;
    }

    public static int genNormalForTexture(int srcTexture, int width, int height, boolean keyCheck, ShaderUtil.NormalMapGenParam param) {
        return genNormalForTextureCore(srcTexture, width, height, keyCheck, param, false);
    }

    public static int genNormalForTexture(int srcTexture, boolean keyCheck, ShaderUtil.NormalMapGenParam param) {
        return genNormalForTextureCore(srcTexture, 0, 0, keyCheck, param, true);
    }

    public static int tryNormalForTexture(int srcTexture) {
        return haveAutoGenNormal(srcTexture) ? getAutoGenNormal(srcTexture) : genNormalForTexture(srcTexture, true, DEFAULT_AUTO_NORMAL_PARAM);
    }

    private TextureManager() {}
}
