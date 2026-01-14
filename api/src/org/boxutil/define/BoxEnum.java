package org.boxutil.define;

public final class BoxEnum {
    public final static byte STATE_SUCCESS = 0;
    public final static byte STATE_FAILED = 1;
    public final static byte STATE_FAILED_OTHER = 2;

    public final static byte FALSE = 0;
    public final static byte TRUE = 1;
    public final static byte ZERO = 0;
    public final static byte ONE = 1;
    public final static byte NEG_ONE = -1;
    public final static byte ONE_COLOR = -1;

    public final static byte MODE_COMMON = 0;
    public final static byte MODE_COLOR = 1;

    public final static byte TIMER_IN = 10;
    public final static byte TIMER_FULL = 11;
    public final static byte TIMER_OUT = 12;
    public final static byte TIMER_ONCE = 13;
    public final static byte TIMER_INVALID = 14;

    public final static byte ENTITY_COMMON = 0;
    public final static byte ENTITY_SPRITE = 1;
    public final static byte ENTITY_CURVE = 2;
    public final static byte ENTITY_SEGMENT = 3;
    public final static byte ENTITY_FLARE = 4;
    @Deprecated
    public final static byte ENTITY_CUSTOM = 5;
    public final static byte ENTITY_TEXT = 6;
    public final static byte ENTITY_TRAIL = 7;

    public final static byte ENTITY_DISTORTION = 0;
    public final static byte ENTITY_INFINITE_LIGHT = 1;
    public final static byte ENTITY_POINT_LIGHT = 2;
    public final static byte ENTITY_SPOT_LIGHT = 3;
    public final static byte ENTITY_LINE_LIGHT = 4;
    public final static byte ENTITY_AREA_LIGHT = 5;

    public final static byte MP_BEAUTY = 0;
    public final static byte MP_COLOR = 1;
    public final static byte MP_EMISSIVE = 2;
    public final static byte MP_POSITION = 3;
    public final static byte MP_NORMAL = 4;
    public final static byte MP_TANGENT = 5;
    public final static byte MP_MATERIAL = 6;
    public final static byte MP_BLOOM = 7;

    public final static byte AA_DISABLE = 30;
    public final static byte AA_FXAA_CONSOLE = 31;
    public final static byte AA_FXAA_QUALITY = 32;

    public final static byte PARALLEL_JVM = 0;
    public final static byte PARALLEL_GL = 1;

    public final static byte GL_DEVICE_AMD_ATI = 0;
    public final static byte GL_DEVICE_NVIDIA = 1;
    public final static byte GL_DEVICE_INTEL = 2;
    public final static byte GL_DEVICE_OTHER = 3;

    private BoxEnum() {}
}
