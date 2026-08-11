package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.CommonUtil;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import java.util.concurrent.*;

public final class BUtil_BoxUtilBackgroundThread {
    private final static ExecutorService __POOL = Executors.newFixedThreadPool(3, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    private final static byte _RENDERING_THREAD = 0;
    private final static byte _LOGICAL_THREAD = 1;
    private final static byte _LOGICAL_AUX_THREAD = 2;
    private final static byte _TOTAL_THREAD = 3;
    private final static _ThreadTemplate[] _THREAD_RUNNABLE = new _ThreadTemplate[_TOTAL_THREAD];
    private final static Thread[] _THREAD = new Thread[_TOTAL_THREAD];

    private static boolean _INIT = false;
    private static boolean _VALID = true;

    @FunctionalInterface
    private interface _ThreadInit {
        _ThreadTemplate apply(Thread thread, Drawable drawable, Object args);
    }

    private static void setupThread(byte target, final _ThreadInit thread, final Object args, final String name) {
        try {
            _THREAD_RUNNABLE[target] = thread.apply(Thread.currentThread(), new SharedDrawable(Display.getDrawable()), args);
            __POOL.execute(_THREAD_RUNNABLE[target]);
            _THREAD_RUNNABLE[target]._INIT_SYNC.await();
            _THREAD[target] = _THREAD_RUNNABLE[target]._CURR_THREAD;
        } catch (Exception e) {
            if (e instanceof LWJGLException lwjglException) CommonUtil.printThrowable(ShaderCore.class, "'BoxUtil' " + name + " additional thread failed: ", lwjglException);
        } finally {
            _VALID &= !_THREAD_RUNNABLE[target]._FAILED;
            _THREAD_RUNNABLE[target].clearTmpSync();
        }
    }

    public static boolean initWithFailedCheck() {
        if (_INIT) return _VALID;
        _INIT = true;

        setupThread(_RENDERING_THREAD, BUtil_RenderingThread::new, null, "rendering");
        setupThread(_LOGICAL_THREAD, BUtil_LogicalThread::new, false, "logical");
        setupThread(_LOGICAL_AUX_THREAD, BUtil_LogicalThread::new, true, "logical-aux");
        return _VALID;
    }

    static abstract class _ThreadTemplate implements Runnable {
        protected boolean _FAILED = true;
        protected CountDownLatch _INIT_SYNC = new CountDownLatch(1);

        protected final Thread _HOST_THREAD;
        protected final Drawable _DRAWABLE;
        protected final Logger _LOG;

        protected Thread _CURR_THREAD = null;

        _ThreadTemplate(final Thread hostThread, final Drawable sharedDrawable, final Object args) {
            this._HOST_THREAD = hostThread;
            this._DRAWABLE = sharedDrawable;
            this._LOG = Global.getLogger(this.getClass());
        }

        void destroyDrawable() {
            if (this._DRAWABLE != null) this._DRAWABLE.destroy();
        }

        void clearTmpSync() {
            this._INIT_SYNC = null;
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
                final int _glError = GL11.glGetError();
                this._FAILED = _glError != 0;
                if (this._FAILED) {
                    this.destroyDrawable();
                    CommonUtil.printThrowable(this._LOG, "'BoxUtil' additional thread gl-context failed: ", new OpenGLException(_glError));
                    this._INIT_SYNC.countDown();
                    return;
                }
            }
            this._INIT_SYNC.countDown();
        }

        protected abstract void logicalInit();
        protected abstract void logicalDestroy();
        protected abstract void runBody();

        public void run() {
            this._CURR_THREAD = Thread.currentThread();
            this._CURR_THREAD.setName(this._CURR_THREAD.getName() + "-AS-" + this.getClass().getSimpleName());
            this.glInit();
            if (this._FAILED) this.destroyDrawable();
            this.logicalInit();
            this._LOG.info("'BoxUtil' additional thread running.");

            try {
                while (!this._CURR_THREAD.isInterrupted()) {
                    if (this._HOST_THREAD == null || !this._HOST_THREAD.isAlive()) break;
                    this.runBody();
                }
            } catch (Throwable e) {
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

    public static Thread getRenderingThread() {
        return _THREAD[_RENDERING_THREAD];
    }

    public static Thread getLogicalThread() {
        return _THREAD[_LOGICAL_THREAD];
    }

    public static Thread getLogicalAuxThread() {
        return _THREAD[_LOGICAL_AUX_THREAD];
    }

    public static boolean isGLValid() {
        return _VALID;
    }
}
