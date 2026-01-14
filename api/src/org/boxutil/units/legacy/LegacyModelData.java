package org.boxutil.units.legacy;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.backends.array.BUtil_Stack2f;
import org.boxutil.backends.array.BUtil_Stack3f;
import org.boxutil.backends.array.BUtil_Stack3i;
import org.boxutil.backends.array.BUtil_TriIndex;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

/**
 * For draw 3D object with lights and sprite:
 * <pre>
 * {@code
 * LegacyModelData mesh = 'some init code';
 * }
 * </pre>
 */
public class LegacyModelData {
    protected final int verticesCount;
    protected final FloatBuffer[] _buffers = new FloatBuffer[3];

    public LegacyModelData(LegacyModelData source) {
        this.verticesCount = source.verticesCount;
        this._buffers[0] = source._buffers[0].duplicate();
        this._buffers[1] = source._buffers[1].duplicate();
        this._buffers[2] = source._buffers[2].duplicate();
    }

    public LegacyModelData(BUtil_Stack3f[] v, BUtil_Stack3f[] vn, BUtil_Stack2f[] vt, BUtil_TriIndex[] vf) {
        this.verticesCount = vf.length * 3;
        this._buffers[0] = BufferUtils.createFloatBuffer(this.verticesCount * 3);
        this._buffers[1] = BufferUtils.createFloatBuffer(this.verticesCount * 3);
        this._buffers[2] = BufferUtils.createFloatBuffer(this.verticesCount * 2);
        BUtil_Stack3f vTmp, vnTmp;
        BUtil_Stack2f vtTmp;
        for (BUtil_TriIndex mapping : vf) {
            for (BUtil_Stack3i index : mapping.index()) {
                vTmp = v[index.x()];
                vnTmp = vn[index.y()];
                vtTmp = vt[index.z()];

                this._buffers[0].put(vTmp.x());
                this._buffers[0].put(vTmp.y());
                this._buffers[0].put(vTmp.z());
                this._buffers[1].put(vnTmp.x());
                this._buffers[1].put(vnTmp.y());
                this._buffers[1].put(vnTmp.z());
                this._buffers[2].put(vtTmp.x());
                this._buffers[2].put(vtTmp.y());
            }
        }
    }

    protected void putColorData(FloatBuffer uploadBuffer, Vector4f color, float alphaMulti) {
        uploadBuffer.position(0);
        uploadBuffer.put(0, color.x);
        uploadBuffer.put(0, color.y);
        uploadBuffer.put(0, color.z);
        uploadBuffer.put(0, color.w * alphaMulti);
        uploadBuffer.position(0);
        uploadBuffer.limit(4);
    }

    /**
     * Must call {@link LegacyModelData#glPopAfterApplyIlluminationAndDraw()} after draw.
     *
     * @param illuminant maximum 8 objects.
     */
    public void glPushAndApplyIllumination(float alphaMulti, @Nullable Vector4f ambientColor, @Nullable Vector4f materialAmbient, @Nullable Vector4f materialDiffuse, @Nullable Vector4f materialSpecular, @Nullable Vector4f materialEmission, @Nullable Float materialShininess, boolean twoSideShading, boolean smoothShading, @NotNull LegacyIlluminantObject... illuminant) {
        final int _glAttribBits = GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_POLYGON_BIT;
        GL11.glPushAttrib(_glAttribBits);
        final byte totalIllum = (byte) illuminant.length;
        if (totalIllum < 1) return;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        for (byte i = 0; i < totalIllum; i++) illuminant[i].glApplyIlluminant(alphaMulti, i);
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, twoSideShading ? GL11.GL_TRUE : GL11.GL_FALSE);
        GL11.glShadeModel(smoothShading ? GL11.GL_SMOOTH : GL11.GL_FLAT);

        final FloatBuffer uploadBuffer = BufferUtils.createFloatBuffer(4);
        final int materialFace = twoSideShading ? GL11.GL_FRONT_AND_BACK : GL11.GL_FRONT;
        if (ambientColor != null) {
            this.putColorData(uploadBuffer, ambientColor, alphaMulti);
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, uploadBuffer);
        }
        if (materialAmbient != null) {
            this.putColorData(uploadBuffer, materialAmbient, alphaMulti);
            GL11.glMaterial(materialFace, GL11.GL_AMBIENT, uploadBuffer);
        }
        if (materialDiffuse != null) {
            this.putColorData(uploadBuffer, materialDiffuse, alphaMulti);
            GL11.glMaterial(materialFace, GL11.GL_DIFFUSE, uploadBuffer);
        }
        if (materialSpecular != null) {
            this.putColorData(uploadBuffer, materialSpecular, alphaMulti);
            GL11.glMaterial(materialFace, GL11.GL_SPECULAR, uploadBuffer);
        }
        if (materialEmission != null) {
            this.putColorData(uploadBuffer, materialEmission, alphaMulti);
            GL11.glMaterial(materialFace, GL11.GL_EMISSION, uploadBuffer);
        }
        if (materialShininess != null) {
            GL11.glMaterialf(materialFace, GL11.GL_SHININESS, materialShininess);
        }
    }

    /**
     * Must call {@link LegacyModelData#glPopAfterApplyIlluminationAndDraw()} after draw.
     *
     * @param illuminant maximum 8 objects.
     */
    public void glPushAndApplyIllumination(float alphaMulti, @Nullable Vector4f materialAmbient, @Nullable Vector4f materialDiffuse, @NotNull LegacyIlluminantObject... illuminant) {
        this.glPushAndApplyIllumination(alphaMulti, null, materialAmbient, materialDiffuse, null, null, null, true, true, illuminant);
    }

    public void glBindSpriteBeforeDraw(int textureID) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public void glBindSpriteBeforeDraw(SpriteAPI sprite) {
        this.glBindSpriteBeforeDraw(sprite.getTextureId());
    }

    /**
     * Call <code>GL11.glEnable(GL11.GL_CULL_FACE); GL11.glEnable(GL11.GL_DEPTH_TEST);</code> before draw, if needed.
     */
    public void glDraw(boolean withNormal, boolean withUV) {
        for (FloatBuffer buffer : this._buffers) {
            buffer.position(0);
            buffer.limit(buffer.capacity());
        }
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        if (withNormal) GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY); else GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        if (withUV) GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY); else GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glVertexPointer(3, 0, this._buffers[0]);
        if (withNormal) GL11.glNormalPointer(3, this._buffers[1]);
        if (withUV) GL11.glTexCoordPointer(2, 0, this._buffers[2]);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.verticesCount);
        GL11.glPopClientAttrib();
    }

    public void glPopAfterApplyIlluminationAndDraw() {
        GL11.glPopAttrib();
    }

    public int getVerticesCount() {
        return this.verticesCount;
    }

    public FloatBuffer getVertexBuffer() {
        return this._buffers[0];
    }

    public FloatBuffer getNormalBuffer() {
        return this._buffers[1];
    }

    public FloatBuffer getUVBuffer() {
        return this._buffers[2];
    }
}
