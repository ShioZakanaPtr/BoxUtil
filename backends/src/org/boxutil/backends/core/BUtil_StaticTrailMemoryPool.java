package org.boxutil.backends.core;

import org.boxutil.units.standard.attribute.StaticTrailData;
import org.lwjgl.opengl.GL32;

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
public final class BUtil_StaticTrailMemoryPool {
    private final static HashMap<StaticTrailData, BufferObject> _MEM = new HashMap<>(64);

    private final class BufferObject {
        private int id;
        private long size;
        private int nodeSize;
    }

    private BUtil_StaticTrailMemoryPool() {}
}
