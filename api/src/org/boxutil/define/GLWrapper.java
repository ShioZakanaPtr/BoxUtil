package org.boxutil.define;

import org.boxutil.BoxUtilModPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.nio.*;
import java.util.function.*;
import java.util.regex.Pattern;

/**
 * This wrapper automatically adapts commonly used OpenGL functions by querying driver and hardware support for specific features.
 * Functions are organized by functionality or OpenGL object type rather than by OpenGL version.
 * All calls are statically linked via method references, incurring no runtime branch overhead.<p>
 *
 * The adaptation priority follows: OpenGL Core &gt; ARB Extension &gt; EXT Extension &gt; Other Extensions.<p>
 *
 * Before using this wrapper, you must call {@link BoxUtilModPlugin#initPre()} in your mod's initialization method.
 * Subsequently, before invoking actual OpenGL functions through {@link GLWrapper},
 * you must check availability via <code>valid()</code> or <code>valid_XXX()</code> for the corresponding feature,
 * to decide whether to fall back or abort.
 * Failing to do so will result in an {@link NullPointerException NPE} if the required feature is not supported.<p>
 *
 * For OpenGL enumerants, {@link GLWrapper} stores them as static non‑final fields.
 * They should be read only; set new values to these fields is forbidden.<p>
 *
 * Excluded: parts of <b>OpenGL 1.1 / OpenGL 1.2</b> (for example: Immediate mode, Client array, Display list).
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SpellCheckingInspection", "UnusedReturnValue", "unused"})
public final class GLWrapper {
    private static volatile boolean NOT_INIT = true;

    private interface IntIntFun {
        void run(int a, int b);
    }

    private interface LongIntFun {
        void run(long a, int b);
    }

    private interface IntLongFun {
        void run(int a, long b);
    }

    private interface IntIntFunLong {
        long run(int a, int b);
    }

    private interface IntIntFunFloat {
        float run(int a, int b);
    }

    private interface IntIntFunDouble {
        double run(int a, int b);
    }

    private interface IntFloatFun {
        void run(int a, float b);
    }

    private interface IntDoubleFun {
        void run(int a, double b);
    }

    private interface IntXFun<X> {
        void run(int a, X b);
    }

    private interface FloatBoolFun {
        void run(float a, boolean b);
    }

    private interface IntIntFunInt {
        int run(int a, int b);
    }

    private interface IntIntFunBool {
        boolean run(int a, int b);
    }

    private interface IntIntFunX<X> {
        X run(int a, int b);
    }

    private interface IntXFunInt<X> {
        int run(int a, X b);
    }

    private interface XIntFunInt<X> {
        int run(X a, int b);
    }

    private interface IntIntIntFun {
        void run(int a, int b, int c);
    }

    private interface IntFloatFloatFun {
        void run(int a, float b, float c);
    }

    private interface IntDoubleDoubleFun {
        void run(int a, double b, double c);
    }

    private interface IntIntLongFun {
        void run(int a, int b, long c);
    }

    private interface IntIntFloatFun {
        void run(int a, int b, float c);
    }

    private interface IntIntDoubleFun {
        void run(int a, int b, double c);
    }

    private interface IntIntIntFunInt {
        int run(int a, int b, int c);
    }

    private interface IntIntXFunInt<X> {
        int run(int a, int b, X c);
    }

    private interface IntBoolXFun<X> {
        void run(int a, boolean b, X c);
    }

    private interface XIntLongFun<X> {
        void run(X a, int b, long c);
    }

    private interface XIntLongFunInt<X> {
        int run(X a, int b, long c);
    }

    private interface IntIntIntFunX<X> {
        X run(int a, int b, int c);
    }

    private interface IntXYFun<X, Y> {
        void run(int a, X b, Y c);
    }

    private interface IntXIntFun<X> {
        void run(int a, X b, int c);
    }

    private interface IntIntXFun<X> {
        void run(int a, int b, X c);
    }

    private interface IntLongIntFun {
        void run(int a, long b, int c);
    }

    private interface IntLongLongFun {
        void run(int a, long b, long c);
    }

    private interface IntLongXFun<X> {
        void run(int a, long b, X c);
    }

    private interface IntIntIntIntFun {
        void run(int a, int b, int c, int d);
    }

    private interface IntIntIntIntFunInt {
        int run(int a, int b, int c, int d);
    }

    private interface FloatFloatFloatFloatFun {
        void run(float a, float b, float c, float d);
    }

    private interface IntIntFloatFloatFun {
        void run(int a, int b, float c, float d);
    }

    private interface IntIntDoubleDoubleFun {
        void run(int a, int b, double c, double d);
    }

    private interface IntIntFloatIntFun {
        void run(int a, int b, float c, int d);
    }

    private interface IntFloatFloatFloatFun {
        void run(int a, float b, float c, float d);
    }

    private interface IntDoubleDoubleDoubleFun {
        void run(int a, double b, double c, double d);
    }

    private interface IntIntIntLongFun {
        void run(int a, int b, int c, long d);
    }

    private interface IntIntLongIntFun {
        void run(int a, int b, long c, int d);
    }

    private interface IntLongIntIntFun {
        void run(int a, long b, int c, int d);
    }

    private interface IntXIntIntFun<X> {
        void run(int a, X b, int c, int d);
    }

    private interface IntIntXIntFun<X> {
        void run(int a, int b, X c, int d);
    }

    private interface IntIntIntXFun<X> {
        void run(int a, int b, int c, X d);
    }

    private interface IntIntBoolXFun<X> {
        void run(int a, int b, boolean c, X d);
    }

    private interface IntIntXYFun<X, Y> {
        void run(int a, int b, X c, Y d);
    }

    private interface IntXIntYFun<X, Y> {
        void run(int a, X b, int c, Y d);
    }

    private interface XIntYZFun<X, Y, Z> {
        void run(X a, int b, Y c, Z d);
    }

    private interface IntIntIntIntIntFun {
        void run(int a, int b, int c, int d, int e);
    }

    private interface IntIntBoolIntIntFunLong {
        long run(int a, int b, boolean c, int d, int e);
    }

    private interface IntIntIntBoolIntFun {
        void run(int a, int b, int c, boolean d, int e);
    }

    private interface IntBoolBoolBoolBoolFun {
        void run(int a, boolean b, boolean c, boolean d, boolean e);
    }

    private interface IntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, X e);
    }

    private interface IntIntIntXIntFun<X> {
        void run(int a, int b, int c, X d, int e);
    }

    private interface IntIntXIntIntFun<X> {
        void run(int a, int b, X c, int d, int e);
    }

    private interface IntXIntIntIntFun<X> {
        void run(int a, X b, int c, int d, int e);
    }

    private interface IntLongLongIntXFunY<X, Y> {
        Y run(int a, long b, long c, int d, X e);
    }

    private interface IntIntXYZFun<X, Y, Z> {
        void run(int a, int b, X c, Y d, Z e);
    }

    private interface IntFloatFloatFloatFloatFun {
        void run(int a, float b, float c, float d, float e);
    }

    private interface IntDoubleDoubleDoubleDoubleFun {
        void run(int a, double b, double c, double d, double e);
    }

    private interface IntIntFloatFloatFloatFun {
        void run(int a, int b, float c, float d, float e);
    }

    private interface IntIntDoubleDoubleDoubleFun {
        void run(int a, int b, double c, double d, double e);
    }

    private interface IntIntIntIntLongFun {
        void run(int a, int b, int c, int d, long e);
    }

    private interface IntIntIntLongIntFun {
        void run(int a, int b, int c, long d, int e);
    }

    private interface IntIntLongIntIntFun {
        void run(int a, int b, long c, int d, int e);
    }

    private interface IntIntIntLongLongFun {
        void run(int a, int b, int c, long d, long e);
    }

    private interface IntIntLongLongLongFun {
        void run(int a, int b, long c, long d, long e);
    }

    private interface IntIntIntIntIntIntFun {
        void run(int a, int b, int c, int d, int e, int f);
    }

    private interface IntIntIntIntIntBoolFun {
        void run(int a, int b, int c, int d, int e, boolean f);
    }
    
    private interface IntIntIntIntIntLongFun {
        void run(int a, int b, int c, int d, int e, long f);
    }

    private interface IntIntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, int e, X f);
    }

    private interface IntXIntIntIntIntFun<X> {
        void run(int a, X b, int c, int d, int e, int f);
    }

    private interface IntIntIntBoolIntLongFun {
        void run(int a, int b, int c, boolean d, int e, long f);
    }

    private interface IntIntIntLongIntIntFun {
        void run(int a, int b, int c, long d, int e, int f);
    }

    private interface IntIntIntXYZFun<X, Y, Z> {
        void run(int a, int b, int c, X d, Y e, Z f);
    }

    private interface IntIntFloatFloatFloatFloatFun {
        void run(int a, int b, float c, float d, float e, float f);
    }

    private interface IntIntDoubleDoubleDoubleDoubleFun {
        void run(int a, int b, double c, double d, double e, double f);
    }

    private interface IntIntIntIntIntIntBoolFun {
        void run(int a, int b, int c, int d, int e, int f, boolean g);
    }

    private interface IntIntIntIntIntIntLongFun {
        void run(int a, int b, int c, int d, int e, int f, long g);
    }

    private interface IntIntIntIntIntLongIntFun {
        void run(int a, int b, int c, int d, int e, long f, int g);
    }

    private interface IntIntIntLongIntIntIntFun {
        void run(int a, int b, int c, long d, int e, int f, int g);
    }

    private interface IntIntIntBoolIntIntIntFun {
        void run(int a, int b, int c, boolean d, int e, int f, int g);
    }

    private interface IntIntIntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, int e, int f, X g);
    }

    private interface IntIntLongLongIntIntXFun<X> {
        void run(int a, int b, long c, long d, int e, int f, X g);
    }

    private interface IntIntIntIntIntIntIntIntFun {
        void run(int a, int b, int c, int d, int e, int f, int g, int h);
    }

    private interface IntIntIntIntIntIntIntLongFun {
        void run(int a, int b, int c, int d, int e, int f, int g, long h);
    }

    private interface IntIntIntIntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, int e, int f, int g, X h);
    }

    private interface IntIntIntIntIntIntIntIntLongFun {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, long i);
    }

    private interface IntIntIntIntIntIntIntIntIntIntFun {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j);
    }

    private interface IntIntIntIntIntIntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, int i, X j);
    }

    private interface IntIntIntIntIntIntIntIntIntIntLongFun {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, long k);
    }

    private interface IntIntIntIntIntIntIntIntIntIntXFun<X> {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, X k);
    }

    private interface IntIntIntIntIntIntIntIntIntIntIntIntIntIntIntFun {
        void run(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l, int m, int n, int o);
    }

    public final static class DataType {
        private static boolean VALID_FP16;

        public static final int GL_BYTE = GL11.GL_BYTE;
        public static final int GL_UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
        public static final int GL_SHORT = GL11.GL_SHORT;
        public static final int GL_UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;
        public static final int GL_INT = GL11.GL_INT;
        public static final int GL_UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
        public static final int GL_FLOAT = GL11.GL_FLOAT;
        public static final int GL_DOUBLE = GL11.GL_DOUBLE;

        public static int GL_HALF_FLOAT;

        private static void init(ContextCapabilities cap) {
            VALID_FP16 = cap.OpenGL30 || (cap.GL_ARB_half_float_pixel && cap.GL_ARB_half_float_vertex) || cap.GL_NV_half_float;
            if (cap.OpenGL30) {
                GL_HALF_FLOAT = GL30.GL_HALF_FLOAT;
            } else if (cap.GL_ARB_half_float_pixel && cap.GL_ARB_half_float_vertex) {
                GL_HALF_FLOAT = ARBHalfFloatPixel.GL_HALF_FLOAT_ARB;
            } else if (cap.GL_NV_half_float) {
                GL_HALF_FLOAT = NVHalfFloat.GL_HALF_FLOAT_NV;
            }
        }

        public static boolean valid_FP16() {
            return VALID_FP16;
        }

        private DataType() {}
    }

    public sealed static class Operation permits Operation.Sync, Operation.Get {
        private static boolean VALID_Multisample;
        private static boolean VALID_BlendFuncSeparate;
        private static boolean VALID_BlendEquation;
        private static boolean VALID_BlendColor;
        private static boolean VALID_BlendEquationSeparate;
        private static boolean VALID_EnableIndexed;
        private static boolean VALID_DepthClamp;
        private static boolean VALID_ColorMaskIndexed;
        private static boolean VALID_BlendIndexed;

        public static final int GL_ALL_ATTRIB_BITS = GL11.GL_ALL_ATTRIB_BITS;
        public static final int GL_COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
        public static final int GL_CURRENT_BIT = GL11.GL_CURRENT_BIT;
        public static final int GL_DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
        public static final int GL_ENABLE_BIT = GL11.GL_ENABLE_BIT;
        public static final int GL_HINT_BIT = GL11.GL_HINT_BIT;
        public static final int GL_LIGHTING_BIT = GL11.GL_LIGHTING_BIT;
        public static final int GL_LINE_BIT = GL11.GL_LINE_BIT;
        public static final int GL_LIST_BIT = GL11.GL_LIST_BIT;
        public static final int GL_PIXEL_MODE_BIT = GL11.GL_PIXEL_MODE_BIT;
        public static final int GL_POINT_BIT = GL11.GL_POINT_BIT;
        public static final int GL_POLYGON_BIT = GL11.GL_POLYGON_BIT;
        public static final int GL_SCISSOR_BIT = GL11.GL_SCISSOR_BIT;
        public static final int GL_STENCIL_BUFFER_BIT = GL11.GL_STENCIL_BUFFER_BIT;
        public static final int GL_TEXTURE_BIT = GL11.GL_TEXTURE_BIT;
        public static final int GL_TRANSFORM_BIT = GL11.GL_TRANSFORM_BIT;
        public static final int GL_VIEWPORT_BIT = GL11.GL_VIEWPORT_BIT;

        public static final int GL_BLEND = GL11.GL_BLEND;
        public static final int GL_COLOR_LOGIC_OP = GL11.GL_COLOR_LOGIC_OP;
        public static final int GL_CULL_FACE = GL11.GL_CULL_FACE;
        public static final int GL_DEPTH_TEST = GL11.GL_DEPTH_TEST;
        public static final int GL_STENCIL_TEST = GL11.GL_STENCIL_TEST;
        public static final int GL_SCISSOR_TEST = GL11.GL_SCISSOR_TEST;
        public static final int GL_ALPHA_TEST = GL11.GL_ALPHA_TEST;
        public static final int GL_POINT_SMOOTH = GL11.GL_POINT_SMOOTH;
        public static final int GL_LINE_SMOOTH = GL11.GL_LINE_SMOOTH;
        public static final int GL_POLYGON_SMOOTH = GL11.GL_POLYGON_SMOOTH;

        public static final int GL_CW = GL11.GL_CW;
        public static final int GL_CCW = GL11.GL_CCW;

        public static final int GL_NEVER = GL11.GL_NEVER;
        public static final int GL_LESS = GL11.GL_LESS;
        public static final int GL_EQUAL = GL11.GL_EQUAL;
        public static final int GL_LEQUAL = GL11.GL_LEQUAL;
        public static final int GL_GREATER = GL11.GL_GREATER;
        public static final int GL_NOTEQUAL = GL11.GL_NOTEQUAL;
        public static final int GL_GEQUAL = GL11.GL_GEQUAL;
        public static final int GL_ALWAYS = GL11.GL_ALWAYS;

        public static final int GL_CLEAR = GL11.GL_CLEAR;
        public static final int GL_SET = GL11.GL_SET;
        public static final int GL_COPY = GL11.GL_COPY;
        public static final int GL_COPY_INVERTED = GL11.GL_COPY_INVERTED;
        public static final int GL_NOOP = GL11.GL_NOOP;
        public static final int GL_INVERT = GL11.GL_INVERT;
        public static final int GL_AND = GL11.GL_AND;
        public static final int GL_NAND = GL11.GL_NAND;
        public static final int GL_OR = GL11.GL_OR;
        public static final int GL_NOR = GL11.GL_NOR;
        public static final int GL_XOR = GL11.GL_XOR;
        public static final int GL_EQUIV = GL11.GL_EQUIV;
        public static final int GL_AND_REVERSE = GL11.GL_AND_REVERSE;
        public static final int GL_AND_INVERTED = GL11.GL_AND_INVERTED;
        public static final int GL_OR_REVERSE = GL11.GL_OR_REVERSE;
        public static final int GL_OR_INVERTED = GL11.GL_OR_INVERTED;

        public static final int GL_FRONT = GL11.GL_FRONT;
        public static final int GL_BACK = GL11.GL_BACK;
        public static final int GL_FRONT_AND_BACK = GL11.GL_FRONT_AND_BACK;

        public static final int GL_POINT_SMOOTH_HINT = GL11.GL_POINT_SMOOTH_HINT;
        public static final int GL_LINE_SMOOTH_HINT = GL11.GL_LINE_SMOOTH_HINT;
        public static final int GL_POLYGON_SMOOTH_HINT = GL11.GL_POLYGON_SMOOTH_HINT;
        public static final int GL_FASTEST = GL11.GL_FASTEST;
        public static final int GL_NICEST = GL11.GL_NICEST;
        public static final int GL_DONT_CARE = GL11.GL_DONT_CARE;

        public static final int GL_ZERO = GL11.GL_ZERO;
        public static final int GL_ONE = GL11.GL_ONE;
        public static final int GL_SRC_COLOR = GL11.GL_SRC_COLOR;
        public static final int GL_ONE_MINUS_SRC_COLOR = GL11.GL_ONE_MINUS_SRC_COLOR;
        public static final int GL_SRC_ALPHA = GL11.GL_SRC_ALPHA;
        public static final int GL_ONE_MINUS_SRC_ALPHA = GL11.GL_ONE_MINUS_SRC_ALPHA;
        public static final int GL_DST_ALPHA = GL11.GL_DST_ALPHA;
        public static final int GL_ONE_MINUS_DST_ALPHA = GL11.GL_ONE_MINUS_DST_ALPHA;
        public static final int GL_DST_COLOR = GL11.GL_DST_COLOR;
        public static final int GL_ONE_MINUS_DST_COLOR = GL11.GL_ONE_MINUS_DST_COLOR;
        public static final int GL_SRC_ALPHA_SATURATE = GL11.GL_SRC_ALPHA_SATURATE;

        public static int GL_MULTISAMPLE;
        public static int GL_MULTISAMPLE_BIT;
        public static int GL_SAMPLE_ALPHA_TO_COVERAGE;
        public static int GL_SAMPLE_ALPHA_TO_ONE;
        public static int GL_SAMPLE_COVERAGE;

        public static int GL_BLEND_DST_RGB;
        public static int GL_BLEND_SRC_RGB;
        public static int GL_BLEND_DST_ALPHA;
        public static int GL_BLEND_SRC_ALPHA;

        public static int GL_FUNC_ADD;
        public static int GL_FUNC_SUBTRACT;
        public static int GL_FUNC_REVERSE_SUBTRACT;
        public static int GL_MIN;
        public static int GL_MAX;
        public static int GL_BLEND_EQUATION;

        public static int GL_CONSTANT_COLOR;
        public static int GL_ONE_MINUS_CONSTANT_COLOR;
        public static int GL_CONSTANT_ALPHA;
        public static int GL_ONE_MINUS_CONSTANT_ALPHA;
        public static int GL_BLEND_COLOR;

        public static int GL_DEPTH_CLAMP;

        private static FloatBoolFun glSampleCoverage;

        private static IntIntIntIntFun glBlendFuncSeparate;

        private static IntConsumer glBlendEquation;

        private static FloatFloatFloatFloatFun glBlendColor;

        private static IntIntFun glBlendEquationSeparate;

        private static IntIntFun glEnablei;
        private static IntIntFun glDisablei;

        private static IntBoolBoolBoolBoolFun glColorMaski;

        private static IntIntIntFun glBlendFunci;
        private static IntIntIntIntIntFun glBlendFuncSeparatei;
        private static IntIntFun glBlendEquationi;
        private static IntIntIntFun glBlendEquationSeparatei;

        private static void init(ContextCapabilities cap) {
            VALID_Multisample = cap.OpenGL13 || cap.GL_ARB_multisample;
            if (cap.OpenGL13) {
                GL_MULTISAMPLE = GL13.GL_MULTISAMPLE;
                GL_MULTISAMPLE_BIT = GL13.GL_MULTISAMPLE_BIT;
                GL_SAMPLE_ALPHA_TO_COVERAGE = GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;
                GL_SAMPLE_ALPHA_TO_ONE = GL13.GL_SAMPLE_ALPHA_TO_ONE;
                GL_SAMPLE_COVERAGE = GL13.GL_SAMPLE_COVERAGE;

                glSampleCoverage = GL13::glSampleCoverage;
            } else if (cap.GL_ARB_multisample) {
                GL_MULTISAMPLE = ARBMultisample.GL_MULTISAMPLE_ARB;
                GL_MULTISAMPLE_BIT = ARBMultisample.GL_MULTISAMPLE_BIT_ARB;
                GL_SAMPLE_ALPHA_TO_COVERAGE = ARBMultisample.GL_SAMPLE_ALPHA_TO_COVERAGE_ARB;
                GL_SAMPLE_ALPHA_TO_ONE = ARBMultisample.GL_SAMPLE_ALPHA_TO_ONE_ARB;
                GL_SAMPLE_COVERAGE = ARBMultisample.GL_SAMPLE_COVERAGE_ARB;

                glSampleCoverage = ARBMultisample::glSampleCoverageARB;
            }

            VALID_BlendFuncSeparate = cap.OpenGL14 || cap.GL_EXT_blend_func_separate;
            if (cap.OpenGL14) {
                GL_BLEND_DST_RGB = GL14.GL_BLEND_DST_RGB;
                GL_BLEND_SRC_RGB = GL14.GL_BLEND_SRC_RGB;
                GL_BLEND_DST_ALPHA = GL14.GL_BLEND_DST_ALPHA;
                GL_BLEND_SRC_ALPHA = GL14.GL_BLEND_SRC_ALPHA;

                glBlendFuncSeparate = GL14::glBlendFuncSeparate;
            } else if (cap.GL_EXT_blend_func_separate) {
                GL_BLEND_DST_RGB = EXTBlendFuncSeparate.GL_BLEND_DST_RGB_EXT;
                GL_BLEND_SRC_RGB = EXTBlendFuncSeparate.GL_BLEND_SRC_RGB_EXT;
                GL_BLEND_DST_ALPHA = EXTBlendFuncSeparate.GL_BLEND_DST_ALPHA_EXT;
                GL_BLEND_SRC_ALPHA = EXTBlendFuncSeparate.GL_BLEND_SRC_ALPHA_EXT;

                glBlendFuncSeparate = EXTBlendFuncSeparate::glBlendFuncSeparateEXT;
            }

            VALID_BlendEquation = cap.OpenGL14 || cap.GL_ARB_imaging || (cap.GL_EXT_blend_minmax && cap.GL_EXT_blend_subtract);
            if (cap.OpenGL14) {
                GL_FUNC_ADD = GL14.GL_FUNC_ADD;
                GL_FUNC_SUBTRACT = GL14.GL_FUNC_SUBTRACT;
                GL_FUNC_REVERSE_SUBTRACT = GL14.GL_FUNC_REVERSE_SUBTRACT;
                GL_MIN = GL14.GL_MIN;
                GL_MAX = GL14.GL_MAX;
                GL_BLEND_EQUATION = GL14.GL_BLEND_EQUATION;

                glBlendEquation = GL14::glBlendEquation;
            } else if (cap.GL_ARB_imaging) {
                GL_FUNC_ADD = ARBImaging.GL_FUNC_ADD;
                GL_FUNC_SUBTRACT = ARBImaging.GL_FUNC_SUBTRACT;
                GL_FUNC_REVERSE_SUBTRACT = ARBImaging.GL_FUNC_REVERSE_SUBTRACT;
                GL_MIN = ARBImaging.GL_MIN;
                GL_MAX = ARBImaging.GL_MAX;
                GL_BLEND_EQUATION = ARBImaging.GL_BLEND_EQUATION;

                glBlendEquation = ARBImaging::glBlendEquation;
            } else if (cap.GL_EXT_blend_minmax && cap.GL_EXT_blend_subtract) {
                GL_FUNC_ADD = EXTBlendMinmax.GL_FUNC_ADD_EXT;
                GL_FUNC_SUBTRACT = EXTBlendSubtract.GL_FUNC_SUBTRACT_EXT;
                GL_FUNC_REVERSE_SUBTRACT = EXTBlendSubtract.GL_FUNC_REVERSE_SUBTRACT_EXT;
                GL_MIN = EXTBlendMinmax.GL_MIN_EXT;
                GL_MAX = EXTBlendMinmax.GL_MAX_EXT;
                GL_BLEND_EQUATION = EXTBlendMinmax.GL_BLEND_EQUATION_EXT;

                glBlendEquation = EXTBlendMinmax::glBlendEquationEXT;
            }

            VALID_BlendColor = cap.OpenGL14 || cap.GL_ARB_imaging || cap.GL_EXT_blend_color;
            if (cap.OpenGL14) {
                GL_CONSTANT_COLOR = GL14.GL_BLEND_COLOR;
                GL_ONE_MINUS_CONSTANT_COLOR = GL11.GL_ONE_MINUS_CONSTANT_COLOR;
                GL_CONSTANT_ALPHA = GL11.GL_CONSTANT_ALPHA;
                GL_ONE_MINUS_CONSTANT_ALPHA = GL11.GL_ONE_MINUS_CONSTANT_ALPHA;
                GL_BLEND_COLOR = GL14.GL_BLEND_COLOR;

                glBlendColor = GL14::glBlendColor;
            } else if (cap.GL_ARB_imaging) {
                GL_CONSTANT_COLOR = ARBImaging.GL_BLEND_COLOR;
                GL_ONE_MINUS_CONSTANT_COLOR = GL11.GL_ONE_MINUS_CONSTANT_COLOR;
                GL_CONSTANT_ALPHA = GL11.GL_CONSTANT_ALPHA;
                GL_ONE_MINUS_CONSTANT_ALPHA = GL11.GL_ONE_MINUS_CONSTANT_ALPHA;
                GL_BLEND_COLOR = ARBImaging.GL_BLEND_COLOR;

                glBlendColor = ARBImaging::glBlendColor;
            } else if (cap.GL_EXT_blend_color) {
                GL_CONSTANT_COLOR = EXTBlendColor.GL_BLEND_COLOR_EXT;
                GL_ONE_MINUS_CONSTANT_COLOR = EXTBlendColor.GL_ONE_MINUS_CONSTANT_COLOR_EXT;
                GL_CONSTANT_ALPHA = EXTBlendColor.GL_CONSTANT_ALPHA_EXT;
                GL_ONE_MINUS_CONSTANT_ALPHA = EXTBlendColor.GL_ONE_MINUS_CONSTANT_ALPHA_EXT;
                GL_BLEND_COLOR = EXTBlendColor.GL_BLEND_COLOR_EXT;

                glBlendColor = EXTBlendColor::glBlendColorEXT;
            }

            VALID_BlendEquationSeparate = cap.OpenGL20 || cap.GL_EXT_blend_equation_separate;
            if (cap.OpenGL20) {
                glBlendEquationSeparate = GL20::glBlendEquationSeparate;
            } else if (cap.GL_EXT_blend_equation_separate) {
                glBlendEquationSeparate = EXTBlendEquationSeparate::glBlendEquationSeparateEXT;
            }

            VALID_EnableIndexed = cap.OpenGL30 || cap.GL_ARB_viewport_array || cap.GL_EXT_draw_buffers2;
            if (cap.OpenGL30) {
                glEnablei = GL30::glEnablei;
                glDisablei = GL30::glDisablei;
            } else if (cap.GL_ARB_viewport_array) {
                glEnablei = ARBViewportArray::glEnableIndexedEXT;
                glDisablei = ARBViewportArray::glDisableIndexedEXT;
            } else if (cap.GL_EXT_draw_buffers2) {
                glEnablei = EXTDrawBuffers2::glEnableIndexedEXT;
                glDisablei = EXTDrawBuffers2::glDisableIndexedEXT;
            }

            VALID_DepthClamp = cap.OpenGL32 || cap.GL_ARB_depth_clamp || cap.GL_NV_depth_clamp;
            if (cap.OpenGL32) {
                GL_DEPTH_CLAMP = GL32.GL_DEPTH_CLAMP;
            } else if (cap.GL_ARB_depth_clamp) {
                GL_DEPTH_CLAMP = ARBDepthClamp.GL_DEPTH_CLAMP;
            } else if (cap.GL_NV_depth_clamp) {
                GL_DEPTH_CLAMP = NVDepthClamp.GL_DEPTH_CLAMP_NV;
            }

            VALID_ColorMaskIndexed = cap.OpenGL30 || cap.GL_EXT_draw_buffers2;
            if (cap.OpenGL30) {
                glColorMaski = GL30::glColorMaski;
            } else if (cap.GL_EXT_draw_buffers2) {
                glColorMaski = EXTDrawBuffers2::glColorMaskIndexedEXT;
            }

            VALID_BlendIndexed = cap.OpenGL40 || cap.GL_ARB_draw_buffers_blend || cap.GL_AMD_draw_buffers_blend;
            if (cap.OpenGL40) {
                glBlendFunci = GL40::glBlendFunci;
                glBlendFuncSeparatei = GL40::glBlendFuncSeparatei;
                glBlendEquationi = GL40::glBlendEquationi;
                glBlendEquationSeparatei = GL40::glBlendEquationSeparatei;
            } else if (cap.GL_ARB_draw_buffers_blend) {
                glBlendFunci = ARBDrawBuffersBlend::glBlendFunciARB;
                glBlendFuncSeparatei = ARBDrawBuffersBlend::glBlendFuncSeparateiARB;
                glBlendEquationi = ARBDrawBuffersBlend::glBlendEquationiARB;
                glBlendEquationSeparatei = ARBDrawBuffersBlend::glBlendEquationSeparateiARB;
            } else if (cap.GL_AMD_draw_buffers_blend) {
                glBlendFunci = AMDDrawBuffersBlend::glBlendFuncIndexedAMD;
                glBlendFuncSeparatei = AMDDrawBuffersBlend::glBlendFuncSeparateIndexedAMD;
                glBlendEquationi = AMDDrawBuffersBlend::glBlendEquationIndexedAMD;
                glBlendEquationSeparatei = AMDDrawBuffersBlend::glBlendEquationSeparateIndexedAMD;
            }
        }

        public final static class Get extends Operation {
            private static boolean VALID_BoolI_IntI;
            private static boolean VALID_StringI;
            private static boolean VALID_Int64;
            private static boolean VALID_Int64Vec;
            private static boolean VALID_FpI;

            private static IntIntFunInt glGetInteger;
            private static IntIntXFun<IntBuffer> glGetInteger_buf;
            private static IntIntFunBool glGetBoolean;
            private static IntIntXFun<ByteBuffer> glGetBoolean_buf;

            private static IntToLongFunction glGetInteger64;
            private static IntIntFunLong glGetInteger64_i;
            private static IntXFun<LongBuffer> glGetInteger64_buf;

            private static IntIntFunFloat glGetFloat;
            private static IntIntXFun<FloatBuffer> glGetFloat_buf;
            private static IntIntFunDouble glGetDouble;
            private static IntIntXFun<DoubleBuffer> glGetDouble_buf;

            private static void init(ContextCapabilities cap) {
                VALID_BoolI_IntI = cap.OpenGL30 || cap.GL_EXT_draw_buffers2 || cap.GL_EXT_direct_state_access || cap.GL_NV_explicit_multisample;
                if (cap.OpenGL30) {
                    glGetInteger = GL30::glGetInteger;
                    glGetInteger_buf = GL30::glGetInteger;
                    glGetBoolean = GL30::glGetBoolean;
                    glGetBoolean_buf = GL30::glGetBoolean;
                } else if (cap.GL_EXT_draw_buffers2) {
                    glGetInteger = EXTDrawBuffers2::glGetIntegerIndexedEXT;
                    glGetInteger_buf = EXTDrawBuffers2::glGetIntegerIndexedEXT;
                    glGetBoolean = EXTDrawBuffers2::glGetBooleanIndexedEXT;
                    glGetBoolean_buf = EXTDrawBuffers2::glGetBooleanIndexedEXT;
                } else if (cap.GL_EXT_direct_state_access) {
                    glGetInteger = EXTDirectStateAccess::glGetIntegerIndexedEXT;
                    glGetInteger_buf = EXTDirectStateAccess::glGetIntegerIndexedEXT;
                    glGetBoolean = EXTDirectStateAccess::glGetBooleanIndexedEXT;
                    glGetBoolean_buf = EXTDirectStateAccess::glGetBooleanIndexedEXT;
                } else if (cap.GL_NV_explicit_multisample) {
                    glGetInteger = NVExplicitMultisample::glGetIntegerIndexedEXT;
                    glGetInteger_buf = NVExplicitMultisample::glGetIntegerIndexedEXT;
                    glGetBoolean = NVExplicitMultisample::glGetBooleanIndexedEXT;
                    glGetBoolean_buf = NVExplicitMultisample::glGetBooleanIndexedEXT;
                }

                VALID_StringI = cap.OpenGL30;

                VALID_Int64 = cap.OpenGL32 || cap.GL_ARB_sync;
                if (cap.OpenGL32) {
                    glGetInteger64 = GL32::glGetInteger64;
                    glGetInteger64_i = GL32::glGetInteger64;
                    glGetInteger64_buf = GL32::glGetInteger64;
                } else if (cap.GL_ARB_sync) {
                    glGetInteger64 = ARBSync::glGetInteger64;
                    glGetInteger64_i = (int value, int index) -> {
                        final LongBuffer tmp = BufferUtils.createLongBuffer(4).clear();
                        ARBSync.glGetInteger64(value, tmp);
                        return tmp.get(index);
                    };
                    glGetInteger64_buf = ARBSync::glGetInteger64;
                }

                VALID_Int64Vec = cap.OpenGL32;

                VALID_FpI = cap.OpenGL41 || cap.GL_ARB_viewport_array;
                if (cap.OpenGL41) {
                    glGetFloat = GL41::glGetFloat;
                    glGetFloat_buf = GL41::glGetFloat;
                    glGetDouble = GL41::glGetDouble;
                    glGetDouble_buf = GL41::glGetDouble;
                } else if (cap.GL_ARB_viewport_array) {
                    glGetFloat = ARBViewportArray::glGetFloat;
                    glGetFloat_buf = ARBViewportArray::glGetFloat;
                    glGetDouble = ARBViewportArray::glGetDouble;
                    glGetDouble_buf = ARBViewportArray::glGetDouble;
                }
            }

            /**
             * {@link GL30#glGetInteger(int, int)}<p>
             * {@link GL30#glGetInteger(int, int, IntBuffer)}<p>
             * {@link GL30#glGetBoolean(int, int)}<p>
             * {@link GL30#glGetBoolean(int, int, ByteBuffer)}<p>
             */
            public static boolean valid_BoolI_IntI() {
                return VALID_BoolI_IntI;
            }

            /**
             * {@link GL30#glGetStringi(int, int)}<p>
             */
            public static boolean valid_StringI() {
                return VALID_StringI;
            }

            /**
             * {@link GL32#glGetInteger64(int)}<p>
             * {@link GL32#glGetInteger64(int, int)}<p>
             * {@link GL32#glGetInteger64(int, LongBuffer)}<p>
             */
            public static boolean valid_Int64() {
                return VALID_Int64;
            }

            /**
             * {@link GL32#glGetInteger64(int, int, LongBuffer)}<p>
             */
            public static boolean valid_Int64Vec() {
                return VALID_Int64Vec;
            }

            /**
             * {@link GL41#glGetFloat(int, int)}<p>
             * {@link GL41#glGetFloat(int, int, FloatBuffer)}<p>
             * {@link GL41#glGetDouble(int, int)}<p>
             * {@link GL41#glGetDouble(int, int, DoubleBuffer)}<p>
             */
            public static boolean valid_FpIndexed() {
                return VALID_FpI;
            }

            public static int glGetError() {
                return GL11.glGetError();
            }

            public static String glGetErrorStr() {
                return GLU.gluErrorString(GL11.glGetError());
            }

            public static boolean glGetBoolean(int pname) {
                return GL11.glGetBoolean(pname);
            }

            public static void glGetBoolean(int pname, ByteBuffer params) {
                GL11.glGetBoolean(pname, params);
            }

            public static int glGetInteger(int pname) {
                return GL11.glGetInteger(pname);
            }

            public static void glGetInteger(int pname, IntBuffer params) {
                GL11.glGetInteger(pname, params);
            }

            public static float glGetFloat(int pname) {
                return GL11.glGetFloat(pname);
            }

            public static void glGetFloat(int pname, FloatBuffer params) {
                GL11.glGetFloat(pname, params);
            }

            public static double glGetDouble(int pname) {
                return GL11.glGetDouble(pname);
            }

            public static void glGetDouble(int pname, DoubleBuffer params) {
                GL11.glGetDouble(pname, params);
            }

            public static String glGetString(int name) {
                return GL11.glGetString(name);
            }

            public static int glGetInteger(int value, int index) {
                return glGetInteger.run(value, index);
            }

            public static void glGetInteger(int value, int index, IntBuffer data) {
                glGetInteger_buf.run(value, index, data);
            }

            public static boolean glGetBoolean(int value, int index) {
                return glGetBoolean.run(value, index);
            }

            public static void glGetBoolean(int value, int index, ByteBuffer data) {
                glGetBoolean_buf.run(value, index, data);
            }

            public static String glGetStringi(int name, int index) {
                return GL30.glGetStringi(name, index);
            }

            public static long glGetInteger64(int pname) {
                return glGetInteger64.applyAsLong(pname);
            }

            public static long glGetInteger64(int value, int index) {
                return glGetInteger64_i.run(value, index);
            }

            public static void glGetInteger64(int pname, LongBuffer data) {
                glGetInteger64_buf.run(pname, data);
            }

            public static void glGetInteger64(int value, int index, LongBuffer data) {
                GL32.glGetInteger64(value, index, data);
            }

            public static float glGetFloat(int target, int index) {
                return glGetFloat.run(target, index);
            }

            public static void glGetFloat(int target, int index, FloatBuffer data) {
                glGetFloat_buf.run(target, index, data);
            }

            public static double glGetDouble(int target, int index) {
                return glGetDouble.run(target, index);
            }

            public static void glGetDouble(int target, int index, DoubleBuffer data) {
                glGetDouble_buf.run(target, index, data);
            }
        }

        public final static class Sync extends Operation {
            private static boolean VALID_Sync;
            private static boolean VALID_Barrier;
            private static boolean VALID_TextureBarrier;
            private static boolean VALID_BarrierRegion;

            public static int GL_MAX_SERVER_WAIT_TIMEOUT;
            public static int GL_OBJECT_TYPE;
            public static int GL_SYNC_CONDITION;
            public static int GL_SYNC_STATUS;
            public static int GL_SYNC_FLAGS;
            public static int GL_SYNC_FENCE;
            public static int GL_SYNC_GPU_COMMANDS_COMPLETE;
            public static int GL_UNSIGNALED;
            public static int GL_SIGNALED;
            public static int GL_SYNC_FLUSH_COMMANDS_BIT;
            public static long GL_TIMEOUT_IGNORED;
            public static int GL_ALREADY_SIGNALED;
            public static int GL_TIMEOUT_EXPIRED;
            public static int GL_CONDITION_SATISFIED;
            public static int GL_WAIT_FAILED;

            public static int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT;
            public static int GL_ELEMENT_ARRAY_BARRIER_BIT;
            public static int GL_UNIFORM_BARRIER_BIT;
            public static int GL_TEXTURE_FETCH_BARRIER_BIT;
            public static int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT;
            public static int GL_COMMAND_BARRIER_BIT;
            public static int GL_PIXEL_BUFFER_BARRIER_BIT;
            public static int GL_TEXTURE_UPDATE_BARRIER_BIT;
            public static int GL_BUFFER_UPDATE_BARRIER_BIT;
            public static int GL_FRAMEBUFFER_BARRIER_BIT;
            public static int GL_TRANSFORM_FEEDBACK_BARRIER_BIT;
            public static int GL_ATOMIC_COUNTER_BARRIER_BIT;
            public static int GL_ALL_BARRIER_BITS;

            public static int GL_SHADER_STORAGE_BARRIER_BIT;

            public static int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
            public static int GL_QUERY_BUFFER_BARRIER_BIT;

            private static IntIntFunX<GLSync> glFenceSync;
            private static Consumer<GLSync> glDeleteSync;
            private static XIntLongFunInt<GLSync> glClientWaitSync;
            private static XIntLongFun<GLSync> glWaitSync;
            private static XIntYZFun<GLSync, IntBuffer, IntBuffer> glGetSync;
            private static XIntFunInt<GLSync> glGetSynci;

            private static IntConsumer glMemoryBarrier;
            private static Runnable glTextureBarrier;
            private static IntConsumer glMemoryBarrierByRegion;

            private static void init(ContextCapabilities cap) {
                VALID_Sync = cap.OpenGL32 || cap.GL_ARB_sync;
                if (cap.OpenGL32) {
                    GL_MAX_SERVER_WAIT_TIMEOUT = GL32.GL_MAX_SERVER_WAIT_TIMEOUT;
                    GL_OBJECT_TYPE = GL32.GL_OBJECT_TYPE;
                    GL_SYNC_CONDITION = GL32.GL_SYNC_CONDITION;
                    GL_SYNC_STATUS = GL32.GL_SYNC_STATUS;
                    GL_SYNC_FLAGS = GL32.GL_SYNC_FLAGS;
                    GL_SYNC_FENCE = GL32.GL_SYNC_FENCE;
                    GL_SYNC_GPU_COMMANDS_COMPLETE = GL32.GL_SYNC_GPU_COMMANDS_COMPLETE;
                    GL_UNSIGNALED = GL32.GL_UNSIGNALED;
                    GL_SIGNALED = GL32.GL_SIGNALED;
                    GL_SYNC_FLUSH_COMMANDS_BIT = GL32.GL_SYNC_FLUSH_COMMANDS_BIT;
                    GL_TIMEOUT_IGNORED = GL32.GL_TIMEOUT_IGNORED;
                    GL_ALREADY_SIGNALED = GL32.GL_ALREADY_SIGNALED;
                    GL_TIMEOUT_EXPIRED = GL32.GL_TIMEOUT_EXPIRED;
                    GL_CONDITION_SATISFIED = GL32.GL_CONDITION_SATISFIED;
                    GL_WAIT_FAILED = GL32.GL_WAIT_FAILED;

                    glFenceSync = GL32::glFenceSync;
                    glDeleteSync = GL32::glDeleteSync;
                    glClientWaitSync = GL32::glClientWaitSync;
                    glWaitSync = GL32::glWaitSync;
                    glGetSync = GL32::glGetSync;
                    glGetSynci = GL32::glGetSynci;
                } else if (cap.GL_ARB_sync) {
                    GL_MAX_SERVER_WAIT_TIMEOUT = ARBSync.GL_MAX_SERVER_WAIT_TIMEOUT;
                    GL_OBJECT_TYPE = ARBSync.GL_OBJECT_TYPE;
                    GL_SYNC_CONDITION = ARBSync.GL_SYNC_CONDITION;
                    GL_SYNC_STATUS = ARBSync.GL_SYNC_STATUS;
                    GL_SYNC_FLAGS = ARBSync.GL_SYNC_FLAGS;
                    GL_SYNC_FENCE = ARBSync.GL_SYNC_FENCE;
                    GL_SYNC_GPU_COMMANDS_COMPLETE = ARBSync.GL_SYNC_GPU_COMMANDS_COMPLETE;
                    GL_UNSIGNALED = ARBSync.GL_UNSIGNALED;
                    GL_SIGNALED = ARBSync.GL_SIGNALED;
                    GL_SYNC_FLUSH_COMMANDS_BIT = ARBSync.GL_SYNC_FLUSH_COMMANDS_BIT;
                    GL_TIMEOUT_IGNORED = ARBSync.GL_TIMEOUT_IGNORED;
                    GL_ALREADY_SIGNALED = ARBSync.GL_ALREADY_SIGNALED;
                    GL_TIMEOUT_EXPIRED = ARBSync.GL_TIMEOUT_EXPIRED;
                    GL_CONDITION_SATISFIED = ARBSync.GL_CONDITION_SATISFIED;
                    GL_WAIT_FAILED = ARBSync.GL_WAIT_FAILED;

                    glFenceSync = ARBSync::glFenceSync;
                    glDeleteSync = ARBSync::glDeleteSync;
                    glClientWaitSync = ARBSync::glClientWaitSync;
                    glWaitSync = ARBSync::glWaitSync;
                    glGetSynci = ARBSync::glGetSynci;
                }

                VALID_Barrier = cap.OpenGL42 || cap.GL_ARB_shader_image_load_store || cap.GL_EXT_shader_image_load_store;
                if (cap.OpenGL42) {
                    GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = GL42.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT;
                    GL_ELEMENT_ARRAY_BARRIER_BIT = GL42.GL_ELEMENT_ARRAY_BARRIER_BIT;
                    GL_UNIFORM_BARRIER_BIT = GL42.GL_UNIFORM_BARRIER_BIT;
                    GL_TEXTURE_FETCH_BARRIER_BIT = GL42.GL_TEXTURE_FETCH_BARRIER_BIT;
                    GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT;
                    GL_COMMAND_BARRIER_BIT = GL42.GL_COMMAND_BARRIER_BIT;
                    GL_PIXEL_BUFFER_BARRIER_BIT = GL42.GL_PIXEL_BUFFER_BARRIER_BIT;
                    GL_TEXTURE_UPDATE_BARRIER_BIT = GL42.GL_TEXTURE_UPDATE_BARRIER_BIT;
                    GL_BUFFER_UPDATE_BARRIER_BIT = GL42.GL_BUFFER_UPDATE_BARRIER_BIT;
                    GL_FRAMEBUFFER_BARRIER_BIT = GL42.GL_FRAMEBUFFER_BARRIER_BIT;
                    GL_TRANSFORM_FEEDBACK_BARRIER_BIT = GL42.GL_TRANSFORM_FEEDBACK_BARRIER_BIT;
                    GL_ATOMIC_COUNTER_BARRIER_BIT = GL42.GL_ATOMIC_COUNTER_BARRIER_BIT;
                    GL_ALL_BARRIER_BITS = GL42.GL_ALL_BARRIER_BITS;

                    GL_SHADER_STORAGE_BARRIER_BIT = GL43.GL_SHADER_STORAGE_BARRIER_BIT;

                    GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = GL44.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
                    GL_QUERY_BUFFER_BARRIER_BIT = GL44.GL_QUERY_BUFFER_BARRIER_BIT;

                    glMemoryBarrier = GL42::glMemoryBarrier;
                } else if (cap.GL_ARB_shader_image_load_store) {
                    GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = ARBShaderImageLoadStore.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT;
                    GL_ELEMENT_ARRAY_BARRIER_BIT = ARBShaderImageLoadStore.GL_ELEMENT_ARRAY_BARRIER_BIT;
                    GL_UNIFORM_BARRIER_BIT = ARBShaderImageLoadStore.GL_UNIFORM_BARRIER_BIT;
                    GL_TEXTURE_FETCH_BARRIER_BIT = ARBShaderImageLoadStore.GL_TEXTURE_FETCH_BARRIER_BIT;
                    GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = ARBShaderImageLoadStore.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT;
                    GL_COMMAND_BARRIER_BIT = ARBShaderImageLoadStore.GL_COMMAND_BARRIER_BIT;
                    GL_PIXEL_BUFFER_BARRIER_BIT = ARBShaderImageLoadStore.GL_PIXEL_BUFFER_BARRIER_BIT;
                    GL_TEXTURE_UPDATE_BARRIER_BIT = ARBShaderImageLoadStore.GL_TEXTURE_UPDATE_BARRIER_BIT;
                    GL_BUFFER_UPDATE_BARRIER_BIT = ARBShaderImageLoadStore.GL_BUFFER_UPDATE_BARRIER_BIT;
                    GL_FRAMEBUFFER_BARRIER_BIT = ARBShaderImageLoadStore.GL_FRAMEBUFFER_BARRIER_BIT;
                    GL_TRANSFORM_FEEDBACK_BARRIER_BIT = ARBShaderImageLoadStore.GL_TRANSFORM_FEEDBACK_BARRIER_BIT;
                    GL_ATOMIC_COUNTER_BARRIER_BIT = ARBShaderImageLoadStore.GL_ATOMIC_COUNTER_BARRIER_BIT;
                    GL_ALL_BARRIER_BITS = ARBShaderImageLoadStore.GL_ALL_BARRIER_BITS;

                    GL_SHADER_STORAGE_BARRIER_BIT = ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BARRIER_BIT;

                    GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = ARBBufferStorage.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
                    GL_QUERY_BUFFER_BARRIER_BIT = ARBQueryBufferObject.GL_QUERY_BUFFER_BARRIER_BIT;

                    glMemoryBarrier = ARBShaderImageLoadStore::glMemoryBarrier;
                } else if (cap.GL_EXT_shader_image_load_store) {
                    GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = EXTShaderImageLoadStore.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT_EXT;
                    GL_ELEMENT_ARRAY_BARRIER_BIT = EXTShaderImageLoadStore.GL_ELEMENT_ARRAY_BARRIER_BIT_EXT;
                    GL_UNIFORM_BARRIER_BIT = EXTShaderImageLoadStore.GL_UNIFORM_BARRIER_BIT_EXT;
                    GL_TEXTURE_FETCH_BARRIER_BIT = EXTShaderImageLoadStore.GL_TEXTURE_FETCH_BARRIER_BIT_EXT;
                    GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = EXTShaderImageLoadStore.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT_EXT;
                    GL_COMMAND_BARRIER_BIT = EXTShaderImageLoadStore.GL_COMMAND_BARRIER_BIT_EXT;
                    GL_PIXEL_BUFFER_BARRIER_BIT = EXTShaderImageLoadStore.GL_PIXEL_BUFFER_BARRIER_BIT_EXT;
                    GL_TEXTURE_UPDATE_BARRIER_BIT = EXTShaderImageLoadStore.GL_TEXTURE_UPDATE_BARRIER_BIT_EXT;
                    GL_BUFFER_UPDATE_BARRIER_BIT = EXTShaderImageLoadStore.GL_BUFFER_UPDATE_BARRIER_BIT_EXT;
                    GL_FRAMEBUFFER_BARRIER_BIT = EXTShaderImageLoadStore.GL_FRAMEBUFFER_BARRIER_BIT_EXT;
                    GL_TRANSFORM_FEEDBACK_BARRIER_BIT = EXTShaderImageLoadStore.GL_TRANSFORM_FEEDBACK_BARRIER_BIT_EXT;
                    GL_ATOMIC_COUNTER_BARRIER_BIT = EXTShaderImageLoadStore.GL_ATOMIC_COUNTER_BARRIER_BIT_EXT;
                    GL_ALL_BARRIER_BITS = EXTShaderImageLoadStore.GL_ALL_BARRIER_BITS_EXT;

                    GL_SHADER_STORAGE_BARRIER_BIT = GL43.GL_SHADER_STORAGE_BARRIER_BIT; // none EXT

                    GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = GL44.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT; // none EXT
                    GL_QUERY_BUFFER_BARRIER_BIT = GL44.GL_QUERY_BUFFER_BARRIER_BIT; // none EXT

                    glMemoryBarrier = EXTShaderImageLoadStore::glMemoryBarrierEXT;
                }

                VALID_TextureBarrier = cap.OpenGL45 || cap.GL_ARB_texture_barrier || cap.GL_NV_texture_barrier;
                if (cap.OpenGL45) {
                    glTextureBarrier = GL45::glTextureBarrier;
                } else if (cap.GL_ARB_texture_barrier) {
                    glTextureBarrier = ARBTextureBarrier::glTextureBarrier;
                } else if (cap.GL_NV_texture_barrier) {
                    glTextureBarrier = NVTextureBarrier::glTextureBarrierNV;
                }

                VALID_BarrierRegion = cap.OpenGL45 || cap.GL_ARB_ES3_1_compatibility;
                if (cap.OpenGL45) {
                    glMemoryBarrierByRegion = GL45::glMemoryBarrierByRegion;
                } else if (cap.GL_ARB_ES3_1_compatibility) {
                    glMemoryBarrierByRegion = ARBES31Compatibility::glMemoryBarrierByRegion;
                }
            }

            /**
             * {@link GL32#glFenceSync(int, int)}<p>
             * {@link GL32#glDeleteSync(GLSync)}<p>
             * {@link GL32#glClientWaitSync(GLSync, int, long)}<p>
             * {@link GL32#glWaitSync(GLSync, int, long)}<p>
             * {@link GL32#glGetSync(GLSync, int, IntBuffer, IntBuffer)}<p>
             * {@link GL32#glGetSynci(GLSync, int)}
             */
            public static boolean valid_Sync() {
                return VALID_Sync;
            }

            /**
             * {@link GL42#glMemoryBarrier(int)}
             */
            public static boolean valid_Barrier() {
                return VALID_Barrier;
            }

            /**
             * {@link GL45#glTextureBarrier()}
             */
            public static boolean valid_TextureBarrier() {
                return VALID_TextureBarrier;
            }

            /**
             * {@link GL45#glMemoryBarrierByRegion(int)}
             */
            public static boolean valid_BarrierRegion() {
                return VALID_BarrierRegion;
            }

            public static void glFlush() {
                GL11.glFlush();
            }

            public static void glFinish() {
                GL11.glFinish();
            }

            public static @NotNull GLSync glFenceSync(int condition, int flags) {
                return glFenceSync.run(condition, flags);
            }

            public static void glDeleteSync(GLSync sync) {
                glDeleteSync.accept(sync);
            }

            public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
                return glClientWaitSync.run(sync, flags, timeout);
            }

            public static void glWaitSync(GLSync sync, int flags, long timeout) {
                glWaitSync.run(sync, flags, timeout);
            }

            public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
                glGetSync.run(sync, pname, length, values);
            }

            public static int glGetSynci(GLSync sync, int pname) {
                return glGetSynci.run(sync, pname);
            }

            public static void glMemoryBarrier(int barriers) {
                glMemoryBarrier.accept(barriers);
            }

            public static void glTextureBarrier() {
                glTextureBarrier.run();
            }

            public static void glMemoryBarrierByRegion(int barriers) {
                glMemoryBarrierByRegion.accept(barriers);
            }
        }

        /**
         * {@link GL13#glSampleCoverage(float, boolean)}
         */
        public static boolean valid_Multisample() {
            return VALID_Multisample;
        }

        /**
         * {@link GL14#glBlendFuncSeparate(int, int, int, int)}
         */
        public static boolean valid_BlendFuncSeparate() {
            return VALID_BlendFuncSeparate;
        }

        /**
         * {@link GL14#glBlendEquation(int)}
         */
        public static boolean valid_BlendEquation() {
            return VALID_BlendEquation;
        }

        /**
         * {@link GL14#glBlendColor(float, float, float, float)}
         */
        public static boolean valid_BlendColor() {
            return VALID_BlendColor;
        }

        /**
         * {@link GL20#glBlendEquationSeparate(int, int)}
         */
        public static boolean valid_BlendEquationSeparate() {
            return VALID_BlendEquationSeparate;
        }

        /**
         * {@link GL30#glEnablei(int, int)}<p>
         * {@link GL30#glDisablei(int, int)}
         */
        public static boolean valid_EnableIndexed() {
            return VALID_EnableIndexed;
        }

        /**
         * {@link GL32#GL_DEPTH_CLAMP}
         */
        public static boolean valid_DepthClamp() {
            return VALID_DepthClamp;
        }

        /**
         * {@link GL30#glColorMaski(int, boolean, boolean, boolean, boolean)}
         */
        public static boolean valid_ColorMaskIndexed() {
            return VALID_ColorMaskIndexed;
        }

        /**
         * {@link GL40#glBlendFunci(int, int, int)}<p>
         * {@link GL40#glBlendFuncSeparatei(int, int, int, int, int)}<p>
         * {@link GL40#glBlendEquationi(int, int)}<p>
         * {@link GL40#glBlendEquationSeparatei(int, int, int)}
         */
        public static boolean valid_BlendIndexed() {
            return VALID_BlendIndexed;
        }

        public static void glPushAttrib(int mask) {
            GL11.glPushAttrib(mask);
        }

        public static void glPopAttrib() {
            GL11.glPopAttrib();
        }

        public static void glViewport(int x, int y, int width, int height) {
            GL11.glViewport(x, y, width, height);
        }

        public static void glCullFace(int mode) {
            GL11.glCullFace(mode);
        }

        public static void glHint(int target, int mode) {
            GL11.glHint(target, mode);
        }

        public static void glEnable(int cap) {
            GL11.glEnable(cap);
        }

        public static void glDisable(int cap) {
            GL11.glDisable(cap);
        }

        public static void glBlendFunc(int sfactor, int dfactor) {
            GL11.glBlendFunc(sfactor, dfactor);
        }

        public static void glFrontFace(int mode) {
            GL11.glFrontFace(mode);
        }

        public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
            GL11.glColorMask(red, green, blue, alpha);
        }

        public static void glDepthRange(double zNear, double zFar) {
            GL11.glDepthRange(zNear, zFar);
        }

        public static void glDepthFunc(int func) {
            GL11.glDepthFunc(func);
        }

        public static void glDepthMask(boolean flag) {
            GL11.glDepthMask(flag);
        }

        public static void glStencilFunc(int func, int ref, int mask) {
            GL11.glStencilFunc(func, ref, mask);
        }

        public static void glStencilOp(int fail, int zfail, int zpass) {
            GL11.glStencilOp(fail, zfail, zpass);
        }

        public static void glStencilMask(int mask) {
            GL11.glStencilMask(mask);
        }

        public static void glScissor(int x, int y, int width, int height) {
            GL11.glScissor(x, y, width, height);
        }

        public static void glSampleCoverage(float value, boolean invert) {
            glSampleCoverage.run(value, invert);
        }

        public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
            glBlendFuncSeparate.run(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
        }

        public static void glBlendEquation(int mode) {
            glBlendEquation.accept(mode);
        }

        public static void glBlendColor(float red, float green, float blue, float alpha) {
            glBlendColor.run(red, green, blue, alpha);
        }

        public static void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
            glBlendEquationSeparate.run(modeRGB, modeAlpha);
        }

        public static void glEnablei(int target, int index) {
            glEnablei.run(target, index);
        }

        public static void glDisablei(int target, int index) {
            glDisablei.run(target, index);
        }

        public static void glColorMaski(int buf, boolean r, boolean g, boolean b, boolean a) {
            glColorMaski.run(buf, r, g, b, a);
        }

        public static void glBlendFunci(int buf, int src, int dst) {
            glBlendFunci.run(buf, src, dst);
        }

        public static void glBlendFuncSeparatei(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
            glBlendFuncSeparatei.run(buf, srcRGB, dstRGB, srcAlpha, dstAlpha);
        }

        public static void glBlendEquationi(int buf, int mode) {
            glBlendEquationi.run(buf, mode);
        }

        public static void glBlendEquationSeparatei(int buf, int modeRGB, int modeAlpha) {
            glBlendEquationSeparatei.run(buf, modeRGB, modeAlpha);
        }

        private Operation() {}
    }

    public sealed static class Shader permits Shader.Vert, Shader.Frag, Shader.Geom, Shader.Tess, Shader.Comp {
        private static boolean VALID;
        private static boolean VALID_RectangularMatrix;
        private static boolean VALID_UniformUint;
        private static boolean VALID_ProgramParameter;
        private static boolean VALID_Subroutine;
        private static boolean VALID_UniformDouble;
        private static boolean VALID_ProgramUniform;
        private static boolean VALID_ProgramUniformDouble;
        private static boolean VALID_Include;
        private static short highestVersionGLSL = 0;

        public static final int GL_TRUE = GL11.GL_TRUE;
        public static final int GL_FALSE = GL11.GL_FALSE;
        public static int GL_SHADING_LANGUAGE_VERSION;
        public static int GL_DELETE_STATUS;
        public static int GL_COMPILE_STATUS;
        public static int GL_LINK_STATUS;
        public static int GL_VALIDATE_STATUS;
        public static int GL_INFO_LOG_LENGTH;

        public static int GL_SHADER_INCLUDE;

        private static IntXFun<ByteBuffer> glShaderSource;
        private static IntXFun<CharSequence> glShaderSource_str;
        private static IntXFun<CharSequence[]> glShaderSource_strp;
        private static IntUnaryOperator glCreateShader;
        private static IntConsumer glCompileShader;
        private static IntConsumer glDeleteShader;
        private static IntSupplier glCreateProgram;
        private static IntIntFun glAttachShader;
        private static IntIntFun glDetachShader;
        private static IntConsumer glLinkProgram;
        private static IntConsumer glUseProgram;
        private static IntConsumer glDeleteProgram;
        private static IntFloatFun glUniform1f;
        private static IntFloatFloatFun glUniform2f;
        private static IntFloatFloatFloatFun glUniform3f;
        private static IntFloatFloatFloatFloatFun glUniform4f;
        private static IntIntFun glUniform1i;
        private static IntIntIntFun glUniform2i;
        private static IntIntIntIntFun glUniform3i;
        private static IntIntIntIntIntFun glUniform4i;
        private static IntXFun<FloatBuffer> glUniform1;
        private static IntXFun<FloatBuffer> glUniform2;
        private static IntXFun<FloatBuffer> glUniform3;
        private static IntXFun<FloatBuffer> glUniform4;
        private static IntXFun<IntBuffer> glUniform1_IntBuffer;
        private static IntXFun<IntBuffer> glUniform2_IntBuffer;
        private static IntXFun<IntBuffer> glUniform3_IntBuffer;
        private static IntXFun<IntBuffer> glUniform4_IntBuffer;
        private static IntBoolXFun<FloatBuffer> glUniformMatrix2;
        private static IntBoolXFun<FloatBuffer> glUniformMatrix3;
        private static IntBoolXFun<FloatBuffer> glUniformMatrix4;
        private static IntIntFunInt glGetShaderi;
        private static IntIntFunInt glGetProgrami;
        private static IntIntFunX<String> glGetShaderInfoLog;
        private static IntIntFunX<String> glGetProgramInfoLog;
        private static IntXYFun<IntBuffer, IntBuffer> glGetAttachedShaders;
        private static IntXFunInt<ByteBuffer> glGetUniformLocation;
        private static IntXFunInt<CharSequence> glGetUniformLocation_str;

        private static IntIntFun glUniform1ui;
        private static IntIntIntFun glUniform2ui;
        private static IntIntIntIntFun glUniform3ui;
        private static IntIntIntIntIntFun glUniform4ui;
        private static IntXFun<IntBuffer> glUniform1u;
        private static IntXFun<IntBuffer> glUniform2u;
        private static IntXFun<IntBuffer> glUniform3u;
        private static IntXFun<IntBuffer> glUniform4u;

        private static IntIntIntFun glProgramParameteri;

        private static IntIntXFunInt<ByteBuffer> glGetSubroutineUniformLocation;
        private static IntIntXFunInt<CharSequence> glGetSubroutineUniformLocation_str;
        private static IntIntXFunInt<ByteBuffer> glGetSubroutineIndex;
        private static IntIntXFunInt<CharSequence> glGetSubroutineIndex_str;
        private static IntIntIntIntXFun<IntBuffer> glGetActiveSubroutineUniform;
        private static IntIntIntIntFunInt glGetActiveSubroutineUniformi;
        private static IntXFun<IntBuffer> glUniformSubroutinesu;

        private static IntDoubleFun glUniform1d;
        private static IntDoubleDoubleFun glUniform2d;
        private static IntDoubleDoubleDoubleFun glUniform3d;
        private static IntDoubleDoubleDoubleDoubleFun glUniform4d;
        private static IntXFun<DoubleBuffer> glUniform1_DoubleBuffer;
        private static IntXFun<DoubleBuffer> glUniform2_DoubleBuffer;
        private static IntXFun<DoubleBuffer> glUniform3_DoubleBuffer;
        private static IntXFun<DoubleBuffer> glUniform4_DoubleBuffer;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix2d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix3d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix4d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix2x3d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix2x4d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix3x2d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix3x4d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix4x2d;
        private static IntBoolXFun<DoubleBuffer> glUniformMatrix4x3d;

        private static IntIntIntFun glProgramUniform1i;
        private static IntIntIntIntFun glProgramUniform2i;
        private static IntIntIntIntIntFun glProgramUniform3i;
        private static IntIntIntIntIntIntFun glProgramUniform4i;
        private static IntIntIntFun glProgramUniform1ui;
        private static IntIntIntIntFun glProgramUniform2ui;
        private static IntIntIntIntIntFun glProgramUniform3ui;
        private static IntIntIntIntIntIntFun glProgramUniform4ui;
        private static IntIntFloatFun glProgramUniform1f;
        private static IntIntFloatFloatFun glProgramUniform2f;
        private static IntIntFloatFloatFloatFun glProgramUniform3f;
        private static IntIntFloatFloatFloatFloatFun glProgramUniform4f;
        private static IntIntXFun<IntBuffer> glProgramUniform1_IntBuffer;
        private static IntIntXFun<IntBuffer> glProgramUniform2_IntBuffer;
        private static IntIntXFun<IntBuffer> glProgramUniform3_IntBuffer;
        private static IntIntXFun<IntBuffer> glProgramUniform4_IntBuffer;
        private static IntIntXFun<IntBuffer> glProgramUniform1u;
        private static IntIntXFun<IntBuffer> glProgramUniform2u;
        private static IntIntXFun<IntBuffer> glProgramUniform3u;
        private static IntIntXFun<IntBuffer> glProgramUniform4u;
        private static IntIntXFun<FloatBuffer> glProgramUniform1_FloatBuffer;
        private static IntIntXFun<FloatBuffer> glProgramUniform2_FloatBuffer;
        private static IntIntXFun<FloatBuffer> glProgramUniform3_FloatBuffer;
        private static IntIntXFun<FloatBuffer> glProgramUniform4_FloatBuffer;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix2;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix3;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix4;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix2x3;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix2x4;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix3x2;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix3x4;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix4x2;
        private static IntIntBoolXFun<FloatBuffer> glProgramUniformMatrix4x3;

        private static IntIntDoubleFun glProgramUniform1d;
        private static IntIntDoubleDoubleFun glProgramUniform2d;
        private static IntIntDoubleDoubleDoubleFun glProgramUniform3d;
        private static IntIntDoubleDoubleDoubleDoubleFun glProgramUniform4d;
        private static IntIntXFun<DoubleBuffer> glProgramUniform1_DoubleBuffer;
        private static IntIntXFun<DoubleBuffer> glProgramUniform2_DoubleBuffer;
        private static IntIntXFun<DoubleBuffer> glProgramUniform3_DoubleBuffer;
        private static IntIntXFun<DoubleBuffer> glProgramUniform4_DoubleBuffer;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix2d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix3d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix4d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix2x3d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix2x4d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix3x2d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix3x4d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix4x2d;
        private static IntIntBoolXFun<DoubleBuffer> glProgramUniformMatrix4x3d;

        private static IntXYFun<ByteBuffer, ByteBuffer> glNamedString;
        private static IntXYFun<CharSequence, CharSequence> glNamedString_str;
        private static Consumer<ByteBuffer> glDeleteNamedString;
        private static Consumer<CharSequence> glDeleteNamedString_str;
        private static IntIntXFun<ByteBuffer> glCompileShaderInclude;
        private static IntXFun<CharSequence[]> glCompileShaderInclude_strp;
        private static Predicate<ByteBuffer> glIsNamedString;
        private static Predicate<CharSequence> glIsNamedString_str;

        private static void init(ContextCapabilities cap) {
            VALID = cap.OpenGL20 || (cap.GL_ARB_shading_language_100 && cap.GL_ARB_shader_objects);
            if (cap.OpenGL20) {
                GL_SHADING_LANGUAGE_VERSION = GL20.GL_SHADING_LANGUAGE_VERSION;
                GL_DELETE_STATUS = GL20.GL_DELETE_STATUS;
                GL_COMPILE_STATUS = GL20.GL_COMPILE_STATUS;
                GL_LINK_STATUS = GL20.GL_LINK_STATUS;
                GL_VALIDATE_STATUS = GL20.GL_VALIDATE_STATUS;
                GL_INFO_LOG_LENGTH = GL20.GL_INFO_LOG_LENGTH;

                glShaderSource = GL20::glShaderSource;
                glShaderSource_str = GL20::glShaderSource;
                glShaderSource_strp = GL20::glShaderSource;
                glCreateShader = GL20::glCreateShader;
                glCompileShader = GL20::glCompileShader;
                glDeleteShader = GL20::glDeleteShader;
                glCreateProgram = GL20::glCreateProgram;
                glAttachShader = GL20::glAttachShader;
                glDetachShader = GL20::glDetachShader;
                glLinkProgram = GL20::glLinkProgram;
                glUseProgram = GL20::glUseProgram;
                glDeleteProgram = GL20::glDeleteProgram;
                glUniform1f = GL20::glUniform1f;
                glUniform2f = GL20::glUniform2f;
                glUniform3f = GL20::glUniform3f;
                glUniform4f = GL20::glUniform4f;
                glUniform1i = GL20::glUniform1i;
                glUniform2i = GL20::glUniform2i;
                glUniform3i = GL20::glUniform3i;
                glUniform4i = GL20::glUniform4i;
                glUniform1 = GL20::glUniform1;
                glUniform2 = GL20::glUniform2;
                glUniform3 = GL20::glUniform3;
                glUniform4 = GL20::glUniform4;
                glUniform1_IntBuffer = GL20::glUniform1;
                glUniform2_IntBuffer = GL20::glUniform2;
                glUniform3_IntBuffer = GL20::glUniform3;
                glUniform4_IntBuffer = GL20::glUniform4;
                glUniformMatrix2 = GL20::glUniformMatrix2;
                glUniformMatrix3 = GL20::glUniformMatrix3;
                glUniformMatrix4 = GL20::glUniformMatrix4;
                glGetShaderi = GL20::glGetShaderi;
                glGetProgrami = GL20::glGetProgrami;
                glGetShaderInfoLog = GL20::glGetShaderInfoLog;
                glGetProgramInfoLog = GL20::glGetProgramInfoLog;
                glGetAttachedShaders = GL20::glGetAttachedShaders;
                glGetUniformLocation = GL20::glGetUniformLocation;
                glGetUniformLocation_str = GL20::glGetUniformLocation;
            } else if (cap.GL_ARB_shading_language_100 && cap.GL_ARB_shader_objects) {
                GL_SHADING_LANGUAGE_VERSION = ARBShadingLanguage100.GL_SHADING_LANGUAGE_VERSION_ARB;
                GL_DELETE_STATUS = ARBShaderObjects.GL_OBJECT_DELETE_STATUS_ARB;
                GL_COMPILE_STATUS = ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB;
                GL_LINK_STATUS = ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB;
                GL_VALIDATE_STATUS = ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB;
                GL_INFO_LOG_LENGTH = ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB;

                glShaderSource = ARBShaderObjects::glShaderSourceARB;
                glShaderSource_str = ARBShaderObjects::glShaderSourceARB;
                glShaderSource_strp = ARBShaderObjects::glShaderSourceARB;
                glCreateShader = ARBShaderObjects::glCreateShaderObjectARB;
                glCompileShader = ARBShaderObjects::glCompileShaderARB;
                glDeleteShader = ARBShaderObjects::glDeleteObjectARB;
                glCreateProgram = ARBShaderObjects::glCreateProgramObjectARB;
                glAttachShader = ARBShaderObjects::glAttachObjectARB;
                glDetachShader = ARBShaderObjects::glDetachObjectARB;
                glLinkProgram = ARBShaderObjects::glLinkProgramARB;
                glUseProgram = ARBShaderObjects::glUseProgramObjectARB;
                glDeleteProgram = ARBShaderObjects::glDeleteObjectARB;
                glUniform1f = ARBShaderObjects::glUniform1fARB;
                glUniform2f = ARBShaderObjects::glUniform2fARB;
                glUniform3f = ARBShaderObjects::glUniform3fARB;
                glUniform4f = ARBShaderObjects::glUniform4fARB;
                glUniform1i = ARBShaderObjects::glUniform1iARB;
                glUniform2i = ARBShaderObjects::glUniform2iARB;
                glUniform3i = ARBShaderObjects::glUniform3iARB;
                glUniform4i = ARBShaderObjects::glUniform4iARB;
                glUniform1 = ARBShaderObjects::glUniform1ARB;
                glUniform2 = ARBShaderObjects::glUniform2ARB;
                glUniform3 = ARBShaderObjects::glUniform3ARB;
                glUniform4 = ARBShaderObjects::glUniform4ARB;
                glUniform1_IntBuffer = ARBShaderObjects::glUniform1ARB;
                glUniform2_IntBuffer = ARBShaderObjects::glUniform2ARB;
                glUniform3_IntBuffer = ARBShaderObjects::glUniform3ARB;
                glUniform4_IntBuffer = ARBShaderObjects::glUniform4ARB;
                glUniformMatrix2 = ARBShaderObjects::glUniformMatrix2ARB;
                glUniformMatrix3 = ARBShaderObjects::glUniformMatrix3ARB;
                glUniformMatrix4 = ARBShaderObjects::glUniformMatrix4ARB;
                glGetShaderi = ARBShaderObjects::glGetObjectParameteriARB;
                glGetProgrami = ARBShaderObjects::glGetObjectParameteriARB;
                glGetShaderInfoLog = ARBShaderObjects::glGetInfoLogARB;
                glGetProgramInfoLog = ARBShaderObjects::glGetInfoLogARB;
                glGetAttachedShaders = ARBShaderObjects::glGetAttachedObjectsARB;
                glGetUniformLocation = ARBShaderObjects::glGetUniformLocationARB;
                glGetUniformLocation_str = ARBShaderObjects::glGetUniformLocationARB;
            }
            if (VALID) {
                final var matcher = Pattern.compile("\\d\\.\\d\\d").matcher(Operation.Get.glGetString(GL_SHADING_LANGUAGE_VERSION));
                highestVersionGLSL = matcher.find() ? Short.parseShort(matcher.group().replace(".", "")) : 110;
            }

            VALID_RectangularMatrix = cap.OpenGL21;

            VALID_UniformUint = cap.OpenGL30 || cap.GL_EXT_gpu_shader4;
            if (cap.OpenGL30) {
                glUniform1ui = GL30::glUniform1ui;
                glUniform2ui = GL30::glUniform2ui;
                glUniform3ui = GL30::glUniform3ui;
                glUniform4ui = GL30::glUniform4ui;
                glUniform1u = GL30::glUniform1u;
                glUniform2u = GL30::glUniform2u;
                glUniform3u = GL30::glUniform3u;
                glUniform4u = GL30::glUniform4u;
            } else if (cap.GL_EXT_gpu_shader4) {
                glUniform1ui = EXTGpuShader4::glUniform1uiEXT;
                glUniform2ui = EXTGpuShader4::glUniform2uiEXT;
                glUniform3ui = EXTGpuShader4::glUniform3uiEXT;
                glUniform4ui = EXTGpuShader4::glUniform4uiEXT;
                glUniform1u = EXTGpuShader4::glUniform1uEXT;
                glUniform2u = EXTGpuShader4::glUniform2uEXT;
                glUniform3u = EXTGpuShader4::glUniform3uEXT;
                glUniform4u = EXTGpuShader4::glUniform4uEXT;
            }

            VALID_ProgramParameter = cap.OpenGL41 || cap.GL_ARB_separate_shader_objects || cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4;
            if (cap.OpenGL41) {
                glProgramParameteri = GL41::glProgramParameteri;
            } else if (cap.GL_ARB_separate_shader_objects) {
                glProgramParameteri = ARBSeparateShaderObjects::glProgramParameteri;
            } else if (cap.GL_ARB_geometry_shader4) {
                glProgramParameteri = ARBGeometryShader4::glProgramParameteriARB;
            } else if (cap.GL_EXT_geometry_shader4) {
                glProgramParameteri = EXTGeometryShader4::glProgramParameteriEXT;
            }

            VALID_Subroutine = cap.OpenGL40 || cap.GL_ARB_shader_subroutine;
            if (cap.OpenGL40) {
                glGetSubroutineUniformLocation = GL40::glGetSubroutineUniformLocation;
                glGetSubroutineUniformLocation_str = GL40::glGetSubroutineUniformLocation;
                glGetSubroutineIndex = GL40::glGetSubroutineIndex;
                glGetSubroutineIndex_str = GL40::glGetSubroutineIndex;
                glGetActiveSubroutineUniform = GL40::glGetActiveSubroutineUniform;
                glGetActiveSubroutineUniformi = GL40::glGetActiveSubroutineUniformi;
                glUniformSubroutinesu = GL40::glUniformSubroutinesu;
            } else if (cap.GL_ARB_shader_subroutine) {
                glGetSubroutineUniformLocation = ARBShaderSubroutine::glGetSubroutineUniformLocation;
                glGetSubroutineUniformLocation_str = ARBShaderSubroutine::glGetSubroutineUniformLocation;
                glGetSubroutineIndex = ARBShaderSubroutine::glGetSubroutineIndex;
                glGetSubroutineIndex_str = ARBShaderSubroutine::glGetSubroutineIndex;
                glGetActiveSubroutineUniform = ARBShaderSubroutine::glGetActiveSubroutineUniform;
                glGetActiveSubroutineUniformi = ARBShaderSubroutine::glGetActiveSubroutineUniformi;
                glUniformSubroutinesu = ARBShaderSubroutine::glUniformSubroutinesu;
            }

            VALID_UniformDouble = cap.OpenGL40 || cap.GL_ARB_gpu_shader_fp64;
            if (cap.OpenGL40) {
                glUniform1d = GL40::glUniform1d;
                glUniform2d = GL40::glUniform2d;
                glUniform3d = GL40::glUniform3d;
                glUniform4d = GL40::glUniform4d;
                glUniform1_DoubleBuffer = GL40::glUniform1;
                glUniform2_DoubleBuffer = GL40::glUniform2;
                glUniform3_DoubleBuffer = GL40::glUniform3;
                glUniform4_DoubleBuffer = GL40::glUniform4;
                glUniformMatrix2d = GL40::glUniformMatrix2;
                glUniformMatrix3d = GL40::glUniformMatrix3;
                glUniformMatrix4d = GL40::glUniformMatrix4;
                glUniformMatrix2x3d = GL40::glUniformMatrix2x3;
                glUniformMatrix2x4d = GL40::glUniformMatrix2x4;
                glUniformMatrix3x2d = GL40::glUniformMatrix3x2;
                glUniformMatrix3x4d = GL40::glUniformMatrix3x4;
                glUniformMatrix4x2d = GL40::glUniformMatrix4x2;
                glUniformMatrix4x3d = GL40::glUniformMatrix4x3;
            } else if (cap.GL_ARB_gpu_shader_fp64) {
                glUniform1d = ARBGpuShaderFp64::glUniform1d;
                glUniform2d = ARBGpuShaderFp64::glUniform2d;
                glUniform3d = ARBGpuShaderFp64::glUniform3d;
                glUniform4d = ARBGpuShaderFp64::glUniform4d;
                glUniform1_DoubleBuffer = ARBGpuShaderFp64::glUniform1;
                glUniform2_DoubleBuffer = ARBGpuShaderFp64::glUniform2;
                glUniform3_DoubleBuffer = ARBGpuShaderFp64::glUniform3;
                glUniform4_DoubleBuffer = ARBGpuShaderFp64::glUniform4;
                glUniformMatrix2d = ARBGpuShaderFp64::glUniformMatrix2;
                glUniformMatrix3d = ARBGpuShaderFp64::glUniformMatrix3;
                glUniformMatrix4d = ARBGpuShaderFp64::glUniformMatrix4;
                glUniformMatrix2x3d = ARBGpuShaderFp64::glUniformMatrix2x3;
                glUniformMatrix2x4d = ARBGpuShaderFp64::glUniformMatrix2x4;
                glUniformMatrix3x2d = ARBGpuShaderFp64::glUniformMatrix3x2;
                glUniformMatrix3x4d = ARBGpuShaderFp64::glUniformMatrix3x4;
                glUniformMatrix4x2d = ARBGpuShaderFp64::glUniformMatrix4x2;
                glUniformMatrix4x3d = ARBGpuShaderFp64::glUniformMatrix4x3;
            }

            VALID_ProgramUniform = cap.OpenGL41 || cap.GL_ARB_separate_shader_objects || cap.GL_EXT_direct_state_access;
            if (cap.OpenGL41) {
                glProgramUniform1i = GL41::glProgramUniform1i;
                glProgramUniform2i = GL41::glProgramUniform2i;
                glProgramUniform3i = GL41::glProgramUniform3i;
                glProgramUniform4i = GL41::glProgramUniform4i;
                glProgramUniform1ui = GL41::glProgramUniform1ui;
                glProgramUniform2ui = GL41::glProgramUniform2ui;
                glProgramUniform3ui = GL41::glProgramUniform3ui;
                glProgramUniform4ui = GL41::glProgramUniform4ui;
                glProgramUniform1f = GL41::glProgramUniform1f;
                glProgramUniform2f = GL41::glProgramUniform2f;
                glProgramUniform3f = GL41::glProgramUniform3f;
                glProgramUniform4f = GL41::glProgramUniform4f;
                glProgramUniform1_IntBuffer = GL41::glProgramUniform1;
                glProgramUniform2_IntBuffer = GL41::glProgramUniform2;
                glProgramUniform3_IntBuffer = GL41::glProgramUniform3;
                glProgramUniform4_IntBuffer = GL41::glProgramUniform4;
                glProgramUniform1u = GL41::glProgramUniform1u;
                glProgramUniform2u = GL41::glProgramUniform2u;
                glProgramUniform3u = GL41::glProgramUniform3u;
                glProgramUniform4u = GL41::glProgramUniform4u;
                glProgramUniform1_FloatBuffer = GL41::glProgramUniform1;
                glProgramUniform2_FloatBuffer = GL41::glProgramUniform2;
                glProgramUniform3_FloatBuffer = GL41::glProgramUniform3;
                glProgramUniform4_FloatBuffer = GL41::glProgramUniform4;
                glProgramUniformMatrix2 = GL41::glProgramUniformMatrix2;
                glProgramUniformMatrix3 = GL41::glProgramUniformMatrix3;
                glProgramUniformMatrix4 = GL41::glProgramUniformMatrix4;
                glProgramUniformMatrix2x3 = GL41::glProgramUniformMatrix2x3;
                glProgramUniformMatrix2x4 = GL41::glProgramUniformMatrix2x4;
                glProgramUniformMatrix3x2 = GL41::glProgramUniformMatrix3x2;
                glProgramUniformMatrix3x4 = GL41::glProgramUniformMatrix3x4;
                glProgramUniformMatrix4x2 = GL41::glProgramUniformMatrix4x2;
                glProgramUniformMatrix4x3 = GL41::glProgramUniformMatrix4x3;

            } else if (cap.GL_ARB_separate_shader_objects) {
                glProgramUniform1i = ARBSeparateShaderObjects::glProgramUniform1i;
                glProgramUniform2i = ARBSeparateShaderObjects::glProgramUniform2i;
                glProgramUniform3i = ARBSeparateShaderObjects::glProgramUniform3i;
                glProgramUniform4i = ARBSeparateShaderObjects::glProgramUniform4i;
                glProgramUniform1ui = ARBSeparateShaderObjects::glProgramUniform1ui;
                glProgramUniform2ui = ARBSeparateShaderObjects::glProgramUniform2ui;
                glProgramUniform3ui = ARBSeparateShaderObjects::glProgramUniform3ui;
                glProgramUniform4ui = ARBSeparateShaderObjects::glProgramUniform4ui;
                glProgramUniform1f = ARBSeparateShaderObjects::glProgramUniform1f;
                glProgramUniform2f = ARBSeparateShaderObjects::glProgramUniform2f;
                glProgramUniform3f = ARBSeparateShaderObjects::glProgramUniform3f;
                glProgramUniform4f = ARBSeparateShaderObjects::glProgramUniform4f;
                glProgramUniform1_IntBuffer = ARBSeparateShaderObjects::glProgramUniform1;
                glProgramUniform2_IntBuffer = ARBSeparateShaderObjects::glProgramUniform2;
                glProgramUniform3_IntBuffer = ARBSeparateShaderObjects::glProgramUniform3;
                glProgramUniform4_IntBuffer = ARBSeparateShaderObjects::glProgramUniform4;
                glProgramUniform1u = ARBSeparateShaderObjects::glProgramUniform1u;
                glProgramUniform2u = ARBSeparateShaderObjects::glProgramUniform2u;
                glProgramUniform3u = ARBSeparateShaderObjects::glProgramUniform3u;
                glProgramUniform4u = ARBSeparateShaderObjects::glProgramUniform4u;
                glProgramUniform1_FloatBuffer = ARBSeparateShaderObjects::glProgramUniform1;
                glProgramUniform2_FloatBuffer = ARBSeparateShaderObjects::glProgramUniform2;
                glProgramUniform3_FloatBuffer = ARBSeparateShaderObjects::glProgramUniform3;
                glProgramUniform4_FloatBuffer = ARBSeparateShaderObjects::glProgramUniform4;
                glProgramUniformMatrix2 = ARBSeparateShaderObjects::glProgramUniformMatrix2;
                glProgramUniformMatrix3 = ARBSeparateShaderObjects::glProgramUniformMatrix3;
                glProgramUniformMatrix4 = ARBSeparateShaderObjects::glProgramUniformMatrix4;
                glProgramUniformMatrix2x3 = ARBSeparateShaderObjects::glProgramUniformMatrix2x3;
                glProgramUniformMatrix2x4 = ARBSeparateShaderObjects::glProgramUniformMatrix2x4;
                glProgramUniformMatrix3x2 = ARBSeparateShaderObjects::glProgramUniformMatrix3x2;
                glProgramUniformMatrix3x4 = ARBSeparateShaderObjects::glProgramUniformMatrix3x4;
                glProgramUniformMatrix4x2 = ARBSeparateShaderObjects::glProgramUniformMatrix4x2;
                glProgramUniformMatrix4x3 = ARBSeparateShaderObjects::glProgramUniformMatrix4x3;

            } else if (cap.GL_EXT_direct_state_access) {
                glProgramUniform1i = EXTDirectStateAccess::glProgramUniform1iEXT;
                glProgramUniform2i = EXTDirectStateAccess::glProgramUniform2iEXT;
                glProgramUniform3i = EXTDirectStateAccess::glProgramUniform3iEXT;
                glProgramUniform4i = EXTDirectStateAccess::glProgramUniform4iEXT;
                glProgramUniform1ui = EXTDirectStateAccess::glProgramUniform1uiEXT;
                glProgramUniform2ui = EXTDirectStateAccess::glProgramUniform2uiEXT;
                glProgramUniform3ui = EXTDirectStateAccess::glProgramUniform3uiEXT;
                glProgramUniform4ui = EXTDirectStateAccess::glProgramUniform4uiEXT;
                glProgramUniform1f = EXTDirectStateAccess::glProgramUniform1fEXT;
                glProgramUniform2f = EXTDirectStateAccess::glProgramUniform2fEXT;
                glProgramUniform3f = EXTDirectStateAccess::glProgramUniform3fEXT;
                glProgramUniform4f = EXTDirectStateAccess::glProgramUniform4fEXT;
                glProgramUniform1_IntBuffer = EXTDirectStateAccess::glProgramUniform1EXT;
                glProgramUniform2_IntBuffer = EXTDirectStateAccess::glProgramUniform2EXT;
                glProgramUniform3_IntBuffer = EXTDirectStateAccess::glProgramUniform3EXT;
                glProgramUniform4_IntBuffer = EXTDirectStateAccess::glProgramUniform4EXT;
                glProgramUniform1u = EXTDirectStateAccess::glProgramUniform1uEXT;
                glProgramUniform2u = EXTDirectStateAccess::glProgramUniform2uEXT;
                glProgramUniform3u = EXTDirectStateAccess::glProgramUniform3uEXT;
                glProgramUniform4u = EXTDirectStateAccess::glProgramUniform4uEXT;
                glProgramUniform1_FloatBuffer = EXTDirectStateAccess::glProgramUniform1EXT;
                glProgramUniform2_FloatBuffer = EXTDirectStateAccess::glProgramUniform2EXT;
                glProgramUniform3_FloatBuffer = EXTDirectStateAccess::glProgramUniform3EXT;
                glProgramUniform4_FloatBuffer = EXTDirectStateAccess::glProgramUniform4EXT;
                glProgramUniformMatrix2 = EXTDirectStateAccess::glProgramUniformMatrix2EXT;
                glProgramUniformMatrix3 = EXTDirectStateAccess::glProgramUniformMatrix3EXT;
                glProgramUniformMatrix4 = EXTDirectStateAccess::glProgramUniformMatrix4EXT;
                glProgramUniformMatrix2x3 = EXTDirectStateAccess::glProgramUniformMatrix2x3EXT;
                glProgramUniformMatrix2x4 = EXTDirectStateAccess::glProgramUniformMatrix2x4EXT;
                glProgramUniformMatrix3x2 = EXTDirectStateAccess::glProgramUniformMatrix3x2EXT;
                glProgramUniformMatrix3x4 = EXTDirectStateAccess::glProgramUniformMatrix3x4EXT;
                glProgramUniformMatrix4x2 = EXTDirectStateAccess::glProgramUniformMatrix4x2EXT;
                glProgramUniformMatrix4x3 = EXTDirectStateAccess::glProgramUniformMatrix4x3EXT;
            }

            VALID_ProgramUniformDouble = cap.OpenGL41 || cap.GL_ARB_separate_shader_objects || cap.GL_ARB_gpu_shader_fp64;
            if (cap.OpenGL41) {
                glProgramUniform1d = GL41::glProgramUniform1d;
                glProgramUniform2d = GL41::glProgramUniform2d;
                glProgramUniform3d = GL41::glProgramUniform3d;
                glProgramUniform4d = GL41::glProgramUniform4d;
                glProgramUniform1_DoubleBuffer = GL41::glProgramUniform1;
                glProgramUniform2_DoubleBuffer = GL41::glProgramUniform2;
                glProgramUniform3_DoubleBuffer = GL41::glProgramUniform3;
                glProgramUniform4_DoubleBuffer = GL41::glProgramUniform4;
                glProgramUniformMatrix2d = GL41::glProgramUniformMatrix2;
                glProgramUniformMatrix3d = GL41::glProgramUniformMatrix3;
                glProgramUniformMatrix4d = GL41::glProgramUniformMatrix4;
                glProgramUniformMatrix2x3d = GL41::glProgramUniformMatrix2x3;
                glProgramUniformMatrix2x4d = GL41::glProgramUniformMatrix2x4;
                glProgramUniformMatrix3x2d = GL41::glProgramUniformMatrix3x2;
                glProgramUniformMatrix3x4d = GL41::glProgramUniformMatrix3x4;
                glProgramUniformMatrix4x2d = GL41::glProgramUniformMatrix4x2;
                glProgramUniformMatrix4x3d = GL41::glProgramUniformMatrix4x3;
            } else if (cap.GL_ARB_separate_shader_objects) {
                glProgramUniform1d = ARBSeparateShaderObjects::glProgramUniform1d;
                glProgramUniform2d = ARBSeparateShaderObjects::glProgramUniform2d;
                glProgramUniform3d = ARBSeparateShaderObjects::glProgramUniform3d;
                glProgramUniform4d = ARBSeparateShaderObjects::glProgramUniform4d;
                glProgramUniform1_DoubleBuffer = ARBSeparateShaderObjects::glProgramUniform1;
                glProgramUniform2_DoubleBuffer = ARBSeparateShaderObjects::glProgramUniform2;
                glProgramUniform3_DoubleBuffer = ARBSeparateShaderObjects::glProgramUniform3;
                glProgramUniform4_DoubleBuffer = ARBSeparateShaderObjects::glProgramUniform4;
                glProgramUniformMatrix2d = ARBSeparateShaderObjects::glProgramUniformMatrix2;
                glProgramUniformMatrix3d = ARBSeparateShaderObjects::glProgramUniformMatrix3;
                glProgramUniformMatrix4d = ARBSeparateShaderObjects::glProgramUniformMatrix4;
                glProgramUniformMatrix2x3d = ARBSeparateShaderObjects::glProgramUniformMatrix2x3;
                glProgramUniformMatrix2x4d = ARBSeparateShaderObjects::glProgramUniformMatrix2x4;
                glProgramUniformMatrix3x2d = ARBSeparateShaderObjects::glProgramUniformMatrix3x2;
                glProgramUniformMatrix3x4d = ARBSeparateShaderObjects::glProgramUniformMatrix3x4;
                glProgramUniformMatrix4x2d = ARBSeparateShaderObjects::glProgramUniformMatrix4x2;
                glProgramUniformMatrix4x3d = ARBSeparateShaderObjects::glProgramUniformMatrix4x3;
            } else if (cap.GL_ARB_gpu_shader_fp64) {
                glProgramUniform1d = ARBGpuShaderFp64::glProgramUniform1dEXT;
                glProgramUniform2d = ARBGpuShaderFp64::glProgramUniform2dEXT;
                glProgramUniform3d = ARBGpuShaderFp64::glProgramUniform3dEXT;
                glProgramUniform4d = ARBGpuShaderFp64::glProgramUniform4dEXT;
                glProgramUniform1_DoubleBuffer = ARBGpuShaderFp64::glProgramUniform1EXT;
                glProgramUniform2_DoubleBuffer = ARBGpuShaderFp64::glProgramUniform2EXT;
                glProgramUniform3_DoubleBuffer = ARBGpuShaderFp64::glProgramUniform3EXT;
                glProgramUniform4_DoubleBuffer = ARBGpuShaderFp64::glProgramUniform4EXT;
                glProgramUniformMatrix2d = ARBGpuShaderFp64::glProgramUniformMatrix2EXT;
                glProgramUniformMatrix3d = ARBGpuShaderFp64::glProgramUniformMatrix3EXT;
                glProgramUniformMatrix4d = ARBGpuShaderFp64::glProgramUniformMatrix4EXT;
                glProgramUniformMatrix2x3d = ARBGpuShaderFp64::glProgramUniformMatrix2x3EXT;
                glProgramUniformMatrix2x4d = ARBGpuShaderFp64::glProgramUniformMatrix2x4EXT;
                glProgramUniformMatrix3x2d = ARBGpuShaderFp64::glProgramUniformMatrix3x2EXT;
                glProgramUniformMatrix3x4d = ARBGpuShaderFp64::glProgramUniformMatrix3x4EXT;
                glProgramUniformMatrix4x2d = ARBGpuShaderFp64::glProgramUniformMatrix4x2EXT;
                glProgramUniformMatrix4x3d = ARBGpuShaderFp64::glProgramUniformMatrix4x3EXT;
            }

            VALID_Include = cap.GL_ARB_shading_language_include;
            if (cap.GL_ARB_shading_language_include) {
                GL_SHADER_INCLUDE = ARBShadingLanguageInclude.GL_SHADER_INCLUDE_ARB;

                glNamedString = ARBShadingLanguageInclude::glNamedStringARB;
                glNamedString_str = ARBShadingLanguageInclude::glNamedStringARB;
                glDeleteNamedString = ARBShadingLanguageInclude::glDeleteNamedStringARB;
                glDeleteNamedString_str = ARBShadingLanguageInclude::glDeleteNamedStringARB;
                glCompileShaderInclude = ARBShadingLanguageInclude::glCompileShaderIncludeARB;
                glCompileShaderInclude_strp = ARBShadingLanguageInclude::glCompileShaderIncludeARB;
                glIsNamedString = ARBShadingLanguageInclude::glIsNamedStringARB;
                glIsNamedString_str = ARBShadingLanguageInclude::glIsNamedStringARB;
            }
        }

        private static void checkBaseShaderValid() {
            VALID &= Vert.valid() && Frag.valid();
        }

        public final static class Vert extends Shader {
            private static boolean VALID;

            public static int GL_VERTEX_SHADER;

            private static IntIntXFun<ByteBuffer> glBindAttribLocation;
            private static IntIntXFun<CharSequence> glBindAttribLocation_str;
            private static IntXFunInt<ByteBuffer> glGetAttribLocation;
            private static IntXFunInt<CharSequence> glGetAttribLocation_str;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL20 || cap.GL_ARB_vertex_shader;
                if (cap.OpenGL20) {
                    GL_VERTEX_SHADER = GL20.GL_VERTEX_SHADER;

                    glBindAttribLocation = GL20::glBindAttribLocation;
                    glBindAttribLocation_str = GL20::glBindAttribLocation;
                    glGetAttribLocation = GL20::glGetAttribLocation;
                    glGetAttribLocation_str = GL20::glGetAttribLocation;
                } else if (cap.GL_ARB_vertex_shader) {
                    GL_VERTEX_SHADER = ARBVertexShader.GL_VERTEX_SHADER_ARB;

                    glBindAttribLocation = ARBVertexShader::glBindAttribLocationARB;
                    glBindAttribLocation_str = ARBVertexShader::glBindAttribLocationARB;
                    glGetAttribLocation = ARBVertexShader::glGetAttribLocationARB;
                    glGetAttribLocation_str = ARBVertexShader::glGetAttribLocationARB;
                }
            }

            /**
             * For data configuration: checkout {@link VAO#valid_Attrib()}
             */
            public static boolean valid() {
                return VALID;
            }

            public static void glBindAttribLocation(int program, int index, ByteBuffer name) {
                glBindAttribLocation.run(program, index, name);
            }

            public static void glBindAttribLocation(int program, int index, CharSequence name) {
                glBindAttribLocation_str.run(program, index, name);
            }

            public static int glGetAttribLocation(int program, ByteBuffer name) {
                return glGetAttribLocation.run(program, name);
            }

            public static int glGetAttribLocation(int program, CharSequence name) {
                return glGetAttribLocation_str.run(program, name);
            }

            private Vert() {}
        }

        public final static class Frag extends Shader {
            private static boolean VALID;
            private static boolean VALID_FragLocation;
            private static boolean VALID_DualSource;

            public static int GL_FRAGMENT_SHADER;
            public static int GL_FRAGMENT_SHADER_DERIVATIVE_HINT;

            public static int GL_SRC1_COLOR;
            public static int GL_SRC1_ALPHA;
            public static int GL_ONE_MINUS_SRC1_COLOR;
            public static int GL_ONE_MINUS_SRC1_ALPHA;

            private static IntIntXFun<ByteBuffer> glBindFragDataLocation;
            private static IntIntXFun<CharSequence> glBindFragDataLocation_str;

            private static IntIntIntXFun<ByteBuffer> glBindFragDataLocationIndexed;
            private static IntIntIntXFun<CharSequence> glBindFragDataLocationIndexed_str;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL20 || cap.GL_ARB_fragment_shader;
                if (cap.OpenGL20) {
                    GL_FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;
                    GL_FRAGMENT_SHADER_DERIVATIVE_HINT = GL20.GL_FRAGMENT_SHADER_DERIVATIVE_HINT;
                } else if (cap.GL_ARB_vertex_shader) {
                    GL_FRAGMENT_SHADER = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
                    GL_FRAGMENT_SHADER_DERIVATIVE_HINT = ARBFragmentShader.GL_FRAGMENT_SHADER_DERIVATIVE_HINT_ARB;
                }

                VALID_FragLocation = cap.OpenGL30 || cap.GL_EXT_gpu_shader4;
                if (cap.OpenGL20) {
                    glBindFragDataLocation = GL30::glBindFragDataLocation;
                    glBindFragDataLocation_str = GL30::glBindFragDataLocation;
                } else if (cap.GL_ARB_vertex_shader) {
                    glBindFragDataLocation = EXTGpuShader4::glBindFragDataLocationEXT;
                    glBindFragDataLocation_str = EXTGpuShader4::glBindFragDataLocationEXT;
                }

                VALID_DualSource = cap.OpenGL33 || cap.GL_ARB_blend_func_extended;
                if (cap.OpenGL20) {
                    GL_SRC1_COLOR = GL33.GL_SRC1_COLOR;
                    GL_SRC1_ALPHA = GL15.GL_SRC1_ALPHA;
                    GL_ONE_MINUS_SRC1_COLOR = GL33.GL_ONE_MINUS_SRC1_COLOR;
                    GL_ONE_MINUS_SRC1_ALPHA = GL33.GL_ONE_MINUS_SRC1_ALPHA;

                    glBindFragDataLocationIndexed = GL33::glBindFragDataLocationIndexed;
                    glBindFragDataLocationIndexed_str = GL33::glBindFragDataLocationIndexed;
                } else if (cap.GL_ARB_vertex_shader) {
                    GL_SRC1_COLOR = ARBBlendFuncExtended.GL_SRC1_COLOR;
                    GL_SRC1_ALPHA = ARBBlendFuncExtended.GL_SRC1_ALPHA;
                    GL_ONE_MINUS_SRC1_COLOR = ARBBlendFuncExtended.GL_ONE_MINUS_SRC1_COLOR;
                    GL_ONE_MINUS_SRC1_ALPHA = ARBBlendFuncExtended.GL_ONE_MINUS_SRC1_ALPHA;

                    glBindFragDataLocationIndexed = ARBBlendFuncExtended::glBindFragDataLocationIndexed;
                    glBindFragDataLocationIndexed_str = ARBBlendFuncExtended::glBindFragDataLocationIndexed;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            /**
             * {@link GL30#glBindFragDataLocation(int, int, ByteBuffer)}<p>
             * {@link GL30#glBindFragDataLocation(int, int, CharSequence)}
             */
            public static boolean valid_FragLocation() {
                return VALID_FragLocation;
            }

            /**
             * {@link GL33#glBindFragDataLocationIndexed(int, int, int, ByteBuffer)}<p>
             * {@link GL33#glBindFragDataLocationIndexed(int, int, int, CharSequence)}
             */
            public static boolean valid_DualSource() {
                return VALID_DualSource;
            }

            public static void glBindFragDataLocation(int program, int colorNumber, ByteBuffer name) {
                glBindFragDataLocation.run(program, colorNumber, name);
            }

            public static void glBindFragDataLocation(int program, int colorNumber, CharSequence name) {
                glBindFragDataLocation_str.run(program, colorNumber, name);
            }

            public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, ByteBuffer name) {
                glBindFragDataLocationIndexed.run(program, colorNumber, index, name);
            }

            public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, CharSequence name) {
                glBindFragDataLocationIndexed_str.run(program, colorNumber, index, name);
            }

            private Frag() {}
        }

        public final static class Geom extends Shader {
            private static boolean VALID;
            private static boolean NOT_CORE;

            public static int GL_GEOMETRY_SHADER;
            public static int GL_GEOMETRY_VERTICES_OUT;
            public static int GL_GEOMETRY_INPUT_TYPE;
            public static int GL_GEOMETRY_OUTPUT_TYPE;

            private static void init(ContextCapabilities cap) {
                NOT_CORE = !cap.OpenGL32 && (cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4);
                VALID = cap.OpenGL32 || cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4;
                if (cap.OpenGL32) {
                    GL_GEOMETRY_SHADER = GL32.GL_GEOMETRY_SHADER;
                    GL_GEOMETRY_VERTICES_OUT = GL32.GL_GEOMETRY_VERTICES_OUT;
                    GL_GEOMETRY_INPUT_TYPE = GL32.GL_GEOMETRY_INPUT_TYPE;
                    GL_GEOMETRY_OUTPUT_TYPE = GL32.GL_GEOMETRY_OUTPUT_TYPE;
                } else if (cap.GL_ARB_geometry_shader4) {
                    GL_GEOMETRY_SHADER = ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB;
                    GL_GEOMETRY_VERTICES_OUT = ARBGeometryShader4.GL_GEOMETRY_VERTICES_OUT_ARB;
                    GL_GEOMETRY_INPUT_TYPE = ARBGeometryShader4.GL_GEOMETRY_INPUT_TYPE_ARB;
                    GL_GEOMETRY_OUTPUT_TYPE = ARBGeometryShader4.GL_GEOMETRY_OUTPUT_TYPE_ARB;
                } else if (cap.GL_EXT_geometry_shader4) {
                    GL_GEOMETRY_SHADER = EXTGeometryShader4.GL_GEOMETRY_SHADER_EXT;
                    GL_GEOMETRY_VERTICES_OUT = EXTGeometryShader4.GL_GEOMETRY_VERTICES_OUT_EXT;
                    GL_GEOMETRY_INPUT_TYPE = EXTGeometryShader4.GL_GEOMETRY_INPUT_TYPE_EXT;
                    GL_GEOMETRY_OUTPUT_TYPE = EXTGeometryShader4.GL_GEOMETRY_OUTPUT_TYPE_EXT;
                }
            }

            /**
             * @return <code>true</code> when geometry shader valid, but <code>OpenGL32</code> is not supported.<p>
             *     In this case, <b>layout qualifier</b> may be unusable in geometry shader, so must be configured it before call {@link GL20#glLinkProgram(int)}
             */
            public static boolean supportedByARB_Ext() {
                return NOT_CORE;
            }

            public static boolean valid() {
                return VALID;
            }

            private Geom() {}
        }

        public final static class Tess extends Shader {
            private static boolean VALID;

            public static int GL_TESS_EVALUATION_SHADER;
            public static int GL_TESS_CONTROL_SHADER;
            public static int GL_PATCHES;
            public static int GL_PATCH_VERTICES;
            public static int GL_PATCH_DEFAULT_INNER_LEVEL;
            public static int GL_PATCH_DEFAULT_OUTER_LEVEL;

            private static IntIntFun glPatchParameteri;
            private static IntXFun<FloatBuffer> glPatchParameter;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL40 || cap.GL_ARB_tessellation_shader;
                if (cap.OpenGL40) {
                    GL_TESS_EVALUATION_SHADER = GL40.GL_TESS_EVALUATION_SHADER;
                    GL_TESS_CONTROL_SHADER = GL40.GL_TESS_CONTROL_SHADER;
                    GL_PATCHES = GL40.GL_PATCHES;
                    GL_PATCH_VERTICES = GL40.GL_PATCH_VERTICES;
                    GL_PATCH_DEFAULT_INNER_LEVEL = GL40.GL_PATCH_DEFAULT_INNER_LEVEL;
                    GL_PATCH_DEFAULT_OUTER_LEVEL = GL40.GL_PATCH_DEFAULT_OUTER_LEVEL;

                    glPatchParameteri = GL40::glPatchParameteri;
                    glPatchParameter = GL40::glPatchParameter;
                } else if (cap.GL_ARB_tessellation_shader) {
                    GL_TESS_EVALUATION_SHADER = ARBTessellationShader.GL_TESS_EVALUATION_SHADER;
                    GL_TESS_CONTROL_SHADER = ARBTessellationShader.GL_TESS_CONTROL_SHADER;
                    GL_PATCHES = ARBTessellationShader.GL_PATCHES;
                    GL_PATCH_VERTICES = ARBTessellationShader.GL_PATCH_VERTICES;
                    GL_PATCH_DEFAULT_INNER_LEVEL = ARBTessellationShader.GL_PATCH_DEFAULT_INNER_LEVEL;
                    GL_PATCH_DEFAULT_OUTER_LEVEL = ARBTessellationShader.GL_PATCH_DEFAULT_OUTER_LEVEL;

                    glPatchParameteri = ARBTessellationShader::glPatchParameteri;
                    glPatchParameter = ARBTessellationShader::glPatchParameter;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static void glPatchParameteri(int pname, int value) {
                glPatchParameteri.run(pname, value);
            }

            public static void glPatchParameter(int pname, FloatBuffer values) {
                glPatchParameter.run(pname, values);
            }

            private Tess() {}
        }

        public final static class Comp extends Shader {
            private static boolean VALID;

            public static int GL_COMPUTE_SHADER;
            public static int GL_DISPATCH_INDIRECT_BUFFER;
            public static int GL_DISPATCH_INDIRECT_BUFFER_BINDING;
            public static int GL_MAX_COMPUTE_WORK_GROUP_COUNT;
            public static int GL_MAX_COMPUTE_WORK_GROUP_SIZE;
            public static int GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS;

            private static IntIntIntFun glDispatchCompute;
            private static LongConsumer glDispatchComputeIndirect;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL43 || cap.GL_ARB_compute_shader;
                if (cap.OpenGL43) {
                    GL_COMPUTE_SHADER = GL43.GL_COMPUTE_SHADER;
                    GL_DISPATCH_INDIRECT_BUFFER = GL43.GL_DISPATCH_INDIRECT_BUFFER;
                    GL_DISPATCH_INDIRECT_BUFFER_BINDING = GL43.GL_DISPATCH_INDIRECT_BUFFER_BINDING;
                    GL_MAX_COMPUTE_WORK_GROUP_COUNT = GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT;
                    GL_MAX_COMPUTE_WORK_GROUP_SIZE = GL43.GL_MAX_COMPUTE_WORK_GROUP_SIZE;
                    GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = GL43.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS;

                    glDispatchCompute = GL43::glDispatchCompute;
                    glDispatchComputeIndirect = GL43::glDispatchComputeIndirect;
                } else if (cap.GL_ARB_compute_shader) {
                    GL_COMPUTE_SHADER = ARBComputeShader.GL_COMPUTE_SHADER;
                    GL_DISPATCH_INDIRECT_BUFFER = ARBComputeShader.GL_DISPATCH_INDIRECT_BUFFER;
                    GL_DISPATCH_INDIRECT_BUFFER_BINDING = ARBComputeShader.GL_DISPATCH_INDIRECT_BUFFER_BINDING;
                    GL_MAX_COMPUTE_WORK_GROUP_COUNT = ARBComputeShader.GL_MAX_COMPUTE_WORK_GROUP_COUNT;
                    GL_MAX_COMPUTE_WORK_GROUP_SIZE = ARBComputeShader.GL_MAX_COMPUTE_WORK_GROUP_SIZE;
                    GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = ARBComputeShader.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS;

                    glDispatchCompute = ARBComputeShader::glDispatchCompute;
                    glDispatchComputeIndirect = ARBComputeShader::glDispatchComputeIndirect;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static void glDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
                glDispatchCompute.run(num_groups_x, num_groups_y, num_groups_z);
            }

            public static void glDispatchComputeIndirect(long indirect) {
                glDispatchComputeIndirect.accept(indirect);
            }

            private Comp() {}
        }

        /**
         * Also within {@link Vert#valid()} and {@link Frag#valid()}, and <b>glsl 110</b> supported at least.
         */
        public static boolean valid() {
            return VALID;
        }

        /**
         * The Uniform type: mat2x3, mat2x4, mat3x2, mat3x4, mat4x2, mat4x3
         */
        public static boolean valid_RectangularMatrix() {
            return VALID_RectangularMatrix;
        }

        /**
         * Unsigned integer.
         */
        public static boolean valid_UniformUint() {
            return VALID_UniformUint;
        }

        /**
         * Valid when <code>OpenGL41</code>/<code>GL_ARB_separate_shader_objects</code> supported, or <code>ARB_geometry_shader4</code>/<code>EXT_geometry_shader4</code> supported.<p>
         * For configure the geometry shader when <b>layout qualifier</b> is not support.<p>
         * {@link GL41#glProgramParameteri(int, int, int)}
         */
        public static boolean valid_ProgramParameter() {
            return VALID_ProgramParameter;
        }

        public static boolean valid_Subroutine() {
            return VALID_Subroutine;
        }

        public static boolean valid_UniformDouble() {
            return VALID_UniformDouble;
        }

        /**
         * Set shader program's uniform directly without {@link GL20#glUseProgram(int)}.
         */
        public static boolean valid_ProgramUniform() {
            return VALID_ProgramUniform;
        }

        /**
         * Set shader program's uniform directly without {@link GL20#glUseProgram(int)}.
         */
        public static boolean valid_ProgramUniformDouble() {
            return VALID_ProgramUniformDouble;
        }

        /**
         * Still recommended to use {@link String#replace(CharSequence, CharSequence)} even if it was valid.<p>
         * {@link ARBShadingLanguageInclude#glNamedStringARB(int, ByteBuffer, ByteBuffer)}<p>
         * {@link ARBShadingLanguageInclude#glNamedStringARB(int, CharSequence, CharSequence)}<p>
         * {@link ARBShadingLanguageInclude#glDeleteNamedStringARB(ByteBuffer)}<p>
         * {@link ARBShadingLanguageInclude#glDeleteNamedStringARB(CharSequence)}<p>
         * {@link ARBShadingLanguageInclude#glCompileShaderIncludeARB(int, int, ByteBuffer)}<p>
         * {@link ARBShadingLanguageInclude#glCompileShaderIncludeARB(int, CharSequence[])}<p>
         * {@link ARBShadingLanguageInclude#glIsNamedStringARB(ByteBuffer)}<p>
         * {@link ARBShadingLanguageInclude#glIsNamedStringARB(CharSequence)}
         */
        public static boolean valid_Include() {
            return VALID_Include;
        }

        /**
         * @return three-digit integer when {@link Shader#valid()} was <code>true</code>, means the highest supported glsl version on current OpenGL context, otherwise returns <code>0</code> if any shader features was unsupported.
         */
        public static short getVersionGLSL() {
            return highestVersionGLSL;
        }

        public static void glShaderSource(int shader, ByteBuffer string) {
            glShaderSource.run(shader, string);
        }

        public static void glShaderSource(int shader, CharSequence string) {
            glShaderSource_str.run(shader, string);
        }

        public static void glShaderSource(int shader, CharSequence[] strings) {
            glShaderSource_strp.run(shader, strings);
        }

        public static int glCreateShader(int type) {
            return glCreateShader.applyAsInt(type);
        }

        public static void glCompileShader(int shader) {
            glCompileShader.accept(shader);
        }

        public static void glDeleteShader(int shader) {
            glDeleteShader.accept(shader);
        }

        public static int glCreateProgram() {
            return glCreateProgram.getAsInt();
        }

        public static void glAttachShader(int program, int shader) {
            glAttachShader.run(program, shader);
        }

        public static void glDetachShader(int program, int shader) {
            glDetachShader.run(program, shader);
        }

        public static void glLinkProgram(int program) {
            glLinkProgram.accept(program);
        }

        public static void glUseProgram(int program) {
            glUseProgram.accept(program);
        }

        public static void glDeleteProgram(int program) {
            glDeleteProgram.accept(program);
        }

        public static void glUniform1f(int location, float v0) {
            glUniform1f.run(location, v0);
        }

        public static void glUniform2f(int location, float v0, float v1) {
            glUniform2f.run(location, v0, v1);
        }

        public static void glUniform3f(int location, float v0, float v1, float v2) {
            glUniform3f.run(location, v0, v1, v2);
        }

        public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {
            glUniform4f.run(location, v0, v1, v2, v3);
        }

        public static void glUniform1i(int location, int v0) {
            glUniform1i.run(location, v0);
        }

        public static void glUniform2i(int location, int v0, int v1) {
            glUniform2i.run(location, v0, v1);
        }

        public static void glUniform3i(int location, int v0, int v1, int v2) {
            glUniform3i.run(location, v0, v1, v2);
        }

        public static void glUniform4i(int location, int v0, int v1, int v2, int v3) {
            glUniform4i.run(location, v0, v1, v2, v3);
        }

        public static void glUniform1(int location, FloatBuffer values) {
            glUniform1.run(location, values);
        }

        public static void glUniform2(int location, FloatBuffer values) {
            glUniform2.run(location, values);
        }

        public static void glUniform3(int location, FloatBuffer values) {
            glUniform3.run(location, values);
        }

        public static void glUniform4(int location, FloatBuffer values) {
            glUniform4.run(location, values);
        }

        public static void glUniform1(int location, IntBuffer values) {
            glUniform1_IntBuffer.run(location, values);
        }

        public static void glUniform2(int location, IntBuffer values) {
            glUniform2_IntBuffer.run(location, values);
        }

        public static void glUniform3(int location, IntBuffer values) {
            glUniform3_IntBuffer.run(location, values);
        }

        public static void glUniform4(int location, IntBuffer values) {
            glUniform4_IntBuffer.run(location, values);
        }

        public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
            glUniformMatrix2.run(location, transpose, matrices);
        }

        public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
            glUniformMatrix3.run(location, transpose, matrices);
        }

        public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
            glUniformMatrix4.run(location, transpose, matrices);
        }

        public static int glGetShaderi(int shader, int pname) {
            return glGetShaderi.run(shader, pname);
        }

        public static int glGetProgrami(int program, int pname) {
            return glGetProgrami.run(program, pname);
        }

        public static @NotNull String glGetShaderInfoLog(int shader, int maxLength) {
            return glGetShaderInfoLog.run(shader, maxLength);
        }

        public static @NotNull String glGetProgramInfoLog(int program, int maxLength) {
            return glGetProgramInfoLog.run(program, maxLength);
        }

        public static void glGetAttachedShaders(int program, IntBuffer count, IntBuffer shaders) {
            glGetAttachedShaders.run(program, count, shaders);
        }

        public static int glGetUniformLocation(int program, ByteBuffer name) {
            return glGetUniformLocation.run(program, name);
        }

        public static int glGetUniformLocation(int program, CharSequence name) {
            return glGetUniformLocation_str.run(program, name);
        }

        public static void glUniformMatrix2x3(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix2x3(location, transpose, matrices);
        }

        public static void glUniformMatrix2x4(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix2x4(location, transpose, matrices);
        }

        public static void glUniformMatrix3x2(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix3x2(location, transpose, matrices);
        }

        public static void glUniformMatrix3x4(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix3x4(location, transpose, matrices);
        }

        public static void glUniformMatrix4x2(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix4x2(location, transpose, matrices);
        }

        public static void glUniformMatrix4x3(int location, boolean transpose, FloatBuffer matrices) {
            GL21.glUniformMatrix4x3(location, transpose, matrices);
        }

        public static void glUniform1ui(int location, int v0) {
            glUniform1ui.run(location, v0);
        }

        public static void glUniform2ui(int location, int v0, int v1) {
            glUniform2ui.run(location, v0, v1);
        }

        public static void glUniform3ui(int location, int v0, int v1, int v2) {
            glUniform3ui.run(location, v0, v1, v2);
        }

        public static void glUniform4ui(int location, int v0, int v1, int v2, int v3) {
            glUniform4ui.run(location, v0, v1, v2, v3);
        }

        public static void glUniform1u(int location, IntBuffer value) {
            glUniform1u.run(location, value);
        }

        public static void glUniform2u(int location, IntBuffer value) {
            glUniform2u.run(location, value);
        }

        public static void glUniform3u(int location, IntBuffer value) {
            glUniform3u.run(location, value);
        }

        public static void glUniform4u(int location, IntBuffer value) {
            glUniform4u.run(location, value);
        }

        public static void glProgramParameteri(int program, int pname, int value) {
            glProgramParameteri.run(program, pname, value);
        }

        public static int glGetSubroutineUniformLocation(int program, int shadertype, ByteBuffer name) {
            return glGetSubroutineUniformLocation.run(program, shadertype, name);
        }

        public static int glGetSubroutineUniformLocation(int program, int shadertype, CharSequence name) {
            return glGetSubroutineUniformLocation_str.run(program, shadertype, name);
        }

        public static int glGetSubroutineIndex(int program, int shadertype, ByteBuffer name) {
            return glGetSubroutineIndex.run(program, shadertype, name);
        }

        public static int glGetSubroutineIndex(int program, int shadertype, CharSequence name) {
            return glGetSubroutineIndex_str.run(program, shadertype, name);
        }

        public static void glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname, IntBuffer values) {
            glGetActiveSubroutineUniform.run(program, shadertype, index, pname, values);
        }

        public static int glGetActiveSubroutineUniformi(int program, int shadertype, int index, int pname) {
            return glGetActiveSubroutineUniformi.run(program, shadertype, index, pname);
        }

        public static void glUniformSubroutinesu(int shadertype, IntBuffer indices) {
            glUniformSubroutinesu.run(shadertype, indices);
        }

        public static void glUniform1d(int location, double x) {
            glUniform1d.run(location, x);
        }

        public static void glUniform2d(int location, double x, double y) {
            glUniform2d.run(location, x, y);
        }

        public static void glUniform3d(int location, double x, double y, double z) {
            glUniform3d.run(location, x, y, z);
        }

        public static void glUniform4d(int location, double x, double y, double z, double w) {
            glUniform4d.run(location, x, y, z, w);
        }

        public static void glUniform1(int location, DoubleBuffer value) {
            glUniform1_DoubleBuffer.run(location, value);
        }

        public static void glUniform2(int location, DoubleBuffer value) {
            glUniform2_DoubleBuffer.run(location, value);
        }

        public static void glUniform3(int location, DoubleBuffer value) {
            glUniform3_DoubleBuffer.run(location, value);
        }

        public static void glUniform4(int location, DoubleBuffer value) {
            glUniform4_DoubleBuffer.run(location, value);
        }

        public static void glUniformMatrix2(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix2d.run(location, transpose, value);
        }

        public static void glUniformMatrix3(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix3d.run(location, transpose, value);
        }

        public static void glUniformMatrix4(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix4d.run(location, transpose, value);
        }

        public static void glUniformMatrix2x3(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix2x3d.run(location, transpose, value);
        }

        public static void glUniformMatrix2x4(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix2x4d.run(location, transpose, value);
        }

        public static void glUniformMatrix3x2(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix3x2d.run(location, transpose, value);
        }

        public static void glUniformMatrix3x4(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix3x4d.run(location, transpose, value);
        }

        public static void glUniformMatrix4x2(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix4x2d.run(location, transpose, value);
        }

        public static void glUniformMatrix4x3(int location, boolean transpose, DoubleBuffer value) {
            glUniformMatrix4x3d.run(location, transpose, value);
        }

        public static void glProgramUniform1i(int program, int location, int v0) {
            glProgramUniform1i.run(program, location, v0);
        }

        public static void glProgramUniform2i(int program, int location, int v0, int v1) {
            glProgramUniform2i.run(program, location, v0, v1);
        }

        public static void glProgramUniform3i(int program, int location, int v0, int v1, int v2) {
            glProgramUniform3i.run(program, location, v0, v1, v2);
        }

        public static void glProgramUniform4i(int program, int location, int v0, int v1, int v2, int v3) {
            glProgramUniform4i.run(program, location, v0, v1, v2, v3);
        }

        public static void glProgramUniform1ui(int program, int location, int v0) {
            glProgramUniform1ui.run(program, location, v0);
        }

        public static void glProgramUniform2ui(int program, int location, int v0, int v1) {
            glProgramUniform2ui.run(program, location, v0, v1);
        }

        public static void glProgramUniform3ui(int program, int location, int v0, int v1, int v2) {
            glProgramUniform3ui.run(program, location, v0, v1, v2);
        }

        public static void glProgramUniform4ui(int program, int location, int v0, int v1, int v2, int v3) {
            glProgramUniform4ui.run(program, location, v0, v1, v2, v3);
        }

        public static void glProgramUniform1f(int program, int location, float v0) {
            glProgramUniform1f.run(program, location, v0);
        }

        public static void glProgramUniform2f(int program, int location, float v0, float v1) {
            glProgramUniform2f.run(program, location, v0, v1);
        }

        public static void glProgramUniform3f(int program, int location, float v0, float v1, float v2) {
            glProgramUniform3f.run(program, location, v0, v1, v2);
        }

        public static void glProgramUniform4f(int program, int location, float v0, float v1, float v2, float v3) {
            glProgramUniform4f.run(program, location, v0, v1, v2, v3);
        }

        public static void glProgramUniform1(int program, int location, IntBuffer value) {
            glProgramUniform1_IntBuffer.run(program, location, value);
        }

        public static void glProgramUniform2(int program, int location, IntBuffer value) {
            glProgramUniform2_IntBuffer.run(program, location, value);
        }

        public static void glProgramUniform3(int program, int location, IntBuffer value) {
            glProgramUniform3_IntBuffer.run(program, location, value);
        }

        public static void glProgramUniform4(int program, int location, IntBuffer value) {
            glProgramUniform4_IntBuffer.run(program, location, value);
        }

        public static void glProgramUniform1u(int program, int location, IntBuffer value) {
            glProgramUniform1u.run(program, location, value);
        }

        public static void glProgramUniform2u(int program, int location, IntBuffer value) {
            glProgramUniform2u.run(program, location, value);
        }

        public static void glProgramUniform3u(int program, int location, IntBuffer value) {
            glProgramUniform3u.run(program, location, value);
        }

        public static void glProgramUniform4u(int program, int location, IntBuffer value) {
            glProgramUniform4u.run(program, location, value);
        }

        public static void glProgramUniform1(int program, int location, FloatBuffer value) {
            glProgramUniform1_FloatBuffer.run(program, location, value);
        }

        public static void glProgramUniform2(int program, int location, FloatBuffer value) {
            glProgramUniform2_FloatBuffer.run(program, location, value);
        }

        public static void glProgramUniform3(int program, int location, FloatBuffer value) {
            glProgramUniform3_FloatBuffer.run(program, location, value);
        }

        public static void glProgramUniform4(int program, int location, FloatBuffer value) {
            glProgramUniform4_FloatBuffer.run(program, location, value);
        }

        public static void glProgramUniformMatrix2(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix2.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix3.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix4.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix2x3.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix2x4.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix3x2.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix3x4.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix4x2.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, FloatBuffer value) {
            glProgramUniformMatrix4x3.run(program, location, transpose, value);
        }

        public static void glProgramUniform1d(int program, int location, double v0) {
            glProgramUniform1d.run(program, location, v0);
        }

        public static void glProgramUniform2d(int program, int location, double v0, double v1) {
            glProgramUniform2d.run(program, location, v0, v1);
        }

        public static void glProgramUniform3d(int program, int location, double v0, double v1, double v2) {
            glProgramUniform3d.run(program, location, v0, v1, v2);
        }

        public static void glProgramUniform4d(int program, int location, double v0, double v1, double v2, double v3) {
            glProgramUniform4d.run(program, location, v0, v1, v2, v3);
        }

        public static void glProgramUniform1(int program, int location, DoubleBuffer value) {
            glProgramUniform1_DoubleBuffer.run(program, location, value);
        }

        public static void glProgramUniform2(int program, int location, DoubleBuffer value) {
            glProgramUniform2_DoubleBuffer.run(program, location, value);
        }

        public static void glProgramUniform3(int program, int location, DoubleBuffer value) {
            glProgramUniform3_DoubleBuffer.run(program, location, value);
        }

        public static void glProgramUniform4(int program, int location, DoubleBuffer value) {
            glProgramUniform4_DoubleBuffer.run(program, location, value);
        }

        public static void glProgramUniformMatrix2(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix2d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix3d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix4d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix2x3d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix2x4d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix3x2d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix3x4d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix4x2d.run(program, location, transpose, value);
        }

        public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, DoubleBuffer value) {
            glProgramUniformMatrix4x3d.run(program, location, transpose, value);
        }

        public static void glNamedStringARB(int type, ByteBuffer name, ByteBuffer string) {
            glNamedString.run(type, name, string);
        }

        public static void glNamedStringARB(int type, CharSequence name, CharSequence string) {
            glNamedString_str.run(type, name, string);
        }

        public static void glDeleteNamedStringARB(ByteBuffer name) {
            glDeleteNamedString.accept(name);
        }

        public static void glDeleteNamedStringARB(CharSequence name) {
            glDeleteNamedString_str.accept(name);
        }

        public static void glCompileShaderIncludeARB(int shader, int count, ByteBuffer path) {
            glCompileShaderInclude.run(shader, count, path);
        }

        public static void glCompileShaderIncludeARB(int shader, CharSequence[] path) {
            glCompileShaderInclude_strp.run(shader, path);
        }

        public static boolean glIsNamedStringARB(ByteBuffer name) {
            return glIsNamedString.test(name);
        }

        public static boolean glIsNamedStringARB(CharSequence name) {
            return glIsNamedString_str.test(name);
        }

        private Shader() {}
    }

    public sealed static class Drawcall permits Drawcall.MultiTex {
        private static boolean VALID_DrawRangeElements;
        private static boolean VALID_MultiDraw;
        private static boolean VALID_InstancedDraw;
        private static boolean VALID_PrimitiveRestart;
        private static boolean VALID_AdjacencyMode;
        private static boolean VALID_DrawElementsBase;
        private static boolean VALID_ProvokingVertex;
        private static boolean VALID_IndirectDraw;
        private static boolean VALID_InstancedDrawBase;
        private static boolean VALID_IndirectMultiDraw;

        public static final int GL_POINTS = GL11.GL_POINTS;
        public static final int GL_LINES = GL11.GL_LINES;
        public static final int GL_LINE_LOOP = GL11.GL_LINE_LOOP;
        public static final int GL_LINE_STRIP = GL11.GL_LINE_STRIP;
        public static final int GL_TRIANGLES = GL11.GL_TRIANGLES;
        public static final int GL_TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;
        public static final int GL_TRIANGLE_FAN = GL11.GL_TRIANGLE_FAN;
        public static final int GL_QUADS = GL11.GL_QUADS;
        public static final int GL_QUAD_STRIP = GL11.GL_QUAD_STRIP;
        public static final int GL_POLYGON = GL11.GL_POLYGON;
        public static int GL_LINES_ADJACENCY;
        public static int GL_LINE_STRIP_ADJACENCY;
        public static int GL_TRIANGLES_ADJACENCY;
        public static int GL_TRIANGLE_STRIP_ADJACENCY;

        public static int GL_PRIMITIVE_RESTART_INDEX;

        public static int GL_PROVOKING_VERTEX;
        public static int GL_LAST_VERTEX_CONVENTION;
        
        private static IntIntIntIntIntLongFun glDrawRangeElements;
        private static IntIntIntXFun<ByteBuffer> glDrawRangeElements_ByteBuffer;
        private static IntIntIntXFun<ShortBuffer> glDrawRangeElements_ShortBuffer;
        private static IntIntIntXFun<IntBuffer> glDrawRangeElements_IntBuffer;

        private static IntXYFun<IntBuffer, IntBuffer> glMultiDrawArrays;

        private static IntIntIntIntFun glDrawArraysInstanced;
        private static IntIntIntLongIntFun glDrawElementsInstanced;
        private static IntXIntFun<ByteBuffer> glDrawElementsInstanced_ByteBuffer;
        private static IntXIntFun<ShortBuffer> glDrawElementsInstanced_ShortBuffer;
        private static IntXIntFun<IntBuffer> glDrawElementsInstanced_IntBuffer;

        private static IntConsumer glPrimitiveRestartIndex;

        private static IntIntIntLongIntFun glDrawElementsBaseVertex;
        private static IntXIntFun<ByteBuffer> glDrawElementsBaseVertex_ByteBuffer;
        private static IntXIntFun<ShortBuffer> glDrawElementsBaseVertex_ShortBuffer;
        private static IntXIntFun<IntBuffer> glDrawElementsBaseVertex_IntBuffer;
        private static IntIntIntIntIntLongIntFun glDrawRangeElementsBaseVertex;
        private static IntIntIntXIntFun<ByteBuffer> glDrawRangeElementsBaseVertex_ByteBuffer;
        private static IntIntIntXIntFun<ShortBuffer> glDrawRangeElementsBaseVertex_ShortBuffer;
        private static IntIntIntXIntFun<IntBuffer> glDrawRangeElementsBaseVertex_IntBuffer;
        private static IntIntIntLongIntIntFun glDrawElementsInstancedBaseVertex;
        private static IntXIntIntFun<ByteBuffer> glDrawElementsInstancedBaseVertex_ByteBuffer;
        private static IntXIntIntFun<ShortBuffer> glDrawElementsInstancedBaseVertex_ShortBuffer;
        private static IntXIntIntFun<IntBuffer> glDrawElementsInstancedBaseVertex_IntBuffer;

        private static IntConsumer glProvokingVertex;

        private static IntLongFun glDrawArraysIndirect;
        private static IntXFun<ByteBuffer> glDrawArraysIndirect_ByteBuffer;
        private static IntXFun<IntBuffer> glDrawArraysIndirect_IntBuffer;
        private static IntIntLongFun glDrawElementsIndirect;
        private static IntIntXFun<ByteBuffer> glDrawElementsIndirect_ByteBuffer;
        private static IntIntXFun<IntBuffer> glDrawElementsIndirect_IntBuffer;

        private static IntIntIntIntIntFun glDrawArraysInstancedBaseInstance;
        private static IntIntIntLongIntIntFun glDrawElementsInstancedBaseInstance;
        private static IntXIntIntFun<ByteBuffer> glDrawElementsInstancedBaseInstance_ByteBuffer;
        private static IntXIntIntFun<ShortBuffer> glDrawElementsInstancedBaseInstance_ShortBuffer;
        private static IntXIntIntFun<IntBuffer> glDrawElementsInstancedBaseInstance_IntBuffer;
        private static IntIntIntLongIntIntIntFun glDrawElementsInstancedBaseVertexBaseInstance;
        private static IntXIntIntIntFun<ByteBuffer> glDrawElementsInstancedBaseVertexBaseInstance_ByteBuffer;
        private static IntXIntIntIntFun<ShortBuffer> glDrawElementsInstancedBaseVertexBaseInstance_ShortBuffer;
        private static IntXIntIntIntFun<IntBuffer> glDrawElementsInstancedBaseVertexBaseInstance_IntBuffer;

        private static IntLongIntIntFun glMultiDrawArraysIndirect;
        private static IntXIntIntFun<ByteBuffer> glMultiDrawArraysIndirect_ByteBuffer;
        private static IntXIntIntFun<IntBuffer> glMultiDrawArraysIndirect_IntBuffer;
        private static IntIntLongIntIntFun glMultiDrawElementsIndirect;
        private static IntIntXIntIntFun<ByteBuffer> glMultiDrawElementsIndirect_ByteBuffer;
        private static IntIntXIntIntFun<IntBuffer> glMultiDrawElementsIndirect_IntBuffer;

        private static void init(ContextCapabilities cap) {
            VALID_DrawRangeElements = cap.OpenGL12 || cap.GL_EXT_draw_range_elements;
            if (cap.OpenGL12) {
                glDrawRangeElements = GL12::glDrawRangeElements;
                glDrawRangeElements_ByteBuffer = GL12::glDrawRangeElements;
                glDrawRangeElements_ShortBuffer = GL12::glDrawRangeElements;
                glDrawRangeElements_IntBuffer = GL12::glDrawRangeElements;
            } else if (cap.GL_EXT_draw_range_elements) {
                glDrawRangeElements = EXTDrawRangeElements::glDrawRangeElementsEXT;
                glDrawRangeElements_ByteBuffer = EXTDrawRangeElements::glDrawRangeElementsEXT;
                glDrawRangeElements_ShortBuffer = EXTDrawRangeElements::glDrawRangeElementsEXT;
                glDrawRangeElements_IntBuffer = EXTDrawRangeElements::glDrawRangeElementsEXT;
            }

            VALID_MultiDraw = cap.OpenGL14 || cap.GL_EXT_multi_draw_arrays;
            if (cap.OpenGL14) {
                glMultiDrawArrays = GL14::glMultiDrawArrays;
            } else if (cap.GL_EXT_multi_draw_arrays) {
                glMultiDrawArrays = EXTMultiDrawArrays::glMultiDrawArraysEXT;
            }

            VALID_InstancedDraw = cap.OpenGL31 || cap.GL_ARB_draw_instanced || cap.GL_EXT_draw_instanced;
            if (cap.OpenGL31) {
                glDrawArraysInstanced = GL31::glDrawArraysInstanced;
                glDrawElementsInstanced = GL31::glDrawElementsInstanced;
                glDrawElementsInstanced_ByteBuffer = GL31::glDrawElementsInstanced;
                glDrawElementsInstanced_ShortBuffer = GL31::glDrawElementsInstanced;
                glDrawElementsInstanced_IntBuffer = GL31::glDrawElementsInstanced;
            } else if (cap.GL_ARB_draw_instanced) {
                glDrawArraysInstanced = ARBDrawInstanced::glDrawArraysInstancedARB;
                glDrawElementsInstanced = ARBDrawInstanced::glDrawElementsInstancedARB;
                glDrawElementsInstanced_ByteBuffer = ARBDrawInstanced::glDrawElementsInstancedARB;
                glDrawElementsInstanced_ShortBuffer = ARBDrawInstanced::glDrawElementsInstancedARB;
                glDrawElementsInstanced_IntBuffer = ARBDrawInstanced::glDrawElementsInstancedARB;
            } else if (cap.GL_EXT_draw_instanced) {
                glDrawArraysInstanced = EXTDrawInstanced::glDrawArraysInstancedEXT;
                glDrawElementsInstanced = EXTDrawInstanced::glDrawElementsInstancedEXT;
                glDrawElementsInstanced_ByteBuffer = EXTDrawInstanced::glDrawElementsInstancedEXT;
                glDrawElementsInstanced_ShortBuffer = EXTDrawInstanced::glDrawElementsInstancedEXT;
                glDrawElementsInstanced_IntBuffer = EXTDrawInstanced::glDrawElementsInstancedEXT;
            }

            VALID_PrimitiveRestart = cap.OpenGL31 || cap.GL_NV_primitive_restart;
            if (cap.OpenGL31) {
                GL_PRIMITIVE_RESTART_INDEX = GL31.GL_PRIMITIVE_RESTART_INDEX;

                glPrimitiveRestartIndex = GL31::glPrimitiveRestartIndex;
            } else if (cap.GL_NV_primitive_restart) {
                GL_PRIMITIVE_RESTART_INDEX = NVPrimitiveRestart.GL_PRIMITIVE_RESTART_INDEX_NV;

                glPrimitiveRestartIndex = NVPrimitiveRestart::glPrimitiveRestartIndexNV;
            }

            VALID_AdjacencyMode = cap.OpenGL32 || cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4;
            if (cap.OpenGL32) {
                GL_LINES_ADJACENCY = GL32.GL_LINES_ADJACENCY;
                GL_LINE_STRIP_ADJACENCY = GL32.GL_LINE_STRIP_ADJACENCY;
                GL_TRIANGLES_ADJACENCY = GL32.GL_TRIANGLES_ADJACENCY;
                GL_TRIANGLE_STRIP_ADJACENCY = GL32.GL_TRIANGLE_STRIP_ADJACENCY;
            } else if (cap.GL_ARB_geometry_shader4) {
                GL_LINES_ADJACENCY = ARBGeometryShader4.GL_LINES_ADJACENCY_ARB;
                GL_LINE_STRIP_ADJACENCY = ARBGeometryShader4.GL_LINE_STRIP_ADJACENCY_ARB;
                GL_TRIANGLES_ADJACENCY = ARBGeometryShader4.GL_TRIANGLES_ADJACENCY_ARB;
                GL_TRIANGLE_STRIP_ADJACENCY = ARBGeometryShader4.GL_TRIANGLE_STRIP_ADJACENCY_ARB;
            } else if (cap.GL_EXT_geometry_shader4) {
                GL_LINES_ADJACENCY = EXTGeometryShader4.GL_LINES_ADJACENCY_EXT;
                GL_LINE_STRIP_ADJACENCY = EXTGeometryShader4.GL_LINE_STRIP_ADJACENCY_EXT;
                GL_TRIANGLES_ADJACENCY = EXTGeometryShader4.GL_TRIANGLES_ADJACENCY_EXT;
                GL_TRIANGLE_STRIP_ADJACENCY = EXTGeometryShader4.GL_TRIANGLE_STRIP_ADJACENCY_EXT;
            }

            VALID_DrawElementsBase = cap.OpenGL32 || cap.GL_ARB_draw_elements_base_vertex;
            if (cap.OpenGL32) {
                glDrawElementsBaseVertex = GL32::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_ByteBuffer = GL32::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_ShortBuffer = GL32::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_IntBuffer = GL32::glDrawElementsBaseVertex;
                glDrawRangeElementsBaseVertex = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_ByteBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_ShortBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_IntBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawElementsInstancedBaseVertex = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_ByteBuffer = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_ShortBuffer = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_IntBuffer = GL32::glDrawElementsInstancedBaseVertex;
            } else if (cap.GL_ARB_draw_elements_base_vertex) {
                glDrawElementsBaseVertex = ARBDrawElementsBaseVertex::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_ByteBuffer = GL32::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_ShortBuffer = GL32::glDrawElementsBaseVertex;
                glDrawElementsBaseVertex_IntBuffer = GL32::glDrawElementsBaseVertex;
                glDrawRangeElementsBaseVertex = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_ByteBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_ShortBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawRangeElementsBaseVertex_IntBuffer = GL32::glDrawRangeElementsBaseVertex;
                glDrawElementsInstancedBaseVertex = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_ByteBuffer = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_ShortBuffer = GL32::glDrawElementsInstancedBaseVertex;
                glDrawElementsInstancedBaseVertex_IntBuffer = GL32::glDrawElementsInstancedBaseVertex;
            }

            VALID_ProvokingVertex = cap.OpenGL32 || cap.GL_ARB_provoking_vertex || cap.GL_EXT_provoking_vertex;
            if (cap.OpenGL32) {
                GL_PROVOKING_VERTEX = GL32.GL_PROVOKING_VERTEX;
                GL_LAST_VERTEX_CONVENTION = GL32.GL_LAST_VERTEX_CONVENTION;

                glProvokingVertex = GL32::glProvokingVertex;
            } else if (cap.GL_ARB_provoking_vertex) {
                GL_PROVOKING_VERTEX = ARBProvokingVertex.GL_PROVOKING_VERTEX;
                GL_LAST_VERTEX_CONVENTION = ARBProvokingVertex.GL_LAST_VERTEX_CONVENTION;

                glProvokingVertex = ARBProvokingVertex::glProvokingVertex;
            } else if (cap.GL_EXT_provoking_vertex) {
                GL_PROVOKING_VERTEX = EXTProvokingVertex.GL_PROVOKING_VERTEX_EXT;
                GL_LAST_VERTEX_CONVENTION = EXTProvokingVertex.GL_LAST_VERTEX_CONVENTION_EXT;

                glProvokingVertex = EXTProvokingVertex::glProvokingVertexEXT;
            }

            VALID_IndirectDraw = cap.OpenGL40 || cap.GL_ARB_draw_indirect;
            if (cap.OpenGL40) {
                glDrawArraysIndirect = GL40::glDrawArraysIndirect;
                glDrawArraysIndirect_ByteBuffer = GL40::glDrawArraysIndirect;
                glDrawArraysIndirect_IntBuffer = GL40::glDrawArraysIndirect;
                glDrawElementsIndirect = GL40::glDrawElementsIndirect;
                glDrawElementsIndirect_ByteBuffer = GL40::glDrawElementsIndirect;
                glDrawElementsIndirect_IntBuffer = GL40::glDrawElementsIndirect;
            } else if (cap.GL_ARB_draw_indirect) {
                glDrawArraysIndirect = ARBDrawIndirect::glDrawArraysIndirect;
                glDrawArraysIndirect_ByteBuffer = ARBDrawIndirect::glDrawArraysIndirect;
                glDrawArraysIndirect_IntBuffer = ARBDrawIndirect::glDrawArraysIndirect;
                glDrawElementsIndirect = ARBDrawIndirect::glDrawElementsIndirect;
                glDrawElementsIndirect_ByteBuffer = ARBDrawIndirect::glDrawElementsIndirect;
                glDrawElementsIndirect_IntBuffer = ARBDrawIndirect::glDrawElementsIndirect;
            }

            VALID_InstancedDrawBase = cap.OpenGL42 || cap.GL_ARB_base_instance;
            if (cap.OpenGL42) {
                glDrawArraysInstancedBaseInstance = GL42::glDrawArraysInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance = GL42::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_ByteBuffer = GL42::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_ShortBuffer = GL42::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_IntBuffer = GL42::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance = GL42::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_ByteBuffer = GL42::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_ShortBuffer = GL42::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_IntBuffer = GL42::glDrawElementsInstancedBaseVertexBaseInstance;
            } else if (cap.GL_ARB_base_instance) {
                glDrawArraysInstancedBaseInstance = ARBBaseInstance::glDrawArraysInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance = ARBBaseInstance::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_ByteBuffer = ARBBaseInstance::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_ShortBuffer = ARBBaseInstance::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseInstance_IntBuffer = ARBBaseInstance::glDrawElementsInstancedBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance = ARBBaseInstance::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_ByteBuffer = ARBBaseInstance::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_ShortBuffer = ARBBaseInstance::glDrawElementsInstancedBaseVertexBaseInstance;
                glDrawElementsInstancedBaseVertexBaseInstance_IntBuffer = ARBBaseInstance::glDrawElementsInstancedBaseVertexBaseInstance;
            }

            VALID_IndirectMultiDraw = cap.OpenGL43 || cap.GL_ARB_multi_draw_indirect || cap.GL_AMD_multi_draw_indirect;
            if (cap.OpenGL43) {
                glMultiDrawArraysIndirect = GL43::glMultiDrawArraysIndirect;
                glMultiDrawArraysIndirect_ByteBuffer = GL43::glMultiDrawArraysIndirect;
                glMultiDrawArraysIndirect_IntBuffer = GL43::glMultiDrawArraysIndirect;
                glMultiDrawElementsIndirect = GL43::glMultiDrawElementsIndirect;
                glMultiDrawElementsIndirect_ByteBuffer = GL43::glMultiDrawElementsIndirect;
                glMultiDrawElementsIndirect_IntBuffer = GL43::glMultiDrawElementsIndirect;
            } else if (cap.GL_ARB_multi_draw_indirect) {
                glMultiDrawArraysIndirect = ARBMultiDrawIndirect::glMultiDrawArraysIndirect;
                glMultiDrawArraysIndirect_ByteBuffer = ARBMultiDrawIndirect::glMultiDrawArraysIndirect;
                glMultiDrawArraysIndirect_IntBuffer = ARBMultiDrawIndirect::glMultiDrawArraysIndirect;
                glMultiDrawElementsIndirect = ARBMultiDrawIndirect::glMultiDrawElementsIndirect;
                glMultiDrawElementsIndirect_ByteBuffer = ARBMultiDrawIndirect::glMultiDrawElementsIndirect;
                glMultiDrawElementsIndirect_IntBuffer = ARBMultiDrawIndirect::glMultiDrawElementsIndirect;
            } else if (cap.GL_AMD_multi_draw_indirect) {
                glMultiDrawArraysIndirect = AMDMultiDrawIndirect::glMultiDrawArraysIndirectAMD;
                glMultiDrawArraysIndirect_ByteBuffer = AMDMultiDrawIndirect::glMultiDrawArraysIndirectAMD;
                glMultiDrawArraysIndirect_IntBuffer = AMDMultiDrawIndirect::glMultiDrawArraysIndirectAMD;
                glMultiDrawElementsIndirect = AMDMultiDrawIndirect::glMultiDrawElementsIndirectAMD;
                glMultiDrawElementsIndirect_ByteBuffer = AMDMultiDrawIndirect::glMultiDrawElementsIndirectAMD;
                glMultiDrawElementsIndirect_IntBuffer = AMDMultiDrawIndirect::glMultiDrawElementsIndirectAMD;
            }
        }

        public final static class MultiTex extends Drawcall {
            private static boolean VALID;

            public static int GL_TEXTURE0;
            public static int GL_TEXTURE1;
            public static int GL_TEXTURE2;
            public static int GL_TEXTURE3;
            public static int GL_TEXTURE4;
            public static int GL_TEXTURE5;
            public static int GL_TEXTURE6;
            public static int GL_TEXTURE7;
            public static int GL_TEXTURE8;
            public static int GL_TEXTURE9;
            public static int GL_TEXTURE10;
            public static int GL_TEXTURE11;
            public static int GL_TEXTURE12;
            public static int GL_TEXTURE13;
            public static int GL_TEXTURE14;
            public static int GL_TEXTURE15;
            public static int GL_TEXTURE16;
            public static int GL_TEXTURE17;
            public static int GL_TEXTURE18;
            public static int GL_TEXTURE19;
            public static int GL_TEXTURE20;
            public static int GL_TEXTURE21;
            public static int GL_TEXTURE22;
            public static int GL_TEXTURE23;
            public static int GL_TEXTURE24;
            public static int GL_TEXTURE25;
            public static int GL_TEXTURE26;
            public static int GL_TEXTURE27;
            public static int GL_TEXTURE28;
            public static int GL_TEXTURE29;
            public static int GL_TEXTURE30;
            public static int GL_TEXTURE31;
            public static int GL_ACTIVE_TEXTURE;
            public static int GL_CLIENT_ACTIVE_TEXTURE;
            public static int GL_MAX_TEXTURE_UNITS;

            private static IntConsumer glClientActiveTexture;
            private static IntConsumer glActiveTexture;
            private static IntFloatFun glMultiTexCoord1f;
            private static IntDoubleFun glMultiTexCoord1d;
            private static IntFloatFloatFun glMultiTexCoord2f;
            private static IntDoubleDoubleFun glMultiTexCoord2d;
            private static IntFloatFloatFloatFun glMultiTexCoord3f;
            private static IntDoubleDoubleDoubleFun glMultiTexCoord3d;
            private static IntFloatFloatFloatFloatFun glMultiTexCoord4f;
            private static IntDoubleDoubleDoubleDoubleFun glMultiTexCoord4d;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL13 || cap.GL_ARB_multitexture;
                if (cap.OpenGL13) {
                    GL_TEXTURE0 = GL13.GL_TEXTURE0;
                    GL_TEXTURE1 = GL13.GL_TEXTURE1;
                    GL_TEXTURE2 = GL13.GL_TEXTURE2;
                    GL_TEXTURE3 = GL13.GL_TEXTURE3;
                    GL_TEXTURE4 = GL13.GL_TEXTURE4;
                    GL_TEXTURE5 = GL13.GL_TEXTURE5;
                    GL_TEXTURE6 = GL13.GL_TEXTURE6;
                    GL_TEXTURE7 = GL13.GL_TEXTURE7;
                    GL_TEXTURE8 = GL13.GL_TEXTURE8;
                    GL_TEXTURE9 = GL13.GL_TEXTURE9;
                    GL_TEXTURE10 = GL13.GL_TEXTURE10;
                    GL_TEXTURE11 = GL13.GL_TEXTURE11;
                    GL_TEXTURE12 = GL13.GL_TEXTURE12;
                    GL_TEXTURE13 = GL13.GL_TEXTURE13;
                    GL_TEXTURE14 = GL13.GL_TEXTURE14;
                    GL_TEXTURE15 = GL13.GL_TEXTURE15;
                    GL_TEXTURE16 = GL13.GL_TEXTURE16;
                    GL_TEXTURE17 = GL13.GL_TEXTURE17;
                    GL_TEXTURE18 = GL13.GL_TEXTURE18;
                    GL_TEXTURE19 = GL13.GL_TEXTURE19;
                    GL_TEXTURE20 = GL13.GL_TEXTURE20;
                    GL_TEXTURE21 = GL13.GL_TEXTURE21;
                    GL_TEXTURE22 = GL13.GL_TEXTURE22;
                    GL_TEXTURE23 = GL13.GL_TEXTURE23;
                    GL_TEXTURE24 = GL13.GL_TEXTURE24;
                    GL_TEXTURE25 = GL13.GL_TEXTURE25;
                    GL_TEXTURE26 = GL13.GL_TEXTURE26;
                    GL_TEXTURE27 = GL13.GL_TEXTURE27;
                    GL_TEXTURE28 = GL13.GL_TEXTURE28;
                    GL_TEXTURE29 = GL13.GL_TEXTURE29;
                    GL_TEXTURE30 = GL13.GL_TEXTURE30;
                    GL_TEXTURE31 = GL13.GL_TEXTURE31;
                    GL_ACTIVE_TEXTURE = GL13.GL_ACTIVE_TEXTURE;
                    GL_CLIENT_ACTIVE_TEXTURE = GL13.GL_CLIENT_ACTIVE_TEXTURE;
                    GL_MAX_TEXTURE_UNITS = GL13.GL_MAX_TEXTURE_UNITS;

                    glClientActiveTexture = GL13::glClientActiveTexture;
                    glActiveTexture = GL13::glActiveTexture;
                    glMultiTexCoord1f = GL13::glMultiTexCoord1f;
                    glMultiTexCoord1d = GL13::glMultiTexCoord1d;
                    glMultiTexCoord2f = GL13::glMultiTexCoord2f;
                    glMultiTexCoord2d = GL13::glMultiTexCoord2d;
                    glMultiTexCoord3f = GL13::glMultiTexCoord3f;
                    glMultiTexCoord3d = GL13::glMultiTexCoord3d;
                    glMultiTexCoord4f = GL13::glMultiTexCoord4f;
                    glMultiTexCoord4d = GL13::glMultiTexCoord4d;
                } else if (cap.GL_ARB_multitexture) {
                    GL_TEXTURE0 = ARBMultitexture.GL_TEXTURE0_ARB;
                    GL_TEXTURE1 = ARBMultitexture.GL_TEXTURE1_ARB;
                    GL_TEXTURE2 = ARBMultitexture.GL_TEXTURE2_ARB;
                    GL_TEXTURE3 = ARBMultitexture.GL_TEXTURE3_ARB;
                    GL_TEXTURE4 = ARBMultitexture.GL_TEXTURE4_ARB;
                    GL_TEXTURE5 = ARBMultitexture.GL_TEXTURE5_ARB;
                    GL_TEXTURE6 = ARBMultitexture.GL_TEXTURE6_ARB;
                    GL_TEXTURE7 = ARBMultitexture.GL_TEXTURE7_ARB;
                    GL_TEXTURE8 = ARBMultitexture.GL_TEXTURE8_ARB;
                    GL_TEXTURE9 = ARBMultitexture.GL_TEXTURE9_ARB;
                    GL_TEXTURE10 = ARBMultitexture.GL_TEXTURE10_ARB;
                    GL_TEXTURE11 = ARBMultitexture.GL_TEXTURE11_ARB;
                    GL_TEXTURE12 = ARBMultitexture.GL_TEXTURE12_ARB;
                    GL_TEXTURE13 = ARBMultitexture.GL_TEXTURE13_ARB;
                    GL_TEXTURE14 = ARBMultitexture.GL_TEXTURE14_ARB;
                    GL_TEXTURE15 = ARBMultitexture.GL_TEXTURE15_ARB;
                    GL_TEXTURE16 = ARBMultitexture.GL_TEXTURE16_ARB;
                    GL_TEXTURE17 = ARBMultitexture.GL_TEXTURE17_ARB;
                    GL_TEXTURE18 = ARBMultitexture.GL_TEXTURE18_ARB;
                    GL_TEXTURE19 = ARBMultitexture.GL_TEXTURE19_ARB;
                    GL_TEXTURE20 = ARBMultitexture.GL_TEXTURE20_ARB;
                    GL_TEXTURE21 = ARBMultitexture.GL_TEXTURE21_ARB;
                    GL_TEXTURE22 = ARBMultitexture.GL_TEXTURE22_ARB;
                    GL_TEXTURE23 = ARBMultitexture.GL_TEXTURE23_ARB;
                    GL_TEXTURE24 = ARBMultitexture.GL_TEXTURE24_ARB;
                    GL_TEXTURE25 = ARBMultitexture.GL_TEXTURE25_ARB;
                    GL_TEXTURE26 = ARBMultitexture.GL_TEXTURE26_ARB;
                    GL_TEXTURE27 = ARBMultitexture.GL_TEXTURE27_ARB;
                    GL_TEXTURE28 = ARBMultitexture.GL_TEXTURE28_ARB;
                    GL_TEXTURE29 = ARBMultitexture.GL_TEXTURE29_ARB;
                    GL_TEXTURE30 = ARBMultitexture.GL_TEXTURE30_ARB;
                    GL_TEXTURE31 = ARBMultitexture.GL_TEXTURE31_ARB;
                    GL_ACTIVE_TEXTURE = ARBMultitexture.GL_ACTIVE_TEXTURE_ARB;
                    GL_CLIENT_ACTIVE_TEXTURE = ARBMultitexture.GL_CLIENT_ACTIVE_TEXTURE_ARB;
                    GL_MAX_TEXTURE_UNITS = ARBMultitexture.GL_MAX_TEXTURE_UNITS_ARB;

                    glClientActiveTexture = ARBMultitexture::glClientActiveTextureARB;
                    glActiveTexture = ARBMultitexture::glActiveTextureARB;
                    glMultiTexCoord1f = ARBMultitexture::glMultiTexCoord1fARB;
                    glMultiTexCoord1d = ARBMultitexture::glMultiTexCoord1dARB;
                    glMultiTexCoord2f = ARBMultitexture::glMultiTexCoord2fARB;
                    glMultiTexCoord2d = ARBMultitexture::glMultiTexCoord2dARB;
                    glMultiTexCoord3f = ARBMultitexture::glMultiTexCoord3fARB;
                    glMultiTexCoord3d = ARBMultitexture::glMultiTexCoord3dARB;
                    glMultiTexCoord4f = ARBMultitexture::glMultiTexCoord4fARB;
                    glMultiTexCoord4d = ARBMultitexture::glMultiTexCoord4dARB;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static void glClientActiveTexture(int texture) {
                glClientActiveTexture.accept(texture);
            }

            public static void glActiveTexture(int texture) {
                glActiveTexture.accept(texture);
            }

            public static void glMultiTexCoord1f(int target, float s) {
                glMultiTexCoord1f.run(target, s);
            }

            public static void glMultiTexCoord1d(int target, double s) {
                glMultiTexCoord1d.run(target, s);
            }

            public static void glMultiTexCoord2f(int target, float s, float t) {
                glMultiTexCoord2f.run(target, s, t);
            }

            public static void glMultiTexCoord2d(int target, double s, double t) {
                glMultiTexCoord2d.run(target, s, t);
            }

            public static void glMultiTexCoord3f(int target, float s, float t, float r) {
                glMultiTexCoord3f.run(target, s, t, r);
            }

            public static void glMultiTexCoord3d(int target, double s, double t, double r) {
                glMultiTexCoord3d.run(target, s, t, r);
            }

            public static void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
                glMultiTexCoord4f.run(target, s, t, r, q);
            }

            public static void glMultiTexCoord4d(int target, double s, double t, double r, double q) {
                glMultiTexCoord4d.run(target, s, t, r, q);
            }
        }
        
        /**
         * {@link GL12#glDrawRangeElements(int, int, int, int, int, long)}<p>
         * {@link GL12#glDrawRangeElements(int, int, int, ByteBuffer)}<p>
         * {@link GL12#glDrawRangeElements(int, int, int, ShortBuffer)}<p>
         * {@link GL12#glDrawRangeElements(int, int, int, IntBuffer)}
         */
        public static boolean valid_DrawRangeElements() {
            return VALID_DrawRangeElements;
        }
        
        /**
         * {@link GL14#glMultiDrawArrays(int, IntBuffer, IntBuffer)}
         */
        public static boolean valid_MultiDraw() {
            return VALID_MultiDraw;
        }

        /**
         * {@link GL31#glDrawArraysInstanced(int, int, int, int)}<p>
         * {@link GL31#glDrawElementsInstanced(int, int, int, long, int)}<p>
         * {@link GL31#glDrawElementsInstanced(int, ByteBuffer, int)}<p>
         * {@link GL31#glDrawElementsInstanced(int, ShortBuffer, int)}<p>
         * {@link GL31#glDrawElementsInstanced(int, IntBuffer, int)}
         */
        public static boolean valid_InstancedDraw() {
            return VALID_InstancedDraw;
        }

        /**
         * {@link GL31#glPrimitiveRestartIndex(int)}
         */
        public static boolean valid_PrimitiveRestart() {
            return VALID_PrimitiveRestart;
        }

        /**
         * {@link GL32#GL_LINES_ADJACENCY}<p>
         * {@link GL32#GL_LINE_STRIP_ADJACENCY}<p>
         * {@link GL32#GL_TRIANGLES_ADJACENCY}<p>
         * {@link GL32#GL_TRIANGLE_STRIP_ADJACENCY}
         */
        public static boolean valid_AdjacencyMode() {
            return VALID_AdjacencyMode;
        }
        
        /**
         * {@link GL32#glDrawElementsBaseVertex(int, int, int, long, int)}<p>
         * {@link GL32#glDrawElementsBaseVertex(int, ByteBuffer, int)}<p>
         * {@link GL32#glDrawElementsBaseVertex(int, ShortBuffer, int)}<p>
         * {@link GL32#glDrawElementsBaseVertex(int, IntBuffer, int)}<p>
         * {@link GL32#glDrawRangeElementsBaseVertex(int, int, int, int, int, long, int)}<p>
         * {@link GL32#glDrawRangeElementsBaseVertex(int, int, int, ByteBuffer, int)}<p>
         * {@link GL32#glDrawRangeElementsBaseVertex(int, int, int, ShortBuffer, int)}<p>
         * {@link GL32#glDrawRangeElementsBaseVertex(int, int, int, IntBuffer, int)}<p>
         * {@link GL32#glDrawElementsInstancedBaseVertex(int, int, int, long, int, int)}<p>
         * {@link GL32#glDrawElementsInstancedBaseVertex(int, ByteBuffer, int, int)}<p>
         * {@link GL32#glDrawElementsInstancedBaseVertex(int, ShortBuffer, int, int)}<p>
         * {@link GL32#glDrawElementsInstancedBaseVertex(int, IntBuffer, int, int)}
         */
        public static boolean valid_DrawElementsBase() {
            return VALID_DrawElementsBase;
        }

        /**
         * {@link GL32#glProvokingVertex(int)}
         */
        public static boolean valid_ProvokingVertex() {
            return VALID_ProvokingVertex;
        }

        /**
         * {@link GL40#glDrawArraysIndirect(int, long)}<p>
         * {@link GL40#glDrawArraysIndirect(int, ByteBuffer)}<p>
         * {@link GL40#glDrawArraysIndirect(int, IntBuffer)}<p>
         * {@link GL40#glDrawElementsIndirect(int, int, long)}<p>
         * {@link GL40#glDrawElementsIndirect(int, int, ByteBuffer)}<p>
         * {@link GL40#glDrawElementsIndirect(int, int, IntBuffer)}
         */
        public static boolean valid_IndirectDraw() {
            return VALID_IndirectDraw;
        }

        /**
         * {@link GL42#glDrawArraysInstancedBaseInstance(int, int, int, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseInstance(int, int, int, long, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseInstance(int, ByteBuffer, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseInstance(int, ShortBuffer, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseInstance(int, IntBuffer, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseVertexBaseInstance(int, int, int, long, int, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseVertexBaseInstance(int, ByteBuffer, int, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseVertexBaseInstance(int, ShortBuffer, int, int, int)}<p>
         * {@link GL42#glDrawElementsInstancedBaseVertexBaseInstance(int, IntBuffer, int, int, int)}<p>
         */
        public static boolean valid_InstancedDrawBase() {
            return VALID_InstancedDrawBase;
        }

        /**
         * {@link GL43#glMultiDrawArraysIndirect(int, long, int, int)}<p>
         * {@link GL43#glMultiDrawArraysIndirect(int, ByteBuffer, int, int)}<p>
         * {@link GL43#glMultiDrawArraysIndirect(int, IntBuffer, int, int)}<p>
         * {@link GL43#glMultiDrawElementsIndirect(int, int, long, int, int)}<p>
         * {@link GL43#glMultiDrawElementsIndirect(int, int, ByteBuffer, int, int)}<p>
         * {@link GL43#glMultiDrawElementsIndirect(int, int, IntBuffer, int, int)}<p>
         */
        public static boolean valid_IndirectMultiDraw() {
            return VALID_IndirectMultiDraw;
        }

        public static void glDrawArrays(int mode, int first, int count) {
            GL11.glDrawArrays(mode, first, count);
        }

        public static void glDrawElements(int mode, int indices_count, int type, long indices_buffer_offset) {
            GL11.glDrawElements(mode, indices_count, type, indices_buffer_offset);
        }

        public static void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
            GL11.glDrawElements(mode, count, type, indices);
        }

        public static void glDrawElements(int mode, ByteBuffer indices) {
            GL11.glDrawElements(mode, indices);
        }

        public static void glDrawElements(int mode, ShortBuffer indices) {
            GL11.glDrawElements(mode, indices);
        }

        public static void glDrawElements(int mode, IntBuffer indices) {
            GL11.glDrawElements(mode, indices);
        }

        public static void glDrawRangeElements(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset) {
            glDrawRangeElements.run(mode, start, end, indices_count, type, indices_buffer_offset);
        }

        public static void glDrawRangeElements(int mode, int start, int end, ByteBuffer indices) {
            glDrawRangeElements_ByteBuffer.run(mode, start, end, indices);
        }

        public static void glDrawRangeElements(int mode, int start, int end, ShortBuffer indices) {
            glDrawRangeElements_ShortBuffer.run(mode, start, end, indices);
        }

        public static void glDrawRangeElements(int mode, int start, int end, IntBuffer indices) {
            glDrawRangeElements_IntBuffer.run(mode, start, end, indices);
        }

        public static void glMultiDrawArrays(int mode, IntBuffer piFirst, IntBuffer piCount) {
            glMultiDrawArrays.run(mode, piFirst, piCount);
        }

        public static void glDrawArraysInstanced(int mode, int first, int count, int primcount) {
            glDrawArraysInstanced.run(mode, first, count, primcount);
        }

        public static void glDrawElementsInstanced(int mode, int indices_count, int type, long indices_buffer_offset, int primcount) {
            glDrawElementsInstanced.run(mode, indices_count, type, indices_count, primcount);
        }

        public static void glDrawElementsInstanced(int mode, ByteBuffer indices, int primcount) {
            glDrawElementsInstanced_ByteBuffer.run(mode, indices, primcount);
        }

        public static void glDrawElementsInstanced(int mode, ShortBuffer indices, int primcount) {
            glDrawElementsInstanced_ShortBuffer.run(mode, indices, primcount);
        }

        public static void glDrawElementsInstanced(int mode, IntBuffer indices, int primcount) {
            glDrawElementsInstanced_IntBuffer.run(mode, indices, primcount);
        }

        public static void glPrimitiveRestartIndex(int index) {
            glPrimitiveRestartIndex.accept(index);
        }

        public static void glDrawElementsBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int basevertex) {
            glDrawElementsBaseVertex.run(mode, indices_count, type, indices_buffer_offset, basevertex);
        }

        public static void glDrawElementsBaseVertex(int mode, ByteBuffer indices, int basevertex) {
            glDrawElementsBaseVertex_ByteBuffer.run(mode, indices, basevertex);
        }

        public static void glDrawElementsBaseVertex(int mode, ShortBuffer indices, int basevertex) {
            glDrawElementsBaseVertex_ShortBuffer.run(mode, indices, basevertex);
        }

        public static void glDrawElementsBaseVertex(int mode, IntBuffer indices, int basevertex) {
            glDrawElementsBaseVertex_IntBuffer.run(mode, indices, basevertex);
        }

        public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, int basevertex) {
            glDrawRangeElementsBaseVertex.run(mode, start, end, indices_count, type, indices_buffer_offset, basevertex);
        }

        public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ByteBuffer indices, int basevertex) {
            glDrawRangeElementsBaseVertex_ByteBuffer.run(mode, start, end, indices, basevertex);
        }

        public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ShortBuffer indices, int basevertex) {
            glDrawRangeElementsBaseVertex_ShortBuffer.run(mode, start, end, indices, basevertex);
        }

        public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, IntBuffer indices, int basevertex) {
            glDrawRangeElementsBaseVertex_IntBuffer.run(mode, start, end, indices, basevertex);
        }

        public static void glDrawElementsInstancedBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex) {
            glDrawElementsInstancedBaseVertex.run(mode, indices_count, type, indices_buffer_offset, primcount, basevertex);
        }

        public static void glDrawElementsInstancedBaseVertex(int mode, ByteBuffer indices, int primcount, int basevertex) {
            glDrawElementsInstancedBaseVertex_ByteBuffer.run(mode, indices, primcount, basevertex);
        }

        public static void glDrawElementsInstancedBaseVertex(int mode, ShortBuffer indices, int primcount, int basevertex) {
            glDrawElementsInstancedBaseVertex_ShortBuffer.run(mode, indices, primcount, basevertex);
        }

        public static void glDrawElementsInstancedBaseVertex(int mode, IntBuffer indices, int primcount, int basevertex) {
            glDrawElementsInstancedBaseVertex_IntBuffer.run(mode, indices, primcount, basevertex);
        }

        public static void glProvokingVertex(int mode) {
            glProvokingVertex.accept(mode);
        }

        public static void glDrawArraysIndirect(int mode, long indirect_buffer_offset) {
            glDrawArraysIndirect.run(mode, indirect_buffer_offset);
        }

        public static void glDrawArraysIndirect(int mode, ByteBuffer indirect) {
            glDrawArraysIndirect_ByteBuffer.run(mode, indirect);
        }

        public static void glDrawArraysIndirect(int mode, IntBuffer indirect) {
            glDrawArraysIndirect_IntBuffer.run(mode, indirect);
        }

        public static void glDrawElementsIndirect(int mode, int type, long indirect_buffer_offset) {
            glDrawElementsIndirect.run(mode, type, indirect_buffer_offset);
        }

        public static void glDrawElementsIndirect(int mode, int type, ByteBuffer indirect) {
            glDrawElementsIndirect_ByteBuffer.run(mode, type, indirect);
        }

        public static void glDrawElementsIndirect(int mode, int type, IntBuffer indirect) {
            glDrawElementsIndirect_IntBuffer.run(mode, type, indirect);
        }

        public static void glDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance) {
            glDrawArraysInstancedBaseInstance.run(mode, first, count, primcount, baseinstance);
        }

        public static void glDrawElementsInstancedBaseInstance(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int baseinstance) {
            glDrawElementsInstancedBaseInstance.run(mode, indices_count, type, indices_buffer_offset, primcount, baseinstance);
        }

        public static void glDrawElementsInstancedBaseInstance(int mode, ByteBuffer indices, int primcount, int baseinstance) {
            glDrawElementsInstancedBaseInstance_ByteBuffer.run(mode, indices, primcount, baseinstance);
        }

        public static void glDrawElementsInstancedBaseInstance(int mode, ShortBuffer indices, int primcount, int baseinstance) {
            glDrawElementsInstancedBaseInstance_ShortBuffer.run(mode, indices, primcount, baseinstance);
        }

        public static void glDrawElementsInstancedBaseInstance(int mode, IntBuffer indices, int primcount, int baseinstance) {
            glDrawElementsInstancedBaseInstance_IntBuffer.run(mode, indices, primcount, baseinstance);
        }

        public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex, int baseinstance) {
            glDrawElementsInstancedBaseVertexBaseInstance.run(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, baseinstance);
        }

        public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, ByteBuffer indices, int primcount, int basevertex, int baseinstance) {
            glDrawElementsInstancedBaseVertexBaseInstance_ByteBuffer.run(mode, indices, primcount, basevertex, baseinstance);
        }

        public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, ShortBuffer indices, int primcount, int basevertex, int baseinstance) {
            glDrawElementsInstancedBaseVertexBaseInstance_ShortBuffer.run(mode, indices, primcount, basevertex, baseinstance);
        }

        public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, IntBuffer indices, int primcount, int basevertex, int baseinstance) {
            glDrawElementsInstancedBaseVertexBaseInstance_IntBuffer.run(mode, indices, primcount, basevertex, baseinstance);
        }

        public static void glMultiDrawArraysIndirect(int mode, long indirect_buffer_offset, int primcount, int stride) {
            glMultiDrawArraysIndirect.run(mode, indirect_buffer_offset, primcount, stride);
        }

        public static void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
            glMultiDrawArraysIndirect_ByteBuffer.run(mode, indirect, primcount, stride);
        }

        public static void glMultiDrawArraysIndirect(int mode, IntBuffer indirect, int primcount, int stride) {
            glMultiDrawArraysIndirect_IntBuffer.run(mode, indirect, primcount, stride);
        }

        public static void glMultiDrawElementsIndirect(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
            glMultiDrawElementsIndirect.run(mode, type, indirect_buffer_offset, primcount, stride);
        }

        public static void glMultiDrawElementsIndirect(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
            glMultiDrawElementsIndirect_ByteBuffer.run(mode, type, indirect, primcount, stride);
        }

        public static void glMultiDrawElementsIndirect(int mode, int type, IntBuffer indirect, int primcount, int stride) {
            glMultiDrawElementsIndirect_IntBuffer.run(mode, type, indirect, primcount, stride);
        }

        private Drawcall() {}
    }

    public sealed static class Buffer permits Buffer.VBO, Buffer.PBO, Buffer.TransformFB, Buffer.TBO, Buffer.UBO, Buffer.Indirect, Buffer.SSBO {
        private static boolean VALID;
        private static boolean VALID_MapRange;
        private static boolean VALID_BindRangeBase;
        private static boolean VALID_CopyBuffer;
        private static boolean VALID_Invalidate;
        private static boolean VALID_ClearBuffer;
        private static boolean VALID_BufferStorage;
        private static boolean VALID_MultiBind;

        public static int GL_STATIC_READ;
        public static int GL_STATIC_COPY;
        public static int GL_STATIC_DRAW;
        public static int GL_STREAM_READ;
        public static int GL_STREAM_COPY;
        public static int GL_STREAM_DRAW;
        public static int GL_DYNAMIC_READ;
        public static int GL_DYNAMIC_COPY;
        public static int GL_DYNAMIC_DRAW;

        public static int GL_MAP_READ_BIT;
        public static int GL_MAP_WRITE_BIT;
        public static int GL_MAP_INVALIDATE_RANGE_BIT;
        public static int GL_MAP_INVALIDATE_BUFFER_BIT;
        public static int GL_MAP_FLUSH_EXPLICIT_BIT;
        public static int GL_MAP_UNSYNCHRONIZED_BIT;

        public static int GL_COPY_READ_BUFFER;
        public static int GL_COPY_WRITE_BUFFER;
        public static int GL_COPY_READ_BUFFER_BINDING;
        public static int GL_COPY_WRITE_BUFFER_BINDING;

        public static int GL_MAP_PERSISTENT_BIT;
        public static int GL_MAP_COHERENT_BIT;
        public static int GL_DYNAMIC_STORAGE_BIT;
        public static int GL_CLIENT_STORAGE_BIT;
        public static int GL_BUFFER_IMMUTABLE_STORAGE;
        public static int GL_BUFFER_STORAGE_FLAGS;
        public static int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;

        private static IntSupplier glGenBuffers;
        private static Consumer<IntBuffer> glGenBuffers_IntBuffer;
        private static IntIntFun glBindBuffer;
        private static IntPredicate glUnmapBuffer;
        private static IntConsumer glDeleteBuffers;
        private static Consumer<IntBuffer> glDeleteBuffers_IntBuffer;

        private static IntLongIntFun glBufferData;
        private static IntXIntFun<ByteBuffer> glBufferData_ByteBuffer;
        private static IntXIntFun<ShortBuffer> glBufferData_ShortBuffer;
        private static IntXIntFun<IntBuffer> glBufferData_IntBuffer;
        private static IntXIntFun<FloatBuffer> glBufferData_FloatBuffer;
        private static IntXIntFun<DoubleBuffer> glBufferData_DoubleBuffer;

        private static IntLongXFun<ByteBuffer> glBufferSubData_ByteBuffer;
        private static IntLongXFun<ShortBuffer> glBufferSubData_ShortBuffer;
        private static IntLongXFun<IntBuffer> glBufferSubData_IntBuffer;
        private static IntLongXFun<FloatBuffer> glBufferSubData_FloatBuffer;
        private static IntLongXFun<DoubleBuffer> glBufferSubData_DoubleBuffer;

        private static IntLongXFun<ByteBuffer> glGetBufferSubData_ByteBuffer;
        private static IntLongXFun<ShortBuffer> glGetBufferSubData_ShortBuffer;
        private static IntLongXFun<IntBuffer> glGetBufferSubData_IntBuffer;
        private static IntLongXFun<FloatBuffer> glGetBufferSubData_FloatBuffer;
        private static IntLongXFun<DoubleBuffer> glGetBufferSubData_DoubleBuffer;

        private static IntLongLongIntXFunY<ByteBuffer, ByteBuffer> glMapBufferRange;
        private static IntLongLongFun glFlushMappedBufferRange;

        private static IntIntIntLongLongFun glBindBufferRange;
        private static IntIntIntFun glBindBufferBase;

        private static IntIntLongLongLongFun glCopyBufferSubData;

        private static IntConsumer glInvalidateBufferData;
        private static IntLongLongFun glInvalidateBufferSubData;

        private static IntIntIntIntXFun<ByteBuffer> glClearBufferData;
        private static IntIntLongLongIntIntXFun<ByteBuffer> glClearBufferSubData;

        private static IntLongIntFun glBufferStorage;
        private static IntXIntFun<ByteBuffer> glBufferStorage_ByteBuffer;
        private static IntXIntFun<ShortBuffer> glBufferStorage_ShortBuffer;
        private static IntXIntFun<IntBuffer> glBufferStorage_IntBuffer;
        private static IntXIntFun<FloatBuffer> glBufferStorage_FloatBuffer;
        private static IntXIntFun<DoubleBuffer> glBufferStorage_DoubleBuffer;

        private static IntIntIntXFun<IntBuffer> glBindBuffersBase;
        private static IntIntIntXYZFun<IntBuffer, PointerBuffer, PointerBuffer> glBindBuffersRange;

        private static void init(ContextCapabilities cap) {
            VALID = cap.OpenGL15 || cap.GL_ARB_vertex_buffer_object;
            if (cap.OpenGL15) {
                GL_STATIC_READ = GL15.GL_STATIC_READ;
                GL_STATIC_COPY = GL15.GL_STATIC_COPY;
                GL_STATIC_DRAW = GL15.GL_STATIC_DRAW;
                GL_STREAM_READ = GL15.GL_STREAM_READ;
                GL_STREAM_COPY = GL15.GL_STREAM_COPY;
                GL_STREAM_DRAW = GL15.GL_STREAM_DRAW;
                GL_DYNAMIC_READ = GL15.GL_DYNAMIC_READ;
                GL_DYNAMIC_COPY = GL15.GL_DYNAMIC_COPY;
                GL_DYNAMIC_DRAW = GL15.GL_DYNAMIC_DRAW;

                glGenBuffers = GL15::glGenBuffers;
                glGenBuffers_IntBuffer = GL15::glGenBuffers;
                glBindBuffer = GL15::glBindBuffer;
                glUnmapBuffer = GL15::glUnmapBuffer;
                glDeleteBuffers = GL15::glDeleteBuffers;
                glDeleteBuffers_IntBuffer = GL15::glDeleteBuffers;

                glBufferData = GL15::glBufferData;
                glBufferData_ByteBuffer = GL15::glBufferData;
                glBufferData_ShortBuffer = GL15::glBufferData;
                glBufferData_IntBuffer = GL15::glBufferData;
                glBufferData_FloatBuffer = GL15::glBufferData;
                glBufferData_DoubleBuffer = GL15::glBufferData;

                glBufferSubData_ByteBuffer = GL15::glBufferSubData;
                glBufferSubData_ShortBuffer = GL15::glBufferSubData;
                glBufferSubData_IntBuffer = GL15::glBufferSubData;
                glBufferSubData_FloatBuffer = GL15::glBufferSubData;
                glBufferSubData_DoubleBuffer = GL15::glBufferSubData;

                glGetBufferSubData_ByteBuffer = GL15::glGetBufferSubData;
                glGetBufferSubData_ShortBuffer = GL15::glGetBufferSubData;
                glGetBufferSubData_IntBuffer = GL15::glGetBufferSubData;
                glGetBufferSubData_FloatBuffer = GL15::glGetBufferSubData;
                glGetBufferSubData_DoubleBuffer = GL15::glGetBufferSubData;
            } else if (cap.GL_ARB_vertex_buffer_object) {
                GL_STATIC_READ = ARBVertexBufferObject.GL_STATIC_READ_ARB;
                GL_STATIC_COPY = ARBVertexBufferObject.GL_STATIC_COPY_ARB;
                GL_STATIC_DRAW = ARBVertexBufferObject.GL_STATIC_DRAW_ARB;
                GL_STREAM_READ = ARBVertexBufferObject.GL_STREAM_READ_ARB;
                GL_STREAM_COPY = ARBVertexBufferObject.GL_STREAM_COPY_ARB;
                GL_STREAM_DRAW = ARBVertexBufferObject.GL_STREAM_DRAW_ARB;
                GL_DYNAMIC_READ = ARBVertexBufferObject.GL_DYNAMIC_READ_ARB;
                GL_DYNAMIC_COPY = ARBVertexBufferObject.GL_DYNAMIC_COPY_ARB;
                GL_DYNAMIC_DRAW = ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB;

                glGenBuffers = ARBVertexBufferObject::glGenBuffersARB;
                glGenBuffers_IntBuffer = ARBVertexBufferObject::glGenBuffersARB;
                glBindBuffer = ARBVertexBufferObject::glBindBufferARB;
                glUnmapBuffer = ARBVertexBufferObject::glUnmapBufferARB;
                glDeleteBuffers = ARBVertexBufferObject::glDeleteBuffersARB;
                glDeleteBuffers_IntBuffer = ARBVertexBufferObject::glDeleteBuffersARB;

                glBufferData = ARBVertexBufferObject::glBufferDataARB;
                glBufferData_ByteBuffer = ARBVertexBufferObject::glBufferDataARB;
                glBufferData_ShortBuffer = ARBVertexBufferObject::glBufferDataARB;
                glBufferData_IntBuffer = ARBVertexBufferObject::glBufferDataARB;
                glBufferData_FloatBuffer = ARBVertexBufferObject::glBufferDataARB;
                glBufferData_DoubleBuffer = ARBVertexBufferObject::glBufferDataARB;

                glBufferSubData_ByteBuffer = ARBVertexBufferObject::glBufferSubDataARB;
                glBufferSubData_ShortBuffer = ARBVertexBufferObject::glBufferSubDataARB;
                glBufferSubData_IntBuffer = ARBVertexBufferObject::glBufferSubDataARB;
                glBufferSubData_FloatBuffer = ARBVertexBufferObject::glBufferSubDataARB;
                glBufferSubData_DoubleBuffer = ARBVertexBufferObject::glBufferSubDataARB;

                glGetBufferSubData_ByteBuffer = ARBVertexBufferObject::glGetBufferSubDataARB;
                glGetBufferSubData_ShortBuffer = ARBVertexBufferObject::glGetBufferSubDataARB;
                glGetBufferSubData_IntBuffer = ARBVertexBufferObject::glGetBufferSubDataARB;
                glGetBufferSubData_FloatBuffer = ARBVertexBufferObject::glGetBufferSubDataARB;
                glGetBufferSubData_DoubleBuffer = ARBVertexBufferObject::glGetBufferSubDataARB;
            }

            VALID_MapRange = cap.OpenGL30 || cap.GL_ARB_map_buffer_range;
            if (cap.OpenGL30) {
                GL_MAP_READ_BIT = GL30.GL_MAP_READ_BIT;
                GL_MAP_WRITE_BIT = GL30.GL_MAP_WRITE_BIT;
                GL_MAP_INVALIDATE_RANGE_BIT = GL30.GL_MAP_INVALIDATE_RANGE_BIT;
                GL_MAP_INVALIDATE_BUFFER_BIT = GL30.GL_MAP_INVALIDATE_BUFFER_BIT;
                GL_MAP_FLUSH_EXPLICIT_BIT = GL30.GL_MAP_FLUSH_EXPLICIT_BIT;
                GL_MAP_UNSYNCHRONIZED_BIT = GL30.GL_MAP_UNSYNCHRONIZED_BIT;

                glMapBufferRange = GL30::glMapBufferRange;
                glFlushMappedBufferRange = GL30::glFlushMappedBufferRange;
            } else if (cap.GL_ARB_map_buffer_range) {
                GL_MAP_READ_BIT = ARBMapBufferRange.GL_MAP_READ_BIT;
                GL_MAP_WRITE_BIT = ARBMapBufferRange.GL_MAP_WRITE_BIT;
                GL_MAP_INVALIDATE_RANGE_BIT = ARBMapBufferRange.GL_MAP_INVALIDATE_RANGE_BIT;
                GL_MAP_INVALIDATE_BUFFER_BIT = ARBMapBufferRange.GL_MAP_INVALIDATE_BUFFER_BIT;
                GL_MAP_FLUSH_EXPLICIT_BIT = ARBMapBufferRange.GL_MAP_FLUSH_EXPLICIT_BIT;
                GL_MAP_UNSYNCHRONIZED_BIT = ARBMapBufferRange.GL_MAP_UNSYNCHRONIZED_BIT;

                glMapBufferRange = ARBMapBufferRange::glMapBufferRange;
                glFlushMappedBufferRange = ARBMapBufferRange::glFlushMappedBufferRange;
            }

            VALID_BindRangeBase = cap.OpenGL30 || cap.GL_ARB_uniform_buffer_object || cap.GL_EXT_transform_feedback || cap.GL_NV_transform_feedback;
            if (cap.OpenGL30) {
                glBindBufferRange = GL30::glBindBufferRange;
                glBindBufferBase = GL30::glBindBufferBase;
            } else if (cap.GL_ARB_uniform_buffer_object) {
                glBindBufferRange = ARBUniformBufferObject::glBindBufferRange;
                glBindBufferBase = ARBUniformBufferObject::glBindBufferBase;
            } else if (cap.GL_EXT_transform_feedback) {
                glBindBufferRange = EXTTransformFeedback::glBindBufferRangeEXT;
                glBindBufferBase = EXTTransformFeedback::glBindBufferBaseEXT;
            } else if (cap.GL_NV_transform_feedback) {
                glBindBufferRange = NVTransformFeedback::glBindBufferRangeNV;
                glBindBufferBase = NVTransformFeedback::glBindBufferBaseNV;
            }

            VALID_CopyBuffer = cap.OpenGL31 || cap.GL_ARB_copy_buffer;
            if (cap.OpenGL31) {
                GL_COPY_READ_BUFFER = GL31.GL_COPY_READ_BUFFER;
                GL_COPY_WRITE_BUFFER = GL31.GL_COPY_WRITE_BUFFER;
                GL_COPY_READ_BUFFER_BINDING = GL31.GL_COPY_READ_BUFFER_BINDING;
                GL_COPY_WRITE_BUFFER_BINDING = GL31.GL_COPY_WRITE_BUFFER_BINDING;

                glCopyBufferSubData = GL31::glCopyBufferSubData;
            } else if (cap.GL_ARB_copy_buffer) {
                GL_COPY_READ_BUFFER = ARBCopyBuffer.GL_COPY_READ_BUFFER;
                GL_COPY_WRITE_BUFFER = ARBCopyBuffer.GL_COPY_WRITE_BUFFER;
                GL_COPY_READ_BUFFER_BINDING = GL31.GL_COPY_READ_BUFFER_BINDING;
                GL_COPY_WRITE_BUFFER_BINDING = GL31.GL_COPY_WRITE_BUFFER_BINDING;

                glCopyBufferSubData = ARBCopyBuffer::glCopyBufferSubData;
            }

            VALID_Invalidate = cap.OpenGL43 || cap.GL_ARB_invalidate_subdata;
            if (cap.OpenGL43) {
                glInvalidateBufferData = GL43::glInvalidateBufferData;
                glInvalidateBufferSubData = GL43::glInvalidateBufferSubData;
            } else if (cap.GL_ARB_invalidate_subdata) {
                glInvalidateBufferData = ARBInvalidateSubdata::glInvalidateBufferData;
                glInvalidateBufferSubData = ARBInvalidateSubdata::glInvalidateBufferSubData;
            }

            VALID_ClearBuffer = cap.OpenGL43 || cap.GL_ARB_clear_buffer_object;
            if (cap.OpenGL43) {
                glClearBufferData = GL43::glClearBufferData;
                glClearBufferSubData = GL43::glClearBufferSubData;
            } else if (cap.GL_ARB_clear_buffer_object) {
                glClearBufferData = ARBClearBufferObject::glClearBufferData;
                glClearBufferSubData = ARBClearBufferObject::glClearBufferSubData;
            }

            VALID_BufferStorage = cap.OpenGL44 || cap.GL_ARB_buffer_storage;
            if (cap.OpenGL44) {
                GL_MAP_PERSISTENT_BIT = GL44.GL_MAP_PERSISTENT_BIT;
                GL_MAP_COHERENT_BIT = GL44.GL_MAP_COHERENT_BIT;
                GL_DYNAMIC_STORAGE_BIT = GL44.GL_DYNAMIC_STORAGE_BIT;
                GL_CLIENT_STORAGE_BIT = GL44.GL_CLIENT_STORAGE_BIT;
                GL_BUFFER_IMMUTABLE_STORAGE = GL44.GL_BUFFER_IMMUTABLE_STORAGE;
                GL_BUFFER_STORAGE_FLAGS = GL44.GL_BUFFER_STORAGE_FLAGS;
                GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = GL44.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;

                glBufferStorage = GL44::glBufferStorage;
                glBufferStorage_ByteBuffer = GL44::glBufferStorage;
                glBufferStorage_ShortBuffer = GL44::glBufferStorage;
                glBufferStorage_IntBuffer = GL44::glBufferStorage;
                glBufferStorage_FloatBuffer = GL44::glBufferStorage;
                glBufferStorage_DoubleBuffer = GL44::glBufferStorage;
            } else if (cap.GL_ARB_buffer_storage) {
                GL_MAP_PERSISTENT_BIT = ARBBufferStorage.GL_MAP_PERSISTENT_BIT;
                GL_MAP_COHERENT_BIT = ARBBufferStorage.GL_MAP_COHERENT_BIT;
                GL_DYNAMIC_STORAGE_BIT = ARBBufferStorage.GL_DYNAMIC_STORAGE_BIT;
                GL_CLIENT_STORAGE_BIT = ARBBufferStorage.GL_CLIENT_STORAGE_BIT;
                GL_BUFFER_IMMUTABLE_STORAGE = ARBBufferStorage.GL_BUFFER_IMMUTABLE_STORAGE;
                GL_BUFFER_STORAGE_FLAGS = ARBBufferStorage.GL_BUFFER_STORAGE_FLAGS;
                GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = ARBBufferStorage.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;

                glBufferStorage = ARBBufferStorage::glBufferStorage;
                glBufferStorage_ByteBuffer = ARBBufferStorage::glBufferStorage;
                glBufferStorage_ShortBuffer = ARBBufferStorage::glBufferStorage;
                glBufferStorage_IntBuffer = ARBBufferStorage::glBufferStorage;
                glBufferStorage_FloatBuffer = ARBBufferStorage::glBufferStorage;
                glBufferStorage_DoubleBuffer = ARBBufferStorage::glBufferStorage;
            }

            VALID_MultiBind = cap.OpenGL44 || cap.GL_ARB_multi_bind;
            if (cap.OpenGL44) {
                glBindBuffersBase = GL44::glBindBuffersBase;
                glBindBuffersRange = GL44::glBindBuffersRange;
            } else if (cap.GL_ARB_multi_bind) {
                glBindBuffersBase = ARBMultiBind::glBindBuffersBase;
                glBindBuffersRange = ARBMultiBind::glBindBuffersRange;
            }
        }

        public final static class VBO extends Buffer {
            private static boolean VALID;

            public static int GL_ARRAY_BUFFER;
            public static int GL_ARRAY_BUFFER_BINDING;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid();
                if (cap.OpenGL15) {
                    GL_ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER;
                    GL_ARRAY_BUFFER_BINDING = GL15.GL_ARRAY_BUFFER_BINDING;
                } else if (cap.GL_ARB_vertex_buffer_object) {
                    GL_ARRAY_BUFFER = ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
                    GL_ARRAY_BUFFER_BINDING = ARBVertexBufferObject.GL_ARRAY_BUFFER_BINDING_ARB;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            private VBO() {}
        }

        public final static class PBO extends Buffer {
            private static boolean VALID;

            public static int GL_PIXEL_PACK_BUFFER;
            public static int GL_PIXEL_UNPACK_BUFFER;
            public static int GL_PIXEL_PACK_BUFFER_BINDING;
            public static int GL_PIXEL_UNPACK_BUFFER_BINDING;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid() && (cap.OpenGL21 || cap.GL_ARB_pixel_buffer_object || cap.GL_EXT_pixel_buffer_object);
                if (cap.OpenGL21) {
                    GL_PIXEL_PACK_BUFFER = GL21.GL_PIXEL_PACK_BUFFER;
                    GL_PIXEL_UNPACK_BUFFER = GL21.GL_PIXEL_UNPACK_BUFFER;
                    GL_PIXEL_PACK_BUFFER_BINDING = GL21.GL_PIXEL_PACK_BUFFER_BINDING;
                    GL_PIXEL_UNPACK_BUFFER_BINDING = GL21.GL_PIXEL_UNPACK_BUFFER_BINDING;
                } else if (cap.GL_ARB_pixel_buffer_object) {
                    GL_PIXEL_PACK_BUFFER = ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB;
                    GL_PIXEL_UNPACK_BUFFER = ARBPixelBufferObject.GL_PIXEL_UNPACK_BUFFER_ARB;
                    GL_PIXEL_PACK_BUFFER_BINDING = ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_ARB;
                    GL_PIXEL_UNPACK_BUFFER_BINDING = ARBPixelBufferObject.GL_PIXEL_UNPACK_BUFFER_BINDING_ARB;
                } else if (cap.GL_EXT_pixel_buffer_object) {
                    GL_PIXEL_PACK_BUFFER = EXTPixelBufferObject.GL_PIXEL_PACK_BUFFER_EXT;
                    GL_PIXEL_UNPACK_BUFFER = EXTPixelBufferObject.GL_PIXEL_UNPACK_BUFFER_EXT;
                    GL_PIXEL_PACK_BUFFER_BINDING = EXTPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_EXT;
                    GL_PIXEL_UNPACK_BUFFER_BINDING = EXTPixelBufferObject.GL_PIXEL_UNPACK_BUFFER_BINDING_EXT;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            private PBO() {}
        }

        /**
         * Transform feedback
         */
        public final static class TransformFB extends Buffer {
            private static boolean VALID;
            private static boolean VALID_FB2;
            private static boolean VALID_StreamDraw;
            private static boolean VALID_InstancedDraw;

            public static int GL_TRANSFORM_FEEDBACK_BUFFER;
            public static int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING;
            public static int GL_INTERLEAVED_ATTRIBS;
            public static int GL_SEPARATE_ATTRIBS;
            public static int GL_RASTERIZER_DISCARD;

            public static int GL_TRANSFORM_FEEDBACK;
            public static int GL_TRANSFORM_FEEDBACK_BINDING;

            private static IntConsumer glBeginTransformFeedback;
            private static Runnable glEndTransformFeedback;
            private static IntIntXIntFun<ByteBuffer> glTransformFeedbackVaryings;
            private static IntXIntFun<CharSequence[]> glTransformFeedbackVaryings_stra;

            private static IntIntFun glBindTransformFeedback;
            private static IntConsumer glDeleteTransformFeedbacks;
            private static Consumer<IntBuffer> glDeleteTransformFeedbacks_buf;
            private static IntSupplier glGenTransformFeedbacks;
            private static Consumer<IntBuffer> glGenTransformFeedbacks_buf;
            private static Runnable glPauseTransformFeedback;
            private static Runnable glResumeTransformFeedback;
            private static IntIntFun glDrawTransformFeedback;

            private static IntIntIntFun glDrawTransformFeedbackStream;

            private static IntIntIntFun glDrawTransformFeedbackInstanced;
            private static IntIntIntIntFun glDrawTransformFeedbackStreamInstanced;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL30 || cap.GL_EXT_transform_feedback; // NV must be set varyings after link, out
                if (cap.OpenGL30) {
                    GL_TRANSFORM_FEEDBACK_BUFFER = GL30.GL_TRANSFORM_FEEDBACK_BUFFER;
                    GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = GL30.GL_TRANSFORM_FEEDBACK_BUFFER_BINDING;
                    GL_INTERLEAVED_ATTRIBS = GL30.GL_INTERLEAVED_ATTRIBS;
                    GL_SEPARATE_ATTRIBS = GL30.GL_SEPARATE_ATTRIBS;
                    GL_RASTERIZER_DISCARD = GL30.GL_RASTERIZER_DISCARD;

                    glBeginTransformFeedback = GL30::glBeginTransformFeedback;
                    glEndTransformFeedback = GL30::glEndTransformFeedback;
                    glTransformFeedbackVaryings = GL30::glTransformFeedbackVaryings;
                    glTransformFeedbackVaryings_stra = GL30::glTransformFeedbackVaryings;
                } else if (cap.GL_EXT_transform_feedback) {
                    GL_TRANSFORM_FEEDBACK_BUFFER = EXTTransformFeedback.GL_TRANSFORM_FEEDBACK_BUFFER_EXT;
                    GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = EXTTransformFeedback.GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT;
                    GL_INTERLEAVED_ATTRIBS = EXTTransformFeedback.GL_INTERLEAVED_ATTRIBS_EXT;
                    GL_SEPARATE_ATTRIBS = EXTTransformFeedback.GL_SEPARATE_ATTRIBS_EXT;
                    GL_RASTERIZER_DISCARD = EXTTransformFeedback.GL_RASTERIZER_DISCARD_EXT;

                    glBeginTransformFeedback = EXTTransformFeedback::glBeginTransformFeedbackEXT;
                    glEndTransformFeedback = EXTTransformFeedback::glEndTransformFeedbackEXT;
                    glTransformFeedbackVaryings = EXTTransformFeedback::glTransformFeedbackVaryingsEXT;
                    glTransformFeedbackVaryings_stra = EXTTransformFeedback::glTransformFeedbackVaryingsEXT;
                }

                VALID_FB2 = cap.OpenGL40 || cap.GL_ARB_transform_feedback2 || cap.GL_NV_transform_feedback2;
                if (cap.OpenGL40) {
                    GL_TRANSFORM_FEEDBACK = GL40.GL_TRANSFORM_FEEDBACK;
                    GL_TRANSFORM_FEEDBACK_BINDING = GL40.GL_TRANSFORM_FEEDBACK_BINDING;

                    glBindTransformFeedback = GL40::glBindTransformFeedback;
                    glDeleteTransformFeedbacks = GL40::glDeleteTransformFeedbacks;
                    glDeleteTransformFeedbacks_buf = GL40::glDeleteTransformFeedbacks;
                    glGenTransformFeedbacks = GL40::glGenTransformFeedbacks;
                    glGenTransformFeedbacks_buf = GL40::glGenTransformFeedbacks;
                    glPauseTransformFeedback = GL40::glPauseTransformFeedback;
                    glResumeTransformFeedback = GL40::glResumeTransformFeedback;
                    glDrawTransformFeedback = GL40::glDrawTransformFeedback;
                } else if (cap.GL_ARB_transform_feedback2) {
                    GL_TRANSFORM_FEEDBACK = ARBTransformFeedback2.GL_TRANSFORM_FEEDBACK;
                    GL_TRANSFORM_FEEDBACK_BINDING = ARBTransformFeedback2.GL_TRANSFORM_FEEDBACK_BINDING;

                    glBindTransformFeedback = ARBTransformFeedback2::glBindTransformFeedback;
                    glDeleteTransformFeedbacks = ARBTransformFeedback2::glDeleteTransformFeedbacks;
                    glDeleteTransformFeedbacks_buf = ARBTransformFeedback2::glDeleteTransformFeedbacks;
                    glGenTransformFeedbacks = ARBTransformFeedback2::glGenTransformFeedbacks;
                    glGenTransformFeedbacks_buf = ARBTransformFeedback2::glGenTransformFeedbacks;
                    glPauseTransformFeedback = ARBTransformFeedback2::glPauseTransformFeedback;
                    glResumeTransformFeedback = ARBTransformFeedback2::glResumeTransformFeedback;
                    glDrawTransformFeedback = ARBTransformFeedback2::glDrawTransformFeedback;
                } else if (cap.GL_NV_transform_feedback2) {
                    GL_TRANSFORM_FEEDBACK = NVTransformFeedback2.GL_TRANSFORM_FEEDBACK_NV;
                    GL_TRANSFORM_FEEDBACK_BINDING = NVTransformFeedback2.GL_TRANSFORM_FEEDBACK_BINDING_NV;

                    glBindTransformFeedback = NVTransformFeedback2::glBindTransformFeedbackNV;
                    glDeleteTransformFeedbacks = NVTransformFeedback2::glDeleteTransformFeedbacksNV;
                    glDeleteTransformFeedbacks_buf = NVTransformFeedback2::glDeleteTransformFeedbacksNV;
                    glGenTransformFeedbacks = NVTransformFeedback2::glGenTransformFeedbacksNV;
                    glGenTransformFeedbacks_buf = NVTransformFeedback2::glGenTransformFeedbacksNV;
                    glPauseTransformFeedback = NVTransformFeedback2::glPauseTransformFeedbackNV;
                    glResumeTransformFeedback = NVTransformFeedback2::glResumeTransformFeedbackNV;
                    glDrawTransformFeedback = NVTransformFeedback2::glDrawTransformFeedbackNV;
                }

                VALID_StreamDraw = cap.OpenGL40 || cap.GL_ARB_transform_feedback3;
                if (cap.OpenGL40) {
                    glDrawTransformFeedbackStream = GL40::glDrawTransformFeedbackStream;
                } else if (cap.GL_ARB_transform_feedback3) {
                    glDrawTransformFeedbackStream = ARBTransformFeedback3::glDrawTransformFeedbackStream;
                }

                VALID_InstancedDraw = cap.OpenGL42 || cap.GL_ARB_transform_feedback_instanced;
                if (cap.OpenGL42) {
                    glDrawTransformFeedbackInstanced = GL42::glDrawTransformFeedbackInstanced;
                    glDrawTransformFeedbackStreamInstanced = GL42::glDrawTransformFeedbackStreamInstanced;
                } else if (cap.GL_ARB_transform_feedback_instanced) {
                    glDrawTransformFeedbackInstanced = ARBTransformFeedbackInstanced::glDrawTransformFeedbackInstanced;
                    glDrawTransformFeedbackStreamInstanced = ARBTransformFeedbackInstanced::glDrawTransformFeedbackStreamInstanced;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            /**
             * {@link GL40#glBindTransformFeedback(int, int)}<p>
             * {@link GL40#glDeleteTransformFeedbacks(int)}<p>
             * {@link GL40#glDeleteTransformFeedbacks(IntBuffer)}<p>
             * {@link GL40#glGenTransformFeedbacks()}<p>
             * {@link GL40#glGenTransformFeedbacks(IntBuffer)}<p>
             * {@link GL40#glPauseTransformFeedback()}<p>
             * {@link GL40#glResumeTransformFeedback()}<p>
             * {@link GL40#glDrawTransformFeedback(int, int)}<p>
             */
            public static boolean valid_FB2() {
                return VALID_FB2;
            }

            /**
             * {@link GL40#glDrawTransformFeedbackStream(int, int, int)}
             */
            public static boolean valid_StreamDraw() {
                return VALID_StreamDraw;
            }

            /**
             * {@link GL42#glDrawTransformFeedbackInstanced(int, int, int)}<p>
             * {@link GL42#glDrawTransformFeedbackStreamInstanced(int, int, int, int)}
             */
            public static boolean valid_InstancedDraw() {
                return VALID_InstancedDraw;
            }

            public static void glBindTransformFeedback(int target, int id) {
                glBindTransformFeedback.run(target, id);
            }

            public static void glDeleteTransformFeedbacks(int id) {
                glDeleteTransformFeedbacks.accept(id);
            }

            public static void glDeleteTransformFeedbacks(IntBuffer ids) {
                glDeleteTransformFeedbacks_buf.accept(ids);
            }

            public static int glGenTransformFeedbacks() {
                return glGenTransformFeedbacks.getAsInt();
            }

            public static void glGenTransformFeedbacks(IntBuffer ids) {
                glGenTransformFeedbacks_buf.accept(ids);
            }

            public static void glPauseTransformFeedback() {
                glPauseTransformFeedback.run();
            }

            public static void glResumeTransformFeedback() {
                glResumeTransformFeedback.run();
            }

            public static void glDrawTransformFeedback(int mode, int id) {
                glDrawTransformFeedback.run(mode, id);
            }

            public static void glBeginTransformFeedback(int primitiveMode) {
                glBeginTransformFeedback.accept(primitiveMode);
            }

            public static void glEndTransformFeedback() {
                glEndTransformFeedback.run();
            }

            public static void glTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int bufferMode) {
                glTransformFeedbackVaryings.run(program, count, varyings, bufferMode);
            }

            public static void glTransformFeedbackVaryings(int program, CharSequence[] varyings, int bufferMode) {
                glTransformFeedbackVaryings_stra.run(program, varyings, bufferMode);
            }

            public static void glDrawTransformFeedbackStream(int mode, int id, int stream) {
                glDrawTransformFeedbackStream.run(mode, id, stream);
            }

            public static void glDrawTransformFeedbackInstanced(int mode, int id, int primcount) {
                glDrawTransformFeedbackInstanced.run(mode, id, primcount);
            }

            public static void glDrawTransformFeedbackStreamInstanced(int mode, int id, int stream, int primcount) {
                glDrawTransformFeedbackStreamInstanced.run(mode, id, stream, primcount);
            }
        }

        public final static class TBO extends Buffer {
            private static boolean VALID;
            private static boolean VALID_TexBufferRange;

            public static int GL_TEXTURE_BUFFER;
            public static int GL_MAX_TEXTURE_BUFFER_SIZE;
            public static int GL_TEXTURE_BINDING_BUFFER;

            private static IntIntIntFun glTexBuffer;

            private static IntIntIntLongLongFun glTexBufferRange;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid() && (cap.OpenGL31 || cap.GL_ARB_texture_buffer_object || cap.GL_EXT_texture_buffer_object);
                if (cap.OpenGL31) {
                    GL_TEXTURE_BUFFER = GL31.GL_TEXTURE_BUFFER;
                    GL_MAX_TEXTURE_BUFFER_SIZE = GL31.GL_MAX_TEXTURE_BUFFER_SIZE;
                    GL_TEXTURE_BINDING_BUFFER = GL31.GL_TEXTURE_BINDING_BUFFER;

                    glTexBuffer = GL31::glTexBuffer;
                } else if (cap.GL_ARB_texture_buffer_object) {
                    GL_TEXTURE_BUFFER = ARBTextureBufferObject.GL_TEXTURE_BUFFER_ARB;
                    GL_MAX_TEXTURE_BUFFER_SIZE = ARBTextureBufferObject.GL_MAX_TEXTURE_BUFFER_SIZE_ARB;
                    GL_TEXTURE_BINDING_BUFFER = ARBTextureBufferObject.GL_TEXTURE_BINDING_BUFFER_ARB;

                    glTexBuffer = ARBTextureBufferObject::glTexBufferARB;
                } else if (cap.GL_EXT_texture_buffer_object) {
                    GL_TEXTURE_BUFFER = EXTTextureBufferObject.GL_TEXTURE_BUFFER_EXT;
                    GL_MAX_TEXTURE_BUFFER_SIZE = EXTTextureBufferObject.GL_MAX_TEXTURE_BUFFER_SIZE_EXT;
                    GL_TEXTURE_BINDING_BUFFER = EXTTextureBufferObject.GL_TEXTURE_BINDING_BUFFER_EXT;

                    glTexBuffer = EXTTextureBufferObject::glTexBufferEXT;
                }

                VALID_TexBufferRange = Buffer.valid() && (cap.OpenGL43 || cap.GL_ARB_texture_buffer_range);
                if (cap.OpenGL43) {
                    glTexBufferRange = GL43::glTexBufferRange;
                } else if (cap.GL_ARB_texture_buffer_range) {
                    glTexBufferRange = ARBTextureBufferRange::glTexBufferRange;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            /**
             * {@link GL43#glTexBufferRange(int, int, int, long, long)}
             */
            public static boolean valid_TexBufferRange() {
                return VALID_TexBufferRange;
            }

            public static void glTexBuffer(int target, int internalformat, int buffer) {
                glTexBuffer.run(target, internalformat, buffer);
            }

            public static void glTexBufferRange(int target, int internalformat, int buffer, long offset, long size) {
                glTexBufferRange.run(target, internalformat, buffer, offset, size);
            }

            private TBO() {}
        }

        public final static class UBO extends Buffer {
            private static boolean VALID;

            public static int GL_UNIFORM_BUFFER;
            public static int GL_UNIFORM_BUFFER_BINDING;

            public static int GL_UNIFORM_BLOCK_DATA_SIZE;
            public static int GL_UNIFORM_OFFSET;

            public static int GL_UNIFORM_SIZE;
            public static int GL_MAX_UNIFORM_BLOCK_SIZE;
            public static int GL_MAX_UNIFORM_BUFFER_BINDINGS;

            private static IntXYFun<ByteBuffer, IntBuffer> glGetUniformIndices_BI;
            private static IntXYFun<CharSequence[], IntBuffer> glGetUniformIndices_strAI;
            private static IntXIntYFun<IntBuffer, IntBuffer> glGetActiveUniforms;
            private static IntIntIntFunInt glGetActiveUniformsi;
            private static IntIntXYFun<IntBuffer, ByteBuffer> glGetActiveUniformName;
            private static IntIntIntFunX<String> glGetActiveUniformName_str;
            private static IntXFunInt<ByteBuffer> glGetUniformBlockIndex;
            private static IntXFunInt<CharSequence> glGetUniformBlockIndex_str;
            private static IntIntIntXFun<IntBuffer> glGetActiveUniformBlock;
            private static IntIntIntFunInt glGetActiveUniformBlocki;
            private static IntIntXYFun<IntBuffer, ByteBuffer> glGetActiveUniformBlockName;
            private static IntIntIntFunX<String> glGetActiveUniformBlockName_str;
            private static IntIntIntFun glUniformBlockBinding;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid() && (cap.OpenGL31 || cap.GL_ARB_uniform_buffer_object) && VALID_BindRangeBase;
                if (cap.OpenGL31) {
                    GL_UNIFORM_BUFFER = GL31.GL_UNIFORM_BUFFER;
                    GL_UNIFORM_BUFFER_BINDING = GL31.GL_UNIFORM_BUFFER_BINDING;

                    GL_UNIFORM_BLOCK_DATA_SIZE = GL31.GL_UNIFORM_BLOCK_DATA_SIZE;
                    GL_UNIFORM_OFFSET = GL31.GL_UNIFORM_OFFSET;

                    GL_UNIFORM_SIZE = GL31.GL_UNIFORM_SIZE;
                    GL_MAX_UNIFORM_BLOCK_SIZE = GL31.GL_MAX_UNIFORM_BLOCK_SIZE;
                    GL_MAX_UNIFORM_BUFFER_BINDINGS = GL31.GL_MAX_UNIFORM_BUFFER_BINDINGS;

                    glGetUniformIndices_BI = GL31::glGetUniformIndices;
                    glGetUniformIndices_strAI = GL31::glGetUniformIndices;
                    glGetActiveUniforms = GL31::glGetActiveUniforms;
                    glGetActiveUniformsi = GL31::glGetActiveUniformsi;
                    glGetActiveUniformName = GL31::glGetActiveUniformName;
                    glGetActiveUniformName_str = GL31::glGetActiveUniformName;
                    glGetUniformBlockIndex = GL31::glGetUniformBlockIndex;
                    glGetUniformBlockIndex_str = GL31::glGetUniformBlockIndex;
                    glGetActiveUniformBlock = GL31::glGetActiveUniformBlock;
                    glGetActiveUniformBlocki = GL31::glGetActiveUniformBlocki;
                    glGetActiveUniformBlockName = GL31::glGetActiveUniformBlockName;
                    glGetActiveUniformBlockName_str = GL31::glGetActiveUniformBlockName;
                    glUniformBlockBinding = GL31::glUniformBlockBinding;

                } else if (cap.GL_ARB_uniform_buffer_object) {
                    GL_UNIFORM_BUFFER = ARBUniformBufferObject.GL_UNIFORM_BUFFER;
                    GL_UNIFORM_BUFFER_BINDING = ARBUniformBufferObject.GL_UNIFORM_BUFFER_BINDING;

                    GL_UNIFORM_BLOCK_DATA_SIZE = ARBUniformBufferObject.GL_UNIFORM_BLOCK_DATA_SIZE;
                    GL_UNIFORM_OFFSET = ARBUniformBufferObject.GL_UNIFORM_OFFSET;

                    GL_UNIFORM_SIZE = ARBUniformBufferObject.GL_UNIFORM_SIZE;
                    GL_MAX_UNIFORM_BLOCK_SIZE = ARBUniformBufferObject.GL_MAX_UNIFORM_BLOCK_SIZE;
                    GL_MAX_UNIFORM_BUFFER_BINDINGS = ARBUniformBufferObject.GL_MAX_UNIFORM_BUFFER_BINDINGS;

                    glGetUniformIndices_BI = ARBUniformBufferObject::glGetUniformIndices;
                    glGetUniformIndices_strAI = ARBUniformBufferObject::glGetUniformIndices;
                    glGetActiveUniforms = ARBUniformBufferObject::glGetActiveUniforms;
                    glGetActiveUniformsi = ARBUniformBufferObject::glGetActiveUniformsi;
                    glGetActiveUniformName = ARBUniformBufferObject::glGetActiveUniformName;
                    glGetActiveUniformName_str = ARBUniformBufferObject::glGetActiveUniformName;
                    glGetUniformBlockIndex = ARBUniformBufferObject::glGetUniformBlockIndex;
                    glGetUniformBlockIndex_str = ARBUniformBufferObject::glGetUniformBlockIndex;
                    glGetActiveUniformBlock = ARBUniformBufferObject::glGetActiveUniformBlock;
                    glGetActiveUniformBlocki = ARBUniformBufferObject::glGetActiveUniformBlocki;
                    glGetActiveUniformBlockName = ARBUniformBufferObject::glGetActiveUniformBlockName;
                    glGetActiveUniformBlockName_str = ARBUniformBufferObject::glGetActiveUniformBlockName;
                    glUniformBlockBinding = ARBUniformBufferObject::glUniformBlockBinding;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static void glGetUniformIndices(int program, ByteBuffer uniformNames, IntBuffer uniformIndices) {
                glGetUniformIndices_BI.run(program, uniformNames, uniformIndices);
            }

            public static void glGetUniformIndices(int program, CharSequence[] uniformNames, IntBuffer uniformIndices) {
                glGetUniformIndices_strAI.run(program, uniformNames, uniformIndices);
            }

            public static void glGetActiveUniforms(int program, IntBuffer uniformIndices, int pname, IntBuffer params) {
                glGetActiveUniforms.run(pname, uniformIndices, pname, params);
            }

            public static int glGetActiveUniformsi(int program, int uniformIndex, int pname ) {
                return glGetActiveUniformsi.run(program, uniformIndex, pname);
            }

            public static void glGetActiveUniformName(int program, int uniformIndex, IntBuffer length, ByteBuffer uniformName) {
                glGetActiveUniformName.run(program, uniformIndex, length, uniformName);
            }

            public static @NotNull String glGetActiveUniformName(int program, int uniformIndex, int bufSize) {
                return glGetActiveUniformName_str.run(program, uniformIndex, bufSize);
            }

            public static int glGetUniformBlockIndex(int program, ByteBuffer uniformBlockName) {
                return glGetUniformBlockIndex.run(program, uniformBlockName);
            }

            public static int glGetUniformBlockIndex(int program, CharSequence uniformBlockName) {
                return glGetUniformBlockIndex_str.run(program, uniformBlockName);
            }

            public static void glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname, IntBuffer params) {
                glGetActiveUniformBlock.run(program, uniformBlockIndex, pname, params);
            }

            public static int glGetActiveUniformBlocki(int program, int uniformBlockIndex, int pname) {
                return glGetActiveUniformBlocki.run(program, uniformBlockIndex, pname);
            }

            public static void glGetActiveUniformBlockName(int program, int uniformBlockIndex, IntBuffer length, ByteBuffer uniformBlockName) {
                glGetActiveUniformBlockName.run(program, uniformBlockIndex, length, uniformBlockName);
            }

            public static @NotNull String glGetActiveUniformBlockName(int program, int uniformBlockIndex, int bufSize) {
                return glGetActiveUniformBlockName_str.run(program, uniformBlockIndex, bufSize);
            }

            public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
                glUniformBlockBinding.run(program, uniformBlockIndex, uniformBlockBinding);
            }

            private UBO() {}
        }

        public final static class Indirect extends Buffer {
            private static boolean VALID;

            public static int GL_DRAW_INDIRECT_BUFFER;
            public static int GL_DRAW_INDIRECT_BUFFER_BINDING;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid() && (cap.OpenGL40 || cap.GL_ARB_draw_indirect);
                if (cap.OpenGL15) {
                    GL_DRAW_INDIRECT_BUFFER = GL40.GL_DRAW_INDIRECT_BUFFER;
                    GL_DRAW_INDIRECT_BUFFER_BINDING = GL40.GL_DRAW_INDIRECT_BUFFER_BINDING;
                } else if (cap.GL_ARB_vertex_buffer_object) {
                    GL_DRAW_INDIRECT_BUFFER = ARBDrawIndirect.GL_DRAW_INDIRECT_BUFFER;
                    GL_DRAW_INDIRECT_BUFFER_BINDING = ARBDrawIndirect.GL_DRAW_INDIRECT_BUFFER_BINDING;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            private Indirect() {}
        }

        public final static class SSBO extends Buffer {
            private static boolean VALID;

            public static int GL_SHADER_STORAGE_BUFFER;
            public static int GL_SHADER_STORAGE_BUFFER_BINDING;
            public static int GL_SHADER_STORAGE_BUFFER_START;
            public static int GL_SHADER_STORAGE_BUFFER_SIZE;

            public static int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS;
            public static int GL_MAX_SHADER_STORAGE_BLOCK_SIZE;

            private static IntIntIntFun glShaderStorageBlockBinding;

            private static void init(ContextCapabilities cap) {
                VALID = Buffer.valid() && (cap.OpenGL43 || cap.GL_ARB_shader_storage_buffer_object);
                if (cap.OpenGL43) {
                    GL_SHADER_STORAGE_BUFFER = GL43.GL_SHADER_STORAGE_BUFFER;
                    GL_SHADER_STORAGE_BUFFER_BINDING = GL43.GL_SHADER_STORAGE_BUFFER_BINDING;
                    GL_SHADER_STORAGE_BUFFER_START = GL43.GL_SHADER_STORAGE_BUFFER_START;
                    GL_SHADER_STORAGE_BUFFER_SIZE = GL43.GL_SHADER_STORAGE_BUFFER_SIZE;

                    GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = GL43.GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS;
                    GL_MAX_SHADER_STORAGE_BLOCK_SIZE = GL43.GL_MAX_SHADER_STORAGE_BLOCK_SIZE;

                    glShaderStorageBlockBinding = GL43::glShaderStorageBlockBinding;
                } else if (cap.GL_ARB_shader_storage_buffer_object) {
                    GL_SHADER_STORAGE_BUFFER = ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BUFFER;
                    GL_SHADER_STORAGE_BUFFER_BINDING = ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BUFFER_BINDING;
                    GL_SHADER_STORAGE_BUFFER_START = ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BUFFER_START;
                    GL_SHADER_STORAGE_BUFFER_SIZE = ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BUFFER_SIZE;

                    GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = ARBShaderStorageBufferObject.GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS;
                    GL_MAX_SHADER_STORAGE_BLOCK_SIZE = ARBShaderStorageBufferObject.GL_MAX_SHADER_STORAGE_BLOCK_SIZE;

                    glShaderStorageBlockBinding = ARBShaderStorageBufferObject::glShaderStorageBlockBinding;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
                glShaderStorageBlockBinding.run(program, storageBlockIndex, storageBlockBinding);
            }

            private SSBO() {}
        }

        public static boolean valid() {
            return VALID;
        }

        /**
         * {@link GL30#glMapBufferRange(int, long, long, int, ByteBuffer)}<p>
         * {@link GL30#glFlushMappedBufferRange(int, long, long)}
         */
        public static boolean valid_MapRange() {
            return VALID_MapRange;
        }

        /**
         * {@link GL30#glBindBufferRange(int, int, int, long, long)}<p>
         * {@link GL30#glBindBufferBase(int, int, int)}
         */
        public static boolean valid_BindRange() {
            return VALID_BindRangeBase;
        }

        /**
         * {@link GL31#glCopyBufferSubData(int, int, long, long, long)}
         */
        public static boolean valid_CopyBuffer() {
            return VALID_CopyBuffer;
        }

        /**
         * {@link GL43#glInvalidateBufferData(int)}<p>
         * {@link GL43#glInvalidateBufferSubData(int, long, long)}
         */
        public static boolean valid_Invalidate() {
            return VALID_Invalidate;
        }

        /**
         * {@link GL43#glClearBufferData(int, int, int, int, ByteBuffer)}<p>
         * {@link GL43#glClearBufferSubData(int, int, long, long, int, int, ByteBuffer)}
         */
        public static boolean valid_ClearBuffer() {
            return VALID_ClearBuffer;
        }

        /**
         * {@link GL44#glBufferStorage}
         */
        public static boolean valid_BufferStorage() {
            return VALID_BufferStorage;
        }

        /**
         * {@link GL44#glBindBuffersBase(int, int, int, IntBuffer)}<p>
         * {@link GL44#glBindBuffersRange(int, int, int, IntBuffer, PointerBuffer, PointerBuffer)}
         */
        public static boolean valid_MultiBind() {
            return VALID_MultiBind;
        }

        public static int glGenBuffers() {
            return glGenBuffers.getAsInt();
        }

        public static void glGenBuffers(IntBuffer buffers) {
            glGenBuffers_IntBuffer.accept(buffers);
        }

        public static void glBindBuffer(int target, int buffer) {
            glBindBuffer.run(target, buffer);
        }

        public static boolean glUnmapBuffer(int target) {
            return glUnmapBuffer.test(target);
        }

        public static void glDeleteBuffers(int buffer) {
            glDeleteBuffers.accept(buffer);
        }

        public static void glDeleteBuffers(IntBuffer buffers) {
            glDeleteBuffers_IntBuffer.accept(buffers);
        }

        public static void glBufferData(int target, long data_size, int usage) {
            glBufferData.run(target, data_size, usage);
        }

        public static void glBufferData(int target, ByteBuffer data, int usage) {
            glBufferData_ByteBuffer.run(target, data, usage);
        }

        public static void glBufferData(int target, ShortBuffer data, int usage) {
            glBufferData_ShortBuffer.run(target, data, usage);
        }

        public static void glBufferData(int target, IntBuffer data, int usage) {
            glBufferData_IntBuffer.run(target, data, usage);
        }

        public static void glBufferData(int target, FloatBuffer data, int usage) {
            glBufferData_FloatBuffer.run(target, data, usage);
        }

        public static void glBufferData(int target, DoubleBuffer data, int usage) {
            glBufferData_DoubleBuffer.run(target, data, usage);
        }

        public static void glBufferSubData(int target, long offset, ByteBuffer data) {
            glBufferSubData_ByteBuffer.run(target, offset, data);
        }

        public static void glBufferSubData(int target, long offset, ShortBuffer data) {
            glBufferSubData_ShortBuffer.run(target, offset, data);
        }

        public static void glBufferSubData(int target, long offset, IntBuffer data) {
            glBufferSubData_IntBuffer.run(target, offset, data);
        }

        public static void glBufferSubData(int target, long offset, FloatBuffer data) {
            glBufferSubData_FloatBuffer.run(target, offset, data);
        }

        public static void glBufferSubData(int target, long offset, DoubleBuffer data) {
            glBufferSubData_DoubleBuffer.run(target, offset, data);
        }

        public static void glGetBufferSubData(int target, long offset, ByteBuffer data) {
            glGetBufferSubData_ByteBuffer.run(target, offset, data);
        }

        public static void glGetBufferSubData(int target, long offset, ShortBuffer data) {
            glGetBufferSubData_ShortBuffer.run(target, offset, data);
        }

        public static void glGetBufferSubData(int target, long offset, IntBuffer data) {
            glGetBufferSubData_IntBuffer.run(target, offset, data);
        }

        public static void glGetBufferSubData(int target, long offset, FloatBuffer data) {
            glGetBufferSubData_FloatBuffer.run(target, offset, data);
        }

        public static void glGetBufferSubData(int target, long offset, DoubleBuffer data) {
            glGetBufferSubData_DoubleBuffer.run(target, offset, data);
        }

        public static ByteBuffer glMapBufferRange(int target, long offset, long length, int access, @Nullable ByteBuffer old_buffer) {
            return glMapBufferRange.run(target, offset, length, access, old_buffer);
        }

        public static void glFlushMappedBufferRange( int target, long offset, long length) {
            glFlushMappedBufferRange.run(target, offset, length);
        }

        public static void glBindBufferRange(int target, int index, int buffer, long offset, long size) {
            glBindBufferRange.run(target, index, buffer, offset, size);
        }

        public static void glBindBufferBase(int target, int index, int buffer) {
            glBindBufferBase.run(target, index, buffer);
        }

        public static void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
            glCopyBufferSubData.run(readTarget, writeTarget, readOffset, writeOffset, size);
        }

        public static void glInvalidateBufferData(int buffer) {
            glInvalidateBufferData.accept(buffer);
        }

        public static void glInvalidateBufferSubData(int buffer, long offset, long length) {
            glInvalidateBufferSubData.run(buffer, offset, length);
        }

        public static void glClearBufferData(int target, int internalformat, int format, int type, ByteBuffer data) {
            glClearBufferData.run(target, internalformat, format, type, data);
        }

        public static void glClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
            glClearBufferSubData.run(target, internalformat, offset, size, format, type, data);
        }

        public static void glBufferStorage(int target, long size, int flags) {
            glBufferStorage.run(target, size, flags);
        }

        public static void glBufferStorage(int target, ByteBuffer data, int flags) {
            glBufferStorage_ByteBuffer.run(target, data, flags);
        }

        public static void glBufferStorage(int target, ShortBuffer data, int flags) {
            glBufferStorage_ShortBuffer.run(target, data, flags);
        }

        public static void glBufferStorage(int target, IntBuffer data, int flags) {
            glBufferStorage_IntBuffer.run(target, data, flags);
        }

        public static void glBufferStorage(int target, FloatBuffer data, int flags) {
            glBufferStorage_FloatBuffer.run(target, data, flags);
        }

        public static void glBufferStorage(int target, DoubleBuffer data, int flags) {
            glBufferStorage_DoubleBuffer.run(target, data, flags);
        }

        public static void glBindBuffersBase(int target, int first, int count, IntBuffer buffers) {
            glBindBuffersBase.run(target, first, count, buffers);
        }

        public static void glBindBuffersRange(int target, int first, int count, IntBuffer buffers, PointerBuffer offsets, PointerBuffer sizes) {
            glBindBuffersRange.run(target, first, count, buffers, offsets, sizes);
        }

        private Buffer() {}
    }

    public final static class VAO {
        private static boolean VALID;
        private static boolean VALID_Attrib;
        private static boolean VALID_IntAttrib;
        private static boolean VALID_VertexDivisor;
        private static boolean VALID_DoubleAttrib;
        private static boolean VALID_BindingDivisor;
        private static boolean VALID_MultiBind;

        public static int GL_VERTEX_ARRAY_BINDING;

        private static IntConsumer glBindVertexArray;
        private static IntConsumer glDeleteVertexArrays;
        private static Consumer<IntBuffer> glDeleteVertexArrays_buf;
        private static IntSupplier glGenVertexArrays;
        private static Consumer<IntBuffer> glGenVertexArrays_buf;

        private static IntIntIntBoolIntLongFun glVertexAttribPointer;
        private static IntConsumer glEnableVertexAttribArray;
        private static IntConsumer glDisableVertexAttribArray;

        private static IntIntIntIntLongFun glVertexAttribIPointer;

        private static IntIntFun glVertexAttribDivisor;

        private static IntIntIntLongFun glVertexAttribLPointer;

        private static IntIntLongIntFun glBindVertexBuffer;
        private static IntIntIntBoolIntFun glVertexAttribFormat;
        private static IntIntIntIntFun glVertexAttribIFormat;
        private static IntIntIntIntFun glVertexAttribLFormat;
        private static IntIntFun glVertexAttribBinding;
        private static IntIntFun glVertexBindingDivisor;

        private static IntIntXYZFun<IntBuffer, PointerBuffer, IntBuffer> glBindVertexBuffers;

        private static void init(ContextCapabilities cap) {
            VALID = cap.OpenGL30 || cap.GL_ARB_vertex_array_object || cap.GL_APPLE_vertex_array_object;
            if (cap.OpenGL30) {
                GL_VERTEX_ARRAY_BINDING = GL30.GL_VERTEX_ARRAY_BINDING;

                glBindVertexArray = GL30::glBindVertexArray;
                glDeleteVertexArrays = GL30::glDeleteVertexArrays;
                glDeleteVertexArrays_buf = GL30::glDeleteVertexArrays;
                glGenVertexArrays = GL30::glGenVertexArrays;
                glGenVertexArrays_buf = GL30::glGenVertexArrays;
            } else if (cap.GL_ARB_vertex_array_object) {
                GL_VERTEX_ARRAY_BINDING = ARBVertexArrayObject.GL_VERTEX_ARRAY_BINDING;

                glBindVertexArray = ARBVertexArrayObject::glBindVertexArray;
                glDeleteVertexArrays = ARBVertexArrayObject::glDeleteVertexArrays;
                glDeleteVertexArrays_buf = ARBVertexArrayObject::glDeleteVertexArrays;
                glGenVertexArrays = ARBVertexArrayObject::glGenVertexArrays;
                glGenVertexArrays_buf = ARBVertexArrayObject::glGenVertexArrays;
            } else if (cap.GL_APPLE_vertex_array_object) {
                GL_VERTEX_ARRAY_BINDING = APPLEVertexArrayObject.GL_VERTEX_ARRAY_BINDING_APPLE;

                glBindVertexArray = APPLEVertexArrayObject::glBindVertexArrayAPPLE;
                glDeleteVertexArrays = APPLEVertexArrayObject::glDeleteVertexArraysAPPLE;
                glDeleteVertexArrays_buf = APPLEVertexArrayObject::glDeleteVertexArraysAPPLE;
                glGenVertexArrays = APPLEVertexArrayObject::glGenVertexArraysAPPLE;
                glGenVertexArrays_buf = APPLEVertexArrayObject::glGenVertexArraysAPPLE;
            }

            VALID_Attrib = cap.OpenGL20 || cap.GL_ARB_vertex_shader || cap.GL_ARB_vertex_program;
            if (cap.OpenGL20) {
                glVertexAttribPointer = GL20::glVertexAttribPointer;
                glEnableVertexAttribArray = GL20::glEnableVertexAttribArray;
                glDisableVertexAttribArray = GL20::glDisableVertexAttribArray;
            } else if (cap.GL_ARB_vertex_shader) {
                glVertexAttribPointer = ARBVertexShader::glVertexAttribPointerARB;
                glEnableVertexAttribArray = ARBVertexShader::glEnableVertexAttribArrayARB;
                glDisableVertexAttribArray = ARBVertexShader::glDisableVertexAttribArrayARB;
            } else if (cap.GL_ARB_vertex_program) {
                glVertexAttribPointer = ARBVertexProgram::glVertexAttribPointerARB;
                glEnableVertexAttribArray = ARBVertexProgram::glEnableVertexAttribArrayARB;
                glDisableVertexAttribArray = ARBVertexProgram::glDisableVertexAttribArrayARB;
            }

            VALID_IntAttrib = cap.OpenGL30 || cap.GL_EXT_gpu_shader4;
            if (cap.OpenGL30) {
                glVertexAttribIPointer = GL30::glVertexAttribIPointer;
            } else if (cap.GL_EXT_gpu_shader4) {
                glVertexAttribIPointer = EXTGpuShader4::glVertexAttribIPointerEXT;
            }

            VALID_VertexDivisor = cap.OpenGL33 || cap.GL_ARB_instanced_arrays;
            if (cap.OpenGL33) {
                glVertexAttribDivisor = GL33::glVertexAttribDivisor;
            } else if (cap.GL_ARB_instanced_arrays) {
                glVertexAttribDivisor = ARBInstancedArrays::glVertexAttribDivisorARB;
            }

            VALID_DoubleAttrib = cap.OpenGL41 || cap.GL_ARB_vertex_attrib_64bit || cap.GL_EXT_vertex_attrib_64bit;
            if (cap.OpenGL41) {
                glVertexAttribLPointer = GL41::glVertexAttribLPointer;
            } else if (cap.GL_ARB_vertex_attrib_64bit) {
                glVertexAttribLPointer = ARBVertexAttrib64bit::glVertexAttribLPointer;
            } else if (cap.GL_EXT_vertex_attrib_64bit) {
                glVertexAttribLPointer = EXTVertexAttrib64bit::glVertexAttribLPointerEXT;
            }

            VALID_BindingDivisor = cap.OpenGL43 || cap.GL_ARB_vertex_attrib_binding;
            if (cap.OpenGL43) {
                glBindVertexBuffer = GL43::glBindVertexBuffer;
                glVertexAttribFormat = GL43::glVertexAttribFormat;
                glVertexAttribIFormat = GL43::glVertexAttribIFormat;
                glVertexAttribLFormat = GL43::glVertexAttribLFormat;
                glVertexAttribBinding = GL43::glVertexAttribBinding;
                glVertexBindingDivisor = GL43::glVertexBindingDivisor;
            } else if (cap.GL_ARB_vertex_attrib_64bit) {
                glBindVertexBuffer = ARBVertexAttribBinding::glBindVertexBuffer;
                glVertexAttribFormat = ARBVertexAttribBinding::glVertexAttribFormat;
                glVertexAttribIFormat = ARBVertexAttribBinding::glVertexAttribIFormat;
                glVertexAttribLFormat = ARBVertexAttribBinding::glVertexAttribLFormat;
                glVertexAttribBinding = ARBVertexAttribBinding::glVertexAttribBinding;
                glVertexBindingDivisor = ARBVertexAttribBinding::glVertexBindingDivisor;
            }

            VALID_MultiBind = cap.OpenGL44 || cap.GL_ARB_multi_bind;
            if (cap.OpenGL44) {
                glBindVertexBuffers = GL44::glBindVertexBuffers;
            } else if (cap.GL_ARB_multi_bind) {
                glBindVertexBuffers = ARBMultiBind::glBindVertexBuffers;
            }
        }

        public static boolean valid() {
            return VALID;
        }

        /**
         * Independent of VAO availability, also parts of vertex shader.<p>
         *
         * {@link GL20#glVertexAttribPointer(int, int, int, boolean, int, long)}<p>
         * {@link GL20#glEnableVertexAttribArray(int)}<p>
         * {@link GL20#glDisableVertexAttribArray(int)}
         */
        private static boolean valid_Attrib() {
            return VALID_Attrib;
        }

        /**
         * {@link GL30#glVertexAttribIPointer(int, int, int, int, long)}
         */
        public static boolean valid_IntAttrib() {
            return VALID_IntAttrib;
        }

        /**
         * {@link GL33#glVertexAttribDivisor(int, int)}
         */
        public static boolean valid_VertexDivisor() {
            return VALID_VertexDivisor;
        }

        /**
         * {@link GL41#glVertexAttribLPointer(int, int, int, long)}
         */
        public static boolean valid_DoubleAttrib() {
            return VALID_DoubleAttrib;
        }

        /**
         * {@link GL43#glBindVertexBuffer(int, int, long, int)}<p>
         * {@link GL43#glVertexAttribFormat(int, int, int, boolean, int)}<p>
         * {@link GL43#glVertexAttribIFormat(int, int, int, int)}<p>
         * {@link GL43#glVertexAttribLFormat(int, int, int, int)}<p>
         * {@link GL43#glVertexBindingDivisor(int, int)}
         */
        public static boolean valid_BindingDivisor() {
            return VALID_BindingDivisor;
        }

        /**
         * {@link GL44#glBindVertexBuffers(int, int, IntBuffer, PointerBuffer, IntBuffer)}
         */
        public static boolean valid_MultiBind() {
            return VALID_MultiBind;
        }

        public static void glBindVertexArray(int array) {
            glBindVertexArray.accept(array);
        }

        public static void glDeleteVertexArrays(int array) {
            glDeleteVertexArrays.accept(array);
        }

        public static void glDeleteVertexArrays(IntBuffer arrays) {
            glDeleteVertexArrays_buf.accept(arrays);
        }

        public static int glGenVertexArrays() {
            return glGenVertexArrays.getAsInt();
        }

        public static void glGenVertexArrays(IntBuffer arrays) {
            glGenVertexArrays_buf.accept(arrays);
        }

        public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
            glVertexAttribPointer.run(index, size, type, normalized, stride, buffer_buffer_offset);
        }

        public static void glEnableVertexAttribArray(int index) {
            glEnableVertexAttribArray.accept(index);
        }

        public static void glDisableVertexAttribArray(int index) {
            glDisableVertexAttribArray.accept(index);
        }

        public static void glVertexAttribIPointer(int index, int size, int type, int stride, long buffer_buffer_offset) {
            glVertexAttribIPointer.run(index, size, type, stride, buffer_buffer_offset);
        }

        public static void glVertexAttribDivisor(int index, int divisor) {
            glVertexAttribDivisor.run(index, divisor);
        }

        public static void glVertexAttribLPointer(int index, int size, int stride, long pointer_buffer_offset) {
            glVertexAttribLPointer.run(index, size, stride, pointer_buffer_offset);
        }

        public static void glBindVertexBuffer(int bindingindex, int buffer, long offset, int stride) {
            glBindVertexBuffer.run(bindingindex, buffer, offset, stride);
        }

        public static void glVertexAttribFormat(int attribindex, int size, int type, boolean normalized, int relativeoffset) {
            glVertexAttribFormat.run(attribindex, size, type, normalized, relativeoffset);
        }

        public static void glVertexAttribIFormat(int attribindex, int size, int type, int relativeoffset) {
            glVertexAttribIFormat.run(attribindex, size, type, relativeoffset);
        }

        public static void glVertexAttribLFormat(int attribindex, int size, int type, int relativeoffset) {
            glVertexAttribLFormat.run(attribindex, size, type, relativeoffset);
        }

        public static void glVertexAttribBinding(int attribindex, int bindingindex) {
            glVertexAttribBinding.run(attribindex, bindingindex);
        }

        public static void glVertexBindingDivisor(int bindingindex, int divisor) {
            glVertexBindingDivisor.run(bindingindex, divisor);
        }

        public static void glBindVertexBuffers(int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
            glBindVertexBuffers.run(first, count, buffers, offsets, strides);
        }

        private VAO() {}
    }

    public final static class FBO {
        private static boolean VALID;
        private static boolean VALID_Clear;
        private static boolean VALID_SRGB;
        private static boolean VALID_DepthFormat;
        private static boolean VALID_DepthStencilAttachment;
        private static boolean VALID_DepthStencilFormat;
        private static boolean VALID_ColorBufferFloat;
        private static boolean VALID_DepthBufferFloat;
        private static boolean VALID_MultisampleRBO;
        private static boolean VALID_TextureLayer;
        private static boolean VALID_Blit;
        private static boolean VALID_MultisampleTex;
        private static boolean VALID_TextureCast;
        private static boolean VALID_MultisampleTexStorage;
        private static boolean VALID_Invalidate;
        private static boolean VALID_NoAttachments;

        private static boolean VALID_BoxUtilBase;

        public static final int GL_FRONT_LEFT = GL11.GL_FRONT_LEFT;
        public static final int GL_FRONT_RIGHT = GL11.GL_FRONT_RIGHT;
        public static final int GL_BACK_LEFT = GL11.GL_BACK_LEFT;
        public static final int GL_BACK_RIGHT = GL11.GL_BACK_RIGHT;
        public static final int GL_FRONT = GL11.GL_FRONT;
        public static final int GL_BACK = GL11.GL_BACK;
        public static final int GL_LEFT = GL11.GL_LEFT;
        public static final int GL_RIGHT = GL11.GL_RIGHT;
        public static final int GL_COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
        public static final int GL_DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
        public static final int GL_STENCIL_BUFFER_BIT = GL11.GL_STENCIL_BUFFER_BIT;

        public static int GL_FRAMEBUFFER;
        public static int GL_RENDERBUFFER;
        public static int GL_STENCIL_INDEX1_EXT;
        public static int GL_STENCIL_INDEX4_EXT;
        public static int GL_STENCIL_INDEX8_EXT;
        public static int GL_STENCIL_INDEX16_EXT;
        public static int GL_COLOR_ATTACHMENT0;
        public static int GL_COLOR_ATTACHMENT1;
        public static int GL_COLOR_ATTACHMENT2;
        public static int GL_COLOR_ATTACHMENT3;
        public static int GL_COLOR_ATTACHMENT4;
        public static int GL_COLOR_ATTACHMENT5;
        public static int GL_COLOR_ATTACHMENT6;
        public static int GL_COLOR_ATTACHMENT7;
        public static int GL_COLOR_ATTACHMENT8;
        public static int GL_COLOR_ATTACHMENT9;
        public static int GL_COLOR_ATTACHMENT10;
        public static int GL_COLOR_ATTACHMENT11;
        public static int GL_COLOR_ATTACHMENT12;
        public static int GL_COLOR_ATTACHMENT13;
        public static int GL_COLOR_ATTACHMENT14;
        public static int GL_COLOR_ATTACHMENT15;
        public static int GL_DEPTH_ATTACHMENT;
        public static int GL_STENCIL_ATTACHMENT;
        public static int GL_FRAMEBUFFER_COMPLETE;
        public static int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
        public static int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
        public static int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
        public static int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
        public static int GL_FRAMEBUFFER_UNSUPPORTED;
        public static int GL_FRAMEBUFFER_UNDEFINED;
        public static int GL_FRAMEBUFFER_BINDING;
        public static int GL_RENDERBUFFER_BINDING;

        public static int GL_FRAMEBUFFER_SRGB;
        public static int GL_FRAMEBUFFER_SRGB_CAPABLE;

        public static int GL_DEPTH_COMPONENT16;
        public static int GL_DEPTH_COMPONENT24;
        public static int GL_DEPTH_COMPONENT32;

        public static int GL_DEPTH24_STENCIL8;
        public static int GL_UNSIGNED_INT_24_8;

        public static int GL_CLAMP_VERTEX_COLOR;
        public static int GL_CLAMP_FRAGMENT_COLOR;
        public static int GL_CLAMP_READ_COLOR;
        public static int GL_FIXED_ONLY;

        public static int GL_DEPTH_COMPONENT32F;
        public static int GL_DEPTH32F_STENCIL8;
        public static int GL_FLOAT_32_UNSIGNED_INT_24_8_REV;

        public static int GL_DEPTH_STENCIL_ATTACHMENT;

        public static int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;
        public static int GL_MAX_SAMPLES;

        public static int GL_SAMPLE_MASK;
        public static int GL_TEXTURE_2D_MULTISAMPLE;
        public static int GL_PROXY_TEXTURE_2D_MULTISAMPLE;
        public static int GL_TEXTURE_BINDING_2D_MULTISAMPLE;
        public static int GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
        public static int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY;
        public static int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY;

        public static int GL_READ_FRAMEBUFFER;
        public static int GL_DRAW_FRAMEBUFFER;
        public static int GL_DRAW_FRAMEBUFFER_BINDING;
        public static int GL_READ_FRAMEBUFFER_BINDING;

        public static int GL_FRAMEBUFFER_DEFAULT_WIDTH;
        public static int GL_FRAMEBUFFER_DEFAULT_HEIGHT;
        public static int GL_FRAMEBUFFER_DEFAULT_LAYERS;
        public static int GL_FRAMEBUFFER_DEFAULT_SAMPLES;
        public static int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS;

        private static IntIntFun glBindRenderbuffer;
        private static IntConsumer glDeleteRenderbuffers;
        private static Consumer<IntBuffer> glDeleteRenderbuffers_buf;
        private static IntSupplier glGenRenderbuffers;
        private static Consumer<IntBuffer> glGenRenderbuffers_buf;
        private static IntIntIntIntFun glRenderbufferStorage;
        private static IntIntFun glBindFramebuffer;
        private static IntConsumer glDeleteFramebuffers;
        private static Consumer<IntBuffer> glDeleteFramebuffers_buf;
        private static IntSupplier glGenFramebuffers;
        private static Consumer<IntBuffer> glGenFramebuffers_buf;
        private static IntUnaryOperator glCheckFramebufferStatus;
        private static IntIntIntIntIntFun glFramebufferTexture1D;
        private static IntIntIntIntIntFun glFramebufferTexture2D;
        private static IntIntIntIntIntIntFun glFramebufferTexture3D;
        private static IntIntIntIntFun glFramebufferRenderbuffer;
        private static IntConsumer glGenerateMipmap;
        private static IntConsumer glDrawBuffers;
        private static Consumer<IntBuffer> glDrawBuffers_buf;

        private static IntIntXFun<IntBuffer> glClearBuffer;
        private static IntIntXFun<FloatBuffer> glClearBuffer_f;
        private static IntIntFloatIntFun glClearBufferfi;
        private static IntIntXFun<IntBuffer> glClearBufferu;

        private static IntIntFun glClampColor;

        private static IntIntIntIntIntFun glRenderbufferStorageMultisample;

        private static IntIntIntIntIntFun glFramebufferTextureLayer;

        private static IntIntIntIntIntIntIntIntIntIntFun glBlitFramebuffer;

        private static IntIntIntIntIntBoolFun glTexImage2DMultisample;
        private static IntIntIntIntIntIntBoolFun glTexImage3DMultisample;
        private static IntIntFun glSampleMaski;

        private static IntIntIntIntFun glFramebufferTexture;

        private static IntIntIntIntIntBoolFun glTexStorage2DMultisample;
        private static IntIntIntIntIntIntBoolFun glTexStorage3DMultisample;

        private static IntXFun<IntBuffer> glInvalidateFramebuffer;
        private static IntXIntIntIntIntFun<IntBuffer> glInvalidateSubFramebuffer;

        private static IntIntIntFun glFramebufferParameteri;

        private static void init(ContextCapabilities cap) {
            VALID = cap.OpenGL30 || (cap.GL_ARB_depth_texture && cap.GL_ARB_framebuffer_object) || ((cap.OpenGL14 || cap.GL_ARB_depth_texture) && cap.GL_EXT_framebuffer_object);
            if (cap.OpenGL30) {
                GL_FRAMEBUFFER = GL30.GL_FRAMEBUFFER;
                GL_RENDERBUFFER = GL30.GL_RENDERBUFFER;
                GL_STENCIL_INDEX1_EXT = GL30.GL_STENCIL_INDEX1;
                GL_STENCIL_INDEX4_EXT = GL30.GL_STENCIL_INDEX4;
                GL_STENCIL_INDEX8_EXT = GL30.GL_STENCIL_INDEX8;
                GL_STENCIL_INDEX16_EXT = GL30.GL_STENCIL_INDEX16;
                GL_COLOR_ATTACHMENT0 = GL30.GL_COLOR_ATTACHMENT0;
                GL_COLOR_ATTACHMENT1 = GL30.GL_COLOR_ATTACHMENT1;
                GL_COLOR_ATTACHMENT2 = GL30.GL_COLOR_ATTACHMENT2;
                GL_COLOR_ATTACHMENT3 = GL30.GL_COLOR_ATTACHMENT3;
                GL_COLOR_ATTACHMENT4 = GL30.GL_COLOR_ATTACHMENT4;
                GL_COLOR_ATTACHMENT5 = GL30.GL_COLOR_ATTACHMENT5;
                GL_COLOR_ATTACHMENT6 = GL30.GL_COLOR_ATTACHMENT6;
                GL_COLOR_ATTACHMENT7 = GL30.GL_COLOR_ATTACHMENT7;
                GL_COLOR_ATTACHMENT8 = GL30.GL_COLOR_ATTACHMENT8;
                GL_COLOR_ATTACHMENT9 = GL30.GL_COLOR_ATTACHMENT9;
                GL_COLOR_ATTACHMENT10 = GL30.GL_COLOR_ATTACHMENT10;
                GL_COLOR_ATTACHMENT11 = GL30.GL_COLOR_ATTACHMENT11;
                GL_COLOR_ATTACHMENT12 = GL30.GL_COLOR_ATTACHMENT12;
                GL_COLOR_ATTACHMENT13 = GL30.GL_COLOR_ATTACHMENT13;
                GL_COLOR_ATTACHMENT14 = GL30.GL_COLOR_ATTACHMENT14;
                GL_COLOR_ATTACHMENT15 = GL30.GL_COLOR_ATTACHMENT15;
                GL_DEPTH_ATTACHMENT = GL30.GL_DEPTH_ATTACHMENT;
                GL_STENCIL_ATTACHMENT = GL30.GL_STENCIL_ATTACHMENT;
                GL_FRAMEBUFFER_COMPLETE = GL30.GL_FRAMEBUFFER_COMPLETE;
                GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
                GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
                GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
                GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
                GL_FRAMEBUFFER_UNSUPPORTED = GL30.GL_FRAMEBUFFER_UNSUPPORTED;
                GL_FRAMEBUFFER_UNDEFINED = GL30.GL_FRAMEBUFFER_UNDEFINED;
                GL_FRAMEBUFFER_BINDING = GL30.GL_FRAMEBUFFER_BINDING;
                GL_RENDERBUFFER_BINDING = GL30.GL_RENDERBUFFER_BINDING;

                glBindRenderbuffer = GL30::glBindRenderbuffer;
                glDeleteRenderbuffers = GL30::glDeleteRenderbuffers;
                glDeleteRenderbuffers_buf = GL30::glDeleteRenderbuffers;
                glGenRenderbuffers = GL30::glGenRenderbuffers;
                glGenRenderbuffers_buf = GL30::glGenRenderbuffers;
                glRenderbufferStorage = GL30::glRenderbufferStorage;
                glBindFramebuffer = GL30::glBindFramebuffer;
                glDeleteFramebuffers = GL30::glDeleteFramebuffers;
                glDeleteFramebuffers_buf = GL30::glDeleteFramebuffers;
                glGenFramebuffers = GL30::glGenFramebuffers;
                glGenFramebuffers_buf = GL30::glGenFramebuffers;
                glCheckFramebufferStatus = GL30::glCheckFramebufferStatus;
                glFramebufferTexture1D = GL30::glFramebufferTexture1D;
                glFramebufferTexture2D = GL30::glFramebufferTexture2D;
                glFramebufferTexture3D = GL30::glFramebufferTexture3D;
                glFramebufferRenderbuffer = GL30::glFramebufferRenderbuffer;
                glGenerateMipmap = GL30::glGenerateMipmap;
            } else if (cap.GL_ARB_depth_texture && cap.GL_ARB_framebuffer_object) {
                GL_FRAMEBUFFER = ARBFramebufferObject.GL_FRAMEBUFFER;
                GL_RENDERBUFFER = ARBFramebufferObject.GL_RENDERBUFFER;
                GL_STENCIL_INDEX1_EXT = ARBFramebufferObject.GL_STENCIL_INDEX1;
                GL_STENCIL_INDEX4_EXT = ARBFramebufferObject.GL_STENCIL_INDEX4;
                GL_STENCIL_INDEX8_EXT = ARBFramebufferObject.GL_STENCIL_INDEX8;
                GL_STENCIL_INDEX16_EXT = ARBFramebufferObject.GL_STENCIL_INDEX16;
                GL_COLOR_ATTACHMENT0 = ARBFramebufferObject.GL_COLOR_ATTACHMENT0;
                GL_COLOR_ATTACHMENT1 = ARBFramebufferObject.GL_COLOR_ATTACHMENT1;
                GL_COLOR_ATTACHMENT2 = ARBFramebufferObject.GL_COLOR_ATTACHMENT2;
                GL_COLOR_ATTACHMENT3 = ARBFramebufferObject.GL_COLOR_ATTACHMENT3;
                GL_COLOR_ATTACHMENT4 = ARBFramebufferObject.GL_COLOR_ATTACHMENT4;
                GL_COLOR_ATTACHMENT5 = ARBFramebufferObject.GL_COLOR_ATTACHMENT5;
                GL_COLOR_ATTACHMENT6 = ARBFramebufferObject.GL_COLOR_ATTACHMENT6;
                GL_COLOR_ATTACHMENT7 = ARBFramebufferObject.GL_COLOR_ATTACHMENT7;
                GL_COLOR_ATTACHMENT8 = ARBFramebufferObject.GL_COLOR_ATTACHMENT8;
                GL_COLOR_ATTACHMENT9 = ARBFramebufferObject.GL_COLOR_ATTACHMENT9;
                GL_COLOR_ATTACHMENT10 = ARBFramebufferObject.GL_COLOR_ATTACHMENT10;
                GL_COLOR_ATTACHMENT11 = ARBFramebufferObject.GL_COLOR_ATTACHMENT11;
                GL_COLOR_ATTACHMENT12 = ARBFramebufferObject.GL_COLOR_ATTACHMENT12;
                GL_COLOR_ATTACHMENT13 = ARBFramebufferObject.GL_COLOR_ATTACHMENT13;
                GL_COLOR_ATTACHMENT14 = ARBFramebufferObject.GL_COLOR_ATTACHMENT14;
                GL_COLOR_ATTACHMENT15 = ARBFramebufferObject.GL_COLOR_ATTACHMENT15;
                GL_DEPTH_ATTACHMENT = ARBFramebufferObject.GL_DEPTH_ATTACHMENT;
                GL_STENCIL_ATTACHMENT = ARBFramebufferObject.GL_STENCIL_ATTACHMENT;
                GL_FRAMEBUFFER_COMPLETE = ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE;
                GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
                GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
                GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
                GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
                GL_FRAMEBUFFER_UNSUPPORTED = ARBFramebufferObject.GL_FRAMEBUFFER_UNSUPPORTED;
                GL_FRAMEBUFFER_UNDEFINED = ARBFramebufferObject.GL_FRAMEBUFFER_UNDEFINED;
                GL_FRAMEBUFFER_BINDING = ARBFramebufferObject.GL_FRAMEBUFFER_BINDING;
                GL_RENDERBUFFER_BINDING = ARBFramebufferObject.GL_RENDERBUFFER_BINDING;

                glBindRenderbuffer = ARBFramebufferObject::glBindRenderbuffer;
                glDeleteRenderbuffers = ARBFramebufferObject::glDeleteRenderbuffers;
                glDeleteRenderbuffers_buf = ARBFramebufferObject::glDeleteRenderbuffers;
                glGenRenderbuffers = ARBFramebufferObject::glGenRenderbuffers;
                glGenRenderbuffers_buf = ARBFramebufferObject::glGenRenderbuffers;
                glRenderbufferStorage = ARBFramebufferObject::glRenderbufferStorage;
                glBindFramebuffer = ARBFramebufferObject::glBindFramebuffer;
                glDeleteFramebuffers = ARBFramebufferObject::glDeleteFramebuffers;
                glDeleteFramebuffers_buf = ARBFramebufferObject::glDeleteFramebuffers;
                glGenFramebuffers = ARBFramebufferObject::glGenFramebuffers;
                glGenFramebuffers_buf = ARBFramebufferObject::glGenFramebuffers;
                glCheckFramebufferStatus = ARBFramebufferObject::glCheckFramebufferStatus;
                glFramebufferTexture1D = ARBFramebufferObject::glFramebufferTexture1D;
                glFramebufferTexture2D = ARBFramebufferObject::glFramebufferTexture2D;
                glFramebufferTexture3D = ARBFramebufferObject::glFramebufferTexture3D;
                glFramebufferRenderbuffer = ARBFramebufferObject::glFramebufferRenderbuffer;
            } else if (cap.GL_EXT_framebuffer_object) {
                GL_FRAMEBUFFER = EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
                GL_RENDERBUFFER = EXTFramebufferObject.GL_RENDERBUFFER_EXT;
                GL_STENCIL_INDEX1_EXT = EXTFramebufferObject.GL_STENCIL_INDEX1_EXT;
                GL_STENCIL_INDEX4_EXT = EXTFramebufferObject.GL_STENCIL_INDEX4_EXT;
                GL_STENCIL_INDEX8_EXT = EXTFramebufferObject.GL_STENCIL_INDEX8_EXT;
                GL_STENCIL_INDEX16_EXT = EXTFramebufferObject.GL_STENCIL_INDEX16_EXT;
                GL_COLOR_ATTACHMENT0 = EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
                GL_COLOR_ATTACHMENT1 = EXTFramebufferObject.GL_COLOR_ATTACHMENT1_EXT;
                GL_COLOR_ATTACHMENT2 = EXTFramebufferObject.GL_COLOR_ATTACHMENT2_EXT;
                GL_COLOR_ATTACHMENT3 = EXTFramebufferObject.GL_COLOR_ATTACHMENT3_EXT;
                GL_COLOR_ATTACHMENT4 = EXTFramebufferObject.GL_COLOR_ATTACHMENT4_EXT;
                GL_COLOR_ATTACHMENT5 = EXTFramebufferObject.GL_COLOR_ATTACHMENT5_EXT;
                GL_COLOR_ATTACHMENT6 = EXTFramebufferObject.GL_COLOR_ATTACHMENT6_EXT;
                GL_COLOR_ATTACHMENT7 = EXTFramebufferObject.GL_COLOR_ATTACHMENT7_EXT;
                GL_COLOR_ATTACHMENT8 = EXTFramebufferObject.GL_COLOR_ATTACHMENT8_EXT;
                GL_COLOR_ATTACHMENT9 = EXTFramebufferObject.GL_COLOR_ATTACHMENT9_EXT;
                GL_COLOR_ATTACHMENT10 = EXTFramebufferObject.GL_COLOR_ATTACHMENT10_EXT;
                GL_COLOR_ATTACHMENT11 = EXTFramebufferObject.GL_COLOR_ATTACHMENT11_EXT;
                GL_COLOR_ATTACHMENT12 = EXTFramebufferObject.GL_COLOR_ATTACHMENT12_EXT;
                GL_COLOR_ATTACHMENT13 = EXTFramebufferObject.GL_COLOR_ATTACHMENT13_EXT;
                GL_COLOR_ATTACHMENT14 = EXTFramebufferObject.GL_COLOR_ATTACHMENT14_EXT;
                GL_COLOR_ATTACHMENT15 = EXTFramebufferObject.GL_COLOR_ATTACHMENT15_EXT;
                GL_DEPTH_ATTACHMENT = EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
                GL_STENCIL_ATTACHMENT = EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT;
                GL_FRAMEBUFFER_COMPLETE = EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
                GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT;
                GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT;
                GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT;
                GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT;
                GL_FRAMEBUFFER_UNSUPPORTED = EXTFramebufferObject.GL_FRAMEBUFFER_UNSUPPORTED_EXT;
                GL_FRAMEBUFFER_UNDEFINED = GL30.GL_FRAMEBUFFER_UNDEFINED; // where?
                GL_FRAMEBUFFER_BINDING = EXTFramebufferObject.GL_FRAMEBUFFER_BINDING_EXT;
                GL_RENDERBUFFER_BINDING = EXTFramebufferObject.GL_RENDERBUFFER_BINDING_EXT;

                glBindRenderbuffer = EXTFramebufferObject::glBindRenderbufferEXT;
                glDeleteRenderbuffers = EXTFramebufferObject::glDeleteRenderbuffersEXT;
                glDeleteRenderbuffers_buf = EXTFramebufferObject::glDeleteRenderbuffersEXT;
                glGenRenderbuffers = EXTFramebufferObject::glGenRenderbuffersEXT;
                glGenRenderbuffers_buf = EXTFramebufferObject::glGenRenderbuffersEXT;
                glRenderbufferStorage = EXTFramebufferObject::glRenderbufferStorageEXT;
                glBindFramebuffer = EXTFramebufferObject::glBindFramebufferEXT;
                glDeleteFramebuffers = EXTFramebufferObject::glDeleteFramebuffersEXT;
                glDeleteFramebuffers_buf = EXTFramebufferObject::glDeleteFramebuffersEXT;
                glGenFramebuffers = EXTFramebufferObject::glGenFramebuffersEXT;
                glGenFramebuffers_buf = EXTFramebufferObject::glGenFramebuffersEXT;
                glCheckFramebufferStatus = EXTFramebufferObject::glCheckFramebufferStatusEXT;
                glFramebufferTexture1D = EXTFramebufferObject::glFramebufferTexture1DEXT;
                glFramebufferTexture2D = EXTFramebufferObject::glFramebufferTexture2DEXT;
                glFramebufferTexture3D = EXTFramebufferObject::glFramebufferTexture3DEXT;
                glFramebufferRenderbuffer = EXTFramebufferObject::glFramebufferRenderbufferEXT;
            }
            VALID &= cap.OpenGL20 || cap.GL_ARB_draw_buffers || cap.GL_ATI_draw_buffers;
            if (cap.OpenGL20) {
                glDrawBuffers = GL20::glDrawBuffers;
                glDrawBuffers_buf = GL20::glDrawBuffers;
            } else if (cap.GL_ARB_draw_buffers) {
                glDrawBuffers = ARBDrawBuffers::glDrawBuffersARB;
                glDrawBuffers_buf = ARBDrawBuffers::glDrawBuffersARB;
            } else if (cap.GL_ATI_draw_buffers) {
                glDrawBuffers = ATIDrawBuffers::glDrawBuffersATI;
                glDrawBuffers_buf = ATIDrawBuffers::glDrawBuffersATI;
            }

            VALID_Clear = cap.OpenGL30;
            if (cap.OpenGL30) {
                glClearBuffer = GL30::glClearBuffer;
                glClearBuffer_f = GL30::glClearBuffer;
                glClearBufferfi = GL30::glClearBufferfi;
                glClearBufferu = GL30::glClearBufferu;
            }

            VALID_DepthFormat = cap.OpenGL14 || cap.GL_ARB_depth_texture;
            if (cap.OpenGL14) {
                GL_DEPTH_COMPONENT16 = GL14.GL_DEPTH_COMPONENT16;
                GL_DEPTH_COMPONENT24 = GL14.GL_DEPTH_COMPONENT24;
                GL_DEPTH_COMPONENT32 = GL14.GL_DEPTH_COMPONENT32;
            } else if (cap.GL_ARB_depth_texture) {
                GL_DEPTH_COMPONENT16 = ARBDepthTexture.GL_DEPTH_COMPONENT16_ARB;
                GL_DEPTH_COMPONENT24 = ARBDepthTexture.GL_DEPTH_COMPONENT24_ARB;
                GL_DEPTH_COMPONENT32 = ARBDepthTexture.GL_DEPTH_COMPONENT32_ARB;
            }

            VALID_SRGB = cap.OpenGL30 || cap.GL_ARB_framebuffer_sRGB || cap.GL_EXT_framebuffer_sRGB;
            if (cap.OpenGL30) {
                GL_FRAMEBUFFER_SRGB = GL30.GL_FRAMEBUFFER_SRGB;
                GL_FRAMEBUFFER_SRGB_CAPABLE = GL30.GL_FRAMEBUFFER_SRGB_CAPABLE;
            } else if (cap.GL_ARB_framebuffer_sRGB) {
                GL_FRAMEBUFFER_SRGB = ARBFramebufferSRGB.GL_FRAMEBUFFER_SRGB_ARB;
                GL_FRAMEBUFFER_SRGB_CAPABLE = ARBFramebufferSRGB.GL_FRAMEBUFFER_SRGB_CAPABLE_ARB;
            } else if (cap.GL_EXT_framebuffer_sRGB) {
                GL_FRAMEBUFFER_SRGB = EXTFramebufferSRGB.GL_FRAMEBUFFER_SRGB_EXT;
                GL_FRAMEBUFFER_SRGB_CAPABLE = EXTFramebufferSRGB.GL_FRAMEBUFFER_SRGB_CAPABLE_EXT;
            }

            VALID_DepthStencilFormat = cap.OpenGL30 || cap.GL_ARB_framebuffer_object || cap.GL_EXT_packed_depth_stencil;
            if (cap.OpenGL30) {
                GL_DEPTH24_STENCIL8 = GL30.GL_DEPTH24_STENCIL8;
                GL_UNSIGNED_INT_24_8 = GL30.GL_UNSIGNED_INT_24_8;
            } else if (cap.GL_ARB_framebuffer_object) {
                GL_DEPTH24_STENCIL8 = ARBFramebufferObject.GL_DEPTH24_STENCIL8;
                GL_UNSIGNED_INT_24_8 = ARBFramebufferObject.GL_UNSIGNED_INT_24_8;
            } else if (cap.GL_EXT_packed_depth_stencil) {
                GL_DEPTH24_STENCIL8 = EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT;
                GL_UNSIGNED_INT_24_8 = EXTPackedDepthStencil.GL_UNSIGNED_INT_24_8_EXT;
            }

            VALID_ColorBufferFloat = cap.OpenGL30 || cap.GL_ARB_color_buffer_float;
            if (cap.OpenGL30) {
                GL_CLAMP_VERTEX_COLOR = GL30.GL_CLAMP_VERTEX_COLOR;
                GL_CLAMP_FRAGMENT_COLOR = GL30.GL_CLAMP_FRAGMENT_COLOR;
                GL_CLAMP_READ_COLOR = GL30.GL_CLAMP_READ_COLOR;
                GL_FIXED_ONLY = GL30.GL_FIXED_ONLY;

                glClampColor = GL30::glClampColor;
            } else if (cap.GL_ARB_color_buffer_float) {
                GL_CLAMP_VERTEX_COLOR = ARBColorBufferFloat.GL_CLAMP_VERTEX_COLOR_ARB;
                GL_CLAMP_FRAGMENT_COLOR = ARBColorBufferFloat.GL_CLAMP_FRAGMENT_COLOR_ARB;
                GL_CLAMP_READ_COLOR = ARBColorBufferFloat.GL_CLAMP_READ_COLOR_ARB;
                GL_FIXED_ONLY = ARBColorBufferFloat.GL_FIXED_ONLY_ARB;

                glClampColor = ARBColorBufferFloat::glClampColorARB;
            }

            VALID_DepthBufferFloat = cap.OpenGL30 || cap.GL_ARB_depth_buffer_float || cap.GL_NV_depth_buffer_float;
            if (cap.OpenGL30) {
                GL_DEPTH_COMPONENT32F = GL30.GL_DEPTH_COMPONENT32F;
                GL_DEPTH32F_STENCIL8 = GL30.GL_DEPTH32F_STENCIL8;
                GL_FLOAT_32_UNSIGNED_INT_24_8_REV = GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;
            } else if (cap.GL_ARB_depth_buffer_float) {
                GL_DEPTH_COMPONENT32F = ARBDepthBufferFloat.GL_DEPTH_COMPONENT32F;
                GL_DEPTH32F_STENCIL8 = ARBDepthBufferFloat.GL_DEPTH32F_STENCIL8;
                GL_FLOAT_32_UNSIGNED_INT_24_8_REV = ARBDepthBufferFloat.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;
            } else if (cap.GL_NV_depth_buffer_float) {
                GL_DEPTH_COMPONENT32F = NVDepthBufferFloat.GL_DEPTH_COMPONENT32F_NV;
                GL_DEPTH32F_STENCIL8 = NVDepthBufferFloat.GL_DEPTH32F_STENCIL8_NV;
                GL_FLOAT_32_UNSIGNED_INT_24_8_REV = NVDepthBufferFloat.GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV;
            }

            VALID_DepthStencilAttachment = cap.OpenGL30 || cap.GL_ARB_framebuffer_object;
            if (cap.OpenGL30) {
                GL_DEPTH_STENCIL_ATTACHMENT = GL30.GL_DEPTH_STENCIL_ATTACHMENT;
            } else if (cap.GL_ARB_framebuffer_object) {
                GL_DEPTH_STENCIL_ATTACHMENT = ARBFramebufferObject.GL_DEPTH_STENCIL_ATTACHMENT;
            }

            VALID_MultisampleRBO = cap.OpenGL30 || cap.GL_ARB_framebuffer_object || cap.GL_EXT_framebuffer_multisample;
            if (cap.OpenGL30) {
                GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;
                GL_MAX_SAMPLES = GL30.GL_MAX_SAMPLES;

                glRenderbufferStorageMultisample = GL30::glRenderbufferStorageMultisample;
            } else if (cap.GL_ARB_framebuffer_object) {
                GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;
                GL_MAX_SAMPLES = ARBFramebufferObject.GL_MAX_SAMPLES;

                glRenderbufferStorageMultisample = ARBFramebufferObject::glRenderbufferStorageMultisample;
            } else if (cap.GL_EXT_framebuffer_multisample) {
                GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = EXTFramebufferMultisample.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT;
                GL_MAX_SAMPLES = EXTFramebufferMultisample.GL_MAX_SAMPLES_EXT;

                glRenderbufferStorageMultisample = EXTFramebufferMultisample::glRenderbufferStorageMultisampleEXT;
            }

            VALID_TextureLayer = cap.OpenGL30 || cap.GL_ARB_framebuffer_object || cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4 || cap.GL_EXT_texture_array || cap.GL_NV_geometry_program4;
            if (cap.OpenGL30) {
                glFramebufferTextureLayer = GL30::glFramebufferTextureLayer;
            } else if (cap.GL_ARB_framebuffer_object) {
                glFramebufferTextureLayer = ARBFramebufferObject::glFramebufferTextureLayer;
            } else if (cap.GL_ARB_geometry_shader4) {
                glFramebufferTextureLayer = ARBGeometryShader4::glFramebufferTextureLayerARB;
            } else if (cap.GL_EXT_geometry_shader4) {
                glFramebufferTextureLayer = EXTGeometryShader4::glFramebufferTextureLayerEXT;
            } else if (cap.GL_EXT_texture_array) {
                glFramebufferTextureLayer = EXTTextureArray::glFramebufferTextureLayerEXT;
            } else if (cap.GL_NV_geometry_program4) {
                glFramebufferTextureLayer = NVGeometryProgram4::glFramebufferTextureLayerEXT;
            }

            VALID_Blit = cap.OpenGL30 || cap.GL_ARB_framebuffer_object || cap.GL_EXT_framebuffer_blit;
            if (cap.OpenGL30) {
                GL_READ_FRAMEBUFFER = GL30.GL_READ_FRAMEBUFFER;
                GL_DRAW_FRAMEBUFFER = GL30.GL_DRAW_FRAMEBUFFER;
                GL_DRAW_FRAMEBUFFER_BINDING = GL30.GL_DRAW_FRAMEBUFFER_BINDING;
                GL_READ_FRAMEBUFFER_BINDING = GL30.GL_READ_FRAMEBUFFER_BINDING;

                glBlitFramebuffer = GL30::glBlitFramebuffer;
            } else if (cap.GL_ARB_framebuffer_object) {
                GL_READ_FRAMEBUFFER = ARBFramebufferObject.GL_READ_FRAMEBUFFER;
                GL_DRAW_FRAMEBUFFER = ARBFramebufferObject.GL_DRAW_FRAMEBUFFER;
                GL_DRAW_FRAMEBUFFER_BINDING = ARBFramebufferObject.GL_DRAW_FRAMEBUFFER_BINDING;
                GL_READ_FRAMEBUFFER_BINDING = ARBFramebufferObject.GL_READ_FRAMEBUFFER_BINDING;

                glBlitFramebuffer = ARBFramebufferObject::glBlitFramebuffer;
            } else if (cap.GL_EXT_framebuffer_blit) {
                GL_READ_FRAMEBUFFER = EXTFramebufferBlit.GL_READ_FRAMEBUFFER_EXT;
                GL_DRAW_FRAMEBUFFER = EXTFramebufferBlit.GL_DRAW_FRAMEBUFFER_EXT;
                GL_DRAW_FRAMEBUFFER_BINDING = EXTFramebufferBlit.GL_DRAW_FRAMEBUFFER_BINDING_EXT;
                GL_READ_FRAMEBUFFER_BINDING = EXTFramebufferBlit.GL_READ_FRAMEBUFFER_BINDING_EXT;

                glBlitFramebuffer = EXTFramebufferBlit::glBlitFramebufferEXT;
            }

            VALID_MultisampleTex = cap.OpenGL32 || cap.GL_ARB_texture_multisample;
            if (cap.OpenGL32) {
                GL_SAMPLE_MASK = GL32.GL_SAMPLE_MASK;
                GL_TEXTURE_2D_MULTISAMPLE = GL32.GL_TEXTURE_2D_MULTISAMPLE;
                GL_PROXY_TEXTURE_2D_MULTISAMPLE = GL32.GL_PROXY_TEXTURE_2D_MULTISAMPLE;
                GL_TEXTURE_BINDING_2D_MULTISAMPLE = GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE;
                GL_TEXTURE_2D_MULTISAMPLE_ARRAY = GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
                GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = GL32.GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY;
                GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY;

                glTexImage2DMultisample = GL32::glTexImage2DMultisample;
                glTexImage3DMultisample = GL32::glTexImage3DMultisample;
                glSampleMaski = GL32::glSampleMaski;
            } else if (cap.GL_ARB_texture_multisample) {
                GL_SAMPLE_MASK = ARBTextureMultisample.GL_SAMPLE_MASK;
                GL_TEXTURE_2D_MULTISAMPLE = ARBTextureMultisample.GL_TEXTURE_2D_MULTISAMPLE;
                GL_PROXY_TEXTURE_2D_MULTISAMPLE = ARBTextureMultisample.GL_PROXY_TEXTURE_2D_MULTISAMPLE;
                GL_TEXTURE_BINDING_2D_MULTISAMPLE = ARBTextureMultisample.GL_TEXTURE_BINDING_2D_MULTISAMPLE;
                GL_TEXTURE_2D_MULTISAMPLE_ARRAY = ARBTextureMultisample.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
                GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = ARBTextureMultisample.GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY;
                GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = ARBTextureMultisample.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY;

                glTexImage2DMultisample = ARBTextureMultisample::glTexImage2DMultisample;
                glTexImage3DMultisample = ARBTextureMultisample::glTexImage3DMultisample;
                glSampleMaski = ARBTextureMultisample::glSampleMaski;
            }

            VALID_TextureCast = cap.OpenGL32 || cap.GL_ARB_geometry_shader4 || cap.GL_EXT_geometry_shader4 || cap.GL_NV_geometry_program4;
            if (cap.OpenGL32) {
                glFramebufferTexture = GL32::glFramebufferTexture;
            } else if (cap.GL_ARB_geometry_shader4) {
                glFramebufferTexture = ARBGeometryShader4::glFramebufferTextureARB;
            } else if (cap.GL_EXT_geometry_shader4) {
                glFramebufferTexture = EXTGeometryShader4::glFramebufferTextureEXT;
            } else if (cap.GL_NV_geometry_program4) {
                glFramebufferTexture = NVGeometryProgram4::glFramebufferTextureEXT;
            }

            VALID_MultisampleTexStorage = cap.OpenGL43 || cap.GL_ARB_texture_storage_multisample;
            if (cap.OpenGL43) {
                glTexStorage2DMultisample = GL43::glTexStorage2DMultisample;
                glTexStorage3DMultisample = GL43::glTexStorage3DMultisample;
            } else if (cap.GL_ARB_texture_storage_multisample) {
                glTexStorage2DMultisample = ARBTextureStorageMultisample::glTexStorage2DMultisample;
                glTexStorage3DMultisample = ARBTextureStorageMultisample::glTexStorage3DMultisample;
            }

            VALID_Invalidate = cap.OpenGL43 || cap.GL_ARB_invalidate_subdata;
            if (cap.OpenGL43) {
                glInvalidateFramebuffer = GL43::glInvalidateFramebuffer;
                glInvalidateSubFramebuffer = GL43::glInvalidateSubFramebuffer;
            } else if (cap.GL_ARB_invalidate_subdata) {
                glInvalidateFramebuffer = ARBInvalidateSubdata::glInvalidateFramebuffer;
                glInvalidateSubFramebuffer = ARBInvalidateSubdata::glInvalidateSubFramebuffer;
            }

            VALID_NoAttachments = cap.OpenGL43 || cap.GL_ARB_framebuffer_no_attachments;
            if (cap.OpenGL43) {
                GL_FRAMEBUFFER_DEFAULT_WIDTH = GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH;
                GL_FRAMEBUFFER_DEFAULT_HEIGHT = GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT;
                GL_FRAMEBUFFER_DEFAULT_LAYERS = GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS;
                GL_FRAMEBUFFER_DEFAULT_SAMPLES = GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES;
                GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS;

                glFramebufferParameteri = GL43::glFramebufferParameteri;
            } else if (cap.GL_ARB_framebuffer_no_attachments) {
                GL_FRAMEBUFFER_DEFAULT_WIDTH = ARBFramebufferNoAttachments.GL_FRAMEBUFFER_DEFAULT_WIDTH;
                GL_FRAMEBUFFER_DEFAULT_HEIGHT = ARBFramebufferNoAttachments.GL_FRAMEBUFFER_DEFAULT_HEIGHT;
                GL_FRAMEBUFFER_DEFAULT_LAYERS = ARBFramebufferNoAttachments.GL_FRAMEBUFFER_DEFAULT_LAYERS;
                GL_FRAMEBUFFER_DEFAULT_SAMPLES = ARBFramebufferNoAttachments.GL_FRAMEBUFFER_DEFAULT_SAMPLES;
                GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = ARBFramebufferNoAttachments.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS;

                glFramebufferParameteri = ARBFramebufferNoAttachments::glFramebufferParameteri;
            }

            VALID_BoxUtilBase = valid() && valid_DepthFormat() && valid_DepthStencilFormat() && valid_DepthStencilAttachment(); // blit always valid if valid_DepthStencilAttachment() returns true
        }

        public static boolean valid() {
            return VALID;
        }

        /**
         * {@link GL30#glClearBuffer(int, int, IntBuffer)}<p>
         * {@link GL30#glClearBuffer(int, int, FloatBuffer)}<p>
         * {@link GL30#glClearBufferfi(int, int, float, int)}<p>
         * {@link GL30#glClearBufferu(int, int, IntBuffer)}<p>
         */
        public static boolean valid_Clear() {
            return VALID_Clear;
        }

        /**
         * {@link GL30#GL_FRAMEBUFFER_SRGB}<p>
         * {@link GL30#GL_FRAMEBUFFER_SRGB_CAPABLE}
         */
        public static boolean valid_SRGB() {
            return VALID_SRGB;
        }

        /**
         * {@link GL14#GL_DEPTH_COMPONENT16}<p>
         * {@link GL14#GL_DEPTH_COMPONENT24}<p>
         * {@link GL14#GL_DEPTH_COMPONENT32}
         */
        public static boolean valid_DepthFormat() {
            return VALID_DepthFormat;
        }

        /**
         * {@link GL30#GL_UNSIGNED_INT_24_8}<p>
         * {@link GL30#GL_DEPTH24_STENCIL8}
         */
        public static boolean valid_DepthStencilFormat() {
            return VALID_DepthStencilFormat;
        }

        /**
         * {@link GL30#glClampColor(int, int)}
         */
        public static boolean valid_ColorBufferFloat() {
            return VALID_ColorBufferFloat;
        }

        /**
         * {@link GL30#GL_DEPTH_COMPONENT32F}<p>
         * {@link GL30#GL_DEPTH32F_STENCIL8}<p>
         * {@link GL30#GL_FLOAT_32_UNSIGNED_INT_24_8_REV}
         */
        public static boolean valid_DepthBufferFloat() {
            return VALID_DepthBufferFloat;
        }

        /**
         * {@link GL30#GL_DEPTH_STENCIL_ATTACHMENT}
         */
        public static boolean valid_DepthStencilAttachment() {
            return VALID_DepthStencilAttachment;
        }

        /**
         * {@link GL30#glRenderbufferStorageMultisample(int, int, int, int, int)}
         */
        public static boolean valid_MultisampleRBO() {
            return VALID_MultisampleRBO;
        }

        /**
         * {@link GL30#glFramebufferTextureLayer(int, int, int, int, int)}
         */
        public static boolean valid_TextureLayer() {
            return VALID_TextureLayer;
        }

        /**
         * {@link GL30#glBlitFramebuffer(int, int, int, int, int, int, int, int, int, int)}
         */
        public static boolean valid_Blit() {
            return VALID_Blit;
        }

        /**
         * {@link GL32#glTexImage2DMultisample(int, int, int, int, int, boolean)}<p>
         * {@link GL32#glTexImage3DMultisample(int, int, int, int, int, int, boolean)}<p>
         * {@link GL32#glSampleMaski(int, int)}
         */
        public static boolean valid_MultisampleTex() {
            return VALID_MultisampleTex;
        }

        /**
         * {@link GL32#glFramebufferTexture(int, int, int, int)}
         */
        public static boolean valid_TextureCast() {
            return VALID_TextureCast;
        }

        /**
         * {@link GL43#glTexStorage2DMultisample(int, int, int, int, int, boolean)}<p>
         * {@link GL43#glTexStorage3DMultisample(int, int, int, int, int, int, boolean)}
         */
        public static boolean valid_MultisampleTexStorage() {
            return VALID_MultisampleTexStorage;
        }

        /**
         * {@link GL43#glInvalidateFramebuffer(int, IntBuffer)}<p>
         * {@link GL43#glInvalidateSubFramebuffer(int, IntBuffer, int, int, int, int)}
         */
        public static boolean valid_Invalidate() {
            return VALID_Invalidate;
        }

        /**
         * {@link GL43#glFramebufferParameteri(int, int, int)}
         */
        public static boolean valid_NoAttachments() {
            return VALID_NoAttachments;
        }

        /**
         * <code>valid() && valid_DepthFormat() && valid_DepthStencilFormat() && valid_DepthStencilAttachment() && valid_Blit()</code>
         */
        public static boolean valid_BoxUtilBase() {
            return VALID_BoxUtilBase;
        }

        public static void glReadBuffer(int mode) {
            GL11.glReadBuffer(mode);
        }

        public static void glClearColor(float red, float green, float blue, float alpha) {
            GL11.glClearColor(red, green, blue, alpha);
        }

        public static void glClearDepth(double depth) {
            GL11.glClearDepth(depth);
        }

        public static void glClearStencil(int s) {
            GL11.glClearStencil(s);
        }

        public static void glClear(int mask) {
            GL11.glClear(mask);
        }

        public static void glBindRenderbuffer(int target, int renderbuffer) {
            glBindRenderbuffer.run(target, renderbuffer);
        }

        public static void glDeleteRenderbuffers(int renderbuffer) {
            glDeleteRenderbuffers.accept(renderbuffer);
        }

        public static void glDeleteRenderbuffers(IntBuffer renderbuffers) {
            glDeleteRenderbuffers_buf.accept(renderbuffers);
        }

        public static int glGenRenderbuffers() {
            return glGenRenderbuffers.getAsInt();
        }

        public static void glGenRenderbuffers(IntBuffer renderbuffers) {
            glGenRenderbuffers_buf.accept(renderbuffers);
        }

        public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
            glRenderbufferStorage.run(target, internalformat, width, height);
        }

        public static void glBindFramebuffer(int target, int framebuffer) {
            glBindFramebuffer.run(target, framebuffer);
        }

        public static void glDeleteFramebuffers(int framebuffer) {
            glDeleteFramebuffers.accept(framebuffer);
        }

        public static void glDeleteFramebuffers(IntBuffer framebuffers) {
            glDeleteFramebuffers_buf.accept(framebuffers);
        }

        public static int glGenFramebuffers() {
            return glGenFramebuffers.getAsInt();
        }

        public static void glGenFramebuffers(IntBuffer framebuffers) {
            glGenFramebuffers_buf.accept(framebuffers);
        }

        public static int glCheckFramebufferStatus(int target) {
            return glCheckFramebufferStatus.applyAsInt(target);
        }

        public static void glFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level) {
            glFramebufferTexture1D.run(target, attachment, textarget, texture, level);
        }

        public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
            glFramebufferTexture2D.run(target, attachment, textarget, texture, level);
        }

        public static void glFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset) {
            glFramebufferTexture3D.run(target, attachment, textarget, texture, level, zoffset);
        }

        public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
            glFramebufferRenderbuffer.run(target, attachment, renderbuffertarget, renderbuffer);
        }

        public static void glGenerateMipmap(int target) {
            glGenerateMipmap.accept(target);
        }

        public static void glDrawBuffers(int buffer) {
            glDrawBuffers.accept(buffer);
        }

        public static void glDrawBuffers( IntBuffer buffers) {
            glDrawBuffers_buf.accept(buffers);
        }

        public static void glClearBuffer(int buffer, int drawbuffer, IntBuffer value) {
            glClearBuffer.run(buffer, drawbuffer, value);
        }

        public static void glClearBuffer(int buffer, int drawbuffer, FloatBuffer value) {
            glClearBuffer_f.run(buffer, drawbuffer, value);
        }

        public static void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
            glClearBufferfi.run(buffer, drawbuffer, depth, stencil);
        }

        public static void glClearBufferu(int buffer, int drawbuffer, IntBuffer value) {
            glClearBufferu.run(buffer, drawbuffer, value);
        }

        public static void glClampColor(int target, int clamp) {
            glClampColor.run(target, clamp);
        }

        public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
            glRenderbufferStorageMultisample.run(target, samples, internalformat, width, height);
        }

        public static void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
            glFramebufferTextureLayer.run(target, attachment, texture, level, layer);
        }

        public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
            glBlitFramebuffer.run(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
        }

        public static void glTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
            glTexImage2DMultisample.run(target, samples, internalformat, width, height, fixedsamplelocations);
        }

        public static void glTexImage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
            glTexImage3DMultisample.run(target, samples, internalformat, width, height, depth, fixedsamplelocations);
        }

        public static void glFramebufferTexture(int target, int attachment, int texture, int level) {
            glFramebufferTexture.run(target, attachment, texture, level);
        }

        public static void glTexStorage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
            glTexStorage2DMultisample.run(target, samples, internalformat, width, height, fixedsamplelocations);
        }

        public static void glTexStorage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
            glTexStorage3DMultisample.run(target, samples, internalformat, width, height, depth, fixedsamplelocations);
        }

        public static void glSampleMaski(int index, int mask) {
            glSampleMaski.run(index, mask);
        }

        public static void glInvalidateFramebuffer(int target, IntBuffer attachments) {
            glInvalidateFramebuffer.run(target, attachments);
        }

        public static void glInvalidateSubFramebuffer(int target, IntBuffer attachments, int x, int y, int width, int height) {
            glInvalidateSubFramebuffer.run(target, attachments, x, y, width, height);
        }

        public static void glFramebufferParameteri(int target, int pname, int param) {
            glFramebufferParameteri.run(target, pname, param);
        }

        private FBO() {}
    }

    public sealed static class Texture permits Texture.Compressed, Texture.Bindless {
        private static boolean VALID_CubeMap;
        private static boolean VALID_BorderClamp;
        private static boolean VALID_MirroredRepeat;
        private static boolean VALID_NPOT;
        private static boolean VALID_SRGB;
        private static boolean VALID_TexArray;
        private static boolean VALID_TexInt;
        private static boolean VALID_TexFloat;
        private static boolean VALID_R11F_G11F_B10F;
        private static boolean VALID_RGB9_E5;
        private static boolean VALID_TexRectangle;
        private static boolean VALID_SeamlessCubeMap;
        private static boolean VALID_TexSnorm;
        private static boolean VALID_RGB10_A2UI;
        private static boolean VALID_CubeMapArray;
        private static boolean VALID_RGB565;
        private static boolean VALID_ImageLoadStore;
        private static boolean VALID_TexStorage;
        private static boolean VALID_SeamlessCubeMapPerTex;
        private static boolean VALID_CopyImage;
        private static boolean VALID_TexView;
        private static boolean VALID_Invalidate;
        private static boolean VALID_MultiBind;
        private static boolean VALID_ClearTex;

        public static final int GL_TEXTURE_1D = GL11.GL_TEXTURE_1D;
        public static final int GL_PROXY_TEXTURE_1D = GL11.GL_PROXY_TEXTURE_1D;
        public static final int GL_TEXTURE_BINDING_1D = GL11.GL_TEXTURE_BINDING_1D;
        public static final int GL_TEXTURE_2D = GL11.GL_TEXTURE_2D;
        public static final int GL_PROXY_TEXTURE_2D = GL11.GL_PROXY_TEXTURE_2D;
        public static final int GL_TEXTURE_BINDING_2D = GL11.GL_TEXTURE_BINDING_2D;
        public static final int GL_TEXTURE_3D = GL12.GL_TEXTURE_3D;
        public static final int GL_PROXY_TEXTURE_3D = GL12.GL_PROXY_TEXTURE_3D;
        public static final int GL_TEXTURE_BINDING_3D = GL12.GL_TEXTURE_BINDING_3D;
        public static final int GL_MAX_TEXTURE_SIZE = GL11.GL_MAX_TEXTURE_SIZE;
        public static final int GL_MAX_3D_TEXTURE_SIZE = GL12.GL_MAX_3D_TEXTURE_SIZE;

        public static final int GL_LINEAR = GL11.GL_LINEAR;
        public static final int GL_NEAREST = GL11.GL_NEAREST;
        public static final int GL_CLAMP = GL11.GL_CLAMP;
        public static final int GL_REPEAT = GL11.GL_REPEAT;
        public static final int GL_CLAMP_TO_EDGE = GL12.GL_CLAMP_TO_EDGE;
        public static final int GL_NEAREST_MIPMAP_NEAREST = GL11.GL_NEAREST_MIPMAP_NEAREST;
        public static final int GL_LINEAR_MIPMAP_NEAREST = GL11.GL_LINEAR_MIPMAP_NEAREST;
        public static final int GL_NEAREST_MIPMAP_LINEAR = GL11.GL_NEAREST_MIPMAP_LINEAR;
        public static final int GL_LINEAR_MIPMAP_LINEAR = GL11.GL_LINEAR_MIPMAP_LINEAR;
        public static final int GL_TEXTURE_MIN_FILTER = GL11.GL_TEXTURE_MIN_FILTER;
        public static final int GL_TEXTURE_MAG_FILTER = GL11.GL_TEXTURE_MAG_FILTER;
        public static final int GL_TEXTURE_MIN_LOD = GL12.GL_TEXTURE_MIN_LOD;
        public static final int GL_TEXTURE_MAX_LOD = GL12.GL_TEXTURE_MAX_LOD;
        public static final int GL_TEXTURE_BASE_LEVEL = GL12.GL_TEXTURE_BASE_LEVEL;
        public static final int GL_TEXTURE_MAX_LEVEL = GL12.GL_TEXTURE_MAX_LEVEL;
        public static final int GL_TEXTURE_WRAP_S = GL11.GL_TEXTURE_WRAP_S;
        public static final int GL_TEXTURE_WRAP_T = GL11.GL_TEXTURE_WRAP_T;
        public static final int GL_TEXTURE_WRAP_R = GL12.GL_TEXTURE_WRAP_R;
        public static final int GL_TEXTURE_WIDTH = GL11.GL_TEXTURE_WIDTH;
        public static final int GL_TEXTURE_HEIGHT = GL11.GL_TEXTURE_HEIGHT;
        public static final int GL_TEXTURE_DEPTH = GL12.GL_TEXTURE_DEPTH;
        public static final int GL_TEXTURE_BORDER_COLOR = GL11.GL_TEXTURE_BORDER_COLOR;
        public static final int GL_TEXTURE_BORDER = GL11.GL_TEXTURE_BORDER;
        public static final int GL_TEXTURE_INTERNAL_FORMAT = GL11.GL_TEXTURE_INTERNAL_FORMAT;
        public static final int GL_TEXTURE_COMPONENTS = GL11.GL_TEXTURE_COMPONENTS;

        public static final int GL_LUMINANCE = GL11.GL_LUMINANCE;
        public static final int GL_LUMINANCE8 = GL11.GL_LUMINANCE8;
        public static final int GL_LUMINANCE_ALPHA = GL11.GL_LUMINANCE_ALPHA;
        public static final int GL_LUMINANCE16_ALPHA16 = GL11.GL_LUMINANCE16_ALPHA16;
        public static final int GL_INTENSITY = GL11.GL_INTENSITY;
        public static final int GL_INTENSITY8 = GL11.GL_INTENSITY8;
        public static final int GL_INTENSITY16 = GL11.GL_INTENSITY16;
        public static final int GL_RED = GL11.GL_RED;
        public static final int GL_GREEN = GL11.GL_GREEN;
        public static final int GL_BLUE = GL11.GL_BLUE;
        public static final int GL_ALPHA = GL11.GL_ALPHA;
        public static final int GL_RGB = GL11.GL_RGB;
        public static final int GL_RGBA = GL11.GL_RGBA;
        public static final int GL_BGR = GL12.GL_BGR;
        public static final int GL_BGRA = GL12.GL_BGRA;
        public static final int GL_R3_G3_B2 = 10768;
        public static final int GL_RGB4 = GL11.GL_RGB4;
        public static final int GL_RGB5 = GL11.GL_RGB5;
        public static final int GL_RGB8 = GL11.GL_RGB8;
        public static final int GL_RGB10 = GL11.GL_RGB10;
        public static final int GL_RGB12 = GL11.GL_RGB12;
        public static final int GL_RGB16 = GL11.GL_RGB16;
        public static final int GL_RGBA2 = GL11.GL_RGBA2;
        public static final int GL_RGBA4 = GL11.GL_RGBA4;
        public static final int GL_RGB5_A1 = GL11.GL_RGB5_A1;
        public static final int GL_RGBA8 = GL11.GL_RGBA8;
        public static final int GL_RGB10_A2 = GL11.GL_RGB10_A2;
        public static final int GL_RGBA12 = GL11.GL_RGBA12;
        public static final int GL_RGBA16 = GL11.GL_RGBA16;

        public static final int GL_UNSIGNED_BYTE_3_3_2 = GL12.GL_UNSIGNED_BYTE_3_3_2;
        public static final int GL_UNSIGNED_BYTE_2_3_3_REV = GL12.GL_UNSIGNED_BYTE_2_3_3_REV;
        public static final int GL_UNSIGNED_SHORT_5_6_5 = GL12.GL_UNSIGNED_SHORT_5_6_5;
        public static final int GL_UNSIGNED_SHORT_5_6_5_REV = GL12.GL_UNSIGNED_SHORT_5_6_5_REV;
        public static final int GL_UNSIGNED_SHORT_4_4_4_4 = GL12.GL_UNSIGNED_SHORT_4_4_4_4;
        public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV = GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV;
        public static final int GL_UNSIGNED_SHORT_5_5_5_1 = GL12.GL_UNSIGNED_SHORT_5_5_5_1;
        public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV = GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
        public static final int GL_UNSIGNED_INT_8_8_8_8 = GL12.GL_UNSIGNED_INT_8_8_8_8;
        public static final int GL_UNSIGNED_INT_8_8_8_8_REV = GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
        public static final int GL_UNSIGNED_INT_10_10_10_2 = GL12.GL_UNSIGNED_INT_10_10_10_2;
        public static final int GL_UNSIGNED_INT_2_10_10_10_REV = GL12.GL_UNSIGNED_INT_2_10_10_10_REV;

        public static int GL_NORMAL_MAP;
        public static int GL_REFLECTION_MAP;
        public static int GL_TEXTURE_CUBE_MAP;
        public static int GL_TEXTURE_BINDING_CUBE_MAP;
        public static int GL_TEXTURE_CUBE_MAP_POSITIVE_X;
        public static int GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
        public static int GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
        public static int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
        public static int GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
        public static int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
        public static int GL_PROXY_TEXTURE_CUBE_MAP;
        public static int GL_MAX_CUBE_MAP_TEXTURE_SIZE;

        public static int GL_CLAMP_TO_BORDER;

        public static int GL_MIRRORED_REPEAT;

        public static int GL_SRGB;
        public static int GL_SRGB8;
        public static int GL_SRGB_ALPHA;
        public static int GL_SRGB8_ALPHA8;
        public static int GL_SLUMINANCE_ALPHA;
        public static int GL_SLUMINANCE8_ALPHA8;
        public static int GL_SLUMINANCE;
        public static int GL_SLUMINANCE8;

        public static int GL_TEXTURE_1D_ARRAY;
        public static int GL_PROXY_TEXTURE_1D_ARRAY;
        public static int GL_TEXTURE_BINDING_1D_ARRAY;
        public static int GL_TEXTURE_2D_ARRAY;
        public static int GL_PROXY_TEXTURE_2D_ARRAY;
        public static int GL_TEXTURE_BINDING_2D_ARRAY;
        public static int GL_MAX_ARRAY_TEXTURE_LAYERS;

        public static int GL_R8;
        public static int GL_R8I;
        public static int GL_R8UI;
        public static int GL_R16;
        public static int GL_R16I;
        public static int GL_R16UI;
        public static int GL_R16F;
        public static int GL_R32I;
        public static int GL_R32UI;
        public static int GL_R32F;
        public static int GL_RG8;
        public static int GL_RG8I;
        public static int GL_RG8UI;
        public static int GL_RG16;
        public static int GL_RG16I;
        public static int GL_RG16UI;
        public static int GL_RG16F;
        public static int GL_RG32I;
        public static int GL_RG32UI;
        public static int GL_RG32F;
        public static int GL_RGB8I;
        public static int GL_RGB8UI;
        public static int GL_RGB16I;
        public static int GL_RGB16UI;
        public static int GL_RGB32I;
        public static int GL_RGB32UI;
        public static int GL_RGBA8I;
        public static int GL_RGBA8UI;
        public static int GL_RGBA16I;
        public static int GL_RGBA16UI;
        public static int GL_RGBA32I;
        public static int GL_RGBA32UI;
        public static int GL_ALPHA8I;
        public static int GL_ALPHA8UI;
        public static int GL_ALPHA16I;
        public static int GL_ALPHA16UI;
        public static int GL_ALPHA32I;
        public static int GL_ALPHA32UI;
        public static int GL_RED_INTEGER;
        public static int GL_GREEN_INTEGER;
        public static int GL_BLUE_INTEGER;
        public static int GL_ALPHA_INTEGER;
        public static int GL_RG;
        public static int GL_RG_INTEGER;
        public static int GL_RGB_INTEGER;
        public static int GL_RGBA_INTEGER;
        public static int GL_BGR_INTEGER;
        public static int GL_BGRA_INTEGER;

        public static int GL_RGB16F;
        public static int GL_RGB32F;
        public static int GL_RGBA16F;
        public static int GL_RGBA32F;
        public static int GL_ALPHA16F;
        public static int GL_ALPHA32F;

        public static int GL_R11F_G11F_B10F;
        public static int GL_UNSIGNED_INT_10F_11F_11F_REV;

        public static int GL_RGB9_E5;
        public static int GL_UNSIGNED_INT_5_9_9_9_REV;

        public static int GL_TEXTURE_RECTANGLE;
        public static int GL_PROXY_TEXTURE_RECTANGLE;
        public static int GL_TEXTURE_BINDING_RECTANGLE;
        public static int GL_MAX_RECTANGLE_TEXTURE_SIZE;

        public static int GL_TEXTURE_CUBE_MAP_SEAMLESS;

        public static int GL_R8_SNORM;
        public static int GL_RG8_SNORM;
        public static int GL_RGB8_SNORM;
        public static int GL_RGBA8_SNORM;
        public static int GL_R16_SNORM;
        public static int GL_RG16_SNORM;
        public static int GL_RGB16_SNORM;
        public static int GL_RGBA16_SNORM;
        public static int GL_RED_SNORM;
        public static int GL_RG_SNORM;
        public static int GL_RGB_SNORM;
        public static int GL_RGBA_SNORM;

        public static int GL_RGB10_A2UI;

        public static int GL_TEXTURE_CUBE_MAP_ARRAY;
        public static int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY;
        public static int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY;

        public static int GL_RGB565;

        public static final int GL_TRUE = GL11.GL_TRUE; // just for glBindImageTexture param
        public static final int GL_FALSE = GL11.GL_FALSE;
        public static int GL_READ_ONLY;
        public static int GL_WRITE_ONLY;
        public static int GL_READ_WRITE;

        public static int GL_TEXTURE_VIEW_MIN_LEVEL;
        public static int GL_TEXTURE_VIEW_NUM_LEVELS;
        public static int GL_TEXTURE_VIEW_MIN_LAYER;
        public static int GL_TEXTURE_VIEW_NUM_LAYERS;
        public static int GL_TEXTURE_IMMUTABLE_LEVELS;

        private static IntIntIntBoolIntIntIntFun glBindImageTexture;

        private static IntIntIntIntFun glTexStorage1D;
        private static IntIntIntIntIntFun glTexStorage2D;
        private static IntIntIntIntIntIntFun glTexStorage3D;

        private static IntIntIntIntIntIntIntIntIntIntIntIntIntIntIntFun glCopyImageSubData;

        private static IntIntIntIntIntIntIntIntFun glTextureView;

        private static IntIntFun glInvalidateTexImage;
        private static IntIntIntIntIntIntIntIntFun glInvalidateTexSubImage;

        private static IntIntXFun<IntBuffer> glBindTextures;
        private static IntIntXFun<IntBuffer> glBindImageTextures;

        private static IntIntIntIntXFun<ByteBuffer> glClearTexImage_ByteBuffer;
        private static IntIntIntIntXFun<ShortBuffer> glClearTexImage_ShortBuffer;
        private static IntIntIntIntXFun<IntBuffer> glClearTexImage_IntBuffer;
        private static IntIntIntIntXFun<LongBuffer> glClearTexImage_LongBuffer;
        private static IntIntIntIntXFun<FloatBuffer> glClearTexImage_FloatBuffer;
        private static IntIntIntIntXFun<DoubleBuffer> glClearTexImage_DoubleBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<ByteBuffer> glClearTexSubImage_ByteBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<ShortBuffer> glClearTexSubImage_ShortBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<IntBuffer> glClearTexSubImage_IntBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<LongBuffer> glClearTexSubImage_LongBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<FloatBuffer> glClearTexSubImage_FloatBuffer;
        private static IntIntIntIntIntIntIntIntIntIntXFun<DoubleBuffer> glClearTexSubImage_DoubleBuffer;

        private static void init(ContextCapabilities cap) {
            if (cap.OpenGL15) {
                GL_READ_ONLY = GL15.GL_READ_ONLY;
                GL_WRITE_ONLY = GL15.GL_WRITE_ONLY;
                GL_READ_WRITE = GL15.GL_READ_WRITE;
            } else if (cap.GL_ARB_vertex_buffer_object) {
                GL_READ_ONLY = ARBVertexBufferObject.GL_READ_ONLY_ARB;
                GL_WRITE_ONLY = ARBVertexBufferObject.GL_WRITE_ONLY_ARB;
                GL_READ_WRITE = ARBVertexBufferObject.GL_READ_WRITE_ARB;
            }

            VALID_CubeMap = cap.OpenGL13 || cap.GL_ARB_texture_cube_map;
            if (cap.OpenGL13) {
                GL_NORMAL_MAP = GL13.GL_NORMAL_MAP;
                GL_REFLECTION_MAP = GL13.GL_REFLECTION_MAP;
                GL_TEXTURE_CUBE_MAP = GL13.GL_TEXTURE_CUBE_MAP;
                GL_TEXTURE_BINDING_CUBE_MAP = GL13.GL_TEXTURE_BINDING_CUBE_MAP;
                GL_TEXTURE_CUBE_MAP_POSITIVE_X = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_X = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
                GL_TEXTURE_CUBE_MAP_POSITIVE_Y = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
                GL_TEXTURE_CUBE_MAP_POSITIVE_Z = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
                GL_PROXY_TEXTURE_CUBE_MAP = GL13.GL_PROXY_TEXTURE_CUBE_MAP;
                GL_MAX_CUBE_MAP_TEXTURE_SIZE = GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE;
            } else if (cap.GL_ARB_texture_cube_map) {
                GL_NORMAL_MAP = ARBTextureCubeMap.GL_NORMAL_MAP_ARB;
                GL_REFLECTION_MAP = ARBTextureCubeMap.GL_REFLECTION_MAP_ARB;
                GL_TEXTURE_CUBE_MAP = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_ARB;
                GL_TEXTURE_BINDING_CUBE_MAP = ARBTextureCubeMap.GL_TEXTURE_BINDING_CUBE_MAP_ARB;
                GL_TEXTURE_CUBE_MAP_POSITIVE_X = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_X_ARB;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_X = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_X_ARB;
                GL_TEXTURE_CUBE_MAP_POSITIVE_Y = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_Y_ARB;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_ARB;
                GL_TEXTURE_CUBE_MAP_POSITIVE_Z = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_Z_ARB;
                GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_ARB;
                GL_PROXY_TEXTURE_CUBE_MAP = ARBTextureCubeMap.GL_PROXY_TEXTURE_CUBE_MAP_ARB;
                GL_MAX_CUBE_MAP_TEXTURE_SIZE = ARBTextureCubeMap.GL_MAX_CUBE_MAP_TEXTURE_SIZE_ARB;
            }

            VALID_BorderClamp = cap.OpenGL13 || cap.GL_ARB_texture_border_clamp;
            if (cap.OpenGL13) {
                GL_CLAMP_TO_BORDER = GL13.GL_CLAMP_TO_BORDER;
            } else if (cap.GL_ARB_texture_border_clamp) {
                GL_CLAMP_TO_BORDER = ARBTextureBorderClamp.GL_CLAMP_TO_BORDER_ARB;
            }

            VALID_MirroredRepeat = cap.OpenGL14 || cap.GL_ARB_texture_mirrored_repeat;
            if (cap.OpenGL14) {
                GL_MIRRORED_REPEAT = GL14.GL_MIRRORED_REPEAT;
            } else if (cap.GL_ARB_texture_mirrored_repeat) {
                GL_MIRRORED_REPEAT = ARBTextureMirroredRepeat.GL_MIRRORED_REPEAT_ARB;
            }

            VALID_NPOT = cap.OpenGL20 || cap.GL_ARB_texture_non_power_of_two;

            VALID_SRGB = cap.OpenGL21 || cap.GL_EXT_texture_sRGB;
            if (cap.OpenGL21) {
                GL_SRGB = GL21.GL_SRGB;
                GL_SRGB8 = GL21.GL_SRGB8;
                GL_SRGB_ALPHA = GL21.GL_SRGB_ALPHA;
                GL_SRGB8_ALPHA8 = GL21.GL_SRGB8_ALPHA8;
                GL_SLUMINANCE_ALPHA = GL21.GL_SLUMINANCE_ALPHA;
                GL_SLUMINANCE8_ALPHA8 = GL21.GL_SLUMINANCE8_ALPHA8;
                GL_SLUMINANCE = GL21.GL_SLUMINANCE;
                GL_SLUMINANCE8 = GL21.GL_SLUMINANCE8;
            } else if (cap.GL_EXT_texture_sRGB) {
                GL_SRGB = EXTTextureSRGB.GL_SRGB_EXT;
                GL_SRGB8 = EXTTextureSRGB.GL_SRGB8_EXT;
                GL_SRGB_ALPHA = EXTTextureSRGB.GL_SRGB_ALPHA_EXT;
                GL_SRGB8_ALPHA8 = EXTTextureSRGB.GL_SRGB8_ALPHA8_EXT;
                GL_SLUMINANCE_ALPHA = EXTTextureSRGB.GL_SLUMINANCE_ALPHA_EXT;
                GL_SLUMINANCE8_ALPHA8 = EXTTextureSRGB.GL_SLUMINANCE8_ALPHA8_EXT;
                GL_SLUMINANCE = EXTTextureSRGB.GL_SLUMINANCE_EXT;
                GL_SLUMINANCE8 = EXTTextureSRGB.GL_SLUMINANCE8_EXT;
            }

            VALID_TexArray = cap.OpenGL30 || cap.GL_EXT_texture_array;
            if (cap.OpenGL30) {
                GL_TEXTURE_1D_ARRAY = GL30.GL_TEXTURE_1D_ARRAY;
                GL_PROXY_TEXTURE_1D_ARRAY = GL30.GL_PROXY_TEXTURE_1D_ARRAY;
                GL_TEXTURE_BINDING_1D_ARRAY = GL30.GL_TEXTURE_BINDING_1D_ARRAY;
                GL_TEXTURE_2D_ARRAY = GL30.GL_TEXTURE_2D_ARRAY;
                GL_PROXY_TEXTURE_2D_ARRAY = GL30.GL_PROXY_TEXTURE_2D_ARRAY;
                GL_TEXTURE_BINDING_2D_ARRAY = GL30.GL_TEXTURE_BINDING_2D_ARRAY;
                GL_MAX_ARRAY_TEXTURE_LAYERS = GL30.GL_MAX_ARRAY_TEXTURE_LAYERS;
            } else if (cap.GL_EXT_texture_array) {
                GL_TEXTURE_1D_ARRAY = EXTTextureArray.GL_TEXTURE_1D_ARRAY_EXT;
                GL_PROXY_TEXTURE_1D_ARRAY = EXTTextureArray.GL_PROXY_TEXTURE_1D_ARRAY_EXT;
                GL_TEXTURE_BINDING_1D_ARRAY = EXTTextureArray.GL_TEXTURE_BINDING_1D_ARRAY_EXT;
                GL_TEXTURE_2D_ARRAY = EXTTextureArray.GL_TEXTURE_2D_ARRAY_EXT;
                GL_PROXY_TEXTURE_2D_ARRAY = EXTTextureArray.GL_PROXY_TEXTURE_2D_ARRAY_EXT;
                GL_TEXTURE_BINDING_2D_ARRAY = EXTTextureArray.GL_TEXTURE_BINDING_2D_ARRAY_EXT;
                GL_MAX_ARRAY_TEXTURE_LAYERS = EXTTextureArray.GL_MAX_ARRAY_TEXTURE_LAYERS_EXT;
            }

            VALID_TexInt = cap.OpenGL30 || (cap.GL_ARB_texture_rg && cap.GL_EXT_texture_integer);
            if (cap.OpenGL30) {
                GL_R8 = GL30.GL_R8;
                GL_R8I = GL30.GL_R8I;
                GL_R8UI = GL30.GL_R8UI;
                GL_R16 = GL30.GL_R16;
                GL_R16I = GL30.GL_R16I;
                GL_R16UI = GL30.GL_R16UI;
                GL_R16F = GL30.GL_R16F;
                GL_R32I = GL30.GL_R32I;
                GL_R32UI = GL30.GL_R32UI;
                GL_R32F = GL30.GL_R32F;
                GL_RG8 = GL30.GL_RG8;
                GL_RG8I = GL30.GL_RG8I;
                GL_RG8UI = GL30.GL_RG8UI;
                GL_RG16 = GL30.GL_RG16;
                GL_RG16I = GL30.GL_RG16I;
                GL_RG16UI = GL30.GL_RG16UI;
                GL_RG16F = GL30.GL_RG16F;
                GL_RG32I = GL30.GL_RG32I;
                GL_RG32UI = GL30.GL_RG32UI;
                GL_RG32F = GL30.GL_RG32F;
                GL_RGB8I = GL30.GL_RGB8I;
                GL_RGB8UI = GL30.GL_RGB8UI;
                GL_RGB16I = GL30.GL_RGB16I;
                GL_RGB16UI = GL30.GL_RGB16UI;
                GL_RGB32I = GL30.GL_RGB32I;
                GL_RGB32UI = GL30.GL_RGB32UI;
                GL_RGBA8I = GL30.GL_RGBA8I;
                GL_RGBA8UI = GL30.GL_RGBA8UI;
                GL_RGBA16I = GL30.GL_RGBA16I;
                GL_RGBA16UI = GL30.GL_RGBA16UI;
                GL_RGBA32I = GL30.GL_RGBA32I;
                GL_RGBA32UI = GL30.GL_RGBA32UI;
                GL_ALPHA8I = GL30.GL_ALPHA8I;
                GL_ALPHA8UI = GL30.GL_ALPHA8UI;
                GL_ALPHA16I = GL30.GL_ALPHA16I;
                GL_ALPHA16UI = GL30.GL_ALPHA16UI;
                GL_ALPHA32I = GL30.GL_ALPHA32I;
                GL_ALPHA32UI = GL30.GL_ALPHA32UI;
                GL_RED_INTEGER = GL30.GL_RED_INTEGER;
                GL_GREEN_INTEGER = GL30.GL_GREEN_INTEGER;
                GL_BLUE_INTEGER = GL30.GL_BLUE_INTEGER;
                GL_ALPHA_INTEGER = GL30.GL_ALPHA_INTEGER;
                GL_RG = GL30.GL_RG;
                GL_RG_INTEGER = GL30.GL_RG_INTEGER;
                GL_RGB_INTEGER = GL30.GL_RGB_INTEGER;
                GL_RGBA_INTEGER = GL30.GL_RGBA_INTEGER;
                GL_BGR_INTEGER = GL30.GL_BGR_INTEGER;
                GL_BGRA_INTEGER = GL30.GL_BGRA_INTEGER;
            } else if (cap.GL_ARB_texture_rg && cap.GL_EXT_texture_integer) {
                GL_R8 = ARBTextureRg.GL_R8;
                GL_R8I = ARBTextureRg.GL_R8I;
                GL_R8UI = ARBTextureRg.GL_R8UI;
                GL_R16 = ARBTextureRg.GL_R16;
                GL_R16I = ARBTextureRg.GL_R16I;
                GL_R16UI = ARBTextureRg.GL_R16UI;
                GL_R16F = ARBTextureRg.GL_R16F;
                GL_R32I = ARBTextureRg.GL_R32I;
                GL_R32UI = ARBTextureRg.GL_R32UI;
                GL_R32F = ARBTextureRg.GL_R32F;
                GL_RG8 = ARBTextureRg.GL_RG8;
                GL_RG8I = ARBTextureRg.GL_RG8I;
                GL_RG8UI = ARBTextureRg.GL_RG8UI;
                GL_RG16 = ARBTextureRg.GL_RG16;
                GL_RG16I = ARBTextureRg.GL_RG16I;
                GL_RG16UI = ARBTextureRg.GL_RG16UI;
                GL_RG16F = ARBTextureRg.GL_RG16F;
                GL_RG32I = ARBTextureRg.GL_RG32I;
                GL_RG32UI = ARBTextureRg.GL_RG32UI;
                GL_RG32F = ARBTextureRg.GL_RG32F;
                GL_RGB8I = EXTTextureInteger.GL_RGB8I_EXT;
                GL_RGB8UI = EXTTextureInteger.GL_RGB8UI_EXT;
                GL_RGB16I = EXTTextureInteger.GL_RGB16I_EXT;
                GL_RGB16UI = EXTTextureInteger.GL_RGB16UI_EXT;
                GL_RGB32I = EXTTextureInteger.GL_RGB32I_EXT;
                GL_RGB32UI = EXTTextureInteger.GL_RGB32UI_EXT;
                GL_RGBA8I = EXTTextureInteger.GL_RGBA8I_EXT;
                GL_RGBA8UI = EXTTextureInteger.GL_RGBA8UI_EXT;
                GL_RGBA16I = EXTTextureInteger.GL_RGBA16I_EXT;
                GL_RGBA16UI = EXTTextureInteger.GL_RGBA16UI_EXT;
                GL_RGBA32I = EXTTextureInteger.GL_RGBA32I_EXT;
                GL_RGBA32UI = EXTTextureInteger.GL_RGBA32UI_EXT;
                GL_ALPHA8I = EXTTextureInteger.GL_ALPHA8I_EXT;
                GL_ALPHA8UI = EXTTextureInteger.GL_ALPHA8UI_EXT;
                GL_ALPHA16I = EXTTextureInteger.GL_ALPHA16I_EXT;
                GL_ALPHA16UI = EXTTextureInteger.GL_ALPHA16UI_EXT;
                GL_ALPHA32I = EXTTextureInteger.GL_ALPHA32I_EXT;
                GL_ALPHA32UI = EXTTextureInteger.GL_ALPHA32UI_EXT;
                GL_RED_INTEGER = EXTTextureInteger.GL_RED_INTEGER_EXT;
                GL_GREEN_INTEGER = EXTTextureInteger.GL_GREEN_INTEGER_EXT;
                GL_BLUE_INTEGER = EXTTextureInteger.GL_BLUE_INTEGER_EXT;
                GL_ALPHA_INTEGER = EXTTextureInteger.GL_ALPHA_INTEGER_EXT;
                GL_RG = ARBTextureRg.GL_RG;
                GL_RG_INTEGER = ARBTextureRg.GL_RG_INTEGER;
                GL_RGB_INTEGER = EXTTextureInteger.GL_RGB_INTEGER_EXT;
                GL_RGBA_INTEGER = EXTTextureInteger.GL_RGBA_INTEGER_EXT;
                GL_BGR_INTEGER = EXTTextureInteger.GL_BGR_INTEGER_EXT;
                GL_BGRA_INTEGER = EXTTextureInteger.GL_BGRA_INTEGER_EXT;
            }

            VALID_TexFloat = cap.OpenGL30 || cap.GL_ARB_texture_float || cap.GL_ATI_texture_float;
            if (cap.OpenGL30) {
                GL_RGB16F = GL30.GL_RGB16F;
                GL_RGB32F = GL30.GL_RGB32F;
                GL_RGBA16F = GL30.GL_RGBA16F;
                GL_RGBA32F = GL30.GL_RGBA32F;
                GL_ALPHA16F = GL30.GL_ALPHA16F;
                GL_ALPHA32F = GL30.GL_ALPHA32F;
            } else if (cap.GL_ARB_texture_float) {
                GL_RGB16F = ARBTextureFloat.GL_RGB16F_ARB;
                GL_RGB32F = ARBTextureFloat.GL_RGB32F_ARB;
                GL_RGBA16F = ARBTextureFloat.GL_RGBA16F_ARB;
                GL_RGBA32F = ARBTextureFloat.GL_RGBA32F_ARB;
                GL_ALPHA16F = ARBTextureFloat.GL_ALPHA16F_ARB;
                GL_ALPHA32F = ARBTextureFloat.GL_ALPHA32F_ARB;
            } else if (cap.GL_ATI_texture_float) {
                GL_RGB16F = ATITextureFloat.GL_RGB_FLOAT16_ATI;
                GL_RGB32F = ATITextureFloat.GL_RGB_FLOAT32_ATI;
                GL_RGBA16F = ATITextureFloat.GL_RGBA_FLOAT16_ATI;
                GL_RGBA32F = ATITextureFloat.GL_RGBA_FLOAT32_ATI;
                GL_ALPHA16F = ATITextureFloat.GL_ALPHA_FLOAT16_ATI;
                GL_ALPHA32F = ATITextureFloat.GL_ALPHA_FLOAT32_ATI;
            }

            VALID_R11F_G11F_B10F = cap.OpenGL30 || cap.GL_EXT_packed_float;
            if (cap.OpenGL30) {
                GL_R11F_G11F_B10F = GL30.GL_R11F_G11F_B10F;
                GL_UNSIGNED_INT_10F_11F_11F_REV = GL30.GL_UNSIGNED_INT_10F_11F_11F_REV;
            } else if (cap.GL_EXT_packed_float) {
                GL_R11F_G11F_B10F = EXTPackedFloat.GL_R11F_G11F_B10F_EXT;
                GL_UNSIGNED_INT_10F_11F_11F_REV = EXTPackedFloat.GL_UNSIGNED_INT_10F_11F_11F_REV_EXT;
            }

            VALID_RGB9_E5 = cap.OpenGL30 || cap.GL_EXT_texture_shared_exponent;
            if (cap.OpenGL30) {
                GL_RGB9_E5 = GL30.GL_RGB9_E5;
                GL_UNSIGNED_INT_5_9_9_9_REV = GL30.GL_UNSIGNED_INT_5_9_9_9_REV;
            } else if (cap.GL_EXT_texture_shared_exponent) {
                GL_RGB9_E5 = EXTTextureSharedExponent.GL_RGB9_E5_EXT;
                GL_UNSIGNED_INT_5_9_9_9_REV = EXTTextureSharedExponent.GL_UNSIGNED_INT_5_9_9_9_REV_EXT;
            }

            VALID_TexRectangle = cap.OpenGL31 || cap.GL_ARB_texture_rectangle || cap.GL_EXT_texture_rectangle || cap.GL_NV_texture_rectangle;
            if (cap.OpenGL31) {
                GL_TEXTURE_RECTANGLE = GL31.GL_TEXTURE_RECTANGLE;
                GL_PROXY_TEXTURE_RECTANGLE = GL31.GL_PROXY_TEXTURE_RECTANGLE;
                GL_TEXTURE_BINDING_RECTANGLE = GL31.GL_TEXTURE_BINDING_RECTANGLE;
                GL_MAX_RECTANGLE_TEXTURE_SIZE = GL31.GL_MAX_RECTANGLE_TEXTURE_SIZE;
            } else if (cap.GL_ARB_texture_rectangle) {
                GL_TEXTURE_RECTANGLE = ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
                GL_PROXY_TEXTURE_RECTANGLE = ARBTextureRectangle.GL_PROXY_TEXTURE_RECTANGLE_ARB;
                GL_TEXTURE_BINDING_RECTANGLE = ARBTextureRectangle.GL_TEXTURE_BINDING_RECTANGLE_ARB;
                GL_MAX_RECTANGLE_TEXTURE_SIZE = ARBTextureRectangle.GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB;
            } else if (cap.GL_EXT_texture_rectangle) {
                GL_TEXTURE_RECTANGLE = EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT;
                GL_PROXY_TEXTURE_RECTANGLE = EXTTextureRectangle.GL_PROXY_TEXTURE_RECTANGLE_EXT;
                GL_TEXTURE_BINDING_RECTANGLE = EXTTextureRectangle.GL_TEXTURE_BINDING_RECTANGLE_EXT;
                GL_MAX_RECTANGLE_TEXTURE_SIZE = EXTTextureRectangle.GL_MAX_RECTANGLE_TEXTURE_SIZE_EXT;
            } else if (cap.GL_NV_texture_rectangle) {
                GL_TEXTURE_RECTANGLE = NVTextureRectangle.GL_TEXTURE_RECTANGLE_NV;
                GL_PROXY_TEXTURE_RECTANGLE = NVTextureRectangle.GL_PROXY_TEXTURE_RECTANGLE_NV;
                GL_TEXTURE_BINDING_RECTANGLE = NVTextureRectangle.GL_TEXTURE_BINDING_RECTANGLE_NV;
                GL_MAX_RECTANGLE_TEXTURE_SIZE = NVTextureRectangle.GL_MAX_RECTANGLE_TEXTURE_SIZE_NV;
            }

            VALID_TexSnorm = cap.OpenGL31 || cap.GL_EXT_texture_snorm;
            if (cap.OpenGL31) {
                GL_R8_SNORM = GL31.GL_R8_SNORM;
                GL_RG8_SNORM = GL31.GL_RG8_SNORM;
                GL_RGB8_SNORM = GL31.GL_RGB8_SNORM;
                GL_RGBA8_SNORM = GL31.GL_RGBA8_SNORM;
                GL_R16_SNORM = GL31.GL_R16_SNORM;
                GL_RG16_SNORM = GL31.GL_RG16_SNORM;
                GL_RGB16_SNORM = GL31.GL_RGB16_SNORM;
                GL_RGBA16_SNORM = GL31.GL_RGBA16_SNORM;
                GL_RED_SNORM = GL31.GL_RED_SNORM;
                GL_RG_SNORM = GL31.GL_RG_SNORM;
                GL_RGB_SNORM = GL31.GL_RGB_SNORM;
                GL_RGBA_SNORM = GL31.GL_RGBA_SNORM;
            } else if (cap.GL_EXT_texture_snorm) {
                GL_R8_SNORM = EXTTextureSnorm.GL_R8_SNORM;
                GL_RG8_SNORM = EXTTextureSnorm.GL_RG8_SNORM;
                GL_RGB8_SNORM = EXTTextureSnorm.GL_RGB8_SNORM;
                GL_RGBA8_SNORM = EXTTextureSnorm.GL_RGBA8_SNORM;
                GL_R16_SNORM = EXTTextureSnorm.GL_R16_SNORM;
                GL_RG16_SNORM = EXTTextureSnorm.GL_RG16_SNORM;
                GL_RGB16_SNORM = EXTTextureSnorm.GL_RGB16_SNORM;
                GL_RGBA16_SNORM = EXTTextureSnorm.GL_RGBA16_SNORM;
                GL_RED_SNORM = EXTTextureSnorm.GL_RED_SNORM;
                GL_RG_SNORM = EXTTextureSnorm.GL_RG_SNORM;
                GL_RGB_SNORM = EXTTextureSnorm.GL_RGB_SNORM;
                GL_RGBA_SNORM = EXTTextureSnorm.GL_RGBA_SNORM;
            }

            VALID_SeamlessCubeMap = cap.OpenGL32 || cap.GL_ARB_seamless_cube_map;
            if (cap.OpenGL32) {
                GL_TEXTURE_CUBE_MAP_SEAMLESS = GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;
            } else if (cap.GL_ARB_seamless_cube_map) {
                GL_TEXTURE_CUBE_MAP_SEAMLESS = ARBSeamlessCubeMap.GL_TEXTURE_CUBE_MAP_SEAMLESS;
            }

            VALID_RGB10_A2UI = cap.OpenGL33 || cap.GL_ARB_texture_rgb10_a2ui;
            if (cap.OpenGL33) {
                GL_RGB10_A2UI = GL33.GL_RGB10_A2UI;
            } else if (cap.GL_ARB_texture_rgb10_a2ui) {
                GL_RGB10_A2UI = ARBTextureRGB10_A2UI.GL_RGB10_A2UI;
            }

            VALID_CubeMapArray = cap.OpenGL40 || cap.GL_ARB_texture_cube_map_array;
            if (cap.OpenGL40) {
                GL_TEXTURE_CUBE_MAP_ARRAY = GL40.GL_TEXTURE_CUBE_MAP_ARRAY;
                GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = GL40.GL_PROXY_TEXTURE_CUBE_MAP_ARRAY;
                GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY;
            } else if (cap.GL_ARB_texture_cube_map_array) {
                GL_TEXTURE_CUBE_MAP_ARRAY = ARBTextureCubeMapArray.GL_TEXTURE_CUBE_MAP_ARRAY_ARB;
                GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = ARBTextureCubeMapArray.GL_PROXY_TEXTURE_CUBE_MAP_ARRAY_ARB;
                GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = ARBTextureCubeMapArray.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY_ARB;
            }

            VALID_RGB565 = cap.OpenGL41 || cap.GL_ARB_ES2_compatibility;
            if (cap.OpenGL41) {
                GL_RGB565 = GL41.GL_RGB565;
            } else if (cap.GL_ARB_texture_rgb10_a2ui) {
                GL_RGB565 = ARBES2Compatibility.GL_RGB565;
            }

            VALID_ImageLoadStore = cap.OpenGL42 || cap.GL_ARB_shader_image_load_store || cap.GL_EXT_shader_image_load_store;
            if (cap.OpenGL42) {
                glBindImageTexture = GL42::glBindImageTexture;
            } else if (cap.GL_ARB_shader_image_load_store) {
                glBindImageTexture = ARBShaderImageLoadStore::glBindImageTexture;
            } else if (cap.GL_EXT_shader_image_load_store) {
                glBindImageTexture = EXTShaderImageLoadStore::glBindImageTextureEXT;
            }

            VALID_TexStorage = cap.OpenGL42 || cap.GL_ARB_texture_storage;
            if (cap.OpenGL42) {
                glTexStorage1D = GL42::glTexStorage1D;
                glTexStorage2D = GL42::glTexStorage2D;
                glTexStorage3D = GL42::glTexStorage3D;
            } else if (cap.GL_ARB_texture_storage) {
                glTexStorage1D = ARBTextureStorage::glTexStorage1D;
                glTexStorage2D = ARBTextureStorage::glTexStorage2D;
                glTexStorage3D = ARBTextureStorage::glTexStorage3D;
            }

            VALID_SeamlessCubeMapPerTex = VALID_SeamlessCubeMap && (cap.OpenGL43 || cap.GL_ARB_seamless_cubemap_per_texture || cap.GL_AMD_seamless_cubemap_per_texture);

            VALID_CopyImage = cap.OpenGL43 || cap.GL_ARB_copy_image || cap.GL_NV_copy_image;
            if (cap.OpenGL43) {
                glCopyImageSubData = GL43::glCopyImageSubData;
            } else if (cap.GL_ARB_copy_image) {
                glCopyImageSubData = ARBCopyImage::glCopyImageSubData;
            } else if (cap.GL_NV_copy_image) {
                glCopyImageSubData = NVCopyImage::glCopyImageSubDataNV;
            }

            VALID_TexView = cap.OpenGL43 || cap.GL_ARB_texture_view;
            if (cap.OpenGL43) {
                GL_TEXTURE_VIEW_MIN_LEVEL = GL43.GL_TEXTURE_VIEW_MIN_LEVEL;
                GL_TEXTURE_VIEW_NUM_LEVELS = GL43.GL_TEXTURE_VIEW_NUM_LEVELS;
                GL_TEXTURE_VIEW_MIN_LAYER = GL43.GL_TEXTURE_VIEW_MIN_LAYER;
                GL_TEXTURE_VIEW_NUM_LAYERS = GL43.GL_TEXTURE_VIEW_NUM_LAYERS;
                GL_TEXTURE_IMMUTABLE_LEVELS = GL43.GL_TEXTURE_IMMUTABLE_LEVELS;

                glTextureView = GL43::glTextureView;
            } else if (cap.GL_ARB_texture_view) {
                GL_TEXTURE_VIEW_MIN_LEVEL = ARBTextureView.GL_TEXTURE_VIEW_MIN_LEVEL;
                GL_TEXTURE_VIEW_NUM_LEVELS = ARBTextureView.GL_TEXTURE_VIEW_NUM_LEVELS;
                GL_TEXTURE_VIEW_MIN_LAYER = ARBTextureView.GL_TEXTURE_VIEW_MIN_LAYER;
                GL_TEXTURE_VIEW_NUM_LAYERS = ARBTextureView.GL_TEXTURE_VIEW_NUM_LAYERS;
                GL_TEXTURE_IMMUTABLE_LEVELS = ARBTextureView.GL_TEXTURE_IMMUTABLE_LEVELS;

                glTextureView = ARBTextureView::glTextureView;
            }

            VALID_Invalidate = cap.OpenGL43 || cap.GL_ARB_invalidate_subdata;
            if (cap.OpenGL43) {
                glInvalidateTexImage = GL43::glInvalidateTexImage;
                glInvalidateTexSubImage = GL43::glInvalidateTexSubImage;
            } else if (cap.GL_ARB_invalidate_subdata) {
                glInvalidateTexImage = ARBInvalidateSubdata::glInvalidateTexImage;
                glInvalidateTexSubImage = ARBInvalidateSubdata::glInvalidateTexSubImage;
            }

            VALID_MultiBind = cap.OpenGL44 || cap.GL_ARB_multi_bind;
            if (cap.OpenGL44) {
                glBindTextures = GL44::glBindTextures;
                glBindImageTextures = GL44::glBindImageTextures;
            } else if (cap.GL_ARB_multi_bind) {
                glBindTextures = ARBMultiBind::glBindTextures;
                glBindImageTextures = ARBMultiBind::glBindImageTextures;
            }

            VALID_ClearTex = cap.OpenGL44 || cap.GL_ARB_clear_texture;
            if (cap.OpenGL44) {
                glClearTexImage_ByteBuffer = GL44::glClearTexImage;
                glClearTexImage_ShortBuffer = GL44::glClearTexImage;
                glClearTexImage_IntBuffer = GL44::glClearTexImage;
                glClearTexImage_LongBuffer = GL44::glClearTexImage;
                glClearTexImage_FloatBuffer = GL44::glClearTexImage;
                glClearTexImage_DoubleBuffer = GL44::glClearTexImage;
                glClearTexSubImage_ByteBuffer = GL44::glClearTexSubImage;
                glClearTexSubImage_ShortBuffer = GL44::glClearTexSubImage;
                glClearTexSubImage_IntBuffer = GL44::glClearTexSubImage;
                glClearTexSubImage_LongBuffer = GL44::glClearTexSubImage;
                glClearTexSubImage_FloatBuffer = GL44::glClearTexSubImage;
                glClearTexSubImage_DoubleBuffer = GL44::glClearTexSubImage;
            } else if (cap.GL_ARB_clear_texture) {
                glClearTexImage_ByteBuffer = ARBClearTexture::glClearTexImage;
                glClearTexImage_ShortBuffer = ARBClearTexture::glClearTexImage;
                glClearTexImage_IntBuffer = ARBClearTexture::glClearTexImage;
                glClearTexImage_LongBuffer = ARBClearTexture::glClearTexImage;
                glClearTexImage_FloatBuffer = ARBClearTexture::glClearTexImage;
                glClearTexImage_DoubleBuffer = ARBClearTexture::glClearTexImage;
                glClearTexSubImage_ByteBuffer = ARBClearTexture::glClearTexSubImage;
                glClearTexSubImage_ShortBuffer = ARBClearTexture::glClearTexSubImage;
                glClearTexSubImage_IntBuffer = ARBClearTexture::glClearTexSubImage;
                glClearTexSubImage_LongBuffer = ARBClearTexture::glClearTexSubImage;
                glClearTexSubImage_FloatBuffer = ARBClearTexture::glClearTexSubImage;
                glClearTexSubImage_DoubleBuffer = ARBClearTexture::glClearTexSubImage;
            }
        }

        public final static class Compressed extends Texture {
            private static boolean VALID;
            private static boolean VALID_SRGB_TC;
            private static boolean VALID_RGTC;
            private static boolean VALID_BPTC;

            public static int GL_COMPRESSED_ALPHA;
            public static int GL_COMPRESSED_LUMINANCE;
            public static int GL_COMPRESSED_LUMINANCE_ALPHA;
            public static int GL_COMPRESSED_INTENSITY;
            public static int GL_COMPRESSED_RGB;
            public static int GL_COMPRESSED_RGBA;
            public static int GL_TEXTURE_COMPRESSION_HINT;
            public static int GL_TEXTURE_COMPRESSED_IMAGE_SIZE;
            public static int GL_TEXTURE_COMPRESSED;
            public static int GL_NUM_COMPRESSED_TEXTURE_FORMATS;
            public static int GL_COMPRESSED_TEXTURE_FORMATS;

            public static int GL_COMPRESSED_SRGB;
            public static int GL_COMPRESSED_SRGB_ALPHA;
            public static int GL_COMPRESSED_SLUMINANCE;
            public static int GL_COMPRESSED_SLUMINANCE_ALPHA;

            public static int GL_COMPRESSED_RED_RGTC1;
            public static int GL_COMPRESSED_SIGNED_RED_RGTC1;
            public static int GL_COMPRESSED_RG_RGTC2;
            public static int GL_COMPRESSED_SIGNED_RG_RGTC2;

            public static int GL_COMPRESSED_RGBA_BPTC_UNORM;
            public static int GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM;
            public static int GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT;
            public static int GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT;

            private static IntIntIntIntIntIntLongFun glCompressedTexImage1D;
            private static IntIntIntIntIntXFun<ByteBuffer> glCompressedTexImage1D_buf;
            private static IntIntIntIntIntIntIntLongFun glCompressedTexImage2D;
            private static IntIntIntIntIntIntXFun<ByteBuffer> glCompressedTexImage2D_buf;
            private static IntIntIntIntIntIntIntIntLongFun glCompressedTexImage3D;
            private static IntIntIntIntIntIntIntXFun<ByteBuffer> glCompressedTexImage3D_buf;
            private static IntIntIntIntIntIntLongFun glCompressedTexSubImage1D;
            private static IntIntIntIntIntXFun<ByteBuffer> glCompressedTexSubImage1D_buf;
            private static IntIntIntIntIntIntIntIntLongFun glCompressedTexSubImage2D;
            private static IntIntIntIntIntIntIntXFun<ByteBuffer> glCompressedTexSubImage2D_buf;
            private static IntIntIntIntIntIntIntIntIntIntLongFun glCompressedTexSubImage3D;
            private static IntIntIntIntIntIntIntIntIntXFun<ByteBuffer> glCompressedTexSubImage3D_buf;
            private static IntIntXFun<ByteBuffer> glGetCompressedTexImage;
            private static IntIntLongFun glGetCompressedTexImage_buf;

            private static void init(ContextCapabilities cap) {
                VALID = cap.OpenGL13 || cap.GL_ARB_texture_compression;
                if (cap.OpenGL13) {
                    GL_COMPRESSED_ALPHA = GL13.GL_COMPRESSED_ALPHA;
                    GL_COMPRESSED_LUMINANCE = GL13.GL_COMPRESSED_LUMINANCE;
                    GL_COMPRESSED_LUMINANCE_ALPHA = GL13.GL_COMPRESSED_LUMINANCE_ALPHA;
                    GL_COMPRESSED_INTENSITY = GL13.GL_COMPRESSED_INTENSITY;
                    GL_COMPRESSED_RGB = GL13.GL_COMPRESSED_RGB;
                    GL_COMPRESSED_RGBA = GL13.GL_COMPRESSED_RGBA;
                    GL_TEXTURE_COMPRESSION_HINT = GL13.GL_TEXTURE_COMPRESSION_HINT;
                    GL_TEXTURE_COMPRESSED_IMAGE_SIZE = GL13.GL_TEXTURE_COMPRESSED_IMAGE_SIZE;
                    GL_TEXTURE_COMPRESSED = GL13.GL_TEXTURE_COMPRESSED;
                    GL_NUM_COMPRESSED_TEXTURE_FORMATS = GL13.GL_NUM_COMPRESSED_TEXTURE_FORMATS;
                    GL_COMPRESSED_TEXTURE_FORMATS = GL13.GL_COMPRESSED_TEXTURE_FORMATS;

                    glCompressedTexImage1D = GL13::glCompressedTexImage1D;
                    glCompressedTexImage1D_buf = GL13::glCompressedTexImage1D;
                    glCompressedTexImage2D = GL13::glCompressedTexImage2D;
                    glCompressedTexImage2D_buf = GL13::glCompressedTexImage2D;
                    glCompressedTexImage3D = GL13::glCompressedTexImage3D;
                    glCompressedTexImage3D_buf = GL13::glCompressedTexImage3D;
                    glCompressedTexSubImage1D = GL13::glCompressedTexSubImage1D;
                    glCompressedTexSubImage1D_buf = GL13::glCompressedTexSubImage1D;
                    glCompressedTexSubImage2D = GL13::glCompressedTexSubImage2D;
                    glCompressedTexSubImage2D_buf = GL13::glCompressedTexSubImage2D;
                    glCompressedTexSubImage3D = GL13::glCompressedTexSubImage3D;
                    glCompressedTexSubImage3D_buf = GL13::glCompressedTexSubImage3D;
                    glGetCompressedTexImage = GL13::glGetCompressedTexImage;
                    glGetCompressedTexImage_buf = GL13::glGetCompressedTexImage;
                } else if (cap.GL_ARB_texture_compression) {
                    GL_COMPRESSED_ALPHA = ARBTextureCompression.GL_COMPRESSED_ALPHA_ARB;
                    GL_COMPRESSED_LUMINANCE = ARBTextureCompression.GL_COMPRESSED_LUMINANCE_ARB;
                    GL_COMPRESSED_LUMINANCE_ALPHA = ARBTextureCompression.GL_COMPRESSED_LUMINANCE_ALPHA_ARB;
                    GL_COMPRESSED_INTENSITY = ARBTextureCompression.GL_COMPRESSED_INTENSITY_ARB;
                    GL_COMPRESSED_RGB = ARBTextureCompression.GL_COMPRESSED_RGB_ARB;
                    GL_COMPRESSED_RGBA = ARBTextureCompression.GL_COMPRESSED_RGBA_ARB;
                    GL_TEXTURE_COMPRESSION_HINT = ARBTextureCompression.GL_TEXTURE_COMPRESSION_HINT_ARB;
                    GL_TEXTURE_COMPRESSED_IMAGE_SIZE = ARBTextureCompression.GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB;
                    GL_TEXTURE_COMPRESSED = ARBTextureCompression.GL_TEXTURE_COMPRESSED_ARB;
                    GL_NUM_COMPRESSED_TEXTURE_FORMATS = ARBTextureCompression.GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB;
                    GL_COMPRESSED_TEXTURE_FORMATS = ARBTextureCompression.GL_COMPRESSED_TEXTURE_FORMATS_ARB;

                    glCompressedTexImage1D = ARBTextureCompression::glCompressedTexImage1DARB;
                    glCompressedTexImage1D_buf = ARBTextureCompression::glCompressedTexImage1DARB;
                    glCompressedTexImage2D = ARBTextureCompression::glCompressedTexImage2DARB;
                    glCompressedTexImage2D_buf = ARBTextureCompression::glCompressedTexImage2DARB;
                    glCompressedTexImage3D = ARBTextureCompression::glCompressedTexImage3DARB;
                    glCompressedTexImage3D_buf = ARBTextureCompression::glCompressedTexImage3DARB;
                    glCompressedTexSubImage1D = ARBTextureCompression::glCompressedTexSubImage1DARB;
                    glCompressedTexSubImage1D_buf = ARBTextureCompression::glCompressedTexSubImage1DARB;
                    glCompressedTexSubImage2D = ARBTextureCompression::glCompressedTexSubImage2DARB;
                    glCompressedTexSubImage2D_buf = ARBTextureCompression::glCompressedTexSubImage2DARB;
                    glCompressedTexSubImage3D = ARBTextureCompression::glCompressedTexSubImage3DARB;
                    glCompressedTexSubImage3D_buf = ARBTextureCompression::glCompressedTexSubImage3DARB;
                    glGetCompressedTexImage = ARBTextureCompression::glGetCompressedTexImageARB;
                    glGetCompressedTexImage_buf = ARBTextureCompression::glGetCompressedTexImageARB;
                }

                VALID_SRGB_TC = cap.OpenGL21 || cap.GL_EXT_texture_sRGB;
                if (cap.OpenGL21) {
                    GL_COMPRESSED_SRGB = GL21.GL_COMPRESSED_SRGB;
                    GL_COMPRESSED_SRGB_ALPHA = GL21.GL_COMPRESSED_SRGB_ALPHA;
                    GL_COMPRESSED_SLUMINANCE = GL21.GL_COMPRESSED_SLUMINANCE;
                    GL_COMPRESSED_SLUMINANCE_ALPHA = GL21.GL_COMPRESSED_SLUMINANCE_ALPHA;
                } else if (cap.GL_EXT_texture_sRGB) {
                    GL_COMPRESSED_SRGB = EXTTextureSRGB.GL_COMPRESSED_SRGB_EXT;
                    GL_COMPRESSED_SRGB_ALPHA = EXTTextureSRGB.GL_COMPRESSED_SRGB_ALPHA_EXT;
                    GL_COMPRESSED_SLUMINANCE = EXTTextureSRGB.GL_COMPRESSED_SLUMINANCE_EXT;
                    GL_COMPRESSED_SLUMINANCE_ALPHA = EXTTextureSRGB.GL_COMPRESSED_SLUMINANCE_ALPHA_EXT;
                }

                VALID_RGTC = cap.OpenGL30 || cap.GL_ARB_texture_compression_rgtc || cap.GL_EXT_texture_compression_rgtc;
                if (cap.OpenGL30) {
                    GL_COMPRESSED_RED_RGTC1 = GL30.GL_COMPRESSED_RED_RGTC1;
                    GL_COMPRESSED_SIGNED_RED_RGTC1 = GL30.GL_COMPRESSED_SIGNED_RED_RGTC1;
                    GL_COMPRESSED_RG_RGTC2 = GL30.GL_COMPRESSED_RG_RGTC2;
                    GL_COMPRESSED_SIGNED_RG_RGTC2 = GL30.GL_COMPRESSED_SIGNED_RG_RGTC2;
                } else if (cap.GL_ARB_texture_compression_rgtc) {
                    GL_COMPRESSED_RED_RGTC1 = ARBTextureCompressionRGTC.GL_COMPRESSED_RED_RGTC1;
                    GL_COMPRESSED_SIGNED_RED_RGTC1 = ARBTextureCompressionRGTC.GL_COMPRESSED_SIGNED_RED_RGTC1;
                    GL_COMPRESSED_RG_RGTC2 = ARBTextureCompressionRGTC.GL_COMPRESSED_RG_RGTC2;
                    GL_COMPRESSED_SIGNED_RG_RGTC2 = ARBTextureCompressionRGTC.GL_COMPRESSED_SIGNED_RG_RGTC2;
                } else if (cap.GL_EXT_texture_compression_rgtc) {
                    GL_COMPRESSED_RED_RGTC1 = EXTTextureCompressionRGTC.GL_COMPRESSED_RED_RGTC1_EXT;
                    GL_COMPRESSED_SIGNED_RED_RGTC1 = EXTTextureCompressionRGTC.GL_COMPRESSED_SIGNED_RED_RGTC1_EXT;
                    GL_COMPRESSED_RG_RGTC2 = EXTTextureCompressionRGTC.GL_COMPRESSED_RED_GREEN_RGTC2_EXT;
                    GL_COMPRESSED_SIGNED_RG_RGTC2 = EXTTextureCompressionRGTC.GL_COMPRESSED_SIGNED_RED_GREEN_RGTC2_EXT;
                }

                VALID_BPTC = cap.OpenGL42 || cap.GL_ARB_texture_compression_bptc;
                if (cap.OpenGL42) {
                    GL_COMPRESSED_RGBA_BPTC_UNORM = GL42.GL_COMPRESSED_RGBA_BPTC_UNORM;
                    GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM;
                    GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT;
                    GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = GL42.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT;
                } else if (cap.GL_ARB_texture_compression_bptc) {
                    GL_COMPRESSED_RGBA_BPTC_UNORM = ARBTextureCompressionBPTC.GL_COMPRESSED_RGBA_BPTC_UNORM_ARB;
                    GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = ARBTextureCompressionBPTC.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM_ARB;
                    GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = ARBTextureCompressionBPTC.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT_ARB;
                    GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = ARBTextureCompressionBPTC.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT_ARB;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static boolean valid_SRGB_TC() {
                return VALID_SRGB_TC;
            }

            public static boolean valid_RGTC() {
                return VALID_RGTC;
            }

            /**
             * BC6H/BC7
             */
            public static boolean valid_BPTC() {
                return VALID_BPTC;
            }

            public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, int data_imageSize, long data_buffer_offset) {
                glCompressedTexImage1D.run(target, level, internalformat, width, border, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, ByteBuffer data) {
                glCompressedTexImage1D_buf.run(target, level, internalformat, width, border, data);
            }

            public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int data_imageSize, long data_buffer_offset) {
                glCompressedTexImage2D.run(target, level, internalformat, width, height, border, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
                glCompressedTexImage2D_buf.run(target, level, internalformat, width, height, border, data);
            }

            public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int data_imageSize, long data_buffer_offset) {
                glCompressedTexImage3D.run(target, level, internalformat, width, height, depth, border, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data) {
                glCompressedTexImage3D_buf.run(target, level, internalformat, width, height, depth, border, data);
            }

            public static void glCompressedTexSubImage1D(int target, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset ) {
                glCompressedTexSubImage1D.run(target, level, xoffset, width, format, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexSubImage1D(int target, int level, int xoffset, int width, int format, ByteBuffer data) {
                glCompressedTexSubImage1D_buf.run(target, level, xoffset, width, format, data);
            }

            public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset) {
                glCompressedTexSubImage2D.run(target, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
                glCompressedTexSubImage2D_buf.run(target, level, xoffset, yoffset, width, height, format, data);
            }

            public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int data_imageSize, long data_buffer_offset) {
                glCompressedTexSubImage3D.run(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data_imageSize, data_buffer_offset);
            }

            public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
                glCompressedTexSubImage3D_buf.run(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data);
            }

            public static void glGetCompressedTexImage(int target, int lod, ByteBuffer img) {
                glGetCompressedTexImage.run(target, lod, img);
            }

            public static void glGetCompressedTexImage(int target, int lod, long img_buffer_offset) {
                glGetCompressedTexImage_buf.run(target, lod, img_buffer_offset);
            }

            private Compressed() {}
        }

        /**
         * <b>NOTE:</b> Not a core feature of any OpenGL version.
         */
        public final static class Bindless extends Texture {
            private static boolean VALID;

            private static IntToLongFunction glGetTextureHandle;
            private static IntIntFunLong glGetTextureSamplerHandle;
            private static LongConsumer glMakeTextureHandleResident;
            private static LongConsumer glMakeTextureHandleNonResident;
            private static IntIntBoolIntIntFunLong glGetImageHandle;
            private static LongIntFun glMakeImageHandleResident;
            private static LongConsumer glMakeImageHandleNonResident;
            private static IntLongFun glUniformHandleui64;
            private static IntXFun<LongBuffer> glUniformHandleu;
            private static IntIntLongFun glProgramUniformHandleui64;
            private static IntIntXFun<LongBuffer> glProgramUniformHandleu;
            private static LongPredicate glIsTextureHandleResident;
            private static LongPredicate glIsImageHandleResident;

            private static void init(ContextCapabilities cap) {
                VALID = cap.GL_ARB_bindless_texture || cap.GL_NV_bindless_texture;
                if (cap.GL_ARB_bindless_texture) {
                    glGetTextureHandle = ARBBindlessTexture::glGetTextureHandleARB;
                    glGetTextureSamplerHandle = ARBBindlessTexture::glGetTextureSamplerHandleARB;
                    glMakeTextureHandleResident = ARBBindlessTexture::glMakeTextureHandleResidentARB;
                    glMakeTextureHandleNonResident = ARBBindlessTexture::glMakeTextureHandleNonResidentARB;
                    glGetImageHandle = ARBBindlessTexture::glGetImageHandleARB;
                    glMakeImageHandleResident = ARBBindlessTexture::glMakeImageHandleResidentARB;
                    glMakeImageHandleNonResident = ARBBindlessTexture::glMakeImageHandleNonResidentARB;
                    glUniformHandleui64 = ARBBindlessTexture::glUniformHandleui64ARB;
                    glUniformHandleu = ARBBindlessTexture::glUniformHandleuARB;
                    glProgramUniformHandleui64 = ARBBindlessTexture::glProgramUniformHandleui64ARB;
                    glProgramUniformHandleu = ARBBindlessTexture::glProgramUniformHandleuARB;
                    glIsTextureHandleResident = ARBBindlessTexture::glIsTextureHandleResidentARB;
                    glIsImageHandleResident = ARBBindlessTexture::glIsImageHandleResidentARB;
                } else if (cap.GL_NV_bindless_texture) {
                    glGetTextureHandle = NVBindlessTexture::glGetTextureHandleNV;
                    glGetTextureSamplerHandle = NVBindlessTexture::glGetTextureSamplerHandleNV;
                    glMakeTextureHandleResident = NVBindlessTexture::glMakeTextureHandleResidentNV;
                    glMakeTextureHandleNonResident = NVBindlessTexture::glMakeTextureHandleNonResidentNV;
                    glGetImageHandle = NVBindlessTexture::glGetImageHandleNV;
                    glMakeImageHandleResident = NVBindlessTexture::glMakeImageHandleResidentNV;
                    glMakeImageHandleNonResident = NVBindlessTexture::glMakeImageHandleNonResidentNV;
                    glUniformHandleui64 = NVBindlessTexture::glUniformHandleui64NV;
                    glUniformHandleu = NVBindlessTexture::glUniformHandleuNV;
                    glProgramUniformHandleui64 = NVBindlessTexture::glProgramUniformHandleui64NV;
                    glProgramUniformHandleu = NVBindlessTexture::glProgramUniformHandleuNV;
                    glIsTextureHandleResident = NVBindlessTexture::glIsTextureHandleResidentNV;
                    glIsImageHandleResident = NVBindlessTexture::glIsImageHandleResidentNV;
                }
            }

            public static boolean valid() {
                return VALID;
            }

            public static long glGetTextureHandle(int texture) {
                return glGetTextureHandle.applyAsLong(texture);
            }

            public static long glGetTextureSamplerHandle(int texture, int sampler) {
                return glGetTextureSamplerHandle.run(texture, sampler);
            }

            public static void glMakeTextureHandleResident(long handle) {
                glMakeTextureHandleResident.accept(handle);
            }

            public static void glMakeTextureHandleNonResident(long handle) {
                glMakeTextureHandleNonResident.accept(handle);
            }

            public static long glGetImageHandle(int texture, int level, boolean layered, int layer, int format) {
                return glGetImageHandle.run(texture, level, layered, layer, format);
            }

            public static void glMakeImageHandleResident(long handle, int access) {
                glMakeImageHandleResident.run(handle, access);
            }

            public static void glMakeImageHandleNonResident(long handle) {
                glMakeImageHandleNonResident.accept(handle);
            }

            public static void glUniformHandleui64(int location, long value) {
                glUniformHandleui64.run(location, value);
            }

            public static void glUniformHandleu(int location, LongBuffer value) {
                glUniformHandleu.run(location, value);
            }

            public static void glProgramUniformHandleui64(int program, int location, long value) {
                glProgramUniformHandleui64.run(program, location, value);
            }

            public static void glProgramUniformHandleu(int program, int location, LongBuffer values) {
                glProgramUniformHandleu.run(program, location, values);
            }

            public static boolean glIsTextureHandleResident(long handle) {
                return glIsTextureHandleResident.test(handle);
            }

            public static boolean glIsImageHandleResident(long handle) {
                return glIsImageHandleResident.test(handle);
            }
        }

        public static boolean valid_CubeMap() {
            return VALID_CubeMap;
        }

        public static boolean valid_BorderClamp() {
            return VALID_BorderClamp;
        }

        public static boolean valid_MirroredRepeat() {
            return VALID_MirroredRepeat;
        }

        public static boolean valid_NPOT() {
            return VALID_NPOT;
        }

        public static boolean valid_SRGB() {
            return VALID_SRGB;
        }

        public static boolean valid_TexArray() {
            return VALID_TexArray;
        }

        /**
         * Also included <code>R16F</code>, <code>R326F</code>, <code>RG16F</code>, <code>RG32F</code>
         */
        public static boolean valid_TexInt() {
            return VALID_TexInt;
        }

        public static boolean valid_TexFloat() {
            return VALID_TexFloat;
        }

        public static boolean valid_R11F_G11F_B10F() {
            return VALID_R11F_G11F_B10F;
        }

        public static boolean valid_RGB9_E5() {
            return VALID_RGB9_E5;
        }

        public static boolean valid_TexRectangle() {
            return VALID_TexRectangle;
        }

        public static boolean valid_SeamlessCubeMap() {
            return VALID_SeamlessCubeMap;
        }

        public static boolean valid_TexSnorm() {
            return VALID_TexSnorm;
        }

        public static boolean valid_RGB10_A2UI() {
            return VALID_RGB10_A2UI;
        }

        public static boolean valid_CubeMapArray() {
            return VALID_CubeMapArray;
        }

        public static boolean valid_RGB565() {
            return VALID_RGB565;
        }

        /**
         * {@link GL42#glBindImageTexture(int, int, int, boolean, int, int, int)}
         */
        public static boolean valid_ImageLoadStore() {
            return VALID_ImageLoadStore;
        }

        /**
         * {@link GL42#glTexStorage1D(int, int, int, int)}<p>
         * {@link GL42#glTexStorage2D(int, int, int, int, int)}<p>
         * {@link GL42#glTexStorage3D(int, int, int, int, int, int)}
         */
        public static boolean valid_TexStorage() {
            return VALID_TexStorage;
        }

        public static boolean valid_SeamlessCubeMapPerTex() {
            return VALID_SeamlessCubeMapPerTex;
        }

        /**
         * {@link GL43#glCopyImageSubData(int, int, int, int, int, int, int, int, int, int, int, int, int, int, int)}
         */
        public static boolean valid_CopyImage() {
            return VALID_CopyImage;
        }

        /**
         * {@link GL43#glTextureView(int, int, int, int, int, int, int, int)}
         */
        public static boolean valid_TexView() {
            return VALID_TexView;
        }

        /**
         * {@link GL43#glInvalidateTexImage(int, int)}<p>
         * {@link GL43#glInvalidateTexSubImage(int, int, int, int, int, int, int, int)}
         */
        public static boolean valid_Invalidate() {
            return VALID_Invalidate;
        }

        /**
         * {@link GL44#glClearTexImage(int, int, int, int, ByteBuffer)}<p>
         * {@link GL44#glClearTexImage(int, int, int, int, ShortBuffer)}<p>
         * {@link GL44#glClearTexImage(int, int, int, int, IntBuffer)}<p>
         * {@link GL44#glClearTexImage(int, int, int, int, LongBuffer)}<p>
         * {@link GL44#glClearTexImage(int, int, int, int, FloatBuffer)}<p>
         * {@link GL44#glClearTexImage(int, int, int, int, DoubleBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, ByteBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, ShortBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, IntBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, LongBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, FloatBuffer)}<p>
         * {@link GL44#glClearTexSubImage(int, int, int, int, int, int, int, int, int, int, DoubleBuffer)}
         */
        public static boolean valid_ClearTex() {
            return VALID_ClearTex;
        }

        public static boolean valid_MultiBind() {
            return VALID_MultiBind;
        }

        public static void glPrioritizeTextures(IntBuffer textures, FloatBuffer priorities) {
            GL11.glPrioritizeTextures(textures, priorities);
        }

        public static boolean glAreTexturesResident(IntBuffer textures, ByteBuffer residences) {
            return GL11.glAreTexturesResident(textures, residences);
        }

        public static void glCopyTexImage1D(int target, int level, int internalFormat, int x, int y, int width, int border) {
            GL11.glCopyTexImage1D(target, level, internalFormat, x, y, width, border);
        }

        public static void glCopyTexImage2D(int target, int level, int internalFormat, int x, int y, int width, int height, int border) {
            GL11.glCopyTexImage2D(target, level, internalFormat, x, y, width, height, border);
        }

        public static void glCopyTexSubImage1D(int target, int level, int xoffset, int x, int y, int width) {
            GL11.glCopyTexSubImage1D(target, level, xoffset, x, y, width);
        }

        public static void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
            GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
        }

        public static void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
            GL12.glCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
        }

        public static int glGenTextures() {
            return GL11.glGenTextures();
        }

        public static void glGenTextures(IntBuffer textures) {
            GL11.glGenTextures(textures);
        }

        public static void glDeleteTextures(int texture) {
            GL11.glDeleteTextures(texture);
        }

        public static void glDeleteTextures(IntBuffer textures) {
            GL11.glDeleteTextures(textures);
        }

        public static void glBindTexture(int target, int texture) {
            GL11.glBindTexture(target, texture);
        }

        public static float glGetTexParameterf(int target, int pname) {
            return GL11.glGetTexParameterf(target, pname);
        }

        public static void glGetTexParameter(int target, int pname, FloatBuffer params) {
            GL11.glGetTexParameter(target, pname, params);
        }

        public static int glGetTexParameteri(int target, int pname) {
            return GL11.glGetTexParameteri(target, pname);
        }

        public static void glGetTexParameter(int target, int pname, IntBuffer params) {
            GL11.glGetTexParameter(target, pname, params);
        }

        public static float glGetTexLevelParameterf(int target, int level, int pname) {
            return GL11.glGetTexLevelParameterf(target, level, pname);
        }

        public static void glGetTexLevelParameter(int target, int level, int pname, FloatBuffer params) {
            GL11.glGetTexLevelParameter(target, level, pname, params);
        }

        public static int glGetTexLevelParameteri(int target, int level, int pname) {
            return GL11.glGetTexLevelParameteri(target, level, pname);
        }

        public static void glGetTexLevelParameter(int target, int level, int pname, IntBuffer params) {
            GL11.glGetTexLevelParameter(target, level, pname, params);
        }

        public static void glGetTexImage(int target, int level, int format, int type, ByteBuffer pixels) {
            GL11.glGetTexImage(target, level, format, type, pixels);
        }

        public static void glGetTexImage(int target, int level, int format, int type, ShortBuffer pixels) {
            GL11.glGetTexImage(target, level, format, type, pixels);
        }

        public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
            GL11.glGetTexImage(target, level, format, type, pixels);
        }

        public static void glGetTexImage(int target, int level, int format, int type, FloatBuffer pixels) {
            GL11.glGetTexImage(target, level, format, type, pixels);
        }

        public static void glGetTexImage(int target, int level, int format, int type, DoubleBuffer pixels) {
            GL11.glGetTexImage(target, level, format, type, pixels);
        }

        public static void glGetTexImage(int target, int level, int format, int type, long pixels_buffer_offset) {
            GL11.glGetTexImage(target, level, format, type, pixels_buffer_offset);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ByteBuffer pixels) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ShortBuffer pixels) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, IntBuffer pixels) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, FloatBuffer pixels) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, DoubleBuffer pixels) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels);
        }

        public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset) {
            GL11.glTexImage1D(target, level, internalformat, width, border, format, type, pixels_buffer_offset);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset) {
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels_buffer_offset);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, DoubleBuffer pixels) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
        }

        public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset) {
            GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels_buffer_offset);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
        }

        public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
            GL11.glTexSubImage1D(target, level, xoffset, width, format, type, pixels_buffer_offset);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
            GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
        }

        public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
            GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset);
        }

        public static void glTexParameterf(int target, int pname, float param) {
            GL11.glTexParameterf(target, pname, param);
        }

        public static void glTexParameter(int target, int pname, FloatBuffer param) {
            GL11.glTexParameter(target, pname, param);
        }

        public static void glTexParameteri(int target, int pname, int param) {
            GL11.glTexParameteri(target, pname, param);
        }

        public static void glTexParameter(int target, int pname, IntBuffer param) {
            GL11.glTexParameter(target, pname, param);
        }

        public static void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
            glBindImageTexture.run(unit, texture, level, layered, layer, access, format);
        }

        public static void glTexStorage1D(int target, int levels, int internalformat, int width) {
            glTexStorage1D.run(target, levels, internalformat, width);
        }

        public static void glTexStorage2D(int target, int levels, int internalformat, int width, int height) {
            glTexStorage2D.run(target, levels, internalformat, width, height);
        }

        public static void glTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth) {
            glTexStorage3D.run(target, levels, internalformat, width, height, depth);
        }

        public static void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth) {
            glCopyImageSubData.run(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth);
        }

        public static void glTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers) {
            glTextureView.run(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers);
        }

        public static void glInvalidateTexImage(int texture, int level) {
            glInvalidateTexImage.run(texture, level);
        }

        public static void glInvalidateTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth) {
            glInvalidateTexSubImage.run(texture, level, xoffset, yoffset, zoffset, width, height, depth);
        }

        public static void glBindTextures(int first, int count, IntBuffer textures) {
            glBindTextures.run(first, count, textures);
        }

        public static void glBindImageTextures(int first, int count, IntBuffer textures) {
            glBindImageTextures.run(first, count, textures);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
            glClearTexImage_ByteBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, ShortBuffer data) {
            glClearTexImage_ShortBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, IntBuffer data) {
            glClearTexImage_IntBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
            glClearTexImage_LongBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, FloatBuffer data) {
            glClearTexImage_FloatBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexImage(int texture, int level, int format, int type, DoubleBuffer data) {
            glClearTexImage_DoubleBuffer.run(texture, level, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
            glClearTexSubImage_ByteBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer data) {
            glClearTexSubImage_ShortBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer data) {
            glClearTexSubImage_IntBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
            glClearTexSubImage_LongBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer data) {
            glClearTexSubImage_FloatBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer data) {
            glClearTexSubImage_DoubleBuffer.run(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
        }

        private Texture() {}
    }

    private static void inline_init(final ContextCapabilities cap) {
        DataType.init(cap);
        Operation.init(cap);
        Operation.Get.init(cap);
        Operation.Sync.init(cap);

        Shader.init(cap);
        Shader.Vert.init(cap);
        Shader.Frag.init(cap);
        Shader.Geom.init(cap);
        Shader.Tess.init(cap);
        Shader.Comp.init(cap);
        Shader.checkBaseShaderValid();

        Drawcall.init(cap);
        Drawcall.MultiTex.init(cap);

        Buffer.init(cap);
        Buffer.VBO.init(cap);
        Buffer.PBO.init(cap);
        Buffer.TransformFB.init(cap);
        Buffer.TBO.init(cap);
        Buffer.UBO.init(cap);
        Buffer.Indirect.init(cap);
        Buffer.SSBO.init(cap);

        VAO.init(cap);
        FBO.init(cap);

        Texture.init(cap);
        Texture.Compressed.init(cap);
        Texture.Bindless.init(cap);
    }

    public static void init() {
        if (NOT_INIT) {
            synchronized (GLWrapper.class) {
                if (NOT_INIT) {
                    NOT_INIT = false;
                    inline_init(GLContext.getCapabilities());
                }
            }
        }
    }

    private GLWrapper() {}
}
