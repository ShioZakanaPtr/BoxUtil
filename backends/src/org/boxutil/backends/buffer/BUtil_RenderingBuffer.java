package org.boxutil.backends.buffer;

import com.fs.starfarer.api.Global;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BUtil_RenderingBuffer {
    private final static byte _SCALE_LAYERS = 8;
    private final static byte _BUFFER_COUNT = 2;
    private final int[] FBO = new int[_BUFFER_COUNT];
    private int RBO = 0;
    private final int[][] texID = new int[][]{new int[7], new int[1]};
    private final int[] bloomPingPongTex = new int[_SCALE_LAYERS];
    private final boolean[] finished = new boolean[_BUFFER_COUNT];
    private final int[][] scaleSize = new int[_SCALE_LAYERS][2];
    private final float[] scaleFactor = new float[_SCALE_LAYERS];
    private final float[][] scaleUV = new float[_SCALE_LAYERS][2];
    private final IntBuffer[] allDrawBuffer = new IntBuffer[]{BufferUtils.createIntBuffer(7).clear(), BufferUtils.createIntBuffer(1).clear()};
    private byte currLayerCount = 0;

    public BUtil_RenderingBuffer() {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        final FloatBuffer borderColor = CommonUtil.createFloatBuffer(0.0f, 0.0f, 0.0f, 0.0f);
        IntBuffer ids;
        int state;

        for (byte i = 0; i < _SCALE_LAYERS; ++i) {
            this.scaleFactor[i] = 1 << i;
            this.scaleSize[i][0] = (int) Math.ceil((float) width / this.scaleFactor[i]);
            this.scaleSize[i][1] = (int) Math.ceil((float) height / this.scaleFactor[i]);
            this.scaleUV[i][0] = (float) this.scaleSize[i][0] / width;
            this.scaleUV[i][1] = (float) this.scaleSize[i][1] / width;
            this.currLayerCount = i;
            if (this.scaleSize[i][0] < 16 || this.scaleSize[i][1] < 16) break;
        }
        ++this.currLayerCount;

        if (!GLWrapper.FBO.valid_BoxUtilBase() || !GLWrapper.Texture.valid_RGB10_A2UI() || !GLWrapper.Texture.valid_TexSnorm() || !GLWrapper.Texture.valid_TexStorage() || !GLWrapper.Texture.valid_BorderClamp()) {
            Global.getLogger(ShaderCore.class).error("'BoxUtil' rendering framebuffers create failed: OpenGL Context unsupported.");
            return;
        }

        final int[][] internalFormat = new int[][]{null, null};
        // color, emissive, pos, normal, tangent, material, data
        internalFormat[0] = new int[]{GLWrapper.Texture.GL_RGB8, GLWrapper.Texture.GL_RGB8, GLWrapper.Texture.GL_RGB16, GLWrapper.Texture.GL_RGB16_SNORM, GLWrapper.Texture.GL_RGB16_SNORM, GLWrapper.Texture.GL_RGB8, GLWrapper.Texture.GL_RGB10_A2UI};
        // blitEmissive
        internalFormat[1] = new int[]{GLWrapper.Texture.GL_RGB10_A2};

        for (byte i = 1; i < this.currLayerCount; ++i) {
            this.bloomPingPongTex[i] = GLWrapper.Texture.glGenTextures();
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.bloomPingPongTex[i]);
            GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, internalFormat[1][0], this.scaleSize[i][0], this.scaleSize[i][1]);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_BORDER);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_BORDER);
            GLWrapper.Texture.glTexParameter(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_BORDER_COLOR, borderColor);
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        for (byte f = 0; f < _BUFFER_COUNT; ++f) {
            this.FBO[f] = GLWrapper.FBO.glGenFramebuffers();
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, this.FBO[f]);

            ids = BufferUtils.createIntBuffer(internalFormat[f].length);
            GLWrapper.Texture.glGenTextures(ids);
            for (byte i = 0; i < internalFormat[f].length; ++i) {
                this.texID[f][i] = ids.get(i);
                if (f == 1 && i == 0) this.bloomPingPongTex[i] = this.texID[f][i];
                int att = GLWrapper.FBO.GL_COLOR_ATTACHMENT0 + i;
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this.texID[f][i]);
                GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, 1, internalFormat[f][i], width, height);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
                if (f == 1 && i == 0) {
                    GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_BORDER);
                    GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_BORDER);
                    GLWrapper.Texture.glTexParameter(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_BORDER_COLOR, borderColor);
                } else {
                    GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
                    GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
                }

                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, att, GLWrapper.Texture.GL_TEXTURE_2D, this.texID[f][i], 0);
                this.allDrawBuffer[f].put(i, att);
            }
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

            if (f == 0) {
                this.RBO = GLWrapper.FBO.glGenRenderbuffers();
                GLWrapper.FBO.glBindRenderbuffer(GLWrapper.FBO.GL_RENDERBUFFER, this.RBO);
                GLWrapper.FBO.glRenderbufferStorage(GLWrapper.FBO.GL_RENDERBUFFER, GLWrapper.FBO.GL_DEPTH_COMPONENT16, width, height);
                GLWrapper.FBO.glFramebufferRenderbuffer(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_DEPTH_ATTACHMENT, GLWrapper.FBO.GL_RENDERBUFFER, this.RBO);
                GLWrapper.FBO.glBindRenderbuffer(GLWrapper.FBO.GL_RENDERBUFFER, 0);
            }

            GLWrapper.FBO.glDrawBuffers(this.allDrawBuffer[f]);

            state = GLWrapper.FBO.glCheckFramebufferStatus(GLWrapper.FBO.GL_FRAMEBUFFER);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
            if (state == GLWrapper.FBO.GL_FRAMEBUFFER_COMPLETE) {
                Global.getLogger(ShaderCore.class).info("'BoxUtil' rendering framebuffer-" + f + " has created.");
                this.finished[f] = true;
            } else {
                this.delete(f);
                Global.getLogger(ShaderCore.class).error("'BoxUtil' rendering framebuffer-" + f + " create failed: " + state);
            }
        }
        if (!this.isFinished(0) || !this.isFinished(1)) this.deleteBloomPingPongTex();
    }

    public static byte getBufferCount() {
        return _BUFFER_COUNT;
    }

    public static byte getAttachmentCount(boolean isAux) {
        return (byte) (isAux ? 1 : 7);
    }

    public byte getLayerCount() {
        return this.currLayerCount;
    }

    public int getBloomPingPongTex(byte lod) {
        return this.bloomPingPongTex[lod];
    }

    public void delete(int index) {
        if (!GLWrapper.FBO.valid_BoxUtilBase()) return;
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
        GLWrapper.Texture.glDeleteTextures(CommonUtil.createIntBuffer(this.texID[index]));
        if (index == 0) {
            this.deleteBloomPingPongTex();
            if (this.RBO > 0) GLWrapper.FBO.glDeleteRenderbuffers(this.RBO);
        }
        if (this.FBO[index] > 0) GLWrapper.FBO.glDeleteFramebuffers(this.FBO[index]);
        this.finished[index] = false;
    }

    public void deleteBloomPingPongTex() {
        GLWrapper.Texture.glDeleteTextures(CommonUtil.createIntBuffer(this.bloomPingPongTex));
    }

    public boolean[] isFinished() {
        return this.finished;
    }

    public boolean isFinished(int index) {
        return this.finished[index];
    }

    public IntBuffer getDrawBufferConfig(byte index) {
        return this.allDrawBuffer[index];
    }

    public int[] getFBOs() {
        return this.FBO;
    }

    public int getFBO(int index) {
        return this.FBO[index];
    }

    public int getRBO() {
        return this.RBO;
    }

    public int[] getScaleSize(int level) {
        return this.scaleSize[level];
    }

    public float[] getScaleUV(int level) {
        return this.scaleUV[level];
    }

    public float getScaleFactor(int level) {
        return this.scaleFactor[level];
    }

    public int[][] getResultTex() {
        return this.texID;
    }

    public int[] getResultTex(int index) {
        return this.texID[index];
    }

    public int getColorResult() {
        return this.texID[0][0];
    }

    public int getAuxEmissiveResult() {
        return this.texID[1][0];
    }

    public int getEmissiveResult() {
        return this.texID[0][1];
    }

    public int getWorldPosResult() {
        return this.texID[0][2];
    }

    public int getNormalResult() {
        return this.texID[0][3];
    }

    public int getTangentResult() {
        return this.texID[0][4];
    }

    public int getMaterialResult() {
        return this.texID[0][5];
    }

    public int getDataResult() {
        return this.texID[0][6];
    }

    public void setScaleViewport(int level) {
        GLWrapper.Operation.glViewport(0, 0, this.scaleSize[level][0], this.scaleSize[level][1]);
    }
}
