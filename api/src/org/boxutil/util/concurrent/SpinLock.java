package org.boxutil.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SpinLock implements Lock {
    protected final AtomicReference<Thread> _ref = new AtomicReference<>(null);

    protected void checkIsSameThread() {
        if (this.getOwner() == Thread.currentThread()) throw new IllegalMonitorStateException("Lock failed, already held by current thread.");
    }

    public void lock() {
        this.checkIsSameThread();
        while (!this._ref.compareAndSet(null, Thread.currentThread())) {
            Thread.onSpinWait();
        }
    }

    public void unlock() {
        final var curr = Thread.currentThread();
        if (this.getOwner() != curr) throw new IllegalMonitorStateException("Unlock failed, not held by thread: '" + curr.getName() + '\'');
        this._ref.compareAndSet(curr, null);
    }

    public void lockInterruptibly() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        this.checkIsSameThread();
        while (!this._ref.compareAndSet(null, Thread.currentThread())) {
            if (Thread.interrupted()) throw new InterruptedException();
            Thread.onSpinWait();
        }
    }

    public boolean tryLock() {
        this.checkIsSameThread();
        return this._ref.compareAndSet(null, Thread.currentThread());
    }

    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        this.checkIsSameThread();
        final long overtime = System.nanoTime() + unit.toNanos(time);
        while (true) {
            if (Thread.interrupted()) throw new InterruptedException();

            if (this._ref.compareAndSet(null, Thread.currentThread())) return true;
            if (System.nanoTime() >= overtime) return false;
            Thread.onSpinWait();
        }
    }

    @NotNull
    public Condition newCondition() {
        throw new UnsupportedOperationException("Conditions not supported.");
    }

    public Thread getOwner() {
        return this._ref.get();
    }

    public boolean isLocked() {
        return this._ref.get() != null;
    }
}
