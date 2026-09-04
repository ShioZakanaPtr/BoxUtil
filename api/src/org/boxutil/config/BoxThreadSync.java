package org.boxutil.config;

import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL44;

import java.util.concurrent.Phaser;

/**
 * <strong>Make sure you are understand what you want to do.</strong>
 */
public final class BoxThreadSync {
    /**
     * In rendering loop: beforeRendering => runBeforeRenderingDelayGLCmd => applyMemoryBarrier => beginRendering => beginIllumination => runBeginIlluminationDelayGLCmd => afterRendering => logical...
     */
    public final static class Rendering {
        public static Phaser beforeRendering() {
            return BUtil_ResourceStorage.syncResource().getBeforeRendering();
        }

        /**
         * Submits OpenGL command packets to the deferred execution queue associated with the specified barrier point.<p>
         * These packets are executed on the main thread during the relevant phase.<p>
         * The execution order of the packets in the queue should not be assumed to be the same as the order in which they were enqueued.
         *
         * @param inCampaign <code>true</code> if in campaign, otherwise <code>false</code> if in combat.
         */
        public static void offerBeforeRenderingDelayGLCmd(final Runnable cmd, boolean inCampaign) {
            if (inCampaign) BUtil_ResourceStorage.campaignLayered().offerGLCmdBeforeRendering(cmd);
            else BUtil_ResourceStorage.combatLayered().offerGLCmdBeforeRendering(cmd);
        }

        /**
         * Submits OpenGL memory barrier to the collector.<p>
         * These barriers are apply on the main thread at just before the <code>beginRendering</code> sync.<p>
         * Has default barriers in each frame: {@link GL42#GL_BUFFER_UPDATE_BARRIER_BIT}, {@link GL43#GL_SHADER_STORAGE_BARRIER_BIT}, {@link GL44#GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT}<p>
         *
         * @param barriers Same as {@link GL42#glMemoryBarrier(int)}, the provided barrier bits are combined internally using a bitwise OR,
         *                 and are executed uniformly after the barrier point, thereby avoiding multiple repeated barrier calls.
         */
        public static void offerMemoryBarrier(int barriers, boolean inCampaign) {
            if (inCampaign) BUtil_ResourceStorage.campaignLayered().offerMemoryBarrier(barriers);
            else BUtil_ResourceStorage.combatLayered().offerMemoryBarrier(barriers);
        }

        public static Phaser beginRendering() {
            return BUtil_ResourceStorage.syncResource().getBeginRendering();
        }

        public static Phaser beginIllumination() {
            return BUtil_ResourceStorage.syncResource().getBeginIllumination();
        }

        /**
         * Submits OpenGL command packets to the deferred execution queue associated with the specified barrier point.<p>
         * These packets are executed on the main thread during the relevant phase.<p>
         * The execution order of the packets in the queue should not be assumed to be the same as the order in which they were enqueued.
         *
         * @param inCampaign <code>true</code> if in campaign, otherwise <code>false</code> if in combat.
         */
        public static void offerBeginIlluminationDelayGLCmd(final Runnable cmd, boolean inCampaign) {
            if (inCampaign) BUtil_ResourceStorage.campaignLayered().offerGLCmdBeginIllumination(cmd);
            else BUtil_ResourceStorage.combatLayered().offerGLCmdBeginIllumination(cmd);
        }

        public static Phaser afterRendering() {
            return BUtil_ResourceStorage.syncResource().getAfterRendering();
        }

        private Rendering() {}
    }

    /**
     * In rendering loop: rendering... => beginAdvance => [next frame] => finishAdvance => next frame rendering...
     */
    public final static class Logical {
        public static Phaser beginAdvance() {
            return BUtil_ResourceStorage.syncResource().getBeginAdvance();
        }

        /**
         * Submits OpenGL command packets to the deferred execution queue associated with the specified barrier point.<p>
         * These packets are executed on the main thread during the relevant phase.<p>
         * The execution order of the packets in the queue should not be assumed to be the same as the order in which they were enqueued.
         *
         * @param inCampaign <code>true</code> if in campaign, otherwise <code>false</code> if in combat.
         */
        public static void offerBeginAdvanceDelayGLCmd(final Runnable cmd, boolean inCampaign) {
            if (inCampaign) BUtil_ResourceStorage.campaignLayered().offerGLCmdBeginAdvance(cmd);
            else BUtil_ResourceStorage.combatLayered().offerGLCmdBeginAdvance(cmd);
        }

        public static Phaser beginPoolCompact() {
            return BUtil_ResourceStorage.syncResource().getBeginPoolCompact();
        }

        public static Phaser beginInstanceCompute() {
            return BUtil_ResourceStorage.syncResource().getBeginInstanceCompute();
        }

        public static Phaser finishAdvance() {
            return BUtil_ResourceStorage.syncResource().getFinishAdvance();
        }

        private Logical() {}
    }

    private BoxThreadSync() {}
}
