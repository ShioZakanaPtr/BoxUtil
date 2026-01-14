package org.boxutil.units.standard.misc;

import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.*;

/**
 * Vertices: vec2(-1.0), vec2(1.0, -1.0), vec2(-1.0, 1.0), vec2(1.0)
 */
public class QuadObject implements SimpleVAOAPI {
    public final static byte VERTICES_COUNT = 4;
    public final static byte[] VERTICES = new byte[]{-128, -128, 127, -128, -128, 127, 127, 127};
    protected final int _quadID;
    protected final int _quadVBO;
    protected boolean isValid = false;

    public QuadObject() {
        if (!BoxConfigs.isVAOSupported()) {
            this._quadID = 0;
            this._quadVBO = 0;
            return;
        }

        this._quadID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this._quadID);

        this._quadVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this._quadVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, CommonUtil.createByteBuffer(VERTICES), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_BYTE, true, BoxDatabase.BYTE_SIZE * 2, 0); // v
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        if (this._quadID > 0 && this._quadVBO > 0) this.isValid = true;
    }

    public void destroy() {
        if (BoxConfigs.isVAOSupported()) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
            if (this._quadVBO != 0) GL15.glDeleteBuffers(this._quadVBO);
            if (this._quadID != 0) GL30.glDeleteVertexArrays(this._quadID);
            this.isValid = false;
        }
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void glDraw() {
        if (!this.isValid) return;
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, VERTICES_COUNT);
    }

    public void glDraw(int primCount) {
        if (!this.isValid) return;
        GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, VERTICES_COUNT, Math.max(primCount, 1));
    }

    public int getVAO() {
        return this._quadID;
    }

    public int getVBO() {
        return this._quadVBO;
    }

    public void glBind() {
        GL30.glBindVertexArray(this._quadID);
    }

    public void glReleaseBind() {
        GL30.glBindVertexArray(0);
    }
}
