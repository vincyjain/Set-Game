package stacs.starcade.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
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
 * Implementation of the {@link ISetGameController} class.
 */
public class SetGameController implements ISetGameController {

    private PropertyChangeSupport notifier;
    private WebClient client;
    private Integer gameId = 0;
    private Integer numberOfSetsFound;
    private Integer numberOfPossibleSets;
    private String timePassed = "testTime";
    private Boolean hasWon = false;
    private ArrayList<Player> scoreBoard = new ArrayList<>();

    private ArrayList<Card> cards;


    /**
     * Constructor.
     * @param baseUrl Base url for the web client e.g. "http://localhost:8080"
     */
    public SetGameController(String baseUrl) {
        this.cards = new ArrayList<Card>();
        this.notifier = new PropertyChangeSupport(this);
        this.client = WebClient.create(baseUrl);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebClient getClient() {
        return this.client;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyChangeSupport getNotifier() {
        return this.notifier;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(PropertyChangeListener listener) {
        this.notifier.addPropertyChangeListener(listener);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyChangeListener[] getObservers() {
        return notifier.getPropertyChangeListeners();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getApiDescription() {
        String output = this.client.get()
                .uri("api/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return output;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        // Reset game status
        resetGameState();

        int oldGameId = 0;
        if (this.getGameId() != null) {
            oldGameId = this.getGameId();
        }
        Integer output = this.client.post()
                .uri("api/game")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
        this.gameId = output;
        int newGameId = this.getGameId();
        notifier.firePropertyChange("setCards", oldGameId, newGameId);
        System.out.println("VectorDrawingModel:startGame - new game ID obtained from the server");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getGameId() {
        return this.gameId;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void setCards() {
        System.out.println("controller game ID: " + this.gameId);
        ArrayList<Card> oldCards = this.cards;
        ArrayList<Card> cards = this.client.get()
                .uri("api/game/"+ this.gameId + "/cards")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Card>>() {})
                .block();
        this.cards = cards;
        notifier.firePropertyChange("setCards", oldCards, this.cards);
        System.out.println("VectorDrawingModel:setCards - cards obtained from the server");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Card> getCards() {
        return this.cards;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkCardsAreAValidSet(ArrayList<ICard> cards) {
        String uri = "/api/game/" + this.gameId + "/cards/checkValid?";
        for (int i = 0; i < cards.size(); i++) {
            uri = uri + "shape" + i + "=" + cards.get(i).getShape() + "&";
            uri = uri + "lineStyle" + i + "=" + cards.get(i).getLineStyle() + "&";
            uri = uri + "shapeCount" + i + "=" + cards.get(i).getShapeCount() + "&";
            uri = uri + "colour" + i + "=" + cards.get(i).getColour().getRGB() + "&";
        }

        // Remove excess "&"
        if (uri != null && uri.length() > 0 && uri.charAt(uri.length() - 1) == '&') {
            uri = uri.substring(0, uri.length() - 1);
        }

        Boolean result = this.client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();


        this.setNumberOfSetsFound();
        this.checkHasWon();

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resetCardSelectedStatus(ArrayList<CardPanel> listOfCardPanels) {

        for (CardPanel listOfCardPanel : listOfCardPanels) {
            if (listOfCardPanel.getCardSelectedStatus()) {
                listOfCardPanel.mouseClickedActions();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setNumberOfSetsFound() {
        Integer oldNumberOfSetsFound = this.numberOfSetsFound;
        Integer numberOfSetsFound = this.client.get()
                .uri("api/game/"+ this.gameId + "/cards/setsFound")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
        this.numberOfSetsFound = numberOfSetsFound;
        notifier.firePropertyChange("setNumberOfSetsFound", oldNumberOfSetsFound, this.numberOfSetsFound);
        System.out.println("VectorDrawingModel:setNumberOfSetsFound - number of sets found obtained from the server");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNumberOfSetsFound() {
        return this.numberOfSetsFound;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setNumberOfPossibleSets() {
        Integer oldNumberOfPossibleSets = this.numberOfPossibleSets;
        Integer numberOfPossibleSets = this.client.get()
                .uri("api/game/"+ this.gameId + "/cards/totalPossibleSets")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
        this.numberOfPossibleSets = numberOfPossibleSets;
        notifier.firePropertyChange("setNumberOfPossibleSets", oldNumberOfPossibleSets, this.numberOfPossibleSets);
        System.out.println("VectorDrawingModel:setNumberOfPossibleSets - number of possible sets obtained from the server");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNumberOfPossibleSets() {
        return this.numberOfPossibleSets;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String updateSetCounter() {
        this.setNumberOfPossibleSets();
        this.setNumberOfSetsFound();

        return this.getNumberOfSetsFound() + " out of "
                + this.getNumberOfPossibleSets() + " sets found!";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTime() {
        if (this.timePassed.isEmpty()) {
            setTime();
        }

        return this.timePassed;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime() {
        String oldTime = this.timePassed;
        String output = this.client.get()
                .uri("api/game/ " + this.gameId + "/getCurrentTime")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        this.timePassed = output;
        notifier.firePropertyChange("setTime", oldTime, this.timePassed);
        System.out.println("VectorDrawingModel:setTime - current time obtained from the server");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startOrStopTimer() {
        this.client.get()
                .uri("api/game/ " + this.gameId + "/startStopTimer")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void checkHasWon() {
        Boolean output = this.client.get()
                .uri("api/game/" + this.gameId + "/hasWon")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (output) {
            this.hasWon = true;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getHasWonStatus() {
        return this.hasWon;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayerToLeaderBoard(String playerName) {
        String uri = "api/game/" + this.gameId + "/addPlayerToLeaderBoard?playerName=" + playerName;
        this.client.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        // TODO if successful do nothing???
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setScoreBoard() {
        String uri = "api/game/scoreBoard";
        ArrayList<Player> scoreBoard = this.client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Player>>() {})
                .block();

        this.scoreBoard = scoreBoard;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Player> getScoreBoard() {
        return this.scoreBoard;
    }

    /**
     * {@inheritDoc}
     * Adapted from lucasls solution (08/11/16) accessed 09/03/21 from:
     * https://stackoverflow.com/questions/3471397/how-can-i-pretty-print-a-duration-in-java
     */
    @Override
    public String humanReadableFormat(Duration duration) {
            return duration.toString()
                    .substring(2)
            .replaceAll("(\\d[HMS])(?!$)", "$1 ")
            .toLowerCase();
}

    /**
     * For a new game, reset all previous progress
     */
    private void resetGameState() {

        if (this.gameId != 0) {
            this.client.delete().uri("api/game/" + this.gameId + "/delete")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }

        this.numberOfSetsFound = 0;
        this.numberOfPossibleSets = 0;
        this.timePassed = "0:0";
        this.hasWon = false;
    }
}
