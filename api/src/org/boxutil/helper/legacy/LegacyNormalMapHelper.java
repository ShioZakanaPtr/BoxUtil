package org.boxutil.helper.legacy;

import org.boxutil.base.BaseShaderData;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

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
    protected static _FuncPtr _FUNC_PTR = null;
    protected static _Func42Ptr _FUNC42_PTR = null;
    private static boolean _PTR_INIT = false;

    protected final int _FBO;
    protected final int _VBO;
    protected final FloatBuffer _programUpdate = BufferUtils.createFloatBuffer(9);
    protected final BaseShaderData _programBlur;
    protected final BaseShaderData _programResult;
    protected final int _lastSwapTex;
    protected boolean _valid;

    protected int _lastSrcTex = 0;
    protected int _lastGenTex = 0;
    protected int _currWidth = 0;
    protected int _currHeight = 0;
    protected float _currLocalU_Div = 1.0f;
    protected float _currLocalV_Div = 1.0f;
    protected boolean _notBlur = false;
    protected boolean _textureStorage = false;
    protected boolean _genMipmapAfter = false;

    protected interface _FuncPtr {
        int glGenFramebuffers();

        void glBindFramebuffer(int framebuffer);

        void glFramebufferTexture2D(int texture);

        void glDeleteFramebuffers(int framebuffer);

        void glGenerateMipmap();

        void glDrawBuffers();
    }

    protected interface _Func42Ptr {
        void glTexStorage2D(int levels, int width, int height);
    }

    public LegacyNormalMapHelper() {
        this._lastSwapTex = GL11.glGenTextures();

        this._programUpdate.position(0);
        this._programUpdate.limit(this._programUpdate.capacity());

        if (this._lastSwapTex > 0) {GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastSwapTex);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
            GL11.glTexParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, CommonUtil.createFloatBuffer(0.0f, 0.0f, 0.0f, 0.0f));
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        if (!_PTR_INIT) {
            _PTR_INIT = true;
            ContextCapabilities cap = GLContext.getCapabilities();

            if (cap.OpenGL30) {
                _FUNC_PTR = new _FuncPtr() {
                    public int glGenFramebuffers() {
                        return GL30.glGenFramebuffers();
                    }

                    public void glBindFramebuffer(int framebuffer) {
                        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
                    }

                    public void glFramebufferTexture2D(int texture) {
                        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
                    }

                    public void glDeleteFramebuffers(int framebuffer) {
                        GL30.glDeleteFramebuffers(framebuffer);
                    }

                    public void glGenerateMipmap() {
                        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                    }

                    public void glDrawBuffers() {
                        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
                    }
                };
            } else if (cap.GL_ARB_framebuffer_object) {
                _FUNC_PTR = new _FuncPtr() {
                    public int glGenFramebuffers() {
                        return ARBFramebufferObject.glGenFramebuffers();
                    }

                    public void glBindFramebuffer(int framebuffer) {
                        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, framebuffer);
                    }

                    public void glFramebufferTexture2D(int texture) {
                        ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
                    }

                    public void glDeleteFramebuffers(int framebuffer) {
                        ARBFramebufferObject.glDeleteFramebuffers(framebuffer);
                    }

                    public void glGenerateMipmap() {
                        ARBFramebufferObject.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                    }

                    public void glDrawBuffers() {
                        GL20.glDrawBuffers(ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
                    }
                };
            } else if (cap.GL_EXT_framebuffer_object) {
                _FUNC_PTR = new _FuncPtr() {
                    public int glGenFramebuffers() {
                        return EXTFramebufferObject.glGenFramebuffersEXT();
                    }

                    public void glBindFramebuffer(int framebuffer) {
                        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebuffer);
                    }

                    public void glFramebufferTexture2D(int texture) {
                        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, texture, 0);
                    }

                    public void glDeleteFramebuffers(int framebuffer) {
                        EXTFramebufferObject.glDeleteFramebuffersEXT(framebuffer);
                    }

                    public void glGenerateMipmap() {
                        EXTFramebufferObject.glGenerateMipmapEXT(GL11.GL_TEXTURE_2D);
                    }

                    public void glDrawBuffers() {
                        GL20.glDrawBuffers(EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
                    }
                };
            } else _FUNC_PTR = null;

            if (cap.OpenGL42) {
                _FUNC42_PTR = (levels, width, height) -> GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, levels, GL11.GL_RGBA8, width, height);
            } else if (cap.GL_ARB_texture_storage) {
                _FUNC42_PTR = (levels, width, height) -> ARBTextureStorage.glTexStorage2D(GL11.GL_TEXTURE_2D, levels, GL11.GL_RGBA8, width, height);
            } else _FUNC42_PTR = null;
        }

        if (_FUNC_PTR != null) {
            this._FBO = _FUNC_PTR.glGenFramebuffers();
        } else this._FBO = 0;

        if (this._FBO > 0) {
            _FUNC_PTR.glBindFramebuffer(this._FBO);
            _FUNC_PTR.glDrawBuffers();
            _FUNC_PTR.glBindFramebuffer(0);

            final float[] _vertices = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
            this._VBO = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this._VBO);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, CommonUtil.createFloatBuffer(_vertices), GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        } else this._VBO = 0;

        this._programBlur = ShaderCore.getLegacyNormalGenBlurProgram();
        this._programResult = ShaderCore.getLegacyNormalGenResultProgram();

        this._valid = this._lastSwapTex > 0 && this._FBO > 0 && this._VBO > 0 && ShaderCore.isLegacyNormalMapGenValid();
    }

    public void destroy() {
        if (this._FBO > 0) {
            _FUNC_PTR.glBindFramebuffer(this._FBO);
            _FUNC_PTR.glFramebufferTexture2D(0);
            _FUNC_PTR.glBindFramebuffer(0);
            _FUNC_PTR.glDeleteFramebuffers(this._FBO);
        }
        if (this._lastSwapTex > 0) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//            GL11.glDeleteTextures(this._lastSwapTex);
        }
        this._valid = false;
    }

    public boolean isValid() {
        return this._valid;
    }

    public void glBeginProcess() {
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_ENABLE_BIT | GL11.GL_POLYGON_BIT);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColorMask(true, true, true, true);
        _FUNC_PTR.glBindFramebuffer(this._FBO);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this._VBO);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
    }

    /**
     * <strong>Note: without "bilateral filter", "volume", "details"</strong>
     */
    public void glPutParameter(ShaderUtil.NormalMapGenParam param) {
        this._notBlur = param.srcBlurStep == 1;
        this._textureStorage = _FUNC42_PTR != null && param.useTextureStorage;
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
        this._programUpdate.position(0);
        this._programUpdate.limit(9);

        this._programBlur.active();
        GL20.glUniform3(this._programBlur.location[0], this._programUpdate);
        if (this._notBlur) GL20.glUniform1i(this._programBlur.location[2], 1);
        this._programResult.active();
        GL20.glUniform2f(this._programResult.location[0], param.normalStrength, param.keepSrcAlpha ? 1.0f : -1.0f);
        this._programResult.close();
    }

    /**
     * @param alignPOT force true when NPOT texture was not supported.
     */
    public boolean glPutSourceTexture(int texture, int localWidth, int localHeight, int resultWidth, int resultHeight, boolean alignPOT, boolean tryMipmap) {
        if (texture < 1 || resultWidth < 1 || resultHeight < 1) return false;
        this._lastSrcTex = texture;
        this._lastGenTex = GL11.glGenTextures();
        if (this._lastGenTex < 1) return false;
        final boolean forcePOT = alignPOT || !BoxDatabase.getGLState().GL_NPOT_TEXTURE;
        if (forcePOT) {
            resultWidth = CalculateUtil.getPOTMax(localWidth);
            resultHeight = CalculateUtil.getPOTMax(localHeight);
        }
        this._currLocalU_Div = (float) resultWidth / localWidth;
        this._currLocalV_Div = (float) resultHeight / localHeight;
        this._currWidth = resultWidth;
        this._currHeight = resultHeight;
        this._genMipmapAfter = tryMipmap && forcePOT;
        final byte levels = this._genMipmapAfter ? CalculateUtil.getExponentPOTMin(Math.min(this._currWidth, this._currHeight)) : 1;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastGenTex);
        if (this._textureStorage) _FUNC42_PTR.glTexStorage2D(levels, resultWidth, resultHeight);
        else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, resultWidth, resultHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, levels > 1 ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        if (levels > 1) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, levels - 1);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, levels - 1);
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastSwapTex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA16, resultWidth, resultHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_SHORT, (ByteBuffer) null); // for better ramp additional
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return true;
    }

    public int glGenerateMap() {
        GL11.glViewport(0, 0, this._currWidth, this._currHeight);
        final float stepUVX = 1.0f / (this._currWidth - 1), stepUVY = 1.0f / (this._currHeight - 1);

        { // init and blur
            this._programBlur.active();
            if (this._notBlur) {
                _FUNC_PTR.glFramebufferTexture2D(this._lastSwapTex);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastSrcTex);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
            } else {
                _FUNC_PTR.glFramebufferTexture2D(this._lastGenTex);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastSrcTex);
                GL20.glUniform4f(this._programBlur.location[1], stepUVX, stepUVY, this._currLocalU_Div, this._currLocalV_Div);
                GL20.glUniform1i(this._programBlur.location[2], 0);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

                _FUNC_PTR.glFramebufferTexture2D(this._lastSwapTex);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastGenTex);
                GL20.glUniform1i(this._programBlur.location[2], 1);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
            }
        }

        { // result
            _FUNC_PTR.glFramebufferTexture2D(this._lastGenTex);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastSwapTex);
            this._programResult.active();
            GL20.glUniform2f(this._programResult.location[1], stepUVX, stepUVY);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
            this._programResult.close();
        }

        _FUNC_PTR.glFramebufferTexture2D(0);
        if (this._genMipmapAfter) {
            this._genMipmapAfter = false;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._lastGenTex);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            _FUNC_PTR.glGenerateMipmap();
        }
        return this._lastGenTex;
    }

    public void glEndProcess() {
        _FUNC_PTR.glFramebufferTexture2D(0);
        _FUNC_PTR.glBindFramebuffer(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glPopAttrib();
        GL11.glPopClientAttrib();
        this._lastSrcTex = 0;
    }
}
