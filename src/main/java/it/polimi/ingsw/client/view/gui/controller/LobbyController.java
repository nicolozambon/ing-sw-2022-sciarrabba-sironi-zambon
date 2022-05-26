package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class LobbyController implements GUIController {

    private ViewGUI gui;

    @FXML
    private VBox playersVBox;

    @FXML
    private void exitButtonClicked() {
        System.out.println("exit");
        try {
            this.gui.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {
        playersVBox.getChildren().clear();
        options.forEach(n -> playersVBox.getChildren().add(new Text(n)));
    }

    @Override
    public void onWaitEvent(String name) {

    }

    @Override
    public void onWaitEvent() {

    }
}
