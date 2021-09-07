package stacs.starcade.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Card} class.
 */
public class CardTests {

    private ICard card;

    @BeforeAll
    static void printInitial() {
        System.out.println("Starting tests on the Card class...");
    }

    @BeforeEach
    void setup() {
        this.card = new Card("circle", Color.GREEN, "dashed", 2);
    }

    @Test
    void testConstructor() {
        assertNotNull(this.card);
    }

    @Test
    void testSetValidColour() throws NoSuchFieldException, IllegalAccessException {
        Color testVar = Color.RED;
        this.card.setColour(testVar);
        Field field = this.card.getClass().getDeclaredField("colour");
        field.setAccessible(true);
        assertEquals(field.get(this.card), testVar, "Fields didn't match");
    }

    @Test
    void testSetInvalidColourThrowsException() {
        Color testVar = Color.PINK;
        assertThrows(IllegalArgumentException.class, () -> this.card.setColour(testVar));
    }

    @Test
    void testGetColour() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.card.getClass().getDeclaredField("colour");
        field.setAccessible(true);

        Color testVar = Color.BLUE;
        field.set(this.card, testVar);

        Color result = this.card.getColour();
        assertEquals(testVar.getRGB(), result.getRGB(), "Field wasn't retrieved properly");
    }

    @Test
    void testSetValidShape() throws NoSuchFieldException, IllegalAccessException {
        String testVar = "triangle";
        this.card.setShape(testVar);
        Field field = this.card.getClass().getDeclaredField("shape");
        field.setAccessible(true);
        assertEquals(field.get(this.card), testVar, "Fields didn't match");
    }

    @Test
    void testSetInvalidShapeThrowsException() {
        String testVar = "pentagram";
        assertThrows(IllegalArgumentException.class, () -> this.card.setShape(testVar));
    }

    @Test
    void testGetShape() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.card.getClass().getDeclaredField("shape");
        field.setAccessible(true);

        String testVar = "square";
        field.set(this.card, testVar);

        String result = this.card.getShape();
        assertEquals(testVar, result, "Field wasn't retrieved properly");
    }

    @Test
    void testSetValidLineStyle() throws NoSuchFieldException, IllegalAccessException {
        String testVar = "solid";
        this.card.setLineStyle(testVar);
        Field field = this.card.getClass().getDeclaredField("lineStyle");
        field.setAccessible(true);
        assertEquals(field.get(this.card), testVar, "Fields didn't match");
    }

    @Test
    void testSetInvalidLineStyleThrowsException() {
        String testVar = "polka dot";
        assertThrows(IllegalArgumentException.class, () -> this.card.setLineStyle(testVar));
    }

    @Test
    void testGetLineStyle() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.card.getClass().getDeclaredField("lineStyle");
        field.setAccessible(true);

        String testVar = "dotted";
        field.set(this.card, testVar);

        String result = this.card.getLineStyle();
        assertEquals(testVar, result, "Field wasn't retrieved properly");
    }

    @Test
    void testSetValidShapeCount() throws NoSuchFieldException, IllegalAccessException {
        int testVar = 1;
        this.card.setShapeCount(testVar);
        Field field = this.card.getClass().getDeclaredField("shapeCount");
        field.setAccessible(true);
        assertEquals(field.get(this.card), testVar, "Fields didn't match");
    }

    @Test
    void testSetInvalidShapeCountThrowsException() {
        int testVar = 9000;
        assertThrows(IllegalArgumentException.class, () -> this.card.setShapeCount(testVar));
    }

    @Test
    void testGetShapeCount() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.card.getClass().getDeclaredField("shapeCount");
        field.setAccessible(true);

        int testVar = 2;
        field.set(this.card, testVar);

        int result = this.card.getShapeCount();
        assertEquals(testVar, result, "Field wasn't retrieved properly");
    }
}
