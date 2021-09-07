package stacs.starcade.view;

import stacs.starcade.impl.Card;
import stacs.starcade.impl.ICard;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * An extended JPanel for drawing the Cards used in a Set Game to the GUI. When clicked, on
 * the panel is highlighted and the Card selected status is set to true. When clicked again, the panel
 * returns to its original colour and the Card selected status is returned to false;
 */
public class CardPanel extends JPanel implements MouseInputListener {

    private static final Color NOT_SELECTED = Color.WHITE;
    private static final Color SELECTED = Color.YELLOW;
    private static final float LINE_WIDTH = 4.0f;
    private static final int SHAPE_POSITION = 95;
    private static final int SHAPE_WIDTH = 70;
    private static final int SHAPE_HEIGHT = 70;
    private static final int centerX = 40;
    private static final int centerY = 125;

    private ICard card;
    private Color backgroundColour;
    private boolean cardSelected;


    /**
     * Constructor.
     *
     */
    public CardPanel() {
        this.cardSelected = false;
        backgroundColour = NOT_SELECTED;
        setBackground(backgroundColour);
        addMouseListener(this);
    }


    /**
     * When notified of a mouse released event, the card background is filled in yellow and the card selected
     * status is set to true. If the panel is clicked again, the card is
     *
     * @param e mouse clicked event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.mouseClickedActions();
    }


    /**
     * Returns the selected status of the card.
     *
     * @return True if the card is selected
     */
    public boolean getCardSelectedStatus() {
        return this.cardSelected;
    }


    /**
     * Returns the Card associated with the panel.
     *
     * @return Card object
     */
    public ICard getCard() {
        return this.card;
    }


    /**
     * Sets the Card associated with the panel.
     *
     * @param card Card object
     */
    public void setCard(ICard card) {
        this.card = card;
    }


    /**
     * Paint the Card shapes to the panel.
     *
     * @param g Graphics object to be painted to the panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (this.card != null) {

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Setting line style
            this.setLineStyle(g2d);

            // Setting Line Colour
            this.setLineColour(g2d);

            // Drawing appropriate number of shapes
            for (int i = 0; i < this.getCard().getShapeCount(); i++) { // Iterating through all shapes
                int shapeLocation = i * SHAPE_POSITION;

                if (this.getCard().getShape().equals("circle")) {
                    Ellipse2D.Double ellipse = new Ellipse2D.Double(centerX, 10 + shapeLocation,
                            SHAPE_WIDTH, SHAPE_HEIGHT);
                    g2d.draw(ellipse);
                } else if (this.getCard().getShape().equals("square")) {
                    Rectangle2D.Double rectangle = new Rectangle2D.Double(centerX, 10 + shapeLocation,
                            SHAPE_WIDTH, SHAPE_HEIGHT);
                    g2d.draw(rectangle);
                } else if (this.card.getShape().equals("triangle")) {
                    Path2D triangle = getTriangle(shapeLocation);
                    g2d.draw(triangle);
                }
            }
        }
    }


    /**
     * Change the card selected status and visual indicator of the JPanel.
     */
    public void mouseClickedActions() {
        System.out.println("CardPanel:mouseClicked - Mouse has been clicked, changing Card selected status to "
                + !this.cardSelected + "...");
        this.cardSelected = !this.cardSelected;
        if (backgroundColour.getRGB() == SELECTED.getRGB()) {
            backgroundColour = NOT_SELECTED;
        }
        else {
            backgroundColour = SELECTED;
        }
        this.setBackground(backgroundColour);
        repaint();
    }


    /**
     * For a y-direction location on the CardPanel, returns an equilateral triangle constructed
     * using a Path2D that is ready to be drawn.
     * Adapted from Hovercraft Full Of Eels' solution (04/04/2015) from:
     * https://stackoverflow.com/questions/29447994/how-do-i-draw-a-triangle (accessed 08/03/21)
     *
     * @param shapeLocation Position of the triangle in the y-direction
     * @return Equilateral triangle
     */
    private Path2D getTriangle(int shapeLocation) {
        Path2D triangle = new Path2D.Double();
        double firstX = centerX + (SHAPE_WIDTH / 2.0);
        double firstY = 10 + shapeLocation;
        triangle.moveTo(firstX, firstY);
        triangle.lineTo(firstX + SHAPE_WIDTH / 2.0, firstY + SHAPE_HEIGHT);
        triangle.lineTo(firstX - SHAPE_WIDTH / 2.0, firstY + SHAPE_HEIGHT);
        triangle.closePath();
        return triangle;
    }


    /**
     * For a Graphic2D object, set its line style based on the Card associated with the CardPanel.
     *
     * @param g2d Graphic2D object for drawing the shapes for the card
     */
    private void setLineStyle(Graphics2D g2d) {
        if (this.getCard().getLineStyle().equals("solid")) {
            g2d.setStroke(new BasicStroke(LINE_WIDTH));
        }
        else if (this.getCard().getLineStyle().equals("dotted")) {
            float dot[] = { 2.0f };
            g2d.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f, dot, 0.0f));
        }
        else if (this.getCard().getLineStyle().equals("dashed")) {
            float dash[] = { 10.0f };
            g2d.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        }
        else {
            throw new IllegalArgumentException(this.getCard().getLineStyle() + " is not a valid line style!");
        }
    }


    /**
     * For a Graphic2D object, set its line colour based on the Card associated with the CardPanel.
     *
     * @param g2d Graphic2D object for drawing the shapes for the card
     */
    private void setLineColour(Graphics2D g2d) {
        boolean colourFound = false;
        for (int i = 0; i < Card.POSSIBLE_COLOURS.length; i++) {
            if (this.getCard().getColour().getRGB() == (Card.POSSIBLE_COLOURS[i].getRGB())) {
                g2d.setColor(this.getCard().getColour());
                colourFound = true;
                break;
            }
        }
        if (!colourFound) {
            throw new IllegalArgumentException(this.getCard().getColour() + " is not a valid colour!");
        }
    }


    /**
     * Returns the background colour of the JPanel.
     *
     * @return Background colour
     */
    private Color getBackGroundColour() {
        return backgroundColour;
    }


    /**
     * Unused.
     *
     * @param e mouse pressed event
     */
    @Override
    public void mousePressed(MouseEvent e) {}


    /**
     * Unused.
     *
     * @param e mouse released event
     */
    @Override
    public void mouseReleased(MouseEvent e) {}


    /**
     * Unused.
     *
     * @param e mouse entered event
     */
    @Override
    public void mouseEntered(MouseEvent e) {}


    /**
     * Unused.
     *
     * @param e mouse exited event
     */
    @Override
    public void mouseExited(MouseEvent e) {}


    /**
     * Unused.
     *
     * @param e mouse dragged event
     */
    @Override
    public void mouseDragged(MouseEvent e) {}


    /**
     * Unused.
     *
     * @param e mouse moved event
     */
    @Override
    public void mouseMoved(MouseEvent e) {}
}
