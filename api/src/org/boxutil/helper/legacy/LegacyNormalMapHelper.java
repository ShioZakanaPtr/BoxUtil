package org.boxutil.helper.legacy;

import org.boxutil.base.BaseShaderData;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * For example:
 * <pre>
 * {@code
 * AnyStruct[] todoTex; // {int texID; int texWidth; int texHeight;};
 * List<Integer> resultTex;
 * LegacyNormalMapHelper helper;
 * NormalMapGenParam param;
 *
 * if (helper.isValid()) {
 *     int tmp;
 *     helper.glBeginProcess();
 *     helper.glPutParameter(param); // Required and not null.
 *
 *     for (AnyStruct tex : todoTex) {
 *         if (helper.glPutSourceTexture(tex.texID, tex.texLocalWidth, tex.texLocalHeight, tex.texGLWidth, tex.texGLHeight, true, true)) {
 *             tmp = helper.glGenerateMap();
 *             if (tmp > 0) resultTex.add(tmp);
 *         }
 *     }
 *
 *     helper.glEndProcess();
 * }
 *
 * // ***
 *
 * if (helper.isValid()) helper.destroy(); // REQUIRED!!! if no need after.
 * }
 * </pre>
 */
public class LegacyNormalMapHelper {
    protected final int _FBO;
    protected final int _VBO;
    protected final FloatBuffer _programUpdate = BufferUtils.createFloatBuffer(9).clear();
    protected final BaseShaderData _programBlur;
    protected final BaseShaderData _programResult;
    protected final int _lastSwapTex;
    protected boolean _valid;

    protected int _lastSrcTex = 0;
    protected int _lastGenTex = 0;
    protected int _currWidth = 0;
    protected int _currHeight = 0;
    protected int _currLocalWidth = 0;
    protected int _currLocalHeight = 0;
    protected int _lastSwapWidth = 0;
    protected int _lastSwapHeight = 0;
    protected int _currResultWidth = 0;
    protected int _currResultHeight = 0;
    protected float _currLocalU_Div = 1.0f;
    protected float _currLocalV_Div = 1.0f;
    protected boolean _notBlur = false;
    protected boolean _textureStorage = false;
    protected boolean _genMipmapAfter = false;

    public LegacyNormalMapHelper() {
        this._lastSwapTex = GLWrapper.Texture.glGenTextures();

        if (this._lastSwapTex > 0) {GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
        }

        if (GLWrapper.FBO.valid()) {
            this._FBO = GLWrapper.FBO.glGenFramebuffers();
        } else this._FBO = 0;

        if (this._FBO > 0 && GLWrapper.Buffer.VBO.valid()) {
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, this._FBO);
            GLWrapper.FBO.glDrawBuffers(GLWrapper.FBO.GL_COLOR_ATTACHMENT0);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);

