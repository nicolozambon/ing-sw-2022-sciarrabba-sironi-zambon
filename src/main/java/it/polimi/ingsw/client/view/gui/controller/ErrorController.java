package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import javafx.fxml.FXML;

import java.util.List;

public class ErrorController implements GUIController {

    private ViewGUI gui;

    @FXML
    private void okButtonOnClick() {
        gui.getErrorStage().close();
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    @Override
    public void optionsHandling(List<String> options) {

    }
}
