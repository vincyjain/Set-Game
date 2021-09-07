package stacs.starcade.impl;

import java.awt.*;

/**
 * Interface for a Card in a Set Game.
 */
public interface ICard {

    /**
     * Returns the shape on the card.
     *
     * @return The shape on the card
     */
    String getShape();


    /**
     * Set the shape on the card.
     *
     * @param shape The new shape
     */
    void setShape(String shape);


    /**
     * Returns the colour of the shapes on the card.
     *
     * @return The shape colour
     */
    Color getColour();


    /**
     * Set the colour of the shapes on the card.
     *
     * @param colour The new colour of the shapes
     */
    void setColour(Color colour);


    /**
     * Returns the line style of the shapes on the card.
     *
     * @return The shape line style
     */
    String getLineStyle();


    /**
     * Set the line style of the shapes on the card.
     *
     * @param lineStyle The new shape
     */
    void setLineStyle(String lineStyle);


    /**
     * Returns the number of shapes on the card.
     *
     * @return The number of shapes
     */
    int getShapeCount();


    /**
     * Set the number of shapes on the card.
     *
     * @param shapeCount The new shape
     */
    void setShapeCount(int shapeCount);
}
