package org.boxutil;

import com.fs.starfarer.Version;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.boxutil.backends.core.BUtil_CampaignEFS;
import org.boxutil.backends.core.BUtil_ThreadResource;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.*;

/**
 * A multi-thread OpenGL based lib/engine of the game <a href="https://fractalsoftworks.com/">Starsector</a>.
 * <blockquote>Environment: JDK17.0.10_zulu</blockquote>
 * <blockquote>Repository: <a href="https://www.fossic.org/thread-15746-1-1.html">https://www.fossic.org/thread-15746-1-1.html</a></blockquote>
 * <blockquote>For how to use <strong>BoxUtil</strong> as lib: <a href="rd">rd</a></blockquote>
 * @since 2024.08.18
 * @author ShioZakana
 * @version 2026.01.14 - 1.5.0
 */
public final class BoxUtilModPlugin extends BaseModPlugin {
    private static final String _ADAPTATION_VERSION = "0.98";
    private static boolean isPreInitialized = false;

    /**
     * Optional.
     */
    public synchronized static void initPre() {
        if (isPreInitialized) return;
        Global.getSettings().getModManager().getModSpec(BoxDatabase.MOD_ID).setSortString("\t\t\t\t\tBoxUtil");
        BoxDatabase.initGLState();
        BoxConfigs.init();
        ShaderCore.initScreenSize();
        ModelManager.loadModelDataCSV(BoxDatabase.BUILTIN_OBJ_CSV);
        EntityShadingDataManager.loadTextureData(BoxDatabase.BUILTIN_TEXTURE_CSV);
        EntityShadingDataManager.loadIlluminantData(BoxDatabase.BUILTIN_ILLUMINANT_CSV);
        isPreInitialized = true;
    }

    /**
     * Optional.<p>
     * Make sure OpenGL context has created.
     */
    public synchronized static void initLater() {
        BoxConfigGUI.globalInitLater();
    }

    public static boolean isPreInitialized() {
        return isPreInitialized;
    }

    public static boolean isGlobalInitialized() {
        return BoxConfigGUI.isGlobalInitialized();
    }

    public void onApplicationLoad() {
        if (!Version.versionInfoForMods.getMajor().contentEquals(_ADAPTATION_VERSION)) {
            String version = Global.getSettings().getModManager().getModSpec(BoxDatabase.MOD_ID).getVersionInfo().getString();
            throw new RuntimeException("'BoxUtil' this mod with version '" + version + "' should running at Starsector '" + _ADAPTATION_VERSION + "', current game major version is '" + Version.versionInfoForMods.getMajor() + "'.");
        }
        initPre();
    }

    private void initCampaignPlugin() {
        if (Global.getSector() != null && !Global.getSector().getListenerManager().hasListenerOfClass(BUtil_CampaignEFS.class)) {
            Global.getSector().addTransientScript(new BUtil_CampaignEFS());
        }
    }

    public void onGameLoad(boolean newGame) {
        initCampaignPlugin();
    }

    public void beforeGameSave() {
        if (Global.getSector() != null) Global.getSector().getListenerManager().removeListenerOfClass(BUtil_CampaignEFS.class);
        BUtil_ThreadResource.Rendering.Campaign.cleanupQueue();
        BUtil_ThreadResource.Rendering.Campaign.cleanupCustomData();
    }

    public void afterGameSave() {
        initCampaignPlugin();
    }
}