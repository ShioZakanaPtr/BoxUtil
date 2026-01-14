package org.boxutil.units.standard.entity;

import org.boxutil.define.LayeredEntityType;
import org.boxutil.util.CurveUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.BaseMIRenderData;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.NodeData;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a 2D curve.<p>
 * Curve entity will not apply AA if depth based AA is enabled.<p>
 * Use vanilla beam texture uv(transversal beam texture).
 */
public class CurveEntity extends BaseMIRenderData {
    protected final static float _CIRCLE_FIX = 1.3333334f;
    protected final static byte _NODE_LENGTH = 13;
    protected final int _curveID;
    protected final int _nodesVBO;
    protected final boolean _isValid;
    // {vec2(loc), vec2(tangent left), vec2(tangent right), vec4(color), vec4(emissive), width}
    protected List<NodeData> nodeList = null;
    protected int _lastNodeLength = 0;
    protected int _lastNodeLengthReal = 0;
    protected List<Float> _distance = null;
    protected int shouldRenderingCount = 0;
    protected final int[] nodeRefreshState = new int[]{0, 0, 8};
    protected short interpolation = 0;
    protected final float[] state = new float[]{4.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f}; // texture speed, texture pixels, fillStart, fillEnd, startFactor, endFactor, uvOffset
    protected final boolean[] stateB = new boolean[]{false, false, false, true}; // flowWhenPaused, globalUV, synchronousSubmit, mappingMode

    protected int _StatePackageStack() {
        return 5;
    }

