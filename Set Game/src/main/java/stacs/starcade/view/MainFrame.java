package stacs.starcade.view;

import stacs.starcade.controller.ISetGameController;
import stacs.starcade.impl.ICard;
import stacs.starcade.impl.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MainFrame extends GameFrame implements PropertyChangeListener, ActionListener {

    private static final int NUMBER_OF_CARDS = 12;
    private final static int ONE_SECOND = 1000;

    private JButton leaderButton;
    private JButton solveButton;
    private JButton resetButton;
    private JPanel panel;
    ISetGameController controller;
    private JPanel buttonPanel;
    private ArrayList<CardPanel> listOfCardPanels = new ArrayList<>();
    private JLabel setCounterLabel;
    private String setCounter = "- out of - sets found!";
    private JLabel timerLabel;
    private Timer timer;
    private boolean checkTimer = false;



    public MainFrame(ISetGameController controller) {
        this.controller = controller;
         cards();
         setUpButtons();
         setupSetCounter();
    }

    private void setupTimer() {
        this.timerLabel = new JLabel();
        this.timerLabel.setText("0:0");
        this.buttonPanel.add(this.timerLabel);

    }

    private void setupSetCounter() {
        this.setCounter = "- out of - sets found!";
        this.setCounterLabel = new JLabel();
        this.setCounterLabel.setText(this.setCounter);
        this.buttonPanel.add(this.setCounterLabel);
    }

    /**
     * Method to setup all the buttons needed for the Game
     */
    private void setUpButtons() {
        leaderButton = new JButton("Leaderboard");
        solveButton = new JButton("Check Cards are a Set");
        resetButton = new JButton("New game");

        this.buttonPanel = new JPanel();
        this.buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        setupTimer();
        updateTimer();

        this.buttonPanel.add(leaderButton);
        this.buttonPanel.add(solveButton);
        this.buttonPanel.add(resetButton);

        this.getContentPane().add(this.buttonPanel, BorderLayout.SOUTH);


        // Adding above buttons as action listeners
        addActionListenerForButtons(this);

        // Adding the mainFrame as an observer of the controller
        controller.addObserver(this);
    }

    public void cards() {
        this.panel = new JPanel();
        this.panel.setLayout(new GridLayout(2,6));
        this.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            CardPanel cardPanel = new CardPanel();
            this.listOfCardPanels.add(cardPanel);
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.panel.add(cardPanel);
        }

        this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.getContentPane().add(panel, BorderLayout.CENTER);
    }

    /**
     * Adds an action listener (the mainFrame) to the main buttons.
     *
     * @param al The action listener
     */
    private void addActionListenerForButtons(ActionListener al) {
        this.leaderButton.addActionListener(al);
        this.solveButton.addActionListener(al);
        this.resetButton.addActionListener(al);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.leaderButton) {
            System.out.println("MainFrame:actionPerformed - leaderButton pressed!");
            scoreBoardPopUp();
        }
        else if (event.getSource() == this.solveButton) {
            ArrayList<ICard> selectedCards = new ArrayList<>();
            System.out.println("MainFrame:actionPerformed - solveButton pressed!");
            for (CardPanel listOfCardPanel : listOfCardPanels) {

                if (listOfCardPanel.getCardSelectedStatus() == true) {
                    selectedCards.add(listOfCardPanel.getCard());
                }
            }
            this.controller.checkCardsAreAValidSet(selectedCards);
            this.controller.resetCardSelectedStatus(this.listOfCardPanels);
        }
        else if (event.getSource() == this.resetButton) {
            try {
                this.controller.startGame();
                this.controller.setCards();
                this.controller.resetCardSelectedStatus(this.listOfCardPanels);
                this.controller.setNumberOfPossibleSets();
                this.controller.setNumberOfSetsFound();
                this.controller.setTime();
                this.checkTimer = true;
                this.timer.start();
            }
            catch (Exception e) {
                serverConnectionErrorPopUp();
            }

        }
    }

    private void serverConnectionErrorPopUp() {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,"No connection to the server :'(",
                "Error!", JOptionPane.INFORMATION_MESSAGE);

    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (event.getSource() == this.controller) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("propertyChange - running!");

                    for (int i = 0; i < getListOfCardPanels().size(); i++) {
                        getListOfCardPanels().get(i).setCard(controller.getCards().get(i));
                        getListOfCardPanels().get(i).repaint();
                    }
                    setCounterLabel.setText(controller.updateSetCounter()); // Update the number of sets of cards found
                    repaint();

                    if (controller.getHasWonStatus()) {
                        checkTimer = false;
                        winningPopUp(controller.getTime());
                    }
                }
            });
        }
    }


    /**
     * Window that pops up when the all of the sets have been found. Requests that the user provides
     * a name to add their time to the scoreboard.
     *
     * @param winningTime The time taken to win the set game
     */
    public void winningPopUp(String winningTime) {
        String playerName = JOptionPane.showInputDialog(new JFrame(),
                "Congratulations on finding all of the sets in " + winningTime + "!"
                        + "\n" + "Enter your name below to add your time to the scoreboard:");
        if (playerName != null) {
            this.controller.addPlayerToLeaderBoard(playerName);
        }
    }


    /**
     * Creates the scoreboard when the user clicks the ScoreBoard button.
     */
    public void scoreBoardPopUp() {
        JFrame frame = new JFrame();
        ArrayList<Player> scores = new ArrayList<>();

        // Connection is valid
        try {
            this.controller.setScoreBoard();
            scores = this.controller.getScoreBoard();
            // Preparing Leaderboard
            String scoreboard = "";
            if (!scores.isEmpty()) {
                scoreboard = getScoreBoardAsAString(scores);
            }
            JOptionPane.showMessageDialog(frame, scoreboard,"Scoreboard", JOptionPane.INFORMATION_MESSAGE);
        } // No connection, display cached results or show an error message if it has not been cached
        catch (Exception e) {
            scores = this.controller.getScoreBoard();
            if (scores.isEmpty()) {
                serverConnectionErrorPopUp();
            }
            else {
                String scoreboard = getScoreBoardAsAString(scores);
                JOptionPane.showMessageDialog(frame, scoreboard,"Scoreboard", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    /**
     * For the array list of Players representing the scoreboard, convert it into a string
     * that can be shown in the pop-up scoreboard box.
     *
     * @param scores The scoreboard
     * @return String representing the scoreboard
     */
    private String getScoreBoardAsAString(ArrayList<Player> scores) {
        String scoreboard = "";
        for (int i = 0; i < scores.size(); i++) {
            String time = this.controller.humanReadableFormat(scores.get(i).getWinningTime());
            String score = (i + 1) + ". " + scores.get(i).getPlayerName() + " - " + time;
            scoreboard = scoreboard + score + "\n";
        }
        return scoreboard;
    }


    /**
     * Returns the 12 CardPanels used to represent the cards in the Set Game.
     *
     * @return Array of Card Panels
     */
    public ArrayList<CardPanel> getListOfCardPanels() {
        return this.listOfCardPanels;
    }


    /**
     * Periodically requests the current time that is running on the server for the current game.
     */
    private void updateTimer() {
        this.timer = new Timer(ONE_SECOND, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (checkTimer) {
                    System.out.println("Timer is checking the time");
                    controller.setTime();
                    timerLabel.setText(controller.getTime());
                    timerLabel.repaint();
                }
            }
        });
    }
}
