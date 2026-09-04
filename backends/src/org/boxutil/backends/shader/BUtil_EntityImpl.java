package org.boxutil.backends.shader;

import com.fs.starfarer.api.combat.ViewportAPI;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.base.api.ControlDataAPI;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.base.api.MaterialRenderAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.units.standard.entity.*;
import org.boxutil.units.standard.light.*;
import org.boxutil.util.CalculateUtil;

import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class BUtil_EntityImpl {
    private BUtil_EntityImpl() {}

    public final static class Mesh {
        private static void processCommonEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            MaterialData material;
            ControlDataAPI data;
            MemoryBlock memory;
            CommonEntity commonEntity;
            GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_CULL_FACE);
            GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_DEPTH_TEST);
            BaseShaderData program = ShaderCore.getCommonProgram();
            program.active();
            program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 0);
            boolean lastStyleBit = true;
            program.putUniformSubroutine(GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER, 1, 0);
            int instanceBit, dataBit;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                commonEntity = (CommonEntity) entity;
                material = ((MaterialRenderAPI) entity).getMaterialData();
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData();

                if (validInstanceData) {
                    if (instance.getRenderingCount() < 1) {
                        BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    instanceBit = memory.is_type_2D() ? 1 : 3;
                    if (memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                }
                if (commonEntity.isCommonDraw() != lastStyleBit) {
                    lastStyleBit = commonEntity.isCommonDraw();
                    program.putUniformSubroutine(GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER, 1, lastStyleBit ? 0 : 1);
                }

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GLWrapper.Shader.glUniform4(program.location[1], entity.pickDataPackage_vec4());
                GLWrapper.Shader.glUniform3f(program.location[2], commonEntity.getBaseSizeX(), commonEntity.getBaseSizeY(), commonEntity.getBaseSizeZ());
                dataBit = layerBits;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GLWrapper.Shader.glUniform3ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit, validInstanceData ? memory.address_instance() + instance.getRenderingOffset() : 0);
                material.putShaderTexture();
                commonEntity.getModel().putTBNShaderData();

                BUtil_GLImpl.glMaterialEntityDraw(entity, material);

                if (validInstanceData) program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 0);

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
            GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_DEPTH_TEST);
            GLWrapper.Operation.glDisable(GLWrapper.Operation.GL_CULL_FACE);
        }

        private static void processSpriteEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            MaterialData material;
            ControlDataAPI data;
            MemoryBlock memory;
            SpriteEntity spriteEntity;
            BaseShaderData program = ShaderCore.getSpriteProgram();
            ShaderCore.getDefaultQuadObject().glBind();
            program.active();
            int[] vertexSub = new int[2];
            vertexSub[0] = program.subroutineLocation[0][0];
            vertexSub[1] = program.subroutineLocation[0][4];
            program.putUniformSubroutines(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, vertexSub);
            int instanceBit = 4, dataBit;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                material = ((MaterialRenderAPI) entity).getMaterialData();
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                spriteEntity = (SpriteEntity) entity;
                final boolean haveTiles = spriteEntity.isTilesRendering(),
                        validInstanceData = instance.haveValidInstanceData(),
                        notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                if (validInstanceData && instance.getRenderingCount() < 1) {
                    BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                    continue;
                }

                if (haveTiles) vertexSub[0] = spriteEntity.isRandomTile() ? program.subroutineLocation[0][2] : program.subroutineLocation[0][1];
                if (notDefaultInstanceDataType) {
                    if (!validInstanceData) instanceBit = 3; else if (!memory.is_type_2D()) instanceBit = 6;
                    if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                    vertexSub[1] = program.subroutineLocation[0][instanceBit];
                }
                if (haveTiles || notDefaultInstanceDataType) program.putUniformSubroutines(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, vertexSub);

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GLWrapper.Shader.glUniform4(program.location[1], entity.pickDataPackage_vec4());
                dataBit = layerBits;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GLWrapper.Shader.glUniform3ui(program.location[2], material.isAdditionEmissive() ? 1 : 0, dataBit, validInstanceData ? memory.address_instance() + instance.getRenderingOffset() : 0);
                GLWrapper.Shader.glUniform1f(program.location[3], validInstanceData ? instance.getInstanceTimerOverride() : entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                BUtil_GLImpl.glMaterialEntityFlatDraw(entity, material);

                if (haveTiles) vertexSub[0] = program.subroutineLocation[0][0];
                if (notDefaultInstanceDataType) {
                    instanceBit = 4;
                    vertexSub[1] = program.subroutineLocation[0][instanceBit];
                }
                if (haveTiles || notDefaultInstanceDataType) program.putUniformSubroutines(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, vertexSub);

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        private static void processCurveEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            MaterialData material;
            ControlDataAPI data;
            MemoryBlock memory;
            CurveEntity curveEntity;
            float uvFlow;
            BaseShaderData program = ShaderCore.getCurveProgram();
            program.active();
            program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 0);
            GLWrapper.Shader.Tess.glPatchParameteri(GLWrapper.Shader.Tess.GL_PATCH_VERTICES, 2);
            int instanceBit, dataBit;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                curveEntity = (CurveEntity) entity;
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!curveEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstance = instance.haveValidInstanceData();

                if (validInstance) {
                    if (instance.getRenderingCount() < 1) {
                        BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    instanceBit = memory.is_type_2D() ? 1 : 3;
                    if (memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                    GLWrapper.Shader.glUniform1i(program.location[5], memory.address_instance() + instance.getRenderingOffset());
                }

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (curveEntity.getTextureSpeed() == 0.0f || curveEntity.getTexturePixels() == 0.0f) ? 0.0f : curveEntity.getTextureSpeed() / curveEntity.getTexturePixels() * (curveEntity.isFlowWhenPaused() ? BUtil_GLImpl.getElapsedTime() : BUtil_GLImpl.getElapsedTimeWithoutPaused());
                buffer.put(15, CalculateUtil.fraction(uvFlow + curveEntity.getUVOffset()));
                GLWrapper.Shader.glUniform4(program.location[1], buffer);
                GLWrapper.Shader.glUniform1f(program.location[2], curveEntity.getValidNodeCount() - 1);
                dataBit = layerBits;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GLWrapper.Shader.glUniform2ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GLWrapper.Shader.glUniform1f(program.location[4], validInstance ? instance.getInstanceTimerOverride() : entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                BUtil_GLImpl.glMaterialEntityFlatDraw(entity, material);

                if (validInstance) program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 0);

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        private static void processSegmentEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            MaterialData material;
            ControlDataAPI data;
            SegmentEntity segmentEntity;
            float uvFlow;
            BaseShaderData program = ShaderCore.getSegmentProgram();
            program.active();
            GLWrapper.Shader.Tess.glPatchParameteri(GLWrapper.Shader.Tess.GL_PATCH_VERTICES, 2);
            int dataBit;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                segmentEntity = (SegmentEntity) entity;
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!segmentEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (segmentEntity.getTextureSpeed() == 0.0f || segmentEntity.getTexturePixels() == 0.0f) ? 0.0f : segmentEntity.getTextureSpeed() / segmentEntity.getTexturePixels() * (segmentEntity.isFlowWhenPaused() ? BUtil_GLImpl.getElapsedTime() : BUtil_GLImpl.getElapsedTimeWithoutPaused());
                buffer.put(15, CalculateUtil.fraction(uvFlow + segmentEntity.getUVOffset()));
                GLWrapper.Shader.glUniform4(program.location[1], buffer);
                dataBit = layerBits;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GLWrapper.Shader.glUniform2ui(program.location[2], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GLWrapper.Shader.glUniform1f(program.location[3], entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                BUtil_GLImpl.glMaterialEntityFlatDraw(entity, material);

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        private static void processTrailEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            MaterialData material;
            ControlDataAPI data;
            TrailEntity trailEntity;
            float uvFlow, flickValue;
            BaseShaderData program = ShaderCore.getTrailProgram();
            ShaderCore.getDefaultLineObject().glBind();
            program.active();
            boolean lastStyleBit = true;
            program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 0);
            int dataBit;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                trailEntity = (TrailEntity) entity;
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!trailEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();

                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (trailEntity.getTextureSpeed() == 0.0f || trailEntity.getTexturePixels() == 0.0f) ? 0.0f : trailEntity.getTextureSpeed() / trailEntity.getTexturePixels() * (trailEntity.isFlowWhenPaused() ? BUtil_GLImpl.getElapsedTime() : BUtil_GLImpl.getElapsedTimeWithoutPaused());
                buffer.put(12, CalculateUtil.fraction(uvFlow + trailEntity.getUVOffset()));
                GLWrapper.Shader.glUniform4(program.location[1], buffer);

                flickValue = trailEntity.getCurrentFlickerSyncValue() + (trailEntity.isFlickWhenPaused() ? BUtil_GLImpl.getElapsedTime() : BUtil_GLImpl.getElapsedTimeWithoutPaused());
                if (!trailEntity.isSyncFlick()) flickValue = -flickValue;
                GLWrapper.Shader.glUniform3f(program.location[2], trailEntity.getCurrentFlickerSyncValue(), flickValue, entity.getGlobalTimerAlpha());

                dataBit = layerBits;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GLWrapper.Shader.glUniform2ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                trailEntity.putShaderTrailData();
                material.putShaderTexture();
                if (trailEntity.isStripLineMode() != lastStyleBit) {
                    lastStyleBit = trailEntity.isStripLineMode();
                    program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, lastStyleBit ? 0 : 1);
                }

                BUtil_GLImpl.glMaterialEntityFlatDraw(entity, material);
                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        private static void processFlareEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            ControlDataAPI data;
            MemoryBlock memory;
            FlareEntity flareEntity;
            BaseShaderData program = ShaderCore.getFlareProgram();
            ShaderCore.getDefaultQuadObject().glBind();
            program.active();
            int instanceBit = 1;
            byte lastStyleBit = 0;
            program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
            program.putUniformSubroutine(GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER, 1, lastStyleBit);
            GLWrapper.Shader.glUniform1ui(program.location[2], layerBits | 0b10);
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                instance = (InstanceRenderAPI) entity;
                flareEntity = (FlareEntity) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData(),
                        notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                if (validInstanceData && instance.getRenderingCount() < 1) {
                    BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                    continue;
                }

                if (flareEntity.getStyleBit() != lastStyleBit) {
                    lastStyleBit = flareEntity.getStyleBit();
                    program.putUniformSubroutine(GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER, 1, lastStyleBit);
                }
                if (notDefaultInstanceDataType) {
                    if (!validInstanceData) instanceBit = 0; else if (!memory.is_type_2D()) instanceBit = 3;
                    if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                }
                if (validInstanceData) GLWrapper.Shader.glUniform1i(program.location[3], memory.address_instance() + instance.getRenderingOffset());

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                float flickerTime = flareEntity.isFlickWhenPaused() ? BUtil_GLImpl.getElapsedTime() : BUtil_GLImpl.getElapsedTimeWithoutPaused();
                flickerTime *= flareEntity.getFlickerAnimationRateMulti();
                buffer.put(15, flickerTime);
                GLWrapper.Shader.glUniform4(program.location[1], buffer);

                BUtil_GLImpl.glEntityDraw(entity);

                if (notDefaultInstanceDataType) {
                    instanceBit = 1;
                    program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                }

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        private static void processTextFieldEntity(List<RenderDataAPI> list, int layerBits) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            ControlDataAPI data;
            TextFieldEntity textFieldEntity;
            BaseShaderData program = ShaderCore.getTextProgram();
            program.active();
            GLWrapper.Shader.glUniform1ui(program.location[7], layerBits | 0b11);
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                textFieldEntity = (TextFieldEntity) entity;
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!textFieldEntity.isValidRenderingTextField()) continue;

                GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GLWrapper.Shader.glUniform1f(program.location[5], textFieldEntity.getCurrentItalicFactor());
                GLWrapper.Shader.glUniform4(program.location[6], textFieldEntity.pickDataPackage_vec4());
                GLWrapper.Shader.glUniform2f(program.location[8], textFieldEntity.isBlendBloomColor() ? 1.0f : 0.0f, textFieldEntity.getGlobalTimerAlpha());
                textFieldEntity.putShaderTextures();

                BUtil_GLImpl.glEntityDraw(entity);
                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processDistortionEntity(boolean canRendering, List<RenderDataAPI> list) {
            if (list == null || list.isEmpty()) return;
            if (canRendering) {
                RenderDataAPI entity;
                InstanceRenderAPI instance;
                ControlDataAPI data;
                MemoryBlock memory;
                BaseShaderData program = ShaderCore.getDistortionProgram();
                ShaderCore.getDefaultQuadObject().glBind();
                program.active();

                BUtil_GLImpl.glScreenBlit();

                program.bindTexture2D(0, ShaderCore.getRenderingBuffer().getColorResult());
                program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, 1);
                int instanceBit = 1;
                for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                    entity = entitiesI.next();
                    if (entity == null) {
                        entitiesI.remove();
                        continue;
                    }
                    if (entity.hasDelete()) continue;
                    data = entity.getControlData();
                    if (data != null) {
                        data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                        if (!data.controlCanRenderNow(entity)) continue;
                    }
                    instance = (InstanceRenderAPI) entity;
                    memory = instance.getInstanceDataMemory();
                    final boolean validInstanceData = instance.haveValidInstanceData(),
                            notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                    if (validInstanceData && instance.getRenderingCount() < 1) {
                        BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    if (notDefaultInstanceDataType) {
                        if (!validInstanceData) instanceBit = 0; else if (!memory.is_type_2D()) instanceBit = 3;
                        if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                        program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                    }
                    if (validInstanceData) GLWrapper.Shader.glUniform1i(program.location[2], memory.address_instance() + instance.getRenderingOffset());
                    GLWrapper.Shader.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                    GLWrapper.Shader.glUniform4(program.location[1], entity.pickDataPackage_vec4());

                    BUtil_GLImpl.matrixCheck(entity.getPrimeMatrixState(), entity.pickPrimeMatrixPackage_mat4());
                    entity.glDraw();

                    if (notDefaultInstanceDataType) {
                        instanceBit = 1;
                        program.putUniformSubroutine(GLWrapper.Shader.Vert.GL_VERTEX_SHADER, 0, instanceBit);
                    }

                    BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                }
                program.close();
            } else BUtil_GLImpl.glDisabledIterator(list);
        }

        private static void forRenderingPlugins(Set<LayeredRenderingPlugin> renderingPlugins, Object layer, int layerBit, boolean notMultiPass, ViewportAPI viewport) {
            if (renderingPlugins == null || renderingPlugins.isEmpty()) return;

            final boolean framebufferValid = ShaderCore.getRenderingBuffer() != null && notMultiPass;
            LayeredRenderingPlugin plugin;
            for (Iterator<LayeredRenderingPlugin> pluginI = renderingPlugins.iterator(); pluginI.hasNext();) {
                plugin = pluginI.next();
                if (plugin == null) {
                    pluginI.remove();
                    continue;
                }
                if (plugin.isExpired()) {
                    pluginI.remove();
                    continue;
                }
                BUtil_GLImpl.applyLayeredRenderingPlugin(plugin, layer, layerBit, framebufferValid, viewport);
            }
        }

        private static void processStaticTrail(int layerBits, int layerLoc) {
            if (BUtil_StaticTrailMemoryPool.bypassDrawTrail(layerLoc)) return;

            final var program = ShaderCore.getStaticTrailProgram();
            program.active();
            BUtil_GLImpl.cullCheck(BoxEnum.MATERIAL_CULL_DISABLED);
            BUtil_GLImpl.matrixCheck(BoxEnum.ENTITY_VANILLA_PRIME_MATRIX, null);
            BUtil_StaticTrailMemoryPool.drawEachTrail(program, layerLoc, layerBits);
            program.close();
        }

        public static void processMeshCurrentLayout(int layerBits, Object layer, int layerLoc, boolean canRendering, ViewportAPI viewport, EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, Set<LayeredRenderingPlugin> renderingPlugins) {
            if (meshMap != null) {
                if (canRendering) {
                    processCommonEntity(meshMap.get(LayeredEntityType.COMMON), layerBits);
                    processSpriteEntity(meshMap.get(LayeredEntityType.SPRITE), layerBits);
                    processCurveEntity(meshMap.get(LayeredEntityType.CURVE), layerBits);
                    processSegmentEntity(meshMap.get(LayeredEntityType.SEGMENT), layerBits);
                    processTrailEntity(meshMap.get(LayeredEntityType.TRAIL), layerBits);
                    processFlareEntity(meshMap.get(LayeredEntityType.FLARE), layerBits);
                    processTextFieldEntity(meshMap.get(LayeredEntityType.TEXT), layerBits);
                } else for (List<RenderDataAPI> entity : meshMap.values()) BUtil_GLImpl.glDisabledIterator(entity);
            }
            forRenderingPlugins(renderingPlugins, layer, layerBits, canRendering, viewport);
            if (canRendering && BoxConfigs.isTrailSystemEnable()) processStaticTrail(layerBits, layerLoc); // must be after all
        }

        private Mesh() {}
    }


    public final static class Illumination {
        private interface _FuncPtr {
            void render(BaseShaderPacksContext context, RenderDataAPI entity, boolean validInstanceData, boolean notDefaultInstanceDataType);
        }

        private static void processLightFramework(List<RenderDataAPI> list, BaseShaderPacksContext context, _FuncPtr ptr) {
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            ControlDataAPI data;
            MemoryBlock memory;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, BUtil_GLImpl.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData(),
                        notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                if (validInstanceData && instance.getRenderingCount() < 1) {
                    BUtil_GLImpl.removeCheck(entitiesI, data, entity);
                    continue;
                }

                ptr.render(context, entity, validInstanceData, notDefaultInstanceDataType);

                BUtil_GLImpl.removeCheck(entitiesI, data, entity);
            }
        }

        private static void processInfiniteLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyInfiniteLightShading((InfiniteLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        private static void processPointLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyPointLightShading((PointLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        private static void processSpotLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applySpotLightShading((SpotLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        private static void processLinearLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyLinearLightShading((LinearLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        private static void processAreaLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyAreaLightShading((AreaLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processIlluminationPass(boolean beautyOrBloom, boolean canIllumination, EnumMap<DirectEntityType, List<RenderDataAPI>> entities, ViewportAPI viewport, final boolean isCampaign, BaseShaderPacksContext context) {
            if (beautyOrBloom) BUtil_GLImpl.applyBeforeIlluminationPass(context, viewport, isCampaign);
            if (canIllumination) {
                if (context.applyBeforeInfiniteLightShading()) processInfiniteLight(entities.get(DirectEntityType.INFINITE_LIGHT), context);
                if (context.applyBeforePointLightShading()) processPointLight(entities.get(DirectEntityType.POINT_LIGHT), context);
                if (context.applyBeforeSpotLightShading()) processSpotLight(entities.get(DirectEntityType.SPOT_LIGHT), context);
                if (context.applyBeforeLinearLightShading()) processLinearLight(entities.get(DirectEntityType.LINEAR_LIGHT), context);
                if (context.applyBeforeAreaLightShading()) processAreaLight(entities.get(DirectEntityType.AREA_LIGHT), context);
            } else {
                entities.forEach((type, list) -> {
                    if (!type.isIlluminant() || list == null) return;
                    BUtil_GLImpl.glDisabledIterator(list);
                });
            }
            if (beautyOrBloom) BUtil_GLImpl.applyAfterIlluminationPass(context, viewport, isCampaign);
        }

        private Illumination() {}
    }
}
