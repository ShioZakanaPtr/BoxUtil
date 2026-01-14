package org.boxutil.backends.reflect;

import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.NotNull;

import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class BUtil_RefMethod {
    private final static MethodHandle[] _METHOD_HANDLE = new MethodHandle[13];
    public final static boolean VALID;
    public final static String java_io_File_SEPARATOR;

    static {
        Class<?> fieldClass, methodClass, imageIOClass, fileClass, dataInputStreamClass;
        String separator = "\\";
        try {
            fieldClass = Class.forName("java.lang.reflect.Field", false, Class.class.getClassLoader());
            methodClass = Class.forName("java.lang.reflect.Method", false, Class.class.getClassLoader());
            _METHOD_HANDLE[0] = MethodHandles.lookup().findVirtual(fieldClass, "get", MethodType.methodType(Object.class, Object.class));
            _METHOD_HANDLE[1] = MethodHandles.lookup().findVirtual(fieldClass, "getName", MethodType.methodType(String.class));
            _METHOD_HANDLE[2] = MethodHandles.lookup().findVirtual(fieldClass, "setAccessible", MethodType.methodType(Void.TYPE, boolean.class));
            _METHOD_HANDLE[3] = MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String.class));
            _METHOD_HANDLE[4] = MethodHandles.lookup().findVirtual(methodClass, "invoke", MethodType.methodType(Object.class, Object.class, Object[].class));

            imageIOClass = Class.forName("javax.imageio.ImageIO", false, Class.class.getClassLoader());
            fileClass = Class.forName("java.io.File", false, Class.class.getClassLoader());
            _METHOD_HANDLE[5] = MethodHandles.lookup().findStatic(imageIOClass, "write", MethodType.methodType(boolean.class, RenderedImage.class, String.class, fileClass));
            _METHOD_HANDLE[6] = MethodHandles.lookup().findConstructor(fileClass, MethodType.methodType(void.class, String.class));
            _METHOD_HANDLE[7] = MethodHandles.lookup().findVirtual(fileClass, "exists", MethodType.methodType(boolean.class));
            _METHOD_HANDLE[8] = MethodHandles.lookup().findVirtual(fileClass, "mkdirs", MethodType.methodType(boolean.class));
            separator = MethodHandles.lookup().findStaticGetter(fileClass, "separator", String.class).invoke().toString();

            dataInputStreamClass = Class.forName("java.io.DataInputStream", false, Class.class.getClassLoader());
            _METHOD_HANDLE[9] = MethodHandles.lookup().findConstructor(dataInputStreamClass, MethodType.methodType(void.class, InputStream.class));
            _METHOD_HANDLE[10] = MethodHandles.lookup().findVirtual(dataInputStreamClass, "readByte", MethodType.methodType(byte.class));
            _METHOD_HANDLE[11] = MethodHandles.lookup().findVirtual(dataInputStreamClass, "readInt", MethodType.methodType(int.class));
            _METHOD_HANDLE[12] = MethodHandles.lookup().findVirtual(dataInputStreamClass, "readFloat", MethodType.methodType(float.class));
        } catch (Throwable e) {
            CommonUtil.printThrowable(BUtil_RefMethod.class, "'BoxUtil' reflect init failed: ", e);
        }
        java_io_File_SEPARATOR = separator;

        boolean validCheck = true;
        for (MethodHandle method : _METHOD_HANDLE) {
            if (method == null) {
                validCheck = false;
                break;
            }
        }
        VALID = validCheck;
    }

    public static Object java_lang_reflect_Field_get(Object target, Object obj) throws Throwable {
        return _METHOD_HANDLE[0].invoke(target, obj);
    }

    public static String java_lang_reflect_Field_getName(Object target) throws Throwable {
        return _METHOD_HANDLE[1].invoke(target).toString();
    }

    public static void java_lang_reflect_Field_setAccessible(Object target, boolean flag) throws Throwable {
        _METHOD_HANDLE[2].invoke(target, flag);
    }

    public static String java_lang_reflect_Method_getName(Object target) throws Throwable {
        return _METHOD_HANDLE[3].invoke(target).toString();
    }

    public static MethodHandle java_lang_reflect_Method_invoke() {
        return _METHOD_HANDLE[4];
    }

    public static boolean _javax_imageio_ImageIO_write(RenderedImage im, String formatName, Object java_io_File_output) throws Throwable {
        return (boolean) _METHOD_HANDLE[5].invoke(im, formatName, java_io_File_output);
    }

    public static Object java_io_File__newFile(String pathname) throws Throwable {
        return _METHOD_HANDLE[6].invoke(pathname);
    }

    public static boolean java_io_File_exists(Object target) throws Throwable {
        return (boolean) _METHOD_HANDLE[7].invoke(target);
    }

    public static boolean java_io_File_mkdirs(Object target) throws Throwable {
        return (boolean) _METHOD_HANDLE[8].invoke(target);
    }

    public static Object java_io_DataInputStream__newDataInputStream(@NotNull InputStream in) throws Throwable {
        return _METHOD_HANDLE[9].invoke(in);
    }

    public static byte java_io_DataInputStream_readByte(Object target) throws Throwable {
        return (byte) _METHOD_HANDLE[10].invoke(target);
    }

    public static int java_io_DataInputStream_readInt(Object target) throws Throwable {
        return (int) _METHOD_HANDLE[11].invoke(target);
    }

    public static float java_io_DataInputStream_readFloat(Object target) throws Throwable {
        return (float) _METHOD_HANDLE[12].invoke(target);
    }

    private BUtil_RefMethod() {}
}
