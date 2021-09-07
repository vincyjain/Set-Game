package stacs.starcade.impl;

import java.util.ArrayList;

/**
 * Interface for the game logic behind the Set Game.
 */
public interface IGameLogic {

    /**
     * Starts the game and displays 12 cards set.
     *
     */
    void startGame();

    /**
     * Returns the 12 cards for playing the game.
     *
     * @return The list of the cards
     */
     ArrayList<ICard> setOfDisplayCards();

    /**
     * Checks if the combination selected by the user forms a set.
     *
     * @return True is the selected combination is a set
     */
     boolean checkSelectedCombination(ArrayList<ICard> selectedSet);

    /**
     * Returns the list of correct sets formed by the player.
     *
     * @return The list of sets
     */
    public ArrayList<ArrayList<ICard>> setOfCorrectSelections(ArrayList<ICard> correctSet);

    /**
     * Returns the list of all possible selections.
     *
     * @return The list of sets
     */
    public ArrayList<ArrayList<ICard>> listOfAllPossibleSets(ArrayList<ICard> cardsOnDisplay);

    /**
     * Returns if the user has won the game.
     *
     * @return True is player wins
     */
    boolean hasWon();


    /**
     * Set the timer for the set game model.
     *
     * @param timer
     */
    void setTimer(ITimer timer);


    /**
     * Get the timer for the set game model.
     *
     * @return timer for the set game
     */
    ITimer getTimer();

    /**
     * Gets the leaderboard of players.
     *
     * @return list of all players in sorted order with respect to time.
     */
    ArrayList<Player> getLeaderBoardRanking(ArrayList<Player> listOfPlayers);

    /**
     * Checks if the selected set by the player has been selected before.
     *
     * @return True is set has been selected before.
     */
    boolean isSetAlreadySelected(ArrayList<ICard> selectedSet);



    ArrayList<ArrayList<ICard>> returnSetOfAllPossibleCorrectSelections();

    ArrayList<ArrayList<ICard>> returnSetOfAllCorrectSelectionsOfPlayer();

}
