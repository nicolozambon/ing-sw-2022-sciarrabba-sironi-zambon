package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;
import javafx.fxml.FXML;

import java.util.List;

public class ErrorController implements GUIController {

    private ViewGUI gui;

    @FXML
    private void okButtonOnClick() {
        gui.playPopEffect();
        gui.getErrorStage().close();
    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }

    //Nothing to do
    @Override
    public void optionsHandling(List<String> options) {

    }

    //Nothing to do
    @Override
    public void onWaitEvent(String name) {

    }

    //Nothing to do
    @Override
    public void updateModel(ThinModel model) {

    }

}
