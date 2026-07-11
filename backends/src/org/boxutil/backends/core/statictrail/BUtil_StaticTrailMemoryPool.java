package org.boxutil.backends.core.statictrail;

import org.boxutil.units.standard.attribute.StaticTrailData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * <pre>
 * {@code
 * // 20 byte
 * struct Node {
 *     vec2 a_vertex;
 *     vec2f16 a_facingVector;
 *     float a_timeStamp; // life time and 3-bits id
 *     float a_distance;
 * };
 * }
 * </pre>
 *
 * Draw with {@link GL32#GL_LINE_STRIP_ADJACENCY} that have <code>n+2</code> nodes memory allocated for <code>n</code> nodes.<p>
 * Typically, nodes are recorded every 1/30 second, so at most 30 nodes per second.
 */
// todo 每data单独锁
public final class BUtil_StaticTrailMemoryPool {
    private final static HashMap<StaticTrailData, BufferObject> _MEM = new HashMap<>(64);
    private final static FloatBuffer _STATE_PACKAGE_MEM = BufferUtils.createFloatBuffer(40);

    private static boolean _IMMUTABLE;

    private static boolean _INIT = false;
    private static boolean _INVALID = true;
    private static short _NODES_PER_SECOND = 30;

    public abstract class BufferObject {
        public int id;
        public long size;
        public int nodeSize;
    }

    public static void init() {
        if (_INIT) return;
        _INIT = true;

        _INVALID = false;
    }

    public static boolean isNotSupported() {
        return _INVALID;
    }

    static void _PoolDestroy() {
        _INVALID = true;
        _INIT = false;
    }

    public static BufferObject malloc(final StaticTrailData target) {
        return null;
    }

    public static boolean isImmutableBuffer() {
        return _IMMUTABLE;
    }

    private static void tryVec4(int index, final Vector4f vec, float defX, float defY, float defZ, float defW) {
        final boolean putDef = vec == null;
        _STATE_PACKAGE_MEM.put(index, putDef ? defX : vec.x);
        _STATE_PACKAGE_MEM.put(index, putDef ? defY : vec.y);
        _STATE_PACKAGE_MEM.put(index, putDef ? defZ : vec.z);
        _STATE_PACKAGE_MEM.put(index, putDef ? defW : vec.w);
    }

    private static void tryVec2(int index, final Vector2f vec, float defX, float defY) {
        final boolean putDef = vec == null;
        _STATE_PACKAGE_MEM.put(index, putDef ? defX : vec.x);
        _STATE_PACKAGE_MEM.put(index, putDef ? defY : vec.y);
    }

    public static FloatBuffer packingStateUniforms(final StaticTrailData data) {
        _STATE_PACKAGE_MEM.put(0, data.getMaterial().getState(), 0, 12);
        tryVec4(12, data.getColorIn(), 1.0f, 1.0f, 1.0f, 1.0f);
        tryVec4(16, data.getColorOut(), 1.0f, 1.0f, 1.0f, 1.0f);
        _STATE_PACKAGE_MEM.put(20, data.getFadeInTime());
        _STATE_PACKAGE_MEM.put(21, data.getFullTime());
        _STATE_PACKAGE_MEM.put(22, data.getFadeOutTime());
        _STATE_PACKAGE_MEM.put(23, data.isRandomUVStartOffset() ? 1.0f : 0.0f);
        _STATE_PACKAGE_MEM.put(24, data.getSizeIn());
        _STATE_PACKAGE_MEM.put(25, data.getSizeOut());
        _STATE_PACKAGE_MEM.put(26, 1.0f / data.getTexturePixels());
        _STATE_PACKAGE_MEM.put(27, data.getTextureSpeed());
        tryVec4(28, data.getVelocityInRange(), 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec4(32, data.getVelocityOutRange(), 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec2(36, data.getAngularInRange(), 0.0f, 0.0f);
        tryVec2(38, data.getAngularOutRange(), 0.0f, 0.0f);
        _STATE_PACKAGE_MEM.position(0).limit(40);
        return _STATE_PACKAGE_MEM;
    }

    private BUtil_StaticTrailMemoryPool() {}
}
