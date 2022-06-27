package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

/**
 * GUI Controller for the Winner Scene
 */
public class WinnerController implements GUIController {
    /**
     * Current GUI
     */
    private ViewGUI gui;

    /**
     * Text message to display the user, containing the winner of this match
     */
    @FXML
    private Text winnerText;

    /**
     * Exit the application
     */
    @FXML
    private void exitGame() {
        gui.playPopEffect();
        Platform.exit();
    }

    /**
     * Setter method for the ViewGUI gui
     * @param gui current ViewGUI
     */
    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    /**
     * Option Handling method for the Winner Scene: display the winner message
     * @param options available options to the controller
     */
    @Override
    public void optionsHandling(List<String> options) {
        if (options.contains(this.gui.getNickname())) options.set(options.indexOf(this.gui.getNickname()), "You");
        String winnerMessage = "Winners of this game: " + options.toString().substring(1, options.toString().length() - 1);
        winnerText.setText(winnerMessage);
    }

    /**
     * Not implemented in this class
     */
    @Override
    public void onWaitEvent(String name) {

    }

    /**
     * Not implemented in this class
     */
    @Override
    public void updateModel(ThinModel model) {

    }

}
