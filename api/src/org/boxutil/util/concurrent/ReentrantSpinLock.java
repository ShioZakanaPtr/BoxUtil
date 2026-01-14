package org.boxutil.util.concurrent;

import com.fs.starfarer.api.Global;

import java.util.concurrent.atomic.AtomicReference;

public class ReentrantSpinLock {
    protected record State(Thread thread, int count) {}

    protected final AtomicReference<State> _ref = new AtomicReference<>(null);

    public void lock() {
        final Thread curr = Thread.currentThread();
        while (true) {
            final State state = this._ref.get();
            if (state == null) {
                if (this._ref.compareAndSet(null, new State(curr, 1))) return;
            } else if (state.thread == curr) {
                if (this._ref.compareAndSet(state, new State(curr, state.count + 1))) return;
            } else Thread.onSpinWait();
        }
    }

    public void unlock() {
        final Thread curr = Thread.currentThread();
        while (true) {
            final State state = this._ref.get();
            if (state == null) throw new IllegalMonitorStateException("Unlock failed, not held by any thread.");
            if (state.thread != curr) throw new IllegalMonitorStateException("Unlock failed, not held by thread: '" + curr.getName() + '\'');

            if (this._ref.compareAndSet(state, state.count > 1 ? new State(curr, state.count - 1) : null)) return;
        }
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
