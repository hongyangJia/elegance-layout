package com.elegance.util;

import android.content.pm.ProviderInfo;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by hongyang on 17-8-9.
 */

public class IntervalCountDown extends Thread {

    private static final int SECONDS = 1000;
    private IntervalRunnable intervalRunnable;
    private IntervalExecute mRunnable;
    private boolean isRuning = false;
    private boolean suspend = false;

    private long programDelayMillis;
    private Interval mInterval;
    private int currentCount;
    private int delayMillis;
    private int count;

    public IntervalCountDown() {
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            synchronized (this) {
                if (suspend) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e(IntervalCountDown.class.getSimpleName(), "InterruptedException");
                        break;
                    }
                }
            }
            this.intervalRange();
        }
    }

    public void intervalRange(int delayMillis, int count, IntervalRunnable mIntervalCall) {
        this.intervalRunnable = mIntervalCall;
        initDataCountDown(Interval.INTERVAL, count, delayMillis);
        start();
    }

    public void intervalReuse(int delayMillis, int count, IntervalRunnable mIntervalCall) {
        this.intervalRunnable = mIntervalCall;
        this.initDataCountDown(Interval.REPEAT, count, delayMillis);
        running();
    }

    public void timeCountdown(int count, IntervalRunnable mIntervalCall) {
        this.intervalRunnable = mIntervalCall;
        initDataCountDown(Interval.COUNTDOWN, count, SECONDS);
        running();
    }

    public void postDelayed(int delayMillis, IntervalExecute mRunnable) {
        this.programDelayMillis = System.currentTimeMillis();
        this.delayMillis = delayMillis;
        this.mRunnable = mRunnable;
        this.mInterval = Interval.DELAYED;
        start();
    }

    private void initDataCountDown(Interval interval, int count, int delayMillis) {
        this.programDelayMillis = System.currentTimeMillis();
        this.mInterval = interval;
        this.delayMillis = delayMillis;
        this.count = count;
        this.currentCount = 0;
    }

    public void intervalRange() {
        try {
            programDelayMillis = System.currentTimeMillis() - programDelayMillis;
            sleep(delayMillis);
            programDelayMillis = System.currentTimeMillis();
            if (!suspend) {
                switch (mInterval) {
                    case INTERVAL:
                        schedulingEvents();
                        if (currentCount >= count) {
                            interrupt();
                            intervalRunnable.onComplete();
                        }
                        break;
                    case REPEAT:
                    case COUNTDOWN:
                        schedulingEvents();
                        onComplete();
                        break;
                    case DELAYED:
                        mRunnable.onComplete();
                        /*interrupt();*/
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isSuspend() {
        return this.suspend;
    }

    private void schedulingEvents() {
        if (currentCount < count) {
            intervalRunnable.onNext(mInterval == Interval.COUNTDOWN ? count - currentCount : currentCount);
        }
        currentCount++;
    }

    private void onComplete() {
        if (currentCount >= count) {
            restore();
            intervalRunnable.onComplete();
        }
    }

    private void running() {
        if (!isRuning) {
            start();
            isRuning = true;
        } else {
            rest();
        }
    }

    public void rest() {
        programDelayMillis = System.currentTimeMillis();
        synchronized (this) {
            notifyAll();
        }
        this.suspend = false;
    }

    public void restore() {
        programDelayMillis = System.currentTimeMillis();
        this.suspend = true;
    }

    public interface IntervalExecute {
        void onComplete();
    }

    public interface IntervalRunnable extends IntervalExecute {
        void onNext(int value);
    }

    private enum Interval {
        INTERVAL, DELAYED, REPEAT, COUNTDOWN;
    }

}
