package org.boxutil.backends.util;

import java.util.concurrent.atomic.AtomicInteger;

public class BUtil_SpinBarrier {
    private final int threadNum;
    private final AtomicInteger arrivedNum = new AtomicInteger(0);
    private final AtomicInteger round = new AtomicInteger(0);

    public BUtil_SpinBarrier(int threadNum) {
        this.threadNum = threadNum;
    }

    public void barrier() {
        final int currRound = this.round.get();

        if (this.arrivedNum.incrementAndGet() == this.threadNum) {
            this.arrivedNum.set(0);
            this.round.incrementAndGet();
            return;
        }

        while (this.round.get() == currRound) Thread.onSpinWait();
    }
}
