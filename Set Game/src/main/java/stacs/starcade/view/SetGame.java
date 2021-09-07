package stacs.starcade.view;

import stacs.starcade.controller.ISetGameController;
import stacs.starcade.controller.SetGameController;

/**
 * Main method used to start the GUI for the Set Game,
 */
public class SetGame {

    public static void main(String[] args) {
        ISetGameController controller = new SetGameController("http://localhost:8080");
        MainFrame frame = new MainFrame(controller);
        frame.Title("CS5031 P2 Set game");
        frame.Size(900, 650);
        frame.setResizable(false);
        frame.Show();
    }
}
