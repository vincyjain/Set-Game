package stacs.starcade.impl;
import java.time.Duration;

/**
 * Player object.
 * Class for creating a player and generating the leaderboard ranking
 */
public class Player implements Comparable{

    private String playerName;
    private Duration winningTime;

    /**
     * Constructor
     * @param name Player name
     * @param time Time duration used by player for winning the game
     */
    public Player(String name, Duration time) {
        this.playerName = name;
        this.winningTime = time;
    }

    /**
     * Default constructor.
     */
    public Player() {}

    /**
     * Gets player name.
     * @return playerName
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets player name.
     * @param name playerName
     */
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    /**
     * Gets winning time.
     * @return time converted to minutes
     */
    public Duration getWinningTime() {

//        int timeInMinutes = this.winningTime.toMinutesPart();
        return this.winningTime;
    }

    /**
     * Sets winning time.
     * @param time time for winning the game.
     */
    public void setWinningTime(Duration time) {
        this.winningTime = time;
    }

    /**
     * Compares players based on time duration for winning.
     * @param comparePlayer compares the time duration for each player
     * @return
     */
    @Override
    public int compareTo(Object comparePlayer) {
        Duration compareWinningTime = ((Player)comparePlayer).getWinningTime();
        return this.winningTime.toMinutesPart() - compareWinningTime.toMinutesPart();
    }

    /**
     * Override method of Comparable interface
     * @return
     */
    @Override
    public String toString() {
        return null;
    }

}
