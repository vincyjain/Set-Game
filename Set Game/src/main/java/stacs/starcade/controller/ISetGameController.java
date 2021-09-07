package stacs.starcade.controller;

import org.springframework.web.reactive.function.client.WebClient;
import stacs.starcade.impl.Card;
import stacs.starcade.impl.ICard;
import stacs.starcade.impl.Player;
import stacs.starcade.view.CardPanel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Duration;
import java.util.ArrayList;

/**
 * The Set Game Controller class. A view can observe the controller and call its methods. These send the
 * required API requests to a server running a Set Game model. If appropriate, all observers will be notified
 * following the server's response.
 */
public interface ISetGameController {


    /**
     * Returns the WebClient object for the controller.
     *
     * @return WebClient for the controller
     */
    WebClient getClient();


    /**
     * Get the Notifier object for the Controller.
     *
     * @return Notifier for the controller
     */
    PropertyChangeSupport getNotifier();


    /**
     * Add a component that listens for changes that occur within the controller.
     *
     * @param listener Component of GUI listening for changes
     */
    void addObserver(PropertyChangeListener listener);


    /**
     * Return the array of all registered listeners of the model.
     *
     * @return Array of all listeners of the controller
     */
    PropertyChangeListener[] getObservers();


    /**
     * Return the Set Game API description that the controller is connected to.
     *
     * @return Description of the Set Game API
     */
    String getApiDescription();


    /**
     * Sends a request to the server to start a new Set Game and returns
     * the new game ID,
     */
    void startGame();


    /**
     * Returns the current Game ID number
     *
     * @return The Game ID
     */
    Integer getGameId();


    /**
     * Returns the list of 12, unique Card objects for the game from the server and
     * sets them on the client controller.
     */
    void setCards();


    /**
     * Returns the 12 cards for a Set Game that are stored on the client
     *
     * @return Set of 12 Cards
     */
    ArrayList<Card> getCards();


    /**
     * For a list of cards, return true if they are a valid set of cards.
     *
     * @param cards Array of cards to be checked
     * @return True if the cards are a valid set of cards
     */
    boolean checkCardsAreAValidSet(ArrayList<ICard> cards);



    /**
     * Resets the cardSelectedStatus for an array of CardPanels
     *
     * @param listOfCardPanels List of CardPanels
     */
    void resetCardSelectedStatus(ArrayList<CardPanel> listOfCardPanels);


    /**
     * Requests the number of sets of cards found by the user from the server and the result
     * is cached locally.
     */
    void setNumberOfSetsFound();


    /**
     * Returns the locally-stored number of sets of cards found.
     *
     * @return The number of sets found
     */
    Integer getNumberOfSetsFound();



    /**
     * Requests the number of possible sets of cards for the given set game from the server and the result
     * is cached locally.
     */
    void setNumberOfPossibleSets();


    /**
     * Returns the locally-stored number of possible sets for the given set game.
     *
     * @return The number of sets found
     */
    Integer getNumberOfPossibleSets();


    /**
     * Updates the number of sets found and the number of possible sets and returns it as a String to be used
     * in a set counter JLabel.
     *
     * @return A string containing the number of sets found compared ot the number of sets to find
     */
    String updateSetCounter();


    /**
     * Returns the current time in the format of MM:SS.
     *
     * @return Current time
     */
    String getTime();

    /**
     * Returns the current time from the server and stores it locally as a string.
     */
    void setTime();


    /**
     * Starts or stops the game timer on the server.
     */
    void startOrStopTimer();


    /**
     * Checks whether the user has won the current set game. If they have, stop the server side game timer.
     *
     */
    void checkHasWon();


    /**
     * Returns the winning status of the game.
     *
     * @return Winning status
     */
    boolean getHasWonStatus();


    /**
     * Adds the player with their current time to the leaderboard.
     *
     * @param playerName PLayer's name
     */
    void addPlayerToLeaderBoard(String playerName);


    /**
     * Obtains the latest version of the scoreboard from the server and saves it locally.
     */
    void setScoreBoard();


    /**
     * Returns the locally saved scoreboard, consisting of an array of Players.
     *
     * @return Array of Players
     */
    ArrayList<Player> getScoreBoard();


    /**
     * Converts a duration into a human-readable format.
     *
     * @param duration Duration
     * @return The duration as a string
     */
    String humanReadableFormat(Duration duration);
}
