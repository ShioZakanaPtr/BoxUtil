package org.boxutil.base;

import org.boxutil.define.BoxDatabase;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

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
        return GL20.glGetUniformLocation(this.id, name);
    }

    public int getStructUniformIndex(String name) {
        return GL31.glGetUniformBlockIndex(this.id, name);
    }

    public int getUBOIndex(String name, int bindingIndex) {
        final int index = this.getStructUniformIndex(name);;
        GL31.glUniformBlockBinding(this.id, index, bindingIndex);
        return index;
    }

    public int getSubroutineIndex(int shaderType, String name) {
        return GL40.glGetSubroutineIndex(this.id, shaderType, name);
    }

    /**
     * Unneeded for usual.
     */
    public int getSubroutineUniformLocation(int shaderType, String name) {
        return GL40.glGetSubroutineUniformLocation(this.id, shaderType, name);
    }

    public void active() {
        GL20.glUseProgram(this.id);
    }

    public void close() {
        GL20.glUseProgram(0);
    }

    public void delete() {
        if (this.id < 1) return;
        this.close();
        IntBuffer shadersBuffer = BufferUtils.createIntBuffer(16);
        GL20.glGetAttachedShaders(this.id, null, shadersBuffer);
        if (shadersBuffer.hasArray()) {
            for (int shaderID : shadersBuffer.array()) {
                GL20.glDetachShader(this.id, shaderID);
                GL20.glDeleteShader(shaderID);
            }
        }
        GL20.glDeleteProgram(this.id);
    }

    public boolean isValid() {
        return this.id > 0;
    }

    public int getId() {
        return this.id;
    }

    public void putDefaultTextureUnit(int location, int unit) {
        GL41.glProgramUniform1i(this.getId(), location, unit);
    }

    public void putBindingImageTexture(int binding, int textureID, int format) {
        GL42.glBindImageTexture(binding, textureID, 0, false, 0, GL15.GL_READ_WRITE, format);
    }

    public void putBindingImageTextureReadOnly(int binding, int textureID, int format) {
        GL42.glBindImageTexture(binding, textureID, 0, false, 0, GL15.GL_READ_ONLY, format);
    }

    public void putBindingImageTextureWriteOnly(int binding, int textureID, int format) {
        GL42.glBindImageTexture(binding, textureID, 0, false, 0, GL15.GL_WRITE_ONLY, format);
    }

    public void bindTextureBuffer(int textureID) {
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, textureID);
    }

    public void putUniformTextureBuffer(int uniformIndex, int textureID) {
        this.bindTextureBuffer(textureID);
        GL20.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTextureBuffer(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTextureBuffer(int uniformIndex, int textureUnit, int textureID) {
        this.bindTextureBuffer(textureUnit, textureID);
        GL20.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture1D(int textureID) {
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, textureID);
    }

    public void putUniformTexture1D(int uniformIndex, int textureID) {
        this.bindTexture1D(textureID);
        GL20.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture1D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture1D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture1D(textureUnit, textureID);
        GL20.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture2D(int textureID) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public void putUniformTexture2D(int uniformIndex, int textureID) {
        this.bindTexture2D(textureID);
        GL20.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture2D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture2D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture2D(textureUnit, textureID);
        GL20.glUniform1i(uniformIndex, textureUnit);
    }

    public void bindTexture3D(int textureID) {
        GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureID);
    }

    public void putUniformTexture3D(int uniformIndex, int textureID) {
        this.bindTexture3D(textureID);
        GL20.glUniform1i(uniformIndex, 0);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void bindTexture3D(int textureUnit, int textureID) {
        if (textureUnit >= BoxDatabase.getGLState().MAX_TEXTURE_UNITS) return;
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureID);
    }

    /**
     * @param textureUnit 0 to 31 only, total 32 texture channels.
     */
    public void putUniformTexture3D(int uniformIndex, int textureUnit, int textureID) {
        this.bindTexture3D(textureUnit, textureID);
        GL20.glUniform1i(uniformIndex, textureUnit);
    }

    public void putBindless(int uniformIndex, LongBuffer handles) {
        if (BoxDatabase.getGLState().GL_BINDLESS_TEXTURE) {
            if (BoxDatabase.getGLState().NV_BINDLESS_TEXTURE) {
                NVBindlessTexture.glProgramUniformHandleuNV(this.id, uniformIndex, handles);
            } else {
                ARBBindlessTexture.glProgramUniformHandleuARB(this.id, uniformIndex, handles);
            }
        }
    }

    public void putBindless(int uniformIndex, long handles) {
        if (BoxDatabase.getGLState().GL_BINDLESS_TEXTURE) {
            if (BoxDatabase.getGLState().NV_BINDLESS_TEXTURE) {
                NVBindlessTexture.glProgramUniformHandleui64NV(this.id, uniformIndex, handles);
            } else {
                ARBBindlessTexture.glProgramUniformHandleui64ARB(this.id, uniformIndex, handles);
            }
        }
    }

    public void putUniformSubroutine(int shaderType, int shaderTypeIndex, int subroutineIndex) {
        GL40.glUniformSubroutinesu(shaderType, this.getSubroutineBuffer(shaderTypeIndex, subroutineIndex));
    }

    public void putUniformSubroutines(int shaderType, int... subroutines) {
        GL40.glUniformSubroutinesu(shaderType, CommonUtil.createIntBuffer(subroutines));
    }

    public void putUniformSubroutines(int shaderType, int shaderTypeIndex, int... subroutines) {
        IntBuffer buffer = BufferUtils.createIntBuffer(this.maxSubroutineUniformLocation[shaderTypeIndex]);
        for (int i = 0; i < subroutines.length; i++) {
            buffer.put(this.subroutineUniformLocation[shaderTypeIndex][i], subroutines[i]);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        GL40.glUniformSubroutinesu(shaderType, buffer);
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
