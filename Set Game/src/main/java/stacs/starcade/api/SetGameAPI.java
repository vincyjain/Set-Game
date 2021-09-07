package stacs.starcade.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import stacs.starcade.impl.*;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simplified controller for a REST API for Set Games.
 * Main simplification is that the record of scores are kept
 * in memory on a single server.
 */
@RestController
@RequestMapping(path = "api")
public class SetGameAPI {

    // In-memory representation of the game data
    public static final int CARDS_IN_SET = 3;
    private int gameId;
    private Map<Integer, IGameLogic> games = new HashMap<>();
    private static ArrayList<Player> scoreBoard = new ArrayList<>();
    private static boolean addDummyScores = true;


    /**
     * Returns the Set Game API description.
     *
     * @return Set Game API description
     */
    @GetMapping("")
    public String apiDescription() {
        return "A simplified controller for a REST API for Set Games";
    }


    /**
     * Starts a new Set Game on the server, and returns its associated game ID number,
     *
     * @return Set Game ID number
     */
    @PostMapping("/game")
    public Integer startGame(
    ) {
        IGameLogic model = new GameLogic();
        model.setTimer(new Timer());
        // TODO Add various other model components (ie model.setCards(new ), model.setTimer(new TimerImplementation()) etc.

        games.put(++gameId, model);

        // Start the timer
        model.getTimer().startTimer();

        System.out.println("Created new game with game ID: " + gameId);
        return gameId;
    }


    /**
     * Removes the game with game ID from the map of all games, thus terminating it.
     *
     * @param id Game ID
     */
    @DeleteMapping("/game/{id}/delete")
    public void deleteGame(
        @PathVariable int id
    ) {
        System.out.println("Deleting game");
        IGameLogic model = getModel(id);
        games.remove(id);
    }


    /**
     * Creates a set of dummy score that populate the scoreboard.
     */
    private void addDummyScoresToScoreBoard() {
        if (addDummyScores) {
            Instant start = Instant.parse("2017-10-03T10:15:00.00Z");
            Instant end1 = Instant.parse("2017-10-03T10:26:00.50Z");
            Instant end2 = Instant.parse("2017-10-03T10:45:35.20Z");
            Instant end3 = Instant.parse("2017-10-03T10:15:37.00Z");
            Instant end4 = Instant.parse("2017-10-03T10:55:31.10Z");

            Duration duration1 = Duration.between(start, end1);
            Duration duration2 = Duration.between(start, end2);
            Duration duration3 = Duration.between(start, end3);
            Duration duration4 = Duration.between(start, end4);
            scoreBoard.add(new Player("Bilbo Baggins", duration1));
            scoreBoard.add(new Player("Gandalf", duration2));
            scoreBoard.add(new Player("Glorfindel", duration3));
            scoreBoard.add(new Player("Sauron", duration4));
            addDummyScores = false;
        }
    }


