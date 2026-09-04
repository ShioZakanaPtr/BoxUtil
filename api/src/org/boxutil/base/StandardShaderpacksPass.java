package org.boxutil.base;

import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.TrigUtil;

import java.nio.ByteBuffer;

public final class StandardShaderpacksPass {
    private static boolean _DO_BLOOM = false;

    public static byte[] applyTexturedAreaLightPreFiltering(int src, int preFiltering, boolean shouldAllocate) {
        byte[] result = new byte[]{BoxEnum.STATE_SUCCESS, 0};
        final byte[] resultFailed = new byte[]{BoxEnum.STATE_SUCCESS, 0};
        if (!ShaderCore.isAreaLightTexValid()) return resultFailed;
        BaseShaderData program = ShaderCore.getAreaLightTex();
        final float divA = 1.0f / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f), divB = 1.0f / 8.0f;
        final int[][] size = new int[7][2];
        int itemDimX, itemDimY;
        byte level = 0, lod, step = 2;

        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, src);
        size[0][0] = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_WIDTH);
        size[0][1] = GLWrapper.Texture.glGetTexLevelParameteri(GLWrapper.Texture.GL_TEXTURE_2D, 0, GLWrapper.Texture.GL_TEXTURE_HEIGHT);
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, preFiltering);
        while (level <= 6 && size[level][0] >= 4 && size[level][1] >= 4) {
            if (level > 0) {
                size[level][0] = size[level - 1][0] / 2;
                size[level][1] = size[level - 1][1] / 2;
            }
            if (shouldAllocate) {
                GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, level, GLWrapper.Texture.GL_RGBA8, size[level][0], size[level][1], 0, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_S, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_WRAP_T, GLWrapper.Texture.GL_CLAMP_TO_EDGE);
            }
            ++level;
        }
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAX_LEVEL, level);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_BASE_LEVEL, 0);
        if (level < 1) return resultFailed;

        program.active();
        program.bindTexture2D(0, src);
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, level == 1 ? 0 : 1);
        if (level == 1) {
            itemDimX = (int) Math.ceil(size[0][0] * divA);
            itemDimY = (int) Math.ceil(size[0][1] * divB);
            GLWrapper.Shader.glUniform3i(program.location[0], size[0][0], size[0][1], step);
            program.putBindingImageTextureWriteOnly(0, preFiltering, GLWrapper.Texture.GL_RGBA8);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

        } else {
            for (byte i = 1; i < level; ++i) {
                itemDimX = (int) Math.ceil(size[i][0] * divA);
                itemDimY = (int) Math.ceil(size[i][1] * divB);
                lod = (byte) (i - 1);
                GLWrapper.Shader.glUniform3i(program.location[0], size[i][0], size[i][1], step);
                GLWrapper.Shader.glUniform1i(program.location[1], 0);
                GLWrapper.Shader.glUniform4f(program.location[2], 1.0f / size[lod][0], 1.0f / size[lod][1], lod, 1.0f / (step * 0.1111111f * TrigUtil.PI_F));
                GLWrapper.Texture.glBindImageTexture(0, preFiltering, i, false, 0, GLWrapper.Texture.GL_WRITE_ONLY, GLWrapper.Texture.GL_RGBA8);
                GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
                GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
                GLWrapper.Shader.glUniform1i(program.location[1], 1);
                GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
                GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
                if (i == 1) program.bindTexture2D(0, preFiltering);
                ++step;
            }
        }
        program.bindTexture2D(0, 0);
        program.close();
        return result;
    }

    public static void applyFXAA(boolean enabled, boolean console, boolean useDepthBasedAA, int colorMap, int worldDataMap) {
        if (enabled) {
            BaseShaderData program = console ? ShaderCore.getFXAAConsoleProgram() : ShaderCore.getFXAAQualityProgram();
            if (program == null || !program.isValid()) return;
            int[] subroutines = new int[]{
                    program.subroutineLocation[0][useDepthBasedAA ? 1 : 0],
                    program.subroutineLocation[0][BoxConfigs.isAAShowEdge() ? 3 : 2]
            };
            BUtil_GLImpl.glScreenBlit();
            ShaderCore.getDefaultQuadObject().glBind();
            program.active();
            program.putUniformSubroutines(GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER, 0, subroutines);
            program.bindTexture2D(0, colorMap);
            program.bindTexture2D(1, worldDataMap);
            ShaderCore.getDefaultQuadObject().glDraw();
            program.bindTexture2D(0, 0);
            program.close();
            ShaderCore.getDefaultQuadObject().glReleaseBind();
        }
    }

    public static void applyBloom(boolean isMultiPassBloom, int emissiveMap, boolean withHighlightAdd, int highlightMap) {
        if (!_DO_BLOOM) return;
        final var program = ShaderCore.getBloomProgram();
        if (program == null || !program.isValid() || !BoxConfigs.isBaseGL43Supported() || !BoxConfigs.isShaderEnable()) return;
        final var renderingBuffer = ShaderCore.getRenderingBuffer();
        if (renderingBuffer.getLayerCount() < 2) return;
        final float divA = 1.0f / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f), divB = 1.0f / 8.0f;
        int itemDimX, itemDimY,
                resultTex = emissiveMap,
                widthCurr, heightCurr, widthNext, heightNext;

        program.active();
        if (withHighlightAdd) {
            GLWrapper.Shader.glUniform1i(program.location[2], 1);
            program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, 0);
            program.bindTexture2D(0, emissiveMap);
            program.bindTexture2D(1, highlightMap);
            program.putBindingImageTextureWriteOnly(1, emissiveMap, GLWrapper.Texture.GL_RGB8);
            widthCurr = renderingBuffer.getScaleSize(0)[0];
            heightCurr = renderingBuffer.getScaleSize(0)[1];
            itemDimX = (int) Math.ceil(widthCurr * divA);
            itemDimY = (int) Math.ceil(heightCurr * divB);
            GLWrapper.Shader.glUniform2i(program.location[0], widthCurr, heightCurr);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        }

        // down
        byte nextI;
        boolean notAvg = true;
        GLWrapper.Shader.glUniform1i(program.location[2], 0);
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, 1);
        for (byte i = 0; i < renderingBuffer.getLayerCount() - 1; ++i) {
            nextI = i;
            ++nextI;
            widthCurr = renderingBuffer.getScaleSize(i)[0];
            heightCurr = renderingBuffer.getScaleSize(i)[1];
            widthNext = renderingBuffer.getScaleSize(nextI)[0];
            heightNext = renderingBuffer.getScaleSize(nextI)[1];
            itemDimX = (int) Math.ceil(widthNext * divA);
            itemDimY = (int) Math.ceil(heightNext * divB);
            GLWrapper.Shader.glUniform2i(program.location[0], widthNext, heightNext);
            GLWrapper.Shader.glUniform4f(program.location[1], 1.0f / (widthCurr - 1), 1.0f / (heightCurr - 1), 1.0f / (widthNext - 1), 1.0f / (heightNext - 1));
            program.bindTexture2D(0, i == 0 ? emissiveMap : renderingBuffer.getBloomPingPongTex(i));
            program.putBindingImageTextureWriteOnly(0, renderingBuffer.getBloomPingPongTex(nextI), GLWrapper.Texture.GL_RGB10_A2);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
            if (notAvg) {
                notAvg = false;
                program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, 2);
            }
        }

        // up
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, 3);
        for (byte i = (byte) (renderingBuffer.getLayerCount() - 1); i > 1; --i) {
            nextI = i;
            --nextI;
            widthCurr = renderingBuffer.getScaleSize(i)[0];
            heightCurr = renderingBuffer.getScaleSize(i)[1];
            widthNext = renderingBuffer.getScaleSize(nextI)[0];
            heightNext = renderingBuffer.getScaleSize(nextI)[1];
            itemDimX = (int) Math.ceil(widthNext * divA);
            itemDimY = (int) Math.ceil(heightNext * divB);
            resultTex = renderingBuffer.getBloomPingPongTex(nextI);
            program.bindTexture2D(0, renderingBuffer.getBloomPingPongTex(i));
            program.bindTexture2D(1, resultTex);
            GLWrapper.Shader.glUniform2i(program.location[0], widthNext, heightNext);
            GLWrapper.Shader.glUniform4f(program.location[1], 1.0f / (widthCurr - 1), 1.0f / (heightCurr - 1), 1.0f / (widthNext - 1), 1.0f / (heightNext - 1));
            program.putBindingImageTextureWriteOnly(0, resultTex, GLWrapper.Texture.GL_RGB10_A2);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        }

        final var resultProgram = ShaderCore.getDirectDrawProgram();
        ShaderCore.getDefaultQuadObject().glBind();
        resultProgram.active();
        resultProgram.bindTexture2D(0, resultTex);
        GLWrapper.Shader.glUniform1f(ShaderCore.getDirectDrawProgram().location[0], 1.0f);
        GLWrapper.Shader.glUniform1f(ShaderCore.getDirectDrawProgram().location[1], 0.0f);
        if (!isMultiPassBloom) {
            GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
            GLWrapper.Operation.glBlendFuncSeparatei(0, GLWrapper.Operation.GL_ONE, GLWrapper.Operation.GL_ONE, GLWrapper.Operation.GL_ZERO, GLWrapper.Operation.GL_ONE);
            GLWrapper.Operation.glBlendEquationi(0, GLWrapper.Operation.GL_FUNC_ADD);
        }
        ShaderCore.getDefaultQuadObject().glDraw();
        resultProgram.bindTexture2D(0, 0);
        resultProgram.close();
        ShaderCore.getDefaultQuadObject().glReleaseBind();
    }

    public static boolean isHaveBloomPass() {
        return _DO_BLOOM;
    }

    public static void resetBloomPass() {
        _DO_BLOOM = false;
    }

    public static void activateBloomPass() {
        _DO_BLOOM = true;
    }

    private StandardShaderpacksPass() {}
}
