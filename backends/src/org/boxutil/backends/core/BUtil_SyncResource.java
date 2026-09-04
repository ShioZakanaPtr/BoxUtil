package org.boxutil.backends.core;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BUtil_SyncResource {
    private final AtomicBoolean currentSyncFlag = new AtomicBoolean(false);

    private final Phaser beforeRendering = new Phaser(1);
    private final Phaser beginRendering = new Phaser(1);
    private final Phaser beginIllumination = new Phaser(1);
    private final Phaser afterRendering = new Phaser(1);

    private final Phaser beginAdvance = new Phaser(1);
    private final Phaser beginPoolCompact = new Phaser(0);
    private final Phaser beginInstanceCompute = new Phaser(0);
    private final Phaser finishAdvance = new Phaser(1);

    public boolean shouldLogicalSync() {
        return this.currentSyncFlag.compareAndSet(false, true);
    }

    public boolean shouldRenderingSync() {
        return this.currentSyncFlag.compareAndSet(true, false);
    }

    public Phaser getBeforeRendering() {
        return beforeRendering;
    }

    public Phaser getBeginRendering() {
        return beginRendering;
    }

    public Phaser getBeginIllumination() {
        return beginIllumination;
    }

    public Phaser getAfterRendering() {
        return afterRendering;
    }

    public Phaser getBeginAdvance() {
        return beginAdvance;
    }

    public Phaser getBeginPoolCompact() {
        return beginPoolCompact;
    }

    public Phaser getBeginInstanceCompute() {
        return beginInstanceCompute;
    }

    public Phaser getFinishAdvance() {
        return finishAdvance;
    }
}
