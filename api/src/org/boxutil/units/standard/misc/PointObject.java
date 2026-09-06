package org.boxutil.units.standard.misc;

import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.util.CommonUtil;

/**
 * Vertices: vec2(0.0)<p>
 * Required {@link GLWrapper.VAO#valid()}, required <{@link GLWrapper.Drawcall#valid_InstancedDraw()} for instanced draw.
 */
public class PointObject implements SimpleVAOAPI {
    public final static byte VERTICES_COUNT = 1;
    public final static byte[] VERTICES = new byte[]{0, 0};
    protected final int _pointID;
    protected final int _pointVBO;
    protected boolean isValid = false;

    public PointObject() {
        if (!GLWrapper.VAO.valid()) {
            this._pointID = 0;
            this._pointVBO = 0;
            return;
        }

        this._pointID = GLWrapper.VAO.glGenVertexArrays();
        GLWrapper.VAO.glBindVertexArray(this._pointID);

        this._pointVBO = GLWrapper.Buffer.VBO.glGenBuffers();
        GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, this._pointVBO);
        GLWrapper.Buffer.VBO.glBufferData(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, CommonUtil.createByteBuffer(VERTICES), GLWrapper.Buffer.VBO.GL_STATIC_DRAW);

        GLWrapper.VAO.glVertexAttribPointer(0, 2, GLWrapper.DataType.GL_BYTE, true, BoxDatabase.BYTE_SIZE * 2, 0); // v
        GLWrapper.VAO.glEnableVertexAttribArray(0);
        GLWrapper.VAO.glBindVertexArray(0);
        GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        if (this._pointID > 0 && this._pointVBO > 0) this.isValid = true;
    }

    public void destroy() {
        if (GLWrapper.VAO.valid()) {
            GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
            GLWrapper.VAO.glBindVertexArray(0);
            if (this._pointVBO != 0) GLWrapper.Buffer.VBO.glDeleteBuffers(this._pointVBO);
            if (this._pointID != 0) GLWrapper.VAO.glDeleteVertexArrays(this._pointID);
            this.isValid = false;
        }
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void glDraw() {
        if (!this.isValid) return;
        GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_POINTS, 0, VERTICES_COUNT);
    }

    public void glDraw(int primCount) {
        if (!this.isValid) return;
        GLWrapper.Drawcall.glDrawArraysInstanced(GLWrapper.Drawcall.GL_POINTS, 0, VERTICES_COUNT, Math.max(primCount, 1));
    }

    public int getVAO() {
        return this._pointID;
    }

    public int getVBO() {
        return this._pointVBO;
    }
}
