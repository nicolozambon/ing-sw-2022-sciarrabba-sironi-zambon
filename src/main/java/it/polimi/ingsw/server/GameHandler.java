package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;

import java.util.*;

public class GameHandler implements Runnable {

    private Model model;
    private ModelBuilder modelBuilder;
    Map<String, Connection> playersConnection;

    public GameHandler(Map<String, Connection> playersConnection) {

        this.playersConnection = new HashMap<>(playersConnection);

        this.modelBuilder = new ModelBuilder();

        this.model = modelBuilder.buildModel(this.playersConnection.keySet().stream().toList());
        System.out.println("GameHandler");
    }

    @Override
    public void run() {
        System.out.println("GameHandler");
        for (String name : playersConnection.keySet()) {

        }
    }
}
