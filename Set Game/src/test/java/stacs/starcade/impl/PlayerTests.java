package stacs.starcade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tests for the {@link Player} class.
 */
public class PlayerTests {
    private Player player1;
    private Player player2;
    private Player player3;
    private ArrayList<Player> playerList;

    @BeforeEach
    void setup() {
        Duration d1 = Duration.ofMinutes(25);
        Duration d2 = Duration.ofMinutes(30);
        Duration d3 = Duration.ofMinutes(22);
        player1 = new Player("Nick", d1);
        player2 = new Player("Jack", d2);
        player3 = new Player("Lauri", d3);
        playerList = new ArrayList<>();
    }

    @Test
    public void TestPlayerExists() {
        assertNotNull(player1);
        assertNotNull(player2);
        assertNotNull(player3);
    }

    @Test
    public void TestLeaderBoardRanking() {
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        Collections.sort(playerList);
        assertEquals(player3, playerList.get(0));
        assertEquals(player1, playerList.get(1));
        assertEquals(player2, playerList.get(2));
    }
}
