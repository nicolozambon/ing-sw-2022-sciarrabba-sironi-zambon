package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {

    //Maybe here is useful have 2 List of Player ordered in different way:
    // one for the clockwise order and other one for the round order
    private ArrayList<Player> playerOrder;

    private Cloud Cloud1;
    private Cloud Cloud2;
    private Cloud Cloud3;

    public Game() {
        StudentBag studentbag = new StudentBag();
    }

    public ArrayList<Player> getPlayerOrder() {
        return this.playerOrder;
    }

    void addPlayer(Player player) {
        this.playerOrder.add(player);
    }

}
