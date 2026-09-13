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
    BUtil_RenderingThread(Thread host, Drawable sharedDrawable, Object ignored) {
        super(host, sharedDrawable, ignored);
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
        if (this.tryArrive(BoxThreadSync.Rendering.beforeRendering())) return;
        if (!this.checkExit()) BUtil_ResourceStorage.sharedResource().runEntitySubmit(true);
        if (!this.checkExit()) BUtil_ResourceStorage.sharedResource().delayAddRenderingPlugin();

        if (this.tryArrive(BoxThreadSync.Rendering.beginRendering())) return;
        if (!this.checkExit()) this.runThreadPlugin(BackgroundEveryFramePlugin::runBeginRendering);

        if (this.tryArrive(BoxThreadSync.Rendering.beginIllumination())) return;
        if (!this.checkExit()) this.runThreadPlugin(BackgroundEveryFramePlugin::runBeginIllumination);

        if (this.tryArrive(BoxThreadSync.Rendering.afterRendering())) return;
        if (!this.checkExit()) this.runThreadPlugin(BackgroundEveryFramePlugin::runAfterRendering);
        if (!(this._FAILED || this.checkExit())) GLWrapper.Operation.Sync.glFlush();
    }

    protected void logicalInit() {
        registerPhaser(BoxThreadSync.Rendering.beforeRendering());
        registerPhaser(BoxThreadSync.Rendering.beginRendering());
        registerPhaser(BoxThreadSync.Rendering.beginIllumination());
        registerPhaser(BoxThreadSync.Rendering.afterRendering());
    }

    protected void logicalDestroy() {
        deregisterPhaser(BoxThreadSync.Rendering.beforeRendering());
        deregisterPhaser(BoxThreadSync.Rendering.beginRendering());
        deregisterPhaser(BoxThreadSync.Rendering.beginIllumination());
        deregisterPhaser(BoxThreadSync.Rendering.afterRendering());
    }
}
