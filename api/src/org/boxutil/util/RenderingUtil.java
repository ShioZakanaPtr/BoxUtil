package org.boxutil.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.SimpleParticleControlData;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.InstanceType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.entity.*;
import org.boxutil.units.standard.misc.TextFieldObject;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.CampaignRenderingManager;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.units.standard.attribute.Instance2Data;
import org.lazywizard.console.Console;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public final class RenderingUtil {
    private static final CombatEngineLayers[] _COMBAT_LAYER = CombatEngineLayers.values();
    private static final EnumMap<CombatEngineLayers, Integer> _COMBAT_LAYER_LINKED = new EnumMap<>(CombatEngineLayers.class);
    private static final CombatEngineLayers _COMBAT_HIGHEST_LAYER = _COMBAT_LAYER[_COMBAT_LAYER.length - 1];
    private static final CombatEngineLayers _COMBAT_LOWEST_LAYER = _COMBAT_LAYER[0];
    private static final CampaignEngineLayers[] _CAMPAIGN_LAYER = CampaignEngineLayers.values();
    private static final EnumMap<CampaignEngineLayers, Integer> _CAMPAIGN_LAYER_LINKED = new EnumMap<>(CampaignEngineLayers.class);
    private static final CampaignEngineLayers _CAMPAIGN_HIGHEST_LAYER = _CAMPAIGN_LAYER[_CAMPAIGN_LAYER.length - 1];
    private static final CampaignEngineLayers _CAMPAIGN_LOWEST_LAYER = _CAMPAIGN_LAYER[0];
    static {
        for (int i = 0; i < _COMBAT_LAYER.length; i++) {
            _COMBAT_LAYER_LINKED.put(_COMBAT_LAYER[i], i);
        }
        for (int i = 0; i < _CAMPAIGN_LAYER.length; i++) {
            _CAMPAIGN_LAYER_LINKED.put(_CAMPAIGN_LAYER[i], i);
        }
    }

    public static CombatEngineLayers getHighestCombatLayer() {
        return _COMBAT_HIGHEST_LAYER;
    }

    public static CombatEngineLayers getLowestCombatLayer() {
        return _COMBAT_LOWEST_LAYER;
    }

    public static CampaignEngineLayers getHighestCampaignLayer() {
        return _CAMPAIGN_HIGHEST_LAYER;
    }

    public static CampaignEngineLayers getLowestCampaignLayer() {
        return _CAMPAIGN_LOWEST_LAYER;
    }

    public static CombatEngineLayers getPreCombatLayer(CombatEngineLayers layer) {
        int index = _COMBAT_LAYER_LINKED.get(layer) - 1;
        return _COMBAT_LAYER[index < 0 ? _COMBAT_LAYER.length - 1 : index];
    }

    public static CombatEngineLayers getNextCombatLayer(CombatEngineLayers layer) {
        int index = _COMBAT_LAYER_LINKED.get(layer) + 1;
        return _COMBAT_LAYER[index >= _COMBAT_LAYER.length ? 0 : index];
    }

    public static CampaignEngineLayers getPreCampaignLayer(CampaignEngineLayers layer) {
        int index = _CAMPAIGN_LAYER_LINKED.get(layer) - 1;
        return _CAMPAIGN_LAYER[index < 0 ? _CAMPAIGN_LAYER.length - 1 : index];
    }

    public static CampaignEngineLayers getNextCampaignLayer(CampaignEngineLayers layer) {
        int index = _CAMPAIGN_LAYER_LINKED.get(layer) + 1;
        return _CAMPAIGN_LAYER[index >= _CAMPAIGN_LAYER.length ? 0 : index];
    }

    /**
     * Curve beam entity.
     */
    public static TrailEntity createBeamVisual(Vector2f location, float facing, float length, float width, Color coreColor, @Nullable Color fringeColor, SpriteAPI core, @Nullable SpriteAPI fringe, float fadeIn, float full, float fadeOut, boolean isAdditiveBlend) {
        TrailEntity entity = new TrailEntity();
        entity.addNode(new Vector2f(length, 0.0f));
        entity.addNode(new Vector2f());
        entity.submitNodes();
        entity.getMaterialData().setColor(coreColor);
        if (fringeColor != null) entity.getMaterialData().setEmissiveColor(fringeColor);
        entity.setStartWidth(width);
        entity.setEndWidth(width);
        entity.setGlobalTimer(fadeIn, full, fadeOut);
        if (isAdditiveBlend) entity.setAdditiveBlend();
        entity.getMaterialData().setDiffuse(core);
        if (fringe != null) entity.getMaterialData().setEmissive(fringe);
        TransformUtil.createModelMatrixVanilla(location, facing, entity.getModelMatrix());
        return entity;
    }

    /**
     * Curve beam entity.
     */
    public static Pair<TrailEntity, Byte> addCampaignBeamVisual(Vector2f location, float facing, float length, float width, Color coreColor, SpriteAPI core, float full, float fadeOut, CampaignEngineLayers layer) {
        return addCampaignBeamVisual(location, facing, length, width, coreColor, null, core, null, 0.0f, full, fadeOut, true, layer);
    }

    /**
     * Curve beam entity.
     */
    public static Pair<TrailEntity, Byte> addCampaignBeamVisual(Vector2f location, float facing, float length, float width, Color coreColor, @Nullable Color fringeColor, SpriteAPI core, @Nullable SpriteAPI fringe, float fadeIn, float full, float fadeOut, boolean isAdditiveBlend, CampaignEngineLayers layer) {
        TrailEntity entity = createBeamVisual(location, facing, length, width, coreColor, fringeColor, core, fringe, fadeIn, full, fadeOut, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CampaignRenderingManager.addEntity(entity));
    }

    /**
     * Curve beam entity.
     */
    public static Pair<TrailEntity, Byte> addCombatBeamVisual(Vector2f location, float facing, float length, float width, Color coreColor, SpriteAPI core, float full, float fadeOut, CombatEngineLayers layer) {
        return addCombatBeamVisual(location, facing, length, width, coreColor, null, core, null, 0.0f, full, fadeOut, true, layer);
    }

    /**
     * Curve beam entity.
     */
    public static Pair<TrailEntity, Byte> addCombatBeamVisual(Vector2f location, float facing, float length, float width, Color coreColor, @Nullable Color fringeColor, SpriteAPI core, @Nullable SpriteAPI fringe, float fadeIn, float full, float fadeOut, boolean isAdditiveBlend, CombatEngineLayers layer) {
        TrailEntity entity = createBeamVisual(location, facing, length, width, coreColor, fringeColor, core, fringe, fadeIn, full, fadeOut, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CombatRenderingManager.addEntity(entity));
    }

    public static Pair<TrailEntity, FlareEntity> spawnEmpArcVisual(@Nullable Vector2f offset, float width, Vector2f start, Vector2f end, Color fringe, @Nullable Color core, float jitterPower, float full, float fadeOut) {
        TrailEntity arc = new TrailEntity();
        arc.getMaterialData().setDiffuse(Global.getSettings().getSprite("graphics/fx/beamcoreb.png"));
        arc.getMaterialData().setEmissive(Global.getSettings().getSprite("graphics/fx/beamfringeb.png"));
        if (core != null) arc.getMaterialData().setColor(core);
        arc.getMaterialData().setEmissiveColor(fringe);
        arc.setJitterPower(0.1f);
        arc.setFlick(true);
        Vector2f normal = new Vector2f(start.y - end.y, end.x - start.x), curr;
        float arcLength = normal.length(), jitterLength = (float) Math.sqrt(arcLength) * jitterPower, factor, minFactor;
        int maxJitterNode = (int) Math.floor(jitterLength) - 2;
        normal.scale(1.0f / arcLength);
        arc.addNode(end);
        minFactor = 0.8f / maxJitterNode;
        for (int i = maxJitterNode - 1; i > 0; --i) {
            factor = (float) i / (float) maxJitterNode;
            factor += ((float) Math.random() - 0.5f) * minFactor;
            curr = CalculateUtil.mix(start, end, new Vector2f(), factor);
            curr.x += normal.x * jitterLength;
            curr.y += normal.y * jitterLength;
            if ((float) Math.random() >= 0.5f) curr.set(-curr.x, -curr.y);
            arc.addNode(curr);
        }
        arc.addNode(start);
        arc.setNodeRefreshAllFromCurrentIndex();
        arc.submitNodes();
        arc.setStartWidth(width);
        arc.setEndWidth(width);
        float smoothFactor = Math.max(arcLength - 4.0f, 0.0f) / arcLength;
        arc.setFillStartAlpha(0.0f);
        arc.setFillStartFactor(smoothFactor);
        arc.setFillEndAlpha(0.0f);
        arc.setFillEndFactor(smoothFactor);
        arc.setGlobalTimer(0.0f, full, fadeOut);

        FlareEntity ends = new FlareEntity();
        float endsSize = width * 2.0f;
        List<InstanceDataAPI> dataList = new ArrayList<>();
        Instance2Data data = new Instance2Data();
        data.setLocation(start);
        dataList.add(data);
        data = new Instance2Data();
        data.setLocation(end);
        dataList.add(data);
        ends.setInstanceData(dataList, 0.0f, full, fadeOut);
        ends.setInstanceDataRefreshAllFromCurrentIndex();
        ends.submitInstanceData();
        ends.setRenderingCount(2);
        ends.setAlwaysRefreshInstanceData(true);
        ends.setSmooth();
        ends.setFlick(true);
        ends.setSyncFlick(true);
        if (core != null) ends.setCoreColor(core);
        ends.setFringeColor(fringe);
        ends.setFlickerSyncCode(arc.hashCode());
        ends.setGlobalTimer(0.0f, full, fadeOut);
        ends.setSize(endsSize, endsSize);
        if (offset != null) {
            arc.setLocation(offset);
            ends.setLocation(offset);
        }
        return new Pair<>(arc, ends);
    }

    public static Pair<Byte, Pair<TrailEntity, FlareEntity>> spawnCombatEmpArcVisual(Vector2f start, Vector2f end, float width, Color fringe, @Nullable Color core, float jitterPower, float full, float fadeOut) {
        Vector2f realEnd = Vector2f.sub(end, start, new Vector2f());
        Pair<TrailEntity, FlareEntity> result = spawnEmpArcVisual(start, width, new Vector2f(), realEnd, fringe, core, jitterPower, full, fadeOut);
        result.one.setLayer(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
        result.two.setLayer(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
        byte code = CombatRenderingManager.addEntity(result.one);
        code |= CombatRenderingManager.addEntity(result.two);
        return new Pair<>(code, result);
    }

    public static Pair<Byte, Pair<TrailEntity, FlareEntity>> spawnCombatEmpArcVisual(Vector2f start, Vector2f end, float width, Color fringe, @Nullable Color core) {
        return spawnCombatEmpArcVisual(start, end, width, fringe, core, 1.0f, (float) Math.random() * 2.0f + 0.5f, 0.5f);
    }

    public static Pair<Byte, Pair<TrailEntity, FlareEntity>> spawnCampaignEmpArcVisual(Vector2f start, Vector2f end, float width, Color fringe, @Nullable Color core, float jitterPower, float full, float fadeOut) {
        Vector2f realEnd = Vector2f.sub(end, start, new Vector2f());
        Pair<TrailEntity, FlareEntity> result = spawnEmpArcVisual(start, width, new Vector2f(), realEnd, fringe, core, jitterPower, full, fadeOut);
        result.one.setLayer(CampaignEngineLayers.TERRAIN_8);
        result.two.setLayer(CampaignEngineLayers.TERRAIN_8);
        byte code = CombatRenderingManager.addEntity(result.one);
        code |= CombatRenderingManager.addEntity(result.two);
        return new Pair<>(code, result);
    }

    public static Pair<Byte, Pair<TrailEntity, FlareEntity>> spawnCampaignEmpArcVisual(Vector2f start, Vector2f end, float width, Color fringe, @Nullable Color core) {
        return spawnCampaignEmpArcVisual(start, end, width, fringe, core, 1.0f, (float) Math.random() * 2.0f + 0.5f, 0.5f);
    }

    public static SpriteEntity createParticleField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f velocityRange, @Nullable Vector2f facingRange, @Nullable Vector2f turnRateRange, Vector4f sizeRangeXY, @Nullable Vector4f sizeGrowScaleRangeXY, @Nullable Color baseColor, @Nullable Color baseColorShift, @Nullable Color baseEmissiveColor, @Nullable Color baseEmissiveColorShift, SpriteAPI diffuse, @Nullable SpriteAPI emissive, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend) {
        SpriteEntity entity = new SpriteEntity();
        if (baseColor == null) baseColor = Color.WHITE;
        if (baseEmissiveColor == null) baseEmissiveColor = Color.WHITE;
        if (isAdditiveBlend) entity.setAdditiveBlend();
        entity.getMaterialData().setDiffuse(diffuse);
        if (emissive != null) entity.getMaterialData().setEmissive(emissive);
        TransformUtil.createModelMatrixVanilla(location, facing, entity.getModelMatrix());

        final int finalCount = Math.min(BoxConfigs.getMaxInstanceDataSize(), count);
        final float finalArc = arc / 2.0f;
        final boolean haveColorShift = baseColorShift != null;
        final boolean haveEmissiveColorShift = baseEmissiveColorShift != null;
        final boolean haveSpreadRange = baseSpreadRange != null;
        final boolean haveVelocityRange = velocityRange != null;
        List<InstanceDataAPI> dataList = new ArrayList<>();
        for (int i = 0; i < finalCount; i++) {
            float factor = (float) Math.random();
            float factor2 = (float) Math.random();
            float timerOffset = timerOffsetRange * factor;
            Instance2Data data = new Instance2Data();
            float angle = facing + finalArc * (factor2 * 2.0f - 1.0f);
            if (angle < 0.0f) angle += 360.0f;
            if (angle > 360.0f) angle -= 360.0f;
            float baseX = (float) Math.cos(Math.toRadians(angle));
            float baseY = TrigUtil.sinFormCosF(baseX, angle);
            Color finalColor;
            if (haveColorShift) {
                finalColor = CalculateUtil.mix(baseColor, baseColorShift, true, factor);
            } else finalColor = baseColor;
            Color finalEmissiveColor;
            if (haveEmissiveColorShift) {
                finalEmissiveColor = CalculateUtil.mix(baseEmissiveColor, baseEmissiveColorShift, true, factor);
            } else finalEmissiveColor = baseEmissiveColor;
            if (haveSpreadRange) {
                float locationLength = (baseSpreadRange.y - baseSpreadRange.x) * factor + baseSpreadRange.x;
                data.setLocation(locationLength * baseX, locationLength * baseY);
            }
            if (haveVelocityRange) {
                float velocityLength = (velocityRange.x - velocityRange.y) * factor + velocityRange.x;
                data.setVelocity(velocityLength * baseX, velocityLength * baseY);
            }
            float sizeX = (sizeRangeXY.z - sizeRangeXY.x) * factor + sizeRangeXY.x;
            float sizeY = (sizeRangeXY.w - sizeRangeXY.y) * factor + sizeRangeXY.y;
            data.setScale(sizeX * 0.5f, sizeY * 0.5f);
            if (sizeGrowScaleRangeXY != null) {
                float growX = (sizeGrowScaleRangeXY.z - sizeGrowScaleRangeXY.x) * factor + sizeGrowScaleRangeXY.x;
                float growY = (sizeGrowScaleRangeXY.w - sizeGrowScaleRangeXY.y) * factor + sizeGrowScaleRangeXY.y;
                data.setScaleRate(growX * 0.5f, growY * 0.5f);
            }
            data.setColor(finalColor);
            data.setEmissiveColor(finalEmissiveColor);
            if (facingRange != null) data.setFacing((facingRange.y - facingRange.x) * factor + facingRange.x);
            if (turnRateRange != null) data.setTurnRate((turnRateRange.y - turnRateRange.x) * factor + turnRateRange.x);
            data.setTimer(fadeIn + timerOffset, full + timerOffset, fadeOut + timerOffset);
            dataList.add(data);
        }
        float timerOffsetCheck = Math.max(timerOffsetRange, 0.0f);
        entity.mallocInstance(InstanceType.DYNAMIC_2D, finalCount);
        entity.setInstanceData(dataList, fadeIn + timerOffsetCheck, full + timerOffsetCheck, fadeOut + timerOffsetCheck);
        entity.setInstanceDataRefreshSize(finalCount);
        entity.submitInstance();
        entity.setRenderingCount(finalCount);
        entity.setAlwaysRefreshInstanceData(true);
        return entity;
    }

    public static Pair<SpriteEntity, Byte> addCampaignParticleField(Vector2f location, int count, float facing, float arc, float fieldRadius, Vector4f sizeRangeXY, @Nullable Color baseColor, SpriteAPI diffuse, float full, float fadeOut, CampaignEngineLayers layer) {
        return addCampaignParticleField(location, count, facing, arc, null, new Vector2f(0.0f, fieldRadius), new Vector2f(0.0f, 360.0f), null, sizeRangeXY, null, baseColor, null, null, null, diffuse, null, 0.0f, full, fadeOut, 0.0f, true, layer);
    }

    public static Pair<SpriteEntity, Byte> addCampaignParticleField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f velocityRange, @Nullable Vector2f facingRange, @Nullable Vector2f turnRateRange, Vector4f sizeRangeXY, @Nullable Vector4f sizeGrowScaleRangeXY, @Nullable Color baseColor, @Nullable Color baseColorShift, @Nullable Color baseEmissiveColor, @Nullable Color baseEmissiveColorShift, SpriteAPI diffuse, @Nullable SpriteAPI emissive, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend, CampaignEngineLayers layer) {
        SpriteEntity entity = createParticleField(location, count, facing, arc, baseSpreadRange, velocityRange, facingRange, turnRateRange, sizeRangeXY, sizeGrowScaleRangeXY, baseColor, baseColorShift, baseEmissiveColor, baseEmissiveColorShift, diffuse, emissive, fadeIn, full, fadeOut, timerOffsetRange, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CampaignRenderingManager.addEntity(entity));
    }

    public static Pair<SpriteEntity, Byte> addCombatParticleField(Vector2f location, int count, float facing, float arc, float fieldRadius, Vector4f sizeRangeXY, @Nullable Color baseColor, SpriteAPI diffuse, float full, float fadeOut, CombatEngineLayers layer) {
        return addCombatParticleField(location, count, facing, arc, null, new Vector2f(0.0f, fieldRadius), new Vector2f(0.0f, 360.0f), null, sizeRangeXY, null, baseColor, null, null, null, diffuse, null, 0.0f, full, fadeOut, 0.0f, true, layer);
    }

    public static Pair<SpriteEntity, Byte> addCombatParticleField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f velocityRange, @Nullable Vector2f facingRange, @Nullable Vector2f turnRateRange, Vector4f sizeRangeXY, @Nullable Vector4f sizeGrowScaleRangeXY, @Nullable Color baseColor, @Nullable Color baseColorShift, @Nullable Color baseEmissiveColor, @Nullable Color baseEmissiveColorShift, SpriteAPI diffuse, @Nullable SpriteAPI emissive, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend, CombatEngineLayers layer) {
        SpriteEntity entity = createParticleField(location, count, facing, arc, baseSpreadRange, velocityRange, facingRange, turnRateRange, sizeRangeXY, sizeGrowScaleRangeXY, baseColor, baseColorShift, baseEmissiveColor, baseEmissiveColorShift, diffuse, emissive, fadeIn, full, fadeOut, timerOffsetRange, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CombatRenderingManager.addEntity(entity));
    }

    public static FlareEntity createFlareField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f facingRange, Vector4f sizeRangeXY, @Nullable Color baseFringeColor, @Nullable Color baseFringeColorShift, @Nullable Color baseCoreColor, @Nullable Color baseCoreColorShift, boolean flick, boolean syncFlick, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend) {
        FlareEntity entity = new FlareEntity();
        if (baseFringeColor == null) baseFringeColor = Color.WHITE;
        if (baseCoreColor == null) baseCoreColor = Color.WHITE;
        entity.setFlick(flick);
        entity.setSyncFlick(syncFlick);
        entity.setAspect((sizeRangeXY.x + sizeRangeXY.z) / (sizeRangeXY.y + sizeRangeXY.w) * 0.25f);
        if (isAdditiveBlend) entity.setAdditiveBlend();
        TransformUtil.createModelMatrixVanilla(location, facing, entity.getModelMatrix());

        final int finalCount = Math.min(BoxConfigs.getMaxInstanceDataSize(), count);
        final float finalArc = arc / 2.0f;
        final boolean haveFringeColorShift = baseFringeColorShift != null;
        final boolean haveCoreColorShift = baseCoreColorShift != null;
        final boolean haveSpreadRange = baseSpreadRange != null;
        List<InstanceDataAPI> dataList = new ArrayList<>();
        for (int i = 0; i < finalCount; i++) {
            float factor = (float) Math.random();
            float factor2 = (float) Math.random();
            float timerOffset = timerOffsetRange * factor;
            Instance2Data data = new Instance2Data();
            float angle = facing + finalArc * (factor2 * 2.0f - 1.0f);
            if (angle < 0.0f) angle += 360.0f;
            if (angle > 360.0f) angle -= 360.0f;
            float baseX = (float) Math.cos(Math.toRadians(angle));
            float baseY = TrigUtil.sinFormCosF(baseX, angle);
            Color finalFringeColor;
            if (haveFringeColorShift) {
                finalFringeColor = CalculateUtil.mix(baseFringeColor, baseFringeColorShift, true, factor);
            } else finalFringeColor = baseFringeColor;
            Color finalCoreColor;
            if (haveCoreColorShift) {
                finalCoreColor = CalculateUtil.mix(baseCoreColor, baseCoreColorShift, true, factor);
            } else finalCoreColor = baseCoreColor;
            if (haveSpreadRange) {
                float locationLength = (baseSpreadRange.y - baseSpreadRange.x) * factor + baseSpreadRange.x;
                data.setLocation(locationLength * baseX, locationLength * baseY);
            }
            float sizeX = (sizeRangeXY.z - sizeRangeXY.x) * factor + sizeRangeXY.x;
            float sizeY = (sizeRangeXY.w - sizeRangeXY.y) * factor + sizeRangeXY.y;
            data.setScale(sizeX * 0.5f, sizeY * 0.5f);
            data.setColor(finalCoreColor);
            data.setEmissiveColor(finalFringeColor);
            if (facingRange != null) data.setFacing((facingRange.y - facingRange.x) * factor + facingRange.x);
            data.setTimer(fadeIn + timerOffset, full + timerOffset, fadeOut + timerOffset);
            dataList.add(data);
        }
        float timerOffsetCheck = Math.max(timerOffsetRange, 0.0f);
        entity.mallocInstance(InstanceType.DYNAMIC_2D, finalCount);
        entity.setInstanceData(dataList, fadeIn + timerOffsetCheck, full + timerOffsetCheck, fadeOut + timerOffsetCheck);
        entity.setInstanceDataRefreshSize(finalCount);
        entity.submitInstance();
        entity.setRenderingCount(finalCount);
        entity.setAlwaysRefreshInstanceData(true);
        return entity;
    }

    public static Pair<FlareEntity, Byte> addCampaignFlareField(Vector2f location, int count, float facing, float arc, float fieldRadius, Vector4f sizeRangeXY, @Nullable Color fringeColor, @Nullable Color coreColor, float full, float fadeOut, CampaignEngineLayers layer) {
        return addCampaignFlareField(location, count, facing, arc, new Vector2f(0.0f, fieldRadius), null, sizeRangeXY, fringeColor, null, coreColor, null, true, false, 0.0f, full, fadeOut, 0.0f, true, layer);
    }

    public static Pair<FlareEntity, Byte> addCampaignFlareField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f facingRange, Vector4f sizeRangeXY, @Nullable Color baseFringeColor, @Nullable Color baseFringeColorShift, @Nullable Color baseCoreColor, @Nullable Color baseCoreColorShift, boolean flick, boolean syncFlick, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend, CampaignEngineLayers layer) {
        FlareEntity entity = createFlareField(location, count, facing, arc, baseSpreadRange, facingRange, sizeRangeXY, baseFringeColor, baseFringeColorShift, baseCoreColor, baseCoreColorShift, flick, syncFlick, fadeIn, full, fadeOut, timerOffsetRange, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CampaignRenderingManager.addEntity(entity));
    }

    public static Pair<FlareEntity, Byte> addCombatFlareField(Vector2f location, int count, float facing, float arc, float fieldRadius, Vector4f sizeRangeXY, @Nullable Color fringeColor, @Nullable Color coreColor, float full, float fadeOut, CombatEngineLayers layer) {
        return addCombatFlareField(location, count, facing, arc, new Vector2f(0.0f, fieldRadius), null, sizeRangeXY, fringeColor, null, coreColor, null, true, false, 0.0f, full, fadeOut, 0.0f, true, layer);
    }

    public static Pair<FlareEntity, Byte> addCombatFlareField(Vector2f location, int count, float facing, float arc, @Nullable Vector2f baseSpreadRange, @Nullable Vector2f facingRange, Vector4f sizeRangeXY, @Nullable Color baseFringeColor, @Nullable Color baseFringeColorShift, @Nullable Color baseCoreColor, @Nullable Color baseCoreColorShift, boolean flick, boolean syncFlick, float fadeIn, float full, float fadeOut, float timerOffsetRange, boolean isAdditiveBlend, CombatEngineLayers layer) {
        FlareEntity entity = createFlareField(location, count, facing, arc, baseSpreadRange, facingRange, sizeRangeXY, baseFringeColor, baseFringeColorShift, baseCoreColor, baseCoreColorShift, flick, syncFlick, fadeIn, full, fadeOut, timerOffsetRange, isAdditiveBlend);
        entity.setLayer(layer);
        return new Pair<>(entity, CombatRenderingManager.addEntity(entity));
    }

    public static TextFieldEntity createTextField(@NotNull String fontPath, float width, float height, @NotNull String text, TextFieldEntity.Alignment alignment, float padding, Color baseColor, boolean italic, boolean underline, boolean strikeout, Color highlightColor, String... highlight) {
        TextFieldEntity entity = new TextFieldEntity(fontPath);
        float pad = padding;
        String[] splits = text.split("%s");
        String split;
        String hlText;
        for (int i = 0; i < splits.length; i++) {
            split = splits[i];
            entity.addText(split, pad, baseColor, false, italic, underline, strikeout, 0);
            if (i == (splits.length - 1)) break;
            if (i == 0) pad = 0.0f;
            hlText = (highlight != null && i < highlight.length) ? highlight[i] : "null";
            entity.addText(hlText, 0.0f, highlightColor, false, italic, underline, strikeout, 0);
        }
        entity.setAlignment(alignment);
        entity.setFieldSize(width, height);
        entity.setTextDataRefreshAllFromCurrentIndex();
        entity.submitText();
        return entity;
    }

    public static TextFieldEntity createTextField(@NotNull String fontPath, float width, float height, @NotNull String text, float padding, Color baseColor, boolean italic, boolean underline, boolean strikeout, Color highlightColor, String... highlight) {
        return createTextField(fontPath, width, height, text, TextFieldEntity.Alignment.LEFT, padding, baseColor, italic, underline, strikeout, highlightColor, highlight);
    }

    public static TextFieldEntity createTextField(@NotNull String fontPath, float width, float height, @NotNull String text, float padding, Color baseColor, Color highlightColor, String... highlight) {
        return createTextField(fontPath, width, height, text, padding, baseColor, false, false, false, highlightColor, highlight);
    }

    public static TextFieldEntity createTextField(@NotNull String fontPath, float width, float height, @NotNull String text, float padding, Color baseColor) {
        return createTextField(fontPath, width, height, text, padding, baseColor, Misc.getHighlightColor(), (String) null);
    }

    public static TextFieldEntity createTextField(@NotNull String fontPath, float width, float height, @NotNull String text) {
        return createTextField(fontPath, width, height, text, 0.0f, Misc.getTextColor(), Misc.getHighlightColor(), (String) null);
    }

    public static TextFieldObject createTextFieldCompatible(@NotNull String fontPath, float width, float height, @NotNull String text, TextFieldEntity.Alignment alignment, float padding, Color baseColor, boolean italic, Color highlightColor, String... highlight) {
        TextFieldObject entity = new TextFieldObject(fontPath);
        float pad = padding;
        String[] splits = text.split("%s");
        String split;
        String hlText;
        for (int i = 0; i < splits.length; i++) {
            split = splits[i];
            entity.addText(split, pad, baseColor, italic);
            if (i == (splits.length - 1)) break;
            if (i == 0) pad = 0.0f;
            hlText = (highlight != null && i < highlight.length) ? highlight[i] : "null";
            entity.addText(hlText, 0.0f, highlightColor, italic);
        }
        entity.setAlignment(alignment);
        entity.setFieldSize(width, height);
        entity.setTextDataRefreshAllFromCurrentIndex();
        entity.submitText();
        return entity;
    }

    public static TextFieldObject createTextFieldCompatible(@NotNull String fontPath, float width, float height, @NotNull String text, float padding, Color baseColor, Color highlightColor, String... highlight) {
        return createTextFieldCompatible(fontPath, width, height, text, TextFieldEntity.Alignment.LEFT, padding, baseColor, false, highlightColor, highlight);
    }

    public static TextFieldObject createTextFieldCompatible(@NotNull String fontPath, float width, float height, @NotNull String text, float padding, String... highlight) {
        return createTextFieldCompatible(fontPath, width, height, text, TextFieldEntity.Alignment.LEFT, padding, Misc.getTextColor(), false, Misc.getHighlightColor(), highlight);
    }

    public static TextFieldObject createTextFieldCompatible(@NotNull String fontPath, float width, float height, @NotNull String text) {
        return createTextFieldCompatible(fontPath, width, height, text, TextFieldEntity.Alignment.LEFT, 0.0f, Misc.getTextColor(), false, Misc.getHighlightColor(), (String) null);
    }

    private static float hash11(float in) {
        float result = CalculateUtil.fraction(in * 0.1031f);
        result *= result + 33.33f;
        result *= result + result;
        return CalculateUtil.fraction(result);
    }

    /**
     * Rendering simple jitter for sprite, should call it in layered rendering plugin.<p>
     * Vanilla supported.
     *
     * @param seed default for total elapsed time, as {@link CombatEngineAPI#getTotalElapsedTime(boolean)}
     */
    public static void glDrawSimpleJitter(SpriteAPI sprite, Vector2f location, float facing, Color color, float alphaMulti, int num, float minRadius, float maxRadius, boolean additive, float seed) {
        final int bufferSize = num << 3;
        final float[] oriVertices = new float[] {-sprite.getCenterX(), -sprite.getCenterY(), sprite.getWidth() - sprite.getCenterX(), sprite.getHeight() - sprite.getCenterY()};
        final float[] uvs = new float[] {sprite.getTexX(), sprite.getTexY(), sprite.getTexX() + sprite.getTexWidth(), sprite.getTexY() + sprite.getTexHeight()};
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glPushMatrix();
        GL11.glTranslatef(location.x, location.y, 0.0f);
        GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, additive ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureId());

        Misc.setColor(color, alphaMulti);
        float[] currVertices = new float[4];
        float currRadius, currRad, currOffsetX, currOffsetY;
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(bufferSize);
        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(bufferSize);
        for (int i = 0; i < num; ++i) {
            currRadius = hash11(seed + i) * (maxRadius - minRadius) + minRadius;
            currRad = hash11(seed + i + 3) * TrigUtil.PI2_F;
            currOffsetX = (float) Math.cos(currRad);
            currOffsetY = TrigUtil.sinFormCosRadiansF(currOffsetX, currRad) * currRadius;
            currOffsetX *= currRadius;

            currVertices[0] = oriVertices[0] + currOffsetX;
            currVertices[1] = oriVertices[1] + currOffsetY;
            currVertices[2] = oriVertices[2] + currOffsetX;
            currVertices[3] = oriVertices[3] + currOffsetY;

            vertexBuffer.put(currVertices[0]);
            vertexBuffer.put(currVertices[1]);
            vertexBuffer.put(currVertices[2]);
            vertexBuffer.put(currVertices[1]);
            vertexBuffer.put(currVertices[2]);
            vertexBuffer.put(currVertices[3]);
            vertexBuffer.put(currVertices[0]);
            vertexBuffer.put(currVertices[3]);
            uvBuffer.put(uvs[0]);
            uvBuffer.put(uvs[1]);
            uvBuffer.put(uvs[2]);
            uvBuffer.put(uvs[1]);
            uvBuffer.put(uvs[2]);
            uvBuffer.put(uvs[3]);
            uvBuffer.put(uvs[0]);
            uvBuffer.put(uvs[3]);
        }
        vertexBuffer.position(0);
        vertexBuffer.limit(vertexBuffer.capacity());
        uvBuffer.position(0);
        uvBuffer.limit(uvBuffer.capacity());

        GL11.glVertexPointer(2, 0, vertexBuffer);
        GL11.glTexCoordPointer(2, 0, uvBuffer);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, num * 4);

        GL11.glPopMatrix();
        GL11.glPopClientAttrib();
    }

    /**
     * Immediate draw text when called.<p>
     * Should not use it for release version.
     */
    public static void debugText(Object message, Vector2f screenPos, Color textColor, boolean additive, @NotNull String fontPath) {
        String messageStr;
        if (message == null) messageStr = "null"; else messageStr = message.toString();
        if (messageStr.isEmpty()) return;

        TextFieldObject textField = new TextFieldObject(fontPath);
        if (textField.getFontMap() == null || !textField.getFontMap().isValid()) return;
        textField.mallocTextData(messageStr.length());
        textField.addText(messageStr);
        textField.setFieldWidth(Float.MAX_VALUE);
        textField.setFieldHeight(Float.MAX_VALUE);
        textField.setFontSpace(1.0f, 0.0f);
        textField.setTextDataRefreshIndex(0);
        textField.setTextDataRefreshAllFromCurrentIndex();
        textField.submitText();
        if (!textField.isValidRenderingTextField()) return;

        FloatBuffer projMat = CommonUtil.createFloatBuffer(TransformUtil.createWindowOrthoMatrix(new Matrix4f()));
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_POLYGON_BIT);
        GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadMatrix(projMat);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        textField.render(screenPos, 0.0f, additive, textColor);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    /**
     * Immediate draw text when called.<p>
     * Should not use it for release version.
     */
    public static void debugText(Object message, Vector2f screenPos) {
        debugText(message, screenPos, Misc.getTextColor(), false, "graphics/fonts/orbitron24aa.fnt");
    }

    /**
     * Immediate draw text when called.<p>
     * Should not use it for release version.<p>
     * Will rendering at the center of screen.
     */
    public static void debugText(Object message) {
        debugText(message, new Vector2f(ShaderCore.getScreenWidth() * 0.5f, ShaderCore.getScreenHeight() * 0.5f));
    }


    ///
    /// quickly spawn fx
    ///
    private static Object defaultFXLayer(final boolean isCampaign) {
        return isCampaign ? CampaignEngineLayers.TERRAIN_8 : CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER;
    }

    private static SpinLock getControllerLock(final boolean isCampaign, final String key) {
        final var map = isCampaign ? CampaignRenderingManager.getCustomData() : CombatRenderingManager.getCustomData();
        return (SpinLock) map.computeIfAbsent(key + "_SyncLock", k -> new SpinLock());
    }

    private static SimpleParticleControlData getControllerFromMap(final boolean isCampaign, final String key) {
        return (SimpleParticleControlData) (isCampaign ? CampaignRenderingManager.getCustomData().get(key) : CombatRenderingManager.getCustomData().get(key));
    }

    private static void putControllerFromMap(final boolean isCampaign, final RenderDataAPI target, final String key, final SimpleParticleControlData controller) {
        if (isCampaign) {
            CampaignRenderingManager.addEntity(target);
            CampaignRenderingManager.getCustomData().put(key, controller);
        } else {
            CombatRenderingManager.addEntity(target);
            CombatRenderingManager.getCustomData().put(key, controller);
        }
    }

    /**
     * Some {@link CombatEngineAPI} like visual effects spawning method.<p>
     * Recommend to use <code>addSmoothParticle</code> in vanilla combat engine, not <code>addHitParticle</code>.<p>
     * About EmpArc, refer to the source of {@link RenderingUtil#spawnEmpArcVisual(Vector2f, float, Vector2f, Vector2f, Color, Color, float, float, float)} and make one for your mod.<p>
     * About the return value, returns <code>false</code> when BoxUtil renderer invalid or can not add all particles.
     */
    public final static class VanillaFX {
        private final static Map<Integer, Integer> _AUTO_PARTICLE_NORMAL = new HashMap<>();
        private final static ShaderUtil.NormalMapGenParam _AUTO_PARTICLE_NORMAL_PARAM = new ShaderUtil.NormalMapGenParam();
        static {
            _AUTO_PARTICLE_NORMAL_PARAM.details = null;
        }

        public final static class Controllers {
            private static SimpleParticleControlData getController(final boolean isCampaign, final int maxParticles, final String key, final String diffuse, final String emissive, final int tileX, final int tileY, final float emissiveAlphaMult, final boolean additive, final boolean negative) {
                final var lock = getControllerLock(isCampaign, key);

                lock.lock();
                var controller = getControllerFromMap(isCampaign, key);

                if (controller == null || controller.isEntityExpired()) {
                    controller = new SimpleParticleControlData(maxParticles, 3.2f, -5120.0f, false);

                    final boolean withDiffuse = diffuse != null, withEmissive = emissive != null;
                    final SpriteAPI diffuseSprite = withDiffuse ? Global.getSettings().getSprite(diffuse) : null,
                            emissiveSprite = withEmissive ? Global.getSettings().getSprite(emissive) : null;

                    SpriteEntity sprite = new SpriteEntity();
                    if (withDiffuse) sprite.setDiffuseSprite(diffuseSprite); else sprite.setDiffuseSprite(BoxDatabase.BUtil_NONE);
                    if (withEmissive) sprite.setEmissiveSprite(emissiveSprite);
                    sprite.getMaterialData().setEmissiveColorAlpha(emissiveAlphaMult);
                    sprite.getMaterialData().setColorToEmissive(0.0f);
                    sprite.getMaterialData().setAlphaToEmissive(0.0f);
                    if (additive || negative) {
                        if (negative) sprite.setNegativeBlend(); else sprite.setAdditiveBlend();
                        sprite.getMaterialData().setIgnoreIllumination(true);
                    } else if (withDiffuse) {
                        sprite.getMaterialData().setNormal(_AUTO_PARTICLE_NORMAL.computeIfAbsent(diffuseSprite.getTextureId(), tex -> {
                            if (tex < 1) return 0;
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
                            final int texWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
                                    texHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                            if (texWidth < 1 || texHeight < 1) return 0;
                            return ShaderUtil.genNormalMapFromRGB(tex, texWidth, texHeight, _AUTO_PARTICLE_NORMAL_PARAM);
                        }));
                    }

                    if (tileX > 1 || tileY > 1) {
                        sprite.setTileSize(tileX, tileY);
                        sprite.setRandomTile(true);
                        sprite.setRandomTileEachInstance(true);
                    }
                    if (withDiffuse) sprite.setUVEnd(diffuseSprite.getTexWidth(), diffuseSprite.getTexHeight());
                    else if (withEmissive) sprite.setUVEnd(emissiveSprite.getTexWidth(), emissiveSprite.getTexHeight());
                    if (!negative) sprite.getMaterialData().setGlowPower(0.25f);
                    sprite.setBaseSizePerTiles(0.5f, 0.5f);
                    sprite.setLayer(defaultFXLayer(isCampaign));
                    sprite.setControlData(controller);

                    putControllerFromMap(isCampaign, sprite, key, controller);
                }
                lock.unlock();
                return controller;
            }

            public static SimpleParticleControlData getHitParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addHitParticle", null, Global.getSettings().getSpriteName("fx", "BUtil_hitParticle"), 1, 1, 1.0f, true, false);
            }

            public static SimpleParticleControlData getSmoothParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addSmoothParticle", null, Global.getSettings().getSpriteName("fx", "BUtil_smoothParticle"), 1, 1, 1.0f, true, false);
            }

            public static SimpleParticleControlData getNegativeParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNegativeParticle", null, Global.getSettings().getSpriteName("fx", "BUtil_negativeParticle"), 1, 1, 1.0f, true, true);
            }

            public static SimpleParticleControlData getSmokeParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addSmokeParticle", Global.getSettings().getSpriteName("fx", "BUtil_smoke"), null, 1, 1, 1.0f, false, false);
            }

            public static SimpleParticleControlData getNebulaParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNebulaParticle", null, Global.getSettings().getSpriteName("misc", "nebula_particles"), 4, 4, 1.0f, true, false);
            }

            public static SimpleParticleControlData getNegativeNebulaParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNegativeNebulaParticle", null, Global.getSettings().getSpriteName("misc", "nebula_particles"), 4, 4, 1.0f, true, true);
            }

            public static SimpleParticleControlData getSwirlyNebulaParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addSwirlyNebulaParticle", null, Global.getSettings().getSpriteName("misc", "fx_particles2"), 4, 4, 1.0f, true, false);
            }

            public static SimpleParticleControlData getNegativeSwirlyNebulaParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNegativeSwirlyNebulaParticle", null, Global.getSettings().getSpriteName("misc", "fx_particles2"), 4, 4, 1.0f, true, true);
            }

            public static SimpleParticleControlData getNebulaSmokeParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNebulaSmokeParticle", Global.getSettings().getSpriteName("misc", "nebula_particles"), null, 4, 4, 1.0f, false, false);
            }

            public static SimpleParticleControlData getNebulaSmoothParticle(final boolean isCampaign) {
                return getController(isCampaign, 8192, "BUtil_VanillaFX_addNebulaSmoothParticle", null, Global.getSettings().getSpriteName("misc", "nebula_particles2"), 2, 2, 1.0f, true, false);
            }

            /**
             * @return {normal explosion, ring explosion, round}
             */
            public static SimpleParticleControlData[] getExplosion(final boolean isCampaign) {
                return new SimpleParticleControlData[]{
                        getController(isCampaign, 13100, "BUtil_VanillaFX_spawnExplosion_A", null, Global.getSettings().getSpriteName("fx", "BUtil_explosion"), 3, 1, 1.0f, true, false),
                        getController(isCampaign, 3200, "BUtil_VanillaFX_spawnExplosion_B", null, Global.getSettings().getSpriteName("fx", "BUtil_explosionRing"), 1, 1, 1.0f, true, false),
                        getController(isCampaign, 84, "BUtil_VanillaFX_spawnExplosion_C", null, Global.getSettings().getSpriteName("fx", "BUtil_smoothParticle"), 1, 1, 1.0f, true, false)
                };
            }

            /**
             * @return {normal debris, glow debris}
             */
            public static SimpleParticleControlData[] getDebrisSmall(final boolean isCampaign) {
                final var tex = Global.getSettings().getSpriteName("fx", "BUtil_debris_small");
                return new SimpleParticleControlData[]{
                        getController(isCampaign, 2732, "BUtil_VanillaFX_spawnDebrisSmall", tex, null, 2, 2, 1.0f, false, false),
                        getController(isCampaign, 5460, "BUtil_VanillaFX_spawnDebrisSmall_Glow", null, tex, 2, 2, 2.0f, true, false)
                };
            }

            /**
             * @return {normal debris, glow debris}
             */
            public static SimpleParticleControlData[] getDebrisMedium(final boolean isCampaign) {
                final var tex = Global.getSettings().getSpriteName("fx", "BUtil_debris_medium");
                return new SimpleParticleControlData[]{
                        getController(isCampaign, 2732, "BUtil_VanillaFX_spawnDebrisMedium", tex, null, 2, 1, 1.0f, false, false),
                        getController(isCampaign, 5460, "BUtil_VanillaFX_spawnDebrisMedium_Glow", null, tex, 2, 1, 2.0f, true, false)
                };
            }

            /**
             * @return {normal debris, glow debris}
             */
            public static SimpleParticleControlData[] getDebrisLarge(final boolean isCampaign) {
                final var tex = Global.getSettings().getSpriteName("fx", "BUtil_debris_large");
                return new SimpleParticleControlData[]{
                        getController(isCampaign, 2732, "BUtil_VanillaFX_spawnDebrisLarge", tex, null, 2, 1, 1.0f, false, false),
                        getController(isCampaign, 5460, "BUtil_VanillaFX_spawnDebrisLarge_Glow", null, tex, 2, 1, 2.0f, true, false)
                };
            }

            private Controllers() {}
        }

        private static boolean ignoreSpawn(final boolean isCampaign, final Vector2f loc, final float size) {
            if (size <= 0.0f) return true;
            ViewportAPI viewport = isCampaign ? Global.getSector().getViewport() : Global.getCombatEngine().getViewport();
            return !viewport.isNearViewport(loc, size * 4.0f);
        }

        private static boolean setParticle(final SimpleParticleControlData controller, final Vector2f loc, final Vector2f vel, final float scale, final float size, final float facing, final float spin, final Color color, final float brightness, final float fadeIn, final float full, final float fadeOut) {
            Instance2Data particle = controller.addParticle();
            if (particle != null) {
                final byte realAlpha = (byte) Math.min(Math.max(Math.round(color.getAlpha() * brightness), 0), 255);
                particle.setLocation(loc);
                particle.setVelocity(vel);
                particle.setScaleAll(size);
                particle.setFacing(facing);
                particle.setTurnRate(spin);
                particle.setScaleRateAll(scale);
                particle.setColor(color.getRed(), color.getGreen(), color.getBlue(), realAlpha);
                particle.setEmissiveColor(color.getRed(), color.getGreen(), color.getBlue(), realAlpha);
                particle.setTimer(fadeIn, full, fadeOut);
                controller.refreshRemainingTimeToReset(Math.max(fadeIn + full + fadeOut, 0.0f));
                return true;
            }
            return false;
        }

        /**
         * @param durationIn factor, not in second!
         */
        public static boolean addHitParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float brightness, float durationIn, float totalDuration, Color color) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || totalDuration <= 0.0f || brightness < 1.0f || color.getAlpha() < 1) return true;
            final float fadeIn = totalDuration * durationIn, fadeOut = totalDuration - fadeIn;

            final var controller = Controllers.getHitParticle(isCampaign);
            return setParticle(
                    controller,
                    loc, vel, 0.0f, size, 0.0f, 0.0f, color, brightness, fadeIn, 0.0f, fadeOut);
        }

        public static boolean addHitParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float brightness, float duration, Color color) {
            return addHitParticle(isCampaign, loc, vel, size, brightness, 0.0f, duration, color);
        }

        public static boolean addHitParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float brightness, Color color) {
            float duration = 0.5f;
            if (size < 50.0f) {
                duration = size * 0.01f;
            } else if (size > 100.0f) {
                duration = 0.5f + (size - 100.0f) * 0.01f;
            }

            if (duration > 2.0f) {
                duration = 2.0f;
            }
            return addHitParticle(isCampaign, loc, vel, size, brightness, duration, color);
        }

        /**
         * @param brightness useless
         */
        public static boolean addSmoothParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float brightness, float rampUpFraction, float totalDuration, Color color) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || totalDuration <= 0.0f || color.getAlpha() < 1) return true;
            final float fadeIn = totalDuration * rampUpFraction, fadeOut = totalDuration - fadeIn;

            final var controller = Controllers.getSmoothParticle(isCampaign);
            return setParticle(
                    controller,
                    loc, vel, 0.0f, size, 0.0f, 0.0f, color, 1.0f, fadeIn, 0.0f, fadeOut);
        }

        /**
         * @param brightness useless
         */
        public static boolean addSmoothParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float brightness, float duration, Color color) {
            return addSmoothParticle(isCampaign, loc, vel, size, brightness, 0.0f, duration, color);
        }

        public static boolean addNegativeParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float rampUpFraction, float totalDuration, Color color) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || totalDuration <= 0.0f || color.getAlpha() < 1) return true;
            final float fadeIn = totalDuration * rampUpFraction, fadeOut = totalDuration - fadeIn;

            final var controller = Controllers.getNegativeParticle(isCampaign);
            return setParticle(
                    controller,
                    loc, vel, 0.0f, size, 0.0f, 0.0f, color, 1.0f, fadeIn, 0.0f, fadeOut);
        }

        /**
         * @param opacity useless
         */
        public static boolean addSmokeParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float opacity, float duration, Color color) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || duration <= 0.0f || color.getAlpha() < 1) return true;
            final float fadeIn = duration * (float) Math.random() * 0.5f, fadeOut = duration - fadeIn, scaleRate = size * 0.25f / duration;

            final var controller = Controllers.getSmokeParticle(isCampaign);
            return setParticle(
                    controller,
                    loc, vel, scaleRate, size, (float) Math.random() * 360.0f, (float) Math.random() * 140.0f - 70.0f, color, 1.0f, fadeIn, 0.0f, fadeOut);
        }

        private static boolean _nebulaParticleCommon(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color, final SimpleParticleControlData controller) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || totalDuration <= 0.0f || color.getAlpha() < 1) return true;
            final float fadeIn = Math.min(totalDuration * rampUpFraction, totalDuration),
                    full = totalDuration * Math.min(Math.max(fullBrightnessFraction - rampUpFraction, 0.0f), 1.0f),
                    fadeOut = totalDuration - fadeIn - full,
                    scaleRate = size * (endSizeMult - 1.0f) / totalDuration;
            float brightness = (fullBrightnessFraction > 0.0f && rampUpFraction > fullBrightnessFraction) ? Math.min(fullBrightnessFraction, 1.0f) : 1.0f;
            if (rampUpFraction > 1.0f) brightness /= rampUpFraction;

            return setParticle(
                    controller,
                    loc, vel, scaleRate, size, (float) Math.random() * 360.0f, 0.0f, color, brightness, fadeIn, full, fadeOut);
        }

        public static boolean addNebulaParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getNebulaParticle(isCampaign));
        }

        public static boolean addNegativeNebulaParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getNegativeNebulaParticle(isCampaign));
        }

        public static boolean addSwirlyNebulaParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getSwirlyNebulaParticle(isCampaign));
        }

        public static boolean addNegativeSwirlyNebulaParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getNegativeSwirlyNebulaParticle(isCampaign));
        }

        public static boolean addNebulaSmokeParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getNebulaSmokeParticle(isCampaign));
        }

        public static boolean addNebulaSmoothParticle(boolean isCampaign, Vector2f loc, Vector2f vel, float size, float endSizeMult, float rampUpFraction, float fullBrightnessFraction, float totalDuration, Color color) {
            return _nebulaParticleCommon(isCampaign, loc, vel, size, endSizeMult, rampUpFraction, fullBrightnessFraction, totalDuration, color, Controllers.getNebulaSmoothParticle(isCampaign));
        }

        private static float clampRad(final float in) {
            float result = in % TrigUtil.PI2_F;
            if (result < 0.0f) result += TrigUtil.PI2_F;
            return result;
        }

        public static boolean spawnExplosion(boolean isCampaign, Vector2f loc, Vector2f vel, Color color, float size, float maxDuration) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, size) || maxDuration <= 0.0f || color.getAlpha() < 1) return true;

            final SimpleParticleControlData[] controller = Controllers.getExplosion(isCampaign);
            final boolean[] withoutSetter = new boolean[]{true, true, true};

            final float baseSize = 20.0f + 0.12f * size, extraVel = 0.04f * size;
            final float rollCountF = (size * size) / (baseSize * baseSize) * 3.4435262f;
            final int count = Math.max((int) rollCountF, 5);

            Instance2Data particle;
            int roll;
            float finalSize, finalSizeEnd, rndRad, vecX, vecY, velOffsetLength, posOffsetLength;
            boolean pickRing, pickRound;

            for (int i = 0; i < count; ++i) {
                roll = (int) (Math.random() * 4.0f); // almost none 4 wtf
                if (roll == 3) roll = (int) (Math.random() * 4.0f);
                pickRound = roll == 4;
                pickRing = roll == 3;

                finalSize = baseSize + baseSize * (float) Math.random();
                finalSizeEnd = finalSize * 1.25f;
                if (pickRing) {
                    finalSizeEnd = finalSize * 3.0f;
                    finalSize = finalSizeEnd * 0.1f;
                } else if (pickRound) {
                    finalSize *= 1.5f;
                    finalSizeEnd = finalSize;
                }

                rndRad = clampRad(360.0f * (float) Math.random()); // wtf
                vecX = (float) Math.cos(rndRad);
                vecY = TrigUtil.sinFormCosRadiansF(vecX, rndRad);

                if (!pickRing && i > 4) posOffsetLength = size * 0.25f * (float) Math.random();
                else posOffsetLength = 0.0f;

                roll = Math.max(roll - 2, 0);
                particle = controller[roll].addParticle();
                if (particle != null) {
                    particle.setLocation(vecX * posOffsetLength + loc.x, vecY * posOffsetLength + loc.y);
                    if (pickRing) {
                        particle.setVelocity(vel.x, vel.y);
                    } else {
                        velOffsetLength = 10.0f + (float) Math.random() * extraVel;
                        particle.setVelocity(vecX * velOffsetLength + vel.x, vecY * velOffsetLength + vel.y);
                    }
                    particle.setFacing((float) (Math.random() * 360.0f));
                    particle.setScaleAll(finalSize);
                    particle.setScaleRateAll((finalSizeEnd - finalSize) / maxDuration);
                    particle.setEmissiveColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
                    particle.setTimer(0.0f, 0.0f, maxDuration);

                    if (withoutSetter[roll]) withoutSetter[roll] = false;
                } else {
                    for (byte c = 0; c < 3; ++c) if (!withoutSetter[c]) controller[c].refreshRemainingTimeToReset(maxDuration);
                    return false;
                }
            }

            for (byte c = 0; c < 3; ++c) if (!withoutSetter[c]) controller[c].refreshRemainingTimeToReset(maxDuration);
            return true;
        }

        private enum _DebrisType {
            SMALL(8.0f, 2.0f),
            MEDIUM(12.0f, 3.0f),
            LARGE(16.0f, 4.0f);

            final float size;
            final float fadeIn;
            final float full;
            final float fadeOut;
            final float totalDur;

            _DebrisType(final float size, final float totalDuration) {
                this.size = size;

                final float rampUpFraction = 0.05f / totalDuration, fullBrightnessFraction = 0.75f;
                final float fadeIn = Math.min(totalDuration * rampUpFraction, totalDuration),
                        full = totalDuration * Math.min(Math.max(fullBrightnessFraction - rampUpFraction, 0.0f), 1.0f),
                        fadeOut = totalDuration - fadeIn - full;

                this.fadeIn = fadeIn;
                this.full = full;
                this.fadeOut = fadeOut;
                this.totalDur = Math.max(this.fadeIn + this.full + this.fadeOut, 0.0f);
            }
        }

        private static float clampAngleRad(final float in) {
            float result = in % 360.0f;
            if (result < 0.0f) result += 360.0f;
            return (float) Math.toRadians(result);
        }

        private static boolean setDebrisParticle(SimpleParticleControlData controller, final _DebrisType type, final boolean isGlowDebris, final Vector2f loc, final Vector2f vel, final float facing, final float spread, final float minVel, final float velRange, final float maxRotation) {
            Instance2Data particle = controller.addParticle();
            if (particle != null) {
                final float size = type.size * (float) (Math.random() + 1.0f) * 0.5f,
                        currFacing = clampAngleRad((float) Math.random() * spread + facing - spread * 0.5f),
                        vecX = (float) Math.cos(currFacing),
                        vecY = TrigUtil.sinFormCosRadiansF(vecX, currFacing), vecLength = minVel + (float) Math.random() * velRange,
                        spawnFacing = clampAngleRad(facing + 90.0f + (float) Math.random() * 180.0f),
                        posX = (float) Math.cos(spawnFacing),
                        posY = TrigUtil.sinFormCosRadiansF(posX, spawnFacing);

                particle.setLocation(size * posX + loc.x, size * posY + loc.y);
                particle.setVelocity(vecLength * vecX + vel.x, vecLength * vecY + vel.y);
                particle.setScaleAll(size);
                particle.setTurnRate(((float) Math.random() - 0.5f) * maxRotation * 2.0f);
                if (isGlowDebris) particle.setEmissiveColor(255, (int) (155.0f + 100.0f * (float) Math.random()), 100, 255);
                particle.setTimer(type.fadeIn, type.full, type.fadeOut);
                return false;
            }
            return true;
        }

        private static boolean _spawnDebrisCommon(boolean isCampaign, final _DebrisType type, final SimpleParticleControlData[] controller, Vector2f loc, Vector2f vel, int num, float facing, float spread, float minVel, float velRange, float maxRotation) {
            if (!BoxConfigs.isShaderEnable()) return false;
            if (ignoreSpawn(isCampaign, loc, type.size) || num < 1) return true;
            final boolean[] withoutSetter = new boolean[]{true, true};

            byte picker;
            boolean spawnGlowDebris;
            for (int i = 0; i < num; i++) {
                spawnGlowDebris = Math.random() > 0.33f;
                picker = spawnGlowDebris ? BoxEnum.ONE : BoxEnum.ZERO;
                if (setDebrisParticle(controller[picker], type, spawnGlowDebris, loc, vel, facing, spread, minVel, velRange, maxRotation)) {
                    for (byte c = 0; c < 2; ++c) if (!withoutSetter[c]) controller[c].refreshRemainingTimeToReset(type.totalDur);
                    return false;
                } else if (withoutSetter[picker]) withoutSetter[picker] = false;
            }

            for (byte c = 0; c < 2; ++c) if (!withoutSetter[c]) controller[c].refreshRemainingTimeToReset(type.totalDur);
            return true;
        }

        public static boolean spawnDebrisSmall(boolean isCampaign, Vector2f loc, Vector2f vel, int num, float facing, float spread, float minVel, float velRange, float maxRotation) {
            return _spawnDebrisCommon(isCampaign, _DebrisType.SMALL, Controllers.getDebrisSmall(isCampaign),
                    loc, vel, num, facing, spread, minVel, velRange, maxRotation);
        }

        public static boolean spawnDebrisMedium(boolean isCampaign, Vector2f loc, Vector2f vel, int num, float facing, float spread, float minVel, float velRange, float maxRotation) {
            return _spawnDebrisCommon(isCampaign, _DebrisType.MEDIUM, Controllers.getDebrisMedium(isCampaign),
                    loc, vel, num, facing, spread, minVel, velRange, maxRotation);
        }

        public static boolean spawnDebrisLarge(boolean isCampaign, Vector2f loc, Vector2f vel, int num, float facing, float spread, float minVel, float velRange, float maxRotation) {
            return _spawnDebrisCommon(isCampaign, _DebrisType.LARGE, Controllers.getDebrisLarge(isCampaign),
                    loc, vel, num, facing, spread, minVel, velRange, maxRotation);
        }

        private VanillaFX() {}
    }

    public final static class SpecialFX {
        public final static class Controllers {
            public static SimpleParticleControlData getImpactDistortion(final boolean isCampaign) {
                final var key = "BUtil_SpecialFX_addDistortion";

                final var lock = getControllerLock(isCampaign, key);
                lock.lock();
                var controller = getControllerFromMap(isCampaign, key);

                if (controller == null || controller.isEntityExpired()) {
                    controller = new SimpleParticleControlData(8192, 6.4f, -5120.0f, false);

                    DistortionEntity entity = new DistortionEntity();
                    entity.setPowerIn(0.0f);
                    entity.setPowerFull(1.0f);
                    entity.setPowerOut(0.0f);
                    entity.setSizeOut(2.0f, 2.0f);
                    entity.setInnerFull(0.5f, 0.5f);
                    entity.setInnerOut(0.9f, 0.9f);
                    entity.setControlData(controller);

                    putControllerFromMap(isCampaign, entity, key, controller);
                }
                lock.unlock();
                return controller;
            }

            private static SimpleParticleControlData _getFlareCommon(final boolean isCampaign, final String key, final boolean isSharp) {
                final var lock = getControllerLock(isCampaign, key);

                lock.lock();
                var controller = getControllerFromMap(isCampaign, key);

                if (controller == null || controller.isEntityExpired()) {
                    controller = new SimpleParticleControlData(8192, 6.4f, -5120.0f, false);

                    FlareEntity entity = new FlareEntity();
                    entity.setSize(4.0f, 1.0f);
                    entity.setFlick(true);
                    entity.setFlickWhenPaused(false);
                    if (isSharp) entity.setSharpDisc(); else entity.setSmoothDisc();
                    entity.setGlowPower(0.25f);
                    entity.setAdditiveBlend();
                    entity.autoAspect();
                    entity.setLayer(defaultFXLayer(isCampaign));
                    entity.setControlData(controller);

                    putControllerFromMap(isCampaign, entity, key, controller);
                }
                lock.unlock();
                return controller;
            }

            public static SimpleParticleControlData getSmoothFlare(final boolean isCampaign) {
                return _getFlareCommon(isCampaign, "BUtil_SpecialFX_addSmoothFlare", false);
            }

            public static SimpleParticleControlData getSharpFlare(final boolean isCampaign) {
                return _getFlareCommon(isCampaign, "BUtil_SpecialFX_addSharpFlare", true);
            }
            
            private Controllers() {}
        }

        /**
         * @param maximumDurationTime Required, the total value about <code>fadeIn + full + fadeOut</code> time.
         *
         * @return <code>null</code> when failed, and don't store it, just setting the parameter it when get it.
         */
        public static @Nullable Instance2Data addImpactDistortion(boolean isCampaign, float maximumDurationTime) {
            final var controller = Controllers.getImpactDistortion(isCampaign);
            controller.refreshRemainingTimeToReset(Math.max(maximumDurationTime, 0.0f));
            return controller.addParticle();
        }

        /**
         * @param maximumDurationTime Required, the total value about <code>fadeIn + full + fadeOut</code> time.
         *
         * @return <code>null</code> when failed, and don't store it, just setting the parameter it when get it.
         */
        public static @Nullable Instance2Data addSmoothFlare(boolean isCampaign, float maximumDurationTime) {
            final var controller = Controllers.getSmoothFlare(isCampaign);
            controller.refreshRemainingTimeToReset(Math.max(maximumDurationTime, 0.0f));
            return controller.addParticle();
        }

        /**
         * @param maximumDurationTime Required, the total value about <code>fadeIn + full + fadeOut</code> time.
         *
         * @return <code>null</code> when failed, and don't store it, just setting the parameter it when get it.
         */
        public static @Nullable Instance2Data addSharpFlare(boolean isCampaign, float maximumDurationTime) {
            final var controller = Controllers.getSharpFlare(isCampaign);
            controller.refreshRemainingTimeToReset(Math.max(maximumDurationTime, 0.0f));
            return controller.addParticle();
        }

        private SpecialFX() {}
    }

    private RenderingUtil() {}
}