    /**
     * Returns the set of 12 cards for the game with game ID that forms the basis of a Set Game.
     *
     * @param id Game ID
     * @return Array of 12 cards that forms the basis of a Set Game
     */
    @GetMapping("game/{id}/cards")
    public ArrayList<ICard> getCards(
            @PathVariable int id
    ) {
        IGameLogic model = getModel(id);
        ArrayList<ICard> cards = model.setOfDisplayCards();

        if (!cards.isEmpty()) {
            System.out.println("Returning cards");
            return cards;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cards found :'(");
    }


    /**
     * Parses the input to create three Cards and check that they are a valid set of three cards
     * for the give game.
     *
     * @param id Game ID
     * @param customQuery Parse input into three cards (shape, colour, line style and number of shapes)
     * @return True if a valid set of cards for the game
     */
    @GetMapping("game/{id}/cards/checkValid")
    public boolean checkCardsAreAValidSet(
            @PathVariable int id,
            @RequestParam Map<String, String> customQuery
    ) {
        System.out.println("checkCardsAreAValidSet request made!");
        IGameLogic model = getModel(id);

        // Recreating cards
        ArrayList<ICard> cards = new ArrayList<>();
        for (int i = 0; i < CARDS_IN_SET; i++) {
            checkCardAttributesNotNull(customQuery, i);
            String shape = customQuery.get("shape" + i);
            String lineStyle = customQuery.get("lineStyle" + i);
            int shapeCount = Integer.parseInt(customQuery.get("shapeCount" + i));
            int colour = Integer.parseInt(customQuery.get("colour" + i));
            cards.add(new Card(shape, new Color(colour), lineStyle, shapeCount));
        }

        checkQuantityOfCardsProvidedForSet(customQuery);

        // Printing cards
        System.out.println("Cards to be analysed");
        for (ICard card : cards) {
            System.out.println(card.toString());
        }
        boolean output = model.checkSelectedCombination(cards);
        System.out.println(output);
        return output;
    }


    /**
     * For the game with game ID, returns the number of sets found by the user.
     *
     * @param id Game ID
     * @return Number of sets of cards found
     */
    @GetMapping("game/{id}/cards/setsFound")
    public Integer getSetsFound(
            @PathVariable int id
    ) {
        System.out.println("Finding the total number of sets found!");
        IGameLogic model = getModel(id);
        return model.returnSetOfAllCorrectSelectionsOfPlayer().size();
    }


    /**
     * For the game with game ID, returns the number of possible sets for the game.
     *
     * @param id Game ID
     * @return Number of possible sets of cards for the given game
     */
    @GetMapping("game/{id}/cards/totalPossibleSets")
    public Integer getTotalPossibleSets(
            @PathVariable int id
    ) {
        System.out.println("Finding the total number of possible sets!");
        IGameLogic model = getModel(id);

        // Printing sets to the server console for easy checking of the GUI
        printSets(model.returnSetOfAllPossibleCorrectSelections());
        return model.returnSetOfAllPossibleCorrectSelections().size();
    }


    /**
     * For a given game ID, returns the current time for the associated game as a string in the
     * format MM:SS.
     *
     * @param id Set Game ID
     * @return Current time as a string
     */
    @GetMapping("game/{id}/getCurrentTime")
    public String getCurrentTime(
            @PathVariable int id
    ) {
        System.out.println("Find and return the current time!");
        IGameLogic model = getModel(id);
        ITimer timer = model.getTimer();
        Duration currentTime = timer.getCurrentTime();
        return currentTime.toMinutesPart() + ":" + currentTime.toSecondsPart();
    }


    /**
     * Starts or stops the timer for a game.
     *
     * @param id Set Game ID
     */
    @PostMapping("game/{id}/startStopTimer")
    public void startStopTimer(
            @PathVariable int id
    ) {
        System.out.println("Starting/stopping the timer!");
        IGameLogic model = getModel(id);
        ITimer timer = model.getTimer();
        if (timer.checkTimerRunning()) {
            timer.pauseTimer();
        }
        else {
            timer.startTimer();
        }
    }


    /**
     * Checks if the conditions are met that a user has won a game and returns true if they have.
     *
     * @param id Set Game ID number
     * @return True if the user has won the game
     */
    @GetMapping("game/{id}/hasWon")
    public boolean checkHasWon(
            @PathVariable int id
    ) {
        System.out.println("Checking the user has won!");
        IGameLogic model = getModel(id);

        if (model.hasWon()) {
            // Pausing the timer!
            model.getTimer().pauseTimer();
            return true;
        }
        return false;
    }


    @PostMapping("game/{id}/addPlayerToLeaderBoard")
    public void addPlayerToLeaderBoard(
            @PathVariable int id,
            @RequestParam(name = "playerName", defaultValue = "Anonymous") String playerName
    ) {
        System.out.println("Adding the player to the leaderboard!");
        IGameLogic model = getModel(id);
        Duration time = model.getTimer().getCurrentTime();
        Player player = new Player(playerName, time);
        System.out.println(player.getPlayerName());
        scoreBoard.add(player);
        scoreBoard = model.getLeaderBoardRanking(scoreBoard);
    }


    /**
     * Returns the latest version of the scoreboard, sorted by the best times.
     *
     * @return True if the user has won the game
     */
    @GetMapping("game/scoreBoard")
    public ArrayList<Player> getScoreBoard() {
        System.out.println("Retrieving the latest version of the scoreboard!");
        addDummyScoresToScoreBoard();
        return scoreBoard;
    }


    /**
     * For a list of possible sets of cards, prints each set of cards to the server console.
     *
     * @param setsOfCards List of possible sets of cards
     */
    private void printSets(ArrayList<ArrayList<ICard>> setsOfCards) {
        System.out.println("Possible sets: ");
        for (int i = 0; i < setsOfCards.size(); i++) {
            ArrayList<ICard> setOfCards = setsOfCards.get(i);

            System.out.println("Set " + i + ": ");
            for (ICard setOfCard : setOfCards) {
                System.out.println(setOfCard.toString());
            }
        }
    }


    /**
     * For a query from a checkCardsAreAValidSet request, throws an exception
     * if a card is missing an attribute.
     *
     * @param customQuery Map from a checkCardsAreAValidSet request
     * @param cardNumber Card number
     */
    private void checkCardAttributesNotNull(Map customQuery, int cardNumber) {
        if (!customQuery.containsKey("shape" + cardNumber)
                || !customQuery.containsKey("lineStyle" + cardNumber)
                || !customQuery.containsKey("shapeCount" + cardNumber)
                || !customQuery.containsKey("colour" + cardNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cards are missing attributes :'(");
        }
    }


    /**
     * For a query from a checkCardsAreAValidSet request, throws an exception
     * if there are too many cards in the request (more than three).
     *
     * @param customQuery Map from a checkCardsAreAValidSet request
     */
    private void checkQuantityOfCardsProvidedForSet(Map customQuery) {
        if (customQuery.containsKey("shape" + CARDS_IN_SET)
                || customQuery.containsKey("lineStyle" + CARDS_IN_SET)
                || customQuery.containsKey("shapeCount" + CARDS_IN_SET)
                || customQuery.containsKey("colour" + CARDS_IN_SET)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect number of cards sent as a set :'(");
        }
    }


    /**
     * For a game ID, returns the associated Set Game model.
     * Throws HTTP not found exception if no model is found.
     *
     * @param gameId Game ID of the Set Game
     * @return Set Game Model
     */
    private IGameLogic getModel(Integer gameId) {
        IGameLogic model = this.games.get(gameId);
        if (model != null) {
            return model;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Set Game with Game ID " + gameId + " was not found :'(");
    }
}
