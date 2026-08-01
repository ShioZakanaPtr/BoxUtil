package org.boxutil.backends.core.statictrail;

import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.struct.GPUPoolBehavior;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.units.standard.attribute.StaticTrailData;
import org.boxutil.util.CalculateUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

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
 * Draw with {@link GL32#GL_LINE_STRIP_ADJACENCY} that have <code>n+1</code> nodes memory allocated for <code>n</code> nodes, and with 1 reversed node in pool start.<p>
 * Typically, nodes are recorded every 1/30 second, so at most 30 nodes per second.
 */
public final class BUtil_StaticTrailMemoryPool extends GPUMemoryPool<BUtil_StaticTrailMemory, StaticTrailTracker> {
    public final static byte RESERVED_SIZE = 20;
    private final static HashMap<StaticTrailData, BUtil_StaticTrailMemoryPool> TRAIL = new HashMap<>(64);
    private final static GPUPoolBehavior<BUtil_StaticTrailMemory, StaticTrailTracker> BEHAVIOR;
    private final static FloatBuffer STATE_PACKAGE_MEM = BufferUtils.createFloatBuffer(40);
    private static boolean INVALID = true;

    static {
        BEHAVIOR = new GPUPoolBehavior<>(GL15.GL_ARRAY_BUFFER, BUtil_StaticTrailMemoryPool::makeEmptyMem, BUtil_StaticTrailMemoryPool::makeNotEmptyMem);
        BEHAVIOR.persistentMappingAllow = true;
        BEHAVIOR.defaultBufferSize = 16384;
        BEHAVIOR.reservedSize = RESERVED_SIZE;
        BEHAVIOR.glContextRequirements = BUtil_StaticTrailMemoryPool::poolReq;
    }

    private final int maxFullNodes;
    private final float maxDur;
    private final long allocSize;
    private IntBuffer piFirst = null;
    private IntBuffer piCount = null;
    private FloatBuffer legacyUploadBuf = null;
    private final StaticTrailData trailData;

    public BUtil_StaticTrailMemoryPool(final GPUPoolBehavior<BUtil_StaticTrailMemory, StaticTrailTracker> behavior, final StaticTrailData trailData, float maxDur) {
        super(behavior);
        this.maxDur = maxDur;
        this.maxFullNodes = (int) Math.ceil(this.maxDur * BoxConfigs.getTrailSystemNodesPerSecond()) + 1;
        this.allocSize = (long) this.maxFullNodes * 20;
        this.trailData = trailData;
    }

    private static boolean poolReq(GPUMemoryPool<BUtil_StaticTrailMemory, StaticTrailTracker> ignore) {
        return BoxDatabase.getGLState().BOXUTIL_VALID && BoxConfigs.isBackgroundThreadGLValid() && BoxConfigs.isTrailSystemEnable();
    }

    public static void initPool() {
        if (!poolReq(null)) return;
        INVALID = false;
    }

    private static BUtil_StaticTrailMemory makeEmptyMem(StaticTrailTracker meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, StaticTrailTracker> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, true);
    }

    private static BUtil_StaticTrailMemory makeNotEmptyMem(StaticTrailTracker meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, StaticTrailTracker> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, false);
    }

    public static boolean setupTrail(final StaticTrailData target) {
        final float maxDur = Math.max(target.getFadeInTime(), 0.0f) + Math.max(target.getFullTime(), 0.0f) + Math.max(target.getFadeOutTime(), 0.0f);
        if (maxDur < StaticTrailData.MINIMUM_TOTAL_DURATION) return false;

        TRAIL.computeIfAbsent(target, (targetData) -> new BUtil_StaticTrailMemoryPool(BEHAVIOR, targetData, maxDur));
        return true;
    }

    private void checkVertexPtr() {
        final int trackerNum = this.mem.size();
        if (this.piFirst == null || this.piFirst.capacity() < trackerNum) {
            final int size = CalculateUtil.getPOTMax(trackerNum);
            this.piFirst = BufferUtils.createIntBuffer(size).position(0);
            this.piCount = BufferUtils.createIntBuffer(size).position(0);
        }
    }

    private void finishVertexPtr(int drawNum) {
        this.piFirst.position(0).limit(drawNum);
        this.piCount.position(0).limit(drawNum);
    }

    public static void computeTrailNode(float amount, float elapsedTime) {
        for (var pool : TRAIL.values()) {
            if (pool.isInvalid()) continue;

            final boolean notPersistentMapping = !pool.isPersistentMapping() || pool.getMappingBuffer() == null;
            final FloatBuffer writeBuffer = notPersistentMapping ? pool.legacyUploadBuf : pool.getMappingBuffer().asFloatBuffer();
            pool.checkVertexPtr();

            BUtil_StaticTrailMemory trailMemory;
            int computeCode, writePtr = 0;
            for (final var iterator = pool.mem.iterator(); iterator.hasNext();) {
                trailMemory = iterator.next();

                computeCode = trailMemory.computeData(pool, writeBuffer, notPersistentMapping, amount, elapsedTime);
                if (computeCode < 0) {
                    iterator.remove();
                    continue;
                }

                pool.piFirst.put(writePtr, computeCode + 2); // todo
                pool.piCount.put(writePtr, computeCode + 2); // fill node of curr-node and pre-node
                writePtr++;
            }
            pool.finishVertexPtr(writePtr); // when have valid node: ptr++ => size
        }
    }

    public void glDraw() {
        if (this.piFirst.limit() > 0) GL14.glMultiDrawArrays(GL32.GL_LINE_STRIP_ADJACENCY, this.piFirst, this.piCount);
    }

    public static BUtil_StaticTrailMemoryPool getPool(final StaticTrailData target) {
        return TRAIL.get(target);
    }

    public static void cleanupPool() {
        for (BUtil_StaticTrailMemoryPool pool : TRAIL.values()) pool.destroy();
        TRAIL.clear();
    }

    public static boolean isNotSupported() {
        return INVALID;
    }

    public static int getTrailTypes() {
        return TRAIL.size();
    }

    public static long getTotalAllocatedMemory() {
        return TRAIL.values().stream().filter(Objects::nonNull).mapToLong(BUtil_StaticTrailMemoryPool::getBufferTotal).sum();
    }

    public static long getTotalSpace() {
        return TRAIL.values().stream().filter(Objects::nonNull).mapToLong(BUtil_StaticTrailMemoryPool::getBufferSpace).sum();
    }

    public int getMaxFullNodes() {
        return this.maxFullNodes;
    }

    public float getMaxDur() {
        return this.maxDur;
    }

    public StaticTrailData getTrailData() {
        return this.trailData;
    }

    public boolean realloc(BUtil_StaticTrailMemory memory, long newSize) {
        throw new UnsupportedOperationException("Pool method realloc() is not supported on static trail memory.");
    }

    public BUtil_StaticTrailMemory split(BUtil_StaticTrailMemory memory, long newSize, boolean fromStartOrEnd) {
        throw new UnsupportedOperationException("Pool method split() is not supported on static trail memory.");
    }

    private static void tryVec4(int index, final Vector4f vec, float defX, float defY, float defZ, float defW) {
        final boolean putDef = vec == null;
        STATE_PACKAGE_MEM.put(index, putDef ? defX : vec.x);
        STATE_PACKAGE_MEM.put(index, putDef ? defY : vec.y);
        STATE_PACKAGE_MEM.put(index, putDef ? defZ : vec.z);
        STATE_PACKAGE_MEM.put(index, putDef ? defW : vec.w);
    }

    private static void tryVec2(int index, final Vector2f vec, float defX, float defY) {
        final boolean putDef = vec == null;
        STATE_PACKAGE_MEM.put(index, putDef ? defX : vec.x);
        STATE_PACKAGE_MEM.put(index, putDef ? defY : vec.y);
    }

    public static FloatBuffer updateStateUniforms(final StaticTrailData data) {
        STATE_PACKAGE_MEM.put(0, data.getMaterial().getState(), 0, 12);
        tryVec4(12, data.getColorIn(), 1.0f, 1.0f, 1.0f, 1.0f);
        tryVec4(16, data.getColorOut(), 1.0f, 1.0f, 1.0f, 1.0f);
        STATE_PACKAGE_MEM.put(20, data.getFadeInTime());
        STATE_PACKAGE_MEM.put(21, data.getFullTime());
        STATE_PACKAGE_MEM.put(22, data.getFadeOutTime());
        STATE_PACKAGE_MEM.put(23, data.isRandomUVStartOffset() ? 1.0f : 0.0f);
        STATE_PACKAGE_MEM.put(24, data.getSizeIn());
        STATE_PACKAGE_MEM.put(25, data.getSizeOut());
        STATE_PACKAGE_MEM.put(26, 1.0f / data.getTexturePixels());
        STATE_PACKAGE_MEM.put(27, data.getTextureSpeed());
        tryVec4(28, data.getVelocityInRange(), 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec4(32, data.getVelocityOutRange(), 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec2(36, data.getAngularInRange(), 0.0f, 0.0f);
        tryVec2(38, data.getAngularOutRange(), 0.0f, 0.0f);
        return STATE_PACKAGE_MEM.position(0).limit(40);
    }
}
