package org.boxutil.util.concurrent;

import com.fs.starfarer.api.Global;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReentrantSpinLock implements Lock {
    protected record State(Thread thread, int count) {}

    protected final AtomicReference<State> _ref = new AtomicReference<>(null);

    public void lock() {
        final Thread curr = Thread.currentThread();
        State state;
        while (true) {
            state = this._ref.get();
            if (state == null) {
                if (this._ref.compareAndSet(null, new State(curr, 1))) return;
            } else if (state.thread == curr) {
                if (this._ref.compareAndSet(state, new State(curr, state.count + 1))) return;
            } else Thread.onSpinWait();
        }
    }

    public void unlock() {
        final Thread curr = Thread.currentThread();
        State state;
        while (true) {
            state = this._ref.get();
            if (state == null) throw new IllegalMonitorStateException("Unlock failed, not held by any thread.");
            if (state.thread != curr) throw new IllegalMonitorStateException("Unlock failed, not held by thread: '" + curr.getName() + '\'');

            if (this._ref.compareAndSet(state, state.count > 1 ? new State(curr, state.count - 1) : null)) return;
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        final Thread curr = Thread.currentThread();
        State state;
        while (true) {
            if (Thread.interrupted()) throw new InterruptedException();

            state = this._ref.get();
            if (state == null) {
                if (this._ref.compareAndSet(null, new State(curr, 1))) return;
            } else if (state.thread == curr) {
                if (this._ref.compareAndSet(state, new State(curr, state.count + 1))) return;
            } else Thread.onSpinWait();
        }
    }

    public boolean tryLock() {
        final Thread curr = Thread.currentThread();
        final State state = this._ref.get();
        if (state == null) return this._ref.compareAndSet(null, new State(curr, 1));
        else if (state.thread == curr) return this._ref.compareAndSet(state, new State(curr, state.count + 1));
        return false;
    }

    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        final Thread curr = Thread.currentThread();
        State state = this._ref.get();
        if (state != null && state.thread == curr) return this._ref.compareAndSet(state, new State(curr, state.count + 1));

        final long overtime = System.nanoTime() + unit.toNanos(time);
        while (true) {
            if (Thread.interrupted()) throw new InterruptedException();

            state = this._ref.get();
            if (state == null) {
                if (this._ref.compareAndSet(null, new State(curr, 1))) return true;
            } else if (state.thread == curr) {
                if (this._ref.compareAndSet(state, new State(curr, state.count + 1))) return true;
            }
            if (System.nanoTime() >= overtime) return false;
            Thread.onSpinWait();
        }
    }

    @NotNull
    public Condition newCondition() {
        throw new UnsupportedOperationException("Conditions not supported.");
    }

    public Thread getOwner() {
        final State state = this._ref.get();
        return state == null ? null : state.thread;
    }

    public boolean isLocked() {
        return this._ref.get() != null;
    }

    /**
     * @return returns {@link Integer#MIN_VALUE} if not held by any thread.
     */
    public int getHeldCount() {
        final State state = this._ref.get();
        return state == null ? Integer.MIN_VALUE : state.count;
    }
}
