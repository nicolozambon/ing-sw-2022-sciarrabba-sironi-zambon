package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;

import java.util.List;

public interface GUIController {
    void setGUI(ViewGUI gui);
    void optionsHandling(List<String> options);
}