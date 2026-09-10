package org.boxutil.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.define.GLWrapper;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.TrigUtil;
import org.boxutil.util.container.Obj2ObjRHMap;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public final class TextureManager {
    public final static String ALIGN_FORWARD_SUFFIX = "_RF";

    private static volatile int[][] _INTERNAL_FORMAT = null;
    private static int[][] _FORMAT = null;

    private final static Map<String, Integer> _PATH_TEX = new Obj2ObjRHMap<>(64);
    private final static Map<Integer, Integer> _AUTO_NORMAL = new Obj2ObjRHMap<>(64);
    public final static ShaderUtil.NormalMapGenParam DEFAULT_AUTO_NORMAL_PARAM = new ShaderUtil.NormalMapGenParam();

    private final static Logger _LOG = Global.getLogger(TextureManager.class);

    public static boolean haveTexture(@NotNull final String file) {
        return _PATH_TEX.containsKey(file);
    }

    public static int getTexture(@NotNull final String file) {
        return _PATH_TEX.get(file);
    }

    public static int putTexture(@NotNull final String file, int texture) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        if (texture < 1) return 0;
        Integer result = _PATH_TEX.put(file, texture);
        return result == null ? 0 : result;
    }

    public static int deleteTexture(@NotNull final String file) {
        GLWrapper.Texture.glDeleteTextures(getTexture(file));
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
        GLWrapper.Texture.glDeleteTextures(getAutoGenNormal(srcTexture));
        return _AUTO_NORMAL.remove(srcTexture);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTexture(@NotNull final String file, int channelNum, boolean is1DTexture, boolean uintTexture, boolean useTextureStorage, boolean nearestSampler, boolean potAligned) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        if (_INTERNAL_FORMAT == null) {
            synchronized (TextureManager.class) {
                if (_INTERNAL_FORMAT == null) {
                    _INTERNAL_FORMAT = new int[][]{
                            new int[]{GLWrapper.Texture.valid_TexInt() ? GLWrapper.Texture.GL_R8 : GLWrapper.Texture.GL_INTENSITY8, GLWrapper.Texture.GL_RG8, GLWrapper.Texture.GL_RGB8, GLWrapper.Texture.GL_RGBA8},
                            new int[]{GLWrapper.Texture.GL_R8UI, GLWrapper.Texture.GL_RG8UI, GLWrapper.Texture.GL_RGB8UI, GLWrapper.Texture.GL_RGBA8UI}
                    };
                    _FORMAT = new int[][]{
                            new int[]{GLWrapper.Texture.valid_TexInt() ? GLWrapper.Texture.GL_RED : GLWrapper.Texture.GL_INTENSITY, GLWrapper.Texture.GL_RG, GLWrapper.Texture.GL_RGB, GLWrapper.Texture.GL_RGBA},
                            new int[]{GLWrapper.Texture.GL_RED_INTEGER, GLWrapper.Texture.GL_RG_INTEGER, GLWrapper.Texture.GL_RGB_INTEGER, GLWrapper.Texture.GL_RGBA_INTEGER}
                    };
                }
            }
        }

        if ((channelNum == 2 || uintTexture) && !GLWrapper.Texture.valid_TexInt()) return new int[12];

        final byte clampChannel = (byte) Math.max(Math.min(channelNum, 4), 1);
        final boolean isPOT = potAligned || !GLWrapper.Texture.valid_NPOT();
        final boolean texStorage = useTextureStorage && GLWrapper.Texture.valid_TexStorage();
        final Pair<int[], ByteBuffer> rawData = CommonUtil.getRawPixels(file, clampChannel);
        final int[] result = new int[12];
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
            final int target = is1DTexture ? GLWrapper.Texture.GL_TEXTURE_1D : GLWrapper.Texture.GL_TEXTURE_2D;
            final int sampler = nearestSampler ? GLWrapper.Texture.GL_NEAREST : GLWrapper.Texture.GL_LINEAR;
            final byte typePicker = (byte) (uintTexture ? 1 : 0);
            final byte channelPicker = (byte) (clampChannel - 1);

            result[0] = GLWrapper.Texture.glGenTextures();
            GLWrapper.Texture.glBindTexture(target, result[0]);
            if (is1DTexture) {
                if (texStorage) GLWrapper.Texture.glTexStorage1D(GLWrapper.Texture.GL_TEXTURE_1D, 1, _INTERNAL_FORMAT[typePicker][channelPicker], result[2]);
                else GLWrapper.Texture.glTexImage1D(GLWrapper.Texture.GL_TEXTURE_1D, 0, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], 0, _FORMAT[typePicker][channelPicker], GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GLWrapper.Texture.glTexSubImage1D(GLWrapper.Texture.GL_TEXTURE_1D, 0, 0, result[4], _FORMAT[typePicker][channelPicker], GLWrapper.DataType.GL_UNSIGNED_BYTE, rawData.two);
            } else {
                if (texStorage) GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], result[3]);
                else GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, _INTERNAL_FORMAT[typePicker][channelPicker], result[2], result[3], 0, _FORMAT[typePicker][channelPicker], GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GLWrapper.Texture.glTexSubImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, 0, 0, result[4], result[5], _FORMAT[typePicker][channelPicker], GLWrapper.DataType.GL_UNSIGNED_BYTE, rawData.two);
            }
            GLWrapper.Texture.glTexParameteri(target, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, sampler);
            GLWrapper.Texture.glTexParameteri(target, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, sampler);
            GLWrapper.Texture.glBindTexture(target, 0);
            _LOG.info("'BoxUtil' OpenGL texture loading finished: '" + file + "' with ID: " + result[0]);
        }
        putTexture(file, result[0]);
        return result;
    }

    /**
     * @param alignForward <code>true</code> rotate the image from facing upward to facing right, i.e. rotate it in -90 angle,
     *                     that force it point to positive x-axis rather than positive y-axis.<p>
     *                     Also, if set to <code>true</code>, will store the texture as key: <code>&lt;pathAndName&gt;{@link TextureManager#ALIGN_FORWARD_SUFFIX}.&lt;format&gt;</code>
     *
     * @return int[] = {textureID, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTextureRGBA8(@NotNull final String file, boolean alignForward, boolean is1DTexture, boolean useTextureStorage, boolean nearestSampler, boolean potAligned) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        final boolean isPOT = potAligned || !GLWrapper.Texture.valid_NPOT();
        final boolean texStorage = useTextureStorage && GLWrapper.Texture.valid_TexStorage();
        final Pair<int[], ByteBuffer> rawData = CommonUtil.getRawPixels(file, 4, alignForward);
        final int[] result = new int[11];
        result[1] = isPOT ? CalculateUtil.getPOTMax(rawData.one[0]) : rawData.one[0];
        result[2] = isPOT ? CalculateUtil.getPOTMax(rawData.one[1]) : rawData.one[1];
        result[3] = rawData.one[0];
        result[4] = rawData.one[1];
        result[5] = rawData.one[4];
        result[6] = rawData.one[5];
        result[7] = rawData.one[6];
        result[8] = rawData.one[7];
        result[9] = rawData.one[8];
        result[10] = rawData.one[9];
        if (rawData.two != null) {
            final int target = is1DTexture ? GLWrapper.Texture.GL_TEXTURE_1D : GLWrapper.Texture.GL_TEXTURE_2D;
            final int sampler = nearestSampler ? GLWrapper.Texture.GL_NEAREST : GLWrapper.Texture.GL_LINEAR;

            result[0] = GLWrapper.Texture.glGenTextures();
            GLWrapper.Texture.glBindTexture(target, result[0]);
            if (is1DTexture) {
                if (texStorage) GLWrapper.Texture.glTexStorage1D(GLWrapper.Texture.GL_TEXTURE_1D, 1, GLWrapper.Texture.GL_RGBA8, result[2]);
                else GLWrapper.Texture.glTexImage1D(GLWrapper.Texture.GL_TEXTURE_1D, 0, GLWrapper.Texture.GL_RGBA8, result[2], 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GLWrapper.Texture.glTexSubImage1D(GLWrapper.Texture.GL_TEXTURE_1D, 0, 0, result[4], GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, rawData.two);
            } else {
                if (texStorage) GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, GLWrapper.Texture.GL_RGBA8, result[2], result[3]);
                else GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_RGBA8, result[2], result[3], 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GLWrapper.Texture.glTexSubImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, 0, 0, result[4], result[5], GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, rawData.two);
            }
            GLWrapper.Texture.glTexParameteri(target, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, sampler);
            GLWrapper.Texture.glTexParameteri(target, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, sampler);
            GLWrapper.Texture.glBindTexture(target, 0);
            _LOG.info("'BoxUtil' OpenGL texture loading finished: '" + file + "'" + (alignForward ? "(aligned forward)" : "") + " with ID: " + result[0]);
        }
        putTexture(file, result[0]);
        return result;
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTexture(@NotNull final String file) {
        return loadTexture(file, 4, false, false, true, false, true);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize), ivec3(averageColor), ivec3(averageBrightColor)}
     */
    public static int[] loadTextureChannel3(@NotNull final String file) {
        return loadTexture(file, 3, false, false, true, false, true);
    }

    /**
     * @return int[] = {textureID, pixelPreImage, ivec2(size), ivec2(localSize)}
     */
    public static int[] loadTangentMap(@NotNull final String file, boolean isAngleMap, boolean useTextureStorage, boolean potAligned) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        final byte clampChannel = (byte) (isAngleMap ? 1 : 3);
        final boolean isPOT = potAligned || !GLWrapper.Texture.valid_NPOT();
        final boolean texStorage = useTextureStorage && GLWrapper.Texture.valid_TexStorage();
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

            result[0] = GLWrapper.Texture.glGenTextures();
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, result[0]);
            if (texStorage) GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, GLWrapper.Texture.GL_RGB8, result[2], result[3]);
            else GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_RGB8, result[2], result[3], 0, GLWrapper.Texture.GL_RGB, GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            GLWrapper.Texture.glTexSubImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, 0, 0, result[4], result[5], GLWrapper.Texture.GL_RGB, GLWrapper.DataType.GL_UNSIGNED_BYTE, putBuffer);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
            _LOG.info("'BoxUtil' OpenGL tangent texture loading finished: '" + file + "' with ID: " + result[0]);
        }
        putTexture(file, result[0]);
        return result;
    }

    public static int tryTexture(@NotNull final String file, int channelNum, boolean is1DTexture, boolean uintTexture, boolean useTextureStorage, boolean nearestSampler, boolean potAligned) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTexture(file, channelNum, is1DTexture, uintTexture, useTextureStorage, nearestSampler, potAligned)[0];
    }

    public static int tryTexture(@NotNull final String file, boolean alignForward) {
        final String realFile = BUtil_MiscUtil.fetchFilePostfix(file, alignForward ? ALIGN_FORWARD_SUFFIX : null);
        return haveTexture(realFile) ? _PATH_TEX.get(realFile) : loadTextureRGBA8(realFile, alignForward, false, true, false, true)[0];
    }

    public static int tryTexture(@NotNull final String file) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTexture(file)[0];
    }

    public static int tryTextureChannel3(@NotNull final String file) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTextureChannel3(file)[0];
    }

    public static int tryTangent(@NotNull final String file, boolean isAngleMap, boolean useTextureStorage, boolean potAligned) {
        return haveTexture(file) ? _PATH_TEX.get(file) : loadTangentMap(file, isAngleMap, useTextureStorage, potAligned)[0];
    }

    private static int genNormalForTextureCore(int srcTexture, int width, int height, boolean keyCheck, ShaderUtil.NormalMapGenParam param, boolean findSize) {
        if (keyCheck && haveAutoGenNormal(srcTexture)) return getAutoGenNormal(srcTexture);
        if (findSize) {
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, srcTexture);
            width = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_WIDTH);
            height = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_HEIGHT);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
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