            final float[] _vertices = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
            this._VBO = GLWrapper.Buffer.glGenBuffers();
            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, this._VBO);
            GLWrapper.Buffer.glBufferData(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, CommonUtil.createFloatBuffer(_vertices), GLWrapper.Buffer.GL_STATIC_DRAW);
            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        } else this._VBO = 0;

        this._programBlur = ShaderCore.getLegacyNormalGenBlurProgram();
        this._programResult = ShaderCore.getLegacyNormalGenResultProgram();

        this._valid = this._lastSwapTex > 0 && this._FBO > 0 && this._VBO > 0 && ShaderCore.isLegacyNormalMapGenValid();
    }

    public void destroy() {
        if (this._FBO > 0) {
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, this._FBO);
            GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, 0, 0);
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
            GLWrapper.FBO.glDeleteFramebuffers(this._FBO);
        }
        if (this._lastSwapTex > 0) {
            if (GLWrapper.Drawcall.MultiTex.valid()) GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
            GLWrapper.Texture.glDeleteTextures(this._lastSwapTex);
        }
        this._valid = false;
    }

    public boolean isValid() {
        return this._valid;
    }

    public void glBeginProcess() {
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GLWrapper.Operation.glPushAttrib(GLWrapper.Operation.GL_VIEWPORT_BIT | GLWrapper.Operation.GL_ENABLE_BIT | GLWrapper.Operation.GL_POLYGON_BIT);
        GL11.glPolygonMode(GLWrapper.Operation.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_POLYGON_SMOOTH);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_DEPTH_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_ALPHA_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_STENCIL_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_SCISSOR_TEST);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_MULTISAMPLE);
        GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_BLEND);
        GLWrapper.Operation.glColorMask(true, true, true, true);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, this._FBO);

        if (GLWrapper.Drawcall.MultiTex.valid()) GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0);
        GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, this._VBO);
        GL11.glVertexPointer(2, GLWrapper.DataType.GL_FLOAT, 0, 0);
    }

    /**
     * <strong>Note: without "bilateral filter", "volume", "details"</strong>
     */
    public void glPutParameter(ShaderUtil.NormalMapGenParam param) {
        this._notBlur = param.srcBlurStep == 1;
        this._textureStorage = GLWrapper.Texture.valid_TexStorage() && param.useTextureStorage;
        this._programUpdate.put(0, new float[]{
                param.applyHorizontalRamp,
                param.applyVerticalRamp,
                param.srcBlurStep,

                param.srcBrightness,
                param.srcContrast,
                1.0f / (param.srcBlurStep * 0.1111111f * TrigUtil.PI_F),

                param.srcStrength,
                param.srcPowFactor,
                param.srcSmoothstepMix}, 0, 9);

        this._programBlur.active();
        GLWrapper.Shader.glUniform3(this._programBlur.location[1], this._programUpdate);
        if (this._notBlur) GLWrapper.Shader.glUniform1i(this._programBlur.location[3], 1);
        this._programResult.active();
        GLWrapper.Shader.glUniform2f(this._programResult.location[1], param.normalStrength, param.keepSrcAlpha ? 1.0f : -1.0f);
        this._programResult.close();
    }

    /**
     * Only for make the new normal map texture directly, use it with {@link LegacyNormalMapHelper#glGenerateMap()}.
     *
     * @param alignPOT will force true when NPOT texture was not supported.
     */
    public boolean glPutSourceTexture(int texture, int localWidth, int localHeight, int resultWidth, int resultHeight, boolean alignPOT, boolean tryMipmap) {
        if (texture < 1 || localWidth < 1 || localHeight < 1 || resultWidth < 1 || resultHeight < 1) return false;
        this._lastSrcTex = texture;
        this._lastGenTex = GLWrapper.Texture.glGenTextures();
        if (this._lastGenTex < 1) return false;
        final boolean forcePOT = alignPOT || !BoxDatabase.getGLState().GL_NPOT_TEXTURE;
        if (forcePOT) {
            resultWidth = CalculateUtil.getPOTMax(localWidth);
            resultHeight = CalculateUtil.getPOTMax(localHeight);
        }
        this._currLocalU_Div = (float) resultWidth / localWidth;
        this._currLocalV_Div = (float) resultHeight / localHeight;
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, texture);
        this._currWidth = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_WIDTH);
        this._currHeight = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_HEIGHT);
        this._currResultWidth = resultWidth;
        this._currResultHeight = resultHeight;
        this._currLocalWidth = localWidth;
        this._currLocalHeight = localHeight;
        this._genMipmapAfter = tryMipmap && forcePOT;
        final byte levels = this._genMipmapAfter ? CalculateUtil.getExponentPOTMin(Math.min(this._currWidth, this._currHeight)) : 1;

        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex);
        if (this._textureStorage) GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, levels, resultWidth, GLWrapper.Texture.GL_RGBA8, resultHeight);
        else GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_RGBA8, resultWidth, resultHeight, 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, levels > 1 ? GLWrapper.Texture.GL_LINEAR_MIPMAP_LINEAR : GLWrapper.Texture.GL_LINEAR);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
        if (levels > 1) {
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_LOD, 0);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_BASE_LEVEL, 0);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAX_LOD, levels - 1);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAX_LEVEL, levels - 1);
        }


        if (resultWidth > this._lastSwapWidth || resultHeight > this._lastSwapHeight) {
            this._lastSwapWidth = Math.max(resultWidth, this._lastSwapWidth);
            this._lastSwapHeight = Math.max(resultHeight, this._lastSwapHeight);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex);
            GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_RGBA16, this._lastSwapWidth, this._lastSwapHeight, 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_SHORT, (ByteBuffer) null); // for better ramp additional
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
        }
        return true;
    }

    /**
     * Only for if you hava a <code>RGBA8</code> result texture, use it with {@link LegacyNormalMapHelper#glGenerateMap()}.
     */
    public boolean glPutSourceTexture(int texture, int localWidth, int localHeight, int resultTex) {
        if (texture < 1 || localWidth < 1 || localHeight < 1 || resultTex < 1) return false;
        this._lastSrcTex = texture;
        this._genMipmapAfter = false;
        this._lastGenTex = resultTex;
        this._currLocalWidth = localWidth;
        this._currLocalHeight = localHeight;

        if (localWidth > this._lastSwapWidth || localHeight > this._lastSwapHeight) {
            this._lastSwapWidth = Math.max(localWidth, this._lastSwapWidth);
            this._lastSwapHeight = Math.max(localHeight, this._lastSwapHeight);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex);
            GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_RGBA16, this._lastSwapWidth, this._lastSwapHeight, 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_SHORT, (ByteBuffer) null); // for better ramp additional
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
        }
        return true;
    }

    /**
     * Only for make normal map and write into a existing texture, must be use it with {@link LegacyNormalMapHelper#glPutSourceTexture(int, int, int, int)}.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param sourceFullWidth the source full-texture width.
     * @param sourceFullHeight the source full-texture height.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param resultFullWidth the result full-texture width.
     * @param resultFullHeight the result full-texture height.
     *
     * @return the result texture that have been set before calls
     */
    public int glGenerateMapRegion(int sourceOffsetX, int sourceOffsetY, int sourceFullWidth, int sourceFullHeight, int resultOffsetX, int resultOffsetY, int resultFullWidth, int resultFullHeight) {
        final int dstWidth = resultFullWidth - resultOffsetX,
                dstHeight = resultFullHeight - resultOffsetY;
        final float stepUVX = 1.0f / (this._currLocalWidth - 1),
                stepUVY = 1.0f / (this._currLocalHeight - 1);

        { // init and blur
            this._programBlur.active();
            final float srcOffsetU = (float) sourceOffsetX / sourceFullWidth,
                    srcOffsetV = (float) sourceOffsetY / sourceFullHeight,
                    srcU = (float) this._currLocalWidth / sourceFullWidth,
                    srcV = (float) this._currLocalHeight / sourceFullHeight;
            GLWrapper.Shader.glUniform4f(this._programBlur.location[0], srcOffsetU, srcOffsetV, srcU, srcV);
            if (this._notBlur) {
                GLWrapper.Operation.glViewport(0, 0, this._currLocalWidth, this._currLocalHeight);
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex, 0);
                GLWrapper.FBO.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSrcTex);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
            } else {
                GLWrapper.Operation.glViewport(resultOffsetX, resultOffsetY, dstWidth, dstHeight);
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex, 0);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSrcTex);
                GLWrapper.Shader.glUniform4f(this._programBlur.location[2], stepUVX, stepUVY, 1.0f, 1.0f);
                GLWrapper.Shader.glUniform1i(this._programBlur.location[3], 0);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);

                GLWrapper.Operation.glViewport(0, 0, this._currLocalWidth, this._currLocalHeight);
                final float resultOffsetU = (float) resultOffsetX / resultFullWidth,
                        resultOffsetV = (float) resultOffsetY / resultFullHeight,
                        resultU = (float) this._currLocalWidth / resultFullWidth,
                        resultV = (float) this._currLocalHeight / resultFullHeight;
                GLWrapper.Shader.glUniform4f(this._programBlur.location[0], resultOffsetU, resultOffsetV, resultU, resultV);
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex, 0);
                GLWrapper.FBO.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex);
                GLWrapper.Shader.glUniform1i(this._programBlur.location[3], 1);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
            }
        }

        { // result
            GLWrapper.Operation.glViewport(resultOffsetX, resultOffsetY, dstWidth, dstHeight);
            final float swapU = (float) this._currLocalWidth / this._lastSwapWidth,
                    swapV = (float) this._currLocalHeight / this._lastSwapHeight;
            GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex, 0);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex);
            this._programResult.active();
            GLWrapper.Shader.glUniform2f(this._programBlur.location[0], swapU, swapV);
            GLWrapper.Shader.glUniform2f(this._programResult.location[2], stepUVX, stepUVY);
            GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
        }
        return this._lastGenTex;
    }

    /**
     * Only for make a new texture, must be use it with {@link LegacyNormalMapHelper#glPutSourceTexture(int, int, int, int, int, boolean, boolean)}.
     *
     * @return new normal map texture
     */
    public int glGenerateMap() {
        GLWrapper.Operation.glViewport(0, 0, this._currLocalWidth, this._currLocalHeight);
        final float stepUVX = 1.0f / (this._currLocalWidth - 1),
                stepUVY = 1.0f / (this._currLocalHeight - 1);

        { // init and blur
            this._programBlur.active();
            final float srcU = (float) this._currLocalWidth / this._currWidth,
                    srcV = (float) this._currLocalHeight / this._currHeight;
            GLWrapper.Shader.glUniform4f(this._programBlur.location[0], 0.0f, 0.0f, srcU, srcV);
            if (this._notBlur) {
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex, 0);
                GLWrapper.FBO.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSrcTex);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
            } else {
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex, 0);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSrcTex);
                GLWrapper.Shader.glUniform4f(this._programBlur.location[2], stepUVX, stepUVY, this._currLocalU_Div, this._currLocalV_Div);
                GLWrapper.Shader.glUniform1i(this._programBlur.location[3], 0);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);

                final float resultU = (float) this._currLocalWidth / this._currResultWidth,
                        resultV = (float) this._currLocalHeight / this._currResultHeight;
                GLWrapper.Shader.glUniform4f(this._programBlur.location[0], 0.0f, 0.0f, resultU, resultV);
                GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex, 0);
                GLWrapper.FBO.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLWrapper.FBO.glClear(GLWrapper.Operation.GL_COLOR_BUFFER_BIT);
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex);
                GLWrapper.Shader.glUniform1i(this._programBlur.location[3], 1);
                GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
            }
        }

        { // result
            final float swapU = (float) this._currLocalWidth / this._lastSwapWidth,
                    swapV = (float) this._currLocalHeight / this._lastSwapHeight;
            GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex, 0);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastSwapTex);
            this._programResult.active();
            GLWrapper.Shader.glUniform2f(this._programBlur.location[0], swapU, swapV);
            GLWrapper.Shader.glUniform2f(this._programResult.location[2], stepUVX, stepUVY);
            GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
        }

        GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, 0, 0);
        if (this._genMipmapAfter) {
            this._genMipmapAfter = false;
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, this._lastGenTex);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR_MIPMAP_LINEAR);
            GLWrapper.FBO.glGenerateMipmap(GLWrapper.Texture.GL_TEXTURE_2D);
        }
        return this._lastGenTex;
    }

    public void glEndProcess() {
        this._programResult.close();
        GLWrapper.FBO.glFramebufferTexture2D(GLWrapper.FBO.GL_FRAMEBUFFER, GLWrapper.FBO.GL_COLOR_ATTACHMENT0, GLWrapper.Texture.GL_TEXTURE_2D, 0, 0);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
        GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        GLWrapper.Operation.glPopAttrib();
        GL11.glPopClientAttrib();
        this._lastSrcTex = 0;
    }
}
