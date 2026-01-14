package org.boxutil.units.standard.entity;

import org.boxutil.base.BaseRenderData;
import org.boxutil.base.api.MaterialRenderAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a 2D trail.<p>
 * Use vanilla beam texture uv(transversal beam texture).
 */
public class TrailEntity extends BaseRenderData implements MaterialRenderAPI {
    protected final static byte _BUFFER_DATA_SIZE = 3;
    protected final int _TBO;
    protected final int _TBOTex;
    protected final boolean _isValid;
    // {vec2(loc), vec2(tangent left), vec2(tangent right), vec4(color), vec4(emissive), width, mixFactor}
    protected List<Vector2f> nodeList = null;
    protected int _lastNodeLength = 0;
    protected List<Float> _distance = null;
    protected int shouldRenderingCount = 0;
    // {vec2(loc), float(distance)}
    protected final int[] nodeRefreshState = new int[]{0, 0, 8};
    // vec4(start), vec4(end), vec4(startEmissive), vec4(endEmissive)
    protected final float[] colorState = new float[]{BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE};
    // texturePixels, mixFactor, fillStart, fillEnd, startFactor, endFactor, startWidth, endWidth, jitterPower, flickerMix, texture speed, flickerCode, uvOffset
    protected final float[] state = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 4.0f, Math.abs(this.hashCode()) * 0.00066667f, 0.0f};
    protected final boolean[] stateB = new boolean[]{false, false, false, true, true, false, true}; // flowWhenPaused, flickWhenPaused, flickToggle, syncFlick, stripLineMode, synchronousSubmit, mappingMode
    protected MaterialData material = new MaterialData();

    protected int _StatePackageStack() {
        return 10;
    }

    public TrailEntity() {
        this._TBO = BoxConfigs.isTBOSupported() ? GL15.glGenBuffers() : 0;
        this._TBOTex = BoxConfigs.isTBOSupported() ? GL11.glGenTextures() : 0;
        this._isValid = this.getNodesTBO() > 0 && this.getNodesTBOTex() > 0;
        this.getMaterialData().setDisableCullFace();
        this.getMaterialData().setIgnoreIllumination(true);
    }

    public int getNodesTBO() {
        return this._TBO;
    }

    public int getNodesTBOTex() {
        return this._TBOTex;
    }

    public boolean isValid() {
        return this._isValid;
    }

    protected void _deleteExc() {
        super._deleteExc();
        this.material = null;
        this._lastNodeLength = 0;
        if (this.nodeList != null) this.nodeList.clear();
        this.nodeList = null;
        if (this._distance != null) this._distance.clear();
        this._distance = null;
        if (this.getNodesTBOTex() > 0) {
            GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
            GL11.glDeleteTextures(this.getNodesTBOTex());
        }
        if (this.getNodesTBO() > 0) {
            GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
            GL15.glDeleteBuffers(this.getNodesTBO());
        }
    }

    public void glDraw() {
        if (!this.isHaveValidNodeCount()) return;
        GL31.glDrawArraysInstanced(GL11.GL_LINES, 0, 2, this.glPrimCount());
    }

    protected void _resetExc() {
        super._resetExc();
        this.colorState[0] = this.colorState[1] = this.colorState[2] = this.colorState[3] = BoxEnum.ONE;
        this.colorState[4] = this.colorState[5] = this.colorState[6] = this.colorState[7] = BoxEnum.ONE;
        this.colorState[8] = this.colorState[9] = this.colorState[10] = this.colorState[11] = BoxEnum.ONE;
        this.colorState[12] = this.colorState[13] = this.colorState[14] = this.colorState[15] = BoxEnum.ONE;
        this.state[0] = 1.0f;
        this.state[1] = 1.0f;
        this.state[2] = 1.0f;
        this.state[3] = 1.0f;
        this.state[4] = 1.0f;
        this.state[5] = 1.0f;
        this.state[6] = 1.0f;
        this.state[7] = 1.0f;
        this.state[8] = 0.0f;
        this.state[9] = 1.0f;
        this.state[10] = 4.0f;
        this.state[11] = Math.abs(this.hashCode()) * 0.00066667f;
        this.state[12] = 0.0f;
        this.stateB[0] = false;
        this.stateB[1] = false;
        this.stateB[2] = false;
        this.stateB[3] = true;
        this.stateB[4] = true;
    }

    public void resetNodes() {
        this._sync_lock.lock();
        if (this.nodeList != null) this.nodeList.clear();
        if (this._distance != null) this._distance.clear();
        this._lastNodeLength = 0;
        this.nodeRefreshState[0] = 0;
        this.nodeRefreshState[1] = 0;
        this.nodeRefreshState[2] = 8;
        this.shouldRenderingCount = 0;
        this.stateB[5] = false;
        this.stateB[6] = true;
        this._sync_lock.unlock();
    }

    protected int computePrim(int num) {
        return this.isStripLineMode() ? num - 1 : num & 0xFFFFFE;
    }

    /**
     * Use it when you changed the node data.<p>
     * Will not keeping any node data if beyonds the initial size or the maximum submit size, recommend to call {@link TrailEntity#mallocNodeData(int)} for allocate enough size before.<p>
     * Cost <code>12 Byte * n</code> of vRAM when have number of <code>n</code> nodes<strong>(n is non-zero even number)</strong>, that if it had <code>8192</code> nodes will cost <code>96 KiB</code> of vRAM.
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
        final int refreshCount = newBuffer ? nodeSize - refreshIndex : this.nodeRefreshState[1];
        if (refreshCount < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        final int refreshLimit = refreshIndex + refreshCount;
        final int bufferSizeLoc = refreshCount * _BUFFER_DATA_SIZE;
        final long bufferSize = (long) bufferSizeLoc << 2;
        final long bufferIndex = (long) refreshIndex * _BUFFER_DATA_SIZE << 2;

        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, this.getNodesTBO());
        if (newBuffer) GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, bufferSize, GL15.GL_DYNAMIC_DRAW);

        ByteBuffer buffer;
        final boolean useMapping = this.isMappingModeSubmitData();
        if (useMapping) {
            final int _access = this.isSynchronousSubmit() ? GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT : GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT;
            buffer = GL30.glMapBufferRange(GL31.GL_TEXTURE_BUFFER, bufferIndex, bufferSize, _access, null);
            if (buffer == null || buffer.capacity() < 1) {
                GL15.glUnmapBuffer(GL31.GL_TEXTURE_BUFFER);
                GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
                this.shouldRenderingCount = 0;
                this._sync_lock.unlock();
                return BoxEnum.STATE_FAILED_OTHER;
            }
        } else {
            buffer = BufferUtils.createByteBuffer((int) bufferSize);
        }

        Vector2f currLoc, lastLoc;
        FloatBuffer newData = buffer.asFloatBuffer();
        int index = 0;
        float distance, tmpX, tmpY;
        if (refreshIndex == 0) distance = 0.0f;
        else {
            int getIndex = refreshIndex - 1;
            distance = (this._distance != null && this._distance.size() > getIndex) ? this._distance.get(getIndex) : 0.0f;
        }
        if (this._distance == null) this._distance = new ArrayList<>(nodeSize);
        for (int i = refreshIndex; i < refreshLimit; ++i) {
            currLoc = this.nodeList.get(i);
            newData.put(index, currLoc.x);
            ++index;
            newData.put(index, currLoc.y);
            ++index;
            if (i != 0) {
                lastLoc = this.nodeList.get(i - 1);
                tmpX = currLoc.x - lastLoc.x;
                tmpY = currLoc.y - lastLoc.y;
                distance += (float) Math.sqrt(tmpX * tmpX + tmpY * tmpY);
            }
            if (i >= this._distance.size()) this._distance.add(distance); else this._distance.set(i, distance);
            newData.put(index, distance);
            ++index;
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());

        if (useMapping) GL15.glUnmapBuffer(GL31.GL_TEXTURE_BUFFER);
        else GL15.glBufferSubData(GL31.GL_TEXTURE_BUFFER, bufferIndex, buffer);

        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, this.getNodesTBOTex());
        GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_RGB32F, this.getNodesTBO()); // damn it where my RGB16F is
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        if (newBuffer) this._lastNodeLength = nodeSize;
        this.shouldRenderingCount = this.computePrim(this._lastNodeLength);
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

        final long bufferSize = (long) nodeNum * _BUFFER_DATA_SIZE << 2;
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, this.getNodesTBO());
        GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, bufferSize, GL15.GL_DYNAMIC_DRAW);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, this.getNodesTBOTex());
        GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_RGB32F, this.getNodesTBO());
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
        this._lastNodeLength = nodeNum;
        this.shouldRenderingCount = this.computePrim(this._lastNodeLength);
        this._sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    public boolean isSynchronousSubmit() {
        return this.stateB[5];
    }

    /**
     * <strong>High performance impact when is synchronized.</strong>
     */
    public void setSynchronousSubmit(boolean sync) {
        this._sync_lock.lock();
        this.stateB[5] = sync;
        this._sync_lock.unlock();
    }

    public boolean isMappingModeSubmitData() {
        return this.stateB[6];
    }

    /**
     * For some very slight data, use <code>glBufferSubData()</code> may faster.<p>
     * Besides, some devices(some ARM SoC) may slower with <code>glMapBufferRange()</code>, decided by the drive how implements it.
     *
     * @param mappingMode to controls whether submit use <code>glMapBufferRange()</code>, else use <code>glBufferSubData()</code>.
     */
    public void setMappingModeSubmitData(boolean mappingMode) {
        this._sync_lock.lock();
        this.stateB[6] = mappingMode;
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

    public int getValidNodeCount() {
        return this._lastNodeLength;
    }

    @Deprecated
    public int getValidRenderingNodeCount() {
        return this._lastNodeLength;
    }

    public boolean isHaveValidNodeCount() {
        return this._lastNodeLength > 0;
    }

    public int getNodeRenderingCount() {
        return this.shouldRenderingCount < 1 ? 0 : (this.isStripLineMode() ? this.shouldRenderingCount + 1 : this.shouldRenderingCount);
    }

    public void setNodeRenderingCount(int nodeCount) {
        this.shouldRenderingCount = nodeCount < 2 ? 0 : this.computePrim(nodeCount);
    }

    protected int glPrimCount() {
        return Math.min(this.shouldRenderingCount, this.getValidNodeCount());
    }

    public List<Vector2f> getNodes() {
        return this.nodeList;
    }

    /**
     * Recommend to use relative coordinate.<p>
     * Node at index 0 will be the end point of the trail.
     *
     * @param nodeList Cannot have any null-node, and at least have two nodes, if not then pass this entity when rendering.
     */
    public void setNodes(@NotNull List<Vector2f> nodeList) {
        this._sync_lock.lock();
        if (nodeList.size() > BoxConfigs.getMaxInstanceDataSize()) this.nodeList = nodeList.subList(0, BoxConfigs.getMaxInstanceDataSize());
        else this.nodeList = nodeList;
        if (this._distance == null) this._distance = new ArrayList<>(this.nodeList.size());
        this._sync_lock.unlock();
    }

    /**
     * Node at index 0 will be the end point of the trail, and draw from index end to the last index/ the start point.
     */
    public void addNode(@NotNull Vector2f node) {
        this._sync_lock.lock();
        if (this.nodeList == null) this.nodeList = new ArrayList<>();
        if (this.nodeList.size() > BoxConfigs.getMaxInstanceDataSize()) {
            this._sync_lock.unlock();
            return;
        }
        this.nodeList.add(node);
        if (this._distance == null) this._distance = new ArrayList<>();
        this._sync_lock.unlock();
    }

    public void putShaderTrailData() {
        GL13.glActiveTexture(GL13.GL_TEXTURE10);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, this.getNodesTBOTex());
    }

    public float[] getColorState() {
        return this.colorState;
    }

    public void copyColorState(float[] colorState) {
        System.arraycopy(colorState, 0, this.colorState, 0, this.colorState.length);
    }

    public float[] getStartColorArray() {
        return new float[]{this.colorState[0], this.colorState[1], this.colorState[2], this.colorState[3]};
    }

    public Color getStartColorC() {
        return CommonUtil.toCommonColor(this.getStartColor());
    }

    public Vector4f getStartColor() {
        return new Vector4f(this.colorState[0], this.colorState[1], this.colorState[2], this.colorState[3]);
    }

    public float getStartColorAlpha() {
        return this.colorState[3];
    }

    public void setStartColor(@NotNull Vector4f color) {
        this.colorState[0] = color.x;
        this.colorState[1] = color.y;
        this.colorState[2] = color.z;
        this.colorState[3] = color.w;
    }

    public void setStartColor(float r, float g, float b, float a) {
        this.colorState[0] = r;
        this.colorState[1] = g;
        this.colorState[2] = b;
        this.colorState[3] = a;
    }

    public void setStartColor(Color color) {
        this.colorState[0] = color.getRed() / 255.0f;
        this.colorState[1] = color.getGreen() / 255.0f;
        this.colorState[2] = color.getBlue() / 255.0f;
        this.colorState[3] = color.getAlpha() / 255.0f;
    }

    public void setStartColorAlpha(float alpha) {
        this.colorState[3] = alpha;
    }

    public float[] getEndColorArray() {
        return new float[]{this.colorState[4], this.colorState[5], this.colorState[6], this.colorState[7]};
    }

    public Color getEndColorC() {
        return CommonUtil.toCommonColor(this.getEndColor());
    }

    public Vector4f getEndColor() {
        return new Vector4f(this.colorState[4], this.colorState[5], this.colorState[6], this.colorState[7]);
    }

    public float getEndColorAlpha() {
        return this.colorState[7];
    }

    public void setEndColor(@NotNull Vector4f color) {
        this.colorState[4] = color.x;
        this.colorState[5] = color.y;
        this.colorState[6] = color.z;
        this.colorState[7] = color.w;
    }

    public void setEndColor(float r, float g, float b, float a) {
        this.colorState[4] = r;
        this.colorState[5] = g;
        this.colorState[6] = b;
        this.colorState[7] = a;
    }

    public void setEndColor(Color color) {
        this.colorState[4] = color.getRed() / 255.0f;
        this.colorState[5] = color.getGreen() / 255.0f;
        this.colorState[6] = color.getBlue() / 255.0f;
        this.colorState[7] = color.getAlpha() / 255.0f;
    }

    public void setEndColorAlpha(float alpha) {
        this.colorState[7] = alpha;
    }

    public float[] getStartEmissiveArray() {
        return new float[]{this.colorState[8], this.colorState[9], this.colorState[10], this.colorState[11]};
    }

    public Color getStartEmissiveC() {
        return CommonUtil.toCommonColor(this.getStartEmissive());
    }

    public Vector4f getStartEmissive() {
        return new Vector4f(this.colorState[8], this.colorState[9], this.colorState[10], this.colorState[11]);
    }

    public float getStartEmissiveAlpha() {
        return this.colorState[11];
    }

    public void setStartEmissive(@NotNull Vector4f color) {
        this.colorState[8] = color.x;
        this.colorState[9] = color.y;
        this.colorState[10] = color.z;
        this.colorState[11] = color.w;
    }

    public void setStartEmissive(float r, float g, float b, float a) {
        this.colorState[8] = r;
        this.colorState[9] = g;
        this.colorState[10] = b;
        this.colorState[11] = a;
    }

    public void setStartEmissive(Color color) {
        this.colorState[8] = color.getRed() / 255.0f;
        this.colorState[9] = color.getGreen() / 255.0f;
        this.colorState[10] = color.getBlue() / 255.0f;
        this.colorState[11] = color.getAlpha() / 255.0f;
    }

    public void setStartEmissiveAlpha(float alpha) {
        this.colorState[11] = alpha;
    }

    public float[] getEndEmissiveArray() {
        return new float[]{this.colorState[12], this.colorState[13], this.colorState[14], this.colorState[15]};
    }

    public Color getEndEmissiveC() {
        return CommonUtil.toCommonColor(this.getEndEmissive());
    }

    public Vector4f getEndEmissive() {
        return new Vector4f(this.colorState[12], this.colorState[13], this.colorState[14], this.colorState[15]);
    }

    public float getEndEmissiveAlpha() {
        return this.colorState[15];
    }

    public void setEndEmissive(@NotNull Vector4f color) {
        this.colorState[12] = color.x;
        this.colorState[13] = color.y;
        this.colorState[14] = color.z;
        this.colorState[15] = color.w;
    }

    public void setEndEmissive(float r, float g, float b, float a) {
        this.colorState[12] = r;
        this.colorState[13] = g;
        this.colorState[14] = b;
        this.colorState[15] = a;
    }

    public void setEndEmissive(Color color) {
        this.colorState[12] = color.getRed() / 255.0f;
        this.colorState[13] = color.getGreen() / 255.0f;
        this.colorState[14] = color.getBlue() / 255.0f;
        this.colorState[15] = color.getAlpha() / 255.0f;
    }

    public void setEndEmissiveAlpha(float alpha) {
        this.colorState[15] = alpha;
    }

    public float getStartWidth() {
        return this.state[6] + this.state[6];
    }

    public void setStartWidth(float width) {
        this.state[6] = width * 0.5f;
    }

    public void setStartWidthDirect(float widthHalf) {
        this.state[6] = widthHalf;
    }

    public float getEndWidth() {
        return this.state[7] + this.state[7];
    }

    public void setEndWidth(float width) {
        this.state[7] = width * 0.5f;
    }

    public void setEndWidthDirect(float widthHalf) {
        this.state[7] = widthHalf;
    }

    public float getMixFactor() {
        return this.state[1];
    }

    public void setMixFactor(float factor) {
        this.state[1] = factor;
    }

    public float getTextureSpeed() {
        return this.state[10];
    }

    /**
     * It is working together with {@link CurveEntity#setTexturePixels(float)}.
     *
     * @param textureSpeed looks flowing forward when less than zero.
     */
    public void setTextureSpeed(float textureSpeed) {
        this.state[10] = textureSpeed;
    }

    public float getTexturePixels() {
        return this.state[0];
    }

    /**
     * @param texturePixels texture fill size.
     */
    public void setTexturePixels(float texturePixels) {
        this.state[0] = texturePixels;
    }

    public boolean isFlowWhenPaused() {
        return this.stateB[0];
    }

    public void setFlowWhenPaused(boolean flow) {
        this.stateB[0] = flow;
    }

    public float getUVOffset() {
        return this.state[12];
    }

    public void setUVOffset(float offset) {
        this.state[12] = offset;
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

    public float getJitterPower() {
        return this.state[8];
    }

    /**
     * perpendicular to trail, effect on trail texture uv.
     */
    public void setJitterPower(float power) {
        this.state[8] = power;
    }

    public boolean isFlick() {
        return this.stateB[2];
    }

    public void setFlick(boolean flick) {
        this.stateB[2] = flick;
    }

    public boolean isFlickWhenPaused() {
        return this.stateB[1];
    }

    public void setFlickWhenPaused(boolean flick) {
        this.stateB[1] = flick;
    }

    public float getFlickMixValue() {
        return this.state[9];
    }

    public void setFlickMixValue(float mix) {
        this.state[9] = mix;
    }

    public float getCurrentFlickerSyncValue() {
        return this.state[11];
    }

    public boolean isSyncFlick() {
        return this.stateB[3];
    }

    /**
     * For each line segments.
     */
    public void setSyncFlick(boolean syncFlick) {
        this.stateB[3] = syncFlick;
    }

    /**
     * @return <code>true</code> for default.
     */
    public boolean isStripLineMode() {
        return this.stateB[4];
    }

    /**
     * @param isStripLine <code>false</code> that draw dotted line as {@link GL11#GL_LINES} style, else {@link GL11#GL_LINE_STRIP} style.
     */
    public void setStripLineMode(boolean isStripLine) {
        this.stateB[4] = isStripLine;
    }

    /**
     * @param code java instance {@link #hashCode()} * 0.00066667f for default.
     */
    public void setFlickerSyncCode(int code) {
        this.state[11] = Math.abs(code) * 0.00066667f;
    }

    public @NotNull MaterialData getMaterialData() {
        if (this.hasDelete()) return new MaterialData();
        return this.material;
    }

    public void setMaterialData(@NotNull MaterialData material) {
        this.material = material == null ? new MaterialData() : material;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.material.getState(), 0, 12);
        this._statePackageBuffer.put(13, this.glPrimCount());
        this._statePackageBuffer.put(14, this.state, 0, 9);
        this._statePackageBuffer.put(23, this.isFlick() ? this.getFlickMixValue() : -10.0f);
        this._statePackageBuffer.put(24, this.colorState); // 24
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.TRAIL;
    }
}
