package org.boxutil.backends.core;

import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.config.BoxThreadSync;
import org.lwjgl.opengl.*;

import java.util.Iterator;

final class BUtil_RenderingThread extends BUtil_BoxUtilBackgroundThread._ThreadTemplate {
    BUtil_RenderingThread(Thread hostThread, Drawable sharedDrawable) {
        super(hostThread, sharedDrawable);
    }

    private enum ExcFun {
        BEGIN_RENDERING,
        BEGIN_ILLUMINATION,
        AFTER_RENDERING;
    }


    private void runThreadPlugin(final ExcFun stage) {
        final Iterator<BackgroundEveryFramePlugin> list = BUtil_ThreadResource.Rendering.getThreadPluginQueue().iterator();
        final float amount = BUtil_ThreadResource._CURR_AMOUNT;
        final boolean isPaused = BUtil_ThreadResource._CURR_PAUSED;

        BackgroundEveryFramePlugin plugin;
        while (list.hasNext()) {
            plugin = list.next();
            if (plugin == null) continue;
            switch (stage) {
                case BEGIN_RENDERING -> plugin.runBeginRendering(amount, isPaused);
                case BEGIN_ILLUMINATION -> plugin.runBeginIllumination(amount, isPaused);
                case AFTER_RENDERING -> plugin.runAfterRendering(amount, isPaused);
            }
            if (plugin.isRenderingExpired()) list.remove();
        }
    }

    private void sendBeginRenderingSync() {
        BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_BEGIN_RENDERING);
    }

    private void sendBeginIlluminationSync() {
        BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ILLUMINATION);
    }

    private void sendAfterRenderingSync() {
        BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_AFTER_RENDERING);
    }

    protected void runBody() {
        BoxThreadSync.Rendering.beforeRendering().arriveAndAwaitAdvance();
        BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_FINISH_ADVANCE_HOST);
        BUtil_ThreadResource.Logical.runEntitySubmit(true);

        BoxThreadSync.Rendering.beginRendering().arriveAndAwaitAdvance();
        this.runThreadPlugin(ExcFun.BEGIN_RENDERING);
        this.sendBeginIlluminationSync();

        BoxThreadSync.Rendering.beginIllumination().arriveAndAwaitAdvance();
        this.runThreadPlugin(ExcFun.BEGIN_ILLUMINATION);
        this.sendAfterRenderingSync();

        BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
        this.runThreadPlugin(ExcFun.AFTER_RENDERING);
        this.sendBeginRenderingSync();
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
