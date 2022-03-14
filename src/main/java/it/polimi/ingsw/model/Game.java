package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> playerOrder;

    public Game() {

    }

    void round() {

    }

    void addPlayer(Player player) {
        this.playerOrder.add(player);
    }
}
