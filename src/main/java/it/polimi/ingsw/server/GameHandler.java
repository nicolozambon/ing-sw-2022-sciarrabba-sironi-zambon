package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;

import java.io.*;
import java.util.*;

public class GameHandler implements Runnable {

    private Model model;
    Controller controller;
    Map<String, Connection> playersConnection;

    public GameHandler(Map<String, Connection> playersConnection) {

        this.playersConnection = new HashMap<>(playersConnection);
        this.model = new ModelBuilder().buildModel(this.playersConnection.keySet().stream().toList());
        this.controller = model.getController();
    }

    @Override
    public void run() {


    }

    protected List<String> getPlayersNicknames() {
        return this.playersConnection.keySet().stream().toList();
    }
}
