package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import org.boxutil.backends.core.gameloop.BUtil_CombatEFS;
import org.boxutil.backends.util.BUtil_AtomicEnumSet32;
import org.boxutil.util.RenderingUtil;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BUtil_CombatLayeredResource extends BUtil_LayeredResourceTemplate<CombatEngineLayers> {
    private final BUtil_AtomicEnumSet32<CombatEngineLayers> activeLayerSet = new BUtil_AtomicEnumSet32<>(CombatEngineLayers.class);
    private final Set<DamagingProjectileAPI> autogenMarkedProj = ConcurrentHashMap.newKeySet(8192);

    public BUtil_CombatLayeredResource() {
        super(CombatEngineLayers.class);
    }

    protected boolean isCampaign() {
        return false;
    }

    protected void cleanupAllVanillaPlugin() {
        this.activeLayerSet.clear();
    }

    protected void checkRenderingPluginAndAdd(CombatEngineLayers layer) {
        if (this.activeLayerSet.add(layer)) {
            Global.getCombatEngine().addLayeredRenderingPlugin(new BUtil_CombatEFS.Renderer(layer));
            this.log.info("'BoxUtil' rendering plugin join layer: '" + layer.name() + '\'');
        }
    }

    public void initBasicLayers() {
        this.lock.lock();
        checkRenderingPluginAndAdd(RenderingUtil.getLowestCombatLayer());
        checkRenderingPluginAndAdd(RenderingUtil.getHighestCombatLayer());
        this.lock.unlock();
    }

    protected void cleanupVanillaPlugin(CombatEngineLayers layer) {
        this.activeLayerSet.remove(layer);
    }

    protected CombatEngineLayers layerCast(Object layerRaw) {
        if (layerRaw instanceof CombatEngineLayers cast) return cast;
        else return null;
    }

    public boolean checkAndMarkAutogenProjectile(final DamagingProjectileAPI proj) {
        return this.autogenMarkedProj.add(proj);
    }
}
