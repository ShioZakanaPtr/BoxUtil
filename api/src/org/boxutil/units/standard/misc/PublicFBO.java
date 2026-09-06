package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.Global;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Above <strong>55.37MB</strong> cost in vRAM at <strong>1920x1080</strong>.<p>
 * Will not be created at default, call {@link ShaderCore#tryPublicFBO()} and check it is valid.<p>
 * BoxUtil will not use this FBO, you can use it for your own purpose.
 */
public class PublicFBO {
    private final static byte _BUFFER_TEX_COUNT = 4;
    private final int FBO;
    private int RBO = 0;
    private final int[] texID = new int[_BUFFER_TEX_COUNT];
    private boolean finished = false;

    public PublicFBO() {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        IntBuffer ids;
        int state;

        int instance = this.hashCode();
        if (!GLWrapper.FBO.valid_BoxUtilBase() || !GLWrapper.Texture.valid_TexFloat()) {
            Global.getLogger(ShaderCore.class).error("'BoxUtil' public framebuffer \"" + instance + "\" create failed: OpenGL Context unsupported.");
            this.FBO = 0;
            return;
        }

        this.FBO = GLWrapper.FBO.glGenFramebuffers();
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, this.FBO);

        ids = BufferUtils.createIntBuffer(_BUFFER_TEX_COUNT).clear();
        GLWrapper.Texture.glGenTextures(ids);
        for (byte i = 0; i < _BUFFER_TEX_COUNT; ++i) {
            this.texID[i] = ids.get(i);
            final boolean floatTex = i == 3;
            final int att = GLWrapper.FBO.GL_COLOR_ATTACHMENT0 + i;
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.texID[i]);
            if (GLWrapper.Texture.valid_TexStorage()) {
                GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, floatTex ? GLWrapper.Texture.GL_RGBA32F : GLWrapper.Texture.GL_RGBA8, width, height);
            } else {
                GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, floatTex ? GLWrapper.Texture.GL_RGBA32F : GLWrapper.Texture.GL_RGBA8, width, height, 0, GLWrapper.Texture.GL_RGBA, floatTex ? GLWrapper.DataType.GL_FLOAT : GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            }
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
            GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, att, GLWrapper.Texture.GL_TEXTURE_2D, this.texID[i], 0);
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        this.RBO = GLWrapper.FBO.glGenRenderbuffers();
        GLWrapper.FBO.glBindRenderbuffer(GLWrapper.FBO.GL_RENDERBUFFER, this.RBO);
        GLWrapper.FBO.glRenderbufferStorage(GLWrapper.FBO.GL_RENDERBUFFER, GLWrapper.FBO.GL_DEPTH24_STENCIL8, width, height);
        GLWrapper.FBO.glFramebufferRenderbuffer(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_DEPTH_STENCIL_ATTACHMENT, GLWrapper.FBO.GL_RENDERBUFFER, this.RBO);
        GLWrapper.FBO.glBindRenderbuffer(GLWrapper.FBO.GL_RENDERBUFFER, 0);

        GLWrapper.FBO.glDrawBuffers(CommonUtil.createIntBuffer(GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.FBO.GL_COLOR_ATTACHMENT1, GLWrapper.FBO.GL_COLOR_ATTACHMENT2, GLWrapper.FBO.GL_COLOR_ATTACHMENT3));

        state = GLWrapper.FBO.glCheckFramebufferStatus(GLWrapper.FBO.GL_FRAMEBUFFER);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
        if (state == GLWrapper.FBO.GL_FRAMEBUFFER_COMPLETE) {
            Global.getLogger(ShaderCore.class).info("'BoxUtil' public framebuffer \"" + instance + "\" has created.");
            this.finished = true;
        } else {
            this.delete();
            Global.getLogger(ShaderCore.class).error("'BoxUtil' public framebuffer \"" + instance + "\" create failed: " + state);
        }
    }

    public void delete() {
        if (!BoxDatabase.getGLState().GL_FBO) return;
        GLWrapper.Texture.glDeleteTextures(CommonUtil.createIntBuffer(this.texID));
        if (this.RBO > 0) GLWrapper.FBO.glDeleteRenderbuffers(this.RBO);
        GLWrapper.FBO.glDeleteFramebuffers(this.FBO);
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public int getFBO() {
        return this.FBO;
    }

    public int getRBO() {
        return this.RBO;
    }

    public int[] getResultTex() {
        return this.texID;
    }
}
