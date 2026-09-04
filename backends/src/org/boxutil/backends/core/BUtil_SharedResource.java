package org.boxutil.backends.core;

import com.fs.starfarer.Version;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL43;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BUtil_SharedResource {
    private boolean lastSwapBit = false;
    private final AtomicBoolean swapQueue = new AtomicBoolean(false);
    private final Deque<Runnable> submitDeque = new ConcurrentLinkedDeque<>();

    @SuppressWarnings("unchecked")
    private final Deque<RenderDataAPI>[] sharedLogicalEntities = new Deque[]{new ConcurrentLinkedDeque<RenderDataAPI>(), new ConcurrentLinkedDeque<RenderDataAPI>()};
    private final Deque<BackgroundEveryFramePlugin> renderingPluginDelayAdder = new ConcurrentLinkedDeque<>();
    private final List<BackgroundEveryFramePlugin> renderingPlugins = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private final Deque<BackgroundEveryFramePlugin>[] logicalPlugins = new Deque[]{new ConcurrentLinkedDeque<BackgroundEveryFramePlugin>(), new ConcurrentLinkedDeque<BackgroundEveryFramePlugin>()};

    private final Deque<Throwable> threadCatchExceptionCollection = new ConcurrentLinkedDeque<>();
    private final AtomicBoolean threadCatchException = new AtomicBoolean(false);

    private byte pickDeque() {
        return this.swapQueue.get() ? BoxEnum.ONE : BoxEnum.ZERO;
    }

    private byte pickNextDeque() {
        return this.swapQueue.get() ? BoxEnum.ZERO : BoxEnum.ONE;
    }

    public void offerSubmitInstance(final Runnable command) {
        this.submitDeque.offer(command);
    }

    public void runEntitySubmit(final boolean atLast) {
        this.swapQueue.compareAndSet(!this.lastSwapBit, this.lastSwapBit);
        final Deque<Runnable> queue = this.submitDeque;
        Runnable command;
        while ((command = atLast ? queue.pollLast() : queue.pollFirst()) != null) {
            command.run();
        }
        if (BoxDatabase.getGLState().GL_GL43 && !BoxDatabase.getGLState().GL_GL44) GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void refreshQueueBit() {
        this.lastSwapBit = !this.lastSwapBit;
    }

    public Deque<RenderDataAPI> getSharedLogicalEntities() {
        return this.sharedLogicalEntities[this.pickDeque()];
    }

    public Deque<RenderDataAPI> getNextSharedLogicalEntities() {
        return this.sharedLogicalEntities[this.pickNextDeque()];
    }

    public List<BackgroundEveryFramePlugin> getRenderingPlugins() {
        return this.renderingPlugins;
    }

    // Only rendering thread
    public void delayAddRenderingPlugin() {
        BackgroundEveryFramePlugin toAdd;
        while ((toAdd = this.renderingPluginDelayAdder.poll()) != null) {
            this.renderingPlugins.add(toAdd);
        }
    }

    public Deque<BackgroundEveryFramePlugin> getLogicalPlugins() {
        return this.logicalPlugins[this.pickDeque()];
    }

    public Deque<BackgroundEveryFramePlugin> getNextLogicalPlugins() {
        return this.logicalPlugins[this.pickNextDeque()];
    }

    public boolean offerBackgroundRenderingPlugin(@NotNull final BackgroundEveryFramePlugin plugin) {
        return this.renderingPluginDelayAdder.offer(plugin);
    }

    public boolean containsBackgroundRenderingPlugin(@NotNull final BackgroundEveryFramePlugin plugin) {
        return this.renderingPlugins.contains(plugin) || this.renderingPluginDelayAdder.contains(plugin);
    }

    public boolean offerBackgroundLogicalPlugin(@NotNull final BackgroundEveryFramePlugin plugin) {
        return this.logicalPlugins[this.pickNextDeque()].offer(plugin);
    }

    public boolean containsBackgroundLogicalPlugin(@NotNull final BackgroundEveryFramePlugin plugin) {
        return this.logicalPlugins[this.pickDeque()].contains(plugin) || this.logicalPlugins[this.pickNextDeque()].contains(plugin);
    }

    public void pushThreadException(final Throwable exception) {
        this.threadCatchExceptionCollection.offerLast(exception);
        this.threadCatchException.set(true);
    }

    public void checkShouldCloseGame() {
        if (this.threadCatchException.get()) {
            StringBuilder eStr = new StringBuilder();
            Throwable e;
            while ((e = this.threadCatchExceptionCollection.pollFirst()) != null) {
                eStr.append('\t').append(e.getClass().getName()).append(": ").append(e.getMessage()).append('\n');
            }
            Sys.alert(Version.versionString, BoxConfigs.getString("BUtil_ThreadAlertMessage").replace("%s", eStr));
            System.exit(1);
        }
    }
}
