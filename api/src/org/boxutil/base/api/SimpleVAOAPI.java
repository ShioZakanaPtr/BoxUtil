package org.boxutil.base.api;

import org.boxutil.define.GLWrapper;

public interface SimpleVAOAPI {
    void destroy();

    boolean isValid();

    void glDraw();

    void glDraw(int primCount);

    int getVAO();

    int getVBO();

    default void glBind() {
        GLWrapper.VAO.glBindVertexArray(this.getVAO());
    }

    default void glReleaseBind() {
        GLWrapper.VAO.glBindVertexArray(0);
    }
}
