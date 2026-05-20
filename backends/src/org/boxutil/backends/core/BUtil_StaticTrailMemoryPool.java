package org.boxutil.backends.core;

import org.boxutil.units.standard.attribute.StaticTrailData;

import java.util.HashMap;

/**
 * <pre>
 * {@code
 * // 16 byte
 * struct Node {
 *     vec2 location;
 *     vec2f16 facingVector;
 *     float timeStamp; // life time and id
 * };
 * }
 * </pre>
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
