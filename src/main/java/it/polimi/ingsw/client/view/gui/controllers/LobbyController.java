package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

public class LobbyController implements GUIController {

    private ViewGUI gui;

    @FXML
    private Button exitButton;

    @FXML
    private TextFlow playersTextFlow;

    @FXML
    private void exitButtonClicked() {
        try {
            this.gui.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLobbyNames(List<String> names) {
        names.forEach(n -> playersTextFlow.getChildren().add(new Text(n)));
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }
}
