package org.boxutil.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinBarrier {
    protected final static int _ROUND_MASK = 0b11;
    protected final static int _ROUND_L_MOVE = 30;
    protected final static int _ARRIVED_MASK = 0x3fffffff;
    protected final int threadNum;
    protected final AtomicInteger state = new AtomicInteger(0); // ((roundBit & _ROUND_MASK) << _ROUND_L_MOVE) | arrivedNum & _ARRIVED_MASK

    /**
     * @param threadNum fixed thread number, means how many threads sync required, supports up to 1073741823 threads.
     */
    public SpinBarrier(int threadNum) {
        if (threadNum > 1073741823) throw new IllegalArgumentException("OVER 1_073_741_823 THREADS, ARE YOU ALIEN?");
        this.threadNum = threadNum;
    }

    public void barrier() {
        while (true) {
            final int currState = this.state.get(),
                    currRoundBit = currState >> _ROUND_L_MOVE,
                    currArrived = currState & _ARRIVED_MASK,
                    nextArrived = (currArrived + 1) & _ARRIVED_MASK;
            final boolean lastThread = nextArrived == this.threadNum;
            final int nextState = lastThread ?
                    (((currRoundBit + 1) & _ROUND_MASK) << _ROUND_L_MOVE) :
                    ((currRoundBit & _ROUND_MASK) << _ROUND_L_MOVE) | nextArrived;

            if (this.state.compareAndSet(currState, nextState)) {
                if (lastThread) return;

                while (currRoundBit == this.getRoundBit()) Thread.onSpinWait();
                return;
            }

            if (currRoundBit != this.getRoundBit()) return;
        }
    }

    public int getRoundBit() {
        return this.state.get() >> _ROUND_L_MOVE;
    }

    public int getArrived() {
        return this.state.get() & _ARRIVED_MASK;
    }
}
