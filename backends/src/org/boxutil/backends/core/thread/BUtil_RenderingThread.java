package org.boxutil.backends.core.thread;

import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.GLWrapper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.*;

import java.util.Iterator;

final class BUtil_RenderingThread extends BUtil_BoxUtilBackgroundThread.ThreadTemplate {
    BUtil_RenderingThread(Thread hostThread, Drawable sharedDrawable, Object ignored) {
        super(hostThread, sharedDrawable, ignored);
    }

    private interface InvokePluginFun {
        void run(BackgroundEveryFramePlugin plugin, float amount, boolean isPaused);
    }

    private void runThreadPlugin(@NotNull final InvokePluginFun stage) {
        final Iterator<BackgroundEveryFramePlugin> list = BUtil_ResourceStorage.sharedResource().getRenderingPlugins().iterator();
        final float amount = BUtil_GLImpl.getLastFrameAmount();
        final boolean isPaused = BUtil_GLImpl.isPaused();

        BackgroundEveryFramePlugin plugin;

        while (list.hasNext()) {
            plugin = list.next();
            if (plugin == null) continue;
            stage.run(plugin, amount, isPaused);
            if (plugin.isRenderingExpired()) list.remove();
        }
    }

    protected void runBody() {
        BoxThreadSync.Rendering.beforeRendering().arriveAndAwaitAdvance();
        BUtil_ResourceStorage.sharedResource().runEntitySubmit(true);
        BUtil_ResourceStorage.sharedResource().delayAddRenderingPlugin();

        BoxThreadSync.Rendering.beginRendering().arriveAndAwaitAdvance();
        this.runThreadPlugin(BackgroundEveryFramePlugin::runBeginRendering);

        BoxThreadSync.Rendering.beginIllumination().arriveAndAwaitAdvance();
        this.runThreadPlugin(BackgroundEveryFramePlugin::runBeginIllumination);

        BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
        this.runThreadPlugin(BackgroundEveryFramePlugin::runAfterRendering);
        if (!this._FAILED) GLWrapper.Operation.Sync.glFlush();
    }

    protected void logicalInit() {
        BoxThreadSync.Rendering.beforeRendering().register();
        BoxThreadSync.Rendering.beginRendering().register();
        BoxThreadSync.Rendering.beginIllumination().register();
        BoxThreadSync.Rendering.afterRendering().register();
    }

    protected void logicalDestroy() {
        BoxThreadSync.Rendering.beforeRendering().arriveAndDeregister();
        BoxThreadSync.Rendering.beginRendering().arriveAndDeregister();
        BoxThreadSync.Rendering.beginIllumination().arriveAndDeregister();
        BoxThreadSync.Rendering.afterRendering().arriveAndDeregister();
    }
}
