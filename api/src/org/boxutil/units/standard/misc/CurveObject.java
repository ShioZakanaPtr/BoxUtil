package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.units.standard.attribute.NodeData;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.CurveUtil;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Easy way for rendering cubic bezier with width and texture anywhere.<p>
 * Emissive color of nodes is not supported.<p>
 * Required <code>OpenGL 1.5</code> supported.
 */
public class CurveObject {
    public final static byte STRIDE = 5 * BoxDatabase.FLOAT_SIZE;
    public final static byte UV_OFFSET = 2 * BoxDatabase.FLOAT_SIZE;
    public final static byte COLOR_OFFSET = 4 * BoxDatabase.FLOAT_SIZE;
    protected final int _curveID;
    protected final byte interpolation;
    protected final short interpolationPOne;
    protected final int vertexCount;
    protected final int bufferSize;
    protected final SpinLock _sync_lock = new SpinLock();
    protected float uvScale = 1.0f;
    protected volatile boolean isValid = false;
    protected NodeData[] nodes = new NodeData[2];
    protected FloatBuffer buffer = null;
    protected SpriteAPI sprite = BoxDatabase.BUtil_ONE;
    protected int glTex = 0;

    private CurveObject() {
        this._curveID = 0;
        this.interpolation = 0;
        this.interpolationPOne = 1;
        this.vertexCount = 0;
        this.bufferSize = 0;
    }

