package stacs.starcade.view;

import javax.swing.*;

public class GameFrame extends JFrame{

    public GameFrame() {
        this.setSize(885, 650);
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * @param SizeX
     * @param SizeY
     * @param LocX
     * @param LocY
     * @param Visible
     * @param Title
     */
    public GameFrame(int SizeX, int SizeY, int LocX, int LocY, boolean Visible, String Title) {
        this.setSize(SizeX,SizeY);
        this.setLocation(LocX,LocY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(Visible);
        this.setTitle(Title);
    }

    /**
     *
     * @param Text
     */
    public void Title(String Text) {
        this.setTitle(Text);
    }

    /**
     *
     * @param X
     * @param Y
     */
    public void Size(int X, int Y) {
        this.setSize(X,Y);
    }

    /**
     *
     * @param X
     * @param Y
     */
    public void Location(int X, int Y) {
        this.setLocation(X, Y);
    }

    /**
     * Show the frame when called
     */
    public void Show() {
        this.setVisible(true);
    }

    /**
     * Hide the frame
     */
    public void Hide() {
        this.setVisible(false);
    }
}
