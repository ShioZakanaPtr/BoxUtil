package org.boxutil.backends.buffer;

import com.fs.starfarer.api.Global;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BUtil_RenderingBuffer {
    private final static int[][] _INTERNAL_FORMAT = new int[][]{null, null};
//    private final static int[][] _FORMAT = new int[][]{null, null};
//    private final static int[][] _TYPE = new int[][]{null, null};
    static {
        // color, emissive, pos, normal, tangent, material, data
        _INTERNAL_FORMAT[0] = new int[]{GL11.GL_RGB8, GL11.GL_RGB8, GL11.GL_RGB16, GL31.GL_RGB16_SNORM, GL31.GL_RGB16_SNORM, GL11.GL_RGB8, GL33.GL_RGB10_A2UI};
        // blitEmissive
        _INTERNAL_FORMAT[1] = new int[]{GL11.GL_RGB10_A2};
//        _FORMAT[0] = new int[]{GL11.GL_RGB, GL11.GL_RGB, GL11.GL_RGB, GL11.GL_RGB, GL11.GL_RGB, GL11.GL_RGB, GL30.GL_RGBA_INTEGER};
//        _FORMAT[1] = new int[]{GL11.GL_RGBA};
//        _TYPE[0] = new int[]{GL11.GL_UNSIGNED_BYTE, GL11.GL_UNSIGNED_BYTE, GL11.GL_UNSIGNED_SHORT, GL11.GL_SHORT, GL11.GL_SHORT, GL11.GL_UNSIGNED_BYTE, GL12.GL_UNSIGNED_INT_2_10_10_10_REV};
//        _TYPE[1] = new int[]{GL12.GL_UNSIGNED_INT_2_10_10_10_REV};
    }

    private final static byte _SCALE_LAYERS = 8;
    private final static byte _BUFFER_COUNT = 2;
    private final int[] FBO = new int[_BUFFER_COUNT];
    private int RBO = 0;
    private final int[][] texID = new int[][]{new int[_INTERNAL_FORMAT[0].length], new int[_INTERNAL_FORMAT[1].length]};
    private final int[] bloomPingPongTex = new int[_SCALE_LAYERS];
    private final boolean[] finished = new boolean[_BUFFER_COUNT];
    private final int[][] scaleSize = new int[_SCALE_LAYERS][2];
    private final float[] scaleFactor = new float[_SCALE_LAYERS];
    private final float[][] scaleUV = new float[_SCALE_LAYERS][2];
    private final IntBuffer[] allDrawBuffer = new IntBuffer[]{BufferUtils.createIntBuffer(_INTERNAL_FORMAT[0].length), BufferUtils.createIntBuffer(_INTERNAL_FORMAT[1].length)};
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

        for (byte i = 1; i < this.currLayerCount; ++i) {
            this.bloomPingPongTex[i] = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.bloomPingPongTex[i]);
            GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, _INTERNAL_FORMAT[1][0], this.scaleSize[i][0], this.scaleSize[i][1]);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
            borderColor.position(0);
            borderColor.limit(borderColor.capacity());
            GL11.glTexParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        if (!BoxDatabase.getGLState().GL_FBO || !BoxDatabase.getGLState().GL_TEXTURE_STORAGE) {
            Global.getLogger(ShaderCore.class).error("'BoxUtil' rendering framebuffers create failed: OpenGL Context unsupported.");
            return;
        }

        for (byte f = 0; f < _BUFFER_COUNT; ++f) {
            this.FBO[f] = GL30.glGenFramebuffers();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.FBO[f]);

            ids = BufferUtils.createIntBuffer(_INTERNAL_FORMAT[f].length);
            GL11.glGenTextures(ids);
            for (byte i = 0; i < _INTERNAL_FORMAT[f].length; ++i) {
                this.texID[f][i] = ids.get(i);
                if (f == 1 && i == 0) this.bloomPingPongTex[i] = this.texID[f][i];
                int att = GL30.GL_COLOR_ATTACHMENT0 + i;
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texID[f][i]);
                GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, _INTERNAL_FORMAT[f][i], width, height);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                if (f == 1 && i == 0) {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
                    borderColor.position(0);
                    borderColor.limit(borderColor.capacity());
                    GL11.glTexParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
                } else {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                }
                GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, att, GL11.GL_TEXTURE_2D, this.texID[f][i], 0);
                this.allDrawBuffer[f].put(i, att);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            this.allDrawBuffer[f].position(0);
            this.allDrawBuffer[f].limit(this.allDrawBuffer[f].capacity());

            if (f == 0) {
                this.RBO = GL30.glGenRenderbuffers();
                GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.RBO);
                GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT16, width, height);
                GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, this.RBO);
                GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
            }

            GL20.glDrawBuffers(this.allDrawBuffer[f]);

            state = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            if (state == GL30.GL_FRAMEBUFFER_COMPLETE) {
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

    public static byte getAttachmentCount(byte index) {
        return (byte) _INTERNAL_FORMAT[index].length;
    }

    public byte getLayerCount() {
        return this.currLayerCount;
    }

    public int getBloomPingPongTex(byte lod) {
        return this.bloomPingPongTex[lod];
    }

    public void delete(int index) {
        if (!BoxDatabase.getGLState().GL_FBO) return;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(CommonUtil.createIntBuffer(this.texID[index]));
        if (index == 0) {
            this.deleteBloomPingPongTex();
            if (this.RBO > 0) GL30.glDeleteRenderbuffers(this.RBO);
        }
        if (this.FBO[index] > 0) GL30.glDeleteFramebuffers(this.FBO[index]);
        this.finished[index] = false;
    }

    public void deleteBloomPingPongTex() {
        GL11.glDeleteTextures(CommonUtil.createIntBuffer(this.bloomPingPongTex));
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
        GL11.glViewport(0, 0, this.scaleSize[level][0], this.scaleSize[level][1]);
    }
}
