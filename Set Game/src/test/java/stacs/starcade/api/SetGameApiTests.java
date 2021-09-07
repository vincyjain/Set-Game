package stacs.starcade.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import stacs.starcade.impl.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Tests for the {@link SetGameAPI} class.
 */
@SpringBootTest
public class SetGameApiTests {

    private WebTestClient client;

    @BeforeEach
    void setup() {
        this.client = WebTestClient.bindToController(new SetGameAPI()).build();
    }

    @Test
    void getApiDescription() {
        this.client.get().uri("/api")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("A simplified controller for a REST API for Set Games");
    }

    @Test
    void createGame() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(1);
    }

    @Test
    void createGameReturnsIterativeGameId() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(1);

        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(2);

        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(3);
    }

    @Test
    void getDisplayCardsReturnsDisplayCardsForAGame() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get().uri("/api/game/1/cards")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ArrayList<Card>>() {});
    }

    @Test
    void getDisplayCardsForInvalidGameReturnsGameNotFound() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get().uri("/api/game/2/cards")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void mustReturnNotFoundForGameThatDoesNotExist() {
        this.client.get().uri("/api/game/2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void sendingValidSetOfThreeCardsToServerReturnsTrue() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card("circle", Color.GREEN, "dashed", 1));
        cards.add(new Card("circle", Color.BLUE, "dashed", 2));
        cards.add(new Card("circle", Color.RED, "dashed", 3));

        String uri = "/api/game/1/cards/checkValid?";
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

        this.client.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(boolean.class)
                .isEqualTo(true);
    }

    @Test
    void sendingInvalidSetOfThreeCardsToServerReturnsFalse() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card("circle", Color.GREEN, "dashed", 1));
        cards.add(new Card("circle", Color.BLUE, "dashed", 1));
        cards.add(new Card("circle", Color.RED, "dashed", 3));

        String uri = "/api/game/1/cards/checkValid?";
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

        this.client.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(boolean.class)
                .isEqualTo(false);
    }

    @Test
    void sendingTwoCardsInASetToServerThrowsException() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card("circle", Color.GREEN, "dashed", 1));
        cards.add(new Card("circle", Color.BLUE, "dashed", 2));

        String uri = "/api/game/1/cards/checkValid?";
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

        this.client.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void sendingFourCardsInASetToServerThrowsException() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card("circle", Color.GREEN, "dashed", 1));
        cards.add(new Card("circle", Color.BLUE, "dashed", 2));
        cards.add(new Card("square", Color.BLUE, "dashed", 2));
        cards.add(new Card("triangle", Color.BLUE, "dashed", 2));

        String uri = "/api/game/1/cards/checkValid?";
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

        this.client.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void requestForNumberOfSetsFoundReturnsAnIntegerAndIsInitiallyZero() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get()
                .uri("/api/game/1/cards/setsFound")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(0);
    }

    @Test
    void requestForNumberOfPossibleSetsReturnsAnInteger() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get()
                .uri("/api/game/1/cards/totalPossibleSets")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class);
    }

    @Test
    void requestCurrentTimeForGameReturnsString() throws InterruptedException {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        TimeUnit.SECONDS.sleep(2);

        this.client.get()
                .uri("/api/game/1/getCurrentTime")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void checkIfPlayerHasWonReturnsFalse() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get()
                .uri("/api/game/1/hasWon")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).isEqualTo(false);
    }

    @Test
    void addPlayerToTheLeaderBoardReturnsOkForNoPlayerName() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.post()
                .uri("/api/game/1/addPlayerToLeaderBoard")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void addPlayerToTheLeaderBoardWithPlayerNameReturnsOk() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.post()
                .uri("/api/game/1/addPlayerToLeaderBoard?playerName=Frodo Baggins")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getTheLatestVersionOfTheScoreBoard() {
        this.client.get().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        this.client.get()
                .uri("/api/game/scoreBoard")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ArrayList<Player>>() {});
    }


    @Test
    void accessingDeletedGameThrowsCannotBeFound() {
        this.client.post().uri("/api/game")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(1);

        this.client.delete().uri("/api/game/1/delete")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        this.client.get().uri("/api/game/1/cards")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
