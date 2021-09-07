package stacs.starcade.impl;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DeckOfCards} class.
 */
public class DeckOfCardsTests {
    private DeckOfCards deckofCards;
    private ArrayList<ICard> initialDeck;
    private ICard card, card1, card2, card3, card4, card5;

    @BeforeEach
    public void setup() {
        deckofCards = new DeckOfCards();
        initialDeck = deckofCards.initializeDeck();
    }

    @Test
    public void TestNullDeck() {
        assertNotNull(initialDeck);
    }

    @Test
    public void TestDeckWithAllCards() {

        int deckSize = initialDeck.size();
        assertEquals(81, deckSize);
        card = initialDeck.get(10);
        assertEquals("triangle", card.getShape());
        card = initialDeck.get(80);
        assertEquals(3, card.getShapeCount());

        ArrayList<Color> color = new ArrayList<>();
        ArrayList<String> shape = new ArrayList<>();
        ArrayList<String> line_style = new ArrayList<>();
        ArrayList<Integer> shapeCount = new ArrayList<>();

        for(int i = 0; i < 81; i++) {
            card = initialDeck.get(i);
            color.add(card.getColour());
            line_style.add(card.getLineStyle());
            shape.add(card.getShape());
            shapeCount.add(card.getShapeCount());
        }
        assertEquals(27, Collections.frequency(color,Color.RED));
        assertEquals(27, Collections.frequency(shape,"circle"));
        assertEquals(27, Collections.frequency(line_style,"solid"));
        assertEquals(27, Collections.frequency(shapeCount, 1));
    }

    @Test
    public void TestRandomGeneratedDeck() {
        ArrayList<ICard> random = deckofCards.generateRandomCards();
        assertFalse(random.isEmpty());
        assertEquals(12, random.size());
    }

    @Test
    public void TestDuplicatesInRandomGeneratedCardSet() {
        card1 = new Card("circle", Color.RED, "solid", 2);
        card2 = new Card("square", Color.BLUE, "dotted", 3);
        card3 = new Card("square", Color.BLUE, "dotted", 3);
        card4 = new Card("circle", Color.GREEN, "dashed", 1);
        ArrayList<ICard> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        boolean b = deckofCards.hasDuplicateCards(cardList);
        assertTrue(b);
    }

    @Test
    public void TestAtLeastOneSetPresent() {
        card1 = new Card("circle", Color.RED, "solid", 3);
        card2 = new Card("square", Color.BLUE, "dotted", 3);
        card3 = new Card("square", Color.RED, "dotted", 3);
        card4 = new Card("circle", Color.BLUE, "dashed", 2);
        card5 = new Card("triangle", Color.RED, "solid", 3);
        ArrayList<ICard> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        boolean b = deckofCards.checkAtLeastOneSetPresent(cardList);
        assertFalse(b);

        card = new Card("circle", Color.GREEN, "dotted", 1);
        cardList.add(card);
        b = deckofCards.checkAtLeastOneSetPresent(cardList);
        assertTrue(b);
    }

}
