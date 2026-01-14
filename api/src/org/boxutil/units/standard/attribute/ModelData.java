package org.boxutil.units.standard.attribute;

import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.backends.array.BUtil_TriIndex;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.MeshUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

public class ModelData {
    protected final String _rawID;
    protected final int[] _objectData = new int[3];
    // TBN, vn, vt
    protected final int[] _verticesDataTBO = new int[2]; // tbo, tex
    protected final int _dataType;
    protected final int[] _textures = new int[5];
    protected final boolean _isImmutableBuffer;
    protected boolean isValid = false;

    public ModelData(String rawID, ModelData entity, int diffuse, int normal, int complex, int emissive, int tangent) {
        this._rawID = rawID;
        this._dataType = entity.getDataType();
        this._objectData[0] = entity.getVAO();
        this._objectData[1] = entity.getVBO();
        this._objectData[2] = entity.getPatchCount();
        this._verticesDataTBO[0] = entity.getTBNDataTBO();
        this._verticesDataTBO[1] = entity.getTBNDataTBOTex();
        this._textures[0] = diffuse;
        this._textures[1] = normal;
        this._textures[2] = complex;
        this._textures[3] = emissive;
        this._textures[4] = tangent;
        this._isImmutableBuffer = entity._isImmutableBuffer;
        if (this._objectData[0] > 0 && this._objectData[1] > 0 && this._objectData[2] > 0) isValid = true;
    }