    public CurveObject(NodeData start, NodeData end, byte interpolation, int usage) {
        this._curveID = BoxDatabase.getGLState().GL_VBO ? GL15.glGenBuffers() : 0;
        this.isValid = this.getCurveID() > 0;
        this.nodes[0] = start;
        this.nodes[1] = end;
        this.interpolation = (byte) Math.max(Math.min(interpolation, BoxConfigs.getMaxCurveInterpolation()), 0);
        this.interpolationPOne = (short) (this.interpolation + 1);
        this.vertexCount = this.interpolation * 2 + 4;
        int vertexSize = this.vertexCount * 5;
        this.bufferSize = vertexSize * BoxDatabase.FLOAT_SIZE;
        if (!this.isValid()) {
            this.buffer = BufferUtils.createFloatBuffer(vertexSize);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getCurveID());
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.bufferSize, usage);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
    }

    public CurveObject(NodeData start, NodeData end, byte interpolation) {
        this(start, end, interpolation, GL15.GL_DYNAMIC_DRAW);
    }

    public CurveObject(NodeData start, NodeData end) {
        this(start, end, (byte) 32);
    }

    public int getCurveID() {
        return this._curveID;
    }

    public boolean isValid() {
        return this.isValid;
    }

    /**
     * <strong>!!! Must be call it if NOT REQUIRED !!!</strong>
     */
    public void destroy() {
        this.isValid = false;
        this._sync_lock.lock();
        if (this.getCurveID() > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glDeleteBuffers(this.getCurveID());
        }
        this._sync_lock.unlock();
    }

    /**
     * General rendering method.
     */
    public void renderAtStart(Vector2f offset, float facing, boolean withTexture, boolean isAdditiveBlend) {
        if (!this.isValid()) return;
        GL11.glPushMatrix();
        GL11.glTranslatef(offset.x, offset.y, 0.0f);
        if (facing != 0.0f) GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (withTexture) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glTex);
        } else GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.glDraw();
        if (withTexture) GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glPopMatrix();
    }

    public void glDraw() {
        if (!this.isValid()) return;
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getCurveID());
        GL11.glVertexPointer(2, GL11.GL_FLOAT, STRIDE, 0);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, STRIDE, UV_OFFSET);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, STRIDE, COLOR_OFFSET);
        this._sync_lock.lock();
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, this.vertexCount);
        this._sync_lock.unlock();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glPopClientAttrib();
    }

    private void putPart(Vector2f currPoint, Vector2f currTangent, float uv, float currWidth, byte[] currColor) {
        currTangent.scale(1.0f / currTangent.length());
        currTangent.set(-currTangent.y, currTangent.x);
        currTangent.scale(currWidth);
        this.buffer.put(currPoint.x + currTangent.x);
        this.buffer.put(currPoint.y + currTangent.y);
        this.buffer.put(uv);
        this.buffer.put(1.0f);
        CommonUtil.putPackingBytes(this.buffer, currColor[3], currColor[2], currColor[1], currColor[0]);
        this.buffer.put(currPoint.x - currTangent.x);
        this.buffer.put(currPoint.y - currTangent.y);
        this.buffer.put(uv);
        this.buffer.put(0.0f);
        CommonUtil.putPackingBytes(this.buffer, currColor[3], currColor[2], currColor[1], currColor[0]);
    }

    /**
     * Must be called before rendering.
     * @param elapsedTime {@link CombatEngineAPI#getTotalElapsedTime(boolean)} etc.
     */
    public void submitNode(float elapsedTime) {
        if (!this.isValid()) return;
        this._sync_lock.lock();
        this.buffer.position(0);
        Vector2f currPoint, currTangent;
        Color currColorC;
        byte[] currColor;
        float t, tPow, currWidth;
        currColor = this.nodes[0].getColorArray();
        currWidth = this.nodes[0].getWidth();
        currPoint = this.nodes[0].getLocation();
        currTangent = CurveUtil.getCurveDerivative(this.nodes[0], this.nodes[1], 0.0f);
        this.putPart(currPoint, currTangent, elapsedTime, currWidth, currColor);

        for (short i = 1; i < this.interpolationPOne; ++i) {
            t = (float) i / this.interpolationPOne;
            tPow = (float) Math.pow(t, this.nodes[0].getMixFactor());
            currColorC = CalculateUtil.mix(this.nodes[0].getColorC(), this.nodes[1].getColorC(), true, tPow);
            currColor[0] = (byte) currColorC.getRed();
            currColor[1] = (byte) currColorC.getGreen();
            currColor[2] = (byte) currColorC.getBlue();
            currColor[3] = (byte) currColorC.getAlpha();
            currWidth = CalculateUtil.mix(this.nodes[0].getWidth(), this.nodes[1].getWidth(), tPow);
            currPoint = CurveUtil.getPointOnCurve(this.nodes[0], this.nodes[1], t);
            currTangent = CurveUtil.getCurveDerivative(this.nodes[0], this.nodes[1], t);
            this.putPart(currPoint, currTangent, t * this.getUvScale() + elapsedTime, currWidth, currColor);
        }

        currColor = this.nodes[1].getColorArray();
        currWidth = this.nodes[1].getWidth();
        currPoint = this.nodes[1].getLocation();
        currTangent = CurveUtil.getCurveDerivative(this.nodes[0], this.nodes[1], 1.0f);
        this.putPart(currPoint, currTangent, this.getUvScale() + elapsedTime, currWidth, currColor);

        this.buffer.position(0);
        this.buffer.limit(this.buffer.capacity());
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getCurveID());
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, this.buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this._sync_lock.unlock();
    }

    public NodeData getStartNode() {
        return this.nodes[0];
    }

    public void setStartNode(NodeData start) {
        this._sync_lock.lock();
        this.nodes[0] = start;
        this._sync_lock.unlock();
    }

    public NodeData getEndNode() {
        return this.nodes[1];
    }

    public void setEndNode(NodeData end) {
        this._sync_lock.lock();
        this.nodes[1] = end;
        this._sync_lock.unlock();
    }

    public void setNodes(NodeData start, NodeData end) {
        this._sync_lock.lock();
        this.nodes[0] = start;
        this.nodes[1] = end;
        this._sync_lock.unlock();
    }

    public NodeData[] getNodes() {
        return this.nodes;
    }

    public SpriteAPI getSprite() {
        return this.sprite;
    }

    public int getSpriteID() {
        return this.glTex;
    }

    public void setSprite(@NotNull SpriteAPI sprite) {
        this.sprite = sprite;
        this.glTex = sprite.getTextureId();
    }

    public void setSprite(int sprite) {
        this.glTex = sprite;
    }

    public float getUvScale() {
        return this.uvScale;
    }

    public void setUvScale(float scale) {
        this.uvScale = scale;
    }
}
