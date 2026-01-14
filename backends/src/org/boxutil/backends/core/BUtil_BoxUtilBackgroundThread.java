package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class BUtil_BoxUtilBackgroundThread {
    private final static ExecutorService __POOL = Executors.newFixedThreadPool(3, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });
    private static BUtil_RenderingThread __RENDERING_THREAD = null;
    private static BUtil_LogicalThread __LOGICAL_THREAD = null;
    private static BUtil_LogicalThread __LOGICAL_AUX_THREAD = null;

    private static boolean _INIT = false;
    private static boolean _VALID = false;

    public static boolean initWithFailedCheck() {
        if (_INIT) return _VALID;
        _INIT = true;

        {
            try {
                __RENDERING_THREAD = new BUtil_RenderingThread(Thread.currentThread(), new SharedDrawable(Display.getDrawable()));
                __POOL.execute(__RENDERING_THREAD);
                __RENDERING_THREAD._INIT_SYNC.await();
                __RENDERING_THREAD._INIT_SYNC = null;
            } catch (Exception e) {
                if (e instanceof LWJGLException lwjglException)
                    CommonUtil.printThrowable(ShaderCore.class, "'BoxUtil' rendering additional thread failed: ", lwjglException);
            }
            _VALID |= __RENDERING_THREAD._FAILED.get();
            __RENDERING_THREAD.clearTmpSync();
        }

        {
            try {
                __LOGICAL_THREAD = new BUtil_LogicalThread(Thread.currentThread(), new SharedDrawable(Display.getDrawable()), false);
                __POOL.execute(__LOGICAL_THREAD);
                __LOGICAL_THREAD._INIT_SYNC.await();
                __LOGICAL_THREAD._INIT_SYNC = null;
            } catch (Exception e) {
                if (e instanceof LWJGLException lwjglException)
                    CommonUtil.printThrowable(ShaderCore.class, "'BoxUtil' logical additional thread failed: ", lwjglException);
            }
            _VALID |= __LOGICAL_THREAD._FAILED.get();
            __LOGICAL_THREAD.clearTmpSync();
        }

        {
            try {
                __LOGICAL_AUX_THREAD = new BUtil_LogicalThread(Thread.currentThread(), new SharedDrawable(Display.getDrawable()), true);
                __POOL.execute(__LOGICAL_AUX_THREAD);
                __LOGICAL_AUX_THREAD._INIT_SYNC.await();
                __LOGICAL_AUX_THREAD._INIT_SYNC = null;
            } catch (Exception e) {
                if (e instanceof LWJGLException lwjglException)
                    CommonUtil.printThrowable(ShaderCore.class, "'BoxUtil' logical-aux additional thread failed: ", lwjglException);
            }
            _VALID |= __LOGICAL_AUX_THREAD._FAILED.get();
            __LOGICAL_AUX_THREAD.clearTmpSync();
        }
        return _VALID;
    }

    static abstract class _ThreadTemplate implements Runnable {
        CountDownLatch _INIT_SYNC = new CountDownLatch(1);
        AtomicBoolean _FAILED = new AtomicBoolean(true);

        protected final Thread _HOST_THREAD;
        protected final Drawable _DRAWABLE;
        protected final Logger _LOG;

        _ThreadTemplate(final Thread hostThread, final Drawable sharedDrawable) {
            this._HOST_THREAD = hostThread;
            this._DRAWABLE = sharedDrawable;
            this._LOG = Global.getLogger(this.getClass());
        }

        void destroyDrawable() {
            if (this._DRAWABLE != null) this._DRAWABLE.destroy();
        }

        void clearTmpSync() {
            this._INIT_SYNC = null;
            this._FAILED = null;
        }

        private void glInit() {
            try {
                this._DRAWABLE.makeCurrent();
            } catch (LWJGLException e) {
                CommonUtil.printThrowable(this._LOG, "'BoxUtil' additional thread gl-context failed: ", e);
                this._INIT_SYNC.countDown();
                return;
            }
            {
                int _glError = GL11.glGetError();
                if (_glError != 0) {
                    this.destroyDrawable();
                    CommonUtil.printThrowable(this._LOG, "'BoxUtil' additional thread gl-context failed: ", new OpenGLException(_glError));
                    this._INIT_SYNC.countDown();
                    return;
                } else this._FAILED.set(false);
            }
            this._INIT_SYNC.countDown();
        }

        protected abstract void logicalInit();
        protected abstract void logicalDestroy();
        protected abstract void runBody();

        public void run() {
            final var currentThread = Thread.currentThread();
            currentThread.setName(currentThread.getName() + "-AS-" + this.getClass().getSimpleName());
            this.glInit();
            if (this._FAILED.get()) this.destroyDrawable(); else this.logicalInit();
            this._LOG.info("'BoxUtil' additional thread running.");

            try { while (!currentThread.isInterrupted()) {
                if (this._HOST_THREAD == null || !this._HOST_THREAD.isAlive()) break;
                this.runBody();
            }} catch (Throwable e) {
                CommonUtil.printThrowable(this._LOG, "'BoxUtil' additional thread catch: \n", e);
                this.destroyDrawable();
                this.logicalDestroy();
                this._LOG.info("'BoxUtil' additional thread destroy by exception.");
                BUtil_ThreadResource.pushThreadException(e);
                return;
            }
            this.destroyDrawable();
            this.logicalDestroy();
            this._LOG.info("'BoxUtil' additional thread destroy.");
        }
    }
}
