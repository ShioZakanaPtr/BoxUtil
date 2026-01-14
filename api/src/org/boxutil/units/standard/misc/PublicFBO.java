package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.Global;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Above <strong>55.37MB</strong> cost in vRAM at <strong>1920x1080</strong>.<p>
 * Will not be created at default, call {@link ShaderCore#refreshPublicFBO()} and check it is valid.<p>
 * BoxUtil will not use this FBO, you can use it for your own purpose.
 */
public class PublicFBO {
    private final static byte _BUFFER_TEX_COUNT = 4;
    public final static int[][] FORMAT = new int[_BUFFER_TEX_COUNT][3];
    private final int FBO;
    private int RBO = 0;
    private final int[] texID = new int[_BUFFER_TEX_COUNT];
    private boolean finished = false;

    static {
        for (byte i = 0; i < _BUFFER_TEX_COUNT; ++i) {
            if (i < _BUFFER_TEX_COUNT - 1) {
                FORMAT[i][0] = GL11.GL_RGBA8;
                FORMAT[i][1] = GL11.GL_RGBA;
                FORMAT[i][2] = GL11.GL_UNSIGNED_BYTE;
            } else {
                FORMAT[i][0] = GL30.GL_RGBA32F;
                FORMAT[i][1] = GL11.GL_RGBA;
                FORMAT[i][2] = GL11.GL_FLOAT;
            }
        }
    }

    public PublicFBO() {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        IntBuffer ids;
        int state;

        int instance = this.hashCode();
        if (!BoxDatabase.getGLState().GL_FBO) {
            Global.getLogger(ShaderCore.class).error("'BoxUtil' public framebuffer \"" + instance + "\" create failed: OpenGL Context unsupported.");
            this.FBO = 0;
            return;
        }

        this.FBO = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.FBO);

        ids = BufferUtils.createIntBuffer(_BUFFER_TEX_COUNT);
        GL11.glGenTextures(ids);
        for (byte i = 0; i < _BUFFER_TEX_COUNT; ++i) {
            this.texID[i] = ids.get(i);
            int att = GL30.GL_COLOR_ATTACHMENT0 + i;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texID[i]);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, FORMAT[i][0], width, height, 0, FORMAT[i][1], FORMAT[i][2], (ByteBuffer) null);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, att, GL11.GL_TEXTURE_2D, this.texID[i], 0);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        this.RBO = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.RBO);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, this.RBO);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);

        GL20.glDrawBuffers(CommonUtil.createIntBuffer(GL30.GL_COLOR_ATTACHMENT0, GL30.GL_COLOR_ATTACHMENT1, GL30.GL_COLOR_ATTACHMENT2, GL30.GL_COLOR_ATTACHMENT3));

        state = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        if (state == GL30.GL_FRAMEBUFFER_COMPLETE) {
            Global.getLogger(ShaderCore.class).info("'BoxUtil' public framebuffer \"" + instance + "\" has created.");
            this.finished = true;
        } else {
            this.delete();
            Global.getLogger(ShaderCore.class).error("'BoxUtil' public framebuffer \"" + instance + "\" create failed: " + state);
        }
    }

    public void delete() {
        if (!BoxDatabase.getGLState().GL_FBO) return;
        GL11.glDeleteTextures(CommonUtil.createIntBuffer(this.texID));
        if (this.RBO > 0) GL30.glDeleteRenderbuffers(this.RBO);
        GL30.glDeleteFramebuffers(this.FBO);
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
