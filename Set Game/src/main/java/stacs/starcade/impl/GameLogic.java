package stacs.starcade.impl;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * GameLogic object.
 * Class for implementing the rules of the game and generating required details for player
 */
public class GameLogic implements IGameLogic{

    private DeckOfCards deckOfCards;
    private ArrayList<ICard> displayCards;
    private ArrayList<ArrayList<ICard>> listOfAllPossibleSets;
    private ArrayList<ArrayList<ICard>> listOfCorrectSelections;
    private ITimer timer;
    private int flag, similarSetFound;

    /**
     * Constructor.
     *
     */
    public GameLogic() {
          this.deckOfCards = new DeckOfCards();
          this.displayCards = new ArrayList<>();
          listOfCorrectSelections = new ArrayList<>();
    }

    public ArrayList<ICard> stopGame() {
        displayCards = null;
        return displayCards;
    }

    /**
     * For initiating the game.
     *
     */
    public void startGame() {
        displayCards = setOfDisplayCards();
    }

    public ArrayList<ArrayList<ICard>> returnSetOfAllPossibleCorrectSelections() {
        listOfAllPossibleSets = listOfAllPossibleSets(displayCards);
        return listOfAllPossibleSets;
    }

    public ArrayList<ArrayList<ICard>> returnSetOfAllCorrectSelectionsOfPlayer() {
       return listOfCorrectSelections;
    }

    /**
     * For a displaying the set of 12 cards on the screen randomly generated.
     *
     * @return True if the shape is a valid shape
     */
    public ArrayList<ICard> setOfDisplayCards() {
        deckOfCards.initializeDeck();
        displayCards = deckOfCards.generateRandomCards();
        return displayCards;
    }

    /**
     * For checking the validity of the set selected by the user.
     *
     * @param selectedSet list of card in the set
     * @return True if the list is an acceptable combination
     */
    public boolean checkSelectedCombination(ArrayList<ICard> selectedSet) {
        if(selectedSet == null) {
            throw new IllegalArgumentException();
        }
        boolean result = isASet(selectedSet);
        if (result) {
            boolean cardsFound = checkSetHasNotAlreadyBeenFound(selectedSet);
            if (!cardsFound) {
                // add card to list of cards
                System.out.println("Adding cards!");
                this.listOfCorrectSelections.add(selectedSet);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }


    /**
     * For the given set of cards, returns true if that set of cards has already been found by the user
     *
     * @param selectedSet Set of Cards
     * @return True if the cards have been found
     */
    private boolean checkSetHasNotAlreadyBeenFound(ArrayList<ICard> selectedSet) {
        boolean setFound = false;

        for (int i = 0; i < this.listOfCorrectSelections.size(); i++) { // Loop through all sets found

            boolean[] cardsFound = {false, false, false};

            for (int j = 0; j < this.listOfCorrectSelections.get(i).size(); j++) { // Loop through the set found

                for (int k = 0; k < selectedSet.size(); k++) {

                    if (selectedSet.get(k).toString().equals(this.listOfCorrectSelections.get(i).get(j).toString())) {
                        System.out.println("Cards found that matched:");
                        System.out.println(selectedSet.get(k).toString());
                        System.out.println(this.listOfCorrectSelections.get(i).get(j).toString());
                        cardsFound[k] = true;
                    }
                }
            }
            if (checkBooleanArrayAllTrue(cardsFound)) {
                System.out.println("All cards found");
                setFound = true;
            }
        }
        return setFound;
    }


    /**
     * Takes an array of booleans and returns true if they are all true.
     *
     * @param cardsFound True or false
     * @return True if all booleans are true
     */
    private boolean checkBooleanArrayAllTrue(boolean[] cardsFound) {
        for(boolean b : cardsFound) {
            if(!b) {
                return false;
            }
        }
        return true;

    }

    /**
     * Checks if the selected correct set has been selected before.
     *
     * @param selectedSet list of cards in the set
     * @return True if the list has been selected before
     */
    public boolean isSetAlreadySelected(ArrayList<ICard> selectedSet) {
        setOfCorrectSelections(selectedSet);
        if(flag == 1) {
            return false;
        }
        return true;
    }

    /**
     * Returns the list of all correct sets selected by the player till now.
     *
     * @param correctSelection list of selected cards forming a correct set
     * @return list of all correct selections of the player till now
     */
    public ArrayList<ArrayList<ICard>> setOfCorrectSelections(ArrayList<ICard> correctSelection) {
        flag = 0;


        ArrayList<ICard> checkSimilarCard = new ArrayList<>();

        for(int cards = 0; cards < listOfCorrectSelections.size(); cards++) {
            checkSimilarCard = listOfCorrectSelections.get(cards);
            similarSetFound = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (correctSelection.get(i).getShape() == checkSimilarCard.get(j).getShape()) {
                        if (correctSelection.get(i).getColour() == checkSimilarCard.get(j).getColour()) {
                            if (correctSelection.get(i).getLineStyle() == checkSimilarCard.get(j).getLineStyle()) {
                                if (correctSelection.get(i).getShapeCount() == checkSimilarCard.get(j).getShapeCount()) {
                                    similarSetFound++;
                                }
                            }
                        }
                    }
                }
            }
            if (similarSetFound == 3) {
                return listOfCorrectSelections;
            }
        }

        listOfCorrectSelections.add(correctSelection);
        flag = 1;

        return listOfCorrectSelections;
    }