    public ModelData(String rawID, List<Vector3f> vertex, List<Vector3f> normal, List<Vector2f> uv, List<BUtil_TriIndex> patchIndex, int diffuse, int normalMap, int complex, int emissive, int tangent, int type) {
        this._rawID = rawID;
        this._dataType = type;
        this._objectData[2] = patchIndex.size() * 3;

        this._textures[0] = diffuse;
        this._textures[1] = normalMap;
        this._textures[2] = complex;
        this._textures[3] = emissive;
        this._textures[4] = tangent;

        this._isImmutableBuffer = BoxDatabase.getGLState() != null && BoxDatabase.getGLState().GL_GL44;

        if (!BoxConfigs.isTBOSupported()) return;

        final boolean normalize = type == GL11.GL_BYTE;
        final int s = type == GL11.GL_BYTE ? BoxDatabase.BYTE_SIZE : (type == GL30.GL_HALF_FLOAT ? BoxDatabase.HALF_FLOAT_SIZE : BoxDatabase.FLOAT_SIZE);
        final int patchVertexSize = 8 * s;

        ByteBuffer vertexBuffer = BufferUtils.createByteBuffer(patchIndex.size() * 24 * s);
        final byte _tangentComponent = 3;
        FloatBuffer tbnBuffer_A = BufferUtils.createFloatBuffer(patchIndex.size() * _tangentComponent);

        int patchVertexIndex, patchNormalIndex, patchUVIndex, patchBufferIndex, vertexBufferIndex;
        Vector3f[] patchVertex = new Vector3f[3];
        Vector3f patchNormal;
        Vector2f[] patchUV = new Vector2f[3];
        float[] TBNData = new float[3];
        Vector3f tangentRaw;
        for (int i = 0; i < patchIndex.size(); ++i) {
            BUtil_TriIndex patch = patchIndex.get(i);
            vertexBufferIndex = i * 24;
            for (byte j = 0; j < 3; ++j) {
                patchVertexIndex = patch.index()[j].x();
                patchNormalIndex = patch.index()[j].y();
                patchUVIndex = patch.index()[j].z();

                patchVertex[j] = vertex.get(patchVertexIndex);
                patchNormal = normal.get(patchNormalIndex);
                patchUV[j] = uv.get(patchUVIndex);

                if (type == GL11.GL_BYTE) {
                    vertexBuffer.put(vertexBufferIndex, CommonUtil.normalizedFloatToByte(patchVertex[j].getX()));
                    vertexBuffer.put(vertexBufferIndex + 1, CommonUtil.normalizedFloatToByte(patchVertex[j].getY()));
                    vertexBuffer.put(vertexBufferIndex + 2, CommonUtil.normalizedFloatToByte(patchVertex[j].getZ()));

                    vertexBuffer.put(vertexBufferIndex + 3, CommonUtil.normalizedFloatToByte(patchNormal.getX()));
                    vertexBuffer.put(vertexBufferIndex + 4, CommonUtil.normalizedFloatToByte(patchNormal.getY()));
                    vertexBuffer.put(vertexBufferIndex + 5, CommonUtil.normalizedFloatToByte(patchNormal.getZ()));

                    vertexBuffer.put(vertexBufferIndex + 6, CommonUtil.normalizedFloatToByte(patchUV[j].getX()));
                    vertexBuffer.put(vertexBufferIndex + 7, CommonUtil.normalizedFloatToByte(patchUV[j].getY()));
                } else if (type == GL30.GL_HALF_FLOAT) {
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex, CommonUtil.float16ToShort(patchVertex[j].getX()));
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 1, CommonUtil.float16ToShort(patchVertex[j].getY()));
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 2, CommonUtil.float16ToShort(patchVertex[j].getZ()));

                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 3, CommonUtil.float16ToShort(patchNormal.getX()));
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 4, CommonUtil.float16ToShort(patchNormal.getY()));
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 5, CommonUtil.float16ToShort(patchNormal.getZ()));

                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 6, CommonUtil.float16ToShort(patchUV[j].getX()));
                    vertexBuffer.asShortBuffer().put(vertexBufferIndex + 7, CommonUtil.float16ToShort(patchUV[j].getY()));
                } else {
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex, patchVertex[j].getX());
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 1, patchVertex[j].getY());
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 2, patchVertex[j].getZ());

                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 3, patchNormal.getX());
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 4, patchNormal.getY());
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 5, patchNormal.getZ());

                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 6, patchUV[j].getX());
                    vertexBuffer.asFloatBuffer().put(vertexBufferIndex + 7, patchUV[j].getY());
                }
                vertexBufferIndex += 8;
            }

            tangentRaw = MeshUtil.tangentOnlyMaker(patchVertex[0], patchVertex[1], patchVertex[2], patchUV[0], patchUV[1], patchUV[2]);
            TBNData[0] = tangentRaw.x;
            TBNData[1] = tangentRaw.y;
            TBNData[2] = tangentRaw.z;

            patchBufferIndex = i * _tangentComponent;
            for (byte j = 0; j < _tangentComponent; ++j) {
                tbnBuffer_A.put(patchBufferIndex + j, TBNData[j]);
            }
        }

        vertexBuffer.position(0);
        vertexBuffer.limit(vertexBuffer.capacity());

        tbnBuffer_A.position(0);
        tbnBuffer_A.limit(tbnBuffer_A.capacity());

        this._verticesDataTBO[0] = GL15.glGenBuffers();
        this._verticesDataTBO[1] = GL11.glGenTextures();
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, this._verticesDataTBO[0]);
        if (this._isImmutableBuffer) GL44.glBufferStorage(GL31.GL_TEXTURE_BUFFER, tbnBuffer_A, 0);
        else GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, tbnBuffer_A, GL15.GL_STATIC_DRAW);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, this._verticesDataTBO[1]);
        GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_RGB32F, this._verticesDataTBO[0]);
        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);

        this._objectData[0] = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this._objectData[0]);

        this._objectData[1] = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this._objectData[1]);
        if (this._isImmutableBuffer)GL44.glBufferStorage(GL15.GL_ARRAY_BUFFER, vertexBuffer, 0);
        else GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, type, normalize, patchVertexSize, 0); // v
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 3, type, normalize, patchVertexSize, s * 3); // vn
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(2, 2, type, normalize, patchVertexSize, s * 6); // vt
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        if (this._objectData[0] > 0 && this._objectData[1] > 0 && this._objectData[2] > 0) isValid = true;
    }

    public String getRawID() {
        return this._rawID;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public boolean isImmutableBuffer() {
        return this._isImmutableBuffer;
    }

    public void destroy() {
        this.isValid = false;
        if (this.getVAO() > 0) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.getVAO());
        }
        if (this.getVBO() > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glDeleteBuffers(this.getVBO());
        }
        if (this.getTBNDataTBOTex() > 0) {
            GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
            GL11.glDeleteTextures(this.getTBNDataTBOTex());
        }
        if (this.getTBNDataTBO() > 0) {
            GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
            GL15.glDeleteBuffers(this.getTBNDataTBO());
        }
    }

    public void putTBNShaderData() {
        GL13.glActiveTexture(GL13.GL_TEXTURE8);
        GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, this.getTBNDataTBOTex());
    }

    public int getDataType() {
        return this._dataType;
    }

    /**
     * You shouldn't edit it.
     */
    public int getVAO() {
        return this._objectData[0];
    }

    /**
     * You shouldn't edit it.
     */
    public int getVBO() {
        return this._objectData[1];
    }

    public int getPatchCount() {
        return this._objectData[2];
    }

    public int getTBNDataTBO() {
        return this._verticesDataTBO[0];
    }

    public int getTBNDataTBOTex() {
        return this._verticesDataTBO[1];
    }

    public int getDiffuseID() {
        return this._textures[0];
    }

    public void setDiffuse(int texID) {
        this._textures[0] = texID;
    }

    public int getNormalID() {
        return _textures[1];
    }

    public void setNormal(int texID) {
        this._textures[1] = texID;
    }

    public int getComplexID() {
        return _textures[2];
    }

    public void setComplex(int texID) {
        this._textures[2] = texID;
    }

    public int getEmissiveID() {
        return _textures[3];
    }

    public void setEmissive(int texID) {
        this._textures[3] = texID;
    }

    public int getTangentID() {
        return _textures[4];
    }

    public void setTangent(int texID) {
        this._textures[4] = texID;
    }
}
