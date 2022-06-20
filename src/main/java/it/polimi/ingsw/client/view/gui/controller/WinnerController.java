package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class WinnerController implements GUIController {

    private ViewGUI gui;

    @FXML
    private Text winnerText;

    @FXML
    private void exitGame() {
        gui.playPopEffect();

    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {
        if (options.contains(this.gui.getNickname())) options.set(options.indexOf(this.gui.getNickname()), "You");
        String winnerMessage = "Winners of this game: " + options.toString().substring(1, options.toString().length() - 1);
        winnerText.setText(winnerMessage);
    }

    @Override
    public void onWaitEvent(String name) {

    }

    @Override
    public void updateModel(ThinModel model) {

    }

}
