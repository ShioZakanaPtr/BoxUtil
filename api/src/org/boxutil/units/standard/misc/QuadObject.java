package org.boxutil.units.standard.misc;

import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.*;

/**
 * Vertices: vec2(-1.0), vec2(1.0, -1.0), vec2(-1.0, 1.0), vec2(1.0)<p>
 * Required <b>OpenGL 3.0</b> supported, required <b>OpenGL 3.1</b> for instanced draw.
 */
public class QuadObject implements SimpleVAOAPI {
    public final static byte VERTICES_COUNT = 4;
    public final static byte[] VERTICES = new byte[]{-128, -128, 127, -128, -128, 127, 127, 127};
    protected final int _quadID;
    protected final int _quadVBO;
    protected boolean isValid = false;

    public QuadObject() {
        if (!GLWrapper.VAO.valid()) {
            this._quadID = 0;
            this._quadVBO = 0;
            return;
        }

        this._quadID = GLWrapper.VAO.glGenVertexArrays();
        GLWrapper.VAO.glBindVertexArray(this._quadID);

        this._quadVBO = GLWrapper.Buffer.VBO.glGenBuffers();
        GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, this._quadVBO);
        GLWrapper.Buffer.VBO.glBufferData(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, CommonUtil.createByteBuffer(VERTICES), GLWrapper.Buffer.VBO.GL_STATIC_DRAW);

        GLWrapper.VAO.glVertexAttribPointer(0, 2, GLWrapper.DataType.GL_BYTE, true, BoxDatabase.BYTE_SIZE * 2, 0); // v
        GLWrapper.VAO.glEnableVertexAttribArray(0);
        GLWrapper.VAO.glBindVertexArray(0);
        GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        if (this._quadID > 0 && this._quadVBO > 0) this.isValid = true;
    }

    public void destroy() {
        if (GLWrapper.VAO.valid()) {
            GLWrapper.Buffer.VBO.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
            GLWrapper.VAO.glBindVertexArray(0);
            if (this._quadVBO != 0) GLWrapper.Buffer.VBO.glDeleteBuffers(this._quadVBO);
            if (this._quadID != 0) GLWrapper.VAO.glDeleteVertexArrays(this._quadID);
            this.isValid = false;
        }
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void glDraw() {
        if (!this.isValid) return;
        GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, VERTICES_COUNT);
    }

    public void glDraw(int primCount) {
        if (!this.isValid) return;
        GLWrapper.Drawcall.glDrawArraysInstanced(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, VERTICES_COUNT, Math.max(primCount, 1));
    }

    public int getVAO() {
        return this._quadID;
    }

    public int getVBO() {
        return this._quadVBO;
    }
}