    protected void initResourceLayout() {
        final int size = _NODE_LENGTH * BoxDatabase.HALF_FLOAT_SIZE;
        GL30.glBindVertexArray(this.getCurveID());
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getNodesVBO());
        GL20.glVertexAttribPointer(0, 2, GL30.GL_HALF_FLOAT, false, size, 0); // loc
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 4, GL30.GL_HALF_FLOAT, false, size, 2 * BoxDatabase.HALF_FLOAT_SIZE); // tangent
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(2, 1, GL30.GL_HALF_FLOAT, false, size, 6 * BoxDatabase.HALF_FLOAT_SIZE); // width
        GL20.glEnableVertexAttribArray(2);
        GL20.glVertexAttribPointer(3, 1, GL30.GL_HALF_FLOAT, false, size, 7 * BoxDatabase.HALF_FLOAT_SIZE); // mixFactor
        GL20.glEnableVertexAttribArray(3);
        GL20.glVertexAttribPointer(4, 1, GL30.GL_HALF_FLOAT, false, size, 8 * BoxDatabase.HALF_FLOAT_SIZE); // nodeDistance
        GL20.glEnableVertexAttribArray(4);
        GL20.glVertexAttribPointer(5, 4, GL11.GL_UNSIGNED_BYTE, true, size, 9 * BoxDatabase.HALF_FLOAT_SIZE); // color
        GL20.glEnableVertexAttribArray(5);
        GL20.glVertexAttribPointer(6, 4, GL11.GL_UNSIGNED_BYTE, true, size, 11 * BoxDatabase.HALF_FLOAT_SIZE); // emissiveColor
        GL20.glEnableVertexAttribArray(6);
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public CurveEntity() {
        this._curveID = BoxConfigs.isVAOSupported() ? GL30.glGenVertexArrays() : 0;
        this._nodesVBO = BoxConfigs.isVAOSupported() ? GL15.glGenBuffers() : 0;
        this._isValid = this.getCurveID() > 0 && this.getNodesVBO() > 0;
        this.getMaterialData().setDisableCullFace();
        this.getMaterialData().setIgnoreIllumination(true);

        if (this.isValid()) this.initResourceLayout();
    }

    public CurveEntity(@NotNull List<NodeData> nodeList) {
        this();
        this.setNodes(nodeList);
        this.submitNodes();
    }

    public int getCurveID() {
        return this._curveID;
    }

    public int getNodesVBO() {
        return this._nodesVBO;
    }

    public boolean isValid() {
        return this._isValid;
    }

    protected void _deleteExc() {
        super._deleteExc();
        this._lastNodeLength = 0;
        this._lastNodeLengthReal = 0;
        if (this.nodeList != null) this.nodeList.clear();
        this.nodeList = null;
        if (this._distance != null) this._distance.clear();
        this._distance = null;
        if (this.getCurveID() > 0) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.getCurveID());
        }
        if (this.getNodesVBO() > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glDeleteBuffers(this.getNodesVBO());
        }
    }

    public void glDraw() {
        int realCount = Math.min(this.shouldRenderingCount, this.getRealValidNodeCount());
        if (realCount < 2) return;
        GL30.glBindVertexArray(this.getCurveID());
        GL31.glDrawArraysInstanced(GL40.GL_PATCHES, 0, realCount, Math.max(Math.min(this.getValidInstanceDataCount(), this.getRenderingCount()), 1));
    }

    protected void _resetExc() {
        super._resetExc();
        this.interpolation = 0;
        this.state[0] = 4.0f;
        this.state[1] = 1.0f;
        this.state[2] = 1.0f;
        this.state[3] = 1.0f;
        this.state[4] = 1.0f;
        this.state[5] = 1.0f;
        this.state[6] = 0.0f;
        this.stateB[0] = false;
        this.stateB[1] = false;
    }

    /**
     * Excludes nodeList data.
     */
    public void reset() {
        super.reset();
    }

    public void resetNodes() {
        this._sync_lock.lock();
        if (this.nodeList != null) this.nodeList.clear();
        if (this._distance != null) this._distance.clear();
        this._lastNodeLength = 0;
        this._lastNodeLengthReal = 0;
        this.nodeRefreshState[0] = 0;
        this.nodeRefreshState[1] = 0;
        this.nodeRefreshState[2] = 8;
        this.stateB[2] = false;
        this.stateB[3] = true;
        this._sync_lock.unlock();
    }

    /**
     * Use it when you changed the node data.<p>
     * Will not keeping any node data if beyonds the initial size or the maximum submit size, recommend to call {@link CurveEntity#mallocNodeData(int)} for allocate enough size before.<p>
     * Cost <code>26 Byte * (2n - 2)</code> of vRAM when have number of <code>n</code> nodes, that if it had <code>8192</code> nodes will cost <code>415.95 KB</code> of vRAM.
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when an empty node list or refresh count is zero.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    public byte submitNodes() {
        this._sync_lock.lock();
        if (this.nodeList == null || this.nodeList.size() < 2) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        if (!this.isValid()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }
        final int nodeSize = this.nodeList.size();
        final boolean newBuffer = nodeSize > this._lastNodeLength;
        final int refreshIndex = newBuffer ? 0 : this.nodeRefreshState[0];
        final int refreshCount = newBuffer ? nodeSize : this.nodeRefreshState[1];
        if (refreshCount < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        final int refreshLimit = refreshIndex + refreshCount;
        final long bufferSize = (refreshCount > 1 ? (2L * refreshCount - 2) : refreshCount) * _NODE_LENGTH << 2;

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getNodesVBO());
        if (newBuffer) GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferSize, GL15.GL_DYNAMIC_DRAW);

        final long bufferIndex = (refreshIndex > 1 ? (2L * refreshIndex - 1) : refreshIndex) * _NODE_LENGTH << 2;
        final boolean useMapping = this.isMappingModeSubmitData();
        ByteBuffer buffer;
        if (useMapping) {
            final int _access = this.isSynchronousSubmit() ? GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT : GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT;
            buffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, bufferIndex, bufferSize, _access, null);
            if (buffer == null || buffer.capacity() < 1) {
                GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                this._lastNodeLengthReal = this.shouldRenderingCount = 0;
                this._sync_lock.unlock();
                return BoxEnum.STATE_FAILED_OTHER;
            }
        } else {
            buffer = BufferUtils.createByteBuffer((int) bufferSize);
        }

        ShortBuffer newData = buffer.asShortBuffer();
        NodeData data;
        float nodeDis;
        if (refreshIndex == 0) nodeDis = 0.0f;
        else {
            int getIndex = refreshIndex - 1;
            nodeDis = (this._distance != null && this._distance.size() > getIndex) ? this._distance.get(getIndex) : 0.0f;
        }
        if (this._distance == null) this._distance = new ArrayList<>(nodeSize);
        short[] dataPackage = new short[_NODE_LENGTH];
        for (int i = refreshIndex; i < refreshLimit; i++) {
            data = this.nodeList.get(i);
            if (i != 0) {
                nodeDis += CurveUtil.getCurveLength(data, this.nodeList.get(i - 1), this.nodeRefreshState[2]);
            }
            if (i >= this._distance.size()) this._distance.add(nodeDis); else this._distance.set(i, nodeDis);
            System.arraycopy(CommonUtil.float16ToShort(data.getState()), 0, dataPackage, 0, 8);
            dataPackage[8] = CommonUtil.float16ToShort(nodeDis);
            System.arraycopy(CommonUtil.packingBytesToShort(data.pickColor_RGBA8_A(), data.pickColor_RGBA8_B()), 0, dataPackage, 9, 4);
            newData.put(dataPackage);
            if (i != 0 && i < this.nodeList.size() - 1) newData.put(dataPackage);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());

        if (useMapping) GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
        else GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, bufferIndex, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        if (newBuffer) this._lastNodeLength = nodeSize;
        this._lastNodeLengthReal = this.shouldRenderingCount = nodeSize * 2 - 2;
        this._sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Optional.<p>
     * Just <code>malloc()</code> without any submit call.
     *
     * @param nodeNum must be positive integer.
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    public byte mallocNodeData(int nodeNum) {
        this._sync_lock.lock();
        if (nodeNum < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        if (!this.isValid()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }

        this._lastNodeLength = nodeNum;
        this._lastNodeLengthReal = this.shouldRenderingCount = nodeNum * 2 - 2;
        final long bufferSize = (long) this._lastNodeLengthReal * _NODE_LENGTH << 1;

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getNodesVBO());
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferSize, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this._sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Default value is <code>false</code>.
     */
    public boolean isSynchronousSubmit() {
        return this.stateB[2];
    }

    /**
     * <strong>High performance impact when is synchronized.</strong>
     */
    public void setSynchronousSubmit(boolean sync) {
        this._sync_lock.lock();
        this.stateB[2] = sync;
        this._sync_lock.unlock();
    }

    /**
     * Default value is <code>true</code>.
     */
    public boolean isMappingModeSubmitData() {
        return this.stateB[3];
    }

    /**
     * For some very slight data, use <code>glBufferSubData()</code> may faster.<p>
     * Besides, some devices(some ARM SoC) may slower with <code>glMapBufferRange()</code>, decided by the drive how implements it.
     *
     * @param mappingMode to controls whether submit use <code>glMapBufferRange()</code>, else use <code>glBufferSubData()</code>.
     */
    public void setMappingModeSubmitData(boolean mappingMode) {
        this._sync_lock.lock();
        this.stateB[3] = mappingMode;
        this._sync_lock.unlock();
    }

    public int getNodeRefreshIndex() {
        return this.nodeRefreshState[0];
    }

    /**
     * @param index Will refresh node data start from this index.
     */
    public void setNodeRefreshIndex(int index) {
        this._sync_lock.lock();
        if (this.nodeList == null) {
            this._sync_lock.unlock();
            return;
        }
        this.nodeRefreshState[0] = Math.min(Math.max(index, 0), Math.max(this.nodeList.size() - 1, 0));
        this._sync_lock.unlock();
    }

    /**
     * @param size Will refresh node data count.
     */
    public void setNodeRefreshSize(int size) {
        this._sync_lock.lock();
        if (this.nodeList == null) {
            this._sync_lock.unlock();
            return;
        }
        final int nodeSize = this.nodeList.size();
        this.nodeRefreshState[1] = this.nodeRefreshState[0] + size > nodeSize ? nodeSize - this.nodeRefreshState[0] : Math.max(size, 0);
        this._sync_lock.unlock();
    }

    public void setNodeRefreshAllFromCurrentIndex() {
        this._sync_lock.lock();
        if (this.nodeList == null) {
            this._sync_lock.unlock();
            return;
        }
        this.nodeRefreshState[1] = this.nodeList.size() - this.nodeRefreshState[0];
        this._sync_lock.unlock();
    }

    public int getSubmitLengthCalculatingStep() {
        return this.nodeRefreshState[2];
    }

    /**
     * @param step must be greater than zero.
     */
    public void setSubmitLengthCalculatingStep(int step) {
        this._sync_lock.lock();
        this.nodeRefreshState[2] = Math.max(step, 1);
        this._sync_lock.unlock();
    }

    public int getValidNodeCount() {
        return this._lastNodeLength;
    }

    public boolean isHaveValidNodeCount() {
        return this._lastNodeLength >= 2;
    }

    public int getRealValidNodeCount() {
        return this._lastNodeLengthReal;
    }

    public int getNodeRenderingCount() {
        return (this.shouldRenderingCount + 2) / 2;
    }

    public void setNodeRenderingCount(int nodeCount) {
        this.shouldRenderingCount = nodeCount < 1 ? 0 : nodeCount * 2 - 2;
    }

    public List<NodeData> getNodes() {
        return this.nodeList;
    }

    /**
     * @param nodeList Cannot have any null-node, and at least have two nodeList, if not then pass this entity when rendering.
     */
    public void setNodes(@NotNull List<NodeData> nodeList) {
        this._sync_lock.lock();
        if (nodeList.size() > BoxConfigs.getMaxCurveNodeSize()) this.nodeList = nodeList.subList(0, BoxConfigs.getMaxCurveNodeSize());
        else this.nodeList = nodeList;
        if (this._distance == null) this._distance = new ArrayList<>(this.nodeList.size());
        this._sync_lock.unlock();
    }

    public void addNode(@NotNull NodeData node) {
        this._sync_lock.lock();
        if (this.nodeList == null) this.nodeList = new ArrayList<>();
        if (this.nodeList.size() > BoxConfigs.getMaxCurveNodeSize()) {
            this._sync_lock.unlock();
            return;
        }
        this.nodeList.add(node);
        if (this._distance == null) this._distance = new ArrayList<>();
        this._sync_lock.unlock();
    }

    /**
     * Create a line-strip as {@link GL11#GL_LINE_STRIP}, and submit nodeList.<p>
     * Flat tangent, just a line-strip.
     *
     * @param points size must be larger than 2.
     */
    public CurveEntity initLineStrip(@NotNull List<Vector2f> points, Color color, Color emissiveColor, float width, boolean closed) {
        if (points.size() < 2) return this;
        List<NodeData> lineStrip = new ArrayList<>(closed ? points.size() + 1 : points.size());
        for (Vector2f point : points) {
            NodeData node = new NodeData(point.x, point.y, -1.0f, 0.0f, 1.0f, 0.0f);
            node.setColor(color);
            node.setEmissiveColor(emissiveColor);
            node.setWidth(width);
            lineStrip.add(node);
        }
        if (closed) lineStrip.add(lineStrip.get(0));
        this.setNodes(lineStrip);
        this.setNodeRefreshAllFromCurrentIndex();
        this.submitNodes();
        return this;
    }

    /**
     * Create a two-points line, and submit nodeList.<p>
     * Flat tangent, just a line.
     */
    public CurveEntity initLine(@Nullable Vector2f offset, float length, Color color, Color emissiveColor, float width) {
        if (offset == null) offset = new Vector2f(0.0f, 0.0f);
        List<NodeData> line = new ArrayList<>(2);
        NodeData from = new NodeData(offset.x, offset.y, -1.0f, 0.0f, 1.0f, 0.0f);
        from.setColor(color);
        from.setEmissiveColor(emissiveColor);
        from.setWidth(width);
        NodeData to = new NodeData(offset.x + length, offset.y, -1.0f, 0.0f, 1.0f, 0.0f);
        to.setColor(color);
        to.setEmissiveColor(emissiveColor);
        to.setWidth(width);
        line.add(from);
        line.add(to);
        this.setNodes(line);
        this.setNodeRefreshAllFromCurrentIndex();
        this.submitNodes();
        return this;
    }

    /**
     * Create a three-points line with smooth both ends, and submit nodeList.
     */
    public CurveEntity initSmoothLine(@Nullable Vector2f offset, float length, Color color, Color emissiveColor, float width, float smoothFactor) {
        if (offset == null) offset = new Vector2f(0.0f, 0.0f);
        List<NodeData> line = new ArrayList<>(2);
        NodeData from = new NodeData(offset.x, offset.y, -1.0f, 0.0f, 1.0f, 0.0f);
        from.setColor(color);
        from.setEmissiveColor(emissiveColor);
        from.setWidth(width);
        from.setMixFactor(smoothFactor);
        NodeData mid = new NodeData(from);
        float[] tmpLoc = mid.getLocationArray();
        mid.setLocation(tmpLoc[0] + length * 0.05f, tmpLoc[1]);
        mid.setMixFactor(1.0f - smoothFactor);
        NodeData to = new NodeData(from);
        to.setLocation(tmpLoc[0] + length * 0.95f, tmpLoc[1]);
        to.setMixFactor(smoothFactor);
        from.setAlpha(0.0f);
        to.setAlpha(0.0f);
        line.add(from);
        line.add(mid);
        line.add(to);
        this.setNodes(line);
        this.setNodeRefreshAllFromCurrentIndex();
        this.submitNodes();
        return this;
    }

    /**
     * Rough fitting but more performance.<p>
     * Create a circle with three-points, and submit nodeList.<p>
     * Interpolation should be greater than zero.
     */
    public CurveEntity initCircle(@Nullable Vector2f offset, float radius, Color color, Color emissiveColor, float width) {
        return this.initElliptic(offset, radius, radius, color, emissiveColor, width);
    }

    /**
     * Rough fitting but more performance.<p>
     * Create an elliptic with three-points, and submit nodeList.<p>
     * Interpolation should be greater than zero.
     */
    public CurveEntity initElliptic(@Nullable Vector2f offset, float radiusW, float radiusH, Color color, Color emissiveColor, float width) {
        if (offset == null) offset = new Vector2f(0.0f, 0.0f);
        float heightFix = radiusH * _CIRCLE_FIX;
        List<NodeData> line = new ArrayList<>(3);
        NodeData left = new NodeData(offset.x - radiusW, offset.y, 0.0f, -heightFix, 0.0f, heightFix);
        left.setColor(color);
        left.setEmissiveColor(emissiveColor);
        left.setWidth(width);
        NodeData right = new NodeData(offset.x + radiusW, offset.y, 0.0f, heightFix, 0.0f, -heightFix);
        right.setColor(color);
        right.setEmissiveColor(emissiveColor);
        right.setWidth(width);
        line.add(left);
        line.add(right);
        line.add(left);
        this.setNodes(line);
        this.setNodeRefreshAllFromCurrentIndex();
        this.submitNodes();
        return this;
    }

    public int getInterpolation() {
        return this.interpolation;
    }

    /**
     * From zero.<p>
     * Will add points at geometry shader.
     */
    public void setInterpolation(short interpolation) {
        this.interpolation = (short) Math.max(Math.min(interpolation, BoxConfigs.getMaxCurveInterpolation()), 0);
    }

    public float[] getCurveState() {
        return this.state;
    }

    public float getTextureSpeed() {
        return this.state[0];
    }

    /**
     * It is working together with {@link CurveEntity#setTexturePixels(float)}.
     *
     * @param textureSpeed looks flowing forward when less than zero.
     */
    public void setTextureSpeed(float textureSpeed) {
        this.state[0] = textureSpeed;
    }

    public float getTexturePixels() {
        return this.state[1];
    }

    /**
     * @param texturePixels texture fill size.
     */
    public void setTexturePixels(float texturePixels) {
        this.state[1] = texturePixels;
    }

    public boolean isFlowWhenPaused() {
        return this.stateB[0];
    }

    public void setFlowWhenPaused(boolean flow) {
        this.stateB[0] = flow;
    }

    public boolean isGlobalUV() {
        return this.stateB[1];
    }

    /**
     * Force uv mapping to [0, 1] from the first node to the last node, such as used for flag or tentacle.
     */
    public void setGlobalUV(boolean useGlobal) {
        this.stateB[1] = useGlobal;
    }

    public float getUVOffset() {
        return this.state[6];
    }

    public void setUVOffset(float offset) {
        this.state[6] = offset;
    }

    public float getFillStartAlpha() {
        return this.state[2];
    }

    public void setFillStartAlpha(float alpha) {
        this.state[2] = alpha;
    }

    public float getFillEndAlpha() {
        return this.state[3];
    }

    public void setFillEndAlpha(float alpha) {
        this.state[3] = alpha;
    }

    public float getFillStartFactor() {
        return this.state[4];
    }

    public void setFillStartFactor(float factor) {
        this.state[4] = factor;
    }

    public float getFillEndFactor() {
        return this.state[5];
    }

    public void setFillEndFactor(float factor) {
        this.state[5] = factor;
    }

    public FloatBuffer pickDataPackage_vec4() {
        final float iPO = this.interpolation + 1;
        this._statePackageBuffer.put(0, this.material.getState(), 0, 12);
        this._statePackageBuffer.put(12, iPO);
        this._statePackageBuffer.put(13, this.state[1]);
        this._statePackageBuffer.put(14, this.stateB[1] ? 1.0f / (this._lastNodeLength - 1) : -1.0f);
        this._statePackageBuffer.put(16, this.state, 2, 4);
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.CURVE;
    }
}
