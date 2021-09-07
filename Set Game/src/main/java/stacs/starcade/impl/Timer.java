package stacs.starcade.impl;

import java.time.Duration;
import java.time.Instant;

/**
 * Implementation of a {@link ITimer}. Adapter from the Minesweeper TimerImpl.java class
 */
public class Timer implements ITimer {

    private Instant startTime;
    private Duration timeElapsed;
    private boolean timerRunning;


    /**
     * Constructor.
     * Timer is set to zero and paused.
     */
    public Timer() {
        this.resetTimer();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resetTimer() {
        this.startTime = null;
        this.timeElapsed = Duration.ZERO;
        this.timerRunning = false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startTimer() {
        if (!this.checkTimerRunning()) {
            this.startTime = Instant.now();
            this.timerRunning = true;
        }
        else {
            throw new IllegalStateException("Cannot start the timer if it has already been started!");
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseTimer() {
        if (this.checkTimerRunning()) {
            this.timeElapsed = this.timeElapsed.plus(this.getPeriod());
            this.startTime = null;
            this.timerRunning = false;
        }
        else {
            throw new IllegalStateException("Cannot pause the timer if it has not been started!");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkTimerRunning() {
        return this.timerRunning;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Duration getCurrentTime() {
        return this.timeElapsed.plus(this.getPeriod());
    }


    /**
     * Get the time that has passed since the timer was last started.
     * If the timer is paused, will return {@link Duration#ZERO}.
     * @return Duration since timer last started
     */
    private Duration getPeriod() {
        if(this.checkTimerRunning()) {
            return Duration.between(this.startTime, Instant.now());
        } else {
            return Duration.ZERO;
        }
    }
}
