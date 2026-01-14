package org.boxutil.util.lut;

import org.boxutil.define.BoxDatabase;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.SerializationUtil;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/// Default look-up tables.
///
/// # The MIT License (MIT)
/// Copyright © 2025 ShioZakana
///
/// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
///
/// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
///
/// **THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.**
public final class ShadingLUT {
    /**
     * @see <a href="github.com/AakashKT/LTC-Anisotropic">github.com/AakashKT/LTC-Anisotropic</a>, MIT license.
     */
    public final static class AnisotropicLTCs {
        /**
         * The anisotropic LTC matrix 3D texture, 8*8*64 vec3 layout, total three texture(M0, M1, M2).<p>
         * Build for the <code>alphaX, alphaY, theta, phi</code> parameterization.
         *
         * @return int[] = {M0(m00, m01, m02), M1(m10, m11, m12), M2(m20, m21, m22)};
         */
        public static int[] genLTCMatrix(boolean useTextureStorage) {
            if (!BoxDatabase.getGLState().GL_GL30) return new int[3];
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            final int size = 8 * 8 * 64;
            int[] result = new int[]{GL11.glGenTextures(), GL11.glGenTextures(), GL11.glGenTextures()};
            FloatBuffer data;
            for (byte i = 0; i < 3; ++i) {
                data = SerializationUtil.loadFloatLUT(BoxDatabase.AnisotropicLTC[i]);
                if (data == null || data.capacity() < size) {
                    GL11.glDeleteTextures(CommonUtil.createIntBuffer(result));
                    return null;
                }
                GL11.glBindTexture(GL12.GL_TEXTURE_3D, result[0]);
                if (texStorage) GL42.glTexStorage3D(GL12.GL_TEXTURE_3D, 1, GL30.GL_RGB32F, 8, 8, 64);
                else GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL30.GL_RGB32F, 8, 8, 64, 0, GL11.GL_RGB, GL11.GL_FLOAT, (ByteBuffer) null);
                GL12.glTexSubImage3D(GL12.GL_TEXTURE_3D, 0, 0, 0, 0, 32, 8, 64, GL11.GL_RGB, GL11.GL_FLOAT, data);
                GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
            }
            GL11.glBindTexture(GL12.GL_TEXTURE_3D, 0);
            if (result[0] > 0 && result[1] > 0 && result[2] > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f, 1.0f, 1.0f));
            return result;
        }

        private AnisotropicLTCs() {}
    }

    public final static class IsotropicLTCs {
        /**
         * The isotropic LTC matrix 2D texture, 64*64 vec4 layout.<p>
         * Build for the <code>roughness, sqrt(1.0f - NdotV)</code> parameterization.
         *
         * @return InvMatrix(m[0].x, m[0].z, m[2].x, m[2].z);
         */
        public static int genLTCMatrix(boolean useTextureStorage) {
            if (!BoxDatabase.getGLState().GL_GL30) return 0;
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            final int size = 16384;
            int result = GL11.glGenTextures();
            FloatBuffer data = SerializationUtil.loadFloatLUT(BoxDatabase.IsotropicLTC[0]);
            if (data == null || data.capacity() < size) {
                GL11.glDeleteTextures(result);
                return 0;
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
            if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL30.GL_RGBA32F, 64, 64);
            else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, 64, 64, 0, GL11.GL_RGBA, GL11.GL_FLOAT, (ByteBuffer) null);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 64, 64, GL11.GL_RGBA, GL11.GL_FLOAT, data);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            if (result > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f));
            return result;
        }

        /**
         * The isotropic LTC magnitude 2D texture, 64*64 vec2&float layout.
         *
         * @return int[] = {Integral(GGX norm, fresnel), Clipping(sphere for horizon-clipping)};
         */
        public static int[] genLTCMagnitudeSplit(boolean useTextureStorage) {
            if (!BoxDatabase.getGLState().GL_GL30) return new int[2];
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            final int[] size = new int[]{8192, 4096};
            final int[] internalFormat = new int[]{GL30.GL_RG32F, GL30.GL_R32F};
            final int[] format = new int[]{GL30.GL_RG, GL11.GL_RED};
            int[] result = new int[]{GL11.glGenTextures(), GL11.glGenTextures()};
            FloatBuffer data;
            for (byte i = 0; i < 2; ++i) {
                data = SerializationUtil.loadFloatLUT(BoxDatabase.IsotropicLTC[i + 1]);
                if (data == null || data.capacity() < size[i]) {
                    GL11.glDeleteTextures(CommonUtil.createIntBuffer(result));
                    return null;
                }
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, result[i]);
                if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, internalFormat[i], 64, 64);
                else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat[i], 64, 64, 0, format[i], GL11.GL_FLOAT, (ByteBuffer) null);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 64, 64, format[i], GL11.GL_FLOAT, data);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            if (result[0] > 0 && result[1] > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f, 1.0f));
            return result;
        }

        /**
         * The isotropic LTC magnitude 2D texture, 64*64 vec3 layout.
         *
         * @return Magnitude(GGX norm, fresnel, sphere for horizon-clipping);
         */
        public static int genLTCMagnitude(boolean useTextureStorage) {
            if (!BoxDatabase.getGLState().GL_GL30) return 0;
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            final int size = 12288;
            int result = GL11.glGenTextures();
            FloatBuffer data = SerializationUtil.loadFloatLUT(BoxDatabase.IsotropicLTC[3]);
            if (data == null || data.capacity() < size) {
                GL11.glDeleteTextures(result);
                return 0;
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
            if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL30.GL_RGB32F, 64, 64);
            else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, 64, 64, 0, GL11.GL_RGB, GL11.GL_FLOAT, (ByteBuffer) null);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 64, 64, GL11.GL_RGB, GL11.GL_FLOAT, data);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            if (result > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f));
            return result;
        }

        private IsotropicLTCs() {}
    }

    public final static class KullaContyBRDF {
        private static float getVisibility(float NdotV, float NdotL, float roughness) {
            float result = CalculateUtil.mix(2.0f * NdotV * NdotL, NdotV + NdotL, roughness);
            return 0.5f / result;
        }

        private static Vector3f ImportanceSampleGGX_Emu(Vector2f Xi, float alpha, Vector3f result) {
            float phi = TrigUtil.PI2_F * Xi.x;
            float cosTheta = (float) Math.sqrt((1.0f - Xi.y) / (1.0f + (alpha * alpha - 1.0f) * Xi.y));
            float sinTheta = (float) Math.sqrt(1.0f - cosTheta * cosTheta);
            result.set((float) Math.sin(phi) * sinTheta, -(float) Math.cos(phi) * sinTheta, cosTheta);
            return result;
        }

        /**
         * Real-time calculation.<p>
         * For how to sample the value in shader:<p>
         * <pre>
         * {@code glsl:
         * float EMuVOM = texture(emuTex, vec2(NdotV, roughness)).x; // 1.0 - EMu; sampler2D
         * float EMuLOM = texture(emuTex, vec2(NdotL, roughness)).x; // 1.0 - EMu; sampler2D
         * float EAvg = texture(eavgTex, roughness).x; // just EAvg; sampler1D
         * }
         * </pre>
         *
         * @return int[] = {EMu, EAvg};
         */
        public static int[] genApproximation(int sampleNum, byte sizeExponent, boolean useTextureStorage, boolean bit16) {
            if (!BoxDatabase.getGLState().GL_GL30) return new int[2];
            final int num = Math.max(sampleNum, 256), size = 1 << Math.max(sizeExponent, 1), bufferSize = size * size, q = bit16 ? 65535 : 255;
            final float weightDiv = 4.0f / num, eavgWeightDiv = 2.0f / size, sizeLimit = 1.0f / size;
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            Vector3f H = new Vector3f(), V = new Vector3f(), negV = new Vector3f(), L = new Vector3f();
            int storageValue;
            float roughness, alpha, NdotV;
            float weight, weightAvg;
            int[] result = new int[]{GL11.glGenTextures(), GL11.glGenTextures()};
            Buffer emuData = bit16 ? BufferUtils.createShortBuffer(bufferSize) : BufferUtils.createByteBuffer(bufferSize);
            Buffer eavgData = bit16 ? BufferUtils.createShortBuffer(bufferSize) : BufferUtils.createByteBuffer(bufferSize);

            final Vector2f[] samples = new Vector2f[num];
            for (int i = 0; i < num; ++i) samples[i] = CalculateUtil.getHammersley(i, num);

            for (int y = 0; y < num; ++y) {
                roughness = (y + 0.5f) * sizeLimit;
                alpha = roughness * roughness;

                weightAvg = 0.0f;
                for (int x = 0; x < num; ++x) {
                    NdotV = (x + 0.5f) * sizeLimit;
                    V.set((float) Math.sqrt(1.0f - NdotV * NdotV), 0.0f, NdotV);
                    V.negate(negV);

                    weight = 0.0f;
                    for (Vector2f sample : samples) {
                        ImportanceSampleGGX_Emu(sample, alpha, H);
                        CalculateUtil.reflect(negV, H, L);
                        if (L.z <= 0.0f) continue;
                        weight += getVisibility(Math.max(V.z, 0.0f), L.z, roughness) * L.z * Math.max(Vector3f.dot(V, H), 0.0f) / Math.max(H.z, 0.0f);
                    }
                    weight *= weightDiv;
                    weightAvg += weight * NdotV;
                    weight = 1.0f - weight;

                    storageValue = Math.max(Math.min(Math.round(weight * q), q), 0);
                    if (bit16) ((ShortBuffer) emuData).put((short) storageValue);
                    else ((ByteBuffer) emuData).put((byte) storageValue);
                }
                weightAvg *= eavgWeightDiv;

                storageValue = Math.max(Math.min(Math.round(weightAvg * q), q), 0);
                if (bit16) ((ShortBuffer) eavgData).put((short) storageValue);
                else ((ByteBuffer) eavgData).put((byte) storageValue);
            }
            emuData.position(0);
            emuData.limit(emuData.capacity());
            eavgData.position(0);
            eavgData.limit(eavgData.capacity());

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result[0]);
            if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, bit16 ? GL30.GL_R16 : GL30.GL_R8, size, size);
            else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, bit16 ? GL30.GL_R16 : GL30.GL_R8, size, size, 0, GL11.GL_RED, bit16 ? GL11.GL_UNSIGNED_SHORT : GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            if (bit16) GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, size, size, GL11.GL_RED, GL11.GL_UNSIGNED_SHORT, (ShortBuffer) emuData);
            else GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, size, size, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) emuData);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result[1]);
            if (texStorage) GL42.glTexStorage1D(GL11.GL_TEXTURE_1D, 1, bit16 ? GL30.GL_R16 : GL30.GL_R8, size);
            else GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, bit16 ? GL30.GL_R16 : GL30.GL_R8, size, 0, GL11.GL_RED, bit16 ? GL11.GL_UNSIGNED_SHORT : GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            if (bit16) GL11.glTexSubImage1D(GL11.GL_TEXTURE_1D, 0, 0, size, GL11.GL_RED, GL11.GL_UNSIGNED_SHORT, (ShortBuffer) eavgData);
            else GL11.glTexSubImage1D(GL11.GL_TEXTURE_1D, 0, 0, size, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) eavgData);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            if (result[0] > 0 && result[1] > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f, 1.0f));
            return result;
        }

        /**
         * The Kulla-Conty Approximation 2D&1D texture, 64*64 / 64 half-float layout.<p>
         * For how to sample the value in shader:
         * <pre>
         * {@code glsl:
         * float EMuVOM = texture(emuTex, vec2(NdotV, roughness)).x; // 1.0 - EMu; sampler2D
         * float EMuLOM = texture(emuTex, vec2(NdotL, roughness)).x; // 1.0 - EMu; sampler2D
         * float EAvg = texture(eavgTex, roughness).x; // just EAvg; sampler1D
         * }
         * </pre>
         *
         * @return int[] = {EMu, EAvg};
         */
        public static int[] genApproximation(boolean useTextureStorage) {
            if (!BoxDatabase.getGLState().GL_GL30) return new int[2];
            final boolean texStorage = useTextureStorage && BoxDatabase.getGLState().GL_TEXTURE_STORAGE;
            final int[] size = new int[]{4096, 64};
            int[] result = new int[]{GL11.glGenTextures(), GL11.glGenTextures()};
            ShortBuffer data;
            data = SerializationUtil.loadFloatLUTConvertHalfFloat(BoxDatabase.KullaContyBRDF[0]);
            if (data == null || data.capacity() < size[0]) {
                GL11.glDeleteTextures(CommonUtil.createIntBuffer(result));
                return null;
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, result[0]);
            if (texStorage) GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL30.GL_R16F, size[1], size[1]);
            else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R16F, size[1], size[1], 0, GL11.GL_RED, GL30.GL_HALF_FLOAT, (ByteBuffer) null);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, size[1], size[1], GL11.GL_RED, GL30.GL_HALF_FLOAT, data);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            data = SerializationUtil.loadFloatLUTConvertHalfFloat(BoxDatabase.KullaContyBRDF[1]);
            if (data == null || data.capacity() < size[1]) {
                GL11.glDeleteTextures(CommonUtil.createIntBuffer(result));
                return null;
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_1D, result[0]);
            if (texStorage) GL42.glTexStorage1D(GL11.GL_TEXTURE_1D, 1, GL30.GL_R16F, size[1]);
            else GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, GL30.GL_R16F, size[1], 0, GL11.GL_RED, GL30.GL_HALF_FLOAT, (ByteBuffer) null);
            GL11.glTexSubImage1D(GL11.GL_TEXTURE_1D, 0, 0, size[1], GL11.GL_RED, GL30.GL_HALF_FLOAT, data);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL11.GL_TEXTURE_1D, 0);
            if (result[0] > 0 && result[1] > 0) GL11.glPrioritizeTextures(CommonUtil.createIntBuffer(result), CommonUtil.createFloatBuffer(1.0f, 1.0f));
            return result;
        }

        private KullaContyBRDF() {}
    }

    private ShadingLUT() {}
}
