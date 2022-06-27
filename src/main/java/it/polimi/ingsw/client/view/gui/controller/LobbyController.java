package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Controller for the Lobby Scene
 */
public class LobbyController implements GUIController {
    /**
     * Current GUI
     */
    private ViewGUI gui;

    /**
     * VBox containing the listing of the connected users
     */
    @FXML
    private VBox playersVBox;

    /**
     * Exit button, closes the application entirely
     */
    @FXML
    private void exitButtonClicked() {
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
     * Option Handling method for the Lobby Scene: display the connected players
     * @param options available options to the controller
     */
    @Override
    public void optionsHandling(List<String> options) {
        playersVBox.getChildren().clear();
        Text text;
        for (String option : options) {
            text = new Text(option);
            text.getStyleClass().add("lobbyText");
            playersVBox.getChildren().add(text);
        }

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
