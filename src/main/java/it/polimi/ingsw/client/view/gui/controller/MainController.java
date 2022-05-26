package it.polimi.ingsw.client.view.gui.controller;

import it.polimi.ingsw.client.view.gui.ViewGUI;

import java.util.List;

public class MainController implements GUIController{

    private ViewGUI gui;


    @Override
    public void optionsHandling(List<String> options) {

    }

    @Override
    public void onWaitEvent(String name) {

    }

    @Override
    public void onWaitEvent() {

    }

    @Override
    public void setGUI(ViewGUI gui) {
        this.gui = gui;
    }
}
