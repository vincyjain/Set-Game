package stacs.starcade.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stacs.starcade.api.SetGameAPI;
import stacs.starcade.impl.Card;
import stacs.starcade.impl.ICard;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for the {@link CardPanel} class.
 */
public class CardPanelTests {

    private CardPanel cardPanel;
    private ICard card;

    @BeforeAll
    static void printInitial() {
        System.out.println("Starting tests on the Card class...");
    }

    @BeforeEach
    void setup() {
//        this.cardPanel = new CardPanel(mock(Card.class));
        this.cardPanel = new CardPanel();
    }

    @Test
    void testConstructor() {
        assertNotNull(this.cardPanel);
    }

    @Test
    void testGetCard() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.cardPanel.getClass().getDeclaredField("card");
        field.setAccessible(true);

        ICard testVar = new Card("circle", Color.BLUE, "dotted", 2);
        field.set(this.cardPanel, testVar);

        ICard result = this.cardPanel.getCard();
        assertEquals(testVar, result, "Field wasn't retrieved properly");
    }

    @Test
    void testGetCardSelectedStatus() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.cardPanel.getClass().getDeclaredField("cardSelected");
        field.setAccessible(true);

        boolean testVar = true;
        field.set(this.cardPanel, testVar);

        boolean result = this.cardPanel.getCardSelectedStatus();
        assertEquals(testVar, result, "Field wasn't retrieved properly");

        testVar = false;
        field.set(this.cardPanel, testVar);

        result = this.cardPanel.getCardSelectedStatus();
        assertEquals(testVar, result, "Field wasn't retrieved properly");

    }

    @Test
    void mouseClickedActionsChangesCardSelectedStatusToTrueAndToFalse()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertFalse(this.cardPanel.getCardSelectedStatus());

        Class[] cArg = new Class[0];

        Method method = this.cardPanel.getClass().getDeclaredMethod("mouseClickedActions", cArg);
        method.setAccessible(true);
        method.invoke(this.cardPanel);
        assertTrue(this.cardPanel.getCardSelectedStatus());

        method.invoke(this.cardPanel);
        assertFalse(this.cardPanel.getCardSelectedStatus());
    }
}
