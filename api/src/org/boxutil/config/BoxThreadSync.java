package org.boxutil.config;

import java.util.concurrent.Phaser;

/**
 * <strong>make sure you are understand what you want to do.</strong>
 */
public final class BoxThreadSync {
    private final static Phaser _LOCK_BEFORE_RENDERING = new Phaser(1);
    private final static Phaser _LOCK_BEGIN_RENDERING = new Phaser(1);
    private final static Phaser _LOCK_BEGIN_ILLUMINATION = new Phaser(1);
    private final static Phaser _LOCK_AFTER_RENDERING = new Phaser(1);

    private final static Phaser _LOCK_BEGIN_ADVANCE = new Phaser(1);
    private final static Phaser _LOCK_BEGIN_POOL_COMPACT = new Phaser(0);
    private final static Phaser _LOCK_BEGIN_INSTANCE_COMPUTE = new Phaser(0);
    private final static Phaser _LOCK_FINISH_ADVANCE = new Phaser(1);

    public final static class Rendering {
        public static Phaser beforeRendering() {
            return _LOCK_BEFORE_RENDERING;
        }

        public static Phaser beginRendering() {
            return _LOCK_BEGIN_RENDERING;
        }

        public static Phaser beginIllumination() {
            return _LOCK_BEGIN_ILLUMINATION;
        }

        public static Phaser afterRendering() {
            return _LOCK_AFTER_RENDERING;
        }

        private Rendering() {}
    }

    public final static class Logical {
        public static Phaser beginAdvance() {
            return _LOCK_BEGIN_ADVANCE;
        }

        public static Phaser beginPoolCompact() {
            return _LOCK_BEGIN_POOL_COMPACT;
        }

        public static Phaser beginInstanceCompute() {
            return _LOCK_BEGIN_INSTANCE_COMPUTE;
        }

        public static Phaser finishAdvance() {
            return _LOCK_FINISH_ADVANCE;
        }

        private Logical() {}
    }

    private BoxThreadSync() {}
}
