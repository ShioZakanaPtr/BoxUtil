package org.boxutil.backends.shader;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.base.api.*;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.*;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.ShaderCore;
import org.boxutil.backends.buffer.BUtil_RenderingBuffer;
import org.boxutil.units.standard.attribute.MaterialData;
import org.boxutil.units.standard.entity.*;
import org.boxutil.units.standard.light.*;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.TransformUtil;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public final class BUtil_GLImpl {
    private final static Matrix4f[] _ORTHO_VIEWPORT = new Matrix4f[]{new Matrix4f(), new Matrix4f()};
    private final static Matrix4f[] _PERSPECTIVE_VIEWPORT = new Matrix4f[]{new Matrix4f(), new Matrix4f()};
    private final static FloatBuffer _VANILLA_MATRIX = BufferUtils.createFloatBuffer(20);
    private final static FloatBuffer _PERSPECTIVE_MATRIX = BufferUtils.createFloatBuffer(20);
    private final static float[] _TIMER = new float[5]; // lastFrameAmount, frameTime, frameTimePausedCheck, fractionIncludePaused, fractionIncludePaused
    private final static int[] _FBO_RESOURCE = new int[10];
    private static GameState _LAST_GAME_STATE = GameState.TITLE;
    private static GameState _TITLE_LAST_GAME_STATE = GameState.TITLE;
    private static boolean _IN_CAMPAIGN_FLAG = false;
    private static boolean _CLEANUP_ILLUMINANT_AFTER_SHADERPACKS_SWITCH = false;

    public final static class MeshRender {
        public static void processCommonEntity(List<RenderDataAPI> list, int layer) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            InstanceRenderAPI instance;
            MaterialData material;
            ControlDataAPI data;
            MemoryBlock memory;
            CommonEntity commonEntity;
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            BaseShaderData program = ShaderCore.getCommonProgram();
            program.active();
            program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 0);
            boolean lastStyleBit = true;
            program.putUniformSubroutine(GL20.GL_FRAGMENT_SHADER, 1, 0);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                commonEntity = (CommonEntity) entity;
                material = ((MaterialRenderAPI) entity).getMaterialData();
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData();

                if (validInstanceData) {
                    if (instance.getRenderingCount() < 1) {
                        Operations.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    instanceBit = memory.is_type_2D() ? 1 : 3;
                    if (memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                }
                if (commonEntity.isCommonDraw() != lastStyleBit) {
                    lastStyleBit = commonEntity.isCommonDraw();
                    program.putUniformSubroutine(GL20.GL_FRAGMENT_SHADER, 1, lastStyleBit ? 0 : 1);
                }

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GL20.glUniform4(program.location[1], entity.pickDataPackage_vec4());
                GL20.glUniform3f(program.location[2], commonEntity.getBaseSizeX(), commonEntity.getBaseSizeY(), commonEntity.getBaseSizeZ());
                dataBit = layer;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GL30.glUniform3ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit, validInstanceData ? memory.address_instance() + instance.getRenderingOffset() : 0);
                material.putShaderTexture();
                commonEntity.getModel().putTBNShaderData();

                Operations.glMaterialEntityDraw(entity, material);

                if (validInstanceData) program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 0);

                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
        }

        public static void processSpriteEntity(List<RenderDataAPI> list, int layer) {
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
            program.putUniformSubroutines(GL20.GL_VERTEX_SHADER, 0, vertexSub);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
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
                    Operations.removeCheck(entitiesI, data, entity);
                    continue;
                }

                if (haveTiles) vertexSub[0] = spriteEntity.isRandomTile() ? program.subroutineLocation[0][2] : program.subroutineLocation[0][1];
                if (notDefaultInstanceDataType) {
                    if (!validInstanceData) instanceBit = 3; else if (!memory.is_type_2D()) instanceBit = 6;
                    if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                    vertexSub[1] = program.subroutineLocation[0][instanceBit];
                }
                if (haveTiles || notDefaultInstanceDataType) program.putUniformSubroutines(GL20.GL_VERTEX_SHADER, 0, vertexSub);

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GL20.glUniform4(program.location[1], entity.pickDataPackage_vec4());
                dataBit = layer;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GL30.glUniform3ui(program.location[2], material.isAdditionEmissive() ? 1 : 0, dataBit, validInstanceData ? memory.address_instance() + instance.getRenderingOffset() : 0);
                GL20.glUniform1f(program.location[3], validInstanceData ? instance.getInstanceTimerOverride() : entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                Operations.glMaterialEntityFlatDraw(entity, material);

                if (haveTiles) vertexSub[0] = program.subroutineLocation[0][0];
                if (notDefaultInstanceDataType) {
                    instanceBit = 4;
                    vertexSub[1] = program.subroutineLocation[0][instanceBit];
                }
                if (haveTiles || notDefaultInstanceDataType) program.putUniformSubroutines(GL20.GL_VERTEX_SHADER, 0, vertexSub);

                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processCurveEntity(List<RenderDataAPI> list, int layer) {
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
            program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 0);
            GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 2);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!curveEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstance = instance.haveValidInstanceData();

                if (validInstance) {
                    if (instance.getRenderingCount() < 1) {
                        Operations.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    instanceBit = memory.is_type_2D() ? 1 : 3;
                    if (memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                    GL20.glUniform1i(program.location[5], memory.address_instance() + instance.getRenderingOffset());
                }

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (curveEntity.getTextureSpeed() == 0.0f || curveEntity.getTexturePixels() == 0.0f) ? 0.0f : curveEntity.getTextureSpeed() / curveEntity.getTexturePixels() * (curveEntity.isFlowWhenPaused() ? Operations.getFrameTime() : Operations.getFrameTimeWithoutPaused());
                buffer.put(15, CalculateUtil.fraction(uvFlow + curveEntity.getUVOffset()));
                GL20.glUniform4(program.location[1], buffer);
                GL20.glUniform1f(program.location[2], curveEntity.getValidNodeCount() - 1);
                dataBit = layer;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GL30.glUniform2ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GL20.glUniform1f(program.location[4], validInstance ? instance.getInstanceTimerOverride() : entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                Operations.glMaterialEntityFlatDraw(entity, material);

                if (validInstance) program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 0);

                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processSegmentEntity(List<RenderDataAPI> list, int layer) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            MaterialData material;
            ControlDataAPI data;
            SegmentEntity segmentEntity;
            float uvFlow;
            BaseShaderData program = ShaderCore.getSegmentProgram();
            program.active();
            GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 2);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!segmentEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (segmentEntity.getTextureSpeed() == 0.0f || segmentEntity.getTexturePixels() == 0.0f) ? 0.0f : segmentEntity.getTextureSpeed() / segmentEntity.getTexturePixels() * (segmentEntity.isFlowWhenPaused() ? Operations.getFrameTime() : Operations.getFrameTimeWithoutPaused());
                buffer.put(15, CalculateUtil.fraction(uvFlow + segmentEntity.getUVOffset()));
                GL20.glUniform4(program.location[1], buffer);
                dataBit = layer;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GL30.glUniform2ui(program.location[2], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GL20.glUniform1f(program.location[3], entity.getGlobalTimerAlpha());
                material.putShaderTexture();

                Operations.glMaterialEntityFlatDraw(entity, material);

                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processTrailEntity(List<RenderDataAPI> list, int layer) {
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
            program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 0);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!trailEntity.isHaveValidNodeCount()) continue;
                material = ((MaterialRenderAPI) entity).getMaterialData();

                FloatBuffer buffer = entity.pickDataPackage_vec4();
                uvFlow = (trailEntity.getTextureSpeed() == 0.0f || trailEntity.getTexturePixels() == 0.0f) ? 0.0f : trailEntity.getTextureSpeed() / trailEntity.getTexturePixels() * (trailEntity.isFlowWhenPaused() ? Operations.getFrameTime() : Operations.getFrameTimeWithoutPaused());
                buffer.put(12, CalculateUtil.fraction(uvFlow + trailEntity.getUVOffset()));
                GL20.glUniform4(program.location[1], buffer);

                flickValue = trailEntity.getCurrentFlickerSyncValue() + (trailEntity.isFlickWhenPaused() ? Operations.getFrameTime() : Operations.getFrameTimeWithoutPaused());
                if (!trailEntity.isSyncFlick()) flickValue = -flickValue;
                GL20.glUniform3f(program.location[2], trailEntity.getCurrentFlickerSyncValue(), flickValue, entity.getGlobalTimerAlpha());

                dataBit = layer;
                if (material.isIgnoreIllumination()) dataBit |= 0b10;
                if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
                GL30.glUniform2ui(program.location[3], material.isAdditionEmissive() ? 1 : 0, dataBit);
                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                trailEntity.putShaderTrailData();
                material.putShaderTexture();
                if (trailEntity.isStripLineMode() != lastStyleBit) {
                    lastStyleBit = trailEntity.isStripLineMode();
                    program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, lastStyleBit ? 0 : 1);
                }

                Operations.glMaterialEntityFlatDraw(entity, material);
                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processFlareEntity(List<RenderDataAPI> list, int layer) {
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
            program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
            program.putUniformSubroutine(GL20.GL_FRAGMENT_SHADER, 1, lastStyleBit);
            GL30.glUniform1ui(program.location[2], layer | 0b10);
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                instance = (InstanceRenderAPI) entity;
                flareEntity = (FlareEntity) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData(),
                        notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                if (validInstanceData && instance.getRenderingCount() < 1) {
                    Operations.removeCheck(entitiesI, data, entity);
                    continue;
                }

                if (flareEntity.getStyleBit() != lastStyleBit) {
                    lastStyleBit = flareEntity.getStyleBit();
                    program.putUniformSubroutine(GL20.GL_FRAGMENT_SHADER, 1, lastStyleBit);
                }
                if (notDefaultInstanceDataType) {
                    if (!validInstanceData) instanceBit = 0; else if (!memory.is_type_2D()) instanceBit = 3;
                    if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                    program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                }
                if (validInstanceData) GL20.glUniform1i(program.location[3], memory.address_instance() + instance.getRenderingOffset());

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                FloatBuffer buffer = entity.pickDataPackage_vec4();
                float flickerTime = flareEntity.isFlickWhenPaused() ? Operations.getFrameTime() : Operations.getFrameTimeWithoutPaused();
                flickerTime *= flareEntity.getFlickerAnimationRateMulti();
                buffer.put(15, flickerTime);
                GL20.glUniform4(program.location[1], buffer);

                Operations.glEntityDraw(entity);

                if (notDefaultInstanceDataType) {
                    instanceBit = 1;
                    program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                }

                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processTextFieldEntity(List<RenderDataAPI> list, int layer) {
            if (list == null || list.isEmpty()) return;
            RenderDataAPI entity;
            ControlDataAPI data;
            TextFieldEntity textFieldEntity;
            BaseShaderData program = ShaderCore.getTextProgram();
            program.active();
            GL30.glUniform1ui(program.location[7], layer | 0b11);
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                if (!textFieldEntity.isValidRenderingTextField()) continue;

                GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                GL20.glUniform1f(program.location[5], textFieldEntity.getCurrentItalicFactor());
                GL20.glUniform4(program.location[6], textFieldEntity.pickColorPackage_vec4());
                GL20.glUniform1i(program.location[8], textFieldEntity.isBlendBloomColor() ? 1 : 0);
                for (int i = 0; i < textFieldEntity.getFontMapArray().length; i++) {
                    if (textFieldEntity.getFontMapArray()[i] != null && textFieldEntity.getFontMapArray()[i].isValid()) program.bindTexture2D(i, textFieldEntity.getFontMapArray()[i].getMapID());
                }

                Operations.glEntityDraw(entity);
                program.active();
                Operations.removeCheck(entitiesI, data, entity);
            }
            program.close();
        }

        public static void processDistortionEntity(boolean canRendering, List<RenderDataAPI> list, ViewportAPI viewport) {
            if (list == null || list.isEmpty()) return;
            if (canRendering) {
                RenderDataAPI entity;
                InstanceRenderAPI instance;
                ControlDataAPI data;
                MemoryBlock memory;
                BaseShaderData program = ShaderCore.getDistortionProgram();
                ShaderCore.getDefaultQuadObject().glBind();
                program.active();

                Operations.glScreenBlit();

                program.bindTexture2D(0, ShaderCore.getRenderingBuffer().getColorResult());
                program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, 1);
                GL20.glUniform1f(program.location[2], viewport.getViewMult());
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
                        data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                        if (!data.controlCanRenderNow(entity)) continue;
                    }
                    instance = (InstanceRenderAPI) entity;
                    memory = instance.getInstanceDataMemory();
                    final boolean validInstanceData = instance.haveValidInstanceData(),
                            notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                    if (validInstanceData && instance.getRenderingCount() < 1) {
                        Operations.removeCheck(entitiesI, data, entity);
                        continue;
                    }

                    if (notDefaultInstanceDataType) {
                        if (!validInstanceData) instanceBit = 0; else if (!memory.is_type_2D()) instanceBit = 3;
                        if (validInstanceData && memory.is_type_fixed()) ++instanceBit;
                        program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                    }
                    if (validInstanceData) GL20.glUniform1i(program.location[3], memory.address_instance() + instance.getRenderingOffset());
                    GL20.glUniformMatrix4(program.location[0], false, entity.pickModelMatrixPackage_mat4());
                    GL20.glUniform4(program.location[1], entity.pickDataPackage_vec4());

                    Operations.matrixCheckA(entity);
                    entity.glDraw();
                    Operations.matrixCheckB(entity);

                    if (notDefaultInstanceDataType) {
                        instanceBit = 1;
                        program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 0, instanceBit);
                    }

                    Operations.removeCheck(entitiesI, data, entity);
                }
                program.close();
            } else Operations.glDisabledIterator(list);
        }

        private MeshRender() {}
    }

    public final static class IlluminationRender {
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
                    data.controlBeforeRenderingAdvance(entity, Operations.getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                instance = (InstanceRenderAPI) entity;
                memory = instance.getInstanceDataMemory();
                final boolean validInstanceData = instance.haveValidInstanceData(),
                        notDefaultInstanceDataType = !validInstanceData || !memory.is_type_2D() || memory.is_type_fixed();

                if (validInstanceData && instance.getRenderingCount() < 1) {
                    Operations.removeCheck(entitiesI, data, entity);
                    continue;
                }

                ptr.render(context, entity, validInstanceData, notDefaultInstanceDataType);

                Operations.removeCheck(entitiesI, data, entity);
            }
        }

        public static void processInfiniteLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyInfiniteLightShading((InfiniteLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processPointLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyPointLightShading((PointLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processSpotLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applySpotLightShading((SpotLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processLinearLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyLinearLightShading((LinearLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processAreaLight(List<RenderDataAPI> list, BaseShaderPacksContext context) {
            if (list == null || list.isEmpty()) return;
            processLightFramework(list, context, (context1, entity, validInstanceData, notDefaultInstanceDataType) -> context1.applyAreaLightShading((AreaLight) entity, validInstanceData, notDefaultInstanceDataType));
        }

        public static void processIlluminationPass(boolean beautyOrBloom, boolean canIllumination, EnumMap<DirectEntityType, List<RenderDataAPI>> entities, ViewportAPI viewport, final boolean isCampaign, BaseShaderPacksContext context) {
            if (beautyOrBloom) BUtil_GLImpl.Operations.applyBeforeIlluminationPass(context, viewport, isCampaign);
            if (canIllumination) {
                if (context.applyBeforeInfiniteLightShading()) BUtil_GLImpl.IlluminationRender.processInfiniteLight(entities.get(DirectEntityType.INFINITE_LIGHT), context);
                if (context.applyBeforePointLightShading()) BUtil_GLImpl.IlluminationRender.processPointLight(entities.get(DirectEntityType.POINT_LIGHT), context);
                if (context.applyBeforeSpotLightShading()) BUtil_GLImpl.IlluminationRender.processSpotLight(entities.get(DirectEntityType.SPOT_LIGHT), context);
                if (context.applyBeforeLinearLightShading()) BUtil_GLImpl.IlluminationRender.processLinearLight(entities.get(DirectEntityType.LINEAR_LIGHT), context);
                if (context.applyBeforeAreaLightShading()) BUtil_GLImpl.IlluminationRender.processAreaLight(entities.get(DirectEntityType.AREA_LIGHT), context);
            } else {
                entities.forEach((type, list) -> {
                    if (!type.isIlluminant() || list == null) return;
                    BUtil_GLImpl.Operations.glDisabledIterator(list);
                });
            }
            if (beautyOrBloom) BUtil_GLImpl.Operations.applyAfterIlluminationPass(context, viewport, isCampaign);
        }

        public static void processResultPass(ViewportAPI viewport, final boolean isCampaign, BaseShaderPacksContext context) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            ShaderCore.glMultiPass();
            if (BoxConfigs.isMultiPassBeauty() && context.isAASupported()) {
                context.applyAAPass(viewport, isCampaign,
                        _FBO_RESOURCE[0],
                        _FBO_RESOURCE[1],
                        _FBO_RESOURCE[2],
                        _FBO_RESOURCE[3],
                        _FBO_RESOURCE[4],
                        _FBO_RESOURCE[5],
                        _FBO_RESOURCE[6],
                        _FBO_RESOURCE[7]);
            }
            if (BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassBloom()) {
                if (context.isBloomSupported()) {
                    context.applyBloomPass(viewport, isCampaign, BoxConfigs.isMultiPassBloom(),
                            _FBO_RESOURCE[0],
                            _FBO_RESOURCE[1],
                            _FBO_RESOURCE[2],
                            _FBO_RESOURCE[3],
                            _FBO_RESOURCE[4],
                            _FBO_RESOURCE[5],
                            _FBO_RESOURCE[6],
                            _FBO_RESOURCE[7],
                            _FBO_RESOURCE[8],
                            _FBO_RESOURCE[9]);
                }
            }
        }

        private IlluminationRender() {}
    }

    public final static class Operations {
        public static void forRenderingPlugins(Set<LayeredRenderingPlugin> pluginSet, Object layer, int layerBit, boolean notMultiPass, ViewportAPI viewport) {
            final boolean framebufferValid = ShaderCore.getRenderingBuffer() != null && notMultiPass;
            LayeredRenderingPlugin plugin;
            for (Iterator<LayeredRenderingPlugin> pluginI = pluginSet.iterator(); pluginI.hasNext();) {
                plugin = pluginI.next();
                if (plugin == null) {
                    pluginI.remove();
                    continue;
                }
                if (plugin.isExpired()) {
                    pluginI.remove();
                    continue;
                }
                BUtil_GLImpl.Operations.applyLayeredRenderingPlugin(plugin, layer, layerBit, framebufferValid, viewport);
            }
        }

        public static boolean checkSkipMeshCurrentLayout(EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, Set<LayeredRenderingPlugin> renderingPlugins) {
            boolean result = true;
            if (meshMap != null) {
                for (List<RenderDataAPI> list : meshMap.values()) {
                    if (list != null && !list.isEmpty()) {
                        result = false;
                        break;
                    }
                }
            }
            return result && (renderingPlugins == null || renderingPlugins.isEmpty());
        }

        public static void processMeshCurrentLayout(int layerBits, Object layer, boolean canRendering, ViewportAPI viewport, EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, Set<LayeredRenderingPlugin> renderingPlugins) {
            if (meshMap != null) {
                if (canRendering) {
                    BUtil_GLImpl.MeshRender.processCommonEntity(meshMap.get(LayeredEntityType.COMMON), layerBits);
                    BUtil_GLImpl.MeshRender.processSpriteEntity(meshMap.get(LayeredEntityType.SPRITE), layerBits);
                    BUtil_GLImpl.MeshRender.processCurveEntity(meshMap.get(LayeredEntityType.CURVE), layerBits);
                    BUtil_GLImpl.MeshRender.processSegmentEntity(meshMap.get(LayeredEntityType.SEGMENT), layerBits);
                    BUtil_GLImpl.MeshRender.processTrailEntity(meshMap.get(LayeredEntityType.TRAIL), layerBits);
                    BUtil_GLImpl.MeshRender.processFlareEntity(meshMap.get(LayeredEntityType.FLARE), layerBits);
                    BUtil_GLImpl.MeshRender.processTextFieldEntity(meshMap.get(LayeredEntityType.TEXT), layerBits);
                } else for (List<RenderDataAPI> entity : meshMap.values()) BUtil_GLImpl.Operations.glDisabledIterator(entity);
            }
            if (renderingPlugins != null && !renderingPlugins.isEmpty()) BUtil_GLImpl.Operations.forRenderingPlugins(renderingPlugins, layer, layerBits, canRendering, viewport);
        }

        public static void resetTimer() {
            _TIMER[0] = _TIMER[1] = _TIMER[2] = _TIMER[3] = _TIMER[4] = 0.0f;
        }
        
        public static void advanceTimer(float amount, boolean paused) {
            _TIMER[0] = amount;
            _TIMER[1] += amount;
            if (!paused) _TIMER[2] += amount;

            if (!paused) {
                _TIMER[3] += amount;
                _TIMER[3] -= (byte) _TIMER[3];
            }
            _TIMER[4] += amount;
            _TIMER[4] -= (byte) _TIMER[4];
        }

        public static float getLastFrameAmount() {
            return _TIMER[0];
        }

        public static float getFrameTime() {
            return _TIMER[1];
        }

        public static float getFrameTimeWithoutPaused() {
            return _TIMER[2];
        }

        public static float getElapsedTimeFraction() {
            return _TIMER[3];
        }

        public static float getElapsedTimeFractionIncludePaused() {
            return _TIMER[4];
        }

        public static void setCampaignFlag() {
            _IN_CAMPAIGN_FLAG = true;
        }

        public static boolean checkCampaignCleanup() {
            final GameState curr = Global.getCurrentState();
            boolean result = curr != _LAST_GAME_STATE && _IN_CAMPAIGN_FLAG;

            if (_IN_CAMPAIGN_FLAG && Global.getCombatEngine() != null && Global.getCombatEngine().isInCampaignSim()) {
                _LAST_GAME_STATE = GameState.COMBAT;
                return false;
            } else {
                if (_IN_CAMPAIGN_FLAG) {
                    if (curr == GameState.COMBAT) _TITLE_LAST_GAME_STATE = GameState.COMBAT;
                    if (curr == GameState.TITLE) {
                        _IN_CAMPAIGN_FLAG = false;
                        result = true;
                    }
                }
                _LAST_GAME_STATE = curr;
            }
            return result;
        }

        public static boolean checkTitleCleanup() {
            if (_IN_CAMPAIGN_FLAG) return false;
            _LAST_GAME_STATE = GameState.TITLE;
            final GameState curr = Global.getCurrentState();
            if (curr == GameState.TITLE && Global.getCombatEngine() != null && Global.getCombatEngine().isSimulation()) {
                _TITLE_LAST_GAME_STATE = GameState.COMBAT;
                return true;
            }
            if (curr != GameState.CAMPAIGN && _TITLE_LAST_GAME_STATE != curr) {
                _TITLE_LAST_GAME_STATE = curr;
                return true;
            } else return false;
        }

        public static void refreshFBOResource(BUtil_RenderingBuffer renderingBuffer) {
            if (renderingBuffer != null) {
                _FBO_RESOURCE[0] = renderingBuffer.getFBO(0);
                _FBO_RESOURCE[1] = renderingBuffer.getColorResult();
                _FBO_RESOURCE[2] = renderingBuffer.getEmissiveResult();
                _FBO_RESOURCE[3] = renderingBuffer.getWorldPosResult();
                _FBO_RESOURCE[4] = renderingBuffer.getNormalResult();
                _FBO_RESOURCE[5] = renderingBuffer.getTangentResult();
                _FBO_RESOURCE[6] = renderingBuffer.getMaterialResult();
                _FBO_RESOURCE[7] = renderingBuffer.getDataResult();
                _FBO_RESOURCE[8] = renderingBuffer.getFBO(1);
                _FBO_RESOURCE[9] = renderingBuffer.getAuxEmissiveResult();
            }
        }
        
        public static void refreshCurrFrameState(ViewportAPI viewport, BaseShaderPacksContext context, final byte isCampaign) {
            TransformUtil.createGameOrthoMatrix(viewport, _ORTHO_VIEWPORT[isCampaign]);
            TransformUtil.createGamePerspectiveMatrix(40.0f, viewport, _PERSPECTIVE_VIEWPORT[isCampaign]);
            _VANILLA_MATRIX.put(0, CommonUtil.getMatrix4fArray(_ORTHO_VIEWPORT[isCampaign]), 0, 16);
            _PERSPECTIVE_MATRIX.put(0, CommonUtil.getMatrix4fArray(_PERSPECTIVE_VIEWPORT[isCampaign]), 0, 16);
            _VANILLA_MATRIX.position(0);
            _VANILLA_MATRIX.limit(_VANILLA_MATRIX.capacity());
            _PERSPECTIVE_MATRIX.position(0);
            _PERSPECTIVE_MATRIX.limit(_PERSPECTIVE_MATRIX.capacity());
            if (BoxConfigs.isShaderEnable()) {
                ShaderCore.refreshGameVanillaViewportUBOAll(_VANILLA_MATRIX, viewport);
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GL41.glClearDepthf(1.0f);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(1));
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
                GL42.glMemoryBarrier(GL42.GL_BUFFER_UPDATE_BARRIER_BIT | GL43.GL_SHADER_STORAGE_BARRIER_BIT);
                for (InstanceType instanceType : InstanceType.values()) BUtil_InstanceDataMemoryPool.rebindSSBO(instanceType);
            }
            context.applyBeforeLowestLayerRender(viewport, isCampaign == BoxEnum.TRUE,
                    _FBO_RESOURCE[0],
                    _FBO_RESOURCE[1],
                    _FBO_RESOURCE[2],
                    _FBO_RESOURCE[3],
                    _FBO_RESOURCE[4],
                    _FBO_RESOURCE[5],
                    _FBO_RESOURCE[6],
                    _FBO_RESOURCE[7],
                    _FBO_RESOURCE[8],
                    _FBO_RESOURCE[9]);
        }

        public static void applyBeforeIlluminationPass(BaseShaderPacksContext context, ViewportAPI viewport, final boolean isCampaign) {
            context.applyBeforeIlluminationPass(viewport, isCampaign,
                    _FBO_RESOURCE[0],
                    _FBO_RESOURCE[1],
                    _FBO_RESOURCE[2],
                    _FBO_RESOURCE[3],
                    _FBO_RESOURCE[4],
                    _FBO_RESOURCE[5],
                    _FBO_RESOURCE[6],
                    _FBO_RESOURCE[7],
                    _FBO_RESOURCE[8],
                    _FBO_RESOURCE[9]);
        }

        public static void applyAfterIlluminationPass(BaseShaderPacksContext context, ViewportAPI viewport, final boolean isCampaign) {
            context.applyAfterIlluminationPass(viewport, isCampaign,
                    _FBO_RESOURCE[0],
                    _FBO_RESOURCE[1],
                    _FBO_RESOURCE[2],
                    _FBO_RESOURCE[3],
                    _FBO_RESOURCE[4],
                    _FBO_RESOURCE[5],
                    _FBO_RESOURCE[6],
                    _FBO_RESOURCE[7],
                    _FBO_RESOURCE[8],
                    _FBO_RESOURCE[9]);
        }

        public static void applyLayeredRenderingPlugin(LayeredRenderingPlugin plugin, Object layer, int layerBit, boolean framebufferValid, ViewportAPI viewport) {
            plugin.render(layer, layerBit, framebufferValid, viewport,
                    _FBO_RESOURCE[0],
                    _FBO_RESOURCE[1],
                    _FBO_RESOURCE[2],
                    _FBO_RESOURCE[3],
                    _FBO_RESOURCE[4],
                    _FBO_RESOURCE[5],
                    _FBO_RESOURCE[6],
                    _FBO_RESOURCE[7]);
        }

        public static void glMaterialEntityDraw(RenderDataAPI entity, MaterialData material) {
            cullCheckA(material);
            glEntityDraw(entity);
            cullCheckB(material);
        }

        public static void glMaterialEntityFlatDraw(RenderDataAPI entity, MaterialData material) {
            cullCheckA_B(material);
            glEntityDraw(entity);
            cullCheckB_B(material);
        }

        public static void glEntityDraw(RenderDataAPI entity) {
            matrixCheckA(entity);
            blendCheckA(entity);
            entity.glDraw();
            matrixCheckB(entity);
            blendCheckB(entity);
        }

        public static void matrixCheckA(RenderDataAPI entity) {
            switch (entity.getPrimeMatrixState()) {
                case 0:
                    break;
                case 1: {
                    ShaderCore.refreshGameViewportMatrix(_PERSPECTIVE_MATRIX);
                    break;
                }
                case 2: {
                    ShaderCore.refreshGameViewportMatrix(entity.pickPrimeMatrixPackage_mat4());
                    break;
                }
                default: {
                    ShaderCore.refreshGameViewportMatrixNone();
                }
            }
        }

        public static void matrixCheckB(RenderDataAPI entity) {
            if (entity.getPrimeMatrixState() != 0) ShaderCore.refreshGameViewportMatrix(_VANILLA_MATRIX);
        }

        public static void blendCheckA(RenderDataAPI entity) {
            switch (entity.getBlendState()) {
                case 0:
                    break;
                case 1: {
                    GL40.glBlendFunci(0, entity.getBlendColorSRC(), entity.getBlendColorDST());
                    GL40.glBlendFunci(1, entity.getBlendColorSRC(), entity.getBlendColorDST());
                    break;
                }
                case 2: {
                    GL40.glBlendFuncSeparatei(0, entity.getBlendColorSRC(), entity.getBlendColorDST(), entity.getBlendAlphaSRC(), entity.getBlendAlphaDST());
                    GL40.glBlendEquationi(0, entity.getBlendEquation());
                    GL40.glBlendFuncSeparatei(1, entity.getBlendColorSRC(), entity.getBlendColorDST(), entity.getBlendAlphaSRC(), entity.getBlendAlphaDST());
                    GL40.glBlendEquationi(1, entity.getBlendEquation());
                    break;
                }
                default: {
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }

        public static void blendCheckB(RenderDataAPI entity) {
            switch (entity.getBlendState()) {
                case 0:
                    break;
                case 1: {
                    GL40.glBlendFunci(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL40.glBlendFunci(1, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    break;
                }
                case 2: {
                    GL40.glBlendFuncSeparatei(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
                    GL40.glBlendEquationi(0, GL14.GL_FUNC_ADD);
                    GL40.glBlendFuncSeparatei(1, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
                    GL40.glBlendEquationi(1, GL14.GL_FUNC_ADD);
                    break;
                }
                default: {
                    GL11.glEnable(GL11.GL_BLEND);
                }
            }
        }

        public static void cullCheckA(MaterialData material) {
            switch (material.getCullFace()) {
                case 0: break;
                case 1: {
                    GL11.glCullFace(GL11.GL_FRONT);
                    break;
                }
                case 2: {
                    GL11.glCullFace(GL11.GL_FRONT_AND_BACK);
                    break;
                }
                default: {
                    GL11.glDisable(GL11.GL_CULL_FACE);
                }
            }
        }

        public static void cullCheckB(MaterialData material) {
            switch (material.getCullFace()) {
                case 0: break;
                case 1:
                case 2: {
                    GL11.glCullFace(GL11.GL_BACK);
                    break;
                }
                default: {
                    GL11.glEnable(GL11.GL_CULL_FACE);
                }
            }
        }

        public static void cullCheckA_B(MaterialData material) {
            if (material.getCullFace() != 3)  {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            switch (material.getCullFace()) {
                case 0:
                    GL11.glCullFace(GL11.GL_BACK);
                    break;
                case 1: {
                    GL11.glCullFace(GL11.GL_FRONT);
                    break;
                }
                case 2: {
                    GL11.glCullFace(GL11.GL_FRONT_AND_BACK);
                    break;
                }
            }
        }

        public static void cullCheckB_B(MaterialData material) {
            if (material.getCullFace() != 3)  {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
        }

        public static void removeCheck(Iterator<RenderDataAPI> iterator, ControlDataAPI data, RenderDataAPI entity) {
            boolean toRemove = false;
            if (data != null) {
                data.controlAfterRenderingAdvance(entity, getLastFrameAmount());
                if (data.controlIsOnceRender(entity)) {
                    entity.delete();
                    toRemove = true;
                }
            } else if (entity.isGlobalTimerOnce()) {
                toRemove = true;
            }
            if (toRemove) iterator.remove();
        }

        public static void glDisabledIterator(List<RenderDataAPI> list) {
            if (list.isEmpty()) return;
            RenderDataAPI entity;
            for (Iterator<RenderDataAPI> entitiesI = list.iterator(); entitiesI.hasNext();) {
                entity = entitiesI.next();
                if (entity == null) {
                    entitiesI.remove();
                    continue;
                }
                if (entity.hasDelete()) continue;
                ControlDataAPI data = entity.getControlData();
                if (data != null) {
                    data.controlBeforeRenderingAdvance(entity, getLastFrameAmount());
                    if (!data.controlCanRenderNow(entity)) continue;
                }
                removeCheck(entitiesI, data, entity);
            }
        }

        public static void glScreenBlit() {
            final int blitWidth = ShaderCore.getScreenScaleWidth();
            final int blitHeight = ShaderCore.getScreenScaleHeight();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));
            GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
            GL30.glBlitFramebuffer(0, 0, blitWidth, blitHeight, 0, 0, blitWidth, blitHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ShaderCore.getRenderingBuffer().getFBO(0));

            IntBuffer drawBuffer = ShaderCore.getRenderingBuffer().getDrawBufferConfig((byte) 0);
            drawBuffer.position(0);
            drawBuffer.limit(drawBuffer.capacity());
            GL20.glDrawBuffers(drawBuffer);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }

        public static void callIlluminantCleanupForShaderPacks(BaseShaderPacksContext curr, BaseShaderPacksContext toSwitch) {
            if (curr.haveCustomInstanceDataLayout() || toSwitch.haveCustomInstanceDataLayout()) _CLEANUP_ILLUMINANT_AFTER_SHADERPACKS_SWITCH = true;
        }

        public static boolean checkIlluminantCleanupForShaderPacks() {
            final boolean result = _CLEANUP_ILLUMINANT_AFTER_SHADERPACKS_SWITCH;
            _CLEANUP_ILLUMINANT_AFTER_SHADERPACKS_SWITCH = false;
            return result;
        }
        
        private Operations() {}
    }

    public final static class StandardShaderPacks {
        public static byte[] applyTexturedAreaLightPreFiltering(int src, int preFiltering, boolean shouldAllocate) {
            byte[] result = new byte[]{BoxEnum.STATE_SUCCESS, 0};
            final byte[] resultFailed = new byte[]{BoxEnum.STATE_SUCCESS, 0};
            if (!ShaderCore.isAreaLightTexValid() || !BoxConfigs.isGLParallelSupported() || !BoxConfigs.isShaderEnable()) return resultFailed;
            BaseShaderData program = ShaderCore.getAreaLightTex();
            final float divA = 1.0f / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f), divB = 1.0f / 8.0f;
            final int[][] size = new int[7][2];
            int itemDimX, itemDimY;
            byte level = 0, lod, step = 2;

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, src);
            size[0][0] = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            size[0][1] = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, preFiltering);
            while (level <= 6 && size[level][0] >= 4 && size[level][1] >= 4) {
                if (level > 0) {
                    size[level][0] = size[level - 1][0] / 2;
                    size[level][1] = size[level - 1][1] / 2;
                }
                if (shouldAllocate) {
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA8, size[level][0], size[level][1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                }
                ++level;
            }
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, level);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
            if (level < 1) return resultFailed;

            program.active();
            program.bindTexture2D(0, src);
            program.putUniformSubroutine(GL43.GL_COMPUTE_SHADER, 0, level == 1 ? 0 : 1);
            if (level == 1) {
                itemDimX = (int) Math.ceil(size[0][0] * divA);
                itemDimY = (int) Math.ceil(size[0][1] * divB);
                GL20.glUniform3i(program.location[0], size[0][0], size[0][1], step);
                program.putBindingImageTextureWriteOnly(0, preFiltering, GL11.GL_RGBA8);
                GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

            } else {
                for (byte i = 1; i < level; ++i) {
                    itemDimX = (int) Math.ceil(size[i][0] * divA);
                    itemDimY = (int) Math.ceil(size[i][1] * divB);
                    lod = (byte) (i - 1);
                    GL20.glUniform3i(program.location[0], size[i][0], size[i][1], step);
                    GL20.glUniform1i(program.location[1], 0);
                    GL20.glUniform4f(program.location[2], 1.0f / size[lod][0], 1.0f / size[lod][1], lod, 1.0f / (step * 0.1111111f * TrigUtil.PI_F));
                    GL42.glBindImageTexture(0, preFiltering, i, false, 0, GL15.GL_WRITE_ONLY, GL11.GL_RGBA8);
                    GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                    GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
                    GL20.glUniform1i(program.location[1], 1);
                    GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                    GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
                    if (i == 1) program.bindTexture2D(0, preFiltering);
                    ++step;
                }
            }
            program.bindTexture2D(0, 0);
            program.close();
            return result;
        }

        public static void applyFXAA(boolean enabled, boolean console, boolean useDepthBasedAA, int colorMap, int worldDataMap) {
            if (enabled && BoxConfigs.isShaderEnable()) {
                BaseShaderData program = console ? ShaderCore.getFXAAConsoleProgram() : ShaderCore.getFXAAQualityProgram();
                if (program == null || !program.isValid()) return;
                int[] subroutines = new int[]{
                        program.subroutineLocation[0][useDepthBasedAA ? 1 : 0],
                        program.subroutineLocation[0][BoxConfigs.isAAShowEdge() ? 3 : 2]
                };
                Operations.glScreenBlit();
                ShaderCore.getDefaultQuadObject().glBind();
                program.active();
                program.putUniformSubroutines(GL20.GL_FRAGMENT_SHADER, 0, subroutines);
                program.bindTexture2D(0, colorMap);
                program.bindTexture2D(1, worldDataMap);
                ShaderCore.getDefaultQuadObject().glDraw();
                program.bindTexture2D(0, 0);
                program.close();
                ShaderCore.getDefaultQuadObject().glReleaseBind();
            }
        }

        public static void applyBloom(boolean isMultiPassBloom, int emissiveMap, boolean withHighlightAdd, int highlightMap) {
            BaseShaderData program = ShaderCore.getBloomProgram();
            if (program == null || !program.isValid() || !BoxConfigs.isGLParallelSupported() || !BoxConfigs.isShaderEnable()) return;
            BUtil_RenderingBuffer renderingBuffer = ShaderCore.getRenderingBuffer();
            if (renderingBuffer.getLayerCount() < 2) return;
            final float divA = 1.0f / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f), divB = 1.0f / 8.0f;
            int itemDimX, itemDimY,
                    resultTex = emissiveMap,
                    widthCurr, heightCurr, widthNext, heightNext;

            program.active();
            if (withHighlightAdd) {
                GL20.glUniform1i(program.location[2], 1);
                program.putUniformSubroutine(GL43.GL_COMPUTE_SHADER, 0, 0);
                program.bindTexture2D(0, emissiveMap);
                program.bindTexture2D(1, highlightMap);
                program.putBindingImageTextureWriteOnly(1, emissiveMap, GL11.GL_RGB8);
                widthCurr = renderingBuffer.getScaleSize(0)[0];
                heightCurr = renderingBuffer.getScaleSize(0)[1];
                itemDimX = (int) Math.ceil(widthCurr * divA);
                itemDimY = (int) Math.ceil(heightCurr * divB);
                GL20.glUniform2i(program.location[0], widthCurr, heightCurr);
                GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
            }

            // down
            byte nextI;
            boolean notAvg = true;
            GL20.glUniform1i(program.location[2], 0);
            program.putUniformSubroutine(GL43.GL_COMPUTE_SHADER, 0, 1);
            for (byte i = 0; i < renderingBuffer.getLayerCount() - 1; ++i) {
                nextI = i;
                ++nextI;
                widthCurr = renderingBuffer.getScaleSize(i)[0];
                heightCurr = renderingBuffer.getScaleSize(i)[1];
                widthNext = renderingBuffer.getScaleSize(nextI)[0];
                heightNext = renderingBuffer.getScaleSize(nextI)[1];
                itemDimX = (int) Math.ceil(widthNext * divA);
                itemDimY = (int) Math.ceil(heightNext * divB);
                GL20.glUniform2i(program.location[0], widthNext, heightNext);
                GL20.glUniform4f(program.location[1], 1.0f / (widthCurr - 1), 1.0f / (heightCurr - 1), 1.0f / (widthNext - 1), 1.0f / (heightNext - 1));
                program.bindTexture2D(0, i == 0 ? emissiveMap : renderingBuffer.getBloomPingPongTex(i));
                program.putBindingImageTextureWriteOnly(0, renderingBuffer.getBloomPingPongTex(nextI), GL11.GL_RGB10_A2);
                GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
                if (notAvg) {
                    notAvg = false;
                    program.putUniformSubroutine(GL43.GL_COMPUTE_SHADER, 0, 2);
                }
            }

            // up
            program.putUniformSubroutine(GL43.GL_COMPUTE_SHADER, 0, 3);
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
                GL20.glUniform2i(program.location[0], widthNext, heightNext);
                GL20.glUniform4f(program.location[1], 1.0f / (widthCurr - 1), 1.0f / (heightCurr - 1), 1.0f / (widthNext - 1), 1.0f / (heightNext - 1));
                program.putBindingImageTextureWriteOnly(0, resultTex, GL11.GL_RGB10_A2);
                GL43.glDispatchCompute(itemDimX, itemDimY, 1);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
            }

            program = ShaderCore.getDirectDrawProgram();
            ShaderCore.getDefaultQuadObject().glBind();
            program.active();
            program.bindTexture2D(0, resultTex);
            GL20.glUniform1f(ShaderCore.getDirectDrawProgram().location[0], 1.0f);
            GL20.glUniform1f(ShaderCore.getDirectDrawProgram().location[1], 0.0f);
            if (!isMultiPassBloom) {
                GL11.glEnable(GL11.GL_BLEND);
                GL40.glBlendFuncSeparatei(0, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO, GL11.GL_ONE);
                GL40.glBlendEquationi(0, GL14.GL_FUNC_ADD);
            }
            ShaderCore.getDefaultQuadObject().glDraw();
            program.bindTexture2D(0, 0);
            program.close();
            ShaderCore.getDefaultQuadObject().glReleaseBind();
        }

        private StandardShaderPacks() {}
    }

    public static Matrix4f getGameOrthoViewport(byte isCampaign) {
        return _ORTHO_VIEWPORT[isCampaign];
    }

    public static Matrix4f getGamePerspectiveViewport(byte isCampaign) {
        return _PERSPECTIVE_VIEWPORT[isCampaign];
    } 

    private BUtil_GLImpl() {}
}
