package org.boxutil.base;

import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

public abstract class BaseShaderData {
    private final int id;
    public int[] location;
    public int[] structLocation;
    public int[] uboLocation;
    public int[] maxSubroutineUniformLocation;
    public int[][] subroutineLocation;
    public int[][] subroutineUniformLocation;

    public BaseShaderData(int id) {
        this.id = id;
    }

    public int getUniformIndex(String name) {
        return GLWrapper.Shader.glGetUniformLocation(this.id, name);
    }

    public int getStructUniformIndex(String name) {
        return GLWrapper.Buffer.UBO.glGetUniformBlockIndex(this.id, name);
    }

    public int getUBOIndex(String name, int bindingIndex) {
        final int index = this.getStructUniformIndex(name);;
        GLWrapper.Buffer.UBO.glUniformBlockBinding(this.id, index, bindingIndex);
        return index;
    }

    public int getSubroutineIndex(int shaderType, String name) {
        return GLWrapper.Shader.glGetSubroutineIndex(this.id, shaderType, name);
    }

    /**
     * Unneeded for usual.
     */
    public int getSubroutineUniformLocation(int shaderType, String name) {
        return GLWrapper.Shader.glGetSubroutineUniformLocation(this.id, shaderType, name);
    }

    public void active() {
        GLWrapper.Shader.glUseProgram(this.id);
    }

    public void close() {
        GLWrapper.Shader.glUseProgram(0);
    }

    public void delete() {
        if (this.id < 1) return;
        this.close();
        IntBuffer shadersBuffer = BufferUtils.createIntBuffer(16);
        GLWrapper.Shader.glGetAttachedShaders(this.id, null, shadersBuffer);
        if (shadersBuffer.hasArray()) {
            for (int shaderID : shadersBuffer.array()) {
                GLWrapper.Shader.glDetachShader(this.id, shaderID);
                GLWrapper.Shader.glDeleteShader(shaderID);
            }
        }
        GLWrapper.Shader.glDeleteProgram(this.id);
    }

    public boolean isValid() {
        return this.id > 0;
    }

    public int getId() {
        return this.id;
    }

    public void putDefaultTextureUnit(int location, int unit) {
        GLWrapper.Shader.glProgramUniform1i(this.getId(), location, unit);
    }

    public void putBindingImageTexture(int binding, int textureID, int format) {
        GLWrapper.Texture.glBindImageTexture(binding, textureID, 0, false, 0, GLWrapper.Texture.GL_READ_WRITE, format);
    }

    public void putBindingImageTextureReadOnly(int binding, int textureID, int format) {
        GLWrapper.Texture.glBindImageTexture(binding, textureID, 0, false, 0, GLWrapper.Texture.GL_READ_ONLY, format);
    }

    public void putBindingImageTextureWriteOnly(int binding, int textureID, int format) {
        GLWrapper.Texture.glBindImageTexture(binding, textureID, 0, false, 0, GLWrapper.Texture.GL_WRITE_ONLY, format);
    }

    public void bindTextureBuffer(int textureID) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Buffer.TBO.GL_TEXTURE_BUFFER, textureID);
    }

    public void putUniformTextureBuffer(int uniformIndex, int textureID) {
        this.bindTextureBuffer(textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTextureBuffer(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0 + textureUnit);
        GLWrapper.Texture.glBindTexture(GLWrapper.Buffer.TBO.GL_TEXTURE_BUFFER, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTextureBuffer(int uniformIndex, int textureUnit, int textureID) {
        this.bindTextureBuffer(textureUnit, textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture1D(int textureID) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_1D, textureID);
    }

    public void putUniformTexture1D(int uniformIndex, int textureID) {
        this.bindTexture1D(textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture1D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0 + textureUnit);
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_1D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture1D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture1D(textureUnit, textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture2D(int textureID) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, textureID);
    }

    public void putUniformTexture2D(int uniformIndex, int textureID) {
        this.bindTexture2D(textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture2D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0 + textureUnit);
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture2D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture2D(textureUnit, textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture3D(int textureID) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_3D, textureID);
    }

    public void putUniformTexture3D(int uniformIndex, int textureID) {
        this.bindTexture3D(textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture3D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0 + textureUnit);
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_3D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture3D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture3D(textureUnit, textureID);
        GLWrapper.Shader.glUniform1i(uniformIndex, textureUnit);
    }

    public void putBindless(int uniformIndex, LongBuffer handles) {
        GLWrapper.Texture.Bindless.glProgramUniformHandleu(this.id, uniformIndex, handles);
    }

    public void putBindless(int uniformIndex, long handle) {
        GLWrapper.Texture.Bindless.glProgramUniformHandleui64(this.id, uniformIndex, handle);
    }

    public void putUniformSubroutine(int shaderType, int shaderTypeIndex, int subroutineIndex) {
        GLWrapper.Shader.glUniformSubroutinesu(shaderType, this.getSubroutineBuffer(shaderTypeIndex, subroutineIndex));
    }

    public void putUniformSubroutines(int shaderType, int... subroutines) {
        GLWrapper.Shader.glUniformSubroutinesu(shaderType, CommonUtil.createIntBuffer(subroutines));
    }

    public void putUniformSubroutines(int shaderType, int shaderTypeIndex, int... subroutines) {
        IntBuffer buffer = BufferUtils.createIntBuffer(this.maxSubroutineUniformLocation[shaderTypeIndex]);
        for (int i = 0; i < subroutines.length; i++) {
            buffer.put(this.subroutineUniformLocation[shaderTypeIndex][i], subroutines[i]);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        GLWrapper.Shader.glUniformSubroutinesu(shaderType, buffer);
    }

    public IntBuffer getSubroutineBuffer(int shaderTypeIndex, int subroutineIndex) {
        return CommonUtil.createIntBuffer(this.subroutineLocation[shaderTypeIndex][subroutineIndex]);
    }

    public void initMaxSubroutineUniformLocation() {
        int shaderTypeCount = this.subroutineUniformLocation.length;
        this.maxSubroutineUniformLocation = new int[shaderTypeCount];
        for (int i = 0; i < shaderTypeCount; i++) {
            int max = 0;
            for (int index : this.subroutineUniformLocation[i]) {
                max = Math.max(index, max);
            }
            this.maxSubroutineUniformLocation[i] = max + 1;
        }
    }
}
