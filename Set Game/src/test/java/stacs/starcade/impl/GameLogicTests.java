package stacs.starcade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GameLogic} class.
 */
public class GameLogicTests {
    ArrayList<ICard> setOfDisplayCards;
    GameLogic game;

    @BeforeEach
    public void setup() {
          game = new GameLogic();
    }

    /**
     * Tests that a set of 12 random cards has been retrieved successfully.
     */
    @Test
    public void TestSetOfDisplayCards() {
        setOfDisplayCards = game.setOfDisplayCards();
        assertEquals(12, setOfDisplayCards.size());
    }

    /**
     * Tests is the set selected has been selected by the player before.
     */
    @Test
    public void TestSetHasAlreadyBeenSelected() {
        ICard card1 = new Card("circle", Color.GREEN, "solid", 1);
        ICard card2 = new Card("circle", Color.RED, "dotted", 3);
        ICard card3 = new Card("circle", Color.BLUE, "dashed", 2);
        ArrayList<ICard> set = new ArrayList<>();
        set.add(card1);
        set.add(card2);
        set.add(card3);
        boolean result = game.checkSelectedCombination(set);
        assertTrue(result);
        ArrayList<ICard> newSet = new ArrayList<>();
        ICard card6 = new Card("circle", Color.GREEN, "solid", 1);
        ICard card7 = new Card("circle", Color.RED, "dotted", 3);
        ICard card8 = new Card("circle", Color.BLUE, "dashed", 2);
        newSet.add(card7);
        newSet.add(card6);
        newSet.add(card8);
        boolean newResult = game.checkSelectedCombination(newSet);   //as the set has been selected before, it will not be considered valid selection
        assertFalse(newResult);
        assertEquals(1, game.returnSetOfAllCorrectSelectionsOfPlayer().size()); //as the set is already selected before, it will not be re-added to the list of correct selections of the player

        card1 = new Card("square", Color.GREEN, "solid", 1);
        card2 = new Card("triangle", Color.RED, "dotted", 3);
        card3 = new Card("circle", Color.BLUE, "dashed", 2);
        ArrayList<ICard> setN = new ArrayList<>();
        setN.add(card1);
        setN.add(card2);
        setN.add(card3);
        boolean ResultN = game.checkSelectedCombination(setN);
        assertTrue(ResultN);
        assertEquals(2, game.returnSetOfAllCorrectSelectionsOfPlayer().size());
    }

    /**
     * Tests if the selected set is valid.
     */
    @Test
    public void TestIsSelectedSetCorrect() {
        ICard card1 = new Card("triangle", Color.GREEN, "solid", 1);
        ICard card2 = new Card("triangle", Color.RED, "dotted", 3);
        ICard card3 = new Card("triangle", Color.BLUE, "dashed", 1);
        ArrayList<ICard> set = new ArrayList<>();
        set.add(card1);
        set.add(card2);
        set.add(card3);
        boolean result = game.checkSelectedCombination(set);
        assertFalse(result);
    }

    /**
     * Tests if selected cards form a set.
     */
    @Test
    public void TestIsASet() {
        ICard card1 = new Card("square", Color.GREEN, "dotted", 0);
        ICard card2 = new Card("circle", Color.RED, "dashed", 0);
        ICard card3 = new Card("triangle", Color.BLUE, "solid", 0);
        ArrayList<ICard> set = new ArrayList<>();
        set.add(card1);
        set.add(card2);
        set.add(card3);
        boolean result = game.isASet(set);
        assertTrue(result);
    }

    /**
     * Test to check the list of all the possible sets for the 12 cards combination.
     */
    @Test
    public void TestAllPossibleSets() {
        ICard card1 = new Card("square", Color.GREEN, "dotted", 0);
        ICard card2 = new Card("circle", Color.RED, "dashed", 0);
        ICard card3 = new Card("triangle", Color.BLUE, "solid", 0);
        ICard card4 = new Card("square", Color.BLUE, "dotted", 1);
        ICard card5 = new Card("square", Color.RED, "dashed", 0);
        ICard card6 = new Card("square", Color.RED, "solid", 2);
        ICard card7 = new Card("square", Color.GREEN, "dotted", 2);
        ICard card8 = new Card("circle", Color.BLUE, "dashed", 2);
        ICard card9 = new Card("circle", Color.RED, "solid", 0);

        /* set1 is card1, card2, card3.
           set2 is card3, card4, card8.
           total 2 sets possible
        */
        ArrayList<ICard> set = new ArrayList<>();
        set.add(card1);
        set.add(card2);
        set.add(card3);
        set.add(card4);
        set.add(card5);
        set.add(card6);
        set.add(card7);
        set.add(card8);
        set.add(card9);

        assertEquals(2, game.listOfAllPossibleSets(set).size());
    }

    /**
     * Tests the list of correctly selected sets by the player till now.
     */
    @Test
    public void TestSetOfCorrectSelections() {
        ICard card1 = new Card("triangle", Color.GREEN, "dotted", 1);
        ICard card2 = new Card("triangle", Color.RED, "dashed", 1);
        ICard card3 = new Card("triangle", Color.BLUE, "solid", 1);
        ArrayList<ICard> set = new ArrayList<>();
        set.add(card1);
        set.add(card2);
        set.add(card3);
        ArrayList<ArrayList<ICard>> list = game.setOfCorrectSelections(set);
        assertEquals(1, list.size());
        assertTrue(list.get(0).contains(card3));
    }

    /**
     * Tests that method is generating correct leaderboard ranking.
     */
    @Test
    public void TestLeaderBoardRanking() {
        Duration d1 = Duration.ofMinutes(30);
        Duration d2 = Duration.ofMinutes(22);
        Duration d3 = Duration.ofMinutes(25);
        Player player1 = new Player("Nick", d1);
        Player player2 = new Player("Jack", d2);
        Player player3 = new Player("Lauri", d3);
        Player player4 = new Player("Melvin", d3);
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        playerList = game.getLeaderBoardRanking(playerList);
        assertEquals(player2, playerList.get(0));
        assertEquals(player3, playerList.get(1));
        assertEquals(player4, playerList.get(2));
        assertEquals(player1, playerList.get(3));
    }


    @Test
    void testSetTimer() throws NoSuchFieldException, IllegalAccessException {
        ITimer testVar = new Timer();
        this.game.setTimer(testVar);
        Field field = this.game.getClass().getDeclaredField("timer");
        field.setAccessible(true);
        assertEquals(field.get(this.game), testVar, "Fields didn't match");
    }

    @Test
    void testGetTimer() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.game.getClass().getDeclaredField("timer");
        field.setAccessible(true);

        ITimer testVar = new Timer();
        field.set(this.game, testVar);

        ITimer result = this.game.getTimer();
        assertEquals(testVar, result, "Field wasn't retrieved properly");
    }

    @Test
    void testSendingInvalidSetOfThreeReturnsFalse() {
        ArrayList<ICard> cards = new ArrayList<ICard>();
        cards.add(new Card("circle", Color.GREEN, "dashed", 1));
        cards.add(new Card("circle", Color.BLUE, "dashed", 1));
        cards.add(new Card("circle", Color.RED, "dashed", 3));

        assertFalse(game.isASet(cards));
    }
}
