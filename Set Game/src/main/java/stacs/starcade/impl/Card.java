package stacs.starcade.impl;

import java.awt.*;

/**
 * Card object.
 * Class for creating a card object in a a Set Game. Each card can have a different combination
 * of shape, colour, line style and number of shapes.
 */
public class Card implements ICard {

    public static final String[] POSSIBLE_SHAPES = {"triangle", "circle", "square"};
    public static final Color[] POSSIBLE_COLOURS = {Color.RED, Color.GREEN, Color.BLUE};
    public static final String[] POSSIBLE_LINE_STYLES = {"solid", "dashed", "dotted"};
    public static final int[] POSSIBLE_SHAPE_COUNTS = {1, 2, 3};

    private String shape;
    private Color colour;
    private String lineStyle;
    private int shapeCount;




    /**
     * Constructor.
     *
     * @param shape Shape type on the card
     * @param colour Colour of the shape(s) on the card
     * @param lineStyle Line style of the shape(s) on the card
     * @param shapeCount Number of shapes on the card
     */
    public Card(String shape, Color colour, String lineStyle, int shapeCount) {
        this.shape = shape;
        this.colour = colour;
        this.lineStyle = lineStyle;
        this.shapeCount = shapeCount;
    }

    /**
     * Default Constructor.
     */
    public Card() {}


    /**
     * {@inheritDoc}
     */
    public String getShape() {
        return this.shape;
    }


    /**
     * {@inheritDoc}
     */
    public void setShape(String shape) {
        if (checkShapeValid(shape)) {
            this.shape = shape;
        }
        else {
            throw new IllegalArgumentException(shape + " is not a valid shape!");
        }
    }


    /**
     * {@inheritDoc}
     */
    public Color getColour() {
        return this.colour;
    }


    /**
     * {@inheritDoc}
     */
    public void setColour(Color colour) {
        if (checkColourValid(colour)) {
            this.colour = colour;
        }
        else {
            throw new IllegalArgumentException(colour + " is not a valid colour!");
        }
    }


    /**
     * {@inheritDoc}
     */
    public String getLineStyle() {
        return this.lineStyle;
    }


    /**
     * {@inheritDoc}
     */
    public void setLineStyle(String lineStyle) {
        if (checkLineStyleValid(lineStyle)) {
            this.lineStyle = lineStyle;
        }
        else {
            throw new IllegalArgumentException(lineStyle + " is not a valid line style!");
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getShapeCount() {
        return this.shapeCount;
    }


    /**
     * {@inheritDoc}
     */
    public void setShapeCount(int shapeCount) {
        if (checkShapeCountValid(shapeCount)) {
            this.shapeCount = shapeCount;
        }
        else {
            throw new IllegalArgumentException(shapeCount + " is not a valid number of shapes!");
        }
    }


    /**
     * For a given shape, returns true if the shape is a valid shape type.
     *
     * @param shape A shape
     * @return True if the shape is a valid shape
     */
    private boolean checkShapeValid(String shape) {
        for (String possibleShape : POSSIBLE_SHAPES) {
            if (possibleShape.equals(shape)) {
                return true;
            }
        }
        return false;
    }


    /**
     * For a given colour, returns true if the colour is a valid.
     *
     * @param colour A colour
     * @return True if the colour is a valid colour
     */
    private boolean checkColourValid(Color colour) {
        for (Color possibleColour : POSSIBLE_COLOURS) {
            if (possibleColour.getRGB() == colour.getRGB()) {
                return true;
            }
        }
        return false;
    }


    /**
     * For a given line style, returns true if the line style is valid.
     *
     * @param lineStyle A shape line style
     * @return True if the line style is a valid line style
     */
    private boolean checkLineStyleValid(String lineStyle) {
        for (String possibleLineStyle : POSSIBLE_LINE_STYLES) {
            if (possibleLineStyle.equals(lineStyle)) {
                return true;
            }
        }
        return false;
    }


    /**
     * For a given shape count, returns true if the number of shapes is valid.
     *
     * @param shapeCount A number of shapes
     * @return True if the shape count is a valid number of shapes
     */
    private boolean checkShapeCountValid(int shapeCount) {
        for (int possibleShapeCount : POSSIBLE_SHAPE_COUNTS) {
            if (possibleShapeCount == shapeCount) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Card{" +
                "shape='" + shape + '\'' +
                ", colour=" + colour +
                ", lineStyle='" + lineStyle + '\'' +
                ", shapeCount=" + shapeCount +
                '}';
    }
}
