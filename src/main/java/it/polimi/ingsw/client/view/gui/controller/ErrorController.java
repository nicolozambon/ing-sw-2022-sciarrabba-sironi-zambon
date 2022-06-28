package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;
import javafx.fxml.FXML;

import java.util.List;

/**
 * GUI Controller for the Error Scene
 */
public class ErrorController implements GUIController {

    /**
     * Current GUI
     */
    private ViewGUI gui;

    /**
     * Ok button in the scene, specifies the behavior when clicked
     */
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
