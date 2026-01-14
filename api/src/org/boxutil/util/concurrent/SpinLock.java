package org.boxutil.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock {
    protected final AtomicReference<Thread> _ref = new AtomicReference<>(null);

    public void lock() {
        while (!this._ref.compareAndSet(null, Thread.currentThread())) {
            Thread.onSpinWait();
        }
    }

    public void unlock() {
        final Thread curr = Thread.currentThread();
        if (this._ref.get() != curr) throw new IllegalMonitorStateException("Unlock failed, not held by thread: '" + curr.getName() + '\'');
        this._ref.compareAndSet(curr, null);
    }

    public Thread getOwner() {
        return this._ref.get();
    }

    public boolean isLocked() {
        return this._ref.get() != null;
    }
}
