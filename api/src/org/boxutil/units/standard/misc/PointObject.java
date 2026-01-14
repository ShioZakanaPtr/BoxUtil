package org.boxutil.units.standard.misc;

import org.boxutil.base.api.SimpleVAOAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.util.CommonUtil;
import org.lwjgl.opengl.*;

/**
 * Vertices: vec2(0.0)
 */
public class PointObject implements SimpleVAOAPI {
    public final static byte VERTICES_COUNT = 1;
    public final static byte[] VERTICES = new byte[]{0, 0};
    protected final int _pointID;
    protected final int _pointVBO;
    protected boolean isValid = false;

    public PointObject() {
        if (!BoxConfigs.isVAOSupported()) {
            this._pointID = 0;
            this._pointVBO = 0;
            return;
        }

        this._pointID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this._pointID);

        this._pointVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this._pointVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, CommonUtil.createByteBuffer(VERTICES), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_BYTE, true, BoxDatabase.BYTE_SIZE * 2, 0); // v
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        if (this._pointID > 0 && this._pointVBO > 0) this.isValid = true;
    }

    public void destroy() {
        if (BoxConfigs.isVAOSupported()) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
            if (this._pointVBO != 0) GL15.glDeleteBuffers(this._pointVBO);
            if (this._pointID != 0) GL30.glDeleteVertexArrays(this._pointID);
            this.isValid = false;
        }
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void glDraw() {
        if (!this.isValid) return;
        GL11.glDrawArrays(GL11.GL_POINTS, 0, VERTICES_COUNT);
    }

    public void glDraw(int primCount) {
        if (!this.isValid) return;
        GL31.glDrawArraysInstanced(GL11.GL_POINTS, 0, VERTICES_COUNT, Math.max(primCount, 1));
    }

    public int getVAO() {
        return this._pointID;
    }

    public int getVBO() {
        return this._pointVBO;
    }

    public void glBind() {
        GL30.glBindVertexArray(this._pointID);
    }

    public void glReleaseBind() {
        GL30.glBindVertexArray(0);
    }
}
