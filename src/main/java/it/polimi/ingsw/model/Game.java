package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> playerOrder;



    private Cloud Cloud1;
    private Cloud Cloud2;
    private Cloud Cloud3;

    public Game() {
        StudentBag studentbag = new StudentBag();
    }

    /* void round() {

    } */

    void addPlayer(Player player) {
        this.playerOrder.add(player);
    }

}
