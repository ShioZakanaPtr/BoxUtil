package org.boxutil.util;

import com.fs.starfarer.api.Global;
import org.boxutil.backends.reflect.BUtil_RefMethod;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public final class SerializationUtil {
    public static FloatBuffer loadFloatLUT(String file) {
        if (!BUtil_RefMethod.VALID) return null;

        FloatBuffer result = null;
        try (InputStream inputStream = Global.getSettings().openStream(file)) {
            Object binary = BUtil_RefMethod.java_io_DataInputStream__newDataInputStream(inputStream);
            int length = BUtil_RefMethod.java_io_DataInputStream_readInt(binary);
            if (length < 1) {
                Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + " failed: invalid file.");
            }
            result = BufferUtils.createFloatBuffer(length);
            for (int i = 0; i < length; ++i) {
                result.put(i, BUtil_RefMethod.java_io_DataInputStream_readFloat(binary));
            }
            result.position(0);
            result.limit(result.capacity());
        } catch (Throwable e) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + "' failed: " + e);
        }
        return result;
    }

    public static ShortBuffer loadFloatLUTConvertHalfFloat(String file) {
        if (!BUtil_RefMethod.VALID) return null;

        ShortBuffer result = null;
        try (InputStream inputStream = Global.getSettings().openStream(file)) {
            Object binary = BUtil_RefMethod.java_io_DataInputStream__newDataInputStream(inputStream);
            int length = BUtil_RefMethod.java_io_DataInputStream_readInt(binary);
            if (length < 1) {
                Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + " failed: invalid file.");
            }
            result = BufferUtils.createShortBuffer(length);
            for (int i = 0; i < length; ++i) {
                result.put(i, CommonUtil.float16ToShort(BUtil_RefMethod.java_io_DataInputStream_readFloat(binary)));
            }
            result.position(0);
            result.limit(result.capacity());
        } catch (Throwable e) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + "' failed: " + e);
        }
        return result;
    }

    public static ByteBuffer loadFloatLUTConvertNormalizedUnsignedByte(String file) {
        if (!BUtil_RefMethod.VALID) return null;

        ByteBuffer result = null;
        try (InputStream inputStream = Global.getSettings().openStream(file)) {
            Object binary = BUtil_RefMethod.java_io_DataInputStream__newDataInputStream(inputStream);
            int length = BUtil_RefMethod.java_io_DataInputStream_readInt(binary);
            if (length < 1) {
                Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + " failed: invalid file.");
            }
            result = BufferUtils.createByteBuffer(length);
            for (int i = 0; i < length; ++i) {
                result.put(i, CommonUtil.normalizedFloatToUnsignedByte(BUtil_RefMethod.java_io_DataInputStream_readFloat(binary)));
            }
            result.position(0);
            result.limit(result.capacity());
        } catch (Throwable e) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + "' failed: " + e);
        }
        return result;
    }

    public static ByteBuffer loadByteLUT(String file) {
        if (!BUtil_RefMethod.VALID) return null;

        ByteBuffer result = null;
        try (InputStream inputStream = Global.getSettings().openStream(file)) {
            Object binary = BUtil_RefMethod.java_io_DataInputStream__newDataInputStream(inputStream);
            int length = BUtil_RefMethod.java_io_DataInputStream_readInt(binary);
            if (length < 1) {
                Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + " failed: invalid file.");
            }
            result = BufferUtils.createByteBuffer(length);
            for (int i = 0; i < length; ++i) {
                result.put(i, BUtil_RefMethod.java_io_DataInputStream_readByte(binary));
            }
            result.position(0);
            result.limit(result.capacity());
        } catch (Throwable e) {
            Global.getLogger(CommonUtil.class).error("'BoxUtil' loading lut '" + file + "' failed: " + e);
        }
        return result;
    }

    private SerializationUtil() {}
}
