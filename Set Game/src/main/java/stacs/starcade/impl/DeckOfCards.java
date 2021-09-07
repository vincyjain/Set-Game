package stacs.starcade.impl;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * DeckOfCards object.
 * Class for handling the deck of cards.
 */
public class DeckOfCards {
    private String shape, lineStyle;
    private Color color;
    private int shapeCount;
    private ICard generateCard;
    private final ArrayList<ICard> deck;
    private ArrayList<ICard> randomCardsFromDeck;


    private static final String[] SHAPES = Card.POSSIBLE_SHAPES;
    private static final Color[] COLOURS = Card.POSSIBLE_COLOURS;
    private static final String[] LINE_STYLES = Card.POSSIBLE_LINE_STYLES;


    /**
     * Constructor.
     *
     */
    public DeckOfCards() {
          this.deck = new ArrayList<>();
    }

    /**
     * For initializing the deck of 81 cards.
     *
     * @return list of 81 cards.
     */
    public ArrayList<ICard> initializeDeck() {
        for (int attribute1 = 0; attribute1 < 3; attribute1++) {
            shape = SHAPES[attribute1];
            for (int attribute2 = 0; attribute2 < 3; attribute2++) {
                color = COLOURS[attribute2];
                for (int attribute3 = 0; attribute3 < 3; attribute3++) {
                       lineStyle = LINE_STYLES[attribute3];
                    for (int attribute4 = 1; attribute4 <= 3; attribute4++) {
                        shapeCount = attribute4;
                        generateCard = new Card(shape, color, lineStyle, shapeCount);
                        deck.add(generateCard);
                    }
                }
            }
        }
        return deck;
    }

    /**
     * Generates a random deck of 12 cards from the deck of 81 cards.
     * @return list of 12 cards
     */
    protected ArrayList<ICard> generateRandomCards() {
        this.randomCardsFromDeck = new ArrayList<>();

        Random randomSet = new Random();
        for(int set = 0; set < 12; set++) {
            ICard c = deck.get(randomSet.nextInt(deck.size()));
            randomCardsFromDeck.add(c);
        }
        if(hasDuplicateCards(randomCardsFromDeck)) {
            generateRandomCards();
        }
        else if(!checkAtLeastOneSetPresent(randomCardsFromDeck)) {
            generateRandomCards();
        }
        return randomCardsFromDeck;
    }

    /**
     * Checks if the random deck has duplicate cards.
     * @param deckOfRandomCards deck of 12 random cards
     * @return True if duplicate cards found
     */
    protected boolean hasDuplicateCards(ArrayList<ICard> deckOfRandomCards) {

        for(int card = 0; card < deckOfRandomCards.size(); card++) {
            for(int nextCard = card+1; nextCard < deckOfRandomCards.size(); nextCard++) {
                if(deckOfRandomCards.get(card).getShape() == deckOfRandomCards.get(nextCard).getShape()) {
                    if(deckOfRandomCards.get(card).getColour() == deckOfRandomCards.get(nextCard).getColour()) {
                        if(deckOfRandomCards.get(card).getLineStyle() == deckOfRandomCards.get(nextCard).getLineStyle()) {
                            if(deckOfRandomCards.get(card).getShapeCount() == deckOfRandomCards.get(nextCard).getShapeCount()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        /*
        HashSet<ICard> cardHS =  new HashSet<>();
        cardHS.addAll(deckOfRandomCards);
        if(cardHS.size() != deckOfRandomCards.size()) {
            return true;
        }
        */

        return false;
    }

    /**
     * Checks whether at least one set can be formed from given cards.
     * @param randomCardsList deck of 12 random cards
     * @return True if at lease one set can be formed
     */
    protected boolean checkAtLeastOneSetPresent(ArrayList<ICard> randomCardsList) {
        GameLogic gl = new GameLogic();
        int countOfPossibleSets = gl.listOfAllPossibleSets(randomCardsList).size();
        if(countOfPossibleSets > 0) {
            return true;
        }
        return false;
    }
}
