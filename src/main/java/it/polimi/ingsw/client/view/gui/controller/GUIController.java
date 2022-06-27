package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.model.ThinModel;

import java.util.List;

/**
 * GUIController Interface
 */
public interface GUIController {
    /**
     * Setter method for the ViewGUI gui
     * @param gui current ViewGUI
     */
    void setGUI(ViewGUI gui);

    /**
     * Option Handling method for the Current Scene
     * @param options available options to the controller
     */
    void optionsHandling(List<String> options);

    /**
     * Actions to take upon a wait event
     */
    void onWaitEvent(String name);

    /**
     * Update this.model
     * @param model to set this.model to
     */
    void updateModel(ThinModel model);
}
