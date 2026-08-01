package org.boxutil.backends.core.dev;

import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.TransformUtil;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class BUtil_GLDrawInfo {
    private final static BUtil_GLDrawInfo _INSTANCE = new BUtil_GLDrawInfo();

    private final List<Drawable> _drawQueue = new ArrayList<>(2);
    private final FloatBuffer matrix = TransformUtil.createWindowOrthoMatrix(false, CommonUtil.createIdentityMatrix4x4f());

    public interface Drawable {
        /**
         * @return append y-offset for next widget.
         */
        float glDraw(float totalYOffset);

        boolean isShown();
    }

    public static void addInfo(final Drawable drawable) {
        _INSTANCE._drawQueue.add(drawable);
    }

    private void glDrawInfo() {
        boolean bypass = true;
        for (final Drawable drawable : this._drawQueue) {
            if (drawable.isShown()) {
                bypass = false;
                break;
            }
        }
        if (bypass) return;

        this.matrix.position(0).limit(this.matrix.capacity());
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_POLYGON_BIT);
        GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadMatrix(this.matrix);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glTranslatef(ShaderCore.getScreenWidth(), ShaderCore.getScreenHeight(),0.0f);

        float offset, totalOffset = 0.0f;
        for (final Drawable drawable : this._drawQueue) {
            if (!drawable.isShown()) continue;
            offset = drawable.glDraw(totalOffset);
            totalOffset += offset;
            GL11.glTranslatef(0.0f, offset, 0.0f);
        }

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void showInfo() {
        _INSTANCE.glDrawInfo();
    }
}