    /**
     * Returns list of all possible solutions for the 12 cards displayed on the game screen.
     *
     * @param cardsOnDisplay list of 12 cards
     * @return list of list of possible sets
     */
    public ArrayList<ArrayList<ICard>> listOfAllPossibleSets(ArrayList<ICard> cardsOnDisplay) {

        if(cardsOnDisplay == null) {
            throw new IllegalArgumentException();
        }

        listOfAllPossibleSets = new ArrayList<>();
        int numberOfCards = cardsOnDisplay.size();

            for (int card1 = 0; card1 < numberOfCards - 2; card1++) {
                ICard firstCard = cardsOnDisplay.get(card1);
                for (int card2 = card1 + 1; card2 < numberOfCards - 1; card2++) {
                    ICard secondCard = cardsOnDisplay.get(card2);
                    for (int card3 = card2 + 1; card3 < numberOfCards; card3++) {
                        ICard thirdCard = cardsOnDisplay.get(card3);

                        ArrayList<ICard> setOf3cards = new ArrayList<>();
                        setOf3cards.add(firstCard);
                        setOf3cards.add(secondCard);
                        setOf3cards.add(thirdCard);

                        if (isASet(setOf3cards)) {
                            listOfAllPossibleSets.add(setOf3cards);
                        }
                    }
                }
            }
        return listOfAllPossibleSets;
    }

    /**
     * Checks the attributes as per game rules.
     *
     * @param s1 attribute of card1
     * @param s2 attribute of card2
     * @param s3 attribute of card3
     * @return True if attributes follow the rules
     */
    public boolean checkAttributes(String s1, String s2, String s3) {
        if(s1 == null || s2 == null || s3 == null) {
            throw new IllegalArgumentException();
        }
        if(!((s1 == s2 && s2 == s3) || (s1 != s2 && s2 != s3 && s3 != s1))) {     //checks if the attributes are either same for the 3 cards or different for all 3 cards
            return true;
        }
        return false;
    }


    /**
     * Checks the color attribute as per game rules.
     *
     * @param c1 color of card1
     * @param c2 color of card2
     * @param c3 color of card3
     * @return True if attribute follow the rules
     */
    private boolean checkColors(Color c1, Color c2, Color c3) {
        if(c1 == null || c2 == null || c3 == null) {
            throw new IllegalArgumentException();
        }
        if(!((c1.getRGB() == c2.getRGB() && c2.getRGB() == c3.getRGB()) || (c1.getRGB() != c2.getRGB() && c2.getRGB() != c3.getRGB() && c3.getRGB() != c1.getRGB()))) {
            return true;
        }
        return false;
    }

    /**
     * Checks the shapeCount attribute as per game rules.
     *
     * @param i1 shapeCount of card1
     * @param i2 shapeCount of card2
     * @param i3 shapeCount of card3
     * @return True if attribute follow the rules
     */
    private boolean checkShapeCount(int i1, int i2, int i3) {
        if(!((i1 == i2 && i2 == i3) || (i1 != i2 && i2 != i3 && i3 != i1))) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the 3 cards form a correct set.
     *
     * @param set
     * @return True if the set can be formed
     */
    public boolean isASet(ArrayList<ICard> set) {

        if(set == null) {
            throw new IllegalArgumentException();
        }

        String s1 = set.get(0).getShape();
        String s2 = set.get(1).getShape();
        String s3 = set.get(2).getShape();
        if(checkAttributes(s1, s2, s3)) {
            return false;
        }

        Color c1 = set.get(0).getColour();
        Color c2 = set.get(1).getColour();
        Color c3 = set.get(2).getColour();
        if(checkColors(c1, c2, c3)) {
            return false;
        }

        s1 = set.get(0).getLineStyle();
        s2 = set.get(1).getLineStyle();
        s3 = set.get(2).getLineStyle();
        if(checkAttributes(s1, s2, s3)) {
            return false;
        }

        int i1 = set.get(0).getShapeCount();
        int i2 = set.get(1).getShapeCount();
        int i3 = set.get(2).getShapeCount();
        if(checkShapeCount(i1, i2, i3)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if player wins the game.
     *
     * @return True if the player has won
     */
    public boolean hasWon() {
        if (!listOfCorrectSelections.isEmpty()) {
            if(listOfCorrectSelections.size() == listOfAllPossibleSets.size()) {
                return true;       //wins if the player has found all possible sets
            }
        }
        return false;
    }

    /**
     * Retrieves the leaderboard rankings.
     *
     * @param playersList list of players on server
     * @return list of players in sorted order in respect to time
     */
    public ArrayList<Player> getLeaderBoardRanking(ArrayList<Player> playersList) {
        Collections.sort(playersList);          //sorted order on leaderboard in terms of time for completion of the game for each player
        return playersList;
    }

    /**
     * Sets timer.
     *
     * @param timer timer
     */
    public void setTimer(ITimer timer) {
        this.timer = timer;
    }

    /**
     * Gets the timer.
     *
     * @return timer
     */
    public ITimer getTimer() {
        return this.timer;
    }

}
