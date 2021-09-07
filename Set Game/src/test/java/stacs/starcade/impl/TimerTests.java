package stacs.starcade.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.SizeLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Timer class of the model.
 */
public class TimerTests {

    private ITimer timer;

    @BeforeAll
    static void printInitial() {
        System.out.println("Starting tests on the Timer class...");
    }

    @BeforeEach
    void setup() {
        this.timer = new Timer();
    }

    @Test
    void testConstructor() {
        assertNotNull(this.timer);
    }

    @Test
    void checkTimerRunningStatusInitiallyFalse() {
        assertFalse(this.timer.checkTimerRunning());
    }

    @Test
    void checkTimerRunningStatusTrueWhenTimerStarted() {
        this.timer.startTimer();
        assertTrue(this.timer.checkTimerRunning());
    }

    @Test
    void checkTimerRunningStatusFalseWhenTimerPaused() {
        this.timer.startTimer();
        this.timer.pauseTimer();
        assertFalse(this.timer.checkTimerRunning());
    }

    @Test
    void checkTimerRunningStatusFalseWhenTimerReset() {
        this.timer.startTimer();
        this.timer.resetTimer();
        assertFalse(this.timer.checkTimerRunning());
    }

    @Test
    void checkTimerCannotBeStartedIfItIsAlreadyRunning() {
        this.timer.startTimer();
        assertThrows(IllegalStateException.class, () -> this.timer.startTimer());
    }

    @Test
    void checkTimerCannotBePausedIfItIsNotRunning() {
        assertThrows(IllegalStateException.class, () -> this.timer.pauseTimer());
    }

}
