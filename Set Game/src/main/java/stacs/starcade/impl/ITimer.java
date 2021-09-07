package stacs.starcade.impl;

import java.time.Duration;

/**
 * Interface for a game clock/timer. Adapter from the Minesweeper Timer.java class
 */
public interface ITimer {

    /**
     * Reset timer to zero and leaves it in a paused state.
     */
    void resetTimer();

    /**
     * Start/restart timer.
     */
    void startTimer();

    /**
     * Pause the timer.
     */
    void pauseTimer();

    /**
     * Query the timer state.
     * @return true if the timer is currently running, false otherwise.
     */
    boolean checkTimerRunning();

    /**
     * Get the time on the timer.
     * @return a {@link Duration} value for the time on the timer.
     */
    Duration getCurrentTime();

}
