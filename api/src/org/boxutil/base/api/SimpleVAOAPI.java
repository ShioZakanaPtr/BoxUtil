package org.boxutil.base.api;

public interface SimpleVAOAPI {
    void destroy();

    boolean isValid();

    void glDraw();

    void glDraw(int primCount);

    int getVAO();

    int getVBO();

    void glBind();

    void glReleaseBind();
}
